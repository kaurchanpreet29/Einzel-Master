package com.floreantpos.bo.actions;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Tax;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.model.MenuItemForm;

import net.miginfocom.swing.MigLayout;

public class WordDesign extends POSDialog  {
	JTextField payNow;
	JTextField payLater;
	JTextField diverse;
	JTextField additem;
	JTextField ticketpreview;
	JTextField deleteitem;  
	JTextField WeightSonsName;
	JCheckBox cbWeightSons = new JCheckBox();

	JTextField settle;
	JTextField reopen;
	JTextField deleteTicket;
	JTextField oldTicket;
	JTextField closeTicket;
	JTextField itemSonstigesText;
	JTextField cashBookText;
	JButton textColorBtn;
	JButton bckColorBtn;
	Color textColor;
	Color btnBckColor;
	PosButton saveButton;
	PosButton cancelButton;
	private JCheckBox dupTseBackendDispaly;
	private JCheckBox trinkgeld;
	private JCheckBox normalAuswalEnable;
	private JCheckBox detailedRecieptEnable;
	
	JTextField numberDisplayWidth;
	JTextField numberDisplayHeight;
	JTextField tfRabattDirect = new JTextField();
	JCheckBox cbCustomRechnerDisplay = new JCheckBox("Manual Nummeric Display");
	JCheckBox cbOnlyEcCard = new JCheckBox("Kein Karte Bestätigung");
	JCheckBox cbFinanzPrufung = new JCheckBox("Finazprüfung");
	JCheckBox cbTakeItemCount = new JCheckBox("Item Count");
	JCheckBox cbPriceCategory = new JCheckBox("Price list");
	JCheckBox cbPriceCategoryKunden = new JCheckBox("Kunden Nr. As Price list");
	JCheckBox cbPriceCategoryUpdate = new JCheckBox("Price list update view");
	JCheckBox cbWholeSale = new JCheckBox("Whole Sale");
	JCheckBox cbOffer = new JCheckBox(POSConstants.OFFER.toLowerCase());
	JCheckBox cbAutoRestartSystem = new JCheckBox("Auto Restart");
	JCheckBox cbPlayNotification = new JCheckBox("Alert Sound");
	JCheckBox cbAddItemDirect = new JCheckBox("Produkt-Add");
	JCheckBox cbCashDrawerAll = new JCheckBox("Cashdrawer-All");
	JCheckBox cbRemoteBackup = new JCheckBox("DB-BCKP");
	JCheckBox cbRabattDirekt = new JCheckBox("Rabatt direkt");
	
	JCheckBox cbKellnerStorno = new JCheckBox("Storno Kellner");
	JCheckBox cbDrukDisplay = new JCheckBox("Druck Display");
	JCheckBox cbDrukFootrMsgDisplay = new JCheckBox("Druck Footer Nachricht");
	
	JCheckBox cbGutInBalance = new JCheckBox("Gut.Diplay in Balance");

	JCheckBox cbOnlinePayment = new JCheckBox("Online Zahlung");

	JCheckBox cbItemSorting = new JCheckBox("Item View Sorting on ID");
	JCheckBox cbItemPriceSorting = new JCheckBox("Item View Sorting on Price");
	JCheckBox cbCashBook = new JCheckBox("");

	JCheckBox cbCalculator = new JCheckBox("Taschenrechner");
	JCheckBox cbMonthReport = new JCheckBox("MonatsAbschluss");
	JCheckBox cbServerReport = new JCheckBox("MitarbeiterAbschluss");

	//	JCheckBox cbMainCustomer = new JCheckBox("Kunden");
	JCheckBox cbBonRoll = new JCheckBox("Bon Roll");
	JCheckBox cbReservation = new JCheckBox("Termine");

	JCheckBox cbKarte = new JCheckBox("Karte");
	JCheckBox cbPreview = new JCheckBox("Vorschau");
	
	JCheckBox cbRabatt = new JCheckBox("Rabatt");
	JCheckBox cbOpendrawer = new JCheckBox("Schublade Oeffnen");
	JCheckBox cbCustomer = new JCheckBox("Kunden ");
	JCheckBox cbSonstiges = new JCheckBox("Sonstiges");
	JCheckBox cbItemsearch = new JCheckBox("Art. suchen");
	JCheckBox cbItemBarcode = new JCheckBox("Art. barcode");
	JCheckBox cbOrders = new JCheckBox("Bestellungen");
	JCheckBox cbUpdated = new JCheckBox("Updated-Design");
	JCheckBox cbSaldo = new JCheckBox("Saldo");
	JCheckBox cbQrCode = new JCheckBox("Receipt Mit QR");
	JCheckBox cbViceVersa = new JCheckBox("19%/Rabatt");
	JCheckBox cbViceVersa7 = new JCheckBox("7%/Rabatt");

	JCheckBox cbOnlineSales = new JCheckBox("Online Listner");
	JCheckBox cbInventurAlert = new JCheckBox("Inventur alert");
	JCheckBox cbInventur = new JCheckBox("Inventur");

	JCheckBox cbFastPayment = new JCheckBox("Sofort Zahlung");
	JCheckBox cbDuplicateRechnug = new JCheckBox("Duplikat Rechnug");
	JCheckBox cbtWarenAbs = new JCheckBox("Warengroup Abs");
	JCheckBox cbShowReturnWindow = new JCheckBox("Zuruck Dialog");
	JCheckBox cbAutoSalesReport = new JCheckBox();	
	JComboBox<String> cmbAutoSalesHour;

	JCheckBox cbHideItemPrice = new JCheckBox("Art. Preis verstecken");
	JCheckBox cbHideItemId = new JCheckBox("Art. Id verstecken");

	JCheckBox cbReopen = new JCheckBox("Sichtbar");
	
	JCheckBox cbTicketDelete = new JCheckBox("Sichtbar");
	JCheckBox cbTicketArchive = new JCheckBox("Sichtbar");
	JCheckBox cbSpecial = new JCheckBox("Speical");
	JCheckBox cbSalesKellner = new JCheckBox("T.A. All");
	JCheckBox cbMultiUser = new JCheckBox("Multi User");
	JCheckBox cbMultiUserVerify = new JCheckBox("Multi User Verify");

