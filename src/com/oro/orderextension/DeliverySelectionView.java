package com.oro.orderextension;

import com.floreantpos.PosException;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.TicketType;
import com.floreantpos.swing.FixedLengthDocument;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.swing.PosSmallButton;
import com.floreantpos.swing.QwertyKeyPad;
import com.floreantpos.swing.TimeSelector;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.NumberUtil;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXDatePicker;

public class DeliverySelectionView
  extends JPanel
  implements WizardPage
{
  private WizardPage previousPage;
  private Date selectedDate;
  private String shippingAddress;
  private JXDatePicker datePicker;
  private JTextArea taDeliveryAddress;
  private TimeSelector timeSelector;
  private JTextField tfRecipientName;
  
  private PosSmallButton btnCancel;
  private DeliverySelectionDialog deliverySelectionDialog;
  private OnlineDeliveryExtension onlineDeliveryExtension;
  private JCheckBox cbCustomerPickup;
  private JTextField tfMinOrder;
  private JTextField tfDeliveryCost;
  private JTextField tfDeliveryTime;
  private JLabel lblFound;
  private PosSmallButton btnSaveProceed;
  private JCheckBox cbOkay;
  FixedLengthTextField tfHour;
  FixedLengthTextField tfMin;
  int found = 99;
  public DeliverySelectionView()
  {
    this(null);
  }
  
  public DeliverySelectionView(DeliverySelectionDialog dialog)
  {
    this.deliverySelectionDialog = dialog;
    
    createUI();
  }
  
  private void createUI()
  {
    JPanel topPanel = new JPanel(new MigLayout());
    setLayout(new MigLayout());
    this.setPreferredSize(new Dimension(800,600));
    setBackground(new Color(209,222,235));
    JLabel label = new JLabel("");
    topPanel.add(label, "cell 0 0");
    
    JLabel lblRecepientName = new JLabel("Kundenname:");
    topPanel.add(lblRecepientName, "cell 1 0,alignx trailing");
    topPanel.setBackground(new Color(209,222,235));
    this.tfRecipientName = new JTextField();
    topPanel.add(this.tfRecipientName, "cell 2 0,growx");
    this.tfRecipientName.setColumns(10);
    
    JLabel label_1 = new JLabel("");
    topPanel.add(label_1, "cell 2 2");
    
    this.cbCustomerPickup = new JCheckBox("Abholung");
    cbCustomerPickup.setFont(new Font("Times New Roman", Font.BOLD, 24));
    cbCustomerPickup.setBackground(new Color(209,222,235));
    this.cbCustomerPickup.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        boolean enabled = DeliverySelectionView.this.cbCustomerPickup.isSelected();
        DeliverySelectionView.this.setPickupEnable(enabled);
      }
    });
    JLabel lblDeliveryDate = new JLabel("Lieferdatum:");
    topPanel.add(lblDeliveryDate, "cell 1 1,alignx trailing");
    
    this.datePicker = new JXDatePicker();
    this.datePicker.setDate(new Date());
    
    topPanel.add(this.datePicker, "flowx,cell 2 1");
    datePicker.setBackground(new Color(209,222,235));
    //topPanel.add(this.cbCustomerPickup, "cell 2 2");
    
    JPanel dpanel = new JPanel();
    dpanel.setLayout(new MigLayout());
    
    JLabel lblHour = new JLabel("Bestekkzeitpunkt:");
    dpanel.add(lblHour);
    tfHour = new FixedLengthTextField();
    tfHour.setDocument(new FixedLengthDocument(10));
    tfHour.setText("18");
    dpanel.add(tfHour);
    
    JLabel lblMin = new JLabel(":");
    dpanel.add(lblMin);
    tfMin = new FixedLengthTextField();
    tfMin.setDocument(new FixedLengthDocument(10));
    tfMin.setText("0");
    dpanel.add(tfMin);
    dpanel.setBackground(new Color(209,222,235));
    
    topPanel.add(dpanel, "cell 3 1");
    JLabel lblShippingAddress = new JLabel("Lieferanschrift:");
    topPanel.add(lblShippingAddress, "cell 1 3,alignx trailing");
    
    JScrollPane scrollPane = new JScrollPane();
    scrollPane.setPreferredSize(new Dimension(3, 60));
    
    
    this.taDeliveryAddress = new JTextArea();
    this.taDeliveryAddress.setRows(4);
    scrollPane.setViewportView(this.taDeliveryAddress);
    topPanel.add(taDeliveryAddress, "cell 2 3,growx");
    JLabel lblMinimumValue = new JLabel("Mindestbestellwert (€)");
    topPanel.add(lblMinimumValue, "cell 1 4,alignx trailing");
    
    tfMinOrder = new JTextField(10);
    tfMinOrder.setText("0.00");
    tfMinOrder.setFont(new Font("Times New Roman", Font.BOLD, 16));
    topPanel.add(tfMinOrder, "cell 2 4,growx");
   
    lblFound = new JLabel();
    lblFound.setFont(new Font("Times New Roman", Font.BOLD, 22));
	lblFound.setForeground(new Color(255,80,80));
	topPanel.add(lblFound, "cell 3 4,growx");
	lblFound.setVisible(false);
    
	cbOkay = new JCheckBox();
	cbOkay.setText("Weiter");
	cbOkay.setFont(new Font("Times New Roman", Font.BOLD, 26));
	cbOkay.setBackground(new Color(209,222,235));
	cbOkay.setVisible(false);
	cbOkay.addActionListener(new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(cbOkay.isSelected())
			{
				DeliverySelectionView.this.deliverySelectionDialog.finishWizard();
			}
		}
		
	});
	topPanel.add(cbOkay, "cell 3 5,growx");
    JLabel lblDeliveryCost = new JLabel("Lieferkosten (€)");
    topPanel.add(lblDeliveryCost, "cell 1 5,alignx trailing");
    
    tfDeliveryCost = new JTextField(10);
    tfDeliveryCost.setText("0.00");
    tfDeliveryCost.setFont(new Font("Times New Roman", Font.BOLD, 16));
    topPanel.add(tfDeliveryCost, "cell 2 5,growx");
   
    JLabel lblTime = new JLabel("Lieferzeit (min)");
    topPanel.add(lblTime, "cell 1 6,alignx trailing");
    
    tfDeliveryTime = new JTextField(10);
    tfDeliveryTime.setText("0");
    tfDeliveryTime.setFont(new Font("Times New Roman", Font.BOLD, 16));
    topPanel.add(tfDeliveryTime, "cell 2 6,growx");
   
    JPanel panel_1 = new JPanel();
    panel_1.setBackground(new Color(209,222,235));
    topPanel.add(panel_1, "cell 1 7 2 1,shrinky 0,grow");
    
    this.btnCancel = new PosSmallButton();
    this.btnCancel.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        DeliverySelectionView.this.deliverySelectionDialog.setCanceled(true);
        DeliverySelectionView.this.deliverySelectionDialog.dispose();
      }
    });
    this.btnCancel.setText("Abbrechen");    
    btnCancel.setBackground(new Color(255,153,153));
    panel_1.add(this.btnCancel);
    
    btnSaveProceed = new PosSmallButton(); 
    btnSaveProceed.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        DeliverySelectionView.this.deliverySelectionDialog.finishWizard();
      }
    });
    btnSaveProceed.setText("Speichern und Weiter");
    btnSaveProceed.setBackground(new Color(102,255,102));
    panel_1.add(btnSaveProceed);
    
    add(topPanel, "cell 0 0,grow");
    
    JPanel panel = new JPanel(new BorderLayout());
    add(panel, "cell 0 1,grow");
    
    QwertyKeyPad qwertyKeyPad = new QwertyKeyPad();
    panel.add(qwertyKeyPad,BorderLayout.NORTH);
    panel.setBackground(new Color(209,222,235));
   
    if (this.deliverySelectionDialog.getTicketType().equals(TicketType.PICKUP))
    {
      this.cbCustomerPickup.setSelected(true);
      setPickupEnable(true);
    }
  }
  
  private Date captureDeliveryDate()
  {
    Date date = this.datePicker.getDate();
    int hour = Integer.parseInt(tfHour.getText());
    int min = Integer.parseInt(tfMin.getText());
    if ((hour == -1) || (hour < 0) || (hour > 24)) {
      throw new PosException("Bitte wählen Sie Stunden 0-24.");
    }
    if ((min == -1) || (min < 0) || (min > 59)) {
      throw new PosException("Bitte wählen Minutenwert von 0 bis 59");
    }
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.HOUR, hour);
    calendar.set(Calendar.MINUTE, min);
    return calendar.getTime();
  }
  
  private String captureShippingAddress()
  {
    return this.taDeliveryAddress.getText();
  }
  
  public Date getDeliveryDate()
  {
    return this.selectedDate;
  }
  
  public String getShippingAddress()
  {
    return this.shippingAddress;
  }

  public Double getMinOrder()
  {
	  Double defaultValue = 0.00;
	  if(tfMinOrder.getText().length() == 0)
		  return defaultValue;
	  else
	  {
		  String minOrder = tfMinOrder.getText().replace(',', '.');
		  return Double.valueOf(minOrder);
	  }
  }
  
  public Double getDeliveryCost()
  {
	  Double defaultValue = 0.00;
	  if(tfDeliveryCost.getText().length() == 0)
		  return defaultValue;
	  else
	  {
		  String deliveryCost = tfDeliveryCost.getText().replace(',', '.');
		  return Double.valueOf(deliveryCost);
	  }
  }
  
  public String getDeliveryTime()
  {
	  String defaultValue = "0";
	  if(tfDeliveryTime.getText().length() == 0)
		  return defaultValue;
	  else
	   return tfDeliveryTime.getText();
  }
  
  public String getName()
  {
    return "D";
  }
  
  public boolean canGoNext()
  {
    return false;
  }
  
  public boolean canGoBack()
  {
    return true;
  }
  
  public void setRecipientName(String name)
  {
    this.tfRecipientName.setText(name);
  }
  
  public void setDeliveryAddress(String shippingAddress)
  {
    this.taDeliveryAddress.setText(shippingAddress);
  }
  
  public void setMinOrder(String minValue)
  {
	if(minValue.length() == 0)
		this.tfMinOrder.setText("0.00");
	else
		this.tfMinOrder.setText(minValue);
  }
  
  public void setDeliveryCost(String deliveryCost)
  {
	  
	  if(deliveryCost.length() == 0)
			this.tfMinOrder.setText("0.00");
	  else	  
		  this.tfDeliveryCost.setText(deliveryCost);
  }
  
  public void setDeliveryTime(String time)
  {
	 if(time.length() == 0)
		 this.tfDeliveryTime.setText("0");
	 else
		 this.tfDeliveryTime.setText(time);
  }
  
  public void setFound(int found)
  {
	   if(found == 1)
	   {
		   lblFound.setText("Lieferservice heute nicht verfuegbar");
		   lblFound.setVisible(true);
		   
		   if(TerminalConfig.isLieferKostenEnable())
		   {
			   btnSaveProceed.setEnabled(false);
		   	   cbOkay.setVisible(true);
		   }
	   }
	   else if(found == 0)
	   {
		   lblFound.setText("Lieferservice nicht verfuegbar");
		   lblFound.setVisible(true);
		   
		   if(TerminalConfig.isLieferKostenEnable())
		   {
			   btnSaveProceed.setEnabled(false);
		  	   cbOkay.setVisible(true);
		   }
	   }
	  
  }
  public void setPreviousPage(WizardPage previousPage)
  {
    this.previousPage = previousPage;
  }
  
  public WizardPage getPreviousPage()
  {
    return this.previousPage;
  }
  
  public WizardPage getNextPage()
  {
    return null;
  }
  
  public boolean canFinishWizard()
  {
    return true;
  }
  
  public boolean willCustomerPickup()
  {
    return this.cbCustomerPickup.isSelected();
  }
  
  public boolean finish()
  {
    try
    {
      this.selectedDate = captureDeliveryDate();
      this.shippingAddress = captureShippingAddress();
      return true;
    }
    catch (PosException e)
    {
      POSMessageDialog.showError(e.getMessage());
    }
    return false;
  }
  
  private void setPickupEnable(boolean enabled)
  {
    this.taDeliveryAddress.setEditable(!enabled);
  }
}
