package com.floreantpos.ui.views.order;

import java.util.HashMap;

import net.sf.jasperreports.engine.JasperPrint;

import com.floreantpos.POSConstants;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.Ticket;
import com.floreantpos.report.JReportPrintService;
import com.floreantpos.report.TicketPrintProperties;
import com.floreantpos.ui.views.TicketDetailView.PrintType;

public class PrintThread implements Runnable {

  private Ticket ticket;

  private boolean official;
  private boolean isA4;

  private PrintType type;

  private double balance;
  
  public PrintThread(Ticket ticket, boolean official, PrintType type, double balance, boolean isA4) {
    this.ticket = ticket;
    this.official = official;
    this.type = type;
    this.isA4 = isA4;
    this.balance = balance;
    Thread t = new Thread(this);
    t.start();
  }

  @Override
  public void run() {
    int count = 1;

    try {
      count = Integer.parseInt(TerminalConfig.getBonNr());
    } catch (Exception e) {
    }

    try {
      for (int index = 0; index < count; index++) {
        TicketPrintProperties printProperties = new TicketPrintProperties(
            "*** RECHNUNG ***", true, true, true, false);
       
        if(!isA4) {
        	HashMap map = JReportPrintService.populateTicketProperties(ticket,
                    printProperties, null, type, true, balance);
                JasperPrint jasperPrint;
                if (official)
                  jasperPrint = JReportPrintService
                      .createPrint(ticket, map, null, true);
                else
                  jasperPrint = JReportPrintService.createPrint(ticket, map, null,
                      false);
    			jasperPrint.setName(POSConstants.RECEIPT_NR+"_" + (ticket.getTicketid()!=null?ticket.getTicketid():0));
                jasperPrint.setProperty("printerName", Application.getPrinters()
                    .getReceiptPrinter());
                JReportPrintService.printQuitely(jasperPrint);
                
                if(Application.getInstance().getRestaurant().isCopyRechnug())
                	JReportPrintService.printTicket(ticket, true);
               
        } else {
        	HashMap map = JReportPrintService.populateTicketPropertiesA4(ticket, printProperties, null);
                JasperPrint jasperPrint= JReportPrintService.createA4Print(ticket, map, null);              
    			jasperPrint.setName(POSConstants.RECEIPT_NR+"_" + (ticket.getTicketid()!=null?ticket.getTicketid():0));
                jasperPrint.setProperty("printerName", Application.getPrinters()
                    .getA4Printer());
                JReportPrintService.printQuitelyA4(jasperPrint);
        }
        
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
