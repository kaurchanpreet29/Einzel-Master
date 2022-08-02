package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;


/**
 * This is an object that contains data related to the RESERVATION table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="PROPERTYDATA"
 */

public abstract class BasePropertyData  implements Comparable, Serializable {

	// constructors
	public BasePropertyData () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BasePropertyData (java.lang.Integer autoId) {
		this.setId(autoId);
		initialize();
	}
	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;
	// fields
	
	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="AUTO_ID"
     */
	public java.lang.Integer getId () {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * @param autoId the new ID
	 */
	public void setId (java.lang.Integer id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Constructor for required fields
	 */
	public BasePropertyData (
		java.lang.Integer id,
		java.lang.String propertytext
		) {

		this.setId(id);
		this.setPropertytext(propertytext);
		initialize();
	}

	protected void initialize () {}

	String propertytext;

	public String getPropertytext() {
		return propertytext;
	}

	public void setPropertytext(String propertytext) {
		this.propertytext = propertytext;
	}
	
	
	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.PropertyData)) return false;
		else {
			com.floreantpos.model.PropertyData property = (com.floreantpos.model.PropertyData) obj;
			if (null == this.getId() || null == property.getId()) return false;
			else return (this.getId().equals(property.getId()));
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