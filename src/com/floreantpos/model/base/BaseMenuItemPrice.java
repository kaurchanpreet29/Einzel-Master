package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;


/**
 * This is an object that contains data related to the MENUITEMPRICE table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="MENUITEMPRICE"
 */

public abstract class BaseMenuItemPrice  implements Comparable,Serializable {

	// constructors
	public BaseMenuItemPrice () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseMenuItemPrice (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}
	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;
	// fields
	private java.lang.String name;
	private java.lang.Double price;
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
	 * @param autoId the new ID
	 */
	public void setId (java.lang.Integer id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Constructor for required fields
	 */
	public BaseMenuItemPrice (
		java.lang.Integer id,
		java.lang.String name,
		java.lang.Double price
		) {

		this.setId(id);
		this.setName(name);
		this.setPrice(price);
		initialize();
	}

	protected void initialize () {}

	public java.lang.String getName () {
		return name;
	}
	public void setName (java.lang.String name) {
		this.name = name;		 
	}
	
	public java.lang.Double getPrice () {
		return price;
	}
	
	public void setPrice (java.lang.Double price) {
		this.price = price;
	}
	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.MenuItemPrice)) return false;
		else {
			com.floreantpos.model.MenuItemPrice menuitemprice = (com.floreantpos.model.MenuItemPrice) obj;
			if (null == this.getId() || null == menuitemprice.getId()) return false;
			else return (this.getId().equals(menuitemprice.getId()));
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
}