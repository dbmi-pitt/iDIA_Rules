FROM alpine/git
WORKDIR /app
RUN git clone https://github.com/rkboyce/iDIA_Rules.git

ENV date 10221029
# since this is in the same directory maybe dont need /iDEA-Rules
ENV config_file config.properties

FROM maven:3.5-jdk-8-alpine
WORKDIR /app
COPY --from=0 /app/iDIA_Rules /app 

RUN mvn install:install-file -Dfile=./lib/opencsv-2.3.jar -DgroupId=org.opencsv -DartifactId=opencsv -Dversion=2.3 -Dpackaging=jar
RUN mvn install:install-file -Dfile=./lib/hibernate-core-3.3.0.SP1.jar -DgroupId=hibernate -DartifactId=hibernate -Dversion=3.3.0 -Dpackaging=jar
RUN mvn install:install-file -Dfile=./lib/hibernate-annotations-3.4.0.GA.jar -DgroupId=hibernate -DartifactId=hibernate-annotations -Dversion=3.4.0 -Dpackaging=jar
RUN mvn install:install-file -Dfile=./lib/hibernate-commons-annotations-3.1.0.GA.jar -DgroupId=hibernate -DartifactId=hibernate-commons-annotations -Dversion=3.1.0 -Dpackaging=jar
RUN mvn install:install-file -Dfile=./lib/ehcache-core-2.1.0.jar -DgroupId=ehcache -DartifactId=ehcache-core -Dversion=2.1.0 -Dpackaging=jar
RUN mvn install:install-file -Dfile=./lib/antlr.jar -DgroupId=antlr -DartifactId=antlr -Dversion=unknown -Dpackaging=jar
RUN mvn install

# Might not be necessary
FROM openjdk:8-jre-alpine
#WORKDIR /iDEA-Rules/

# Need to add PostgreSQL commands

COPY config.properties ${config_file}

RUN mkdir banner-run-${date}

WORKDIR /app
CMD ["runOverBanner.sh"] [${date}]