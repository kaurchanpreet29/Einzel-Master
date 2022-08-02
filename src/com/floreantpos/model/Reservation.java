package com.floreantpos.model;

import com.floreantpos.model.base.BaseReservation;

public class Reservation extends BaseReservation{
	/*[CONSTRUCTOR MARKER BEGIN]*/
	
	public static String PROP_ID = "id";
	public Reservation () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Reservation (java.lang.Integer autoId) {
		super(autoId);
	}
	/**
	 * Constructor for required fields
	 */
	public Reservation (
			java.lang.Integer autoId,
			java.lang.String name,
			java.lang.String date,
			java.lang.String email,
			java.lang.String message,
			java.lang.Integer person,
			java.lang.String telefon,
			java.lang.String time
		) {
		super (
				autoId,
				name,
				date,
				email,
				message,
				person,
				telefon,
				time);
	}	
}
