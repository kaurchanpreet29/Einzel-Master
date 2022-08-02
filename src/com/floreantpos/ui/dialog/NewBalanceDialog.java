package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang3.StringUtils;

import com.floreantpos.IconFactory;
import com.floreantpos.POSConstants;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.Customer;
import com.floreantpos.model.PaymentType;
import com.floreantpos.model.Ticket;
import com.floreantpos.ui.views.order.TicketView;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.CustomerScreen;
import com.floreantpos.util.NumberUtil;


public class NewBalanceDialog extends POSDialog implements ActionListener {
	
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
	private PosButton cashDrawerOpen;

	private JTextField tfTotalAmount;
	private JTextField tfPaidAmount;
	private JTextField tfBalanceAmount;


	private JTextField tfCardAmount;
	private JTextField tfBarAmount;
	private PosButton splitPayment;
	private PosButton splitPaymentWithOutPrint;
	private JCheckBox cbSplitPayment;
	private JLabel lblCardAmount;
	private JLabel lblBarAmount;
	private Double cardAmountValue;
	private PaymentType paymentType = PaymentType.CASH;

	private PosButton payButton;
	private PosButton payPrintButton;
	private PosButton payCardButton;
	private PosButton onlineButton;
	private PosButton uberwisungButton;

	private JTextField tfNumber;
	private Double totalAmount;
	private TicketView ticketView;
	public JPanel topPanel;

	private boolean print =false;

	private boolean official;
	private boolean card =false;

	private Double paid = 0.00;
	public JCheckBox cbA4Rechnung;
	
	public NewBalanceDialog(boolean cash, Double totalAmount, TicketView ticketView){
		this.totalAmount = totalAmount;
		this.ticketView = ticketView;
		tfTotalAmount = new JTextField(15);
		Ticket ticket = ticketView.getTicket();
		tfTotalAmount.setText(NumberUtil.formatNumber(totalAmount) + " €");
		tfPaidAmount = new JTextField(15);		
		tfBalanceAmount = new JTextField(15);
		
		if (!cash) {
			paid = totalAmount;
			tfPaidAmount.setText(NumberUtil.formatNumber(totalAmount));
			double balanceAmount = 0.00;

			tfBalanceAmount.setText(NumberUtil.formatNumber(balanceAmount) + " €");
		}
		initComponents();
	}
	
	public boolean isOfficial() {
		return this.official;
	};
	
	public boolean cardField=false;

	public PaymentType getPaymentType() {
		return this.paymentType;
	}

	public void update2ndScreen(Double paid, Double zuruck) {
		if(TerminalConfig.isKundenScreen())
			CustomerScreen.updateRest(paid, zuruck);
		
	}

	public Double getCardAmount() {
		Double cardAmount = 0.00;

		try {
			cardAmount = StringUtils.isBlank(tfCardAmount.getText()) ? 0.00 : Double
					.parseDouble(tfCardAmount.getText().replaceAll(",", "."));
			setCardAmountValue(cardAmount);
			//cardAmount = Double.parseDouble(tfCardAmount.getText().replaceAll(",", "."));
		} catch(Exception ex) {

		}
		return cardAmount;
	}

	public Double getBarAmount() {
		Double barAmount = 0.00;

		try {
			barAmount = StringUtils.isBlank(tfBarAmount.getText()) ? 0.00 : Double
					.parseDouble(tfBarAmount.getText().replaceAll(",", "."));		 
			//barAmount = Double.parseDouble(tfBarAmount.getText().replaceAll(",", "."));


		} catch(Exception ex) {

		}
		return barAmount;
	}

	public Double getCardAmountValue() {
		return cardAmountValue;
	}

	public void setCardAmountValue(Double cardAmountValue) {
		this.cardAmountValue = cardAmountValue;
	}

