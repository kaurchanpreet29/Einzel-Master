package com.floreantpos.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.base.BaseMenuItem;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.ui.views.order.OrderView;
import com.floreantpos.ui.views.order.TicketView;

@XmlRootElement(name="menu-item")
public class MenuItem extends BaseMenuItem  implements Comparable{
	private static final long serialVersionUID = 1L;
	public static class ItemComparator implements Comparator {
		public ItemComparator(){}
		@Override
		public int compare(Object o1, Object o2) {
			if (!(o1 instanceof MenuItem) || !(o2 instanceof MenuItem))
				throw new ClassCastException();

			MenuItem e1 = (MenuItem) o1;
			MenuItem e2 = (MenuItem) o2;
			try {
			return Integer.parseInt(e1.getItemId()) - Integer.parseInt(e2.getItemId());
			}catch (IllegalArgumentException e) {
				// TODO: handle exception
			}
			return 0;
		}
	}
	
	public static class InstockComparator implements Comparator {
		public InstockComparator(){}
		@Override
		public int compare(Object o1, Object o2) {
			if (!(o1 instanceof MenuItem) || !(o2 instanceof MenuItem))
				throw new ClassCastException();

			MenuItem e1 = (MenuItem) o1;
			MenuItem e2 = (MenuItem) o2;

			if (e1.getParent().getName() == e2.getParent().getName()) {
				return 0;
			}
			if (e1.getParent().getName() == null) {
				return -1;
			}
			if (e2.getParent().getName() == null) {
				return 1;
			}
			return e1.getParent().getName().compareTo(e2.getParent().getName());
		}
	}
	/*[CONSTRUCTOR MARKER BEGIN]*/
	public MenuItem () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public MenuItem (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public MenuItem (
			java.lang.Integer id,
			java.lang.String itemId,
			java.lang.String name,
			java.lang.Double buyPrice,
			java.lang.Double price) {

		super (
				id,
				itemId,
				name,
				buyPrice,
				price);
	}

	/*[CONSTRUCTOR MARKER END]*/

	@Override
	@XmlTransient
	public Date getModifiedTime() {
		return super.getModifiedTime();
	}

	@Override
	public List<MenuItemPrice> getMenuitemprice() {
		List<MenuItemPrice> menuItemprice = super.getMenuitemprice();

		if (menuItemprice == null) {
			menuItemprice = new ArrayList<MenuItemPrice>();
			super.setMenuitemprice(menuItemprice);
		}
		return menuItemprice;
	}
	
	public double getPrice(Shift currentShift) {
		List<MenuItemShift> shifts = getShifts();
		double price = super.getPrice();

		if(currentShift == null) {
			return price;
		}
		if(shifts == null || shifts.size() == 0) {
			return price;
		}

		//		Date formattedTicketTime = ShiftUtil.formatShiftTime(ticketCreateTime);
		//		Calendar calendar = Calendar.getInstance();
		//		calendar.setTime(formattedTicketTime);
		//		formattedTicketTime = calendar.getTime();
		//		
		for (MenuItemShift shift : shifts) {
			if(shift.getShift().equals(currentShift)) {
				return shift.getShiftPrice();
			}
			//			Date startTime = shift.getShift().getStartTime();
			//			Date endTime = shift.getShift().getEndTime();
			//			if(startTime.after(currentShift.getStartTime()) && endTime.before(currentShift.getEndTime())) {
			//				return shift.getShiftPrice();
			//			}
		}
		return price;
	}
	
	@Override
	public String toString() {
		return getName();
	}

	public String getUniqueId() {
		return ("menu_item_" + getName() + "_" + getId()).replaceAll("\\s+", "_");
	}

	public TicketItem convertToTicketItem() {		
		TicketItem ticketItem = new TicketItem();
		ticketItem.setItemCount(1);
		ticketItem.setName(this.getName());
		
		System.out.println("Application.getInstance().getDineInTax(): " + Application.getInstance().getDineInTax());
		
		try {
			 
				ticketItem.setBon(this.getParent().getParent().getBon());
			 
		} catch(Exception ex) {
			ticketItem.setBon(0);
		}

		
			
		ticketItem.setGroupName(this.getParent().getName());
		ticketItem.setItemId(Integer.parseInt(this.getItemId()));
		ticketItem.setCategoryName(this.getParent().getParent().getName());
		 
			 
		 
			ticketItem.setUnitPrice(this.getPrice());
	 

		 

		if(this.isPfand() != null)
			ticketItem.setPfand(isPfand());

		if(getDiscountRate() != null && getDiscountRate() > 0.00) {
			Double unitPrice = ticketItem.getUnitPrice();      
			Double discountRate = getDiscountRate();
			ticketItem.setUnitPrice(unitPrice);
			ticketItem.setDiscountRate(discountRate);
		}

		if(this.getTax()==null||this.getTax().getRate()==0.0) {
			try {
				Tax tax = null;
				
				tax = TaxDAO.getInstance().findByName(this.getParent().getParent().getName());

					
				ticketItem.setTaxRate(tax.getRate());
			} catch(Exception ex) {
				if(this.getParent().getParent().isBeverage()||OrderView.getInstance().getTicketView().getCurrentTicket().getType()!=null&&
						!TerminalConfig.isSpecial()&&OrderView.getInstance().getTicketView().getCurrentTicket().getType().compareTo(TicketType.DINE_IN)==0)
					ticketItem.setTaxRate(Application.getInstance().getDineInTax());
				else {
					ticketItem.setTaxRate(Application.getInstance().getHomeDeleveryTax());
				}
			}
		} else {		
			ticketItem.setTaxRate(this.getTax().getRate());
		}
		ticketItem.setHasModifiers(hasModifiers());
		if (this.getParent().getParent().isBeverage()) {
			ticketItem.setBeverage(true);			
		}
		else {
			ticketItem.setBeverage(false);			
		}


		ticketItem.setId(getId());
		ticketItem.setVirtualPrinter(this.getVirtualPrinter());
		TicketView ticketView = OrderView.getInstance().getTicketView();
		 
		 ticketItem.setGroupName(getParent().getName());  
		
		return ticketItem;
	}
	
	public TicketItem convertToTicketItem(int count) {
		TicketItem ticketItem = new TicketItem();
		//ticketItem.setItemId(this.getId());


		ticketItem.setItemCount(count);
		String name = this.getName();
		if(name.length()>60) {
			name = name.substring(0,60)+"..";
		}
		String catName = this.getParent().getParent().getName();
		if(catName.length()>30) {
			catName = catName.substring(0,25)+"..";
		}
		String grpName = this.getParent().getName();
		if(grpName.length()>30) {
			grpName = grpName.substring(0,25)+"..";
		}
		ticketItem.setName(name);
		
		if(this.isPfand() != null)
            ticketItem.setPfand(this.isPfand());
		
		if(this.getPrice()<0)
			ticketItem.setPfand(this.isPfand());
		
		ticketItem.setWeightgrams(this.getWeightgrams());
		if(this.getParent().getParent().getBon() != null)
			ticketItem.setBon(this.getParent().getParent().getBon());
		
		ticketItem.setGroupName(grpName);
		ticketItem.setItemId(Integer.parseInt(this.getItemId()));
		ticketItem.setCategoryName(catName);
		ticketItem.setUnitType(this.getUnitType());

		Double discount = this.getParent().getDiscount();
		if(discount != null && discount > 0.00) {
			double unitPrice = this.getPrice();
			ticketItem.setUnitPrice(unitPrice);
			ticketItem.setDiscountRate(discount);
		}
		else {
			ticketItem.setUnitPrice(this.getPrice(Application.getInstance().getCurrentShift()));
		}

		if(this.getBarcode() != null)
			ticketItem.setBarcode(this.getBarcode());

		ticketItem.setTaxRate(this.getTax() == null ? 0 : this.getTax().getRate());
		ticketItem.setHasModifiers(hasModifiers());
		if (this.getParent().getParent().isBeverage()) {
			ticketItem.setBeverage(true);
			ticketItem.setShouldPrintToKitchen(true);
		}
		else {
			ticketItem.setBeverage(false);
			ticketItem.setShouldPrintToKitchen(true);
		}
		ticketItem.setVirtualPrinter(this.getVirtualPrinter());
		ticketItem.setPrintorder(this.getParent().getGaeng());
		return ticketItem;
	}

	public TicketItem convertToItemTicket()
	{
		TicketItem ticketItem = new TicketItem();

		ticketItem.setItemCount(1);

		String name = this.getName();
		if(name.length()>60) {
			name = name.substring(0,60)+"..";
		}
		String catName = this.getParent().getParent().getName();
		if(catName.length()>30) {
			catName = catName.substring(0,25)+"..";
		}
		String grpName = this.getParent().getName();
		if(grpName.length()>30) {
			grpName = grpName.substring(0,25)+"..";
		}
		ticketItem.setName(name);
		if(this.getParent().getParent().getBon() != null)
			ticketItem.setBon(this.getParent().getParent().getBon());
		ticketItem.setGroupName(grpName);
		ticketItem.setCategoryName(catName);
		ticketItem.setUnitType(this.getUnitType());
		ticketItem.setWeightgrams(this.getWeightgrams());

		ticketItem.setItemId(Integer.parseInt(this.getItemId()));
		ticketItem.setPfand(this.isPfand());
		if(this.getPrice()<0)
			ticketItem.setPfand(this.isPfand());

		Double discount = this.getParent().getDiscount();
		if(discount != null && discount > 0.00)
		{
			double unitPrice = this.getPrice();
			ticketItem.setUnitPrice(unitPrice);
			ticketItem.setDiscountRate(discount);
		} else {
			ticketItem.setUnitPrice(this.getPrice());
		}
		if(this.getBarcode() != null)
			ticketItem.setBarcode(this.getBarcode());

		ticketItem.setTaxRate(this.getTax() == null ? 0 : this.getTax().getRate());
		ticketItem.setHasModifiers(false);
		if (this.getParent().getParent().isBeverage()) {
			ticketItem.setBeverage(true);
			ticketItem.setShouldPrintToKitchen(true);
		}
		else {
			ticketItem.setBeverage(false);
			ticketItem.setShouldPrintToKitchen(true);
		}
		ticketItem.setVirtualPrinter(this.getVirtualPrinter());
		ticketItem.setPrintorder(this.getParent().getGaeng());
		return ticketItem;
	}
	public TicketItem convertToTicketItem(Ticket ticket, int count) {
		TicketItem ticketItem = new TicketItem();
		ticketItem.setItemCount(count);
		ticketItem.setPfand(this.isPfand());
		if(this.getPrice()<0)
			ticketItem.setPfand(this.isPfand());
		ticketItem.setItemId(Integer.parseInt(this.getItemId()));


		String name = this.getName();
		if(name.length()>60) {
			name = name.substring(0,60)+"..";
		}
		String catName = this.getParent().getParent().getName();
		if(catName.length()>30) {
			catName = catName.substring(0,25)+"..";
		}
		String grpName = this.getParent().getName();
		if(grpName.length()>30) {
			grpName = grpName.substring(0,25)+"..";
		}
		ticketItem.setName(name);
		ticketItem.setGroupName(grpName);
		ticketItem.setCategoryName(catName);
		ticketItem.setUnitType(this.getUnitType());
		ticketItem.setWeightgrams(this.getWeightgrams());

		if(this.getParent().getParent().getBon() != null)
			ticketItem.setBon(this.getParent().getParent().getBon());
		if(this.getBarcode() != null)
			ticketItem.setBarcode(this.getBarcode());

		Double discount = this.getParent().getDiscount();
		if(discount != null && discount > 0.00)
		{
			double unitPrice = this.getPrice();
			ticketItem.setUnitPrice(unitPrice);
			ticketItem.setDiscountRate(discount);
		} else {
			ticketItem.setUnitPrice(this.getPrice());
		}

		Restaurant restaurant = RestaurantDAO.getRestaurant();
		if((ticket.getType() == TicketType.HOME_DELIVERY) && (restaurant.getDupdinein() != null)&&(restaurant.getDupdinein()))
		{
			if(this.getParent().getParent().isBeverage())
			{
				ticketItem.setTaxRate(OrderView.taxDineIn);
			}
			else
			{
				ticketItem.setTaxRate(OrderView.taxHomeDelivery);
			}
		}
		else	
			ticketItem.setTaxRate(this.getTax() == null ? 0 : this.getTax().getRate());

		try {
			ticketItem.setHasModifiers(hasModifiers());

		}catch(Exception ex) {

		}
		if (this.getParent().getParent().isBeverage()) {
			ticketItem.setBeverage(true);
			ticketItem.setShouldPrintToKitchen(true);
		}
		else {
			ticketItem.setBeverage(false);
			ticketItem.setShouldPrintToKitchen(true);
		}
		ticketItem.setVirtualPrinter(this.getVirtualPrinter());
		ticketItem.setPrintorder(this.getParent().getGaeng());
		return ticketItem;
	}


	public TicketItem convertToTicketItem(Ticket ticket, int count, boolean pfand) {
		TicketItem ticketItem = new TicketItem();
		ticketItem.setItemCount(count);
		ticketItem.setPfand(pfand);
		if(this.getPrice()<0)
			ticketItem.setPfand(this.isPfand());
		ticketItem.setItemId(Integer.parseInt(this.getItemId()));
		String name = this.getName();
		if(name.length()>60) {
			name = name.substring(0,60)+"..";
		}
		String catName = this.getParent().getParent().getName();
		if(catName.length()>30) {
			catName = catName.substring(0,25)+"..";
		}
		String grpName = this.getParent().getName();
		if(grpName.length()>30) {
			grpName = grpName.substring(0,25)+"..";
		}
		ticketItem.setName(name);
		ticketItem.setGroupName(grpName);
		ticketItem.setCategoryName(catName);
		ticketItem.setUnitType(this.getUnitType());
		ticketItem.setWeightgrams(this.getWeightgrams());

		if(this.getParent().getParent().getBon() != null)
			ticketItem.setBon(this.getParent().getParent().getBon());
		if(this.getBarcode() != null)
			ticketItem.setBarcode(this.getBarcode());

		if(pfand)
			ticketItem.setUnitPrice(0-this.getPrice());
		else
			ticketItem.setUnitPrice(this.getPrice());
		ticketItem.setDiscountRate(0.0);

		Restaurant restaurant = RestaurantDAO.getRestaurant();
		if((ticket.getType() == TicketType.HOME_DELIVERY) && (restaurant.getDupdinein() != null)&&(restaurant.getDupdinein()))
		{
			if(this.getParent().getParent().isBeverage())
			{
				ticketItem.setTaxRate(OrderView.taxDineIn);
			}
			else
			{
				ticketItem.setTaxRate(OrderView.taxHomeDelivery);
			}
		}
		else	
			ticketItem.setTaxRate(this.getTax() == null ? 0 : this.getTax().getRate());

		try {
			ticketItem.setHasModifiers(false);

		} catch(Exception ex) {  }


		Double discount = this.getParent().getDiscount();
		if(discount != null && discount > 0.00&&!pfand)
		{
			double unitPrice = this.getPrice();
			ticketItem.setUnitPrice(unitPrice);
			ticketItem.setDiscountRate(discount);
		}

		if (this.getParent().getParent().isBeverage()) {
			ticketItem.setBeverage(true);
			ticketItem.setShouldPrintToKitchen(true);
		}
		else {
			ticketItem.setBeverage(false);
			ticketItem.setShouldPrintToKitchen(true);
		}
		ticketItem.setVirtualPrinter(this.getVirtualPrinter());
		ticketItem.setPrintorder(this.getParent().getGaeng());
		return ticketItem;
	}

	public boolean hasModifiers() {
		return (this.getMenuItemModiferGroups() != null && this.getMenuItemModiferGroups().size() > 0);
	}
}