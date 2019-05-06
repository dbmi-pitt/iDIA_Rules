package edu.pitt.dbmi.ohdsiv5.db;
// Generated Jun 15, 2010 5:43:59 PM by Hibernate Tools 3.1.0.beta4



/**
 * ObservationTypeRef generated by hbm2java
 */
@Entity
@Table(name="OBSERVATION_TYPE_REF")

public class ObservationTypeRef  {


    // Fields    

     private Long id;


    // Constructors

    /** default constructor */
    public ObservationTypeRef() {
    }

    
    /** full constructor */
    public ObservationTypeRef(Long id) {
        this.id = id;
    }
    

   
    // Property accessors
    @EmbeddedId
    @AttributeOverrides( {
        @AttributeOverride(name="observationType", column=@Column(name="OBSERVATION_TYPE", unique=false, nullable=false, insertable=true, updatable=true, length=3) ), 
        @AttributeOverride(name="observationTypeDesc", column=@Column(name="OBSERVATION_TYPE_DESC", unique=false, nullable=true, insertable=true, updatable=true) ) } )

    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
   








}
