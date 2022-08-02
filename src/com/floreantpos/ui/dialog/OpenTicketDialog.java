package com.floreantpos.ui.dialog;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.floreantpos.actions.SettleTicketAction;
import com.floreantpos.main.Application;
import com.floreantpos.model.Ticket; 
import com.floreantpos.model.User;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.util.POSUtil;

public class OpenTicketDialog extends POSDialog {
	
	public OpenTicketDialog(List<Ticket> tickets)
	{
		setSize(new Dimension(800,600));
		setTitle("Offene Bestellungen");
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		openTicketList = new com.floreantpos.ui.TicketListView();
		openTicketList.setTickets(tickets);
		JPanel panel = new JPanel();
		JLabel label = new JLabel();
		label.setText("Bitte schliessen Sie diese Bestellungen!!!");
		label.setFont(new Font("Times New Roman", Font.BOLD, 24));
		label.setForeground(Color.RED);
		panel.setLayout(new MigLayout());
		panel.add(label,"wrap");
		JButton button = new JButton("SCHLIESSEN");
		button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
		    }
		});
		button.setBackground(new Color(255,153,153));
		button.setFont(new Font("Times New Roman", Font.BOLD,16));
		panel.add(openTicketList);
		
		JPanel southPanel = new JPanel();
	
		
		JButton closeOrderButton = new JButton();
		closeOrderButton.setText("Geschlossen");
		closeOrderButton.setFont(new Font("Times New Roman", Font.BOLD,16));
		closeOrderButton.setBackground(new Color(102,255,102));
		closeOrderButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Ticket ticket = openTicketList.getFirstSelectedTicket();
				if(ticket == null)
    				return;
    			int due = (int) POSUtil.getDouble(ticket.getDueAmount());
    			if (due != 0) {
    				POSMessageDialog.showError("Diese Rechnung ist nicht bezahlt");
    				return;
    			}
				int option = JOptionPane.showOptionDialog(Application.getPosWindow(), "Moechten Sie dieser Rechnung# " + ticket.getId() + " schliessen", "Bestaetigen",
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
				repaint();
			}});
		JButton payButton = new JButton();
		payButton.setText("Bezahlen");
		payButton.setFont(new Font("Times New Roman", Font.BOLD,16));
		payButton.setBackground(new Color(102,255,102));
		payButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				 Ticket ticket =  openTicketList.getFirstSelectedTicket();
				 if (ticket != null)
				 {
					 new SettleTicketAction(ticket.getId()).execute();
					 updateTicketList();					 
				 }
				
			}
			
		});
		southPanel.add(closeOrderButton);
		southPanel.add(payButton);
		
		southPanel.add(button);
		southPanel.setBackground(new Color(209,222,235));
		
		panel.setPreferredSize(new Dimension(780,520));
		panel.setBackground(new Color(209,222,235));
		setBackground(new Color(209,222,235));
		contentPane.add(panel,BorderLayout.NORTH);
		contentPane.add(southPanel,BorderLayout.SOUTH);
		openTicketList.repaint();
		repaint();
	}
	
	public void updateTicketList()
	{
		List<Ticket> tickets = TicketDAO.getInstance().findAllOpenTickets();
		openTicketList.setTickets(tickets);
		openTicketList.repaint();
		openTicketList = new com.floreantpos.ui.TicketListView();
		repaint();
		
	}
	private com.floreantpos.ui.TicketListView openTicketList;
	
}

