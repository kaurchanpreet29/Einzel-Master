package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.views.SwitchboardView;

public class OpenTicketListDialog extends POSDialog{
	private com.floreantpos.ui.TicketListView openTicketList;
	public OpenTicketListDialog()
	{
		init();
	}
	public void init()
	{
		setBackground(new Color(209,222,235));
		setTitle("Offene Bestellungen_");
		setLayout(new BorderLayout());
		openTicketList = new com.floreantpos.ui.TicketListView(SwitchboardView.getInstance(),this);
		getContentPane().add(openTicketList,BorderLayout.CENTER);
		openTicketList.setTickets(TicketDAO.getInstance().findOpenTickets());
		PosButton cancelButton = new PosButton("ABBRECHEN");
		if(StringUtils.isNotEmpty(POSConstants.ABBRECHEN))
			cancelButton.setText(POSConstants.ABBRECHEN);
		
		cancelButton.setBackground(new Color(255,153,153));
		cancelButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		getContentPane().add(cancelButton,BorderLayout.SOUTH);
	}
}
