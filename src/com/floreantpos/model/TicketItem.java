package com.floreantpos.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.model.base.BaseTicketItem;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.util.NumberUtil;

public class TicketItem extends BaseTicketItem implements ITicketItem {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public TicketItem () {
		super();
	}

	public static class DateComparator implements Comparator {
		public DateComparator() {
		}

		@Override
		public int compare(Object o1, Object o2) {
			if (!(o1 instanceof TicketItem) || !(o2 instanceof TicketItem))
				throw new ClassCastException();

			TicketItem e1 = (TicketItem) o1;
			TicketItem e2 = (TicketItem) o2;

			if (e1.getTicket() == null || e1.getTicket().getCreateDate() == null || e2.getTicket().getCreateDate() == null)
				return 0;
			return e1.getTicket().getCreateDate().compareTo(e2.getTicket().getCreateDate());
		}
	}


	/**
	 * Constructor for primary key
	 */
	public TicketItem (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public TicketItem (
			java.lang.Integer id,
			com.floreantpos.model.Ticket ticket) {
		super (
				id,
				ticket);
	}

	public static TicketItem newZeroTicketItem() {
		TicketItem ticketItem = new TicketItem();
		ticketItem.setItemCount(1);
		ticketItem.setUnitPrice(0.00);
		Restaurant restaurant = RestaurantDAO.getRestaurant();
		if(restaurant.getItemsonstigestext() != null)
			ticketItem.setName(restaurant.getItemsonstigestext());
		else
			ticketItem.setName("Sonstiges");
		ticketItem.setCategoryName(com.floreantpos.POSConstants.MISC);
		ticketItem.setGroupName(com.floreantpos.POSConstants.MISC);
		ticketItem.setShouldPrintToKitchen(true);
		ticketItem.setPrintorder(1);
		ticketItem.setBeverage(false);
		ticketItem.setItemId(997);
		Double tax = Application.getInstance().dineInTax;
		Double subTotal = 0.00;
		Double taxAmount = 0.00; 

		ticketItem.setSubtotalAmount(subTotal);
		Double totalAmount = 0.00;

		ticketItem.setTotalAmount(totalAmount);
		ticketItem.setTotalAmountWithoutModifiers(totalAmount);
		ticketItem.setTaxAmount(taxAmount);
		ticketItem.setTaxRate(tax);

		return ticketItem;
	}
	/*[CONSTRUCTOR MARKER END]*/

	private boolean priceIncludesTax;

	private int tableRowNum;

	public int getTableRowNum() {
		return tableRowNum;
	}

	public void setTableRowNum(int tableRowNum) {
		this.tableRowNum = tableRowNum;
	}

	@Override
	public boolean canAddCookingInstruction() {
		if (isPrintedToKitchen())
			return false;

		return true;
	}

	public static class ItemComparator implements Comparator {
		public ItemComparator(){}
		@Override
		public int compare(Object o1, Object o2) {
			if (!(o1 instanceof TicketItem) || !(o2 instanceof TicketItem))
				throw new ClassCastException();

			TicketItem e1 = (TicketItem) o1;
			TicketItem e2 = (TicketItem) o2;

			return e1.getItemId() - (e2.getItemId());
		}
	}

	@Override
	public java.lang.Double getTaxAmount() {
		if (getTicket() == null)
			return 0.0;
		if(getTicket().isTaxExempt()) {
			return 0.0;
		}

		return super.getTaxAmount();
	}

	@Override
	public String toString() {
		return getName();
	}

	public void addCookingInstruction(TicketItemCookingInstruction cookingInstruction) {
		List<TicketItemCookingInstruction> cookingInstructions = getCookingInstructions();

		if (cookingInstructions == null) {
			cookingInstructions = new ArrayList<TicketItemCookingInstruction>(2);
			setCookingInstructions(cookingInstructions);
		}

		cookingInstructions.add(cookingInstruction);
	}

	public void addCookingInstructions(List<TicketItemCookingInstruction> instructions) {
		List<TicketItemCookingInstruction> cookingInstructions = getCookingInstructions();

		if (cookingInstructions == null) {
			cookingInstructions = new ArrayList<TicketItemCookingInstruction>(2);
			setCookingInstructions(cookingInstructions);
		}

		cookingInstructions.addAll(instructions);
	}

	public void removeCookingInstruction(TicketItemCookingInstruction itemCookingInstruction) {
		List<TicketItemCookingInstruction> cookingInstructions2 = getCookingInstructions();
		if (cookingInstructions2 == null) {
			return;
		}

		for (Iterator iterator = cookingInstructions2.iterator(); iterator.hasNext();) {
			TicketItemCookingInstruction ticketItemCookingInstruction = (TicketItemCookingInstruction) iterator.next();
			if (ticketItemCookingInstruction.getTableRowNum() == itemCookingInstruction.getTableRowNum()) {
				iterator.remove();
				return;
			}
		}
	}

	public TicketItemModifierGroup findTicketItemModifierGroup(MenuModifier menuModifier, boolean createNew) {
		MenuItemModifierGroup menuItemModifierGroup = menuModifier.getMenuItemModifierGroup();

		List<TicketItemModifierGroup> ticketItemModifierGroups = getTicketItemModifierGroups();

		if (ticketItemModifierGroups != null) {
			for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
				if (ticketItemModifierGroup.getModifierGroupId().equals(menuItemModifierGroup.getId())) {
					return ticketItemModifierGroup;
				}
			}
		}

		TicketItemModifierGroup ticketItemModifierGroup = new TicketItemModifierGroup();
		ticketItemModifierGroup.setModifierGroupId(menuItemModifierGroup.getId());
		ticketItemModifierGroup.setMinQuantity(menuItemModifierGroup.getMinQuantity());
		ticketItemModifierGroup.setMaxQuantity(menuItemModifierGroup.getMaxQuantity());
		ticketItemModifierGroup.setParent(this);
		addToticketItemModifierGroups(ticketItemModifierGroup);
		return ticketItemModifierGroup;
	}

