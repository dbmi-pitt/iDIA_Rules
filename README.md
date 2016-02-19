This is a sample application to demonstrates using Drools rules against the OHDSI data structure.  

A few simple drools rules are contained within the ```src/main/resources/rules/risk.drl``` directory.

__SETUP__

You must change the following in the __DroolsTest.java__ file

```
private static final String USERNAME = "<username>";    
private static final String PASS = "<password>";
private static final String IP = "<IP>";
```

__BUILD__

There is a maven __pom.xml__ file used to build the application

```mvn install```

__RUN__

```java -jar target/droolstest-1.0.jar```


The results can be viewed in the __OHDSI.RISK_ANALYSIS__ table
