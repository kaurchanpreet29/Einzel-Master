package com.floreantpos.model.base;

import java.io.Serializable;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonManagedReference;


/**
 * This is an object that contains data related to the MENU_ITEM table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="MENU_ITEM"
 */

public abstract class BaseMenuItem  implements Comparable, Serializable {
	
	public static String REF = "MenuItem";
	public static String PROP_ITEM_ID = "itemId";
	public static String PROP_SUB_ITEM_ID = "subitemId";
	public static String PROP_BUY_PRICE = "buyPrice";
	public static String PROP_BARCODE = "barcode";
	public static String PROP_BARCODE1 = "barcode1";
	public static String PROP_BARCODE2 = "barcode2";
	public static String PROP_PARENT = "parent";
	public static String PROP_VISIBLE = "visible";
	public static String PROP_CHANGE_PRICE = "changeprice";
	public static String PROP_SHOW_IMAGE_ONLY = "showImageOnly";
	public static String PROP_DISCOUNT_RATE = "discountRate";
	public static String PROP_TAX = "tax";
	public static String PROP_NAME = "name";
	public static String PROP_LIFERANT_NAME = "lieferantName";
	public static String PROP_RECEPIE = "recepie";
	public static String PROP_PRICE = "price";
	public static String PROP_LIFERANT_BUY_PRICE = "liferantBuyPrice";
	
	public static String PROP_IMAGE = "image";
	public static String PROP_ID = "id";
	
	public static String PROP_PRICE_CATEGORY = "priceCategory";
	public static String PROP_VIRTUAL_PRINTER = "virtualPrinter";

