package edu.pitt.dbmi.ohdsiv5.db;

import java.util.Calendar;


@Entity
@Table(name="OBSERVATION_PERIOD")

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
    @Id
    @Column(name="OBSERVATION_PERIOD_ID", unique=false, nullable=false, insertable=true, updatable=true, precision=15, scale=0) 
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="HIBERNATE_SEQUENCE_GENERATOR")
    @SequenceGenerator(name="HIBERNATE_SEQUENCE_GENERATOR", sequenceName="HIBERNATE_SEQUENCE", initialValue = 1, allocationSize = 1)
    Long observationPeriodId;
    public Long getObservationPeriodId() {
        return this.observationPeriodId;
    }    
    public void setObservationPeriodId(Long observationPeriodId) {
        this.observationPeriodId = observationPeriodId;
    }


    @Column(name="OBSERVATION_PERIOD_START_DATE", unique=false, nullable=true, insertable=true, updatable=true, length=7) 
    Calendar observationPeriodStartDate;
    public void setObservationPeriodStartDate(Calendar observationPeriodStartDate) {
	this.observationPeriodStartDate = observationPeriodStartDate;
    }    
    public Calendar getObservationPeriodStartDate() {
	return observationPeriodStartDate;
    }
 


    @Column(name="OBSERVATION_PERIOD_END_DATE", unique=false, nullable=true, insertable=true, updatable=true, length=7) 
    Calendar observationPeriodEndDate;
    public void setObservationPeriodEndDate(Calendar observationPeriodEndDate) {
	this.observationPeriodEndDate = observationPeriodEndDate;
    }    
    public Calendar getObservationPeriodEndDate() {
	return observationPeriodEndDate;
    }
    

    @Column(name="PERSON_ID", unique=false, nullable=false, insertable=true, updatable=true, precision=12, scale=0) 
    Long personId;
    public Long getPersonId() {
	return personId;
    }
    public void setPersonId(Long personId) {
	this.personId = personId;
    }

    @Column(name="PERIOD_TYPE_CONCEPT_ID", unique=false, nullable=false, insertable=true, updatable=true, precision=12, scale=0)
    Long periodTypeConceptId; // 35124300, MDS
    public Long getPeriodTypeConceptId() {
	return periodTypeConceptId;
    }
    public void setPeriodTypeConceptId(Long periodTypeConceptId) {
	this.periodTypeConceptId = periodTypeConceptId;
    }
}
