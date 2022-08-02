package com.oro.orderextension;

import com.floreantpos.POSConstants;
import com.floreantpos.customer.CustomerListTableModel;
import com.floreantpos.customer.CustomerTable;
import com.floreantpos.isdnmonitor.CallMon;
import com.floreantpos.main.Application;
import com.floreantpos.model.Customer;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.CustomerDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.swing.POSTextField;
import com.floreantpos.swing.PosSmallButton;
import com.floreantpos.swing.QwertyKeyPad;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.forms.CustomerForm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

public class OnlineCustomerSelectionView
  extends JPanel
  implements WizardPage, KeyListener
{
  private WizardPage nextPage;
  private PosSmallButton btnCreateNewCustomer;
  private PosSmallButton btnRefresh;
  private PosSmallButton btnShowDelete;
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
  private JList orderList;
  private JScrollPane scrollPane;
  private DefaultListModel listModel;
  private JPanel panel_4;
  private JPanel panel_5;
  private OnlineDeliveryExtension onlineDeliveryExtension;
  private JLabel lblOrderList;
  public OnlineCustomerSelectionView()
  {
    createUI();
  }
  
  public OnlineCustomerSelectionView(OnlineDeliveryExtension onlineDeliveryExtension)
  {
    this.onlineDeliveryExtension = onlineDeliveryExtension;
    
    createUI();
  }
  
  public void populateList(DefaultListModel orderList)
  {}
  
  public void populateList(DefaultListModel orderList, int type)
  {}
  public void updateView()
  {
	  listModel.clear();
	  populateList(listModel);
	 	
	  orderList.revalidate();
	  orderList.repaint();
	 
	  scrollPane.revalidate();
	  scrollPane.repaint();
	  
  }
  public void updateView(int type)
  {
	  listModel.clear();
	  populateList(listModel,1);
	 	
	  orderList.revalidate();
	  orderList.repaint();
	 
	  scrollPane.revalidate();
	  scrollPane.repaint();
	  
  }
  private void createUI(){

	  	setBackground(new Color(209,222,235));
		setPreferredSize(new Dimension(1000, 2000));
		setLayout(new MigLayout("", "[650px,grow]", "[grow][][shrink 0,fill][grow][grow]"));
		this.setFocusable(true);
		panel_5 = new JPanel(new GridLayout());
		panel_5.setBorder(new TitledBorder(null, "", TitledBorder.LEFT, TitledBorder.TOP, null, null));
		add(panel_5, "cell 0 0,grow");
		panel_5.setLayout(new MigLayout("", "[0px,grow]", "[grow][][][]"));
		panel_5.setBackground(new Color(209,222,235));
		listModel = new DefaultListModel();
		
		populateList(listModel);
		orderList = new JList(listModel);
		orderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	
		orderList.addListSelectionListener(new ListSelectionListener() {
	         @Override
	         public void valueChanged(ListSelectionEvent lse) {
	        	 
	        	 if (orderList.getSelectedIndex() != -1)
	        		 doSearchCustomer(orderList.getSelectedValue().toString());
	         }
	      });
	    
	    scrollPane = new JScrollPane(orderList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    scrollPane.setPreferredSize(new Dimension(350,250));
	    //scrollPane.setPreferredSize(panel_5.getPreferredSize());
	    lblOrderList = new JLabel();
		lblOrderList.setText("BESTELLEN LISTE");
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
	    
		this.btnShowDelete = new PosSmallButton();
		btnShowDelete.setPreferredSize(new Dimension(10,10));
		btnShowDelete.setFocusable(true);
		
		btnShowDelete.setText("Alt");
		
	    this.btnShowDelete.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	        updateView(1);
	      }
	    });

	    
	    panel_5.add(lblOrderList, BorderLayout.NORTH);
	    panel_5.add(scrollPane, BorderLayout.WEST);
	    panel_5.add(btnRefresh,BorderLayout.NORTH);
	    panel_5.add(btnShowDelete,BorderLayout.SOUTH);
		
		
		panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(null, "", TitledBorder.LEFT, TitledBorder.TOP, null, null));
		add(panel_4,"cell 0 0,grow");
		panel_4.setLayout(new MigLayout("", "[][][][][][]", "[][][][][][]"));

	    JLabel lblByAddress = new JLabel("Addresse Suche");
		panel_4.add(lblByAddress, "cell 0 0,alignx trailing");
		tfAddress = new POSTextField();
		panel_4.add(tfAddress, "cell 1 0");
		panel_4.setBackground(new Color(209,222,235));
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
		this.addKeyListener(new KeyListener() {

			public void keyTyped(KeyEvent e){}
			@Override
			public void keyReleased(KeyEvent e) {
				
				if(e.getKeyCode() == KeyEvent.VK_F5)
				{
					updateView();
				}
				else if (e.getKeyCode() == KeyEvent.VK_ALT)
				{
					updateView(1);
				}
				else if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
				{
					OnlineCustomerSelectionView.this.onlineDeliveryExtension.setCanceled(true);
			        OnlineCustomerSelectionView.this.onlineDeliveryExtension.dispose();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});

		JLabel lblByZipCode = new JLabel("Postleitzahl");
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
		panel_4.add(lblByBellName, "cell 0 2,alignx trailing");

		tfBellName = new POSTextField();
		panel_4.add(tfBellName, "cell 1 2");
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

		JLabel lblFirmName = new JLabel("Firma Name");
		panel_4.add(lblFirmName, "cell 0 3,alignx trailing");

		tfFirmName = new POSTextField();
		panel_4.add(tfFirmName, "cell 1 3");
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
		panel_4.setBackground(new Color(209,222,235));
		JLabel lblByPhone = new JLabel("Telfon");
		panel_4.add(lblByPhone, "cell 0 1,alignx trailing");

		tfPhone = new POSTextField();
		panel_4.add(tfPhone, "cell 1 1");
		tfPhone.setColumns(16);

		JLabel lblByName = new JLabel("Kunden Nr");
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
		customerTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

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
		});
		scrollPane.setViewportView(customerTable);

		JPanel panel = new JPanel();
		panel_2.add(panel, BorderLayout.SOUTH);
		panel_2.setBackground(new Color(209,222,235));
		
	   
		btnInfo = new PosSmallButton();
		btnInfo.setFocusable(false);
		//panel.add(btnInfo);
		btnInfo.setEnabled(false);
		btnInfo.setText("DETAIL");

		PosSmallButton btnHistory = new PosSmallButton();
		btnHistory.setEnabled(false);
		btnHistory.setText("HISTORY");
		//panel.add(btnHistory);
		
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
		    	  if (orderList.getSelectedIndex() != -1)
		    	  {} else
					try {
						OnlineCustomerSelectionView.this.createNewCustomer();
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					} catch (SQLException e1) {
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
	    	  if (orderList.getSelectedIndex() != -1)
	    	  {}
	    	  try {
				editCustomer();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
	      }
	    });
	    btnSelect.setText("AUSWAEHLEN");
	    panel.add(btnSelect);
	    btnSelect.setBackground(new Color(102,255,102)); 
	    
	    PosSmallButton btnCancel = new PosSmallButton();
	    btnCancel.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	        OnlineCustomerSelectionView.this.onlineDeliveryExtension.setCanceled(true);
	        OnlineCustomerSelectionView.this.onlineDeliveryExtension.dispose();
	      }
	    });
	    btnCancel.setText("ABBRECHEN");
	    panel.add(btnCancel);
	    btnCancel.setBackground(new Color(255,153,153));

		JPanel panel_3 = new JPanel(new BorderLayout());
		panel_3.setBackground(new Color(209,222,235));
		add(panel_3, "cell 0 1,grow, gapright 2px");

		com.floreantpos.swing.QwertyKeyPad qwertyKeyPad = new com.floreantpos.swing.QwertyKeyPad();
		panel_3.add(qwertyKeyPad);
		
		tfName.getDocument().addDocumentListener(new DocumentListener() {
			 	@Override
				public void changedUpdate(DocumentEvent arg0) {
			 		doSearchCustomer();				
				}
				@Override
				public void insertUpdate(DocumentEvent e) {
					doSearchCustomer();				
				}
				@Override
				public void removeUpdate(DocumentEvent e) {
					doSearchCustomer();					
				}
		});
		
		
		tfLoyaltyNo.getDocument().addDocumentListener(new DocumentListener() {
		 	@Override
			public void changedUpdate(DocumentEvent arg0) {
		 		doSearchCustomer();				
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				doSearchCustomer();				
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				doSearchCustomer();					
			}
		});
		
		tfPhone.getDocument().addDocumentListener(new DocumentListener() {
		 	@Override
			public void changedUpdate(DocumentEvent arg0) {
		 		doSearchCustomer();				
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				doSearchCustomer();				
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				doSearchCustomer();					
			}
		});
		tfZip.getDocument().addDocumentListener(new DocumentListener() {
		 	@Override
			public void changedUpdate(DocumentEvent arg0) {
		 		doSearchCustomer();				
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				doSearchCustomer();				
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				doSearchCustomer();					
			}
		});
		
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
  protected void createNewCustomer() throws ClassNotFoundException, SQLException
  {
	CustomerForm form = null;
    form = new CustomerForm();
    BeanEditorDialog dialog = new BeanEditorDialog(form, Application.getPosWindow(), true);
    dialog.open();
    if (!dialog.isCanceled())
    {
      this.selectedCustomer = ((Customer)form.getBean());
      this.onlineDeliveryExtension.gotoNextPage();
    }
  }
  
  protected void createNewCustomer(com.floreantpos.onlineOrder.Customer customer) throws ClassNotFoundException, SQLException
  {
	CustomerForm form =  new CustomerForm(customer); 
    BeanEditorDialog dialog = new BeanEditorDialog(form, Application.getPosWindow(), true,true);
    dialog.open();
    if (!dialog.isCanceled())
    {
      this.selectedCustomer = ((Customer)form.getBean());
      this.onlineDeliveryExtension.gotoNextPage();
    }
  }
  
  protected void editCustomer() throws ClassNotFoundException, SQLException
  {
	  if(customerTable.getSelectedRow() == -1)
		  return;
	  Customer customer = customerTable.getSelectedCustomer();
	  
	  CustomerForm form = new CustomerForm();
	  form.setBean(customer);
	  BeanEditorDialog dialog = new BeanEditorDialog(form, Application.getPosWindow(), true);
	  dialog.open();
	  if (!dialog.isCanceled())
	  {
	    this.selectedCustomer = ((Customer)form.getBean());
	    OnlineCustomerSelectionView.this.onlineDeliveryExtension.gotoNextPage();
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
			if (orderList.getSelectedValue() != null)
				phone = orderList.getSelectedValue().toString();
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
			return;
		}

		List<Customer> list = CustomerDAO.getInstance().findBy(phone, loyalty, name,zipCode,Address,bellName,firmName,phone);
		customerTable.setModel(new CustomerListTableModel(list));
	}

  protected void doSearchCustomer(String fileName) {}
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
