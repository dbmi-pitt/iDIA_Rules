package edu.pitt.dbmi.ohdsiv5.db;
               # Replace Windows newlines with Unix newlines// Generated Aug 25, 2010 7:28:02 AM by Hibernate Tools 3.1.0.beta4
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlinesimport java.util.Calendar;
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines/**
               # Replace Windows newlines with Unix newlines * Concept generated by hbm2java
               # Replace Windows newlines with Unix newlines */
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlinespublic class Concept  {
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    // Fields    
               # Replace Windows newlines with Unix newlines    private Integer conceptId;
               # Replace Windows newlines with Unix newlines    private String conceptName;
               # Replace Windows newlines with Unix newlines    private String domainId;
               # Replace Windows newlines with Unix newlines    private String vocabularyId;
               # Replace Windows newlines with Unix newlines    private String conceptClassId;
               # Replace Windows newlines with Unix newlines    private String standardConcept;
               # Replace Windows newlines with Unix newlines    private String conceptCode;
               # Replace Windows newlines with Unix newlines    private Calendar validStartDate;
               # Replace Windows newlines with Unix newlines    private Calendar validEndDate;
               # Replace Windows newlines with Unix newlines    private String invalidReason;
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    // Constructors
               # Replace Windows newlines with Unix newlines    /** default constructor */
               # Replace Windows newlines with Unix newlines    public Concept() {
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    /** minimal constructor */
               # Replace Windows newlines with Unix newlines    public Concept(Integer conceptId, String conceptName, String vocabularyId, String conceptCode) {
               # Replace Windows newlines with Unix newlines	this.conceptId = conceptId;
               # Replace Windows newlines with Unix newlines	this.conceptName = conceptName;
               # Replace Windows newlines with Unix newlines	this.vocabularyId = vocabularyId;
               # Replace Windows newlines with Unix newlines	this.conceptCode = conceptCode;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines    
               # Replace Windows newlines with Unix newlines    /** full constructor */
               # Replace Windows newlines with Unix newlines    public Concept(Integer conceptId, String conceptName, String vocabularyId, String conceptCode, String conceptClassId) {
               # Replace Windows newlines with Unix newlines	this.conceptId = conceptId;
               # Replace Windows newlines with Unix newlines	this.conceptName = conceptName;
               # Replace Windows newlines with Unix newlines	this.vocabularyId = vocabularyId;
               # Replace Windows newlines with Unix newlines	this.conceptCode = conceptCode;
               # Replace Windows newlines with Unix newlines	this.conceptClassId = conceptClassId;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines   
               # Replace Windows newlines with Unix newlines    // Property accessors
               # Replace Windows newlines with Unix newlines    public Integer getConceptId() {
               # Replace Windows newlines with Unix newlines	return conceptId;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines    public void setConceptId(Integer conceptId) {
               # Replace Windows newlines with Unix newlines	this.conceptId = conceptId;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines    
               # Replace Windows newlines with Unix newlines        public String getConceptName() {
               # Replace Windows newlines with Unix newlines	return conceptName;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines    public void setConceptName(String conceptName) {
               # Replace Windows newlines with Unix newlines	this.conceptName = conceptName;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines   
               # Replace Windows newlines with Unix newlines        public String getDomainId() {
               # Replace Windows newlines with Unix newlines	return domainId;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines    public void setDomainId(String domainId) {
               # Replace Windows newlines with Unix newlines	this.domainId = domainId;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines    
               # Replace Windows newlines with Unix newlines        public String getVocabularyId() {
               # Replace Windows newlines with Unix newlines	return vocabularyId;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines    public void setVocabularyId(String vocabularyId) {
               # Replace Windows newlines with Unix newlines	this.vocabularyId = vocabularyId;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines        public String getConceptClassId() {
               # Replace Windows newlines with Unix newlines	return conceptClassId;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines    public void setConceptClassId(String conceptClassId) {
               # Replace Windows newlines with Unix newlines	this.conceptClassId = conceptClassId;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines        public String getStandardConcept() {
               # Replace Windows newlines with Unix newlines	return standardConcept;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines    public void setStandardConcept(String standardConcept) {
               # Replace Windows newlines with Unix newlines	this.standardConcept = standardConcept;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines        public String getConceptCode() {
               # Replace Windows newlines with Unix newlines	return conceptCode;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines    public void setConceptCode(String conceptCode) {
               # Replace Windows newlines with Unix newlines	this.conceptCode = conceptCode;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines        public Calendar getValidStartDate() {
               # Replace Windows newlines with Unix newlines	return validStartDate;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines    public void setValidStartDate(Calendar validStartDate) {
               # Replace Windows newlines with Unix newlines	this.validStartDate = validStartDate;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines        public Calendar getValidEndDate() {
               # Replace Windows newlines with Unix newlines	return validEndDate;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines    public void setValidEndDate(Calendar validEndDate) {
               # Replace Windows newlines with Unix newlines	this.validEndDate = validEndDate;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines        public String getInvalidReason() {
               # Replace Windows newlines with Unix newlines	return invalidReason;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines    public void setInvalidReason(String invalidReason) {
               # Replace Windows newlines with Unix newlines	this.invalidReason = invalidReason;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines}
               # Replace Windows newlines with Unix newlines