package com.floreantpos.ui.views.payment;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.miginfocom.swing.MigLayout;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.floreantpos.IconFactory;
import com.floreantpos.POSConstants;
import com.floreantpos.PosException;
import com.floreantpos.config.AppConfig;
import com.floreantpos.config.CardConfig;
import com.floreantpos.isdnmonitor.CallMon;
import com.floreantpos.main.Application;
import com.floreantpos.model.CallList;
import com.floreantpos.model.CardReader;
import com.floreantpos.model.CashTransaction;
import com.floreantpos.model.CouponAndDiscount;
import com.floreantpos.model.CreditCardTransaction;
import com.floreantpos.model.GiftCertificateTransaction;
import com.floreantpos.model.Gratuity;
import com.floreantpos.model.MerchantGateway;
import com.floreantpos.model.PaymentType;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketCouponAndDiscount;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketType;
import com.floreantpos.model.TransactionType;
import com.floreantpos.model.UserPermission;
import com.floreantpos.model.dao.CallListDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.report.JReportPrintService;
import com.floreantpos.services.PosTransactionService;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.CardDialog;
import com.floreantpos.ui.dialog.CardDialog.CardPaymentType;
import com.floreantpos.ui.dialog.CouponAndDiscountDialog;
import com.floreantpos.ui.dialog.DiscountListDialog;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.dialog.PaymentTypeSelectionDialog;
import com.floreantpos.ui.dialog.TransactionCompletionDialog;
import com.floreantpos.ui.views.SwitchboardView;
import com.floreantpos.ui.views.TicketDetailView;
import com.floreantpos.ui.views.TicketDetailView.PrintType;
import com.floreantpos.ui.views.order.OrderController;
import com.floreantpos.util.POSUtil;

public class SettleTicketDialog extends POSDialog implements CardInputListener,Runnable {
	public static final String LOYALTY_DISCOUNT_PERCENTAGE = "loyalty_discount_percentage";
	public static final String LOYALTY_POINT = "loyalty_point";
	public static final String LOYALTY_COUPON = "loyalty_coupon";
	public static final String LOYALTY_DISCOUNT = "loyalty_discount";
	public static final String LOYALTY_ID = "loyalty_id";
	Thread t;
	public final static String VIEW_NAME = "PAYMENT_VIEW";

	private String previousViewName = SwitchboardView.VIEW_NAME;

	private com.floreantpos.swing.TransparentPanel leftPanel = new com.floreantpos.swing.TransparentPanel(new BorderLayout());
	private com.floreantpos.swing.TransparentPanel rightPanel = new com.floreantpos.swing.TransparentPanel(new BorderLayout());
	
	private TicketDetailView ticketDetailView;
	private PaymentView paymentView; 

	private Ticket ticket; 

