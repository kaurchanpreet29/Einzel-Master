package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.commons.lang.StringUtils;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.explorer.ListTableModel;
import com.floreantpos.customer.CustomerListTableModel;
import com.floreantpos.model.Customer;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.Tax;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketType;
import com.floreantpos.model.dao.CustomerDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosSmallButton;
import com.floreantpos.swing.QwertyKeyPad;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.model.MenuItemForm;
import com.floreantpos.util.NumberUtil;



public class ItemSearchDialog extends POSDialog{
	private MenuItemExplorerTableModel tableModel;
	public JTextField tfSearch;
	public JTextField tfCount;
	public JTextField tfBarcode;
	public JTextField tfName;
	public JTextField tfWeight;
	public JTextField tfPrice;
	public JButton btnUpdate;
	JCheckBox cbUpdate = new JCheckBox();
	private int count;

	public int getCount() {
		return count;
	}
	private JTable table;
	private List<MenuItem> itemList;
	private PosButton cancelButton;
	private PosButton okButton;
	private MenuItem item;
	private String ticketType;
	private int priceCategory;
	private boolean all = false;
	JCheckBox noPriceCategory;
	public ItemSearchDialog(String tickettype, int priceCategory)
	{
		setLayout(new BorderLayout());
		this.priceCategory = priceCategory;
		this.ticketType = tickettype;
		itemList = new ArrayList();
		noPriceCategory = new JCheckBox(POSConstants.ALL);
		noPriceCategory.setFont(new Font("Times New Roman", Font.BOLD, 22));
		noPriceCategory.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				all = noPriceCategory.isSelected();
				
			}
		});
		JPanel topPanel = new JPanel();
		topPanel.setBackground(new Color(209,222,235));
		JPanel southPanel = new JPanel();

		southPanel.setBackground(new Color(209,222,235));
		getContentPane().setBackground(new Color(209,222,235));
		southPanel.setLayout(new MigLayout());
		topPanel.setLayout(new MigLayout());
		topPanel.setPreferredSize(new Dimension(800,140));
		tableModel = new MenuItemExplorerTableModel();
		table = new JTable(tableModel);
		table.setRowHeight(50);
		table.setDefaultRenderer(Object.class, new PosTableRenderer());
		
		
		DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
		leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

		table.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
		table.getColumnModel().getColumn(1).setCellRenderer(otherRenderer);
		table.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);
		table.getColumnModel().getColumn(3).setCellRenderer(otherRenderer);
		table.getColumnModel().getColumn(4).setCellRenderer(otherRenderer);
		

		table.getColumnModel().getColumn(2).setMinWidth(300);
		table.getColumnModel().getColumn(0).setMinWidth(80);
		table.getColumnModel().getColumn(1).setMinWidth(100);
		table.getColumnModel().getColumn(3).setMinWidth(100);
		table.getColumnModel().getColumn(4).setMinWidth(80);

		
		
		table.getTableHeader().setBackground(new Color(209,222,235));
		JScrollPane jScrollPane = new JScrollPane(table);
		jScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(30,20));
		jScrollPane.getVerticalScrollBar().setBackground(Color.WHITE);
		jScrollPane.setPreferredSize(new Dimension(1000,600));
		southPanel.add(jScrollPane);
		okButton = new PosButton("   OK   ");
		okButton.setBackground(new Color(102,255,102));
		table.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent me) {
				if (me.getClickCount() == 2)
				{
					okActionPerformed();
					dispose();
				}
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

		});
		okButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				okActionPerformed();
				dispose();
			}
		});
		cancelButton = new PosButton("ABBRECHEN");
		cancelButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				setCanceled(false);
			}
		});

		//		southPanel.add(okButton);
		//		southPanel.add(cancelButton);
		cancelButton.setBackground(new Color(255,102,102));
		jScrollPane.getViewport().setBackground(new Color(209,222,235));		
		tfSearch = new JTextField(20);
		tfSearch.setFocusable(true);		
		tfCount = new JTextField(2);
		tfCount.setText("1");
		topPanel.add(tfCount,"cell 0 0,growx");
		topPanel.add(new JLabel("X"),"cell 1 0");
		topPanel.add(tfSearch,"cell 2 0,growx");
		topPanel.add(noPriceCategory,"cell 3 0,growx");
		topPanel.add(okButton,"cell 4 0,growx");
		topPanel.add(cancelButton,"cell 5 0,wrap,growx");

		JPanel editPanel = new JPanel();
		editPanel.setBackground(new Color(209,222,235));
		editPanel.setLayout(new MigLayout());
		tfBarcode = new JTextField(10);
		tfName = new JTextField(20);
		tfWeight = new JTextField(5);
		tfPrice = new JTextField(7);
		
		tfBarcode.setEnabled(false);
		tfName.setEnabled(false);
		tfWeight.setEnabled(false);
		tfPrice.setEnabled(false);
		JButton btnBarcode = new JButton();
		btnBarcode.setText("Barcode :");
		btnBarcode.setBackground(new Color(209,222,235));
		btnBarcode.setEnabled(false);
		btnBarcode.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getClickCount()==2) {
					int randomNumber = 0;
					while (true) {
						Random randomGenerator = new Random();
						randomNumber = randomGenerator.nextInt(2147483640);
						if (randomNumber < 0)
							continue;	
						if (MenuItemDAO.getInstance().findByBarcode(randomNumber + "").size() > 0)
							continue;
						else
							break;
					}
					tfBarcode.setText(randomNumber + "");
				}
				
				
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
				
			}
		});