	public void changeTicketPrice(Double value)
	{
		//		Double taxablePrice = this.getUnitPrice()*((value)/100);
		//		this.setDiscountAmount(taxablePrice);
		this.setDiscountRate(value);
		//		this.setUnitPrice(this.getUnitPrice() - taxablePrice);
	}


	public void changeDeleteCouponPrice(Double value)
	{		
		//		Double price = this.getUnitPrice()/(1- (value/100));
		Double price = this.getUnitPrice();

		if(this.getDiscountRate()>0) {
			setDiscountAmount(0.0);
			setDiscountRate(0.0);
			this.setUnitPrice(price);	
		}			
	}
	public void calculatePrice() {
		priceIncludesTax = Application.getInstance().isPriceIncludesTax();

		/*try
		{
			List<TicketItemModifierGroup> ticketItemModifierGroups = getTicketItemModifierGroups();
			 
			if ((ticketItemModifierGroups != null&&!ticketItemModifierGroups.isEmpty())&& ticketItemModifierGroups.size()>0 ) {
				try
				{
				for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
					ticketItemModifierGroup.calculatePrice();
				}
				} catch (Exception e) {}
			}
			
		}
		catch(Exception ex)
		{
			
		}*/

		setSubtotalAmount(NumberUtil.roundToTwoDigit(calculateSubtotal(true)));
		setSubtotalAmountWithoutModifiers(NumberUtil.roundToTwoDigit(calculateSubtotal(false)));
		setDiscountAmount(NumberUtil.roundToTwoDigit(calculateDiscount()));
		setTaxAmount(NumberUtil.roundToTwoDigit(calculateTax(true)));
		setTaxAmountWithoutModifiers(NumberUtil.roundToTwoDigit(calculateTax(false)));
		setTotalAmount(NumberUtil.roundToTwoDigit(calculateTotal(true)));
		setTotalAmountWithoutModifiers(NumberUtil.roundToTwoDigit(calculateTotal(false)));
	}

