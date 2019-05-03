package com.sample;

import org.kie.api.KieBase;
import org.kie.internal.builder.conf.RuleEngineOption;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.logger.KieRuntimeLogger;
import org.kie.api.KieBaseConfiguration; 





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
/*
    static SessionFactory session = HibernateUtil.getSession().getSessionFactory();  

    private static void openDbSession() {
	session.openSession();
    }

    private static void closeDbSession() {
	session.close();
    }
    */
    @SuppressWarnings({ "unchecked" })
    public static void main(String[] args) throws ClassNotFoundException, SQLException {

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
			prop.load(input);
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

    Class.forName("org.postgresql.Driver");
	String url = "jdbc:postgresql://localhost:5432/idiarules";
	Properties props = new Properties();
	props.setProperty("user","postgres");
	props.setProperty("password","e123779871435990");

	Connection conn = DriverManager.getConnection(url, props);

	System.out.println("Gathering data...");

	////////////////////////////////////////////////////////////////////////////
	// QUERIES...
	////////////////////////////////////////////////////////////////////////////

	Statement personSt = conn.createStatement();

	ResultSet personQuery = personSt.executeQuery("SELECT * FROM person");
	System.out.println("INFO: persons: " + personQuery);

	Statement csSt = conn.createStatement();

	ResultSet csQuery = csSt.executeQuery("SELECT concept_set_name,concept_id FROM ohdsi.concept_set cs INNER JOIN ohdsi.concept_set_item csi ON cs.concept_set_id = csi.concept_set_id");
	System.out.println("INFO: conceptTpls: " + csQuery); 

/*
	SQLQuery deraQuery = hibernateSession.createSQLQuery("SELECT * FROM drug_era WHERE DRUG_ERA_START_DATE <= TO_DATE('" + dateStr + "','yyyy-MM-dd') AND DRUG_ERA_END_DATE >= (TO_DATE('" + dateStr + "','yyyy-MM-dd'))");
	deraQuery.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	List deraResults = deraQuery.list();
	System.out.println("INFO: number of deras: " + deraResults.size());

	SQLQuery dexpQuery = hibernateSession.createSQLQuery("SELECT dexp.drug_exposure_id, dexp.person_id, dexp.drug_concept_id, to_char(dexp.drug_exposure_start_date, 'yyyy-MM-dd HH24:MI:SS') as drug_exposure_start_date, to_char(dexp.drug_exposure_end_date, 'yyyy-MM-dd HH24:MI:SS') as drug_exposure_end_date, dexp.drug_type_concept_id, dexp.stop_reason, dexp.refills, dexp.quantity, dexp.days_supply, dexp.sig, sm.expected, sm.min, sm.max, dexp.route_concept_id, dexp.lot_number, dexp.provider_id, dexp.visit_occurrence_id, dexp.drug_source_value, dexp.drug_source_concept_id, dexp.route_source_value, dexp.dose_unit_source_value, dstr.amount_value, dstr.amount_unit_concept_id, dstr.numerator_value, dstr.numerator_unit_concept_id, dstr.denominator_value, dstr.denominator_unit_concept_id, dstr.ingredient_concept_id, dexp.indication_concept_id "
		+ "FROM drug_exposure dexp, drug_strength dstr, sig_mapping sm "
		+ "WHERE dexp.drug_concept_id = dstr.drug_concept_id "
		+ "AND dexp.sig = sm.sig "
		+ "AND dexp.person_id IN "
		+ "(SELECT DISTINCT de.person_id FROM drug_era AS de WHERE drug_era_start_date <= TO_DATE('" + dateStr + "','yyyy-MM-dd') AND drug_era_end_date >= (TO_DATE('" + dateStr + "','yyyy-MM-dd')))");
	dexpQuery.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	List dexpResults = dexpQuery.list();
	System.out.println("INFO: number of dexps: " + dexpResults.size());
*/

	////////////////////////////////////////////////////////////////////////////
	// CREATE OBJECTS...
	////////////////////////////////////////////////////////////////////////////

	System.out.println("Done gathering data...");
	// int total_cnt = (csItemResults.size() + deraResults.size() + personResults.size() + ceras.size() + msnts.size() + dexpResults.size() + voccs.size());

	KieServices ks = KieServices.Factory.get();
    KieContainer kContainer = ks.getKieClasspathContainer();
    
	System.out.println("INFO: Rule engine session open!");
    
	KieBaseConfiguration kconfig = ks.newKieBaseConfiguration();
    
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
		   
	System.out.println("Asserting facts...");
	int cnt = 0;
	while (csQuery.next()){
	    System.out.println(csQuery.getString(1));
	    cnt++;
	}
	while (personQuery.next()){
		/*Map map = (Map) iter.next();
				Person p = new Person();
		Long pId = ((Number) map.get("person_id")).longValue();
		p.setPersonId(pId);
		p.setYearOfBirth( (Integer) map.get("year_of_birth"));
		kSession.insert(p);*/
		System.out.println(personQuery.getString(1));
		cnt++;
	} /*
	iter = deraResults.iterator();
	while (iter.hasNext()){
		Map map = (Map) iter.next();
				Long pId = ((Number) map.get("person_id")).longValue();
		Calendar start = Calendar.getInstance();
		start.setTime((java.util.Date) map.get("drug_era_start_date"));
		Calendar end = Calendar.getInstance();
		end.setTime((java.util.Date) map.get("drug_era_end_date"));
		Integer drugConceptId = (Integer) map.get("drug_concept_id");
		Integer drugExposureCount = (Integer) map.get("drug_exposure_count");
		DrugEra dera = new DrugEra(start, pId, end, drugConceptId, drugExposureCount);
		kSession.insert(dera);
		cnt++;
	}
	iter = dexpResults.iterator();
	DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	while (iter.hasNext()) {
		Map map = (Map) iter.next();
		System.out.println("DEXP QUERY: " + map.toString());
				ExtendedDrugExposure ex_dexp = new ExtendedDrugExposure();
	    ex_dexp.setDrugExposureId(Long.parseLong(map.get("drug_exposure_id").toString()));
	    ex_dexp.setPersonId(Long.parseLong(map.get("person_id").toString()));
	    ex_dexp.setDrugConceptId(Integer.parseInt(map.get("drug_concept_id").toString()));
	    try {
			LocalDateTime ldt = LocalDateTime.parse(map.get("drug_exposure_start_date").toString(),df);
			Calendar cal = Calendar.getInstance();
			cal.set(ldt.getYear(), ldt.getMonthValue()-1, ldt.getDayOfMonth(), ldt.getHour(), ldt.getMinute(), ldt.getSecond());
			ex_dexp.setDrugExposureStartDateCal(cal);
	    } 
	    catch (DateTimeParseException e) { e.printStackTrace(); }
	    if(map.get("drug_exposure_end_date").toString() != "null"){
	      try 
	      {
		  LocalDateTime ldt2 = LocalDateTime.parse(map.get("drug_exposure_end_date").toString(),df);
		  Calendar cal2 = Calendar.getInstance();
		  cal2.set(ldt2.getYear(), ldt2.getMonthValue()-1, ldt2.getDayOfMonth(), ldt2.getHour(), ldt2.getMinute(), ldt2.getSecond());
		  ex_dexp.setDrugExposureEndDateCal(cal2);
	      } 
	      catch (DateTimeParseException e) 
	      {
		  e.printStackTrace();
	      }}
	    else { continue; }

	    ex_dexp.setDrugTypeConceptId(Integer.parseInt(map.get("drug_type_concept_id").toString()));
	    if(map.get("stop_reason") != null){
	    	ex_dexp.setStopReason(map.get("stop_reason").toString());
		}
	    if(map.get("refills") != null){
			ex_dexp.setRefills(Short.parseShort(map.get("refills").toString()));
		}
	    if(map.get("quantity") != null){
	    	ex_dexp.setDrugQuantity(Integer.parseInt(map.get("quantity").toString()));
	    }	   
	    if(map.get("days_supply") != null){
	    	ex_dexp.setDaysSupply(Short.parseShort(map.get("days_supply").toString()));
	    }
	    ex_dexp.setSig(map.get("sig").toString());

	    
	    if(map.get("expected") != null){
			ex_dexp.setSigExpected(Integer.parseInt(map.get("expected").toString()));
		}
	    if(map.get("min") != null){
			ex_dexp.setSigMin(Integer.parseInt(map.get("min").toString()));
		}
	    if(map.get("max") != null){
			ex_dexp.setSigMax(Integer.parseInt(map.get("max").toString()));
		}
	    if(map.get("route_concept_id") != null){
			ex_dexp.setRouteConceptId(Integer.parseInt(map.get("route_concept_id").toString()));
		}
		if(map.get("lot_number") != null){
			ex_dexp.setLotNumber(map.get("lot_number").toString());
		}
 	    if(map.get("provider_id") != null){
			ex_dexp.setProviderId(Integer.parseInt(map.get("provider_id").toString()));
		}
	    if(map.get("visit_occurrence_id") != null){
			ex_dexp.setVisitOccurrenceId(Long.parseLong(map.get("visit_occurrence_id").toString()));
		}
		if(map.get("drug_source_value") != null){
			ex_dexp.setDrugSourceValue(map.get("drug_source_value").toString());
		}	    
	    if(map.get("drug_source_concept_id") != null){
			ex_dexp.setDrugSourceConceptId(Integer.parseInt(map.get("drug_source_concept_id").toString()));
		}
		if(map.get("route_source_value") != null) {
	    	ex_dexp.setRouteSourceValue(map.get("route_source_value").toString());
		}
		if(map.get("dose-unit_source_value") != null) {
		    ex_dexp.setDoseUnitSourceValue(map.get("dose_unit_source_value").toString());
		}	    
	    if(map.get("amount_value") != null){
	    	ex_dexp.setAmountValue(Double.parseDouble(map.get("amount_value").toString()));
	    }
	    if(map.get("amount_unit_concept_id") != null){
	    	ex_dexp.setAmountUnitConceptId(Integer.parseInt(map.get("amount_unit_concept_id").toString()));
	    }	    
	    if(map.get("numerator_value") != null){
	    	ex_dexp.setNumeratorValue(Double.parseDouble(map.get("numerator_value").toString()));
	    }	    if(map.get("numerator_unit_concept_id") != null){
	    	ex_dexp.setNumeratorUnitConceptId(Integer.parseInt(map.get("numerator_unit_concept_id").toString()));
	  	}	    
	    if(map.get("dose_unit_source_value") != null){
	    	ex_dexp.setDenominatorValue(Double.parseDouble(map.get("dose_unit_source_value").toString()));
	    }
	    if(map.get("denominator_unit_concept_id") != null){
	    	ex_dexp.setDenominatorUnitConceptId(Integer.parseInt(map.get("denominator_unit_concept_id").toString()));
	    }	      

        
	    if(map.get("amount_value") != null && map.get("expected") != null){
                ex_dexp.setSigDailyDosage(Double.parseDouble(map.get("amount_value").toString()), Double.parseDouble(map.get("expected").toString()));
	    }
	    else if(map.get("quantity") != null && map.get("days_supply") != null && map.get("amount_value") != null){
	      ex_dexp.setRegDailyDosage(Integer.parseInt(map.get("quantity").toString()), Short.parseShort(map.get("days_supply").toString()), Double.parseDouble(map.get("amount_value").toString()));
	  	}
            
	    else if(map.get("quantity") != null && map.get("days_supply") != null && map.get("numerator_value") != null){
	      ex_dexp.setComplexDailyDosage(Integer.parseInt(map.get("quantity").toString()), Short.parseShort(map.get("days_supply").toString()), Double.parseDouble(map.get("numerator_value").toString()));
	  	}
	    
	    else{
	      ex_dexp.setNullDailyDosage(0.00);
	  	}	      
	    ex_dexp.setIngredientConceptId(Integer.parseInt(map.get("ingredient_concept_id").toString()));
	    if(map.get("indication_concept_id") != null){
	      ex_dexp.setIndicationConceptId(Integer.parseInt(map.get("indication_concept_id").toString()));
	    }
	    kSession.insert(ex_dexp);

	}*/

	// System.out.println("Number of facts asserted: " + cnt);	System.out.println("Total number of facts that should've been asserted: " + total_cnt);

	System.out.println("Firing rules for assessment...");
	int nrules = -1;
	try {
	  nrules = kSession.fireAllRules();
	  
	} catch (Throwable t) {     
		System.out.println("Firing rules triggered an exception:");
		t.printStackTrace();
	}
    System.out.println("INFO: number of rules fired (-1 on error):" + nrules);

	kieLogger.close();
	kSession.dispose();
	kSession.destroy();

	personQuery.close();
	csQuery.close();
	personSt.close();
	csSt.close();	

	System.out.println("INFO: Rule engine session closed!"); 
            
    }       
}
