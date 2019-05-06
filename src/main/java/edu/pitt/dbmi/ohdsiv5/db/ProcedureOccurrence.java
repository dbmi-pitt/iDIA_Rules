
package edu.pitt.dbmi.ohdsiv5.db;


import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="PROCEDURE_OCCURRENCE")

public class ProcedureOccurrence  {


    // Constructors

    /** default constructor */
    public ProcedureOccurrence() {
    }

    public ProcedureOccurrence(Long personId, Calendar procedureDate, Integer procedureConceptId, Integer procedureTypeConceptId, String procedureSourceValue) {
	super();
	this.personId = personId;
	this.procedureDate = procedureDate;
	this.procedureConceptId = procedureConceptId;
	this.procedureTypeConceptId = procedureTypeConceptId;
	this.procedureSourceValue = procedureSourceValue;
    }
   
    // Property accessors
    @Id
    @Column(name="PROCEDURE_OCCURRENCE_ID", unique=false, nullable=false, insertable=true, updatable=true, precision=15, scale=0) 
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="HIBERNATE_SEQUENCE_GENERATOR")
    @SequenceGenerator(name="HIBERNATE_SEQUENCE_GENERATOR", sequenceName="HIBERNATE_SEQUENCE", initialValue = 1, allocationSize = 1)
    Long ProcedureOccurrenceId;
    public Long getProcedureOccurrenceId() {
        return this.ProcedureOccurrenceId;
    }    
    public void setProcedureOccurrenceId(Long ProcedureOccurrenceId) {
        this.ProcedureOccurrenceId = ProcedureOccurrenceId;
    }

    @Column(name="PERSON_ID", unique=false, nullable=false, insertable=true, updatable=true, precision=12, scale=0) 
    Long personId;
    public Long getPersonId() {
	return personId;
    }
    public void setPersonId(Long personId) {
	this.personId = personId;
    }

    /**
     * @return procedureConceptId the procedureConceptId
     */
    @Column(name="PROCEDURE_CONCEPT_ID", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0)
    Integer procedureConceptId;
    public Integer getProcedureConceptId() {
	return procedureConceptId;
    }
	
    /**
     * @param procedureConceptId to set
     */
    public void setProcedureConceptId(Integer procedureConceptId) {
	this.procedureConceptId = procedureConceptId;
    }

    @Column(name="PROCEDURE_DATE", unique=false, nullable=true, insertable=true, updatable=true, length=7) 
    Calendar procedureDate;
    public void setProcedureDate(Calendar procedureDate) {
	this.procedureDate = procedureDate;
    }    
    public Calendar getProcedureDate() {
	return procedureDate;
    }

    /**
     * @return the procedureTypeConceptId
     */
    @Column(name="PROCEDURE_TYPE_CONCEPT_ID", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0)
    Integer procedureTypeConceptId;
    public Integer getProcedureTypeConceptId() {
	return procedureTypeConceptId;
    }
	
    /**
     * @param procedureTypeConceptId to set
     */
    public void setProcedureTypeConceptId(Integer procedureTypeConceptId) {
		this.procedureTypeConceptId = procedureTypeConceptId;
    }
    
    /**
     * @return the procedureSourceValue
     */
    @Column(name="PROCEDURE_SOURCE_VALUE", unique=false, nullable=true, insertable=true, updatable=true, precision=50, scale=0)
	String procedureSourceValue;
    public String getProcedureSourceValue() {
	return procedureSourceValue;
    }
	
    /**
     * @param procedureSourceValue to set
     */
    public void setProcedureSourcevalue(String procedureSourceValue) {
		this.procedureSourceValue = procedureSourceValue;
    }
    
    @Column(name="MODIFIER_CONCEPT_ID", unique=false, nullable=true, insertable=true, updatable=true, precision=12, scale=0)
    Integer modifierConceptId;
    public Integer getModifierConceptId() {
	return modifierConceptId;
    }
    public void setModifierConceptId(Integer modifierConceptId) {
	this.modifierConceptId = modifierConceptId;
    }
}
