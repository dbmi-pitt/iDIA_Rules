package edu.pitt.dbmi.ohdsiv5.db;

import java.util.Calendar;



@Entity
@Table(name="CONDITION_ERA")

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
    @Id
    @Column(name = "CONDITION_ERA_ID", nullable = false, insertable = false, updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="HIBERNATE_SEQUENCE_GENERATOR")
    @SequenceGenerator(name="HIBERNATE_SEQUENCE_GENERATOR", sequenceName="HIBERNATE_SEQUENCE", initialValue = 1, allocationSize = 1)
    Long conditionEraId;
    public Long getConditionEraId() {
        return this.conditionEraId;
    }    
    public void setConditionEraId(Long id) {
        this.conditionEraId = id;
    }

   @Column(name="PERSON_ID", unique=false, nullable=false, insertable=true, updatable=true, precision=12, scale=0) 
    Long personId;
    public Long getPersonId() {
	return personId;
    }
    public void setPersonId(Long personId) {
	this.personId = personId;
    }


    @Column(name="CONDITION_ERA_START_DATE", unique=false, nullable=true, insertable=true, updatable=true, length=7) 
    Calendar conditionEraStartDate;
    public void setConditionEraStartDate(Calendar conditionEraStartDate) {
	this.conditionEraStartDate = conditionEraStartDate;
    }    
    public Calendar getConditionEraStartDate() {
	return conditionEraStartDate;
    }



    @Column(name="CONDITION_ERA_END_DATE", unique=false, nullable=true, insertable=true, updatable=true, length=7) 
    Calendar conditionEraEndDate;
    public void setConditionEraEndDate(Calendar conditionEraEndDate) {
	this.conditionEraEndDate = conditionEraEndDate;
    }    
    public Calendar getConditionEraEndDate() {
	return conditionEraEndDate;
    }

    @Column(name="CONDITION_CONCEPT_ID", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0) 
    Integer conditionConceptID;
    public void setConditionConceptId(Integer condId) { 
	this.conditionConceptID = condId;
    }
    public Integer getConditionConceptId() { 
	return conditionConceptID;
    }

    @Column(name="CONDITION_OCCURRENCE_COUNT", unique=false, nullable=true, insertable=true, updatable=true, precision=4, scale=0)
    Integer conditionOccurrenceCount;
    public void setConditionOccurrenceCount(Integer conditionOccurrenceCount) { 
	this.conditionOccurrenceCount = conditionOccurrenceCount;
    }
    public Integer getConditionOccurrenceCount() { 
	return conditionOccurrenceCount;
    }

}
