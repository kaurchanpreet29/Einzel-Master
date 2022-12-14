package com.floreantpos.model;

import com.floreantpos.config.CardConfig;

public enum PaymentType {
	CASH("CASH"), DEBIT_VISA("Visa", "visa_card.png"), DEBIT_MASTER_CARD("MasterCard", "master_card.png"), 
	CREDIT_VISA("Visa", "visa_card.png"), CREDIT_MASTER_CARD("MasterCard", "master_card.png"), 
	CREDIT_AMEX("Amex", "am_ex_card.png"), CREDIT_DISCOVERY("Discover", "discover_card.png"), 
	GIFT_CERTIFICATE("GIFT CERTIFICATE"), CARD ("CARD"), ONLINE("ONLINE"), BOTH("BOTH"),RECHNUNG("RECHNUNG"), DEBITOR("DEBITOR"),GUTSCHEIN("GUTSCHEIN"), DEBITOR_KARTE("DEBITOR_KARTE"),ONLINE_BAR("ONLINE_BAR"), PAYPAL("PAYPAL");

	
	private String displayString;
	private String imageFile;

	private PaymentType(String display) {
		this.displayString = display;
	}

	private PaymentType(String display, String image) {
		this.displayString = display;
		this.imageFile = image;
	}

	@Override
	public String toString() {
		return displayString;
	}

	public String getDisplayString() {
		return displayString;
	}

	public void setDisplayString(String displayString) {
		this.displayString = displayString;
	}

	public String getImageFile() {
		return imageFile;
	}

	public void setImageFile(String imageFile) {
		this.imageFile = imageFile;
	};
	
	public boolean isSupported() {
		switch (this) {
		case CASH:
			return true;
		case CARD:
			return true;
		case RECHNUNG:
			return true;
		default:
			return CardConfig.isSwipeCardSupported() || CardConfig.isManualEntrySupported() || CardConfig.isExtTerminalSupported();
		}
	}
	
	public PosTransaction createTransaction() {
		PosTransaction transaction = null;
		switch (this) {
			case CREDIT_VISA:
			case CREDIT_AMEX:
			case CREDIT_DISCOVERY:
			case CREDIT_MASTER_CARD:
				transaction = new CreditCardTransaction();
				transaction.setAuthorizable(true);
				break;
				
			case DEBIT_MASTER_CARD:
			case DEBIT_VISA:
				transaction = new DebitCardTransaction();
				transaction.setAuthorizable(true);
				break;
				
			case GIFT_CERTIFICATE:
				transaction = new GiftCertificateTransaction();
				break;
				
			default:
				transaction = new CashTransaction();
				break;
		}
		
		transaction.setPaymentType(name());
		return transaction;
	}
}
