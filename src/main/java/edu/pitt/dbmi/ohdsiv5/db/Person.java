package edu.pitt.dbmi.ohdsiv5.db;

import java.sql.Date;


// NOTE: not all rows are mapped

@Entity
@Table(name="PERSON")

public class Person  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Long personId;
	Integer yearOfBirth;
	Integer monthOfBirth;
	Integer dayOfBirth;
	Integer genderCUI;
	Integer raceCUI;
	Integer locationId;
	String genderSourceCode;
	String raceSourceCode;
        Integer ethnicityCUI;
        String ethnicitySourceCode;
    // Constructors

    /** default constructor */
    public Person() {
    }

    /** @return the personId */
        @Id
        @Column(name="PERSON_ID", nullable = false, insertable = true, updatable = false)  
	public Long getPersonId() {
		return personId;
        }

    /** @param personId the personId to set */
	public void setPersonId(Long personId) {
		this.personId = personId;
	}

    /** @return the yearOfBirth */
	@Column(name="YEAR_OF_BIRTH", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0)
	public Integer getYearOfBirth() {
		return yearOfBirth;
	}

    /** @param yearOfBirth the yearOfBirth to set */
	public void setYearOfBirth(Integer yearOfBirth) {
		this.yearOfBirth = yearOfBirth;
	}
	
    /** @return the monthOfBirth */
	@Column(name="MONTH_OF_BIRTH", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0)
	public Integer getMonthOfBirth() {
		return monthOfBirth;
	}

    /** @param monthOfBirth the monthOfBirth to set */
	public void setMonthOfBirth(Integer monthOfBirth) {
		this.monthOfBirth = monthOfBirth;
	}
	
    /** @return the dayOfBirth */
	@Column(name="DAY_OF_BIRTH", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0)
	public Integer getDayOfBirth() {
		return dayOfBirth;
	}

    /** @param dayOfBirth the dayOfBirth to set */
	public void setDayOfBirth(Integer dayOfBirth) {
		this.dayOfBirth = dayOfBirth;
	}	
	
    /** @return the genderCUI */
	@Column(name="GENDER_CONCEPT_ID", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0)
	public Integer getGenderCUI() {
		return genderCUI;
	}
	
    /** @param genderCUI the genderCUI to set */
	public void setGenderCUI(Integer genderCUI) {
		this.genderCUI = genderCUI;
	}
	
    /** @return the raceCUI */
	@Column(name="RACE_CONCEPT_ID", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0)
	public Integer getRaceCUI() {
		return raceCUI;
	}

    /** @param raceCUI the raceCUI to set */
	public void setRaceCUI(Integer raceCUI) {
		this.raceCUI = raceCUI;
	}

    /** @return the locationId */
	@Column(name="LOCATION_ID", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0)
	public Integer getLocationId() {
		return locationId;
	}

    /** @param locationId the locationId to set */
	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

    /** @return the genderSourceCode */
	@Column(name="GENDER_SOURCE_VALUE", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0)
	public String getGenderSourceCode() {
		return genderSourceCode;
	}

    /** @param enthnicityCUI the ethnicityCUI to set */
        public void setEthnicityCUI(Integer ethnicityCUI) {
                this.ethnicityCUI = ethnicityCUI;
        }

    /** @return the ethnicityCUI */
        @Column(name="ETHNICITY_CONCEPT_ID", unique=false, nullable=false, insertable=true, updatable=true, precision=8, scale=0)
        public Integer getEthnicityCUI() {
	        return ethnicityCUI;
        } 

    /** @param ethnicitySourceCode the ethnicitySourceCode to set */
        public void setEthnicitySourceCode(String ethnicitySourceCode) {
       	        this.ethnicitySourceCode = ethnicitySourceCode;
         }

    /** @return the enthnicitySourceValue */
        @Column(name="ETHNICITY_SOURCE_VALUE", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0)
        public String getEthnicitySourceCode() {
	        return ethnicitySourceCode;
        }

    /** @param genderSourceCode the genderSourceCode to set */
	public void setGenderSourceCode(String genderSourceCode) {
		this.genderSourceCode = genderSourceCode;
	}

    /** @return the raceSourceCode */
	@Column(name="RACE_SOURCE_VALUE", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0)
	public String getRaceSourceCode() {
		return raceSourceCode;
	}

    /** @param raceSourceCode the raceSourceCode to set */
	public void setRaceSourceCode(String raceSourceCode) {
		this.raceSourceCode = raceSourceCode;
	}
}
