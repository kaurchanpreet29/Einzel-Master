package com.floreantpos.config.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;
import org.jdesktop.swingx.JXDatePicker;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.swing.IntegerTextField;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.NumberUtil;

import net.miginfocom.swing.MigLayout;

public class TerminalConfigurationView extends ConfigurationView {

	private IntegerTextField tfTerminalNumber;
	private IntegerTextField tfSalesId;
	private IntegerTextField tfMonthSalesId;
	private IntegerTextField tfLoyaltyNr;
	private IntegerTextField tfSecretKeyLength;
	private IntegerTextField tfLimit;
	private JXDatePicker startDatePicker = new JXDatePicker();
	private JCheckBox cbFullscreenMode = new JCheckBox("Full Screen");
	private JCheckBox cbDailyReport = new JCheckBox("Kürzere Tagesbericht");
	private JCheckBox cbPlan1 = new JCheckBox("Tisch Plan 1");
	private JCheckBox cbPlan2 = new JCheckBox("Tisch Plan 2");
	private JCheckBox cbPrintLight = new JCheckBox("Feindruck");
	private JCheckBox cbDeliveryTime = new JCheckBox("Liefezeit");
	private JCheckBox cbShortReceipt = new JCheckBox("Kurze Rechnung");
	private JCheckBox cbTabVersion = new JCheckBox("Tab Version");
	private JCheckBox cbtype = new JCheckBox("IMBISS");
	private JCheckBox cbTabPrint = new JCheckBox("Tab Druck");
	private JCheckBox cbCashPaymentOnly = new JCheckBox("Nur Barzahlung");
	private JCheckBox cbMorePrint = new JCheckBox("Mehrere Ausdrucke");
	private JCheckBox cbAllTickets = new JCheckBox(" Bestellungen");
	private JCheckBox cbHideCurrency = new JCheckBox("EUR display Verstecken");
	private JCheckBox cbCommaPayment = new JCheckBox("Comma zahlen");
	private JCheckBox cbTicketWoId = new JCheckBox("Ticket o. Id");
	private JCheckBox cbDateOnly = new JCheckBox("Rechnung nur mit Datum");
	private JCheckBox cbHideDrawer = new JCheckBox("Schublade verstecken");
	private JCheckBox cbNoGui = new JCheckBox("NO GUI");
	//added
	private JCheckBox cbCardTerminalDeActivate = new JCheckBox("Disable Card Terminal");
	private JCheckBox cbRechnungNummerPrintEnable = new JCheckBox("Enable Rechnung Nummer");
	private JCheckBox cbBothInput = new JCheckBox("Beider Eingabe");
	private JButton createCsv = new JButton("Generate Menu CSV");
	private IntegerTextField tfSecondScreen;
	private JCheckBox cbKundenScreen = new JCheckBox("2nd Display Enable");
	private IntegerTextField tfSecondScreenWidth;
	private IntegerTextField tfSecondScreenHeight;
	private JTextField tfWaageCom;

	private JTextField ZVT_IP = new JTextField(10);
	private JTextField ZVT_PORT = new JTextField(10);
	private JCheckBox ZVT_ENABLE = new JCheckBox("Khna KT");
	private JTextField WEIGHT_IP = new JTextField(10);
	private JCheckBox WEIGHT_SERVER = new JCheckBox("Weight Se.");
	private JCheckBox WEIGHT_CLIENT = new JCheckBox("Weight Cl.");
	Restaurant rest;
	private JTextField tfAdimatCom = new JTextField(15);
	private JCheckBox cbAdimatCom = new JCheckBox("");
	private JComboBox<String> cbKeyType = new JComboBox<String>();

	//till here
	private JComboBox<String> cbFonts = new JComboBox<String>();
	private IntegerTextField tfBon = new IntegerTextField(20);
	private IntegerTextField tfButtonHeight;
	private IntegerTextField tfCategoryButtonHeight;
	private IntegerTextField tfCategoryButtonWidth;
	private IntegerTextField tfItemButtonHeight;
	private IntegerTextField tfFontSize;
	private IntegerTextField tfItemButtonWidth;
	private IntegerTextField tfItemButtonRed;
	private IntegerTextField tfItemButtonGreen;
	private IntegerTextField tfItemButtonBlue;
	private JTextField tfCom;

	private JTextField tfPfand1;
	private JTextField tfPfand2;
	private JTextField tfPfand3;
	private JTextField lastTseNr;

	private JTextField tfCashDrawerFile;
	private JTextField tfCashDrawerCom;
	private JTextField tfCardCommand;
	
	private JCheckBox cbAutoLogoff = new JCheckBox("Automatische Abmeldung");
	
	private JCheckBox cbIsdn = new JCheckBox("ISDN Dienstleistung");
	private JCheckBox cbDisplay = new JCheckBox("Kunden Display");
	private JCheckBox cbCashDrawer = new JCheckBox("Kassenschublade");
	private JCheckBox cbCashDrawerPrint = new JCheckBox("Kassenschublade mit Drucker");
	private JCheckBox cbKitchenPrint = new JCheckBox("Lieferungsdruck an Kueche");
	private JCheckBox cbLogo = new JCheckBox("Logo");
	private IntegerTextField tfLogoffTime = new IntegerTextField(4);
	public TerminalConfigurationView() {
		super();
		initComponents();
	}

