package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.floreantpos.POSConstants;
import com.floreantpos.PosException;
import com.floreantpos.main.Application;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketType;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.views.OrderInfoDialog;
import com.floreantpos.ui.views.OrderInfoView;

import net.miginfocom.swing.MigLayout;

public class BarCodeDialog extends POSDialog implements FocusListener {
	String driverName;
	public BarCodeDialog() {
		setTitle("BarCode");
		setBackground(new Color(209,222,235));
		driverName= "";
		createUI();
	}

	private void createUI() {
		setLayout(new BorderLayout());
		this.setFocusable(true);
		this.addFocusListener(this);
		panel = new JPanel();
		panel.setLayout(new MigLayout());
		cbFahrer = new JCheckBox();
		cbFahrer.setText("Fahrer");
		panel.setBackground(new Color(209,222,235));
		cbFahrer.setBackground(new Color(209,222,235));
		cbFahrer.setSelected(true);
		cbFahrer.setFocusable(false);
		//panel.add(cbFahrer);
		
		cbBestellung = new JCheckBox();
		cbBestellung.setText("Bestellung");
		cbBestellung.setFocusable(false);
		cbBestellung.setBackground(new Color(209,222,235));
		//panel.add(cbBestellung,"wrap");
		
		tfBarCode = new JTextField(20);
		panel.add(tfBarCode);
		tfBarCode.setFocusable(true);
		tfBarCode.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if((driverName.length() > 1) && (tfBarCode.getText().contains("X")))
				{
					lblStatus.setText(driverName + " im Fokus");
					String data = tfBarCode.getText();
					tfBarCode.setText("");
					int startIndex = data.indexOf("S");
					int endIndex = data.indexOf("X");
					
					int ticketId = Integer.parseInt(data.substring(startIndex+1, endIndex));
					Ticket ticket = TicketDAO.getInstance().loadFullTicket(ticketId);

					if (ticket == null) {
						POSMessageDialog.showError("Rechnung: "+ ticketId + " ist nicht gefunden");
						return;
					}
					if(ticket.isClosed())
					{
						POSMessageDialog.showError("Rechnung: "+ ticketId + " ist geschlossen");
						return;
					}
					if(ticket.getTicketType().compareTo(TicketType.HOME_DELIVERY.name()) != 0)
					{
						POSMessageDialog.showError("Rechnung " + ticketId + " ist nicht Lieferung");
						return;
					}
					else if(ticket.getDeliveryAddress() == null)
					{
						POSMessageDialog.showError("Rechnung " + ticketId + " ist kunden Abholung");
						return;
					}
					User user = null;
					List<User> userList = UserDAO.getInstance().findDrivers();
					for(Iterator itr = userList.iterator();itr.hasNext();)
					{
						User user1 = (User) itr.next();
						if(user1.getFirstName().compareTo(driverName) == 0)
						{
							user =user1;
							break;
						}
					}
						
					if(user == null){
						POSMessageDialog.showError("Benutzer nicht Verfuegbar");
						return;
					}
					
					ticket.setAssignedDriver(user);
				    
				    Session session = TicketDAO.getInstance().createNewSession();
				    Transaction transaction = null;
				    try
				    {
				      transaction = session.beginTransaction();
				      session.saveOrUpdate(ticket);
				      
				      transaction.commit();
				      lblStatus.setText(driverName +  " belegt fuer Rechnung: " + ticketId);
				    }
				    catch (Exception ex)
				    {
				      ex.printStackTrace();
				      if (transaction != null) {
				        transaction.rollback();
				      }
				    }
				    finally
				    {
				      session.close();
				    }
				}
				else if(tfBarCode.getText().contains("X"))
				{
					String data = tfBarCode.getText();
					tfBarCode.setText("");
					int startIndex = data.indexOf("S");
					int endIndex = data.indexOf("X");
					
					int ticketId = Integer.parseInt(data.substring(startIndex+1, endIndex));
					Ticket ticket = TicketDAO.getInstance().loadFullTicket(ticketId);

					if (ticket == null) {
						POSMessageDialog.showError("Rechnung: "+ ticketId + " ist nicht gefunden");
						return;
					}

					if(ticket.isVoided()) {
						if(StringUtils.isNotEmpty(POSConstants.Geloechte_Rechnungen))
							throw new PosException(POSConstants.Geloechte_Rechnungen);
						else
						    throw new PosException("Geloechte Rechnungen koennten nicht geoeffnet");
					}

					List<Ticket> ticketList = new ArrayList();
					ticketList.add(ticket);
					OrderInfoView view = null;
					try {
						view = new OrderInfoView(ticketList);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					OrderInfoDialog dialog = new OrderInfoDialog(view, false);
					dialog.setSize(400, 600);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setLocationRelativeTo(Application.getPosWindow());
					dialog.setVisible(true);
				}
				else if(tfBarCode.getText().contains("F"))
				{
					lblStatus.setText("");
					String data = tfBarCode.getText();
					tfBarCode.setText("");
					System.out.println("Data:"+data);
					int startIndex = data.indexOf("S");
					int endIndex = data.indexOf("F");
					
					driverName = data.substring(startIndex+1, endIndex);
					User user = null;
					List<User> userList = UserDAO.getInstance().findDrivers();
					for(Iterator itr = userList.iterator();itr.hasNext();)
					{
						User user1 = (User) itr.next();
						if(user1.getFirstName().compareTo(driverName) == 0)
						{
							user =user1;
							break;
						}
					}
						
					if(user == null){
						tfBarCode.setText("");
						driverName="";
						POSMessageDialog.showError("Benutzer nicht Verfuegbar");
						return;
					}
					
					lblStatus.setText(driverName +  " im Fokus");
					tfBarCode.setText("");
					return;
				}
				
			}
			
		});
		finishButton = new JButton();
		finishButton.setText("OK");
		finishButton.setFocusable(false);
		finishButton.setBackground(new Color(102,255,102));
		finishButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				driverName = "";
				lblStatus.setText("");
			}
			
		});
		panel.add(finishButton);
		finishButton.setPreferredSize(new Dimension(30,30));
		cancelButton = new JButton();
		cancelButton.setText("ABRECHEN");
		cancelButton.setFocusable(false);
		cancelButton.setBackground(new Color(255,153,153));
		cancelButton.setPreferredSize(new Dimension(30,30));
		cancelButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				
			}
			
		});
		panel.add(cancelButton);
		lblStatus = new JLabel();
		lblStatus.setFont(new Font("Times New Roman", Font.BOLD, 26));
		panel.add(lblStatus);
		add(panel,BorderLayout.CENTER);
	}
	
	JPanel panel;
	JCheckBox cbFahrer;
	JCheckBox cbBestellung;
	JTextField tfBarCode;
	JButton finishButton;
	JButton cancelButton;
	JLabel lblStatus;
	@Override
	public void focusGained(FocusEvent arg0) {
		tfBarCode.requestFocus();
		
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
