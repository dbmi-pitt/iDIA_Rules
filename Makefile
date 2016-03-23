compile:
	mvn install


run: compile
	java -jar target/droolstest-1.0.jar
