package com.floreantpos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Menu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import com.floreantpos.POSConstants;
import com.floreantpos.add.service.ExportToAccesDatabase;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.Tax;
import com.floreantpos.model.ZutatenForm;
import com.floreantpos.model.dao.GenericDAO;
import com.floreantpos.model.dao.MenuCategoryDAO;
import com.floreantpos.model.dao.MenuGroupDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.report.JReportPrintService;
import com.floreantpos.report.ReportUtil;
import com.floreantpos.report.SalesReport;
import com.floreantpos.services.UtilityService;
import com.floreantpos.swing.ComboBoxModel;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.ConfirmDeleteDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.model.MenuItemForm;
import com.floreantpos.ui.views.order.OrderView;
import com.floreantpos.util.NumberUtil;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


public class MenuItemExplorer extends TransparentPanel {
	private List<MenuItem> itemList;
	private JTable table;
	private List<MenuItem> tempList;
	private JComboBox cbType;
	private JComboBox cbCategory;
	private JTextField tfBarcodeSearch;
	private JTextField tfSearch;
	private MenuItemExplorerTableModel tableModel;
	List<MenuGroup> groupList;
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

	public MenuItemExplorer() {
		this.setPreferredSize(new Dimension(800, 480));
		MenuItemDAO dao = new MenuItemDAO();
		itemList = dao.findAll();
		//itemList = dao.findAllItemsSorted();
		
		try {
			Collections.sort(itemList, new MenuItem.ItemComparator());
		}catch(IllegalArgumentException ex) {}
		MenuGroupDAO grpdao = new MenuGroupDAO();
		groupList = grpdao.findAll();
		List<String> listType = new ArrayList<String>();
		listType.add(OrderView.getTaxDineIn()+"%");
		listType.add(OrderView.getTaxHomeDelivery()+"%");
		listType.add("0%");
		tempList = new ArrayList<MenuItem>();
		this.tempList = itemList;
		cbType = new JComboBox();
		cbType.setBackground(Color.WHITE);
		listType.stream().forEach(type -> cbType.addItem(type));
		cbType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MenuItemDAO dao = new MenuItemDAO();
				itemList = dao.findAll();
				tempList.clear();
				if (cbType.getSelectedIndex() == 0) {					
					for (Iterator<MenuItem> itr = itemList.iterator(); itr.hasNext();) {
						MenuItem item = (MenuItem) itr.next();
						if (item.getParent() == null || item.getParent().getParent() == null
								|| item.getParent().getParent().getType() == null)
							continue;
						if (item.getParent().getParent().getType() != null) {
							if (item.getParent().getParent().getType().compareTo(POSConstants.DINE_IN) == 0) {
								tempList.add(item);
							}
						}
					}
				} else if (cbType.getSelectedIndex() == 1) {
					for (Iterator<MenuItem> itr = itemList.iterator(); itr.hasNext();) {
						MenuItem item = (MenuItem) itr.next();

						if (item.getParent() == null || item.getParent().getParent() == null
								|| item.getParent().getParent().getType() == null)
							continue;
						if (item.getParent().getParent().getType() != null) {
							if (item.getParent().getParent().getType().compareTo(POSConstants.HOME_DELIVERY) == 0) {
								tempList.add(item);
							}
						}
					}
				} else {
					for (Iterator<MenuItem> itr = itemList.iterator(); itr.hasNext();) {
						MenuItem item = (MenuItem) itr.next();

						if (item.getParent() == null || item.getParent().getParent() == null
								|| item.getParent().getParent().getType() == null)
							continue;
						if (item.getParent().getParent().getType() != null) {
							if (item.getParent().getParent().getType().compareTo("ZERO") == 0) {
								tempList.add(item);
							}
						}
					}
				}
				itemList = tempList;
				Collections.sort(itemList, new MenuItem.ItemComparator());
				tableModel.setRows(tempList);
				table.repaint();
				table.revalidate();
			}
		});


		cbCategory = new JComboBox<String>();
		cbCategory.setBackground(Color.WHITE);
		changeCategories("");
		cbCategory.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MenuItemDAO dao = new MenuItemDAO();
				itemList = dao.findAll();
				tempList.clear();
				for (MenuItem item: itemList) {
					MenuCategory category = item.getParent().getParent();
					String type ="ZERO";
					if(cbType.getSelectedIndex()==0) {
						type = POSConstants.DINE_IN;
					} else if(cbType.getSelectedIndex()==1) {
						type = POSConstants.HOME_DELIVERY;
					}					

					if (category.getType() != null) {				
						if (category.getType()
								.compareTo(type) == 0
								&& category.getName().equals(cbCategory.getSelectedItem().toString())) {
							tempList.add(item);
						}
					}
				}
				itemList = tempList;
				Collections.sort(itemList, new MenuItem.ItemComparator());
				tableModel.setRows(tempList);
				table.repaint();
				table.revalidate();

			}

		});

		tableModel = new MenuItemExplorerTableModel();
		tableModel.setRows(itemList);
		table = new JTable(tableModel);
		table.setRowHeight(30);
		DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
		leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

		table.getColumnModel().getColumn(0).setCellRenderer(otherRenderer);
		table.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
		table.getColumnModel().getColumn(2).setCellRenderer(otherRenderer);
		table.getColumnModel().getColumn(3).setCellRenderer(otherRenderer);
		table.getColumnModel().getColumn(4).setCellRenderer(leftRenderer);
		table.getColumnModel().getColumn(5).setCellRenderer(leftRenderer);
		table.getColumnModel().getColumn(6).setCellRenderer(otherRenderer);
		table.getColumnModel().getColumn(7).setCellRenderer(otherRenderer);
		table.getColumnModel().getColumn(8).setCellRenderer(otherRenderer);
		table.getColumnModel().getColumn(9).setCellRenderer(otherRenderer);
		table.getColumnModel().getColumn(10).setCellRenderer(otherRenderer);
		table.getColumnModel().getColumn(11).setCellRenderer(leftRenderer);

		table.getColumnModel().getColumn(1).setMinWidth(160);
		table.getColumnModel().getColumn(4).setMinWidth(80);
		table.getColumnModel().getColumn(5).setMinWidth(80);
		table.getColumnModel().getColumn(7).setMinWidth(100);
		table.getColumnModel().getColumn(10).setMinWidth(80);


		TableColumn menuGroupColumn = table.getColumnModel().getColumn(5);

		JComboBox comboBox = new JComboBox();
		MenuGroupDAO foodGroupDAO = new MenuGroupDAO();
		List<MenuGroup> foodGroups = foodGroupDAO.findAll();
		comboBox.setModel(new ComboBoxModel(foodGroups));
		menuGroupColumn.setCellEditor(new DefaultCellEditor(comboBox));

		TableColumn taxColumn = table.getColumnModel().getColumn(6);

		JComboBox comboBoxTax = new JComboBox();
		TaxDAO taxDAO = new TaxDAO();
		List<Tax> taxes = taxDAO.findAll();
		comboBoxTax.setModel(new ComboBoxModel(taxes));
		taxColumn.setCellEditor(new DefaultCellEditor(comboBoxTax));
		table.setDefaultRenderer(Object.class, new PosTableRenderer());
		table.getTableHeader().setBackground(new Color(209, 222, 235));
		setLayout(new BorderLayout(5, 5));
		table.setRowSelectionAllowed(true);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JScrollPane jScrollPane = new JScrollPane(table);
		jScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(30, 20));
		jScrollPane.getVerticalScrollBar().setBackground(Color.WHITE);
		add(jScrollPane);
		jScrollPane.getViewport().setBackground(new Color(209, 222, 235));
		ExplorerButtonPanel explorerButton = new ExplorerButtonPanel();

		tfSearch = new JTextField(10);
		tfSearch.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		tfSearch.setText("Suchen...");
		tfSearch.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (tfSearch.getText().compareTo("Suchen...") == 0) {
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
		tfSearch.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {

			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				if (tfSearch.getText().length() == 0) {
					tableModel.setRows(itemList);
					table.repaint();
					table.revalidate();
					table.setRowSelectionInterval(0, 0);
				} else {
					String text = tfSearch.getText();
					if (text.length() == 1) {
						text = text.toUpperCase();
						tfSearch.setText(text);
					}
					doSearch(text);
				}
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				
			}

		});

		tfBarcodeSearch = new JTextField(20);
		tfBarcodeSearch.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		tfBarcodeSearch.setText("Barcode...");
		tfBarcodeSearch.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (tfBarcodeSearch.getText().compareTo("Barcode...") == 0) {
					tfBarcodeSearch.setText("");
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
		tfBarcodeSearch.setFocusable(true);
		tfBarcodeSearch.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
//					if (tfBarcodeSearch.getText().length() == 0) {
//						loadAllItem();
//					} else {
//						searchBarcode(tfBarcodeSearch.getText());
//					}
					if (tfBarcodeSearch.getText().length() == 0) {
						tableModel.setRows(itemList);
						table.repaint();
						table.revalidate();
						table.setRowSelectionInterval(0, 0);
					} else {
						String text = tfBarcodeSearch.getText();					
						doSearchBarcode(text, true);
					}

				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				if (tfBarcodeSearch.getText().length() == 0) {
					tableModel.setRows(itemList);
					table.repaint();
					table.revalidate();
					table.setRowSelectionInterval(0, 0);
				} else {
					String text = tfBarcodeSearch.getText();					
					doSearchBarcode(text, false);
				}
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
//				if (tfBarcodeSearch.getText().length() == 0) {
//					tableModel.setRows(itemList);
//					table.repaint();
//					table.revalidate();
//					table.setRowSelectionInterval(0, 0);
//				} else {
//					String text = tfBarcodeSearch.getText();					
//					doSearchBarcode(text, false);
//				}
			}

		});

		JButton editButton = explorerButton.getEditButton();
		JButton addButton = explorerButton.getAddButton();

		JButton deleteButton = explorerButton.getDeleteButton();
		JButton fitButton = explorerButton.getFitButton();
		JButton extraButton = explorerButton.getExtraButton();
		JButton printButton = explorerButton.getPrintButton();		
		JButton menuExportBtn = new JButton("Export");
		JButton updateBarcodeBtn = new JButton("UpdateBarcode");
		JButton dataExportBtn = new JButton("Export mdb");
		JButton updatPriceCat = new JButton("UpdatePriceCategory");
		JButton btnFilter = new JButton("Filter");
		JButton duplicateButton = explorerButton.getDuplicateButton();
		JComboBox<String> cmbFilter = new JComboBox<String>();
		cmbFilter.addItem("ART");
		cmbFilter.addItem("BARCODE");
		cmbFilter.addItem("GROUPSORTING");
		cmbFilter.setSelectedIndex(0);

		dataExportBtn.addActionListener(new ActionListener() {

			@SuppressWarnings("unused")
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int[] rows = table.getSelectedRows();
					List<MenuItem> itemss = new ArrayList<MenuItem>();
					if (rows.length > 0) {
						for (int index = 0; index < rows.length; index++) {
							MenuItem item = tempList.get(rows[index]);
							itemss.add(item);
						}   

					}            
					if(itemss==null) {
						POSMessageDialog.showError("Bitte Waehlen sie Artike..");
						return;
					}
					File destinationFolder = new File("C:\\Data\\Export");
					if (!destinationFolder.exists())
					{
						destinationFolder.mkdirs();
						File file = new File("C:\\\\Data\\\\Export\\myData.accdb");


					}

					JFileChooser fileChooser = new JFileChooser("C:\\Data\\Export");
					fileChooser.setFileFilter(new FileFilter()
					{
						public String getDescription()
						{
							return "ACCDB file";
						}

						public boolean accept(File f)
						{
							return f.getName().toLowerCase().endsWith(".accdb");
						}
					});
					fileChooser.showOpenDialog(BackOfficeWindow.getInstance());
					File file = fileChooser.getSelectedFile();
					if(!file.getName().toLowerCase().endsWith(".accdb"))
						file = new File(file.getPath()+".accdb");
					else {
						int option = JOptionPane.showConfirmDialog(BackOfficeWindow.getInstance(), "Overwrite file " + file.getName() + "?",
								"Confirm", JOptionPane.YES_NO_OPTION);
						if (option != JOptionPane.YES_OPTION) {
							return;
						}
					}

					ExportToAccesDatabase.doWrite(itemss, file);

				} catch (Throwable x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}

			}
		});


		duplicateButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRows[] = table.getSelectedRows();
				if(selectedRows != null && selectedRows.length > 0) {
					Session session = MenuItemDAO.getInstance().createNewSession();
					Transaction tx = session.beginTransaction();
					for(int i = 0; i < selectedRows.length;i++) {
						MenuItem menuItem = tempList.get(selectedRows[i]);
						final MenuItem duplicatedItem = duplicateMenuItem(menuItem);
						if(duplicatedItem != null) {
							session.save(duplicatedItem);
						}
					}
					tx.commit();
					session.close();
				}
				JOptionPane.showMessageDialog(BackOfficeWindow.getInstance(), "Erfolg..");
				loadPartially();
			}
		});

		updateBarcodeBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRows = table.getSelectedRow();
				MenuItem menuItem = tempList.get(selectedRows);
				String barcode = menuItem.getBarcode();
				int inStock = menuItem.getInstock();
				if(barcode!=null&&!StringUtils.isEmpty(barcode)||inStock!=0) {
					List<MenuItem> itemList = MenuItemDAO.getInstance().findById(menuItem.getItemId());
					for(MenuItem Item:itemList) {
						Item.setBarcode(barcode);
						Item.setInstock(inStock);
						MenuItemDAO.getInstance().saveOrUpdate(Item);	
					}
				}

				tableModel.setRows(MenuItemDAO.getInstance().findAll());
				table.repaint();
			}
		});


		updatPriceCat.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				List<MenuItem> itemList = MenuItemDAO.getInstance().findAll();
				for(MenuItem item:itemList) {
					item.setPriceCategory(1);
					MenuItemDAO.getInstance().saveOrUpdate(item);
				}
				JOptionPane.showMessageDialog(BackOfficeWindow.getInstance(), "Erfolg..");
			}
		});


		menuExportBtn.addActionListener(new ActionListener() {
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


		extraButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int selectedRows[] = table.getSelectedRows();
				MenuItem menuItem1 = tempList.get(0);
				String type = "";
				type = menuItem1.getParent().getParent().getType();

				for (int i = 0; i < selectedRows.length; i++) {
					MenuItem menuItem = tempList.get(selectedRows[i]);
					if ((menuItem.getParent().getParent().getType().compareTo(type)) != 0) {
						BOMessageDialog.showError("Fehler - Alle Artikel nicht gleich");
						return;
					}
					ZutatenForm editor = new ZutatenForm(selectedRows, menuItem);
					BeanEditorDialog dialog = new BeanEditorDialog(editor, BackOfficeWindow.getInstance(), true);
					dialog.open();
					if (dialog.isCanceled())
						continue;

					table.repaint();
					table.validate();
				}
			}

		});
		

		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;

					MenuItem menuItem = tempList.get(index);
					menuItem = MenuItemDAO.getInstance().initialize(menuItem);
					tempList.set(index, menuItem);

					MenuItemForm editor = new MenuItemForm(menuItem);
					BeanEditorDialog dialog = new BeanEditorDialog(editor, BackOfficeWindow.getInstance(), true);
					dialog.open();
					if (dialog.isCanceled())
						return;

					table.repaint();

				} catch (Throwable x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		printButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					HashMap map = new HashMap();
					ReportUtil.populateRestaurantProperties(map);
					Calendar c = Calendar.getInstance();
					Date date = new Date();
					int month = c.get(Calendar.MONTH) + 1;
					String minutes = date.getMinutes() + "";
					if (minutes.length() == 1)
						minutes = "0" + minutes;
					String monthString = month + "";
					if (monthString.length() == 1)
						monthString = "0" + monthString;
					String dateString = c.get(Calendar.DATE) + "." + monthString + "." + c.get(Calendar.YEAR) + ","
							+ date.getHours() + ":" + minutes + " Uhr";
					map.put("footerMessage", "Von Khana Kassensysteme erzeugt, " + dateString);
					JasperReport masterReport = (JasperReport) JRLoader
							.loadObject(SalesReport.class.getResource("/com/floreantpos/report/template/MenuItems.jasper"));

					JasperPrint print = JasperFillManager.fillReport(masterReport, map, new JRTableModelDataSource(tableModel));
					print.setName("Menu Artikel");

					print.setProperty("printerName", Application.getPrinters().getA4Printer());
					JReportPrintService.printQuitelyA4(print);

				} catch (Throwable x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					while (true) {
						MenuItemForm editor = MenuItemForm.getmForm();
						BeanEditorDialog dialog = new BeanEditorDialog(editor, BackOfficeWindow.getInstance(), true);
						dialog.open();
						if (dialog.isCanceled())
							return;
						MenuItem foodItem = (MenuItem) editor.getBean();
						tableModel.addMenuItem(foodItem);
					}


				} catch (Throwable x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});


		btnFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					List<MenuItem> mList = MenuItemDAO.getInstance().findAll();
					String barcode="";
					double price = 0.00;
					if(cmbFilter.getSelectedItem().toString().compareTo("BARCODE")==0) {
						for(MenuItem mItem:mList) {
							if(barcode.compareTo(mItem.getBarcode())!=0) {
								barcode = mItem.getBarcode();
							}else {
								try {
									MenuItemDAO.getInstance().delete(mItem.getId());
								}catch(Exception ex) {

								}								
							}
							if(mItem.getParent().getParent().getName().contains("1")) {
								List<MenuCategory> category = MenuCategoryDAO.getInstance().findByName(mItem.getParent().getParent().getName().replace("1", ""));
								MenuGroup grp = mItem.getParent();
								grp.setParent(category.get(0));
								MenuGroupDAO.getInstance().saveOrUpdate(grp);
							}else if(mItem.getParent().getParent().getName().contains("2")) {
								List<MenuCategory> category = MenuCategoryDAO.getInstance().findByName(mItem.getParent().getParent().getName().replace("2", ""));
								MenuGroup grp = mItem.getParent();
								grp.setParent(category.get(0));
								MenuGroupDAO.getInstance().saveOrUpdate(grp);
							}else if(mItem.getParent().getParent().getName().contains("3")) {
								List<MenuCategory> category = MenuCategoryDAO.getInstance().findByName(mItem.getParent().getParent().getName().replace("3", ""));
								MenuGroup grp = mItem.getParent();
								grp.setParent(category.get(0));
								MenuGroupDAO.getInstance().saveOrUpdate(grp);
							}
						}
					}else if(cmbFilter.getSelectedItem().toString().compareTo("ART")==0) {
						for(MenuItem mItem:mList) {
							if(barcode.compareTo(mItem.getBarcode())!=0) {
								barcode = mItem.getBarcode();
								price = NumberUtil.roundToTwoDigit(mItem.getPrice());
							}else if(price==NumberUtil.roundToTwoDigit(mItem.getPrice())) {
								try {
									MenuItemDAO.getInstance().delete(mItem.getId());
								}catch(Exception ex) {

								}								
							}
						}

					}else if(cmbFilter.getSelectedItem().toString().compareTo("GROUPSORTING")==0) {
						for(MenuItem mItem:mList) {							
							if(mItem.getParent().getParent().getName().contains("1")) {
								List<MenuCategory> category = MenuCategoryDAO.getInstance().findByName(mItem.getParent().getParent().getName().replace("1", ""));
								MenuGroup grp = mItem.getParent();
								grp.setParent(category.get(0));
								MenuGroupDAO.getInstance().saveOrUpdate(grp);
							}else if(mItem.getParent().getParent().getName().contains("2")) {
								List<MenuCategory> category = MenuCategoryDAO.getInstance().findByName(mItem.getParent().getParent().getName().replace("2", ""));
								MenuGroup grp = mItem.getParent();
								grp.setParent(category.get(0));
								MenuGroupDAO.getInstance().saveOrUpdate(grp);
							}else if(mItem.getParent().getParent().getName().contains("3")) {
								List<MenuCategory> category = MenuCategoryDAO.getInstance().findByName(mItem.getParent().getParent().getName().replace("3", ""));
								MenuGroup grp = mItem.getParent();
								grp.setParent(category.get(0));
								MenuGroupDAO.getInstance().saveOrUpdate(grp);
							}
						}
					}

				} catch (Throwable x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});


		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int[] rows = table.getSelectedRows();
					if (rows.length > 0) {
						for (int index = 0; index < rows.length; index++) {
							MenuItem item = tempList.get(rows[index]);
							MenuItemDAO foodItemDAO = new MenuItemDAO();
							foodItemDAO.delete(item);							
						}
						loadPartially();
					}
				} catch (Throwable x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		JButton printLabelButton = new JButton("Print Lable");
		printLabelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int[] rows = table.getSelectedRows();
				if (rows.length > 0) {
					for (int index = 0; index < rows.length; index++) {
						MenuItem item = tempList.get(rows[index]);
						try {
							UtilityService.printBarcodLevel(item);
						} catch (Throwable x) {
							BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
						}
					}
				}

			}

		});

		fitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					List<MenuItem> tempList;
					tempList = dao.findAll();
					Session session = MenuItemDAO.getInstance().createNewSession();
					Transaction tx = session.beginTransaction();

					Tax dineIn = TaxDAO.getInstance().findByName(POSConstants.DINE_IN);
					Tax home = TaxDAO.getInstance().findByName(POSConstants.HOME_DELIVERY);
					Tax zero = TaxDAO.getInstance().findByName("ZERO");
					for (Iterator itr = tempList.iterator(); itr.hasNext();) {
						MenuItemForm editor;
						MenuItem item = (MenuItem) itr.next();
						if (item.getParent().getParent().getType().compareTo(POSConstants.DINE_IN) == 0) {
							item.setTax(dineIn);
						} else if (item.getParent().getParent().getType().compareTo(POSConstants.HOME_DELIVERY) == 0) {
							item.setTax(home);
						} else {
							item.setTax(zero);
						}
						
						item.setBarcode(ReportUtil.removeLeadingZeroes(item.getBarcode()));						
						session.saveOrUpdate(item);
					}

					tx.commit();
					session.close();
					MenuItemDAO dao1 = new MenuItemDAO();
					itemList = dao1.findAll();
					MenuItemExplorer.this.tempList = itemList;
					Collections.sort(itemList, new MenuItem.ItemComparator());
					tableModel.setRows(itemList);
					table.repaint();
				} catch (Throwable x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		TransparentPanel panel = new TransparentPanel();
		panel.setLayout(new MigLayout());
		panel.add(tfSearch, "cell 0 0, alignx leading");
		panel.add(tfBarcodeSearch, "cell 1 0, alignx leading");
		panel.add(addButton);
		panel.add(editButton);
		panel.add(duplicateButton);
		panel.add(deleteButton);
		panel.add(fitButton);
		panel.add(extraButton);
		panel.add(printButton);		
		if (TerminalConfig.isSupermarket()) {
			panel.add(cbType);
			panel.add(cbCategory);
		}
		panel.add(printLabelButton);
		panel.add(menuExportBtn, "wrap");
		if(Application.getCurrentUser().getFirstName().compareTo("Super-User")==0) {
			panel.add(updateBarcodeBtn);
			panel.add(dataExportBtn);
			panel.add(cmbFilter);
			panel.add(btnFilter);
			panel.add(updatPriceCat);
		}

		add(panel, BorderLayout.SOUTH);
	}

	private boolean dontchange = false;

	public void changeCategories(String type) {
		cbCategory.removeAllItems();
		List<MenuCategory> categoryList = MenuCategoryDAO.getInstance().findAll();
		for(MenuCategory cat:categoryList) {
			if(cat.getName().compareTo(com.floreantpos.POSConstants.MISC)==0) {
				categoryList.remove(cat);
				break;
			}
		}
		for (MenuCategory category : categoryList) {
			cbCategory.addItem(category.getName());

		}
		repaint();
		revalidate();
	}


	public void searchItem(String itemId) {
		MenuItemDAO dao = new MenuItemDAO();
		itemList = dao.findAll();
		try {
			Integer.parseInt(itemId);
			List<MenuItem> tempList = new ArrayList();
			for (Iterator<MenuItem> itr = itemList.iterator(); itr.hasNext();) {
				MenuItem menuItem = itr.next();
				if (menuItem.getItemId().compareTo(itemId) == 0) {
					tempList.add(menuItem);
				}
			}
			itemList = tempList;
			Collections.sort(itemList, new MenuItem.ItemComparator());
			tableModel.setRows(tempList);
			table.repaint();
			table.revalidate();
		} catch (NumberFormatException e) {
			List<MenuItem> tempList = new ArrayList();
			for (Iterator<MenuItem> itr = itemList.iterator(); itr.hasNext();) {
				MenuItem menuItem = itr.next();
				if (menuItem.getName().contains(itemId)) {
					tempList.add(menuItem);
				}
			}
			itemList = tempList;
			Collections.sort(itemList, new MenuItem.ItemComparator());
			tableModel.setRows(tempList);
			table.repaint();
			table.revalidate();
		}
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

	public void loadPartially() {
		if (tfBarcodeSearch.getText().length() != 0&&!tfBarcodeSearch.getText().equals("Barcode...")) {
			doSearchBarcode(tfBarcodeSearch.getText(), false);
		}else if (tfSearch.getText().length() != 0&&!tfSearch.getText().equals("Suchen...")) {
			String text = tfSearch.getText();
			if (text.length() == 1) {
				text = text.toUpperCase();
				tfSearch.setText(text);
			}
			searchItem(text);
		} else {
			loadAllItem();
		}

	}


	public void exportDataToExcel() {

		FileOutputStream excelFos = null;
		XSSFWorkbook excelJTableExport = null;
		BufferedOutputStream excelBos = null;
		try {

			JFileChooser excelFileChooser = new JFileChooser("C:\\Users\\Authentic\\Desktop");
			excelFileChooser.setDialogTitle("Save As ..");
			FileNameExtensionFilter fnef = new FileNameExtensionFilter("Files", "xls", "xlsx", "xlsm");
			excelFileChooser.setFileFilter(fnef);
			int chooser = excelFileChooser.showSaveDialog(null);
			if (chooser == JFileChooser.APPROVE_OPTION) {
				excelJTableExport = new XSSFWorkbook();
				XSSFSheet excelSheet = excelJTableExport.createSheet("Jtable Export");
				for (int i = 0; i<=tableModel.getRowCount(); i++) {
					XSSFRow excelRow = excelSheet.createRow(i);
					for (int j = 0; j<=tableModel.getColumnCount(); j++) {
						XSSFCell excelCell = excelRow.createCell(j);
						try {
							excelCell.setCellValue(tableModel.getValueAt(i,j).toString());  
						}catch(Exception ex) {
							excelCell.setCellValue("");
						}         
					}
				}
				excelFos = new FileOutputStream(excelFileChooser.getSelectedFile() + ".xlsx");
				excelBos = new BufferedOutputStream(excelFos);
				excelJTableExport.write(excelBos);
				JOptionPane.showMessageDialog(null, "Exported Successfully");
			}

		} catch (FileNotFoundException ex) {
			JOptionPane.showMessageDialog(null, ex);
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, ex);
		} finally {
			try {
				if (excelFos != null) {
					excelFos.close();
				}
				if (excelBos != null) {
					excelBos.close();
				}
				if (excelJTableExport != null) {
					excelJTableExport.close();
				}
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(null, ex);
			}
		}
	}

	public MenuItem duplicateMenuItem(final MenuItem item) {
		final MenuItem menuItem = MenuItemDAO.getInstance().loadMenuItem(item.getId());
		final MenuCategory duplicateCategory = menuItem.getParent().getParent();
		final MenuGroup duplicateGroup = menuItem.getParent();	
		if(duplicateCategory != null) {
			final List<MenuGroup> menuGroups = MenuGroupDAO.getInstance().findByParent(duplicateCategory);
			if (duplicateGroup != null) {
				menuItem.setParent(duplicateGroup);
				return menuItem;
			}
		}
		return null;
	}
	public void searchBarcode(String barcode) {
		MenuItemDAO dao = new MenuItemDAO();
		itemList = dao.findAll();
		try {
			List<MenuItem> tempList = new ArrayList();
			for (Iterator<MenuItem> itr = itemList.iterator(); itr.hasNext();) {
				MenuItem menuItem = itr.next();
				if (menuItem.getBarcode().equals(barcode)) {
					tempList.add(menuItem);
				}
			}
			itemList = tempList;
			Collections.sort(itemList, new MenuItem.ItemComparator());
			tableModel.setRows(tempList);
			table.repaint();
			table.revalidate();

			if (tempList.isEmpty()) {
				int option = JOptionPane.showOptionDialog(BackOfficeWindow.getInstance(),
						"Moechten Sie dieses Barcode " + barcode + " erstellen", POSConstants.CONFIRM, JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (option == JOptionPane.OK_OPTION) {
					MenuItem menuItem = new MenuItem();
					menuItem.setBarcode(barcode);
					MenuItemForm editor;
					try {
						editor = new MenuItemForm(menuItem);
						BeanEditorDialog dialog = new BeanEditorDialog(editor, BackOfficeWindow.getInstance(), true);
						dialog.open();
						if (dialog.isCanceled())
							return;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					table.repaint();

				}
			}
		} catch (NumberFormatException e) {
		}
	}



	public Image getBarcodLevel(MenuItem item) {
		try {		
			HashMap map = new HashMap();
			map.put("itemName", UtilityService.getNameFormated(item.getName()));
			map.put("itemPrice", NumberUtil.formatNumber(item.getPrice()) + " €");
			map.put("itemId", item.getItemId());
			map.put("barcode", UtilityService.createBarcode(item.getBarcode(), 200));
			if(item.getNote()!=null&&!item.getNote().isEmpty()||item.getDescription()!=null&&!item.getDescription().isEmpty())
				map.put("infoLable", UtilityService.buildInfoLable(item.getNote(), item.getDescription()));
			String FILE_RECEIPT_REPORT = "/com/floreantpos/report/template/barcode.jasper";

			JasperPrint jasperPrint = JReportPrintService.createJasperPrint(
					FILE_RECEIPT_REPORT, map, new JREmptyDataSource());
			if (jasperPrint != null)
				jasperPrint.setName("barcode:" + UtilityService.createBarcode(item.getBarcode(), 200));

			return JasperPrintManager.printPageToImage(jasperPrint, 0, 2.0f);					

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/*
	 * Finish A4 PDF
	 */

	//	public void createA4Pdf(List<MenuItem> itemList) {
	//		Document document = new Document(PageSize.A4.rotate());
	//		PdfWriter.getInstance(document, new FileOutputStream("iTextTable.pdf"));
	//		 
	//		document.open();
	//		PdfPTable table = new PdfPTable(3);
	//
	//		for(MenuItem item:itemList) {
	//		com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(getBarcodLevel(item));
	//			PdfPCell cell = new PdfPCell(, false);
	//			table.addCell(new PdfPCell());
	//			if( != 0 && i%3 == 0)
	//	        {
	//	          doc.add(mainTable);
	//	          doc.newPage();
	//	          mainTable =  new PdfPTable(3);
	//	        }
	//		}
	//		 
	//		
	//		 
	//		document.add(table);
	//		document.close();
	//	}


	public void doSearch(String keyword) {
		if (!StringUtils.isEmpty(keyword)){
			List<MenuItem> list1 =itemList.stream().filter(menuItem -> menuItem.getName()!=null&&UtilityService.isMatching(keyword,menuItem.getName())
					||menuItem.getName()!=null&&UtilityService.isMatching(keyword.toLowerCase(),menuItem.getName())
					||menuItem.getName()!=null&&UtilityService.isMatching(keyword.toUpperCase(),menuItem.getName())
					||menuItem.getItemId()!=null&&UtilityService.isMatching(keyword,menuItem.getItemId())
					).collect(Collectors.toList());			
			tableModel.setRows(list1);
			this.tempList = list1;
			table.repaint();
			table.revalidate();			
		}else {
			tableModel.setRows(itemList);
			this.tempList = itemList;
			table.repaint();
			table.revalidate();
		}
	}

	public void doSearchBarcode(String keyword, boolean enter) {
		 
		if (!StringUtils.isEmpty(keyword)){
			List<MenuItem> list1 =itemList.stream().filter(menuItem -> menuItem.getBarcode()!=null&UtilityService.isMatching(ReportUtil.removeLeadingZeroes(keyword), menuItem.getBarcode())				
					).collect(Collectors.toList());
			if(!list1.isEmpty()) {
				this.tempList = list1;
				tableModel.setRows(list1);
				table.repaint();
				table.revalidate();
			}else if(enter){
				int option = JOptionPane.showOptionDialog(BackOfficeWindow.getInstance(),
						"Artikel nicht gefunden!!! \nMoechten Sie Artikel mit dieses Barcode " + keyword + " erstellen??", POSConstants.CONFIRM, JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (option == JOptionPane.YES_OPTION) {
						MenuItem menuItem = new MenuItem();
						menuItem.setBarcode(keyword);
						MenuItemForm editor;
						try {
							editor = new MenuItemForm(menuItem);
							BeanEditorDialog dialog = new BeanEditorDialog(editor, BackOfficeWindow.getInstance(), true);
							dialog.open();
							if (dialog.isCanceled())
								return;							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						
						loadAllItem();
					}
				}
			} else {
				this.tempList = list1;
				tableModel.setRows(list1);
				table.repaint();
				table.revalidate();
			}
			
		}else {
			tableModel.setRows(itemList);
			this.tempList = itemList;
			table.repaint();
			table.revalidate();
		}
	}

	public void loadAllItem() {
		MenuItemDAO dao = new MenuItemDAO();
		itemList = dao.findAll();
		this.tempList = itemList;
		Collections.sort(itemList, new MenuItem.ItemComparator());
		tableModel.setRows(itemList);
		table.repaint();
		table.revalidate();
	}

	class MenuItemExplorerTableModel extends ListTableModel {
		String[] columnNames = { "Pro.Id.", com.floreantpos.POSConstants.NAME, //$NON-NLS-1$
				com.floreantpos.POSConstants.PRICE + " (EUR)", com.floreantpos.POSConstants.VISIBLE, //$NON-NLS-1$
				com.floreantpos.POSConstants.Speisegruppe, com.floreantpos.POSConstants.CATEGORY, com.floreantpos.POSConstants.TAX, "Barcode", "Buy price", //$NON-NLS-1$ //$NON-NLS-2$
				"Price Category",com.floreantpos.POSConstants.Bestand, com.floreantpos.POSConstants.Inhalt};
		
		String[] columnNames_ = { "Pro.Id.", com.floreantpos.POSConstants.NAME, //$NON-NLS-1$
				com.floreantpos.POSConstants.PRICE + " (EUR)", com.floreantpos.POSConstants.VISIBLE, //$NON-NLS-1$
				com.floreantpos.POSConstants.Speisegruppe, com.floreantpos.POSConstants.CATEGORY, com.floreantpos.POSConstants.TAX, "Barcode", "Buy price", //$NON-NLS-1$ //$NON-NLS-2$
				"Price Category",com.floreantpos.POSConstants.Bestand, com.floreantpos.POSConstants.Inhalt, com.floreantpos.POSConstants.Liferant, com.floreantpos.POSConstants.Einkaufspreis };

		MenuItemExplorerTableModel() {
			
			if(TerminalConfig.isLiferantOptionDisplay()) {
				setColumnNames(columnNames_);
			} else {
			setColumnNames(columnNames);
		  }
		}
		
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			if (columnIndex != 3&&columnIndex!=4&&columnIndex!=5)
				return true;
			else
				return false;
		}

		public void setValueAt(Object value, int rowIndex, int columnIndex) {
			try {
				MenuItem menuItem = tempList.get(rowIndex);
				 
				menuItem = MenuItemDAO.getInstance().initialize(menuItem);
				if (columnIndex == 0) {
					menuItem.setItemId(value.toString());
				} else if (columnIndex == 1) {
					menuItem.setName(value.toString());
				} else if (columnIndex == 2) {
					value = value.toString().replace(',', '.');					
					try {
						menuItem.setPrice(NumberUtil.roundToTwoDigit(Double.valueOf((String) value)));
					}catch(Exception ex) {

					}
				} else if (columnIndex == 3) {
					menuItem.setVisible((Boolean.valueOf(value.toString())));
				} else if (columnIndex == 6) {
					menuItem.setTax((Tax) value);
				}else if (columnIndex == 7) {
					menuItem.setBarcode(value.toString());
				} else if (columnIndex == 8) {
					value = value.toString().replace(',', '.');					
					try {
						menuItem.setBuyPrice(NumberUtil.roundToTwoDigit(Double.valueOf((String) value)));
					}catch(Exception ex) {

					}
				} else if (columnIndex == 9) {
					try {
						menuItem.setPriceCategory(Integer.valueOf(value.toString()));
					}catch(Exception ex) {

					}

				} else if (columnIndex == 10) {
					try {
						menuItem.setInstock(Integer.valueOf(value.toString()));
					}catch(Exception ex) {

					}
				} else if (columnIndex == 11) {
					menuItem.setUnitType(value.toString());
				} else if (columnIndex == 12) {
					menuItem.setLieferantName(value.toString());
				} else if (columnIndex == 13) {
					menuItem.setLiferantBuyPrice(NumberUtil.roundToTwoDigit(Double.valueOf((String) value)));
				}
				
				itemList.set(rowIndex, menuItem);
				
				/*tempList.set(rowIndex, menuItem);
				rows.set(rowIndex, menuItem);
				itemList.add(menuItem);*/
				
//				MenuItemForm editor;
				try {
//					editor = new MenuItemForm(menuItem);
				MenuItemDAO.getInstance().saveOrUpdate(menuItem);
				} catch (Exception e) {
					e.printStackTrace();
				}

				this.fireTableDataChanged();
				table.repaint();
			} catch (Exception e) {
			}
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			MenuItem item = (MenuItem) rows.get(rowIndex);
			if (item.getParent() == null || item.getParent().getParent() == null)
				return null;
			if (item.getParent().getParent().getType() == POSConstants.HOME_DELIVERY) {

				return null;
			}
			switch (columnIndex) {
			case 0:
				if (item.getItemId() != null) {
					return item.getItemId();
				}
				return "";

			case 1: {
				return item.getName();
			}

			case 2:
				return NumberUtil.formatNumber(item.getPrice());

			case 3:
				String visible = item.isVisible() + "";
				return visible;

			case 4:
				if (item.getParent() != null) {
					return item.getParent().getName();
				}
				return ""; //$NON-NLS-1$
			case 5:
				return item.getParent().getParent().getName();
			case 6:

				String tax = "";
				if (item.getTax() != null) {
					tax = NumberUtil.formatNumber(item.getTax().getRate()) + "";
					return tax;
				}
				return ""; //$NON-NLS-1$
			case 7:
				return item.getBarcode();
			case 8:
				return NumberUtil.formatNumber(item.getBuyPrice());
			case 9:
				return item.getPriceCategory();	
			case 10:
				return item.getInstock();
			case 11:
				return item.getUnitType();	
			case 12:
				return item.getLieferantName();				
			case 13:
				return item.getLiferantBuyPrice();	
			}
			return null;
		}

		public void addMenuItem(MenuItem menuItem) {
			super.addItem(menuItem);
		}

		public void updateMenuItem(int index) {
			super.updateItem(index);
		}

		public void deleteMenuItem(MenuItem category, int index) {
			super.deleteItem(index);
		}
	}
}