	private void initComponents(){
		fiveCent = new PosButton();
		fiveCent.setText("5 ¢");
		fiveCent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String paidString = tfPaidAmount.getText();
				try {
					Double paidAmount = StringUtils.isBlank(paidString) ? 0.00 : Double
							.parseDouble(paidString.replaceAll(",", "."));
					paidAmount = paidAmount + 0.05;
					paid = paidAmount;
					tfPaidAmount.setText(NumberUtil.formatNumber(paidAmount));

					double balanceAmount = paidAmount - totalAmount;
					tfBalanceAmount.setText(NumberUtil.formatNumber(balanceAmount)+ " €");
					update2ndScreen(paidAmount, balanceAmount);
				} catch (Exception ex) {        
				}
			}
		});

		tenCent = new PosButton();
		tenCent.setText("10 ¢");
		tenCent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String paidString = tfPaidAmount.getText();
				try {
					Double paidAmount = StringUtils.isBlank(paidString) ? 0.00 : Double
							.parseDouble(paidString.replaceAll(",", "."));
					paidAmount = paidAmount + 0.10;
					paid = paidAmount;
					tfPaidAmount.setText(NumberUtil.formatNumber(paidAmount));

					double balanceAmount = paidAmount - totalAmount;
					tfBalanceAmount.setText(NumberUtil.formatNumber(balanceAmount)+ " €");
					update2ndScreen(paidAmount, balanceAmount);
				} catch (Exception ex) {        
				}
			}
		});

		twentyCent = new PosButton();
		twentyCent.setText("20 ¢");
		twentyCent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String paidString = tfPaidAmount.getText();
				try {
					Double paidAmount = StringUtils.isBlank(paidString) ? 0.00 : Double
							.parseDouble(paidString.replaceAll(",", "."));
					paidAmount = paidAmount + 0.20;
					paid = paidAmount;
					tfPaidAmount.setText(NumberUtil.formatNumber(paidAmount));

					double balanceAmount = paidAmount - totalAmount;
					tfBalanceAmount.setText(NumberUtil.formatNumber(balanceAmount)+ " €");
					update2ndScreen(paidAmount, balanceAmount);
				} catch (Exception ex) {        
				}
			}
		});

		fiftyCent = new PosButton();
		fiftyCent.setText("50 ¢");
		fiftyCent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String paidString = tfPaidAmount.getText();
				try {
					Double paidAmount = StringUtils.isBlank(paidString) ? 0.00 : Double
							.parseDouble(paidString.replaceAll(",", "."));
					paidAmount = paidAmount + 0.50;
					paid = paidAmount;
					tfPaidAmount.setText(NumberUtil.formatNumber(paidAmount));

					double balanceAmount = paidAmount - totalAmount;
					tfBalanceAmount.setText(NumberUtil.formatNumber(balanceAmount)+ " €");
					update2ndScreen(paidAmount, balanceAmount);
				} catch (Exception ex) {        
				}
			}
		});

		fiftyCent = new PosButton();
		fiftyCent.setText("50 ¢");
		fiftyCent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String paidString = tfPaidAmount.getText();
				try {
					Double paidAmount = StringUtils.isBlank(paidString) ? 0.00 : Double
							.parseDouble(paidString.replaceAll(",", "."));
					paidAmount = paidAmount + 0.50;
					paid = paidAmount;
					tfPaidAmount.setText(NumberUtil.formatNumber(paidAmount));

					double balanceAmount = paidAmount - totalAmount;
					tfBalanceAmount.setText(NumberUtil.formatNumber(balanceAmount)+ " €");
					update2ndScreen(paidAmount, balanceAmount);
				} catch (Exception ex) {        
				}
			}
		});

		oneEuro = new PosButton();
		oneEuro.setText("1 €");
		oneEuro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String paidString = tfPaidAmount.getText();
				try {
					Double paidAmount = StringUtils.isBlank(paidString) ? 0.00 : Double
							.parseDouble(paidString.replaceAll(",", "."));
					paidAmount = paidAmount + 1.00;
					paid = paidAmount;
					tfPaidAmount.setText(NumberUtil.formatNumber(paidAmount));

					double balanceAmount = paidAmount - totalAmount;
					tfBalanceAmount.setText(NumberUtil.formatNumber(balanceAmount)+ " €");
					update2ndScreen(paidAmount, balanceAmount);
				} catch (Exception ex) {        
				}
			}
		});

		twoEuro = new PosButton();
		twoEuro.setText("2 €");
		twoEuro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String paidString = tfPaidAmount.getText();
				try {
					Double paidAmount = StringUtils.isBlank(paidString) ? 0.00 : Double
							.parseDouble(paidString.replaceAll(",", "."));
					paidAmount = paidAmount + 2.00;
					paid = paidAmount;
					tfPaidAmount.setText(NumberUtil.formatNumber(paidAmount));

					double balanceAmount = paidAmount - totalAmount;
					tfBalanceAmount.setText(NumberUtil.formatNumber(balanceAmount)+ " €");
					update2ndScreen(paidAmount, balanceAmount);
				} catch (Exception ex) {        
				}
			}
		});

		fiveEuro = new PosButton();
		try{
			fiveEuro.setIcon(IconFactory.getIcon("fiveeuro.png"));
		} catch(Exception e){}
		fiveEuro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String paidString = tfPaidAmount.getText();
				try {
					Double paidAmount = StringUtils.isBlank(paidString) ? 0.00 : Double
							.parseDouble(paidString.replaceAll(",", "."));
					paidAmount = paidAmount + 5.00;
					paid = paidAmount;
					tfPaidAmount.setText(NumberUtil.formatNumber(paidAmount));

					double balanceAmount = paidAmount - totalAmount;
					tfBalanceAmount.setText(NumberUtil.formatNumber(balanceAmount)+ " €");
					update2ndScreen(paidAmount, balanceAmount);
				} catch (Exception ex) {        
				}
			}
		});

		tenEuro = new PosButton();
		try {
			tenEuro.setIcon(IconFactory.getIcon("tenEuro.png"));
		} catch(Exception e){}
		tenEuro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String paidString = tfPaidAmount.getText();
				try {
					Double paidAmount = StringUtils.isBlank(paidString) ? 0.00 : Double
							.parseDouble(paidString.replaceAll(",", "."));
					paidAmount = paidAmount + 10.00;
					paid = paidAmount;
					tfPaidAmount.setText(NumberUtil.formatNumber(paidAmount));
					double balanceAmount = paidAmount - totalAmount;
					tfBalanceAmount.setText(NumberUtil.formatNumber(balanceAmount)+ " €");
					update2ndScreen(paidAmount, balanceAmount);
				} catch (Exception ex) {        
				}
			}
		});

		twentyEuro = new PosButton();
		try {
			twentyEuro.setIcon(IconFactory.getIcon("twentyEuro.png"));
		} catch(Exception e){}
		twentyEuro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String paidString = tfPaidAmount.getText();
				try {
					Double paidAmount = StringUtils.isBlank(paidString) ? 0.00 : Double
							.parseDouble(paidString.replaceAll(",", "."));
					paidAmount = paidAmount + 20.00;
					paid = paidAmount;
					tfPaidAmount.setText(NumberUtil.formatNumber(paidAmount));
					double balanceAmount = paidAmount - totalAmount;
					tfBalanceAmount.setText(NumberUtil.formatNumber(balanceAmount)+ " €");
					update2ndScreen(paidAmount, balanceAmount);
				} catch (Exception ex) {        
				}
			}
		});

		fiftyEuro = new PosButton();
		try {
			fiftyEuro.setIcon(IconFactory.getIcon("fiftyEuro.png"));
		} catch(Exception e){}
		fiftyEuro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String paidString = tfPaidAmount.getText();
				try {
					Double paidAmount = StringUtils.isBlank(paidString) ? 0.00 : Double
							.parseDouble(paidString.replaceAll(",", "."));
					paidAmount = paidAmount + 50.00;
					paid = paidAmount;
					tfPaidAmount.setText(NumberUtil.formatNumber(paidAmount));
					double balanceAmount = paidAmount - totalAmount;
					tfBalanceAmount.setText(NumberUtil.formatNumber(balanceAmount)+ " €");
					update2ndScreen(paidAmount, balanceAmount);
				} catch (Exception ex) {        
				}
			}
		});

		hundredEuro = new PosButton();
		try {
			hundredEuro.setIcon(IconFactory.getIcon("hundredEuro.png"));
		} catch(Exception e){}
		hundredEuro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String paidString = tfPaidAmount.getText();
				try {
					Double paidAmount = StringUtils.isBlank(paidString) ? 0.00 : Double
							.parseDouble(paidString.replaceAll(",", "."));
					paidAmount = paidAmount + 100.00;
					paid = paidAmount;
					tfPaidAmount.setText(NumberUtil.formatNumber(paidAmount));
					double balanceAmount = paidAmount - totalAmount;
					tfBalanceAmount.setText(NumberUtil.formatNumber(balanceAmount)+ " €");
					update2ndScreen(paidAmount, balanceAmount);
				} catch (Exception ex) {        
				}
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
				String paidString = tfPaidAmount.getText();
				try {
					Double paidAmount = StringUtils.isBlank(paidString) ? 0.00 : Double
							.parseDouble(paidString.replaceAll(",", "."));
					paidAmount = paidAmount + 200.00;
					paid = paidAmount;
					tfPaidAmount.setText(NumberUtil.formatNumber(paidAmount));
					double balanceAmount = paidAmount - totalAmount;
					tfBalanceAmount.setText(NumberUtil.formatNumber(balanceAmount)+ " €");
					update2ndScreen(paidAmount, balanceAmount);
				} catch (Exception ex) {        
				}
			}
		});

		setCoinFont(fiveCent);
		setCoinFont(tenCent);
		setCoinFont(twentyCent);
		setCoinFont(fiftyCent);
		setCoinFont(oneEuro);
		setCoinFont(twoEuro);

		JPanel currencyPanel = new JPanel();
		currencyPanel.setLayout(new MigLayout());

		currencyPanel.add(oneEuro, "growx");
		currencyPanel.add(twoEuro, "growx, wrap");
		currencyPanel.add(fiveEuro, "growx");
		currencyPanel.add(tenEuro, "growx,wrap");
		currencyPanel.add(twentyEuro, "growx");
		currencyPanel.add(fiftyEuro, "growx, wrap");
		currencyPanel.add(hundredEuro, "growx");
		currencyPanel.add(twohundredEuro, "growx, wrap");

		JPanel numberPanel = new JPanel();
		numberPanel.setLayout(new MigLayout("insets 0, gapx 0, gapy 0", "[grow][grow][grow]", ""));
		tfNumber = new javax.swing.JTextField();
		tfNumber.setHorizontalAlignment(SwingConstants.TRAILING);
		tfNumber.setBackground(Color.BLACK);
		tfNumber.setForeground(Color.WHITE);
		tfNumber.setText("");
		tfNumber.setFont(new java.awt.Font("Tahoma", 1, 24));

		numberPanel.add(tfNumber,"cell 0 0 3 1,grow");

		PosButton posButton7 = new PosButton();
		posButton7.setFocusable(false);
		setFont(posButton7, "7");
		numberPanel.add(posButton7, "cell 0 1,growx");
		posButton7.setActionCommand("7");
		posButton7.addActionListener(this);


		PosButton posButton8 = new PosButton();
		posButton8.setFocusable(false);
		setFont(posButton8, "8");
		numberPanel.add(posButton8, "cell 1 1,growx");
		posButton8.setActionCommand("8");
		posButton8.addActionListener(this);


		PosButton posButton9 = new PosButton();
		posButton9.setFocusable(false);
		setFont(posButton9, "9");
		numberPanel.add(posButton9, "cell 2 1,growx");
		posButton9.setActionCommand("9");
		posButton9.addActionListener(this);


		PosButton posButton4 = new PosButton();
		posButton4.setFocusable(false);
		setFont(posButton4, "4");
		numberPanel.add(posButton4, "cell 0 2,growx");
		posButton4.setActionCommand("4");
		posButton4.addActionListener(this);


		PosButton posButton5 = new PosButton();
		posButton5.setFocusable(false);
		setFont(posButton5, "5");
		numberPanel.add(posButton5, "cell 1 2,growx");
		posButton5.setActionCommand("5");
		posButton5.addActionListener(this);


		PosButton posButton6 = new PosButton();
		posButton6.setFocusable(false);
		setFont(posButton6, "6");
		numberPanel.add(posButton6, "cell 2 2,growx");
		posButton6.setActionCommand("6");
		posButton6.addActionListener(this);

		PosButton posButton1 = new PosButton();
		posButton1.setFocusable(false);
		setFont(posButton1, "1");
		numberPanel.add(posButton1, "cell 0 3,growx");
		posButton1.setActionCommand("1");
		posButton1.addActionListener(this);

		PosButton posButton2 = new PosButton();
		posButton2.setFocusable(false);
		setFont(posButton2, "2");
		numberPanel.add(posButton2, "cell 1 3,growx");
		posButton2.setActionCommand("2");
		posButton2.addActionListener(this);

		PosButton posButton3 = new PosButton();
		posButton3.setFocusable(false);
		setFont(posButton3, "3");
		numberPanel.add(posButton3, "cell 2 3,growx");
		posButton3.setActionCommand("3");
		posButton3.addActionListener(this);

		PosButton posButtonM = new PosButton();
		posButtonM.setFocusable(false);
		numberPanel.add(posButtonM, "cell 0 4,growx");
		if(TerminalConfig.isCommaPayment()) {
			setFont(posButtonM, ",");
			posButtonM.setActionCommand(",");  
		} else {
			setFont(posButtonM, "00");
			posButtonM.setActionCommand("00");      
		}

		posButtonM.addActionListener(this);

		PosButton posButton0 = new PosButton();
		posButton0.setFocusable(false);
		setFont(posButton0, "0");
		numberPanel.add(posButton0, "cell 1 4,growx");
		posButton0.setActionCommand("0");
		posButton0.addActionListener(this);

		PosButton posButtonC = new PosButton();
		posButtonC.setFocusable(false);
		setFont(posButtonC, "C");
		numberPanel.add(posButtonC, "cell 2 4,growx");
		posButtonC.setActionCommand("");
		posButtonC.addActionListener(this);

		PosButton posButtonEnter = new PosButton("ENTER");
		posButtonEnter.setBackground(new Color(2, 64, 2));
		posButtonEnter.setForeground(Color.WHITE);
		posButtonEnter.setFont(new Font(null, Font.BOLD, 22));
		numberPanel.add(posButtonEnter, "cell 3 0,growx");
		posButtonEnter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				double enteredAmount = 0.00;
				String enteredAmountString;
				if(cbSplitPayment.isSelected()&&cardField) {
					enteredAmountString = tfCardAmount.getText();
				}else if(cbSplitPayment.isSelected()&&!cardField) {
					enteredAmountString = tfBarAmount.getText();
				}else  {
					enteredAmountString = tfNumber.getText();
				}

				if (StringUtils.isNotBlank(enteredAmountString)) {
					if (TerminalConfig.isCommaPayment()) {
						enteredAmount = Double.parseDouble(enteredAmountString.replace(",",
								"."));
					} else {
						if(cbSplitPayment.isSelected()) {
							Double cardAmnt = 0.00;
							Double cashAmnt = 0.00;
							if (StringUtils.isNotBlank(tfBarAmount.getText())) {
								cashAmnt = Double.parseDouble(tfBarAmount.getText());
							}
							if (StringUtils.isNotBlank(tfCardAmount.getText())) {
								cardAmnt = Double.parseDouble(tfCardAmount.getText());
							}
							enteredAmount = cardAmnt+cashAmnt;        		  

						} else {
							enteredAmount = Double.parseDouble(enteredAmountString) / 100.00;
						}

					}
				}
				tfNumber.setText("");
				if(enteredAmount == 0.00) {
					return;
				}
				String paidString = tfPaidAmount.getText();
				try {
					Double paidAmount = StringUtils.isBlank(paidString) ? 0.00 : Double
							.parseDouble(paidString.replaceAll(",", "."));
					paidAmount = paidAmount + enteredAmount;
					paid = paidAmount;
					tfPaidAmount.setText(NumberUtil.formatNumber(paidAmount));
					double balanceAmount = paidAmount - totalAmount;
					tfBalanceAmount.setText(NumberUtil.formatNumber(balanceAmount) + " €"); 
					update2ndScreen(paidAmount, balanceAmount);
				} catch (Exception ex) {
				}
			}
		});

		numberPanel.add(fiveCent, "cell 3 4, growx");
		numberPanel.add(tenCent, "cell 3 3, growx");
		numberPanel.add(twentyCent, "cell 3 2, growx");
		numberPanel.add(fiftyCent, "cell 3 1, growx");

		topPanel = new JPanel();
		JLabel lblTotal = new JLabel("Gesamt");
		if(StringUtils.isNotEmpty(POSConstants.TOTAL))
			lblTotal.setText(POSConstants.TOTAL);
		  
		lblTotal.setFont(new Font(null, Font.BOLD, 18));
		lblTotal.setForeground(Color.WHITE);

		JLabel lblPaidAmount = new JLabel("Bezahlt");
		if(StringUtils.isNotEmpty(POSConstants.Bezahlt))
			lblPaidAmount.setText(POSConstants.Bezahlt);
		
		lblPaidAmount.setFont(new Font(null, Font.BOLD, 18));
		lblPaidAmount.setForeground(Color.WHITE);
		PosButton resetButton = new PosButton("RESET");
		resetButton.setFont(new Font(null, Font.BOLD, 18));
		resetButton.setForeground(Color.WHITE);
		resetButton.setBackground(new Color(125,6,42));
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tfPaidAmount.setText("");
				paid = 0.00;
				tfBalanceAmount.setText("");
				if(cbSplitPayment.isSelected()) {
					tfCardAmount.setText("");
					tfBarAmount.setText("");
				}
			}
		});

		PosButton cancelButton = new PosButton("ABBRECHEN");
		if(StringUtils.isNotEmpty(POSConstants.ABBRECHEN))
			cancelButton.setText(POSConstants.ABBRECHEN);
		
		cancelButton.setFont(new Font(null, Font.BOLD, 18));
		cancelButton.setForeground(Color.WHITE);
		cancelButton.setBackground(new Color(125,6,42));
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}
		});

		payButton = new PosButton("BAR");
		if(StringUtils.isNotEmpty(POSConstants.BAR))
			payButton.setText(POSConstants.BAR);
		
		payButton.setFont(new Font(null, Font.BOLD, 18));
		payButton.setForeground(Color.WHITE);
		payButton.setBackground(new Color(2, 64, 2));
		payButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				print = false;
				paymentType = paymentType.CASH;				 
				dispose();
				 
			}
		});

		payPrintButton = new PosButton("Bar-Druck");
		if(StringUtils.isNotEmpty(POSConstants.Bar_Druck))
			payPrintButton.setText(POSConstants.Bar_Druck);
		
		payPrintButton.setForeground(Color.WHITE);
		payPrintButton.setFont(new Font(null, Font.BOLD, 18));
		payPrintButton.setBackground(new Color(2, 64, 2));
		payPrintButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				print = true;
				paymentType = paymentType.CASH;
				dispose();
			}
		});

		payCardButton = new PosButton("Karte");
		if(StringUtils.isNotEmpty(POSConstants.Karte))
			payCardButton.setText(POSConstants.Karte);
		
		payCardButton.setForeground(Color.WHITE);
		payCardButton.setFont(new Font(null, Font.BOLD, 18));
		payCardButton.setBackground(new Color(2, 64, 2));
		payCardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				card = true;
				print = true;
				paymentType = paymentType.CARD;
				dispose();
			}
		});

		onlineButton = new PosButton("Online");
		onlineButton.setForeground(Color.WHITE);
		onlineButton.setFont(new Font(null, Font.BOLD, 18));
		onlineButton.setBackground(new Color(2, 64, 2));
		onlineButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				card = false;
				print = true;
				paymentType = PaymentType.ONLINE;
				dispose();
			}
		});

		uberwisungButton = new PosButton("Überweisen");
		if(StringUtils.isNotEmpty(POSConstants.Uberweisen))
			uberwisungButton.setText(POSConstants.Uberweisen);
		
		uberwisungButton.setForeground(Color.WHITE);
		uberwisungButton.setFont(new Font(null, Font.BOLD, 18));
		uberwisungButton.setBackground(new Color(2, 64, 2));
		uberwisungButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				card = false;
				print = true;
				paymentType = PaymentType.RECHNUNG;
				dispose();
			}
		});


		cashDrawerOpen = new PosButton("SCHUBLADE OFFNEN");
		if(StringUtils.isNotEmpty(POSConstants.SCHUBLADE_OFFNEN))
			cashDrawerOpen.setText(POSConstants.SCHUBLADE_OFFNEN);
		
		cashDrawerOpen.setFont(new Font(null, Font.BOLD, 18));
		cashDrawerOpen.setForeground(Color.WHITE);
		cashDrawerOpen.setBackground(new Color(2, 64, 2));
		cashDrawerOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (StringUtils.isNotBlank(TerminalConfig.getCashDrawerFile())) {
					ticketView.openCashDrawerFile();
				} else if (TerminalConfig.isCashDrawerPrint()) {
					ticketView.openCashDrawerPrint();
				} else if (TerminalConfig.isCashDrawer()) {
					ticketView.openCashDrawer();
				}
			}
		});

		tfCardAmount = new JTextField(6);
		tfBarAmount	= new JTextField(6);	
		tfBarAmount.setVisible(false);
		tfCardAmount.setVisible(false);
		setTextFieldFont(tfCardAmount);
		setTextFieldFont(tfBarAmount);

		tfCardAmount.addKeyListener(new KeyListener() {	
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				tfPaidAmount.setText(null);
				tfBarAmount.setText(null);
				tfPaidAmount.setText(Double.toString(NumberUtil.roundToTwoDigit(getCardAmount()+getBarAmount())));	
				String paidString = tfPaidAmount.getText();
				try {
					Double paidAmount = StringUtils.isBlank(paidString) ? 0.00 : Double
							.parseDouble(paidString.replaceAll(",", "."));
					paidAmount = paidAmount;
					paid = paidAmount;
					tfPaidAmount.setText(NumberUtil.formatNumber(paidAmount));
					double balanceAmount = paidAmount - totalAmount;
					tfBalanceAmount.setText(NumberUtil.formatNumber(balanceAmount)+ " €");
					update2ndScreen(paidAmount, balanceAmount);
				} catch (Exception ex) {        
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
			}
		});

		tfCardAmount.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void focusGained(FocusEvent e) {
				cardField = true;
			}
		});
		
		tfBarAmount.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				tfPaidAmount.setText(null);
				tfPaidAmount.setText(Double.toString(NumberUtil.roundToTwoDigit(getCardAmount()+getBarAmount())));
				String paidString = tfPaidAmount.getText();
				try {
					Double paidAmount = StringUtils.isBlank(paidString) ? 0.00 : Double
							.parseDouble(paidString.replaceAll(",", "."));
					paidAmount = paidAmount;
					paid = paidAmount;
					tfPaidAmount.setText(NumberUtil.formatNumber(paidAmount));
					double balanceAmount = paidAmount - totalAmount;
					tfBalanceAmount.setText(NumberUtil.formatNumber(balanceAmount)+ " €");
					update2ndScreen(paidAmount, balanceAmount);
				} catch (Exception ex) {        
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		});

		tfBarAmount.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void focusGained(FocusEvent e) {
				cardField = false;
			}
		});


		lblCardAmount = new JLabel("Karte Beitrag");
		lblCardAmount.setFont(new Font(null, Font.BOLD, 18));
		lblCardAmount.setForeground(Color.WHITE);
		lblCardAmount.setVisible(false);

		lblBarAmount = new JLabel("Bar Beitrag");
		lblBarAmount.setFont(new Font(null, Font.BOLD, 18));
		lblBarAmount.setForeground(Color.WHITE);
		lblBarAmount.setVisible(false);

		splitPayment = new PosButton("ZAHLEN M.D");
		splitPayment.setFont(new Font(null, Font.BOLD, 18));
		splitPayment.setForeground(Color.WHITE);
		splitPayment.setBackground(new Color(2, 64, 2));
		splitPayment.setVisible(false);
		splitPayment.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tfPaidAmount.setText(getCardAmount()+getBarAmount()+"");				
				paymentType = PaymentType.BOTH;
				print = true;
				card = true;
				dispose();			
			}
		});

		splitPaymentWithOutPrint = new PosButton("ZAHLEN O.D");
		splitPaymentWithOutPrint.setFont(new Font(null, Font.BOLD, 18));
		splitPaymentWithOutPrint.setForeground(Color.WHITE);
		splitPaymentWithOutPrint.setBackground(new Color(2, 64, 2));
		splitPaymentWithOutPrint.setVisible(false);
		splitPaymentWithOutPrint.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tfPaidAmount.setText(Double.toString(getCardAmount()+getBarAmount()));
				paymentType = PaymentType.BOTH;
				print = false;
				card = true;
				dispose();

			}
		});


		cbSplitPayment = new JCheckBox("Split Zahlen");
		if(StringUtils.isNotEmpty(POSConstants.Split_ZAHLEN))
			cbSplitPayment.setText(POSConstants.Split_ZAHLEN);
		
		cbSplitPayment.setFont(new Font(null, Font.BOLD, 18));
		cbSplitPayment.setForeground(Color.WHITE);
		cbSplitPayment.setBackground(Color.DARK_GRAY);
		cbSplitPayment.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				if(cbSplitPayment.isSelected()) {
					splitOption(true);
					tfBarAmount.setText(null);
					tfCardAmount.setText(null);
				}else if(!cbSplitPayment.isSelected()) {
					splitOption(false);
				}
			}
		});    


		cbA4Rechnung = new JCheckBox("A4 Rechnung");
		if(StringUtils.isNotEmpty(POSConstants.A4_Rechnung))
			cbA4Rechnung.setText(POSConstants.A4_Rechnung);
		
		cbA4Rechnung.setFont(new Font(null, Font.BOLD, 18));
		cbA4Rechnung.setForeground(Color.WHITE);
		cbA4Rechnung.setBackground(Color.DARK_GRAY);
		cbA4Rechnung.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				if(cbA4Rechnung.isSelected()) {
					showCustomerDialog(ticketView.getTicket());
					payButton.setEnabled(false);
				}
			}
		});    


		JLabel lblBalanceAmount = new JLabel("Züruck");
		if(StringUtils.isNotEmpty(POSConstants.Zuruck))
			lblBalanceAmount.setText(POSConstants.Zuruck);
		
		lblBalanceAmount.setFont(new Font(null, Font.BOLD, 18));
		lblBalanceAmount.setForeground(Color.WHITE);
		topPanel.setLayout(new MigLayout());
		tfTotalAmount.setFont(new Font(null, Font.BOLD, 18));
		setTextFieldFont(tfPaidAmount);
		setTextFieldFont(tfTotalAmount);
		setTextFieldFont(tfBalanceAmount);
		tfBalanceAmount.setForeground(new Color(102,223,102));
		tfBalanceAmount.setFont(new Font(null, Font.BOLD, 28));

		topPanel.add(lblTotal, "cell 0 0,growx");
		topPanel.add(tfTotalAmount, "cell 1 0,growx");
		topPanel.add(lblPaidAmount, "cell 0 1, growx");
		topPanel.add(tfPaidAmount, "cell 1 1,growx");
		topPanel.add(lblBalanceAmount, "cell 0 2,growx");
		topPanel.add(tfBalanceAmount, "cell 1 2,growx");
		topPanel.add(resetButton, "cell 0 3,growx");    

		if(!TerminalConfig.isHideDrawer()) {
			topPanel.add(cashDrawerOpen, "cell 1 3");
		}    
		topPanel.add(cbSplitPayment, "cell 0 4, growx");//added for Split
		topPanel.add(cbA4Rechnung, "cell 1 4, growx");//added for Split


		
		topPanel.add(cancelButton, "cell 4 0 2 4, growx");
		topPanel.add(payCardButton, "cell 4 2 2 4, growx");
		if(!TerminalConfig.isWholeSale())
			topPanel.add(payButton, "cell 2 0 2 4,growx");
		if(TerminalConfig.isWholeSale())
			payPrintButton.setText("   BAR   ");
		topPanel.add(payPrintButton, "cell 2 2 2 4,growx");

		if(TerminalConfig.isPayTransfer()) {
			
			topPanel.add(uberwisungButton, "cell 4 4 2 4,growx");
		}
		
		if(TerminalConfig.isGutInBalanceDialog()) {
			//topPanel.add(onlineButton, "cell 2 4 2 4,growx");
			topPanel.add(onlineButton, "cell 2 4 2 4,growx");
		}

		getContentPane().setLayout(new BorderLayout());
		getContentPane().setBackground(new Color(35,35,36));
		topPanel.setBackground(new Color(35,35,36));
		numberPanel.setBackground(new Color(35,35,36));
		currencyPanel.setBackground(new Color(35,35,36));
		getContentPane().add(topPanel, BorderLayout.NORTH);
		getContentPane().add(numberPanel, BorderLayout.CENTER);
		getContentPane().add(currencyPanel, BorderLayout.EAST);
	}

	public void showCustomerDialog(Ticket ticket1) {
		CustomerDataDialog dialog = new CustomerDataDialog(ticket1);
		dialog.pack();
		dialog.open();
		if(dialog.isCanceled())
			return;
		Customer cust = dialog.getNewCustomer();
		ticket1.setCustomer(cust);
		//		TicketDAO.getInstance().saveOrUpdate(ticket1);
		//		ticket1.setKunden(cust);
	}

	public void splitOption(boolean enable) {

		if(enable) {
			topPanel.add(lblCardAmount, "cell 0 5, growx");//added for Split
			topPanel.add(tfCardAmount, "cell 1 5, growx");//added for Split
			topPanel.add(lblBarAmount, "cell 2 5, growx");//added for Split
			topPanel.add(tfBarAmount, "cell 3 5, growx");//added for Split
			topPanel.add(splitPaymentWithOutPrint, "cell 0 6, growx");//added for Split
			topPanel.add(splitPayment, "cell 1 6 , growx");//added for Split 
			topPanel.repaint();
		}else {
			topPanel.remove(lblCardAmount);//added for Split
			topPanel.remove(tfCardAmount);//added for Split
			topPanel.remove(lblBarAmount);//added for Split
			topPanel.remove(tfBarAmount);//added for Split
			topPanel.remove(splitPaymentWithOutPrint);//added for Split
			topPanel.remove(splitPayment);//added for Split 
			topPanel.repaint();
		}

		payButton.setEnabled(!enable);
		payCardButton.setEnabled(!enable);
		payPrintButton.setEnabled(!enable);
		splitPayment.setVisible(enable);
		tfCardAmount.setVisible(enable);
		tfBarAmount.setVisible(enable);
		lblCardAmount.setVisible(enable);
		lblBarAmount.setVisible(enable);
		splitPaymentWithOutPrint.setVisible(enable);
	}


	private void setTextFieldFont(JTextField textField) {
		textField.setFont(new Font(null, Font.BOLD, 18));
		textField.setBackground(Color.BLACK);
		textField.setForeground(Color.WHITE);
	}

	public void setFont(PosButton button, String text) {
		button.setBackground(new Color(102,51,0));
		button.setForeground(Color.WHITE);
		button.setFont(new Font(null, Font.BOLD, 22));
		if(text.length() > 0) {
			button.setText(text);
		}
	}

	public void setCoinFont(PosButton button) {
		button.setBackground(Color.BLACK);
		button.setForeground(Color.WHITE);
		button.setFont(new Font(null, Font.BOLD, 22));
	}

	public boolean isPrint() {
		return this.print;
	}

	public boolean isCard() {
		return this.card;
	}
	
	public double getPaidAmount() {
		return this.paid;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if (actionCommand.equals(POSConstants.CLEAR)) {
			if(cbSplitPayment.isSelected()&&cardField) {  		 
				tfCardAmount.setText("");
			}else if(cbSplitPayment.isSelected()&&!cardField) {  		 
				tfBarAmount.setText("");
			}else {
				tfNumber.setText("");
			}
		} else
		{
			doInsertNumber(actionCommand);
		}
	}

	private void doInsertNumber(String number) {
		if(cbSplitPayment.isSelected()&&cardField) {
			String s = tfCardAmount.getText();
			s = s + number;
			if (number.compareTo("CLEAR") != 0)
				tfCardAmount.setText(s);
		}else if(cbSplitPayment.isSelected()&&!cardField) {
			String s = tfBarAmount.getText();
			s = s + number;
			if (number.compareTo("CLEAR") != 0)
				tfBarAmount.setText(s);
		}else {
			String s = tfNumber.getText();
			s = s + number;
			if (number.compareTo("CLEAR") != 0)
				tfNumber.setText(s);
		}
	}

}
