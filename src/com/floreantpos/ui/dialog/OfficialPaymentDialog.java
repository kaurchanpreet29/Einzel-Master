package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.floreantpos.config.TerminalConfig;

import net.miginfocom.swing.MigLayout;

public class OfficialPaymentDialog extends POSDialog{

  private JButton cancelButton;
  private JButton AbbrechenButton;
  private JButton cash;
	private JButton online;
	private JButton americanexpress;
	private JButton eccard;
	private JButton gutschein;
	private JButton karte;
	private JButton khana;
	private JButton master;
	private JButton paypal;
	private JButton visa;
	public OfficialPaymentDialog()
	{
		super();
		initComponents();
	}
	
	public enum OfficialPaymentType{
		AMERICAN_EXPRESS,
		EC_CARD,
		GUTSCHEIN,
		KHANA,
		MASTER,
		PAYPAL,
		VISA,
		KARTE,
		CASH,
		ONLINE
	}
	private OfficialPaymentType cardPaymentType;
	public void initComponents()
	{
	  
	  JPanel buttonPanel = new JPanel();
	  
		buttonPanel.setLayout(new GridLayout(6,2));
		setTitle("KARTEN ZAHLUNG");
		setPreferredSize(new Dimension(800,500));

		americanexpress = new JButton();
		americanexpress.setIcon(new ImageIcon(getClass().getResource("/images/americanexpress.png")));
		americanexpress.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setOfficialPaymentType(OfficialPaymentType.AMERICAN_EXPRESS);
				setCanceled(false);
				dispose();
			}
		});
		americanexpress.setBackground(new Color(209,222,235));
		buttonPanel.add(americanexpress);
		
		eccard = new JButton();
		eccard.setIcon(new ImageIcon(getClass().getResource("/images/eccard.png")));
		buttonPanel.add(eccard);
		eccard.setBackground(new Color(209,222,235));
		eccard.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setOfficialPaymentType(OfficialPaymentType.EC_CARD);
				setCanceled(false);
				dispose();
			}
			
		});

		gutschein = new JButton();
		gutschein.setBackground(new Color(209,222,235));
		gutschein.setIcon(new ImageIcon(getClass().getResource("/images/gutschein.png")));
		gutschein.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setOfficialPaymentType(OfficialPaymentType.GUTSCHEIN);
				setCanceled(false);
				dispose();
			}
			
		});

		buttonPanel.add(gutschein);
		
		khana = new JButton();
		khana.setBackground(new Color(209,222,235));
		khana.setIcon(new ImageIcon(getClass().getResource("/images/khana.png")));
		khana.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setOfficialPaymentType(OfficialPaymentType.KHANA);
				setCanceled(false);
				dispose();
			}
			
		});
		buttonPanel.add(khana);

		master = new JButton();
		master.setBackground(new Color(209,222,235));
		master.setIcon(new ImageIcon(getClass().getResource("/images/master.png")));
		master.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setOfficialPaymentType(OfficialPaymentType.MASTER);
				setCanceled(false);
				dispose();
			}
			
		});
		buttonPanel.add(master);
		
		paypal = new JButton();
		paypal.setBackground(new Color(209,222,235));
		paypal.setIcon(new ImageIcon(getClass().getResource("/images/paypal.png")));
		paypal.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setOfficialPaymentType(OfficialPaymentType.PAYPAL);
				setCanceled(false);
				dispose();
			}
			
		});
		buttonPanel.add(paypal);
		
		visa = new JButton();
		visa.setBackground(new Color(209,222,235));
		visa.setIcon(new ImageIcon(getClass().getResource("/images/visa.png")));
		visa.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setOfficialPaymentType(OfficialPaymentType.VISA);
				setCanceled(false);
				dispose();
			}
			
		});
		buttonPanel.add(visa);
		
		karte = new JButton();
		karte.setBackground(new Color(209,222,235));
		karte.setIcon(new ImageIcon(getClass().getResource("/images/kartez.png")));
		karte.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setOfficialPaymentType(OfficialPaymentType.KARTE);
				setCanceled(false);
				dispose();
			}
			
		});
		
		buttonPanel.add(karte);
		
		cash = new JButton("BAR");
		cash.setBackground(new Color(0,175,0));
		cash.setFont(new Font("Times New Roman", Font.BOLD, 22));
		cash.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setOfficialPaymentType(OfficialPaymentType.CASH);
				setCanceled(false);
				dispose();
			}
		});
		buttonPanel.add(cash);

		online = new JButton("ONLINE");
		online.setBackground(new Color(0,175,0));
		online.setFont(new Font("Times New Roman", Font.BOLD, 22));
		online.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setOfficialPaymentType(OfficialPaymentType.ONLINE);
				setCanceled(false);
				dispose();
			}
		});
		
		if(TerminalConfig.isPayTransfer())
			buttonPanel.add(online);
		
		
		cancelButton = new JButton("Cancel");
		cancelButton.setBackground(new Color(255, 153, 153));
		cancelButton.setFont(new Font("Times New Roman", Font.BOLD, 22));
    cancelButton.addActionListener(new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent e) {
        setCanceled(true);
        dispose();
      }
    });
    
    AbbrechenButton = new JButton("Abbrechen");
    AbbrechenButton.setBackground(new Color(255, 153, 153));
    AbbrechenButton.setFont(new Font("Times New Roman", Font.BOLD, 22));
    AbbrechenButton.addActionListener(new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent e) {
        setCanceled(true);
        dispose();
      }
    });
    buttonPanel.add(AbbrechenButton);
    buttonPanel.add(cancelButton);
    
		getContentPane().add(buttonPanel);
		setBackground(new Color(209,222,235));
	}
	
	public void setOfficialPaymentType(OfficialPaymentType type)
	{
		this.cardPaymentType = type;
	}
	public OfficialPaymentType getOfficialPaymentType()
	{
		return this.cardPaymentType;
	}

}
