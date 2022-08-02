package com.floreantpos.model.base;

import java.io.Serializable;

import com.floreantpos.model.CashBookData;


/**
 * This is an object that contains data related to the RESERVATION table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="CASHBOOK"
 */

public abstract class BaseCashbookData  implements Comparable, Serializable {

	// constructors
	public BaseCashbookData () {
	}

	/**
	 * Constructor for primary key
	 */
	public BaseCashbookData (java.lang.Integer autoId) {
		this.setId(autoId);
	}
	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	private java.lang.String beschreibung;
	private boolean einzahlung;
	private boolean auszahlung;
	private java.lang.Integer konto;  


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

	public int getHashCode() {
		return hashCode;
	}

	public void setHashCode(int hashCode) {
		this.hashCode = hashCode;
	}

	public java.lang.String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(java.lang.String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public boolean isEinzahlung() {
		return einzahlung;
	}

	public void setEinzahlung(boolean einzahlung) {
		this.einzahlung = einzahlung;
	}

	public boolean isAuszahlung() {
		return auszahlung;
	}

	public void setAuszahlung(boolean auszahlung) {
		this.auszahlung = auszahlung;
	}

	public java.lang.Integer getKonto() {
		return konto!=null?konto:0;
	}

	public void setKonto(java.lang.Integer konto) {
		this.konto = konto;
	}

	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof CashBookData)) return false;
		else {
			CashBookData cashbook = (CashBookData) obj;
			if (null == this.getId() || null == cashbook.getId()) return false;
			else return (this.getId().equals(cashbook.getId()));
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
		return this.getBeschreibung();
	}
}