	private void initComponents() {
		setLayout(new MigLayout()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		setBackground(new Color(209,222,235));
		JPanel configPanel = new JPanel();
		configPanel.setLayout(new MigLayout());
		configPanel.setBackground(new Color(209,222,235));
		setLayout(new MigLayout());
		rest = RestaurantDAO.getRestaurant();
		JLabel lblTerminalNumber = new JLabel(Messages.getString("TerminalConfigurationView.TERMINAL_NUMBER")); //$NON-NLS-1$
		configPanel.add(lblTerminalNumber); //$NON-NLS-1$
		tfTerminalNumber = new IntegerTextField();
		tfTerminalNumber.setColumns(10);
		configPanel.add(tfTerminalNumber, "wrap"); //$NON-NLS-1$

		JLabel lblSalesId = new JLabel(); //$NON-NLS-1$
		
		if(StringUtils.isNotEmpty(POSConstants.Tages_Abschluss_id))
			lblSalesId.setText(POSConstants.Tages_Abschluss_id);
		else
			lblSalesId.setText("Tages Abschluss Id");
		
		configPanel.add(lblSalesId); //$NON-NLS-1$
		tfSalesId = new IntegerTextField();
		tfSalesId.setEditable(false);

		tfSalesId.setColumns(10);
		configPanel.add(tfSalesId, "wrap"); //$NON-NLS-1$

		JLabel lblMonthSalesId = new JLabel(); //$NON-NLS-1$
		
		if(StringUtils.isNotEmpty(POSConstants.Monatabschluss_id))
			lblMonthSalesId.setText(POSConstants.Monatabschluss_id);
		else
			lblMonthSalesId.setText("Monatabschluss Id");
		
		configPanel.add(lblMonthSalesId); //$NON-NLS-1$
		tfMonthSalesId = new IntegerTextField();
		tfMonthSalesId.setEditable(false);
		if(Application.getCurrentUser().getFirstName().compareTo("Master")==0||Application.getCurrentUser().getFirstName().compareTo("Super-User")==0) {
			tfSalesId.setEditable(true);
			tfMonthSalesId.setEditable(true);
		}

		tfMonthSalesId.setColumns(10);
		configPanel.add(tfMonthSalesId); //$NON-NLS-1$
		JLabel lblLoyaltyNo = new JLabel(); //$NON-NLS-1$
		
		if(StringUtils.isNotEmpty(POSConstants.Startpunkt_Kundennr))
			lblLoyaltyNo.setText(POSConstants.Startpunkt_Kundennr);
		else
			lblLoyaltyNo.setText("Startpunkt Kundennr.");
		
		if(StringUtils.isNotEmpty(POSConstants.Monatabschluss_id))
			lblLoyaltyNo.setText(POSConstants.Monatabschluss_id);
		else
			lblLoyaltyNo.setText("Monatabschluss Id");
		
		configPanel.add(lblLoyaltyNo); //$NON-NLS-1$
		tfLoyaltyNr = new IntegerTextField();
		tfLoyaltyNr.setColumns(10);
		configPanel.add(tfLoyaltyNr); //$NON-NLS-1$

		JLabel lblLimit = new JLabel(); //$NON-NLS-1$
		if(StringUtils.isNotEmpty(POSConstants.Maximale_Bestellung))
			lblLimit.setText(POSConstants.Maximale_Bestellung);
		else
			lblLimit.setText("Maximale Bestellung (€)");
		
		configPanel.add(lblLimit); //$NON-NLS-1$
		tfLimit = new IntegerTextField();
		tfLimit.setColumns(10);
		configPanel.add(tfLimit,"wrap"); //$NON-NLS-1$

		if(StringUtils.isNotEmpty(POSConstants.Passwortlaenge))
			configPanel.add(new JLabel(POSConstants.Passwortlaenge));
		else
			configPanel.add(new JLabel("Standart fuer Passwortlaenge"));
		
		
		tfSecretKeyLength = new IntegerTextField(3);
		configPanel.add(tfSecretKeyLength, "wrap");

		if(StringUtils.isNotEmpty(POSConstants.Automatische_Abmeldung))
			cbAutoLogoff.setText(POSConstants.Automatische_Abmeldung);
		
		cbAutoLogoff.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(cbAutoLogoff.isSelected()) {
					tfLogoffTime.setEnabled(true);
				} 
				else {
					tfLogoffTime.setEnabled(false);
				}
			}
		});
		configPanel.add(cbAutoLogoff);
		cbAutoLogoff.setBackground(new Color(209,222,235));
		
		if(StringUtils.isNotEmpty(POSConstants.Rechnung_nur_mit_Datum))
			cbDateOnly.setText(POSConstants.Rechnung_nur_mit_Datum);
		
		cbDateOnly.setBackground(new Color(209,222,235));
		
		if(StringUtils.isNotEmpty(POSConstants.EUR_display_Verstecken))
			cbHideDrawer.setText(POSConstants.EUR_display_Verstecken);
		
		cbHideDrawer.setBackground(new Color(209,222,235));
		cbNoGui.setBackground(new Color(209,222,235));
		if(StringUtils.isNotEmpty(POSConstants.ISDN_Dienstleistung))
			cbIsdn.setText(POSConstants.ISDN_Dienstleistung);
		
		cbIsdn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(cbIsdn.isSelected()) {
					TerminalConfig.setIsdn(true);
				} 
				else {
					TerminalConfig.setIsdn(false);
				}
			}
		});

		cbIsdn.setBackground(new Color(209,222,235));

		cbLogo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(cbLogo.isSelected()) {
					cbLogo.setEnabled(true);
				} 
				else {
					cbLogo.setEnabled(false);
				}
			}
		});

		tfSecondScreen = new IntegerTextField();
		tfSecondScreen.setEditable(true);
		tfSecondScreen.setColumns(2);
		tfSecondScreen.setText(TerminalConfig.getSDNummer());

		tfSecondScreenWidth = new IntegerTextField(5);
		tfSecondScreenWidth.setEditable(true);
		tfSecondScreenWidth.setColumns(2);
		tfSecondScreenWidth.setText(TerminalConfig.getKundenScreenWidth());

		tfSecondScreenHeight = new IntegerTextField(5);
		tfSecondScreenHeight.setEditable(true);
		tfSecondScreenHeight.setColumns(2);
		tfSecondScreenHeight.setText(TerminalConfig.getKundenScreenHeight());
		cbKundenScreen.setBackground(new Color(209,222,235));


		configPanel.add(new JLabel("Second screen"));// $NON-NLS-1$
		configPanel.add(tfSecondScreen,"growx, wrap"); 


		configPanel.add(cbAutoLogoff);
		
		if(StringUtils.isNotEmpty(POSConstants.Kassenschublade))
			cbCashDrawer.setText(POSConstants.Kassenschublade);
		
		cbCashDrawer.setBackground(new Color(209,222,235));
		configPanel.add(cbCashDrawer);
		
		if(StringUtils.isNotEmpty(POSConstants.Kassenschublade_mit_Drucker))
			cbCashDrawerPrint.setText(POSConstants.Kassenschublade_mit_Drucker);
		
		if(StringUtils.isNotEmpty(POSConstants.Lieferungsdruck_an_Kueche))
			cbKitchenPrint.setText(POSConstants.Lieferungsdruck_an_Kueche);
		
		cbCashDrawerPrint.setBackground(new Color(209,222,235));
		configPanel.add(cbCashDrawerPrint);
		if(StringUtils.isNotEmpty(POSConstants.Auto_Logout_Time))
			cbCashDrawer.setText(POSConstants.Auto_Logout_Time);
		else
		    configPanel.add(new JLabel("Zeit bis Auto-Abmeldung(Min)")); //$NON-NLS-1$
		
		configPanel.add(tfLogoffTime,"wrap");

		configPanel.add(cbFullscreenMode); //$NON-NLS-1$
		cbFullscreenMode.setBackground(new Color(209,222,235));

		if(StringUtils.isNotEmpty(POSConstants.Kurzere_Tagesbericht))
			cbDailyReport.setText(POSConstants.Kurzere_Tagesbericht);
		
		configPanel.add(cbDailyReport); //$NON-NLS-1$
		cbDailyReport.setBackground(new Color(209,222,235));

		if(StringUtils.isNotEmpty(POSConstants.Feindruck))
			cbPrintLight.setText(POSConstants.Feindruck);
		
		if(StringUtils.isNotEmpty(POSConstants.Liefezeit))
			cbDeliveryTime.setText(POSConstants.Liefezeit);
		
		if(StringUtils.isNotEmpty(POSConstants.Kurze_Rechnung))
			cbShortReceipt.setText(POSConstants.Kurze_Rechnung);
		
		configPanel.add(cbShortReceipt); //$NON-NLS-1$
		cbShortReceipt.setBackground(new Color(209,222,235));

		configPanel.add(cbTabVersion); //$NON-NLS-1$
		cbTabVersion.setBackground(new Color(209,222,235));

		if(StringUtils.isNotEmpty(POSConstants.Tab_Druck))
			cbTabPrint.setText(POSConstants.Tab_Druck);
		
		configPanel.add(cbLogo); //$NON-NLS-1$
		cbLogo.setBackground(new Color(209,222,235));

		if(StringUtils.isNotEmpty(POSConstants.Nur_Barzahlung))
			cbCashPaymentOnly.setText(POSConstants.Nur_Barzahlung);
		
		if(StringUtils.isNotEmpty(POSConstants.Mehrere_Ausdrucke))
			cbMorePrint.setText(POSConstants.Mehrere_Ausdrucke);
		
		configPanel.add(cbMorePrint, "wrap"); //$NON-NLS-1$
		cbMorePrint.setBackground(new Color(209,222,235));

		if(StringUtils.isNotEmpty(POSConstants.Kunden_Display))
			cbDisplay.setText(POSConstants.Kunden_Display);
		
		cbDisplay.setBackground(new Color(209,222,235));
		cbDisplay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(cbDisplay.isSelected()) {
					TerminalConfig.setDisplay(true);
				} 
				else {
					TerminalConfig.setDisplay(false);
				}
			}
		});
		configPanel.add(cbDisplay);

		if(StringUtils.isNotEmpty(POSConstants.Bestellen))
			cbAllTickets.setText(POSConstants.Bestellen);
		
		configPanel.add(cbAllTickets); //$NON-NLS-1$
		cbAllTickets.setBackground(new Color(209,222,235));

		configPanel.add(cbHideCurrency); //$NON-NLS-1$
		cbHideCurrency.setBackground(new Color(209,222,235));

		if(StringUtils.isNotEmpty(POSConstants.Comma_zahlen))
			cbCommaPayment.setText(POSConstants.Comma_zahlen);
		
		configPanel.add(cbCommaPayment); //$NON-NLS-1$
		cbCommaPayment.setBackground(new Color(209,222,235));

		configPanel.add(cbDateOnly, "wrap"); //$NON-NLS-1$

		if(StringUtils.isNotEmpty(POSConstants.Beider_Eingabe))
			cbBothInput.setText(POSConstants.Beider_Eingabe);
		
		cbBothInput.setBackground(new Color(209,222,235));		
		configPanel.add(cbHideDrawer); //$NON-NLS-1$
		configPanel.add(cbBothInput); //$NON-NLS-1$

		configPanel.add(new JLabel("2nd Screen Width"));
		configPanel.add(tfSecondScreenWidth);
		configPanel.add(new JLabel("2nd Screen height"));
		configPanel.add(tfSecondScreenHeight, "wrap");
		cbHideDrawer.setBackground(new Color(209,222,235));


		cbRechnungNummerPrintEnable.setBackground(new Color(209, 222, 235));
		configPanel.add(cbRechnungNummerPrintEnable,"growx");

		configPanel.add(cbNoGui, "growx"); //$NON-NLS-1$
		cbNoGui.setBackground(new Color(209,222,235));

		configPanel.add(new JLabel("Second screen Nr."));// $NON-NLS-1$
		configPanel.add(tfSecondScreen,"growx");
		configPanel.add(cbKundenScreen,"growx, wrap");

		tfCashDrawerFile = new JTextField(20);
		configPanel.add(new JLabel("Schublade File"));
		configPanel.add(tfCashDrawerFile); //$NON-NLS-1$

		tfCashDrawerCom = new JTextField(20);
		configPanel.add(new JLabel("Schublade Com"));
		configPanel.add(tfCashDrawerCom,"wrap"); //$NON-NLS-1$




		tfCardCommand = new JTextField(20);
		configPanel.add(new JLabel("ZVT CARD CMD"));
		configPanel.add(tfCardCommand,"growx"); //$NON-NLS-1$

		configPanel.add(cbCardTerminalDeActivate, "growx"); // $NON-NLS-1$
		cbCardTerminalDeActivate.setBackground(new Color(209, 222, 235));

		configPanel.add(new JLabel("Schlußel COM PORT"));
		cbAdimatCom.setBackground(new Color(209, 222, 235));
		configPanel.add(tfAdimatCom, "growx"); // $NON-NLS-1$


		cbKeyType.addItem("PRO-IBUTTON");
		cbKeyType.addItem("USB-ADDIMAT");
		cbKeyType.addItem("NCR-INCUTTON");
		if(TerminalConfig.getKeyType().toUpperCase().compareTo("IBUTTON")==0)
			cbKeyType.setSelectedIndex(0);
		else if(TerminalConfig.getKeyType().toUpperCase().compareTo("ADDIMAT")==0)
			cbKeyType.setSelectedIndex(1);
		else if(TerminalConfig.getKeyType().toUpperCase().compareTo("NCRIBUTTON")==0)
			cbKeyType.setSelectedIndex(2);			configPanel.add(cbAdimatCom, "growx"); // $NON-NLS-1$
			configPanel.add(cbKeyType, "wrap"); // $NON-NLS-1$

			configPanel.add(new JLabel("KHNA ZVT IP"));
			configPanel.add(ZVT_IP,"growx");			

			configPanel.add(new JLabel("KHNA ZVT PORT"));// $NON-NLS-1$
			configPanel.add(ZVT_PORT,"growx");  

			configPanel.add(ZVT_ENABLE, "growx, wrap"); // $NON-NLS-1$
			ZVT_ENABLE.setBackground(new Color(209, 222, 235));

			configPanel.add(new JLabel("Weight S. IP"));
			configPanel.add(WEIGHT_IP,"growx");			

			configPanel.add(WEIGHT_SERVER,"growx");  
			WEIGHT_SERVER.setBackground(new Color(209, 222, 235));

			configPanel.add(WEIGHT_CLIENT, "growx, wrap"); // $NON-NLS-1$
			WEIGHT_CLIENT.setBackground(new Color(209, 222, 235));		

			if(StringUtils.isNotEmpty(POSConstants.Mehrer_ausdrucke))
				configPanel.add(new JLabel(POSConstants.Mehrer_ausdrucke));
			else
				configPanel.add(new JLabel("Mehrer ausdrucke"));
			
			configPanel.add(tfBon,"growx"); //$NON-NLS-1$

			tfWaageCom = new JTextField(20);
			tfWaageCom.setText(TerminalConfig.getComWeight());
			
			if(StringUtils.isNotEmpty(POSConstants.Waage_COM_PORT))
				configPanel.add(new JLabel(POSConstants.Waage_COM_PORT));
			else
				configPanel.add(new JLabel("Waage COM PORT"));
			
			configPanel.add(tfWaageCom,"growx, wrap"); //$NON-NLS-1$

			tfCom = new JTextField();
			configPanel.add(new JLabel());
			
			if(StringUtils.isNotEmpty(POSConstants.Kunden_Display_COM_PORT))
				configPanel.add(new JLabel(POSConstants.Kunden_Display_COM_PORT));
			else
				configPanel.add(new JLabel("Kunden Display COM PORT"));
			
			if(StringUtils.isNotEmpty(POSConstants.Waage_COM_PORT))
				configPanel.add(new JLabel(POSConstants.Waage_COM_PORT));
			else
				configPanel.add(new JLabel("Waage COM PORT"));
			
			configPanel.add(tfCom,"growx"); //$NON-NLS-1$

			cbFonts.setBackground(Color.WHITE);
			add(configPanel,"growx,wrap");

			tfPfand1 = new JTextField();
			configPanel.add(new JLabel());
			
			if(StringUtils.isNotEmpty(POSConstants.Aus_Zahlung1))
				configPanel.add(new JLabel(POSConstants.Aus_Zahlung1));
			else
				configPanel.add(new JLabel("Aus-Zahlung 1"));
			
			configPanel.add(tfPfand1,"growx, wrap"); //$NON-NLS-1$

			tfPfand2 = new JTextField();
			 
			
			if(StringUtils.isNotEmpty(POSConstants.Aus_Zahlung2))
				configPanel.add(new JLabel(POSConstants.Aus_Zahlung2));
			else
				configPanel.add(new JLabel("Aus-Zahlung 2"));
			
			configPanel.add(tfPfand2,"growx"); //$NON-NLS-1$

			tfPfand3 = new JTextField();
			if(StringUtils.isNotEmpty(POSConstants.Aus_Zahlung3))
				configPanel.add(new JLabel(POSConstants.Aus_Zahlung3));
			else
				configPanel.add(new JLabel("Aus-Zahlung 3"));
			
			configPanel.add(tfPfand3,"growx, wrap"); //$NON-NLS-1$

			if(StringUtils.isNotEmpty(POSConstants.Start_Datum))
				configPanel.add(new JLabel(POSConstants.Start_Datum));
			else
				configPanel.add(new JLabel("Start Datum"));
			
			configPanel.add(startDatePicker,"growx");

			configPanel.add(new JLabel("Nr.: "));	 
			lastTseNr = new JTextField(20);
			lastTseNr.setSize(2, 2);
			lastTseNr.setText(TerminalConfig.getLastTseNr());		 
			configPanel.add(lastTseNr,"growx, wrap");

			JPanel panel = new JPanel();
			panel.setBackground(new Color(209,222,235));
			panel.setLayout(new MigLayout());

			createCsv.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					try {
						GdpduMenuItem();
					}catch(Exception ex) {

					}
				}
			});

			panel.add(createCsv);

			if(StringUtils.isNotEmpty(POSConstants.Default_Schrift))
				panel.add(new JLabel(POSConstants.Default_Schrift));
			else
				panel.add(new JLabel("Default Schrift")); //$NON-NLS-1$
			
			panel.add(cbFonts, "growx, wrap"); //$NON-NLS-1$
			add(panel,"wrap");
			JPanel touchConfigPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
			touchConfigPanel.setBackground(new Color(209,222,235));
			touchConfigPanel.setLayout(new MigLayout());
			//touchConfigPanel.setBorder(BorderFactory.createTitledBorder("TOUCH SCREEN EINSTELLUNGEN"));
			
			if(StringUtils.isNotEmpty(POSConstants.TOUCH_SCREEN_EINSTELLUNGEN))
				touchConfigPanel.setBorder(BorderFactory.createTitledBorder(POSConstants.TOUCH_SCREEN_EINSTELLUNGEN));
			else
				touchConfigPanel.setBorder(BorderFactory.createTitledBorder("TOUCH SCREEN EINSTELLUNGEN"));
			
			if(StringUtils.isNotEmpty(POSConstants.Buttonhoehe))
				touchConfigPanel.add(new JLabel(POSConstants.Buttonhoehe));
			else
				touchConfigPanel.add(new JLabel("Buttonhoehe"));
			
			tfButtonHeight = new IntegerTextField(5);
			touchConfigPanel.add(tfButtonHeight);
			
			
			if(StringUtils.isNotEmpty(POSConstants.Kategorie_Buttonhoehe))
				touchConfigPanel.add(new JLabel(POSConstants.Kategorie_Buttonhoehe));
			else
				touchConfigPanel.add(new JLabel("Kategorie Buttonhoehe"));
			
			tfCategoryButtonHeight = new IntegerTextField(5);
			touchConfigPanel.add(tfCategoryButtonHeight);			
			if(StringUtils.isNotEmpty(POSConstants.Kategorie_ButtonBreit))
				touchConfigPanel.add(new JLabel(POSConstants.Kategorie_ButtonBreit));
			else
				touchConfigPanel.add(new JLabel("Kategorie ButtonBreit"));
			
			tfCategoryButtonWidth = new IntegerTextField(5);
			touchConfigPanel.add(tfCategoryButtonWidth);
			if(StringUtils.isNotEmpty(POSConstants.Schriftgroesse))
				touchConfigPanel.add(new JLabel(POSConstants.Schriftgroesse));
			else
				touchConfigPanel.add(new JLabel("Schriftgroesse fuer Button"));
			
			tfFontSize = new IntegerTextField(5);
			touchConfigPanel.add(tfFontSize, "wrap");			
			if(StringUtils.isNotEmpty(POSConstants.Hoehe_des_Artikelbuttons))
				touchConfigPanel.add(new JLabel(POSConstants.Hoehe_des_Artikelbuttons));
			else
				touchConfigPanel.add(new JLabel("Hoehe des Artikelbuttons"));
			
			tfItemButtonHeight = new IntegerTextField(5);
			touchConfigPanel.add(tfItemButtonHeight);

			
			if(StringUtils.isNotEmpty(POSConstants.Breite_des_Artikelbuttons))
				touchConfigPanel.add(new JLabel(POSConstants.Breite_des_Artikelbuttons));
			else
				touchConfigPanel.add(new JLabel("Breite des Artikelbuttons"));
			
			tfItemButtonWidth = new IntegerTextField(5);
			touchConfigPanel.add(tfItemButtonWidth);
            if(StringUtils.isNotEmpty(POSConstants.Rot_Artikel))
				touchConfigPanel.add(new JLabel(POSConstants.Rot_Artikel));
			else
				touchConfigPanel.add(new JLabel("Rot Artikel"));
			
			tfItemButtonRed = new IntegerTextField(5);
			touchConfigPanel.add(tfItemButtonRed);
			if(StringUtils.isNotEmpty(POSConstants.Gruene_Artikel))
				touchConfigPanel.add(new JLabel(POSConstants.Gruene_Artikel));
			else
				touchConfigPanel.add(new JLabel("Gruene Artikel"));
			
			tfItemButtonGreen = new IntegerTextField(5);
			touchConfigPanel.add(tfItemButtonGreen);

			touchConfigPanel.add(new JLabel("Blaue Artikel"));
			if(StringUtils.isNotEmpty(POSConstants.Blaue_Artikel))
				touchConfigPanel.add(new JLabel(POSConstants.Blaue_Artikel));
			else
				touchConfigPanel.add(new JLabel("Blaue Artikel"));
			
			tfItemButtonBlue = new IntegerTextField(5);
			touchConfigPanel.add(tfItemButtonBlue);


			add(touchConfigPanel, "span 4, grow, wrap");

	}

	public boolean GdpduMenuItem() throws IOException {

		String current = new java.io.File( "." ).getCanonicalPath();
		String dir = current+"/Design";
		File directory = new File(dir);

		if(!directory.exists()){
			directory.mkdir();
		}
		FileWriter writer = new FileWriter(dir + "/Speisekarte.csv");		  
		writer.append("Name");
		writer.append(';');
		writer.append("Preis");
		writer.append(';');
		writer.append("Barcode");
		writer.append(';');		    
		writer.append('\n');

		List<MenuItem> menuitems = MenuItemDAO.getInstance().findAll();
		for (MenuItem item : menuitems) {
			writer.append(item.getName());
			writer.append(';');
			writer.append(NumberUtil.formatNumber(item.getPrice()));
			writer.append(';');
			if(item.getBarcode()!=null) {
				writer.append(item.getBarcode().trim());
			}else {
				writer.append("");
			}		      
			writer.append(';');		     
			writer.append('\n');
		}
		writer.flush();
		writer.close();
		return true;
	}


	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new TerminalConfigurationView());
		frame.setSize(500, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public boolean canSave() {
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean save() {
		int terminalNumber = 0;
		int buttonHeight = tfButtonHeight.getInteger();
		int buttonCategoryHeight = Integer.parseInt(tfCategoryButtonHeight.getText());
		int buttonCategorywidth = Integer.parseInt(tfCategoryButtonWidth.getText());

		int fontSize = tfFontSize.getInteger();
		int itemButtonHeight = tfItemButtonHeight.getInteger();
		int itemButtonWidth = tfItemButtonWidth.getInteger();

		if(itemButtonWidth > 135)
			itemButtonWidth = 135;
		int red = tfItemButtonRed.getInteger();
		int green = tfItemButtonGreen.getInteger();
		int blue = tfItemButtonBlue.getInteger();

		if(red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255)
		{
			red = 144;
			green = 233;
			blue = 162;
		}
		if(buttonHeight < 20) {
			POSMessageDialog.showError(BackOfficeWindow.getInstance(), "Bitte stellen Sie sicher Knopfgröße mindestens 20");
			return false;
		}

		if(fontSize < 8) {
			POSMessageDialog.showError(BackOfficeWindow.getInstance(), "Bitte stellen Sie sicher Schaltfläche Schriftgröße mindestens 8");
			return false;
		}

		try {
			terminalNumber = Integer.parseInt(tfTerminalNumber.getText());
		} catch(Exception x) {
			POSMessageDialog.showError(Messages.getString("TerminalConfigurationView.14")); //$NON-NLS-1$
			return false;
		}

		int defaultPassLen = tfSecretKeyLength.getInteger();
		if(defaultPassLen == 0) defaultPassLen = 4;

		TerminalConfig.setTerminalId(terminalNumber);
		try {
			rest.setSalesid(Integer.parseInt(tfSalesId.getText()));
			RestaurantDAO.getInstance().saveOrUpdate(rest);
		}catch(Exception ex) {
			TerminalConfig.setSalesId(tfSalesId.getText());
		}

		TerminalConfig.setLoyaltyNo(tfLoyaltyNr.getText());
		TerminalConfig.setLimit(tfLimit.getText());
		TerminalConfig.setDefaultPassLen(defaultPassLen);
		TerminalConfig.setFullscreenMode(cbFullscreenMode.isSelected());
		TerminalConfig.setDailyReport(cbDailyReport.isSelected());
		TerminalConfig.setPlan1(cbPlan1.isSelected());
		TerminalConfig.setPlan2(cbPlan2.isSelected());
		TerminalConfig.setPrintLight(cbPrintLight.isSelected());
		TerminalConfig.setDeliveryTime(cbDeliveryTime.isSelected());
		TerminalConfig.setShortReceipt(cbShortReceipt.isSelected());
		TerminalConfig.setTabVersion(cbTabVersion.isSelected());
		TerminalConfig.setType(cbtype.isSelected());
		TerminalConfig.setDisplay(cbDisplay.isSelected());
		TerminalConfig.setAllTickets(cbAllTickets.isSelected());
		TerminalConfig.setHideCurrency(cbHideCurrency.isSelected());
		TerminalConfig.setCommaPayment(cbCommaPayment.isSelected());
		TerminalConfig.setTicketOId(cbTicketWoId.isSelected());
		TerminalConfig.setCom(tfCom.getText());
		TerminalConfig.setPfand1(tfPfand1.getText());
		TerminalConfig.setPfand2(tfPfand2.getText());
		TerminalConfig.setPfand3(tfPfand3.getText());
		TerminalConfig.setLastTseNr(lastTseNr.getText());
		
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		TerminalConfig.setStartDate(df.format(startDatePicker.getDate()));
		TerminalConfig.setCashDrawerFile(tfCashDrawerFile.getText());
		TerminalConfig.setCashDrawerCom(tfCashDrawerCom.getText());
		TerminalConfig.setZvtCardPayment(tfCardCommand.getText());
		TerminalConfig.setBonNr(tfBon.getText());
		TerminalConfig.setCategoryHeight(buttonCategoryHeight+"");
		TerminalConfig.setCategoryWidth(buttonCategorywidth+"");
		TerminalConfig.setTouchScreenButtonHeight(buttonHeight);
		TerminalConfig.setTouchScreenFontSize(fontSize);
		TerminalConfig.setTouchScreenItemButtonHeight(itemButtonHeight);
		TerminalConfig.setItemRed(red);
		TerminalConfig.setItemGreen(green);
		TerminalConfig.setItemBlue(blue);
		TerminalConfig.setTouchScreenItemButtonWidth(itemButtonWidth);
		TerminalConfig.setIsdn(cbIsdn.isSelected());
		TerminalConfig.setLogo(cbLogo.isSelected());
		TerminalConfig.setAutoLogoffEnable(cbAutoLogoff.isSelected());
		TerminalConfig.setAutoLogoffTime(tfLogoffTime.getInteger() <= 0 ? 10 : tfLogoffTime.getInteger());
		TerminalConfig.setKitchenPrint(cbKitchenPrint.isSelected());
		TerminalConfig.setTabPrint(cbTabPrint.isSelected());
		TerminalConfig.setCashDrawer(cbCashDrawer.isSelected());
		TerminalConfig.setCashDrawerPrint(cbCashDrawerPrint.isSelected());
		TerminalConfig.setCashPaymentOnly(cbCashPaymentOnly.isSelected());
		TerminalConfig.setMultipleBon(cbMorePrint.isSelected());
		TerminalConfig.setDateOnly(cbDateOnly.isSelected());
		TerminalConfig.setHideDrawer(cbHideDrawer.isSelected());
		TerminalConfig.setNoGUI(cbNoGui.isSelected());
		//added
		TerminalConfig.setAdimatCom(tfAdimatCom.getText());
		TerminalConfig.setAdimatComEnable(cbAdimatCom.isSelected());
		if(cbKeyType.getSelectedIndex()==0)
			TerminalConfig.setKeyType("ibutton");
		else if(cbKeyType.getSelectedIndex()==1)
			TerminalConfig.setKeyType("addimat");
		else if(cbKeyType.getSelectedIndex()==2)
			TerminalConfig.setKeyType("ncributton");
		TerminalConfig.setBothInput(cbBothInput.isSelected());
		TerminalConfig.setTerminalDisable(cbCardTerminalDeActivate.isSelected());
		TerminalConfig.setRechnungNummerPrintEnable(cbRechnungNummerPrintEnable.isSelected());
		TerminalConfig.setKundenScreen(cbKundenScreen.isSelected());
		TerminalConfig.setSDNummer(tfSecondScreen.getText());
		TerminalConfig.setKundenScreenWidth(tfSecondScreenWidth.getText());
		TerminalConfig.setKundenScreenHeight(tfSecondScreenHeight.getText());
		TerminalConfig.setComWeight(tfWaageCom.getText());
		TerminalConfig.setMonthSalesId(tfMonthSalesId.getText());
		TerminalConfig.setKhanaZvtEnable(ZVT_ENABLE.isSelected());
		TerminalConfig.setKhanaCardIp(ZVT_IP.getText());
		TerminalConfig.setKhanaCardPort(ZVT_PORT.getText());

		TerminalConfig.seteWeightClient(WEIGHT_CLIENT.isSelected());
		TerminalConfig.setWeightServerIp(WEIGHT_IP.getText());
		TerminalConfig.seteWeightServer(WEIGHT_SERVER.isSelected());
		//till here

		POSMessageDialog.showMessage(BackOfficeWindow.getInstance(), "Bitte wieder starten Sie das System");

		String selectedFont = (String) cbFonts.getSelectedItem();
		if("<select>".equals(selectedFont)) {
			selectedFont = null;
		}

		TerminalConfig.setUiDefaultFont(selectedFont);

		return true;
	}

	@Override
	public void initialize() throws Exception {

		tfTerminalNumber.setText(String.valueOf(TerminalConfig.getTerminalId()));
		tfSalesId.setText(rest.getSalesid()!=null?rest.getSalesid()+"":String.valueOf(TerminalConfig.getSalesId()));
		tfMonthSalesId.setText(String.valueOf(TerminalConfig.getMonthSalesId()));
		tfLoyaltyNr.setText(String.valueOf(TerminalConfig.getLoyaltyNo()));
		tfLimit.setText(String.valueOf(TerminalConfig.getLimit()));
		tfSecretKeyLength.setText(String.valueOf(TerminalConfig.getDefaultPassLen()));
		cbFullscreenMode.setSelected(TerminalConfig.isFullscreenMode());
		cbDailyReport.setSelected(TerminalConfig.isDailyReport());
		cbPlan1.setSelected(TerminalConfig.isPlan1());
		cbPlan2.setSelected(TerminalConfig.isPlan2());
		cbPrintLight.setSelected(TerminalConfig.isPrintLight());
		cbDeliveryTime.setSelected(TerminalConfig.isDeliveryTime());
		cbShortReceipt.setSelected(TerminalConfig.isShortReceipt());
		cbTabVersion.setSelected(TerminalConfig.isTabVersion());
		cbtype.setSelected(TerminalConfig.istype());
		cbDisplay.setSelected(TerminalConfig.isDisplay());
		cbAllTickets.setSelected(TerminalConfig.isAllTickets());
		cbHideCurrency.setSelected(TerminalConfig.isHideCurrency());
		cbCommaPayment.setSelected(TerminalConfig.isCommaPayment());
		cbTicketWoId.setSelected(TerminalConfig.isTicketOId());
		tfCom.setText(TerminalConfig.getCom());
		tfPfand1.setText(TerminalConfig.getPfand1());
		tfPfand2.setText(TerminalConfig.getPfand2());
		tfPfand3.setText(TerminalConfig.getPfand3());
		lastTseNr.setText(TerminalConfig.getLastTseNr());
		tfCashDrawerFile.setText(TerminalConfig.getCashDrawerFile());
		tfCashDrawerCom.setText(TerminalConfig.getCashDrawerCom());
		tfCardCommand.setText(TerminalConfig.getZvtCardPayment());
		tfButtonHeight.setText("" + TerminalConfig.getTouchScreenButtonHeight());
		tfCategoryButtonHeight.setText("" + TerminalConfig.getCategoryHeight());
		tfCategoryButtonWidth.setText("" + TerminalConfig.getCategoryWidth());
		tfFontSize.setText("" + TerminalConfig.getTouchScreenFontSize());
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		startDatePicker.setDate(df.parse(TerminalConfig.getStartDate()));
		tfItemButtonHeight.setText("" + TerminalConfig.getTouchScreenItemButtonHeight());
		tfItemButtonRed.setText("" + TerminalConfig.getItemRed());
		tfItemButtonGreen.setText("" + TerminalConfig.getItemGreen());
		tfItemButtonBlue.setText("" + TerminalConfig.getItemBlue());
		tfItemButtonWidth.setText("" + TerminalConfig.getTouchScreenItemButtonWidth());
		cbAutoLogoff.setSelected(TerminalConfig.isAutoLogoffEnable());
		cbIsdn.setSelected(TerminalConfig.isIsdnEnabled());
		cbLogo.setSelected(TerminalConfig.isLogoEnabled());
		cbTabPrint.setSelected(TerminalConfig.isTabPrint());
		cbCashDrawer.setSelected(TerminalConfig.isCashDrawer());
		cbCashDrawerPrint.setSelected(TerminalConfig.isCashDrawerPrint());
		cbCashPaymentOnly.setSelected(TerminalConfig.isCashPaymentOnly());
		cbMorePrint.setSelected(TerminalConfig.isMultipleBon());
		cbDateOnly.setSelected(TerminalConfig.isDateOnly());
		cbHideDrawer.setSelected(TerminalConfig.isHideDrawer());
		cbBothInput.setSelected(TerminalConfig.isBothInput());

		cbNoGui.setSelected(TerminalConfig.isNoGUI());
		cbKundenScreen.setSelected(TerminalConfig.isKundenScreen());

		tfLogoffTime.setText("" + TerminalConfig.getAutoLogoffTime());
		tfLogoffTime.setEnabled(cbAutoLogoff.isSelected());
		tfBon.setText(TerminalConfig.getBonNr());
		cbKitchenPrint.setSelected(TerminalConfig.isKitchenPrint());
		cbRechnungNummerPrintEnable.setSelected(TerminalConfig.isRechnungNummerPrintEnable());
		cbCardTerminalDeActivate.setSelected(TerminalConfig.isCardTerminalDisable());
		ZVT_IP.setText(TerminalConfig.getKhanaCardIp());
		ZVT_PORT.setText(TerminalConfig.getKhanaCardPort());
		ZVT_ENABLE.setSelected(TerminalConfig.isKhanaZvtEnable());
		WEIGHT_CLIENT.setSelected(TerminalConfig.isWeightClient());
		WEIGHT_SERVER.setSelected(TerminalConfig.isWeightServer());
		WEIGHT_IP.setText(TerminalConfig.getWeightServerIp());
		tfAdimatCom.setText(TerminalConfig.getAdimatCom());
		cbAdimatCom.setSelected(TerminalConfig.isAdimatComEnable());
		initializeFontConfig();

		setInitialized(true);
	}

	private void initializeFontConfig() {
		GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Font[] fonts = e.getAllFonts(); // Get the fonts
		DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) cbFonts.getModel();
		model.addElement("<select>");

		for (Font f : fonts) {
			model.addElement(f.getFontName());
		}

		String uiDefaultFont = TerminalConfig.getUiDefaultFont();
		if(StringUtils.isNotEmpty(uiDefaultFont)) {
			cbFonts.setSelectedItem(uiDefaultFont);
		}
	}

	@Override
	public String getName() {
		return "Terminal";
	}
}