//		btnBarcode.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				
//			}
//
//		});

		btnUpdate = new PosSmallButton("Edit");
		btnUpdate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(cbUpdate.isSelected()) {
					tfBarcode.setEnabled(false);
					tfName.setEnabled(false);
					tfWeight.setEnabled(false);
					tfPrice.setEnabled(false);
					btnUpdate.setText("Edit");
					cbUpdate.setSelected(false);
					btnBarcode.setEnabled(false);
					doUpdateMenu();	
				}else {					
					tfBarcode.setEnabled(true);
					tfName.setEnabled(true);
					tfWeight.setEnabled(true);
					tfPrice.setEnabled(true);
					doPopulateMenu();
					btnUpdate.setText("Update");
					cbUpdate.setSelected(true);
					btnBarcode.setEnabled(true);
				}
			}
		});
		btnUpdate.setBackground(new Color(50, 168, 82));
		btnUpdate.setFont(new Font("Times New Roman", Font.PLAIN,20));
		cbUpdate.setBackground(new Color(209,222,235));

		tfBarcode.setFont(new Font("Times New Roman", Font.PLAIN,22));
		tfName.setFont(new Font("Times New Roman", Font.PLAIN,22));
		tfName.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				String text = tfName.getText();
				if (text.length() == 1) {
					text = text.toUpperCase();
					tfName.setText(text);
				}

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		});
		tfWeight.setFont(new Font("Times New Roman", Font.PLAIN,22));
		tfPrice.setFont(new Font("Times New Roman", Font.PLAIN,22));
