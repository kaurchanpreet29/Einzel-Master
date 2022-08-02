package com.floreantpos.model;

import java.util.Comparator;

import javax.xml.bind.annotation.XmlRootElement;

import com.floreantpos.model.base.BaseMenuCategory;

@XmlRootElement(name="menu-category")
public class MenuCategory extends BaseMenuCategory {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public MenuCategory () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public MenuCategory (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public MenuCategory (
		java.lang.Integer id,
		java.lang.String name) {

		super (
			id,
			name);
	}
	public static class ItemComparator implements Comparator {
		public ItemComparator(){}
	    public int compare(Object o1, Object o2) {
	      if (!(o1 instanceof MenuCategory) || !(o2 instanceof MenuCategory))
	        throw new ClassCastException();

	      MenuCategory e1 = (MenuCategory) o1;
	      MenuCategory e2 = (MenuCategory) o2;

	      return (int) (e1.getPriceCategory() - (e2.getPriceCategory()));
       }
	}

	/*[CONSTRUCTOR MARKER END]*/
	
	@Override
	public String toString() {
		return getName();
	}

	public String getUniqueId() {
		return ("menu_category_" + getName() + "_" + getId()).replaceAll("\\s+", "_");
	}

	
}