	private JCheckBox cbA4Background = new JCheckBox("A4 With Background");
	private JCheckBox cbA4BackgroundSP = new JCheckBox("A4_SP");
	private JCheckBox cbZahlungSteur = new JCheckBox("Tagesabs mit Zahlung");
	private JCheckBox cbTagesRefresh = new JCheckBox("TagesAbs Refresh");


	public WordDesign(String title)
	{
		setTitle(title);
		setPreferredSize(new Dimension(1000,768));
		JPanel orderPanel = new JPanel();
		setLayout(new MigLayout());

		JPanel homePanel = new JPanel();
		homePanel.setLayout(new MigLayout());

		orderPanel.setLayout(new MigLayout());
		
		
		if(StringUtils.isNotEmpty(POSConstants.Sofort_zahlen))
			orderPanel.add(new JLabel(POSConstants.Sofort_zahlen));
		else
			orderPanel.add(new JLabel("Sofort zahlen: "));
		
		payNow = new JTextField(20);
		payNow.setText("RECHNUNG");
		orderPanel.add(payNow);

		if(StringUtils.isNotEmpty(POSConstants.Spaeter_zahlen))
			orderPanel.add(new JLabel(POSConstants.Spaeter_zahlen));
		else
			orderPanel.add(new JLabel("Spaeter zahlen: "));
		
		payLater = new JTextField(20);
		payLater.setText("SALDO");
		orderPanel.add(payLater,"wrap");

		 
		if(StringUtils.isNotEmpty(POSConstants.Diverse))
			orderPanel.add(new JLabel(POSConstants.Diverse));
		else
			orderPanel.add(new JLabel("Diverse: "));
		
		diverse = new JTextField(20);
		diverse.setText("Sonstiges");
		
		if(StringUtils.isNotEmpty(POSConstants.OTHERS))
			diverse.setText(POSConstants.OTHERS);
		else
			diverse.setText("Sonstiges");
		
		orderPanel.add(diverse);

		if(StringUtils.isNotEmpty(POSConstants.Artikel_hinzufuegen))
			orderPanel.add(new JLabel(POSConstants.Artikel_hinzufuegen));
		else
			orderPanel.add(new JLabel("Artikel hinzufuegen: "));
		
		additem = new JTextField(20);
		additem.setText("ART");
		orderPanel.add(additem,"wrap");

		if(StringUtils.isNotEmpty(POSConstants.Rechnungsvorschau))
			orderPanel.add(new JLabel(POSConstants.Rechnungsvorschau));
		else
			orderPanel.add(new JLabel("Rechnungsvorschau: "));
		
		ticketpreview = new JTextField(20);
		ticketpreview.setText("Vorschau");
		if(StringUtils.isNotEmpty(POSConstants.Vorschau))
			ticketpreview.setText(POSConstants.Vorschau);
		else
			ticketpreview.setText("Vorschau");
		
		orderPanel.add(ticketpreview);

		if(StringUtils.isNotEmpty(POSConstants.Artikel_loeschen))
			orderPanel.add(new JLabel(POSConstants.Artikel_loeschen));
		else
			orderPanel.add(new JLabel("Artikel loeschen: "));
				
		deleteitem = new JTextField(20);
		deleteitem.setText("CLR");
		orderPanel.add(deleteitem,"wrap");

		
		if(StringUtils.isNotEmpty(POSConstants.Rabatt_Direkt))
			orderPanel.add(new JLabel(POSConstants.Rabatt_Direkt));
		else
			orderPanel.add(new JLabel("Rabatt-Direkt"));
		
		tfRabattDirect = new JTextField(20);
		tfRabattDirect.setText("Rabatt-Direkt");
		if(StringUtils.isNotEmpty(POSConstants.Rabatt_Direkt))
			tfRabattDirect.setText(POSConstants.Rabatt_Direkt);
		else
			tfRabattDirect.setText("Rabatt-Direkt");
		
		orderPanel.add(tfRabattDirect);
		orderPanel.add(cbRabattDirekt, "wrap");
		cbRabattDirekt.setBackground(new Color(209,222,235));
		
		if(StringUtils.isNotEmpty(POSConstants.Sons_Waage_btnName))
			orderPanel.add(new JLabel(POSConstants.Sons_Waage_btnName));
		else
			orderPanel.add(new JLabel("Sons. Waage btn Name: "));
		
		WeightSonsName = new JTextField(20);
		WeightSonsName.setText(TerminalConfig.getWageSonsName());
		orderPanel.add(WeightSonsName,"grow");
		cbWeightSons.setSelected(TerminalConfig.isWageSons());
		cbWeightSons.setBackground(new Color(209,222,235));
		orderPanel.add(cbWeightSons,"wrap");

		if(StringUtils.isNotEmpty(POSConstants.Abschliessen))
			homePanel.add(new JLabel(POSConstants.Abschliessen));
		else
			homePanel.add(new JLabel("Abschliessen: "));
		
		settle = new JTextField(20);
		settle.setText("ERLEDIGEN");
		homePanel.add(settle,"growx");
		
		if(StringUtils.isNotEmpty(POSConstants.Rechnung_schliessen))
			homePanel.add(new JLabel(POSConstants.Rechnung_schliessen));		
		else
			homePanel.add(new JLabel("Rechnung schliessen: "));		
		
		closeTicket = new JTextField(20);
		closeTicket.setText("GES.");
		homePanel.add(closeTicket,"wrap");
		if(StringUtils.isNotEmpty(POSConstants.Art_Sonstiges_Text))
			homePanel.add(new JLabel(POSConstants.Art_Sonstiges_Text));	
		else
			homePanel.add(new JLabel("Art. Sonstiges Text"));	
		
		itemSonstigesText = new JTextField(20);
		itemSonstigesText.setText("Sonstiges");
		homePanel.add(itemSonstigesText,"growx");

		homePanel.add(new JLabel("Auto Sales Hour"));
		cmbAutoSalesHour = new JComboBox<>();
		for(int i =0;i<24;i++) {
			cmbAutoSalesHour.addItem(String.format("%02d", i));			
		}
		homePanel.add(cmbAutoSalesHour,"growx");
		homePanel.add(cbAutoSalesReport,"wrap");		

		if(StringUtils.isNotEmpty(POSConstants.wieder_oeffnen))
			homePanel.add(new JLabel(POSConstants.wieder_oeffnen));	
		else
			homePanel.add(new JLabel("wieder oeffnen: "));	
		
		reopen = new JTextField(20);
		reopen.setText("Rechnung Bearbeiten");
		homePanel.add(reopen);
		homePanel.add(cbReopen,"wrap");
		cbReopen.setBackground(new Color(209,222,235));

		if(StringUtils.isNotEmpty(POSConstants.Rechnung_loeschen))
			homePanel.add(new JLabel(POSConstants.Rechnung_loeschen));	
		else
			homePanel.add(new JLabel("Rechnung loeschen: "));	
		
		deleteTicket = new JTextField(20);
		deleteTicket.setText("ENTSORGEN");
		homePanel.add(deleteTicket);
		homePanel.add(cbTicketDelete,"wrap");
		cbTicketDelete.setBackground(new Color(209,222,235));

		 
		if(StringUtils.isNotEmpty(POSConstants.Alte_Rechnungen))
			homePanel.add(new JLabel(POSConstants.Alte_Rechnungen));	
		else
			homePanel.add(new JLabel("Alte Rechnungen: "));
		
		oldTicket = new JTextField(20);
		oldTicket.setText("RECHNUNGS ARCHIV");
		homePanel.add(oldTicket);
		homePanel.add(cbTicketArchive,"wrap");
		cbTicketArchive.setBackground(new Color(209,222,235));

		 
		if(StringUtils.isNotEmpty(POSConstants.Kassenbuch))
			homePanel.add(new JLabel(POSConstants.Kassenbuch));	
		else
			homePanel.add(new JLabel("Kassenbuch: "));
		
		cashBookText = new JTextField(20);
		cashBookText.setText(TerminalConfig.getCashbookText());
		homePanel.add(cashBookText);
		homePanel.add(cbCashBook,"wrap");

		JPanel secPanel = new JPanel();
		secPanel.setLayout(new MigLayout());
		secPanel.setBackground(new Color(209,222,235));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new MigLayout());
		buttonPanel.setBackground(new Color(209,222,235));

