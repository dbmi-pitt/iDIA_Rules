package com.sample;

import org.kie.api.KieBase;
import org.kie.internal.builder.conf.RuleEngineOption;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.logger.KieRuntimeLogger;
import org.kie.api.KieBaseConfiguration; 
//import org.drools.runtime.rule.WorkingMemory;

// import com.sample.model.Risk;
// import com.sample.model.RiskScore;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;
import java.text.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.sample.model.ConceptSetItem;

import edu.pitt.dbmi.ohdsiv5.db.util.HibernateUtil;
import edu.pitt.dbmi.ohdsiv5.db.Concept;
import edu.pitt.dbmi.ohdsiv5.db.ConceptRelationship;
import edu.pitt.dbmi.ohdsiv5.db.ConditionOccurrence;
import edu.pitt.dbmi.ohdsiv5.db.ConditionEra;
import edu.pitt.dbmi.ohdsiv5.db.DrugExposure;
import edu.pitt.dbmi.ohdsiv5.db.DrugEra;
import edu.pitt.dbmi.ohdsiv5.db.DrugStrength;
import edu.pitt.dbmi.ohdsiv5.db.Measurement;
import edu.pitt.dbmi.ohdsiv5.db.Observation;
import edu.pitt.dbmi.ohdsiv5.db.ObservationPeriod;
import edu.pitt.dbmi.ohdsiv5.db.ProcedureOccurrence;
import edu.pitt.dbmi.ohdsiv5.db.Person;
import edu.pitt.dbmi.ohdsiv5.db.ExtendedDrugExposure;
import edu.pitt.dbmi.ohdsiv5.db.VisitOccurrence;


public class DroolsTest {
    static SessionFactory session = HibernateUtil.getSession().getSessionFactory();  

    private static void openDbSession() {
	session.openSession();
    }

    private static void closeDbSession() {
	session.close();
    }
    
