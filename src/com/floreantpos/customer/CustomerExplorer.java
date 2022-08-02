package com.floreantpos.customer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.floreantpos.bo.actions.KundenExportAction;
import com.floreantpos.bo.actions.KundenImportAction;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.bo.ui.explorer.ExplorerButtonPanel;
import com.floreantpos.bo.ui.explorer.ListTableModel;
import com.floreantpos.model.Customer;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.CustomerDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.NoteDialog;
import com.floreantpos.ui.forms.CustomerForm;
import com.floreantpos.ui.model.MenuItemForm;

public class CustomerExplorer extends TransparentPanel {
	private List<Customer> customerList;

	private JTable table;

	private CustomerTableModel tableModel;

	public CustomerExplorer() {
		this.setPreferredSize(new Dimension(800,480));
		CustomerDAO dao = new CustomerDAO();
		customerList = dao.findAll();
		tableModel = new CustomerTableModel();
		tableModel.setRows(customerList);
		table = new JTable(tableModel);
		table.setRowHeight(30);
		table.setDefaultRenderer(Object.class, new PosTableRenderer());

		table.getTableHeader().setBackground(new Color(209,222,235));
		setLayout(new BorderLayout(5,5));

		table.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent me) {

				try
				{	 
					if (me.getClickCount() == 2)
					{
						Point p = me.getPoint();
						int row = table.rowAtPoint(p);
						Customer customer =  (Customer) tableModel.getRowData(row);
						if(customer == null)
							return;
						CustomerForm editor = new CustomerForm(customer);
						BeanEditorDialog dialog = new BeanEditorDialog(editor, BackOfficeWindow.getInstance(), true);
						dialog.open();
						if (dialog.isCanceled())
							return;
						table.repaint();

					}
				}
				catch(Exception e){e.printStackTrace();}
			}
		});
		JScrollPane jScrollPane = new JScrollPane(table);
		jScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(30,20));
		jScrollPane.getVerticalScrollBar().setBackground(Color.WHITE);
		add(jScrollPane);
		jScrollPane.getViewport().setBackground(new Color(209,222,235));

		ExplorerButtonPanel explorerButton = new ExplorerButtonPanel();
		JButton addButton = explorerButton.getAddButton();
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					CustomerForm editor = new CustomerForm();
					BeanEditorDialog dialog = new BeanEditorDialog(editor, BackOfficeWindow.getInstance(), true);
					dialog.open();
					if (dialog.isCanceled())
						return;
					Customer customer = (Customer) editor.getBean();
					customerList.add(customer);
					table.revalidate();
					table.repaint();
				} catch (Exception x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		JButton exportButton = explorerButton.getExtraButton();
		exportButton.setText("Export");
		exportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {

					File destinationFolder = new File("C:\\Data\\Export");
					if (!destinationFolder.exists())
					{
						destinationFolder.mkdirs();
						File file = new File("C:\\\\Data\\\\Export\\menuExport.xls");						
					}

					JFileChooser fileChooser = new JFileChooser("C:\\Data\\Export");

					fileChooser.showOpenDialog(BackOfficeWindow.getInstance());
					File file = fileChooser.getSelectedFile();
					boolean overwrite = false;
					if(!file.getName().toLowerCase().endsWith(".xlsx"))
						file = new File(file.getPath()+".xlsx");
					else {
						int option = JOptionPane.showConfirmDialog(BackOfficeWindow.getInstance(), "Overwrite file " + file.getName() + "?",
								"Confirm", JOptionPane.YES_NO_OPTION);
						if (option != JOptionPane.YES_OPTION) {
							return;
						}
					}

					if (!file.getName().toLowerCase().endsWith(".xlsx")) {
						file = new File(file.getParentFile(), file.getName() + ".xlsx");
					}
					;
					toExcel(file);
					//					exportDataToExcel();

				} catch (Throwable x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});
		
		JButton imPortButton = explorerButton.getCopyButton();
			imPortButton.setText("Import");
			imPortButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					new KundenImportAction("Import");
					customerList = CustomerDAO.getInstance().findAll();
					table.revalidate();
					table.repaint();
				}
			});

		JButton editButton = explorerButton.getEditButton();
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;

					Customer customer = customerList.get(index);
					CustomerForm editor = new CustomerForm(customer);
					BeanEditorDialog dialog = new BeanEditorDialog(editor, BackOfficeWindow.getInstance(), true);
					dialog.open();
					if (dialog.isCanceled())
						return;
					/*else
					{
						customer = (Customer) editor.getBean();
						//customerList.set(index, customer);
					}*/
					table.repaint();
				} catch (Exception x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});
		JButton deleteButton = explorerButton.getDeleteButton();
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				try {
//					int index = table.getSelectedRow();
//					if (index < 0)
//						return;
//					Customer customer = customerList.get(index);
//					tableModel.deleteItem(index);
//					CustomerDAO.getInstance().delete(customer);
//				}
//				catch (Exception x) {
//					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
//				}
				try {
					int[] rows = table.getSelectedRows();
					if (rows.length > 0) {
						for (int index = 0; index < rows.length; index++) {
							Customer customer = customerList.get(rows[index]);
							CustomerDAO.getInstance().delete(customer);

						}
						tableModel.setRows(CustomerDAO.getInstance().findAll());
						
					}
				} catch (Throwable x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
				table.repaint();
			}

		});
		JButton fitButton =explorerButton.getFitButton();

		fitButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				List<Customer> customerList = CustomerDAO.getInstance().findAll();
				int index = 0;
				for(Iterator<Customer> itr=customerList.iterator();itr.hasNext();)
				{
					index = 0;
					Customer customer = itr.next();
					String origAddress = customer.getAddress();

					System.out.println(origAddress);
					index = origAddress.indexOf('.');
					if(index != -1)
					{
						String address = origAddress.substring(0, index+1);
						int numberLength = origAddress.length() - address.length() - 1;
						System.out.println(numberLength);
						String number = origAddress.substring(index+1,index+numberLength+1);
						number = number.trim();

						customer.setAddress(address);
						customer.setDoorNo(number);
						CustomerDAO.getInstance().saveOrUpdate(customer);

					}
				}
			}

		});
		final JTextField tfSearch = new JTextField(10);
		tfSearch.setFont(new Font("Times New Roman", Font.PLAIN,14));
		tfSearch.setText("Suchen...");
		tfSearch.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(tfSearch.getText().compareTo("Suchen...") == 0)
				{
					tfSearch.setText("");
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
		tfSearch.setFocusable(true);
		tfSearch.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent arg0) {

			}

			@Override
			public void keyReleased(KeyEvent arg0) {

				if(tfSearch.getText().length() == 0)
				{
					loadAllItem();
				}
				else
				{
					String text = tfSearch.getText();
					text = text.toUpperCase();
					if(text.length() == 1)
					{
						text = text.toUpperCase();
						tfSearch.setText(text);
					}

					searchCustomer(text);				
				}
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
			}

		});
		TransparentPanel panel = new TransparentPanel();
		panel.add(tfSearch);
		panel.add(addButton);
		panel.add(editButton);
		panel.add(deleteButton);
		panel.add(imPortButton);
		panel.add(exportButton);
		add(panel, BorderLayout.SOUTH);
	}
	public void loadAllItem()
	{
		CustomerDAO dao = new CustomerDAO();
		customerList = dao.findAll();

		Collections.sort(customerList, new Customer.customerComparator());
		tableModel.setRows(customerList);
		table.repaint();
		table.revalidate();
	}

	/*
	 * Export excel
	 */

	public void toExcel(File file){
		try{
			Workbook wb = new XSSFWorkbook();
			Sheet sheet = wb.createSheet();
			Row row = sheet.createRow(0);
			for (int i = 0; i < tableModel.getColumnCount(); i++) {
				row.createCell(i).setCellValue(tableModel.getColumnName(i));
			}
			for (int i = 0; i < tableModel.getRowCount(); i++) {
				row = sheet.createRow(i + 1);
				for (int j = 0; j < tableModel.getColumnCount(); j++) {
				
							try {
								row.createCell(j).setCellValue(tableModel.getValueAt(i, j).toString()
										);
							}catch(Exception ex) {
								row.createCell(j).setCellValue("");
							}
							
				}
			}
			FileOutputStream fileOut = new FileOutputStream(file);
			wb.write(fileOut);
			fileOut.close();
			wb.close();
			JOptionPane.showMessageDialog(null, "Erfolg");
		}catch(IOException e){ System.out.println(e); }
	}


	public void searchCustomer(String customerId)
	{
		CustomerDAO dao = new CustomerDAO();
		customerList = dao.findAll();
		try
		{
			Integer.parseInt(customerId);
			List<Customer> tempList = new ArrayList(); 
			for(Iterator<Customer> itr = customerList.iterator();itr.hasNext();)
			{
				Customer customer = itr.next();
				if(customer.getLoyaltyNo().contains(customerId))
				{
					tempList.add(customer);
				}
			}
			customerList = tempList;
			Collections.sort(customerList, new Customer.customerComparator());
			tableModel.setRows(tempList);
			table.repaint();
			table.revalidate();
		}
		catch(NumberFormatException e)
		{

			List<Customer> tempList = new ArrayList(); 
			for(Iterator<Customer> itr = customerList.iterator();itr.hasNext();)
			{
				Customer customer = itr.next();
				if(customer.getName().contains(customerId))
				{
					tempList.add(customer);
				}
			}
			customerList = tempList;
			Collections.sort(customerList, new Customer.customerComparator());
			tableModel.setRows(tempList);
			table.repaint();
			table.revalidate();
		}
	}


	public class CustomerTableModel extends ListTableModel {		

		String[] columnNames = {com.floreantpos.POSConstants.Kunden_Nr,"SALUTATION", "Name",com.floreantpos.POSConstants.Firma, com.floreantpos.POSConstants.Telefon, com.floreantpos.POSConstants.Strasse,"Nr.",com.floreantpos.POSConstants.PLZ,"City"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		CustomerTableModel(){
			setColumnNames(columnNames);
		}
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Customer customer  = (Customer) rows.get(rowIndex);		
			switch (columnIndex) {
			case 0:
				return customer.getLoyaltyNo();
			case 1:
				return customer.getSalutation();
			case 2:
				return customer.getName();
			case 3:
				return customer.getFirmName();
			case 4:
				return customer.getTelephoneNo();
			case 5:
				return customer.getAddress();
			case 6:
				return customer.getDoorNo();
			case 7:
				return customer.getZipCode();
			case 8:
				return customer.getCity();
			}
			return null;
		}
		@Override
		public int getColumnCount() {
			return columnNames.length;
		}
		@Override
		public int getRowCount() {
			return customerList.size();
		}

	}
}
