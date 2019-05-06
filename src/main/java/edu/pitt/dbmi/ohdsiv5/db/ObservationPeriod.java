package edu.pitt.dbmi.ohdsiv5.db;

import java.util.Calendar;



public class ObservationPeriod  {


    // Constructors

    /** default constructor */
    public ObservationPeriod() {
    }

    public ObservationPeriod(Calendar observationPeriodStartDate, Calendar observationPeriodEndDate, Long personId, Long periodTypeConceptId) {
	super();
	this.observationPeriodStartDate = observationPeriodStartDate;
	this.observationPeriodEndDate = observationPeriodEndDate;
	this.personId = personId;
	this.periodTypeConceptId = periodTypeConceptId;
    }

   
    // Property accessors
                    Long observationPeriodId;
    public Long getObservationPeriodId() {
        return this.observationPeriodId;
    }    
    public void setObservationPeriodId(Long observationPeriodId) {
        this.observationPeriodId = observationPeriodId;
    }


        Calendar observationPeriodStartDate;
    public void setObservationPeriodStartDate(Calendar observationPeriodStartDate) {
	this.observationPeriodStartDate = observationPeriodStartDate;
    }    
    public Calendar getObservationPeriodStartDate() {
	return observationPeriodStartDate;
    }
 


        Calendar observationPeriodEndDate;
    public void setObservationPeriodEndDate(Calendar observationPeriodEndDate) {
	this.observationPeriodEndDate = observationPeriodEndDate;
    }    
    public Calendar getObservationPeriodEndDate() {
	return observationPeriodEndDate;
    }
    

        Long personId;
    public Long getPersonId() {
	return personId;
    }
    public void setPersonId(Long personId) {
	this.personId = personId;
    }

        Long periodTypeConceptId; // 35124300, MDS
    public Long getPeriodTypeConceptId() {
	return periodTypeConceptId;
    }
    public void setPeriodTypeConceptId(Long periodTypeConceptId) {
	this.periodTypeConceptId = periodTypeConceptId;
    }
}
