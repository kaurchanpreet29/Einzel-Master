package com.floreantpos.model;

import com.floreantpos.model.base.BaseTseDataTable;



public class TseData extends BaseTseDataTable {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public TseData () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public TseData (java.lang.Integer id) {
		super(id);
	}


	
/*[CONSTRUCTOR MARKER END]*/

/*	public TseData(String number, Integer x, Integer y) {
		super();
		
		setNumber(number);
		setX(x);
		setY(y);
	}*/
	
	@Override
	public String toString() {
		return getSerialNumber();
	}
}