		if(StringUtils.isNotEmpty(POSConstants.Vorschau))
			cbPreview.setText(POSConstants.Vorschau);	
		
		cbPreview.setBackground(new Color(209,222,235));		
		secPanel.add(cbPreview);
		
		if(StringUtils.isNotEmpty(POSConstants.Rabatt))
			cbRabatt.setText(POSConstants.Rabatt);
		
		cbRabatt.setBackground(new Color(209,222,235));
		secPanel.add(cbRabatt);
		
		if(StringUtils.isNotEmpty(POSConstants.Karte))
			cbKarte.setText(POSConstants.Karte);
		
		cbKarte.setBackground(new Color(209,222,235));
		secPanel.add(cbKarte);
		
		if(StringUtils.isNotEmpty(POSConstants.Schublade_Oeffnen))
			cbOpendrawer.setText(POSConstants.Schublade_Oeffnen);
		
		cbOpendrawer.setBackground(new Color(209,222,235));
		secPanel.add(cbOpendrawer);
		
		if(StringUtils.isNotEmpty(POSConstants.OTHERS))
			cbSonstiges.setText(POSConstants.OTHERS);
		
		cbSonstiges.setBackground(new Color(209,222,235));
		secPanel.add(cbSonstiges);
		if(StringUtils.isNotEmpty(POSConstants.Art_suchen))
			cbItemsearch.setText(POSConstants.Art_suchen);
		
		cbItemsearch.setBackground(new Color(209,222,235));
		secPanel.add(cbItemsearch,"wrap");
		cbItemBarcode.setBackground(new Color(209,222,235));
		secPanel.add(cbItemBarcode);
		
		if(StringUtils.isNotEmpty(POSConstants.Bestellen))
			cbOrders.setText(POSConstants.Bestellen);
		
		cbOrders.setBackground(new Color(209,222,235));
		secPanel.add(cbOrders);
		if(StringUtils.isNotEmpty(POSConstants.Saldo))
			cbSaldo.setText(POSConstants.Saldo);
		
		cbSaldo.setBackground(new Color(209,222,235));
		secPanel.add(cbSaldo);
		cbUpdated.setBackground(new Color(209,222,235));		 	
		secPanel.add(cbUpdated);
		
		if(StringUtils.isNotEmpty(POSConstants.Kunden))
			cbCustomer.setText(POSConstants.Kunden);
		
		cbCustomer.setBackground(new Color(209,222,235));
		secPanel.add(cbCustomer);
		
		if(StringUtils.isNotEmpty(POSConstants.MONTHLY_REPORT))
			cbMonthReport.setText(POSConstants.MONTHLY_REPORT);
		
		cbMonthReport.setBackground(new Color(209,222,235));
		secPanel.add(cbMonthReport, "wrap");

		if(StringUtils.isNotEmpty(POSConstants.MitarbeiterAbschluss))
			cbServerReport.setText(POSConstants.MitarbeiterAbschluss);
		
		cbServerReport.setBackground(new Color(209,222,235));
		secPanel.add(cbServerReport);
		
		if(StringUtils.isNotEmpty(POSConstants.Bon_Roll))
			cbBonRoll.setText(POSConstants.Bon_Roll);
		
		cbBonRoll.setBackground(new Color(209,222,235));
		secPanel.add(cbBonRoll);
		
		if(StringUtils.isNotEmpty(POSConstants.TERMINE))
			cbReservation.setText(POSConstants.TERMINE);
		
		cbReservation.setBackground(new Color(209,222,235));
		secPanel.add(cbReservation);
		
		if(StringUtils.isNotEmpty(POSConstants.Art_Id_verstecken))
			cbHideItemId.setText(POSConstants.Art_Id_verstecken);
		
		cbHideItemId.setBackground(new Color(209,222,235));
		secPanel.add(cbHideItemId);
		if(StringUtils.isNotEmpty(POSConstants.Art_Preis_verstecken))
			cbHideItemPrice.setText(POSConstants.Art_Preis_verstecken);
		
