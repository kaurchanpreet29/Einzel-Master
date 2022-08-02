package com.floreantpos.model.base;

import java.io.Serializable;

/**
 * This is an object that contains data related to the SALES_REPORT table. Do not
 * modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class table="SALES_REPORT"
 */

public abstract class BaseSalesReport implements Comparable, Serializable {

	// constructors
	public BaseSalesReport() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseSalesReport(java.lang.Integer autoId) {
		this.setId(autoId);
		initialize();
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;
	// fields
	private java.lang.Integer salesid;
	private java.util.Date startdate;
	private java.util.Date enddate;
	private java.lang.String reporttime;
	private java.lang.Double awt;
	private java.lang.Double awtn;
	private java.lang.Double awts;
	private java.lang.Double awtz;
	private java.lang.Double cashpayment;
	private java.lang.Integer cashpaymentcount;
	private java.lang.Double cashtax;
	private java.lang.Double cardpayment;
	private java.lang.Integer cardpaymentcount;
	private java.lang.Double cardtax;
	private java.lang.Double online;
	private java.lang.Double onlinetax;
	private java.util.List<com.floreantpos.model.MenuUsage> menuUsages;
	private java.lang.Double food;
	private java.lang.Double foodtax;
	private java.lang.Double beverage;
	private java.lang.Double beveragetax;
	private java.lang.Integer cancelleditems;
	private java.lang.Double ticketgutscheinamount;
	private java.lang.Double netton;
	private java.lang.Double nettos;
	private java.lang.Double nettoz;
	private java.lang.Integer solditem;
	private java.lang.Double totalwotax;
	private java.lang.Double taxtotal;
	private java.lang.Double taxn;
	private java.lang.Double taxs;
	private java.lang.Double taxz;
	private java.lang.Double cancelledtrans;
	private java.lang.Double cancelledtax;
	private java.lang.Double discount;
	private java.lang.Integer insertticket;
	private java.lang.Double insertamount;
	private java.lang.Double voidticket;
	private java.lang.Double voidamount;
	private java.lang.Double voidtax;
	private java.lang.Double totalinvoices;
	private java.lang.Double pfand1;
	private java.lang.Double pfand2;
	private java.lang.Double pfand3;
	
	private java.lang.Double rabatt_19;
	private java.lang.Double rabatt_7;
	
	private long voidArticles;
	private java.lang.Double btrinkgeld;
	private java.lang.Double Ktrinkgeld;
	private java.lang.Double soldGutschein;
	
	private java.lang.Integer anzahlRetour;  
	private java.lang.Double retourGesamt;
	private java.lang.Double retourTax;
	private java.lang.Double gesamtSumme;
	private java.lang.Double gesamtMwst19;
	private java.lang.Double gesamtMwst7;

	private java.lang.Double rechnugPayment_tax;	
	private java.lang.Integer rechnugPament_anzahl;
	private java.lang.Double rechnugPaymentAmount;
	
	private java.lang.Integer onlinePament_anzahl;
	
	private String gesamt_19_text;
	private String gesamt_7_text;
	private String gesamt_0_text;
	private String netto_19_text;
	private String netto_7_text;
	private String netto_0_text;
	private String mwst_19_text;
	private String mwst_7_text;
	private String mwst_0_text;

	private String cash19Text;
	private String cash7Text;
	private String card19Text;
	private String card7Text;
	
	private Double cash19;
	private Double cash7;
	private Double card19;
	private Double card7;
	

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="identity" column="AUTO_ID"
	 */
	public java.lang.Integer getId() {
		return id;
	}

	public java.lang.Double getSoldGutschein() {
		return soldGutschein != null ? soldGutschein : 0.00;
	}

	public void setSoldGutschein(java.lang.Double soldGutschein) {
		this.soldGutschein = soldGutschein;
	}
	/**
	 * Set the unique identifier of this class
	 * 
	 * @param autoId
	 *          the new ID
	 */
	public void setId(java.lang.Integer autoId) {
		this.id = autoId;
		this.hashCode = Integer.MIN_VALUE;
	}

	protected void initialize() {
	}

	/**
	 * Return the value associated with the column: SALESID
	 */
	public java.lang.Integer getSalesid() {
		return salesid;
	}

	/**
	 * Set the value related to the column: SALESID
	 * 
	 * @param phone
	 *          the SALESID value
	 */
	public void setSalesid(java.lang.Integer salesid) {
		this.salesid = salesid;
	}

	public java.lang.Integer getCashpaymentcount() {
		return cashpaymentcount!=null?cashpaymentcount:0;
	}

	public void setCashpaymentcount(int cashpaymentcount) {
		this.cashpaymentcount = cashpaymentcount;
	}

	public java.lang.Integer getCardpaymentcount() {
		return cardpaymentcount!=null?cardpaymentcount:0;
	}

	public void setCardpaymentcount(Integer cardpaymentcount) {
		this.cardpaymentcount = cardpaymentcount;
	}

	/**
	 * Return the value associated with the column: STARTDATE
	 */
	public java.util.Date getStartdate() {
		return startdate;
	}

	/**
	 * Set the value related to the column: STARTDATE
	 * 
	 * @param phone
	 *          the STARTDATE value
	 */
	public void setStartdate(java.util.Date startdate) {
		this.startdate = startdate;
	}

	/**
	 * Return the value associated with the column: ENDDATE
	 */
	public java.util.Date getEnddate() {
		return enddate;
	}

	/**
	 * Set the value related to the column: ENDDATE
	 * 
	 * @param phone
	 *          the ENDDATE value
	 */
	public void setEnddate(java.util.Date enddate) {
		this.enddate = enddate;
	}

	/**
	 * Return the value associated with the column: REPORTTIME
	 */
	public java.lang.String getReporttime() {
		return reporttime;
	}

	/**
	 * Set the value related to the column: REPORTTIME
	 * 
	 * @param phone
	 *          the REPORTTIME value
	 */
	public void setReporttime(java.lang.String reporttime) {
		this.reporttime = reporttime;
	}

	/**
	 * Return the value associated with the column: AWT
	 */
	public java.lang.Double getAwt() {
		return awt!=null?awt:0.0;
	}

	/**
	 * Set the value related to the column: AWT
	 * 
	 * @param time
	 *          the AWT value
	 */
	public void setAwt(java.lang.Double awt) {
		this.awt = awt;
	}

	/**
	 * Return the value associated with the column: AWTN
	 */
	public java.lang.Double getAwtn() {
		return awtn!=null?awtn:0.0;
	}

	/**
	 * Set the value related to the column: AWTN
	 * 
	 * @param time
	 *          the AWTN value
	 */
	public void setAwtn(java.lang.Double awtn) {
		this.awtn = awtn;
	}

	/**
	 * Return the value associated with the column: AWTS
	 */
	public java.lang.Double getAwts() {
		return awts!=null?awts:0.0;
	}

	/**
	 * Set the value related to the column: AWTS
	 * 
	 * @param time
	 *          the AWTS value
	 */
	public void setAwts(java.lang.Double awts) {
		this.awts = awts;
	}
	
	public java.lang.Double getAwtz() {
		return awtz!=null?awtz:0.0;
	}

	public void setAwtz(java.lang.Double awtz) {
		this.awtz = awtz;
	}

	/**
	 * Return the value associated with the column: CASHPAYMENT
	 */
	public java.lang.Double getCashpayment() {
		return cashpayment!=null?cashpayment:0.0;
	}

	/**
	 * Set the value related to the column: CASHPAYMENT
	 * 
	 * @param time
	 *          the CASHPAYMENT value
	 */
	public void setCashpayment(java.lang.Double cashpayment) {
		this.cashpayment = cashpayment;
	}

	/**
	 * Return the value associated with the column: CASHTAX
	 */
	public java.lang.Double getCashtax() {
		return cashtax!=null?cashtax:0.0;
	}

	/**
	 * Set the value related to the column: CASHTAX
	 * 
	 * @param time
	 *          the CASHTAX value
	 */
	public void setCashtax(java.lang.Double cashtax) {
		this.cashtax = cashtax;
	}

	/**
	 * Return the value associated with the column: CARDPAYMENT
	 */
	public java.lang.Double getCardpayment() {
		return cardpayment!=null?cardpayment:0.0;
	}

	/**
	 * Set the value related to the column: CARDPAYMENT
	 * 
	 * @param time
	 *          the CARDPAYMENT value
	 */
	public void setCardpayment(java.lang.Double cardpayment) {
		this.cardpayment = cardpayment;
	}

	/**
	 * Return the value associated with the column: CARDTAX
	 */
	public java.lang.Double getCardtax() {
		return cardtax!=null?cardtax:0.0;
	}

	/**
	 * Set the value related to the column: CARDTAX
	 * 
	 * @param time
	 *          the CARDTAX value
	 */
	public void setCardtax(java.lang.Double cardtax) {
		this.cardtax = cardtax;
	}

	/**
	 * Return the value associated with the column: ONLINE
	 */
	public java.lang.Double getOnline() {
		return online!=null?online:0.0;
	}

	/**
	 * Set the value related to the column: ONLINE
	 * 
	 * @param time
	 *          the ONLINE value
	 */
	public void setOnline(java.lang.Double online) {
		this.online = online;
	}

	/**
	 * Return the value associated with the column: ONLINETAX
	 */
	public java.lang.Double getOnlinetax() {
		return onlinetax!=null?onlinetax:0.0;
	}

	/**
	 * Set the value related to the column: ONLINETAX
	 * 
	 * @param time
	 *          the ONLINETAX value
	 */
	public void setOnlinetax(java.lang.Double onlinetax) {
		this.onlinetax = onlinetax;
	}

	/**
	 * Return the value associated with the column: FOOD
	 */
	public java.lang.Double getFood() {
		return food!=null?food:0.0;
	}

	/**
	 * Set the value related to the column: FOOD
	 * 
	 * @param time
	 *          the FOOD value
	 */
	public void setFood(java.lang.Double food) {
		this.food = food;
	}
	
	
	
	public long getVoidArticles() {
		return voidArticles!=0?voidArticles:Long.valueOf(0);
	}

	public void setVoidArticles(long voidArticles) {
		this.voidArticles = voidArticles;
	}
	

	/**
	 * Return the value associated with the column: FOODTAX
	 */
	public java.lang.Double getFoodtax() {
		return foodtax!=null?foodtax:0.0;
	}

	/**
	 * Set the value related to the column: FOODTAX
	 * 
	 * @param time
	 *          the FOODTAX value
	 */
	public void setFoodtax(java.lang.Double foodtax) {
		this.foodtax = foodtax;
	}

	/**
	 * Return the value associated with the column: BEVERAGE
	 */
	public java.lang.Double getBeverage() {
		return beverage!=null?beverage:0.0;
	}

	/**
	 * Set the value related to the column: BEVERAGE
	 * 
	 * @param time
	 *          the BEVERAGE value
	 */
	public void setBeverage(java.lang.Double beverage) {
		this.beverage = beverage;
	}

	/**
	 * Return the value associated with the column: BEVERAGETAX
	 */
	public java.lang.Double getBeveragetax() {
		return beveragetax!=null?beveragetax:0.0;
	}

	/**
	 * Set the value related to the column: BEVERAGETAX
	 * 
	 * @param time
	 *          the BEVERAGETAX value
	 */
	public void setBeveragetax(java.lang.Double beveragetax) {
		this.beveragetax = beveragetax;
	}

	/**
	 * Return the value associated with the column: CANCELLEDITEMS
	 */
	public java.lang.Integer getCancelleditems() {
		return cancelleditems!=null?cancelleditems:0;
	}

	/**
	 * Set the value related to the column: CANCELLEDITEMS
	 * 
	 * @param time
	 *          the CANCELLEDITEMS value
	 */
	public void setCancelleditems(java.lang.Integer cancelleditems) {
		this.cancelleditems = cancelleditems;
	}

	/**
	 * Return the value associated with the column: SOLDITEM
	 */
	public java.lang.Integer getSolditem() {
		return solditem!=null?solditem:0;
	}

	/**
	 * Set the value related to the column: SOLDITEM
	 * 
	 * @param time
	 *          the SOLDITEM value
	 */
	public void setSolditem(java.lang.Integer solditem) {
		this.solditem = solditem;
	}

	/**
	 * Return the value associated with the column: TOTALWOTAX
	 */
	public java.lang.Double getTotalwotax() {
		return totalwotax!=null?totalwotax:0.0;
	}

	/**
	 * Set the value related to the column: TOTALWOTAX
	 * 
	 * @param time
	 *          the TOTLAWOTAX value
	 */
	public void setTotalwotax(java.lang.Double totalwotax) {
		this.totalwotax = totalwotax;
	}

	/**
	 * Return the value associated with the column: TAXTOTAL
	 */
	public java.lang.Double getTaxtotal() {
		return taxtotal!=null?taxtotal:0.0;
	}

	/**
	 * Set the value related to the column: TAXTOTAL
	 * 
	 * @param time
	 *          the TAXTOTAL value
	 */
	public void setTaxtotal(java.lang.Double taxtotal) {
		this.taxtotal = taxtotal;
	}

	/**
	 * Return the value associated with the column: TAXN
	 */
	public java.lang.Double getTaxn() {
		return taxn!=null?taxn:0.0;
	}

	/**
	 * Set the value related to the column: TAXN
	 * 
	 * @param time
	 *          the TAXN value
	 */
	public void setTaxn(java.lang.Double taxn) {
		this.taxn = taxn;
	}
	
	public Double getRabatt_19() {
		return rabatt_19!=null?rabatt_19:0.0;
	}

	public void setRabatt_19(Double rabatt_19) {
		this.rabatt_19 = rabatt_19;
	}
	
	public Double getRabatt_7() {
		return rabatt_7!=null?rabatt_7:0.0;
	}

	public void setRabatt_7(Double rabatt_7) {
		this.rabatt_7 = rabatt_7;
	}
	
	public Double getBtrinkgeld() {
		return btrinkgeld!=null?btrinkgeld:0.0;
	}

	public void setBtrinkgeld(Double btrinkgeld) {
		this.btrinkgeld = btrinkgeld;
	}

	public Double getKtrinkgeld() {
		return Ktrinkgeld!=null?Ktrinkgeld:0.0;
	}

	public void setKtrinkgeld(Double ktrinkgeld) {
		Ktrinkgeld = ktrinkgeld;
	}
	
	public void setTicketgutscheinamount (java.lang.Double amount) {
		this.ticketgutscheinamount = amount;
	}
	/**
   Return the value associated with the column: TOTALINVOICES
	 */
	public java.lang.Double getTicketgutscheinamount () {
		return ticketgutscheinamount!=null?ticketgutscheinamount:0.0;
	}
	
	/**
	 * Return the value associated with the column: TAXS
	 */
	public java.lang.Double getTaxs() {
		return taxs!=null?taxs:0.0;
	}

	/**
	 * Set the value related to the column: TAXS
	 * 
	 * @param time
	 *          the TAXS value
	 */
	public void setTaxs(java.lang.Double taxs) {
		this.taxs = taxs;
	}

	/**
	 * Return the value associated with the column: TAXZ
	 */
	public java.lang.Double getTaxz() {
		return taxz!=null?taxz:0.0;
	}

	/**
	 * Set the value related to the column: TAXZ
	 * 
	 * @param time
	 *          the TAXZ value
	 */
	public void setTaxz(java.lang.Double taxz) {
		this.taxz = taxz;
	}


	/**
	 * Return the value associated with the column: CANCELLEDTRANS
	 */
	public java.lang.Double getCancelledtrans() {
		return cancelledtrans!=null?cancelledtrans:0.0;
	}

	/**
	 * Set the value related to the column: CANCELLEDTRANS
	 * 
	 * @param time
	 *          the CANCELLEDTRANS value
	 */
	public void setCancelledtrans(java.lang.Double cancelledtrans) {
		this.cancelledtrans = cancelledtrans;
	}

	/**
	 * Return the value associated with the column: CANCELLEDTAX
	 */
	public java.lang.Double getCancelledtax() {
		return cancelledtax!=null?cancelledtax:0.0;
	}

	/**
	 * Set the value related to the column: CANCELLEDTAX
	 * 
	 * @param time
	 *          the CANCELLEDTAX value
	 */
	public void setCancelledtax(java.lang.Double cancelledtax) {
		this.cancelledtax = cancelledtax;
	}

	/**
	 * Return the value associated with the column: DISCOUNT
	 */
	public java.lang.Double getDiscount() {
		return discount!=null?discount:0.0;
	}

	/**
	 * Set the value related to the column: DISCOUNT
	 * 
	 * @param time
	 *          the DISCOUNT value
	 */
	public void setDiscount(java.lang.Double discount) {
		this.discount = discount;
	}

	/**
	 * Return the value associated with the column: INSERTTICKET
	 */
	public java.lang.Integer getInsertticket() {
		return insertticket!=null?insertticket:0;
	}

	/**
	 * Set the value related to the column: INSERTTICKET
	 * 
	 * @param time
	 *          the INSERT value
	 */
	public void setInsertticket(java.lang.Integer insertticket) {
		this.insertticket = insertticket;
	}

	/**
	 * Return the value associated with the column: INSERTAMOUNT
	 */
	public java.lang.Double getInsertamount() {
		return insertamount!=null?insertamount:0.0;
	}

	/**
	 * Set the value related to the column: INSERTAMOUNT
	 * 
	 * @param time
	 *          the INSERTAMOUNT value
	 */
	public void setInsertamount(java.lang.Double insertamount) {
		this.insertamount = insertamount;
	}

	/**
	 * Return the value associated with the column: VOIDTICKET
	 */
	public java.lang.Double getVoidticket() {
		return voidticket!=null?voidticket:0.0;
	}

	/**
	 * Set the value related to the column: VOIDTICKET
	 * 
	 * @param time
	 *          the VOIDTICKET value
	 */
	public void setVoidticket(java.lang.Double voidticket) {
		this.voidticket = voidticket;
	}

	/**
	 * Return the value associated with the column: VOIDAMOUNT
	 */
	public java.lang.Double getVoidamount() {
		return voidamount!=null?voidamount:0.0;
	}

	/**
	 * Set the value related to the column: VOIDAMOUNT
	 * 
	 * @param time
	 *          the VOIDAMOUNT value
	 */
	public void setVoidamount(java.lang.Double voidamount) {
		this.voidamount = voidamount;
	}

	/**
	 * Return the value associated with the column: VOIDAMOUNT
	 */
	public java.lang.Double getVoidtax() {
		return voidtax!=null?voidtax:0.0;
	}

	/**
	 * Set the value related to the column: VOIDAMOUNT
	 * 
	 * @param time
	 *          the VOIDAMOUNT value
	 */
	public void setVoidtax(java.lang.Double voidtax) {
		this.voidtax = voidtax;
	}

	/**
	 * Return the value associated with the column: TOTALINVOICES
	 */
	public java.lang.Double getTotalinvoices() {
		return totalinvoices!=null?totalinvoices:0;
	}

	/**
	 * Set the value related to the column: TOTALINVOICES * @param time the
	 * TOTALINVOICES value
	 */
	public void setTotalinvoices(java.lang.Double totalinvoices) {
		this.totalinvoices = totalinvoices;
	}

	public java.util.List<com.floreantpos.model.MenuUsage> getMenuUsages() {
		return menuUsages;
	}

	public java.lang.Double getPfand1() {
		return pfand1!=null?pfand1:0.0;
	}

	public void setPfand1(java.lang.Double pfand1) {
		this.pfand1 = pfand1;
	}

	public java.lang.Double getPfand2() {
		return pfand2!=null?pfand2:0.0;
	}

	public void setPfand2(java.lang.Double pfand2) {
		this.pfand2 = pfand2;
	}

	public java.lang.Double getPfand3() {
		return pfand3!=null?pfand3:0.0;
	}

	public void setPfand3(java.lang.Double pfand3) {
		this.pfand3 = pfand3;
	}


	public java.lang.Integer getAnzahlRetour() {
		return anzahlRetour!=null?anzahlRetour:0;
	}

	public void setAnzahlRetour(java.lang.Integer anzahlRetour) {
		this.anzahlRetour = anzahlRetour;
	}

	public java.lang.Double getRetourGesamt() {
		return retourGesamt!=null?retourGesamt:0.0;
	}

	public void setRetourGesamt(java.lang.Double retourGesamt) {
		this.retourGesamt = retourGesamt;
	}

	public java.lang.Double getRetourTax() {
		return retourTax!=null?retourTax:0.0;
	}

	public void setRetourTax(java.lang.Double retourTax) {
		this.retourTax = retourTax;
	}

	public java.lang.Double getGesamtSumme() {
		return gesamtSumme!=null?gesamtSumme:0.0;
	}

	public void setGesamtSumme(java.lang.Double gesamtSumme) {
		this.gesamtSumme = gesamtSumme;
	}

	public java.lang.Double getGesamtMwst19() {
		return gesamtMwst19!=null?gesamtMwst19:0.0;
	}

	public void setGesamtMwst19(java.lang.Double gesamtMwst19) {
		this.gesamtMwst19 = gesamtMwst19;
	}

	public java.lang.Double getGesamtMwst7() {
		return gesamtMwst7!=null?gesamtMwst7:0.0;
	}

	public void setGesamtMwst7(java.lang.Double gesamtMwst7) {
		this.gesamtMwst7 = gesamtMwst7;
	}



	/**
	 * Set the value related to the column: ticketItems
	 * 
	 * @param ticketItems
	 *          the ticketItems value
	 */
	public void setMenuUsages(
			java.util.List<com.floreantpos.model.MenuUsage> menuUsages) {
		this.menuUsages = menuUsages;
	}

	public java.lang.Double getNetton() {
		return netton!=null?netton:0.0;
	}

	public void setNetton(java.lang.Double netton) {
		this.netton = netton;
	}

	public java.lang.Double getNettos() {
		return nettos!=null?nettos:0.0;
	}

	public void setNettos(java.lang.Double nettos) {
		this.nettos = nettos;
	}

	public java.lang.Double getNettoz() {
		return nettoz!=null?nettoz:0.0;
	}

	public void setNettoz(java.lang.Double nettoz) {
		this.nettoz = nettoz;
	}
	
	public java.lang.Double getRechnugPayment_tax() {
		return rechnugPayment_tax!=null?rechnugPayment_tax:0.0;
	}

	public void setRechnugPayment_tax(java.lang.Double rechnugPayment_tax) {
		this.rechnugPayment_tax = rechnugPayment_tax;
	}

	public java.lang.Integer getRechnugPament_anzahl() {
		return rechnugPament_anzahl!=null?rechnugPament_anzahl:0;
	}	
	
	public void setRechnugPament_anzahl(java.lang.Integer rechnugPament_anzahl) {
		this.rechnugPament_anzahl = rechnugPament_anzahl;
	}

	public java.lang.Double getRechnugPaymentAmount() {
		return rechnugPaymentAmount!=null?rechnugPaymentAmount:0.0;
	}

	public void setRechnugPaymentAmount(java.lang.Double rechnugPaymentAmount) {
		this.rechnugPaymentAmount = rechnugPaymentAmount;
	}

	public java.lang.Integer getOnlinePament_anzahl() {
		return onlinePament_anzahl!=null?onlinePament_anzahl:0;
	}

	public void setOnlinePament_anzahl(java.lang.Integer onlinePament_anzahl) {
		this.onlinePament_anzahl = onlinePament_anzahl;
	}

	/*
	 * Gesamt Texts
	 */
	

	public String getGesamt_19_text() {
		return gesamt_19_text;
	}

	public void setGesamt_19_text(String gesamt_19_text) {
		this.gesamt_19_text = gesamt_19_text;
	}

	public String getGesamt_7_text() {
		return gesamt_7_text;
	}

	public void setGesamt_7_text(String gesamt_7_text) {
		this.gesamt_7_text = gesamt_7_text;
	}

	public String getGesamt_0_text() {
		return gesamt_0_text;
	}

	public void setGesamt_0_text(String gesamt_0_text) {
		this.gesamt_0_text = gesamt_0_text;
	}

	public String getNetto_19_text() {
		return netto_19_text;
	}

	public void setNetto_19_text(String netto_19_text) {
		this.netto_19_text = netto_19_text;
	}

	public String getNetto_7_text() {
		return netto_7_text;
	}

	public void setNetto_7_text(String netto_7_text) {
		this.netto_7_text = netto_7_text;
	}

	public String getNetto_0_text() {
		return netto_0_text;
	}

	public void setNetto_0_text(String netto_0_text) {
		this.netto_0_text = netto_0_text;
	}

	public String getMwst_19_text() {
		return mwst_19_text;
	}

	public void setMwst_19_text(String mwst_19_text) {
		this.mwst_19_text = mwst_19_text;
	}

	public String getMwst_7_text() {
		return mwst_7_text;
	}

	public void setMwst_7_text(String mwst_7_text) {
		this.mwst_7_text = mwst_7_text;
	}

	public String getMwst_0_text() {
		return mwst_0_text;
	}

	public void setMwst_0_text(String mwst_0_text) {
		this.mwst_0_text = mwst_0_text;
	}
		
	/**
	 * @return the cash19Text
	 */
	public String getCash19Text() {
		return cash19Text;
	}

	/**
	 * @param cash19Text the cash19Text to set
	 */
	public void setCash19Text(String cash19Text) {
		this.cash19Text = cash19Text;
	}

	/**
	 * @return the cash7Text
	 */
	public String getCash7Text() {
		return cash7Text;
	}

	/**
	 * @param cash7Text the cash7Text to set
	 */
	public void setCash7Text(String cash7Text) {
		this.cash7Text = cash7Text;
	}

	/**
	 * @return the card19Text
	 */
	public String getCard19Text() {
		return card19Text;
	}

	/**
	 * @param card19Text the card19Text to set
	 */
	public void setCard19Text(String card19Text) {
		this.card19Text = card19Text;
	}

	/**
	 * @return the card7Text
	 */
	public String getCard7Text() {
		return card7Text;
	}

	/**
	 * @param card7Text the card7Text to set
	 */
	public void setCard7Text(String card7Text) {
		this.card7Text = card7Text;
	}
	
		
	/**
	 * @return the cash19
	 */
	public Double getCash19() {
		return cash19;
	}

	/**
	 * @param cash19 the cash19 to set
	 */
	public void setCash19(Double cash19) {
		this.cash19 = cash19;
	}

	/**
	 * @return the cash7
	 */
	public Double getCash7() {
		return cash7;
	}

	/**
	 * @param cash7 the cash7 to set
	 */
	public void setCash7(Double cash7) {
		this.cash7 = cash7;
	}

	/**
	 * @return the card19
	 */
	public Double getCard19() {
		return card19;
	}

	/**
	 * @param card19 the card19 to set
	 */
	public void setCard19(Double card19) {
		this.card19 = card19;
	}

	/**
	 * @return the card7
	 */
	public Double getCard7() {
		return card7;
	}

	/**
	 * @param card7 the card7 to set
	 */
	public void setCard7(Double card7) {
		this.card7 = card7;
	}

	public void addToMenuUsages(com.floreantpos.model.MenuUsage menuUsage) {
		if (null == getMenuUsages())
			setMenuUsages(new java.util.ArrayList<com.floreantpos.model.MenuUsage>());
		getMenuUsages().add(menuUsage);
	}


	@Override
	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.floreantpos.model.CallList))
			return false;
		else {
			com.floreantpos.model.Salesreportdb salesreport = (com.floreantpos.model.Salesreportdb) obj;
			if (null == this.getId() || null == salesreport.getId())
				return false;
			else
				return (this.getId().equals(salesreport.getId()));
		}
	}

	public void setCashpaymentcount(java.lang.Integer cashpaymentcount) {
		this.cashpaymentcount = cashpaymentcount;
	}

	@Override
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

	@Override
	public int compareTo(Object obj) {
		if (obj.hashCode() > hashCode())
			return 1;
		else if (obj.hashCode() < hashCode())
			return -1;
		else
			return 0;
	}

	@Override
	public String toString() {
		return super.toString();
	}

}