	//	public double calculateSubtotal() {
	//		double subtotal = NumberUtil.roundToTwoDigit(calculateSubtotal(true));
	//		
	//		return subtotal;
	//	}
	//	
	//	public double calculateSubtotalWithoutModifiers() {
	//		double subtotalWithoutModifiers = NumberUtil.roundToTwoDigit(calculateSubtotal(false));
	//		
	//		return subtotalWithoutModifiers;
	//	}

	private double calculateSubtotal(boolean includeModifierPrice) {
		double subTotalAmount = NumberUtil.roundToTwoDigit(getUnitPrice() * getItemCount());

		try {
		if (includeModifierPrice) {
			List<TicketItemModifierGroup> ticketItemModifierGroups = getTicketItemModifierGroups();
			
			if (ticketItemModifierGroups != null) {
				for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
					subTotalAmount += ticketItemModifierGroup.getSubtotal();
				}
			 }
		  }
		} catch (Exception e) {
			
		}
		return subTotalAmount;
	}

	private double calculateDiscount() {
		double subtotalWithoutModifiers = getSubtotalAmountWithoutModifiers();
		double discountRate = getDiscountRate();

		double discount = 0;
		if (discountRate > 0) {
			discount = subtotalWithoutModifiers * discountRate / 100.0;
		}

		return discount;
	}

	private double calculateTax(boolean includeModifierTax) {
		double subtotal = 0;

		subtotal = getSubtotalAmountWithoutModifiers();
		double discount = getDiscountAmount();

		subtotal = subtotal - discount;
		double taxRate = getTaxRate();
		double tax = 0;

		if (taxRate > 0) {
			if (priceIncludesTax) {
				tax = subtotal - (subtotal / (1 + (taxRate / 100.0)));		
			}
			else {
				tax = subtotal * (taxRate / 100.0);
			}
		}

		/*if (includeModifierTax) {
			List<TicketItemModifierGroup> ticketItemModifierGroups = getTicketItemModifierGroups();
			if (ticketItemModifierGroups != null) {
				for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
					tax += ticketItemModifierGroup.getTax();
				}
			}
		}*/
		return tax;
	}

	private double calculateTotal(boolean includeModifiers) {
		double total = 0;

		if (includeModifiers) {
			if (priceIncludesTax) {
				total = getSubtotalAmount() - getDiscountAmount();
			}
			else {
				total = getSubtotalAmount() - getDiscountAmount() + getTaxAmount();
			}
		}
		else {
			if (priceIncludesTax) {
				total = getSubtotalAmountWithoutModifiers() - getDiscountAmount();
			}
			else {
				total = getSubtotalAmountWithoutModifiers() - getDiscountAmount() + getTaxAmountWithoutModifiers();
			}
		}

		return total;
	}

	@Override
	public String getNameDisplay() {
		return getName();
	}

	@Override
	public Double getDisAmt(){
		return getDiscountAmt();
		
	} 
	
	@Override
	public boolean getTaxExist() {
	    return getExistDiscount();
	} 
	
	@Override
	public Double getUnitPriceDisplay() {
		return getUnitPrice();
	}

	@Override
	public Integer getItemCountDisplay() {
		return getItemCount();
	}

	@Override
	public Double getTaxAmountWithoutModifiersDisplay() {
		return getTaxAmountWithoutModifiers();
	}

	@Override
	public Double getTotalAmountWithoutModifiersDisplay() {
		return getTotalAmountWithoutModifiers();
	}

	public boolean isPriceIncludesTax() {
		return priceIncludesTax;
	}

	public void setPriceIncludesTax(boolean priceIncludesTax) {
		this.priceIncludesTax = priceIncludesTax;
	}

	@Override
	public String getItemCode() {
		return String.valueOf(getItemId());
	}

	@Override
	public String getBarCode() {
		return String.valueOf(getBarcode());
	}

	public Printer getPrinter() {
		PosPrinters printers = Application.getPrinters();
		VirtualPrinter virtualPrinter = getVirtualPrinter();

		if(virtualPrinter == null) {
			return printers.getDefaultKitchenPrinter();
		}

		return printers.getKitchenPrinterFor(virtualPrinter);
	}

	public String getBarPrinter()
	{
		PosPrinters printers = Application.getPrinters();

		return printers.getBarPrinter();
	}
}