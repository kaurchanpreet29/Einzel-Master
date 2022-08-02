package com.floreantpos.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;

import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.views.payment.SettleTicketDialog;

public class SettleTicketAction extends AbstractAction {

	private int ticketId;
	private Ticket ticket;
	public SettleTicketAction(int ticketId) {
		this.ticketId = ticketId;
	}
	public SettleTicketAction(Ticket ticket) {
		this.ticket = ticket;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		execute();
	}

	public boolean execute(boolean flag) {
		if (ticket.isPaid()) {
			POSMessageDialog.showError("Rechnung bereits bezahlt");
			return false;
		}
		
		SettleTicketDialog posDialog = new SettleTicketDialog();
		posDialog.setTicket(ticket);
		posDialog.setSize(800, 600);
		posDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		posDialog.open();
		
		return !posDialog.isCanceled();
	}

	public boolean execute() {
		Ticket ticket = TicketDAO.getInstance().loadFullTicket(ticketId);

		if (ticket.isPaid()) {
			POSMessageDialog.showError("Rechnung bereits bezahlt");
			return false;
		}
		
		SettleTicketDialog posDialog = new SettleTicketDialog();
		posDialog.setTicket(ticket);
		posDialog.setSize(800, 600);
		posDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		posDialog.open();
		
		return !posDialog.isCanceled();
	}

}
