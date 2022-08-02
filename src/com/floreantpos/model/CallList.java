package com.floreantpos.model;

import com.floreantpos.model.base.BaseCallList;

public class CallList extends BaseCallList {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public CallList () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CallList (java.lang.Integer autoId) {
		super(autoId);
	}
	/**
	 * Constructor for required fields
	 */
	public CallList (
		java.lang.Integer id,
		java.lang.String phone,
		java.lang.String time
		) {
		super (
			id,
			phone,
			time);
	}	
	
}