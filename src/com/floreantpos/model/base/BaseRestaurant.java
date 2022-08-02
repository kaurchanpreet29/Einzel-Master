package com.floreantpos.model.base;

import java.awt.Color;
import java.io.Serializable;
import java.util.Date;


/**
 * This is an object that contains data related to the RESTAURANT table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="RESTAURANT"
 */

public abstract class BaseRestaurant  implements Comparable, Serializable {

	public static String REF = "Restaurant";
	public static String PROP_AUTO_DRAWER_PULL_ENABLE = "autoDrawerPullEnable";
	public static String PROP_ITEM_PRICE_INCLUDES_TAX = "itemPriceIncludesTax";
	public static String PROP_TELEPHONE = "telephone";
	public static String PROP_DRAWER_PULL_MIN = "drawerPullMin";
	public static String PROP_TICKET_FOOTER_MESSAGE = "ticketFooterMessage";
	public static String PROP_SERVICE_CHARGE_PERCENTAGE = "serviceChargePercentage";
	public static String PROP_UNIQUE_ID = "uniqueId";
	public static String PROP_ZIP_CODE = "zipCode";
	public static String PROP_NAME = "name";
	public static String PROP_DEFAULT_GRATUITY_PERCENTAGE = "defaultGratuityPercentage";
	public static String PROP_CURRENCY_NAME = "currencyName";
	public static String PROP_DRAWER_PULL_HOUR = "drawerPullHour";
	public static String PROP_TABLES = "tables";
	public static String PROP_ID = "id";
	public static String PROP_CAPACITY = "capacity";
	public static String PROP_ADDRESS_LINE1 = "addressLine1";
	public static String PROP_ADDRESS_LINE2 = "addressLine2";
	public static String PROP_CURRENCY_SYMBOL = "currencySymbol";
	public static String PROP_ADDRESS_LINE3 = "addressLine3";
	public static String PROP_TICKET_FOOTER_MESSAGE1 = "ticketFooterMessage1";
	public static String PROP_TICKET_FOOTER_MESSAGE2 = "ticketFooterMessage2";
	public static String PROP_SECONDARY_NAME = "secondarName";
	public static String PROP_SECONDARY_TELPHONE = "secondaryTelephone";
	public static String PROP_ITEM_SONSTIGES_TEXT = "itemsonstigestext";
	public static String DUP_DINEIN = "dupdinein";
	// constructors
	public BaseRestaurant () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseRestaurant (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	protected java.lang.Integer uniqueId;
	protected java.lang.String name;
	protected java.lang.String addressLine1;
	protected java.lang.String addressLine2;
	protected java.lang.String addressLine3;
	protected java.lang.String zipCode;
	protected java.lang.String telephone;
	protected java.lang.Integer capacity;
	protected java.lang.Integer tables;
	protected java.lang.Boolean autoDrawerPullEnable;
	protected java.lang.Integer drawerPullHour;
	protected java.lang.Integer drawerPullMin;
	protected java.lang.String currencyName;
	protected java.lang.String currencySymbol;
	protected java.lang.Double serviceChargePercentage;
	protected java.lang.Double defaultGratuityPercentage;
	protected java.lang.String ticketFooterMessage;
	protected java.lang.Boolean itemPriceIncludesTax;
	protected java.lang.String ticketFooterMessage1;
	protected java.lang.String ticketFooterMessage2;
	protected java.lang.String secondaryName;
	protected java.lang.String secondaryTelephone;
	protected java.lang.String licenseKey;
	protected java.lang.String licenseMac;
	protected Date licenseExpiryDate;
	protected String fax;
	protected java.lang.Integer kitchenSerialNo;
	protected java.lang.Integer barSerialNo;
	protected java.lang.String paynow;
	protected java.lang.String paylater;
	protected java.lang.String diverse;
	protected java.lang.String settle;
	protected java.lang.String deleteticket;
	protected java.lang.String reopen;
	protected java.lang.String additem;
	protected java.lang.String ticketpreview;
	protected java.lang.String deleteitem;
	protected java.lang.String oldticket;
	protected java.lang.String closeticket;
	protected java.lang.String splitticket;
	protected java.lang.String itemsonstigestext;
	protected java.lang.String happyhourfrom;
	protected java.lang.String happyhourto;
	protected java.lang.String email;
	protected java.lang.Boolean dupdinein;
	protected java.lang.Integer artikelnummer;	
	protected java.lang.Integer salesid;
	protected Date startToday;
	protected java.lang.Boolean fastPayment;
	protected java.lang.Boolean copyRechnug;
	protected java.lang.Boolean withWarengroup;
	protected java.lang.Boolean showReturnDialog;
	protected java.lang.Boolean autoSalesReport;
	protected java.lang.String autoSalesHour;
	protected java.lang.Integer ticketid;
	protected java.lang.String greetingText;
	protected java.lang.String footerText;
	protected java.lang.String remoteId;	
	protected Date tseActivateDate;
	protected Date tseValidDate;
	protected Color itemTexColor;
	protected Color backButtoColor;
	protected java.lang.String tseVendor;
	protected java.lang.String tseId;	
	protected java.lang.Boolean tseLive;
	protected java.lang.Boolean kellenerSalesReport;
	
	protected java.lang.String certUsbId;


	/**
	 * Return the unique identifier of this class
	 * @hibernate.id
	 *  generator-class="assigned"
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
	/**
	 * Return the value associated with the column: UNIQUE_ID
	 */
	public java.lang.Integer getUniqueId () {
		return uniqueId == null ? Integer.valueOf(0) : uniqueId;
	}

	/**
	 * Set the value related to the column: UNIQUE_ID
	 * @param uniqueId the UNIQUE_ID value
	 */
	public void setUniqueId (java.lang.Integer uniqueId) {
		this.uniqueId = uniqueId;
	}
	public java.lang.String getGreetingText() {
		return greetingText!=null?greetingText:"Wir freuen uns sehr auf Ihnren Besuch und stellen Ihnen folgende Leistungen in Rechnung.";
	}

	public void setGreetingText(java.lang.String greetingText) {
		this.greetingText = greetingText;
	}

	public java.lang.String getFooterText() {
		return footerText!=null?footerText:"Bitte kontrollieren Sie Ihre Ware, spätere Reklamtionen können nicht berücksichtigt werden, Betrag dankend erhalten:";
	}

	public void setFooterText(java.lang.String footerText) {
		this.footerText = footerText;
	}

	/**
	 * Return the value associated with the column: NAME
	 */
	public java.lang.String getName () {
		return name;
	}

	public java.lang.String getItemsonstigestext() {
		return itemsonstigestext;
	}

	public void setItemsonstigestext(java.lang.String itemsonstigestext) {
		this.itemsonstigestext = itemsonstigestext;
	}

	/**
	 * Set the value related to the column: NAME
	 * @param name the NAME value
	 */
	public void setName (java.lang.String name) {
		this.name = name;
	}

	/**
	 * Return the value associated with the column: EMAIL
	 */
	public java.lang.String getEmail () {
		return email;
	}

	/**
	 * Set the value related to the column: EMAIL
	 * @param name the EMAIL value
	 */
	public void setEmail (java.lang.String email) {
		this.email = email;
	}


	/**
	 * Return the value associated with the column: DUPDINEIN
	 */
	public java.lang.Boolean getDupdinein () {
		return dupdinein;
	}

	/**
	 * Set the value related to the column: DUPDINEIN
	 * @param name the DUPDINEIN value
	 */
	public void setDupdinein (java.lang.Boolean dup) {
		this.dupdinein = dup;
	}


	/**
	 * Return the value associated with the column: LICENSE KEY
	 */
	public java.lang.String getLicenseKey () {
		return licenseKey;
	}

	/**
	 * Set the value related to the column: LICENSE KEY
	 * @param name the LICENSE KEY value
	 */
	public void setLicenseKey (java.lang.String licenseKey) {
		this.licenseKey = licenseKey;
	}

	/**
	 * Return the value associated with the column: LICENSE MAC
	 */
	public java.lang.String getLicenseMac () {
		return licenseMac;
	}

	/**
	 * Set the value related to the column: LICENSE EXPIRY DATE
	 * @param name the LICENSE MAC value
	 */
	public void setLicenseExpiryDate(Date licenseExpiryDate) {
		this.licenseExpiryDate = licenseExpiryDate;
	}

	/**
	 * Return the value associated with the column: FAX
	 */
	public java.lang.String getFax () {
		return fax;
	}

	/**
	 * Set the value related to the column: FAX
	 * @param name the FAX value
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	/**
	 * Return the value associated with the column: PAYNOW
	 */
	public java.lang.String getPaynow () {
		return paynow;
	}

	/**
	 * Set the value related to the column: PAYNOW
	 * @param name the PAYNOW value
	 */
	public void setPaynow(String paynow) {
		this.paynow = paynow;
	}


	/**
	 * Return the value associated with the column: PAYLATER
	 */
	public java.lang.String getPaylater () {
		return paylater;
	}

	/**
	 * Set the value related to the column: PAYLATER
	 * @param name the PAYLATER value
	 */
	public void setPaylater(String paylater) {
		this.paylater = paylater;
	}

	/**
	 * Return the value associated with the column: HAPPYHOURFROM
	 */
	public java.lang.String getHappyhourfrom () {
		return happyhourfrom;
	}

	/**
	 * Set the value related to the column: HAPPYHOURFROM
	 * @param name the HAPPYHOURFROM value
	 */
	public void setHappyhourfrom(String hour) {
		this.happyhourfrom = hour;
	}

	/**
	 * Return the value associated with the column: HAPPYHOURTO
	 */
	public java.lang.String getHappyhourto () {
		return happyhourto;
	}

	/**
	 * Set the value related to the column: HAPPYHOURTO
	 * @param name the HAPPYHOURTO value
	 */
	public void setHappyhourto(String hour) {
		this.happyhourto = hour;
	}


	/**
	 * Return the value associated with the column: DIVERSE
	 */
	public java.lang.String getDiverse() {
		return diverse;
	}

	/**
	 * Set the value related to the column: DIVERSE
	 * @param name the DIVERSE value
	 */
	public void setDiverse(String diverse) {
		this.diverse = diverse;
	}

	/**
	 * Return the value associated with the column: SETTLE
	 */
	public java.lang.String getSettle () {
		return settle;
	}

	/**
	 * Set the value related to the column: SETTLE
	 * @param name the SETTLE value
	 */
	public void setSettle(String settle) {
		this.settle = settle;
	}

	/**
	 * Return the value associated with the column: REOPEN
	 */
	public java.lang.String getReopen () {
		return reopen;
	}

	/**
	 * Set the value related to the column: REOPEN
	 * @param name the REOPEN value
	 */
	public void setReopen(String reopen) {
		this.reopen = reopen;
	}

	/**
	 * Return the value associated with the column: DELETETICKET
	 */
	public java.lang.String getDeleteticket () {
		return deleteticket;
	}

	/**
	 * Set the value related to the column: DELETETICKET
	 * @param name the DELETETICKET value
	 */
	public void setDeleteticket(String deleteticket) {
		this.deleteticket = deleteticket;
	}

	/**
	 * Return the value associated with the column: ADDITEM
	 */
	public java.lang.String getAdditem () {
		return additem;
	}

	/**
	 * Set the value related to the column: ADDITEM
	 * @param name the ADDITEM value
	 */
	public void setAdditem(String additem) {
		this.additem = additem;
	}

	/**
	 * Return the value associated with the column: DELETEITEM
	 */
	public java.lang.String getDeleteitem () {
		return deleteitem;
	}

	/**
	 * Set the value related to the column: DELETEITEM
	 * @param name the DELETEITEM value
	 */
	public void setDeleteitem(String deleteitem) {
		this.deleteitem = deleteitem;
	}

	/**
	 * Return the value associated with the column: OLDTICKET
	 */
	public java.lang.String getOldticket () {
		return oldticket;
	}

	/**
	 * Set the value related to the column: OLDTICKET
	 * @param name the OLDTICKET value
	 */
	public void setOldticket(String oldticket) {
		this.oldticket = oldticket;
	}

	/**
	 * Return the value associated with the column: CLOSETICKET
	 */
	public java.lang.String getCloseticket () {
		return closeticket;
	}

	/**
	 * Set the value related to the column: CLOSETICKET
	 * @param name the CLOSETICKET value
	 */
	public void setCloseticket(String closeticket) {
		this.closeticket = closeticket;
	}


	/**
	 * Return the value associated with the column: SPLITTICKET
	 */
	public java.lang.String getSplitticket () {
		return splitticket;
	}

	/**
	 * Set the value related to the column: SPLITTICKET
	 * @param name the SPLITTICKET value
	 */
	public void setSplitticket(String splitticket) {
		this.splitticket = splitticket;
	}


	/**
	 * Return the value associated with the column: TICKETPREVIEW
	 */
	public java.lang.String getTicketpreview () {
		return ticketpreview;
	}

	/**
	 * Set the value related to the column: TICKETPREVIEW
	 * @param name the TICKETPREVIEW value
	 */
	public void setTicketpreview(String ticketPreview) {
		this.ticketpreview = ticketPreview;
	}

	/**
	 * Return the value associated with the column: LICENSE EXPIRY DATE
	 */
	public Date getLicenseExpiryDate () {
		return licenseExpiryDate;
	}

	/**
	 * Set the value related to the column: LICENSE MAC
	 * @param name the LICENSE MAC value
	 */
	public void setLicenseMac (java.lang.String licenseMac) {
		this.licenseMac = licenseMac;
	}

	/**
	 * Return the value associated with the column: ADDRESS_LINE1
	 */
	public java.lang.String getAddressLine1 () {
		return addressLine1;
	}


	/**
	 * Set the value related to the column: ADDRESS_LINE1
	 * @param addressLine1 the ADDRESS_LINE1 value
	 */
	public void setAddressLine1 (java.lang.String addressLine1) {
		this.addressLine1 = addressLine1;
	}



	/**
	 * Return the value associated with the column: ADDRESS_LINE2
	 */
	public java.lang.String getAddressLine2 () {
		return addressLine2;
	}

	/**
	 * Set the value related to the column: ADDRESS_LINE2
	 * @param addressLine2 the ADDRESS_LINE2 value
	 */
	public void setAddressLine2 (java.lang.String addressLine2) {
		this.addressLine2 = addressLine2;
	}



	/**
	 * Return the value associated with the column: ADDRESS_LINE3
	 */
	public java.lang.String getAddressLine3 () {
		return addressLine3;
	}

	/**
	 * Set the value related to the column: ADDRESS_LINE3
	 * @param addressLine3 the ADDRESS_LINE3 value
	 */
	public void setAddressLine3 (java.lang.String addressLine3) {
		this.addressLine3 = addressLine3;
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
	 * Return the value associated with the column: TELEPHONE
	 */
	public java.lang.String getTelephone () {
		return telephone;
	}

	/**
	 * Set the value related to the column: TELEPHONE
	 * @param telephone the TELEPHONE value
	 */
	public void setTelephone (java.lang.String telephone) {
		this.telephone = telephone;
	}



	/**
	 * Return the value associated with the column: CAPACITY
	 */
	public java.lang.Integer getCapacity () {
		return capacity == null ? Integer.valueOf(0) : capacity;
	}

	/**
	 * Set the value related to the column: CAPACITY
	 * @param capacity the CAPACITY value
	 */
	public void setCapacity (java.lang.Integer capacity) {
		this.capacity = capacity;
	}

	/**
	 * Return the value associated with the column: KITCHEN_SERIAL_NO
	 */
	public java.lang.Integer getKitchenSerialNo () {
		return kitchenSerialNo;
	}

	/**
	 * Set the value related to the column: KITCHEN_SERIAL_NO
	 * @param capacity the KITCHEN_SERIAL_NO value
	 */
	public void setKitchenSerialNo (java.lang.Integer serial) {
		this.kitchenSerialNo = serial;
	}

	/**
	 * Return the value associated with the column: BAR_SERIAL_NO
	 */
	public java.lang.Integer getBarSerialNo () {
		return barSerialNo;
	}

	/**
	 * Set the value related to the column: BAR_SERIAL_NO
	 * @param capacity the BAR_SERIAL_NO value
	 */
	public void setBarSerialNo (java.lang.Integer serial) {
		this.barSerialNo = serial;
	}


	/**
	 * Return the value associated with the column: TABLES
	 */
	public java.lang.Integer getTables () {
		return tables == null ? Integer.valueOf(0) : tables;
	}

	/**
	 * Set the value related to the column: TABLES
	 * @param tables the TABLES value
	 */
	public void setTables (java.lang.Integer tables) {
		this.tables = tables;
	}



	/**
	 * Return the value associated with the column: AUTODRAWERPULLENABLE
	 */
	public java.lang.Boolean isAutoDrawerPullEnable () {
		return autoDrawerPullEnable == null ? Boolean.FALSE : autoDrawerPullEnable;
	}

	/**
	 * Set the value related to the column: AUTODRAWERPULLENABLE
	 * @param autoDrawerPullEnable the AUTODRAWERPULLENABLE value
	 */
	public void setAutoDrawerPullEnable (java.lang.Boolean autoDrawerPullEnable) {
		this.autoDrawerPullEnable = autoDrawerPullEnable;
	}



	/**
	 * Return the value associated with the column: DRAWER_PULL_HOUR
	 */
	public java.lang.Integer getDrawerPullHour () {
		return drawerPullHour == null ? Integer.valueOf(0) : drawerPullHour;
	}

	/**
	 * Set the value related to the column: DRAWER_PULL_HOUR
	 * @param drawerPullHour the DRAWER_PULL_HOUR value
	 */
	public void setDrawerPullHour (java.lang.Integer drawerPullHour) {
		this.drawerPullHour = drawerPullHour;
	}



	/**
	 * Return the value associated with the column: DRAWER_PULL_MIN
	 */
	public java.lang.Integer getDrawerPullMin () {
		return drawerPullMin == null ? Integer.valueOf(0) : drawerPullMin;
	}

	/**
	 * Set the value related to the column: DRAWER_PULL_MIN
	 * @param drawerPullMin the DRAWER_PULL_MIN value
	 */
	public void setDrawerPullMin (java.lang.Integer drawerPullMin) {
		this.drawerPullMin = drawerPullMin;
	}



	/**
	 * Return the value associated with the column: CNAME
	 */
	public java.lang.String getCurrencyName () {
		return currencyName;
	}

	/**
	 * Set the value related to the column: CNAME
	 * @param currencyName the CNAME value
	 */
	public void setCurrencyName (java.lang.String currencyName) {
		this.currencyName = currencyName;
	}


	/**
	 * Return the value associated with the column: CSYMBOL
	 */
	public java.lang.String getCurrencySymbol () {
		return currencySymbol;
	}

	/**
	 * Set the value related to the column: CSYMBOL
	 * @param currencySymbol the CSYMBOL value
	 */
	public void setCurrencySymbol (java.lang.String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}


	/**
	 * Return the value associated with the column: SC_PERCENTAGE
	 */
	public java.lang.Double getServiceChargePercentage () {
		return serviceChargePercentage == null ? Double.valueOf(0) : serviceChargePercentage;
	}

	/**
	 * Set the value related to the column: SC_PERCENTAGE
	 * @param serviceChargePercentage the SC_PERCENTAGE value
	 */
	public void setServiceChargePercentage (java.lang.Double serviceChargePercentage) {
		this.serviceChargePercentage = serviceChargePercentage;
	}



	/**
	 * Return the value associated with the column: GRATUITY_PERCENTAGE
	 */
	public java.lang.Double getDefaultGratuityPercentage () {
		return defaultGratuityPercentage == null ? Double.valueOf(0) : defaultGratuityPercentage;
	}

	/**
	 * Set the value related to the column: GRATUITY_PERCENTAGE
	 * @param defaultGratuityPercentage the GRATUITY_PERCENTAGE value
	 */
	public void setDefaultGratuityPercentage (java.lang.Double defaultGratuityPercentage) {
		this.defaultGratuityPercentage = defaultGratuityPercentage;
	}



	/**
	 * Return the value associated with the column: TICKET_FOOTER
	 */
	public java.lang.String getTicketFooterMessage () {
		return ticketFooterMessage;
	}

	/**
	 * Set the value related to the column: TICKET_FOOTER
	 * @param ticketFooterMessage the TICKET_FOOTER value
	 */
	public void setTicketFooterMessage (java.lang.String ticketFooterMessage) {
		this.ticketFooterMessage = ticketFooterMessage;
	}

	/**
	 * Return the value associated with the column: TICKET_FOOTER1
	 */
	public java.lang.String getTicketFooterMessage1 () {
		return ticketFooterMessage1;
	}

	/**
	 * Set the value related to the column: TICKET_FOOTER1
	 * @param ticketFooterMessage the TICKET_FOOTER1 value
	 */
	public void setTicketFooterMessage1 (java.lang.String ticketFooterMessage) {
		this.ticketFooterMessage1 = ticketFooterMessage;
	}

	/**
	 * Return the value associated with the column: TICKET_FOOTER2
	 */
	public java.lang.String getTicketFooterMessage2 () {
		return ticketFooterMessage2;
	}

	/**
	 * Set the value related to the column: TICKET_FOOTER2
	 * @param ticketFooterMessage the TICKET_FOOTER2 value
	 */
	public void setTicketFooterMessage2 (java.lang.String ticketFooterMessage) {
		this.ticketFooterMessage2 = ticketFooterMessage;
	}

	/////////////////
	//added
	

	public Color getItemTexColor() {
		return itemTexColor!=null?itemTexColor:Color.WHITE;
	}

	public void setItemTexColor(Color itemTexColor) {
		this.itemTexColor = itemTexColor;
	}
	

	public Color getBackButtoColor() {
		return backButtoColor!=null?backButtoColor:new Color(2,48,20);
	}

	public void setBackButtoColor(Color backButtoColor) {
		this.backButtoColor = backButtoColor;
	}
	
	
	public java.lang.Integer getArtikelnummer() {
		return artikelnummer;
	}

	public void setArtikelnummer(java.lang.Integer artikelnummer) {
		this.artikelnummer = artikelnummer;
	}	

	/**
	 * Return the value associated with the column: SALES_ID
	 */  
	public java.lang.Integer getSalesid() {
		return salesid;
	}
	/**
	 * Set the unique identifier for the sales report
	 * 
	 * @param salesid
	 *          the new SALES_ID
	 */
	public void setSalesid(java.lang.Integer salesid) {
		this.salesid = salesid;
	}


	public Date getStartToday() {
		return startToday;
	}

	public void setStartToday(Date startToday) {
		this.startToday = startToday;
	}


	public java.lang.Boolean isFastPayment() {
		return fastPayment!=null?fastPayment:false;
	}

	public void setFastPayment(java.lang.Boolean fastPayment) {
		this.fastPayment = fastPayment;
	}

	public java.lang.Boolean isCopyRechnug() {
		return copyRechnug!=null?copyRechnug:false;
	}

	public void setCopyRechnug(java.lang.Boolean copyRechnug) {
		this.copyRechnug = copyRechnug;
	}

	public java.lang.Boolean isWithWarengroup() {
		return withWarengroup!=null?withWarengroup:true;
	}

	public void setWithWarengroup(java.lang.Boolean withoutWarengroup) {
		this.withWarengroup = withoutWarengroup;
	}

	public java.lang.Boolean isShowReturnDialog() {
		return showReturnDialog!=null?showReturnDialog:true;
	}

	public void setShowReturnDialog(java.lang.Boolean showReturnDialog) {
		this.showReturnDialog = showReturnDialog;
	}

	public java.lang.Boolean isAutoSalesReport() {
		return autoSalesReport!=null?autoSalesReport:false;
	}

	public void setAutoSalesReport(java.lang.Boolean autoSalesReport) {
		this.autoSalesReport = autoSalesReport;
	}

	public java.lang.String getAutoSalesHour() {
		return autoSalesHour!=null?autoSalesHour:"23";
	}

	public void setAutoSalesHour(java.lang.String autoSalesHour) {
		this.autoSalesHour = autoSalesHour;
	}

	public java.lang.Integer getTicketid() {
		return ticketid!=null?ticketid:1;
	}

	public void setTicketid(java.lang.Integer ticketid) {
		this.ticketid = ticketid;
	}

	//till here

	/**
	 * Return the value associated with the column: SECONDARY_NAME
	 */
	public java.lang.String getSecondaryName () {
		return secondaryName;
	}

	/**
	 * Set the value related to the column: SECONDARY_NAME
	 * @param secondarName the SECONDARY_NAME value
	 */
	public void setSecondaryName (java.lang.String secondaryName) {
		this.secondaryName = secondaryName;
	}

	/**
	 * Return the value associated with the column: SECONDARY_TELEPHONE
	 */
	public java.lang.String getSecondaryTelephone() {
		return secondaryTelephone;
	}

	/**
	 * Set the value related to the column: SECONDARY_TELEPHONE
	 * @param secondaryTelephone the SECONDARY_TELEPHONE value
	 */
	public void setSecondaryTelephone (java.lang.String secondaryTelephone) {
		this.secondaryTelephone = secondaryTelephone;
	}

	/////////////////
	
	protected java.lang.String updateLink;

	public java.lang.String getUpdateLink() {
		return updateLink!=null?updateLink:"http://khana-gmbh.de/POS/update/einzel/updateCheck.json";
	}

	public void setUpdateLink(java.lang.String updateLink) {
		this.updateLink = updateLink;
	}
	
	/**
	 * Return the value associated with the column: PRICE_INCLUDES_TAX
	 */
	public java.lang.Boolean isItemPriceIncludesTax () {
		return itemPriceIncludesTax == null ? Boolean.FALSE : itemPriceIncludesTax;
	}

	/**
	 * Set the value related to the column: PRICE_INCLUDES_TAX
	 * @param itemPriceIncludesTax the PRICE_INCLUDES_TAX value
	 */
	public void setItemPriceIncludesTax (java.lang.Boolean itemPriceIncludesTax) {
		this.itemPriceIncludesTax = itemPriceIncludesTax;
	}

	/*
	 * Tse Data
	 */
	
	public java.lang.String getTseVendor() {
		return tseVendor;
	}

	public void setTseVendor(java.lang.String tseVendor) {
		this.tseVendor = tseVendor;
	}
	public java.lang.String getTseId() {
		return tseId!=null?tseId:"";
	}

	public void setTseId(java.lang.String tseId) {
		this.tseId = tseId;
	}

	
	public java.lang.Boolean isTseLive() {
		return tseLive!=null?tseLive:false;
	}

	public void setTseLive(java.lang.Boolean tseLive) {
		this.tseLive = tseLive;
	}

	public java.lang.Boolean isKellenerSalesReport() {
		return kellenerSalesReport!=null?kellenerSalesReport:false;
	}

	public void setKellenerSalesReport(java.lang.Boolean kellenerSalesReport) {
		this.kellenerSalesReport = kellenerSalesReport;
	}
	
	
	
	public java.lang.String getRemoteId() {
		return remoteId;
	}

	public void setRemoteId(java.lang.String remoteId) {
		this.remoteId = remoteId;
	}
	
	public java.lang.String getCertUsbId() {
		return certUsbId;
	}

	public void setCertUsbId(java.lang.String certUsbId) {
		this.certUsbId = certUsbId;
	}
	
	
	/**
	 * @return the tseActivateDate
	 */
	public Date getTseActivateDate() {
		return tseActivateDate;
	}

	/**
	 * @param tseActivateDate the tseActivateDate to set
	 */
	public void setTseActivateDate(Date tseActivateDate) {
		this.tseActivateDate = tseActivateDate;
	}

	/**
	 * @return the tseValidDate
	 */
	public Date getTseValidDate() {
		return tseValidDate;
	}

	/**
	 * @param tseValidDate the tseValidDate to set
	 */
	public void setTseValidDate(Date tseValidDate) {
		this.tseValidDate = tseValidDate;
	}
	
	
	
	

	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.Restaurant)) return false;
		else {
			com.floreantpos.model.Restaurant restaurant = (com.floreantpos.model.Restaurant) obj;
			if (null == this.getId() || null == restaurant.getId()) return false;
			else return (this.getId().equals(restaurant.getId()));
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
