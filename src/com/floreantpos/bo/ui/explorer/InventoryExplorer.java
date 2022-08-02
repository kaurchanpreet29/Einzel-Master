package com.floreantpos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.commons.lang.StringUtils;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.QwertyKeyPad;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.dialog.NumberSelectionDialog2;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.NumberUtil;


public class InventoryExplorer extends TransparentPanel {
	private List<MenuItem> articleList;

	private JTable table;
	private ItemExplorerTableModel tableModel;
	JButton btnKeybord;
	JPopupMenu jpKey;
	JPanel panel;
	private boolean check = true;

	public InventoryExplorer() {
		this.setPreferredSize(new Dimension(800,500));
		articleList = MenuItemDAO.getInstance().findBarcodeItems();
		List<MenuItem> tempList = new ArrayList();
		for(Iterator<MenuItem> itr = articleList.iterator();itr.hasNext();)
		{
			MenuItem item = itr.next();
			if(item.getParent().getParent().getPriceCategory()==1)
				tempList.add(item);
		}
		articleList = tempList;
		articleList = removeNullBarcodes(articleList);
		Collections.sort(articleList, new MenuItem.InstockComparator());
		tableModel = new ItemExplorerTableModel();
		btnKeybord = new JButton("KEYBOARD");
		jpKey = new JPopupMenu();
		QwertyKeyPad kepad = new QwertyKeyPad();
		jpKey.add(kepad);
		btnKeybord.setBackground(new Color(104, 244, 66));
		btnKeybord.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(check) {
					check = false;
					panel.add(kepad, "growx");
					updateUI();
				}else {
					check = true;
					panel.remove(kepad);
					updateUI();
				}
			}
		});

		table = new JTable(tableModel);
		table.setDefaultRenderer(Object.class, new PosTableRenderer());		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		table.setRowHeight(35);
		table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);		

		setLayout(new BorderLayout(5,5));
		JScrollPane jScrollPane = new JScrollPane(table);
		table.getTableHeader().setBackground(new Color(209,222,235));
		jScrollPane.getViewport().setBackground(new Color(209,222,235));
		jScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(30,20));
		jScrollPane.getVerticalScrollBar().setBackground(Color.WHITE);
		add(jScrollPane);		

		final JTextField tfItemSearchBarcode = new JTextField(25);
		tfItemSearchBarcode.setFont(new Font("Times New Roman", Font.PLAIN,14));		
		if(StringUtils.isNotEmpty(POSConstants.Barcode_suchen))
			tfItemSearchBarcode.setText(POSConstants.Barcode_suchen);
		else
			tfItemSearchBarcode.setText("Barcode Suchen..");
		
		tfItemSearchBarcode.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(tfItemSearchBarcode.getText().compareTo("suchen...") == 0)
				{
					tfItemSearchBarcode.setText("");
					loadAllItem();
				}

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
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

		});
		tfItemSearchBarcode.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent e) {
				tfItemSearchBarcode.setText("");
			}

			@Override
			public void focusLost(FocusEvent e) {
				if(StringUtils.isNotEmpty(POSConstants.Barcode_suchen))
					tfItemSearchBarcode.setText(POSConstants.Barcode_suchen);
				else
				    tfItemSearchBarcode.setText("Barcode suchen..");
			}

		});
		tfItemSearchBarcode.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent arg0) {

				if(tfItemSearchBarcode.getText().length() == 0)
				{
					loadAllItem();
				}
				else
				{
					String text = tfItemSearchBarcode.getText();
					if(text.length() == 1)
					{
						text = text.toUpperCase();
						tfItemSearchBarcode.setText(text);
					}
					searchArtikelBarcode(text);	
				}
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
			}

		});

		final JTextField tfItemSearchName = new JTextField(10);
		tfItemSearchName.setFont(new Font("Times New Roman", Font.PLAIN,14));
		if(StringUtils.isNotEmpty(POSConstants.name_suchen))
			tfItemSearchName.setText(POSConstants.name_suchen);
		else
		    tfItemSearchName.setText("Name suchen..");
		
		tfItemSearchName.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(tfItemSearchName.getText().compareTo("Suchen...") == 0)
				{
					tfItemSearchName.setText("");
					loadAllItem();
				}

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
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

		});
		tfItemSearchName.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent e) {
				tfItemSearchName.setText("");
			}

			@Override
			public void focusLost(FocusEvent e) {
				if(StringUtils.isNotEmpty(POSConstants.name_suchen))
					tfItemSearchName.setText(POSConstants.name_suchen);
				else
				    tfItemSearchName.setText("Name suchen..");
			}

		});
		tfItemSearchName.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent arg0) {

				if(tfItemSearchName.getText().length() == 0)
				{
					loadAllItem();
				}
				else
				{
					String text = tfItemSearchName.getText();
					if(text.length() == 1)
					{
						text = text.toUpperCase();
						tfItemSearchName.setText(text);
					}
					searchArtikelName(text);	
				}
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
			}

		});
		final JTextField tfSearch = new JTextField(10);
		tfSearch.setFont(new Font("Times New Roman", Font.PLAIN,14));
		if(StringUtils.isNotEmpty(POSConstants.Verfuegbar_produckte))
			tfSearch.setText(POSConstants.Verfuegbar_produckte);
		else
			tfSearch.setText("Verfuegbar produckte barcode eingeben..");
		
		tfSearch.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {

				searchItem(tfSearch.getText());
				tfSearch.setText("");

			}
		});
		tfSearch.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent e) {
				tfSearch.setText("");
			}

			@Override
			public void focusLost(FocusEvent e) {
				if(StringUtils.isNotEmpty(POSConstants.Verfuegbar_produckte))
					tfSearch.setText(POSConstants.Verfuegbar_produckte);
				else
					tfSearch.setText("Verfuegbar produckte barcode eingeben..");
			}

		});
		panel = new JPanel();
		panel.setLayout(new MigLayout());

		final JTextField tfSearchdamanged = new JTextField(10);
		tfSearchdamanged.setFont(new Font("Times New Roman", Font.PLAIN,14));
		if(StringUtils.isNotEmpty(POSConstants.Beschaedigt_produckte))
			tfSearchdamanged.setText(POSConstants.Beschaedigt_produckte);
		else
		    tfSearchdamanged.setText("Beschaedigt produckte barcode eingeben...");
		
		tfSearchdamanged.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {

				searchDamagedItem(tfSearchdamanged.getText());
				tfSearchdamanged.setText("");

			}
		});
		tfSearchdamanged.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent e) {
				tfSearchdamanged.setText("");
			}

			@Override
			public void focusLost(FocusEvent e) {
				if(StringUtils.isNotEmpty(POSConstants.Beschaedigt_produckte))
					tfSearchdamanged.setText(POSConstants.Beschaedigt_produckte);
				else					
				    tfSearchdamanged.setText("Beschaedigt produckte barcode eingeben..");
			}

		});
		panel.add(tfItemSearchBarcode,"growx");
		panel.add(tfItemSearchName,"growx");
		panel.add(tfSearch,"growx");
		panel.add(tfSearchdamanged,"growx");
		panel.add(btnKeybord, "growx, wrap");
		panel.setBackground(new Color(209,222,235));
		add(panel,BorderLayout.SOUTH);
	}
	public void searchArtikelBarcode(String barcode)
	{
		MenuItemDAO dao = new MenuItemDAO();
		articleList = dao.findBarcodeItems();
		try
		{

			List<MenuItem> tempList = new ArrayList(); 
			for(Iterator<MenuItem> itr = articleList.iterator();itr.hasNext();)
			{
				MenuItem menuItem = itr.next();
				if(menuItem.getBarcode().contains(barcode))
				{
					tempList.add(menuItem);
				}
			}
			articleList = tempList;
			Collections.sort(articleList, new MenuItem.ItemComparator());
			articleList = removeNullBarcodes(articleList);
			tableModel.setRows(tempList);
			table.repaint();
			table.revalidate();
		}
		catch(NumberFormatException e)
		{
			e.printStackTrace();
		}
	}	

	public void searchArtikelName(String name)
	{
		MenuItemDAO dao = new MenuItemDAO();
		articleList = dao.findBarcodeItems();
		try
		{
			List<MenuItem> tempList = new ArrayList(); 
			for(Iterator<MenuItem> itr = articleList.iterator();itr.hasNext();)
			{
				MenuItem menuItem = itr.next();
				if(menuItem.getName().contains(name))
				{
					tempList.add(menuItem);
				}
			}
			articleList = tempList;
			Collections.sort(articleList, new MenuItem.ItemComparator());
			tempList = removeNullBarcodes(articleList);
			tableModel.setRows(tempList);
			table.repaint();
			table.revalidate();
		}
		catch(NumberFormatException e)
		{
			e.printStackTrace();
		}
	}
	public void loadAllItem()
	{
		MenuItemDAO dao = new MenuItemDAO();
		articleList = dao.findBarcodeItems();
		articleList = removeNullBarcodes(articleList);
		tableModel.setRows(articleList);
		table.repaint();
		table.revalidate();
	}

	public void searchItem(String barcode)
	{
		MenuItem item = null;
		barcode = barcode.trim();
		System.out.println(barcode);
		for(MenuItem menuItem: MenuItemDAO.getInstance().findByBarcode(barcode))
		{
			if (menuItem == null) {
				continue;
			}
			else
			{
				item = menuItem;
				break;
			}
		}
		if(item != null)
		{
			int input = NumberSelectionDialog2.take(this,
					NumberUtil.formatNumber(item.getPrice()) + " € - "
							+ item.getName() + " (" + item.getBarcode() + ")") ;
			if(item.getInstock() != null)
			{
				item.setInstock(item.getInstock()+input);
			}
			else
				item.setInstock(input);
			MenuItemDAO.getInstance().saveOrUpdate(item);
			MenuItemDAO dao = new MenuItemDAO();
			articleList = dao.findBarcodeItems();
			articleList = removeNullBarcodes(articleList);
			tableModel.setRows(articleList);
			table.repaint();
			table.revalidate();
		}
		else
		{
			if(StringUtils.isNotEmpty(POSConstants.nicht_gefunden))
				POSMessageDialog.showError(POSConstants.nicht_gefunden);
			else
			    POSMessageDialog.showError("Artikel nicht gefunden");
			
			return;
		}
	}
	
	public void searchDamagedItem(String barcode)
	{
		MenuItem item = null;
		barcode = barcode.trim();
		System.out.println(barcode);
		for(MenuItem menuItem: MenuItemDAO.getInstance().findByBarcode(barcode))
		{
			if (menuItem == null) {
				continue;
			}
			else
			{
				item = menuItem;
				break;
			}
		}
		if(item != null)
		{
			if(item.getInstock() == null)
			{
				if(StringUtils.isNotEmpty(POSConstants.Nicht_verfuegbar))
					POSMessageDialog.showError(POSConstants.Nicht_verfuegbar);
				else
				   POSMessageDialog.showError("Nicht verfuegbar in stock");
				
				return;
			}
			int input = NumberSelectionDialog2.take(this,
					NumberUtil.formatNumber(item.getPrice()) + " € - "
							+ item.getName() + " (" + item.getBarcode() + ")");
			if(item.getDamaged() != null)
			{
				item.setDamaged(item.getDamaged()+input);
				item.setInstock(item.getInstock()-input);
			}
			else
			{
				item.setDamaged(input);
				item.setInstock(item.getInstock()-input);
			}
			MenuItemDAO.getInstance().saveOrUpdate(item);
			MenuItemDAO dao = new MenuItemDAO();
			articleList = dao.findBarcodeItems();
			articleList = removeNullBarcodes(articleList);
			articleList = removeNullBarcodes(articleList);
			tableModel.setRows(articleList);
			table.repaint();
			table.revalidate();
		}
		else
		{
			if(StringUtils.isNotEmpty(POSConstants.nicht_gefunden))
				POSMessageDialog.showError(POSConstants.nicht_gefunden);
			else
			   POSMessageDialog.showError("Artikel nicht gefunden");
			
			return;
		}
	}

	public List<MenuItem> removeNullBarcodes(List<MenuItem> itemList)
	{
		List<MenuItem> newList = new ArrayList();
		for(MenuItem item: itemList)
		{
			if(item.getBarcode() != null && item.getBarcode().length() > 0)
				newList.add(item);
		}
		return newList;
	}

	class ItemExplorerTableModel extends ListTableModel{
		String[] columnNames = {"Barcode","Name", POSConstants.MENU_GROUPS, POSConstants.CATEGORY, POSConstants.Verfuegbar_, POSConstants.Verkauft, POSConstants.Beschaedigt, POSConstants.Artikelmenge_Hinzufügen };

		public int getRowCount() {
			if(articleList == null) {
				return 0;
			}
			return articleList.size();
		}

		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public String getColumnName(int column) {
			return columnNames[column];
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			if (Application.getCurrentUser().getFirstName().compareTo("Master")==0&&columnIndex > 3)
				return true;
			else if (columnIndex > 5)
				return true;
			else
				return false;
		}


		public void setValueAt(Object value, int rowIndex, int columnIndex)
		{
			try
			{ 
				MenuItem menuItem = articleList.get(rowIndex);
				menuItem = MenuItemDAO.getInstance().initialize(menuItem);
				if (columnIndex == 4)
				{


					if(value.toString() != null && value.toString().length() > 0)
						menuItem.setInstock(Integer.parseInt(value.toString()));					
				}else if (columnIndex == 5)	{
					try {
						if(value.toString() != null && value.toString().length() > 0)
							if(Integer.parseInt(value.toString())<menuItem.getInstock()) {
								menuItem.setInstock(menuItem.getInstock()-Integer.parseInt(value.toString()));
							}
						if(menuItem.getSold()!=null&&menuItem.getSold()>0) {
							menuItem.setSold(Integer.parseInt(value.toString())+menuItem.getSold());
						}else {
							menuItem.setSold(Integer.parseInt(value.toString()));
						}
					}catch(Exception ex) {

					}
					if(Application.getCurrentUser().getFirstName().compareTo("Master")==0) {
						menuItem.setSold(Integer.parseInt(value.toString()));
					}

				}
				else if (columnIndex == 6)
				{
					try {
						if(value.toString() != null && value.toString().length() > 0)
							if(Integer.parseInt(value.toString())<menuItem.getInstock()) {
								menuItem.setInstock(menuItem.getInstock()-Integer.parseInt(value.toString()));
							}
						if(menuItem.getDamaged()!=null&&menuItem.getDamaged()>0) {
							menuItem.setDamaged(Integer.parseInt(value.toString())+menuItem.getDamaged());
						}else {
							menuItem.setDamaged(Integer.parseInt(value.toString()));
						}
					}catch(Exception ex) {

					}

				}
				else if (columnIndex == 7)
				{
					try {
						if(value.toString() != null && value.toString().length() > 0)
							if(menuItem.getInstock()!=null) {
								menuItem.setInstock(Integer.parseInt(value.toString())+menuItem.getInstock());
							}else {
								menuItem.setInstock(Integer.parseInt(value.toString()));
							}
					}catch(Exception ex) {

					}

				}
				articleList.set(rowIndex, menuItem);
				MenuItemDAO.getInstance().saveOrUpdate(menuItem);
				this.fireTableDataChanged();
				table.repaint();
			}
			catch(Exception e){e.printStackTrace();}
		}
		public Object getValueAt(int rowIndex, int columnIndex) {
			if(articleList == null)
				return ""; //$NON-NLS-1$

			MenuItem item = articleList.get(rowIndex);
			switch(columnIndex) {
			case 0:
				return item.getBarcode();

			case 1:
				return item.getName();

			case 2:
				return item.getParent().getName();
			case 3:
				return item.getParent().getParent().getName();
			case 4:
				return item.getInstock();
			case 5:
				return item.getSold();
			case 6:
				return item.getDamaged();
			case 7:
				return "";	
			}

			return null;
		}

		public void addTax(MenuItem item) {
			int size = articleList.size();
			articleList.add(item);
			fireTableRowsInserted(size, size);
		}

		public void deleteTax(MenuItem item, int index) {
			articleList.remove(item);
			fireTableRowsDeleted(index, index);
		}
	}
}
