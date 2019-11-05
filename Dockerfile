# Labels the first stage as clone
FROM alpine/git as clone
WORKDIR /app
RUN git clone -b ec-sql-modifiedclasses --single-branch https://github.com/rkboyce/iDIA_Rules.git

# Labels the second stage as build
FROM maven:3.5-jdk-8-alpine as build
WORKDIR /app

# References the first stage using the label
COPY --from=clone /app/iDIA_Rules /app

# Need to copy correct config.properties to container
COPY config.properties /app/config.properties

RUN mvn install:install-file -Dfile=./lib/opencsv-2.3.jar -DgroupId=org.opencsv -DartifactId=opencsv -Dversion=2.3 -Dpackaging=jar
RUN mvn install:install-file -Dfile=./lib/hibernate-core-3.3.0.SP1.jar -DgroupId=hibernate -DartifactId=hibernate -Dversion=3.3.0 -Dpackaging=jar
RUN mvn install:install-file -Dfile=./lib/hibernate-annotations-3.4.0.GA.jar -DgroupId=hibernate -DartifactId=hibernate-annotations -Dversion=3.4.0 -Dpackaging=jar
RUN mvn install:install-file -Dfile=./lib/hibernate-commons-annotations-3.1.0.GA.jar -DgroupId=hibernate -DartifactId=hibernate-commons-annotations -Dversion=3.1.0 -Dpackaging=jar
RUN mvn install:install-file -Dfile=./lib/ehcache-core-2.1.0.jar -DgroupId=ehcache -DartifactId=ehcache-core -Dversion=2.1.0 -Dpackaging=jar
RUN mvn install:install-file -Dfile=./lib/antlr.jar -DgroupId=antlr -DartifactId=antlr -Dversion=unknown -Dpackaging=jar
RUN mvn install

RUN apk update && apk add coreutils

RUN ["chmod", "+x", "/app/runRules.sh"]
ENTRYPOINT ["/app/runRules.sh"]

# Allows user to specify the run of either "simulated" or "banner"
CMD []

# Running:
# -v mounts the container and then the user can specify the local filepath to copy contents from container
# -m sets the memory for the container
# can override properties such as schema, ruleFolder, connectionURL, etc.
# docker run -m 8g -v ~/simulated-run:/app/simulated-run -it --rm maxsibilla/idia_rules simulated schema=simulated