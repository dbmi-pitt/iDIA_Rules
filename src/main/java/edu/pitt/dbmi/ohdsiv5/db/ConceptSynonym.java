package edu.pitt.dbmi.ohdsiv5.db;
               # Replace Windows newlines with Unix newlines// Generated Aug 25, 2010 7:48:41 AM by Hibernate Tools 3.1.0.beta4
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlinesimport java.math.BigDecimal;
               # Replace Windows newlines with Unix newlinesimport javax.persistence.Column;
               # Replace Windows newlines with Unix newlinesimport javax.persistence.Entity;
               # Replace Windows newlines with Unix newlinesimport javax.persistence.Id;
               # Replace Windows newlines with Unix newlinesimport javax.persistence.Table;
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines/**
               # Replace Windows newlines with Unix newlines * ConceptSynonym generated by hbm2java
               # Replace Windows newlines with Unix newlines */
               # Replace Windows newlines with Unix newlines@Entity
               # Replace Windows newlines with Unix newlines@Table(name="CONCEPT_SYNONYM", uniqueConstraints = {  })
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlinespublic class ConceptSynonym  implements java.io.Serializable {
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    // Fields    
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines     private BigDecimal conceptSynonymId;
               # Replace Windows newlines with Unix newlines     private BigDecimal conceptId;
               # Replace Windows newlines with Unix newlines     private String descriptionName;
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    // Constructors
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    /** default constructor */
               # Replace Windows newlines with Unix newlines    public ConceptSynonym() {
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    
               # Replace Windows newlines with Unix newlines    /** full constructor */
               # Replace Windows newlines with Unix newlines    public ConceptSynonym(BigDecimal conceptSynonymId, BigDecimal conceptId, String descriptionName) {
               # Replace Windows newlines with Unix newlines        this.conceptSynonymId = conceptSynonymId;
               # Replace Windows newlines with Unix newlines        this.conceptId = conceptId;
               # Replace Windows newlines with Unix newlines        this.descriptionName = descriptionName;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines    
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines   
               # Replace Windows newlines with Unix newlines    // Property accessors
               # Replace Windows newlines with Unix newlines    @Id
               # Replace Windows newlines with Unix newlines    @Column(name="CONCEPT_SYNONYM_ID", unique=true, nullable=false, insertable=true, updatable=true, precision=22, scale=0)
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    public BigDecimal getConceptSynonymId() {
               # Replace Windows newlines with Unix newlines        return this.conceptSynonymId;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines    
               # Replace Windows newlines with Unix newlines    public void setConceptSynonymId(BigDecimal conceptSynonymId) {
               # Replace Windows newlines with Unix newlines        this.conceptSynonymId = conceptSynonymId;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines    @Column(name="CONCEPT_ID", unique=false, nullable=false, insertable=true, updatable=true, precision=22, scale=0)
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    public BigDecimal getConceptId() {
               # Replace Windows newlines with Unix newlines        return this.conceptId;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines    
               # Replace Windows newlines with Unix newlines    public void setConceptId(BigDecimal conceptId) {
               # Replace Windows newlines with Unix newlines        this.conceptId = conceptId;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines    @Column(name="DESCRIPTION_NAME", unique=false, nullable=false, insertable=true, updatable=true, length=1000)
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    public String getDescriptionName() {
               # Replace Windows newlines with Unix newlines        return this.descriptionName;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines    
               # Replace Windows newlines with Unix newlines    public void setDescriptionName(String descriptionName) {
               # Replace Windows newlines with Unix newlines        this.descriptionName = descriptionName;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines   
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines}
               # Replace Windows newlines with Unix newlines