	private double tenderAmount;
	private PaymentType paymentType;
	private String cardName;
	
	
	public SettleTicketDialog() {
		super(Application.getPosWindow(), true);
		
		setTitle("Rechnung");

		setLayout(new MigLayout());
		ticketDetailView = new TicketDetailView(this);
		ticketDetailView.setBackground(new Color(209,222,235));
		paymentView = new PaymentView(this);

		leftPanel.add(ticketDetailView);
		
		rightPanel.add(paymentView);
		leftPanel.setBackground(new Color(209,222,235));
		rightPanel.setBackground(new Color(209,222,235));
		getContentPane().add(leftPanel, BorderLayout.CENTER);
		getContentPane().setBackground(new Color(209,222,235));
		getContentPane().add(rightPanel, BorderLayout.EAST);
		
		setPreferredSize(new Dimension(800,600));
	}
	private void updateModel() {
		if (ticket == null) {
			return;
		}
		revalidate();
		pack();
		ticket.calculatePrice();
	}
	public void updateModel(Double value,Boolean deleteCoupon) {
		if (ticket == null) {
			return;
		}
		revalidate();
		pack();
		ticket.calculatePrice(value,deleteCoupon);
	}
	public void doApplyCoupon() {// GEN-FIRST:event_btnApplyCoupondoApplyCoupon
		try {
			if (ticket == null)
				return;
			
			if(!Application.getCurrentUser().hasPermission(UserPermission.ADD_DISCOUNT)) {
				POSMessageDialog.showError("You do not have permission to execute this action");
				return;
			}

			if (ticket.getCouponAndDiscounts() != null && ticket.getCouponAndDiscounts().size() > 0) {
				POSMessageDialog.showError(com.floreantpos.POSConstants.DISCOUNT_COUPON_LIMIT_);
				return;
			}

			CouponAndDiscountDialog dialog = new CouponAndDiscountDialog();
			dialog.setTicket(ticket);
			dialog.initData();
			dialog.open();
			if (!dialog.isCanceled()) {
				TicketCouponAndDiscount coupon = dialog.getSelectedCoupon();
				
				
				coupon.getValue();
				ticket.addTocouponAndDiscounts(coupon);

				if(coupon.getType() == CouponAndDiscount.PERCENTAGE_PER_ITEM)
					updateModel(coupon.getValue(),false);
				else
					updateModel();
				OrderController.saveOrder(ticket);
				ticketDetailView.updateView(false);
				paymentView.updateView();
			}
		} catch (Exception e) {
			POSMessageDialog.showError(this, com.floreantpos.POSConstants.ERROR_MESSAGE, e);
		}
	}// GEN-LAST:event_btnApplyCoupondoApplyCoupon
	public void doTaxExempt(boolean taxExempt) {// GEN-FIRST:event_doTaxExempt
		if (ticket == null)
			return;

		boolean setTaxExempt = taxExempt;
		if (setTaxExempt) {
			int option = JOptionPane.showOptionDialog(this, POSConstants.CONFIRM_SET_TAX_EXEMPT, POSConstants.CONFIRM, JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, null, null);
			if (option != JOptionPane.YES_OPTION) {
				return;
			}

			ticket.setTaxExempt(true);
			ticket.calculatePrice();
			TicketDAO.getInstance().saveOrUpdate(ticket);
		}
		else {
			ticket.setTaxExempt(false);
			ticket.calculatePrice();
			TicketDAO.getInstance().saveOrUpdate(ticket);
		}

		ticketDetailView.updateView(false);
		paymentView.updateView();
	}// GEN-LAST:event_doTaxExempt

	public void doSetGratuity() {
		if (ticket == null)
			return;

		GratuityInputDialog d = new GratuityInputDialog();
		d.setSize(300, 500);
		d.setResizable(false);
		d.open();

		if (d.isCanceled()) {
			return;
		}

		double gratuityAmount = d.getGratuityAmount();
		Gratuity gratuity = ticket.createGratuity();
		gratuity.setAmount(gratuityAmount);

		ticket.setGratuity(gratuity);
		ticket.calculatePrice();
		OrderController.saveOrder(ticket);

		ticketDetailView.updateView(false);
		paymentView.updateView();
	}

	public void doViewDiscounts() {// GEN-FIRST:event_btnViewDiscountsdoViewDiscounts
		try {

			if (ticket == null)
				return;

			DiscountListDialog dialog = new DiscountListDialog(Arrays.asList(ticket));
			dialog.open();

			if (!dialog.isCanceled() && dialog.isModified()) {
				
				if(dialog.getDiscountCoupon() != null)
					updateModel(dialog.getDiscountCoupon().getValue(), true);
				else
					updateModel();

				TicketDAO.getInstance().saveOrUpdate(ticket);

				ticketDetailView.updateView(false);
				paymentView.updateView();
			}

		} catch (Exception e) {
			POSMessageDialog.showError(this, com.floreantpos.POSConstants.ERROR_MESSAGE, e);
		}
	}// GEN-LAST:event_btnViewDiscountsdoViewDiscounts
	private int copies;
	public void setCopies(String copies)
	{
		this.copies = Integer.parseInt(copies);
	}
	
	public void doSettle() {
		if (ticket == null)
			return;
		t = new Thread(this);
		t.start();
		this.setVisible(false);
		getContentPane().setVisible(false);
	}
		
