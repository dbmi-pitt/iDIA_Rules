package edu.pitt.dbmi.ohdsiv5.db.util;
               # Replace Windows newlines with Unix newlines
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
               # Replace Windows newlines with Unix newlines	
               # Replace Windows newlines with Unix newlines	private static Log log = LogFactory.getLog(HibernateUtil.class);
               # Replace Windows newlines with Unix newlines	
               # Replace Windows newlines with Unix newlines	//TODO put in property file
               # Replace Windows newlines with Unix newlines	// Work Computer
               # Replace Windows newlines with Unix newlines	private static String defaultUsername = "student";
               # Replace Windows newlines with Unix newlines	private static String defaultPassword = "student";
               # Replace Windows newlines with Unix newlines	private static String defaultConnectionURL  = "jdbc:postgresql://localhost:5432/testohdsi";
               # Replace Windows newlines with Unix newlines	//TODO set up port, hostname, etc. for later
               # Replace Windows newlines with Unix newlines	private static Configuration hibernateConf = null;
               # Replace Windows newlines with Unix newlines	private static boolean validConnection = false;
               # Replace Windows newlines with Unix newlines	
               # Replace Windows newlines with Unix newlines    private static SessionFactory sessionFactory = null;
               # Replace Windows newlines with Unix newlines 
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
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines	    // hibernateConf.configure().buildSessionFactory();
               # Replace Windows newlines with Unix newlines            hibernateConf.setProperty("hibernate.hbm2ddl.auto", hbm2ddl)
               # Replace Windows newlines with Unix newlines            	.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver") 
               # Replace Windows newlines with Unix newlines	            .setProperty("hibernate.connection.url", defaultConnectionURL)
               # Replace Windows newlines with Unix newlines	            .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
               # Replace Windows newlines with Unix newlines	            .setProperty("hibernate.connection.username", defaultUsername)
               # Replace Windows newlines with Unix newlines	        	.setProperty("hibernate.connection.password", defaultPassword)
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
               # Replace Windows newlines with Unix newlines	public static Session getSession() {
               # Replace Windows newlines with Unix newlines		Session session = null;
               # Replace Windows newlines with Unix newlines		try {
               # Replace Windows newlines with Unix newlines			 session = sessionFactory.openSession();
               # Replace Windows newlines with Unix newlines		}catch (Exception ex){
               # Replace Windows newlines with Unix newlines			ex.printStackTrace();
               # Replace Windows newlines with Unix newlines		}
               # Replace Windows newlines with Unix newlines		return session;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines}
               # Replace Windows newlines with Unix newlines