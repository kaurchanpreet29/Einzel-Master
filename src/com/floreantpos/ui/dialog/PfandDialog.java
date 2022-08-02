package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.IconFactory;
import com.floreantpos.POSConstants;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.Ticket;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.views.order.OrderView;
import com.floreantpos.ui.views.order.OthersView;
import com.floreantpos.util.NumberUtil;

public class PfandDialog extends POSDialog implements ActionListener{
	private JTextField tfItem;
	private JTextField tfItem2;
	private JTextField tfItem3;
	private JLabel label;
	private JLabel label2;
	private JLabel label3;
	private JLabel labelTotal;
	private Double totalPrice = 0.00;
	private JTextField tfTotal;
	Ticket ticket;

	private PosButton oneCent;
	private PosButton twoCent;
	private PosButton fiveCent;
	private PosButton tenCent;
	private PosButton twentyCent;
	private PosButton fiftyCent;
	private PosButton oneEuro;
	private PosButton twoEuro;

	private PosButton fiveEuro;
	private PosButton tenEuro;
	private PosButton twentyEuro;
	private PosButton fiftyEuro;
	private PosButton hundredEuro;
	private PosButton twohundredEuro;
	private JCheckBox cbRetour;
	private JTextField tfRetourField;


	public PfandDialog(Ticket ticket)
	{
		this.ticket = ticket;
		initComponents();
	}