    @SuppressWarnings({ "unchecked" })
    public static void main(String[] args) throws ClassNotFoundException {

	String dateStr = args[0];
	if(dateStr == null){
	    System.out.println("ERROR: Pass a date that will be used to extract data to run the rule engine in the format YYY-MM-DD");
	    System.exit(1);
	} else {
	    System.out.println("INFO: Running rule engine with data from date: " + dateStr);
	}

	String rule_folder = "";
	String schema = "";
	Properties prop = new Properties();
	InputStream input = null;
	try {
		input = new FileInputStream("config.properties");
		// load a properties file
		prop.load(input);
		// get the property value and print it out
		rule_folder = prop.getProperty("ruleFolder");
	} catch (IOException ex) {
		ex.printStackTrace();
	} finally {
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}    	
	
	System.out.println("Gathering data...");
	openDbSession();
	System.out.println("INFO: Hibernate session open!");

	Session hibernateSession = session.getCurrentSession();
	hibernateSession.beginTransaction();

	//***** simple debugging query *****
	SQLQuery personQuery = hibernateSession.createSQLQuery("SELECT * FROM person");
	personQuery.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	List personResults = personQuery.list();
	System.out.println("INFO: number of persons: " + personResults.size());

	//**** Pull data to be loaded into Drools working memory *****
	// pull data from a specific date
	// String dateStr = "2008-02-13"; // for the simulated population
	// String dateStr = "2016-02-08"; // warfarin NSAID for the banner population
	// String dateStr = "2016-02-16"; // Amiodarone - QT prolonging agents for banner population
	// String dateStr = "2016-02-15"; // Fluconazole - Opioids for banner population
	// String dateStr = "2016-02-06"; // Immunonosupressants - Azole antifungals for banner population
	// String dateStr = "2016-01-16"; // K - K-sparing diuretics for banner population 
	// String dateStr = "2016-03-29"; // Metoclopramide and an Antipsychotic or Cholinesterase inhibitor for banner population
	// String dateStr = "2016-01-17"; // Warfarin - SSRI/SNRIs for banner population
	
	// Get concept ids and names from the defined concept sets. There is currently no hibernate mapping for this.
	SQLQuery query = hibernateSession.createSQLQuery("SELECT concept_set_name,concept_id FROM ohdsi.concept_set cs INNER JOIN ohdsi.concept_set_item csi ON cs.concept_set_id = csi.concept_set_id");
	query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	List csItemResults =  query.list();
	System.out.println("INFO: number of conceptTpls: " + csItemResults.size()); // this is a list of Map objects  [{concept_id=40163566, concept_set_name=Anticoagulants}, {concept_id=40163560, concept_set_name=Anticoagulants}, ...}

	// The dataset records drug exposures as single day events and then creates drug era for exposures with <= 30 day gaps. So, we have to query first the drug eras (wich are coded as ingredients) and then the drug exposures (coded as clinical drugs) 
	List<DrugEra> deras = (List<DrugEra>) hibernateSession.createQuery("FROM DrugEra WHERE DRUG_ERA_START_DATE <= TO_DATE('" + dateStr + "','yyyy-MM-dd') AND DRUG_ERA_END_DATE >= (TO_DATE('" + dateStr + "','yyyy-MM-dd'))").list();
	System.out.println("INFO: number of deras: " + deras.size());

	// The query for drug exposures is limited to exposures for those persons with drug eras overlapping the target date
	// List<DrugExposure> dexps = (List<DrugExposure>) hibernateSession.createQuery("FROM DrugExposure dexp WHERE dexp.personId IN (SELECT DISTINCT de.personId FROM DrugEra AS de WHERE DRUG_ERA_START_DATE <= TO_DATE('" + dateStr + "','yyyy-MM-dd') AND DRUG_ERA_END_DATE >= (TO_DATE('" + dateStr + "','yyyy-MM-dd')))").list();
	// System.out.println("INFO: number of dexps for persons with drug eras during the date range: " + dexps.size());

	// The query for person data is limited to exposures for those persons with drug eras overlapping the target date
	List<Person> persons = (List<Person>) hibernateSession.createQuery("FROM Person AS p WHERE p.personId IN (SELECT DISTINCT de.personId FROM DrugEra AS de WHERE DRUG_ERA_START_DATE <= TO_DATE('" + dateStr + "','yyyy-MM-dd') AND DRUG_ERA_END_DATE >= (TO_DATE('" + dateStr + "','yyyy-MM-dd')))").list();
	System.out.println("INFO: number of persons with drug eras during the date range: " + persons.size());

	// The query for condition eras is limited to condition eras overlapping the target date and drug eras overlapping the target date
	List<ConditionEra> ceras = (List<ConditionEra>) hibernateSession.createQuery("FROM ConditionEra AS cera WHERE cera.personId IN (SELECT DISTINCT de.personId FROM DrugEra AS de WHERE DRUG_ERA_START_DATE <= TO_DATE('" + dateStr + "','yyyy-MM-dd') AND DRUG_ERA_END_DATE >= (TO_DATE('" + dateStr + "','yyyy-MM-dd'))) AND cera.conditionEraStartDate <= TO_DATE('" + dateStr + "','yyyy-MM-dd') AND cera.conditionEraEndDate >= TO_DATE('" + dateStr + "','yyyy-MM-dd')").list();
	System.out.println("INFO: number of ceras for persons with drug eras during the date range and with conditions that similarly overlap: " + ceras.size());

	// The query to get drug strength - overlaps with the drugs that are in the above queries?
	// List<DrugStrength> dstrs = (List<DrugStrength>) hibernateSession.createQuery("FROM DrugStrength AS dstr WHERE dstr.drugConceptId IN (SELECT DISTINCT dexp.drugConceptId FROM DrugExposure AS dexp WHERE dexp.personId IN (SELECT DISTINCT de.personId FROM DrugEra AS de WHERE DRUG_ERA_START_DATE <= TO_DATE('" + dateStr + "','yyyy-MM-dd') AND DRUG_ERA_END_DATE >= (TO_DATE('" + dateStr + "','yyyy-MM-dd'))))").list();
	// System.out.println("INFO: number of dstrs for persons with drug eras during the date range: " + dstrs.size());

	// The query to get drug strength - overlaps with the drugs that are in the above queries
	List<Measurement> msnts = (List<Measurement>) hibernateSession.createQuery("FROM Measurement as msnt WHERE msnt.measurementDate <= TO_DATE('" + dateStr + "','yyyy-MM-dd') and  msnt.personId IN (SELECT DISTINCT de.personId FROM DrugEra AS de WHERE DRUG_ERA_START_DATE <= TO_DATE('" + dateStr + "','yyyy-MM-dd') AND DRUG_ERA_END_DATE >= (TO_DATE('" + dateStr + "','yyyy-MM-dd')))").list();
	System.out.println("INFO: number of measurements for persons with drug eras during the date range where the measurement date is <= to the date passed in: " + msnts.size());

	// The query to get visit occurrences - also overlaps with the drugs in the above queries
	List<VisitOccurrence> voccs = (List<VisitOccurrence>) hibernateSession.createQuery("FROM VisitOccurrence as vocc WHERE vocc.personId IN (SELECT DISTINCT de.personId FROM DrugEra AS de WHERE DRUG_ERA_START_DATE <= TO_DATE('" + dateStr + "','yyyy-MM-dd') AND DRUG_ERA_END_DATE >= (TO_DATE('" + dateStr + "','yyyy-MM-dd'))) AND vocc.visitStartDate <= TO_DATE('" + dateStr + "','yyyy-MM-dd') AND vocc.visitEndDate >= TO_DATE('" + dateStr + "','yyyy-MM-dd')").list();
	System.out.println("INFO: number of visit occurrences for persons with drug eras during the date range: " + voccs.size());
	
	// Query to create the "extended drug exposure" that includes drug strength and ingredient
	List<Object> temps = (List<Object>) hibernateSession.createQuery("SELECT dexp.drugExposureId, dexp.personId, dexp.drugConceptId, to_char(dexp.drugExposureStartDate, 'yyyy-MM-dd HH24:MI:SS') as drugExposureStartDate, to_char(dexp.drugExposureEndDate, 'yyyy-MM-dd HH24:MI:SS') as drugExposureEndDate, dexp.drugTypeConceptId, dexp.stopReason, dexp.refills, dexp.drugQuantity, dexp.daysSupply, dexp.sig, sm.expected, sm.min, sm.max, dexp.routeConceptId, dexp.lotNumber, dexp.providerId, dexp.visitOccurrenceId, dexp.drugSourceValue, dexp.drugSourceConceptId, dexp.routeSourceValue, dexp.doseUnitSourceValue, dstr.amountValue, dstr.amountUnitConceptId, dstr.numeratorValue, dstr.numeratorUnitConceptId, dstr.denominatorValue, dstr.denominatorUnitConceptId, dstr.ingredientConceptId, dexp.indicationConceptId " +
  "FROM DrugExposure dexp, DrugStrength dstr, SigMapping sm " +
  "WHERE dexp.drugConceptId = dstr.drugConceptId " +
     "AND dexp.sig = sm.sig " +
     "AND dexp.personId IN " +
        "(SELECT DISTINCT de.personId FROM DrugEra AS de WHERE drugEraStartDate <= TO_DATE('" + dateStr + "','yyyy-MM-dd') AND drugEraEndDate >= (TO_DATE('" + dateStr + "','yyyy-MM-dd')))").list();
	System.out.println("INFO: number of temp drug exposures: " + temps.size());
	// System.out.println("INFO: temp drug exposures: " + temps.toString());
	
	DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	List<ExtendedDrugExposure> ex_dexps = new ArrayList<ExtendedDrugExposure>();	
	Iterator itr = temps.iterator();
	while(itr.hasNext())
	{
	    Object[] obj = (Object[]) itr.next();
	    ExtendedDrugExposure ex_dexp = new ExtendedDrugExposure();
	    ex_dexp.setDrugExposureId(Long.parseLong(String.valueOf(obj[0])));
	    ex_dexp.setPersonId(Long.parseLong(String.valueOf(obj[1])));
	    ex_dexp.setDrugConceptId(Integer.parseInt(String.valueOf(obj[2])));
	    try 
	    {
		LocalDateTime ldt = LocalDateTime.parse(String.valueOf(obj[3]),df);
		Calendar cal = Calendar.getInstance();
		cal.set(ldt.getYear(), ldt.getMonthValue()-1, ldt.getDayOfMonth(), ldt.getHour(), ldt.getMinute(), ldt.getSecond());
		ex_dexp.setDrugExposureStartDateCal(cal);
	    } 
	    catch (DateTimeParseException e) 
	    {
		e.printStackTrace();}
		System.out.println("END DATE" + String.valueOf(obj[4]));
	    if(String.valueOf(obj[4]) != "null"){
	      try 
	      {
		  LocalDateTime ldt2 = LocalDateTime.parse(String.valueOf(obj[4]),df);
		  Calendar cal2 = Calendar.getInstance();
		  cal2.set(ldt2.getYear(), ldt2.getMonthValue()-1, ldt2.getDayOfMonth(), ldt2.getHour(), ldt2.getMinute(), ldt2.getSecond());
		  ex_dexp.setDrugExposureEndDateCal(cal2);
	      } 
	      catch (DateTimeParseException e) 
	      {
		  e.printStackTrace();
	      }}
	    else { continue; }

	    ex_dexp.setDrugTypeConceptId(Integer.parseInt(String.valueOf(obj[5])));
	    ex_dexp.setStopReason(String.valueOf(obj[6]));
	    if(String.valueOf(obj[7]) != "null"){
		ex_dexp.setRefills(Short.parseShort(String.valueOf(obj[7])));}
	    if(String.valueOf(obj[8]) != "null"){
	      ex_dexp.setDrugQuantity(Integer.parseInt(String.valueOf(obj[8])));}	   
	    if(String.valueOf(obj[9]) != "null"){
	      ex_dexp.setDaysSupply(Short.parseShort(String.valueOf(obj[9])));}
	    ex_dexp.setSig(String.valueOf(obj[10]));

	    // adding setSigExpected, setSigMin, setSigMax
	    if(String.valueOf(obj[11]) != "null"){
		ex_dexp.setSigExpected(Integer.parseInt(String.valueOf(obj[11])));}
	    if(String.valueOf(obj[12]) != "null"){
		ex_dexp.setSigMin(Integer.parseInt(String.valueOf(obj[12])));}
	    if(String.valueOf(obj[13]) != "null"){
		ex_dexp.setSigMax(Integer.parseInt(String.valueOf(obj[13])));}

	    if(String.valueOf(obj[14]) != "null"){
		ex_dexp.setRouteConceptId(Integer.parseInt(String.valueOf(obj[14])));}

	    ex_dexp.setLotNumber(String.valueOf(obj[15]));

 	    if(String.valueOf(obj[16]) != "null"){
		ex_dexp.setProviderId(Integer.parseInt(String.valueOf(obj[16])));}
	    if(String.valueOf(obj[17]) != "null"){
		ex_dexp.setVisitOccurrenceId(Long.parseLong(String.valueOf(obj[17])));}
		
	    ex_dexp.setDrugSourceValue(String.valueOf(obj[18]));
	    
	    if(String.valueOf(obj[19]) != "null"){
		ex_dexp.setDrugSourceConceptId(Integer.parseInt(String.valueOf(obj[19])));}
		
	    ex_dexp.setRouteSourceValue(String.valueOf(obj[20]));
	    ex_dexp.setDoseUnitSourceValue(String.valueOf(obj[21]));
	    
	    if(String.valueOf(obj[22]) != "null"){
	      ex_dexp.setAmountValue(Double.parseDouble(String.valueOf(obj[22])));}
	    if(String.valueOf(obj[23]) != "null"){
	      ex_dexp.setAmountUnitConceptId(Integer.parseInt(String.valueOf(obj[23])));}	    
	    if(String.valueOf(obj[24]) != "null"){
	      ex_dexp.setNumeratorValue(Double.parseDouble(String.valueOf(obj[24])));}	
	    if(String.valueOf(obj[25]) != "null"){
	      ex_dexp.setNumeratorUnitConceptId(Integer.parseInt(String.valueOf(obj[25])));}	    
	    if(String.valueOf(obj[26]) != "null"){
	      ex_dexp.setDenominatorValue(Double.parseDouble(String.valueOf(obj[26])));}
	    if(String.valueOf(obj[27]) != "null"){
	      ex_dexp.setDenominatorUnitConceptId(Integer.parseInt(String.valueOf(obj[27])));}	      

        // Prioritize Sig field for daily dosage. use sigExpected and amountValue
	    if(String.valueOf(obj[11]) != "null" && String.valueOf(obj[22]) != "null"){
                ex_dexp.setSigDailyDosage(Double.parseDouble(String.valueOf(obj[11])), Double.parseDouble(String.valueOf(obj[22])));
	    }
	    else if(String.valueOf(obj[8]) != "null" && String.valueOf(obj[9]) != "null" && String.valueOf(obj[21]) != "null"){
	      ex_dexp.setRegDailyDosage(Integer.parseInt(String.valueOf(obj[8])), Short.parseShort(String.valueOf(obj[9])), Double.parseDouble(String.valueOf(obj[22])));
	  	}
            // currently uses quantity, days_supply, amount_value columns
	    else if(String.valueOf(obj[8]) != "null" && String.valueOf(obj[9]) != "null" && String.valueOf(obj[23]) != "null"){
	      ex_dexp.setComplexDailyDosage(Integer.parseInt(String.valueOf(obj[8])), Short.parseShort(String.valueOf(obj[9])), Double.parseDouble(String.valueOf(obj[24])));
	  	}
	    // currently uses quantity, days_supply, numerator_value columns
	    else{
	      ex_dexp.setNullDailyDosage(0.00);
	  	}	      
	    ex_dexp.setIngredientConceptId(Integer.parseInt(String.valueOf(obj[28])));
	    if(String.valueOf(obj[29]) != "null"){
	      ex_dexp.setIndicationConceptId(Integer.parseInt(String.valueOf(obj[29])));
	    }
	    ex_dexps.add(ex_dexp);

	}	
	System.out.println("INFO: number of ex_dexps for persons with drug eras during the date range: " + ex_dexps.size());	   
	
	System.out.println("Done gathering data...");
	int total_cnt = (csItemResults.size() + deras.size() + persons.size() + ceras.size() + msnts.size() + ex_dexps.size() + voccs.size());
	//-------------------------------------------------

	// Run the rule engine
	
	KieServices ks = KieServices.Factory.get();
    KieContainer kContainer = ks.getKieClasspathContainer();
    
	System.out.println("INFO: Rule engine session open!");	
	// KieSession kSession1 = kContainer.newKieSession(rule_folder); // This is the line that should be edited in the  configuration file to change what rules are fired!

    // Switching Between PHREAK and ReteOO for the rule engine
	KieBaseConfiguration kconfig = ks.newKieBaseConfiguration();
    // kconfig.setOption(RuleEngineOption.RETEOO);
    kconfig.setOption(RuleEngineOption.PHREAK);
    KieBase kbase = kContainer.newKieBase("rules_progress", kconfig);
    KieSession kSession = kbase.newKieSession();
	
	KieRuntimeLogger kieLogger = ks.getLoggers().newFileLogger(kSession, "audit");

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	try 
	{
	  kSession.setGlobal("currentDateStr", dateStr);
	  Calendar cal2 = Calendar.getInstance();
	  cal2.setTime(sdf.parse(dateStr));
	  kSession.setGlobal("currentDate", cal2);
	  Calendar cal3 = Calendar.getInstance();
	  cal3.setTime(sdf.parse(dateStr));
	  cal3.add(Calendar.DAY_OF_YEAR, -2);	  
	  kSession.setGlobal("within48hours", cal3);
	  Calendar cal4 = Calendar.getInstance();
	  cal4.setTime(sdf.parse(dateStr));
	  cal4.add(Calendar.DAY_OF_YEAR, -28);
	  kSession.setGlobal("within28days", cal4);
	} 
	catch (ParseException e) 
	{
	  e.printStackTrace();
	}
		   
	// Load all the facts
	System.out.println("Asserting facts...");
	int cnt = 0;
	Iterator iter = csItemResults.iterator();
	while (iter.hasNext()){
	    Map map = (Map) iter.next();
	    kSession.insert( new ConceptSetItem((String) map.get("concept_set_name"), (Integer) map.get("concept_id")));
	    cnt++;
	}
	// TEST PERSON SQL
	iter = personResults.iterator();
	while (iter.hasNext()){
		Map map = (Map) iter.next();
		System.out.println("PERSON SQL " + map);
		// example map object:
		// {gender_concept_id=8507, day_of_birth=1, ethnicity_source_concept_id=null, race_source_concept_id=null, care_site_id=null, gender_source_concept_id=null, race_source_value=null, location_id=321, year_of_birth=1955, death_datetime=null, person_source_value=null, gender_source_value=null, ethnicity_concept_id=38003564, race_concept_id=8527, month_of_birth=1, ethnicity_source_value=null, provider_id=null, birth_datetime=null, person_id=1564}
		Person p = new Person();
		Long pId = ((Number) map.get("person_id")).longValue();
		p.setPersonId(pId);
		p.setYearOfBirth( (Integer) map.get("year_of_birth"));
		kSession.insert(p);
		cnt++;
	}
	for (DrugEra dera : deras) {           	
	    kSession.insert((DrugEra) hibernateSession.get(DrugEra.class, dera.getDrugEraId()));            	
	    cnt++;
	}
	// for (Person p : persons) {           	
	//     kSession.insert((Person) hibernateSession.get(Person.class, p.getPersonId()));
	//     cnt++;
	// }	
	for (ConditionEra cera : ceras) {           	
	    kSession.insert((ConditionEra) hibernateSession.get(ConditionEra.class, cera.getConditionEraId()));            	
	    cnt++;
	}	
	for (Measurement msnt : msnts){
	    kSession.insert((Measurement) hibernateSession.get(Measurement.class, msnt.getMeasurementId()));
	    cnt++;
	}
	for (VisitOccurrence vocc : voccs){
	    kSession.insert((VisitOccurrence) hibernateSession.get(VisitOccurrence.class, vocc.getVisitOccurrenceId()));
	    cnt++;
	}
	
	//// Tried to sleep the thread in case the inserts above are not settled in working memory due
    //// to multi-threaded issues.
    // try {
    // 	Thread.sleep(2000);
    // } catch (Exception e) {
    //     System.out.println(e);
    // }

	for (ExtendedDrugExposure ex_dexp : ex_dexps){
	    kSession.insert((ExtendedDrugExposure) ex_dexp);
	    cnt++;
	}

	System.out.println("Number of facts asserted: " + cnt);	
	System.out.println("Total number of facts that should've been asserted: " + total_cnt);


	System.out.println("Firing rules for assessment...");
	// fire rules
	int nrules = -1;
	try {
	  nrules = kSession.fireAllRules();
	  // kSession.fireUntilHalt(); // Tried to switch to this approach to firing rules but it did not fix the issue
	} catch (Throwable t) {     
		System.out.println("Firing rules triggered an exception:");
		t.printStackTrace();
	}
    System.out.println("INFO: number of rules fired (-1 on error):" + nrules);

	closeDbSession();
	System.out.println("INFO: Hibernate session closed!");
	
	kieLogger.close();
	kSession.dispose();
	kSession.destroy();
	System.out.println("INFO: Rule engine session closed!"); 
            
    }       
}
