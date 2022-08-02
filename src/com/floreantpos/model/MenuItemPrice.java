package com.floreantpos.model;

import com.floreantpos.model.base.BaseMenuItemPrice;

public class MenuItemPrice extends BaseMenuItemPrice{
	/*[CONSTRUCTOR MARKER BEGIN]*/
	public MenuItemPrice () {
		super();		 
	}

	/**
	 * Constructor for primary key
	 */
	public MenuItemPrice (java.lang.Integer id) {
		super(id);
	}
	/**
	 * Constructor for required fields
	 */
	public MenuItemPrice (
			java.lang.Integer id,
			java.lang.String name,
			java.lang.Double price
		) {
		
		super (
				id,
				name,
				price
			);		
	  }
}

	