	// constructors
	public BaseMenuItem () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseMenuItem (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseMenuItem (
			java.lang.Integer id,
			java.lang.String itemId,
			java.lang.String name,
			java.lang.Double buyPrice,
			java.lang.Double price) {

		this.setId(id);
		this.setName(name);
		this.setBuyPrice(buyPrice);
		this.setPrice(price);
		this.setItemId(itemId);

		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	java.util.Date modifiedTime;

	// fields
	protected java.lang.String barcode;
	protected java.lang.Double buyPrice;	
	protected java.lang.Double liferantBuyPrice;
	
	protected java.lang.Double discountRate;
	protected byte[] image;
	protected java.lang.Integer red;
	protected java.lang.Integer green;
	protected java.lang.Integer blue;

	protected java.lang.Integer fred;
	protected java.lang.Integer fgreen;
	protected java.lang.Integer fblue;

	protected java.lang.String name;
	protected java.lang.String lieferantName;
	
	protected java.lang.String description;
	protected java.lang.String description2;
	protected java.lang.String note;
	protected java.lang.String itemId;
	protected java.lang.String subitemid;
	protected java.lang.Boolean type;
	protected java.lang.Double price;
	protected java.lang.Boolean showImageOnly;
	protected java.lang.Boolean visible;
	protected java.lang.Boolean changeprice;
	protected java.lang.Double weightgrams;
	protected java.lang.Integer instock;
	protected java.lang.Integer sold;
	protected java.lang.Integer damaged;
	protected java.lang.Integer priceCategory;
	protected java.lang.String unitType;
	protected java.lang.Boolean pfand;
	protected java.lang.String barcode1;
	protected java.lang.String barcode2;
	
	// many to one
	private com.floreantpos.model.MenuGroup parent;
	private com.floreantpos.model.inventory.Recepie recepie;
	private com.floreantpos.model.Tax tax;
	private com.floreantpos.model.VirtualPrinter virtualPrinter;

	// collections
	private java.util.List<com.floreantpos.model.MenuItemModifierGroup> menuItemModiferGroups;
	private java.util.List<com.floreantpos.model.MenuItemShift> shifts;
	@JsonManagedReference
	private java.util.List<com.floreantpos.model.MenuItemPrice> menuitemprice; 


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

	public java.lang.Boolean isChangeprice() {
		return changeprice!=null?changeprice:false;
	}

	public void setChangeprice(java.lang.Boolean changeprice) {
		this.changeprice = changeprice;
	}
	public java.lang.Integer getInstock() {
		return instock!=null?instock:0;
	}

	public void setInstock(java.lang.Integer instock) {
		this.instock = instock;
	}

	public java.lang.Integer getSold() {
		return sold;
	}

	public void setSold(java.lang.Integer sold) {
		this.sold = sold;
	}

	public java.lang.Integer getDamaged() {
		return damaged;
	}

	public void setDamaged(java.lang.Integer damaged) {
		this.damaged = damaged;
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



	/**
	 * Return the value associated with the column: BARCODE
	 */
	public java.lang.String getBarcode () {
		return barcode;
	}
	/**
	 * Return the value associated with the column: DESCRIPTION
	 */
	public java.lang.String getDescription () {
		return description;
	}

	public void setDescription (java.lang.String description) {
		this.description = description;
	}
	
	public java.lang.String getDescription2 () {
		return description2;
	}

	public void setDescription2 (java.lang.String description2) {
		this.description2 = description2;
	}
	
	/**
	 * Return the value associated with the column: NOTE
	 */
	public java.lang.String getNote () {
		return note;
	}

	/**
	 * Set the value related to the column: NOTE
	 * @param name the NOTE value
	 */
	public void setNote (java.lang.String note) {
		this.note = note;
	}


	/**
	 * Set the value related to the column: BARCODE
	 * @param barcode the BARCODE value
	 */
	public void setBarcode (java.lang.String barcode) {
		this.barcode = barcode;
	}



	/**
	 * Return the value associated with the column: BUY_PRICE
	 */
	public java.lang.Double getBuyPrice () {
		return buyPrice == null ? Double.valueOf(0) : buyPrice;
	}

	/**
	 * Set the value related to the column: BUY_PRICE
	 * @param buyPrice the BUY_PRICE value
	 */
	public void setBuyPrice (java.lang.Double buyPrice) {
		this.buyPrice = buyPrice;
	}
	
	public java.lang.Double getLiferantBuyPrice () {
		return liferantBuyPrice == null ? Double.valueOf(0) : liferantBuyPrice;
	}

	
	public void setLiferantBuyPrice (java.lang.Double liferantBuyPrice) {
		this.liferantBuyPrice = liferantBuyPrice;
	}
	
	
	/**
	 * Return the value associated with the column: DISCOUNT_RATE
	 */
	public java.lang.Double getDiscountRate () {
		return discountRate == null ? Double.valueOf(0) : discountRate;
	}

	/**
	 * Set the value related to the column: DISCOUNT_RATE
	 * @param discountRate the DISCOUNT_RATE value
	 */
	public void setDiscountRate (java.lang.Double discountRate) {
		this.discountRate = discountRate;
	}

	public void addToItemPrice(com.floreantpos.model.MenuItemPrice menuitemprice) {
		if (null == getMenuitemprice())
			setMenuitemprice(new java.util.ArrayList<com.floreantpos.model.MenuItemPrice>());
		getMenuitemprice().add(menuitemprice);
	}

	 
	
	/**
	 * Return the value associated with the column: IMAGE
	 */
	public byte[] getImage () {
		return image;
	}

	/**
	 * Set the value related to the column: IMAGE
	 * @param image the IMAGE value
	 */
	public void setImage (byte[] image) {
		this.image = image;
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
	
	public java.lang.String getLieferantName () {
		return lieferantName;
	}

	public void setLieferantName (java.lang.String lieferantName) {
		this.lieferantName = lieferantName;
	}
	
	
	/**
	 * Return the value associated with the column: ITEM ID
	 */
	public java.lang.String getItemId () {
		return itemId;
	}


	/**
	 * Set the value related to the column: ITEM ID
	 * @param itemId the ITEM_ID value
	 */
	public void setItemId (java.lang.String itemId) {
		this.itemId = itemId;
	}
	/**
	 * Return the value associated with the column: SUB ITEM ID
	 */
	public java.lang.String getSubitemid () {
		return subitemid;
	}

	/**
	 * Set the value related to the column: SUB ITEM ID
	 * @param itemId the SUB_ITEM_ID value
	 */
	public void setSubitemid (java.lang.String subitemId) {
		this.subitemid = subitemId;
	}

	/**
	 * Return the value associated with the column: menuitemprice
	 */
	public java.util.List<com.floreantpos.model.MenuItemPrice> getMenuitemprice() {
		return menuitemprice;
	}

	/**
	 * Set the value related to the column: menuitemprice
	 * 
	 * @param shifts
	 *          the menuitemprice value
	 */
	public void setMenuitemprice(
			java.util.List<com.floreantpos.model.MenuItemPrice> menuitemprice) {
		this.menuitemprice = menuitemprice;
	}

	/**
	 * Return the value associated with the column: PRICE
	 */
	public java.lang.Double getPrice () {
		return price == null ? Double.valueOf(0) : price;
	}

	/**
	 * Set the value related to the column: PRICE
	 * @param price the PRICE value
	 */
	public void setPrice (java.lang.Double price) {
		this.price = price;
	}



	/**
	 * Return the value associated with the column: SHOW_IMAGE_ONLY
	 */
	public java.lang.Boolean isShowImageOnly () {
		return showImageOnly == null ? Boolean.FALSE : showImageOnly;
	}

	/**
	 * Set the value related to the column: SHOW_IMAGE_ONLY
	 * @param showImageOnly the SHOW_IMAGE_ONLY value
	 */
	public void setShowImageOnly (java.lang.Boolean showImageOnly) {
		this.showImageOnly = showImageOnly;
	}



	/**
	 * Return the value associated with the column: VISIBLE
	 */
	public java.lang.Boolean isVisible () {
		return visible == null ? Boolean.valueOf(true) : visible;
	}

	/**
	 * Set the value related to the column: VISIBLE
	 * @param visible the VISIBLE value
	 */
	public void setVisible (java.lang.Boolean visible) {
		this.visible = visible;
	}
	/**
	 * Set the value related to the column: WEIGHTGRAMS
	 * @param visible the WEIGHTGRAMS value
	 */
	public void setWeightgrams (java.lang.Double weightgrams) {
		this.weightgrams = weightgrams;
	}

	/**
	 * Return the value associated with the column: WEIGHTGRAMS
	 */
	public java.lang.Double getWeightgrams () {
		return weightgrams;
	}


	/**
	 * Custom property
	 */
	public static String getVisibleDefaultValue () {
		return "true";
	}


	/**
	 * Return the value associated with the column: GROUP_ID
	 */
	public com.floreantpos.model.MenuGroup getParent () {
		return parent;
	}

	/**
	 * Set the value related to the column: GROUP_ID
	 * @param parent the GROUP_ID value
	 */
	public void setParent (com.floreantpos.model.MenuGroup parent) {
		this.parent = parent;
	}

	public java.lang.Integer getFred() {
		return fred != null ? fred : 255;
	}

	public void setFred(java.lang.Integer fred) {
		this.fred = fred;
	}

	public java.lang.Integer getFgreen() {
		return fgreen != null ? fgreen : 255;
	}

	public void setFgreen(java.lang.Integer fgreen) {
		this.fgreen = fgreen;
	}

	public java.lang.Integer getFblue() {
		return fblue != null ? fblue : 255;
	}

	public void setFblue(java.lang.Integer fblue) {
		this.fblue = fblue;
	}
	
	public java.lang.Integer getPriceCategory() {
		return priceCategory!=null?priceCategory:1;
	}

	public void setPriceCategory(java.lang.Integer priceCategory) {
		this.priceCategory = priceCategory;
	}

	public java.lang.String getUnitType() {
		return unitType!=null?unitType:"";
	}

	public void setUnitType(java.lang.String unitType) {
		this.unitType = unitType;
	}


	public java.lang.Boolean isPfand() {
		return pfand!=null?pfand:false;
	}

	public void setPfand(java.lang.Boolean pfand) {
		this.pfand = pfand;
	}

	public java.lang.String getBarcode1() {
		return barcode1;
	}

	public void setBarcode1(java.lang.String barcode1) {
		this.barcode1 = barcode1;
	}

	public java.lang.String getBarcode2() {
		return barcode2;
	}

	public void setBarcode2(java.lang.String barcode2) {
		this.barcode2 = barcode2;
	}


	/**
	 * Return the value associated with the column: RECEPIE
	 */
	public com.floreantpos.model.inventory.Recepie getRecepie () {
		return recepie;
	}

	/**
	 * Set the value related to the column: RECEPIE
	 * @param recepie the RECEPIE value
	 */
	public void setRecepie (com.floreantpos.model.inventory.Recepie recepie) {
		this.recepie = recepie;
	}



	/**
	 * Return the value associated with the column: TAX_ID
	 */
	public com.floreantpos.model.Tax getTax () {
		return tax;
	}

	/**
	 * Set the value related to the column: TAX_ID
	 * @param tax the TAX_ID value
	 */
	public void setTax (com.floreantpos.model.Tax tax) {
		this.tax = tax;
	}



	/**
	 * Return the value associated with the column: VPRINTER_ID
	 */
	public com.floreantpos.model.VirtualPrinter getVirtualPrinter () {
		return virtualPrinter;
	}

	/**
	 * Set the value related to the column: VPRINTER_ID
	 * @param virtualPrinter the VPRINTER_ID value
	 */
	public void setVirtualPrinter (com.floreantpos.model.VirtualPrinter virtualPrinter) {
		this.virtualPrinter = virtualPrinter;
	}



	/**
	 * Return the value associated with the column: menuItemModiferGroups
	 */
	public java.util.List<com.floreantpos.model.MenuItemModifierGroup> getMenuItemModiferGroups () {
		return menuItemModiferGroups;
	}

	/**
	 * Set the value related to the column: menuItemModiferGroups
	 * @param menuItemModiferGroups the menuItemModiferGroups value
	 */
	public void setMenuItemModiferGroups (java.util.List<com.floreantpos.model.MenuItemModifierGroup> menuItemModiferGroups) {
		this.menuItemModiferGroups = menuItemModiferGroups;
	}

	public void addTomenuItemModiferGroups (com.floreantpos.model.MenuItemModifierGroup menuItemModifierGroup) {
		if (null == getMenuItemModiferGroups()) setMenuItemModiferGroups(new java.util.ArrayList<com.floreantpos.model.MenuItemModifierGroup>());
		getMenuItemModiferGroups().add(menuItemModifierGroup);
	}

	/**
	 * Return the value associated with the column: shifts
	 */
	public java.util.List<com.floreantpos.model.MenuItemShift> getShifts () {
		return shifts;
	}

	/**
	 * Set the value related to the column: shifts
	 * @param shifts the shifts value
	 */
	public void setShifts (java.util.List<com.floreantpos.model.MenuItemShift> shifts) {
		this.shifts = shifts;
	}

	public void addToshifts (com.floreantpos.model.MenuItemShift menuItemShift) {
		if (null == getShifts()) setShifts(new java.util.ArrayList<com.floreantpos.model.MenuItemShift>());
		getShifts().add(menuItemShift);
	}

	public java.lang.Integer getRed() {
		return red;
	}

	public void setRed(java.lang.Integer red) {
		this.red = red;
	}

	public java.lang.Integer getGreen() {
		return green;
	}

	public void setGreen(java.lang.Integer green) {
		this.green = green;
	}

	public java.lang.Integer getBlue() {
		return blue;
	}

	public void setBlue(java.lang.Integer blue) {
		this.blue = blue;
	}

	@Override
	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.MenuItem)) return false;
		else {
			com.floreantpos.model.MenuItem menuItem = (com.floreantpos.model.MenuItem) obj;
			if (null == this.getId() || null == menuItem.getId()) return false;
			else return (this.getId().equals(menuItem.getId()));
		}
	}

	@Override
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

	@Override
	public int compareTo (Object obj) {
		if (obj.hashCode() > hashCode()) return 1;
		else if (obj.hashCode() < hashCode()) return -1;
		else return 0;
	}

	@Override
	public String toString () {
		return "BaseMenuItem [hashCode=" + hashCode + ", id=" + id + ", modifiedTime=" + modifiedTime + ", barcode="
				+ barcode + ", buyPrice=" + buyPrice + ", liferantBuyPrice=" + liferantBuyPrice + ", discountRate=" + discountRate + ", image="
				+ Arrays.toString(image) + ", name=" + name + ", lieferantName=" + lieferantName + ", description=" + description+ ", description2=" + description2 + ", note=" + note
				+ ", itemId=" + itemId + ", subitemid=" + subitemid + ", type=" + type + ", price=" + price
				+ ", red=" + red + ", green=" + green + ", blue=" + blue
				+ ", showImageOnly=" + showImageOnly + ", weightgrams=" + weightgrams + ", instock=" + instock 
				+ ", pfand=" + pfand + ", visible="	+ visible + ", changeprice=" + changeprice + ", parent=" + parent 
				+ ", recepie="+ recepie + ", tax=" + tax + ", virtualPrinter=" + virtualPrinter + ", menuItemModiferGroups="
				+ menuItemModiferGroups + ", shifts=" + shifts + ", sold=" + sold + ", damaged=" + damaged + ", priceCategory=" + priceCategory 
				+ ", damaged=" + damaged + ", barcode1=" + barcode1 + ", barcode2=" + barcode2 + ", fred=" + fred + ", fgreen=" + fgreen + ", fblue=" + fblue + ""
				+ ", fblue=" + fblue + ", menuitemprice=" + menuitemprice + "]";
	}


}