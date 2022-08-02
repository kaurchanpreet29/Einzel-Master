package com.floreantpos.model;

import com.floreantpos.model.base.BaseTicketItemCookingInstruction;

public class TicketItemCookingInstruction extends BaseTicketItemCookingInstruction implements ITicketItem {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public TicketItemCookingInstruction () {
		super();
	}

/*[CONSTRUCTOR MARKER END]*/

	private int tableRowNum;

	public int getTableRowNum() {
		return tableRowNum;
	}

	public void setTableRowNum(int tableRowNum) {
		this.tableRowNum = tableRowNum;
	}

	@Override
	public boolean canAddCookingInstruction() {
		return false;
	}
	
	@Override
	public String toString() {
		return getDescription();
	}
	
	@Override
	public String getNameDisplay() {
		return "   * " + getDescription();
	}
	
	@Override
	public boolean getTaxExist() {
	    return false;
	}

	@Override
	public Double getDisAmt(){
		return null;
	}

	@Override
	public Double getUnitPriceDisplay() {
		return null;
	}

	@Override
	public Integer getPrintorder()
	{
		return 1;
	}
	@Override
	public Integer getItemCountDisplay() {
		return null;
	}

	@Override
	public Double getTaxAmountWithoutModifiersDisplay() {
		return null;
	}

	@Override
	public Double getTotalAmountWithoutModifiersDisplay() {
		return null;
	}
	
	@Override
	public String getItemCode() {
		return "";
	}

	
	@Override
	public String getBarCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUnitType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getTaxRate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getDiscountAmount() {
		// TODO Auto-generated method stub
		return null;
	}

	
}