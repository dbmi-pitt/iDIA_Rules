package edu.pitt.dbmi.ohdsiv5.db;

import java.util.Calendar;

@Entity
@Table(name="DRUG_STRENGTH")

public class DrugStrength {
    
    // Constructor

    /** default constructor */
    public DrugStrength() {
    }

    public DrugStrength(
			Integer drugConceptId,
			Integer ingredientConceptId,
			Double amountValue,
			Integer amountUnitConceptId,
			Double numeratorValue,
			Integer numeratorUnitConceptId,
			Double denominatorValue,
			Integer denominatorUnitConceptId,
			Calendar validEndDate,
			Calendar validStartDate,
			String invalidReason
			) {
	this.drugConceptId = drugConceptId;
	this.ingredientConceptId = ingredientConceptId;
	this.amountValue = amountValue;
	this.amountUnitConceptId = amountUnitConceptId;
	this.numeratorValue = numeratorValue;
	this.numeratorUnitConceptId = numeratorUnitConceptId;
	this.denominatorValue = denominatorValue;
	this.denominatorUnitConceptId = denominatorUnitConceptId;
	this.validEndDate = validEndDate;
	this.validStartDate = validStartDate;
	this.invalidReason = invalidReason;
    }

    // Property accessors
    @Id
    @Column(name="DRUG_CONCEPT_ID", unique=false, nullable=false, insertable=true, updatable=true, precision=8, scale=0)
    Integer drugConceptId;
    public Integer getDrugConceptId() {
	return this.drugConceptId;
    }
    public void setDrugConceptId(Integer drugConceptId) {
	this.drugConceptId = drugConceptId;
    }

    @Column(name="INGREDIENT_CONCEPT_ID", unique=false, nullable=false, insertable=true, updatable=true, precision=8, scale=0)
    Integer ingredientConceptId;
    public Integer getIngredientConceptId() {
	return this.ingredientConceptId;
    }
    public void setIngredientConceptId(Integer ingredientConceptId) {
	this.ingredientConceptId = ingredientConceptId;
    }

    @Column(name="AMOUNT_VALUE", unique=false, nullable=true, insertable=true, updatable=true, precision=12, scale=0)
    Double amountValue;
    public Double getAmountValue() {
	return this.amountValue;
    }
    public void setAmountValue(Double amountValue) {
	this.amountValue = amountValue;
    }

    @Column(name="AMOUNT_UNIT_CONCEPT_ID", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0)
    Integer amountUnitConceptId;
    public Integer getAmountUnitConceptId() {
	return this.amountUnitConceptId;
    }
    public void setAmountUnitConceptId(Integer amountUnitConceptId) {
	this.amountUnitConceptId = amountUnitConceptId;
    }

    @Column(name="NUMERATOR_VALUE", unique=false,nullable=true, updatable=true, insertable=true, precision=8, scale=0)
    Double numeratorValue;
    public Double getNumeratorValue() {
	return this.numeratorValue;
    }
    public void setNumeratorValue(Double numeratorValue) {
	this.numeratorValue = numeratorValue;
    }

    @Column(name="NUMERATOR_UNIT_CONCEPT_ID", unique=false, nullable=true, updatable=true, insertable=true, precision=8, scale=0)
    Integer numeratorUnitConceptId;
    public Integer getNumeratorUnitConceptId() {
	return this.numeratorUnitConceptId;
    }
    public void setNumeratorUnitConceptId(Integer numeratorUnitConceptId) {
	this.numeratorUnitConceptId = numeratorUnitConceptId;
    }

    @Column(name="DENOMINATOR_VALUE", unique=false,nullable=true, updatable=true, insertable=true, precision=8, scale=0)
    Double denominatorValue;
    public Double getDenominatorValue() {
	return this.denominatorValue;
    }
    public void setDenominatorValue(Double denominatorValue) {
	this.denominatorValue = denominatorValue;
    }
    
    @Column(name="DENOMINATOR_UNIT_CONCEPT_ID", unique=false, nullable=true, updatable=true, insertable=true, precision=8, scale=0)
    Integer denominatorUnitConceptId;
    public Integer getDenominatorUnitConceptId() {
	return this.denominatorUnitConceptId;
    }
    public void setDenominatorUnitConceptId(Integer denominatorUnitConceptId) {
	this.denominatorUnitConceptId = denominatorUnitConceptId;
    }

    @Column(name="VALID_START_DATE", unique=false, nullable=false, insertable=true, updatable=true, length=7) 
    Calendar validStartDate;
    public Calendar getValidStartDate() {
	return this.validStartDate;
    }
    public void setValidStartDate(Calendar validStartDate) {
	this.validStartDate = validStartDate;
    }    
   
    @Column(name="VALID_END_DATE", unique=false, nullable=false, insertable=true, updatable=true, length=7) 
    Calendar validEndDate;
    public Calendar getValidEndDate() {
	return this.validEndDate;
    }
    public void setValidEndDate(Calendar validEndDate) {
	this.validEndDate = validEndDate;
    }

    @Column(name="INVALID_REASON", unique=false, nullable=true, updatable=true, insertable=true, length=1)
    String invalidReason;
    public String getInvalidReason() {
	return this.invalidReason;
    }
    public void setInvalidReason(String invalidReason) {
	this.invalidReason = invalidReason;
    }
}
