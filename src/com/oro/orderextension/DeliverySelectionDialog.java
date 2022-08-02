package com.oro.orderextension;

import com.floreantpos.main.Application;
import com.floreantpos.model.Customer;
import com.floreantpos.model.Gebiet;
import com.floreantpos.model.TicketType;
import com.floreantpos.model.dao.DeliveryCostDAO;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.NumberUtil;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

public class DeliverySelectionDialog
  extends JDialog
{
  private CustomerSelectionView customerSelectionView;
  private DeliverySelectionView deliverySelectionView;
  private JPanel contentPanel;
  private CardLayout cardLayout;
  private boolean canceled = true;
  private TicketType ticketType;
  
  public DeliverySelectionDialog()
  {
    this(null, TicketType.HOME_DELIVERY);
  }
  
  public DeliverySelectionDialog(Frame owner, TicketType ticketType)
  {
    super(owner, "Kundendaten", true);
    this.ticketType = ticketType;
    
    createUI();
    
    setDefaultCloseOperation(2);
  }
  
  private void createUI()
  {
	 KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
	    public boolean dispatchKeyEvent(KeyEvent e) {
		       
	        if (e.getID() == KeyEvent.KEY_PRESSED) {
	           if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
	        	  dispose();
	        	  return false;
	            }
	        }
	        return false;
	}});
    getContentPane().setLayout(new BorderLayout(0, 0));
    this.contentPanel = new JPanel();
    getContentPane().add(this.contentPanel, "Center");
    this.cardLayout = new CardLayout(0, 0);
    this.contentPanel.setLayout(this.cardLayout);
    this.customerSelectionView = new CustomerSelectionView(this);         
    this.deliverySelectionView = new DeliverySelectionView(this);
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
          
          List<Gebiet> gebietList = DeliveryCostDAO.getInstance().findAll(); 
          int found = 0;
          if(gebietList != null)
          {
        	  for(Iterator<Gebiet> itr = gebietList.iterator(); itr.hasNext();)
        	  {
        		  Gebiet gebiet = itr.next();
        		  String zip = gebiet.getPlz()+"";
        		  String bezirk = gebiet.getBezirk().toUpperCase();
        		  zip = zip.trim();
              
        		  if((bezirk.compareTo(customer.getBezerk()) == 0) && (zip.compareTo(customer.getZipCode()) == 0))
        		  {
        			  found = 1;
        			  if(gebiet.getIsopen())
        			  {
        				  this.deliverySelectionView.setDeliveryCost(NumberUtil.formatNumber(gebiet.getLieferkosten())+"");
        				  this.deliverySelectionView.setMinOrder(NumberUtil.formatNumber(gebiet.getMindest())+"");
        				  this.deliverySelectionView.setDeliveryTime(gebiet.getLieferzeit()+"");
        				  found = 2;
        			  }
        		  }
        	  }
          }
          
          this.deliverySelectionView.setFound(found);  
          System.out.println("Found value: "+ found);
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
  
  public Double getMinOrder()
  {
	  return this.deliverySelectionView.getMinOrder();
  }
  
  public Double getDeliveryCost()
  {
	  return this.deliverySelectionView.getDeliveryCost();
  }
  
  public String getDeliveryTime()
  {
	  return this.deliverySelectionView.getDeliveryTime();
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
}
