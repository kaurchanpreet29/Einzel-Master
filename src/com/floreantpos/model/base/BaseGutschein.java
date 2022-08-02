package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the TAX table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="GUTSCHEIN"
 */

public abstract class BaseGutschein  implements Comparable, Serializable {

	public static String REF = "Gutschein";
	public static String PROP_BARCODE = "barcode";
	public static String PROP_BILL_NR = "rechnungNr";
	public static String PROP_CREATE_DATE = "createdDate";


	// constructors
	public BaseGutschein () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseGutschein (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	private java.util.Date expiryDate;
	private java.util.Date createdDate;
	private java.lang.Double value;
	private java.lang.Double splittedAmount;
	// fields
	private java.lang.String barcode;
	private java.lang.String createdBy;
	private java.lang.Boolean unlimited;
	private java.lang.Boolean closed;
	private java.lang.Boolean splitted;
	private java.lang.Integer count;
	private java.lang.String name;
	private java.lang.String rechnungNr;
	
	protected void initialize(){}

	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="ID"
     */
	public java.lang.Integer getId () {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * @param id the new ID
	 */
	public void setId (java.lang.Integer id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}

	public java.util.Date getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(java.util.Date expiryDate) {
    this.expiryDate = expiryDate;
  }

  public java.lang.String getBarcode() {
    return barcode;
  }

  public void setBarcode(java.lang.String barcode) {
    this.barcode = barcode;
  }

  public java.lang.Boolean isUnlimited() {
    return unlimited;
  }

  public void setUnlimited(java.lang.Boolean unlimited) {
    this.unlimited = unlimited;
  }

  public java.lang.Boolean isClosed() {
    return closed;
  }

  public void setClosed(java.lang.Boolean closed) {
    this.closed = closed;
  }
  
  public java.lang.Double getValue() {
    return value;
  }

  public void setValue(java.lang.Double value) {
    this.value = value;
  }

  public java.lang.Boolean isSplitted() {
    return splitted;
  }

  public void setSplitted(java.lang.Boolean splitted) {
    this.splitted = splitted;
  }

  public java.lang.Double getSplittedAmount() {
    return splittedAmount;
  }

  public void setSplittedAmount(java.lang.Double splittedAmount) {
    this.splittedAmount = splittedAmount;
  }

  public java.util.Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(java.util.Date createdDate) {
    this.createdDate = createdDate;
  }

  public java.lang.String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(java.lang.String createdBy) {
    this.createdBy = createdBy;
  }
  

	public java.lang.Integer getCount() {
		return count!=null?count:1;
	}

	public void setCount(java.lang.Integer count) {
		this.count = count;
	}


  public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}
	
	
public java.lang.String getRechnungNr() {
		return rechnungNr;
	}

	public void setRechnungNr(java.lang.String rechnungNr) {
		this.rechnungNr = rechnungNr;
	}

public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.Gutschein)) return false;
		else {
			com.floreantpos.model.Gutschein gutschein = (com.floreantpos.model.Gutschein) obj;
			if (null == this.getId() || null == gutschein.getId()) return false;
			else return (this.getId().equals(gutschein.getId()));
		}
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}

	public int compareTo (Object obj) {
		if (obj.hashCode() > hashCode()) return 1;
		else if (obj.hashCode() < hashCode()) return -1;
		else return 0;
	}

	public String toString () {
		return super.toString();
	}
}