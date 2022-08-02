package com.floreantpos.model.base;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlTransient;


/**
 * This is an object that contains data related to the MENU_GROUP table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="MENU_GROUP"
 */

public abstract class BaseMenuGroup  implements Comparable, Serializable {

	public static String REF = "MenuGroup";
	public static String PROP_NAME = "name";
	public static String PROP_PARENT = "parent";
	public static String PROP_VISIBLE = "visible";
	public static String PROP_ID = "id";
	public static String PROP_LINK = "link";
	public static String PROP_GROUPID = "groupid";
	// constructors
	public BaseMenuGroup () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseMenuGroup (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseMenuGroup (
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

	private java.util.Date modifiedTime;

	// fields
	private java.lang.String name;
	private java.lang.Boolean visible;
	private java.lang.String type;
	private java.lang.String link;
	private java.lang.String description;
	private java.lang.String note;
	private java.lang.String secname;
	private java.lang.Integer groupid;
	private java.lang.Double price;
	private java.lang.Double discount;
	private java.lang.Integer gaeng;
	protected byte[] image;
	// many to one
	private com.floreantpos.model.MenuCategory parent;



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
	 * Return the value associated with the column: GAENG
	 */
	public java.lang.Integer getGaeng () {
			return gaeng;
	}

	/**
	 * Set the value related to the column: GAENG
	 * @param name the GAENG value
	 */
	public void setGaeng (java.lang.Integer gaeng) {
		this.gaeng = gaeng;
	}

	/**
	 * Return the value associated with the column: PRICE
	 */
	public java.lang.Double getPrice () {
			return price;
	}

	/**
	 * Set the value related to the column: PRICE
	 * @param name the PRICE value
	 */
	public void setPrice (java.lang.Double price) {
		this.price = price;
	}
	
	/**
	 * Return the value associated with the column: GROUPID
	 */
	public java.lang.Integer getGroupid () {
			return groupid;
	}

	/**
	 * Set the value related to the column: GROUPID
	 * @param name the GROUPID value
	 */
	public void setGroupid (java.lang.Integer groupid) {
		this.groupid = groupid;
	}

	/**
	 * Return the value associated with the column: LINK
	 */
	public java.lang.String getLink () {
			return link;
	}

	/**
	 * Set the value related to the column: LINK
	 * @param name the LINK value
	 */
	public void setLink (java.lang.String link) {
		this.link = link;
	}
	
	/**
	 * Return the value associated with the column: DESCRIPTION
	 */
	public java.lang.String getDescription () {
			return description;
	}

	/**
	 * Set the value related to the column: DESCRIPTION
	 * @param name the DESCRIPTION value
	 */
	public void setDescription (java.lang.String description) {
		this.description = description;
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
	 * Return the value associated with the column: SECNAME
	 */
	public java.lang.String getSecname () {
			return secname;
	}

	/**
	 * Set the value related to the column: SECNAME
	 * @param name the SECNAME value
	 */
	public void setSecname(java.lang.String secname) {
		this.secname = secname;
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
	 * Return the value associated with the column: CATEGORY_ID
	 */
	public com.floreantpos.model.MenuCategory getParent () {
			return parent;
	}

	/**
	 * Set the value related to the column: CATEGORY_ID
	 * @param parent the CATEGORY_ID value
	 */
	public void setParent (com.floreantpos.model.MenuCategory parent) {
		this.parent = parent;
	}


	public java.lang.Double getDiscount() {
    return discount;
  }

  public void setDiscount(java.lang.Double discount) {
    this.discount = discount;
  }

  public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.MenuGroup)) return false;
		else {
			com.floreantpos.model.MenuGroup menuGroup = (com.floreantpos.model.MenuGroup) obj;
			if (null == this.getId() || null == menuGroup.getId()) return false;
			else return (this.getId().equals(menuGroup.getId()));
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