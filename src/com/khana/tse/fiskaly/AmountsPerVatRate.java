package com.khana.tse.fiskaly;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

@JsonInclude
public class AmountsPerVatRate {
	
	@SerializedName("vat_rate")
	private String vat_rate;
	
	@JsonProperty("amount")
	private String amount;

	

	public String getVat_rate() {
		return vat_rate;
	}



	public void setVat_rate(String vat_rate) {
		this.vat_rate = vat_rate;
	}



	public String getAmount() {
		return amount;
	}



	public void setAmount(String amount) {
		this.amount = amount;
	}



	public AmountsPerVatRate(String vatRate, String amount) {
		super();
		this.vat_rate = vatRate;
		this.amount = amount;
	}
	
	

}
