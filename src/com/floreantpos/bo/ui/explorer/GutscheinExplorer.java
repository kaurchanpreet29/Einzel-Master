package com.floreantpos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.JXDatePicker;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.main.Application;
import com.floreantpos.model.Gutschein;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.dao.GutscheinDAO;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.report.JReportPrintService;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.ConfirmDeleteDialog;
import com.floreantpos.ui.model.GutscheinForm;
import com.floreantpos.ui.util.BusinessDateUtil;
import com.floreantpos.util.NumberUtil;

import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.util.JRProperties;

public class GutscheinExplorer extends TransparentPanel {
	private List<Gutschein> gutscheinList;

	private JTable table;
	JXDatePicker fromDatePicker;
	JXDatePicker toDatePicker;

	private GutscheinExplorerTableModel tableModel;

	public GutscheinExplorer() {
		this.setPreferredSize(new Dimension(800,500));
		gutscheinList = GutscheinDAO.getInstance().findOpenGutscheins();
		tableModel = new GutscheinExplorerTableModel();
		table = new JTable(tableModel);
		table.setDefaultRenderer(Object.class, new PosTableRenderer());

		setLayout(new BorderLayout(5,5));
		JScrollPane jScrollPane = new JScrollPane(table);
		table.getTableHeader().setBackground(new Color(209,222,235));
		jScrollPane.getViewport().setBackground(new Color(209,222,235));
		add(jScrollPane);

		PosButton addButton = new PosButton(com.floreantpos.POSConstants.ADD);
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					GutscheinForm editor = new GutscheinForm();
					BeanEditorDialog dialog = new BeanEditorDialog(editor, BackOfficeWindow.getInstance(), true);
					dialog.open();
					if (dialog.isCanceled())
						return;

					tableModel.addGutschein((Gutschein) editor.getBean());
				} catch (Exception x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});


		PosButton editButton = new PosButton(com.floreantpos.POSConstants.EDIT);
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;

					Gutschein gutschein = gutscheinList.get(index);

					BeanEditorDialog dialog = new BeanEditorDialog(new GutscheinForm(gutschein), BackOfficeWindow.getInstance(), true);
					dialog.open();
					if (dialog.isCanceled())
						return;

					table.repaint();
				} catch (Throwable x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		PosButton deleteButton = new PosButton(com.floreantpos.POSConstants.DELETE);
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int selectedRows[] = table.getSelectedRows();
					List<Gutschein> deleteList = new ArrayList<Gutschein>();
					if(selectedRows.length>1&&ConfirmDeleteDialog.showMessage(GutscheinExplorer.this,
							"Sind sie sicher alle löeschen?",
							"Alle Löeschen?") != ConfirmDeleteDialog.NO) {
						for (int i = 0; i < selectedRows.length; i++) {
							Gutschein gutschein = gutscheinList.get(selectedRows[i]);
							TaxDAO.getInstance().delete(gutschein);
						}
						loadAll();
					}
					else {
						int index = table.getSelectedRow();
						if (index < 0)
							return;
						if (ConfirmDeleteDialog.showMessage(GutscheinExplorer.this, com.floreantpos.POSConstants.CONFIRM_DELETE, com.floreantpos.POSConstants.DELETE) == ConfirmDeleteDialog.YES) {
							Gutschein gutschein = gutscheinList.get(index);
							TaxDAO.getInstance().delete(gutschein);
							tableModel.deleteTax(gutschein, index);
						}
					}


				} catch (Exception x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		JCheckBox cbShowAll = new JCheckBox("Alles anzeigen");
		if(StringUtils.isNotEmpty(POSConstants.Alles_anzeigen))
			cbShowAll.setText(POSConstants.Alles_anzeigen);
		 
		
		cbShowAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(cbShowAll.isSelected()) {
					gutscheinList = GutscheinDAO.getInstance().findAll();
				} else {
					gutscheinList = GutscheinDAO.getInstance().findOpenGutscheins();
				}
				table.repaint();
				table.revalidate();
			} 

		});


		fromDatePicker = new JXDatePicker();
		fromDatePicker.setDate(Calendar.getInstance().getTime());
		fromDatePicker.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		fromDatePicker.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				loadTiemPeriod(cbShowAll.isSelected());				
			}
		});

		toDatePicker = new JXDatePicker();
		toDatePicker.setDate(Calendar.getInstance().getTime());
		toDatePicker.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		toDatePicker.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				loadTiemPeriod(cbShowAll.isSelected());				
			}
		});
		
		
		
		PosButton printButton = new PosButton("Druck-Report");
		if(StringUtils.isNotEmpty(POSConstants.Druck_Report))
			printButton.setText(POSConstants.Druck_Report);
		 
		
		printButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					printGutscheinReport();
				} catch (Throwable x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});
		

		cbShowAll.setBackground(new Color(209,222,235));

		TransparentPanel panel = new TransparentPanel();
		panel.setLayout(new MigLayout());
		panel.add(addButton);
		panel.add(editButton);
		panel.add(deleteButton);
		panel.add(cbShowAll, "wrap");
		panel.add(fromDatePicker);
		panel.add(toDatePicker);
		panel.add(printButton);
		add(panel, BorderLayout.SOUTH);
	}

	public void loadTiemPeriod(boolean all) {		
		Date fromDate = fromDatePicker.getDate();
		Date toDate = toDatePicker.getDate();
		if(all)
			gutscheinList = GutscheinDAO.getInstance().findByDates(BusinessDateUtil.startOfOfficialDay(fromDate), BusinessDateUtil.endOfOfficialDay(toDate)); 
		else
			gutscheinList = GutscheinDAO.getInstance().findByDates(BusinessDateUtil.startOfOfficialDay(fromDate), BusinessDateUtil.endOfOfficialDay(toDate), false); 	
		table.repaint();
		table.revalidate();
	}

	public void printGutscheinReport() {
		Date fromDate = fromDatePicker.getDate();
		Date toDate = toDatePicker.getDate();
		gutscheinList = GutscheinDAO.getInstance().findByDates(BusinessDateUtil.startOfOfficialDay(fromDate), BusinessDateUtil.endOfOfficialDay(toDate));
		SimpleDateFormat dtFormat =new SimpleDateFormat("dd.MM.yyyy HH:mm");
		SimpleDateFormat dtFormat1 =new SimpleDateFormat("dd.MM.yy");

		if(gutscheinList!=null&&gutscheinList.size()>0) {
			Double totalAmount = 0.0;
			Double totalOpenAmount = 0.0;
			Double totalCloseAmount = 0.0;
			int total = 0;
			int totalOpen = 0;
			int totalClosed = 0;
			for(Gutschein guts:gutscheinList) {
				double open = 0.0;
				double used = 0.0;
				if(guts.isSplitted()!=null&&guts.isSplitted()) {
					open = guts.getValue();
					used = guts.getSplittedAmount();
				}else if(guts.isClosed()!=null&&guts.isClosed()){
					used +=guts.getValue();					
				}else {
					open += guts.getValue();
				}
				if(guts.isClosed()!=null&&guts.isClosed())
					totalClosed +=1;
				else
					totalOpen +=1;
				totalAmount += open+used;
				totalCloseAmount += used;
				totalOpenAmount +=open;
				total +=1;
			}	

			Restaurant rest = RestaurantDAO.getInstance().getRestaurant();
			Map<String, String> map = new HashMap<String, String>(2);
			map.put("restaurantName", rest.getName());
			map.put("restStreet", rest.getAddressLine1());
			map.put("restZipOrt", rest.getZipCode()+", "+rest.getAddressLine3());
			map.put("reportTitle", "Gütschein - Report");
			map.put("reportTime", dtFormat.format(new Date()));
			map.put("fromDay", dtFormat.format(fromDate));
			map.put("toDay", dtFormat.format(toDate));
			map.put("totalOpenAmount", NumberUtil.formatNumber(totalOpenAmount)+Application.getCurrencySymbol());
			map.put("totalUsedAmount", NumberUtil.formatNumber(totalCloseAmount)+Application.getCurrencySymbol());
			map.put("totalAmount", NumberUtil.formatNumber(totalAmount)+Application.getCurrencySymbol());
			map.put("totalOpen", totalOpen+"");
			map.put("totalClosed", totalClosed+"");
			map.put("total", total+"");
			 
			try {
				JasperPrint jasperPrint = JReportPrintService.createJasperPrint("/com/floreantpos/report/template/gutschein_report.jasper", map, new JREmptyDataSource());
				JRProperties.setProperty("net.sf.jasperreports.xpath.executer.factory",
						"net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");
				jasperPrint.setName("GutscheinReport_" + dtFormat1.format(fromDate)+"-"+dtFormat1.format(toDate));
				jasperPrint.setProperty("printerName", Application.getPrinters().getA4Printer());			
				JReportPrintService.printQuitelyA4(jasperPrint);
			}catch (Exception e) {
				// TODO: handle exception
			}		

		}


	}




	public void loadAll() {
		gutscheinList = GutscheinDAO.getInstance().findAll();    
		table.repaint();
		table.revalidate();
	}

	class GutscheinExplorerTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;
		String[] columnNames = {POSConstants.ID, POSConstants.PRICE+" (EUR)", "Barcode", POSConstants.Gueltig_bis, POSConstants.Unendlich, "Status", POSConstants.USERS, POSConstants.Erstellt};

		public int getRowCount() {
			if(gutscheinList == null) {
				return 0;
			}
			return gutscheinList.size();
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
			return false;
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			if(gutscheinList == null)
				return ""; //$NON-NLS-1$

			Gutschein gutschein = gutscheinList.get(rowIndex);
			SimpleDateFormat df =new SimpleDateFormat("dd.MM.yy");
			switch(columnIndex) {
			case 0:
				return String.valueOf(gutschein.getId());

			case 1:
				return NumberUtil.formatNumber(gutschein.getValue());

			case 2:
				return gutschein.getBarcode();

			case 3:
				return df.format(gutschein.getExpiryDate()!=null?gutschein.getExpiryDate():new Date());

			case 4:
				return gutschein.isUnlimited();

			case 5:
				if(gutschein.isClosed() != null && gutschein.isClosed()) {
					return "Abgeschlossen";
				} else {
					return "Offnen";
				}

			case 6:
				return StringUtils.isNotBlank(gutschein.getCreatedBy()) ? gutschein.getCreatedBy() : "Admin";

			case 7:
				return gutschein.getCreatedDate() != null ? df.format(gutschein.getCreatedDate()) : "-";

			}

			return null;
		}

		public void addGutschein(Gutschein gutschein) {
			int size = gutscheinList.size();
			gutscheinList.add(gutschein);
			fireTableRowsInserted(size, size);
		}

		public void deleteTax(Gutschein gutschein, int index) {
			gutscheinList.remove(gutschein);
			fireTableRowsDeleted(index, index);
		}
	}
}
