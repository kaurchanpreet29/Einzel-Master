package com.floreantpos.ui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXTable;

import com.floreantpos.bo.ui.explorer.ListTableModel;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.Reservation;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.ReservationDAO;
import com.floreantpos.swing.ImageIcon;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.PosTableRenderer;

import java.util.ArrayList;

import net.miginfocom.swing.MigLayout;
public class ReservationView extends JPanel{
	private List<Reservation> permList;
	ReservationView()
	{
		initComponents();
	}
	DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
	    Font font = new Font("Times New Roman", Font.BOLD, 15);
	    
	    @Override
	    public Component getTableCellRendererComponent(JTable table,
	            Object value, boolean isSelected, boolean hasFocus,
	            int row, int column) {
	        super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
	                row, column);
	        this.setAlignmentX(Component.CENTER_ALIGNMENT);
	        
	        //this.setForeground(new Color(255,128,128));
	        setFont(font);
	        return this;
	    }

	};

	public void initComponents()
	{
		setLayout(new BorderLayout());
		
		reservationPanel = new JPanel();
		reservationPanel.setLayout(new MigLayout());
		lblDate = new JLabel("Datum");
		setFont(lblDate);
		picker = new JXDatePicker();
		JButton pck_btn = (JButton)picker.getComponent(1);
		ImageIcon imageIcon = new ImageIcon(getClass().getResource("/images/calendar.png"));
		pck_btn.setIcon(imageIcon);
		picker.getMonthView().setFont(new Font("Times New Roman", Font.PLAIN, 28));
		picker.setFont(new Font("Times New Roman", Font.PLAIN,22));
	    picker.setDate(Calendar.getInstance().getTime());
	    picker.setFormats(new SimpleDateFormat("dd.MM.yy"));
	    reservationPanel.add(lblDate);
		reservationPanel.add(picker,"wrap");
		lblTime = new JLabel("Zeit");
		setFont(lblTime);
		cbHour = new JComboBox();
		cbHour.setFont(new Font("Times New Roman", Font.PLAIN,28));
		cbHour.addItem("06:15");cbHour.addItem("06:30");cbHour.addItem("06:45");cbHour.addItem("07:00");
		cbHour.addItem("07:15");cbHour.addItem("07:30");cbHour.addItem("07:45");cbHour.addItem("08:00");
		cbHour.addItem("08:15");cbHour.addItem("08:30");cbHour.addItem("08:45");cbHour.addItem("09:00");
		cbHour.addItem("09:15");cbHour.addItem("09:30");cbHour.addItem("09:45");cbHour.addItem("10:00");
		cbHour.addItem("10:15");cbHour.addItem("10:30");cbHour.addItem("10:45");cbHour.addItem("11:00");
		cbHour.addItem("11:15");cbHour.addItem("11:30");cbHour.addItem("11:45");cbHour.addItem("12:00");
		cbHour.addItem("12:15");cbHour.addItem("12:30");cbHour.addItem("12:45");cbHour.addItem("13:00");
		cbHour.addItem("13:15");cbHour.addItem("13:30");cbHour.addItem("13:45");cbHour.addItem("14:00");
		cbHour.addItem("14:15");cbHour.addItem("14:30");cbHour.addItem("14:45");cbHour.addItem("15:00");
		cbHour.addItem("15:15");cbHour.addItem("15:30");cbHour.addItem("15:45");cbHour.addItem("16:00");
		cbHour.addItem("16:15");cbHour.addItem("16:30");cbHour.addItem("16:45");cbHour.addItem("17:00");
		cbHour.addItem("17:15");cbHour.addItem("17:30");cbHour.addItem("17:45");cbHour.addItem("18:00");
		cbHour.addItem("18:15");cbHour.addItem("18:30");cbHour.addItem("18:45");cbHour.addItem("19:00");
		cbHour.addItem("19:15");cbHour.addItem("19:30");cbHour.addItem("19:45");cbHour.addItem("20:00");
		cbHour.addItem("20:15");cbHour.addItem("20:30");cbHour.addItem("20:45");cbHour.addItem("21:00");
		cbHour.addItem("21:15");cbHour.addItem("21:30");cbHour.addItem("21:45");cbHour.addItem("22:00");
		cbHour.addItem("22:15");cbHour.addItem("22:30");cbHour.addItem("22:45");cbHour.addItem("23:00");
		cbHour.addItem("23:15");cbHour.addItem("23:30");cbHour.addItem("23:45");cbHour.addItem("24:00");
		cbHour.addItem("00:15");cbHour.addItem("24:30");cbHour.addItem("00:45");cbHour.addItem("01:00");
		cbHour.setBackground(Color.WHITE);
		reservationPanel.add(lblTime);
		reservationPanel.add(cbHour,"wrap");
		
		lblPerson = new JLabel("Personnen");
		setFont(lblPerson);
		cbPerson = new JComboBox();
		cbPerson.setFont(new Font("Times New Roman", Font.PLAIN,28));
		cbPerson.setBackground(Color.WHITE);
		for(int i = 1; i<51;i++)
			cbPerson.addItem(i);
		reservationPanel.add(lblPerson);
		reservationPanel.add(cbPerson,"wrap");
	
		lblName = new JLabel("Name");
		setFont(lblName);
		tfName = new JTextField(25);
		setFont(tfName);
		reservationPanel.add(lblName);
		reservationPanel.add(tfName,"wrap");
		
		lblTelefon = new JLabel("Telefon");
		setFont(lblTelefon);
		tfTelfon = new JTextField(25);
		setFont(tfTelfon);
		reservationPanel.add(lblTelefon);
		reservationPanel.add(tfTelfon,"wrap");
		
		lblEmail = new JLabel("Email");
		setFont(lblEmail);
		tfEmail = new JTextField(25);
		
		tfId = new JTextField(25);
		setFont(tfEmail);
		reservationPanel.add(lblEmail);
		reservationPanel.add(tfEmail,"wrap");
		
		lblWorker = new JLabel("Mitarbeiter");
		setFont(lblWorker);
		tfWorker = new JTextField(25);
		setFont(tfWorker);
		reservationPanel.add(lblWorker);
		reservationPanel.add(tfWorker,"wrap");
		
		
		lblMessage = new JLabel("Nachricht");
		setFont(lblMessage);
		tfMessage = new JTextArea(10,25);
		setFont(tfMessage);
		reservationPanel.add(lblMessage);
		reservationPanel.add(tfMessage,"wrap");
		
		btnSave = new PosButton();
		btnSave.setText("Speichern");
		btnSave.setBackground(new Color(102,255,102));
		btnSave.setFont(new Font("Times New Roman", Font.BOLD, 22));
		btnSave.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				List<Reservation> reservationList = ReservationDAO.getInstance().findAll();
				if(reservationList == null)
					reservationList = new ArrayList();
				Reservation reservation;
				if(tfId.getText().length() > 0)
				{
					 reservation = ReservationDAO.getInstance().findById(Integer.parseInt(tfId.getText()));
				}
				else
					reservation = new Reservation();
				
				DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
				reservation.setDate(dateFormat.format(picker.getDate()));
				reservation.setEmail(tfEmail.getText().replace(' ', '@'));
				reservation.setName(tfName.getText().toUpperCase());
				reservation.setWorker(tfWorker.getText().toUpperCase());
				reservation.setTime(cbHour.getSelectedItem().toString());
				reservation.setMessage(tfMessage.getText());
				reservation.setPerson(Integer.parseInt(cbPerson.getSelectedItem().toString()));
				reservation.setTelefon(tfTelfon.getText());
				
				ReservationDAO.getInstance().saveOrUpdate(reservation);
				reservationList.add(reservation);
				
				tfId.setText("");
				tfEmail.setText("");
				tfMessage.setText("");
				tfTelfon.setText("");
				tfMessage.setText("");
				tfName.setText("");
				tfWorker.setText("");
				table.repaint();
				table.validate();
				
				tableModel.setDate(dateFormat.format(picker1.getDate()));
				tableModel.setTempList();
				tableModel.setRows(tableModel.getTempList());
			}
			
		});
		reservationPanel.add(btnSave,"wrap");
		
		btnClear = new PosButton();
		btnClear.setText("CLR");
		btnClear.setBackground(new Color(102,178,255));
		btnClear.setFont(new Font("Times New Roman", Font.BOLD, 22));
		btnClear.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("clear button clicked");
				tfEmail.setText("");
				tfMessage.setText("");
				tfTelfon.setText("");
				tfMessage.setText("");
				tfName.setText("");
				tfWorker.setText("");
			}
		});
		qwertyPanel = new JPanel();
		qwertyPanel.setLayout(new MigLayout());
		
		com.floreantpos.swing.QwertyKeyPad qwertyKeyPad = new com.floreantpos.swing.QwertyKeyPad();
		//qwertyKeyPad.setMaximumSize(new Dimension(450,250));
		qwertyPanel.add(qwertyKeyPad,"growx");
		
		displayPanel = new JPanel();
		displayPanel.setLayout(new MigLayout());
		
		JLabel lblDate1 = new JLabel();
		setFont(lblDate1);
		picker1 = new JXDatePicker();
		picker1.getMonthView().setFont(new Font("Times New Roman", Font.PLAIN, 28));
		picker1.setFont(new Font("Times New Roman", Font.PLAIN,22));
	    picker1.setDate(Calendar.getInstance().getTime());
	    picker1.setFormats(new SimpleDateFormat("dd.MM.yy"));
	    picker1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
				tableModel.setDate(dateFormat.format(picker1.getDate()));
				tableModel.setTempList();
				tableModel.setRows(tableModel.getTempList());
			}
	    });
	   
	    pck_btn = (JButton)picker1.getComponent(1);
	    imageIcon = new ImageIcon(getClass().getResource("/images/calendar.png"));
	    pck_btn.setIcon(imageIcon);
	    displayPanel.add(lblDate1,"cell 0 0,alignx leading");
		table = new ReservationListTable();
		table.setSortable(true);
		table.setModel(tableModel = new ReservationListModel());
		
		PosButton btnRefresh = new PosButton();
		btnRefresh.setFont(new Font("Times New Roman", Font.PLAIN,18));
		btnRefresh.setText("Aktualisieren");
		btnRefresh.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
				tableModel.setDate(dateFormat.format(picker1.getDate()));
				tableModel.setTempList();
				tableModel.setRows(tableModel.getTempList());
				
			}
			
		});
		btnRefresh.setBackground(new Color(102,255,102));
		
		PosButton btnDelete = new PosButton();
		btnDelete.setFont(new Font("Times New Roman", Font.PLAIN,18));
		btnDelete.setText("Loeschen");
		btnDelete.setBackground(new Color(255,153,153));
		btnDelete.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(table.getSelectedRow() == -1)return;
				Reservation reservation = (Reservation)tableModel.getRowData(table.getSelectedRow());
				deleteReservation(reservation);
				
				DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
				tableModel.setDate(dateFormat.format(picker1.getDate()));
				tableModel.setTempList();
				tableModel.setRows(tableModel.getTempList());
			}
			
		});
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
		Date date = new Date();
		tableModel.setDate(dateFormat.format(date));
		tableModel.setTempList();
		tableModel.setRows(tableModel.getTempList());
		table.setRowHeight(35);
		table.setPreferredSize(new Dimension(640, 260));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		table.setDefaultRenderer(Object.class, new PosTableRenderer());
		table.setGridColor(Color.BLACK);
		table.setBackground(Color.WHITE);
		table.getTableHeader().setBackground(new Color(209,222,235));
		table.setSelectionBackground(Color.GREEN);
		table.setSelectionForeground(Color.black);
		table.setOpaque(true);
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		table.setFillsViewportHeight(true);
		table.addMouseListener(new MouseAdapter() {
			 public void mousePressed(MouseEvent me) 
			 {
				 if (me.getClickCount() == 1) 
				 {
					 Point p = me.getPoint();
					 int row = table.rowAtPoint(p);
					 if(row == -1)return;
					 Reservation reservation =  (Reservation) tableModel.getRowData(row);
					 tfId.setText(reservation.getId()+"");
					 tfName.setText(reservation.getName());
					 tfEmail.setText(reservation.getEmail());
					 tfMessage.setText(reservation.getMessage());
					 tfWorker.setText(reservation.getWorker());
					 cbHour.setSelectedItem(reservation.getTime());
					 cbPerson.setSelectedItem(reservation.getPerson());
					 DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
					 try {
						picker.setDate(dateFormat.parse(reservation.getDate()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
			 }
		});
	    table.getColumn(0).setCellRenderer(cellRenderer);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(30);
		columnModel.getColumn(1).setPreferredWidth(80);
		columnModel.getColumn(2).setPreferredWidth(80);
		columnModel.getColumn(3).setPreferredWidth(20);
		columnModel.getColumn(4).setPreferredWidth(100);
		columnModel.getColumn(5).setPreferredWidth(100);
			
		JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
		scrollBar.setPreferredSize(new Dimension(30, 60));
		scrollBar.setBackground(Color.WHITE);
		table.setBackground(Color.WHITE);
		
		scrollBar.setBackground(Color.WHITE);
		setLayout(new BorderLayout());
	   	displayPanel.add(scrollPane);
	   	JPanel buttonPanel = new JPanel();
	   	buttonPanel.setLayout(new MigLayout());
	   	buttonPanel.setBackground(new Color(209,222,235));
	   	buttonPanel.add(picker1,BorderLayout.CENTER);
	   	buttonPanel.add(btnRefresh,"growx");
	   	buttonPanel.add(btnDelete,"wrap");
		displayPanel.add(buttonPanel,"wrap");
	   	

		tfSearch = new JTextField("   Mitarbeiter Suchen...");
		tfSearch.setFont(new Font("Times New Roman", Font.PLAIN, 18));
	   	buttonPanel.add(tfSearch);
	   	tfSearch.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				if(tfSearch.getText().compareTo("   Mitarbeiter Suchen...") == 0)
					return;
				searchAll();
				
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				searchAll();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				searchAll();
			}
	   	});
	   	tfSearch.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				tfSearch.setText("   Mitarbeiter Suchen...");
			}
	   		
	   	});
	   	tfSearch.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(tfSearch.getText().compareTo("   Mitarbeiter Suchen...") == 0)
				{
					tfSearch.setText("");
					loadAllItem();
				}
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
			}
			@Override
			public void mousePressed(MouseEvent arg0) {
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
			
		});
	   	
	   	tfCustomerSearch = new JTextField("   Kunden Suchen...");
	   	tfCustomerSearch.setFont(new Font("Times New Roman", Font.PLAIN, 18));
	   	buttonPanel.add(tfCustomerSearch);
	   	tfCustomerSearch.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				if(tfCustomerSearch.getText().compareTo("   Kunden Suchen...") == 0)
					return;
				searchCustomerAll();
				
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				searchCustomerAll();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				searchCustomerAll();
			}
	   	});
	   	tfCustomerSearch.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				tfCustomerSearch.setText("   Kunden Suchen...");
			}
	   		
	   	});
	   	tfCustomerSearch.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(tfCustomerSearch.getText().compareTo("   Kunden Suchen...") == 0)
				{
					tfCustomerSearch.setText("");
					loadAllItem();
				}
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
			}
			@Override
			public void mousePressed(MouseEvent arg0) {
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
			
		});
	   	
	   	
		reservationPanel.setBackground(new Color(209,222,235));
		displayPanel.setBackground(new Color(209,222,235));
	
		add(displayPanel,BorderLayout.NORTH);
		add(reservationPanel,BorderLayout.WEST);
		qwertyPanel.setBackground(new Color(209,222,235));
		add(qwertyPanel);
			
		setBackground(new Color(209,222,235));
		
		dateFormat = new SimpleDateFormat("dd.MM.yy");
		tableModel.setDate(dateFormat.format(picker1.getDate()));
		tableModel.setTempList();
		tableModel.setRows(tableModel.getTempList());
		
	}
	public void searchAll()
	{
		if(tfSearch.getText().length() == 0)
		{
			loadAllItem();
		}
		else
		{
			searchItem(tfSearch.getText().toUpperCase());				
		}
	}
	public void searchCustomerAll()
	{
		if(tfSearch.getText().length() == 0)
		{
			loadAllItem();
		}
		else
		{
			searchCustomer(tfCustomerSearch.getText().toUpperCase());				
		}
	}
	public void searchCustomer(String searchStr)
	{
		ReservationDAO dao = new ReservationDAO();
		List<Reservation> tempList = new ArrayList();
		List<Reservation> reservationList = dao.findAll();
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
		String date = dateFormat.format(picker1.getDate());
		for(Iterator<Reservation> itr = reservationList.iterator();itr.hasNext();)
		{
			Reservation reservation = itr.next();
			if(reservation.getName().contains(searchStr) && reservation.getDate().compareTo(date) == 0)
			{
				tempList.add(reservation);
			}
		}
		permList = tempList;
		tableModel.setRows(tempList);
		table.repaint();
		table.revalidate();
	}
	public void searchItem(String searchStr)
	{
		ReservationDAO dao = new ReservationDAO();
		List<Reservation> tempList = new ArrayList();
		List<Reservation> reservationList = dao.findAll();
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
		String date = dateFormat.format(picker1.getDate());
		for(Iterator<Reservation> itr = reservationList.iterator();itr.hasNext();)
		{
			Reservation reservation = itr.next();
			if(reservation.getWorker().contains(searchStr) && reservation.getDate().compareTo(date) == 0)
			{
				tempList.add(reservation);
			}
		}
		permList = tempList;
		tableModel.setRows(tempList);
		table.repaint();
		table.revalidate();
	}
	public void loadAllItem()
	{
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
		tableModel.setDate(dateFormat.format(picker1.getDate()));
		tableModel.setTempList();
		tableModel.setRows(tableModel.getTempList());
	}
	public void deleteReservation(Reservation deleteReservation)
	{
		List<Reservation> reservationList = ReservationDAO.getInstance().findAll();
		if(reservationList == null)
			return;
		int index = 0;
		for(Iterator itr= reservationList.iterator();itr.hasNext();)
		{
			Reservation reservation = (Reservation)itr.next();
			if((reservation.getName().compareTo(deleteReservation.getName()) == 0) 
					&& (reservation.getDate().compareTo(deleteReservation.getDate()) == 0)
					&& (reservation.getTime().compareTo(deleteReservation.getTime()) == 0)
					&& (reservation.getTelefon().compareTo(deleteReservation.getTelefon()) == 0))
			{
				ReservationDAO.getInstance().delete(reservation);
				reservationList.remove(index);
				break;
			}
			else
				index++;
		}
	}
	public void setFont(JLabel label)
	{
		label.setFont(new Font("Times New Roman", Font.PLAIN,19));
	}
	public void setFont(JTextField label)
	{
		//label.setFont(new Font("Times New Roman", Font.BOLD,11));
	}
	public void setFont(JTextArea label)
	{
		//label.setFont(new Font("Times New Roman", Font.PLAIN,19));
	}
	private class ReservationListTable extends JXTable {
		
		public ReservationListTable() {
			setColumnControlVisible(true);
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
	private class ReservationListModel extends ListTableModel {
		private String date;
		
		public void setDate(String date)
		{
			this.date = date;
		}
		public String getDate()
		{
			return date;
		}
		public void setTempList()
		{
			
			if(permList != null)
				permList.clear();
			List<Reservation> reservationList = ReservationDAO.getInstance().findAll();
			if(reservationList == null)return;
			List<Reservation> tempList = new ArrayList();
			for(Iterator<Reservation> itr = reservationList.iterator();itr.hasNext();)
			{
				Reservation reservation = itr.next();
				
				if(reservation.getDate().compareTo(getDate()) == 0)
				{
					tempList.add(reservation);
				}
			}
			permList = tempList;
		}
		
		public List<Reservation> getTempList()
		{
			return permList;
		}
		public ReservationListModel() {
			super(new String[] { "Zeit", "Name", "Mitarbeiter","Pers.","Telefon","Nachricht","E-mail"});
		}
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return true;
		}

		public void setValueAt(Object value, int rowIndex, int columnIndex)
		{
			Reservation reservationOld = permList.get(rowIndex);
			String oldName = reservationOld.getName();
			String oldtelefon = reservationOld.getTelefon();
			String oldDate = reservationOld.getDate();
			String oldTime = reservationOld.getTime();
			
			Reservation reservation = permList.get(rowIndex);
			if (columnIndex == 0)
			{
				reservation.setTime(value.toString());
			}
			else if(columnIndex == 1)
				reservation.setName(value.toString().toUpperCase());
			else if(columnIndex == 2)
				reservation.setWorker(tfWorker.getText().toUpperCase());
			else if(columnIndex == 3)
				reservation.setPerson(Integer.parseInt(value.toString()));
			else if(columnIndex == 4)
				reservation.setTelefon(value.toString());
			else if(columnIndex == 5)
				reservation.setMessage(value.toString());
			else if(columnIndex == 6)
				reservation.setEmail(value.toString());
			
			List<Reservation> reservationList = ReservationDAO.getInstance().findAll();
			if(reservationList == null)
				return;
			int index = 0;
			for(Iterator itr= reservationList.iterator();itr.hasNext();)
			{
				Reservation reservationOrg = (Reservation)itr.next();
						
				if((reservationOrg.getName().compareTo(oldName) == 0) 
						&& (reservationOrg.getDate().compareTo(oldDate) == 0)
						&& (reservationOrg.getTime().compareTo(oldTime) == 0)
						&& (reservationOrg.getTelefon().compareTo(oldtelefon) == 0))
				{
					reservationList.remove(index);
					ReservationDAO.getInstance().delete(reservationOrg);
					reservationList.add(reservation);
					ReservationDAO.getInstance().save(reservation);
					break;
					
				}
				index++;
			}
		}
		public Object getValueAt(int rowIndex, int columnIndex) {
			
			Reservation reservation = permList.get(rowIndex);
			switch (columnIndex) {
			case 0:
				return reservation.getTime();
			
			case 1:
					return reservation.getName();

			case 2:
				return reservation.getWorker();
			case 3:
				return reservation.getPerson();
				
			case 4:
				return reservation.getTelefon();
				
			case 5:
				return reservation.getMessage();
				
			case 6:
				return reservation.getEmail();
			}
			return null;
		}
	}
	private JPanel reservationPanel;
	private JPanel displayPanel;
	private JPanel qwertyPanel;
	private JLabel lblName;
	private JTextField tfName;
	private JLabel lblTelefon;
	private JTextField tfTelfon;
	private JLabel lblEmail;
	private JTextField tfEmail;
	private JTextField tfId;
	private JLabel lblWorker;
	private JTextField tfWorker;
	private JLabel lblMessage;
	private JTextArea tfMessage;
	private JLabel lblPerson;
	private JComboBox cbPerson;
	private JLabel lblDate;
	private JButton btnSave;
	private JButton btnClear;
	private JXTable table;
	private JLabel lblTime;
	private JComboBox cbHour;
	private ReservationListModel tableModel;
	JTextField tfSearch;
	JTextField tfCustomerSearch;
	JXDatePicker picker1;
	JXDatePicker picker;
	
}
