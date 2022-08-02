package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.calendar.DateUtils;

import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.util.DateUtil;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.util.BusinessDateUtil;

import net.miginfocom.swing.MigLayout;


public class SalesResetDialog extends POSDialog{


  private static final long serialVersionUID = 1L;
  JXDatePicker dpStartDate;
	JXDatePicker dpEndDate;
	
	PosButton resetButton;
	PosButton setTButton;

	
	public SalesResetDialog()
	{
		setTitle("Sales-Reset");
		initComponents();
	}
	
	public void initComponents()
	{
	  JPanel panel = new JPanel();
	  dpStartDate = new JXDatePicker();
    dpStartDate.getMonthView().setFont(
        new Font("Times New Roman", Font.PLAIN, 28));
    dpStartDate.setFont(new Font("Times New Roman", Font.PLAIN, 22));

    dpEndDate = new JXDatePicker();
    dpEndDate.getMonthView().setFont(
        new Font("Times New Roman", Font.PLAIN, 28));
    dpEndDate.setFont(new Font("Times New Roman", Font.PLAIN, 22));

    
    dpStartDate.setDate(DateUtils.startOfDay(new Date()));
    dpEndDate.setDate(DateUtils.endOfDay(new Date()));
    
    resetButton = new PosButton();
    resetButton.setText("RESET");
    resetButton.setBackground(new Color(102, 255, 102));
    resetButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        final Date startDate = BusinessDateUtil.startOfOfficialDay(dpStartDate.getDate());
        final Date endDate = BusinessDateUtil.endOfOfficialDay(dpEndDate.getDate());
        List<Ticket> tickets = TicketDAO.getInstance().findAllTickets(startDate, endDate); 
        if(tickets != null) {
          Session session = TicketDAO.getInstance().createNewSession();
          Transaction tx = session.beginTransaction();
          tickets.stream().forEach(ticket -> {
            ticket.setDrawerResetted(false);
            session.saveOrUpdate(ticket);
          });
          tx.commit();
          session.close();
          POSMessageDialog.showError("Erfolg");
        }
      }
    });

    setTButton = new PosButton();
    setTButton.setText("SET T");
    setTButton.setBackground(new Color(102, 255, 102));
    setTButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        final Date startDate = DateUtil.startOfDay(dpStartDate.getDate());
        final Date endDate = DateUtil.endOfDay(dpEndDate.getDate());
        List<Ticket> tickets = TicketDAO.getInstance().findAllTickets(startDate, endDate); 
        if(tickets != null) {
          Session session = TicketDAO.getInstance().createNewSession();
          Transaction tx = session.beginTransaction();
          tickets.stream().forEach(ticket -> {
            ticket.setDrawerResetted(true);
            session.saveOrUpdate(ticket);
          });
          tx.commit();
          session.close();
          POSMessageDialog.showError("Erfolg");
        }
      }
    });
    
    panel.setLayout(new MigLayout());
    panel.add(dpStartDate);
    panel.add(dpEndDate);
    panel.add(resetButton);
    panel.add(setTButton);
    
		setSize(new Dimension(1000,600));
		setLayout(new BorderLayout());
		add(panel, BorderLayout.CENTER);
		panel.setBackground(new Color(209,222,235));
		setBackground(new Color(209,222,235));
		getContentPane().setBackground(new Color(209,222,235));
	}
}
