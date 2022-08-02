package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import com.floreantpos.main.Application;
import com.floreantpos.model.CouponAndDiscount;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketCouponAndDiscount;
import com.floreantpos.model.UserPermission;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.views.TicketDetailView;
import com.floreantpos.ui.views.order.OrderView;

public class DiscountDialog extends POSDialog{
	Ticket ticket;
	TicketDetailView view;
	PosButton btnCancel;

	public DiscountDialog(Ticket ticket)
	{
		this.ticket = ticket;
		setTitle("Rabatt");
		initComponents();
	}

	public void initComponents()
	{
		setLayout(new BorderLayout());
		view = new TicketDetailView(this);
		view.setTicket(ticket);
		getContentPane().setBackground(new Color(209,222,235));
		getContentPane().add(view,BorderLayout.CENTER);
		btnCancel = new PosButton("ABBRECHEN");
		btnCancel.setBackground(new Color(255,153,153));
		btnCancel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
//				OrderController.saveOrder(ticket);
				OrderView.getInstance().getTicketView().updateAllView();
			}
		});
		getContentPane().add(btnCancel,BorderLayout.SOUTH);
	}
	
	public void doViewDiscounts() {// GEN-FIRST:event_btnViewDiscountsdoViewDiscounts
		try {

			if (ticket == null)
				return;

			DiscountListDialog dialog = new DiscountListDialog(Arrays.asList(ticket));
			dialog.open();

			if (!dialog.isCanceled() && dialog.isModified()) {

				if(dialog.isDelete())
					updateModel(ticket.getCouponAndDiscounts().get(0).getValue(), true);
				else
					updateModel();

				view.updateView(false);
			}
//			OrderController.saveOrder(ticket);

		} catch (Exception e) {
			POSMessageDialog.showError(this, com.floreantpos.POSConstants.ERROR_MESSAGE, e);
		}
	}// GEN-LAST:event_btnViewDiscountsdoViewDiscounts
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
		if(deleteCoupon) {
			ticket.setCouponAndDiscounts(null);
			ticket.setDiscountAmount(0.0);
		}	
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
//				ticket.addTocouponAndDiscounts(coupon);
				ticket.calculatePrice(coupon.getValue(), false);
				if(coupon.getType() == CouponAndDiscount.PERCENTAGE_PER_ITEM)
					updateModel(coupon.getValue(),false);
				else
					updateModel();
//				//OrderController.saveOrder(ticket);
				view.updateView(true);
			}
		} catch (Exception e) {
			POSMessageDialog.showError(this, com.floreantpos.POSConstants.ERROR_MESSAGE, e);
		}
		OrderView.getInstance().getTicketView().focusToScanid();
	}// GEN-LAST:event_btnApplyCoupondoApplyCoupon


	//finish for manual Rabatt
	
	

	public void doManualDiscount() {		
		NumberSelectionDialog2 dialog = new NumberSelectionDialog2();
		dialog.setFloatingPoint(true);
		dialog.setTitle("Enter value");
		dialog.pack();
		dialog.open();

		if(!dialog.isCanceled()) {
			double value = dialog.getValue(); 	


			TicketCouponAndDiscount coupon = new TicketCouponAndDiscount();
			double totalDiscount = 0;
			double subtotal = ticket.getSubtotalAmount();

			coupon.setCouponAndDiscountId(99999);
			coupon.setName("Freiwilliges");
			coupon.setType(CouponAndDiscount.FREE_AMOUNT);
			coupon.setValue(value);

			totalDiscount = ticket.calculateDiscountFromType(coupon, subtotal);
			coupon.setValue(totalDiscount);

			ticket.addTocouponAndDiscounts(coupon);

//			if(coupon.getType() == CouponAndDiscount.PERCENTAGE_PER_ITEM)
//				updateModel(coupon.getValue(),false);
//			else
//				updateModel();
			//OrderController.saveOrder(ticket);
			view.updateView(false);
		}
		OrderView.getInstance().getTicketView().focusToScanid();
	}
	public void setCancelText() {
		this.btnCancel.setText("OK");
	}
	public Ticket getTicket()
	{
		return ticket;
	}
}
