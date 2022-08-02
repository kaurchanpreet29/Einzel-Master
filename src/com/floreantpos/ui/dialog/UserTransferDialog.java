package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.main.Application;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.util.NumberUtil;

public class UserTransferDialog extends POSDialog {

	private static final long serialVersionUID = 1L;

	private User selectedUser;

	public UserTransferDialog() {
		initComponents();
	}

	public void initComponents() {
		List<User> users = UserDAO.getInstance().findAll();
		for(int i=0;i<users.size();i++) {
			if(users.get(i).getFirstName().equals("Master")||users.get(i).getFirstName().equals("master")){
				users.remove(i);
			}

		}
		JPanel userPanel = new JPanel();
		userPanel.setBackground(new Color(35,35,36));
		getContentPane().setBackground(new Color(35,35,36));
		userPanel.setLayout(new MigLayout());

		int index = 0;
		for(User user: users) {
			PosButton button = new PosButton();
			button.setBackground(new Color(2, 64, 2));
			button.setFont(new Font("Arial", Font.BOLD, 22));
			button.setPreferredSize(new Dimension(200,150));
			button.setLayout(new BorderLayout());  
			button.setText(user.getAutoId()+":"+user.getFirstName());
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String[] split = button.getText().split(":");
					selectedUser = UserDAO.getInstance().get(Integer.parseInt(split[0]));
					if(selectedUser.getAutoId().equals(Application.getCurrentUser().getAutoId())) {
						setCanceled(true);
						dispose();
					}else {
						setCanceled(false);
						dispose();
					}						
					
				}
			});

			if(index % 3 == 0 && index > 0) {
				userPanel.add(button, "growx, wrap");
				if(index==3)
					button.setBackground(Color.GRAY);
				else if(index==6)
					button.setBackground(Color.ORANGE);
			} else {
				if(index==1)
					button.setBackground(Color.BLUE);
				else if(index==2)
					button.setBackground(Color.YELLOW);
				else if(index==4)
					button.setBackground(Color.GREEN);
				else if(index==5)
					button.setBackground(Color.MAGENTA);
				else if(index==7)
					button.setBackground(Color.CYAN);
				userPanel.add(button, "growx");
			}
			
			setTotalValue(user, button);
			String name = user.getFirstName();    
			JLabel lableName = new JLabel(name);
			lableName.setFont(button.getFont());
			button.setForeground(button.getBackground());      
			lableName.setOpaque(true);
			lableName.setHorizontalAlignment(JLabel.CENTER);
			lableName.setBackground(button.getBackground()); 
			lableName.setForeground(Color.WHITE);
			button.add(lableName, BorderLayout.CENTER);
			++index;
		}
		JPanel bottomPanel = new JPanel();
		bottomPanel.setBackground(new Color(35,35,36));
		bottomPanel.setLayout(new GridLayout());
		PosButton cancelButton = new PosButton();
		cancelButton.setBackground(new Color(125,6,42));
		cancelButton.setForeground(Color.WHITE);
		cancelButton.setText("Abbrechen");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);        
				dispose();
			}
		});

		bottomPanel.add(cancelButton);    
		getContentPane().setLayout(new BorderLayout(0,0));
		getContentPane().add(userPanel, BorderLayout.CENTER);
		getContentPane().add(bottomPanel, BorderLayout.NORTH);
		getRootPane().setBorder(BorderFactory.createLineBorder(Color.WHITE));
	}


	public void setTotalValue(User user, PosButton button) {
		List<Ticket> ticketList = TicketDAO.getInstance().findOpenTicketsForUser(user);
		double total = 0.0;

		if(ticketList!=null&&ticketList.size()>0) {
			for(Ticket ticket:ticketList) {
				total += ticket.getTotalAmount();
			}
		}
		if(total>0) {
			JLabel lablePrice = new JLabel(NumberUtil.formatNumber(total)+"€");
			lablePrice.setFont(button.getFont());
			lablePrice.setHorizontalAlignment(JLabel.CENTER);
			lablePrice.setBackground(button.getBackground());
			lablePrice.setForeground(Color.WHITE);
			button.add(lablePrice, BorderLayout.SOUTH);
		}

	}

	public User getSelectedUser() {
		return selectedUser;
	}
}
