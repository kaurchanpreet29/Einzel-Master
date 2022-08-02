package com.floreantpos.model;

public interface ITicketItem {
	String getItemCode();
	String getBarCode();
	boolean canAddCookingInstruction();
	Boolean isPrintedToKitchen();
	
	String getNameDisplay();
    boolean getTaxExist();
    Double getDisAmt();
    
	Double getUnitPriceDisplay();

	Integer getItemCountDisplay();

	Double getTaxAmountWithoutModifiersDisplay();

	Double getTotalAmountWithoutModifiersDisplay();
	Double getDiscountAmount();
	
	Integer getPrintorder();
	
	String getUnitType();
	Double getTaxRate();
}
