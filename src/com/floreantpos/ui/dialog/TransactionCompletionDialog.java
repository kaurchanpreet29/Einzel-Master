package com.floreantpos.ui.dialog;

import java.awt.Color;
import java.awt.Frame;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.main.Application;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.report.JReportPrintService;
import com.floreantpos.swing.PosButton;
import com.floreantpos.util.NumberUtil;

public class TransactionCompletionDialog extends POSDialog {
	//private List<Ticket> tickets;
	private double tenderedAmount;
	private double totalAmount;
	private double paidAmount;
	private double dueAmount;
	private double gratuityAmount;
	private double changeAmount;

	private JLabel lblTenderedAmount;
	private JLabel lblTotalAmount;
	private JLabel lblPaidAmount;
	private JLabel lblDueAmount;
	private JLabel lblChangeDue;
	private JLabel lblGratuityAmount;

	private PosTransaction completedTransaction;

	public TransactionCompletionDialog(Frame parent, PosTransaction transaction) {
		super(parent, true);
		
		this.completedTransaction = transaction;

		setTitle(com.floreantpos.POSConstants.TRANSACTION_COMPLETED);

		setLayout(new MigLayout("align 50% 0%, ins 20", "[]20[]", ""));

		add(createLabel("Gesamt" + ":", JLabel.LEFT), "grow");
		lblTotalAmount = createLabel("0.0", JLabel.RIGHT);
		add(lblTotalAmount, "span, grow");
		
		add(createLabel("Bar" + ":", JLabel.LEFT), "newline,grow");
		lblTenderedAmount = createLabel("0.0", JLabel.RIGHT);
		add(lblTenderedAmount, "span, grow");

		add(new JSeparator(), "newline,span, grow");

		//add(createLabel("BAR" + ":", JLabel.LEFT), "newline,grow");
		lblPaidAmount = createLabel("0.0", JLabel.RIGHT);
		//add(lblPaidAmount, "span, grow");

		add(createLabel("Zurueck" + ":", JLabel.LEFT), "newline,grow");
		lblDueAmount = createLabel("0.0", JLabel.RIGHT);
		//add(lblDueAmount, "span, grow");

		//add(new JSeparator(), "newline,span, grow");

		//add(createLabel("GRATUITY AMOUNT" + ":", JLabel.LEFT), "newline,grow");
		lblGratuityAmount = createLabel("0.0", JLabel.RIGHT);
		//add(lblGratuityAmount, "span, grow");

		//add(new JSeparator(), "newline,span, grow");

		//add(createLabel("CHANGE DUE" + ":", JLabel.LEFT), "grow");
		lblChangeDue = createLabel("0.0", JLabel.RIGHT);
		add(lblChangeDue, "span, grow");

		add(new JSeparator(), "sg mygroup,newline,span,grow");
		PosButton btnClose = new PosButton("SCHLIESSEN");
		btnClose.setBackground(new Color(255,153,153));
		btnClose.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
			}

		});
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
		    public boolean dispatchKeyEvent(KeyEvent e) {
		        boolean keyHandled = false;
		        if (e.getID() == KeyEvent.KEY_PRESSED) {
		           if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
		        	  dispose();    
		            }
		           else if(e.getKeyCode() == KeyEvent.VK_ENTER){dispose();}
		        }
		        return keyHandled;
		    }
		});
		PosButton btnPrintStoreCopy = new PosButton("PRINT STORE COPY");
		btnPrintStoreCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {

					JReportPrintService.printTransaction(completedTransaction, false,false,false);

				} catch (Exception ee) {
					POSMessageDialog.showError(Application.getPosWindow(), "There was an error while printing.", ee);
				}
				dispose();
			}
		});

		PosButton btnPrintOfficialCopy = new PosButton("PRINT OFFICIAL COPY");
		btnPrintOfficialCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JReportPrintService.printTransaction(completedTransaction, false,true,false);

				} catch (Exception ee) {
					POSMessageDialog.showError(Application.getPosWindow(), "There was an error while printing.", ee);
				}
				dispose();
			}
		});

		PosButton btnPrintA4Copy = new PosButton("PRINT A4");
		btnPrintA4Copy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JReportPrintService.printTransactionA4(completedTransaction);

				} catch (Exception ee) {
					POSMessageDialog.showError(Application.getPosWindow(), "There was an error while printing.", ee);
				}
				dispose();
			}
		});
		PosButton btnPrintAllCopy = new PosButton("PRINT STORE & MERCHANT COPY");
		btnPrintAllCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {

					JReportPrintService.printTransaction(completedTransaction, true,false,false);

				} catch (Exception ee) {
					POSMessageDialog.showError(Application.getPosWindow(), "There was an error while printing.", ee);
				}
				dispose();
			}
		});

		JPanel p = new JPanel();
		p.setBackground(new Color(209,222,235));
		if (completedTransaction.isCard()) {
		/*	p.add(btnPrintAllCopy, "newline,skip, h 50");
			p.add(btnPrintOfficialCopy,"skip, h 50");
			p.add(btnPrintA4Copy,"skip, h 50");
			p.add(btnPrintStoreCopy, "skip, h 50");*/
			p.add(btnClose, "skip, h 50");
		}
		else {
			/*btnPrintStoreCopy.setText("PRINT");
			p.add(btnPrintStoreCopy, "skip, h 50");
			p.add(btnPrintOfficialCopy,"skip, h 50");
			p.add(btnPrintA4Copy,"skip, h 50");*/
			p.add(btnClose, "skip, h 50");
		}

		add(p, "newline, span 2, grow, gaptop 15px");
		getContentPane().setBackground(new Color(209,222,235));
		//setResizable(false);
	}

	protected JLabel createLabel(String text, int alignment) {
		JLabel label = new JLabel(text);
		label.setFont(new java.awt.Font("Tahoma", 1, 24));
		//label.setForeground(new java.awt.Color(255, 102, 0));
		label.setHorizontalAlignment(alignment);
		label.setText(text);
		return label;
	}

	public double getTenderedAmount() {
		return tenderedAmount;
	}

	public void setTenderedAmount(double amountTendered) {
		this.tenderedAmount = amountTendered;
	}

	public void updateView() {
		lblTotalAmount.setText(NumberUtil.formatNumber(totalAmount));
		lblTenderedAmount.setText(NumberUtil.formatNumber(tenderedAmount));
		lblPaidAmount.setText(NumberUtil.formatNumber(paidAmount));
		lblDueAmount.setText(NumberUtil.formatNumber(dueAmount));
		lblGratuityAmount.setText(NumberUtil.formatNumber(gratuityAmount));
		lblChangeDue.setText(NumberUtil.formatNumber(changeAmount));
	}


	public double getDueAmount() {
		return dueAmount;
	}

	public void setDueAmount(double dueAmount) {
		this.dueAmount = dueAmount;
	}

	public double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public double getGratuityAmount() {
		return gratuityAmount;
	}

	public void setGratuityAmount(double gratuityAmount) {
		this.gratuityAmount = gratuityAmount;
	}

	public double getChangeAmount() {
		return changeAmount;
	}

	public void setChangeAmount(double changeAmount) {
		this.changeAmount = changeAmount;
	}

	public void setCompletedTransaction(PosTransaction completedTransaction) {
		this.completedTransaction = completedTransaction;
	}
}