	private void doSettleBarTabTicket(Ticket ticket) {
		ConfirmPayDialog confirmPayDialog = new ConfirmPayDialog();
		confirmPayDialog.setAmount(tenderAmount);
		confirmPayDialog.open();

		if (confirmPayDialog.isCanceled()) {
			return;
		}

		PaymentProcessWaitDialog waitDialog = new PaymentProcessWaitDialog(this);
		waitDialog.setVisible(true);

		try {
			String transactionId = ticket.getProperty(Ticket.PROPERTY_CARD_TRANSACTION_ID);

			CreditCardTransaction transaction = new CreditCardTransaction();
			transaction.setPaymentType(ticket.getProperty(Ticket.PROPERTY_PAYMENT_METHOD));
			transaction.setTransactionType(TransactionType.CREDIT.name());
			transaction.setTicket(ticket);
			transaction.setCardType(ticket.getProperty(Ticket.PROPERTY_CARD_NAME));
			transaction.setCaptured(false);
			transaction.setCardMerchantGateway(CardConfig.getMerchantGateway().name());
			transaction.setCardAuthCode(ticket.getProperty("AuthCode"));
			transaction.addProperty("AcqRefData", ticket.getProperty("AcqRefData"));

			CardReader cardReader = CardReader.valueOf(ticket.getProperty(Ticket.PROPERTY_CARD_READER));

			if (cardReader == CardReader.SWIPE) {
				transaction.setCardReader(CardReader.SWIPE.name());
				transaction.setCardTrack(ticket.getProperty(Ticket.PROPERTY_CARD_TRACKS));
				transaction.setCardTransactionId(transactionId);
			}
			else if (cardReader == CardReader.MANUAL) {
				transaction.setCardReader(CardReader.MANUAL.name());
				transaction.setCardTransactionId(transactionId);
				transaction.setCardNumber(ticket.getProperty(Ticket.PROPERTY_CARD_NUMBER));
				transaction.setCardExpiryMonth(ticket.getProperty(Ticket.PROPERTY_CARD_EXP_MONTH));
				transaction.setCardExpiryYear(ticket.getProperty(Ticket.PROPERTY_CARD_EXP_YEAR));
			}
			else {
				transaction.setCardReader(CardReader.EXTERNAL_TERMINAL.name());
				transaction.setCardAuthCode(ticket.getProperty(Ticket.PROPERTY_CARD_AUTH_CODE));
			}

			setTransactionAmounts(transaction);

			if (cardReader == CardReader.SWIPE || cardReader == CardReader.MANUAL) {
				double advanceAmount = Double.parseDouble(ticket.getProperty(Ticket.PROPERTY_ADVANCE_PAYMENT, "" + CardConfig.getMerchantGateway()));
				
				CardProcessor cardProcessor = CardConfig.getMerchantGateway().getProcessor();
				if (tenderAmount > advanceAmount) {
					cardProcessor.voidAmount(transactionId, advanceAmount);
				}

				cardProcessor.authorizeAmount(transaction);
			}

			settleTicket(transaction);

		} catch (Exception e) {
			POSMessageDialog.showError(e.getMessage(), e);
		} finally {
			waitDialog.setVisible(false);
		}
	}

