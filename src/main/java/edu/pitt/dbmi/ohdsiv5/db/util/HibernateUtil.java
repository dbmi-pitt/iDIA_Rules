package edu.pitt.dbmi.ohdsiv5.db.util;
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlinesimport java.io.FileInputStream;
               # Replace Windows newlines with Unix newlinesimport java.io.IOException;
               # Replace Windows newlines with Unix newlinesimport java.io.InputStream;
               # Replace Windows newlines with Unix newlinesimport java.util.*;
               # Replace Windows newlines with Unix newlinesimport org.apache.commons.logging.Log;
               # Replace Windows newlines with Unix newlinesimport org.apache.commons.logging.LogFactory;
               # Replace Windows newlines with Unix newlinesimport org.hibernate.HibernateException;
               # Replace Windows newlines with Unix newlinesimport org.hibernate.Session;
               # Replace Windows newlines with Unix newlinesimport org.hibernate.SessionFactory;
               # Replace Windows newlines with Unix newlinesimport org.hibernate.cfg.AnnotationConfiguration;
               # Replace Windows newlines with Unix newlinesimport org.hibernate.cfg.Configuration;
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlinespublic class HibernateUtil {
               # Replace Windows newlines with Unix newlines	
               # Replace Windows newlines with Unix newlines	private static String hbm2ddl = "none";	
               # Replace Windows newlines with Unix newlines	private static Log log = LogFactory.getLog(HibernateUtil.class);
               # Replace Windows newlines with Unix newlines		
               # Replace Windows newlines with Unix newlines	//TODO put in property file
               # Replace Windows newlines with Unix newlines	// Work Computer
               # Replace Windows newlines with Unix newlines	private static String defaultUsername = getPropertyValues("username");
               # Replace Windows newlines with Unix newlines	private static String defaultPassword = getPropertyValues("password");
               # Replace Windows newlines with Unix newlines	private static String defaultConnectionURL = getPropertyValues("connectionURL");
               # Replace Windows newlines with Unix newlines        private static String defaultTableSchema = getPropertyValues("schema");
               # Replace Windows newlines with Unix newlines	//TODO set up port, hostname, etc. for later
               # Replace Windows newlines with Unix newlines	private static Configuration hibernateConf = null;
               # Replace Windows newlines with Unix newlines	private static boolean validConnection = false;
               # Replace Windows newlines with Unix newlines	
               # Replace Windows newlines with Unix newlines	private static SessionFactory sessionFactory = null;
               # Replace Windows newlines with Unix newlines	
               # Replace Windows newlines with Unix newlines	static {
               # Replace Windows newlines with Unix newlines		try {
               # Replace Windows newlines with Unix newlines			
               # Replace Windows newlines with Unix newlines            hibernateConf = new AnnotationConfiguration();
               # Replace Windows newlines with Unix newlines            ((AnnotationConfiguration)hibernateConf).addAnnotatedClass(Class.forName("edu.pitt.dbmi.ohdsiv5.db.Concept"));
               # Replace Windows newlines with Unix newlines	    ((AnnotationConfiguration)hibernateConf).addAnnotatedClass(Class.forName("edu.pitt.dbmi.ohdsiv5.db.ConceptRelationship"));
               # Replace Windows newlines with Unix newlines            ((AnnotationConfiguration)hibernateConf).addAnnotatedClass(Class.forName("edu.pitt.dbmi.ohdsiv5.db.Mds3"));
               # Replace Windows newlines with Unix newlines            ((AnnotationConfiguration)hibernateConf).addAnnotatedClass(Class.forName("edu.pitt.dbmi.ohdsiv5.db.Mds2"));
               # Replace Windows newlines with Unix newlines            ((AnnotationConfiguration)hibernateConf).addAnnotatedClass(Class.forName("edu.pitt.dbmi.ohdsiv5.db.Location"));
               # Replace Windows newlines with Unix newlines            ((AnnotationConfiguration)hibernateConf).addAnnotatedClass(Class.forName("edu.pitt.dbmi.ohdsiv5.db.Person"));
               # Replace Windows newlines with Unix newlines            ((AnnotationConfiguration)hibernateConf).addAnnotatedClass(Class.forName("edu.pitt.dbmi.ohdsiv5.db.ObservationPeriod"));
               # Replace Windows newlines with Unix newlines	    ((AnnotationConfiguration)hibernateConf).addAnnotatedClass(Class.forName("edu.pitt.dbmi.ohdsiv5.db.ProcedureOccurrence"));
               # Replace Windows newlines with Unix newlines            ((AnnotationConfiguration)hibernateConf).addAnnotatedClass(Class.forName("edu.pitt.dbmi.ohdsiv5.db.DrugExposure"));
               # Replace Windows newlines with Unix newlines            ((AnnotationConfiguration)hibernateConf).addAnnotatedClass(Class.forName("edu.pitt.dbmi.ohdsiv5.db.DrugEra"));
               # Replace Windows newlines with Unix newlines            ((AnnotationConfiguration)hibernateConf).addAnnotatedClass(Class.forName("edu.pitt.dbmi.ohdsiv5.db.Observation"));
               # Replace Windows newlines with Unix newlines	    ((AnnotationConfiguration)hibernateConf).addAnnotatedClass(Class.forName("edu.pitt.dbmi.ohdsiv5.db.ConditionOccurrence"));
               # Replace Windows newlines with Unix newlines	    ((AnnotationConfiguration)hibernateConf).addAnnotatedClass(Class.forName("edu.pitt.dbmi.ohdsiv5.db.ConditionEra"));
               # Replace Windows newlines with Unix newlines	    ((AnnotationConfiguration)hibernateConf).addAnnotatedClass(Class.forName("edu.pitt.dbmi.ohdsiv5.db.ConceptAncestor"));
               # Replace Windows newlines with Unix newlines	    ((AnnotationConfiguration)hibernateConf).addAnnotatedClass(Class.forName("edu.pitt.dbmi.ohdsiv5.db.DrugStrength"));
               # Replace Windows newlines with Unix newlines	    ((AnnotationConfiguration)hibernateConf).addAnnotatedClass(Class.forName("edu.pitt.dbmi.ohdsiv5.db.Measurement"));
               # Replace Windows newlines with Unix newlines	    ((AnnotationConfiguration)hibernateConf).addAnnotatedClass(Class.forName("edu.pitt.dbmi.ohdsiv5.db.ExtendedDrugExposure"));
               # Replace Windows newlines with Unix newlines	    ((AnnotationConfiguration)hibernateConf).addAnnotatedClass(Class.forName("edu.pitt.dbmi.ohdsiv5.db.VisitOccurrence"));
               # Replace Windows newlines with Unix newlines	    ((AnnotationConfiguration)hibernateConf).addAnnotatedClass(Class.forName("edu.pitt.dbmi.ohdsiv5.db.SigMapping"));
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines	    // hibernateConf.configure().buildSessionFactory();
               # Replace Windows newlines with Unix newlines            hibernateConf.setProperty("hibernate.hbm2ddl.auto", hbm2ddl)
               # Replace Windows newlines with Unix newlines            	.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver") 
               # Replace Windows newlines with Unix newlines	            .setProperty("hibernate.connection.url", defaultConnectionURL)
               # Replace Windows newlines with Unix newlines	            .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
               # Replace Windows newlines with Unix newlines	            .setProperty("hibernate.connection.username", defaultUsername)
               # Replace Windows newlines with Unix newlines	        	.setProperty("hibernate.connection.password", defaultPassword)
               # Replace Windows newlines with Unix newlines   		        .setProperty("hibernate.default_schema", defaultTableSchema)
               # Replace Windows newlines with Unix newlines	        	.setProperty("hibernate.show_sql","true")
               # Replace Windows newlines with Unix newlines   		         .setProperty("net.sf.ehcache.configurationResourceName", "ehcache.xml")
               # Replace Windows newlines with Unix newlines	        	.setProperty("hibernate.cache.provider_class","net.sf.ehcache.hibernate.EhCacheProvider")
               # Replace Windows newlines with Unix newlines	        	.setProperty("hibernate.min-pool-size","1")
               # Replace Windows newlines with Unix newlines	        	.setProperty("hibernate.max-pool-size", "1")
               # Replace Windows newlines with Unix newlines	        	.setProperty("hibernate.current_session_context_class", "thread")	        	
               # Replace Windows newlines with Unix newlines	            .setProperty("hibernate.c3p0.acquire_increment", "1")
               # Replace Windows newlines with Unix newlines	            .setProperty("hibernate.c3p0.idle_test_period","100")
               # Replace Windows newlines with Unix newlines	            .setProperty("hibernate.c3p0.max_size","3")
               # Replace Windows newlines with Unix newlines	            .setProperty("hibernate.c3p0.max_statements","0")
               # Replace Windows newlines with Unix newlines	            .setProperty("hibernate.c3p0.min_size","0")
               # Replace Windows newlines with Unix newlines	            .setProperty("hibernate.c3p0.timeout","20")
               # Replace Windows newlines with Unix newlines	        	.setProperty("hibernate.cache.use_second_level_cache","true")
               # Replace Windows newlines with Unix newlines	            .setProperty("hibernate.jdbc.batch.size","20");
               # Replace Windows newlines with Unix newlines            	
               # Replace Windows newlines with Unix newlines            	sessionFactory = hibernateConf.buildSessionFactory();    			
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines		} catch (Throwable ex) {
               # Replace Windows newlines with Unix newlines			// Log exception!
               # Replace Windows newlines with Unix newlines			ex.printStackTrace();
               # Replace Windows newlines with Unix newlines		}
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines	public static Session getSession() 
               # Replace Windows newlines with Unix newlines	{ // consider moving the try loop from the constructor/main to here!!
               # Replace Windows newlines with Unix newlines	    
               # Replace Windows newlines with Unix newlines	      String defaultUsername = "";  
               # Replace Windows newlines with Unix newlines	      String defaultPassword = "";
               # Replace Windows newlines with Unix newlines	      String defaultConnectionURL = "";
               # Replace Windows newlines with Unix newlines	      Properties prop = new Properties();
               # Replace Windows newlines with Unix newlines	      InputStream input = null;
               # Replace Windows newlines with Unix newlines	      try {
               # Replace Windows newlines with Unix newlines		      input = new FileInputStream("config.properties");
               # Replace Windows newlines with Unix newlines		      // load a properties file
               # Replace Windows newlines with Unix newlines		      prop.load(input);
               # Replace Windows newlines with Unix newlines		      // get the property value and print it out
               # Replace Windows newlines with Unix newlines		      defaultUsername = prop.getProperty("username");
               # Replace Windows newlines with Unix newlines		      defaultPassword = prop.getProperty("password");
               # Replace Windows newlines with Unix newlines		      defaultConnectionURL = prop.getProperty("connectionURL");
               # Replace Windows newlines with Unix newlines		      defaultTableSchema = prop.getProperty("schema");
               # Replace Windows newlines with Unix newlines	      } catch (IOException ex) {
               # Replace Windows newlines with Unix newlines		      ex.printStackTrace();
               # Replace Windows newlines with Unix newlines	      } finally {
               # Replace Windows newlines with Unix newlines		      if (input != null) {
               # Replace Windows newlines with Unix newlines			      try {
               # Replace Windows newlines with Unix newlines				      input.close();
               # Replace Windows newlines with Unix newlines			      } catch (IOException e) {
               # Replace Windows newlines with Unix newlines				      e.printStackTrace();
               # Replace Windows newlines with Unix newlines			      }
               # Replace Windows newlines with Unix newlines		      }
               # Replace Windows newlines with Unix newlines	      }   
               # Replace Windows newlines with Unix newlines	      Session session = null;
               # Replace Windows newlines with Unix newlines	      try {
               # Replace Windows newlines with Unix newlines		session = sessionFactory.openSession();
               # Replace Windows newlines with Unix newlines	      }catch (Exception ex){
               # Replace Windows newlines with Unix newlines		ex.printStackTrace();
               # Replace Windows newlines with Unix newlines	      }
               # Replace Windows newlines with Unix newlines	      return session;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines	
               # Replace Windows newlines with Unix newlines	public static String getPropertyValues(String value)
               # Replace Windows newlines with Unix newlines	{
               # Replace Windows newlines with Unix newlines	    String temp = "";
               # Replace Windows newlines with Unix newlines	    Properties prop = new Properties();
               # Replace Windows newlines with Unix newlines	    InputStream input = null;
               # Replace Windows newlines with Unix newlines	    try {
               # Replace Windows newlines with Unix newlines		input = new FileInputStream("config.properties");
               # Replace Windows newlines with Unix newlines		// load a properties file
               # Replace Windows newlines with Unix newlines		prop.load(input);
               # Replace Windows newlines with Unix newlines		// get the property value and print it out
               # Replace Windows newlines with Unix newlines		temp = prop.getProperty(value);
               # Replace Windows newlines with Unix newlines	      } catch (IOException ex) {
               # Replace Windows newlines with Unix newlines		      ex.printStackTrace();
               # Replace Windows newlines with Unix newlines	      } finally {
               # Replace Windows newlines with Unix newlines		      if (input != null) {
               # Replace Windows newlines with Unix newlines			      try {
               # Replace Windows newlines with Unix newlines				      input.close();
               # Replace Windows newlines with Unix newlines			      } catch (IOException e) {
               # Replace Windows newlines with Unix newlines				      e.printStackTrace();
               # Replace Windows newlines with Unix newlines			      }
               # Replace Windows newlines with Unix newlines		      }
               # Replace Windows newlines with Unix newlines	      } 
               # Replace Windows newlines with Unix newlines	    return temp;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines}
               # Replace Windows newlines with Unix newlines