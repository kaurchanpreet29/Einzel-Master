package com.floreantpos.model.base;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This is an object that contains data related to the TICKET table. Do not
 * modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class table="TICKET"
 */

public abstract class BaseTicket implements Comparable, Serializable {
	public static String REF = "Ticket";
	public static String PROP_RE_OPENED = "reOpened";
	public static String PROP_VOID_REASON = "voidReason";
	public static String PROP_DUE_AMOUNT = "dueAmount";
	public static String PROP_DISCOUNT_AMOUNT = "discountAmount";
	public static String PROP_CREATE_DATE = "createDate";
	public static String PROP_NUMBER_OF_GUESTS = "numberOfGuests";
	public static String PROP_DELIVERY_CHARGE = "deliveryCharge";
	public static String PROP_PAID = "paid";
	public static String PROP_ADVANCE_AMOUNT = "advanceAmount";
	public static String PROP_ACTIVE_DATE = "activeDate";
	public static String PROP_ASSIGNED_DRIVER = "assignedDriver";
	public static String PROP_CREATION_HOUR = "creationHour";
	public static String PROP_CUSTOMER_WILL_PICKUP = "customerWillPickup";
	public static String PROP_DRAWER_RESETTED = "drawerResetted";
	public static String PROP_OWNER = "owner";
	public static String PROP_DELIVERY_DATE = "deliveryDate";
	public static String PROP_GRATUITY = "gratuity";
	public static String PROP_TERMINAL = "terminal";
	public static String PROP_CLOSED = "closed";
	public static String PROP_TABLENUMBER = "tables";
	 
	public static String PROP_READY_BON_PRINT = "readybonprint";
	public static String PROP_TAB_PRINT = "tabprint";
	public static String PROP_CLOSING_DATE = "closingDate";
	public static String PROP_DELIVERY_ADDRESS = "deliveryAddress";
	public static String PROP_SHIFT = "shift";
	public static String PROP_TAX_AMOUNT = "taxAmount";
	public static String PROP_REFUNDED = "refunded";
	public static String PROP_STATUS = "status";
	public static String PROP_SUBTOTAL_AMOUNT = "subtotalAmount";
	public static String PROP_VOIDED_BY = "voidedBy";
	public static String PROP_TICKET_TYPE = "ticketType";
	public static String PROP_TAX_EXEMPT = "taxExempt";
	public static String PROP_ID = "id";
	public static String PROP_WASTED = "wasted";
	public static String PROP_VOIDED = "voided";
	public static String PROP_TOTAL_AMOUNT = "totalAmount";
	public static String PROP_PAID_AMOUNT = "paidAmount";
	public static String PROP_EXTRA_DELIVERY_INFO = "extraDeliveryInfo";
	public static String PROP_SERVICE_CHARGE = "serviceCharge";
	public static String PROP_DELIVERY_COST = "deliveryCost";
	public static String PROP_DELIVERY_TIME = "deliveryTime";
	public static String PROP_MIN_ORDER = "minOrder";
	public static String PROP_SALES_ID = "salesid";
	public static String PROP_KUNDEN_LOYALTY_NO = "loyaltyNo";
	public static String PROP_RECHNUG_PAID = "rechnugpaid";
	public static String PROP_TICKET_ID = "ticketid";
    public static String PROP_TSE_RECEIPT_TX_ID="tseReceiptTxId";
    public static String PROP_CASH_PAYMENT="cashPayment";
	

