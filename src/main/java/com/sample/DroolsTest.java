package com.sample;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
//import org.drools.runtime.rule.WorkingMemory;

// import com.sample.model.Risk;
// import com.sample.model.RiskScore;

import java.sql.*;
import java.util.*;

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
import edu.pitt.dbmi.ohdsiv5.db.Observation;
import edu.pitt.dbmi.ohdsiv5.db.ObservationPeriod;
import edu.pitt.dbmi.ohdsiv5.db.ProcedureOccurrence;
import edu.pitt.dbmi.ohdsiv5.db.Person;

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
	    	
	System.out.println("Gathering data...");
	openDbSession();
	System.out.println("INFO: Hibernate session open!");

	Session hibernateSession = session.getCurrentSession();
	hibernateSession.beginTransaction();

	//***** simple debugging query *****
	//List<Long> personIds = (List<Long>) hibernateSession.createQuery("SELECT personId FROM Person WHERE personId in (3,63,123,183)").list();
	//System.out.println("INFO: personIds: " + personIds.toString());

	//**** Pull data to be loaded into Drools working memory *****
	// pull data from a specific date
	String startDateStr = "2008-03-12";
	String endDateStr = "2008-03-14";

	// Get concept ids and names from the defined concept sets. There is currently no hibernate mapping for this.
	SQLQuery query = hibernateSession.createSQLQuery("SELECT concept_set_name,concept_id FROM ohdsi.concept_set cs INNER JOIN ohdsi.concept_set_item csi ON cs.concept_set_id = csi.concept_set_id");
	query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	List csItemResults =  query.list();
	System.out.println("INFO: number of conceptTpls: " + csItemResults.size()); // this is a list of Map objects  [{concept_id=40163566, concept_set_name=Anticoagulants}, {concept_id=40163560, concept_set_name=Anticoagulants}, ...}

	// The dataset records drug exposures as single day events and then creates drug era for exposures with <= 30 day gaps. So, we have to query first the drug eras (wich are coded as ingredients) and then the drug exposures (coded as clinical drugs) 
	List<DrugEra> deras = (List<DrugEra>) hibernateSession.createQuery("FROM DrugEra WHERE DRUG_ERA_START_DATE <= TO_DATE('" + endDateStr + "','yyyy-MM-dd') AND DRUG_ERA_END_DATE >= (TO_DATE('" + startDateStr + "','yyyy-MM-dd'))").list();
	System.out.println("INFO: number of deras: " + deras.size());

	// The query for drug exposures is limited to exposures for those persons with drug eras overlapping the target date
	List<DrugExposure> dexps = (List<DrugExposure>) hibernateSession.createQuery("FROM DrugExposure AS dexp WHERE personId IN (SELECT DISTINCT de.personId FROM DrugEra AS de WHERE DRUG_ERA_START_DATE <= TO_DATE('" + endDateStr + "','yyyy-MM-dd') AND DRUG_ERA_END_DATE >= (TO_DATE('" + startDateStr + "','yyyy-MM-dd')))").list();
	System.out.println("INFO: number of dexps for persons with drug eras during the date range: " + dexps.size());

	// The query for person data is limited to exposures for those persons with drug eras overlapping the target date
	List<Person> persons = (List<Person>) hibernateSession.createQuery("FROM Person AS p WHERE p.personId IN (SELECT DISTINCT de.personId FROM DrugEra AS de WHERE DRUG_ERA_START_DATE <= TO_DATE('" + endDateStr + "','yyyy-MM-dd') AND DRUG_ERA_END_DATE >= (TO_DATE('" + startDateStr + "','yyyy-MM-dd')))").list();
	//List<Person> persons = (List<Person>) hibernateSession.createQuery("FROM Person").list();
	System.out.println("INFO: number of persons with drug eras during the date range: " + persons.size());


	System.out.println("Done gathering data...");
	//-------------------------------------------------

	// Run the rule engine
	KieServices ks = KieServices.Factory.get();
	KieContainer kContainer = ks.getKieClasspathContainer();

	System.out.println("INFO: Rule engine session open!");
	KieSession kSession = kContainer.newKieSession("ksession-rules");
	kSession.setGlobal("hibernateSession", hibernateSession);
	   
	// Load all the facts
	System.out.println("Asserting facts...");
	int cnt = 0;
	Iterator iter = csItemResults.iterator();
	while (iter.hasNext()){
	    Map map = (Map) iter.next();
	    kSession.insert( new ConceptSetItem((String) map.get("concept_set_name"), (Integer) map.get("concept_id")));
	    cnt++;
	}
	for (Person p : persons) {           	
	    kSession.insert((Person) hibernateSession.get(Person.class, p.getPersonId()));            	
	    cnt++;
	}
	for (DrugExposure dex : dexps) {           	
	    kSession.insert((DrugExposure) hibernateSession.get(DrugExposure.class, dex.getDrugExposureId()));            	
	    cnt++;
	}
	for (DrugEra dera : deras) {           	
	    kSession.insert((DrugEra) hibernateSession.get(DrugEra.class, dera.getDrugEraId()));            	
	    cnt++;
	}
	System.out.println("Facts asserted: " + cnt);
            
	System.out.println("Firing rules for assessment...");
	// fire rules
	kSession.fireAllRules();            
 
            
	closeDbSession();
	System.out.println("INFO: Hibernate session closed!");
	
	kSession.dispose();
	System.out.println("INFO: Rule engine session closed!");
	//     System.out.println("Patient Count: " + cnt);           
            
    }       
}
