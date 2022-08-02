package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.floreantpos.swing.PosButton;

import net.miginfocom.swing.MigLayout;

public class ZVTNewCardTryDialog extends POSDialog {

	private static final long serialVersionUID = 1L;

	private Double totalAmount;

	private Double inputAmount;

	public ZVTNewCardTryDialog(Double totalAmount, Double inputAmount) {
		this.totalAmount = totalAmount;
		this.inputAmount = inputAmount;
		initComponents();
	}

	public void initComponents() {getContentPane().setBackground(new Color(209, 222, 235));
	getContentPane().setLayout(new BorderLayout(5, 5));				

	JPanel centerPanel = new JPanel();
	centerPanel.setBackground(new Color(209, 222, 235));
	centerPanel.setLayout(new MigLayout());

	JPanel middlePanel = new JPanel();
	middlePanel.setBackground(new Color(209, 222, 235));
	middlePanel.setLayout(new GridLayout(2, 1));

	JPanel bottomPanel = new JPanel();
	bottomPanel.setBackground(new Color(209, 222, 235));
	bottomPanel.setLayout(new GridLayout());

	JLabel lblConfirmationMessage = new JLabel("Zahlung Nicht Moglich", SwingUtilities.CENTER);
	lblConfirmationMessage.setFont(new Font(null, Font.BOLD, 20));
	middlePanel.add(lblConfirmationMessage);

	JLabel lblConfirmationMessage1 = new JLabel("Bitte Neue Karte Nehmen", SwingUtilities.CENTER);
	lblConfirmationMessage1.setFont(new Font(null, Font.BOLD, 20));
	middlePanel.add(lblConfirmationMessage1);

	PosButton okButton = new PosButton("      OK       ");
	okButton.setBackground(new Color(0, 200, 0));
	okButton.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			setCanceled(true);
			dispose();
		}
	});
	
	bottomPanel.add(okButton);	

	getContentPane().add(middlePanel, BorderLayout.CENTER);
	getContentPane().add(bottomPanel, BorderLayout.SOUTH);

	}
}
