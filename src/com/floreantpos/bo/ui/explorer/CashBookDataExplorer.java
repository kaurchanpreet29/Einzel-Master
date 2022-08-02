package com.floreantpos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import org.jdesktop.swingx.JXTable;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.main.Application;
import com.floreantpos.model.CashBookData;
import com.floreantpos.model.dao.CashbookDataDAO;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.dialog.POSDialog;

public class CashBookDataExplorer extends POSDialog {

	private JXTable table;
	private CashBookDataExplorerTableModel tableModel;
	private List<CashBookData> cashbookData;
	JButton addButton;
	JButton deleteButton;

	public CashBookDataExplorer() {
		initComponents();
	}

	DefaultTableCellRenderer otherRenderer = new DefaultTableCellRenderer() {
		Font font = new Font("Times New Roman", Font.PLAIN, 16);

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			setFont(font);
			this.setHorizontalAlignment(SwingConstants.LEFT);
			return this;
		}

	};

	public void initComponents() {
		setLayout(new BorderLayout());
		cashbookData = CashbookDataDAO.getInstance().findAll();
		if (cashbookData.isEmpty()) {
			cashbookData = new ArrayList();
			generateAnfangsBestand();
		}
		tableModel = new CashBookDataExplorerTableModel();

		tableModel.setRows(cashbookData);

		table = new JXTable(tableModel);

		TableColumn typeColumn = table.getColumnModel().getColumn(2);
		JComboBox comboBox = new JComboBox();
		comboBox.setFont(new Font(null, Font.BOLD, 28));
		comboBox.addItem("Einzahlung (+)");
		comboBox.addItem("Auszahlung (-)");
		typeColumn.setCellEditor(new DefaultCellEditor(comboBox));

		table.setRowHeight(35);
		table.getTableHeader().setBackground(new Color(209, 222, 235));

		DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
		leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

		table.getColumnModel().getColumn(0).setCellRenderer(otherRenderer);
		table.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);

		table.getColumnModel().getColumn(0).setPreferredWidth(300);
		table.getColumnModel().getColumn(1).setPreferredWidth(250);

		final JScrollPane jScrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(30, 20));
		jScrollPane.getVerticalScrollBar().setBackground(Color.WHITE);
		getContentPane().add(jScrollPane, BorderLayout.CENTER);
		ExplorerButtonPanel explorerButton = new ExplorerButtonPanel();

		addButton = explorerButton.getAddButton();
		addButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				CashBookData cashbookInfo = null;
				cashbookInfo = new CashBookData();
				cashbookInfo.setBeschreibung("");
				cashbookInfo.setAuszahlung(false);
				cashbookInfo.setEinzahlung(true);
				cashbookData.add(cashbookInfo);
				CashbookDataDAO.getInstance().save(cashbookInfo);
				table.repaint();
				tableModel.fireTableDataChanged();
				JScrollBar vertical = jScrollPane.getVerticalScrollBar();
				vertical.setValue(vertical.getMinimum());
			}
		});
		deleteButton = explorerButton.getDeleteButton();
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int index = table.getSelectedRow();
				CashBookData cashbookInfo = cashbookData.get(index);
				if(Application.getCurrentUser().getFirstName().compareTo("Master")==0||Application.getCurrentUser().getFirstName().compareTo("Super-User")==0) {
					tableModel.deleteItem(index);
					CashbookDataDAO.getInstance().delete(cashbookInfo);
					return;
				}
				if(cashbookInfo.getBeschreibung().contains("Anfangs Bestand Ein.")
						||cashbookInfo.getBeschreibung().contains("Anfangs Bestand Aus.")
						||cashbookInfo.getBeschreibung().contains("Karte 19% - 1")
						||cashbookInfo.getBeschreibung().contains("Karte 7% - 1")
						||cashbookInfo.getBeschreibung().contains("Bar 19% - 1")
						||cashbookInfo.getBeschreibung().contains("Bar 7% - 1")
						||cashbookInfo.getBeschreibung().contains("Karte Zahlung")
						)
					return;
				tableModel.deleteItem(index);
				CashbookDataDAO.getInstance().delete(cashbookInfo);
			}
		});
		TransparentPanel panel = new TransparentPanel();
		panel.add(addButton);
		panel.add(deleteButton);
		JButton generate = new JButton("Anfangs-Beschriebung");
		generate.setFont(new Font("Times New Roman", Font.BOLD, 15));
		generate.setBackground(new Color(102, 255, 102));
		generate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean check =false;
				for(CashBookData cbook:cashbookData) {
					if(cbook.getBeschreibung().contains("Anfangs Bestand Ein.")||cbook.getBeschreibung().contains("Anfangs Bestand Aus.")) {
						check = true;
						break;
					}
				}
				if(!check) {
					generateAnfangsBestand();
					cashbookData = CashbookDataDAO.getInstance().findAll();
					tableModel.setRows(cashbookData);
					table.repaint();
					tableModel.fireTableDataChanged();
				}
			}
		});

		panel.add(generate);


		getContentPane().add(panel, BorderLayout.SOUTH);
		getContentPane().setBackground(new Color(209, 222, 235));
		jScrollPane.getViewport().setBackground(new Color(209, 222, 235));
	}

	public void generateAnfangsBestand() {
		CashBookData cashbookInfo = new CashBookData();
		cashbookInfo.setBeschreibung("Anfangs Bestand Ein.");
		cashbookInfo.setEinzahlung(true);
		cashbookInfo.setAuszahlung(false);
		cashbookInfo.setKonto(2000);
		CashbookDataDAO.getInstance().saveOrUpdate(cashbookInfo);				

		cashbookInfo = new CashBookData();
		cashbookInfo.setBeschreibung("Anfangs Bestand Aus.");
		cashbookInfo.setEinzahlung(false);
		cashbookInfo.setAuszahlung(true);
		cashbookInfo.setKonto(2000);
		CashbookDataDAO.getInstance().saveOrUpdate(cashbookInfo);

		cashbookInfo = new CashBookData();
		cashbookInfo.setBeschreibung("Karte Zahlung");
		cashbookInfo.setEinzahlung(false);
		cashbookInfo.setAuszahlung(true);
		cashbookInfo.setKonto(1300);
		CashbookDataDAO.getInstance().saveOrUpdate(cashbookInfo);

		cashbookInfo = new CashBookData();
		cashbookInfo.setBeschreibung("Karte 7% - 1");
		cashbookInfo.setEinzahlung(true);
		cashbookInfo.setAuszahlung(false);
		cashbookInfo.setKonto(8300);
		CashbookDataDAO.getInstance().saveOrUpdate(cashbookInfo);				

		cashbookInfo = new CashBookData();
		cashbookInfo.setBeschreibung("Karte 19% - 1");
		cashbookInfo.setEinzahlung(true);
		cashbookInfo.setAuszahlung(false);
		cashbookInfo.setKonto(8400);
		CashbookDataDAO.getInstance().saveOrUpdate(cashbookInfo);

		cashbookInfo = new CashBookData();
		cashbookInfo.setBeschreibung("Bar 7% - 1");
		cashbookInfo.setEinzahlung(true);
		cashbookInfo.setAuszahlung(false);
		cashbookInfo.setKonto(8300);

		CashbookDataDAO.getInstance().saveOrUpdate(cashbookInfo);
		cashbookInfo = new CashBookData();
		cashbookInfo.setBeschreibung("Bar 19% - 1");
		cashbookInfo.setEinzahlung(true);
		cashbookInfo.setAuszahlung(false);
		cashbookInfo.setKonto(8400);
		CashbookDataDAO.getInstance().saveOrUpdate(cashbookInfo);
		cashbookData = CashbookDataDAO.getInstance().findAll();
	}

	class CashBookDataExplorerTableModel extends ListTableModel {
		String[] columnNames = { "Konto-Nr.","Beschreibung", "Einzahlung" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		// //$NON-NLS-4$

		CashBookDataExplorerTableModel() {
			setColumnNames(columnNames);

		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return true;
		}

		public void setValueAt(Object value, int rowIndex, int columnIndex) {
			boolean edit = false;
			if(Application.getCurrentUser().getFirstName().compareTo("Master")==0||Application.getCurrentUser().getFirstName().compareTo("Super-User")==0) {
				edit = true;
			}

			try {
				CashBookData cashbookInfo = cashbookData.get(rowIndex);        
				if (columnIndex == 0) {
					int konto = 0;
					if(edit) {

					}else if(cashbookInfo.getBeschreibung().contains("Anfangs Bestand Ein.")
							||cashbookInfo.getBeschreibung().contains("Anfangs Bestand Aus.")
							||cashbookInfo.getBeschreibung().contains("Karte 19% - 1")
							||cashbookInfo.getBeschreibung().contains("Karte 7% - 1")
							||cashbookInfo.getBeschreibung().contains("Bar 19% - 1")
							||cashbookInfo.getBeschreibung().contains("Bar 7% - 1")
							||cashbookInfo.getBeschreibung().contains("Karte Zahlung")
							) {
						return;
					}


					try {
						konto = Integer.parseInt(value.toString());
						//						for(CashBookData data: CashbookDataDAO.getInstance().findAll()) {
						//							if(konto==data.getKonto()) {
						//								JOptionPane.showMessageDialog(BackOfficeWindow.getInstance(), "Diese Nummer ist bereits vergeben, bitte geben Sie eine andere Nummer an!!!");
						//								return;
						//							}
						//						}
					}catch(Exception ex) {
						JOptionPane.showMessageDialog(BackOfficeWindow.getInstance(), "Bitte geben Sie die Nummer nur auf Konto-Nr.!!!");
						return;

					}
					cashbookInfo.setKonto(konto);
				}else if (columnIndex == 1) {
					if(edit) {

					}else if(cashbookInfo.getBeschreibung().contains("Anfangs Bestand Ein.")
							||cashbookInfo.getBeschreibung().contains("Anfangs Bestand Aus.")
							||cashbookInfo.getBeschreibung().contains("Karte 19% - 1")
							||cashbookInfo.getBeschreibung().contains("Karte 7% - 1")
							||cashbookInfo.getBeschreibung().contains("Bar 19% - 1")
							||cashbookInfo.getBeschreibung().contains("Bar 7% - 1")
							||cashbookInfo.getBeschreibung().contains("Karte Zahlung")
							) {
						return;
					}
					cashbookInfo.setBeschreibung(value.toString());
				} else if (columnIndex == 2) {
					if(edit) {

					}else if(cashbookInfo.getBeschreibung().contains("Anfangs Bestand Ein.")
							||cashbookInfo.getBeschreibung().contains("Anfangs Bestand Aus.")
							||cashbookInfo.getBeschreibung().contains("Karte 19% - 1")
							||cashbookInfo.getBeschreibung().contains("Karte 7% - 1")
							||cashbookInfo.getBeschreibung().contains("Bar 19% - 1")
							||cashbookInfo.getBeschreibung().contains("Bar 7% - 1")
							||cashbookInfo.getBeschreibung().contains("Karte Zahlung")
							) {
						return;
					}
					if ("Einzahlung (+)".equals(value.toString())) {        	  
						cashbookInfo.setEinzahlung(true);
						cashbookInfo.setAuszahlung(false);
					} else {        	  
						cashbookInfo.setEinzahlung(false);
						cashbookInfo.setAuszahlung(true);
					}
				}
				CashbookDataDAO.getInstance().saveOrUpdate(cashbookInfo);
				this.fireTableDataChanged();
				table.repaint();
			} catch (Exception e) {
			}
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			CashBookData cashbookInfo = (CashBookData) rows.get(rowIndex);

			switch (columnIndex) {
			case 0:
				return cashbookInfo.getKonto();
			case 1:
				return cashbookInfo.getBeschreibung();
			case 2:
				if(cashbookInfo.isEinzahlung()) {
					return "Einzahlung";
				} else {
					return "Auszahlung";
				}
			}
			return null;
		}
	}
}