	// constructors
	public BaseTicket() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseTicket(java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	java.util.Date modifiedTime;

	protected java.lang.Double tipAmount;	
	// fields
	protected java.lang.Boolean gutscheinPayment;
	protected java.lang.Boolean soldGutschein;
	protected java.util.Date createDate;
	protected java.util.Date closingDate;
	protected java.util.Date activeDate;
	protected java.util.Date deliveryDate;
	protected java.lang.Integer creationHour;
	protected java.lang.Boolean paid;
	protected java.lang.Boolean voided;
	protected java.lang.String voidReason;
	protected java.lang.Boolean wasted;
	protected java.lang.Boolean refunded;
	protected java.lang.Boolean closed;
	protected java.lang.Boolean drawerResetted;
	protected java.lang.Boolean gutscheinHausbon;
	protected java.lang.Double subtotalAmount;
	protected java.lang.Double discountAmount;
	protected java.lang.Double pfand;
	protected java.lang.Double pfand2;
	protected java.lang.Double pfand3;
	protected java.lang.Double deliveryCost;
	protected java.lang.Boolean printed;
	protected java.lang.Double minOrder;
	protected java.lang.Double taxAmount;
	protected java.lang.Double totalAmount;
	protected java.lang.Double paidAmount;
	protected java.lang.Double dueAmount;
	protected java.lang.Double advanceAmount;
	protected java.lang.Integer numberOfGuests;
	protected java.lang.String status;
	protected java.lang.String deliveryTime;
	protected java.lang.Boolean taxExempt;
	@JsonIgnore
	protected java.lang.Boolean changesAvailable;
	protected java.lang.Boolean reOpened;
	protected java.lang.Double serviceCharge;
	protected java.lang.Double deliveryCharge;
	protected java.lang.String deliveryAddress;
	protected java.lang.Boolean customerWillPickup;
	private java.lang.Integer gdpduid;
	protected java.lang.String extraDeliveryInfo;
	protected java.lang.String ticketType;
	protected java.lang.Boolean cashPayment;
	protected java.lang.Boolean onlinePayment;
	protected java.lang.String barcode;
	protected java.lang.Boolean readybonprint;
	protected java.lang.Boolean tabprint;
	protected java.lang.Boolean split;
	protected java.lang.Integer cardpaymenttype;
	protected java.lang.Double cardAmount;
	protected java.lang.Boolean splitPayment;
	protected java.lang.Boolean retour;
	protected java.lang.Integer salesid;
	protected java.lang.Integer ticketid;
	protected java.lang.String loyaltyNo;
	protected java.lang.Double imHausAmnt;
	protected java.lang.Double ausHausAmnt;
	protected java.lang.Double imHausTax;
	protected java.lang.Double ausHausTax;
	protected java.lang.Double bevAmnt;
	protected java.lang.Double zeroAmnt;
	protected java.lang.Boolean rechnugpaid;
	protected java.lang.Boolean rechnugPayemnt;

	private java.lang.Integer deletedItemsCount;
	private java.lang.Double deletedItemsAmount;
	// many to one
	private com.floreantpos.model.Shift shift;
	private com.floreantpos.model.User owner;
	private com.floreantpos.model.User assignedDriver;
	private com.floreantpos.model.Gratuity gratuity;
	private com.floreantpos.model.User voidedBy;
	private com.floreantpos.model.Terminal terminal;

	// collections
	private java.util.Map<String, String> properties;
	private java.util.List<com.floreantpos.model.TicketItem> ticketItems;
	private java.util.List<com.floreantpos.model.TicketCouponAndDiscount> couponAndDiscounts;
	private java.util.Set<com.floreantpos.model.PosTransaction> transactions;
	private java.util.Set<com.floreantpos.model.ShopTable> tables;

	@JsonIgnore
	protected java.lang.Double gutschrift;
	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="identity" column="ID"
	 */
	public java.lang.Integer getId() {
		return id;
	}

	public java.lang.Boolean getPrinted() {
		return printed != null ? printed : false;
	}

	public void setPrinted(java.lang.Boolean printed) {
		this.printed = printed;
	}		
	
	/**
	 * Set the unique identifier of this class
	 * 
	 * @param id
	 *          the new ID
	 */
	public void setId(java.lang.Integer id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Return the value associated with the column: MODIFIED_TIME
	 */
	public java.util.Date getModifiedTime() {
		return modifiedTime;
	}

	public java.lang.Boolean getGutscheinHausbon() {
		return gutscheinHausbon;
	}

	public void setGutscheinHausbon(java.lang.Boolean gutscheinHausbon) {
		this.gutscheinHausbon = gutscheinHausbon;
	}	
	
	/**
	 * Set the value related to the column: MODIFIED_TIME
	 * 
	 * @param modifiedTime
	 *          the MODIFIED_TIME value
	 */
	public void setModifiedTime(java.util.Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	/**
	 * Return the value associated with the column: CREATE_DATE
	 */
	public java.util.Date getCreateDate() {
		return createDate;
	}

	/**
	 * Set the value related to the column: CREATE_DATE
	 * 
	 * @param createDate
	 *          the CREATE_DATE value
	 */
	public void setCreateDate(java.util.Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * Return the value associated with the column: CLOSING_DATE
	 */
	public java.util.Date getClosingDate() {
		return closingDate;
	}

	/**
	 * Set the value related to the column: CLOSING_DATE
	 * 
	 * @param closingDate
	 *          the CLOSING_DATE value
	 */
	public void setClosingDate(java.util.Date closingDate) {
		this.closingDate = closingDate;
	}

	/**
	 * Return the value associated with the column: ACTIVE_DATE
	 */
	public java.util.Date getActiveDate() {
		return activeDate;
	}

	/**
	 * Set the value related to the column: ACTIVE_DATE
	 * 
	 * @param activeDate
	 *          the ACTIVE_DATE value
	 */
	public void setActiveDate(java.util.Date activeDate) {
		this.activeDate = activeDate;
	}

	/**
	 * Return the value associated with the column: DELIVEERY_DATE
	 */
	public java.util.Date getDeliveryDate() {
		return deliveryDate;
	}

	/**
	 * Set the value related to the column: DELIVEERY_DATE
	 * 
	 * @param deliveryDate
	 *          the DELIVEERY_DATE value
	 */
	public void setDeliveryDate(java.util.Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	/**
	 * Return the value associated with the column: CREATION_HOUR
	 */
	public java.lang.Integer getCreationHour() {
		return creationHour == null ? Integer.valueOf(0) : creationHour;
	}

	/**
	 * Set the value related to the column: CREATION_HOUR
	 * 
	 * @param creationHour
	 *          the CREATION_HOUR value
	 */
	public void setCreationHour(java.lang.Integer creationHour) {
		this.creationHour = creationHour;
	}

	/**
	 * Return the value associated with the column: PAID
	 */
	public java.lang.Boolean isPaid() {
		return paid == null ? Boolean.FALSE : paid;
	}

	/**
	 * Set the value related to the column: PAID
	 * 
	 * @param paid
	 *          the PAID value
	 */
	public void setPaid(java.lang.Boolean paid) {
		this.paid = paid;
	}

	/**
	 * Return the value associated with the column: VOIDED
	 */
	public java.lang.Boolean isVoided() {
		return voided == null ? Boolean.FALSE : voided;
	}

	/**
	 * Set the value related to the column: VOIDED
	 * 
	 * @param voided
	 *          the VOIDED value
	 */
	public void setVoided(java.lang.Boolean voided) {
		this.voided = voided;
	}

	/**
	 * Return the value associated with the column: VOID_REASON
	 */
	public java.lang.String getVoidReason() {
		return voidReason;
	}

	/**
	 * Set the value related to the column: VOID_REASON
	 * 
	 * @param voidReason
	 *          the VOID_REASON value
	 */
	public void setVoidReason(java.lang.String voidReason) {
		this.voidReason = voidReason;
	}

	/**
	 * Return the value associated with the column: WASTED
	 */
	public java.lang.Boolean isWasted() {
		return wasted == null ? Boolean.FALSE : wasted;
	}

	/**
	 * Set the value related to the column: WASTED
	 * 
	 * @param wasted
	 *          the WASTED value
	 */
	public void setWasted(java.lang.Boolean wasted) {
		this.wasted = wasted;
	}

	/**
	 * Return the value associated with the column: REFUNDED
	 */
	public java.lang.Boolean isRefunded() {
		return refunded == null ? Boolean.FALSE : refunded;
	}

	/**
	 * Set the value related to the column: REFUNDED
	 * 
	 * @param refunded
	 *          the REFUNDED value
	 */
	public void setRefunded(java.lang.Boolean refunded) {
		this.refunded = refunded;
	}

	/**
	 * Return the value associated with the column: SETTLED
	 */
	public java.lang.Boolean isClosed() {
		return closed == null ? Boolean.FALSE : closed;
	}

	/**
	 * Set the value related to the column: SETTLED
	 * 
	 * @param closed
	 *          the SETTLED value
	 */
	public void setClosed(java.lang.Boolean closed) {
		this.closed = closed;
	}

	/**
	 * Return the value associated with the column: DRAWER_RESETTED
	 */
	public java.lang.Boolean isDrawerResetted() {
		return drawerResetted == null ? Boolean.FALSE : drawerResetted;
	}

	/**
	 * Set the value related to the column: DRAWER_RESETTED
	 * 
	 * @param drawerResetted
	 *          the DRAWER_RESETTED value
	 */
	public void setDrawerResetted(java.lang.Boolean drawerResetted) {
		this.drawerResetted = drawerResetted;
	}

	/**
	 * Return the value associated with the column: SUB_TOTAL
	 */
	public java.lang.Double getSubtotalAmount() {
		return subtotalAmount == null ? Double.valueOf(0) : subtotalAmount;
	}

	/**
	 * Set the value related to the column: SUB_TOTAL
	 * 
	 * @param subtotalAmount
	 *          the SUB_TOTAL value
	 */
	public void setSubtotalAmount(java.lang.Double subtotalAmount) {
		this.subtotalAmount = subtotalAmount;
	}
	
	/**
	 * Return the value associated with the column: GUTSCHRIFT
	 */
	public java.lang.Double getGutschrift() {
		return gutschrift == null ? Double.valueOf(0) : gutschrift;
	}
	/**
	 * Set the value related to the column: GUTSCHRIFT
	 * 
	 * @param subtotalAmount
	 *          the GUTSCHRIFT value
	 */
	public void setGutschrift(java.lang.Double gutschrift) {
		this.gutschrift = gutschrift;
	}
	
	public java.lang.Double getTipAmount() {
		return tipAmount!=null?tipAmount:0.0;
	}

	public void setTipAmount(java.lang.Double tipAmount) {
		this.tipAmount = tipAmount;
	}
	

	public java.lang.Integer getGdpduid() {
		return gdpduid != null ? gdpduid : 0;
	}

	public void setGdpduid(java.lang.Integer gdpduid) {
		this.gdpduid = gdpduid;
	}

	/**
	 * Return the value associated with the column: TOTAL_DISCOUNT
	 */
	public java.lang.Double getDiscountAmount() {
		return discountAmount == null ? Double.valueOf(0) : discountAmount;
	}

	/**
	 * Set the value related to the column: TOTAL_DISCOUNT
	 * 
	 * @param discountAmount
	 *          the TOTAL_DISCOUNT value
	 */
	public void setDiscountAmount(java.lang.Double discountAmount) {
		this.discountAmount = discountAmount;
	}

	/**
	 * Return the value associated with the column: TOTAL_TAX
	 */
	public java.lang.Double getTaxAmount() {
		return taxAmount == null ? Double.valueOf(0) : taxAmount;
	}

	/**
	 * Set the value related to the column: TOTAL_TAX
	 * 
	 * @param taxAmount
	 *          the TOTAL_TAX value
	 */
	public void setTaxAmount(java.lang.Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	/**
	 * Return the value associated with the column: TOTAL_PRICE
	 */
	public java.lang.Double getTotalAmount() {
		return totalAmount == null ? Double.valueOf(0) : totalAmount;
	}

	public java.lang.Double getTotalAmountWithoutPfand() {
		return totalAmount == null ? Double.valueOf(0) : totalAmount - getPfand() - getPfand2() - getPfand3();
	}

	/**
	 * Set the value related to the column: TOTAL_PRICE
	 * 
	 * @param totalAmount
	 *          the TOTAL_PRICE value
	 */
	public void setTotalAmount(java.lang.Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	/**
	 * Return the value associated with the column: PAID_AMOUNT
	 */
	public java.lang.Double getPaidAmount() {
		return paidAmount == null ? Double.valueOf(0) : paidAmount;
	}

	/**
	 * Set the value related to the column: PAID_AMOUNT
	 * 
	 * @param paidAmount
	 *          the PAID_AMOUNT value
	 */
	public void setPaidAmount(java.lang.Double paidAmount) {
		this.paidAmount = paidAmount;
	}

	/**
	 * Return the value associated with the column: DUE_AMOUNT
	 */
	public java.lang.Double getDueAmount() {
		return dueAmount == null ? Double.valueOf(0) : dueAmount;
	}

	/**
	 * Set the value related to the column: DUE_AMOUNT
	 * 
	 * @param dueAmount
	 *          the DUE_AMOUNT value
	 */
	public void setDueAmount(java.lang.Double dueAmount) {
		this.dueAmount = dueAmount;
	}

	/**
	 * Return the value associated with the column: ADVANCE_AMOUNT
	 */
	public java.lang.Double getAdvanceAmount() {
		return advanceAmount == null ? Double.valueOf(0) : advanceAmount;
	}

	/**
	 * Set the value related to the column: ADVANCE_AMOUNT
	 * 
	 * @param advanceAmount
	 *          the ADVANCE_AMOUNT value
	 */
	public void setAdvanceAmount(java.lang.Double advanceAmount) {
		this.advanceAmount = advanceAmount;
	}

	/**
	 * Return the value associated with the column: NUMBER_OF_GUESTS
	 */
	public java.lang.Integer getNumberOfGuests() {
		return numberOfGuests == null ? Integer.valueOf(0) : numberOfGuests;
	}

	/**
	 * Set the value related to the column: NUMBER_OF_GUESTS
	 * 
	 * @param numberOfGuests
	 *          the NUMBER_OF_GUESTS value
	 */
	public void setNumberOfGuests(java.lang.Integer numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}

	/**
	 * Return the value associated with the column: CARDPAYMENTTYPE
	 */
	public java.lang.Integer getCardpaymenttype() {
		return cardpaymenttype;
	}

	/**
	 * Set the value related to the column: CARDPAYMENTTYPE
	 * 
	 * @param cardpaymenttype
	 *          the CARDPAYMENTTYPE value
	 */
	public void setCardpaymenttype(java.lang.Integer cardpaymenttype) {
		this.cardpaymenttype = cardpaymenttype;
	}

	/**
	 * Return the value associated with the column: STATUS
	 */
	public java.lang.String getStatus() {
		return status;
	}

	/**
	 * Set the value related to the column: STATUS
	 * 
	 * @param status
	 *          the STATUS value
	 */
	public void setStatus(java.lang.String status) {
		this.status = status;
	}

	/**
	 * Return the value associated with the column: IS_TAX_EXEMPT
	 */
	public java.lang.Boolean isTaxExempt() {
		return taxExempt == null ? Boolean.FALSE : taxExempt;
	}

	/**
	 * Set the value related to the column: IS_TAX_EXEMPT
	 * 
	 * @param taxExempt
	 *          the IS_TAX_EXEMPT value
	 */
	public void setTaxExempt(java.lang.Boolean taxExempt) {
		this.taxExempt = taxExempt;
	}

	public java.lang.Boolean isChangesAvailable() {
		return changesAvailable == null ? Boolean.FALSE : changesAvailable;
	}

	public void setChangesAvailable(java.lang.Boolean changesAvailable) {
		this.changesAvailable = changesAvailable;
	}
	
	/**
	 * Return the value associated with the column: IS_RE_OPENED
	 */
	public java.lang.Boolean isReOpened() {
		return reOpened == null ? Boolean.FALSE : reOpened;
	}

	/**
	 * Set the value related to the column: IS_RE_OPENED
	 * 
	 * @param reOpened
	 *          the IS_RE_OPENED value
	 */
	public void setReOpened(java.lang.Boolean reOpened) {
		this.reOpened = reOpened;
	}

	/**
	 * Return the value associated with the column: SERVICE_CHARGE
	 */
	public java.lang.Double getServiceCharge() {
		return serviceCharge == null ? Double.valueOf(0) : serviceCharge;
	}

	/**
	 * Set the value related to the column: SERVICE_CHARGE
	 * 
	 * @param serviceCharge
	 *          the SERVICE_CHARGE value
	 */
	public void setServiceCharge(java.lang.Double serviceCharge) {
		this.serviceCharge = serviceCharge;
	}

	/**
	 * Return the value associated with the column: DELIVERY_CHARGE
	 */
	public java.lang.Double getDeliveryCharge() {
		return deliveryCharge == null ? Double.valueOf(0) : deliveryCharge;
	}

	/**
	 * Set the value related to the column: DELIVERY_CHARGE
	 * 
	 * @param deliveryCharge
	 *          the DELIVERY_CHARGE value
	 */
	public void setDeliveryCharge(java.lang.Double deliveryCharge) {
		this.deliveryCharge = deliveryCharge;
	}

	/**
	 * Return the value associated with the column: DELIVERY_ADDRESS
	 */
	public java.lang.String getDeliveryAddress() {
		return deliveryAddress;
	}

	/**
	 * Set the value related to the column: DELIVERY_ADDRESS
	 * 
	 * @param deliveryAddress
	 *          the DELIVERY_ADDRESS value
	 */
	public void setDeliveryAddress(java.lang.String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	/**
	 * Return the value associated with the column: CUSTOMER_PICKEUP
	 */
	public java.lang.Boolean isCustomerWillPickup() {
		return customerWillPickup == null ? Boolean.FALSE : customerWillPickup;
	}

	/**
	 * Set the value related to the column: CUSTOMER_PICKEUP
	 * 
	 * @param customerWillPickup
	 *          the CUSTOMER_PICKEUP value
	 */
	public void setCustomerWillPickup(java.lang.Boolean customerWillPickup) {
		this.customerWillPickup = customerWillPickup;
	}

	/**
	 * Return the value associated with the column: DELIVERY_EXTRA_INFO
	 */
	public java.lang.String getExtraDeliveryInfo() {
		return extraDeliveryInfo;
	}

	/**
	 * Set the value related to the column: DELIVERY_EXTRA_INFO
	 * 
	 * @param extraDeliveryInfo
	 *          the DELIVERY_EXTRA_INFO value
	 */
	public void setExtraDeliveryInfo(java.lang.String extraDeliveryInfo) {
		this.extraDeliveryInfo = extraDeliveryInfo;
	}

	/**
	 * Return the value associated with the column: MIN_ORDER
	 */
	public java.lang.Double getMinOrder() {
		return minOrder;
	}

	/**
	 * Set the value related to the column: MIN_ORDER
	 * 
	 * @param minOrder
	 *          the MIN_ORDER value
	 */
	public void setMinOrder(java.lang.Double minOrder) {
		this.minOrder = minOrder;
	}

	/**
	 * Return the value associated with the column: DELIVERY_COST
	 */
	public java.lang.Double getDeliveryCost() {
		return deliveryCost;
	}

	/**
	 * Set the value related to the column: DELIVERY_COST
	 * 
	 * @param deliveryCost
	 *          the DELIVERY_COST value
	 */
	public void setDeliveryCost(java.lang.Double deliveryCost) {
		this.deliveryCost = deliveryCost;
	}

	/**
	 * Return the value associated with the column: DEVLIERY_TIME
	 */
	public java.lang.String getDeliveryTime() {
		return deliveryTime;
	}

	/**
	 * Set the value related to the column: DELIVERY_TIME
	 * 
	 * @param deliveryTime
	 *          the DELIVERY_TIME value
	 */
	public void setDeliveryTime(java.lang.String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	/**
	 * Return the value associated with the column: TICKET_TYPE
	 */
	public java.lang.String getTicketType() {
		return ticketType;
	}

	/**
	 * Set the value related to the column: TICKET_TYPE
	 * 
	 * @param ticketType
	 *          the TICKET_TYPE value
	 */
	public void setTicketType(java.lang.String ticketType) {
		this.ticketType = ticketType;
	}

	/**
	 * Return the value associated with the column: CASH PAYMENT TYPE
	 */
	public java.lang.Boolean getCashPayment() {
		return cashPayment;
	}

	/**
	 * Set the value related to the column: TICKET_CASHPAYMENT
	 * 
	 * @param ticketCashPayment
	 *          the TICKET_CASHPAYMENT value
	 */
	public void setCashPayment(java.lang.Boolean cashPayment) {
		this.cashPayment = cashPayment;
	}

	/**
	 * Return the value associated with the column: ONLINE PAYMENT TYPE
	 */
	public java.lang.Boolean getOnlinePayment() {
		return onlinePayment!=null?onlinePayment:false;
	}

	/**
	 * Set the value related to the column: TICKET_ONLINEPAYMENT
	 * 
	 * @param ticketCashPayment
	 *          the TICKET_ONLINEPAYMENT value
	 */
	public void setOnlinePayment(java.lang.Boolean onlinePayment) {
		this.onlinePayment = onlinePayment;
	}

	/**
	 * Return the value associated with the column: READY BON PRINT
	 */
	public java.lang.Boolean getReadybonprint() {
		return readybonprint;
	}

	public java.lang.Boolean getSoldGutschein() {
		return soldGutschein != null ? this.soldGutschein : false;
	}

	public void setSoldGutschein(java.lang.Boolean soldGutschein) {
		this.soldGutschein = soldGutschein;
	}
	
	/**
	 * Get GutSchein Payment
	 * @return
	 */
	public java.lang.Boolean getGutscheinPayment() {
		return gutscheinPayment;
	}
	/**
	 *set GutSchein Payment
	 * 
	 */
	public void setGutscheinPayment(java.lang.Boolean gutscheinPayment) {
		this.gutscheinPayment = gutscheinPayment;
	}
	/**
	 * Set the value related to the column: READY BON PRINT
	 * 
	 * @param readyTabPrint
	 *          the READY BON PRINT value
	 */
	public void setReadybonprint(java.lang.Boolean readybonprint) {
		this.readybonprint = readybonprint;
	}

	/**
	 * Return the value associated with the column: TAB PRINT
	 */
	public java.lang.Boolean getTabprint() {
		return tabprint;
	}

	/**
	 * Set the value related to the column: TAB PRINT
	 * 
	 * @param readyTabPrint
	 *          the TAB PRINT value
	 */
	public void setTabprint(java.lang.Boolean tabprint) {
		this.tabprint = tabprint;
	}

	/**
	 * Return the value associated with the column: SPLIT
	 */
	public java.lang.Boolean getSplit() {
		return split;
	}

	/**
	 * Set the value related to the column: SPLIT
	 * 
	 * @param readyTabPrint
	 *          the SPLIT value
	 */
	public void setSplit(java.lang.Boolean split) {
		this.split = split;
	}

	/**
	 * Return the value associated with the column: SHIFT_ID
	 */
	public com.floreantpos.model.Shift getShift() {
		return shift;
	}

	/**
	 * Set the value related to the column: SHIFT_ID
	 * 
	 * @param shift
	 *          the SHIFT_ID value
	 */
	public void setShift(com.floreantpos.model.Shift shift) {
		this.shift = shift;
	}

	/**
	 * Return the value associated with the column: OWNER_ID
	 */
	public com.floreantpos.model.User getOwner() {
		return owner;
	}

	/**
	 * Set the value related to the column: OWNER_ID
	 * 
	 * @param owner
	 *          the OWNER_ID value
	 */
	public void setOwner(com.floreantpos.model.User owner) {
		this.owner = owner;
	}


	/**
	 * Return the value associated with the column: DRIVER_ID
	 */
	public com.floreantpos.model.User getAssignedDriver() {
		return assignedDriver;
	}

	/**
	 * Set the value related to the column: DRIVER_ID
	 * 
	 * @param assignedDriver
	 *          the DRIVER_ID value
	 */
	public void setAssignedDriver(com.floreantpos.model.User assignedDriver) {
		this.assignedDriver = assignedDriver;
	}

	/**
	 * Return the value associated with the column: GRATUITY_ID
	 */
	public com.floreantpos.model.Gratuity getGratuity() {
		return gratuity;
	}

	/**
	 * Set the value related to the column: GRATUITY_ID
	 * 
	 * @param gratuity
	 *          the GRATUITY_ID value
	 */
	public void setGratuity(com.floreantpos.model.Gratuity gratuity) {
		this.gratuity = gratuity;
	}

	/**
	 * Return the value associated with the column: VOID_BY_USER
	 */
	public com.floreantpos.model.User getVoidedBy() {
		return voidedBy;
	}

	/**
	 * Set the value related to the column: VOID_BY_USER
	 * 
	 * @param voidedBy
	 *          the VOID_BY_USER value
	 */
	public void setVoidedBy(com.floreantpos.model.User voidedBy) {
		this.voidedBy = voidedBy;
	}

	/**
	 * Return the value associated with the column: TERMINAL_ID
	 */
	public com.floreantpos.model.Terminal getTerminal() {
		return terminal;
	}

	/**
	 * Set the value related to the column: TERMINAL_ID
	 * 
	 * @param terminal
	 *          the TERMINAL_ID value
	 */
	public void setTerminal(com.floreantpos.model.Terminal terminal) {
		this.terminal = terminal;
	}

	/**
	 * Return the value associated with the column: properties
	 */
	public java.util.Map<String, String> getProperties() {
		return properties;
	}

	/**
	 * Set the value related to the column: properties
	 * 
	 * @param properties
	 *          the properties value
	 */
	public void setProperties(java.util.Map<String, String> properties) {
		this.properties = properties;
	}

	public java.lang.Double getPfand() {
		return pfand != null ? pfand : 0.00;
	}

	public void setPfand(java.lang.Double pfand) {
		this.pfand = pfand;
	}

	public java.lang.Double getPfand2() {
		return pfand2 != null ? pfand2 : 0.00;
	}

	public void setPfand2(java.lang.Double pfand2) {
		this.pfand2 = pfand2;
	}

	public java.lang.Double getPfand3() {
		return pfand3 != null ? pfand3 : 0.00;
	}

	public void setPfand3(java.lang.Double pfand3) {
		this.pfand3 = pfand3;
	}

	/**
	 * Return the value associated with the column: ticketItems
	 */
	public java.util.List<com.floreantpos.model.TicketItem> getTicketItems() {
		return ticketItems;
	}

	/**
	 * Set the value related to the column: ticketItems
	 * 
	 * @param ticketItems
	 *          the ticketItems value
	 */
	public void setTicketItems(
			java.util.List<com.floreantpos.model.TicketItem> ticketItems) {
		this.ticketItems = ticketItems;
	}

	public void addToticketItems(com.floreantpos.model.TicketItem ticketItem) {
		if (null == getTicketItems())
			setTicketItems(new java.util.ArrayList<com.floreantpos.model.TicketItem>());
		getTicketItems().add(ticketItem);
	}

	/**
	 * Return the value associated with the column: couponAndDiscounts
	 */
	public java.util.List<com.floreantpos.model.TicketCouponAndDiscount> getCouponAndDiscounts() {
		return couponAndDiscounts;
	}

	/**
	 * Set the value related to the column: couponAndDiscounts
	 * 
	 * @param couponAndDiscounts
	 *          the couponAndDiscounts value
	 */
	public void setCouponAndDiscounts(
			java.util.List<com.floreantpos.model.TicketCouponAndDiscount> couponAndDiscounts) {
		this.couponAndDiscounts = couponAndDiscounts;
	}

	public void addTocouponAndDiscounts(
			com.floreantpos.model.TicketCouponAndDiscount ticketCouponAndDiscount) {
		if (null == getCouponAndDiscounts())
			setCouponAndDiscounts(new java.util.ArrayList<com.floreantpos.model.TicketCouponAndDiscount>());
		getCouponAndDiscounts().add(ticketCouponAndDiscount);
	}

	/**
	 * Return the value associated with the column: transactions
	 */
	public java.util.Set<com.floreantpos.model.PosTransaction> getTransactions() {
		return transactions;
	}

	/**
	 * Set the value related to the column: transactions
	 * 
	 * @param transactions
	 *          the transactions value
	 */
	public void setTransactions(
			java.util.Set<com.floreantpos.model.PosTransaction> transactions) {
		this.transactions = transactions;
	}

	public void addTotransactions(
			com.floreantpos.model.PosTransaction posTransaction) {
		if (null == getTransactions())
			setTransactions(new java.util.TreeSet<com.floreantpos.model.PosTransaction>());
		getTransactions().add(posTransaction);
	}

	/**
	 * Return the value associated with the column: tables
	 */
	public java.util.Set<com.floreantpos.model.ShopTable> getTables() {
		return tables;
	}

	/**
	 * Set the value related to the column: tables
	 * 
	 * @param tables
	 *          the tables value
	 */

	public java.lang.Double getCardAmount() {
		return cardAmount;
	}

	public void setCardAmount(java.lang.Double cardAmount) {
		this.cardAmount = cardAmount;
	}

	public java.lang.Boolean getSplitPayment() {
		return splitPayment;
	}

	public void setSplitPayment(java.lang.Boolean splitPayment) {
		this.splitPayment = splitPayment;
	}


	public java.lang.Boolean getRetour() {
		return retour;
	}

	public void setRetour(java.lang.Boolean retour) {
		this.retour = retour;
	}

	public java.lang.Integer getSalesid() {
		return salesid;
	}

	public void setSalesid(java.lang.Integer salesid) {
		this.salesid = salesid;
	}

	public java.lang.Integer getTicketid() {
		return ticketid;
	}

	public void setTicketid(java.lang.Integer ticketid) {
		this.ticketid = ticketid;
	}

	public java.lang.String getLoyaltyNo() {
		return loyaltyNo;
	}

	public void setLoyaltyNo(java.lang.String loyaltyNo) {
		this.loyaltyNo = loyaltyNo;
	}
	public java.lang.Double getImHausAmnt() {
		return imHausAmnt!=null?imHausAmnt:0.0;
	}

	public void setImHausAmnt(java.lang.Double imHausAmnt) {
		this.imHausAmnt = imHausAmnt;
	}

	public java.lang.Double getAusHausAmnt() {
		return ausHausAmnt!=null?ausHausAmnt:0.0;
	}

	public void setAusHausAmnt(java.lang.Double ausHausAmnt) {
		this.ausHausAmnt = ausHausAmnt;
	}

	public java.lang.Double getImHausTax() {
		return imHausTax!=null?imHausTax:0.0;
	}

	public void setImHausTax(java.lang.Double imHausTax) {
		this.imHausTax = imHausTax;
	}

	public java.lang.Double getAusHausTax() {
		return ausHausTax!=null?ausHausTax:0.0;
	}

	public void setAusHausTax(java.lang.Double ausHausTax) {
		this.ausHausTax = ausHausTax;
	}
	public java.lang.Double getBevAmnt() {
		return bevAmnt!=null?bevAmnt:0.0;
	}

	public void setBevAmnt(java.lang.Double bevAmnt) {
		this.bevAmnt = bevAmnt;
	}

	public java.lang.Double getZeroAmnt() {
		return zeroAmnt!=null?zeroAmnt:0.0;
	}

	public void setZeroAmnt(java.lang.Double zeroAmnt) {
		this.zeroAmnt = zeroAmnt;
	}

	
	public java.lang.Boolean isRechnugpaid() {
		return rechnugpaid!=null?rechnugpaid:false;
	}

	public void setRechnugpaid(java.lang.Boolean rechnugpaid) {
		this.rechnugpaid = rechnugpaid;
	}
	
	public java.lang.Boolean getRechnugPayemnt() {
		return rechnugPayemnt!=null?rechnugPayemnt:false;
	}

	public void setRechnugPayemnt(java.lang.Boolean rechnugPayemnt) {
		this.rechnugPayemnt = rechnugPayemnt;
	}

	public void setTables(java.util.Set<com.floreantpos.model.ShopTable> tables) {
		this.tables = tables;
	}

	public void addTotables(com.floreantpos.model.ShopTable shopTable) {
		if (null == getTables())
			setTables(new java.util.TreeSet<com.floreantpos.model.ShopTable>());
		getTables().add(shopTable);
	}
	
	/*
	 * TSE PARAMETER
	 */
	protected java.lang.String tseClientId;
	protected java.lang.String tseTxRevisionNr;
	protected java.lang.String tseReceiptTxId;
	protected java.lang.String tseReceiptTxRevisionNr;
	protected java.lang.Integer tseReceiptDataId;

	public java.lang.Integer getTseReceiptDataId() {
		return tseReceiptDataId;
	}

	public void setTseReceiptDataId(java.lang.Integer tseReceiptDataId) {
		this.tseReceiptDataId = tseReceiptDataId;
	}
	
	public java.lang.String getTseReceiptTxId() {
		return tseReceiptTxId;
	}

	public void setTseReceiptTxId(java.lang.String tseReceiptTxId) {
		this.tseReceiptTxId = tseReceiptTxId;
	}

	public java.lang.String getTseReceiptTxRevisionNr() {
		return tseReceiptTxRevisionNr;
	}

	public void setTseReceiptTxRevisionNr(java.lang.String tseReceiptTxRevisionNr) {
		this.tseReceiptTxRevisionNr = tseReceiptTxRevisionNr;
	}

	public java.lang.String getTseTxRevisionNr() {
		return tseTxRevisionNr;
	}

	public void setTseTxRevisionNr(java.lang.String tseTxRevisionNr) {
		this.tseTxRevisionNr = tseTxRevisionNr;
	}
	
	public java.lang.String getTseClientId() {
		return tseClientId;
	}

	public void setTseClientId(java.lang.String tseClientId) {
		this.tseClientId = tseClientId;
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.floreantpos.model.Ticket))
			return false;
		else {
			com.floreantpos.model.Ticket ticket = (com.floreantpos.model.Ticket) obj;
			if (null == this.getId() || null == ticket.getId())
				return false;
			else
				return (this.getId().equals(ticket.getId()));
		}
	}

	@Override
	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId())
				return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":"
						+ this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}

	
	public void addDeletedItemsAmount(final double deletedAmount) {
		if(this.deletedItemsAmount != null) {
			this.deletedItemsAmount += deletedAmount;
		} else {
			this.deletedItemsAmount = deletedAmount;
		}
	}


	public void addDeletedItemsCount() {
		if(this.deletedItemsCount != null) {
			++this.deletedItemsCount;
		} else {
			this.deletedItemsCount = 1;
		}
	}
	
	@Override
	public int compareTo(Object obj) {
		if (obj.hashCode() > hashCode())
			return 1;
		else if (obj.hashCode() < hashCode())
			return -1;
		else
			return 0;
	}

	@Override
	public String toString() {
		return super.toString();
	}

}