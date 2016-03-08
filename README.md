This is a sample application to demonstrates using Drools rules
against the OHDSI data structure for the AHRQ funded Individualized
Drug Interaction Alerts (IDIA) project (https://goo.gl/t4eqGw). 

A few simple drools rules are contained within the ```src/main/resources/rules/risk.drl``` directory.

__SETUP__

You must change the following in the __src/main/java/edu/pitt/dbmi/ohdsiv5/db/util/HibernateUtil.java__ file

```
private static String defaultConnectionURL  = "jdbc:postgresql://<IP>:<PORT>/<DATABASE>";
```

__BUILD__

Note this requires Java >= 8.

There is a maven __pom.xml__ file used to build the application. Before running that, execute the following statements:

```
mvn install:install-file -Dfile=./lib/opencsv-2.3.jar -DgroupId=org.opencsv -DartifactId=opencsv -Dversion=2.3 -Dpackaging=jar
mvn install:install-file -Dfile=./lib/hibernate-core-3.3.0.SP1.jar -DgroupId=hibernate -DartifactId=hibernate -Dversion=3.3.0 -Dpackaging=jar
mvn install:install-file -Dfile=./lib/hibernate-annotations-3.4.0.GA.jar -DgroupId=hibernate -DartifactId=hibernate-annotations -Dversion=3.4.0 -Dpackaging=jar
mvn install:install-file -Dfile=./lib/hibernate-commons-annotations-3.1.0.GA.jar -DgroupId=hibernate -DartifactId=hibernate-commons-annotations -Dversion=3.1.0 -Dpackaging=jar
mvn install:install-file -Dfile=./lib/ehcache-core-2.1.0.jar -DgroupId=ehcache -DartifactId=ehcache-core -Dversion=2.1.0 -Dpackaging=jar
mvn install:install-file -Dfile=./lib/antlr.jar -DgroupId=antlr -DartifactId=antlr -Dversion=unknown -Dpackaging=jar
```

Then you should be able to run:
```mvn install```

__RUN__

```java -jar target/droolstest-1.0.jar```