	public void settleTicket(PosTransaction transaction) {
		try {
			final double dueAmount = ticket.getDueAmount();
			
			confirmLoyaltyDiscount(ticket);

			PosTransactionService transactionService = PosTransactionService.getInstance();
			transactionService.settleTicket(ticket, transaction);
			
			
			System.out.println("Printing ticket");
			//FIXME
			printTicket(ticket, transaction);
			showTransactionCompleteMsg(dueAmount, transaction.getTenderAmount(), ticket, transaction);

			
			if (ticket.getDueAmount() > 0.0) {
				int option = JOptionPane.showConfirmDialog(Application.getPosWindow(), POSConstants.CONFIRM_PARTIAL_PAYMENT, POSConstants.MDS_POS,
						JOptionPane.YES_NO_OPTION);

				if (option != JOptionPane.YES_OPTION) {
					setCanceled(false);
				}

				setTicket(ticket);
			}
			else {
				setCanceled(false);
				dispose();
			}
		
			for(int i = 1; i <= copies;i++)
			{	
				if(ticketDetailView.getPrintType() == PrintType.REGULAR)
					JReportPrintService.printTransaction(transaction, false,false,false);
			    else if (ticketDetailView.getPrintType() == PrintType.REGULAR2)
					JReportPrintService.printTransaction(transaction, false,false,true);
				
			}
			
			
			if(ticket.getDeliveryAddress() != null && ticket.getDeliveryAddress().length() > 0)
			{
				String customerPhone = ticket.getProperty(Ticket.CUSTOMER_PHONE);
				int index = customerPhone.indexOf('0');
				if(index != -1)
				{
					if(index == 0)
					{
						customerPhone = customerPhone.substring(1,customerPhone.length());
					}
				}
				String customerPhone2 = ticket.getProperty(Ticket.CUSTOMER_PHONE2);
				int index2 = customerPhone2.indexOf('0');
				if(index2 != -1)
				{
					if(index2 == 0)
					{
						customerPhone2 = customerPhone2.substring(1,customerPhone.length());
					}
				}
				findandDeletePhone(customerPhone,customerPhone2);	
			}
			dispose();
				
		} catch (UnknownHostException e) {
			POSMessageDialog.showError("My Kala discount server connection error");
		} catch (Exception e) {
			POSMessageDialog.showError(this, POSConstants.ERROR_MESSAGE, e);
		}
	}
	public void findandDeletePhone(String phone,String phone2)
	{
		CallList deleteCaller = null;
		for(Iterator<CallList> itr = CallListDAO.getInstance().findAll().iterator();itr.hasNext();)
		{
			CallList obj = itr.next();
			if(obj.getPhone().compareTo(phone) == 0 || (obj.getPhone().compareTo(phone2) == 0))
			{
				deleteCaller = obj;
				break;
			}
			
		}
		if(deleteCaller != null)
			CallListDAO.getInstance().delete(deleteCaller);
		
	}
	private void showTransactionCompleteMsg(final double dueAmount, final double tenderedAmount, Ticket ticket, PosTransaction transaction) {
		
		Double amt = tenderedAmount - dueAmount;
		if(ticket.getCashPayment() == null || ticket.getCashPayment() != true || amt == 0) return;
		TransactionCompletionDialog dialog = new TransactionCompletionDialog(Application.getPosWindow(), transaction);
		dialog.setCompletedTransaction(transaction);
		dialog.setTenderedAmount(tenderedAmount);
		dialog.setTotalAmount(dueAmount);
		dialog.setPaidAmount(transaction.getAmount());
		dialog.setDueAmount(ticket.getDueAmount());

		if (tenderedAmount > transaction.getAmount()) {
			dialog.setChangeAmount(tenderedAmount - transaction.getAmount());
		}
		else {
			dialog.setChangeAmount(0);
		}

		// dialog.setGratuityAmount(gratuityAmount);
		dialog.updateView();
		dialog.pack();
		dialog.open();
		
	}

	public void confirmLoyaltyDiscount(Ticket ticket) throws IOException, MalformedURLException {
		try {
			if (ticket.hasProperty(LOYALTY_ID)) {
				String url = buildLoyaltyApiURL(ticket, ticket.getProperty(LOYALTY_ID));
				url += "&paid=1";

				IOUtils.toString(new URL(url).openStream());
			}
		} catch (Exception e) {
			POSMessageDialog.showError(e.getMessage(), e);
		}
	}

	private void printTicket(Ticket ticket, PosTransaction transaction) {
		try {
			JReportPrintService.printTicket(ticket, false);
		} catch (Exception ee) {
			POSMessageDialog.showError(Application.getPosWindow(), com.floreantpos.POSConstants.PRINT_ERROR, ee);
		}
	}

	private void payUsingCard(String cardName, final double tenderedAmount) throws Exception {
		if (!CardConfig.getMerchantGateway().isCardTypeSupported(cardName)) {
			POSMessageDialog.showError("<html>Card <b>" + cardName + "</b> not supported.</html>");
			return;
		}

		CardReader cardReader = CardConfig.getCardReader();
		switch (cardReader) {
			case SWIPE:
				SwipeCardDialog swipeCardDialog = new SwipeCardDialog(this);
				swipeCardDialog.pack();
				swipeCardDialog.open();
				break;

			case MANUAL:
				ManualCardEntryDialog dialog = new ManualCardEntryDialog(this);
				dialog.pack();
				dialog.open();
				break;

			case EXTERNAL_TERMINAL:
				AuthorizationCodeDialog authorizationCodeDialog = new AuthorizationCodeDialog(this);
				authorizationCodeDialog.pack();
				authorizationCodeDialog.open();
				break;

			default:
				break;
		}

	}

	public void updatePaymentView() {
		paymentView.updateView();
	}

	public String getPreviousViewName() {
		return previousViewName;
	}

	public void setPreviousViewName(String previousViewName) {
		this.previousViewName = previousViewName;
	}

	public TicketDetailView getTicketDetailView() {
		return ticketDetailView;
	}

	@Override
	public void open() {
		super.open();
	}

