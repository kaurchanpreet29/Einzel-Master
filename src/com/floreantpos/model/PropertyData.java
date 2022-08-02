package com.floreantpos.model;

import com.floreantpos.model.base.BasePropertyData;

public class PropertyData extends BasePropertyData {
	/*[CONSTRUCTOR MARKER BEGIN]*/
	public PropertyData () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public PropertyData (java.lang.Integer id) {
		super(id);
	}
	@Override
	public String toString() {
		return this.getPropertytext();
	}
}
