package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;


/**
 * This is an object that contains data related to the CUSTOMER table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="CUSTOMER"
 */

public abstract class BaseCustomer  implements Comparable, Serializable {
	public static String REF = "Customer";
	public static String PROP_VIP = "vip";
	public static String PROP_NOTE = "note";
	public static String PROP_LANDMARK = "landMark";
	public static String PROP_TELEPHONE_NO = "telephoneNo";
	public static String PROP_TELEPHONE_NO_2 = "telephoneNo2";
	public static String PROP_DOB = "dob";
	public static String PROP_ZIP_CODE = "zipCode";
	public static String PROP_SSN = "ssn";
	public static String PROP_CITY = "city";
	public static String PROP_NAME = "name";
	public static String PROP_EMAIL = "email";
	public static String PROP_BELL_NAME = "bellName";
	public static String PROP_CREDIT_SPENT = "creditSpent";
	public static String PROP_ADDRESS = "address";
	public static String PROP_AUTO_ID = "autoId";
	public static String PROP_CREDIT_CARD_NO = "creditCardNo";
	public static String PROP_CREDIT_LIMIT = "creditLimit";
	public static String PROP_LOYALTY_NO = "loyaltyNo";
	public static String PROP_FIRM_NAME = "firmName";
	public static String PROP_SALUTATION = "salutation";
	public static String PROP_DOORNO = "doorNo";
	// constructors
	public BaseCustomer () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseCustomer (java.lang.Integer autoId) {
		this.setAutoId(autoId);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer autoId;

	 java.util.Date modifiedTime;

	// fields
	private java.lang.String loyaltyNo;
	private java.lang.String telephoneNo;
	private java.lang.String telephoneNo2;
	private java.lang.String email;
	private java.lang.String name;
	private java.lang.String dob;
	private java.lang.String ssn;
	private java.lang.String address;
	private java.lang.String city;
	private java.lang.String bellName;
	private java.lang.String zipCode;
	private java.lang.String landMark;
	private java.lang.Boolean vip;
	private java.lang.Double creditLimit;
	private java.lang.Double creditSpent;
	private java.lang.String creditCardNo;
	private java.lang.String note;
	private java.lang.String firmName;
	private java.lang.String salutation; 
	private java.lang.String doorNo;
	private java.lang.String bezerk;
	private java.lang.String description;
	private java.lang.Integer priceCategory;
	
	// collections
	private java.util.Map<String, String> properties;

	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="AUTO_ID"
     */
	public java.lang.Integer getAutoId () {
		return autoId;
	}

	/**
	 * Set the unique identifier of this class
	 * @param autoId the new ID
	 */
	public void setAutoId (java.lang.Integer autoId) {
		this.autoId = autoId;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Return the value associated with the column: MODIFIED_TIME
	 */
	public java.util.Date getModifiedTime () {
		return modifiedTime;
	}

	/**
	 * Set the value related to the column: MODIFIED_TIME
	 * @param modifiedTime the MODIFIED_TIME value
	 */
	public void setModifiedTime (java.util.Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public java.lang.String getDescription() {
		return description;
	}

	public void setDescription(java.lang.String description) {
		this.description = description;
	}

	/**
	 * Return the value associated with the column: LOYALTY_NO
	 */
	public java.lang.String getLoyaltyNo () {
		return loyaltyNo!=null?loyaltyNo:"";
	}

	/**
	 * Set the value related to the column: LOYALTY_NO
	 * @param loyaltyNo the LOYALTY_NO value
	 */
	public void setLoyaltyNo (java.lang.String loyaltyNo) {
		this.loyaltyNo = loyaltyNo;
	}



	/**
	 * Return the value associated with the column: TELEPHONE_NO
	 */
	public java.lang.String getTelephoneNo () {
		return telephoneNo;
	}

	/**
	 * Set the value related to the column: TELEPHONE_NO
	 * @param telephoneNo the TELEPHONE_NO value
	 */
	public void setTelephoneNo (java.lang.String telephoneNo) {
		this.telephoneNo = telephoneNo;
	}

	/**
	 * Return the value associated with the column: TELEPHONE_NO_2
	 */
	public java.lang.String getTelephoneNo2 () {
		return telephoneNo2;
	}

	/**
	 * Set the value related to the column: TELEPHONE_NO_2
	 * @param telephoneNo the TELEPHONE_NO_2 value
	 */
	public void setTelephoneNo2 (java.lang.String telephoneNo2) {
		this.telephoneNo2 = telephoneNo2;
	}
	

	/**
	 * Return the value associated with the column: EMAIL
	 */
	public java.lang.String getEmail () {
		return email;
	}

	/**
	 * Set the value related to the column: EMAIL
	 * @param email the EMAIL value
	 */
	public void setEmail (java.lang.String email) {
		this.email = email;
	}



	/**
	 * Return the value associated with the column: NAME
	 */
	public java.lang.String getName () {
		return name;
	}

	/**
	 * Set the value related to the column: NAME
	 * @param name the NAME value
	 */
	public void setName (java.lang.String name) {
		this.name = name;
	}



	/**
	 * Return the value associated with the column: DOB
	 */
	public java.lang.String getDob () {
		return dob;
	}

	/**
	 * Set the value related to the column: DOB
	 * @param dob the DOB value
	 */
	public void setDob (java.lang.String dob) {
		this.dob = dob;
	}



	/**
	 * Return the value associated with the column: SSN
	 */
	public java.lang.String getSsn () {
		return ssn;
	}

	/**
	 * Set the value related to the column: SSN
	 * @param ssn the SSN value
	 */
	public void setSsn (java.lang.String ssn) {
		this.ssn = ssn;
	}



	/**
	 * Return the value associated with the column: ADDRESS
	 */
	public java.lang.String getAddress () {
		return address;
	}

	/**
	 * Set the value related to the column: ADDRESS
	 * @param address the ADDRESS value
	 */
	public void setAddress (java.lang.String address) {
		this.address = address;
	}



	/**
	 * Return the value associated with the column: CITY
	 */
	public java.lang.String getCity () {
		return city;
	}

	/**
	 * Set the value related to the column: CITY
	 * @param city the CITY value
	 */
	public void setCity (java.lang.String city) {
		this.city = city;
	}



	/**
	 * Return the value associated with the column: BELL NAME
	 */
	public java.lang.String getBellName () {
		return bellName;
	}

	/**
	 * Set the value related to the column: BELL NAME
	 * @param bellName the BELL NAME value
	 */
	public void setBellName (java.lang.String bellName) {
		this.bellName = bellName;
	}

	/**
	 * Return the value associated with the column: SALUTATION
	 */
	public java.lang.String getSalutation () {
		return salutation;
	}

	/**
	 * Set the value related to the column: SALUTATION
	 * @param bellName the SALUTATION value
	 */
	public void setSalutation (java.lang.String salutation) {
		this.salutation = salutation;
	}

	/**
	 * Return the value associated with the column: DOOR_NO
	 */
	public java.lang.String getDoorNo () {
		return doorNo;
	}

	/**
	 * Set the value related to the column: DOOR_NO
	 * @param doorNo the DOOR_NO value
	 */
	public void setDoorNo (java.lang.String doorNo) {
		this.doorNo = doorNo;
	}

	/**
	 * Return the value associated with the column: BEZERK
	 */
	public java.lang.String getBezerk () {
		return bezerk;
	}

	/**
	 * Set the value related to the column: BEZERK
	 * @param bezerk the BEZERK value
	 */
	public void setBezerk (java.lang.String bezerk) {
		this.bezerk = bezerk;
	}

	/**
	 * Return the value associated with the column: FIRM NAME
	 */
	public java.lang.String getFirmName () {
		return firmName!=null?firmName:"";
	}

	/**
	 * Set the value related to the column: FIRM NAME
	 * @param bellName the FIRM NAME value
	 */
	public void setFirmName (java.lang.String firmName) {
		this.firmName = firmName;
	}

	/**
	 * Return the value associated with the column: ZIP_CODE
	 */
	public java.lang.String getZipCode () {
		return zipCode;
	}

	/**
	 * Set the value related to the column: ZIP_CODE
	 * @param zipCode the ZIP_CODE value
	 */
	public void setZipCode (java.lang.String zipCode) {
		this.zipCode = zipCode;
	}



	/**
	 * Return the value associated with the column: LANDMARK
	 */
	public java.lang.String getLandMark () {
		return landMark;
	}
	
	/**
	 * Set the value related to the column: LANDMARK
	 * @param landmark the LANDMARK value
	 */
	public void setLandMark (java.lang.String landMark) {
		this.landMark = landMark;
	}
	


	/**
	 * Return the value associated with the column: VIP
	 */
	public java.lang.Boolean isVip () {
		return vip;
	}

	/**
	 * Set the value related to the column: VIP
	 * @param vip the VIP value
	 */
	public void setVip (java.lang.Boolean vip) {
		this.vip = vip;
	}



	/**
	 * Return the value associated with the column: CREDIT_LIMIT
	 */
	public java.lang.Double getCreditLimit () {
		return creditLimit!=null?creditLimit:2000.0;
	}

	/**
	 * Set the value related to the column: CREDIT_LIMIT
	 * @param creditLimit the CREDIT_LIMIT value
	 */
	public void setCreditLimit (java.lang.Double creditLimit) {
		this.creditLimit = creditLimit;
	}



	/**
	 * Return the value associated with the column: CREDIT_SPENT
	 */
	public java.lang.Double getCreditSpent () {
		return creditSpent!=null?creditSpent:0.0;
	}

	/**
	 * Set the value related to the column: CREDIT_SPENT
	 * @param creditSpent the CREDIT_SPENT value
	 */
	public void setCreditSpent (java.lang.Double creditSpent) {
		this.creditSpent = creditSpent;
	}


	/**
	 * Return the value associated with the column: CREDIT_CARD_NO
	 */
	public java.lang.String getCreditCardNo () {
		return creditCardNo;
	}

	/**
	 * Set the value related to the column: CREDIT_CARD_NO
	 * @param creditCardNo the CREDIT_CARD_NO value
	 */
	public void setCreditCardNo (java.lang.String creditCardNo) {
		this.creditCardNo = creditCardNo;
	}



	/**
	 * Return the value associated with the column: NOTE
	 */
	public java.lang.String getNote () {
		return note;
	}

	/**
	 * Set the value related to the column: NOTE
	 * @param note the NOTE value
	 */
	public void setNote (java.lang.String note) {
		this.note = note;
	}
	
	
	public java.lang.Integer getPriceCategory() {
		return priceCategory!=null?priceCategory:0;
	}

	public void setPriceCategory(java.lang.Integer priceCategory) {
		this.priceCategory = priceCategory;
	}


	/**
	 * Return the value associated with the column: properties
	 */
	public java.util.Map<String, String> getProperties () {
		return properties;
	}

	/**
	 * Set the value related to the column: properties
	 * @param properties the properties value
	 */
	public void setProperties (java.util.Map<String, String> properties) {
		this.properties = properties;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.Customer)) return false;
		else {
			com.floreantpos.model.Customer customer = (com.floreantpos.model.Customer) obj;
			if (null == this.getAutoId() || null == customer.getAutoId()) return false;
			else return (this.getAutoId().equals(customer.getAutoId()));
		}
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getAutoId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getAutoId().hashCode();
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