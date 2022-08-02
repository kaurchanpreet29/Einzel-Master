package com.floreantpos.report;

import java.text.DecimalFormat;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.util.NumberUtil;

public class SalesReportModel extends AbstractTableModel {
	private static DecimalFormat formatter = new DecimalFormat("#,##0.00");
	private String currencySymbol;

	private String[] columnNames = { "tt", "awt", "awot", "ttd", "tts", "ttz",
			"awt19", "awt7", "cancelledtrans", "voidArticles", "cashPayment",
			"cashTax", "cardPayment", "cardTax", "cancelledItems", "totalInvoices",
			"totalSoldItems", "food", "foodTax", "beverage", "beverageTax",
			"voidTickets", "voidAmount", "totalCash", "discountAmount", "awt0",
			"tt0", "insert", "insertAmt", "onlinePayment", "onlineTax",
			"cashPaymentCount", "cardPaymentCount", "totalCash", "giveAwayText1", "giveAway1",
			"giveAwayText2", "giveAway2", "giveAwayText3", "giveAway3", "netton", "nettos", "nettoz","anzahlRetour", "retourGesamt", "retourTax", "gesamtSumme", "gesamtMwst19", "gesamtMwst7"
//49
			,"einnahme_text",
			"umasat_gesamt_text",
			"gesamt_19_text",
			"gesamt_7_text",
			"gesamt_0_text",
			"gesamt_netto_text",
			"netto_19_text",
			"netto_7_text",
			"netto_0_text",
			"mwst_19_text",
			"mwst_7_text",
			"mwst_0_text",
			"anzahl_retour_text",
			"rtourn_gesamt_text",
			"retour_mwst_text",
			"anzahl_storno_text",
			"anzahl_storno_gesamt_text",
			"storno_mwst_text",
			"anzahl_rechnugen_text",
			"anzahl_sold_items_text",
			"cashpayment_text",
			"mwst_gesamt_text",
			"mwst_gesamt_text1",
			"anzahl_text",
			"anzahl_text1",
			"anzahl_text2",
			//75
			"cardpayment_text",			
			"kunden_rabatt_text",
			"cash_in_cashdrawer_text",
			"gesamt_summe_text",
			"warengroup_abs_text",
			"warengroup_text",
			"gesamt_text",
			"anzahl_text_rechnug",
			"mwst_gesamt_text_rechnug",
			"rechnugPayment_text",
			"rechnugPayment_tax",
			"rechnugPament_anzahl",
			"rechnugPaymentAmount",
			"anzahl_text_online",
			"mwst_gesamt_text_online",
			"onlinePayment_text",
			"onlinePayment_tax",
			"onlinePament_anzahl",
			//93
			"onlinePaymentAmount","cash19","cash7","card19", "card7","cash19Text","cash7Text",
			//100
			"card19Text","card7Text","btrinkgeld", "ktrinkgeld", "soldGutschein", 
			"ticketGutscheinAmount","gutschein_einsgelost_text","verkaufte_gutschein_text","bar_tip_text","other_tip_text",
			"rabatt_19text", "rabatt_7text","rabatt_19", "rabatt_7",
	};

	private List<ReportItem> items;
	private double grandTotal;

	public SalesReportModel() {
		super();
		currencySymbol = Application.getCurrencySymbol();		
	}

