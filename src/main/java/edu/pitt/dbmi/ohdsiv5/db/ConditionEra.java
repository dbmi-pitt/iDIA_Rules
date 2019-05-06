package edu.pitt.dbmi.ohdsiv5.db;

import java.util.Calendar;




public class ConditionEra  {

    // Constructors

    /** default constructor */
    public ConditionEra() {
    }

    public ConditionEra(Calendar conditionEraStartDate, Calendar conditionEraEndDate, Long personId, Integer conditionConceptID, Integer conditionOccurrenceCount) {
	super();
	this.conditionEraStartDate = conditionEraStartDate;
	this.conditionEraEndDate = conditionEraEndDate;
	this.personId = personId;
	this.conditionConceptID = conditionConceptID;
	this.conditionOccurrenceCount = conditionOccurrenceCount;
    }
   
    // Property accessors
                    Long conditionEraId;
    public Long getConditionEraId() {
        return this.conditionEraId;
    }    
    public void setConditionEraId(Long id) {
        this.conditionEraId = id;
    }

       Long personId;
    public Long getPersonId() {
	return personId;
    }
    public void setPersonId(Long personId) {
	this.personId = personId;
    }


        Calendar conditionEraStartDate;
    public void setConditionEraStartDate(Calendar conditionEraStartDate) {
	this.conditionEraStartDate = conditionEraStartDate;
    }    
    public Calendar getConditionEraStartDate() {
	return conditionEraStartDate;
    }



        Calendar conditionEraEndDate;
    public void setConditionEraEndDate(Calendar conditionEraEndDate) {
	this.conditionEraEndDate = conditionEraEndDate;
    }    
    public Calendar getConditionEraEndDate() {
	return conditionEraEndDate;
    }

        Integer conditionConceptID;
    public void setConditionConceptId(Integer condId) { 
	this.conditionConceptID = condId;
    }
    public Integer getConditionConceptId() { 
	return conditionConceptID;
    }

        Integer conditionOccurrenceCount;
    public void setConditionOccurrenceCount(Integer conditionOccurrenceCount) { 
	this.conditionOccurrenceCount = conditionOccurrenceCount;
    }
    public Integer getConditionOccurrenceCount() { 
	return conditionOccurrenceCount;
    }

}
