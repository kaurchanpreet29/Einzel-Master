package com.floreantpos.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.text.html.HTML;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.commons.lang.StringUtils;
import org.jdesktop.swingx.JXTable;
import org.jfree.ui.Align;

import com.floreantpos.POSConstants;
import com.floreantpos.actions.SettleTicketAction;
import com.floreantpos.bo.ui.explorer.ListTableModel;
import com.floreantpos.main.Application;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketStatus;
import com.floreantpos.model.TicketType;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.report.JReportPrintService;
import com.floreantpos.report.TicketPrintProperties;
import com.floreantpos.swing.ImageIcon;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.views.OrderInfoView;
import com.floreantpos.ui.views.SwitchboardView;
import com.floreantpos.ui.views.TicketReceiptView;
import com.floreantpos.ui.views.TicketDetailView.PrintType;
import com.floreantpos.util.NumberUtil;
import com.floreantpos.util.POSUtil;

public class DeliveryTicketListView extends JPanel {
	private TicketListTable table;
	private TicketListTableModel tableModel;
    DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
	    Font font = new Font("Times New Roman", Font.BOLD, 35);
	    
	    @Override
	    public Component getTableCellRendererComponent(JTable table,
	            Object value, boolean isSelected, boolean hasFocus,
	            int row, int column) {
	        super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
	                row, column);
	        this.setAlignmentX(Component.CENTER_ALIGNMENT);
	        
