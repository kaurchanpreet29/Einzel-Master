package com.floreantpos.model;

import com.floreantpos.model.base.BaseDeliveryCost;

public class Gebiet extends BaseDeliveryCost{
	
	/*[CONSTRUCTOR MARKER BEGIN]*/
	public Gebiet () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Gebiet (java.lang.Integer autoId) {
		super(autoId);
	}
	/**
	 * Constructor for required fields
	 */
	public Gebiet (
			java.lang.Integer autoId,
			java.lang.String bezirk,
			java.lang.Double lieferkosten,
			java.lang.Integer lieferzeit,
			java.lang.Double mindest,
			java.lang.Boolean isopen,
			java.lang.String ort,
			java.lang.Integer plz
		) {
		super (
				autoId,
				bezirk,
				lieferkosten,
				lieferzeit,
				mindest,
				isopen,
				ort,
				plz);
	}		
}
