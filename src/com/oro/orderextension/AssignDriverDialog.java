package com.oro.orderextension;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.swing.PosSmallButton;
import com.floreantpos.ui.dialog.POSMessageDialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.table.AbstractTableModel;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jdesktop.swingx.JXTable;

public class AssignDriverDialog
  extends JDialog
{
  private JTextField tfRecipientName;
  private JTextField tfDeliveryDate;
  private Ticket ticket;
  private JTextArea tfDeliveryAddress;
  private JTextArea tfExtraInstruction;
  private JXTable driverTable;
  private PosSmallButton btnCancel;
  private PosSmallButton btnSave;
  protected boolean canceled = true;
  
  public AssignDriverDialog(Frame parent)
  {
    super(parent, "Fahrer Zuweisen", true);
    createUI();
    
    setDefaultCloseOperation(2);
  }
  
  private void createUI()
  {
    getContentPane().setLayout(new MigLayout("", "[][grow]", "[][][60px,grow,shrink 0][60px,shrink 0][grow][shrink 0]"));
    
    JLabel lblRecipientName = new JLabel("Kundenname");
    getContentPane().add(lblRecipientName, "cell 0 0,alignx trailing");
    
    this.tfRecipientName = new JTextField();
    this.tfRecipientName.setEnabled(true);
    this.tfRecipientName.setEditable(false);
    this.tfRecipientName.setFocusable(false);
    tfRecipientName.setBackground(Color.WHITE);
    getContentPane().add(this.tfRecipientName, "cell 1 0,growx");
    this.tfRecipientName.setColumns(10);
    
    JLabel lblDeliveryDate = new JLabel("Lieferungdatum");
    getContentPane().add(lblDeliveryDate, "cell 0 1,alignx trailing");
    
    this.tfDeliveryDate = new JTextField();
    this.tfDeliveryDate.setEnabled(true);
    this.tfDeliveryDate.setEditable(false);
    this.tfDeliveryDate.setFocusable(false);
    tfDeliveryDate.setBackground(Color.WHITE);
    getContentPane().add(this.tfDeliveryDate, "cell 1 1,growx");
    this.tfDeliveryDate.setColumns(10);
    
    JLabel lblDeliveryAddress = new JLabel("Lieferungaddresse");
    getContentPane().add(lblDeliveryAddress, "cell 0 2,alignx trailing");
    
    this.tfDeliveryAddress = new JTextArea();
    this.tfDeliveryAddress.setEnabled(true);
    this.tfDeliveryAddress.setEditable(false);
    this.tfDeliveryAddress.setFocusable(false);
    tfDeliveryAddress.setBackground(Color.WHITE);
    this.tfDeliveryAddress.setBorder(new LineBorder(Color.LIGHT_GRAY));
    this.tfDeliveryAddress.setRows(4);
    getContentPane().add(this.tfDeliveryAddress, "cell 1 2,grow");
    
    JLabel lblExtraInstruction = new JLabel("Zusaetzlicheinformation");
    getContentPane().add(lblExtraInstruction, "cell 0 3,alignx trailing");
    
    this.tfExtraInstruction = new JTextArea();
    this.tfExtraInstruction.setEnabled(true);
    this.tfExtraInstruction.setEditable(false);
    this.tfExtraInstruction.setFocusable(true);
    this.tfExtraInstruction.setBorder(new LineBorder(Color.LIGHT_GRAY));
    this.tfExtraInstruction.setRows(4);
    tfExtraInstruction.setBackground(Color.WHITE);
    
    
    getContentPane().add(this.tfExtraInstruction, "cell 1 3,grow");
    
    JPanel panel = new JPanel();
    getContentPane().add(panel, "cell 0 4 2 1,grow");
    panel.setLayout(new BorderLayout(0, 0));
    
    JScrollPane scrollPane = new JScrollPane();
    panel.add(scrollPane, "Center");
    
    this.driverTable = new JXTable();
    scrollPane.setViewportView(this.driverTable);
    scrollPane.getViewport().setBackground(new Color(209,222,235));
    JPanel panel_1 = new JPanel();
    getContentPane().add(panel_1, "cell 0 5 2 1,grow");
    
    this.btnCancel = new PosSmallButton();
    this.btnCancel.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        AssignDriverDialog.this.canceled = true;
        AssignDriverDialog.this.dispose();
      }
    });
    this.btnCancel.setText("Abbrechen");
    btnCancel.setBackground(new Color(255,153,153));
    panel_1.add(this.btnCancel);
    
    this.btnSave = new PosSmallButton();
    this.btnSave.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        AssignDriverDialog.this.saveTicket();
      }
    });
    this.btnSave.setText("Speichern");
    btnSave.setBackground(new Color(102,255,102));
    panel_1.setBackground(new Color(209,222,235));
    panel_1.add(this.btnSave);
    
    this.driverTable.setModel(new DriverTableModel());
    this.driverTable.setFocusable(false);
    this.driverTable.getTableHeader().setBackground(new Color(209,222,235));
    this.driverTable.setRowHeight(35);
    this.driverTable.getSelectionModel().setSelectionMode(0);
    getContentPane().setBackground(new Color(209,222,235));
    setBackground(new Color(209,222,235));
  }
  
  protected void saveTicket()
  {
    int selectedRow = this.driverTable.getSelectedRow();
    if (selectedRow < 0)
    {
      POSMessageDialog.showError("Bitte wählen Sie einen Fahrer aus");
      return;
    }
    DriverTableModel model = (DriverTableModel)this.driverTable.getModel();
    User driver = model.getDriver(selectedRow);
   
    this.ticket.setAssignedDriver(driver);
    
    Session session = TicketDAO.getInstance().createNewSession();
    Transaction transaction = null;
    try
    {
      transaction = session.beginTransaction();
      session.saveOrUpdate(this.ticket);
      
      transaction.commit();
      sendSMS(driver.getPhoneNo(),ticket.getProperty(Ticket.CUSTOMER_PHONE));
      this.canceled = false;
      dispose();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      POSMessageDialog.showError(e.getMessage());
      if (transaction != null) {
        transaction.rollback();
      }
    }
    finally
    {
      session.close();
    }
  }
  
  public void setData(Ticket ticket, List<User> drivers)
  {
    this.ticket = ticket;
    if (StringUtils.isNotEmpty(ticket.getProperty("CUSTOMER_NAME"))) {
      this.tfRecipientName.setText(ticket.getProperty("CUSTOMER_NAME"));
    }
    if (ticket.getDeliveryDate() != null) {
      this.tfDeliveryDate.setText(ticket.getDeliveryDate().toString());
    }
    this.tfDeliveryAddress.setText(ticket.getDeliveryAddress());
    this.tfExtraInstruction.setText("");
    
    List<User> newDriversList = new ArrayList();
    for(Iterator<User> itr = drivers.iterator();itr.hasNext();)
    {
    	User user = itr.next();
    	if(user.isAvailableForDelivery())
    		newDriversList.add(user);
    }
    this.driverTable.setModel(new DriverTableModel(newDriversList));
  }
  
  class DriverTableModel
    extends AbstractTableModel
  {
    private final String[] columns = { "Fahrer Rufnummer", "Name", "Verfuegbar" };
    private List<User> drivers;
    
    public DriverTableModel() {}
    
    public DriverTableModel(List<User> customers)
    {
      this.drivers = customers;
    }
    
    public int getRowCount()
    {
      if (this.drivers == null) {
        return 0;
      }
      return this.drivers.size();
    }
    
    public int getColumnCount()
    {
      return this.columns.length;
    }
    
    public String getColumnName(int column)
    {
      return this.columns[column];
    }
    
    public Object getValueAt(int rowIndex, int columnIndex)
    {
      if (this.drivers == null) {
        return null;
      }
      User driver = (User)this.drivers.get(rowIndex);
      switch (columnIndex)
      {
      case 0: 
        return driver.getPhoneNo();
      case 1: 
        return driver.getFirstName() + " " + driver.getLastName();
      case 2: 
        return driver.isAvailableForDelivery().booleanValue() ? "Ja" : "Nein";
      }
      return null;
    }
    
    public List<User> getDrivers()
    {
      return this.drivers;
    }
    
    public User getDriver(int index)
    {
      if (this.drivers == null) {
        return null;
      }
      if ((index < 0) || (index >= this.drivers.size())) {
        return null;
      }
      return (User)this.drivers.get(index);
    }
  }
  
  public void sendSMS(String driverPhoneNumber,String customerPhone) throws ClientProtocolException, IOException
  {
	    String username = TerminalConfig.getMessageUserName();
	    String password = TerminalConfig.getMessagePassword();
	    String textMessage = "Thank%20you";
	    try
	    {
	    	if (TerminalConfig.isMessageEnabled())
	    	{
	    		DefaultHttpClient httpclient = new DefaultHttpClient();
	    		HttpResponse response;
	    		String url ="http://www.smsout.de/client/sendsms.php?Username="+username+"&Password="+password+"&SMSTo="+customerPhone+"&SMSType=V4&SMSText="+textMessage;
	       		HttpPost httpost = new HttpPost(url);
	    		response = httpclient.execute(httpost);
	    	}
	    }
	    catch(Exception e){System.out.println(e);}
  }
  public boolean isCanceled()
  {
    return this.canceled;
  }
}
