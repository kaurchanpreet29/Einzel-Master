package com.floreantpos.model.base;

import java.io.Serializable;

public class BaseMenuUsage implements Comparable, Serializable {

  // constructors
  public BaseMenuUsage() {
    initialize();
  }

  /**
   * Constructor for primary key
   */
  public BaseMenuUsage(java.lang.Integer autoId) {
    this.setId(autoId);
    initialize();
  }

  protected void initialize () {}
  
  private int hashCode = Integer.MIN_VALUE;

  // primary key
  private java.lang.Integer id;

  private String category;

  private Integer count;

  private Double grossSales;

  private Integer tax;
  
  public String getCategory() {
    return category;
  }

  /**
   * Set the unique identifier of this class
   * @param autoId the new ID
   */
  public void setId (java.lang.Integer autoId) {
    this.id = autoId;
    this.hashCode = Integer.MIN_VALUE;
  }
  
  public java.lang.Integer getId () {
    return id;
  }
  
  public void setCategory(String category) {
    this.category = category;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  public Double getGrossSales() {
    return grossSales;
  }

  public void setGrossSales(Double grossSales) {
    this.grossSales = grossSales;
  }

  public Integer getTax() {
    return tax;
  }

  public void setTax(Integer tax) {
    this.tax = tax;
  }

  public int hashCode() {
    if (Integer.MIN_VALUE == this.hashCode) {
      if (null == this.getId())
        return super.hashCode();
      else {
        String hashStr = this.getClass().getName() + ":"
            + this.getId().hashCode();
        this.hashCode = hashStr.hashCode();
      }
    }
    return this.hashCode;
  }

  public int compareTo(Object obj) {
    if (obj.hashCode() > hashCode())
      return 1;
    else if (obj.hashCode() < hashCode())
      return -1;
    else
      return 0;
  }

  public String toString() {
    return super.toString();
  }
}
