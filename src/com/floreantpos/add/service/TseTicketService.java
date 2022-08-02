package com.floreantpos.add.service;

import java.util.List;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.PaymentType;
import com.floreantpos.model.TSEReceiptData;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.dao.TSEReceiptDataDAO;
import com.khana.tse.fiskaly.FiskalyKeyParameter;
import com.khana.tse.fiskaly.PosFiskalyController;
import com.khana.tse.fiskaly.transaction.FiskalyPaymentType;
import com.khana.tse.fiskaly.transaction.FiskalyReceiptType;

/**
 * @author Jyoti Rai
 *
 */

public class TseTicketService {
	public static TseTicketService tseService;
	public static TseTicketService getTseService() {
		if(tseService==null) {
			tseService = new TseTicketService();
			return tseService;
		}
		return tseService;
	}

	private PosFiskalyController posTseController;
	public boolean isTseEnable = false;
	public boolean isTseEnable() {
		return isTseEnable;
	}
	public TseTicketService() {
		init();
	}

	private void init() {
		isTseEnable = TerminalConfig.isTseEnable();
		posTseController = new PosFiskalyController(isTseEnable);
	}

	public void initOrder(Ticket ticket) {
		posTseController.setRestartOrder(needsKitchenPrint(ticket, ticket.getTicketItems()));
		if(ticket.getTseTxRevisionNr()==null) {
			posTseController.setRestartReceipt(true);
		}
		boolean orderStarted = false;
		if(posTseController.isRestartOrder()) {
			posTseController.setInstantParam(posTseController.tseStartOrder(Application.getInstance().getParam()));
			posTseController.setRestartOrder(false);
			orderStarted = true;
			/*
			 * for sofort storno please remove this
			 */
			if(ticket.getTseReceiptTxRevisionNr()==null||posTseController.isRestartReceipt())
				posTseController.tseReStartReceipt(posTseController.getInstantParam(), ticket);
		}
		if(orderStarted)
			posTseController.setInstantParam(posTseController.tseFinishOrder(ticket.getTicketItems(), posTseController.getInstantParam()));

	}
	
	
	public void forceRestartOrder(Ticket ticket) {
		posTseController.setRestartOrder(true);
		if(ticket.getTseTxRevisionNr()==null) {
			posTseController.setRestartReceipt(true);
		}
		boolean orderStarted = false;
		if(posTseController.isRestartOrder()) {
			posTseController.setInstantParam(posTseController.tseStartOrder(Application.getInstance().getParam()));
			posTseController.setRestartOrder(false);
			orderStarted = true;
			/*
			 * for sofort storno please remove this
			 */
			if(ticket.getTseReceiptTxRevisionNr()==null||posTseController.isRestartReceipt())
				posTseController.tseReStartReceipt(posTseController.getInstantParam(), ticket);
		}
		if(orderStarted)
			posTseController.setInstantParam(posTseController.tseFinishOrder(ticket.getTicketItems(), posTseController.getInstantParam()));
	}
	
	
	public boolean needsKitchenPrint(Ticket ticket, List<TicketItem> ticketItems) {
		if (ticket.getDeletedItems() != null && ticket.getDeletedItems().size() > 0) {
			return true;
		}

		for (TicketItem item : ticketItems) {

			if (!item.isPrintedToKitchen() && (!item.getItemId().equals(999))) {
				return true;
			}
		}

		return false;
	}
	
	

	public synchronized TSEReceiptData initFinishTseOrder(Ticket ticket, int paymentType) {
		TSEReceiptData receiptData = null;
		initOrder(ticket);
		FiskalyKeyParameter param = Application.getInstance().getParam();
		try {
			if (ticket.getTseReceiptTxRevisionNr() != null)
				param.setLatestRevisionReceipt(Integer.parseInt(ticket.getTseReceiptTxRevisionNr()));
			else if (posTseController.getInstantParam()!=null&&posTseController.getInstantParam().getLatestRevisionReceipt() > 0)
				param.setLatestRevisionReceipt(posTseController.getInstantParam().getLatestRevisionReceipt());
			if (ticket.getTseReceiptTxId() != null)
				param.setTransactionIdReceipt(ticket.getTseReceiptTxId());
			else if (posTseController.getInstantParam()!=null&&!posTseController.getInstantParam().getTransactionIdReceipt().isEmpty()) {
				ticket.setTseReceiptTxId(posTseController.getInstantParam().getTransactionIdReceipt());
				ticket.setTseClientId(posTseController.getInstantParam().getClientId());
				param.setTransactionIdReceipt(posTseController.getInstantParam().getTransactionIdReceipt());
			}else {
				posTseController.tseReStartReceipt(posTseController.getInstantParam()!=null?posTseController.getInstantParam():Application.getInstance().getParam(), ticket);
			}
			
			if (paymentType == PaymentType.CASH.ordinal())
				receiptData = posTseController.tseFinishReceipt(param, ticket.getTicketItems(), FiskalyPaymentType.CASH,
						FiskalyReceiptType.RECEIPT);
			else
				receiptData = posTseController.tseFinishReceipt(param, ticket.getTicketItems(), FiskalyPaymentType.NON_CASH,
						FiskalyReceiptType.RECEIPT);

			if(receiptData!=null)
				TSEReceiptDataDAO.getInstance().saveOrUpdate(receiptData);			

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return receiptData;
	}


}
