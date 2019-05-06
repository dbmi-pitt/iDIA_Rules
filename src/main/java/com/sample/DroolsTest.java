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

import com.sample.model.ConceptSetItem;

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

	Statement personSt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
	ResultSet personQuery = personSt.executeQuery(
			"SELECT " 
			+ "person_id"
			+ ",gender_concept_id"
			+ ",year_of_birth"
			+ ",race_concept_id"
			+ ",ethnicity_concept_id"
			+ " FROM person"
		);
	personQuery.last(); // cursor to last row to get # of results
	System.out.println("INFO: # of persons: " + personQuery.getRow());
	personQuery.beforeFirst(); // reset cursor so that iterators below will work. 

	Statement csSt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
	ResultSet csQuery = csSt.executeQuery(
			"SELECT" 
			+ " concept_set_name"
			+ ",concept_id" 
			+ " FROM ohdsi.concept_set cs" 
			+ " INNER JOIN ohdsi.concept_set_item csi" 
			+ " ON cs.concept_set_id = csi.concept_set_id"
		);
	csQuery.last(); 
	System.out.println("INFO: # of conceptTpls: " + csQuery.getRow()); 
	csQuery.beforeFirst();

	Statement deraSt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
	ResultSet deraQuery = deraSt.executeQuery(
			"SELECT"
			+ " drug_era_start_date"
			+ ",person_id"
			+ ",drug_era_end_date"
			+ ",drug_concept_id"
			+ ",drug_exposure_count"
			+ " FROM drug_era"
			+ " WHERE DRUG_ERA_START_DATE <= TO_DATE('" + dateStr + "','yyyy-MM-dd') AND DRUG_ERA_END_DATE >= (TO_DATE('" + dateStr + "','yyyy-MM-dd'))"
		);
	deraQuery.last();
	System.out.println("INFO: # of deras: " + deraQuery.getRow());
	deraQuery.beforeFirst();

	Statement dexpSt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
	ResultSet dexpQuery = dexpSt.executeQuery(
			"SELECT"
			+ " dexp.drug_exposure_id, dexp.person_id, dexp.drug_concept_id, to_char(dexp.drug_exposure_start_date, 'yyyy-MM-dd HH24:MI:SS') as drug_exposure_start_date, to_char(dexp.drug_exposure_end_date, 'yyyy-MM-dd HH24:MI:SS') as drug_exposure_end_date, dexp.drug_type_concept_id, dexp.stop_reason, dexp.refills, dexp.quantity, dexp.days_supply, dexp.sig, sm.expected, sm.min, sm.max, dexp.route_concept_id, dexp.lot_number, dexp.provider_id, dexp.visit_occurrence_id, dexp.drug_source_value, dexp.drug_source_concept_id, dexp.route_source_value, dexp.dose_unit_source_value, dstr.ingredient_concept_id, dstr.amount_value, dstr.amount_unit_concept_id, dstr.numerator_value, dstr.numerator_unit_concept_id, dstr.denominator_value, dstr.denominator_unit_concept_id, dexp.indication_concept_id"
			+ " FROM drug_exposure dexp, drug_strength dstr, sig_mapping sm"
			+ " WHERE dexp.drug_concept_id = dstr.drug_concept_id"
			+ " AND dexp.sig = sm.sig"
			+ " AND dexp.person_id IN"
			+ "(SELECT DISTINCT de.person_id FROM drug_era AS de WHERE drug_era_start_date <= TO_DATE('" + dateStr + "','yyyy-MM-dd') AND drug_era_end_date >= (TO_DATE('" + dateStr + "','yyyy-MM-dd')))"
		);
	dexpQuery.last();
	System.out.println("INFO: # of dexps: " + dexpQuery.getRow());
	dexpQuery.beforeFirst();

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
	    kSession.insert( new ConceptSetItem(csQuery.getString(1), csQuery.getInt(2)) );
	    cnt++;
	}
	while (personQuery.next()){
		Person p = new Person();
		p.setPersonId(personQuery.getLong(1));
		p.setYearOfBirth(personQuery.getInt(3));
		kSession.insert(p);
		cnt++;
	} 
	while (deraQuery.next()){
		Calendar start = Calendar.getInstance();
		start.setTime((java.util.Date) deraQuery.getDate(1));
		Calendar end = Calendar.getInstance();
		end.setTime((java.util.Date) deraQuery.getDate(3));
		// System.out.println("DRUG ERA: " + deraQuery.getDate(1) + " | " + deraQuery.getLong(2) + " | " + deraQuery.getDate(3) + " | " + deraQuery.getInt(4) + " | " + deraQuery.getInt(5));
		kSession.insert( new DrugEra(start, deraQuery.getLong(2), end, deraQuery.getInt(4), deraQuery.getInt(5)) );
		cnt++;
	}
	DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	while (dexpQuery.next()){
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		try {
			LocalDateTime ldt = LocalDateTime.parse(dexpQuery.getString(4),df);
			start.set(ldt.getYear(), ldt.getMonthValue()-1, ldt.getDayOfMonth(), ldt.getHour(), ldt.getMinute(), ldt.getSecond());
			LocalDateTime ldt2 = LocalDateTime.parse(dexpQuery.getString(5),df);
			end.set(ldt.getYear(), ldt.getMonthValue()-1, ldt.getDayOfMonth(), ldt.getHour(), ldt.getMinute(), ldt.getSecond());
	    } catch (DateTimeParseException e) { e.printStackTrace(); }
	    System.out.println("CREATING EX_DEXP: " + dexpQuery.getLong(1) + "\n" + "\t" + start + "\n" + "\t" + end + "\n" + "\t" + null + "\n" + "\t" + null + "\n" + "\t" + dexpQuery.getLong(2) + "\n" + "\t" + dexpQuery.getInt(3) + "\n" + "\t" + dexpQuery.getInt(6) + "\n" + "\t" + dexpQuery.getString(7) + "\n" + "\t" + dexpQuery.getShort(8) + "\n" + "\t" + dexpQuery.getInt(9) + "\n" + "\t" + dexpQuery.getShort(10) + "\n" + "\t" + dexpQuery.getString(11) + "\n" + "\t" + dexpQuery.getInt(12) + "\n" + "\t" + dexpQuery.getInt(13) + "\n" + "\t" + dexpQuery.getInt(14) + "\n" + "\t" + dexpQuery.getInt(15) + "\n" + "\t" + dexpQuery.getString(16) + "\n" + "\t" + dexpQuery.getInt(17) + "\n" + "\t" + dexpQuery.getLong(18) + "\n" + "\t" + dexpQuery.getString(19) + "\n" + "\t" + dexpQuery.getInt(20) + "\n" + "\t" + dexpQuery.getString(21) + "\n" + "\t" + dexpQuery.getString(22) + "\n" + "\t" + dexpQuery.getInt(23) + "\n" + "\t" + dexpQuery.getDouble(24) + "\n" + "\t" + dexpQuery.getInt(25) + "\n" + "\t" + dexpQuery.getDouble(26) + "\n" + "\t" + dexpQuery.getInt(27) + "\n" + "\t" + dexpQuery.getDouble(28) + "\n" + "\t" + dexpQuery.getInt(29) + "\n" + "\t" + 0.00 + "\n" + "\t" + dexpQuery.getInt(30));
		ExtendedDrugExposure ex_dexp = new ExtendedDrugExposure(
					dexpQuery.getLong(1), // drugExposureId
					start,
					end,
					null,
					null,
					dexpQuery.getLong(2), // personId
					dexpQuery.getInt(3), // drugConceptId
					dexpQuery.getInt(6), // drugTypeConceptId
					dexpQuery.getString(7), // stopReason
					dexpQuery.getShort(8), // refills
					dexpQuery.getInt(9), // drugQuantity
					dexpQuery.getShort(10), // daysSupply
					dexpQuery.getString(11), // sig
					dexpQuery.getInt(12), // sigExpected
					dexpQuery.getInt(13), // sigMin
					dexpQuery.getInt(14), // sigMax
					dexpQuery.getInt(15), // routeConceptId
					dexpQuery.getString(16), // lotNumber
					dexpQuery.getInt(17), // providerId
					dexpQuery.getLong(18), // visitOccurrenceId
					dexpQuery.getString(19), // drugSourceValue
					dexpQuery.getInt(20), // drugSourceConceptId
					dexpQuery.getString(21), // routeSourceValue
					dexpQuery.getString(22), // doseUnitSourceValue
					dexpQuery.getInt(23), // ingredientConceptId
					dexpQuery.getDouble(24), // amountValue
					dexpQuery.getInt(25), // amountUnitConceptId
					dexpQuery.getDouble(26), // numeratorValue
					dexpQuery.getInt(27), // numeratorUnitConceptId
					dexpQuery.getDouble(28), // denominatorValue
					dexpQuery.getInt(29), // denominatorUnitConceptId
					0.00, // dailyDosage - default value 0.00, set below
					dexpQuery.getInt(30)  // indicationConceptId
				);
		if(dexpQuery.getString(24) != "null" && dexpQuery.getString(12) != "null"){
            ex_dexp.setSigDailyDosage(dexpQuery.getDouble(24), dexpQuery.getDouble(12));
            System.out.println("\tSET SIG DAILY DOSAGE: " + ex_dexp.getDailyDosage());
        }
        else if(dexpQuery.getString(9) != "null" && dexpQuery.getString(10) != "null" && dexpQuery.getString(24) != "null"){
          	ex_dexp.setRegDailyDosage(dexpQuery.getInt(9), dexpQuery.getShort(10), dexpQuery.getDouble(24));
			System.out.println("\tSET REG DAILY DOSAGE: " + ex_dexp.getDailyDosage());

        }
        else if(dexpQuery.getString(9) != "null" && dexpQuery.getString(10) != "null" && dexpQuery.getString(26) != "null"){
          	ex_dexp.setComplexDailyDosage(dexpQuery.getInt(9), dexpQuery.getShort(10),dexpQuery.getDouble(26));
			System.out.println("\tSET COMPLEX DAILY DOSAGE: " + ex_dexp.getDailyDosage());

        }
        kSession.insert(ex_dexp);
        cnt++;
	}

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
	personSt.close();
	csQuery.close();
	csSt.close();
	deraQuery.close();	
	deraSt.close();
	dexpQuery.close();
	dexpSt.close();

	System.out.println("INFO: Rule engine session closed!"); 
            
    }       
}
