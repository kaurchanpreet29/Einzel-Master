package com.floreantpos.ui.views.order;

import java.io.InputStream;
import java.util.HashMap;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketType;
import com.floreantpos.report.JReportPrintService;
import com.floreantpos.report.TicketPrintProperties;
import com.floreantpos.services.QrcodeBarcodeService;
import com.floreantpos.ui.views.TicketDetailView.PrintType;

import com.khana.restclient.RestPrintType;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.util.JRProperties;

public class PrintTicket implements Runnable{

	Ticket ticket;
	boolean official;
	PrintType type;
	boolean close;
	boolean printCopy;
	public PrintTicket(Ticket ticket, boolean official, PrintType type, boolean close, boolean print)
	{
		this.ticket = ticket;
		this.official = official;
		this.type = type;
		this.close = close;
		this.printCopy = print;
		Thread t = new Thread(this);
		t.start();
	}
	@Override
	public void run() {
		if(!close) {
			
			JReportPrintService.printTicket(ticket, true);
			return;
		} else {
			checkAndPrint();
		}
	}

	private void checkAndPrint() {
		
		printTicket();
		try {
			Thread.sleep(1*1000);
		}
		catch(Exception e){e.printStackTrace();}

	}

	public void printTicket()
	{ 
		try{
			JRProperties.setProperty("net.sf.jasperreports.xpath.executer.factory",
					"net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");

			TicketPrintProperties printProperties = new TicketPrintProperties("RECHNUNG", true, true, true,false);
			HashMap map;
			
			if(type == PrintType.A4)
				map = JReportPrintService.populateTicketPropertiesA4(ticket, printProperties, null);
			else	
				map = JReportPrintService.populateTicketProperties(ticket, printProperties, null,type,true, 0.00);
			
			
			
			JasperPrint jasperPrint;
			if(official)
				jasperPrint = JReportPrintService.createPrint(ticket, map, null,true);
			else if(type == PrintType.A4)
				jasperPrint = JReportPrintService.createA4Print(ticket, map, null);
			else
				jasperPrint = JReportPrintService.createPrint(ticket, map, null,false);
			if(type == PrintType.A4)
				jasperPrint.setProperty("printerName", Application.getPrinters().getA4Printer());
			else {
				String deviceName = Application.getPrinters().getReceiptPrinter();
				if(!TerminalConfig.isKitchenPrint()
						&& !ticket.getTicketType().equals(TicketType.DINE_IN.name())) {
					//deviceName = Application.getPrinters().getKitchenPrinter();
					deviceName = Application.getPrinters().getReceiptPrinter();
				}
				jasperPrint.setProperty("printerName", deviceName);
				System.out.println("Printing receipt print to... "+ deviceName);
				
			}
			
			
			if (Application.getPrinters().getReceiptPrinterEnable()) {
				String deviceName = Application.getPrinters().getReceiptPrinter();
				jasperPrint.setProperty("printerName", deviceName);
				System.out.println("Printing duplicate_Receipt to " + Application.getPrinters().getReceiptPrinter());
				if(type == PrintType.A4)
					JReportPrintService.printQuitelyA4(jasperPrint);
				else
					JReportPrintService.printQuitely(jasperPrint);
			}

		}
		catch(Exception e){e.printStackTrace();}
	}

}
