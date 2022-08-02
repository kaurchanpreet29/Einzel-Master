
package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.commons.lang.StringUtils;
import org.jdesktop.swingx.JXDatePicker;

import com.floreantpos.main.Application;
import com.floreantpos.model.CashBookData;
import com.floreantpos.model.Cashbook;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.dao.CashbookDAO;
import com.floreantpos.model.dao.CashbookDataDAO;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.report.JReportPrintService;
import com.floreantpos.report.ReportUtil;
import com.floreantpos.swing.ComboBoxModel;
import com.floreantpos.swing.ImageIcon;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.QwertyKeyPad;
import com.floreantpos.ui.views.TicketDetailView.PrintType;
import com.floreantpos.util.NumberUtil;

import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.engine.JasperPrint;

public class CashBookForm extends POSDialog {
	JTextField tfBetrag;
	JTextField tfNotiz;
	JComboBox<String> cbBechreibung = new JComboBox();
	JXDatePicker dpDatum = new JXDatePicker();
	Cashbook cashbook = null;

	public CashBookForm(Cashbook cashbook) {
		this.cashbook = cashbook;
		initComponents();
		final double betrag = NumberUtil.roundToTwoDigit(cashbook.getBetrag());   
		Integer beschreibungId = cashbook.getBeschreibungId();
		CashBookData cashbookDat = null;		
		tfBetrag.setText(String.valueOf(betrag).replace(".", ","));
		if(beschreibungId != null) {
			cashbookDat = CashbookDataDAO.getInstance().get(beschreibungId);
		}
		
		cbBechreibung.setSelectedItem(cashbookDat != null ? cashbookDat : cashbook.getBeschreibung());
		cbBechreibung.setFont(new Font(null, Font.BOLD, 35));
		dpDatum.setDate(cashbook.getDate());
	}

	public CashBookForm() {
		initComponents();
	}
	private Date date;
	private boolean dispose;
	public CashBookForm(Date date , boolean dispose) {
		this.date = date;
		this.dispose = dispose;
		initComponents();
	}

	public void initComponents() {
		setTitle("Kassenbuch Hinzufegen");
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout());
		tfBetrag = new JTextField(10);
		tfBetrag.setHorizontalAlignment(SwingConstants.RIGHT);
		tfBetrag.setFont(new Font(null, Font.BOLD, 30));
		tfBetrag.requestFocusInWindow();
		tfBetrag.setPreferredSize(new Dimension(150, 40));
		
		tfNotiz = new JTextField(10);
		tfNotiz.setHorizontalAlignment(SwingConstants.RIGHT);
		tfNotiz.setFont(new Font(null, Font.BOLD, 30));
		tfNotiz.requestFocusInWindow();
		tfNotiz.setPreferredSize(new Dimension(150, 40));
		
		cbBechreibung.setPreferredSize(new Dimension(150, 40));
		Beitrag = new JLabel();
		Beitrag.setFocusable(false);
		Beitrag.setText("Betrag (*)          ");
		Beitrag.setFont(new Font(null, Font.PLAIN, 16));
		panel.add(Beitrag , "cell 0 0, alignx trailing");
		panel.add(tfBetrag, "cell 1 0, grow,wrap");
		panel.add(new JLabel("Notiz"), "cell 2 0, alignx trailing");
		panel.add(tfNotiz, "cell 3 0, grow,wrap");
		panel.add(new JLabel("Beschreibung            "), "cell 0 1, alignx trailing");

		List<CashBookData> cashbookData = CashbookDataDAO.getInstance().findAll();
		if(dispose)
			cashbookData = cashbookData.stream()
			.filter(besch -> besch.getBeschreibung().compareTo("Anfangs Bestand Ein.")!=0
			&&besch.getBeschreibung().compareTo("Anfangs Bestand Aus.")!=0
			&&besch.getBeschreibung().compareTo("Umsatz Bar")!=0)
			.collect(Collectors.toList());
		cashbookData.forEach(data -> cbBechreibung.addItem(data.toString()));  

