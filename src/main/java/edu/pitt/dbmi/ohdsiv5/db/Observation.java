package edu.pitt.dbmi.ohdsiv5.db;

import java.util.Calendar;


/**
 * Observation 
 */
@Entity
@Table(name="OBSERVATION")

public class Observation  {
 
    // Constructors

    /** default constructor */
    public Observation() {
    }

    // create Observation for case where the value is a number
    // concept. Pass TRUE for checkVocab if the observation concept is
    // thought to be in the Standard Vocabulary 
    public Observation(Long personId, String sourceObsCode,
		       Integer obsConceptId, Integer obsType, double obsValueAsNumber, Calendar obsDate, Boolean checkVocab) {
		super();
		this.personId = personId;
		this.sourceObsCode = sourceObsCode;
		if (checkVocab == true)
		    this.obsConceptId = SourceToConceptMap.getConceptId(sourceObsCode);
		else if (obsConceptId != null)
		    this.obsConceptId = obsConceptId;
		else
		    this.obsConceptId = 0;
		this.obsValueAsNumber = obsValueAsNumber;
		this.obsType = obsType;
		this.obsUnitsConceptId = obsUnitsConceptId;
		this.obsDate = obsDate;
		this.providerId = 9999; // No provider
	}

    // create Observation for case where the value is a number
    // concept and the observation concept id is known
    public Observation(Long personId, Integer observationId, String sourceObsCode,
		       double obsValueAsNumber, Integer obsType, Calendar obsDate) {
		super();
		this.personId = personId;
		this.obsConceptId = observationId;
		this.sourceObsCode = sourceObsCode;
		this.obsValueAsNumber = obsValueAsNumber;
		this.obsType = obsType;
		this.obsUnitsConceptId = obsUnitsConceptId;
		this.obsDate = obsDate;
		this.providerId = 9999; // No provider
	}

    // create Observation for case where the value is a string. Pass
    // TRUE for checkVocab if the observation concept is thought to be
    // in the Standard Vocabulary. NOTE: obsConceptId will not be used if
    // checkVocab is TRUE
    public Observation(Long personId, String sourceObsCode, 
		       String obsValueAsString, Integer obsType, Calendar obsDate, Boolean checkVocab, Integer obsConceptId) {
		super();
		this.personId = personId;
		if ( sourceObsCode != null && sourceObsCode.length() > 50)
		    sourceObsCode = sourceObsCode.substring(0, 49); // matches the max string length of source codes 
		this.sourceObsCode = sourceObsCode;
		if (checkVocab == true)
		    this.obsConceptId = SourceToConceptMap.getConceptId(sourceObsCode);
		else if (obsConceptId != null)
		    this.obsConceptId = obsConceptId;
		else
		    this.obsConceptId = 0;
		this.obsValueAsString = obsValueAsString;
		this.obsType = obsType;
		this.obsDate = obsDate;
		this.providerId = 9999; // No provider
	}