		cbHideItemPrice.setBackground(new Color(209,222,235));
		secPanel.add(cbHideItemPrice, "wrap");
		if(StringUtils.isNotEmpty(POSConstants.Receipt_Mit_QR))
			cbQrCode.setText(POSConstants.Receipt_Mit_QR);
		
		cbQrCode.setBackground(new Color(209,222,235));
		secPanel.add(cbQrCode);
		
		if(StringUtils.isNotEmpty(POSConstants.Rabatt_19))
			cbViceVersa.setText(POSConstants.Rabatt_19);
		
		cbViceVersa.setBackground(new Color(209,222,235));
		cbViceVersa.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				if(cbViceVersa.isSelected())
					cbViceVersa7.setSelected(false);			
			}
		});
		cbViceVersa7.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(cbViceVersa7.isSelected())
					cbViceVersa.setSelected(false);			
			}
		});

		if(StringUtils.isNotEmpty(POSConstants.Rabatt_7))
			cbViceVersa7.setText(POSConstants.Rabatt_7);
		
		secPanel.add(cbViceVersa);
		cbViceVersa7.setBackground(new Color(209,222,235));
		secPanel.add(cbViceVersa7);
		cbOnlineSales.setBackground(new Color(209,222,235));
		secPanel.add(cbOnlineSales);
		if(StringUtils.isNotEmpty(POSConstants.BACK_INVENTUR))
			cbInventur.setText(POSConstants.BACK_INVENTUR);
		
		cbInventur.setBackground(new Color(209,222,235));
		secPanel.add(cbInventur);
		if(StringUtils.isNotEmpty(POSConstants.Sofort_zahlen))
			cbFastPayment.setText(POSConstants.Sofort_zahlen);
		
		if(StringUtils.isNotEmpty(POSConstants.Inventur_alert))
			cbInventurAlert.setText(POSConstants.Inventur_alert);
		
		cbInventurAlert.setBackground(new Color(209,222,235));
		secPanel.add(cbInventurAlert, "wrap");
		
		if(StringUtils.isNotEmpty(POSConstants.Sofort_zahlen))
			cbFastPayment.setText(POSConstants.Sofort_zahlen);
		 
		cbFastPayment.setBackground(new Color(209,222,235));
		secPanel.add(cbFastPayment);
		
		if(StringUtils.isNotEmpty(POSConstants.Warengroup_Abs))
			cbtWarenAbs.setText(POSConstants.Warengroup_Abs);
		
		cbtWarenAbs.setBackground(new Color(209,222,235));
		secPanel.add(cbtWarenAbs);		

		if(StringUtils.isNotEmpty(POSConstants.Zuruck_Dialog))
			cbShowReturnWindow.setText(POSConstants.Zuruck_Dialog);
		
		cbShowReturnWindow.setBackground(new Color(209,222,235));
		secPanel.add(cbShowReturnWindow);
		if(StringUtils.isNotEmpty(POSConstants.Duplikat_Rechnug))
			cbDuplicateRechnug.setText(POSConstants.Duplikat_Rechnug);
		
		cbDuplicateRechnug.setBackground(new Color(209,222,235));
		secPanel.add(cbDuplicateRechnug, "growx");
		cbTakeItemCount.setBackground(new Color(209,222,235));		
		secPanel.add(cbTakeItemCount);

		
		if(StringUtils.isNotEmpty(POSConstants.Finazpruefung))
			cbFinanzPrufung.setText(POSConstants.Finazpruefung);
		
		cbFinanzPrufung.setBackground(new Color(209,222,235));
		secPanel.add(cbFinanzPrufung, "wrap");
		if(StringUtils.isNotEmpty(POSConstants.Kunden_Nr_Price_list))
			cbPriceCategoryKunden.setText(POSConstants.Kunden_Nr_Price_list);
		
		cbPriceCategoryKunden.setBackground(new Color(209,222,235));
		cbPriceCategory.setBackground(new Color(209,222,235));		
		secPanel.add(cbPriceCategory, "growx");
		secPanel.add(cbPriceCategoryKunden, "growx");
		cbPriceCategoryUpdate.setBackground(new Color(209,222,235));
		cbPriceCategoryUpdate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cbPriceCategory.setSelected(cbPriceCategoryUpdate.isSelected());
			}
		});

		cbPriceCategory.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cbPriceCategoryUpdate.setSelected(cbPriceCategory.isSelected());
			}
		});

		secPanel.add(cbPriceCategoryUpdate, "growx");
		cbWholeSale.setBackground(new Color(209,222,235));	

		cbOffer.setBackground(new Color(209,222,235));				
		secPanel.add(cbWholeSale, "growx");
		secPanel.add(cbOffer, "growx");
		cbFinanzPrufung.setBackground(new Color(209,222,235));
		cbItemSorting.setBackground(new Color(209,222,235));
		secPanel.add(cbItemSorting, "wrap");
		cbAutoRestartSystem.setBackground(new Color(209,222,235));
		secPanel.add(cbAutoRestartSystem, "growx");
		cbItemPriceSorting.setBackground(new Color(209,222,235));
		secPanel.add(cbItemPriceSorting,"growx");		
		cbSpecial.setSelected(TerminalConfig.isSpecial());
		cbSpecial.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					setSpeicial(cbSpecial.isSelected());
					TerminalConfig.setSpecial(cbSpecial.isSelected());
					JOptionPane.showMessageDialog(BackOfficeWindow.getInstance(), "Erfolg");
					Application.getInstance().restartApp();
					System.exit(0);	
				}catch(Exception ex) {
					JOptionPane.showMessageDialog(BackOfficeWindow.getInstance(), ex.getMessage());

				}

			}
		});

		cbMultiUser.setBackground(new Color(209,222,235));
		cbMultiUser.setSelected(TerminalConfig.isMultiUser());

		cbMultiUserVerify.setBackground(new Color(209,222,235));
		cbMultiUserVerify.setSelected(TerminalConfig.isMultiUserProof());
		secPanel.add(cbSpecial);
		cbSpecial.setBackground(new Color(209,222,235));
		secPanel.add(cbSalesKellner);	
		cbSalesKellner.setBackground(new Color(209,222,235));
		if(StringUtils.isNotEmpty(POSConstants.Online_Zahlung))
			cbOnlinePayment.setText(POSConstants.Online_Zahlung);
		
		secPanel.add(cbOnlinePayment, "growx");		
		cbA4Background.setBackground(new Color(209, 222, 235));
		secPanel.add(cbA4Background, "growx, wrap");		

		cbA4BackgroundSP.setBackground(new Color(209, 222, 235));
		secPanel.add(cbA4BackgroundSP, "growx");
		secPanel.add(cbMultiUser, "growx");
		secPanel.add(cbMultiUserVerify, "growx");
		cbPlayNotification.setBackground(new Color(209,222,235));
		cbPlayNotification.setSelected(TerminalConfig.isPlaySound());
		secPanel.add(cbPlayNotification, "growx");

		if(StringUtils.isNotEmpty(POSConstants.Produkt_Add))
			cbAddItemDirect.setText(POSConstants.Produkt_Add);
		
		cbAddItemDirect.setBackground(new Color(209,222,235));
		cbAddItemDirect.setSelected(TerminalConfig.isItemAdd());
		secPanel.add(cbAddItemDirect, "growx");		

		dupTseBackendDispaly = new JCheckBox("T.Rech.Display");
		dupTseBackendDispaly.setBackground(new Color(209,222,235));
		dupTseBackendDispaly.setSelected(TerminalConfig.isDupTseBackendDispaly());		
		secPanel.add(dupTseBackendDispaly, "growx, wrap");
		
		trinkgeld = new JCheckBox("Trinkgeld");
		if(StringUtils.isNotEmpty(POSConstants.Trinkgeld))
			trinkgeld.setText(POSConstants.Trinkgeld);
		
		trinkgeld.setBackground(new Color(209,222,235));
		trinkgeld.setSelected(TerminalConfig.isTRINKGELD());		
		secPanel.add(trinkgeld, "growx");
		
		if(StringUtils.isNotEmpty(POSConstants.Storno_Kellner))
			cbKellnerStorno.setText(POSConstants.Storno_Kellner);
		
		cbKellnerStorno.setBackground(new Color(209,222,235));
		secPanel.add(cbKellnerStorno, "growx");
		
		
		cbRemoteBackup.setBackground(new Color(209,222,235));
		cbRemoteBackup.setSelected(TerminalConfig.isRemoteBackup());

		if(Application.getInstance().getCurrentUser().isSuperUser())		
			secPanel.add(cbRemoteBackup, "growx");

		cbCashDrawerAll.setBackground(new Color(209,222,235));
		cbCashDrawerAll.setSelected(TerminalConfig.isAlwaysOpenCashdrawer());
		secPanel.add(cbCashDrawerAll, "growx");	
		 
		cbZahlungSteur.setBackground(new Color(209,222,235));
		cbZahlungSteur.setSelected(TerminalConfig.isZahlungSteur());
		if(Application.getCurrentUser().isSuperUser())
			secPanel.add(cbZahlungSteur,"growx");
		
		normalAuswalEnable = new JCheckBox("Normal Auswal Enable");
		if(StringUtils.isNotEmpty(POSConstants.Normal_Auswal_Enable))
			normalAuswalEnable.setText(POSConstants.Normal_Auswal_Enable);
		
		normalAuswalEnable.setBackground(new Color(209,222,235));
		normalAuswalEnable.setSelected(TerminalConfig.isNormalAuswalEnable());		
		secPanel.add(normalAuswalEnable, "growx, wrap");
		
		detailedRecieptEnable = new JCheckBox("Detailed Receipt Enable");
		detailedRecieptEnable.setBackground(new Color(209,222,235));
		detailedRecieptEnable.setSelected(TerminalConfig.isDetailedRecieptEnable());		
		secPanel.add(detailedRecieptEnable, "growx");
		
		cbTagesRefresh.setBackground(new Color(209,222,235));
		cbTagesRefresh.setSelected(TerminalConfig.isTagesAbsRefresh());
		secPanel.add(cbTagesRefresh,"growx");
		
		if(StringUtils.isNotEmpty(POSConstants.Druck_Display))
			cbDrukDisplay.setText(POSConstants.Druck_Display);
		
		cbDrukDisplay.setBackground(new Color(209,222,235));
		secPanel.add(cbDrukDisplay, "growx");
		
		if(StringUtils.isNotEmpty(POSConstants.Druck_Footer_Nachricht))
			cbDrukFootrMsgDisplay.setText(POSConstants.Druck_Footer_Nachricht);
		 
		cbDrukFootrMsgDisplay.setBackground(new Color(209,222,235));
		secPanel.add(cbDrukFootrMsgDisplay, "growx");
		
		cbGutInBalance.setBackground(new Color(209,222,235));
		secPanel.add(cbGutInBalance, "growx");
		
		if(StringUtils.isNotEmpty(POSConstants.Taschenrechner))
			cbCalculator.setText(POSConstants.Taschenrechner);
		
		cbCalculator.setBackground(new Color(209,222,235));
		secPanel.add(cbCalculator);
		
		cbA4BackgroundSP.setSelected(TerminalConfig.isA4WithBackgroundSP());		
		cbA4Background.setSelected(TerminalConfig.isA4WithBackground());
		cbOnlinePayment.setBackground(new Color(209,222,235));

		cbPriceCategory.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(cbPriceCategory.isSelected()) {
					cbPriceCategoryKunden.setSelected(false);
					cbCustomer.setSelected(true);
				}				
			}
		});

		cbPriceCategoryKunden.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(cbPriceCategoryKunden.isSelected()) {
					cbPriceCategory.setSelected(false);
					cbCustomer.setSelected(true);
				}				
			}
		});

		Restaurant restaurant = RestaurantDAO.getRestaurant();

		if(restaurant.getPaynow() != null)
			payNow.setText(restaurant.getPaynow());

		
			
		if(restaurant.getPaylater() != null)
			payLater.setText(restaurant.getPaylater());

		if(restaurant.getSettle() != null)
			settle.setText(restaurant.getSettle());

		if(restaurant.getReopen() != null)
			reopen.setText(restaurant.getReopen());

		if(restaurant.getDeleteticket() != null)
			deleteTicket.setText(restaurant.getDeleteticket());

		if(restaurant.getDiverse() != null)
			diverse.setText(restaurant.getDiverse());

		if(restaurant.getAdditem() != null)
			additem.setText(restaurant.getAdditem());

		if(restaurant.getDeleteitem() != null)
			deleteitem.setText(restaurant.getDeleteitem());

		if(restaurant.getOldticket() != null)
			oldTicket.setText(restaurant.getOldticket());

		if(restaurant.getItemsonstigestext() != null)
			itemSonstigesText.setText(restaurant.getItemsonstigestext());

		if(restaurant.getCloseticket() != null)
			closeTicket.setText(restaurant.getCloseticket());

		if(restaurant.getTicketpreview() != null)
			ticketpreview.setText(restaurant.getTicketpreview());

		cmbAutoSalesHour.setSelectedItem(restaurant.getAutoSalesHour());
		cbAutoSalesReport.setSelected(TerminalConfig.isAutoTagesAbs());		
		cbShowReturnWindow.setSelected(restaurant.isShowReturnDialog());

		
		if(TerminalConfig.isRabattDirektEnable())
			cbRabattDirekt.setSelected(true);		
		if(TerminalConfig.isPreviewEnable())
			cbPreview.setSelected(true);
		if(TerminalConfig.isRabattEnable())
			cbRabatt.setSelected(true);
		if(TerminalConfig.isOpenDrawer())
			cbOpendrawer.setSelected(true);
		if(TerminalConfig.isSontiges())
			cbSonstiges.setSelected(true);
		if(TerminalConfig.isCardEnable())
			cbKarte.setSelected(true);
		if(TerminalConfig.isItemSearch())
			cbItemsearch.setSelected(true);
		if(TerminalConfig.isItemBarcode())
			cbItemBarcode.setSelected(true);
		if(TerminalConfig.isOrders())
			cbOrders.setSelected(true);
		if(TerminalConfig.isSaldo())
			cbSaldo.setSelected(true);
		if(TerminalConfig.isHideItemPrice())
			cbHideItemPrice.setSelected(true);
		if(TerminalConfig.isHideItemId())
			cbHideItemId.setSelected(true);
		if(TerminalConfig.isTicketDelete())
			cbTicketDelete.setSelected(true);
		cbCustomer.setSelected(TerminalConfig.isCustomer());
		if(TerminalConfig.isTicketArchive())
			cbTicketArchive.setSelected(true);
		if(TerminalConfig.isTicketReopen())
			cbReopen.setSelected(true);
		if(TerminalConfig.isUpdatedDesign())
			cbUpdated.setSelected(true);
		
		cbCashBook.setSelected(TerminalConfig.isCashBookEnable());

		cbFinanzPrufung.setSelected(TerminalConfig.isFinanzPrufung());
		cbItemSorting.setSelected(TerminalConfig.isItemSorting());
		cbItemPriceSorting.setSelected(TerminalConfig.isItemPriceSorting());
		cbAutoRestartSystem.setSelected(TerminalConfig.isAutoRestartSystem());

		cbPriceCategoryKunden.setSelected(TerminalConfig.isPriceCategoryKunden());
		cbPriceCategoryUpdate.setSelected(TerminalConfig.isUpdatePriceCategory());
		cbWholeSale.setSelected(TerminalConfig.isWholeSale());
		cbOffer.setSelected(TerminalConfig.isOffer());
		cbPriceCategory.setSelected(TerminalConfig.isPriceCategory());
		cbQrCode.setSelected(TerminalConfig.isQRcode());
		cbViceVersa.setSelected(TerminalConfig.isAddRabattAt19());
		cbViceVersa7.setSelected(TerminalConfig.isAddRabattAt7());
		cbOnlineSales.setSelected(TerminalConfig.isOnlineSalesStart());
		cbInventurAlert.setSelected(TerminalConfig.isInventurAlert());
		cbInventur.setSelected(TerminalConfig.isInventur());
		cbCalculator.setSelected(TerminalConfig.isCalculator());
		cbMonthReport.setSelected(TerminalConfig.isMonthReport());
		cbServerReport.setSelected(TerminalConfig.isServerReport());
		//		cbMainCustomer.setSelected(TerminalConfig.isMainCustomerButton());
		cbBonRoll.setSelected(TerminalConfig.isBonRoll());
		cbReservation.setSelected(TerminalConfig.isReservation());
		cbCustomRechnerDisplay.setSelected(TerminalConfig.isCustomNumberDisplay());
		cbOnlyEcCard.setSelected(TerminalConfig.isOnlyEC());
		cbFastPayment.setSelected(restaurant.isFastPayment());
		cbDuplicateRechnug.setSelected(restaurant.isCopyRechnug());		
		cbtWarenAbs.setSelected(restaurant.isWithWarengroup());
		cbTakeItemCount.setSelected(TerminalConfig.isTakeItemCount());
		cbUpdated.setSelected(TerminalConfig.isUpdatedDesign());
		cbKellnerStorno.setSelected(TerminalConfig.isAllowStorno());
		cbDrukDisplay.setSelected(TerminalConfig.isRechGedruktDisplay());
		cbDrukFootrMsgDisplay.setSelected(TerminalConfig.isFooterMsgEnabled());
		cbGutInBalance.setSelected(TerminalConfig.isGutInBalanceDialog());
		cbSalesKellner.setSelected(restaurant.isKellenerSalesReport());	
		cbOnlinePayment.setSelected(TerminalConfig.isPayTransfer());
		tfRabattDirect.setText(TerminalConfig.getRabattDirectText());
		saveButton = new PosButton("Speichern");
		if(StringUtils.isNotEmpty(POSConstants.Speichern))
			saveButton.setText(POSConstants.Speichern);
		else
			saveButton.setText("Speichern");
		
		saveButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				save();
				dispose();
			}

		}); 

		saveButton.setBackground(new Color(102,255,102));
		buttonPanel.add(saveButton);

		cancelButton = new PosButton("ABBRECHEN");
		if(StringUtils.isNotEmpty(POSConstants.ABBRECHEN))
			cancelButton.setText(POSConstants.ABBRECHEN);
		
		if(StringUtils.isNotEmpty(POSConstants.Speichern))
			saveButton.setText(POSConstants.Speichern);
		else
			saveButton.setText("Speichern");
		
		cancelButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}

		});
		cancelButton.setBackground(new Color(255,153,153));


		JPanel displayPanel = new JPanel();
		displayPanel.setLayout(new MigLayout());
		displayPanel.add(new JLabel("Numeric Display Width"));
		numberDisplayWidth = new JTextField(20);
		numberDisplayWidth.setText(TerminalConfig.getNumberDisplayWidth());
		displayPanel.add(numberDisplayWidth,"growx");
		displayPanel.add(new JLabel("Numeric Display Height"));
		numberDisplayHeight = new JTextField(20);
		numberDisplayHeight.setText(TerminalConfig.getNumberDisplayHeight());
		displayPanel.add(numberDisplayHeight,"growx");
		cbCustomRechnerDisplay.setBackground(new Color(209,222,235));
		displayPanel.add(cbCustomRechnerDisplay,"wrap");
		textColorBtn = new JButton("Waehlen");	
		if(StringUtils.isNotEmpty(POSConstants.Waehlen))
			textColorBtn.setText(POSConstants.Waehlen);
		
		bckColorBtn = new JButton("Waehlen");
		if(StringUtils.isNotEmpty(POSConstants.Waehlen))
			bckColorBtn.setText(POSConstants.Waehlen);
		
		textColor = restaurant.getItemTexColor();
		btnBckColor = restaurant.getBackButtoColor();
		textColorBtn.setBackground(btnBckColor);
		bckColorBtn.setBackground(btnBckColor);
		textColorBtn.setForeground(textColor);
		bckColorBtn.setForeground(textColor);

		textColorBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				textColor = JColorChooser.showDialog(null, "Choose a color", restaurant.getItemTexColor());
				textColorBtn.setForeground(textColor);
				bckColorBtn.setForeground(textColor);
			}
		});

		bckColorBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				btnBckColor = JColorChooser.showDialog(null, "Choose a color", restaurant.getBackButtoColor());
				textColorBtn.setBackground(btnBckColor);
				bckColorBtn.setBackground(btnBckColor);
			}
		});
		
		if(StringUtils.isNotEmpty(POSConstants.Kein_karte_Bestaetigung))
			cbOnlyEcCard.setText(POSConstants.Kein_karte_Bestaetigung);
		
		cbOnlyEcCard.setBackground(new Color(209,222,235));
		displayPanel.add(cbOnlyEcCard,"growx");
		displayPanel.add(new JLabel("Item view Text Color"));
		displayPanel.add(textColorBtn, "growx");
		displayPanel.add(new JLabel("Item view Button Color"));
		displayPanel.add(bckColorBtn, "wrap");

		displayPanel.setBackground(new Color(209,222,235));
		homePanel.setBackground(new Color(209,222,235));
		orderPanel.setBackground(new Color(209,222,235));
		setBackground(new Color(209,222,235));
		getContentPane().setBackground(new Color(209,222,235));
		buttonPanel.add(cancelButton);

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.add("Haupt", homePanel);
		tabbedPane.add("Bestellung",orderPanel);
		tabbedPane.add("Selection",secPanel);
		tabbedPane.addTab("View", displayPanel);
		add(tabbedPane,"wrap");
		add(buttonPanel);

	}

	public void save() {
		Restaurant restaurant = RestaurantDAO.getRestaurant();
		restaurant.setPaynow(payNow.getText());
		restaurant.setPaylater(payLater.getText());
		restaurant.setDiverse(diverse.getText());
		restaurant.setSettle(settle.getText());
		restaurant.setReopen(reopen.getText());
		restaurant.setDeleteticket(deleteTicket.getText());
		restaurant.setAdditem(additem.getText());
		restaurant.setTicketpreview(ticketpreview.getText());
		restaurant.setOldticket(oldTicket.getText());
		restaurant.setDeleteitem(deleteitem.getText());
		restaurant.setCloseticket(closeTicket.getText());
		restaurant.setItemsonstigestext(itemSonstigesText.getText());
		restaurant.setFastPayment(cbFastPayment.isSelected());
		restaurant.setCopyRechnug(cbDuplicateRechnug.isSelected());
		restaurant.setWithWarengroup(cbtWarenAbs.isSelected());
		restaurant.setShowReturnDialog(cbShowReturnWindow.isSelected());
		restaurant.setBackButtoColor(btnBckColor);
		restaurant.setItemTexColor(textColor);		
		restaurant.setKellenerSalesReport(cbSalesKellner.isSelected());
		restaurant.setAutoSalesHour(cmbAutoSalesHour.getSelectedItem().toString());
		RestaurantDAO.getInstance().saveOrUpdate(restaurant);
		 
		
		TerminalConfig.setCalculator(cbCalculator.isSelected());
		TerminalConfig.setMonthReport(cbMonthReport.isSelected());
		TerminalConfig.setServerReport(cbServerReport.isSelected());
		TerminalConfig.setReservation(cbReservation.isSelected());
		//		TerminalConfig.setMainCustomerButton(cbMainCustomer.isSelected());
		TerminalConfig.setAutoTagesAbs(cbAutoSalesReport.isSelected());
		TerminalConfig.setPayTransfer(cbOnlinePayment.isSelected());
		TerminalConfig.setBonRoll(cbBonRoll.isSelected());
		TerminalConfig.setFinanzPrufung(cbFinanzPrufung.isSelected());
		TerminalConfig.setItemSorting(cbItemSorting.isSelected());
		TerminalConfig.setItemPriceSorting(cbItemPriceSorting.isSelected());
		TerminalConfig.setAutoRestartSystem(cbAutoRestartSystem.isSelected());
		TerminalConfig.setTakeItemCount(cbTakeItemCount.isSelected());
		TerminalConfig.setPriceCategory(cbPriceCategory.isSelected());
		TerminalConfig.setUpdatePriceCategory(cbPriceCategoryUpdate.isSelected());
		TerminalConfig.setWholeSale(cbWholeSale.isSelected());
		TerminalConfig.setOffer(cbOffer.isSelected());
		TerminalConfig.setRabattDirektEnable(cbRabattDirekt.isSelected());
		TerminalConfig.setPriceCategoryKunden(cbPriceCategoryKunden.isSelected());
		TerminalConfig.setPreviewEnable(cbPreview.isSelected());
		TerminalConfig.setRabattEnable(cbRabatt.isSelected());
		TerminalConfig.setCardEnable(cbKarte.isSelected());
		TerminalConfig.setOpenDrawer(cbOpendrawer.isSelected());
		TerminalConfig.setSonstiges(cbSonstiges.isSelected());
		TerminalConfig.setItemSearch(cbItemsearch.isSelected());
		TerminalConfig.setItemBarcode(cbItemBarcode.isSelected());
		TerminalConfig.setRabattDirectText(tfRabattDirect.getText());
		TerminalConfig.setOrders(cbOrders.isSelected());
		TerminalConfig.setSaldo(cbSaldo.isSelected());
		TerminalConfig.setUpdatedDesign(cbUpdated.isSelected());
		TerminalConfig.setHideItemId(cbHideItemId.isSelected());
		TerminalConfig.setHideItemPrice(cbHideItemPrice.isSelected());
		TerminalConfig.setQRcode(cbQrCode.isSelected());
		TerminalConfig.setAddRabattAt19(cbViceVersa.isSelected());
		TerminalConfig.setAddRabattAt7(cbViceVersa7.isSelected());
		TerminalConfig.setSpecial(cbSpecial.isSelected());
		TerminalConfig.setMultiUser(cbMultiUser.isSelected());
		TerminalConfig.setMultiUserProof(cbMultiUserVerify.isSelected());
		TerminalConfig.setPlaySound(cbPlayNotification.isSelected());
		TerminalConfig.setItemAdd(cbAddItemDirect.isSelected());
		TerminalConfig.setAlwaysOpenCashdrawer(cbCashDrawerAll.isSelected());
		TerminalConfig.setRemoteBackup(cbRemoteBackup.isSelected());
		TerminalConfig.setA4WithBackground(cbA4Background.isSelected());
		TerminalConfig.setA4WithBackgroundSP(cbA4BackgroundSP.isSelected());

		TerminalConfig.setOnlineSalesStart(cbOnlineSales.isSelected());
		TerminalConfig.setInventurAlert(cbInventurAlert.isSelected());
		TerminalConfig.setInventur(cbInventur.isSelected());
		TerminalConfig.setCustomer(cbCustomer.isSelected());
		TerminalConfig.setTicketReopen(cbReopen.isSelected());
		TerminalConfig.setTicketDelete(cbTicketDelete.isSelected());
		TerminalConfig.setTicketArchive(cbTicketArchive.isSelected());
		TerminalConfig.setNumberDisplayHeight(numberDisplayHeight.getText());
		TerminalConfig.setNumberDisplayWidth(numberDisplayWidth.getText());
		TerminalConfig.setCustomNumberDisplay(cbCustomRechnerDisplay.isSelected());
		TerminalConfig.setOnlyEC(cbOnlyEcCard.isSelected());
		TerminalConfig.setWageSonsName(WeightSonsName.getText());
		TerminalConfig.setWageSons(cbWeightSons.isSelected());
		TerminalConfig.setCashBookEnable(cbCashBook.isSelected());
		TerminalConfig.setCashbookText(cashBookText.getText());
		
		TerminalConfig.setDupTseBackendDispaly(dupTseBackendDispaly.isSelected());
		
		TerminalConfig.setNormalAuswalEnable(normalAuswalEnable.isSelected());
		
		TerminalConfig.setDetailedRecieptEnable(detailedRecieptEnable.isSelected());
		TerminalConfig.setTRINKGELD(trinkgeld.isSelected());
		TerminalConfig.setZahlungSteur(cbZahlungSteur.isSelected());
		TerminalConfig.setAllowStorno(cbKellnerStorno.isSelected());
		TerminalConfig.setRechGedruktDisplay(cbDrukDisplay.isSelected());
		TerminalConfig.setFooterMsgEnabled(cbDrukFootrMsgDisplay.isSelected());
		TerminalConfig.setGutInBalanceDialog(cbGutInBalance.isSelected());
		 
	}

	private void setSpeicial(boolean create) {
		if(create) {
			Tax imHausTax = TaxDAO.getInstance().findByName(POSConstants.DINE_IN);
			imHausTax.setRate(16.00);
			TaxDAO.getInstance().saveOrUpdate(imHausTax);
			Tax lieferTax = TaxDAO.getInstance().findByName(POSConstants.HOME_DELIVERY);
			lieferTax.setRate(5.00);
			TaxDAO.getInstance().saveOrUpdate(lieferTax);

		}
		if(!create) {
			Tax imHausTax = TaxDAO.getInstance().findByName(POSConstants.DINE_IN);
			imHausTax.setRate(19.00);
			TaxDAO.getInstance().saveOrUpdate(imHausTax);
			Tax lieferTax = TaxDAO.getInstance().findByName(POSConstants.HOME_DELIVERY);
			lieferTax.setRate(7.00);
			TaxDAO.getInstance().saveOrUpdate(lieferTax);
		}
		updateMenu();
	}
	private void updateMenu() {
		try {
			try {
				List<MenuItem> tempList;
				tempList = MenuItemDAO.getInstance().findAll();
				Session session = MenuItemDAO.getInstance().createNewSession();
				Transaction tx = session.beginTransaction();

				Tax dineIn = TaxDAO.getInstance().findByName(POSConstants.DINE_IN);
				Tax home = TaxDAO.getInstance().findByName(POSConstants.HOME_DELIVERY);
				Tax zero = TaxDAO.getInstance().findByName("ZERO");
				for (Iterator itr = tempList.iterator(); itr.hasNext();) {
					MenuItemForm editor;
					MenuItem item = (MenuItem) itr.next();
					if (item.getParent().getParent().getType().compareTo(POSConstants.DINE_IN) == 0) {
						item.setTax(dineIn);
					} else if (item.getParent().getParent().getType().compareTo(POSConstants.HOME_DELIVERY) == 0) {
						item.setTax(home);
					} else {
						item.setTax(zero);
					}
					session.saveOrUpdate(item);
				}

				tx.commit();
				session.close();				
			} catch (Throwable x) {
				BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
			}


		} catch (Throwable x) {
			BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE,
					x);
		}
	}

}


