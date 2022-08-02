package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;


/**
 * This is an object that contains data related to the RESERVATION table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="CASHBOOK"
 */

public abstract class BaseCashbook  implements Comparable, Serializable {

	// constructors
	public BaseCashbook () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseCashbook (java.lang.Integer autoId) {
		this.setId(autoId);
		initialize();
	}
	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;
	// fields
	private java.util.Date date;
	private java.lang.String beschreibung;
	private java.lang.String bezeichnung;
	private java.lang.Integer beschreibungId;
	private java.lang.Boolean adddata;
	private java.lang.Double betrag;
	private java.lang.Double tax;
	private java.lang.Double taxamount;
	private java.lang.Integer konto;
	private java.lang.Integer salesId;

	
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
	public BaseCashbook (
			java.lang.Integer autoId,
			java.util.Date date,
			java.lang.Double betrag,
			java.lang.String beschreibung,
			java.lang.String bezeichnung,
			java.lang.Boolean add) {

		this.setId(autoId);
		this.setDate(date);
		this.setBetrag(betrag);
		this.setBeschreibung(beschreibung);
		this.setBezeichnung(bezeichnung);
		this.setAdddata(add);
		initialize();
	}

	public java.lang.Double getTax() {
		return tax;
	}
	public java.lang.Integer getKonto() {
		return konto;
	}

	public void setKonto(java.lang.Integer konto) {
		this.konto = konto;
	}
	
	public java.lang.Integer getSalesId() {
		return salesId!=null?salesId:0;
	}

	public void setSalesId(java.lang.Integer salesId) {
		this.salesId = salesId;
	}
	
	public void setTax(java.lang.Double tax) {
		this.tax = tax;
	}

	public java.lang.Double getTaxamount() {
		return taxamount;
	}

	public void setTaxamount(java.lang.Double taxamount) {
		this.taxamount = taxamount;
	}

	protected void initialize () {}

	public java.lang.Double getBetrag() {
		return betrag;
	}

	public void setBetrag(java.lang.Double betrag) {
		this.betrag = betrag;
	}
	public int getHashCode() {
		return hashCode;
	}

	public void setHashCode(int hashCode) {
		this.hashCode = hashCode;
	}

	public java.util.Date getDate() {
		return date;
	}

	public void setDate(java.util.Date date) {
		this.date = date;
	}

	public java.lang.String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(java.lang.String beschreibung) {
		this.beschreibung = beschreibung;
	}
	
	public java.lang.Integer getBeschreibungId() {
	    return beschreibungId;
	  }

	  public void setBeschreibungId(java.lang.Integer beschreibungId) {
	    this.beschreibungId = beschreibungId;
	  }

	public java.lang.String getBezeichnung() {
		return bezeichnung;
	}

	public void setBezeichnung(java.lang.String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public java.lang.Boolean getAdddata() {
		return adddata;
	}

	public void setAdddata(java.lang.Boolean adddata) {
		this.adddata = adddata;
	}

	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.Cashbook)) return false;
		else {
			com.floreantpos.model.Cashbook cashbook = (com.floreantpos.model.Cashbook) obj;
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
		return super.toString();
	}
}