/**
 * Upia2Omop.java
 *
 * Translate and Load drug, MDS, and falls data from UPMC Nursing
 * Homes into the OMOP Common Data Model
 *
 * Copyright (C) 2012-2015 University of Pittsburgh. All Rights Reserved.
 *
 * Authors: Richard D. Boyce, Jeremy Jao, Yifan Ning
 *
 */


package edu.pitt.dbmi.ohdsiv5;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.ClassCastException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.TransactionException;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import edu.pitt.dbmi.ohdsiv5.db.Concept;
import edu.pitt.dbmi.ohdsiv5.db.ConceptRelationship;
import edu.pitt.dbmi.ohdsiv5.db.ConditionOccurrence;
import edu.pitt.dbmi.ohdsiv5.db.ConditionEra;
import edu.pitt.dbmi.ohdsiv5.db.DrugExposure;
import edu.pitt.dbmi.ohdsiv5.db.DrugEra;
import edu.pitt.dbmi.ohdsiv5.db.Mds3;
import edu.pitt.dbmi.ohdsiv5.db.Mds2;
import edu.pitt.dbmi.ohdsiv5.db.Mds;
import edu.pitt.dbmi.ohdsiv5.db.Observation;
import edu.pitt.dbmi.ohdsiv5.db.ObservationPeriod;
import edu.pitt.dbmi.ohdsiv5.db.ProcedureOccurrence;
import edu.pitt.dbmi.ohdsiv5.db.Person;
import edu.pitt.dbmi.ohdsiv5.db.SourceToConceptMap;
import edu.pitt.dbmi.ohdsiv5.db.util.HibernateUtil;

/**
 * Program to convert and persist de-identified UPIA MARS MDS data into OMOP
 * structured database
 *
 * @author Michelle Morris, Richard Boyce, Jeremy Jao
 *
 */

public class Upia2Omop {
    // Read in the MDS Mapping file
    static List<MrnDeidData> mrnList;
    static HashMap<Long, ArrayList<ObservationPeriod>> obsPeriodMap;

    static SessionFactory session = HibernateUtil.getSession().getSessionFactory();

    HashMap hm = new HashMap();

    public Upia2Omop() {
	super();
    }


    private static void update(Object pojo) {
        //update an object in the Database

        // Getting the Session Factory and session
        Session sess = session.getCurrentSession();

        boolean closeTransaction = false;
        if ( sess.getTransaction() == null ||
             sess.getTransaction().isActive() == false ) {
            sess.beginTransaction();
            closeTransaction = true;
        }

        sess.update(pojo);

        // If the transaction was opened in this method commit here
        // otherwise allow the over-arching method handle it
        if ( closeTransaction ) {
            try{
                sess.getTransaction().commit();
            } catch (TransactionException ex) {
                ex.printStackTrace();
                sess.getTransaction().rollback();
            }
        }

        System.out.println("Record updated: " + pojo.getClass());
    }

    private static void save(Object pojo) {
        //Save to the Database

        // Getting the Session Factory and session
        Session sess = session.getCurrentSession();

        boolean closeTransaction = false;
        if ( sess.getTransaction() == null ||
             sess.getTransaction().isActive() == false ) {
            sess.beginTransaction();
            closeTransaction = true;
        }

        Serializable sv = sess.save(pojo);
        if (sv != null)
            System.out.println("Save - serializable: " + sv);
        else
            System.out.println("Save DID NOT return a serializable: " + sv);

        // If the transaction was opened in this method commit here
        // otherwise allow the over-arching method handle it
        if ( closeTransaction ) {
            try{
                sess.getTransaction().commit();
            } catch (TransactionException ex) {
                ex.printStackTrace();
	        sess.getTransaction().rollback();
            }
        }

        System.out.println("Record Inserted: " + pojo.getClass());
    }

    @SuppressWarnings("unchecked")
    private void processPersonsFromMDS3() {
        // Get all the MDS3 Records currently loaded into the
        // DB and then loop through and save person records

        // Acquiring the Transaction
        Session sess = session.getCurrentSession();
	// DEBUG MODE 
        List<Mds3> mdsRecords = (List<Mds3>) sess.createQuery("FROM Mds3 ORDER BY PATIENTID ASC").list();
	//List<Mds3> mdsRecords = (List<Mds3>) sess.createQuery("FROM Mds3 WHERE PATIENTID = 10424 ORDER BY PATIENTID ASC").list();

        if ( mdsRecords != null){
            int[] control = new int[2];
            control[0] =  -1;
            control[1] = 0;
            for ( Mds3 mdsRecord : mdsRecords){
                loadPersons((Mds)mdsRecord, control, sess);
            }
        }
    }

    private void processPersonsFromMDS2() {
        Session sess = session.getCurrentSession();
	
        List<Mds2> mdsRecords = (List<Mds2>) sess.createQuery("FROM Mds2 ORDER BY PATIENTID ASC").list();

        if ( mdsRecords != null){
            int[] control = new int[2];
            control[0] = -1; //flushIdx
            control[1] = 0;
            for ( Mds2 mdsRecord : mdsRecords){
                loadPersons((Mds)mdsRecord, control, sess);
            }
	    
        }
    }

    private void loadPersons(Mds mdsRecord, int[] control, Session sess) {
        control[0] += 1;
	int flushIdx = control[0];

        control[1] += 1;
	int ctr = control[1];

        if ( mdsRecord == null )
            return;

	// to keep performance reasonable with this large transaction
	if (flushIdx % 10 == 9){
	    System.out.println("FLUSHING AND CLEARING SESSION AT MDS RECORD " + flushIdx);
	    sess.flush();
	    sess.clear();
	}

	// FOR DEVELOPMENT AND DEBUGGING
	//if (flushIdx == 21)
	//  break;
	//if (mdsRecord.getPatientId() == "5278") // MIGHT NOT WORK
	//    break;
	//if (ctr == 30)
	//    break;

	Long personId =  Long.parseLong(mdsRecord.getPatientId());
	String gender = mdsRecord.getGender();
	if (gender == null){
	    System.out.println("INFO: mdsRecord.getGender returned NULL. Not adding person using this MDS record (row number " + ctr + ")");
	    return;
	}
	String birthdate = mdsRecord.getBirthdate();
	if (birthdate == null){
	    System.out.println("INFO: mdsRecord.getBirthdate returned NULL. Not adding person using this MDS record (row number " + ctr + ")");
	    return;
	}
	addPerson(personId, mdsRecord, ctr);
    }