        // create Observation for case where the value is a concept. Pass
        // TRUE for checkVocab if the observation concept is thought to be
        // in the Standard Vocabulary. NOTE: obsConceptId will not be used if
        // checkVocab is TRUE
        public Observation(Long personId, String sourceObsCode, 
		       Long obsValueAsConceptId, Integer obsType, Calendar obsDate, Boolean checkVocab, Integer obsConceptId) {
		super();
		this.personId = personId;
		if ( sourceObsCode != null && sourceObsCode.length() > 49)
		    sourceObsCode = sourceObsCode.substring(0, 49); // matches the max string length of source codes 
		this.sourceObsCode = sourceObsCode;
		if (checkVocab == true)
		    this.obsConceptId = SourceToConceptMap.getConceptId(sourceObsCode);
		else if (obsConceptId != null)
		    this.obsConceptId = obsConceptId;
		else
		    this.obsConceptId = 0;
		this.obsValueAsConceptId = obsValueAsConceptId;
		this.obsType = obsType;
		this.obsDate = obsDate;
		this.providerId = 9999; // No provider
	}

 
	/**
	 * @return the obsOccurrenceId
	 */
   	@Id
	@Column(name = "OBSERVATION_ID", nullable = false, insertable = false, updatable = false)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="HIBERNATE_SEQUENCE_GENERATOR")
	@SequenceGenerator(name="HIBERNATE_SEQUENCE_GENERATOR", sequenceName="HIBERNATE_SEQUENCE", initialValue = 1, allocationSize = 1)
        Long obsOccurrenceId = null;
	public Long getObsOccurrenceId() {
		return obsOccurrenceId;
	}


	/**
	 * @param obsOccurrenceId the obsOccurrenceId to set
	 */
	public void setObsOccurrenceId(Long obsOccurrenceId) {
		this.obsOccurrenceId = obsOccurrenceId;
	}


	/**
	 * @return the personId
	 */
    @Column(name="PERSON_ID", unique=false, nullable=false, insertable=true, updatable=true, precision=12, scale=0)
    Long personId;
	public Long getPersonId() {
		return personId;
	}


	/**
	 * @param personId the personId to set
	 */
	public void setPersonId(Long personId) {
		this.personId = personId;
	}


	/**
	 * @return the sourceObsCode
	 */
   @Column(name="OBSERVATION_SOURCE_VALUE", unique=false, nullable=true, insertable=true, updatable=true, length=50) 
   String sourceObsCode;
	public String getSourceObsCode() {
		return sourceObsCode;
	}


	/**
	 * @param sourceObsCode the sourceObsCode to set
	 */
	public void setSourceObsCode(String sourceObsCode) {
		this.sourceObsCode = sourceObsCode;
	}


	/**
	 * @return the obsConceptId
	 */
	@Column(name="OBSERVATION_CONCEPT_ID", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0) 
	Integer obsConceptId;	
	public Integer getObsConceptId() {
		return obsConceptId;
	}


	/**
	 * @param obsConceptId the obsConceptId to set
	 */
	public void setObsConceptId(Integer obsConceptId) {
		this.obsConceptId = obsConceptId;
	}


	/**
	 * @return the obsValueAsNumber
	 */
    @Column(name="VALUE_AS_NUMBER", unique=false, nullable=true, insertable=true, updatable=true, precision=14, scale=3) 
    Double obsValueAsNumber;
	public Double getObsValueAsNumber() {
		return obsValueAsNumber;
	}


	/**
	 * @param obsValueAsNumber the obsValueAsNumber to set
	 */
	public void setObsValueAsNumber(Double obsValueAsNumber) {
		this.obsValueAsNumber = obsValueAsNumber;
	}


	/**
	 * @return the obsDate
	 */
	@Column(name="OBSERVATION_DATE", unique=false, nullable=true, insertable=true, updatable=true, length=7)
	Calendar obsDate;
	public Calendar getObsDate() {
		return obsDate;
	}


	/**
	 * @param obsDate the obsDate to set
	 */
	public void setObsDate(Calendar obsDate) {
		this.obsDate = obsDate;
	}

    @Column(name="OBSERVATION_TYPE_CONCEPT_ID", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0)
    Integer obsType;
	public Integer getObsType() {
		return obsType;
	}


	/**
	 * @param obsType the obsType to set
	 */
	public void setObsType(Integer obsType) {
		this.obsType = obsType;
	}


	/**
	 * @return the obsValueAsString
	 */
    @Column(name="VALUE_AS_STRING", unique=false, nullable=true, insertable=true, updatable=true, length=60)
    String obsValueAsString;
	public String getObsValueAsString() {
		return obsValueAsString;
	}


	/**
	 * @param obsValueAsString the obsValueAsString to set
	 */
	public void setObsValueAsString(String obsValueAsString) {
		this.obsValueAsString = obsValueAsString;
	}


	/**
	 * @return the obsValueAsConceptId
	 */

    @Column(name="VALUE_AS_CONCEPT_ID", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0)  
    Long obsValueAsConceptId;
    public Long getObsValueAsConceptId() {
		return obsValueAsConceptId;
	}


	/**
	 * @param obsValueAsConceptId the obsValueAsConceptId to set
	 */
	public void setObsValueAsConceptId(Long obsValueAsConceptId) {
		this.obsValueAsConceptId = obsValueAsConceptId;
	}


	/**
	 * @return the obsUnitsConceptId
	 */
	@Column(name="UNIT_CONCEPT_ID", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0)
    Long obsUnitsConceptId;
	public Long getObsUnitsConceptId() {
		return obsUnitsConceptId;
	}


	/**
	 * @param obsUnitsConceptId the obsUnitsConceptId to set
	 */

	public void setObsUnitsConceptId(Long obsUnitsConceptId) {
		this.obsUnitsConceptId = obsUnitsConceptId;
	}
   
    @Column(name="PROVIDER_ID", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0)
    Integer providerId;
	public Integer getProviderId() {
		return providerId;
	}


	/**
	 * @param providerId the providerId to set
	 */

	public void setProviderId(Integer providerId) {
		this.providerId = providerId;
	}

    @Column(name="QUALIFIER_CONCEPT_ID", unique=false, insertable=true, nullable=true, updatable=true, precision=8, scale=0)
    Integer qualifierConceptId;
    public Integer getQualifierConceptId() {
	return this.qualifierConceptId;
    }
    public void setQualifierConceptId(Integer qualifierConceptId) {
	this.qualifierConceptId = qualifierConceptId;
    }

    @Column(name="OBSERVATION_SOURCE_CONCEPT_ID", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0)
    Integer observationSourceConceptId;
    public Integer getObservationSourceConceptId() {
	return this.observationSourceConceptId;
    }
    public void setObservationSourceConceptId(Integer observationSourceConceptId) {
	this.observationSourceConceptId = observationSourceConceptId;
    }

    @Column(name="QUALIFIER_SOURCE_VALUE", unique=false, insertable=true, nullable=true, updatable=true, length=50)
    String qualifierSourceValue;
    public String getQualifierSourceValue() {
	return this.qualifierSourceValue;
    }
    public void setQualifierSourceValue(String qualifierSourceValue) {
	this.qualifierSourceValue = qualifierSourceValue;
    }
    
}
