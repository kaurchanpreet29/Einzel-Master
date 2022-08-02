package com.khana.tse.fiskaly;

import com.google.gson.annotations.SerializedName;


public class LineItem {
	
	private String quantity;
	
	private String text;
	
	@SerializedName("price_per_unit")
	private String price_per_unit;

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	

	public String getPrice_per_unit() {
		return price_per_unit;
	}

	public void setPrice_per_unit(String price_per_unit) {
		this.price_per_unit = price_per_unit;
	}

	public LineItem(String quantity, String text, String pricePerUnit) {
		super();
		this.quantity = quantity;
		this.text = text;
		this.price_per_unit = pricePerUnit;
	}

	public LineItem() {
		super();
	}
	
	

}
