package edu.pitt.dbmi.ohdsiv5.db;


import java.util.Calendar;




public class DrugEra  {

    // Constructors

    /** default constructor */
    public DrugEra() {
    }

    public DrugEra(Long drugEraId, Calendar drugEraStartDate, Long personId, Calendar drugEraEndDate, Integer drugConceptID, Integer drugExposureCount) {
	super();
    this.drugEraId = drugEraId;
	this.drugEraStartDate = drugEraStartDate;
	this.personId = personId;
	this.drugEraEndDate = drugEraEndDate;
	this.drugConceptID = drugConceptID;
	this.drugExposureCount = drugExposureCount;
    }

    // Property accessors
                    Long drugEraId;
    public Long getDrugEraId() {
        return this.drugEraId;
    }    
    public void setDrugEraId(Long id) {
        this.drugEraId = id;
    }

        Calendar drugEraStartDate;
    public void setDrugEraStartDate(Calendar drugEraStartDate) {
	this.drugEraStartDate = drugEraStartDate;
    }    
    public Calendar getDrugEraStartDate() {
	return drugEraStartDate;
    }


        Calendar drugEraEndDate;
    public void setDrugEraEndDate(Calendar drugEraEndDate) {
	this.drugEraEndDate = drugEraEndDate;
    }    
    public Calendar getDrugEraEndDate() {
	return drugEraEndDate;
    }

        Long personId;
    public Long getPersonId() {
	return personId;
    }
    public void setPersonId(Long personId) {
	this.personId = personId;
    }

        Integer drugConceptID;
    public void setDrugConceptId(Integer condId) { 
	this.drugConceptID = condId;
    }
    public Integer getDrugConceptId() { 
	return drugConceptID;
    }

        Integer drugExposureCount;
    public void setDrugExposureCount(Integer drugExposureCount) { 
	this.drugExposureCount = drugExposureCount;
    }
    public Integer getDrugExposureCount() { 
	return drugExposureCount;
    }

}
