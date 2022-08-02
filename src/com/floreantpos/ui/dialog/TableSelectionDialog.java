package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.floreantpos.IconFactory;
import com.floreantpos.POSConstants;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.ShopTable;

import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.ShopTableDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.TitlePanel;


public class TableSelectionDialog extends POSDialog implements ActionListener {
	private int defaultValue;

	private TitlePanel titlePanel;
	private JTextField tfNumber;
	private int selectedTable;
	boolean change;
	private DefaultListModel<ShopTable> addedTableListModel = new DefaultListModel<ShopTable>();
	Ticket ticket;
	private JList<ShopTable> addedTableList = new JList<ShopTable>(addedTableListModel);

	public TableSelectionDialog(boolean change) {
		this(Application.getPosWindow());
		this.change = change;
	}

	public TableSelectionDialog(Frame parent) {
		super(parent, true);
		init();
	}

	public TableSelectionDialog(Dialog parent) {
		super(parent, true);

		init();
	}
	public int getSelectedTable()
	{
		return selectedTable;
	}
	public void setSelectedTable(int selectedTable)
	{
		this.selectedTable = selectedTable;
	}
	
	public void selectTable(int number)
	{
		int tableNr = number;
		System.out.println(tableNr+"");
		setSelectedTable(tableNr);
		
		if (addTable(tableNr)) {
			doOk();
		}
		
	}
	
	private void init() {
		setResizable(false);
		selectedTable = 0;
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout(5, 5));