    @SuppressWarnings("unchecked")
    private void loadConditionsFromMDSReports() {

    // Get all the MDS Records currently loaded into the DB and then
    // loop through and save conditions in OMOP format. More details
    // in comments below and the documentation at
    // LoadingData#loadingConditionsFromMDSxs

    // Acquiring the Transaction
    Session sess = session.getCurrentSession();

    //DEBUGGING
    //List<Long> personIds = (List<Long>) sess.createQuery("SELECT personId FROM Person WHERE personId in (7743,7872,7674)").list();

    // DEPLOYMENT
    List<Long> personIds = (List<Long>) sess.createQuery("SELECT personId FROM Person").list();

    if ( personIds != null){

        int flushIdx = -1;
        for ( Long personId : personIds){
            flushIdx += 1;

            if ( personId == null )
		continue;

            // to keep performance reasonable with this large transaction
            if (flushIdx % 10 == 0){
		System.out.println("FLUSHING AND CLEARING SESSION AT MDS RECORD " + flushIdx);
		sess.flush();
		sess.clear();
            }

            // FOR DEVELOPMENT AND DEBUGGING
            // if (flushIdx == 21)
	    // 	break;
            // if (mdsRecord.getPatientId() == "5278") // MIGHT NOT WORK
            //    break;

            List<ObservationPeriod> obsPeriods = (List<ObservationPeriod>) sess.createQuery("FROM ObservationPeriod op WHERE op.personId = " + personId).list();

            // Iterate over the list of observation periods to
            // load conditions from reports. For further
            // explanation, see the algorithm description in
            // LoadingData#loadingConditionsFromMDS
            for (ObservationPeriod op :  obsPeriods) {

                Calendar startDate = op.getObservationPeriodStartDate();
                Calendar endDate = op.getObservationPeriodEndDate();

                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
                String startDateStr = sdf.format(startDate.getTime());
                String endDateStr = sdf.format(endDate.getTime());

                List<ProcedureOccurrence> mdsProcedures = (List<ProcedureOccurrence>) sess.createQuery("FROM ProcedureOccurrence procOc WHERE procOc.personId = " + personId + " AND procOc.procedureDate >= TO_DATE('" + startDateStr + "','MM-DD-YYYY') AND procOc.procedureDate <= TO_DATE('" + endDateStr + "','MM-DD-YYYY') ORDER BY PROCEDURE_DATE ASC").list();
                System.out.println("Person " + personId + " has " + mdsProcedures.size() + " MDS procedures within the observation period:" + startDateStr + " : " + endDateStr );

                // get the conditions within the obs periods and
                // the MDS data and process them together
                HashMap cOccHash = new HashMap(); // for condition occurrences, keyed by condition source value
                HashMap cEraHash = new HashMap(); // for condition eras, keyed by condition concept id

                List<ConditionOccurrence> cOccL = (List<ConditionOccurrence>) sess.createQuery("FROM ConditionOccurrence condOc WHERE condOc.personId = " + personId + " AND condOc.conditionEndDate >= TO_DATE('" + startDateStr + "','MM-DD-YYYY') AND condOc.conditionStartDate <= TO_DATE('" + endDateStr + "','MM-DD-YYYY')").list();
                System.out.println("Person " + personId + " has " + cOccL.size() + " Conditions occurrences that overlap with  the observation period:" + startDateStr + " : " + endDateStr );

                for (ConditionOccurrence co :  cOccL)
		    cOccHash.put((String)co.getSourceConditionCode(), co);

                List<ConditionEra> cEraL = (List<ConditionEra>) sess.createQuery("FROM ConditionEra condEra WHERE condEra.personId = " + personId + " AND condEra.conditionEraEndDate >= TO_DATE('" + startDateStr + "','MM-DD-YYYY') AND condEra.conditionEraStartDate <= TO_DATE('" + endDateStr + "','MM-DD-YYYY')").list();
                System.out.println("INFO: Person " + personId + " has " + cEraL.size() + " Condition eras that overlap with  the observation period:" + startDateStr + " : " + endDateStr );

                for (ConditionEra ce :  cEraL)
		    cEraHash.put((Integer)ce.getConditionConceptId(), ce);

                // iterate over the list of procedures, retrieving
                // the MDS data associated with each and loading
                // conditions present in the MDS report
		for (ProcedureOccurrence po :  mdsProcedures){
		    String dateOfEvent = sdf.format(po.getProcedureDate().getTime());
		    String procedureType = po.getProcedureSourceValue();

		    if (procedureType.equals("M3A") || procedureType.equals("M3Q") || procedureType.equals("M3P") || procedureType.equals("M3O")){
			List<Mds3> mdsRecords3 = (List<Mds3>) sess.createQuery("FROM Mds3 mds WHERE mds.patientId = " + po.getPersonId() + " AND mds.examType = '" + procedureType + "' AND mds.dateOfEvent = '" + dateOfEvent + "'").list();
			// Debugging
			//System.out.println("Found at least one admission MDS matching a procedure occurrence: " + mdsRecords.size() + ". po date: " + dateOfEvent);
			//System.out.println(po.getPersonId() + "  " + po.getProcedureSourceValue() + "  " + po.getProcedureDate());
			for (Mds3 mdsRec :  mdsRecords3){
			    System.out.println("INFO: Adding conditions in MDS 3.0 report " +mdsRec.getId() + " for Person " + personId);
			    addConditions(personId, startDate, endDate, (Mds)mdsRec, po.getProcedureDate(), cOccHash, cEraHash);		    
			}
		    }
		    else {
			// NOTE: the dateOfEvent (recorddate) or dateOfEntry might be the applicable procedure date
			List<Mds2> mdsRecords2 = (List<Mds2>) sess.createQuery("FROM Mds2 mds WHERE mds.patientId = " + po.getPersonId() + " AND mds.examType = '" + procedureType + "' AND (mds.dateOfEvent = '" + dateOfEvent + "' OR mds.entrydate = '" + dateOfEvent + "')").list();
			for (Mds2 mdsRec :  mdsRecords2) {
			    System.out.println("INFO: Adding conditions in MDS 2.0 report " + mdsRec.getId() + " for Person " + personId);
			    addConditions(personId, startDate, endDate, (Mds)mdsRec, po.getProcedureDate(), cOccHash, cEraHash);
			}
		    }
		}
	    }
	}
    }
}

private void addConditions(Long personId, Calendar startDate, Calendar endDate, Mds mdsRecord, Calendar mdsProcDate, HashMap cOccHash, HashMap cEraHash) {
    String mdsType = mdsRecord.getExamType();
    Integer daysToNextMDS = 9; // the default used because the next MDS after a 5-day MDS for a short stay is on day 14; so, the shortest possible time period
    if (mdsType.equals("M3A") || mdsType.equals("M3Q") || mdsType.equals("M2A") || mdsType.equals("M2Q"))
	daysToNextMDS = 90;
    // TODO: do the same for the 14-, and 30-day report types

    Boolean admit = false;
    if (mdsType.equals("M3A") ||  mdsType.equals("M2A"))
	admit = true;

    // Get each of the possible comorbidities and store as conditions
    // use mapping table to map to ICD9 code

    // TODO: many of these are not entering the CDM instance because they are values such as 'no' that are not in the Standard Vocabulary. Handle these special cases as much as possible use thin SNOMED mappings here: http://aspe.hhs.gov/daltcp/reports/2011/StratEng.htm
    // TODO: consider adjusting the condition occurrence types to use the LOINC codes for MDS 3 data elements (see http://dbmi-icode-01.dbmi.pitt.edu/svn/GeriOMOP/docs/MDS-3.0-in-MARS/MDS3-in-LOINC-and-OMOP-SV4.csv). What advantages/disadvantages?
    addCondition(personId, startDate, endDate, mdsRecord.getCancer(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
    addCondition(personId, startDate, endDate, mdsRecord.getSchizophrenia(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit); // jlj69: edited
    addCondition(personId, startDate, endDate, mdsRecord.getHipfracture(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit); // jlj69: edited
    //addCondition(personId, startDate, endDate, mdsRecord.getOtherfracture(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit); // TODO: commented out until we can decide what to do with this ambiguous concept
    addCondition(personId, startDate, endDate, mdsRecord.getArthritis(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
    addCondition(personId, startDate, endDate, mdsRecord.getOsteoporosis(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
    addCondition(personId, startDate, endDate, mdsRecord.getStroke(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
    addCondition(personId, startDate, endDate, mdsRecord.getDiabetes(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit); // jlj69: edited
    addCondition(personId, startDate, endDate, mdsRecord.getHypertension(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
    addCondition(personId, startDate, endDate, mdsRecord.getHypotension(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
    addCondition(personId, startDate, endDate, mdsRecord.getEmphysemacopd(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
    addCondition(personId, startDate, endDate, mdsRecord.getAshd(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
    addCondition(personId, startDate, endDate, mdsRecord.getPneumonia(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, true, admit);
    addCondition(personId, startDate, endDate, mdsRecord.getParkinsons(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
    addCondition(personId, startDate, endDate, mdsRecord.getAlzheimers(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
    addCondition(personId, startDate, endDate, mdsRecord.getDementiaotherthanalzheimers(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
    addCondition(personId, startDate, endDate, mdsRecord.getAnxiety(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit); // jlj69: edited
    addCondition(personId, startDate, endDate, mdsRecord.getTuberculosis(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
    addCondition(personId, startDate, endDate, mdsRecord.getUrinarytractinfection(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, true, admit); // jlj69: had 30 in the method name at the end that I erased
    addCondition(personId, startDate, endDate, mdsRecord.getViralhepatitis(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
    addCondition(personId, startDate, endDate, mdsRecord.getTraumaticbraininjury(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
    addCondition(personId, startDate, endDate, mdsRecord.getDepression(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
    addCondition(personId, startDate, endDate, mdsRecord.getManicdepression(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
    addCondition(personId, startDate, endDate, mdsRecord.getAphasia(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
    addCondition(personId, startDate, endDate, mdsRecord.getCerebralpalsy(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
    addCondition(personId, startDate, endDate, mdsRecord.getHemiplegiahemiparesis(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
    addCondition(personId, startDate, endDate, mdsRecord.getMultiplesclerosis(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
    addCondition(personId, startDate, endDate, mdsRecord.getParaplegia(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
    addCondition(personId, startDate, endDate, mdsRecord.getQuadriplegia(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
    addCondition(personId, startDate, endDate, mdsRecord.getSeizuredisorderorepilepsy(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit); // jlj69: added OrEpilepsy to end
    addCondition(personId, startDate, endDate, mdsRecord.getHeartfailure(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
    addCondition(personId, startDate, endDate, mdsRecord.getDvtpepte(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit); // DVT ICD9
    addCondition(personId, startDate, endDate, mdsRecord.getMultidrugresistantorganism(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, true, admit); // jlj69: ICD9 of drug resistant infection I assume!
    //addCondition(personId, startDate, endDate, mdsRecord.getAsthma(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit); // TODO
    //addCondition(personId, startDate, endDate, mdsRecord.getCataracts(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit); // TODO
    addCondition(personId, startDate, endDate, mdsRecord.getAnemia(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, true, admit);
    addCondition(personId, startDate, endDate, mdsRecord.getSepticemia(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, true, admit);
    addCondition(personId, startDate, endDate, mdsRecord.getOstomy(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, true, admit);

    try {
	Mds3 mds3record = (Mds3) mdsRecord;
	System.out.println("MDS3!");
	//jlj69: missing in the python and sql of mds3
	//addCondition(personId, startDate, endDate, mdsRecord.getFibrillation(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
	addCondition(personId, startDate, endDate, mds3record.getThyroiddisorder(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
	addCondition(personId, startDate, endDate, mds3record.getGerdorgiulcer(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
	addCondition(personId, startDate, endDate, mds3record.getPtsd(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
	addCondition(personId, startDate, endDate, mds3record.getPsychosis(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit); // jlj69: edited
	addCondition(personId, startDate, endDate, mds3record.getConstipation(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, true, admit);
	addCondition(personId, startDate, endDate, mds3record.getCirrhosis(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
	addCondition(personId, startDate, endDate, mds3record.getBenignprostatichyperplasia(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
	addCondition(personId, startDate, endDate, mds3record.getNeurogenicbladder(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
	addCondition(personId, startDate, endDate, mds3record.getObstructiveuropathy(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
	addCondition(personId, startDate, endDate, mds3record.getWoundinfectionnotfoot(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, true, admit); // jlj69: didn't see anything similar so added
	addCondition(personId, startDate, endDate, mds3record.getHyponatremia(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, true, admit);
	addCondition(personId, startDate, endDate, mds3record.getHyperkalemia(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, true, admit);
	addCondition(personId, startDate, endDate, mds3record.getHuntingtonsDisease(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
	addCondition(personId, startDate, endDate, mds3record.getTourettessyndrome(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
    addCondition(personId, startDate, endDate, mds3record.getComatose(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
    } catch(ClassCastException e) {
	System.out.println("This is not MDS3.");
    }
        
    try {
	Mds2 mds2record = (Mds2) mdsRecord;
	System.out.println("MDS2!");
	addCondition(personId, startDate, endDate, mds2record.getHyperthyroidism(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
	addCondition(personId, startDate, endDate, mds2record.getHypothyroidism(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
	addCondition(personId, startDate, endDate, mds2record.getDiabeticretinopathy(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
	addCondition(personId, startDate, endDate, mds2record.getGlaucoma(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
	addCondition(personId, startDate, endDate, mds2record.getAllergies(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
	addCondition(personId, startDate, endDate, mds2record.getMaculardegeneration(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
	addCondition(personId, startDate, endDate, mds2record.getRenalfailure(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
	addCondition(personId, startDate, endDate, mds2record.getDiseasesnoneofabove(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
	addCondition(personId, startDate, endDate, mds2record.getCdiff(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, true, admit);
	addCondition(personId, startDate, endDate, mds2record.getConjunctivitis(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, true, admit);
	addCondition(personId, startDate, endDate, mds2record.getHiv(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
	addCondition(personId, startDate, endDate, mds2record.getPeripheralvasculardisease(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit); // not in list
	addCondition(personId, startDate, endDate, mds2record.getOthercardiovasculardisease(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit); // not in list
	addCondition(personId, startDate, endDate, mds2record.getMissinglimb(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
	addCondition(personId, startDate, endDate, mds2record.getTransientischemicattack(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit); // TODO: jlj69 forgot to code this
	addCondition(personId, startDate, endDate, mds2record.getInfectionsnoneofabove(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS,true, admit);
	addCondition(personId, startDate, endDate, mds2record.getOtherdiagnosisicd9a(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, true, admit);
	addCondition(personId, startDate, endDate, mds2record.getOtherdiagnosisicd9b(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, true, admit);
	addCondition(personId, startDate, endDate, mds2record.getOtherdiagnosisicd9c(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, true, admit);
	addCondition(personId, startDate, endDate, mds2record.getOtherdiagnosisicd9d(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, true, admit);
	addCondition(personId, startDate, endDate, mds2record.getOtherdiagnosisicd9e(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, true, admit);
	addCondition(personId, startDate, endDate, mds2record.getRespiratoryinfection(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, true, admit);
	addCondition(personId, startDate, endDate, mds2record.getStd(), cOccHash, cEraHash, mdsProcDate, daysToNextMDS, false, admit);
    } catch(ClassCastException e) {
	System.out.println("This is not MDS2");
    }
}


private void addCondition(Long personId, Calendar startDate, Calendar endDate, String mdsIcd, HashMap cOccHash, HashMap cEraHash, Calendar mdsProcDate, Integer daysToNextMds, Boolean acute, Boolean admit){
    if ( mdsIcd  == null || mdsIcd.length() <= 0) // skip if this is a blank string
	return;

    Calendar expNextMDSDate = (Calendar) mdsProcDate.clone();
    expNextMDSDate.add(Calendar.DAY_OF_YEAR, (int)daysToNextMds);

    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
    String startDateStr = sdf.format(startDate.getTime());
    String endDateStr = sdf.format(endDate.getTime());
    String mdsProcDateStr = sdf.format(mdsProcDate.getTime());
    String expNextMDSDateStr = sdf.format(expNextMDSDate.getTime());

    Integer conditionOccurrenceType = 38000245; //Problem List

    // Caching source id (ICD or SNOMED) to OMOP standard vocab
    // mapping to a global hashmap
    Integer conditionSourceConceptId = null;
    if (hm.containsKey(mdsIcd))
	conditionSourceConceptId = (Integer)hm.get(mdsIcd);
    else {
	if(mdsIcd.length() >= 8) {
	    conditionSourceConceptId = Integer.parseInt(mdsIcd);
	    hm.put(mdsIcd, conditionSourceConceptId);
	}
	else {
	    conditionSourceConceptId = getConditionConceptId(mdsIcd);
	    if (conditionSourceConceptId != null) 
		hm.put(mdsIcd, conditionSourceConceptId);
	    else {
		System.out.println("INFO: Null conditionSourceConceptId. Not able to input condition");
		return;
	    }
	}
    }
    
    Integer condId = null;
    if (hm.containsKey(Integer.toString(conditionSourceConceptId))) {
	condId = (Integer)hm.get(Integer.toString(conditionSourceConceptId));
    }
    else {
	condId = getConceptId2(conditionSourceConceptId);
	if (condId != null)
	    hm.put(Integer.toString(conditionSourceConceptId), condId);
	else {
	    System.out.println("INFO: Null ConceptId2 found for: " + conditionSourceConceptId);
	    return;
	}
    }
    if(condId == null) {
	System.out.println("INFO: Null Condition concept id: " + conditionSourceConceptId + ". Not able to put this in condition");
	return;
    }
    else
	System.out.println("INFO: Inserting Condition concept id into the CDM: " + condId + " (conditionSourceConceptId: " + conditionSourceConceptId + ") for personId " + personId);

    
    //if(cOccHash != null && cEraHash != null && mdsProcDate != null) { //TODO: re-enable if there is a reason to
    if(cOccHash.containsKey((String)mdsIcd) && cEraHash.containsKey((Integer)condId)){
	// an existing condition, alter dates as needed
	System.out.println("INFO: Condition occurrence and era found in hashmap. Adjusting the dates for these.");
	ConditionOccurrence cond = (ConditionOccurrence) cOccHash.get(mdsIcd);
	ConditionEra condEra = (ConditionEra) cEraHash.get(condId);

	// check if the MDS procedure date is within the condition era
	// Can procedures date equals to condition start or end date?

	if(cond.getConditionStartDate().before(mdsProcDate) && cond.getConditionEndDate().after(mdsProcDate)){
	    // no need to adjust the start date of of the condition
	    System.out.println("INFO: MDS procedures falls within the current Condition era.");

	    if(acute){
                if(cond.getConditionEndDate().before(expNextMDSDate) && expNextMDSDate.before(endDate)){
                    System.out.println("INFO: The end date of an acute condition comes before the end of the observation period but after the current condition end date. Extending the condition end date be the date of the next anticipated MDS in " + daysToNextMds + " days.");
                    cond.setConditionEndDate(expNextMDSDate);
                    update(cond);

                    condEra.setConditionEraEndDate(expNextMDSDate);
                    update(condEra);
                    System.out.println("INFO: New end date: " + expNextMDSDateStr);
                }
                else if(cond.getConditionEndDate().before(expNextMDSDate) && expNextMDSDate.after(endDate)) {
                    System.out.println("INFO: The end date of an acute condition comes before the end of the observation period but after the current condition end date. However, the next expected MDS will occur  after the end date of the observation period. Extending the condition end date be the date of the observation period.");
                    cond.setConditionEndDate(endDate);
                    update(cond);
                    condEra.setConditionEraEndDate(endDate);
                    update(condEra);
                    System.out.println("INFO: New end date: " + endDateStr);
                }
                else
                    System.out.println("INFO: not changing the dates of the existing ACUTE condition");
	    }
	    else { // chronic conditions
		if(cond.getConditionEndDate().before(endDate)){
		    System.out.println("INFO: The end date of an NON-ACUTE condition comes before the end of the observation period. Extending the condition end date be the end date of the observation period: " + endDateStr);
		    cond.setConditionEndDate(endDate);
		    update(cond);
		    condEra.setConditionEraEndDate(endDate);
		    update(condEra);
		}
		else if(cond.getConditionEndDate().after(endDate)){
		    System.out.println("WARNING: Found a condition for which the end date was past the end of the observation period. Correcting the condition end date to be " + endDateStr);
		    cond.setConditionEndDate(endDate);
		    update(cond);
		    condEra.setConditionEraEndDate(endDate);
		    update(condEra);
		}
		else
		    System.out.println("INFO: not changing the dates of the existing CHRONIC condition");
	    }
	}
	else if((startDate.before(mdsProcDate) || startDate.equals(mdsProcDate)) && mdsProcDate.before(cond.getConditionStartDate())){
	    System.out.println("INFO: The start date of a existing condition (acute or chronic) comes after the MDS procedure date but within the same observation period.");
	    if(acute){
		if(expNextMDSDate.before(cond.getConditionStartDate())){
		    System.out.println("INFO: Acute condition and the next expected MDS precedes the start date of an existing condition. Creating a new condition era with start and end dates: " + mdsProcDateStr + " : " + expNextMDSDateStr);
		    ConditionOccurrence newCond = new ConditionOccurrence(personId, mdsProcDate, expNextMDSDate, conditionOccurrenceType, condId, mdsIcd, conditionSourceConceptId);
		    save(newCond);
		    cOccHash.put((String)newCond.getSourceConditionCode(), newCond);

		    ConditionEra newCondEra = new ConditionEra(mdsProcDate, expNextMDSDate, personId, condId, 1);
		    save(newCondEra);
		    cEraHash.put((Integer)newCondEra.getConditionConceptId(), newCondEra);

		}
		else {
		    System.out.println("INFO: Acute condition and the next expected MDS comes after start date of an existing condition. Moving the start date of the existing condition era back to the MDS procedure date.");
		    cond.setConditionStartDate(mdsProcDate);
		    update(cond);
		    condEra.setConditionEraStartDate(mdsProcDate);
		    update(condEra);
		    System.out.println("INFO: New start and end dates: " + mdsProcDateStr + " : " + cond.getConditionEndDate());
		}
	    }
	    else { // chronic condition
		System.out.println("INFO: Chronic condition. Moving the start date of the existing condition era back to the MDS procedure date.");
		cond.setConditionStartDate(mdsProcDate);
		update(cond);
		condEra.setConditionEraStartDate(mdsProcDate);
		update(condEra);
		System.out.println("INFO: New start and end dates: " + mdsProcDateStr + " : " + cond.getConditionEndDate());
	    }
	}
	else if(cond.getConditionEndDate().before(mdsProcDate) && (mdsProcDate.before(endDate) || mdsProcDate.equals(endDate))){
	    System.out.println("INFO: The end date of a condition (acute or chronic) comes before the MDS procedure date but within the same observation period.");
	    if(acute){
		if(expNextMDSDate.before(endDate)){
		    System.out.println("INFO: Acute condition and the next expected MDS precedes the end date of the observation period. Creating a new condition era with start and end dates: " + mdsProcDateStr + " : " + expNextMDSDateStr);
		    ConditionOccurrence newCond = new ConditionOccurrence(personId, mdsProcDate, expNextMDSDate, conditionOccurrenceType, condId, mdsIcd, conditionSourceConceptId);
		    save(newCond);
		    cOccHash.put((String)newCond.getSourceConditionCode(), newCond);

		    ConditionEra newCondEra = new ConditionEra(mdsProcDate, expNextMDSDate, personId, condId, 1);
		    save(newCondEra);
		    cEraHash.put((Integer)newCondEra.getConditionConceptId(), newCondEra);
		}
		else {
		    System.out.println("INFO: Acute condition and the next expected MDS comes after the end of the observation period Creating a new condition era with start and end dates: " + mdsProcDateStr + " : " + endDate);
		    ConditionOccurrence newCond = new ConditionOccurrence(personId, mdsProcDate, endDate, conditionOccurrenceType, condId, mdsIcd, conditionSourceConceptId);
		    save(newCond);
		    cOccHash.put((String)newCond.getSourceConditionCode(), newCond);

		    ConditionEra newCondEra = new ConditionEra(mdsProcDate, endDate, personId, condId, 1);
		    save(newCondEra);
		    cEraHash.put((Integer)newCondEra.getConditionConceptId(), newCondEra);
		}
	    }
	    else { // chronic condition
		System.out.println("WARNING: Chronic condition. Moving the end date of the existing condition era to the end of the observation period, but why wasn't this done when the first condition was created?");
		cond.setConditionEndDate(endDate);
		condEra.setConditionEraEndDate(endDate);
		System.out.println("INFO: New start and end dates: " + cond.getConditionStartDate() + " : " + cond.getConditionEndDate());
	    }
	}
    }
    else {
	System.out.println("INFO: No condition occurrence and era found in hashmap. Creating new ones.");

	Calendar firstDate;
	if(admit){
	    firstDate = startDate;
	    System.out.println("INFO: Admit MDS, using the observation period start date for the start of the condition.");
	}
	else{
	    firstDate = mdsProcDate;
	    System.out.println("INFO: Not an admit MDS, using the MDS procedure date for the start of the condition.");
	}
	String firstDateStr = sdf.format(firstDate.getTime());

	if(acute){
	    Calendar nxtDate;
	    if(expNextMDSDate.after(endDate)){
		nxtDate = endDate;
		System.out.println("INFO: using the end of the observation period for the end of the condition.");
	    }
	    else{
		nxtDate = expNextMDSDate;
		System.out.println("INFO: using the next expected MDS procedure data for the end of the condition.");
	    }
	    String nxtDateStr = sdf.format(nxtDate.getTime());

	    System.out.println("INFO: Acute condition and next MDS in " + daysToNextMds + " days. Creating a new condition era: " + firstDateStr + " : " + nxtDateStr);
	    ConditionOccurrence newCond = new ConditionOccurrence(personId, firstDate, nxtDate, conditionOccurrenceType, condId, mdsIcd, conditionSourceConceptId);
	    save(newCond);
	    cOccHash.put((String)newCond.getSourceConditionCode(), newCond);

	    ConditionEra newCondEra = new ConditionEra(firstDate, nxtDate, personId, condId, 1);
	    save(newCondEra);
	    cEraHash.put((Integer)newCondEra.getConditionConceptId(), newCondEra);
	}
	else { // chronic condition
	    System.out.println("INFO: Chronic condition. New condition: " + firstDateStr + " : " + endDateStr);
	    ConditionOccurrence newCond = new ConditionOccurrence(personId, firstDate, endDate, conditionOccurrenceType, condId, mdsIcd, conditionSourceConceptId);
	    save(newCond);
	    cOccHash.put((String)newCond.getSourceConditionCode(), newCond);

	    ConditionEra newCondEra = new ConditionEra(firstDate, endDate, personId, condId, 1);
	    save(newCondEra);
	    cEraHash.put((Integer)newCondEra.getConditionConceptId(), newCondEra);
	}
    }
}

private Person getPerson(Long personId){
    Person person = (Person)session.getCurrentSession().get(Person.class, personId);
    if ( person == null ){
	System.out.println("INFO (getPerson): No person found -  called with personId: " + personId);
	return null;
    }

    System.out.println("INFO (getPerson): Person found for personId: " + personId + ": Person: " + person);
    return person;
}

private Person addPerson(Long personId, Mds mdsRecord, int rowNum){
    Person person = (Person)session.getCurrentSession().get(Person.class, personId);
    System.out.println("personId: " + personId + "; Person: " + person);
    if ( person == null ){
	person = new Person();
	person.setPersonId(personId);
	if ( mdsRecord != null ){
	    String loc = mdsRecord.getLocation();
	    if (loc == null){
		System.out.println("INFO: mdsRecord.getlocation returned NULL. Not adding person using this MDS record (row number " + rowNum + ")");
		return null;
	    }
	    if (loc.equalsIgnoreCase("LOC_CANT"))
		person.setLocationId(1);
	    else if (loc.equalsIgnoreCase("LOC_HER"))
		person.setLocationId(2);
	    else if (loc.equalsIgnoreCase("LOC_CRAN"))
		person.setLocationId(3);
	    else if (loc.equalsIgnoreCase("LOC_SEN"))
		person.setLocationId(4);
	    else if (loc.equalsIgnoreCase("LOC_SUG"))
		person.setLocationId(5);
	    else {
		System.out.println("INFO: no VALID location, not adding person using this MDS record (row number " + rowNum + ")");
		return null;
	    }

	    // rim20: found that person table loaded genders as all male, deprecated this block
	    // person.setGenderSourceCode(mdsRecord.getGender());
	    // if ( "45878463".equalsIgnoreCase(mdsRecord.getGender()))
	    // 	person.setGenderCUI(8532);
	    // else
	    // 	person.setGenderCUI(8507);

	    person.setGenderSourceCode(mdsRecord.getGender());
	    if((mdsRecord.getGender()).equals("45878463")) 
		person.setGenderCUI(8532);
	    else
		person.setGenderCUI(8507);
	    
	    
	    Calendar birthDate = this.parseDate("MM-dd-yyyy", mdsRecord.getBirthdate());
	    person.setYearOfBirth(birthDate.get(Calendar.YEAR));

	    
	    person.setRaceSourceCode(mdsRecord.getRace());
	    if ( "black".equalsIgnoreCase(mdsRecord.getRace())) {
		person.setRaceCUI(8516);
		person.setEthnicityCUI(8516);
	    }
	    else if ( "white".equalsIgnoreCase(mdsRecord.getRace())) {
		person.setRaceCUI(8527);
		person.setEthnicityCUI(8527);
	    }
	    else if ("other".equalsIgnoreCase(mdsRecord.getRace())) {
		person.setRaceCUI(9117); // 'Other'
		person.setEthnicityCUI(9117);
	    }
	    else { 
		person.setRaceCUI(8552);
		person.setEthnicityCUI(8552);
		
	    }
	}
	save(person);
    }
    return person;
}

// private Long getConceptId(String string) {
//     List<Concept> concepts = (List<Concept>) session.getCurrentSession().createQuery("from Concept where conceptCode = '" + string + "'").list();
//     // return the first concept - this should be unique
//     if ( concepts.size() > 0)
// 	return concepts.get(0).getConceptId().longValue();
//     else {
// 	System.out.println("can't find concept: " + string);
// 	return null; // not found
//     }
// }

private Integer getConceptId(String string) {
    List<Integer> concepts = (List<Integer>) session.getCurrentSession().createQuery("select conceptId from Concept where conceptCode = '" + string + "'").list();
    // return the first concept - this should be unique
    if ( concepts.size() > 0)
	return concepts.get(0);
    else {
	System.out.println("can't find concept: " + string);
	return null; // not found
    }
}

// Hashmap specific to drug era
static Map<String, List<Integer>> drugEraHM = new HashMap<String, List<Integer>>();
static Map<String, Integer> dump = new HashMap<String, Integer>();
private void loadPharmOrdersAndEras(String pharmFile) {
    Integer nullCount = 0;
    // map the missing ingredients and add to drugEraHM
    try {
	FileReader fr = new FileReader("/home/rdb20/CDMv5repo/trunk/src/main/java/edu/pitt/dbmi/ohdsiv5/mapped-concept-ids.txt");
	BufferedReader br = new BufferedReader(fr);
	String line;
	while((line=br.readLine()) != null) {
	    List<Integer> mapList = new ArrayList<Integer>();
	    String[] chunk = line.split("\\s");
	    for(int i=1; i<chunk.length; i++) 
	    	mapList.add(Integer.parseInt(chunk[i]));
	    drugEraHM.put(chunk[0], mapList);
	}
	br.close();
    } catch(IOException e) {
	System.out.println("Error reading file: mapped-concept-ids.txt");
    }
    
    try {
	CSVReader reader = new CSVReader(new FileReader(pharmFile), '|');
	//reader.readNext(); // Skip the header
	//ptId|gpi|ndc|start|end|drug|form|strength|sig|prn|exposureCount

	String [] nextLine;
	int flushIdx = -1;

	// Acquiring the Transaction
	Session sess = session.getCurrentSession();

	while ((nextLine = reader.readNext()) != null) {
	    flushIdx += 1;

	    // TODO: fix the pharmOrders2DrugExposures.py script to
	    // accept a parameter that would shut off the writing of
	    // the header 
	    // (used to skip the line 
	    //    'outbuf = "ptId|gpi|ndc|start|end|drug|form|strength|sig|prn|exposureCount\n"'
	    //  ). 
	    // Then, this next line and the code that
	    // skips the first line of the data file (see above) would
	    // not be needed.
	    //if (nextLine[0].equalsIgnoreCase("ptId"))
	    //	continue;

	    // to keep performance reasonable with this large transaction
	    if (flushIdx != 0 && flushIdx % 10 == 0){
		System.out.println("FLUSHING AND CLEARING SESSION AT OBSERVATION_PERIOD RECORD " + flushIdx);
		sess.flush();
		sess.clear();
	    }
	    // FOR DEVELOPMENT AND DEBUGGING
	    // if (flushIdx == 1000)
	    // 	break;

	    System.out.println("INFO:" + nextLine.length);
	    System.out.flush();

	    if ( nextLine.length < 11 || nextLine[2] == null){
		System.out.println("Error reading Pharm File: " + nextLine);
		continue;
	    }
	    
	    Long personId = Long.valueOf(nextLine[0]);
	    if ( personId != null ){
		Person person = getPerson(personId);
		// If there is no person skip it
		if ( person == null ){
		    System.out.println("Error person not found: " + personId);
		    continue;
		}

		Calendar drugExposureStartDate = parseDate("yyyy-MM-dd", nextLine[3]);
		Calendar drugExposureEndDate = parseDate("yyyy-MM-dd", nextLine[4]);
		String drugSourceValue = nextLine[1];
		Integer drugSourceConceptId = null;
		if (hm.containsKey(drugSourceValue))
		    drugSourceConceptId = (Integer)hm.get(drugSourceValue);
		else {
		    drugSourceConceptId = getConceptId(drugSourceValue);
		    if(drugSourceConceptId != null)
			hm.put(drugSourceValue, drugSourceConceptId);
		}
		    
		Integer drugConceptId = null;
    		if (hm.containsKey(Integer.toString(drugSourceConceptId)))
		    drugConceptId = (Integer)hm.get(Integer.toString(drugSourceConceptId));
		else{
		    drugConceptId = getConceptId2(drugSourceConceptId);
		    if ( drugConceptId != null )
			hm.put(Integer.toString(drugSourceConceptId), drugConceptId);
		}
		if ( drugConceptId == null ){
		    System.out.println("INFO: Null Condition drug id: " + drugSourceValue+ ". Not able to put this pharm order in DRUG_EXPOSURE or DRUG_ERA");
		    continue;
		}

		//filter out drug exposure that have start date after end date 
		if (drugExposureStartDate.after(drugExposureEndDate)){
		    // System.out.println("DEBUG: drug exposure start date after end date --- continue");
		    continue;
		}

		//filter out drug exposure with date that not fits in any person's observation period
		if (createMapForAllObsPeriods().containsKey(personId)){
		    boolean boo = true;
		    for (ObservationPeriod op: createMapForAllObsPeriods().get(personId)){
			// System.out.println("DEBUG:" + drugExposureStartDate.getTime().toString() + "|" + op.getObservationPeriodStartDate().getTime().toString() + "|" + op.getObservationPeriodEndDate().getTime().toString());

			if ((drugExposureStartDate.after(op.getObservationPeriodStartDate()) || drugExposureStartDate.equals(op.getObservationPeriodStartDate())) 
			    && (drugExposureEndDate.equals(op.getObservationPeriodEndDate()) || drugExposureEndDate.before(op.getObservationPeriodEndDate())))
			    boo = false;
		    }
		    if (boo){
			// System.out.println("DEBUG: drug exposure not fits in any observation periods --- continue");
			continue;
		    }
		} else {
		    // System.out.println("DEBUG: current person's observation period not found --- continue");
		    continue;
		}
		

		Integer drugExposureTypeConceptId = 38000175; // a prescription dispensed in the pharmacy
		Integer drugTypeConceptId = 38000181; // drug era 0 day persistence window (we pre-compute the exposure windows in this dataset)
		String stopReason = null;
		Short refills = null;
		Integer drugQuantity = null;
		Short daysSupply = null;
		String sig = nextLine[8];
		// tag on the PRN string if needed - it's the only way
		// we will know that the drug is PRN
		if (nextLine[9].equals("True")){
		    sig = sig + " (PRN)";
		}
		Integer routeConceptId = null;
		Integer effectiveDrugDose = null;
		Integer doseUnitConceptId = null;
		String lotNumber = null;
		Integer providerId = null;
		Integer visitOccurrence = null;
		String routeSourceValue = null;
		String doseUnitSourceValue = null;
	     
		DrugExposure drugExposure = new DrugExposure(drugExposureStartDate, drugExposureEndDate, personId, drugConceptId, drugExposureTypeConceptId, stopReason, refills, drugQuantity, daysSupply, sig, routeConceptId, effectiveDrugDose, doseUnitConceptId, lotNumber, providerId, visitOccurrence, drugSourceValue, drugSourceConceptId, routeSourceValue, doseUnitSourceValue);

		drugExposure.setDrugSourceConceptId(drugSourceConceptId);
		save(drugExposure);

		// Drug era
		List<Integer> ingredientList = new ArrayList<Integer>();
		if(drugEraHM.containsKey(Integer.toString(drugConceptId)))
		    ingredientList = drugEraHM.get(Integer.toString(drugConceptId));
		else {
		    ingredientList = getIngredientConceptId(drugConceptId);
		    if(!ingredientList.isEmpty())
			drugEraHM.put(Integer.toString(drugConceptId), ingredientList);
		    else {
			System.out.println("ERROR: Empty IngredientList at drugConceptId: " + drugConceptId);
			nullCount++;
		    }
		}
		if (!ingredientList.isEmpty()) {
		    for (Integer ingredient: ingredientList) {
			DrugEra drugEra = new DrugEra(drugExposureStartDate, personId, drugExposureEndDate, ingredient, null, null);
			save(drugEra);
		    }	    
		}
	    }
	}
    } catch (FileNotFoundException e) {
	// TODO implement logger
	e.printStackTrace();
    } catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
    }
    System.out.println("nullCount: " + nullCount);
    // // Print the list of drugs with missing ingredient mapping
    // System.out.println("Printing drugEraHM");
    // for (String drug : drugEraHM.keySet())
    // 	System.out.println(drug + ":" + drugEraHM.get(drug));
}
    
private Long getConceptId2(Long conceptId1) {
    Long conceptId2 = null;
    String query = "";
    Session sess = session.getCurrentSession();
    query = "select c.conceptId2 from ConceptRelationship c where c.relationshipId = 'Maps to' and c.conceptId1 = " + conceptId1;
    List<Long>results = (List<Long>)sess.createQuery(query).list();
    conceptId2 = results.get(0);
    return conceptId2;
}
    
private Integer getDrugConceptId(String string) {
    Integer conceptId = null;
    // Using the GPI get the common concept code
    Session sess = session.getCurrentSession();
    /** Starting the Transaction */
    String query = "from Concept c where c.conceptCode = '"+ string +"'";
    List<Concept> tmp = (List<Concept>)sess.createQuery(query).list();
    Integer tmpConceptId1 = tmp.get(0).getConceptId();
    String query2 = "from ConceptRelationship cr where cr.relationshipId = 'Maps to' and cr.conceptId1 = "+ tmpConceptId1;
    List<ConceptRelationship> results = (List<ConceptRelationship>)sess.createQuery(query2).list();
    if ( results.size() > 0 )
	conceptId = results.get(0).getConceptId2();
    return conceptId;
}

private Integer getConceptId2(Integer conceptId1) {
    Integer conceptId2 = null;
    Session sess = session.getCurrentSession();
    String query = "select c.conceptId2 from ConceptRelationship c where c.relationshipId = 'Maps to' and c.conceptId1 = " + conceptId1;
    List<Integer>results = (List<Integer>)sess.createQuery(query).list();
    if(results.size() > 0)
	conceptId2 = results.get(0);
    return conceptId2;
}

private Integer getConditionConceptId(String string) {
    Integer conceptId = null;
    Session sess = session.getCurrentSession();
    String query = "";
    /** Starting the Transaction */
    query = "select c.conceptId from Concept c where c.conceptCode='"+string+"' and domainId='Condition' and vocabularyId in ('ICD10', 'ICD9CM', 'ICD10CM', 'Read', 'SNOMED', 'CIEL', 'MESH', 'OXMIS')";
    List<Integer> tmp = (List<Integer>)sess.createQuery(query).list();
    if(tmp.size() > 0)
	conceptId = tmp.get(0);
    return conceptId;
}
    
    // the conceptId should be the descendant_concept_id to
    // find the ancestor_concept_id which is the ingredient concept id
    // for drug_era schema
private List<Integer> getIngredientConceptId(Integer conceptId) {
    Session sess = session.getCurrentSession();
    String query = "select c.conceptId from Concept c, ConceptAncestor ca where c.conceptId = ca.ancestorConceptId and c.conceptClassId = 'Ingredient' and ca.descendantConceptId = " + conceptId;
    List<Integer> rslt = new ArrayList<Integer>();
    rslt = (List<Integer>)sess.createQuery(query).list();
    if(rslt.size() < 1)
	rslt.add(1);
    return rslt;
}

public void openDbSession() {
    session.openSession();
}

public void closeDbSession() {
    session.close();
}


private void loadObsPeriods(String obsPeriodsFile) {

    try {
	CSVReader reader = new CSVReader(new FileReader(obsPeriodsFile), '\t');
	String [] nextLine;
	int flushIdx = -1;

	// Acquiring the Transaction
	Session sess = session.getCurrentSession();

	while ((nextLine = reader.readNext()) != null) {
	    flushIdx += 1;

	    // to keep performance reasonable with this large transaction
	    if (flushIdx != 0 && flushIdx % 10 == 0){
		System.out.println("FLUSHING AND CLEARING SESSION AT OBSERVATION_PERIOD RECORD " + flushIdx);
		sess.flush();
		sess.clear();
	    }

	    if ( nextLine.length < 3 || nextLine[2] == null ) {
		System.out.println("Error reading OBSERVATION_PERIOD Record: " + nextLine);
		continue;
	    }

	    // This data is de-id - no translation necessary
	    Long personId = Long.valueOf(nextLine[0]);
	    Long periodTypeConceptId = Long.valueOf(35124300);
	    if ( personId != null ){
		Person person = getPerson(personId);
		// If there is no person skip it
		if ( person == null )
		    continue;
		Calendar observationPeriodStartDate = parseDate("yyyy-MM-dd", nextLine[1]);
		Calendar observationPeriodEndDate = parseDate("yyyy-MM-dd", nextLine[2]);
		ObservationPeriod obsPer = new ObservationPeriod(observationPeriodStartDate, observationPeriodEndDate, personId, periodTypeConceptId);
		save(obsPer);
	    }
	}

    } catch (FileNotFoundException e) {
	e.printStackTrace();
    } catch (IOException e) {
	e.printStackTrace();
    }
}

    // create a hash mappings of person_id and list of his/her obs periods
    private static HashMap<Long, ArrayList<ObservationPeriod>> createMapForAllObsPeriods(){

	if (obsPeriodMap != null){
	    return obsPeriodMap;  
	} else {
	    // System.out.println("DEBUG: QUERY TO CREATE obsPeriodMap *********");
	    Session sess = session.getCurrentSession();
	    List<ObservationPeriod> obsPeriod = (List<ObservationPeriod>) sess.createQuery("FROM ObservationPeriod").list(); 
	    obsPeriodMap = new HashMap<Long, ArrayList<ObservationPeriod>>();
	    
	    for (ObservationPeriod obs: obsPeriod){
		
		if (obsPeriodMap.containsKey(obs.getPersonId())){
		    obsPeriodMap.get(obs.getPersonId()).add(obs);
		}
		else{
		    ArrayList<ObservationPeriod> personPeriods = new ArrayList<ObservationPeriod>();
		    personPeriods.add(obs);
		    obsPeriodMap.put(obs.getPersonId(), personPeriods);
		}			       
	    }

	    for (Map.Entry<Long, ArrayList<ObservationPeriod>> entry : obsPeriodMap.entrySet()) {
		String key = entry.getKey().toString();
		ArrayList<ObservationPeriod> value = entry.getValue();
		System.out.println("key, " + key + "| value size is: " + value.size());
	    }

	    // System.out.println("DEBUG: created obsPeriodMap - size" + obsPeriodMap.size());
	    return obsPeriodMap;
	}
	
    }


@SuppressWarnings("unchecked")
    // TODO: The MDS interface should make it so that we only need one loadProceduresFromMDS method rather than two
    private void loadProceduresFromMDS3() {
    // Get all the MDS3 Records currently loaded into the
    // DB and then loop through and save MDS record type to PROCEDURE_OCCURRENCE

    System.out.println("DEBUG: begin load procedures from mds3");

    // Acquiring the Transaction
    Session sess = session.getCurrentSession();

    List<Mds3> mdsRecords = (List<Mds3>) sess.createQuery("FROM Mds3 ORDER BY PATIENTID ASC").list();

    if ( mdsRecords != null){

	int flushIdx = -1;
	int ctr = 0;
	for ( Mds3 mdsRecord : mdsRecords){
	    flushIdx += 1;
	    ctr += 1;

	    if ( mdsRecord == null )
		continue;

	    // to keep performance reasonable with this large transaction
	    if (flushIdx != 0 && flushIdx % 10 == 0){
		System.out.println("FLUSHING AND CLEARING SESSION AT MDS RECORD " + flushIdx);
		sess.flush();
		sess.clear();
	    }

	    //FOR DEVELOPMENT AND DEBUGGING
	    // if (flushIdx == 21)
	    // 	break;
	    // if (mdsRecord.getPatientId() == "5278") // MIGHT NOT WORK
	    // 	break;
	    // if (ctr == 30)
	    // 	break;

	    Long personId =  Long.parseLong(mdsRecord.getPatientId());
	    Person person = getPerson(personId);
	    // If there is no person skip it
	    if ( person == null )
		continue;

	    Calendar procedureDate = parseDate("MM-dd-yyyy", mdsRecord.getDateOfEvent());

	    // if procedure date not fits in any this person's observation periods
	    // skip this procedure during loading
	 
	    if (createMapForAllObsPeriods().containsKey(personId)){
		boolean notPlacedInObsPer = true;
		for (ObservationPeriod op: createMapForAllObsPeriods().get(personId)){
		    if ((procedureDate.after(op.getObservationPeriodStartDate()) || procedureDate.equals(op.getObservationPeriodStartDate())) 
			&& (procedureDate.before(op.getObservationPeriodEndDate()) || procedureDate.equals(op.getObservationPeriodEndDate())))
			notPlacedInObsPer = false;
		}
		if (notPlacedInObsPer){
		    System.out.println("WARNING: MDS 3 record cannot be placed within one of the patient's observation periods. Patient: " + personId + " MDS 3 date of event: " + mdsRecord.getDateOfEvent());
		    continue;
		}
	    } else {
		System.out.println("WARNING: MDS 3 record persondId cannot be found in the list of personIds for patients with observation periods. Patient: " + personId + " MDS 3 date of event: " + mdsRecord.getDateOfEvent());
		continue;
	    }

	    String examType = mdsRecord.getExamType();
	    Integer procedureTypeConceptId = 4033224; // SNOMED Administrative Procedure type

	    Integer procedureConceptId;
	    if (examType.equalsIgnoreCase("M3A"))
		procedureConceptId = 4029703; //SNOMED (SNOMED: Evaluation AND/OR management - new patient)
	    else
		procedureConceptId = 4030170; // (SNOMED:108221006 - Evaluation AND/OR management - established patient)

	    ProcedureOccurrence procOcc = new ProcedureOccurrence(personId, procedureDate, procedureConceptId, procedureTypeConceptId, examType);
	    save(procOcc);
	}
    }
}





@SuppressWarnings("unchecked")
    // TODO: The MDS interface should make it so that we only need one loadProceduresFromMDS method rather than two
    private void loadProceduresFromMDS2() {
    // Get all the MDS 2 Records currently loaded into the
    // DB and then loop through and save MDS record type to PROCEDURE_OCCURRENCE

    System.out.println("DEBUG: begin load procedures from mds2");

    // Acquiring the Transaction
    Session sess = session.getCurrentSession();

    List<Mds2> mdsRecords = (List<Mds2>) sess.createQuery("FROM Mds2 ORDER BY PATIENTID ASC").list();

    if ( mdsRecords != null){
	int flushIdx = -1;
	int ctr = 0;
	for ( Mds2 mdsRecord : mdsRecords){
	    flushIdx += 1;
	    ctr += 1;

	    if ( mdsRecord == null )
		continue;

	    // to keep performance reasonable with this large transaction
	    if (flushIdx % 10 == 0){
		System.out.println("FLUSHING AND CLEARING SESSION AT MDS RECORD " + flushIdx);
		sess.flush();
		sess.clear();
	    }

	    // FOR DEVELOPMENT AND DEBUGGING
	    // if (flushIdx == 21)
	    // 	break;
	    // if (mdsRecord.getPatientId() == "5278") // MIGHT NOT WORK
	    // 	break;
	    // if (ctr == 30)
	    // 	break;

	    Long personId =  Long.parseLong(mdsRecord.getPatientId());
	    Person person = getPerson(personId);
	    // If there is no person skip it
	    if ( person == null )
		continue;

	    Calendar procedureDate = parseDate("MM-dd-yyyy", mdsRecord.getDateOfEvent());
	    String examType = mdsRecord.getExamType();
	    Integer procedureTypeConceptId = 4033224; // SNOMED Administrative Procedure type

	    if (createMapForAllObsPeriods().containsKey(personId)){
		    boolean boo = true;
		    for (ObservationPeriod op: createMapForAllObsPeriods().get(personId)){
			if ((procedureDate.after(op.getObservationPeriodStartDate()) || procedureDate.equals(op.getObservationPeriodStartDate())) 
			    && (procedureDate.before(op.getObservationPeriodEndDate()) || procedureDate.equals(op.getObservationPeriodEndDate())))
			    boo = false;
		    }
		    if (boo)
			continue;
	    } else {
		continue;
	    }

	    Integer procedureConceptId;
	    if (examType.equalsIgnoreCase("M2A"))
		procedureConceptId = 4029703; //SNOMED (SNOMED: Evaluation AND/OR management - new patient)
	    else
		procedureConceptId = 4030170; // (SNOMED:108221006 - Evaluation AND/OR management - established patient)

	    ProcedureOccurrence procOcc = new ProcedureOccurrence(personId, procedureDate, procedureConceptId, procedureTypeConceptId, examType);

	    save(procOcc);
	}
    }
}


@SuppressWarnings("unchecked")
    private void loadObservationsFromMDS3() {
    // Get all the MDS3 Records currently loaded into the
    // DB and then loop through and save MDS record types to OBSERVATION
    //
    // NOTE: a convenient query to examine samples of loaded data:
    //    SELECT PERSON_ID, OBSERVATION_DATE, OBSERVATION_CONCEPT_ID, OBSERVATION_SOURCE_VALUE, VALUE_AS_CONCEPT_ID, VALUE_AS_NUMBER, VALUE_AS_STRING FROM OBSERVATION WHERE PERSON_ID = 10000 ORDER by OBSERVATION_DATE;
  
    // Acquiring the Transaction
    Session sess = session.getCurrentSession();

    List<Mds3> mdsRecords = (List<Mds3>) sess.createQuery("FROM Mds3 ORDER BY PATIENTID ASC").list();

    if ( mdsRecords != null){
	int flushIdx = -1;
	int ctr = 0;
	for ( Mds3 mdsRecord : mdsRecords){
	    flushIdx += 1;
	    ctr += 1;

	    if ( mdsRecord == null )
		continue;

	    // to keep performance reasonable with this large transaction
	    if (flushIdx % 10 == 9){
		System.out.println("FLUSHING AND CLEARING SESSION AT MDS RECORD " + flushIdx);
		sess.flush();
		sess.clear();
	    }

	    // FOR DEVELOPMENT AND DEBUGGING
	    // if (flushIdx == 20)
	    // 	break;
	    // if (mdsRecord.getPatientId() == "5278") // MIGHT NOT WORK
	    //   break;
	    // if (ctr == 11)
	    //   break;

	    Integer obsType = 38000280; // Observation recorded from EHR

	    Calendar obsDate = this.parseDate("MM-dd-yyyy", mdsRecord.getDateOfEvent());
	    Long personId =  Long.parseLong(mdsRecord.getPatientId());
	    Person person = getPerson(personId);
	    // If there is no person skip it
	    if ( person == null )
                continue;
	    
	    /////////////////////////////////////
	    // MDS OBSERVATION DATA 
	    /////////////////////////////////////
	    
	    //Note: We reviewed the report to HHS titled
	    //"Opportunities for Engaging Long-Term and Post-Acute
	    //Care Providers in Health Information Exchange
	    //Activities: Exchanging Interoperable Patient Assessment
	    //Information"
	    //(http://aspe.hhs.gov/daltcp/reports/2011/StratEng.htm). This
	    //report includes a detailed mapping of MDS 3.0 and OASIS
	    //to LOINC and SNOMED. Where possible, we used the
	    //mappings provided by the OMOP Standard Vocabulary (see
	    //http://dbmi-icode-01.dbmi.pitt.edu/svn/GeriOMOP/docs/MDS-3.0-in-MARS/MDS3-in-LOINC-and-OMOP-SV4.csv)
	    //and this report in this dataset. In general, LOINC
	    //encodes the "test" (in this case a MDS question or data
	    //field) and SNOMED encodes the value. Wherever there was
	    //a simple and complete match between the LOINC question
	    //and SNOMED values, the latter were used in
	    //'value_as_concept_id'. However, there are many cases
	    //where there is no SNOMED for one or more of the possible
	    //answers (especially for answers that include 'No'). In
	    //these cases, we used 'value_as_string' or
	    //'value_as_number' as appropriate.

	    // NOTE: Nursing Home Compare measures refer to: http://www.cms.gov/Medicare/Quality-Initiatives-Patient-Assessment-Instruments/NursingHomeQualityInits/NHQIQualityMeasures.html.


	    //// MDS SHORT STAY OR LONG STAY
	    // NOTE: To make sure that all quality measure for NH
	    // Compare are in the dataset, we added short-stay and
	    // long-stay distinctions to the MDS3 table. Specifically,
	    // the codes in A0310A and A0310B were used. Short-stay
	    // residents are indicated by a value of 99 for A0310A and
	    // values other than 99 in A0310B. Long stay residents are
	    // indicated by a value other than 99 in A0310A and 99 for
	    // A0310B. Its possible to have values other than 99 for
	    // both A0310A and A0310B. This would indicate a patient
	    // who is transitioning from short-stay to long-stay.
	    String pps_assess = mdsRecord.getPpsassessment();
	    String federal_assess = mdsRecord.getFederalObraAssessment();
	    String stay = "None";

	    // Entry or discharge report
	    if (mdsRecord.getEntrydischargereport() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_ENTRY_DISCHARGE_REPORT",
						  Long.valueOf(mdsRecord.getEntrydischargereport()), obsType, obsDate, false, 40761209);
	    	save(obs);
	    }

	    // Entered from (enteredForm, because of typo)
	    if (mdsRecord.getEnteredform() != null){
		Observation obs = new Observation(person.getPersonId(), "MDS_ENTERED_FROM", 
						  Long.valueOf(mdsRecord.getEnteredform()), obsType, obsDate, false, 40757722);
		save(obs);
	    }
	    
	    //////////////////////////////////////////////////////////////////////////////////////////
	    // Enter the actual value of the OBRA or PPS assessments
	    // and also the inferred resident type (short or long
	    // stay). Note, this is not the same as the CMS definition
	    // of short or long stay residents which looks at days in
	    // facility across multiple stays within a single
	    // 'episode' (see the MDS 3 QM User Manual)
	    //////////////////////////////////////////////////////////////////////////////////////////
	    if (pps_assess != null){
		Observation obs = new Observation(person.getPersonId(), "MDS_PPS_ASSESSMENT", 
						  Long.valueOf(pps_assess), obsType, obsDate, false, 40757715);
		save(obs);
	    }
	    
	    if (federal_assess != null){
		Observation obs = new Observation(person.getPersonId(), "MDS_FEDERAL_ASSESSMENT", 
						  Long.valueOf(federal_assess), obsType, obsDate, false, 40757714);
		save(obs);
	    }

	    if (pps_assess != null && federal_assess != null){

	    	if ((!pps_assess.equals("45879840")) && (federal_assess.equals("45879840"))){ 
	    	    Observation obs = new Observation(person.getPersonId(), "MDS_SHORT_STAY", 
	    					      Long.valueOf(pps_assess), obsType, obsDate, false, 0);		    
	    	    save(obs);
	    	}
	    	else if ((pps_assess.equals("45879840")) && (!federal_assess.equals("45879840"))){ 
	    	    Observation obs = new Observation(person.getPersonId(), "MDS_LONG_STAY", 
	    					      Long.valueOf(federal_assess), obsType, obsDate, false, 0);

	    	    save(obs);	
	    	}
	    	else if ((!pps_assess.equals("45879840")) && (!federal_assess.equals("45879840"))){ 
	    	    Observation obs = new Observation(person.getPersonId(), "MDS_STAY_TRANS_SHORT_TO_LONG", 
	    					      "transferring", obsType, obsDate, false, 0);
	    	    save(obs);	
	    	}
	    } // END OF MDS SHORT STAY OR LONG STAY


	    // MDS 3 DIRECT AND  AGGREGATED VALUES
	    // NOTE: Custom codes usually have no mapping to the CDM and will be assigned a concept id of "0". See the README for further explanation.

	    // MDS PHQ-9 Depression screening : MDS_PHQ9_SCALE (source GeriOmop)
	    // NOTE: as of 12/31/2014 - this is replaced by DepressionNHCompare because the original method for
	    // handling this data (see mds3script.py) seems in error
	    // if (mdsRecord.getDepressionScale() != null){
	    // 	Observation obs = new Observation(person.getPersonId(), "MDS_PHQ9_SCALE",
	    // 					  mdsRecord.getDepressionScale(), obsType, obsDate, false, 40757808);
	    // 	save(obs);
	    // }
	    if (mdsRecord.getDepressionNHCompare() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_DEPRESSION_NH_COMPARE", mdsRecord.getDepressionNHCompare(), obsType, obsDate, false, 0);
	    	save(obs);
	    }

	    // MDS Cognitive Performance Scale : MDS_COGNITIVE_SCALE (source GeriOmop)
	    if (mdsRecord.getCognitiveskills() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_COGNITIVE_SCALE",
	    					  Long.valueOf(mdsRecord.getCognitiveskills()), obsType, obsDate, false, 40757755);
	    	save(obs);
	    }

	    // MDS Brief Interview of Mental Status : MDS_BIMS_SUMMARY_RANKING (source GeriOmop)
	    if (mdsRecord.getBimssum() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_BIMS_SUMMARY_RANKING",
	    			      mdsRecord.getBimssum(), obsType, obsDate, false, 40757745);
	    	save(obs);
	    }

	    // ADLs Function Status : ADL_FUNCTIONAL_STATUS  (source GeriOmop)
	    // Note that ADLs functional status is calculated from
	    // several MDS 3 entries and so does not have matching
	    // LOINC concept
	    if (mdsRecord.getAdldependence() != null){
	    	Observation obs = new Observation(person.getPersonId(), "ADL_FUNCT_STATUS",
	    					  null,  obsType, mdsRecord.getAdldependence().doubleValue(), obsDate, false);
	    	save(obs);
	    }  

	    // MDS delirium screening : MDS_DELIRIUM_SCALE (source GeriOmop)
	    if (mdsRecord.getDelirium() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_DELIRIUM_SCALE",
	    					  Long.valueOf(mdsRecord.getDelirium()), obsType, obsDate, false, 40757758);
	    	save(obs);
	    }

	    // MDS behavioral symptoms : MDS_BEHAVIORAL_SYMPTOMS and MDS_BEHAVIORAL_SYMPTOMS_TO_OTHERS (source GeriOmop)
	    if (mdsRecord.getBehavioralsymptoms() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_BEHAVIORAL_SYMPTOMS",
	    					  Long.valueOf(mdsRecord.getBehavioralsymptoms()), obsType, obsDate, false, 40757816);
	    	save(obs);
	    }
	    if (mdsRecord.getBehavioralSymptomsToOthers() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_BEHAVIORAL_SYMPTOMS_TO_OTHERS", 
	    					  Long.valueOf(mdsRecord.getBehavioralSymptomsToOthers()), obsType, obsDate, false, 0);
	    	save(obs);
	    }

	    // MDS falls since last admission : MDS_FALLS_SINCE_ADMISSION (source GeriOmop)
	    if (mdsRecord.getFallssinceadmission() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_FALLS_SINCE_ADMISSION",
	    			      Long.valueOf(mdsRecord.getFallssinceadmission()), obsType, obsDate, false, 40757984);
	    	save(obs);
	    }

	    // MDS falls since last month : MDS_FALLS_SINCE_LAST_MONTH (source GeriOmop)
	    if (mdsRecord.getFallsonemonthago() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_FALL_LAST_MNTH",
	    			      Long.valueOf(mdsRecord.getFallsonemonthago()), obsType, obsDate, false, 40757981);
	    	save(obs);
	    }

	    // MDS falls 2 TO 6 months ago : MDS_FALLS_SINCE_TWO_TO_SIX_MONTHS (source GeriOmop)
	    if (mdsRecord.getFallstwotosixmonths() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_FALL_2_TO_6_MNTH",
	    			      Long.valueOf(mdsRecord.getFallstwotosixmonths()), obsType, obsDate, false, 40757981);
	    	save(obs);
	    }
	    
	    //   - Percent of Residents Experiencing One or More Falls with Major Injury (see MDS3 J1900C and LOINC 40757988 Number of falls since admission or prior assessment - major injury MDSv3)
	    if (mdsRecord.getFallsmajorinjury() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_FALLS_MAJOR_INJURY",
	    					  Long.valueOf(mdsRecord.getFallsmajorinjury()), obsType, obsDate, false, 40757988);
	    	save(obs);
	    }

	    // MDS falls with and without injury
	    if (mdsRecord.getFallsminorinjury() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_FALLS_MINOR_INJURY",
	    					  Long.valueOf(mdsRecord.getFallsminorinjury()), obsType, obsDate, false, 40757987);
	    	save(obs);
	    }
	    if (mdsRecord.getFallswithnoinjury() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_FALLS_NO_INJURY", 
	    					  Long.valueOf(mdsRecord.getFallswithnoinjury()), obsType, obsDate, false, 10757986);
	    	save(obs);
	    }

	    // MDS Pain assessments
	    if (mdsRecord.getConductPainAssessmentInterv() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_CONDUCT_PAIN_ASSESSMENT_INTERVIEW", 
	    					  Long.valueOf(mdsRecord.getConductPainAssessmentInterv()), obsType, obsDate, false, 40757959);
	    	save(obs);
	    }	    
	    if (mdsRecord.getPainInLast5Days() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_PAIN_LAST_5_DAYS", 
	    					  Long.valueOf(mdsRecord.getPainInLast5Days()), obsType, obsDate, false, 40757960);
	    	save(obs);
	    }
	    if (mdsRecord.getPainFrequencyLast5Days() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_PAIN_FREQ_LAST_5_DAYS",
	    			      Long.valueOf(mdsRecord.getPainFrequencyLast5Days()), obsType, obsDate, false, 40757961);
	    	save(obs);
	    }
	    if (mdsRecord.getPainIntensity() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_PAIN_INTENSITY",Long.valueOf(mdsRecord.getPainIntensity()), obsType, obsDate, false, 0);
	    	save(obs);	
	    }	    
	    if (mdsRecord.getPainNonVerbal() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_PAIN_NON_VERBAL", mdsRecord.getPainNonVerbal(), obsType, obsDate, false, 0);
	    	save(obs);	
	    }
	    if (mdsRecord.getPainMedicationRegimen() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_PAIN_MEDICATION_REGIMEN", 
	    					  Long.valueOf(mdsRecord.getPainMedicationRegimen()), obsType, obsDate, false, 40757956);
	    	save(obs);
	    }
	    if (mdsRecord.getReceivedPRNPainMedication() != null){ 
	    	Observation obs = new Observation(person.getPersonId(), "MDS_RECEIVED_PRN_PAIN_MEDICATION", 
	    					  Long.valueOf(mdsRecord.getReceivedPRNPainMedication()), obsType, obsDate, false, 40757957);
	    	save(obs);
	    }
	    if (mdsRecord.getReceivedNonMedicationForPain() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_RECEIVED_PAIN_TX_NON_PHARM", 
	    					  Long.valueOf(mdsRecord.getReceivedNonMedicationForPain()), obsType, obsDate, false, 40757958);
	    	save(obs);
	    }

	    // MDS bed and transfer mobility
	    if (mdsRecord.getImpairedBedMobility() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_IMPAIRED_MOBILITY", Long.valueOf(mdsRecord.getImpairedBedMobility()), obsType, obsDate, false, 3044661);
	    	save(obs);
	    }
	    if (mdsRecord.getImpairedTransfer() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_IMPAIRED_TRANSFER", Long.valueOf(mdsRecord.getImpairedTransfer()), obsType, obsDate, false, 3045316);
	    	save(obs);
	    }

	    // MDS various medications of interest
	    if (mdsRecord.getAntipsychoticMedication() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_ANTIPSYCHOTIC_MEDICATION", 
	    					  Long.valueOf(mdsRecord.getAntipsychoticMedication()), obsType, obsDate, false, 40758114);
	    	save(obs);
	    }
	    if (mdsRecord.getAntianxietyMedication() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_ANTIANXIETY_MEDICATION", 
	    					  Long.valueOf(mdsRecord.getAntianxietyMedication()), obsType, obsDate, false, 40758115);
	    	save(obs);
	    }
	    if (mdsRecord.getAntidepressantMedication() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_ANTIDEPRESSANT_MEDICATION", 
	    					  Long.valueOf(mdsRecord.getAntidepressantMedication()), obsType, obsDate, false, 40758116);
	    	save(obs);
	    }
	    if (mdsRecord.getHypnoticMedication() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_HYPNOTIC_MEDICATION", 
	    					  Long.valueOf(mdsRecord.getHypnoticMedication()), obsType, obsDate, false, 40758117);
	    	save(obs);
	    }
	    if (mdsRecord.getAnticoagulantMedication() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_ANTICOAGULANT_MEDICATION", 
	    					  Long.valueOf(mdsRecord.getAnticoagulantMedication()), obsType, obsDate, false, 40758118);
	    	save(obs);
	    }
	    if (mdsRecord.getAntibioticMedication() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_ANTIBIOTIC_MEDICATION", 
	    					  Long.valueOf(mdsRecord.getAntibioticMedication()), obsType, obsDate, false, 40761230);
	    	save(obs);
	    }	 
	    if (mdsRecord.getDiureticMedication() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_DIURETIC_MEDICATION", 
	    					  Long.valueOf(mdsRecord.getDiureticMedication()), obsType, obsDate, false, 40761231);
	    	save(obs);
	    }
   
	    ////////////////////////////////////
	    // MDS CONDITIONS OF INTEREST

	    // MDS malnutrition
	    if (mdsRecord.getMalnutrition() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_MALNUTRITION", 
	    					  Long.valueOf(mdsRecord.getMalnutrition()), obsType, obsDate, false, 40757947);
	    	save(obs);
	    }

	    // MDS risk of pressur ulcer and various stages of pressure ulcer 
	    if (mdsRecord.getRiskOfPressureUlcers() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_RISK_OF_PRESSURE_ULCERS", 
	    					  Long.valueOf(mdsRecord.getRiskOfPressureUlcers()), obsType, obsDate, false, 40758013);
	    	save(obs);
	    }
	    if (mdsRecord.getPressureUlcerStage1() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_PRESSURE_ULCER_STAGE_1", Long.valueOf(mdsRecord.getPressureUlcerStage1()), obsType, obsDate, false, 40758015);
	    	save(obs);
	    }
	    if (mdsRecord.getPressureUlcerStage2() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_PRESSURE_ULCER_STAGE_2", Long.valueOf(mdsRecord.getPressureUlcerStage2()), obsType, obsDate, false, 40758254);
	    	save(obs);
	    }
	    if (mdsRecord.getPressureUlcerStage3() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_PRESSURE_ULCER_STAGE_3", Long.valueOf(mdsRecord.getPressureUlcerStage3()), obsType, obsDate, false, 40758255);
	    	save(obs);
	    }
	    if (mdsRecord.getPressureUlcerStage4() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_PRESSURE_ULCER_STAGE_4", Long.valueOf(mdsRecord.getPressureUlcerStage4()), obsType, obsDate, false, 40758256);
	    	save(obs);
	    }

	    // MDS weight loss
	    if (mdsRecord.getExcessiveWeightLoss() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_EXCESSIVE_WEIGHT_LOSS", 
	    					  Long.valueOf(mdsRecord.getExcessiveWeightLoss()), obsType, obsDate, false, 40757994);
	    	save(obs);
	    }
    
	    // MDS conditions that should only ever be temporary
	    if (mdsRecord.getFever() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_FEVER", Long.valueOf(mdsRecord.getFever()), obsType, obsDate, false, 3045275);
	    	save(obs);
	    }
	    if (mdsRecord.getVomiting() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_VOMITING", Long.valueOf(mdsRecord.getVomiting()), obsType, obsDate, false, 3044773);
	    	save(obs);
	    }
	    if (mdsRecord.getDehydrated() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_DEHYDRATED", Long.valueOf(mdsRecord.getDehydrated()), obsType, obsDate, false, 3045840);
	    	save(obs);
	    }
	    if (mdsRecord.getInternalBleeding() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_INTERNAL_BLEEDING", Long.valueOf(mdsRecord.getInternalBleeding()), obsType, obsDate, false, 3044080);
	    	save(obs);
	    }
	    if (mdsRecord.getNoProblemConditions() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_NO_PROBLEM_CONDITIONS", 
	    					  Long.valueOf(mdsRecord.getNoProblemConditions()), obsType, obsDate, false, 40757979);
	    	save(obs);
	    }

	    // MDS Incontinence
	    if (mdsRecord.getBowelIncontinence() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_BOWEL_INCONTINENCE", 
	    					  Long.valueOf(mdsRecord.getBowelIncontinence()), obsType, obsDate, false, 40757902);
	    	save(obs);
	    }
	    if (mdsRecord.getUrinaryIncontinence() != null){
	    	Observation obs = new Observation(person.getPersonId(), "MDS_URINARY_INCONTINENCE", 
	    					  Long.valueOf(mdsRecord.getUrinaryIncontinence()), obsType, obsDate, false, 40757901);
	    	save(obs);
	    }
	}
    }
}

@SuppressWarnings("unused")
    private void loadFallsData(String fallsFile) {

    try {
	CSVReader reader = new CSVReader(new FileReader(fallsFile), '|');
	String [] nextLine;
	reader.readNext(); // Skip the header
	while ((nextLine = reader.readNext()) != null) {
	    if ( nextLine.length < 3 || nextLine[2] == null ) {
		System.out.println("Error reading Falls File Record: " + nextLine);
		continue;
	    }
	    Integer obsType = 38000280; // Observation recorded from EHR

	    // This data is de-id - no translation necessary
	    Long personId = Long.valueOf(nextLine[0]);
	    if ( personId != null ){
		Person person = getPerson(personId);
		// If there is no person skip it
		if ( person == null )
                    continue;
		// Loop through the remaining columns if the
		// column contains a date record a fall
		// observation. Use the FALL_INCIDENT code to
		// distinguish this fall observation from MDS fall
		// history reports which have no mapping to the
		// CDM
		Observation obs = null;
		String sourceObservationCode = "FALL_INCIDENT";
		Integer observationId = 36211297; // MedDRA preferred term for 'Fall'
		Calendar fallDate = parseDate("MM/dd/yyyy", nextLine[1]);
		if (fallDate != null){
		    obs = new Observation(personId, observationId, sourceObservationCode, 1, obsType, fallDate);
		    save(obs);
		}
	    }
	}

    } catch (FileNotFoundException e) {
	e.printStackTrace();
    } catch (IOException e) {
	e.printStackTrace();
    }
}

private Calendar parseDate(String format, String value) {
    Calendar date = null;
    try {
	DateFormat formatter = new SimpleDateFormat(format);
	date = Calendar.getInstance();
	date.setTime(formatter.parse(value));
    } catch (ParseException e) {
	date = null;
    }
    return date;
}

@SuppressWarnings("unchecked")
    private void testLoadConditionsFromMDSReports(){

    // Acquiring the Transaction
    Session sess = session.getCurrentSession();

    List<Mds2> mdsRecords2 = (List<Mds2>) sess.createQuery("FROM Mds2 mds WHERE mds.patientId IN ('7689', '10024','10122','10041')").list();
    iterateThroughInterface2(mdsRecords2);

    List<Mds3> mdsRecords3 = (List<Mds3>) sess.createQuery("FROM Mds3 mds WHERE mds.patientId IN ('7689', '10024','10122','10041')").list();
    iterateThroughInterface3(mdsRecords3);
}

private void iterateThroughInterface3(List<Mds3> mdsRecordsInterface) {

    for (Mds3 mdsRec : mdsRecordsInterface) {
	testPrint((Mds)mdsRec);
    }

}

private void iterateThroughInterface2(List<Mds2> mdsRecordsInterface) {
    System.out.println("MDS2 entered");
    for (Mds2 mdsRec : mdsRecordsInterface) {
	testPrint((Mds)mdsRec);
    }

}

private void testPrint(Mds mdsRec) {
    System.out.println("ID:\t" + mdsRec.getId() + " Patient ID:\t" + mdsRec.getPatientId());
    System.out.println("Birthdate:\t" + mdsRec.getBirthdate());
    System.out.println("Marital Status:\t" + mdsRec.getMaritalstatus());
    System.out.println("Exam type:\t" + mdsRec.getExamType());
    System.out.println("Location:\t" + mdsRec.getLocation());
    //System.out.println("Race:\t" + mdsRec.getRace());
    System.out.println("Entry date:\t" + mdsRec.getEntrydate());
    System.out.println("Comatose:\t" + mdsRec.getComatose());
    System.out.println("Ashd:\t" + mdsRec.getAshd());
    System.out.println("Cancer:\t" + mdsRec.getCancer());
    System.out.println("Schizophrenia:\t" + mdsRec.getSchizophrenia());
    System.out.println("Hip Fracture:\t" + mdsRec.getHipfracture());
    System.out.println("Other Fracture:\t" + mdsRec.getOtherfracture());
    System.out.println("Arthritis:\t" + mdsRec.getArthritis());
    System.out.println("Osteoporosis:\t" + mdsRec.getOsteoporosis());
    System.out.println("Multdrug Reseistant Organism:\t" + mdsRec.getMultidrugresistantorganism());

    try {
	Mds3 mds3record = (Mds3) mdsRec;
	System.out.println("MDS3!");

	System.out.println("Thyroid disorder: " + mds3record.getThyroiddisorder());
	System.out.println("Hyperlipidemia: " + mds3record.getHyperlipidemia());
	System.out.println("Falls since admission: " + mds3record.getFallssinceadmission());
    } catch(ClassCastException e) {
	System.out.println("Not MDS3");
    }

    try {
	Mds2 mds2record = (Mds2) mdsRec;
	System.out.println("MDS2!");

	System.out.println("Hyperthyroidism: " + mds2record.getHyperthyroidism());
	System.out.println("Hypothyroidism: " + mds2record.getHypothyroidism());
    } catch(ClassCastException e) {
	System.out.println("Not MDS2");
    }
    System.out.println("DONE");
    System.out.println();
}

//////////////////////////////////////////////////////////////////////////////////////////////
// MAIN
//////////////////////////////////////////////////////////////////////////////////////////////

/**
 * @param args
 */
// public static void main(String[] args) throws Exception {
//     // FILE PATHS
//     // TODO: move configuration of these to geriomop.properties in the main folder for this project. These can be passed into the targets captured below
//     String ObsPeriodsFile = "/home/rdb20/GeriOMOP/trunk/data/NIA-K01-fall-prediction-model-TEST-1/K01-DDI-prevalence-observation-periods-LOAD-09022015.txt";
//     String pharmFile = "/home/rdb20/GeriOMOP/trunk/data/NIA-K01-fall-prediction-model-TEST-1/K01-DDI-prevalence-drug-exposures-LOAD-09022015.txt";
//     String fallsFile = "/home/rdb20/GeriOMOP/trunk/data/nursing-home-data-102010-012014/falls_20101001_20140120_withstudyid_CORRECTED_MULTI_PATIENTIDs.bar13";

//     // CONTROLLER
//     Upia2Omop upiaToOmop = new Upia2Omop();
//     upiaToOmop.openDbSession();

//     if (args.length > 0){
// 	System.out.println("INFO: Processing custom load arguments (NOTE: should be used for testing only!)");

// 	for (String s: args) {
// 	    System.out.println("INFO: arg - " + s);

// 	    Session sess = session.getCurrentSession();

// 	    if (s.equalsIgnoreCase("persons")){
// 		System.out.println("INFO: loading data into the PERSONS table");
// 		try{
// 		    sess.beginTransaction();
// 		} catch (TransactionException ex) {
// 		    ex.printStackTrace();
// 		}

// 		upiaToOmop.processPersonsFromMDS3();
// 		// TODO: determine if we should load persons from the
// 		// MDS 2 data. Currently (Fall 2014) this does not
// 		// make sense. If it does, uncomment and test the
// 		// method.
// 		//upiaToOmop.processPersonsFromMDS2();

// 		try{
// 		    sess.getTransaction().commit();
// 		    System.out.println("INFO: ...Committing transaction");
// 		} catch (TransactionException ex) {
// 		    ex.printStackTrace();
// 		    sess.getTransaction().rollback();
// 		}
// 	    }
// 	    else if (s.equalsIgnoreCase("obs_periods")){
// 		System.out.println("INFO: loading data into the OBSERVATION_PERIOD table");
// 		try{
// 		    sess.beginTransaction();
// 		} catch (TransactionException ex) {
// 		    ex.printStackTrace();
// 		}

// 		upiaToOmop.loadObsPeriods(ObsPeriodsFile);

// 		try{
// 		    sess.getTransaction().commit();
// 		} catch (TransactionException ex) {
// 		    ex.printStackTrace();
// 		    sess.getTransaction().rollback();
// 		}
// 	    }
// 	    else if (s.equalsIgnoreCase("procedures")){
// 		System.out.println("INFO: loading data into the PROCEDURE_OCCURRENCE table");
// 		try{
// 		    sess.beginTransaction();
// 		} catch (TransactionException ex) {
// 		    ex.printStackTrace();
// 		}

// 		upiaToOmop.loadProceduresFromMDS3();
// 		upiaToOmop.loadProceduresFromMDS2();

// 		try{
// 		    sess.getTransaction().commit();
// 		} catch (TransactionException ex) {
// 		    ex.printStackTrace();
// 		    sess.getTransaction().rollback();
// 		}
// 	    }
// 	    else if (s.equalsIgnoreCase("drug_exposures")){
// 		System.out.println("INFO: loading data into the DRUG_EXPOSURE and DRUG_ERA tables");
// 		try{
// 		    sess.beginTransaction();
// 		} catch (TransactionException ex) {
// 		    ex.printStackTrace();
// 		}

// 		upiaToOmop.loadPharmOrdersAndEras(pharmFile);

// 		try{
// 		    sess.getTransaction().commit();
// 		} catch (TransactionException ex) {
// 		    ex.printStackTrace();
// 		    sess.getTransaction().rollback();
// 		}
// 	    }
// 	    else if (s.equalsIgnoreCase("observationsFromMDS3")){
// 		System.out.println("INFO: loading data from MDS 3 into the OBSERVATION table");
// 		try{
// 		    sess.beginTransaction();
// 		} catch (TransactionException ex) {
// 		    ex.printStackTrace();
// 		}

// 		upiaToOmop.loadObservationsFromMDS3();

// 		try{
// 		    sess.getTransaction().commit();
// 		} catch (TransactionException ex) {
// 		    ex.printStackTrace();
// 		    //sess.getTransaction().rollback();
// 		}
// 	    }
// 	    else if (s.equalsIgnoreCase("fallIncidents")){
// 		System.out.println("INFO: loading data on fall incidents into the OBSERVATION table");
// 		try{
// 		    sess.beginTransaction();
// 		} catch (TransactionException ex) {
// 		    ex.printStackTrace();
// 		}

// 		upiaToOmop.loadFallsData(fallsFile);

// 		try{
// 		    sess.getTransaction().commit();
// 		} catch (TransactionException ex) {
// 		    ex.printStackTrace();
// 		    sess.getTransaction().rollback();
// 		}
// 	    }
// 	    else if (s.equalsIgnoreCase("conditionsFromMDSReports")){
// 		System.out.println("INFO: loading data from MDS reports into the CONDITION_OCCURRENCE and CONDITION_ERA tables");
//                 try{
//                     sess.beginTransaction();
//                 } catch (TransactionException ex) {
//                     ex.printStackTrace();
//                 }

//                 upiaToOmop.loadConditionsFromMDSReports();

//                 try{
//                     sess.getTransaction().commit();
//                 } catch (TransactionException ex) {
//                     ex.printStackTrace();
//                     sess.getTransaction().rollback();
//                 }
// 	    }
// 	    else if (s.equalsIgnoreCase("testMDSInterface")){
// 		System.out.println("INFO: TEST loading data from MDS reports into the CONDITION_OCCURRENCE and CONDITION_ERA tables");
// 		try{
// 		    sess.beginTransaction();
// 		} catch (TransactionException ex) {
// 		    ex.printStackTrace();
// 		}

// 		upiaToOmop.testLoadConditionsFromMDSReports();

// 		try{
// 		    sess.getTransaction().commit();
// 		} catch (TransactionException ex) {
// 		    ex.printStackTrace();
// 		    sess.getTransaction().rollback();
// 		}
// 	    }



// 	}
//     }

//     upiaToOmop.closeDbSession();
// }




}