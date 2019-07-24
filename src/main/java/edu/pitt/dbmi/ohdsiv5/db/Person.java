package edu.pitt.dbmi.ohdsiv5.db;
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlinesimport java.sql.Date;
               # Replace Windows newlines with Unix newlinesimport java.util.Calendar;
               # Replace Windows newlines with Unix newlinesimport java.sql.Timestamp;
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines// NOTE: not all rows are mapped
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlinespublic class Person  {
               # Replace Windows newlines with Unix newlines
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
               # Replace Windows newlines with Unix newlines    Integer ethnicityCUI;
               # Replace Windows newlines with Unix newlines    String ethnicitySourceCode;
               # Replace Windows newlines with Unix newlines  Timestamp dateOfBirth;
               # Replace Windows newlines with Unix newlines    // Constructors
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    // default constructor
               # Replace Windows newlines with Unix newlines    public Person() {
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    public Person(Long personId, Integer yearOfBirth, Integer genderCUI, Integer raceCUI, Integer ethnicityCUI) {
               # Replace Windows newlines with Unix newlines        this.personId = personId;
               # Replace Windows newlines with Unix newlines        this.yearOfBirth = yearOfBirth;
               # Replace Windows newlines with Unix newlines        this.genderCUI = genderCUI;
               # Replace Windows newlines with Unix newlines        this.raceCUI = raceCUI;
               # Replace Windows newlines with Unix newlines        this.ethnicityCUI = ethnicityCUI;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    public Long getPersonId() {
               # Replace Windows newlines with Unix newlines		return personId;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    public void setPersonId(Long personId) {
               # Replace Windows newlines with Unix newlines		this.personId = personId;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines  public void setDateOfBirth() {
               # Replace Windows newlines with Unix newlines    if (this.yearOfBirth != null && this.monthOfBirth != null && this.dayOfBirth != null) {
               # Replace Windows newlines with Unix newlines      Calendar b = Calendar.getInstance();
               # Replace Windows newlines with Unix newlines      b.set(Calendar.YEAR, this.yearOfBirth);
               # Replace Windows newlines with Unix newlines      b.set(Calendar.MONTH, this.monthOfBirth);
               # Replace Windows newlines with Unix newlines      b.set(Calendar.DAY_OF_MONTH, this.dayOfBirth);
               # Replace Windows newlines with Unix newlines      Timestamp dob = new Timestamp(b.getTimeInMillis());
               # Replace Windows newlines with Unix newlines      this.dateOfBirth = dob;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines  }
               # Replace Windows newlines with Unix newlines  public Timestamp getDateOfBirth() {
               # Replace Windows newlines with Unix newlines    return this.dateOfBirth;
               # Replace Windows newlines with Unix newlines  }
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines  public Double getDobDateDiffInMillis(Timestamp otherDate) {
               # Replace Windows newlines with Unix newlines    if (this.dateOfBirth != null) {
               # Replace Windows newlines with Unix newlines      double diff = (otherDate.getTime() - this.dateOfBirth.getTime());
               # Replace Windows newlines with Unix newlines      return diff;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines    else return null;
               # Replace Windows newlines with Unix newlines  }
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    public Integer getYearOfBirth() {
               # Replace Windows newlines with Unix newlines		return yearOfBirth;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines     public void setYearOfBirth(Integer yearOfBirth) {
               # Replace Windows newlines with Unix newlines		this.yearOfBirth = yearOfBirth;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines	
               # Replace Windows newlines with Unix newlines    public Integer getMonthOfBirth() {
               # Replace Windows newlines with Unix newlines		return monthOfBirth;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    public void setMonthOfBirth(Integer monthOfBirth) {
               # Replace Windows newlines with Unix newlines		this.monthOfBirth = monthOfBirth;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines	
               # Replace Windows newlines with Unix newlines    public Integer getDayOfBirth() {
               # Replace Windows newlines with Unix newlines		return dayOfBirth;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    public void setDayOfBirth(Integer dayOfBirth) {
               # Replace Windows newlines with Unix newlines		this.dayOfBirth = dayOfBirth;
               # Replace Windows newlines with Unix newlines	}	
               # Replace Windows newlines with Unix newlines	
               # Replace Windows newlines with Unix newlines    public Integer getGenderCUI() {
               # Replace Windows newlines with Unix newlines		return genderCUI;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines	
               # Replace Windows newlines with Unix newlines    public void setGenderCUI(Integer genderCUI) {
               # Replace Windows newlines with Unix newlines		this.genderCUI = genderCUI;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines	
               # Replace Windows newlines with Unix newlines    public Integer getRaceCUI() {
               # Replace Windows newlines with Unix newlines		return raceCUI;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    public void setRaceCUI(Integer raceCUI) {
               # Replace Windows newlines with Unix newlines		this.raceCUI = raceCUI;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    public Integer getLocationId() {
               # Replace Windows newlines with Unix newlines		return locationId;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    public void setLocationId(Integer locationId) {
               # Replace Windows newlines with Unix newlines		this.locationId = locationId;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    public String getGenderSourceCode() {
               # Replace Windows newlines with Unix newlines		return genderSourceCode;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    public void setEthnicityCUI(Integer ethnicityCUI) {
               # Replace Windows newlines with Unix newlines        this.ethnicityCUI = ethnicityCUI;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    public Integer getEthnicityCUI() {
               # Replace Windows newlines with Unix newlines        return ethnicityCUI;
               # Replace Windows newlines with Unix newlines    } 
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    public void setEthnicitySourceCode(String ethnicitySourceCode) {
               # Replace Windows newlines with Unix newlines   	    this.ethnicitySourceCode = ethnicitySourceCode;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    public String getEthnicitySourceCode() {
               # Replace Windows newlines with Unix newlines        return ethnicitySourceCode;
               # Replace Windows newlines with Unix newlines    }
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    public void setGenderSourceCode(String genderSourceCode) {
               # Replace Windows newlines with Unix newlines		this.genderSourceCode = genderSourceCode;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    public String getRaceSourceCode() {
               # Replace Windows newlines with Unix newlines		return raceSourceCode;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines
               # Replace Windows newlines with Unix newlines    public void setRaceSourceCode(String raceSourceCode) {
               # Replace Windows newlines with Unix newlines		this.raceSourceCode = raceSourceCode;
               # Replace Windows newlines with Unix newlines	}
               # Replace Windows newlines with Unix newlines}
               # Replace Windows newlines with Unix newlines