	        this.setForeground(new Color(255,128,0));
	        setFont(font);
	        return this;
	    }

	};
	DefaultTableCellRenderer amountRenderer = new DefaultTableCellRenderer() {
	    Font font = new Font("Times New Roman", Font.BOLD, 20);
	    
	    @Override
	    public Component getTableCellRendererComponent(JTable table,
	            Object value, boolean isSelected, boolean hasFocus,
	            int row, int column) {
	        super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
	                row, column);
	        this.setAlignmentX(Component.CENTER_ALIGNMENT);
	        
	        this.setForeground(new Color(0,153,0));
	        setFont(font);
	        return this;
	    }

	};
	DefaultTableCellRenderer otherRenderer = new DefaultTableCellRenderer() {
	    Font font = new Font("Times New Roman", Font.BOLD, 16);

	    @Override
	    public Component getTableCellRendererComponent(JTable table,
	            Object value, boolean isSelected, boolean hasFocus,
	            int row, int column) {
	        super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
	                row, column);
	        setFont(font);
	        return this;
	    }

	};
	public DeliveryTicketListView()
	{
		setLayout(new BorderLayout());
		table = new TicketListTable();
		table.setSortable(false);
		table.setModel(tableModel = new TicketListTableModel());
		table.setRowHeight(40);
		setPreferredSize(new Dimension(800,600));
		table.setDefaultRenderer(Object.class, new PosTableRenderer());
		table.setGridColor(Color.BLACK);
		table.setBackground(Color.WHITE);
		table.setSelectionBackground(new Color(153,255,153));
		table.setSelectionForeground(Color.black);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		cellRenderer.setHorizontalAlignment(JLabel.CENTER);
		table.getColumn(0).setCellRenderer(otherRenderer);
		table.getColumn(1).setCellRenderer(otherRenderer);
		table.getColumn(2).setCellRenderer(otherRenderer);
		table.getTableHeader().setBackground(new Color(209,222,235));
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(150);
		columnModel.getColumn(1).setPreferredWidth(80);
		columnModel.getColumn(2).setPreferredWidth(130);
		JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
		
		scrollBar.setPreferredSize(new Dimension(30, 60));
		scrollBar.setBackground(Color.WHITE);
		setLayout(new BorderLayout());
		JPanel westPanel = new JPanel();
		westPanel.setBackground(new Color(209,222,235));
		westPanel.add(scrollPane);
		
		final JPanel eastPanel = new JPanel();
		eastPanel.setLayout(new MigLayout());
		add(westPanel,BorderLayout.WEST);
		add(eastPanel,BorderLayout.CENTER);
		eastPanel.setBackground(new Color(209,222,235));
		setBackground(new Color(209,222,235));
	   	table.addMouseListener(new MouseAdapter() {
	    	
		    public void mousePressed(MouseEvent me) {
		    	 
		    	 try
		    	 {	
		    		 if (me.getClickCount() == 1) {
		    				 Point p = me.getPoint();
		    				 int row = table.rowAtPoint(p);
		    				 Ticket ticket1 =  (Ticket) tableModel.getRowData(row);
		    				 
		    				 if (ticket1 != null)
		    				 {
		    					 Ticket ticket = TicketDAO.getInstance().loadFullTicket(ticket1.getId());
		    					 
		    					 TicketPrintProperties printProperties = new TicketPrintProperties("*** RECHNUNG ***", true, true, true,false);
		    					 HashMap map = JReportPrintService.populateTicketProperties(ticket, printProperties, null,PrintType.REGULAR,true, 0.00);
		    					 final JasperPrint jasperPrint;
		    					 jasperPrint = JReportPrintService.createPrint(ticket, map, null,false);
		    					
		    					 TicketReceiptView receiptView = new TicketReceiptView(jasperPrint);
		    					 eastPanel.removeAll();
		    					 JScrollPane scrollPane = new JScrollPane(receiptView.getReportPanel(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		    					 JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
		    						
		    					 scrollBar.setPreferredSize(new Dimension(30, 60));
		    					 scrollBar.setBackground(Color.WHITE);
		    					 scrollPane.setPreferredSize(new Dimension(400,600));
		    					 eastPanel.setPreferredSize(new Dimension(400,600));
		    					
		    					 
		    					 JButton button = new JButton();
		    					 button.setText("DRUCK");
		    					 button.setBackground(new Color(102,255,102));
		    					 button.setFont(new Font("Times New Roman", Font.BOLD, 16));
		    					 button.addActionListener(new ActionListener(){

									@Override
									public void actionPerformed(ActionEvent arg0) {
										try{
										jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
										JReportPrintService.printQuitely(jasperPrint);
										}
										catch(Exception e){}
									}
		    						 
		    					 });
		    					 
		    					 
//		    					 JButton duplicate = new JButton();
//		    					 duplicate.setText("Duplikat");
//		    					 duplicate.setBackground(new Color(102,255,102));
//		    					 duplicate.setFont(new Font("Times New Roman", Font.BOLD, 16));
//		    					 duplicate.addActionListener(new ActionListener(){
//
//									@Override
//									public void actionPerformed(ActionEvent arg0) {
//										try{
//										jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
//										JReportPrintService.printQuitely(jasperPrint);
//										}
//										catch(Exception e){}
//									}
//		    						 
//		    					 });
//		    					 
//		    					 eastPanel.add(duplicate);		    					 
		    					 eastPanel.add(button,"wrap");
		    					 eastPanel.add(scrollPane);
		    					 eastPanel.revalidate();
		    					 eastPanel.repaint();
		    					 repaint();
		    				 }
		    	     }
		    	}	    	 
		    	catch(Exception e){}
		    }
		});
	}
	public void updateTicketList()
	{
		List<Ticket> tickets = TicketDAO.getInstance().findAllOpenTickets();
		setTickets(tickets);
		repaint();
		
	}
	
	public void setTickets(List<Ticket> tickets) {
		tableModel.setRows(tickets);
	}

	public void addTicket(Ticket ticket) {
		tableModel.addItem(ticket);
	}

	public Ticket getSelectedTicket() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow < 0) {
			return null;
		}

		return (Ticket) tableModel.getRowData(selectedRow);
	}

	public List<Ticket> getSelectedTickets() {
		int[] selectedRows = table.getSelectedRows();
		
		
		ArrayList<Ticket> tickets = new ArrayList<Ticket>(selectedRows.length);

		for (int i = 0; i < selectedRows.length; i++) {
			Ticket ticket = (Ticket) tableModel.getRowData(selectedRows[i]);
			tickets.add(ticket);
		}
		System.out.println(tickets.size());
		return tickets;
	}

	// public void removeTicket(Ticket ticket) {
	// tableModel.
	// }

	public class TicketListTable extends JXTable {
		
		public TicketListTable() {
			setColumnControlVisible(true);
		}
		public void scrollUp() {
			
			int selectedRow = getSelectedRow();
			int rowCount = this.getRowCount();

			if (selectedRow <= 0) {
				selectedRow = rowCount - 1;
			}
			else if (selectedRow > (rowCount - 1)) {
				selectedRow = rowCount - 1;
			}
			else {
				--selectedRow;
			}

			selectionModel.addSelectionInterval(selectedRow, selectedRow);
			Rectangle cellRect = getCellRect(selectedRow, 0, false);
			scrollRectToVisible(cellRect);
		}
		public void scrollDown() {
			
			int selectedRow = getSelectedRow();
			if (selectedRow < 0) {
				selectedRow = 0;
			}
			else if (selectedRow >= this.getRowCount() - 1) {
				selectedRow = 0;
			}
			else {
				++selectedRow;
			}

			selectionModel.addSelectionInterval(selectedRow, selectedRow);
			Rectangle cellRect = getCellRect(selectedRow, 0, false);
			scrollRectToVisible(cellRect);
		}
		@Override
		public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
			ListSelectionModel selectionModel = getSelectionModel();
			boolean selected = selectionModel.isSelectedIndex(rowIndex);
			if (selected) {
				selectionModel.removeSelectionInterval(rowIndex, rowIndex);
			}
			else {
				selectionModel.addSelectionInterval(rowIndex, rowIndex);
			}
		}
	}
	
	private class TicketListTableModel extends ListTableModel {
		
		public TicketListTableModel() {
			
			super(new String[] { POSConstants.CUSTOMER,POSConstants.TOTAL, "DATUM"  });
		
			
		}
		
		public Object getValueAt(int rowIndex, int columnIndex) {
			Ticket ticket = (Ticket) rows.get(rowIndex);
			
			switch (columnIndex) {
			case 0:
				String customerPhone = ticket.getProperty(Ticket.CUSTOMER_PHONE);
				
				String CustomerAddress = ticket.getProperty(Ticket.CUSTOMER_ADDRESS);
				String CustomerName = ticket.getProperty(Ticket.CUSTOMER_NAME);
				if (customerPhone != null) {
					return CustomerName + ",\n" + CustomerAddress + ",\n" + customerPhone;
				}
				
				return "Gaeste";
				
			case 1:
				if(ticket.getMinOrder() != null)
				{
					Double totalAmount = ticket.getTotalAmount() - ticket.getDeliveryCost();
					return NumberUtil.formatNumber(totalAmount) + " €";
				}
				return NumberUtil.formatNumber(ticket.getTotalAmount()) + " €";
			case 2:
				{
					Date date = ticket.getCreateDate();
					int month = date.getMonth()+1;
					String monthString = month+"";
					if(monthString.length() == 1 )
						monthString = "0"+monthString;
					int year = date.getYear()+1900;
					
					
					int dat = date.getDate();
					String dateStrin = dat+"";
					if(dateStrin.length() == 1)
						dateStrin = "0"+dateStrin;
					
					int hour = date.getHours();
					String hourString = hour+"";
					if(hourString.length() == 1)
						hourString = "0"+hourString;
					
					int min = date.getHours();
					String minString = min+"";
					if(minString.length() == 1)
						minString = "0"+minString;
					
					String dateString = dateStrin + "-"+ monthString + "-"+ year + ", " + hourString + ":"+ minString; 
					return dateString;
				}
								
			}

			return null;
		}
		
	}
	
	public Ticket getFirstSelectedTicket() {
		List<Ticket> selectedTickets = getSelectedTickets();

		if (selectedTickets.size() == 0 || selectedTickets.size() > 1) {
			
			if(StringUtils.isNotEmpty(POSConstants.Bestellung_aus))
				POSMessageDialog.showMessage(POSConstants.Bestellung_aus);
			else
				POSMessageDialog.showMessage("Bitte Waehlen Sie eine Bestellung aus");
			
			 
			return null;
		}

		Ticket ticket = selectedTickets.get(0);

		return ticket;
	}

	public int getFirstSelectedTicketId() {
		Ticket ticket = getFirstSelectedTicket();
		if(ticket == null) {
			return -1;
		}
		
		return ticket.getId();
	}

	public TicketListTable getTable() {
		return table;
	}
	
}