		cbBechreibung.setModel(new ComboBoxModel(cashbookData));
		cbBechreibung.setFont(new Font(null, Font.PLAIN, 18));
		cbBechreibung.setBackground(Color.WHITE);
		panel.add(cbBechreibung, "cell 1 1, grow,wrap");
		cbBechreibung.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CashBookData data = (CashBookData)cbBechreibung.getSelectedItem();
				if(data.isEinzahlung())
					Beitrag.setText("Betrag (*)    +     ");
				else
					Beitrag.setText("Betrag (*)    -     ");			

			}
		});

		panel.add(new JLabel("Datum            "), "cell 0 2, alignx trailing");
		panel.add(dpDatum, "cell 1 2, growx,wrap");
		if(date!=null)
			dpDatum.setDate(date);
		else
			dpDatum.setDate(new Date());
		dpDatum.getMonthView().setFont(new Font("Times New Roman", Font.PLAIN, 28));
		dpDatum.setFont(new Font(null, Font.BOLD, 24));
		JButton pck_btn = (JButton) dpDatum.getComponent(1);
		ImageIcon imageIcon = new ImageIcon(getClass().getResource("/images/calendar.png"));
		pck_btn.setIcon(imageIcon);
		PosButton okButton = new PosButton("  NUR-BUCHEN  ");
		okButton.setBackground(new Color(2,64,2));
		okButton.setForeground(Color.WHITE);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (tfBetrag.getText() == null || tfBetrag.getText().length() <= 0) {
					POSMessageDialog.showError("Ungueltige daten");
					return;
				}

				cashbook = new Cashbook();
				String betrag = tfBetrag.getText().replaceAll(",", ".");
				cashbook.setBetrag(Double.parseDouble(betrag));
				if (cbBechreibung.getSelectedItem() != null) {
					CashBookData cashbookInf = (CashBookData)cbBechreibung.getSelectedItem();
					cashbook.setBeschreibung(cashbookInf.toString());
					cashbook.setBezeichnung(tfNotiz.getText());
					cashbook.setBeschreibungId(cashbookInf.getId());
				}
				Calendar cal = Calendar.getInstance();
				cal.setTime(dpDatum.getDate());
				cal.set(Calendar.HOUR_OF_DAY, new Date().getHours());
				cal.set(Calendar.MINUTE, new Date().getMinutes());

				cashbook.setDate(cal.getTime());
				final CashBookData cashBookData = CashbookDataDAO.getInstance()
						.findByBeschreibung(cbBechreibung.getSelectedItem().toString()).stream().findFirst().orElse(null);
				if (cashBookData == null || cashBookData.isEinzahlung())
					cashbook.setAdddata(true);
				else
					cashbook.setAdddata(false);

				CashbookDAO.getInstance().saveOrUpdate(cashbook);
				tfBetrag.requestFocus();
				tfBetrag.setText("0.00");
				tfNotiz.setText("");
				cbBechreibung.setSelectedIndex(0);
				if(dispose)
					dispose();
			}

		});

		PosButton okButtonPrint = new PosButton("BUCHEN UND DRUCK");
		okButtonPrint.setBackground(new Color(2,64,2));
		okButtonPrint.setForeground(Color.WHITE);
		okButtonPrint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (tfBetrag.getText() == null || tfBetrag.getText().length() <= 0) {
					POSMessageDialog.showError("Ungueltige daten");
					return;
				}

				cashbook = new Cashbook();

				String betrag = tfBetrag.getText().replaceAll(",", ".");
				cashbook.setBetrag(Double.parseDouble(betrag));
				if (cbBechreibung.getSelectedItem() != null) {
					CashBookData cashbookInf = (CashBookData)cbBechreibung.getSelectedItem();				
					cashbook.setBeschreibung(cashbookInf.toString());
					cashbook.setBeschreibungId(cashbookInf.getId());				
					cashbook.setBezeichnung(tfNotiz.getText());

					AuszahlungPrint(cashbookInf, Double.parseDouble(betrag), cashbook);
				}
				Calendar cal = Calendar.getInstance();
				cal.setTime(dpDatum.getDate());
				cal.set(Calendar.HOUR_OF_DAY, new Date().getHours());
				cal.set(Calendar.MINUTE, new Date().getMinutes());

				cashbook.setDate(cal.getTime());
				final CashBookData cashBookData = CashbookDataDAO.getInstance()
						.findByBeschreibung(cbBechreibung.getSelectedItem().toString()).stream().findFirst().orElse(null);
				if (cashBookData == null || cashBookData.isEinzahlung())
					cashbook.setAdddata(true);
				else
					cashbook.setAdddata(false);

				CashbookDAO.getInstance().saveOrUpdate(cashbook);

				tfBetrag.requestFocus();
				tfBetrag.setText("0.00");
				tfNotiz.setText("");
				cbBechreibung.setSelectedIndex(0);
				if(dispose)
					dispose();
			}

		});

		panel.add(okButtonPrint, "cell 0 3 1, growx");
		panel.add(okButton, "cell 1 3 1, growx");

		PosButton cancelButton = new PosButton("  ABBRECHEN  ");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}

		});
		cancelButton.setForeground(Color.WHITE);
		cancelButton.setBackground(new Color(125, 6, 42));
		panel.add(cancelButton, "cell 2 3, growx");
		panel.setBackground(new Color(209, 222, 235));
		getContentPane().setBackground(new Color(209, 222, 235));
		getContentPane().add(panel, BorderLayout.CENTER);

		QwertyKeyPad qwerty = new QwertyKeyPad();
		getContentPane().add(qwerty, BorderLayout.SOUTH);

	}

	private JLabel Beitrag;
	public void AuszahlungPrint(CashBookData cashbookInf, Double Beitrag, Cashbook cash) {
		try {
			HashMap<String, String> map = new HashMap<String, String>();
			Date date = new Date();
			Restaurant rest = RestaurantDAO.getRestaurant();
			DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
			map.put("beschribungName", cash.getBeschreibung()+" - "+cash.getBezeichnung());
			if(cashbookInf.isEinzahlung()) {
				map.put("beschribung", "Einzahlungen");
				//				map.put("beitrag", " + "+NumberUtil.formatNumber(Beitrag)+" "+Application.getCurrencySymbol());
				map.put("grandSubtotal"," + "+ NumberUtil.formatNumber(Beitrag)+" "+Application.getCurrencySymbol());

			}else {
				map.put("beschribung", "Auszahlungen");
				//				map.put("beitrag", " - "+NumberUtil.formatNumber(Beitrag)+" "+Application.getCurrencySymbol());
				map.put("grandSubtotal", " - "+ NumberUtil.formatNumber(Beitrag)+" "+Application.getCurrencySymbol());

			}
			map.put("beitrag", NumberUtil.formatNumber(Beitrag)+" "+Application.getCurrencySymbol());
			ReportUtil.populateHeaderDetails(map, PrintType.REGULAR, true);
			map.put("reportTime", dateFormat.format(dpDatum.getDate()));
			map.put("user", Application.getCurrentUser().getFirstName());
			map.put("totalText", "BAR");
			//			map.put("footerMessage", "Steuer Nr.: "+rest.getTicketFooterMessage2()+"\n"+rest.getTicketFooterMessage()+"\n"+ "****************");
			//			map.put("footerMessage1", rest.getTicketFooterMessage1());
			map.put("footerMessage2", "Steuer Nr.: "+rest.getTicketFooterMessage2()+"\n"+rest.getTicketFooterMessage()+"\n"+ "****************");

			JasperPrint jasperPrint = JReportPrintService.createKassenbuchPrint(map);
			jasperPrint.setName("KassenBeschribungReport");
			jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
			JReportPrintService.printQuitely(jasperPrint);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}




//package com.floreantpos.ui.dialog;
//
//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.Font;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.LinkedHashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.TreeMap;
//
//import javax.swing.JButton;
//import javax.swing.JComboBox;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.JTextField;
//
//import org.jdesktop.swingx.JXDatePicker;
//
//import com.floreantpos.model.Cashbook;
//import com.floreantpos.model.dao.CashbookDAO;
//import com.floreantpos.swing.ImageIcon;
//import com.floreantpos.swing.PosButton;
//import com.floreantpos.swing.QwertyKeyPad;
//import com.floreantpos.ui.forms.CashBookData;
//import com.floreantpos.ui.forms.CashBookIndex;
//
//import net.miginfocom.swing.MigLayout;
//
//public class CashBookForm extends POSDialog{
//
//	JTextField tfBetrag = new JTextField(30);
//	JComboBox<String> cbBechreibung = new JComboBox();
//	JTextField tfBezeichnung = new JTextField(30);
//	JXDatePicker dpDatum = new JXDatePicker();
//	Cashbook cashbook = null; 
//	public CashBookForm(Cashbook cashbook)
//	{
//		this.cashbook = cashbook;
//		initComponents();
//		tfBetrag.setText(cashbook.getBetrag().toString().replace(".",","));
//		cbBechreibung.setSelectedItem(cashbook.getBeschreibung());
//		tfBezeichnung.setText(cashbook.getBezeichnung());
//		dpDatum.setDate(cashbook.getDate());
//	}
//	public CashBookForm()
//	{
//		initComponents();
//	}
//	public void initComponents()
//	{
//		JPanel panel = new JPanel();
//		panel.setLayout(new MigLayout());
//
//		panel.add(new JLabel("Betrag (*)"), "cell 0 0, growx");
//		panel.add(tfBetrag, "cell 1 0, growx,wrap");
//
//		panel.add(new JLabel("Beschreibung"), "cell 0 1, growx");
//
//		
//
//
//		Iterator it = sortByComparator(CashBookData.getData()).entrySet().iterator();
//		while (it.hasNext()) {
//			Map.Entry pair = (Map.Entry)it.next();
//			cbBechreibung.addItem(pair.getKey().toString());
//		}
//		cbBechreibung.setFont(new Font(null, Font.PLAIN, 18));
//		panel.add(cbBechreibung, "cell 1 1, growx,wrap");
//
//		panel.add(new JLabel("Bezeichnung"), "cell 0 2, growx");
//		panel.add(tfBezeichnung, "cell 1 2, growx,wrap");
//
//		panel.add(new JLabel("Datum"), "cell 0 3, growx");
//		panel.add(dpDatum, "cell 1 3, growx,wrap");
//		dpDatum.setDate(new Date());
//		dpDatum.getMonthView().setFont(new Font("Times New Roman", Font.PLAIN, 28));
//		dpDatum.setFont(new Font(null, Font.BOLD, 24));
//		JButton pck_btn = (JButton)dpDatum.getComponent(1);
//		ImageIcon imageIcon = new ImageIcon(getClass().getResource("/images/calendar.png"));
//		pck_btn.setIcon(imageIcon);
//		PosButton okButton = new PosButton("Buchen");
//		okButton.addActionListener(new ActionListener(){
//			@Override
//			public void actionPerformed(ActionEvent e) {
//
//				if(tfBetrag.getText() == null || tfBetrag.getText().length() <= 0)
//				{
//					POSMessageDialog.showError("Ungueltige daten");
//					return;
//				}
//
//				if(cashbook == null)
//					cashbook = new Cashbook();
//
//				String betrag = tfBetrag.getText().replaceAll(",", ".");
//				cashbook.setBetrag(Double.parseDouble(betrag));
//				if(cbBechreibung.getSelectedItem() != null)
//					cashbook.setBeschreibung(cbBechreibung.getSelectedItem().toString());
//				cashbook.setBezeichnung(tfBezeichnung.getText());
//				CashBookIndex index = CashBookData.getCashBookIndex(cashbook.getBeschreibung());
//				double tax = 0.00;
//				double taxAmount = 0.00;
//				int konto = 0;
//				if(index != null)
//				{
//					tax = index.getTax();
//					taxAmount = cashbook.getBetrag() - (cashbook.getBetrag()/((100+tax)/100));
//					konto = index.getNumber();
//				}
//				cashbook.setTax(tax);
//				cashbook.setKonto(konto);
//				cashbook.setTaxamount(taxAmount);
//				cashbook.setDate(dpDatum.getDate());
//				if(cashbook.getBeschreibung().contains("+"))
//					cashbook.setAdddata(true);
//				else
//					cashbook.setAdddata(false);
//
//				CashbookDAO.getInstance().saveOrUpdate(cashbook);
//				setCanceled(false);
//				dispose();
//			}
//
//		});
//
//		panel.add(okButton, "cell 0 4");
//
//		PosButton cancelButton = new PosButton("Abbrechen");
//		cancelButton.addActionListener(new ActionListener(){
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				setCanceled(true);
//				dispose();
//			}
//
//		});
//
//		panel.add(cancelButton, "cell 1 4");
//		panel.setBackground(new Color(209,222,235));
//		getContentPane().setBackground(new Color(209,222,235));
//		getContentPane().add(panel,BorderLayout.CENTER);
//
//		QwertyKeyPad qwerty = new QwertyKeyPad();
//		getContentPane().add(qwerty,BorderLayout.SOUTH);
//
//	}
//	private static Map<String, CashBookIndex> sortByComparator(Map<String, CashBookIndex> unsortMap) {
//
//		List<Map.Entry<String, CashBookIndex>> list = 
//				new LinkedList<Map.Entry<String, CashBookIndex>>(CashBookData.getData().entrySet());
//		Collections.sort(list, new Comparator<Map.Entry<String, CashBookIndex>>() {
//			public int compare(Map.Entry<String, CashBookIndex> o1,
//					Map.Entry<String, CashBookIndex> o2) {
//				return (o1.getKey()).compareTo(o2.getKey());
//			}
//		});
//
//		// Convert sorted map back to a Map
//		Map<String, CashBookIndex> sortedMap = new LinkedHashMap<String, CashBookIndex>();
//		for (Iterator<Map.Entry<String, CashBookIndex>> it = list.iterator(); it.hasNext();) {
//			Map.Entry<String, CashBookIndex> entry = it.next();
//			sortedMap.put(entry.getKey(), entry.getValue());
//		}
//		return sortedMap;
//	}
//}
