package com.floreantpos.model;

import javax.xml.bind.annotation.XmlRootElement;

import com.floreantpos.model.base.BaseGutschein;

@XmlRootElement(name="gutschein")
public class Gutschein extends BaseGutschein {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public Gutschein () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Gutschein (java.lang.Integer id) {
		super(id);
	}

	/*[CONSTRUCTOR MARKER END]*/
	
	@Override
	public String toString() {
		return getBarcode();
	}
}