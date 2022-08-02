package com.floreantpos.model;
import com.floreantpos.model.base.BaseStreet;

public class Street extends BaseStreet {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public Street () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Street (java.lang.Integer autoId) {
		super(autoId);
	}
	/**
	 * Constructor for required fields
	 */
	public Street (
			java.lang.Integer autoId,
			java.lang.String name,
			java.lang.String plz,
			java.lang.String bezirk,
			java.lang.String ort
		) {
		super (
			autoId,
			name,
			plz,
			bezirk,
			ort);
	}	
}