package com.oro.orderextension;

import com.floreantpos.model.Customer;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketType;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Frame;
import java.util.Date;

import javax.swing.JDialog;
import javax.swing.JPanel;

public class OnlineDeliveryExtension
  extends JDialog
{
  private OnlineCustomerSelectionView customerSelectionView;
  private OnlineDeliverySelectionView deliverySelectionView;
  private JPanel contentPanel;
  private CardLayout cardLayout;
  private boolean canceled = true;
  private TicketType ticketType;
  private Ticket ticket;
  public OnlineDeliveryExtension()
  {
    this(null, TicketType.HOME_DELIVERY);
  }
  
  public OnlineDeliveryExtension(Frame owner, TicketType ticketType)
  {
    super(owner, "Kundendaten", true);
    this.ticketType = ticketType;
    
    createUI();
    
    setDefaultCloseOperation(2);
  }
  
  private void createUI()
  {
    getContentPane().setLayout(new BorderLayout(0, 0));
    this.contentPanel = new JPanel();
    getContentPane().add(this.contentPanel, "Center");
    this.cardLayout = new CardLayout(0, 0);
    this.contentPanel.setLayout(this.cardLayout);
    this.customerSelectionView = new OnlineCustomerSelectionView(this);
    this.deliverySelectionView = new OnlineDeliverySelectionView(this);
    this.customerSelectionView.setNextPage(this.deliverySelectionView);
    this.deliverySelectionView.setPreviousPage(this.customerSelectionView);
    
    this.contentPanel.add(this.customerSelectionView, this.customerSelectionView.getName());
    this.contentPanel.add(this.deliverySelectionView, this.deliverySelectionView.getName());
    
    this.cardLayout.show(this.contentPanel, this.customerSelectionView.getName());
  }
  
  protected void finishWizard()
  {
    Component[] components = this.contentPanel.getComponents();
    for (Component component : components) {
      if (component.isVisible())
      {
        WizardPage wizardPage = (WizardPage)component;
        if (!wizardPage.finish()) {
          return;
        }
        this.canceled = false;
        dispose();
      }
    }
  }
  
  protected void gotoNextPage()
  {
    Component[] components = this.contentPanel.getComponents();
    for (Component component : components) {
      if (component.isVisible())
      {
        WizardPage wizardPage = (WizardPage)component;
        if (!wizardPage.finish()) {
          return;
        }
        WizardPage nextPage = wizardPage.getNextPage();
        if (wizardPage.canGoNext())
        {
          Customer customer = this.customerSelectionView.getCustomer();
          
          this.deliverySelectionView.setRecipientName(customer.getName());
          this.deliverySelectionView.setDeliveryAddress(customer.getAddress() + " " +customer.getDoorNo());
          this.cardLayout.show(this.contentPanel, nextPage.getName());
        }
        return;
      }
    }
  }
  
  public boolean isCanceled()
  {
    return this.canceled;
  }
  
  public void setCanceled(boolean canceled)
  {
    this.canceled = canceled;
  }
  
  public Customer getCustomer()
  {
    return this.customerSelectionView.getCustomer();
  }
  
  public Date getDeliveryDate()
  {
    return this.deliverySelectionView.getDeliveryDate();
  }
  
  public String getDeliveryAddress()
  {
    return this.deliverySelectionView.getShippingAddress();
  }
  
  public String getExtraDeliveryInfo()
  {
    return this.deliverySelectionView.getExtraDeliveryInfo();
  }
  
  public boolean willCustomerPickup()
  {
    return this.deliverySelectionView.willCustomerPickup();
  }
  
  public TicketType getTicketType()
  {
    return this.ticketType;
  }
  
  public void setTicketType(TicketType ticketType)
  {
    this.ticketType = ticketType;
  }
  
  
  public void setTicket(Ticket ticket)
  {
	  this.ticket = ticket;
  }
  
  public Ticket getTicket()
  {
	  return ticket;
  }
}
