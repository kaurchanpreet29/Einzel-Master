package com.floreantpos.model.base;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlTransient;

/**
 * This is an object that contains data related to the MENU_CATEGORY table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="MENU_CATEGORY"
 */

public abstract class BaseMenuCategory  implements Comparable, Serializable {

	public static String REF = "MenuCategory";
	public static String PROP_VISIBLE = "visible";
	public static String PROP_NAME = "name";
	public static String PROP_ID = "id";
	public static String PROP_BEVERAGE = "beverage";
	public static String PROP_TYPE = "type";
	public static String PROP_SHOP = "shop";
	public static String PROP_PRIORITY = "priority";
	public static String PROP_CATEGORY_ID = "categoryid";
	public static String PROP_PRICE_CATEGORY = "priceCategory";
	// constructors
	public BaseMenuCategory () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseMenuCategory (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseMenuCategory (
		java.lang.Integer id,
		java.lang.String name) {

		this.setId(id);
		this.setName(name);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	 java.util.Date modifiedTime;

	// fields
	private java.lang.String name;
	private java.lang.Boolean visible;
	private java.lang.Boolean beverage;
	private java.lang.Boolean priority;
	private java.lang.String type;
	private java.lang.String shop;
	private java.lang.Integer categoryid;
	private java.lang.Integer bon;
	private java.lang.Boolean pfand;
	protected java.lang.Integer priceCategory;

	// collections
	private java.util.Set<com.floreantpos.model.MenuGroup> menuGroups;


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

	public java.lang.Integer getBon() {
		return bon;
	}

	public void setBon(java.lang.Integer bon) {
		this.bon = bon;
	}
	
	public java.lang.Boolean getPfand() {
		return pfand!=null?pfand:false;
	}

	public void setPfand(java.lang.Boolean pfand) {
		this.pfand = pfand;
	}
	public java.lang.Integer getPriceCategory() {
		return priceCategory!=null?priceCategory:1;
	}

	public void setPriceCategory(java.lang.Integer priceCategory) {
		this.priceCategory = priceCategory;
	}

	
	/**
	 * Return the value associated with the column: MODIFIED_TIME
	 */
	@XmlTransient
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
	 * Return the value associated with the column: TYPE
	 */
	public java.lang.String getType () {
			return type;
	}

	/**
	 * Set the value related to the column: TYPE
	 * @param type the TYPE value
	 */
	public void setType (java.lang.String type) {
		this.type = type;
	}

	public java.lang.Integer getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(java.lang.Integer categoryid) {
		this.categoryid = categoryid;
	}
	/**
	 * Return the value associated with the column: PRIORITY
	 */
	public java.lang.Boolean getPriority () {
			return priority;
	}

	/**
	 * Set the value related to the column: PRIORITY
	 * @param type the PRIORITY value
	 */
	public void setPriority (java.lang.Boolean priority) {
		this.priority = priority;
	}

	
	/**
	 * Return the value associated with the column: SHOP
	 */
	public java.lang.String getShop () {
			return shop;
	}

	/**
	 * Set the value related to the column: SHOP
	 * @param type the SHOP value
	 */
	public void setShop (java.lang.String shop) {
		this.shop = shop;
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
	 * Return the value associated with the column: VISIBLE
	 */
	public java.lang.Boolean isVisible () {
					return visible == null ? Boolean.FALSE : visible;
			}

	/**
	 * Set the value related to the column: VISIBLE
	 * @param visible the VISIBLE value
	 */
	public void setVisible (java.lang.Boolean visible) {
		this.visible = visible;
	}



	/**
	 * Return the value associated with the column: BEVERAGE
	 */
	public java.lang.Boolean isBeverage () {
					return beverage == null ? Boolean.FALSE : beverage;
			}

	/**
	 * Set the value related to the column: BEVERAGE
	 * @param beverage the BEVERAGE value
	 */
	public void setBeverage (java.lang.Boolean beverage) {
		this.beverage = beverage;
	}



	/**
	 * Return the value associated with the column: menuGroups
	 */
	@XmlTransient
	public java.util.Set<com.floreantpos.model.MenuGroup> getMenuGroups () {
			return menuGroups;
	}

	/**
	 * Set the value related to the column: menuGroups
	 * @param menuGroups the menuGroups value
	 */
	public void setMenuGroups (java.util.Set<com.floreantpos.model.MenuGroup> menuGroups) {
		this.menuGroups = menuGroups;
	}

	public void addTomenuGroups (com.floreantpos.model.MenuGroup menuGroup) {
		if (null == getMenuGroups()) setMenuGroups(new java.util.TreeSet<com.floreantpos.model.MenuGroup>());
		getMenuGroups().add(menuGroup);
	}





	@Override
	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.MenuCategory)) return false;
		else {
			com.floreantpos.model.MenuCategory menuCategory = (com.floreantpos.model.MenuCategory) obj;
			if (null == this.getId() || null == menuCategory.getId()) return false;
			else return (this.getId().equals(menuCategory.getId()));
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
		return super.toString();
	}


}