This is a sample application to demonstrates using Drools rules
against the OHDSI data structure for the AHRQ funded Individualized
Drug Interaction Alerts (IDIA) project (https://goo.gl/t4eqGw). 

__SETUP__

You must change the following in the `config.properties` file.

```
username=<USERNAME>
password=<PASSWORD>
connectionURL=jdbc:postgresql://<IP>:<PORT>/<DATABASE>
```

Replace the variables in <> with your values.

Additionally, there are two folders that can be fired, `src/main/resources/rules-complete/` which includes all rules, and `src/main/resources/rules-progress/` which is an empty folder that select rules can be placed into for firing.
To switch between these two folders, you must edit the `config.properties` file in the root of this repository. If you want all rules to fire, set rule_folder to `ksession-rules`. If you want just the rules you placed into the progress folder to fire, set rule_folder to `ksession-progress`.

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