		renderTableList();
		this.setFocusable(true);
		
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
		    public boolean dispatchKeyEvent(KeyEvent e) {
		        boolean keyHandled = false;
		        if (e.getID() == KeyEvent.KEY_PRESSED) {
		           if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
		        	  dispose();    
		            }
		           else if(e.getKeyCode() == KeyEvent.VK_NUMPAD1 || e.getKeyCode() == KeyEvent.VK_1){selectTable(1);}
		           else if(e.getKeyCode() == KeyEvent.VK_NUMPAD2 || e.getKeyCode() == KeyEvent.VK_2){selectTable(2);}
		           else if(e.getKeyCode() == KeyEvent.VK_NUMPAD3 || e.getKeyCode() == KeyEvent.VK_3){selectTable(3);}
		           else if(e.getKeyCode() == KeyEvent.VK_NUMPAD4 || e.getKeyCode() == KeyEvent.VK_4){selectTable(4);}
		           else if(e.getKeyCode() == KeyEvent.VK_NUMPAD5 || e.getKeyCode() == KeyEvent.VK_5){selectTable(5);}
		           else if(e.getKeyCode() == KeyEvent.VK_NUMPAD6 || e.getKeyCode() == KeyEvent.VK_6){selectTable(6);}
		           else if(e.getKeyCode() == KeyEvent.VK_NUMPAD7 || e.getKeyCode() == KeyEvent.VK_8){selectTable(7);}
		           else if(e.getKeyCode() == KeyEvent.VK_NUMPAD8 || e.getKeyCode() == KeyEvent.VK_9){selectTable(8);}
		           else if(e.getKeyCode() == KeyEvent.VK_NUMPAD9 || e.getKeyCode() == KeyEvent.VK_9){selectTable(9);}
		           else if(e.getKeyCode() == KeyEvent.VK_ENTER){selectTable(99);}
		        }
		        return keyHandled;
		    }
		});
		titlePanel = new TitlePanel();
		titlePanel.setBackground(new Color(209,222,235));
		contentPane.add(titlePanel, BorderLayout.NORTH);

		JPanel keypadPanel = new JPanel(new MigLayout("fill"));
		keypadPanel.setBackground(new Color(209,222,235));
		tfNumber = new JTextField();
		
		tfNumber.setFont(tfNumber.getFont().deriveFont(Font.BOLD, 24));
		tfNumber.setFocusable(true);
		tfNumber.requestFocus();
		tfNumber.setBackground(Color.WHITE);
		keypadPanel.add(tfNumber, "span 2, grow");

		PosButton posButton = new PosButton(POSConstants.OK);
		posButton.setFocusable(false);
		posButton.setMinimumSize(new Dimension(25, 23));
		posButton.addActionListener(this);
		posButton.setFont(new Font("Times New Roman", Font.BOLD, 30));
		posButton.setBackground(new Color(102,255,102));
		keypadPanel.add(posButton, "growy,height 55,wrap, w 100!");

		String[][] numbers = { { "7", "8", "9" }, { "4", "5", "6" }, { "1", "2", "3" }, { "0", POSConstants.CLEAR } };
		String[][] iconNames = new String[][] { { "7_32.png", "8_32.png", "9_32.png" }, { "4_32.png", "5_32.png", "6_32.png" },
				{ "1_32.png", "2_32.png", "3_32.png" }, { "0_32.png", "clear_32.png" } };

		for (int i = 0; i < numbers.length; i++) {
			for (int j = 0; j < numbers[i].length; j++) {
				posButton = new PosButton();
				posButton.setFocusable(false);
				ImageIcon icon = IconFactory.getIcon(iconNames[i][j]);
				String buttonText = String.valueOf(numbers[i][j]);

				if (icon == null) {
					posButton.setText(buttonText);
				}
				else {
					posButton.setIcon(icon);
					if (POSConstants.CLEAR.equals(buttonText)) {
						posButton.setText(buttonText);
					}
				}
			
				posButton.setActionCommand(buttonText);
				posButton.addActionListener(this);
				String constraints = "grow,w 100!, height 80!";
				if (j == numbers[i].length - 1) {
					constraints += ", wrap";
				}
				keypadPanel.add(posButton, constraints);
			}
		}

		JPanel buttonPanel = new JPanel(new MigLayout("align 50% 50%"));
		//buttonPanel.add(new JSeparator(JSeparator.HORIZONTAL), "span 4, grow, gaptop 5");
		buttonPanel.setBackground(new Color(209,222,235));
		posButton = new PosButton("NEXT");
	
		//buttonPanel.add(posButton, "newline, w 80");

		posButton = new PosButton(POSConstants.CLEAR_ALL);
		posButton.setFocusable(false);
		posButton.addActionListener(this);
		posButton.setBackground(new Color(102,255,102));
		//keypadPanel.add(posButton);

		PosButton btnCancel = new PosButton(POSConstants.CANCEL);
		btnCancel.setFocusable(false);
		btnCancel.addActionListener(this);
		//buttonPanel.add(btnCancel, " w 80!");

		List<ShopTable> elements = getTables();
		String currentTableNumber = "";
		
		for(Iterator itr = elements.iterator();itr.hasNext();)
		{
			 ShopTable shopTable = (ShopTable) itr.next();
			 currentTableNumber = shopTable.getNumber();
			 break;
		}
		
		if(currentTableNumber.length() != 0)
			titlePanel.setTitle("Bearbeiten Tisch: " + currentTableNumber);
		else
			titlePanel.setTitle("Waehlen Sie Tisch Nr.");
		
		
		
		JPanel allTablesPanel = new JPanel();
		allTablesPanel.setLayout(new MigLayout());
		List<ShopTable> tableList = ShopTableDAO.getInstance().findAll();
		int index = 1;
		for(Iterator<ShopTable> itr = tableList.iterator();itr.hasNext();)
		{
			final PosButton button = new PosButton();
			ShopTable table = itr.next();
			button.setText(table.getNumber()+"");
			button.setPreferredSize(new Dimension(75,75));
			button.setBackground(new Color(102,255,102));
			ShopTable sTable ;
			final String tableNumber = table.getNumber()+"";
			if((sTable = ShopTableDAO.getInstance().getByNumber(tableNumber.trim())) != null)
			{
				if(sTable.isOccupied())
					button.setBackground(new Color(255,153,153));
			}
			
			
			if (index%9 == 0)
				allTablesPanel.add(button,"wrap");
			else
				allTablesPanel.add(button);
			button.setFont(new Font("Times New Roman",1,24));
			button.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					
					int tableNr = Integer.parseInt(tableNumber.trim());
					System.out.println(tableNr+"");
					setSelectedTable(tableNr);
					
					if (addTable(tableNr)) {
						doOk();
					}
					
				}
				
			});
			index++;
		}
		
		PosButton deliveryButton = new PosButton();
		deliveryButton.setText("99");
		deliveryButton.setFont(new Font("Times New Roman",1,40));
		deliveryButton.setBackground(new Color(102,178,255));
		deliveryButton.setPreferredSize(new Dimension(350,75));

		deliveryButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setSelectedTable(99);
				if (addTable(99)) {
					doOk();
				}
			}
			
		});

		PosButton pickupButton = new PosButton();
		pickupButton.setText("98");
		pickupButton.setFont(new Font("Times New Roman",1,40));
		pickupButton.setBackground(new Color(233,245,185));
		pickupButton.setPreferredSize(new Dimension(350,75));

		pickupButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setSelectedTable(98);
				if (addTable(98)) {
					doOk();
				}
			}
			
		});
	
		JPanel deliveryPanel = new JPanel();
		deliveryPanel.setBorder(new TitledBorder("LIEFERUNG"));
		deliveryPanel.add(pickupButton);
		deliveryPanel.add(deliveryButton);
		deliveryPanel.setBackground(new Color(209,222,235));	
		allTablesPanel.setBackground(new Color(209,222,235));
		contentPane.add(allTablesPanel,BorderLayout.WEST);
		if(TerminalConfig.isHomeDeliveryEnable())
			contentPane.add(deliveryPanel,BorderLayout.SOUTH);
		contentPane.setBackground(new Color(209,222,235));
	}

	private void renderTableList() {
	
		DefaultListCellRenderer renderer = new DefaultListCellRenderer() {
			Dimension preferredSize = new Dimension(60, 40);

			public java.awt.Component getListCellRendererComponent(javax.swing.JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel rendererComponent = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				rendererComponent.setHorizontalAlignment(JLabel.CENTER);
				rendererComponent.setPreferredSize(preferredSize);

				return rendererComponent;
			};
		};
		addedTableList.setCellRenderer(renderer);
		
		//tableListPanel.add(new JScrollPane(addedTableList));

		//getContentPane().add(tableListPanel, BorderLayout.WEST);
	}

	private void doOk() {
		setCanceled(false);
		dispose();
	}

	private boolean addTable(int tableNr) {
		String tableNumber = tableNr+"";
		tableNumber = tableNumber.trim();
		
		if (StringUtils.isEmpty(tableNumber)) {
			POSMessageDialog.showError(this, "Bitte Tisch nummer einfügen");
			return false;
		}
		
		if(change)
		{
			if(tableNr == 99 || tableNr == 98)
			{
				POSMessageDialog.showError(this, "Tisch wechseln zum 98/99 ist nicht moeglich");
				return false;
			}
				
		}

		Enumeration<ShopTable> elements = this.addedTableListModel.elements();
		
		String oldTableNumber ="";
		while (elements.hasMoreElements()) {
			 oldTableNumber = elements.nextElement().getNumber();
			 break;
		}
		ShopTable shopTable = ShopTableDAO.getInstance().getByNumber(tableNumber);
		
		if (shopTable == null) {
			shopTable = new ShopTable();
			shopTable.setNumber(tableNumber);
		}
		
		//Check for Edit/New ticket
		if(!change)
		{
			if (shopTable.isOccupied())//Its a Edit ticket 
			{
				change = false;
			    return true;
			}
			else
			{
				addedTableListModel.addElement(shopTable);//Create a new ticket
				change = false;
				return true;
			}
			
		}
		else
		{
			if (shopTable.isOccupied()) {
				POSMessageDialog.showError(this, "Tisch " + tableNumber + " belegt");
				return false;
			}
			ShopTable oldShopTable = ShopTableDAO.getInstance().getByNumber(oldTableNumber);
			if (oldShopTable != null)//Table change in a old ticket 
			{
				oldShopTable.setOccupied(false);
				addedTableListModel.removeElement(oldShopTable);
			}
			addedTableListModel.addElement(shopTable);
			change = false;
			return true;
		}
	}


	public void actionPerformed(ActionEvent e) {
	}

	public void setTitle(String title) {
		
		
		super.setTitle(title);
	}

	public void setDialogTitle(String title) {
		super.setTitle(title);
		
	}

	public List<ShopTable> getTables() {
		Enumeration<ShopTable> elements = this.addedTableListModel.elements();
		
		List<ShopTable> tables = new ArrayList<ShopTable>();

		while (elements.hasMoreElements()) {
			
			tables.add(elements.nextElement());
		}

		return tables;
	}
	
	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
		if(ticket == null) {
			return;
		}
		
		Set<ShopTable> tables = ticket.getTables();
		if(tables == null) return;
		String currentTableNumber ="";
		for (ShopTable shopTable : tables) {
			addedTableListModel.addElement(shopTable);
			currentTableNumber = shopTable.getNumber();
		}
		titlePanel.setTitle("Bearbeiten Tisch: " + currentTableNumber);
		
	}
	
	
}
