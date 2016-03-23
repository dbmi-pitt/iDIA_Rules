package com.sample.model;

import edu.pitt.dbmi.ohdsiv5.db.DrugExposure;
import edu.pitt.dbmi.ohdsiv5.db.DrugEra;
import edu.pitt.dbmi.ohdsiv5.db.Person;


public class RHSState {
    private String stateName;
    private String state;
    private Person person;
    private DrugExposure dexp1;
    private DrugExposure dexp2;
	
    public RHSState() {
	super();
	// TODO Auto-generated constructor stub
    }

    public RHSState( String stateName, String state, Person person, DrugExposure dexp1, DrugExposure dexp2) {
	super();
	this.stateName = stateName;
	this.state = state;
	this.person = person;
	this. dexp1 = dexp1;
	this.dexp2 = dexp2; 	
    }
    public String getStateName() {
	return stateName;
    }
    public void setStateName(String stateName) {
	this.stateName = stateName;
    }
    public String getState() {
	return state;
    }
    public void setState(String state) {
	this.state = state;
    }
    public Person getPerson() {
	return person;
    }
    public void setPerson(Person person) {
	this.person = person;
    }
    public DrugExposure getDexp1() {
	return dexp1;
    }
    public void setDexp1(DrugExposure dexp1) {
	this.dexp1 = dexp1;
    }
    public DrugExposure getDexp2() {
	return dexp2;
    }
    public void setDexp2(DrugExposure dexp2) {
	this.dexp2 = dexp2;
    }

}
