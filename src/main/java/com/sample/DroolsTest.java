package com.sample;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import com.sample.model.Cns;
import com.sample.model.Risk;
import com.sample.model.RiskScore;
import com.sample.model.Observation;

import java.sql.*;
import java.util.*;

public class DroolsTest {

    private static final String USERNAME = "";    
    private static final String PASS = "";
    private static final String IP = "";
    
    private static final String DATABASE = "dikb";
	private static final String SID = "DBMI01";
    private static final String PORT = "1521";    
    private static final String DRIVER = "oracle.jdbc.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@"+IP+":"+PORT+":"+SID;
    
    private static final String SQL = "SELECT DISTINCT D1.person_id, D1.drug_concept_id, D1.days_supply FROM drug_exposure D1 ORDER BY D1.person_id;";
//    private static final String cnsSQL = "SELECT DISTINCT D1.person_id, count(D1.drug_concept_id) as exposures FROM DIKB_DEV.drug_exposure D1 GROUP BY D1.person_id";
    private static final String cnsSQL = "SELECT person_id, location_id, " +
    										"(SELECT count(D1.drug_concept_id) " + 
    										"FROM DIKB_DEV.drug_exposure D1 "+ 
    										"where D1.person_id = p.person_id) as exposures FROM DIKB_DEV.person p";
    
    
    private static final String fallsSQL = "select DISTINCT person_id, observation_source_value, value_as_concept_id " +
    										"from DIKB_DEV.observation x " +
    										"where (observation_source_value='MDS_FALL_2_TO_6_MNTH'  or observation_source_value='MDS_FALL_LAST_MNTH') order by person_id ";
    

    
    @SuppressWarnings({ "unchecked" })
	public static void main(String[] args) throws ClassNotFoundException {

    Map<Integer, Patient> patientMap = new HashMap<Integer, Patient>() ;
    Connection connection=null;	
    	
        try {  
        	
            // Database connection
            Class.forName(DRIVER);
            Properties props = new Properties();
            props.setProperty("user", USERNAME);
            props.setProperty("password", PASS);
            connection = DriverManager.getConnection(URL, props);
            if (connection != null) System.out.println("Connected to a database!");
            else System.out.println("Problem with database connection!");
        	
            System.out.println("Gathering data...");
            // SQL query
            PreparedStatement ps = connection.prepareStatement(cnsSQL);
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData md = rs.getMetaData();
            //int columns = md.getColumnCount();   
                    	
	        KieServices ks = KieServices.Factory.get();
    	    KieContainer kContainer = ks.getKieClasspathContainer();
        	KieSession kSession = kContainer.newKieSession("ksession-rules");
               
        	// Load all the facts
        	System.out.println("Asserting facts...");
        	int cnt = 0;
            while (rs.next())
            {           	
            	Cns cns = new Cns();
            	cns.setId(rs.getInt(1));
            	cns.setExposures(((Integer)rs.getInt(3)).intValue());

            	kSession.insert(cns);
            	
            	Patient patient = new Patient();
            	patient.setId(rs.getInt(1));
            	patient.setLocation(rs.getInt(2));
            	patientMap.put(patient.id, patient);
 
            	cnt++;
            }
            
            ps = connection.prepareStatement(fallsSQL);
            rs = ps.executeQuery();
            md = rs.getMetaData();
                        
            while (rs.next())
            {           	
            	Observation obs = new Observation();
            	obs.setId(rs.getInt(1));
            	obs.setType(rs.getString(2));
            	obs.setValueConceptId(rs.getInt(3));
            	kSession.insert(obs);  
            }
            
            kSession.setGlobal("risks", new HashMap<Integer, Risk>() );
            kSession.setGlobal("riskscores", new HashMap<Integer, RiskScore>() );
            
            System.out.println("Firing rules for assessment...");
            // fire rules
            kSession.fireAllRules();            
 
            // get all the calculated risks and assert them to be
            Map<Integer, Risk> riskList = (HashMap<Integer, Risk>)kSession.getGlobal("risks");
            for (Map.Entry<Integer, Risk> e : riskList.entrySet()) {
               // System.out.println(e.getKey() + ": " + e.getValue());
            	Risk r = (Risk)e.getValue();
            	kSession.insert(r); 
            	/*if (r.isCns() && r.isFallHx()) {
            		System.out.println(r.getId() + " high risk");
            	} else if (r.isCns()== false && r.isFallHx() == false){
            		System.out.println(r.getId() + " low risk");
            	} else {
            		System.out.println(r.getId() + " med risk");
            	}
            	*/
            }
            
            System.out.println("Firing rules to compute risk scores...");
            // refire rules to compute the riskscores
            kSession.fireAllRules();
            
            System.out.println("Inserting results into the RISK_ANALYSIS table ...");
            // get the risk scores
            Map<Integer, RiskScore> computedRiskScores = (HashMap<Integer, RiskScore>)kSession.getGlobal("riskscores");
            //insertRisksAnalysis(connection, patientMap, computedRiskScores);           

            
            
            ps.close();
            rs.close();
            connection.close();
            
            kSession.dispose();
            System.out.println("Patient Count: " + cnt);           
            
        } catch (SQLException e) {
            e.printStackTrace();
        } 
    }
    
    public static void insertRisksAnalysis(Connection conn, Map<Integer, Patient> patientMap, Map<Integer, RiskScore> computedRiskScores) {
		Statement stmt;
    	try {

		stmt = conn.createStatement();
        for (Map.Entry<Integer, Patient> pm : patientMap.entrySet()) {
        	Patient p = (Patient)pm.getValue();
        	
        	RiskScore rs = (RiskScore)computedRiskScores.get(p.id);
        	if (rs != null) {
				
	        	    String sql = "INSERT INTO ohdsi.RISK_ANALYSIS " +
	        	    		"(risk_analysis_id, rule_id, subject_id, location_id, risk_score_type ) " +
       	                   "VALUES (ohdsi.RISK_ANALYSIS_SEQ.nextval, 1, " + p.id + "," + p.location + ", '" + rs.getScore() + "')"; 
	        	    
	        	     stmt.executeUpdate(sql);
        	}        	
        } // for        
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
    }
    
    
    public static class Patient {    	
    	Integer id;
    	Integer location;
    	public Integer getId() {
    		return id;
    	}
    	public void setId(Integer id) {
    		this.id = id;
    	}
    	public Integer getLocation() {
    		return location;
    	}
    	public void setLocation(Integer location) {
    		this.location = location;
    	}
    }

}
