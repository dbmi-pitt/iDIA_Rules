package edu.pitt.dbmi.ohdsiv5.db.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;


public class HibernateUtil {
	
	private static String hbm2ddl = "none";	
	private static Log log = LogFactory.getLog(HibernateUtil.class);
		
	//TODO put in property file
	// Work Computer
	private static String defaultUsername = getPropertyValues("username");
	private static String defaultPassword = getPropertyValues("password");
	private static String defaultConnectionURL = getPropertyValues("connectionURL");
	//TODO set up port, hostname, etc. for later
	private static Configuration hibernateConf = null;
	private static boolean validConnection = false;
	
	private static SessionFactory sessionFactory = null;
	
	static {
		try {
			
            hibernateConf = new AnnotationConfiguration();
            ((AnnotationConfiguration)hibernateConf).addAnnotatedClass(Class.forName("edu.pitt.dbmi.ohdsiv5.db.Concept"));
	    ((AnnotationConfiguration)hibernateConf).addAnnotatedClass(Class.forName("edu.pitt.dbmi.ohdsiv5.db.ConceptRelationship"));
            ((AnnotationConfiguration)hibernateConf).addAnnotatedClass(Class.forName("edu.pitt.dbmi.ohdsiv5.db.Mds3"));
            ((AnnotationConfiguration)hibernateConf).addAnnotatedClass(Class.forName("edu.pitt.dbmi.ohdsiv5.db.Mds2"));
            ((AnnotationConfiguration)hibernateConf).addAnnotatedClass(Class.forName("edu.pitt.dbmi.ohdsiv5.db.Location"));
            ((AnnotationConfiguration)hibernateConf).addAnnotatedClass(Class.forName("edu.pitt.dbmi.ohdsiv5.db.Person"));
            ((AnnotationConfiguration)hibernateConf).addAnnotatedClass(Class.forName("edu.pitt.dbmi.ohdsiv5.db.ObservationPeriod"));
	    ((AnnotationConfiguration)hibernateConf).addAnnotatedClass(Class.forName("edu.pitt.dbmi.ohdsiv5.db.ProcedureOccurrence"));
            ((AnnotationConfiguration)hibernateConf).addAnnotatedClass(Class.forName("edu.pitt.dbmi.ohdsiv5.db.DrugExposure"));
            ((AnnotationConfiguration)hibernateConf).addAnnotatedClass(Class.forName("edu.pitt.dbmi.ohdsiv5.db.DrugEra"));
            ((AnnotationConfiguration)hibernateConf).addAnnotatedClass(Class.forName("edu.pitt.dbmi.ohdsiv5.db.Observation"));
	    ((AnnotationConfiguration)hibernateConf).addAnnotatedClass(Class.forName("edu.pitt.dbmi.ohdsiv5.db.ConditionOccurrence"));
	    ((AnnotationConfiguration)hibernateConf).addAnnotatedClass(Class.forName("edu.pitt.dbmi.ohdsiv5.db.ConditionEra"));
	    ((AnnotationConfiguration)hibernateConf).addAnnotatedClass(Class.forName("edu.pitt.dbmi.ohdsiv5.db.ConceptAncestor"));
	    ((AnnotationConfiguration)hibernateConf).addAnnotatedClass(Class.forName("edu.pitt.dbmi.ohdsiv5.db.DrugStrength"));
	    ((AnnotationConfiguration)hibernateConf).addAnnotatedClass(Class.forName("edu.pitt.dbmi.ohdsiv5.db.Measurement"));
	    ((AnnotationConfiguration)hibernateConf).addAnnotatedClass(Class.forName("edu.pitt.dbmi.ohdsiv5.db.ExtendedDrugExposure"));
	    ((AnnotationConfiguration)hibernateConf).addAnnotatedClass(Class.forName("edu.pitt.dbmi.ohdsiv5.db.VisitOccurrence"));


	    // hibernateConf.configure().buildSessionFactory();
            hibernateConf.setProperty("hibernate.hbm2ddl.auto", hbm2ddl)
            	.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver") 
	            .setProperty("hibernate.connection.url", defaultConnectionURL)
	            .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
	            .setProperty("hibernate.connection.username", defaultUsername)
	        	.setProperty("hibernate.connection.password", defaultPassword)
	        	.setProperty("hibernate.show_sql","true")
   		         .setProperty("net.sf.ehcache.configurationResourceName", "ehcache.xml")
	        	.setProperty("hibernate.cache.provider_class","net.sf.ehcache.hibernate.EhCacheProvider")
	        	.setProperty("hibernate.min-pool-size","1")
	        	.setProperty("hibernate.max-pool-size", "1")
	        	.setProperty("hibernate.current_session_context_class", "thread")	        	
	            .setProperty("hibernate.c3p0.acquire_increment", "1")
	            .setProperty("hibernate.c3p0.idle_test_period","100")
	            .setProperty("hibernate.c3p0.max_size","3")
	            .setProperty("hibernate.c3p0.max_statements","0")
	            .setProperty("hibernate.c3p0.min_size","0")
	            .setProperty("hibernate.c3p0.timeout","20")
	        	.setProperty("hibernate.cache.use_second_level_cache","true")
	            .setProperty("hibernate.jdbc.batch.size","20");
            	
            	sessionFactory = hibernateConf.buildSessionFactory();    			

		} catch (Throwable ex) {
			// Log exception!
			ex.printStackTrace();
		}
	}

	public static Session getSession() 
	{ // consider moving the try loop from the constructor/main to here!!
	    
	      String defaultUsername = "";  
	      String defaultPassword = "";
	      String defaultConnectionURL = "";
	      Properties prop = new Properties();
	      InputStream input = null;
	      try {
		      input = new FileInputStream("config.properties");
		      // load a properties file
		      prop.load(input);
		      // get the property value and print it out
		      defaultUsername = prop.getProperty("username");
		      defaultPassword = prop.getProperty("password");
		      defaultConnectionURL = prop.getProperty("connectionURL");
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
	      Session session = null;
	      try {
		session = sessionFactory.openSession();
	      }catch (Exception ex){
		ex.printStackTrace();
	      }
	      return session;
	}
	
	public static String getPropertyValues(String value)
	{
	    String temp = "";
	    Properties prop = new Properties();
	    InputStream input = null;
	    try {
		input = new FileInputStream("config.properties");
		// load a properties file
		prop.load(input);
		// get the property value and print it out
		temp = prop.getProperty(value);
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
	    return temp;
	}
}
