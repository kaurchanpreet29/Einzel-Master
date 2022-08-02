package com.floreantpos.model;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;
import org.apache.commons.collections.*;
import com.floreantpos.POSConstants;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.base.BaseTicket;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.ui.views.order.OrderView;
import com.floreantpos.util.NumberUtil;
import com.floreantpos.util.POSUtil;

@XmlRootElement(name="ticket")
public class Ticket extends BaseTicket {
	private static final long serialVersionUID = 1L;
	// public final static int TAKE_OUT = -1;

	//	public final static String DINE_IN = "DINE IN";
	//	public final static String TAKE_OUT = "TAKE OUT";
	//	public final static String PICKUP = "PICKUP";
	//	public final static String HOME_DELIVERY = "HOME DELIVERY";
	//	public final static String DRIVE_THROUGH = "DRIVE THRU";
	//	public final static String BAR_TAB = "BAR_TAB";

	public final static String PROPERTY_CARD_TRANSACTION_ID = "card_transaction_id";
	public final static String PROPERTY_CARD_TRACKS = "card_tracks";
	public static final String PROPERTY_CARD_NAME = "card_name";
	public static final String PROPERTY_PAYMENT_METHOD = "payment_method";
	public static final String PROPERTY_CARD_READER = "card_reader";
	public static final String PROPERTY_CARD_NUMBER = "card_number";
	public static final String PROPERTY_CARD_EXP_YEAR = "card_exp_year";
	public static final String PROPERTY_CARD_EXP_MONTH = "card_exp_month";
	public static final String PROPERTY_ADVANCE_PAYMENT = "advance_payment";
	public static final String PROPERTY_CARD_AUTH_CODE = "card_auth_code";

	/* [CONSTRUCTOR MARKER BEGIN] */
	public Ticket () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Ticket (java.lang.Integer id) {
		super(id);
	}

	/* [CONSTRUCTOR MARKER END] */

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy, h:m a");
	private DecimalFormat numberFormat = new DecimalFormat("0.00");

	private List deletedItems;
	private boolean priceIncludesTax;
	private boolean cashPayment;
	public static final String CUSTOMER_PHONE = "CUSTOMER_PHONE";
	public static final String CUSTOMER_PHONE2 = "CUSTOMER_PHONE2";
	public static final String CUSTOMER_NAME = "CUSTOMER_NAME";
	public static final String CUSTOMER_ID = "CUSTOMER_ID";
	public static final String CUSTOMER_LANDMARK = "LAND_MARK";
	public static final String CUSTOMER_BELLNAME = "BELL_NAME";
	public static final String CUSTOMER_POSTCODE = "POST_CODE";
	public static final String CUSTOMER_CITYNAME = "CITY_NAME";
	public static final String CUSTOMER_FIRMNAME = "FIRM_NAME";
	public static final String CUSTOMER_ADDRESS = "ADDRESS";
	public static final String CUSTOMER_SALUTATION = "SALUTATION";
	public static final String CUSTOMER_LOYALTY_NO = "LOYALTY_NO";
	public static final String CUSTOMER_DOOR = "DOOR";
	public String getTableNumbers() {
		Set<ShopTable> tables = getTables();
		if(tables == null) return "";

		String s = "";
		for (Iterator iterator = tables.iterator(); iterator.hasNext();) {
			ShopTable shopTable = (ShopTable) iterator.next();
			s += shopTable.getNumber();

			if(iterator.hasNext()) {
				s += ", ";
			}
		}

		return s;
	}

	@Override
	public void setClosed(Boolean closed) {
		super.setClosed(closed);
		if(closed) {
			releaseTables();
		}
	}

	private void releaseTables() {
		Set<ShopTable> tables = getTables();
		if(tables == null) return;
		for (ShopTable shopTable : tables) {
			shopTable.setOccupied(false);
		}
	}

	public void setGratuityAmount(double amount) {
		Gratuity gratuity = getGratuity();
		if(gratuity == null) {
			gratuity = createGratuity();
			setGratuity(gratuity);
		}

		gratuity.setAmount(amount);
	}

	public Gratuity createGratuity() {
		Gratuity gratuity;
		gratuity = new Gratuity();
		gratuity.setTicket(this);
		gratuity.setTerminal(Application.getInstance().getTerminal());
		gratuity.setOwner(getOwner());
		gratuity.setPaid(false);
		return gratuity;
	}

	public boolean hasGratuity() {
		return getGratuity() != null;
	}

	@Override
	public void setCreateDate(Date createDate) {
		super.setCreateDate(createDate);
		super.setActiveDate(createDate);
	}

	@Override
	public Date getDeliveryDate() {
		Date deliveryDate = super.getDeliveryDate();
		if (deliveryDate == null) {
			deliveryDate = getCreateDate();
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MINUTE, 10);
			deliveryDate = c.getTime();
			c.setTime(deliveryDate);
		}