//		editPanel.add(cbUpdate);
		editPanel.add(btnBarcode);
		editPanel.add(tfBarcode);
		editPanel.add(new JLabel("Name: "));
		editPanel.add(tfName);
		editPanel.add(new JLabel("Preis (cents): "));
		editPanel.add(tfPrice);
		editPanel.add(new JLabel("  Gewicht (g): "));
		editPanel.add(tfWeight);
		editPanel.add(btnUpdate);
		topPanel.add(editPanel, "cell 0 1 5 0,wrap,growx");

		//		QwertyKeyPad qwerty = new QwertyKeyPad();
		//		topPanel.add(qwerty);
		getContentPane().add(topPanel,BorderLayout.NORTH);
		getContentPane().add(southPanel,BorderLayout.CENTER);
		tfCount.setFont(new Font("Times New Roman", Font.PLAIN,26));

		tfSearch.setFont(new Font("Times New Roman", Font.PLAIN,26));
		loadAllItem();

		tfSearch.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent arg0) {
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				if(tfSearch.getText().length() == 0)
				{
					loadAllItem();
				}
				else
				{
					String text = tfSearch.getText();
					searchItem(text);				
				}
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				if(tfSearch.getText().length() == 0)
				{
					loadAllItem();
				}
				else
				{
					String text = tfSearch.getText();
					searchItem(text);				
				}
			}
		});
	}
	
	protected void doPopulateMenu() {
		int index = table.getSelectedRow();
		if (index < 0)
		{
			JOptionPane.showMessageDialog(null, "Bitte Produkt waehlen Sie ein!!!");
			return;
		}

		MenuItem menuItem = itemList.get(index);
		tfBarcode.setText(menuItem.getBarcode());
		tfName.setText(menuItem.getName());
		
		
		if(menuItem.getWeightgrams()!=null) {
			String weight =String.valueOf(menuItem.getWeightgrams()*100);
			weight = weight.substring(0, weight.indexOf("."));
			tfWeight.setText(weight);
		}
			
		String price =String.valueOf(menuItem.getPrice()*100);
		price = price.substring(0, price.indexOf("."));
		tfPrice.setText(price);		
	}
	
	protected void doUpdateMenu() {
		boolean update = false;
		int index = table.getSelectedRow();
		if (index < 0)
		{
			JOptionPane.showMessageDialog(null, "Bitte Produkt waehlen Sie ein!!!");
			return;
		}

		MenuItem menuItem = itemList.get(index);
		String name = tfName.getText();
		String barcode = tfBarcode.getText();
		String weight = tfWeight.getText();
		String preis = tfPrice.getText();
		
		boolean search = false;
		if(!StringUtils.isEmpty(name)) {
			menuItem.setName(name);
			update = true;
			search = true;
		}
		if(!StringUtils.isEmpty(barcode)) {
			
			menuItem.setBarcode(barcode);
			update = true;
		}
		if(!StringUtils.isEmpty(weight)) {
			Double  weightGrams = 1000.0;
			try {
				weightGrams = Double.parseDouble(weight);
			}catch(Exception ex) {
			JOptionPane.showMessageDialog(null, "Ungultig  Gewicht Zahl");	
			}
			menuItem.setWeightgrams(weightGrams);
			update = true;
		}
		if(!StringUtils.isEmpty(preis)) {
			Double  price = menuItem.getPrice();
			try {
				price = Double.parseDouble(preis.replace(",", "."));
				price = price/100;
			}catch(Exception ex) {
				JOptionPane.showMessageDialog(null, "Ungultig  Preis Zahl");		
			}
			menuItem.setPrice(price);
			update = true;
		}
		if(update) {
			MenuItemDAO.getInstance().saveOrUpdate(menuItem);
			tfBarcode.setText("");
			tfName.setText("");
			tfWeight.setText("");
			tfPrice.setText("");
			cbUpdate.setSelected(false);
			tfBarcode.setEnabled(false);
			tfName.setEnabled(false);
			tfWeight.setEnabled(false);
			tfPrice.setEnabled(false);
			String text = tfSearch.getText();
			if(!StringUtils.isEmpty(text))
				searchItem(text);	
			else if(search)
				searchItem(name);	
		}
		
	}
	
	
	
	public void okActionPerformed()
	{
		int index = table.getSelectedRow();
		if (index < 0)
		{
			return;
		}

		MenuItem menuItem = itemList.get(index);
		item = menuItem;
		count = Integer.parseInt(tfCount.getText());
	}
	public MenuItem getMenuItem()
	{
		return item;
	}
	DefaultTableCellRenderer otherRenderer = new DefaultTableCellRenderer() {
		Font font = new Font("Times New Roman", Font.BOLD, 16);

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			//			setFont(font);
			this.setHorizontalAlignment(SwingConstants.CENTER);
			return this;
		}

	};

	public void searchItem(String itemId)
	{
		MenuItemDAO dao = new MenuItemDAO();
		itemList = dao.findAll();
		try
		{
			Integer.parseInt(itemId);
			List<MenuItem> tempList = new ArrayList(); 
			for(Iterator<MenuItem> itr = itemList.iterator();itr.hasNext();)
			{
				MenuItem menuItem = itr.next();
				
				if((menuItem.getItemId().compareTo(itemId) == 0) && (menuItem.getParent().getParent().getType().compareTo(ticketType) == 0)&&all)
				{
					tempList.add(menuItem);
					System.out.println("Added............."+ menuItem.getName() + ", "+ menuItem.getParent().getParent().getType());
				}else if((menuItem.getItemId().compareTo(itemId) == 0) && (menuItem.getParent().getParent().getType().compareTo(ticketType) == 0)&&(menuItem.getParent().getParent().getPriceCategory()==priceCategory))
				{
					tempList.add(menuItem);
					System.out.println("Added............."+ menuItem.getName() + ", "+ menuItem.getParent().getParent().getType());
				}
			}
			itemList = tempList;
			Collections.sort(itemList, new MenuItem.ItemComparator());
			tableModel.setRows(tempList);
			if(itemList.size() > 0)
				table.setRowSelectionInterval(0, 0);
			table.repaint();
			table.revalidate();
		}
		catch(NumberFormatException e)
		{
			char [] text = itemId.toCharArray();
			text[0] = Character.toUpperCase(text[0]); 
			itemId = new String(text);
			List<MenuItem> tempList = new ArrayList(); 

			int index = 0;
			if(itemId.indexOf(" ") != -1)
			{
				index = itemId.indexOf(' ');
				text = itemId.toCharArray();
				try{
					text[index+1] = Character.toUpperCase(text[index+1]);
					itemId = new String(text);
				}
				catch(Exception ex){}
			}
			for(Iterator<MenuItem> itr = itemList.iterator();itr.hasNext();)
			{
				MenuItem menuItem = itr.next();
				
				if(menuItem.getName().contains(itemId)&&all)
				{
					tempList.add(menuItem);
				}else if(menuItem.getName().contains(itemId)&&menuItem.getParent().getParent().getPriceCategory()==priceCategory)
				{
					tempList.add(menuItem);
				}
			}
			itemList = tempList;
			Collections.sort(itemList, new MenuItem.ItemComparator());
			tableModel.setRows(tempList);
			if(itemList.size() > 0)
				table.setRowSelectionInterval(0, 0);
			table.repaint();
			table.revalidate();
		}
	}
	public void loadAllItem()
	{
		MenuItemDAO dao = new MenuItemDAO();
		itemList = dao.findAll();

		List<MenuItem> tempList = new ArrayList();
		for(Iterator<MenuItem> itr = itemList.iterator();itr.hasNext();)
		{
			MenuItem item = itr.next();
			if(item.getParent().getParent().getType().compareTo(ticketType) == 0&&item.getParent().getParent().getPriceCategory()==priceCategory)
				tempList.add(item);
		}
		itemList = tempList;	
		//		itemList = itemList.stream().filter(item -> item.getPriceCategory()== priceCategory)
		//				.collect(Collectors.toList());
		Collections.sort(itemList, new MenuItem.ItemComparator());
		tableModel.setRows(itemList);
		table.repaint();
		table.revalidate();
	}
	class MenuItemExplorerTableModel extends ListTableModel {
		String[] columnNames = {"Art-Id","Barcode",com.floreantpos.POSConstants.NAME, com.floreantpos.POSConstants.PRICE + " (EUR)", "Gewicht"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		MenuItemExplorerTableModel(){
			setColumnNames(columnNames);


		}
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}
		public Object getValueAt(int rowIndex, int columnIndex) {
			MenuItem item = (MenuItem) rows.get(rowIndex);
			if (item.getParent() == null || item.getParent().getParent() == null)
				return null;
			if (item.getParent().getParent().getType() == POSConstants.HOME_DELIVERY)
			{

				return null;
			}
			switch (columnIndex) {
			case 0:
				if (item.getItemId() != null)
				{
					return item.getItemId();
				}
				return "";

			case 1:
			
				return item.getBarcode();
			case 2:
				
				return item.getName();
			

			case 3:
				return NumberUtil.formatNumber(Double.valueOf(item.getPrice()));
			case 4:
				if(item.getWeightgrams()!=null) {
						String weight =String.valueOf(item.getWeightgrams()*100);
						weight = weight.substring(0, weight.indexOf("."));
						return weight;
				
				}else
					return 0;
			}
			return null;
		}

	}
	
}
