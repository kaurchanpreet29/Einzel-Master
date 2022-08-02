package com.floreantpos.ui.dialog;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.floreantpos.model.Ticket; 

public class DeliveryHistoryDialog extends POSDialog {
	
	public DeliveryHistoryDialog(List<Ticket> tickets, String title) throws Exception
	{
		getContentPane().setPreferredSize(new Dimension(800,600));
		if(title.isEmpty())
		setTitle("History");
		else
			setTitle(title);
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		openTicketList = new com.floreantpos.ui.DeliveryTicketListView();
		openTicketList.setTickets(tickets);
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(800,600));
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
		
		southPanel.add(button);
		southPanel.setBackground(new Color(209,222,235));
		panel.setBackground(new Color(209,222,235));
		getContentPane().setBackground(new Color(209,222,235));
		setBackground(new Color(209,222,235));
		contentPane.add(panel); 
		contentPane.add(southPanel,BorderLayout.SOUTH);
		openTicketList.repaint();
		repaint();
	}
	private com.floreantpos.ui.DeliveryTicketListView openTicketList;
	
}

