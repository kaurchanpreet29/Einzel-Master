package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;


/**
 * This is an object that contains data related to the DELIVERYCOST table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="DELIVERYCOST"
 */

public abstract class BaseDeliveryCost  implements Comparable, Serializable {

	// constructors
	public BaseDeliveryCost () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseDeliveryCost (java.lang.Integer autoId) {
		this.setId(autoId);
		initialize();
	}
	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;
	// fields
	private java.lang.Integer lieferzeit;
	private java.lang.Integer plz;
	private java.lang.String bezirk;
	private java.lang.String ort;
	private java.lang.Double lieferkosten;
	private java.lang.Double mindest;
	private java.lang.Boolean isopen;
	
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
	public BaseDeliveryCost (
		java.lang.Integer autoId,
		java.lang.String bezirk,
		java.lang.Double lieferkosten,
		java.lang.Integer lieferzeit,
		java.lang.Double mindest,
		java.lang.Boolean isopen,
		java.lang.String ort,
		java.lang.Integer plz) {

		this.setId(autoId);
		this.setBezirk(bezirk);
		this.setLieferkosten(lieferkosten);
		this.setLieferzeit(lieferzeit);
		this.setMindest(mindest);
		this.setIsopen(isopen);
		this.setOrt(ort);
		this.setPlz(plz);
		initialize();
	}

	protected void initialize () {}


	/**
	 * Return the value associated with the column: LIEFERKOSTEN
	 */
	public java.lang.Double getLieferkosten () {
		return lieferkosten;
	}

	/**
	 * Set the value related to the column: LIEFERKOSTEN
	 * @param phone the LIEFERKOSTEN value
	 */
	public void setLieferkosten (java.lang.Double lieferkosten) {
		this.lieferkosten = lieferkosten;
	}
	
	/**
	 * Return the value associated with the column: LIEFERZEIT
	 */
	public java.lang.Integer getLieferzeit () {
		return lieferzeit;
	}

	/**
	 * Set the value related to the column: LIEFERZEIT
	 * @param phone the LIEFERZEIT value
	 */
	public void setLieferzeit(java.lang.Integer lieferzeit) {
		this.lieferzeit = lieferzeit;
	}
	
	/**
	 * Return the value associated with the column: MINDEST
	 */
	public java.lang.Double getMindest () {
		return mindest;
	}

	/**
	 * Set the value related to the column: MINDEST
	 * @param phone the MINDEST value
	 */
	public void setMindest (java.lang.Double mindest) {
		this.mindest = mindest;
	}
	/**
	 * Return the value associated with the column: PLZ
	 */
	public java.lang.Integer getPlz () {
		return plz;
	}

	/**
	 * Set the value related to the column: PLZ
	 * @param time the PLZ value
	 */
	public void setPlz (java.lang.Integer plz) {
		this.plz = plz;
	}
	
	
	/**
	 * Return the value associated with the column: BEZIRK
	 */
	public java.lang.String getBezirk () {
		return bezirk;
	}

	/**
	 * Set the value related to the column: BEZIRK
	 * @param time the BEZIRK value
	 */
	public void setBezirk (java.lang.String bezirk) {
		this.bezirk = bezirk;
	}
	
	/**
	 * Return the value associated with the column: ORT
	 */
	public java.lang.String getOrt () {
		return ort;
	}

	/**
	 * Set the value related to the column: OPEN
	 * @param time the OPEN value
	 */
	public void setIsopen(java.lang.Boolean isopen) {
		this.isopen = isopen;
	}
	
	/**
	 * Return the value associated with the column: OPEN
	 */
	public java.lang.Boolean getIsopen () {
		return isopen;
	}

	/**
	 * Set the value related to the column: ORT
	 * @param time the ORT value
	 */
	public void setOrt (java.lang.String ort) {
		this.ort = ort;
	}
	
	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.Gebiet)) return false;
		else {
			com.floreantpos.model.Gebiet gebiet = (com.floreantpos.model.Gebiet) obj;
			if (null == this.getId() || null == gebiet.getId()) return false;
			else return (this.getId().equals(gebiet.getId()));
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