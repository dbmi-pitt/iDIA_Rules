package edu.pitt.dbmi.ohdsiv5.db;

import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="SIG_MAPPING")

public class SigMapping implements java.io.Serializable {

    // Constructor

    /** default constructor */
    public SigMapping() {
    }

    public SigMapping(
              Integer id,
              String sig,
              Integer expected,
              Integer min,
              Integer max
              ) {
    this.id = id;
    this.sig = sig;
    this.expected = expected;
    this.min = min;
    this.max = max;
    }

    // Property accessors
    @Id
    @Column(name="ID", unique=false, nullable=false, insertable=true, updatable=true, precision=8, scale=0)
    Integer id;
    public Integer getSigMappingId() {
    return this.id;
    }
    public void setSigMappingId(Integer id) {
    this.id = id;
    }


    @Column(name="sig", unique=false, nullable=false, insertable=true, updatable=true, length=200)
    String sig;
    public String getSig() {
    return this.sig;
    }
    public void setSig(String sig) {
    this.sig = sig;
    }

    @Column(name="expected", unique=false, nullable=false, insertable=true, updatable=true, precision=8, scale=0)
    Integer expected;
    public Integer getSigExpected() {
    return this.expected;
    }
    public void setSigExpected(Integer expected) {
    this.expected = expected;
    }

    @Column(name="min", unique=false, nullable=false, insertable=true, updatable=true, precision=8, scale=0)
    Integer min;
    public Integer getSigMin() {
    return this.min;
    }
    public void setSigMin(Integer min) {
    this.min = min;
    }

    @Column(name="max", unique=false, nullable=false, insertable=true, updatable=true, precision=8, scale=0)
    Integer max;
    public Integer getSigMax() {
    return this.max;
    }
    public void setSigMax(Integer max) {
    this.max = max;
    }
}