	private OthersView ohersView;
	public void initComponents()
	{
		getContentPane().setBackground(new Color(35,35,36));
		JPanel ticketAmountPanel = new JPanel();
		JPanel tfPanel = new JPanel();
		setTitle("Pfand");
		ohersView = OrderView.getInstance().getOthersView();
		tfPanel.setLayout(new MigLayout());
		tfPanel.setBackground(new Color(35,35,36));
		ticketAmountPanel.setLayout(new MigLayout());
		ticketAmountPanel.setBackground(new Color(35,35,36));
		label = new JLabel(TerminalConfig.getPfand1());
		label.setForeground(Color.WHITE);
		label.setFont(new Font("Times New Roman", Font.BOLD, 18));
		tfPanel.add(label, "growx");		
		tfRetourField = ohersView.tfItemSearch;
		tfRetourField.setEnabled(false);
		cbRetour = new JCheckBox("Retour");
		cbRetour.setFont(new Font(null, Font.BOLD, 26));
		cbRetour.setForeground(Color.WHITE);
		cbRetour.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(cbRetour.isSelected()) {
					ticket.setRefunded(true);
					tfRetourField.setText("Barcode/Art.Id Geben...");
					tfRetourField.setEnabled(true);
					ohersView.setRetour(true);					
				}else {
					ticket.setRefunded(false);
					tfRetourField.setEnabled(false);
					ohersView.setRetour(false);
				}

			}
		});

		tfItem = new JTextField(15);
		tfItem.setBackground(new Color(35, 35, 36));
		tfItem.setForeground(new Color(102, 223, 102));
		tfItem.setFont(new Font("Times New Roman", Font.BOLD, 22));
		tfPanel.add(tfItem,"growx");

		PosButton posButtonCancel = new PosButton(POSConstants.OK);
		posButtonCancel.setBackground(new Color(125,6,42));
		posButtonCancel.setForeground(Color.WHITE);
		posButtonCancel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}
		});
		tfPanel.add(posButtonCancel, "growx, wrap");

		label2 = new JLabel(TerminalConfig.getPfand2());
		label2.setForeground(Color.WHITE);
		label2.setFont(new Font("Times New Roman", Font.BOLD, 18));
		tfPanel.add(label2, "growx");

		tfItem2 = new JTextField(15);
		tfItem2.setBackground(new Color(35, 35, 36));
		tfItem2.setForeground(new Color(102, 223, 102));
		tfItem2.setFont(new Font("Times New Roman", Font.BOLD, 22));
		tfPanel.add(tfItem2,"growx, wrap");   


		label3 = new JLabel(TerminalConfig.getPfand3());
		label3.setForeground(Color.WHITE);
		label3.setFont(new Font("Times New Roman", Font.BOLD, 18));
		tfPanel.add(label3, "growx");

		tfItem3 = new JTextField(15);
		tfItem3.setFont(new Font("Times New Roman", Font.BOLD, 22));
		tfItem3.setBackground(new Color(35, 35, 36));
		tfItem3.setForeground(new Color(102, 223, 102));


		JLabel label4 = new JLabel("Retour");
		label4.setForeground(Color.WHITE);
		label4.setFont(new Font("Times New Roman", Font.BOLD, 18));


		cbRetour.setBackground(new Color(35, 35, 36));

		tfPanel.add(tfItem3,"growx, wrap");
		tfPanel.add(label4, "growx");
		tfPanel.add(tfRetourField, "growx");
		tfPanel.add(cbRetour, "growx, wrap");


		labelTotal = new JLabel("Gesamt");
		labelTotal.setForeground(Color.WHITE);
		labelTotal.setFont(new Font("Times New Roman", Font.BOLD, 18));
		tfPanel.add(labelTotal, "growx");

		tfTotal = new JTextField(15);
		tfTotal.setBackground(new Color(35, 35, 36));
		tfTotal.setForeground(Color.WHITE);
		tfTotal.setFont(new Font("Times New Roman", Font.BOLD, 24));
		tfPanel.add(tfTotal,"growx, wrap");

		oneCent = new PosButton();
		oneCent.setText("1 ¢");
		oneCent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				totalPrice = totalPrice+ 0.01;
				tfTotal.setText(NumberUtil.formatNumber(totalPrice) +  " " + Application.getCurrencySymbol());
			}
		});


		twoCent = new PosButton();
		twoCent.setText("2 ¢");
		twoCent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				totalPrice = totalPrice+ 0.02;
				tfTotal.setText(NumberUtil.formatNumber(totalPrice) +  " " + Application.getCurrencySymbol());
			}
		});



		fiveCent = new PosButton();
		fiveCent.setText("5 ¢");
		fiveCent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				totalPrice = totalPrice+ 0.05;
				tfTotal.setText(NumberUtil.formatNumber(totalPrice) +  " " + Application.getCurrencySymbol());
			}
		});


		tenCent = new PosButton();
		tenCent.setText("10 ¢");
		tenCent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				totalPrice = totalPrice+ 0.10;
				tfTotal.setText(NumberUtil.formatNumber(totalPrice) +  " " + Application.getCurrencySymbol());
			}
		});

		twentyCent = new PosButton();
		twentyCent.setText("20 ¢");
		twentyCent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				totalPrice = totalPrice+ 0.20;
				tfTotal.setText(NumberUtil.formatNumber(totalPrice) +  " " + Application.getCurrencySymbol());
			}
		});

		fiftyCent = new PosButton();
		fiftyCent.setText("50 ¢");
		fiftyCent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				totalPrice = totalPrice+ 0.50;
				tfTotal.setText(NumberUtil.formatNumber(totalPrice) +  " " + Application.getCurrencySymbol());
			}
		});

		oneEuro = new PosButton();
		oneEuro.setText("1 €");
		oneEuro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				totalPrice = totalPrice+ 1.00;
				tfTotal.setText(NumberUtil.formatNumber(totalPrice) +  " " + Application.getCurrencySymbol());
			}
		});

		twoEuro = new PosButton();
		twoEuro.setText("2 €");
		twoEuro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				totalPrice = totalPrice+ 2.00;
				tfTotal.setText(NumberUtil.formatNumber(totalPrice) +  " " + Application.getCurrencySymbol());
			}
		});

		fiveEuro = new PosButton();
		try{
			fiveEuro.setIcon(IconFactory.getIcon("fiveeuro.png"));
		} catch(Exception e){}
		fiveEuro.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				totalPrice = totalPrice+ 5.00;
				tfTotal.setText(NumberUtil.formatNumber(totalPrice) +  " " + Application.getCurrencySymbol());

			}});

		tenEuro = new PosButton();
		try {
			tenEuro.setIcon(IconFactory.getIcon("tenEuro.png"));
		} catch(Exception e){}
		tenEuro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				totalPrice = totalPrice+ 10.00;
				tfTotal.setText(NumberUtil.formatNumber(totalPrice) +  " " + Application.getCurrencySymbol());
			}
		});

		twentyEuro = new PosButton();
		try {
			twentyEuro.setIcon(IconFactory.getIcon("twentyEuro.png"));
		} catch(Exception e){}
		twentyEuro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				totalPrice = totalPrice+ 20.00;
				tfTotal.setText(NumberUtil.formatNumber(totalPrice) +  " " + Application.getCurrencySymbol());
			}
		});

		fiftyEuro = new PosButton();
		try {
			fiftyEuro.setIcon(IconFactory.getIcon("fiftyEuro.png"));
		} catch(Exception e){}
		fiftyEuro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				totalPrice = totalPrice+ 50.00;
				tfTotal.setText(NumberUtil.formatNumber(totalPrice) +  " " + Application.getCurrencySymbol());
			}
		});

		hundredEuro = new PosButton();
		try {
			hundredEuro.setIcon(IconFactory.getIcon("hundredEuro.png"));
		} catch(Exception e){}
		hundredEuro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				totalPrice = totalPrice+ 100.00;
				tfTotal.setText(NumberUtil.formatNumber(totalPrice) +  " " + Application.getCurrencySymbol());
			}
		});

		twohundredEuro = new PosButton(); 
		try {
			twohundredEuro.setIcon(IconFactory.getIcon("twohundredEuro.png"));
		} catch(Exception e){
			e.printStackTrace();
		}
		twohundredEuro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				totalPrice = totalPrice+ 200.00;
				tfTotal.setText(NumberUtil.formatNumber(totalPrice) +  " " + Application.getCurrencySymbol());
			}
		});

		setCoinFont(oneCent);
		setCoinFont(twoCent);
		setCoinFont(fiveCent);
		setCoinFont(tenCent);
		setCoinFont(twentyCent);
		setCoinFont(fiftyCent);
		setCoinFont(oneEuro);
		setCoinFont(twoEuro);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(35,35,36));
		buttonPanel.setLayout(new MigLayout());

		buttonPanel.add(oneCent, "cell 0 0,growx");
		buttonPanel.add(twoCent, "cell 1 0,growx");
		buttonPanel.add(fiveCent, "cell 2 0,growx");
		buttonPanel.add(tenCent, "cell 3 0,growx");

		buttonPanel.add(twentyCent, "cell 0 1,growx");
		buttonPanel.add(fiftyCent, "cell 1 1,growx");
		buttonPanel.add(oneEuro, "cell 2 1,growx");
		buttonPanel.add(twoEuro, "cell 3 1,growx");

		buttonPanel.add(fiveEuro, "cell 0 2,growx");
		buttonPanel.add(tenEuro, "cell 1 2,growx");
		buttonPanel.add(twentyEuro, "cell 2 2,growx");
		buttonPanel.add(fiftyEuro, "cell 3 2,growx");

		buttonPanel.add(hundredEuro, "cell 0 3 2 1,growx");
		buttonPanel.add(twohundredEuro, "cell 2 3 2 1,growx, wrap");

		PosButton posButtonPfand1 = new PosButton(TerminalConfig.getPfand1());
		posButtonPfand1.setBackground(new Color(2, 64, 2));
		posButtonPfand1.setForeground(Color.WHITE);
		posButtonPfand1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				ticket.setPfand(ticket.getPfand() + totalPrice);
				tfItem.setText(NumberUtil.formatNumber(ticket.getPfand()) + " "+ Application.getCurrencySymbol());
				totalPrice = 0.00;
				tfTotal.setText("0,00" + Application.getCurrencySymbol());
			}

		});
		buttonPanel.add(posButtonPfand1, "cell 0 4,growx");

		PosButton posButtonPfand2 = new PosButton(TerminalConfig.getPfand2());
		posButtonPfand2.setBackground(new Color(2, 64, 2));
		posButtonPfand2.setForeground(Color.WHITE);
		posButtonPfand2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				ticket.setPfand2(ticket.getPfand2() + totalPrice);
				totalPrice = 0.00;
				tfItem2.setText(NumberUtil.formatNumber(ticket.getPfand2()) + " " + Application.getCurrencySymbol());
				tfTotal.setText("0,00" + Application.getCurrencySymbol());
			}

		});
		buttonPanel.add(posButtonPfand2, "cell 1 4,growx");

		PosButton posButtonPfand3 = new PosButton(TerminalConfig.getPfand3());
		posButtonPfand3.setBackground(new Color(2, 64, 2));
		posButtonPfand3.setForeground(Color.WHITE);
		posButtonPfand3.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				ticket.setPfand3(ticket.getPfand3() + totalPrice);
				totalPrice = 0.00;
				tfItem3.setText(NumberUtil.formatNumber(ticket.getPfand3()) + " " + Application.getCurrencySymbol());
				tfTotal.setText("0,00" + Application.getCurrencySymbol());
			}

		});
		buttonPanel.add(posButtonPfand3, "cell 2 4,growx");

		PosButton posButtonReset = new PosButton("RESET");
		posButtonReset.setBackground(new Color(125,6,42));
		posButtonReset.setForeground(Color.WHITE);
		posButtonReset.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				totalPrice = 0.00;
				ticket.setPfand(0.00);
				ticket.setPfand2(0.00);
				ticket.setPfand3(0.00);
				tfItem.setText(NumberUtil.formatNumber(0.00) + " " + Application.getCurrencySymbol());
				tfItem2.setText(NumberUtil.formatNumber(0.00)  + " " + Application.getCurrencySymbol());
				tfItem3.setText(NumberUtil.formatNumber(0.00)  + " " + Application.getCurrencySymbol());
			}

		});
		buttonPanel.add(posButtonReset, "cell 3 4,growx");

		if(ticket.getPfand() != null)
			totalPrice = ticket.getPfand();
		tfItem.setText(NumberUtil.formatNumber(totalPrice)+ " €");

		if(ticket.getPfand2() != null)
			totalPrice = ticket.getPfand2();
		tfItem2.setText(NumberUtil.formatNumber(totalPrice)+ " €");

		if(ticket.getPfand3() != null)
			totalPrice = ticket.getPfand3();
		tfItem3.setText(NumberUtil.formatNumber(totalPrice)+ " €");

		getContentPane().add(tfPanel, BorderLayout.NORTH);
		getContentPane().add(ticketAmountPanel, BorderLayout.CENTER);
		getContentPane().add(buttonPanel,BorderLayout.SOUTH);
	}
	public void setFont(PosButton button)
	{
		button.setPreferredSize(new Dimension(120,80));
		button.setFont(new Font("Times New Roman", Font.PLAIN,25));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if (actionCommand.equals(POSConstants.CLEAR)) {
			doClearAll();
		}
		else if(actionCommand.equals("8c"))
		{
			totalPrice += getItemMultiple() * (0.08);
			tfTotal.setText(NumberUtil.formatNumber(totalPrice) + " " + Application.getCurrencySymbol());
		}
		else if(actionCommand.equals("15c"))
		{
			totalPrice += getItemMultiple() * (0.15);
			tfTotal.setText(NumberUtil.formatNumber(totalPrice) + " " + Application.getCurrencySymbol());
		}
		else if(actionCommand.equals("25c"))
		{
			totalPrice += getItemMultiple() * (0.25);
			tfTotal.setText(NumberUtil.formatNumber(totalPrice) + " " + Application.getCurrencySymbol());
		}
		else
		{
			doInsertNumber(actionCommand);
		}
	}

	public int getItemMultiple()
	{
		String temp = tfItem.getText();
		int delimIndex = temp.indexOf('*');
		int itemMultiple = 1;
		if (delimIndex != -1)
		{
			try
			{
				itemMultiple = Integer.parseInt(temp.substring(0, delimIndex));
			}
			catch(Exception e)
			{
				POSMessageDialog.showError(POSConstants.PROVIDE_CORRECT_FORMAT);
				tfItem.setText("");
				return 0;
			}
		}
		tfItem.setText("");
		return itemMultiple;
	}


	public void setCoinFont(PosButton button) {
		button.setBackground(new Color(102,51,0));
		button.setForeground(Color.WHITE);
		button.setFont(new Font(null, Font.BOLD, 22));
	}

	public void doClearAll()
	{
		tfItem.setText("");
	}
	private void doInsertNumber(String number) {
		String s = tfItem.getText();
		s = s + number;
		tfItem.setText(s);
		tfTotal.setText(NumberUtil.formatNumber(totalPrice) + " " + Application.getCurrencySymbol());
	}
}
