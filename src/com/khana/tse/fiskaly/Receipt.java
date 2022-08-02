package com.khana.tse.fiskaly;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Receipt {
	
	@SerializedName("receipt_type")
	private String receiptType;
	
	@SerializedName("amounts_per_vat_rate")
	private List<AmountsPerVatRate> amountsPerVatRate;
	
	@SerializedName("amounts_per_payment_type")
	private List<AmountsPerPaymentType> amountsPerPaymentType;

	public String getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(String receiptType) {
		this.receiptType = receiptType;
	}

	public List<AmountsPerVatRate> getAmountsPerVatRate() {
		return amountsPerVatRate;
	}

	public void setAmountsPerVatRate(List<AmountsPerVatRate> amountsPerVatRate) {
		this.amountsPerVatRate = amountsPerVatRate;
	}

	public List<AmountsPerPaymentType> getAmountsPerPaymentType() {
		return amountsPerPaymentType;
	}

	public void setAmountsPerPaymentType(List<AmountsPerPaymentType> amountsPerPaymentType) {
		this.amountsPerPaymentType = amountsPerPaymentType;
	}	

}
