package edu.pitt.dbmi.ohdsiv5.db;
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlinesimport java.util.Calendar;
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlinesimport javax.persistence.Column;
               # Replace Windows newlines with Unix newlinesimport javax.persistence.Entity;
               # Replace Windows newlines with Unix newlinesimport javax.persistence.GeneratedValue;
               # Replace Windows newlines with Unix newlinesimport javax.persistence.GenerationType;
               # Replace Windows newlines with Unix newlinesimport javax.persistence.Id;
               # Replace Windows newlines with Unix newlinesimport javax.persistence.SequenceGenerator;
               # Replace Windows newlines with Unix newlinesimport javax.persistence.Table;
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines/**
               # Replace Windows newlines with Unix newlines * Observation 
               # Replace Windows newlines with Unix newlines */
               # Replace Windows newlines with Unix newlines@Entity
               # Replace Windows newlines with Unix newlines@Table(name="OBSERVATION")
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlinespublic class Observation  implements java.io.Serializable {
               # Replace Windows newlines with Unix newlines 
               # Replace Windows newlines with Unix newlines    // Constructors
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    /** default constructor */
               # Replace Windows newlines with Unix newlines    public Observation() {
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    // create Observation for case where the value is a number
               # Replace Windows newlines with Unix newlines    // concept. Pass TRUE for checkVocab if the observation concept is
               # Replace Windows newlines with Unix newlines    // thought to be in the Standard Vocabulary 
               # Replace Windows newlines with Unix newlines    public Observation(Long personId, String sourceObsCode,
               # Replace Windows newlines with Unix newlines		       Integer obsConceptId, Integer obsType, double obsValueAsNumber, Calendar obsDate, Boolean checkVocab) {
               # Replace Windows newlines with Unix newlines		super();
               # Replace Windows newlines with Unix newlines		this.personId = personId;
               # Replace Windows newlines with Unix newlines		this.sourceObsCode = sourceObsCode;
               # Replace Windows newlines with Unix newlines		if (checkVocab == true)
               # Replace Windows newlines with Unix newlines		    this.obsConceptId = SourceToConceptMap.getConceptId(sourceObsCode);
               # Replace Windows newlines with Unix newlines		else if (obsConceptId != null)
               # Replace Windows newlines with Unix newlines		    this.obsConceptId = obsConceptId;
               # Replace Windows newlines with Unix newlines		else
               # Replace Windows newlines with Unix newlines		    this.obsConceptId = 0;
               # Replace Windows newlines with Unix newlines		this.obsValueAsNumber = obsValueAsNumber;
               # Replace Windows newlines with Unix newlines		this.obsType = obsType;
               # Replace Windows newlines with Unix newlines		this.obsUnitsConceptId = obsUnitsConceptId;
               # Replace Windows newlines with Unix newlines		this.obsDate = obsDate;
               # Replace Windows newlines with Unix newlines		this.providerId = 9999; // No provider
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    // create Observation for case where the value is a number
               # Replace Windows newlines with Unix newlines    // concept and the observation concept id is known
               # Replace Windows newlines with Unix newlines    public Observation(Long personId, Integer observationId, String sourceObsCode,
               # Replace Windows newlines with Unix newlines		       double obsValueAsNumber, Integer obsType, Calendar obsDate) {
               # Replace Windows newlines with Unix newlines		super();
               # Replace Windows newlines with Unix newlines		this.personId = personId;
               # Replace Windows newlines with Unix newlines		this.obsConceptId = observationId;
               # Replace Windows newlines with Unix newlines		this.sourceObsCode = sourceObsCode;
               # Replace Windows newlines with Unix newlines		this.obsValueAsNumber = obsValueAsNumber;
               # Replace Windows newlines with Unix newlines		this.obsType = obsType;
               # Replace Windows newlines with Unix newlines		this.obsUnitsConceptId = obsUnitsConceptId;
               # Replace Windows newlines with Unix newlines		this.obsDate = obsDate;
               # Replace Windows newlines with Unix newlines		this.providerId = 9999; // No provider
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    // create Observation for case where the value is a string. Pass
               # Replace Windows newlines with Unix newlines    // TRUE for checkVocab if the observation concept is thought to be
               # Replace Windows newlines with Unix newlines    // in the Standard Vocabulary. NOTE: obsConceptId will not be used if
               # Replace Windows newlines with Unix newlines    // checkVocab is TRUE
               # Replace Windows newlines with Unix newlines    public Observation(Long personId, String sourceObsCode, 
               # Replace Windows newlines with Unix newlines		       String obsValueAsString, Integer obsType, Calendar obsDate, Boolean checkVocab, Integer obsConceptId) {
               # Replace Windows newlines with Unix newlines		super();
               # Replace Windows newlines with Unix newlines		this.personId = personId;
               # Replace Windows newlines with Unix newlines		if ( sourceObsCode != null && sourceObsCode.length() > 50)
               # Replace Windows newlines with Unix newlines		    sourceObsCode = sourceObsCode.substring(0, 49); // matches the max string length of source codes 
               # Replace Windows newlines with Unix newlines		this.sourceObsCode = sourceObsCode;
               # Replace Windows newlines with Unix newlines		if (checkVocab == true)
               # Replace Windows newlines with Unix newlines		    this.obsConceptId = SourceToConceptMap.getConceptId(sourceObsCode);
               # Replace Windows newlines with Unix newlines		else if (obsConceptId != null)
               # Replace Windows newlines with Unix newlines		    this.obsConceptId = obsConceptId;
               # Replace Windows newlines with Unix newlines		else
               # Replace Windows newlines with Unix newlines		    this.obsConceptId = 0;
               # Replace Windows newlines with Unix newlines		this.obsValueAsString = obsValueAsString;
               # Replace Windows newlines with Unix newlines		this.obsType = obsType;
               # Replace Windows newlines with Unix newlines		this.obsDate = obsDate;
               # Replace Windows newlines with Unix newlines		this.providerId = 9999; // No provider
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines        // create Observation for case where the value is a concept. Pass
               # Replace Windows newlines with Unix newlines        // TRUE for checkVocab if the observation concept is thought to be
               # Replace Windows newlines with Unix newlines        // in the Standard Vocabulary. NOTE: obsConceptId will not be used if
               # Replace Windows newlines with Unix newlines        // checkVocab is TRUE
               # Replace Windows newlines with Unix newlines        public Observation(Long personId, String sourceObsCode, 
               # Replace Windows newlines with Unix newlines		       Long obsValueAsConceptId, Integer obsType, Calendar obsDate, Boolean checkVocab, Integer obsConceptId) {
               # Replace Windows newlines with Unix newlines		super();
               # Replace Windows newlines with Unix newlines		this.personId = personId;
               # Replace Windows newlines with Unix newlines		if ( sourceObsCode != null && sourceObsCode.length() > 49)
               # Replace Windows newlines with Unix newlines		    sourceObsCode = sourceObsCode.substring(0, 49); // matches the max string length of source codes 
               # Replace Windows newlines with Unix newlines		this.sourceObsCode = sourceObsCode;
               # Replace Windows newlines with Unix newlines		if (checkVocab == true)
               # Replace Windows newlines with Unix newlines		    this.obsConceptId = SourceToConceptMap.getConceptId(sourceObsCode);
               # Replace Windows newlines with Unix newlines		else if (obsConceptId != null)
               # Replace Windows newlines with Unix newlines		    this.obsConceptId = obsConceptId;
               # Replace Windows newlines with Unix newlines		else
               # Replace Windows newlines with Unix newlines		    this.obsConceptId = 0;
               # Replace Windows newlines with Unix newlines		this.obsValueAsConceptId = obsValueAsConceptId;
               # Replace Windows newlines with Unix newlines		this.obsType = obsType;
               # Replace Windows newlines with Unix newlines		this.obsDate = obsDate;
               # Replace Windows newlines with Unix newlines		this.providerId = 9999; // No provider
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines 
               # Replace Windows newlines with Unix newlines	/**
               # Replace Windows newlines with Unix newlines	 * @return the obsOccurrenceId
               # Replace Windows newlines with Unix newlines	 */
               # Replace Windows newlines with Unix newlines   	@Id
               # Replace Windows newlines with Unix newlines	@Column(name = "OBSERVATION_ID", nullable = false, insertable = false, updatable = false)
               # Replace Windows newlines with Unix newlines	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="HIBERNATE_SEQUENCE_GENERATOR")
               # Replace Windows newlines with Unix newlines	@SequenceGenerator(name="HIBERNATE_SEQUENCE_GENERATOR", sequenceName="HIBERNATE_SEQUENCE", initialValue = 1, allocationSize = 1)
               # Replace Windows newlines with Unix newlines        Long obsOccurrenceId = null;
               # Replace Windows newlines with Unix newlines	public Long getObsOccurrenceId() {
               # Replace Windows newlines with Unix newlines		return obsOccurrenceId;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines	/**
               # Replace Windows newlines with Unix newlines	 * @param obsOccurrenceId the obsOccurrenceId to set
               # Replace Windows newlines with Unix newlines	 */
               # Replace Windows newlines with Unix newlines	public void setObsOccurrenceId(Long obsOccurrenceId) {
               # Replace Windows newlines with Unix newlines		this.obsOccurrenceId = obsOccurrenceId;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines	/**
               # Replace Windows newlines with Unix newlines	 * @return the personId
               # Replace Windows newlines with Unix newlines	 */
               # Replace Windows newlines with Unix newlines    @Column(name="PERSON_ID", unique=false, nullable=false, insertable=true, updatable=true, precision=12, scale=0)
               # Replace Windows newlines with Unix newlines    Long personId;
               # Replace Windows newlines with Unix newlines	public Long getPersonId() {
               # Replace Windows newlines with Unix newlines		return personId;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines	/**
               # Replace Windows newlines with Unix newlines	 * @param personId the personId to set
               # Replace Windows newlines with Unix newlines	 */
               # Replace Windows newlines with Unix newlines	public void setPersonId(Long personId) {
               # Replace Windows newlines with Unix newlines		this.personId = personId;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines	/**
               # Replace Windows newlines with Unix newlines	 * @return the sourceObsCode
               # Replace Windows newlines with Unix newlines	 */
               # Replace Windows newlines with Unix newlines   @Column(name="OBSERVATION_SOURCE_VALUE", unique=false, nullable=true, insertable=true, updatable=true, length=50) 
               # Replace Windows newlines with Unix newlines   String sourceObsCode;
               # Replace Windows newlines with Unix newlines	public String getSourceObsCode() {
               # Replace Windows newlines with Unix newlines		return sourceObsCode;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines	/**
               # Replace Windows newlines with Unix newlines	 * @param sourceObsCode the sourceObsCode to set
               # Replace Windows newlines with Unix newlines	 */
               # Replace Windows newlines with Unix newlines	public void setSourceObsCode(String sourceObsCode) {
               # Replace Windows newlines with Unix newlines		this.sourceObsCode = sourceObsCode;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines	/**
               # Replace Windows newlines with Unix newlines	 * @return the obsConceptId
               # Replace Windows newlines with Unix newlines	 */
               # Replace Windows newlines with Unix newlines	@Column(name="OBSERVATION_CONCEPT_ID", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0) 
               # Replace Windows newlines with Unix newlines	Integer obsConceptId;	
               # Replace Windows newlines with Unix newlines	public Integer getObsConceptId() {
               # Replace Windows newlines with Unix newlines		return obsConceptId;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines	/**
               # Replace Windows newlines with Unix newlines	 * @param obsConceptId the obsConceptId to set
               # Replace Windows newlines with Unix newlines	 */
               # Replace Windows newlines with Unix newlines	public void setObsConceptId(Integer obsConceptId) {
               # Replace Windows newlines with Unix newlines		this.obsConceptId = obsConceptId;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines	/**
               # Replace Windows newlines with Unix newlines	 * @return the obsValueAsNumber
               # Replace Windows newlines with Unix newlines	 */
               # Replace Windows newlines with Unix newlines    @Column(name="VALUE_AS_NUMBER", unique=false, nullable=true, insertable=true, updatable=true, precision=14, scale=3) 
               # Replace Windows newlines with Unix newlines    Double obsValueAsNumber;
               # Replace Windows newlines with Unix newlines	public Double getObsValueAsNumber() {
               # Replace Windows newlines with Unix newlines		return obsValueAsNumber;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines	/**
               # Replace Windows newlines with Unix newlines	 * @param obsValueAsNumber the obsValueAsNumber to set
               # Replace Windows newlines with Unix newlines	 */
               # Replace Windows newlines with Unix newlines	public void setObsValueAsNumber(Double obsValueAsNumber) {
               # Replace Windows newlines with Unix newlines		this.obsValueAsNumber = obsValueAsNumber;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines	/**
               # Replace Windows newlines with Unix newlines	 * @return the obsDate
               # Replace Windows newlines with Unix newlines	 */
               # Replace Windows newlines with Unix newlines	@Column(name="OBSERVATION_DATE", unique=false, nullable=true, insertable=true, updatable=true, length=7)
               # Replace Windows newlines with Unix newlines	Calendar obsDate;
               # Replace Windows newlines with Unix newlines	public Calendar getObsDate() {
               # Replace Windows newlines with Unix newlines		return obsDate;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines	/**
               # Replace Windows newlines with Unix newlines	 * @param obsDate the obsDate to set
               # Replace Windows newlines with Unix newlines	 */
               # Replace Windows newlines with Unix newlines	public void setObsDate(Calendar obsDate) {
               # Replace Windows newlines with Unix newlines		this.obsDate = obsDate;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    @Column(name="OBSERVATION_TYPE_CONCEPT_ID", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0)
               # Replace Windows newlines with Unix newlines    Integer obsType;
               # Replace Windows newlines with Unix newlines	public Integer getObsType() {
               # Replace Windows newlines with Unix newlines		return obsType;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines	/**
               # Replace Windows newlines with Unix newlines	 * @param obsType the obsType to set
               # Replace Windows newlines with Unix newlines	 */
               # Replace Windows newlines with Unix newlines	public void setObsType(Integer obsType) {
               # Replace Windows newlines with Unix newlines		this.obsType = obsType;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines	/**
               # Replace Windows newlines with Unix newlines	 * @return the obsValueAsString
               # Replace Windows newlines with Unix newlines	 */
               # Replace Windows newlines with Unix newlines    @Column(name="VALUE_AS_STRING", unique=false, nullable=true, insertable=true, updatable=true, length=60)
               # Replace Windows newlines with Unix newlines    String obsValueAsString;
               # Replace Windows newlines with Unix newlines	public String getObsValueAsString() {
               # Replace Windows newlines with Unix newlines		return obsValueAsString;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines	/**
               # Replace Windows newlines with Unix newlines	 * @param obsValueAsString the obsValueAsString to set
               # Replace Windows newlines with Unix newlines	 */
               # Replace Windows newlines with Unix newlines	public void setObsValueAsString(String obsValueAsString) {
               # Replace Windows newlines with Unix newlines		this.obsValueAsString = obsValueAsString;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines	/**
               # Replace Windows newlines with Unix newlines	 * @return the obsValueAsConceptId
               # Replace Windows newlines with Unix newlines	 */
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    @Column(name="VALUE_AS_CONCEPT_ID", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0)  
               # Replace Windows newlines with Unix newlines    Long obsValueAsConceptId;
               # Replace Windows newlines with Unix newlines    public Long getObsValueAsConceptId() {
               # Replace Windows newlines with Unix newlines		return obsValueAsConceptId;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines	/**
               # Replace Windows newlines with Unix newlines	 * @param obsValueAsConceptId the obsValueAsConceptId to set
               # Replace Windows newlines with Unix newlines	 */
               # Replace Windows newlines with Unix newlines	public void setObsValueAsConceptId(Long obsValueAsConceptId) {
               # Replace Windows newlines with Unix newlines		this.obsValueAsConceptId = obsValueAsConceptId;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines	/**
               # Replace Windows newlines with Unix newlines	 * @return the obsUnitsConceptId
               # Replace Windows newlines with Unix newlines	 */
               # Replace Windows newlines with Unix newlines	@Column(name="UNIT_CONCEPT_ID", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0)
               # Replace Windows newlines with Unix newlines    Long obsUnitsConceptId;
               # Replace Windows newlines with Unix newlines	public Long getObsUnitsConceptId() {
               # Replace Windows newlines with Unix newlines		return obsUnitsConceptId;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines	/**
               # Replace Windows newlines with Unix newlines	 * @param obsUnitsConceptId the obsUnitsConceptId to set
               # Replace Windows newlines with Unix newlines	 */
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines	public void setObsUnitsConceptId(Long obsUnitsConceptId) {
               # Replace Windows newlines with Unix newlines		this.obsUnitsConceptId = obsUnitsConceptId;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines   
               # Replace Windows newlines with Unix newlines    @Column(name="PROVIDER_ID", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0)
               # Replace Windows newlines with Unix newlines    Integer providerId;
               # Replace Windows newlines with Unix newlines	public Integer getProviderId() {
               # Replace Windows newlines with Unix newlines		return providerId;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines	/**
               # Replace Windows newlines with Unix newlines	 * @param providerId the providerId to set
               # Replace Windows newlines with Unix newlines	 */
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines	public void setProviderId(Integer providerId) {
               # Replace Windows newlines with Unix newlines		this.providerId = providerId;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    @Column(name="QUALIFIER_CONCEPT_ID", unique=false, insertable=true, nullable=true, updatable=true, precision=8, scale=0)
               # Replace Windows newlines with Unix newlines    Integer qualifierConceptId;
               # Replace Windows newlines with Unix newlines    public Integer getQualifierConceptId() {
               # Replace Windows newlines with Unix newlines	return this.qualifierConceptId;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines    public void setQualifierConceptId(Integer qualifierConceptId) {
               # Replace Windows newlines with Unix newlines	this.qualifierConceptId = qualifierConceptId;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    @Column(name="OBSERVATION_SOURCE_CONCEPT_ID", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0)
               # Replace Windows newlines with Unix newlines    Integer observationSourceConceptId;
               # Replace Windows newlines with Unix newlines    public Integer getObservationSourceConceptId() {
               # Replace Windows newlines with Unix newlines	return this.observationSourceConceptId;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines    public void setObservationSourceConceptId(Integer observationSourceConceptId) {
               # Replace Windows newlines with Unix newlines	this.observationSourceConceptId = observationSourceConceptId;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    @Column(name="QUALIFIER_SOURCE_VALUE", unique=false, insertable=true, nullable=true, updatable=true, length=50)
               # Replace Windows newlines with Unix newlines    String qualifierSourceValue;
               # Replace Windows newlines with Unix newlines    public String getQualifierSourceValue() {
               # Replace Windows newlines with Unix newlines	return this.qualifierSourceValue;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines    public void setQualifierSourceValue(String qualifierSourceValue) {
               # Replace Windows newlines with Unix newlines	this.qualifierSourceValue = qualifierSourceValue;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines    
               # Replace Windows newlines with Unix newlines}
               # Replace Windows newlines with Unix newlines