		return deliveryDate;
	}

	@Override
	public List<TicketItem> getTicketItems() {
		List<TicketItem> items = super.getTicketItems();

		if (items == null) {
			items = new ArrayList<TicketItem>();
			super.setTicketItems(items);
		}
		return items;
	}

	@Override
	public Integer getNumberOfGuests() {
		Integer guests = super.getNumberOfGuests();
		if (guests == null || guests.intValue() == 0) {
			return Integer.valueOf(1);
		}
		return guests;
	}

	public Ticket(User owner, Date createTime) {
		setOwner(owner);
		setCreateDate(createTime);
	}

	public String getCreateDateFormatted() {
		return dateFormat.format(getCreateDate());
	}

	public String getTitle() {
		String title = "";
		if (getId() != null) {
			title += "#" + getId();
		}
		title += " Server" + ": " + getOwner();
		title += " Create on" + ":" + getCreateDateFormatted();
		title += " Total" + ": " + numberFormat.format(getTotalAmount());

		return title;
	}

	public static class ItemComparator implements Comparator {
		public ItemComparator() {
		}

		@Override
		public int compare(Object o1, Object o2) {
			if (!(o1 instanceof Ticket) || !(o2 instanceof Ticket))
				throw new ClassCastException();

			Ticket e1 = (Ticket) o1;
			Ticket e2 = (Ticket) o2;

			if (e1.getCreateDate() == null || e2.getCreateDate() == null)
				return 0;
			return e1.getCreateDate().compareTo(e2.getCreateDate());
		}
	}

	public int getBeverageCount() {
		List<TicketItem> ticketItems = getTicketItems();
		if (ticketItems == null)
			return 0;

		int count = 0;
		for (TicketItem ticketItem : ticketItems) {
			if (ticketItem.isBeverage()) {
				count += ticketItem.getItemCount();
			}
		}
		return count;
	}
	/*public double calculateTax1() {
		if (isTaxExempt()) {
			return 0;
		}

		Tax dineIn = TaxDAO.getInstance().get(2);
		Tax home = TaxDAO.getInstance().get(1);
		Double dineInTax = dineIn.getRate();
		Double homeTax = home.getRate();
		Double totalTax = 0.00;
		List<TicketItem> ticketItems = getTicketItems();
		if (ticketItems == null) {
			return 0;
		}

		if (RestaurantDAO.getRestaurant().isItemPriceIncludesTax())
		{
				if (this.getType() == TicketType.DINE_IN)
				{
					Double sub = 0.00;
					Double tax = 0.00;
					for (TicketItem ticketItem : ticketItems) {
						sub = ticketItem.getSubtotalAmount();
						tax =  ((dineInTax/100)*sub);
						totalTax += tax;
					}

					return NumberUtil.roundToTwoDigit(totalTax);

				}
				else
				{
					Double sub = 0.00;
					Double tax = 0.00;
					for (TicketItem ticketItem : ticketItems) {
						sub = ticketItem.getSubtotalAmount();
						tax =  ((homeTax/100)*sub);
						totalTax += tax;
					}
					return NumberUtil.roundToTwoDigit(totalTax);
			}
		}
		else
		{
			double tax = 0;
			for (TicketItem ticketItem : ticketItems) {
				tax += ticketItem.getTaxAmount();
			}

			return NumberUtil.roundToTwoDigit(fixInvalidAmount(tax));

		}
	}*/

	/*public double calculateSubtotalAmount1() {
		double subtotalAmount = 0;
		Tax dineIn = TaxDAO.getInstance().get(2);
		Tax home = TaxDAO.getInstance().get(1);
		Double dineInTax = dineIn.getRate();
		Double homeTax = home.getRate();

		List<TicketItem> ticketItems = getTicketItems();
		if (ticketItems == null) {
			return subtotalAmount;
		}

		if (RestaurantDAO.getRestaurant().isItemPriceIncludesTax())
		{
				if (this.getType() == TicketType.DINE_IN)
				{
					Double sub = 0.00;
					for (TicketItem ticketItem : ticketItems) {
						sub = ticketItem.getSubtotalAmount();
						sub = sub - ((dineInTax/100)*sub);
						subtotalAmount += sub;
					}

					subtotalAmount = fixInvalidAmount(subtotalAmount);

					return NumberUtil.roundToTwoDigit(subtotalAmount);

				}
				else
				{
					Double sub = 0.00;
					for (TicketItem ticketItem : ticketItems) {
						sub = ticketItem.getSubtotalAmount();
						sub = sub - ((homeTax/100)*sub);
						subtotalAmount += sub;
					}

					subtotalAmount = fixInvalidAmount(subtotalAmount);

					return NumberUtil.roundToTwoDigit(subtotalAmount);

				}
		}
		else
		{
			for (TicketItem ticketItem : ticketItems) {

				subtotalAmount += ticketItem.getSubtotalAmount();
			}

			subtotalAmount = fixInvalidAmount(subtotalAmount);

		}
		return subtotalAmount;

	}*/

	public void calculatePrice() {
		
		priceIncludesTax = Application.getInstance().isPriceIncludesTax();
		List<TicketItem> ticketItems = getTicketItems();
		if (ticketItems == null || ticketItems.isEmpty()) {
			return;
		}
		double ausTax = 0.0;
		double imTax = 0.0;
		double ausAmnt = 0.0;
		double imAmnt = 0.0;
		double zeroAmnt = 0.0;
		for (TicketItem ticketItem : ticketItems) {
			try {
			ticketItem.calculatePrice();
			} catch(Exception e) {  }
			if(ticketItem.getTaxRate().compareTo(OrderView.taxHomeDelivery)==0&&!ticketItem.isBeverage()) {
				ausTax += ticketItem.getTaxAmount();
				ausAmnt += ticketItem.getTotalAmount();
			} else if(ticketItem.getTaxRate().compareTo(OrderView.getTaxDineIn())==0||ticketItem.isBeverage()) {
				imTax += ticketItem.getTaxAmount();
				imAmnt += ticketItem.getTotalAmount();
			} else if(ticketItem.getTaxRate()==0) {
				zeroAmnt += ticketItem.getTotalAmount();
			}			
		}

		
		double subtotalAmount = calculateSubtotalAmount();
		double discountAmount = calculateDiscountAmount();

		setSubtotalAmount(subtotalAmount);
		setDiscountAmount(discountAmount);

		double taxAmount = calculateTax();
		setTaxAmount(taxAmount);
		setImHausAmnt(imAmnt);
		setImHausTax(imTax);
		setAusHausAmnt(ausAmnt);
		setAusHausTax(ausTax);
		setZeroAmnt(zeroAmnt);

		double totalAmount = 0;

		if (priceIncludesTax) {
			totalAmount = subtotalAmount - discountAmount ;
		}
		else {
			totalAmount = subtotalAmount - discountAmount + taxAmount;
		}

//
//		if (getGratuity() != null) {
//			totalAmount += getGratuity().getAmount();
//		}

		totalAmount = fixInvalidAmount(totalAmount);
		setTotalAmount(NumberUtil.roundToTwoDigit(totalAmount));
		double dueAmount = totalAmount - getPaidAmount();
		setDueAmount(NumberUtil.roundToTwoDigit(dueAmount));
	}

	public void calculatePriceGutshein() {
		priceIncludesTax = true;
		//		if(dineInTax==null) {
		//			Tax dineIn = TaxDAO.getInstance().findByName(POSConstants.DINE_IN);
		//			Tax homeDelevery = TaxDAO.getInstance().findByName(POSConstants.HOME_DELIVERY);    
		//			homeDeleveryTax = homeDelevery.getRate();
		//			dineInTax = dineIn.getRate();
		//		}
		List<TicketItem> ticketItems = getTicketItems();
		if (ticketItems == null) {
			return;
		}

		//		for (TicketItem ticketItem : ticketItems) {
		//			if (this.getTicketType()!=null&&this.getTicketType().compareTo(TicketType.DINE_IN.name()) == 0) {
		//				ticketItem.setTaxRate(0.00);
		//			} else {
		//				if (ticketItem.isBeverage()) {
		//					ticketItem.setTaxRate(dineInTax);
		//				} else if(this.getTicketType().compareTo(TicketType.HOME_DELIVERY.name())==0) {
		//					ticketItem.setTaxRate(homeDeleveryTax);
		//				}else {
		//					ticketItem.setTaxRate(0.00);
		//				}
		//			}
		//			if (ticketItem != null) {
		//				ticketItem.calculatePrice();
		//			}
		//		}
		for (TicketItem ticketItem : ticketItems) {
			if (ticketItem != null) {
				/*if(ticketItem.isAusserhaus())
					ticketItem.setTaxRate(TaxDAO.getInstance().findByName(POSConstants.HOME_DELIVERY).getRate());
				ticketItem.calculatePrice();*/
			}
		}

		double subtotalAmount = calculateSubtotalAmount();
		//		double discountAmount = calculateDiscountAmount();

		setSubtotalAmount(subtotalAmount);
		// setDiscountAmount(discountAmount);

		double taxAmount = calculateTax();

		setTaxAmount(taxAmount);

		double totalAmount = 0;
		totalAmount = subtotalAmount;
		totalAmount = fixInvalidAmount(totalAmount);

		setTotalAmount(NumberUtil.roundToTwoDigit(totalAmount));
		double dueAmount = totalAmount - getPaidAmount();
		setDueAmount(NumberUtil.roundToTwoDigit(dueAmount));
	}
	
	public void calculatePrice(Double value,Boolean deleteCoupon) {
		priceIncludesTax = Application.getInstance().isPriceIncludesTax();

		List<TicketItem> ticketItems = getTicketItems();
		if (ticketItems == null) {
			return;
		}

		for (TicketItem ticketItem : ticketItems) {
			if(ticketItem.isPfand())
				continue;
			if(!deleteCoupon)
				ticketItem.changeTicketPrice(value);
			else 
				ticketItem.changeDeleteCouponPrice(value);
			 
		}
		double discountAmount = calculateDiscountAmount();
		double ausTax = 0.0;
		double imTax = 0.0;
		double ausAmnt = 0.0;
		double imAmnt = 0.0;
		double bevAmnt = 0.0;
		double zeroAmnt = 0.0;		
		for (TicketItem ticketItem : ticketItems) {
			ticketItem.calculatePrice();
			if(ticketItem.getTaxRate().compareTo(OrderView.taxHomeDelivery)==0&&!ticketItem.isBeverage()) {
				ausTax += ticketItem.getTaxAmount();
				ausAmnt += ticketItem.getTotalAmount();
			}
			if(ticketItem.getTaxRate().compareTo(OrderView.getTaxDineIn())==0||ticketItem.isBeverage()) {
				imTax += ticketItem.getTaxAmount();
				imAmnt += ticketItem.getTotalAmount();
				if(ticketItem.isBeverage())
					bevAmnt += ticketItem.getTotalAmount();
			}
			if(ticketItem.getTaxRate().compareTo(0.0)==0) {
				zeroAmnt += ticketItem.getTotalAmount();
			}

		}

		double subtotalAmount = calculateSubtotalAmount();


		setSubtotalAmount(subtotalAmount);
		setDiscountAmount(discountAmount);

		double taxAmount = calculateTax();

		setTaxAmount(taxAmount);
		setImHausAmnt(imAmnt);
		setImHausTax(imTax);
		setAusHausAmnt(ausAmnt);
		setAusHausTax(ausTax);
		double totalAmount = 0;

		if (priceIncludesTax) {
			totalAmount = subtotalAmount - discountAmount;
		}
		else {
			totalAmount = subtotalAmount - discountAmount + taxAmount;
		}

		//		if (priceIncludesTax) {
		//			totalAmount = subtotalAmount - discountAmount;
		//		}
		//		else {
		//			totalAmount = subtotalAmount - discountAmount + taxAmount;
		//		}

		if (getGratuity() != null) {
			totalAmount += getGratuity().getAmount();
		}
		totalAmount = fixInvalidAmount(totalAmount);
		//totalAmount = subtotalAmount;
		setTotalAmount(NumberUtil.roundToTwoDigit(totalAmount));
		double dueAmount = totalAmount - getPaidAmount();
		setDueAmount(NumberUtil.roundToTwoDigit(dueAmount));
	}

	private double calculateSubtotalAmount() {
		double subtotalAmount = 0;

		List<TicketItem> ticketItems = getTicketItems();
		if (ticketItems == null) {
			return subtotalAmount;
		}

		for (TicketItem ticketItem : ticketItems) {
			if (ticketItem != null) {
				if(ticketItem.getName()!=null&&ticketItem.getName().equals("Kunden-Rabatt")) {
					subtotalAmount += ticketItem.getSubtotalAmount();
				} else {
					subtotalAmount += ticketItem.getSubtotalAmount()-ticketItem.getDiscountAmount();
				}
			}
		}

		subtotalAmount = fixInvalidAmount(subtotalAmount);

		return NumberUtil.roundToTwoDigit(subtotalAmount);
	}


	private double calculateDiscountAmount() {
				double subtotalAmount = getSubtotalAmount();
		double discountAmount = 0;

		List<TicketItem> ticketItems = getTicketItems();
		if (ticketItems != null) {

			for (TicketItem ticketItem : ticketItems) {
				if(ticketItem.getTaxExist()) {
				discountAmount += ticketItem.getDiscountAmount();
				}
			}			
		}
		//		if(discountAmount ==0) {
		//			List<TicketCouponAndDiscount> discounts = getCouponAndDiscounts();
		//			if (discounts != null) {
		//				for (TicketCouponAndDiscount discount : discounts) {
		//					discountAmount += calculateDiscountFromType(discount, subtotalAmount);
		//				}
		//			}
		//		}	
		
		List<TicketCouponAndDiscount> discounts = getCouponAndDiscounts();
		if (discounts != null) {
			for (TicketCouponAndDiscount discount : discounts) {
				//				if(deleteCoupon&&(discount.getType() == CouponAndDiscount.GUTSCHEIN))
				//					discountAmount += calculateDiscountFromType(discount, subtotalAmount);
				//				else if(!deleteCoupon)
				//discountAmount += calculateDiscountFromType(discount, subtotalAmount);
				discountAmount += calculateDiscountFromType(discount, subtotalAmount);
			}
		}

		discountAmount = fixInvalidAmount(discountAmount);

		return NumberUtil.roundToTwoDigit(discountAmount);
	}

	private double calculateTax() {
		if (isTaxExempt()) {
			return 0;
		}

		List<TicketItem> ticketItems = getTicketItems();
		if (ticketItems == null) {
			return 0;
		}

		double tax = 0;
		for (TicketItem ticketItem : ticketItems) {
			tax += ticketItem.getTaxAmount();
		}

		return NumberUtil.roundToTwoDigit(fixInvalidAmount(tax));
	}
	public double getTax19() {

		List<TicketItem> ticketItems = getTicketItems();
		if (ticketItems == null) {
			return 0;
		}

		double tax = 0;
		for (TicketItem ticketItem : ticketItems) {
			if(ticketItem.getTaxRate().compareTo(OrderView.taxDineIn)==0)
				tax += ticketItem.getTaxAmount();
		}

		return NumberUtil.roundToTwoDigit(fixInvalidAmount(tax));
	}

	public double getRefunTax19() {

		List<TicketItem> ticketItems = getTicketItems();
		if (ticketItems == null) {
			return 0;
		}

		double tax = 0;
		for (TicketItem ticketItem : ticketItems) {
			if(ticketItem.getTaxRate().compareTo(OrderView.taxDineIn)==0) {
				if(ticketItem.isPfand())
					tax += ticketItem.getTaxAmount();
			}
		}

		return NumberUtil.roundToTwoDigit(fixInvalidAmount(tax));
	}

	public double getRefunTax7() {
		List<TicketItem> ticketItems = getTicketItems();
		if (ticketItems == null) {
			return 0;
		}

		double tax = 0;
		for (TicketItem ticketItem : ticketItems) {
			if(ticketItem.getTaxRate().compareTo(OrderView.taxHomeDelivery)==0) {
				if(ticketItem.isPfand())
					tax += ticketItem.getTaxAmount();
			}
		}

		return NumberUtil.roundToTwoDigit(fixInvalidAmount(tax));
	}

	public int getRefundCount() {	
		List<TicketItem> ticketItems = getTicketItems();
		if (ticketItems == null) {
			return 0;
		}

		int count = 0;
		for (TicketItem ticketItem : ticketItems) {
			if(ticketItem.isPfand())
				count += ticketItem.getItemCount();		
		}

		return count;
	}

	public double getRefunAmount() {	
		List<TicketItem> ticketItems = getTicketItems();
		if (ticketItems == null) {
			return 0;
		}

		double tax = 0;
		for (TicketItem ticketItem : ticketItems) {
			if(ticketItem.isPfand())
				tax += ticketItem.getTotalAmount();		
		}

		return NumberUtil.roundToTwoDigit(fixInvalidAmount(tax));
	}

	public double getTax7() {

		List<TicketItem> ticketItems = getTicketItems();
		if (ticketItems == null) {
			return 0;
		}

		double tax = 0;		
		for (TicketItem ticketItem : ticketItems) {
			if(ticketItem.getTaxRate().compareTo(OrderView.taxHomeDelivery)==0)
				tax += ticketItem.getTaxAmount();		
		}
		return NumberUtil.roundToTwoDigit(fixInvalidAmount(tax));
	}

	public double getTax0() {

		List<TicketItem> ticketItems = getTicketItems();
		if (ticketItems == null) {
			return 0;
		}
		double tax = 0;
		for (TicketItem ticketItem : ticketItems) {
			if(ticketItem.getTaxRate() == 0.00)
				tax += ticketItem.getTaxAmount();
		}
		return NumberUtil.roundToTwoDigit(fixInvalidAmount(tax));
	}

	public double getBeverageTax()
	{
		List<TicketItem> ticketItems = getTicketItems();
		if (ticketItems == null) {
			return 0;
		}

		double tax = 0;
		for (TicketItem ticketItem : ticketItems) {
			if (ticketItem.isBeverage())
			{
				tax += ticketItem.getTaxAmount();
			}
		}
		return NumberUtil.roundToTwoDigit(fixInvalidAmount(tax));

	} 

	public void calculatePrice(Double value, Boolean deleteCoupon, int coupanType) {
		
		priceIncludesTax = Application.getInstance().isPriceIncludesTax();
		List<TicketItem> ticketItems = getTicketItems();
		if (ticketItems == null) {
			return;
		}
		double discountAmount = 0.0;
		discountAmount = calculateDiscountAmount(deleteCoupon);
		//System.out.println("Discount amount  "+discountAmount+coupanType);
		//		boolean gutschein = true;
		//		double total=0.0;
		for (TicketItem ticketItem : ticketItems) {
			if (ticketItem.getItemId() != 999&&coupanType==CouponAndDiscount.PERCENTAGE_PER_ITEM||ticketItem.getItemId() != 999&&coupanType==CouponAndDiscount.PERCENTAGE_PER_ORDER) {
				if(ticketItem.getUnitPrice()<0)
					continue;				
				
					if (!deleteCoupon)
						ticketItem.changeTicketPrice(value);
					else
						ticketItem.changeDeleteCouponPrice(value);				
			}
		}

		for (TicketItem ticketItem : ticketItems) {
			ticketItem.calculatePrice();  
		}

		double subtotalAmount = calculateSubtotalAmount();
		setSubtotalAmount(subtotalAmount);
		setDiscountAmount(discountAmount);

		double taxAmount = calculateTax();

		setTaxAmount(taxAmount);

		double totalAmount = 0;

		if (priceIncludesTax) {
			if(!deleteCoupon&&coupanType==CouponAndDiscount.PERCENTAGE_PER_ITEM||!deleteCoupon&&coupanType==CouponAndDiscount.PERCENTAGE_PER_ORDER)
				totalAmount = subtotalAmount;
			else
				totalAmount = subtotalAmount - discountAmount;
		} else {			
			if(!deleteCoupon&&coupanType==CouponAndDiscount.PERCENTAGE_PER_ITEM||!deleteCoupon&&coupanType==CouponAndDiscount.PERCENTAGE_PER_ORDER)
				totalAmount = subtotalAmount + taxAmount;
			else
				totalAmount = subtotalAmount - discountAmount + taxAmount;			
		}

		totalAmount = fixInvalidAmount(totalAmount);
		// totalAmount = subtotalAmount;
		setTotalAmount(NumberUtil.roundToTwoDigit(totalAmount));
		double dueAmount = totalAmount - getPaidAmount();
		setDueAmount(NumberUtil.roundToTwoDigit(dueAmount));

	}
	
	private double calculateDiscountAmount(boolean deleteCoupon) {
		
		double subtotalAmount = getSubtotalAmount();		
		double discountAmount = 0;

		//		List<TicketItem> ticketItems = getTicketItems();
		//		if (ticketItems != null) {
		//			for (TicketItem ticketItem : ticketItems) {
		//				if (ticketItem != null) {
		//					discountAmount += ticketItem.getDiscountAmount();
		//				}
		//			}
		//			
		//		}
		List<TicketCouponAndDiscount> discounts = getCouponAndDiscounts();
		if (discounts != null) {
			for (TicketCouponAndDiscount discount : discounts) {
				//				if(deleteCoupon&&(discount.getType() == CouponAndDiscount.GUTSCHEIN))
				//					discountAmount += calculateDiscountFromType(discount, subtotalAmount);
				//				else if(!deleteCoupon)
				discountAmount += calculateDiscountFromType(discount, subtotalAmount);
			}
		}

		discountAmount = fixInvalidAmount(discountAmount);

		return NumberUtil.roundToTwoDigit(discountAmount);
	}
	
	public double getPositiveSubtotal(boolean includeModifier) {
		double subtotalAmount = 0;

		List<TicketItem> ticketItems = getTicketItems();
		if (ticketItems == null) {
			return subtotalAmount;
		}

		for (TicketItem ticketItem : ticketItems) {
			if (ticketItem != null && ticketItem.getSubtotalAmount() > 0) {
				subtotalAmount += ticketItem.getSubtotalAmountWithoutModifiers();
				if (includeModifier) {
					if (ticketItem.isHasModifiers()) {
						List<TicketItemModifierGroup> modifierGroups = ticketItem.getTicketItemModifierGroups();
						if (!CollectionUtils.isEmpty(modifierGroups)) {
							for (TicketItemModifierGroup group : modifierGroups) {
								List<TicketItemModifier> modifiers = group.getTicketItemModifiers();

								if (!CollectionUtils.isEmpty(modifiers)) {
									for (TicketItemModifier modifier : modifiers) {
										if (modifier.getTotalAmount() < 0
												&& modifier.getNameDisplay().contains("Art-Rabatt"))
											continue;
										else
											subtotalAmount += modifier.getSubTotalAmount();
									}
								}
							}
						}
					}
				}
			}
		}

		subtotalAmount = fixInvalidAmount(subtotalAmount);

		return NumberUtil.roundToTwoDigit(subtotalAmount);
	}
	
	public double getNonBeverageTax()
	{
		List<TicketItem> ticketItems = getTicketItems();
		if (ticketItems == null) {
			return 0;
		}

		double tax = 0;
		for (TicketItem ticketItem : ticketItems) {
			if (!ticketItem.isBeverage())
			{
				tax += ticketItem.getTaxAmount();
			}
		}
		return NumberUtil.roundToTwoDigit(fixInvalidAmount(tax));

	} 
	private double fixInvalidAmount(double tax) {
		if (!isRefunded()&&tax < 0 || Double.isNaN(tax)) {
			tax = 0;
		}
		return tax;
	}

	public double calculateDiscountFromType(TicketCouponAndDiscount coupon, double subtotal) {
		List<TicketItem> ticketItems = getTicketItems();
		double discount = 0;
		int type = coupon.getType();
		double couponValue = coupon.getValue();

		switch (type) {
		case CouponAndDiscount.FIXED_PER_ORDER:
			discount += couponValue;
			break;

		case CouponAndDiscount.FIXED_PER_CATEGORY:
			HashSet<Integer> categoryIds = new HashSet<Integer>();
			for (TicketItem item : ticketItems) {
				if(item.isPfand())
					continue;
				Integer itemId = item.getItemId();
				if (!categoryIds.contains(itemId)) {
					discount += couponValue;
					categoryIds.add(itemId);
				}
			}
			break;

		case CouponAndDiscount.FIXED_PER_ITEM:
			for (TicketItem item : ticketItems) {
				if(item.isPfand())
					continue;
				discount += (couponValue * item.getItemCount());
			}
			break;

		case CouponAndDiscount.PERCENTAGE_PER_ORDER:
			System.out.println("dicountPerOrder: " + CouponAndDiscount.PERCENTAGE_PER_ORDER );
			discount += ((subtotal * couponValue) / 100.0);
			break;

		case CouponAndDiscount.PERCENTAGE_PER_CATEGORY:
			categoryIds = new HashSet<Integer>();
			for (TicketItem item : ticketItems) {
				if(item.isPfand())
					continue;
				Integer itemId = item.getItemId();
				if (!categoryIds.contains(itemId)) {
					discount += ((item.getUnitPrice() * couponValue) / 100.0);
					categoryIds.add(itemId);
				}
			}
			break;

		case CouponAndDiscount.PERCENTAGE_PER_ITEM:
			for (TicketItem item : ticketItems) {
				if(item.isPfand())
					continue;
				
				if(item.isExistDiscount())
				discount += ((item.getSubtotalAmountWithoutModifiers() * couponValue) / 100.0);
			}
			break;

		case CouponAndDiscount.FREE_AMOUNT:
			discount += couponValue;
			break;
			
		case CouponAndDiscount.DIRECT_RABATT:
			discount += coupon.getUsedValue();
			System.out.println("discount<<: " + discount);
			break;
			
		case CouponAndDiscount.GUTSCHEIN:			
			discount += couponValue;
			System.out.println("discount: " + discount);
			break;
		}
		return discount;
	}

	public void addDeletedItems(Object o) {
		if (deletedItems == null) {
			deletedItems = new ArrayList();
		}	
		
		 
		TicketItem foundItem = null;
		TicketItem org = (TicketItem)o;
		for(Iterator<TicketItem> itr = deletedItems.iterator();itr.hasNext();)
		{
			TicketItem item = itr.next();
			if((org.getName().compareTo(item.getName()) == 0)&&(org.getItemId().intValue() == item.getItemId().intValue()))
			{
				foundItem = item;
				break;
			}
		}
		if(foundItem != null)
		{
			deletedItems.remove(foundItem);
			foundItem.setItemCount(foundItem.getItemCount()+1);
			foundItem.setTotalAmountWithoutModifiers(foundItem.getTotalAmountWithoutModifiers()+foundItem.getUnitPrice());
			deletedItems.add(foundItem);
		}
		else
		{
			deletedItems.add(o);
		}
	}

	public List getDeletedItems() {
		return deletedItems;
	}

	public void clearDeletedItems() {
		if (deletedItems != null) {
			deletedItems.clear();
		}

		deletedItems = null;
	}

	public boolean needsKitchenPrint() {
		if (getDeletedItems() != null && getDeletedItems().size() > 0) {
			return true;
		}

		List<TicketItem> ticketItems = getTicketItems();
		for (TicketItem item : ticketItems) {
			if (item.isShouldPrintToKitchen() && !item.isPrintedToKitchen()) { 
				return true;
			}

			List<TicketItemModifierGroup> modifierGroups = item.getTicketItemModifierGroups();
			if (modifierGroups != null) {
				for (TicketItemModifierGroup modifierGroup : modifierGroups) {
					List<TicketItemModifier> ticketItemModifiers = modifierGroup.getTicketItemModifiers();
					if (ticketItemModifiers != null) {
						for (TicketItemModifier modifier : ticketItemModifiers) {
							if (modifier.isShouldPrintToKitchen() && !modifier.isPrintedToKitchen()) {
								return true;
							}
						}
					}
				}
			}

			List<TicketItemCookingInstruction> cookingInstructions = item.getCookingInstructions();
			if (cookingInstructions != null) {
				for (TicketItemCookingInstruction ticketItemCookingInstruction : cookingInstructions) {
					if (!ticketItemCookingInstruction.isPrintedToKitchen()) {
						return true;
					}
				}
			}
		}

		return false;
	}

	//	public double calculateDefaultGratutity() {
	//		if (!DINE_IN.equals(getTicketType())) {
	//			return 0;
	//		}
	
	//		Restaurant restaurant = Application.getInstance().getRestaurant();
	//		double defaultGratuityPercentage = restaurant.getDefaultGratuityPercentage();
	//
	//		if (defaultGratuityPercentage <= 0) {
	//			return 0;
	//		}
	//
	//		Gratuity gratuity = new Gratuity();
	//		double tip = getDueAmount() * (defaultGratuityPercentage / 100.0);
	//		gratuity.setAmount(tip);
	//		gratuity.setOwner(getOwner());
	//		gratuity.setPaid(false);
	//		gratuity.setTicket(this);
	//		gratuity.setTerminal(getTerminal());
	//
	//		setGratuity(gratuity);
	//
	//		return tip;
	//	}
	public TicketType getType() {
		String type = getTicketType();

		if(StringUtils.isEmpty(type)) {
			return TicketType.DINE_IN;
		}

		return TicketType.valueOf(type);
	}

	public void setType(TicketType type) {
		setTicketType(type.name());
	}

	public boolean isPriceIncludesTax() {
		return priceIncludesTax;
	}

	public void setPriceIncludesTax(boolean priceIncludesTax) {
		this.priceIncludesTax = priceIncludesTax;
	}

	/*public boolean isCashPayment() {
		return cashPayment;
	}

	public void setCashPayment(boolean cashPayment) {
		this.cashPayment = cashPayment;
	}*/

	public void addProperty(String name, String value) {
		if (getProperties() == null) {
			setProperties(new HashMap<String, String>());
		}

		getProperties().put(name, value);
	}

	public boolean hasProperty(String key) {
		return getProperty(key) != null;
	}

	public String getProperty(String key) {
		if (getProperties() == null) {
			return null;
		}

		return getProperties().get(key);
	}

	public String getProperty(String key, String defaultValue) {
		if (getProperties() == null) {
			return null;
		}

		String string = getProperties().get(key);
		if(StringUtils.isEmpty(string)) {
			return defaultValue;
		}

		return string;
	}

	public void removeProperty(String propertyName) {
		Map<String, String> properties = getProperties();
		if (properties == null) {
			return;
		}

		properties.remove(propertyName);
	}

	public boolean isPropertyValueTrue(String propertyName) {
		String property = getProperty(propertyName);

		return POSUtil.getBoolean(property);
	}

	public String toURLForm() {
		String s = "ticket_id=" + getId();

		List<TicketItem> items = getTicketItems();
		if(items == null || items.size() == 0) {
			return s;
		}

		for (int i = 0; i < items.size(); i++) {
			TicketItem ticketItem = items.get(i);
			s += "&items[" + i + "][id]=" + ticketItem.getId();
			s += "&items[" + i + "][name]=" + POSUtil.encodeURLString(ticketItem.getName());
			s += "&items[" + i + "][price]=" + ticketItem.getSubtotalAmount();
		}

		s+= "&tax=" + getTaxAmount();
		s+= "&subtotal=" + getSubtotalAmount();
		s+= "&grandtotal=" + getTotalAmount();

		return s;
	}

	public void setCustomer(Customer customer) {		
		addProperty(Ticket.CUSTOMER_ID, String.valueOf(customer.getAutoId()));
		addProperty(Ticket.CUSTOMER_NAME, customer.getName());
		addProperty(Ticket.CUSTOMER_PHONE, customer.getTelephoneNo());
		addProperty(Ticket.CUSTOMER_PHONE2, customer.getTelephoneNo2());
		addProperty(Ticket.CUSTOMER_LANDMARK, customer.getLandMark());
		addProperty(Ticket.CUSTOMER_POSTCODE, customer.getZipCode());
		addProperty(Ticket.CUSTOMER_BELLNAME, customer.getBellName());
		addProperty(Ticket.CUSTOMER_CITYNAME, customer.getCity());
		addProperty(Ticket.CUSTOMER_FIRMNAME, customer.getFirmName());
		addProperty(Ticket.CUSTOMER_ADDRESS, customer.getAddress());
		addProperty(Ticket.CUSTOMER_DOOR, customer.getDoorNo());
		addProperty(Ticket.CUSTOMER_SALUTATION, customer.getSalutation());
		addProperty(Ticket.CUSTOMER_LOYALTY_NO, customer.getLoyaltyNo());
		setLoyaltyNo(customer.getLoyaltyNo());
	}


	public void removeCustomer() {
		removeProperty(CUSTOMER_ID);
		removeProperty(CUSTOMER_NAME);
		removeProperty(CUSTOMER_PHONE);
		removeProperty(CUSTOMER_PHONE2);
		removeProperty(CUSTOMER_LANDMARK);
		removeProperty(CUSTOMER_BELLNAME);
		removeProperty(CUSTOMER_POSTCODE);
		removeProperty(CUSTOMER_CITYNAME);
		removeProperty(CUSTOMER_FIRMNAME);
		removeProperty(CUSTOMER_ADDRESS);
		removeProperty(CUSTOMER_SALUTATION);
		removeProperty(CUSTOMER_LOYALTY_NO);
		removeProperty(CUSTOMER_DOOR);
		setLoyaltyNo("0");

	}

}