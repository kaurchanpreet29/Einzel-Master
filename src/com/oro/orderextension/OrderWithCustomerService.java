package com.oro.orderextension;

import com.floreantpos.main.Application;
import com.floreantpos.model.Customer;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketType;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.ShopTableDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.ui.dialog.CallerDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.views.order.DefaultOrderServiceExtension;
import com.floreantpos.ui.views.order.OrderView;
import com.floreantpos.ui.views.order.RootView;
import com.floreantpos.util.POSUtil;
import com.floreantpos.util.TicketAlreadyExistsException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class OrderWithCustomerService
  extends DefaultOrderServiceExtension
{
  public String getName()
  {
    return "Order+Customer.";
  }
  
  public String getDescription()
  {
    return "This extension enables storing customer information with ticekt";
  }
  
  public void init() {}
  
  public void createNewTicket(TicketType ticketType)
    throws TicketAlreadyExistsException
  {

    CallerDialog.calls = 0;
      
    DeliverySelectionDialog dialog = new DeliverySelectionDialog(Application.getPosWindow(), ticketType);
    dialog.setSize(800, 650);
    dialog.setLocationRelativeTo(Application.getPosWindow());
    dialog.setVisible(true);
    if (!dialog.isCanceled())
    {
      Customer customer = dialog.getCustomer();
      Date deliveryDate = dialog.getDeliveryDate();
      String shippingAddress = dialog.getDeliveryAddress();
      
      Ticket ticket = new Ticket();
      ticket.setPriceIncludesTax(Application.getInstance().isPriceIncludesTax());
      ticket.setType(ticketType);
      
      ticket.setTerminal(Application.getInstance().getTerminal());
      ticket.setOwner(Application.getCurrentUser());
      ticket.setShift(Application.getInstance().getCurrentShift());
      
      ticket.setCustomer(customer);
      ticket.setLoyaltyNo(customer.getLoyaltyNo());
      if (dialog.willCustomerPickup())
      {
        ticket.setCustomerWillPickup(Boolean.valueOf(true));
      }
      else
      {
        ticket.setDeliveryDate(deliveryDate);
        ticket.setDeliveryAddress(shippingAddress);
        ticket.setExtraDeliveryInfo("");
        ticket.setCustomerWillPickup(Boolean.valueOf(false));
        ticket.setMinOrder(dialog.getMinOrder());
		ticket.setDeliveryCost(dialog.getDeliveryCost());   
		ticket.setDeliveryTime(dialog.getDeliveryTime());
	    
      }
      Calendar currentTime = Calendar.getInstance();
      ShopTable table = ShopTableDAO.getInstance().getByNumber("99");
      if(table == null)
      {
    	  table = new ShopTable();
    	  table.setNumber("99");
      }
      ticket.addTotables(table);
      ticket.setCreateDate(currentTime.getTime());
      ticket.setCreationHour(Integer.valueOf(currentTime.get(11)));
      
      OrderView.getInstance().setCurrentTicket(ticket);
      RootView.getInstance().showView("ORDER_VIEW");
    }
  }
  
  public void createNewOnlineTicket(TicketType ticketType)
		    throws TicketAlreadyExistsException
		  {
	  		Ticket ticket = null;
		    OnlineDeliveryExtension dialog = new OnlineDeliveryExtension(Application.getPosWindow(), ticketType);
		    
		    
		    dialog.setSize(800, 650);
		    dialog.setLocationRelativeTo(Application.getPosWindow());
		    dialog.setVisible(true);
		    if (!dialog.isCanceled())
		    {
		      Customer customer = dialog.getCustomer();
		      Date deliveryDate = dialog.getDeliveryDate();
		      String shippingAddress = dialog.getDeliveryAddress();
		      ticket = dialog.getTicket();
		     if (ticket == null)
		     {
		    	 System.out.println("Creating new ticket");
		    	  ticket = new Ticket();
		     }
		      ticket.setPriceIncludesTax(Application.getInstance().isPriceIncludesTax());
		      ticket.setType(ticketType);
		      ticket.setTerminal(Application.getInstance().getTerminal());
		      ticket.setOwner(Application.getCurrentUser());
		      ticket.setShift(Application.getInstance().getCurrentShift());
		      
		      ticket.setCustomer(customer);
		      ticket.setLoyaltyNo(customer.getLoyaltyNo());
		      if (dialog.willCustomerPickup())
		      {
		        ticket.setCustomerWillPickup(Boolean.valueOf(true));
		      }
		      else
		      {
		        ticket.setDeliveryDate(deliveryDate);
		        ticket.setDeliveryAddress(shippingAddress);
		        ticket.setExtraDeliveryInfo(dialog.getExtraDeliveryInfo());
		        ticket.setCustomerWillPickup(Boolean.valueOf(false));
		      }
		      Calendar currentTime = Calendar.getInstance();
		      ticket.setCreateDate(currentTime.getTime());
		      ticket.setCreationHour(Integer.valueOf(currentTime.get(11)));
		      
		      OrderView.getInstance().setCurrentTicket(ticket);
		      RootView.getInstance().showView("ORDER_VIEW");
		    }
		  }
  public void setCustomerToTicket(int ticketId) {}
  
  public void setDeliveryDate(int ticketId) {}
  
  public void assignDriver(int ticketId)
  {
    List<User> drivers = UserDAO.getInstance().findDrivers();
    if ((drivers == null) || (drivers.size() == 0))
    {
      POSMessageDialog.showError("Kein Fahrer gefunden");
      return;
    }
    Ticket ticket = TicketDAO.getInstance().get(Integer.valueOf(ticketId));
    
    AssignDriverDialog dialog = new AssignDriverDialog(Application.getPosWindow());
    dialog.setData(ticket, drivers);
    dialog.setSize(550, 450);
    dialog.setLocationRelativeTo(Application.getPosWindow());
    dialog.setVisible(true);
  }
  
  public boolean finishOrder(int ticketId)
  {
    Ticket ticket = TicketDAO.getInstance().get(Integer.valueOf(ticketId));
    
    int due = (int)POSUtil.getDouble(ticket.getDueAmount());
    if (due != 0)
    {
      POSMessageDialog.showError("Rechnung nicht bezahlt");
      return false;
    }
    int option = JOptionPane.showOptionDialog(Application.getPosWindow(), "Rechnung# " + ticket.getId() + " wird geschlossen.", "Bestaetigung", 2, 1, null, null, null);
    if (option != 0) {
      return false;
    }
    ticket.setClosed(Boolean.valueOf(true));
    
    
    TicketDAO.getInstance().saveOrUpdate(ticket);
    
    User driver = ticket.getAssignedDriver();
    if (driver != null)
    {
      driver.setAvailableForDelivery(Boolean.valueOf(true));
      UserDAO.getInstance().saveOrUpdate(driver);
    }
    return true;
  }
  
  public void createCustomerMenu(JMenu menu)
  {
    menu.add(new CustomerExplorerAction());
  }
  public void createCustomerButton()
  {
   new CustomerExplorerAction("Kundendatenbank");
  }
}
