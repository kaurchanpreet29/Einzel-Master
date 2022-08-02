package com.floreantpos.ui.dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

public class CardDialog extends POSDialog{

	private JButton americanexpress;
	private JButton eccard;
	private JButton gutschein;
	 
	private JButton karte;
	private JButton khana;
	private JButton master;
	private JButton paypal;
	private JButton visa;
	public CardDialog()
	{
		super();
		initComponents();
	}
	
	public enum CardPaymentType{
		AMERICAN_EXPRESS,
		EC_CARD,
		GUTSCHEIN,		
		KHANA,
		MASTER,
		PAYPAL,
		VISA,
		KARTE
	}
	private CardPaymentType cardPaymentType;
	public void initComponents()
	{
		setLayout(new GridLayout(4,4));
		setTitle("KARTEN ZAHLUNG");
		setPreferredSize(new Dimension(600,400));
		
		americanexpress = new JButton();
		americanexpress.setIcon(new ImageIcon(getClass().getResource("/images/americanexpress.png")));
		americanexpress.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setCardPaymentType(CardPaymentType.AMERICAN_EXPRESS);
				dispose();
			}
		});
		americanexpress.setBackground(new Color(209,222,235));
		add(americanexpress);
		
		eccard = new JButton();
		eccard.setIcon(new ImageIcon(getClass().getResource("/images/eccard.png")));
		add(eccard);
		eccard.setBackground(new Color(209,222,235));
		eccard.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setCardPaymentType(CardPaymentType.EC_CARD);
				dispose();
			}
			
		});

		gutschein = new JButton();
		gutschein.setBackground(new Color(209,222,235));
		gutschein.setIcon(new ImageIcon(getClass().getResource("/images/gutschein.png")));
		gutschein.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setCardPaymentType(CardPaymentType.GUTSCHEIN);
				dispose();
			}
			
		});

		add(gutschein);
		
		khana = new JButton();
		khana.setBackground(new Color(209,222,235));
		khana.setIcon(new ImageIcon(getClass().getResource("/images/khana.png")));
		khana.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setCardPaymentType(CardPaymentType.KHANA);
				dispose();
			}
			
		});
		add(khana);

		master = new JButton();
		master.setBackground(new Color(209,222,235));
		master.setIcon(new ImageIcon(getClass().getResource("/images/master.png")));
		master.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setCardPaymentType(CardPaymentType.MASTER);
				dispose();
			}
			
		});
		add(master);
		
		paypal = new JButton();
		paypal.setBackground(new Color(209,222,235));
		paypal.setIcon(new ImageIcon(getClass().getResource("/images/paypal.png")));
		paypal.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setCardPaymentType(CardPaymentType.PAYPAL);
				dispose();
			}
			
		});
		add(paypal);
		
		visa = new JButton();
		visa.setBackground(new Color(209,222,235));
		visa.setIcon(new ImageIcon(getClass().getResource("/images/visa.png")));
		visa.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setCardPaymentType(CardPaymentType.VISA);
				dispose();
			}
			
		});
		add(visa);
		
		karte = new JButton();
		karte.setBackground(new Color(209,222,235));
		karte.setIcon(new ImageIcon(getClass().getResource("/images/kartez.png")));
		karte.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setCardPaymentType(CardPaymentType.KARTE);
				dispose();
			}
			
		});
		add(karte);
		
		setBackground(new Color(209,222,235));
	}
	
	public void setCardPaymentType(CardPaymentType type)
	{
		this.cardPaymentType = type;
	}
	public CardPaymentType getCardPaymentType()
	{
		return this.cardPaymentType;
	}

}
