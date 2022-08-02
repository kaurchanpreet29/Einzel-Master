package com.khana.tse.fiskaly;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class AmountsPerPaymentType {
	
	@SerializedName("payment_type")
	private String payment_type;
	
	@JsonProperty("amount")
	private String amount;

	

	public String getPayment_type() {
		return payment_type;
	}



	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}



	public String getAmount() {
		return amount;
	}



	public void setAmount(String amount) {
		this.amount = amount;
	}



	public AmountsPerPaymentType(String paymentType, String amount) {
		super();
		this.payment_type = paymentType;
		this.amount = amount;
	}
	
	

}
