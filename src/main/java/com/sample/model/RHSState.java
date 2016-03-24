package com.sample.model;

import edu.pitt.dbmi.ohdsiv5.db.DrugExposure;
import edu.pitt.dbmi.ohdsiv5.db.DrugEra;
import edu.pitt.dbmi.ohdsiv5.db.Person;


public class RHSState {
    private String stateName;
    private String state;
    private Person person;
    //private DrugExposure dexp1;
    //private DrugExposure dexp2;
	
    public RHSState() {
	super();
	// TODO Auto-generated constructor stub
    }

    //public RHSState( String stateName, String state, Person person, DrugExposure dexp1, DrugExposure dexp2) {
    public RHSState( String stateName, String state, Person person) {
	super();
	this.stateName = stateName;
	this.state = state;
	this.person = person;
	//this.dexp1 = dexp1;
	//this.dexp2 = dexp2; 	
    }

    public int hashCode()
    {
	//int hashVal = 37*(stateName.hashCode() + state.hashCode() + person.getPersonId().hashCode() + dexp1.getDrugExposureId().hashCode() + dexp2.getDrugExposureId().hashCode());
	int hashVal = 37*(stateName.hashCode() + state.hashCode() + person.getPersonId().hashCode());	
	return hashVal;
    }
	
    public boolean equals( Object obj )
    {
	boolean flag = false;
	RHSState rhs = ( RHSState )obj;
	//if( rhs.stateName == stateName && rhs.state == state && rhs.person.getPersonId() == person.getPersonId() && rhs.dexp1.getDrugExposureId() == dexp1.getDrugExposureId() && rhs.dexp2.getDrugExposureId() == dexp2.getDrugExposureId() )
	if( rhs.stateName == stateName && rhs.state == state && rhs.person.getPersonId() == person.getPersonId())
	    flag = true;
	return flag;
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
    // public DrugExposure getDexp1() {
    // 	return dexp1;
    // }
    // public void setDexp1(DrugExposure dexp1) {
    // 	this.dexp1 = dexp1;
    // }
    // public DrugExposure getDexp2() {
    // 	return dexp2;
    // }
    // public void setDexp2(DrugExposure dexp2) {
    // 	this.dexp2 = dexp2;
    // }
}
