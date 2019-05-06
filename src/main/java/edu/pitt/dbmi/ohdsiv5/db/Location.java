package edu.pitt.dbmi.ohdsiv5.db;



/**
 * Location
 */
@Entity
@Table(name="LOCATION")

public class Location  {


    // Fields    
    Long locationId;
    String locationSourceVal;

    // Constructors
    /** default constructor */
    public Location() {
    }
   
    @Id
    @Column(name = "LOCATION_ID", nullable = false, insertable = false, updatable = false)  
    public Long getLocationId() {
	return locationId;
    }
    /**
     * @param locationId the locationId to set
     */
    public void setLocationId(Long locationId) {
	this.locationId = locationId;
    }

    @Column(name = "LOCATION_SOURCE_VALUE", nullable = false, insertable = false, updatable = false)  
    public String getLocationSourceVal() {
	return locationSourceVal;
    }
    /**
     * @param locationSourceVal
     */
    public void setLocationSourceVal(String sourceVal) {
	this.locationSourceVal = sourceVal;
    }








}
