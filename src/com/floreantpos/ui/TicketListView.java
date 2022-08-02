package com.floreantpos.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.jdesktop.swingx.JXTable;

import com.floreantpos.POSConstants;
import com.floreantpos.actions.SettleTicketAction;
import com.floreantpos.bo.ui.explorer.ListTableModel;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketStatus;
import com.floreantpos.model.TicketType;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.ui.dialog.ErrorMessageDialog;
import com.floreantpos.ui.dialog.OfficialPaymentDialog;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.dialog.CardDialog.CardPaymentType;
import com.floreantpos.ui.dialog.OfficialPaymentDialog.OfficialPaymentType;
import com.floreantpos.ui.views.SwitchboardView;
import com.floreantpos.util.NumberUtil;
import com.floreantpos.util.POSUtil;

public class TicketListView extends JPanel {
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

			this.setForeground(new Color(246,196,120));
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

			this.setForeground(new Color(102,223,102));
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

	public TicketListView()
	{
		table = new TicketListTable();
		table.setSortable(false);
		table.setModel(tableModel = new TicketListTableModel(true));
		table.setRowHeight(40);
		table.setDefaultRenderer(Object.class, new PosTableRenderer());
		table.setGridColor(Color.WHITE);
		table.setBackground(new Color(36,36,35));
		table.setForeground(Color.WHITE);
		table.setSelectionBackground(Color.GRAY);
		cellRenderer.setHorizontalAlignment(JLabel.CENTER);
		table.getColumn(0).setCellRenderer(cellRenderer);
		table.getColumn(1).setCellRenderer(otherRenderer);
		table.getColumn(2).setCellRenderer(otherRenderer);
		table.getColumn(3).setCellRenderer(amountRenderer);
		table.getTableHeader().setBackground(Color.BLACK);
		table.getTableHeader().setForeground(Color.WHITE);
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(50);
		columnModel.getColumn(1).setPreferredWidth(100);
		columnModel.getColumn(2).setPreferredWidth(50);
		columnModel.getColumn(3).setPreferredWidth(100);
		JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane
		.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
		scrollBar.setPreferredSize(new Dimension(30, 60));
		scrollBar.setBackground(Color.WHITE);
		setLayout(new BorderLayout());
		add(scrollPane);
		table.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent me) {

				try
				{	 
					if (SwingUtilities.isRightMouseButton(me))
					{
						if (me.getClickCount() == 1) {
							Point p = me.getPoint();
							int row = table.rowAtPoint(p);
							Ticket ticket =  (Ticket) tableModel.getRowData(row);
							if (ticket != null)
							{
								if(ticket.getMinOrder() != null)
								{
									Double totalAmount = ticket.getTotalAmount() - ticket.getDeliveryCost(); 
									if(totalAmount < ticket.getMinOrder())
									{
										String value = NumberUtil.formatNumber(ticket.getMinOrder());
										String message = "Mindestbestellwert ist nicht erreicht ("+ value+ " €) , Wohlen Sie Fortsetzen?";
										int option = JOptionPane.showOptionDialog(null, message, "ACHTUNG", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,null,null);

										if(option != JOptionPane.YES_OPTION) {
											return;
										}
									}
								}
								Session s = TicketDAO.getInstance().createNewSession();
								List<TicketItem> itemList =TicketDAO.getInstance().get(ticket.getId(),s).getTicketItems();
								List<TicketItem> newItemList = new ArrayList();
								for(Iterator<TicketItem> itr = itemList.iterator();itr.hasNext();)
								{

									TicketItem item = itr.next();
									if(item.isHasModifiers())
									{
										newItemList.add(item);
									}
									else if(!isFound(newItemList, item.getItemCode(), item.getName())) 
									{
										int count = 0;
										count = checkTicketItem(ticket, item.getItemCode(), item.getName(),itemList);
										Double price = item.getUnitPrice();
										item.setTotalAmountWithoutModifiers(count * price);
										item.setItemCount(count);
										newItemList.add(item);
									}
								}
								ticket.setTicketItems(newItemList);
								//TicketDAO.getInstance().update(ticket); 
								new SettleTicketAction(ticket.getId()).execute();
							}
							updateTicketList();
						}

					}else if (me.getClickCount() == 2)
					{
						Point p = me.getPoint();
						int row = table.rowAtPoint(p);
						Ticket ticket =  (Ticket) tableModel.getRowData(row);
						if(ticket == null)
							return;
						int due = (int) POSUtil.getDouble(ticket.getDueAmount());
						if (due != 0) {
							POSMessageDialog.showError("Diese Rechnung ist nicht bezahlt");
							return;
						}
						int option = JOptionPane.showOptionDialog(Application.getPosWindow(), "Moechten Sie diese Rechnung# " + ticket.getId() + " schliessen", "Bestaetigen",
								JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
						if (option != JOptionPane.OK_OPTION) {
							return;
						}
						ticket.setClosed(true);
						TicketDAO.getInstance().saveOrUpdate(ticket);
						User driver = ticket.getAssignedDriver();
						if (driver != null) {
							driver.setAvailableForDelivery(true);
							UserDAO.getInstance().saveOrUpdate(driver);
						}
						updateTicketList();
					}
				}	    	 
				catch(Exception e){}
			}
		});
	}

	public void updateView() {
		table.repaint();
	}

	public int checkTicketItem(Ticket ticket, String id, String name, List<TicketItem> itemList){
		int index = 0;
		for(Iterator<TicketItem> itr = itemList.iterator();itr.hasNext();)
		{
			TicketItem item = itr.next();
			if((item.getItemCode().compareTo(id)==0) && (item.getName().compareTo(name) == 0))
			{
				index = index + item.getItemCount();
			}
		}
		return index;		
	}

	public boolean isFound(List<TicketItem> itemList, String id, String name)
	{
		for(Iterator<TicketItem> itr = itemList.iterator(); itr.hasNext();)
		{
			TicketItem item = itr.next();
			if((item.getItemCode().compareTo(id) == 0 )&& (item.getName().compareTo(name) == 0))
			{
				return true;
			}
		}
		return false;
	}
	public void updateTicketList()
	{
		List<Ticket> tickets = TicketDAO.getInstance().findAllOpenTickets();
		setTickets(tickets);
		repaint();

	}
	public TicketListView(final SwitchboardView switchview) {
		table = new TicketListTable();
		table.setSortable(false);
		table.setModel(tableModel = new TicketListTableModel());
		table.setRowHeight(60);

		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		table.setDefaultRenderer(Object.class, new PosTableRenderer());
		table.setGridColor(Color.WHITE);
		table.setBackground(new Color(36,36,35));
		table.setForeground(Color.WHITE);
		table.setSelectionBackground(Color.GRAY);
		cellRenderer.setHorizontalAlignment(JLabel.CENTER);
		table.getColumn(0).setCellRenderer(cellRenderer);
		table.getColumn(1).setCellRenderer(otherRenderer);
		table.getColumn(2).setCellRenderer(otherRenderer);
		table.getColumn(3).setCellRenderer(amountRenderer);
		table.getColumn(4).setCellRenderer(otherRenderer);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setBackground(Color.BLACK);
		table.getTableHeader().setForeground(Color.WHITE);
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(30);
		columnModel.getColumn(1).setPreferredWidth(70);
		columnModel.getColumn(2).setPreferredWidth(10);
		columnModel.getColumn(3).setPreferredWidth(120);
		JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane
		.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
		scrollPane.setBackground(Color.WHITE);
		scrollBar.setPreferredSize(new Dimension(30, 60));
		scrollBar.setBackground(Color.WHITE);
		setLayout(new BorderLayout());
		table.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent me) {

				try
				{	 
					if (SwingUtilities.isRightMouseButton(me))
					{
						if (me.getClickCount() == 1) {
							Point p = me.getPoint();
							int row = table.rowAtPoint(p);
							Ticket ticket =  (Ticket) tableModel.getRowData(row);
							if(ticket == null)return;
							if (ticket != null)
							{
								if(ticket.getMinOrder() != null)
								{
									Double totalAmount = ticket.getTotalAmount() - ticket.getDeliveryCost(); 
									if(totalAmount < ticket.getMinOrder())
									{
										String value = NumberUtil.formatNumber(ticket.getMinOrder());
										String message = "Mindestbestellwert ist nicht erreicht ("+ value+ " €) , Wohlen Sie Fortsetzen?";
										int option = JOptionPane.showOptionDialog(null, message, "ACHTUNG", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,null,null);

										if(option != JOptionPane.YES_OPTION) {
											return;
										}
									}
								}
							}
							Session s = TicketDAO.getInstance().createNewSession();
							List<TicketItem> itemList =TicketDAO.getInstance().get(ticket.getId(),s).getTicketItems();

							List<TicketItem> newItemList = new ArrayList();
							for(Iterator<TicketItem> itr = itemList.iterator();itr.hasNext();)
							{

								TicketItem item = itr.next();
								if(item.isHasModifiers())
								{
									newItemList.add(item);
								}
								else if(!isFound(newItemList, item.getItemCode(), item.getName())) 
								{
									int count = 0;
									count = checkTicketItem(ticket, item.getItemCode(), item.getName(),itemList);
									Double price = item.getUnitPrice();
									item.setTotalAmountWithoutModifiers(count * price);
									item.setItemCount(count);
									newItemList.add(item);
								}
							}
							//s = TicketDAO.getInstance().createNewSession();
							ticket.setTicketItems(newItemList);
							//TicketDAO.getInstance().update(ticket,s);
							//s.close();
							new SettleTicketAction(ticket.getId()).execute();
							switchview.updateTicketList();
							//s.close();
						}

					}
					else 
					{
						if (me.getClickCount() == 2) {
							Point p = me.getPoint();
							int row = table.rowAtPoint(p);
							Ticket ticket =  (Ticket) tableModel.getRowData(row);
							if (ticket != null)
								switchview.editTicket(ticket);
						}
					}

				}	    	 
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		add(scrollPane);
		setBackground(Color.WHITE);
	}
	public TicketListView(final SwitchboardView switchview,final POSDialog dialog) {
		table = new TicketListTable();
		table.setSortable(false);
		table.setModel(tableModel = new TicketListTableModel());
		table.setRowHeight(60);

		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		table.setDefaultRenderer(Object.class, new PosTableRenderer());
		table.setGridColor(Color.BLACK);
		table.setBackground(Color.WHITE);
		table.setSelectionBackground(new Color(153,255,153));
		table.setSelectionForeground(Color.black);
		cellRenderer.setHorizontalAlignment(JLabel.CENTER);
		table.getColumn(0).setCellRenderer(cellRenderer);
		table.getColumn(1).setCellRenderer(otherRenderer);
		table.getColumn(2).setCellRenderer(otherRenderer);
		table.getColumn(3).setCellRenderer(amountRenderer);
		table.getColumn(4).setCellRenderer(otherRenderer);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setBackground(Color.BLACK);
		table.getTableHeader().setForeground(Color.WHITE);
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(30);
		columnModel.getColumn(1).setPreferredWidth(70);
		columnModel.getColumn(2).setPreferredWidth(10);
		columnModel.getColumn(3).setPreferredWidth(120);
		JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane
		.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
		scrollPane.setBackground(Color.WHITE);
		scrollBar.setPreferredSize(new Dimension(30, 60));
		scrollBar.setBackground(Color.WHITE);
		setLayout(new BorderLayout());
		table.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent me) {

				try
				{	 
					if (SwingUtilities.isRightMouseButton(me))
					{
						if (me.getClickCount() == 1) {
							Point p = me.getPoint();
							int row = table.rowAtPoint(p);
							Ticket ticket =  (Ticket) tableModel.getRowData(row);
							if(ticket == null)return;
							if (ticket != null)
							{
								if(ticket.getMinOrder() != null)
								{
									Double totalAmount = ticket.getTotalAmount() - ticket.getDeliveryCost(); 
									if(totalAmount < ticket.getMinOrder())
									{
										String value = NumberUtil.formatNumber(ticket.getMinOrder());
										String message = "Mindestbestellwert ist nicht erreicht ("+ value+ " €) , Wohlen Sie Fortsetzen?";
										int option = JOptionPane.showOptionDialog(null, message, "ACHTUNG", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,null,null);

										if(option != JOptionPane.YES_OPTION) {
											return;
										}
									}
								}
							}
							Session s = TicketDAO.getInstance().createNewSession();
							List<TicketItem> itemList =TicketDAO.getInstance().get(ticket.getId(),s).getTicketItems();

							List<TicketItem> newItemList = new ArrayList();
							for(Iterator<TicketItem> itr = itemList.iterator();itr.hasNext();)
							{

								TicketItem item = itr.next();
								if(item.isHasModifiers())
								{
									newItemList.add(item);
								}
								else if(!isFound(newItemList, item.getItemCode(), item.getName())) 
								{
									int count = 0;
									count = checkTicketItem(ticket, item.getItemCode(), item.getName(),itemList);
									Double price = item.getUnitPrice();
									item.setTotalAmountWithoutModifiers(count * price);
									item.setItemCount(count);
									newItemList.add(item);
								}
							}
							//s = TicketDAO.getInstance().createNewSession();
							ticket.setTicketItems(newItemList);
							//TicketDAO.getInstance().update(ticket,s);
							//s.close();
							new SettleTicketAction(ticket.getId()).execute();
							switchview.updateTicketList();
							dialog.dispose();
						}

					}
					else 
					{
						if (me.getClickCount() == 2) {
							Point p = me.getPoint();
							int row = table.rowAtPoint(p);
							Ticket ticket =  (Ticket) tableModel.getRowData(row);
							if (ticket != null)
							{
								switchview.editTicket(ticket);
								dialog.dispose();
							}
						}
					}
				}	    	 
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		add(scrollPane);
		setBackground(Color.WHITE);
	}
	public void setTickets(List<Ticket> tickets) {
		Collections.sort(tickets);
		tableModel.setRows(tickets);
		tableModel.fireTableDataChanged();
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
		return tickets;
	}	


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
			 
			
			super(new String[] { "NR",POSConstants.MITARBEITER,"STATUS", POSConstants.SUMME, POSConstants.DATUM  });


		}
		public TicketListTableModel(boolean flag){
			super(new String[] { "NR",POSConstants.MITARBEITER,"STATUS", POSConstants.SUMME, POSConstants.DATUM   });

		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			Ticket ticket = (Ticket) rows.get(rowIndex);

			switch (columnIndex) {
			case 0:
				return rows.size() - rowIndex;
				/*if(ticket.getTicketType() != null)
				{
					if(ticket.getType() == TicketType.DINE_IN)
						return ticket.getId();
					else
						return ticket.getId()+"*";
				}
				else
					return ticket.getId();*/
			case 1:
				return ticket.getOwner().getFirstName();	
			case 2:
				String customerName = ticket.getProperty(Ticket.CUSTOMER_NAME);
				String paymentType = "";
				if(ticket.getOnlinePayment() != null && ticket.getOnlinePayment() == true)
				{
					paymentType = "Online";
				}
				else if (ticket.getCashPayment() != null && ticket.getCashPayment() == true)
				{
					paymentType = "Bar";
				} else if (ticket.getGutscheinPayment() != null && ticket.getGutscheinPayment() == true)
				{
					paymentType = "Guts.";
				} else if ((ticket.getGutscheinPayment() != null && ticket.getGutscheinPayment() == true) || (ticket.getOnlinePayment() != null && ticket.getOnlinePayment() == true) )
				{
					paymentType = "Guts. + Online";
				} else if ((ticket.getGutscheinPayment() != null && ticket.getGutscheinPayment() == true) || (ticket.getCashPayment() != null && ticket.getCashPayment() == true) )
				{
					paymentType = "Guts. + Bar";
				}
				else if (ticket.getRechnugPayemnt()) {
					if (ticket.isRechnugpaid()&&TerminalConfig.isWholeSale()) {
						return POSConstants.BILL+", Bezahlt";
					}else if(TerminalConfig.isWholeSale()) {
						return POSConstants.BILL+", Offen";
					}else {
						return POSConstants.BILL+", Bezahlt";
					}
				}
				else if (ticket.getCashPayment() != null && ticket.getCashPayment() == false)
				{
					paymentType = "Karte";
				}
				
				if(ticket.isPaid()) {
					if(ticket.getStatus() != null) {
						return TicketStatus.valueOf(ticket.getStatus()).toString();
					}
					return paymentType +", Bezahlt";
				}

				return "Offen";

			case 3:
				return NumberUtil.formatNumber(ticket.getTotalAmountWithoutPfand()) + " €";
			case 4:
			{
				Date date = ticket.getClosingDate() != null ? ticket.getClosingDate() : ticket.getCreateDate();
				String dateValue =date+"";
				try {
					dateValue = new SimpleDateFormat("HH:mm, dd-MM-yyyy").format(date);
				} catch (Exception e) {
					e.printStackTrace();
				} 
				return dateValue;
			}
			case 5:
				if(ticket.getTipAmount()!=null&&ticket.getTipAmount()>0.0)
					return NumberUtil.formatNumber(ticket.getTotalAmount()+ticket.getTipAmount()) + " €";
				else
					return NumberUtil.formatNumber(ticket.getTotalAmount()) + " €";	
			}
			return null;
		}
		/*@Override
		public Class getColumnClass(int columnIndex) {
		    if(columnIndex == 0 || columnIndex == 5){
		        return ImageIcon.class;
		    }
		    return Object.class;
		}*/

		/*public ImageIcon getTableNumberIcon(String tblNo, Ticket ticket)
		{
			if(ticket.getType() == TicketType.HOME_DELIVERY)
			{
				if (StringUtils.isNotEmpty(ticket.getDeliveryAddress()))
					return (new ImageIcon (getClass().getResource("/images/delivery.png")));
				else
					return (new ImageIcon (getClass().getResource("/images/pickup.png")));
			}
			else
			{
				if (tblNo.compareTo("1") == 0){return (new ImageIcon (getClass().getResource("/images/1_32.png")));}
				else if (tblNo.compareTo("2") == 0){return new ImageIcon (getClass().getResource("/images/2_32.png"));}
				else if (tblNo.compareTo("3") == 0){return new ImageIcon (getClass().getResource("/images/3_32.png"));}
				else if (tblNo.compareTo("4") == 0){return new ImageIcon (getClass().getResource("/images/4_32.png"));}
				else if (tblNo.compareTo("5") == 0){return new ImageIcon (getClass().getResource("/images/5_32.png"));}
				else if (tblNo.compareTo("6") == 0){return new ImageIcon (getClass().getResource("/images/6_32.png"));}
				else if (tblNo.compareTo("7") == 0){return new ImageIcon (getClass().getResource("/images/7_32.png"));}
				else if (tblNo.compareTo("8") == 0){return new ImageIcon (getClass().getResource("/images/8_32.png"));}
				else if (tblNo.compareTo("9") == 0){return new ImageIcon (getClass().getResource("/images/9_32.png"));}
				else
					return (new ImageIcon (getClass().getResource("/images/delivery.png")));
			}
		}*/
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
	
	public void removeSelected(){
		table.remove(table.getSelectedRow());
		table.repaint();
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
