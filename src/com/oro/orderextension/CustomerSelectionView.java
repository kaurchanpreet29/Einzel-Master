package com.oro.orderextension;

import com.floreantpos.POSConstants;
import com.floreantpos.customer.CustomerListTableModel;
import com.floreantpos.customer.CustomerTable;
import com.floreantpos.main.Application;
import com.floreantpos.model.CallList;
import com.floreantpos.model.Customer;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.CallListDAO;
import com.floreantpos.model.dao.CustomerDAO; 
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.swing.POSTextField;
import com.floreantpos.swing.PosSmallButton;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.DeliveryHistoryDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.forms.CustomerForm;
import java.awt.BorderLayout;
import java.awt.Color;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

public class CustomerSelectionView
  extends JPanel
  implements WizardPage, KeyListener
{
  private WizardPage nextPage;
  private PosSmallButton btnCreateNewCustomer;
  private PosSmallButton btnRefresh;
  private CustomerTable customerTable;
  private POSTextField tfPhone;
  private POSTextField tfLoyaltyNo;
  private POSTextField tfName;
  private PosSmallButton btnInfo;
  protected Customer selectedCustomer;
  private POSTextField tfZip;
  private POSTextField tfAddress;
  private POSTextField tfBellName;
  private POSTextField tfFirmName;
  private JList callList;
  private JScrollPane scrollPane;
  private DefaultListModel listModel;
  private JPanel panel_4;
  private JPanel panel_5;
  private DeliverySelectionDialog deliverySelectionDialog;
  private JLabel lblCallList;
  public CustomerSelectionView()
  {
    createUI();
  }
  
  public CustomerSelectionView(DeliverySelectionDialog deliverySelectionDialog)
  {
    this.deliverySelectionDialog = deliverySelectionDialog;
    
    createUI();
  }
  
  public void populateList(DefaultListModel callList)
  {
	  java.util.List list  = CallListDAO.getInstance().findAll();
	  Collections.reverse(list);
	  for (Iterator itr = list.iterator(); itr.hasNext();)
	  {
		  CallList caller = (CallList)itr.next();
		  if (caller != null)
		  {
			  callList.addElement(caller.getPhone() + "(" + caller.getNumber() +") - " + caller.getTime());
		  }
	  }
	  Collections.reverse(list);
	  listModel.setSize(500);
	 
  }
  
  public void updateView()
  {
	  listModel.clear();
	  populateList(listModel);
	 	
	  callList.revalidate();
	  callList.repaint();
	 
	  scrollPane.revalidate();
	  scrollPane.repaint();
	  
  }
  private void createUI(){
	  	this.setBackground(new Color(209,222,235));
		setPreferredSize(new Dimension(1000, 2000));
		setLayout(new MigLayout("", "[650px,grow]", "[grow][][shrink 0,fill][grow][grow]"));
		this.setFocusable(true);
		panel_5 = new JPanel(new GridLayout());
		setBackground(new Color(209,222,235));
		panel_5.setBorder(new TitledBorder(null, "", TitledBorder.LEFT, TitledBorder.TOP, null, null));
		add(panel_5, "cell 0 0,grow");
		panel_5.setLayout(new MigLayout("", "[0px,grow]", "[grow][][][]"));
		panel_5.setBackground(new Color(209,222,235));
		listModel = new DefaultListModel();
		
		populateList(listModel);
		callList = new JList(listModel);
		callList.setFont(new Font("Times New Roman", Font.BOLD, 16));
		callList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	
	    callList.addListSelectionListener(new ListSelectionListener() {
	         @Override
	         public void valueChanged(ListSelectionEvent lse) {
	        	 
	        	 if (callList.getSelectedIndex() != -1)
	        		 doSearchCustomer();
	         }
	      });
	    
	    scrollPane = new JScrollPane(callList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    scrollPane.setPreferredSize(new Dimension(350,250));
	    //scrollPane.setPreferredSize(panel_5.getPreferredSize());
	    lblCallList = new JLabel();
		lblCallList.setText("ANRUFLISTE");
		lblCallList.setFont(new Font("Times New Roman",Font.BOLD,18));
		this.btnRefresh = new PosSmallButton();
		btnRefresh.setPreferredSize(new Dimension(10,10));
		btnRefresh.setFocusable(true);
		
		btnRefresh.setText("F5");
		
	    this.btnRefresh.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	        updateView();
	      }
	    });
	    
	    
	    panel_5.add(lblCallList, BorderLayout.NORTH);
	    panel_5.add(scrollPane, BorderLayout.WEST);
	    panel_5.add(btnRefresh,BorderLayout.NORTH);
		
		
		
		panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(null, "", TitledBorder.LEFT, TitledBorder.TOP, null, null));
		panel_4.setBackground(new Color(209,222,235));
		add(panel_4,"cell 0 0,grow");
		panel_4.setLayout(new MigLayout("", "[][][][][][]", "[][][][][][]"));

	    JLabel lblByAddress = new JLabel("Str.");
		panel_4.add(lblByAddress, "cell 0 0,alignx trailing");
		tfAddress = new POSTextField();
		panel_4.add(tfAddress, "cell 1 0");
		tfAddress.setColumns(16);

		tfAddress.addKeyListener(new KeyListener() {

			public void keyTyped(KeyEvent e){}
			@Override
			public void keyReleased(KeyEvent e) {
				
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					doSearchCustomer();
					clearAllContents();
				}
				else
				{
					doSearchCustomer();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		InputMap im = getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
		ActionMap am = getActionMap();
		
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "F5");
	    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "ESC");
	    
	    
	    am.put("F5", new ArrowAction("F5"));
	    am.put("ESC", new ArrowAction("ESC"));
		
	  
		JLabel lblByZipCode = new JLabel("PLZ");
		panel_4.add(lblByZipCode, "cell 2 0,alignx trailing");

		tfZip = new POSTextField();
		panel_4.add(tfZip, "cell 3 0");
		tfZip.setColumns(16);

	
		tfZip.addKeyListener(new KeyListener() {

			public void keyTyped(KeyEvent e){}
			@Override
			public void keyReleased(KeyEvent e) {
				
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					doSearchCustomer();
					clearAllContents();
				}
				else
				{
					doSearchCustomer();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});

		JLabel lblByBellName = new JLabel(POSConstants.BELL_NAME);
		//panel_4.add(lblByBellName, "cell 0 2,alignx trailing");

		tfBellName = new POSTextField();
		//panel_4.add(tfBellName, "cell 1 2");
		tfBellName.setColumns(16);

		tfBellName.addKeyListener(new KeyListener() {

			public void keyTyped(KeyEvent e){}
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					doSearchCustomer();
					clearAllContents();
				}
				else
				{
					doSearchCustomer();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});

		JLabel lblFirmName = new JLabel("Firma");
		panel_4.add(lblFirmName, "cell 0 2,alignx trailing");

		tfFirmName = new POSTextField();
		panel_4.add(tfFirmName, "cell 1 2");
		tfFirmName.setColumns(16);

		tfFirmName.addKeyListener(new KeyListener() {

			public void keyTyped(KeyEvent e){}
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					doSearchCustomer();
					clearAllContents();
				}
				else
				{
					doSearchCustomer();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});

		//JLabel lblNewLabel = new JLabel("");
		//panel_4.add(lblNewLabel, "cell 0 0 1 3,grow");

		JLabel lblByPhone = new JLabel("Telefon");
		panel_4.add(lblByPhone, "cell 0 1,alignx trailing");

		tfPhone = new POSTextField();
		panel_4.add(tfPhone, "cell 1 1");
		tfPhone.setColumns(16);

		JLabel lblByName = new JLabel("Kunden-Nr");
		panel_4.add(lblByName, "cell 2 2,alignx trailing");

		tfLoyaltyNo = new POSTextField();
		panel_4.add(tfLoyaltyNo, "cell 3 2");
		tfLoyaltyNo.setColumns(16);
		

		JLabel lblByEmail = new JLabel("Name");
		panel_4.add(lblByEmail, "cell 2 1,alignx trailing");

		tfName = new POSTextField();
		panel_4.add(tfName, "cell 3 1");
		tfName.setColumns(16);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new EmptyBorder(10, 0, 0, 0));
		panel_4.add(panel_2, "cell 0 4 4 4,growx");
		panel_2.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setFocusable(false);
		panel_2.add(scrollPane, BorderLayout.CENTER);

		customerTable = new CustomerTable();
		customerTable.setModel(new CustomerListTableModel());
		customerTable.setFocusable(false);
		customerTable.setRowHeight(35);
		customerTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		/*customerTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				selectedCustomer = customerTable.getSelectedCustomer();
				if (selectedCustomer != null) {
//					btnInfo.setEnabled(true);
				}
				else {
					btnInfo.setEnabled(false);
				}
			}
		});*/
		scrollPane.setViewportView(customerTable);
		scrollPane.setBackground(new Color(209,222,235));
		JPanel panel = new JPanel();
		panel_2.add(panel, BorderLayout.SOUTH);

		
	   
		btnInfo = new PosSmallButton();
		btnInfo.setFocusable(false);
		//panel.add(btnInfo);
		btnInfo.setEnabled(false);
		btnInfo.setText("DETAIL");

		PosSmallButton btnHistory = new PosSmallButton();
		btnHistory.setText("HISTORIE");
		btnHistory.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if(customerTable.getRowCount() == 0) return;
				Customer customer = customerTable.getSelectedCustomer();
				
				List<Ticket> list = TicketDAO.getInstance().findTktByCustomer(customer);
				DeliveryHistoryDialog dialog = null;
				try {
					dialog = new DeliveryHistoryDialog(list, null);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				dialog.pack();
    			dialog.open();
    		}
			
		});
		panel.add(btnHistory);
		
		PosSmallButton searchButton = new PosSmallButton();
		searchButton.setText("SUCHE");
		searchButton.setBackground(Color.ORANGE);
		panel.add(searchButton);
		
		searchButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				doSearchCustomer();				
			}
		
		});
		
		panel.setBackground(new Color(209,222,235));
		 
		 this.btnCreateNewCustomer = new PosSmallButton();
		    this.btnCreateNewCustomer.setFocusable(false);
		    panel.add(this.btnCreateNewCustomer);
		    this.btnCreateNewCustomer.addActionListener(new ActionListener()
		    {
		      public void actionPerformed(ActionEvent e)
		      {
		        try {
					CustomerSelectionView.this.createNewCustomer();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		      }
		    });
		   
		btnCreateNewCustomer.setText("NEU");
		btnCreateNewCustomer.setBackground(new Color(102,178,255));
		PosSmallButton btnSelect = new PosSmallButton();
		
	    btnSelect.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  try {
				editCustomer();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	      }
	    }); 
	    btnSelect.setText("WEITER");
	    btnSelect.setBackground( new Color(102,255,102));
	    panel.add(btnSelect);
	    
	    PosSmallButton btnCancel = new PosSmallButton();
	    btnCancel.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	        CustomerSelectionView.this.deliverySelectionDialog.setCanceled(true);
	        CustomerSelectionView.this.deliverySelectionDialog.dispose();
	      }
	    });
	    btnCancel.setText("ABBRECHEN");
	    btnCancel.setBackground(new Color(255,153,153));
	    panel.add(btnCancel);

		JPanel panel_3 = new JPanel(new BorderLayout());
		add(panel_3, "cell 0 1,grow, gapright 2px");

		com.floreantpos.swing.QwertyKeyPad qwertyKeyPad = new com.floreantpos.swing.QwertyKeyPad();
		qwertyKeyPad.setForeground(new Color(209,222,235));
		qwertyKeyPad.setBackground(new Color(209,222,235));
		
		panel_3.add(qwertyKeyPad);
		
		tfName.getDocument().addDocumentListener(new DocumentListener() {
			 	@Override
				public void changedUpdate(DocumentEvent arg0) {
//			 		doSearchCustomer();				
				}
				@Override
				public void insertUpdate(DocumentEvent e) {
//					doSearchCustomer();				
				}
				@Override
				public void removeUpdate(DocumentEvent e) {
//					doSearchCustomer();					
				}
		});
		
		
		tfLoyaltyNo.getDocument().addDocumentListener(new DocumentListener() {
		 	@Override
			public void changedUpdate(DocumentEvent arg0) {
		 		doSearchCustomer();				
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
//				doSearchCustomer();				
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
//				doSearchCustomer();					
			}
		});
		
		tfPhone.getDocument().addDocumentListener(new DocumentListener() {
		 	@Override
			public void changedUpdate(DocumentEvent arg0) {
		 		doSearchCustomer();				
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
//				doSearchCustomer();				
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
//				doSearchCustomer();					
			}
		});
		tfZip.getDocument().addDocumentListener(new DocumentListener() {
		 	@Override
			public void changedUpdate(DocumentEvent arg0) {
		 		doSearchCustomer();				
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
//				doSearchCustomer();				
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
//				doSearchCustomer();					
			}
		});
		
  }
  
  public List<Ticket> filterList(List<Ticket> list,String customerPhone, String customerName)
  {
	  List<Ticket> filterList = new ArrayList();
	  for(Iterator<Ticket> itr = list.iterator();itr.hasNext();)
	  {
		  Ticket ticket = itr.next();
		  String name = ticket.getProperty(Ticket.CUSTOMER_NAME);
		  String tel = ticket.getProperty(Ticket.CUSTOMER_PHONE);
		  if(((customerPhone != null) && (tel != null) && (customerPhone.compareTo(tel) == 0)) ||
				  ((customerName != null) && (name != null) &&(customerName.compareTo(name) == 0)))
		  {
			  filterList.add(ticket);
		  }
	  }
	  return filterList;
  }
  public class ArrowAction extends AbstractAction {

	    private String cmd;

	    public ArrowAction(String cmd) {
	        this.cmd = cmd;
	    }

	    @Override
	    public void actionPerformed(ActionEvent e) {
	        if (cmd.equalsIgnoreCase("F5"))
	        {
	        	updateView();
	        }
	    }
  }
  protected void clearAllContents()
	{
		tfPhone.setText("");
		tfLoyaltyNo.setText("");
		tfName.setText("");
		tfZip.setText("");
		tfBellName.setText("");
		tfFirmName.setText("");
	}

  protected void searchCustomer()
  {
    String phone = this.tfPhone.getText();
    String name = this.tfName.getText();
    String loyalty = this.tfLoyaltyNo.getText();
    if ((StringUtils.isEmpty(phone)) && (StringUtils.isEmpty(loyalty)) && (StringUtils.isEmpty(name)))
    {
      List<Customer> list = CustomerDAO.getInstance().findAll();
      this.customerTable.setModel(new CustomerListTableModel(list));
      return;
    }
    List<Customer> list = CustomerDAO.getInstance().findBy(phone, loyalty, name);
    this.customerTable.setModel(new CustomerListTableModel(list));
  }
  
  protected void createNewCustomer() throws ClassNotFoundException, SQLException
  {
	  CustomerForm form = null;
    if ((callList.getSelectedIndex() != -1 )&&(callList.getSelectedValue()) != null)
    {
    	String phone = callList.getSelectedValue().toString();
    	int index = phone.indexOf('(');
	    phone = phone.substring(0, index);
	    
    	 
    	form = new CustomerForm("0"+phone);
    }
    else
    {
    	form = new CustomerForm();
    }
    BeanEditorDialog dialog = new BeanEditorDialog(form, Application.getPosWindow(), true,true);
    dialog.open();
    if (!dialog.isCanceled())
    {
      this.selectedCustomer = ((Customer)form.getBean());
      this.deliverySelectionDialog.gotoNextPage();
    }
  }
  
  protected void editCustomer() throws ClassNotFoundException, SQLException
  {
	  if(customerTable.getSelectedRow() == -1)
		  return;
	  Customer customer = customerTable.getSelectedCustomer();
	  
	  CustomerForm form = new CustomerForm();
	  form.setBean(customer);
	  BeanEditorDialog dialog = new BeanEditorDialog(form, Application.getPosWindow(), true,true);
	  dialog.open();
	  if (!dialog.isCanceled())
	  {
	    this.selectedCustomer = ((Customer)form.getBean());
	    CustomerSelectionView.this.deliverySelectionDialog.gotoNextPage();
	  }
	  
  }
  public Customer getCustomer()
  {
    return this.selectedCustomer;
  }
  
  public void setNextPage(WizardPage nextPage)
  {
    this.nextPage = nextPage;
  }
  protected void doSearchCustomer() {
		String phone = tfPhone.getText().toUpperCase();
		if (phone.length() < 1)
		{
			if (callList.getSelectedValue() != null)
			{
				phone = callList.getSelectedValue().toString();
				int index = phone.indexOf('(');
			    phone = phone.substring(0, index);
			}
			    
		}
		
		String name = tfName.getText().toUpperCase();
		String loyalty = tfLoyaltyNo.getText();
		String zipCode = tfZip.getText();
		String Address = tfAddress.getText().toUpperCase();
		String bellName = tfBellName.getText().toUpperCase();
		String firmName = tfFirmName.getText().toUpperCase();
		if (StringUtils.isEmpty(phone) && StringUtils.isEmpty(loyalty) && StringUtils.isEmpty(name) && StringUtils.isEmpty(zipCode) && StringUtils.isEmpty(Address)&& StringUtils.isEmpty(bellName) && StringUtils.isEmpty(firmName)) {
			List<Customer> list = CustomerDAO.getInstance().findAll();
			customerTable.setModel(new CustomerListTableModel(list));
			if(list.size() > 0)
			{
				customerTable.setRowSelectionInterval(0, 0);
			}
			return;
		}

		List<Customer> list = CustomerDAO.getInstance().findBy(phone, loyalty, name,zipCode,Address,bellName,firmName,phone);
		customerTable.setModel(new CustomerListTableModel(list));
		if(list.size() > 0)
		{
			customerTable.setRowSelectionInterval(0, 0);
		}
	}

  
  public String getName()
  {
    return "C";
  }
  
  public boolean canGoNext()
  {
    return true;
  }
  
  public boolean canGoBack()
  {
    return false;
  }
  
  public WizardPage getPreviousPage()
  {
    return null;
  }
  
  public WizardPage getNextPage()
  {
    return this.nextPage;
  }
  
  public boolean canFinishWizard()
  {
    return true;
  }
  
  public boolean finish()
  {
    if (this.selectedCustomer == null)
    {
      POSMessageDialog.showError("Bitte wählen Sie einen Kunden aus");
      return false;
    }
    return true;
  }

@Override
public void keyPressed(KeyEvent arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void keyReleased(KeyEvent e) {
	if(e.getKeyCode() == KeyEvent.VK_F5)
	{
		updateView();
	}
}
@Override
public void keyTyped(KeyEvent arg0) {
	// TODO Auto-generated method stub
	
}
}
