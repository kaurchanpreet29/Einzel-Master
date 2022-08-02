package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.floreantpos.main.Application;
import com.floreantpos.model.Customer;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketType;
import com.floreantpos.model.dao.CustomerDAO;
import com.floreantpos.model.dao.ShopTableDAO;
import com.floreantpos.swing.ImageIcon;
import com.floreantpos.ui.views.order.OrderView;
import com.floreantpos.ui.views.order.RootView;
import net.miginfocom.swing.MigLayout;

public class CallerDialog extends JDialog implements Runnable,WindowListener{

	public CallerDialog(String phone,String name,String address1,String address2) {
		
		initComponents();
		this.addWindowListener(this);
		t = new Thread(this);
		this.phone = phone;
		
		if(name.length() == 0)
			this.name = "Neu Kunden";
		else
			this.name = name;
		
		this.address1 = address1;
		this.address2 = address2;
		t.start();
		
	}
	public void run()
	{
		
		int i =1;
		cancelled = false;
		while (i < 30)
		{
		
			try {
				if(i%4 == 0)
				{
					phoneLabel.setFont(new Font("Times New Roman", Font.BOLD, 22));
					phoneLabel.setForeground(new Color(204,102,0));
		
				}
				else
				{
					phoneLabel.setFont(new Font("Times New Roman", Font.BOLD, 28));
					phoneLabel.setForeground(new Color(0,153,76));
					
				}
				graph();
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
		}
		if(cancelled != true)
		{
			calls++;
		}
		dispose();
	}

	public void initComponents()
	{
		setBackground(new Color(209,222,235));
		leftPanel = new JPanel();
		leftPanel.setBackground(new Color(209,222,235));
		centerPanel = new JPanel();
		centerPanel.setBackground(new Color(209,222,235));
		setPreferredSize(new Dimension(450,200));
		setLayout(new BorderLayout());
		leftPanel.setLayout(new MigLayout());
		centerPanel.setLayout(new MigLayout());
		picLabel = new JLabel();
		ImageIcon imageIcon = new ImageIcon(getClass().getResource("/images/call.png"));
		picLabel.setIcon(imageIcon);
		picLabel.setPreferredSize(new Dimension(100,100));
		leftPanel.add(picLabel);
		
		phoneLabel = new JLabel();
		phoneLabel.setFont(new Font("Times New Roman", Font.BOLD, 28));
		phoneLabel.setPreferredSize(new Dimension(200,100));
		
		centerPanel.add(phoneLabel,"wrap");
		
		nameLabel = new JLabel();
		nameLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
		nameLabel.setPreferredSize(new Dimension(200,100));
		centerPanel.add(nameLabel,"wrap");
	
		addressLabel1 = new JLabel();
		addressLabel1.setFont(new Font("Times New Roman", Font.BOLD, 20));
		addressLabel1.setPreferredSize(new Dimension(200,100));
		centerPanel.add(addressLabel1,"wrap");
		
		addressLabel2 = new JLabel();
		addressLabel2.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		addressLabel2.setPreferredSize(new Dimension(200,100));
		centerPanel.add(addressLabel2,"wrap");
		
		
		JLabel lblEmpty = new JLabel("          ");
		lblEmpty.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblEmpty.setPreferredSize(new Dimension(200,100));
		
		centerPanel.add(lblEmpty,"wrap");
		
		
		add(leftPanel,BorderLayout.WEST);
		
		JPanel bottomPanel = new JPanel();
		JButton okButton = new JButton("BESTELLEN");
		okButton.setFont(new Font("Times New Roman", Font.BOLD, 22));
		okButton.setBackground(new Color(102,255,102));
		okButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				openTicket();
				dispose();
			}
			
		});
		
		JButton cancelButton = new JButton("ABBRECHEN");
		cancelButton.setFont(new Font("Times New Roman", Font.BOLD, 22));
		cancelButton.setBackground(new Color(255,153,153));
		cancelButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
			
		});
		bottomPanel.setBackground(new Color(209,222,235));
		bottomPanel.add(Box.createVerticalStrut(20));
		
		if(Application.getCurrentUser() != null)
			bottomPanel.add(okButton);
		bottomPanel.add(cancelButton);
		centerPanel.add(Box.createVerticalStrut(100));
		
		add(centerPanel,BorderLayout.CENTER);
		add(bottomPanel,BorderLayout.SOUTH);
	}
	
	public void openTicket()
	{

		List<ShopTable> tables = new ArrayList();
		
	    ShopTable table = ShopTableDAO.getInstance().getByNumber("99");
		if(table == null)
		{
			table = new ShopTable();
			table.setNumber("99");
		    tables.add(table);
		}
		else
		 tables.add(table);
				
		Application application = Application.getInstance();
		
		Ticket ticket = new Ticket();
		
		if (tables.size() == 1)
		{
			for (ShopTable shopTable : tables) {
				if (shopTable.getNumber().compareTo("99") == 0)
					ticket.setType(TicketType.HOME_DELIVERY);
				else
				{
					shopTable.setOccupied(true);
					ticket.setType(TicketType.DINE_IN);
				}
				ticket.addTotables(shopTable);
				break;
			}
		}
		else
			ticket.setType(TicketType.DINE_IN);
		
		ticket.setPriceIncludesTax(application.isPriceIncludesTax());
		
		ticket.setNumberOfGuests(2);
		int found = 0;
		List<Customer> customerList = CustomerDAO.getInstance().findByPhoneName(phone,name);
		for(Iterator<Customer> itr = customerList.iterator();itr.hasNext();)
		{
			Customer customer = itr.next();
			ticket.setCustomer(customer);
//			ticket.setKunden(customer);
			found = 1;
			break;
		}
		if(found == 1)
			ticket.setDeliveryAddress(address1);
		ticket.setTerminal(application.getTerminal());
		ticket.setOwner(Application.getCurrentUser());
		ticket.setShift(application.getCurrentShift());
		
		if (tables.size() != 1)
		{
			for (ShopTable shopTable : tables) {
			shopTable.setOccupied(true);
			ticket.addTotables(shopTable);
			}
		}

		Calendar currentTime = Calendar.getInstance();
		ticket.setCreateDate(currentTime.getTime());
		ticket.setCreationHour(currentTime.get(Calendar.HOUR_OF_DAY));
		OrderView.getInstance().setCurrentTicket(ticket);
		RootView.getInstance().showView(OrderView.VIEW_NAME);
	
	}
	public void graph()
	{
		
		if(phone.substring(0, 1).compareTo("6") == 0)
		{
			phoneLabel.setText("+49 " + "("+phone.substring(0,2) + ") " +  (phone.substring(2,phone.length())));
		}
		else if (phone.substring(0, 1).compareTo("1") == 0)
		{
			phoneLabel.setText("+49 "+ "(" + phone.substring(0,3) + ") " +  (phone.substring(3,phone.length())));
		}
		else
			phoneLabel.setText("+49 "+ " " + phone);
		addressLabel1.setText(address1);
		addressLabel2.setText(address2);
		nameLabel.setText(name);
		this.repaint();
	}

	Thread t;
	
	private String address1;
	private String address2;
	JPanel leftPanel;
	JPanel centerPanel;
	private String phone;
	private String name;
	private JLabel picLabel;
	private JLabel phoneLabel;
	private JLabel addressLabel1;
	private JLabel addressLabel2;
	private JLabel nameLabel;
	private boolean cancelled;
	
	public static int calls = 0;
	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
	}

	public boolean isCancelled()
	{
		return cancelled;
	}
	@Override
	public void windowClosing(WindowEvent arg0) {
		cancelled = true;
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		System.out.println("Window Opened");
		
	}
}