	@Override
	public int getRowCount() {
		if(items == null) {
			return 0;
		}

		return items.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ReportItem item = items.get(rowIndex);

		switch(columnIndex) {
		case 0:
			return NumberUtil.formatNumber(item.gett()) + " " + currencySymbol;

		case 1:
			return NumberUtil.formatNumber(item.getawt()) + " " + currencySymbol;

		case 2:
			return NumberUtil.formatNumber(item.getAwot()) + " " + currencySymbol;

		case 3:
			return NumberUtil.formatNumber(item.getttd()) + " " + currencySymbol;

		case 4:
			if(!TerminalConfig.isSupermarket()) {
				return null;
			}
			return NumberUtil.formatNumber(item.gettts()) + " " +currencySymbol;

		case 5:
			if(!TerminalConfig.isSupermarket()) {
				return null;
			}
			return NumberUtil.formatNumber(item.getttz()) + " " +currencySymbol;

		case 6:
			return NumberUtil.formatNumber(item.getawt19()) + " " + currencySymbol;
		case 7:
			if(!TerminalConfig.isSupermarket()) {
				return null;
			}
			return NumberUtil.formatNumber(item.getawt7()) + " " + currencySymbol;
		case 8:
			return NumberUtil.formatNumber(item.getCancelledTrans()) + " " + currencySymbol;
		case 9:
			return item.getVoidArticles()+"";
		case 10:			
			return NumberUtil.formatNumber(item.getCashPayment()) + " " + currencySymbol;
		case 11:
			return NumberUtil.formatNumber(item.getCashTax()) + " " + currencySymbol;
		case 12:
			return NumberUtil.formatNumber(item.getCardPayment()) + " " + currencySymbol;
		case 13:
			return NumberUtil.formatNumber(item.getCardTax()) + " " + currencySymbol;
		case 14:	
			return item.getCancelledItems() + " "; 

		case 15:
			return item.getTotalInvoices() + " ";

		case 16:
			return item.getTotalSoldItems() + " ";
		case 17:
			return NumberUtil.formatNumber(item.getFood()) + " " + currencySymbol;
		case 18:
			return NumberUtil.formatNumber(item.getFoodTax()) + " " + currencySymbol;
		case 19:
			return NumberUtil.formatNumber(item.getBeverage()) + " " + currencySymbol;
		case 20:
			return NumberUtil.formatNumber(item.getBeverageCount()) + " " + currencySymbol;
		case 21:
			return item.getVoidTickets() + " ";
		case 22:
			return NumberUtil.formatNumber(item.getVoidAmount()) + " " + currencySymbol;
		case 23:			
			return NumberUtil.formatNumber(item.getCashPayment()) + " " + currencySymbol;
		case 24:
			return NumberUtil.formatNumber(item.getDiscountAmount()) + " " + currencySymbol;
		case 25:
			//			if(!TerminalConfig.isSupermarket()) {
			//				return null;
			//			}
			return NumberUtil.formatNumber(item.getAwt0()) + " " + currencySymbol;
		case 26:
			return NumberUtil.formatNumber(item.gettt0()) + " " + currencySymbol;
		case 27:
			return item.getInsert() + " ";
		case 28:
			return NumberUtil.formatNumber(item.getInsertAmt()) + " " + currencySymbol;
		case 29:
			return NumberUtil.formatNumber(item.getOnlinePayment()) + " " + currencySymbol;
		case 30:
			return NumberUtil.formatNumber(item.getOnlineTax()) + " " + currencySymbol;
		case 31:
			return item.getCashPaymentCount()+"";
		case 32:
			return item.getCardPaymentCount()+"";
		case 33:
			return NumberUtil.formatNumber(item.getTotalCash()) + " " + currencySymbol;
		case 34:
			return TerminalConfig.getPfand1();
		case 35:
			return NumberUtil.formatNumber(item.getPfand1()) + " " + currencySymbol;
		case 36:
			return TerminalConfig.getPfand2();
		case 37:
			return NumberUtil.formatNumber(item.getPfand2()) + " " + currencySymbol;
		case 38:
			return TerminalConfig.getPfand3();
		case 39:
			return NumberUtil.formatNumber(item.getPfand3()) + " " + currencySymbol;
		case 40:
			return NumberUtil.formatNumber(item.getNetton()) + " " + currencySymbol;
		case 41:
			return NumberUtil.formatNumber(item.getNettos()) + " " + currencySymbol;
		case 42:
			return NumberUtil.formatNumber(item.getNettoz()) + " " + currencySymbol;
		case 43:
			return item.getAnzahlRetour()+"";
		case 44:	
			return NumberUtil.formatNumber(item.getRetourGesamt()) + " " + currencySymbol;
		case 45:
			return NumberUtil.formatNumber(item.getRetourTax()) + " ";
		case 46:
			return NumberUtil.formatNumber(item.getGesamtSumme()) + " " + currencySymbol;		
		case 47:
			return NumberUtil.formatNumber(item.getGesamtMwst19()) + " ";
		case 48:
			return NumberUtil.formatNumber(item.getGesamtMwst7()) + " " ;
		case 49:
			return item.getEinnahme_text();
		case 50:
			return item.getUmasat_gesamt_text();
		case 51:
			return item.getGesamt_19_text();
		case 52:
			return item.getGesamt_7_text();
		case 53:
			return item.getGesamt_0_text();
		case 54:
			return item.getGesamt_netto_text();
		case 55:
			return item.getNetto_19_text();
		case 56:
			return item.getNetto_7_text();
		case 57:
			return item.getNetto_0_text();
		case 58:
			return item.getMwst_19_text();
		case 59:
			return item.getMwst_7_text();
		case 60:
			return item.getMwst_0_text();
		case 61:
			return item.getAnzahl_retour_text();
		case 62:
			return item.getRetour_gesamt_text();
		case 63:
			return item.getRetour_mwst_text();
		case 64:
			return item.getAnzahl_storno_text();
		case 65:
			return item.getAnzahl_storno_gesamt_text();
		case 66:
			return item.getStorno_mwst_text();
		case 67:
			return item.getAnzahl_rechnugen_text();
		case 68:
			return item.getAnzahl_sold_items_text();
		case 69:
			return item.getCashpayment_text();
		case 70:
			return item.getMwst_gesamt_text();
		case 71:
			return item.getMwst_gesamt_text1();
		case 72:
			return item.getAnzahl_text();
		case 73:
			return item.getAnzahl_text1();
		case 74:
			return item.getAnzahl_text2();
		case 75:
			return item.getCardpayment_text();
		case 76:
			return item.getKunden_rabatt_text();
		case 77:
			return item.getCash_in_cashdrawer_text();				
		case 78:
			return item.getGesamt_summe_text();
		case 79:
			return item.getWarengroup_abs_text();
		case 80:
			return item.getWarengroup_text();
		case 81:
			return item.getGesamt_text();
		case 82:
			return item.getAnzahl_text_rechnug();
		case 83:
			return item.getMwst_gesamt_text_rechnug();				
		case 84:		 
			return item.getRechnugPayment_text();
		case 85:			 
			return item.getRechnugPayment_tax();
		case 86:			 
			return item.getRechnugPament_anzahl();
		case 87:			 
			return item.getRechnugPaymentAmount();
		case 88:
			return item.getAnzahl_text_online();
		case 89:
			return item.getMwst_gesamt_text_online();				
		case 90:
			return item.getOnlinePayment_text();
		case 91:
			return item.getOnlinePayment_tax();
		case 92:
			return item.getOnlinePament_anzahl();
		case 93:
			return item.getOnlinePaymentAmount();	
		case 94:
			return NumberUtil.formatNumber(item.getCash19()) + " " + currencySymbol;
		case 95:
			return NumberUtil.formatNumber(item.getCash7()) + " " + currencySymbol;
		case 96:
			return NumberUtil.formatNumber(item.getCard19()) + " " + currencySymbol;
		case 97:
			return NumberUtil.formatNumber(item.getCard7()) + " " + currencySymbol;			
		case 98:
			return item.getCash19Text();
		case 99:
			return item.getCash7Text();
		case 100:
			return item.getCard19Text();
		case 101:
			return item.getCard7Text();		
		case 102:
			return NumberUtil.formatNumber(item.getBtrinkgeld()) + " " + currencySymbol;   
		case 103:
			return NumberUtil.formatNumber(item.getKtrinkgeld()) + " " + currencySymbol; 
		case 104:			
			return NumberUtil.formatNumber(item.getSoldGutschein()) + " " + currencySymbol;
		case 105:			
			return NumberUtil.formatNumber(item.getTicketGutscheinAmount()) + " " + currencySymbol;
		case 106:			
			return item.getGutschein_eingelost_text();
		case 107:			
			return item.getVerkaufte_gutschein_text();
		case 108:			
			return item.getBar_tip_text();
		case 109:			
			return item.getOther_tip_text();
		case 110:
			return item.getRabatt_19text();
		case 111:
			return item.getRabatt_7text();
		case 112:
			return NumberUtil.formatNumber(item.getRabatt_19()) + " " + currencySymbol;
		case 113:
			return NumberUtil.formatNumber(item.getRabatt_7()) + " " + currencySymbol;
			
		}

		return null;
	}

	public List<ReportItem> getItems() {
		return items;
	}

	public void setItems(List<ReportItem> items) {
		this.items = items;
	}

	public double getGrandTotal() {
		return grandTotal;
	}

	public String getGrandTotalAsString() {
		return currencySymbol + " " + formatter.format(grandTotal);
	}

	public void setGrandTotal(double grandTotal) {
		this.grandTotal = grandTotal;
	}

	public void calculateGrandTotal() {
		/*grandTotal = 0;
		if(items == null) {
			return;
		}

		for (ReportItem item : items) {
			grandTotal += item.getTotal();
		}*/
	}
}