	@Override
	public void cardInputted(CardInputter inputter) {
		//authorize only, do not capture
		PaymentProcessWaitDialog waitDialog = new PaymentProcessWaitDialog(this);

		try {
			waitDialog.setVisible(true);

			PosTransaction transaction = paymentType.createTransaction();
			transaction.setTicket(ticket);

			CardProcessor cardProcessor =  CardConfig.getMerchantGateway().getProcessor();
			
			if (inputter instanceof SwipeCardDialog) {
				SwipeCardDialog swipeCardDialog = (SwipeCardDialog) inputter;
				String cardString = swipeCardDialog.getCardString();

				if (StringUtils.isEmpty(cardString) || cardString.length() < 16) {
					throw new RuntimeException("Invalid card string");
				}

				ConfirmPayDialog confirmPayDialog = new ConfirmPayDialog();
				confirmPayDialog.setAmount(tenderAmount);
				confirmPayDialog.open();

				if (confirmPayDialog.isCanceled()) {
					return;
				}
				
				transaction.setCardType(cardName);
				transaction.setCardTrack(cardString);
				transaction.setCaptured(false);
				transaction.setCardMerchantGateway(CardConfig.getMerchantGateway().name());
				transaction.setCardReader(CardReader.SWIPE.name());
				setTransactionAmounts(transaction);

				cardProcessor.authorizeAmount(transaction);

				settleTicket(transaction);
			}
			else if (inputter instanceof ManualCardEntryDialog) {
				ManualCardEntryDialog mDialog = (ManualCardEntryDialog) inputter;

				transaction.setCardType(cardName);
				transaction.setCaptured(false);
				transaction.setCardMerchantGateway(MerchantGateway.AUTHORIZE_NET.name());
				transaction.setCardReader(CardReader.MANUAL.name());
				transaction.setCardNumber(mDialog.getCardNumber());
				transaction.setCardExpiryMonth(mDialog.getExpMonth());
				transaction.setCardExpiryYear(mDialog.getExpYear());
				setTransactionAmounts(transaction);

				cardProcessor.authorizeAmount(transaction);

				settleTicket(transaction);
			}
			else if (inputter instanceof AuthorizationCodeDialog) {
				AuthorizationCodeDialog authDialog = (AuthorizationCodeDialog) inputter;
				String authorizationCode = authDialog.getAuthorizationCode();
				if (StringUtils.isEmpty(authorizationCode)) {
					throw new PosException("Invalid authorization code");
				}

				transaction.setCardType(cardName);
				transaction.setCaptured(false);
				transaction.setCardReader(CardReader.EXTERNAL_TERMINAL.name());
				transaction.setCardAuthCode(authorizationCode);
				setTransactionAmounts(transaction);

				settleTicket(transaction);
			}
		} catch (Exception e) {
			e.printStackTrace();
			POSMessageDialog.showError(e.getMessage());
		} finally {
			waitDialog.setVisible(false);
		}
	}

	private void setTransactionAmounts(PosTransaction transaction) {
		transaction.setTenderAmount(tenderAmount);

		if (tenderAmount >= ticket.getDueAmount()) {
			transaction.setAmount(ticket.getDueAmount());
		}
		else {
			transaction.setAmount(tenderAmount);
		}
	}

	public boolean hasMyKalaId() {
		if (ticket == null)
			return false;

		if (ticket.hasProperty(LOYALTY_ID)) {
			return true;
		}

		return false;
	}

	public void submitMyKalaDiscount() {
		if (ticket.hasProperty(LOYALTY_ID)) {
			POSMessageDialog.showError("Loyalty discount already added.");
			return;
		}

		try {
			String loyaltyid = JOptionPane.showInputDialog("Enter loyalty id:");

			if (StringUtils.isEmpty(loyaltyid)) {
				return;
			}

			ticket.addProperty(LOYALTY_ID, loyaltyid);

			String transactionURL = buildLoyaltyApiURL(ticket, loyaltyid);

			String string = IOUtils.toString(new URL(transactionURL).openStream());

			JsonReader reader = Json.createReader(new StringReader(string));
			JsonObject object = reader.readObject();
			JsonArray jsonArray = (JsonArray) object.get("discounts");
			for (int i = 0; i < jsonArray.size(); i++) {
				JsonObject jsonObject = (JsonObject) jsonArray.get(i);
				addCoupon(ticket, jsonObject);
			}

			updateModel();

			OrderController.saveOrder(ticket);

			POSMessageDialog.showMessage("Congrations! you have discounts from Kala Loyalty Check discounts list for more.");

			ticketDetailView.updateView(false);
			paymentView.updateView();

			//			if (string.contains("\"success\":false")) {
			//				POSMessageDialog.showError("Coupon already used.");
			//			}
		} catch (Exception e) {
			POSMessageDialog.showError("Error setting My Kala discount.", e);
		}
	}

