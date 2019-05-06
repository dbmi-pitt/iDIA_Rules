package edu.pitt.dbmi.ohdsiv5.db;

import java.util.Calendar;


/**
 * ConditionOccurrence 
 */
@Entity
@Table(name="CONDITION_OCCURRENCE")

public class ConditionOccurrence  {

    // Constructors

    /** default constructor */
    public ConditionOccurrence() {
    }
    
    /** full constructor */
    public ConditionOccurrence(Long personId, Calendar conditionStartDate, Calendar conditionEndDate, Integer occurrenceType, Integer conditionConceptID, String sourceConditionCode, Integer conditionSourceConceptId) {
	super();
        this.personId = personId;
	this.conditionStartDate = conditionStartDate;
	this.conditionEndDate = conditionEndDate;
	this.occurrenceType = occurrenceType;
	this.conditionConceptID = conditionConceptID;
	this.sourceConditionCode = sourceConditionCode;
	this.associatedProviderId = 9999; // No provider
	this.conditionSourceConceptId = conditionSourceConceptId;
    }
    
  
    // Property accessors
    @Id
    @Column(name = "CONDITION_OCCURRENCE_ID", nullable = false, insertable = false, updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="HIBERNATE_SEQUENCE_GENERATOR")
    @SequenceGenerator(name="HIBERNATE_SEQUENCE_GENERATOR", sequenceName="HIBERNATE_SEQUENCE", initialValue = 1, allocationSize = 1)
    Long conditionOccurrenceId;
    public Long getConditionOccurrenceId() {
        return this.conditionOccurrenceId;
    }    
    public void setConditionOccurrenceId(Long id) {
        this.conditionOccurrenceId = id;
    }
    
    @Column(name="PERSON_ID", unique=false, nullable=false, insertable=true, updatable=true, precision=8, scale=0)
    Long personId;
    public Long getPersonId() {
	return personId;
    }
    public void setPersonId(Long personId) {
	this.personId = personId;
    }

    @Column(name="CONDITION_CONCEPT_ID", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0)   
    Integer conditionConceptID;
    public void setConditionConceptId(Integer condId) { 
	this.conditionConceptID = condId;
    }
    public Integer getConditionConceptId() { 
	return conditionConceptID;
    }
 

    @Column(name="CONDITION_START_DATE", unique=false, nullable=false, insertable=true, updatable=true, length=7)
    Calendar conditionStartDate;
    public void setConditionStartDate(Calendar conditionStartDate) {
	this.conditionStartDate = conditionStartDate;
    }    
    public Calendar getConditionStartDate() {
	return conditionStartDate;
    }

    @Column(name="CONDITION_END_DATE", unique=false, nullable=true, insertable=true, updatable=true, length=7) 
    Calendar conditionEndDate;
    public void setConditionEndDate(Calendar conditionEndDate) {
	this.conditionEndDate = conditionEndDate;
    }
    public Calendar getConditionEndDate() {
	return conditionEndDate;
    }
    
    @Column(name="CONDITION_TYPE_CONCEPT_ID", unique=false, nullable=false, insertable=true, updatable=true, precision=8, scale=0)
    Integer occurrenceType;
    public void setConditionOccurrenceType(Integer occurrenceType) {
	this.occurrenceType = occurrenceType;
    }
    public Integer getConditionOccurrenceType() { 
	return occurrenceType;
    }
 

   @Column(name="STOP_REASON", unique=false, nullable=true, insertable=true, updatable=true, length=20)
   String stopReason;
   public void setStopReason(String reason) {
        this.stopReason = reason;
   }
   public String getStopReason() { 
        return stopReason;   
   }

	@Column(name="PROVIDER_ID", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0)
    Integer associatedProviderId;
	public Integer getAssociatedProviderId() {
		return associatedProviderId;
	}


	/**
	 * @param associatedProviderId the associatedProviderId to set
	 */

	public void setAssociatedProviderId(Integer associatedProviderId) {
		this.associatedProviderId = associatedProviderId;
	}


    @Column(name="VISIT_OCCURRENCE_ID", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0)
    Integer visitOccurrenceId;
    public Integer getVisitOccurrenceId() {
	return visitOccurrenceId;
    }
    

    /**
     * @param visitOccurrenceId the visitOccurrenceId to set
     */
    
    public void setVisitOccurrenceId(Integer visitOccurrenceId) {
	this.visitOccurrenceId = visitOccurrenceId;
    }


    @Column(name="CONDITION_SOURCE_VALUE", unique=false, nullable=false, insertable=true, updatable=true, length=50)
    String sourceConditionCode;
    public void setSourceConditionCode(String mdsIcd) {
	this.sourceConditionCode = mdsIcd;
    }
    public String getSourceConditionCode() { 
	return sourceConditionCode;
    }

    @Column(name="CONDITION_SOURCE_CONCEPT_ID", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0)
    Integer conditionSourceConceptId;
    public Integer getConditionSourceConceptId() {
	return conditionSourceConceptId;
    }
    public void setConditionSourceConceptId(Integer conditionSourceConceptId) {
	this.conditionSourceConceptId = conditionSourceConceptId;
    }
}
