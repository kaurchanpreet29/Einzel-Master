package com.oro.orderextension;

import com.floreantpos.PosException;
import com.floreantpos.model.TicketType;
import com.floreantpos.swing.FixedLengthDocument;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.swing.PosSmallButton;
import com.floreantpos.swing.QwertyKeyPad;
import com.floreantpos.swing.TimeSelector;
import com.floreantpos.ui.dialog.POSMessageDialog;

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

public class OnlineDeliverySelectionView
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
  private JTextArea taExtraInsruction;
  private PosSmallButton btnCancel;
  private OnlineDeliveryExtension onlineDeliveryExtension;
  private JCheckBox cbCustomerPickup;
  JTextField tfHour;
  JTextField tfMin;
  public OnlineDeliverySelectionView()
  {
    this(null);
  }
  
  public OnlineDeliverySelectionView(OnlineDeliveryExtension dialog)
  {
    this.onlineDeliveryExtension = dialog;
    
    createUI();
  }
  
  private void createUI()
  {
    JPanel topPanel = new JPanel(new MigLayout("fill", "[grow][][grow][grow]", "[][][][grow][grow][shrink 0][]"));
   
    
    setLayout(new MigLayout("fill", "[549px,grow]", "[grow,fill][]"));
    

    JLabel label = new JLabel("");
    topPanel.add(label, "cell 0 0");
    topPanel.setBackground(new Color(209,222,235));
    JLabel lblRecepientName = new JLabel("Recipient Name:");
    topPanel.add(lblRecepientName, "cell 1 0,alignx trailing");
    
    this.tfRecipientName = new JTextField();
    topPanel.add(this.tfRecipientName, "cell 2 0,growx");
    this.tfRecipientName.setColumns(10);
    
    JLabel label_1 = new JLabel("");
    topPanel.add(label_1, "cell 3 0");
    
    this.cbCustomerPickup = new JCheckBox("Abholung");
    cbCustomerPickup.setFont(new Font("Times New Roman", Font.BOLD, 24));
    cbCustomerPickup.setBackground(new Color(209,222,235));
    this.cbCustomerPickup.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        boolean enabled = OnlineDeliverySelectionView.this.cbCustomerPickup.isSelected();
        OnlineDeliverySelectionView.this.setPickupEnable(enabled);
      } 
    }); 
    JLabel lblDeliveryDate = new JLabel("Liefer Datum:");
    topPanel.add(lblDeliveryDate, "cell 1 1,alignx trailing,growy");
    
    this.datePicker = new JXDatePicker();
    this.datePicker.setDate(new Date());
    datePicker.setBackground(new Color(209,222,235));
    topPanel.add(this.datePicker, "flowx,cell 2 1");
    topPanel.add(this.cbCustomerPickup, "cell 2 2");
    JPanel dpanel = new JPanel();
    dpanel.setLayout(new MigLayout());
    
    JLabel lblHour = new JLabel("Uhr:");
    dpanel.add(lblHour);
    tfHour = new FixedLengthTextField();
    tfHour.setDocument(new FixedLengthDocument(10));
    dpanel.add(tfHour);
    
    JLabel lblMin = new JLabel("Min:");
    dpanel.add(lblMin);
    tfMin = new FixedLengthTextField();
    tfMin.setDocument(new FixedLengthDocument(10));
    dpanel.add(tfMin);
    dpanel.setBackground(new Color(209,222,235));
    
    topPanel.add(dpanel, "cell 3 1");
    
    JLabel lblShippingAddress = new JLabel("Liefer Addresse:");
    topPanel.add(lblShippingAddress, "cell 1 3,alignx trailing,aligny top");
    
    JScrollPane scrollPane = new JScrollPane();
    scrollPane.setPreferredSize(new Dimension(3, 60));
    scrollPane.setBackground(new Color(209,222,235));
    topPanel.add(scrollPane, "cell 2 3,grow");
    
    this.taDeliveryAddress = new JTextArea();
    this.taDeliveryAddress.setRows(4);
    scrollPane.setViewportView(this.taDeliveryAddress);
    
    JLabel lblExtraInstruction = new JLabel("Besonderheiten:");
    topPanel.add(lblExtraInstruction, "cell 1 4,alignx trailing,aligny top");
    
    JScrollPane scrollPane_1 = new JScrollPane();
    topPanel.add(scrollPane_1, "cell 2 4,grow");
    
    this.taExtraInsruction = new JTextArea();
    this.taExtraInsruction.setPreferredSize(new Dimension(0, 40));
    this.taExtraInsruction.setRows(3);
    scrollPane_1.setViewportView(this.taExtraInsruction);
    
    JPanel panel_1 = new JPanel();
    topPanel.add(panel_1, "cell 1 5 2 1,shrinky 0,grow");
    
    this.btnCancel = new PosSmallButton();
    this.btnCancel.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        OnlineDeliverySelectionView.this.onlineDeliveryExtension.setCanceled(true);
        OnlineDeliverySelectionView.this.onlineDeliveryExtension.dispose();
      }
    });
    this.btnCancel.setText("ABBRECHEN");
    btnCancel.setBackground(new Color(255,153,153));
    panel_1.add(this.btnCancel);
    panel_1.setBackground(new Color(209,222,235));
    PosSmallButton btnSaveProceed = new PosSmallButton();
    btnSaveProceed.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        OnlineDeliverySelectionView.this.onlineDeliveryExtension.finishWizard();
      }
    });
    btnSaveProceed.setText("Speichern und Weiter");
    btnSaveProceed.setBackground(new Color(102,255,102));
    panel_1.add(btnSaveProceed);
    
    add(topPanel, "cell 0 0,grow");
    
    JPanel panel = new JPanel(new BorderLayout());
    add(panel, "cell 0 1,grow, gapright 2px");
    
    QwertyKeyPad qwertyKeyPad = new QwertyKeyPad();
    panel.add(qwertyKeyPad);
    panel.setBackground(new Color(209,222,235));
    if (this.onlineDeliveryExtension.getTicketType().equals(TicketType.PICKUP))
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
    if ((hour == -1) || (hour < 1) || (hour > 12)) {
      throw new PosException("Bitte wählen Sie Stunden 1-12.");
    }
    if ((min == -1) || (min < 1) || (min > 59)) {
      throw new PosException("Bitte wählen Minutenwert von 1 bis 59");
    }
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(10, hour);
    calendar.set(12, min);
    
    
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
  
  public String getExtraDeliveryInfo()
  {
    return this.taExtraInsruction.getText();
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
