package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.POSConstants;
import com.floreantpos.PosException;
import com.floreantpos.main.Application;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.views.OrderInfoDialog;
import com.floreantpos.ui.views.OrderInfoView;
import com.floreantpos.ui.views.SwitchboardView;
import com.floreantpos.util.BusinessDateUtil;

public class LastTicketListDialog extends POSDialog {
  private static final long serialVersionUID = 1L;

  private com.floreantpos.ui.TicketListView openTicketList;

  public LastTicketListDialog() {
    init();
  }

  public void init() {
    setBackground(new Color(209, 222, 235));
    setTitle("Bestellungen");
    setLayout(new BorderLayout());
    
    openTicketList = new com.floreantpos.ui.TicketListView(SwitchboardView.getInstance());
    getContentPane().add(openTicketList, BorderLayout.CENTER);
    Date startDate = BusinessDateUtil.startOfOfficialDay(new Date());
    Date endDate = BusinessDateUtil.endOfOfficialDay(new Date());
    List<Ticket> tickets = TicketDAO.getInstance().findAllClosedTicketsDate(startDate, endDate, null);
    Collections.reverse(tickets);
    tickets = tickets.subList(0, Math.min(tickets.size(), 5));
    openTicketList.setTickets(tickets);    
    
    JPanel panel = new JPanel();
    panel.setBackground(new Color(35,35,36));
    panel.setLayout(new BorderLayout());
    PosButton printButton = new PosButton("DRUCK");
    printButton.setBackground(new Color(108, 101, 0));
    printButton.setForeground(Color.WHITE);
    printButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent arg0) {
        showOldTicket();
      }
    });
    
    PosButton cancelButton = new PosButton("ABBRECHEN");
    cancelButton.setBackground(new Color(125, 6, 42));
    cancelButton.setForeground(Color.WHITE);
    cancelButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent arg0) {
        dispose();
      }
    });
    
    panel.add(printButton, BorderLayout.CENTER);
    panel.add(cancelButton, BorderLayout.EAST);
    getContentPane().add(panel, BorderLayout.SOUTH);
  }
  
  public void showOldTicket() {
    try {
      Ticket ticket = null;
      int ticketId = 0;
      /*List<Ticket> selectedTickets = openTicketList.getSelectedTickets();
      int ticketId = 0;
      if (selectedTickets.size() > 0) {
        ticket = selectedTickets.get(0);
        ticketId = ticket.getId();
      } else {
        ticketId = NumberSelectionDialog2
            .takeIntInput("Geben Oder Scannen Sie die Bestellungsnummer ein");
      }*/
      ticket = openTicketList.getSelectedTicket();
      ticketId = ticket.getId();
      ticket = TicketDAO.getInstance().loadFullTicket(ticketId);
      if (ticket == null) {
        throw new PosException(POSConstants.NO_TICKET_WITH_ID + " " + ticketId
            + " " + POSConstants.FOUND);
      }
      if (ticket.isVoided()) {
    	  if(StringUtils.isNotEmpty(POSConstants.Geloechte_Rechnungen))
				throw new PosException(POSConstants.Geloechte_Rechnungen);
			else
                throw new PosException("Geloechte Rechnungen koennten nicht geoeffnet");
      }

      OrderInfoView view = new OrderInfoView(Arrays.asList(ticket));
      OrderInfoDialog dialog = new OrderInfoDialog(view, false);
      dialog.setSize(400, 600);
      dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
      dialog.setLocationRelativeTo(Application.getPosWindow());
      dialog.setVisible(true);
    } catch (PosException e) {
      POSMessageDialog.showError(this, e.getLocalizedMessage());
    } catch (Exception e) {
      POSMessageDialog.showError(this, POSConstants.ERROR_MESSAGE, e);
    }
  }

}