	public String buildLoyaltyApiURL(Ticket ticket, String loyaltyid) {
		Restaurant restaurant = Application.getInstance().getRestaurant();

		String transactionURL = "http://cloud.floreantpos.org/tri2/kala_api?";
		transactionURL += "kala_id=" + loyaltyid;
		transactionURL += "&store_id=" + restaurant.getUniqueId();
		transactionURL += "&store_name=" + POSUtil.encodeURLString(restaurant.getName());
		transactionURL += "&store_zip=" + restaurant.getZipCode();
		transactionURL += "&terminal=" + ticket.getTerminal().getId();
		transactionURL += "&server=" + POSUtil.encodeURLString(ticket.getOwner().getFirstName() + " " + ticket.getOwner().getLastName());
		transactionURL += "&" + ticket.toURLForm();

		return transactionURL;
	}

	private void addCoupon(Ticket ticket, JsonObject jsonObject) {
		Set<String> keys = jsonObject.keySet();
		for (String key : keys) {
			JsonNumber jsonNumber = jsonObject.getJsonNumber(key);
			double doubleValue = jsonNumber.doubleValue();

			TicketCouponAndDiscount coupon = new TicketCouponAndDiscount();
			coupon.setName(key);
			coupon.setType(CouponAndDiscount.FIXED_PER_ORDER);
			coupon.setValue(doubleValue);

			ticket.addTocouponAndDiscounts(coupon);
		}
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;

		ticketDetailView.setTickets(Arrays.asList(ticket));
		paymentView.updateView();
	}
	@Override
	public void run() {
		try{
		this.getRootPane().setOpaque(false);
		tenderAmount = paymentView.getTenderedAmount();
		
		if (ticket.getType() == TicketType.BAR_TAB) {
			doSettleBarTabTicket(ticket);
			return;
		}

		/*PaymentTypeSelectionDialog dialog = new PaymentTypeSelectionDialog();
		dialog.setResizable(false);
		dialog.pack();
		dialog.open();
		if (dialog.isCanceled()) {
			return;
		}*/

		//paymentType = dialog.getSelectedPaymentType();
		if (paymentView.getPaymentType() == PaymentType.CASH)
			paymentType = PaymentType.CASH;
		else if (paymentView.getPaymentType() == PaymentType.CARD)
			paymentType = PaymentType.CARD;
		else
			paymentType = PaymentType.ONLINE;
		
		cardName = paymentType.getDisplayString();
		PosTransaction transaction = null;

		switch (paymentType) {
			case CASH:
			case CARD:
			case ONLINE:
				if (paymentType.name().compareTo(PaymentType.CASH.name()) == 0)
				{
					ticket.setCashPayment(true);
				}
				else if (paymentType.name().compareTo(PaymentType.CARD.name()) == 0)
				{
					CardDialog dialog = new CardDialog();
					dialog.pack();
					dialog.open();
					if(dialog.getCardPaymentType() != null)
						ticket.setCardpaymenttype(dialog.getCardPaymentType().ordinal());
					else
						ticket.setCardpaymenttype(CardPaymentType.KARTE.ordinal());
					
					ticket.setCashPayment(false);
					
				}
				if (paymentType.name().compareTo(PaymentType.ONLINE.name()) == 0)
				{
					ticket.setOnlinePayment(true);
				}
				else
				{
					ticket.setOnlinePayment(false);
				}
				transaction = new CashTransaction();
				transaction.setPaymentType(paymentType.name());
				transaction.setTicket(ticket);
				transaction.setCaptured(true);
				setTransactionAmounts(transaction);

				settleTicket(transaction);
				
				TicketDAO.getInstance().saveOrUpdate(ticket);
				SwitchboardView.getInstance().updateTicketList();
				break;

			
			default:
				break;
		}
	} catch (Exception e) {
		e.printStackTrace();   
	}

		
	}
}
