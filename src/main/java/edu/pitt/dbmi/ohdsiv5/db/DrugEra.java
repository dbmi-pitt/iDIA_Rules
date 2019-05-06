package edu.pitt.dbmi.ohdsiv5.db;


import java.util.Calendar;



@Entity
@Table(name="DRUG_ERA")

public class DrugEra  {

    // Constructors

    /** default constructor */
    public DrugEra() {
    }

    public DrugEra(Calendar drugEraStartDate, Long personId, Calendar drugEraEndDate, Integer drugConceptID, Integer drugExposureCount) {
	super();
	this.drugEraStartDate = drugEraStartDate;
	this.personId = personId;
	this.drugEraEndDate = drugEraEndDate;
	this.drugConceptID = drugConceptID;
	this.drugExposureCount = drugExposureCount;
    }

    // Property accessors
    @Id
    @Column(name = "DRUG_ERA_ID", nullable = false, insertable = false, updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="HIBERNATE_SEQUENCE_GENERATOR")
    @SequenceGenerator(name="HIBERNATE_SEQUENCE_GENERATOR", sequenceName="HIBERNATE_SEQUENCE", initialValue = 1, allocationSize = 1)
    Long drugEraId;
    public Long getDrugEraId() {
        return this.drugEraId;
    }    
    public void setDrugEraId(Long id) {
        this.drugEraId = id;
    }

    @Column(name="DRUG_ERA_START_DATE", unique=false, nullable=true, insertable=true, updatable=true, length=7) 
    Calendar drugEraStartDate;
    public void setDrugEraStartDate(Calendar drugEraStartDate) {
	this.drugEraStartDate = drugEraStartDate;
    }    
    public Calendar getDrugEraStartDate() {
	return drugEraStartDate;
    }


    @Column(name="DRUG_ERA_END_DATE", unique=false, nullable=true, insertable=true, updatable=true, length=7) 
    Calendar drugEraEndDate;
    public void setDrugEraEndDate(Calendar drugEraEndDate) {
	this.drugEraEndDate = drugEraEndDate;
    }    
    public Calendar getDrugEraEndDate() {
	return drugEraEndDate;
    }

    @Column(name="PERSON_ID", unique=false, nullable=false, insertable=true, updatable=true, precision=12, scale=0) 
    Long personId;
    public Long getPersonId() {
	return personId;
    }
    public void setPersonId(Long personId) {
	this.personId = personId;
    }

    @Column(name="DRUG_CONCEPT_ID", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0) 
    Integer drugConceptID;
    public void setDrugConceptId(Integer condId) { 
	this.drugConceptID = condId;
    }
    public Integer getDrugConceptId() { 
	return drugConceptID;
    }

    @Column(name="DRUG_EXPOSURE_COUNT", unique=false, nullable=true, insertable=true, updatable=true, precision=4, scale=0)
    Integer drugExposureCount;
    public void setDrugExposureCount(Integer drugExposureCount) { 
	this.drugExposureCount = drugExposureCount;
    }
    public Integer getDrugExposureCount() { 
	return drugExposureCount;
    }

}
