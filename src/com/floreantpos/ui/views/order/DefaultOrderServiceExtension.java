package com.floreantpos.ui.views.order;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JMenu;
import javax.swing.JOptionPane;

import com.floreantpos.POSConstants;
import com.floreantpos.extension.FloorLayoutPlugin;
import com.floreantpos.extension.OrderServiceExtension;
import com.floreantpos.main.Application;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketType;
import com.floreantpos.model.User;
import com.floreantpos.model.UserPermission;
import com.floreantpos.model.UserType;
import com.floreantpos.model.dao.ShopTableDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.POSUtil;
import com.floreantpos.util.PosGuiUtil;
import com.floreantpos.util.TicketAlreadyExistsException;
import com.floreantpos.config.TerminalConfig;

public class DefaultOrderServiceExtension implements OrderServiceExtension {

	
	@Override
	public String getName() {
		return "Order Handler";
	}

	@Override
	public String getDescription() {
		return "Default order handler";
	}

	@Override
	public void init() {
	}

	int selectedTable = 0;
	public int getSelectedTable()
	{
		return selectedTable;       
	}
	@Override
	public void createNewTicket(TicketType ticketType) throws TicketAlreadyExistsException {
		
		Application application = Application.getInstance();
		
		Ticket ticket = new Ticket();
		
		
		ticket.setPriceIncludesTax(application.isPriceIncludesTax());
		ticket.setNumberOfGuests(1);
		ticket.setTerminal(application.getTerminal());
		ticket.setOwner(Application.getCurrentUser());
		ticket.setShift(application.getCurrentShift());
		
		Calendar currentTime = Calendar.getInstance();
		ticket.setCreateDate(currentTime.getTime());
		ticket.setCreationHour(currentTime.get(Calendar.HOUR_OF_DAY));

		OrderView.getInstance().setCurrentTicket(ticket);
		RootView.getInstance().showView(OrderView.VIEW_NAME);
		
		if(TerminalConfig.istype())
		{
			OrderView.getInstance().getTicketView().border.setTitle("MITNAHME");
		}
		else
		{
			OrderView.getInstance().getTicketView().border.setTitle("BESTELLUNG");
		}
	}

	@Override
	public void setCustomerToTicket(int ticketId) {
	}

	public void setDeliveryDate(int ticketId) {
	}

	@Override
	public void assignDriver(int ticketId) {

	};

	@Override
	public boolean finishOrder(int ticketId) {
		Ticket ticket = TicketDAO.getInstance().get(ticketId);

//		if (ticket.getType() == TicketType.DINE_IN) {
//			POSMessageDialog.showError("Please select tickets of type HOME DELIVERY or PICKUP or DRIVE THRU");
//			return false;
//		}

		int due = (int) POSUtil.getDouble(ticket.getDueAmount());
		if (due != 0) {
			POSMessageDialog.showError("Die Rechnung ist nicht komplett bezahlt");
			return false;
		}
	
		int option = JOptionPane.showOptionDialog(Application.getPosWindow(), "Ticket# " + ticket.getId() + "wird geschlossen.", "Bestaetigung",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);

		if (option != JOptionPane.OK_OPTION) {
			return false;
		}

		ticket.setClosed(true);
		
		ticket.setClosingDate(new Date());
		TicketDAO.getInstance().saveOrUpdate(ticket);  

		User driver = ticket.getAssignedDriver();
		if (driver != null) {
			driver.setAvailableForDelivery(true);
			UserDAO.getInstance().saveOrUpdate(driver);
		}

		return true;
	}

	@Override
	public void createCustomerMenu(JMenu menu) {
	}

	@Override
	public void createNewOnlineTicket(TicketType ticketType)
			throws TicketAlreadyExistsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createCustomerButton() {
		// TODO Auto-generated method stub
		
	}
}
