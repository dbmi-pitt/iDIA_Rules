# Labels the first stage as clone
FROM alpine/git as clone
WORKDIR /app
RUN git clone -b ec-sql-modifiedclasses --single-branch https://github.com/rkboyce/iDIA_Rules.git

# Labels the second stage as build
FROM maven:3.5-jdk-8-alpine as build
WORKDIR /app

# References the first stage using the label
COPY --from=clone /app/iDIA_Rules /app

RUN mvn install:install-file -Dfile=./lib/opencsv-2.3.jar -DgroupId=org.opencsv -DartifactId=opencsv -Dversion=2.3 -Dpackaging=jar
RUN mvn install:install-file -Dfile=./lib/hibernate-core-3.3.0.SP1.jar -DgroupId=hibernate -DartifactId=hibernate -Dversion=3.3.0 -Dpackaging=jar
RUN mvn install:install-file -Dfile=./lib/hibernate-annotations-3.4.0.GA.jar -DgroupId=hibernate -DartifactId=hibernate-annotations -Dversion=3.4.0 -Dpackaging=jar
RUN mvn install:install-file -Dfile=./lib/hibernate-commons-annotations-3.1.0.GA.jar -DgroupId=hibernate -DartifactId=hibernate-commons-annotations -Dversion=3.1.0 -Dpackaging=jar
RUN mvn install:install-file -Dfile=./lib/ehcache-core-2.1.0.jar -DgroupId=ehcache -DartifactId=ehcache-core -Dversion=2.1.0 -Dpackaging=jar
RUN mvn install:install-file -Dfile=./lib/antlr.jar -DgroupId=antlr -DartifactId=antlr -Dversion=unknown -Dpackaging=jar
RUN mvn install

# Might not be necessary
FROM openjdk:8-jre-alpine

# Need to add PostgreSQL commands

WORKDIR /app
COPY config.properties /app/config.properties

RUN mkdir simulated-run-output

WORKDIR /app
COPY --from=clone /app/iDIA_Rules/runOverBanner.sh /app
RUN apk update && apk add bash
RUN ["chmod", "+x", "/app/runOverBanner.sh"]
RUN bash runOverBanner.sh output
# WORKDIR /app
# ENTRYPOINT ["/usr/bin/bash", "-c", "/app/runOverBanner.sh output"]
# ENTRYPOINT ["/app/runOverBanner.sh output"]
