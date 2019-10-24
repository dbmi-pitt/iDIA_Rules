FROM alpine/git
               # Replace Windows newlines with Unix newlinesWORKDIR /app
               # Replace Windows newlines with Unix newlinesRUN git clone https://github.com/rkboyce/iDIA_Rules.git
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlinesENV date 10221029
               # Replace Windows newlines with Unix newlines# since this is in the same directory maybe dont need /iDEA-Rules
               # Replace Windows newlines with Unix newlinesENV config_file config.properties
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlinesFROM maven:3.5-jdk-8-alpine
               # Replace Windows newlines with Unix newlinesWORKDIR /app
               # Replace Windows newlines with Unix newlinesCOPY --from=0 /app/iDIA_Rules /app 
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlinesRUN mvn install:install-file -Dfile=./lib/opencsv-2.3.jar -DgroupId=org.opencsv -DartifactId=opencsv -Dversion=2.3 -Dpackaging=jar
               # Replace Windows newlines with Unix newlinesRUN mvn install:install-file -Dfile=./lib/hibernate-core-3.3.0.SP1.jar -DgroupId=hibernate -DartifactId=hibernate -Dversion=3.3.0 -Dpackaging=jar
               # Replace Windows newlines with Unix newlinesRUN mvn install:install-file -Dfile=./lib/hibernate-annotations-3.4.0.GA.jar -DgroupId=hibernate -DartifactId=hibernate-annotations -Dversion=3.4.0 -Dpackaging=jar
               # Replace Windows newlines with Unix newlinesRUN mvn install:install-file -Dfile=./lib/hibernate-commons-annotations-3.1.0.GA.jar -DgroupId=hibernate -DartifactId=hibernate-commons-annotations -Dversion=3.1.0 -Dpackaging=jar
               # Replace Windows newlines with Unix newlinesRUN mvn install:install-file -Dfile=./lib/ehcache-core-2.1.0.jar -DgroupId=ehcache -DartifactId=ehcache-core -Dversion=2.1.0 -Dpackaging=jar
               # Replace Windows newlines with Unix newlinesRUN mvn install:install-file -Dfile=./lib/antlr.jar -DgroupId=antlr -DartifactId=antlr -Dversion=unknown -Dpackaging=jar
               # Replace Windows newlines with Unix newlinesRUN mvn install
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines# Might not be necessary
               # Replace Windows newlines with Unix newlinesFROM openjdk:8-jre-alpine
               # Replace Windows newlines with Unix newlines#WORKDIR /iDEA-Rules/
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines# Need to add PostgreSQL commands
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlinesCOPY config.properties ${config_file}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlinesRUN mkdir banner-run-${date}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlinesWORKDIR /app
               # Replace Windows newlines with Unix newlinesCMD ["runOverBanner.sh"] [${date}]