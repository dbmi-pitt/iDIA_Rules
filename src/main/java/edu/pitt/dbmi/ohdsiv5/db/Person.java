package edu.pitt.dbmi.ohdsiv5.db;
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlinesimport java.sql.Date;
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlinesimport javax.persistence.AttributeOverride;
               # Replace Windows newlines with Unix newlinesimport javax.persistence.AttributeOverrides;
               # Replace Windows newlines with Unix newlinesimport javax.persistence.Column;
               # Replace Windows newlines with Unix newlinesimport javax.persistence.Entity;
               # Replace Windows newlines with Unix newlinesimport javax.persistence.GeneratedValue;
               # Replace Windows newlines with Unix newlinesimport javax.persistence.GenerationType;
               # Replace Windows newlines with Unix newlinesimport javax.persistence.Id;
               # Replace Windows newlines with Unix newlinesimport javax.persistence.SequenceGenerator;
               # Replace Windows newlines with Unix newlinesimport javax.persistence.Table;
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines// NOTE: not all rows are mapped
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines@Entity
               # Replace Windows newlines with Unix newlines@Table(name="PERSON")
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlinespublic class Person  {
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines	/**
               # Replace Windows newlines with Unix newlines	 * 
               # Replace Windows newlines with Unix newlines	 */
               # Replace Windows newlines with Unix newlines	private static final long serialVersionUID = 1L;
               # Replace Windows newlines with Unix newlines	
               # Replace Windows newlines with Unix newlines	Long personId;
               # Replace Windows newlines with Unix newlines	Integer yearOfBirth;
               # Replace Windows newlines with Unix newlines	Integer monthOfBirth;
               # Replace Windows newlines with Unix newlines	Integer dayOfBirth;
               # Replace Windows newlines with Unix newlines	Integer genderCUI;
               # Replace Windows newlines with Unix newlines	Integer raceCUI;
               # Replace Windows newlines with Unix newlines	Integer locationId;
               # Replace Windows newlines with Unix newlines	String genderSourceCode;
               # Replace Windows newlines with Unix newlines	String raceSourceCode;
               # Replace Windows newlines with Unix newlines        Integer ethnicityCUI;
               # Replace Windows newlines with Unix newlines        String ethnicitySourceCode;
               # Replace Windows newlines with Unix newlines    // Constructors
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    /** default constructor */
               # Replace Windows newlines with Unix newlines    public Person() {
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    /** @return the personId */
               # Replace Windows newlines with Unix newlines        @Id
               # Replace Windows newlines with Unix newlines        @Column(name="PERSON_ID", nullable = false, insertable = true, updatable = false)  
               # Replace Windows newlines with Unix newlines	public Long getPersonId() {
               # Replace Windows newlines with Unix newlines		return personId;
               # Replace Windows newlines with Unix newlines        }
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    /** @param personId the personId to set */
               # Replace Windows newlines with Unix newlines	public void setPersonId(Long personId) {
               # Replace Windows newlines with Unix newlines		this.personId = personId;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    /** @return the yearOfBirth */
               # Replace Windows newlines with Unix newlines	@Column(name="YEAR_OF_BIRTH", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0)
               # Replace Windows newlines with Unix newlines	public Integer getYearOfBirth() {
               # Replace Windows newlines with Unix newlines		return yearOfBirth;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    /** @param yearOfBirth the yearOfBirth to set */
               # Replace Windows newlines with Unix newlines	public void setYearOfBirth(Integer yearOfBirth) {
               # Replace Windows newlines with Unix newlines		this.yearOfBirth = yearOfBirth;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines	
               # Replace Windows newlines with Unix newlines    /** @return the monthOfBirth */
               # Replace Windows newlines with Unix newlines	@Column(name="MONTH_OF_BIRTH", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0)
               # Replace Windows newlines with Unix newlines	public Integer getMonthOfBirth() {
               # Replace Windows newlines with Unix newlines		return monthOfBirth;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    /** @param monthOfBirth the monthOfBirth to set */
               # Replace Windows newlines with Unix newlines	public void setMonthOfBirth(Integer monthOfBirth) {
               # Replace Windows newlines with Unix newlines		this.monthOfBirth = monthOfBirth;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines	
               # Replace Windows newlines with Unix newlines    /** @return the dayOfBirth */
               # Replace Windows newlines with Unix newlines	@Column(name="DAY_OF_BIRTH", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0)
               # Replace Windows newlines with Unix newlines	public Integer getDayOfBirth() {
               # Replace Windows newlines with Unix newlines		return dayOfBirth;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    /** @param dayOfBirth the dayOfBirth to set */
               # Replace Windows newlines with Unix newlines	public void setDayOfBirth(Integer dayOfBirth) {
               # Replace Windows newlines with Unix newlines		this.dayOfBirth = dayOfBirth;
               # Replace Windows newlines with Unix newlines	}	
               # Replace Windows newlines with Unix newlines	
               # Replace Windows newlines with Unix newlines    /** @return the genderCUI */
               # Replace Windows newlines with Unix newlines	@Column(name="GENDER_CONCEPT_ID", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0)
               # Replace Windows newlines with Unix newlines	public Integer getGenderCUI() {
               # Replace Windows newlines with Unix newlines		return genderCUI;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines	
               # Replace Windows newlines with Unix newlines    /** @param genderCUI the genderCUI to set */
               # Replace Windows newlines with Unix newlines	public void setGenderCUI(Integer genderCUI) {
               # Replace Windows newlines with Unix newlines		this.genderCUI = genderCUI;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines	
               # Replace Windows newlines with Unix newlines    /** @return the raceCUI */
               # Replace Windows newlines with Unix newlines	@Column(name="RACE_CONCEPT_ID", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0)
               # Replace Windows newlines with Unix newlines	public Integer getRaceCUI() {
               # Replace Windows newlines with Unix newlines		return raceCUI;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    /** @param raceCUI the raceCUI to set */
               # Replace Windows newlines with Unix newlines	public void setRaceCUI(Integer raceCUI) {
               # Replace Windows newlines with Unix newlines		this.raceCUI = raceCUI;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    /** @return the locationId */
               # Replace Windows newlines with Unix newlines	@Column(name="LOCATION_ID", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0)
               # Replace Windows newlines with Unix newlines	public Integer getLocationId() {
               # Replace Windows newlines with Unix newlines		return locationId;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    /** @param locationId the locationId to set */
               # Replace Windows newlines with Unix newlines	public void setLocationId(Integer locationId) {
               # Replace Windows newlines with Unix newlines		this.locationId = locationId;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    /** @return the genderSourceCode */
               # Replace Windows newlines with Unix newlines	@Column(name="GENDER_SOURCE_VALUE", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0)
               # Replace Windows newlines with Unix newlines	public String getGenderSourceCode() {
               # Replace Windows newlines with Unix newlines		return genderSourceCode;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    /** @param enthnicityCUI the ethnicityCUI to set */
               # Replace Windows newlines with Unix newlines        public void setEthnicityCUI(Integer ethnicityCUI) {
               # Replace Windows newlines with Unix newlines                this.ethnicityCUI = ethnicityCUI;
               # Replace Windows newlines with Unix newlines        }
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    /** @return the ethnicityCUI */
               # Replace Windows newlines with Unix newlines        @Column(name="ETHNICITY_CONCEPT_ID", unique=false, nullable=false, insertable=true, updatable=true, precision=8, scale=0)
               # Replace Windows newlines with Unix newlines        public Integer getEthnicityCUI() {
               # Replace Windows newlines with Unix newlines	        return ethnicityCUI;
               # Replace Windows newlines with Unix newlines        } 
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    /** @param ethnicitySourceCode the ethnicitySourceCode to set */
               # Replace Windows newlines with Unix newlines        public void setEthnicitySourceCode(String ethnicitySourceCode) {
               # Replace Windows newlines with Unix newlines       	        this.ethnicitySourceCode = ethnicitySourceCode;
               # Replace Windows newlines with Unix newlines         }
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    /** @return the enthnicitySourceValue */
               # Replace Windows newlines with Unix newlines        @Column(name="ETHNICITY_SOURCE_VALUE", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0)
               # Replace Windows newlines with Unix newlines        public String getEthnicitySourceCode() {
               # Replace Windows newlines with Unix newlines	        return ethnicitySourceCode;
               # Replace Windows newlines with Unix newlines        }
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    /** @param genderSourceCode the genderSourceCode to set */
               # Replace Windows newlines with Unix newlines	public void setGenderSourceCode(String genderSourceCode) {
               # Replace Windows newlines with Unix newlines		this.genderSourceCode = genderSourceCode;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    /** @return the raceSourceCode */
               # Replace Windows newlines with Unix newlines	@Column(name="RACE_SOURCE_VALUE", unique=false, nullable=true, insertable=true, updatable=true, precision=8, scale=0)
               # Replace Windows newlines with Unix newlines	public String getRaceSourceCode() {
               # Replace Windows newlines with Unix newlines		return raceSourceCode;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    /** @param raceSourceCode the raceSourceCode to set */
               # Replace Windows newlines with Unix newlines	public void setRaceSourceCode(String raceSourceCode) {
               # Replace Windows newlines with Unix newlines		this.raceSourceCode = raceSourceCode;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines}
               # Replace Windows newlines with Unix newlines