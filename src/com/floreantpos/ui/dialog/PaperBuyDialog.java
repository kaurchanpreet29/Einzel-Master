package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.swing.FixedLengthDocument;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.QwertyKeyPad;

public class PaperBuyDialog extends POSDialog{
	
	FixedLengthTextField tfPack = new FixedLengthTextField(10);
	PosButton tfPackInc = new PosButton();
	PosButton tfPackDec = new PosButton();
	
	FixedLengthTextField tfPack10 = new FixedLengthTextField(10);
	PosButton tfPack10Inc =new PosButton();
	PosButton tfPack10Dec =new PosButton();
	
	FixedLengthTextField tfKarten= new FixedLengthTextField(10);
	PosButton tfKartenInc=new PosButton();
	PosButton tfKartenDec=new PosButton();
	
	JTextArea tfInfo = new JTextArea(5,40);
	PosButton sendButton = new PosButton("Senden");
	PosButton cancelButton = new PosButton("Abbrechen");
	public PaperBuyDialog()
	{
		setLayout(new BorderLayout());
		setBackground(new Color(209,222,235));
		setTitle("Bon rollen bestellen");
		JPanel contentPanel = new JPanel();
		contentPanel.setBackground(new Color(209,222,235));
		contentPanel.setLayout(new MigLayout());
		
		contentPanel.add(new JLabel("1 Pack: "));
		tfPack.setDocument(new FixedLengthDocument(3));
		tfPack.setFont(new Font("Times New Roman", Font.BOLD,20));
		tfPack.setText(0+"");
		contentPanel.add(tfPack,"growx");
		tfPackInc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add_user_32.png")));
		tfPackInc.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					int value = Integer.parseInt(tfPack.getText());
					value++;
					tfPack.setText(value+"");
				}
				catch(Exception e6){}
			}
			
		});
		tfPackDec.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/minus_32.png")));
		tfPackDec.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					int value = Integer.parseInt(tfPack.getText());
					if(value == 0)return;
					value--;
					tfPack.setText(value+"");
				}
				catch(Exception e1){}
			}
			
		});
		contentPanel.add(tfPackInc);
		contentPanel.add(tfPackDec,"wrap");
		
		
		contentPanel.add(new JLabel("10 Pack: "));
		tfPack10.setDocument(new FixedLengthDocument(3));
		tfPack10.setFont(new Font("Times New Roman", Font.BOLD,20));
		tfPack10.setText(0+"");
		contentPanel.add(tfPack10,"growx");
		tfPack10Inc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add_user_32.png")));
		tfPack10Inc.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					int value = Integer.parseInt(tfPack10.getText());
					value++;
					tfPack10.setText(value+"");
				}
				catch(Exception e3){}
			}
			
		});
		tfPack10Dec.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/minus_32.png")));
		tfPack10Dec.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					int value = Integer.parseInt(tfPack10.getText());
					if(value == 0)return;
					value--;
					tfPack10.setText(value+"");
				}
				catch(Exception e4){}
			}
			
		});
		contentPanel.add(tfPack10Inc);
		contentPanel.add(tfPack10Dec, "wrap");
		
		contentPanel.add(new JLabel("Karten: "));
		tfKarten.setDocument(new FixedLengthDocument(3));
		tfKarten.setFont(new Font("Times New Roman", Font.BOLD,20));
		tfKarten.setText(0+"");
		contentPanel.add(tfKarten,"growx");
		tfKartenInc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add_user_32.png")));
		tfKartenInc.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					int value = Integer.parseInt(tfKarten.getText());
					value++;
					tfKarten.setText(value+"");
				}
				catch(Exception e5){}
			}
			
		});
		tfKartenDec.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/minus_32.png")));
		tfKartenDec.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					int value = Integer.parseInt(tfKarten.getText());
					if(value == 0)return;
					value--;
					tfKarten.setText(value+"");
				}
				catch(Exception e5){}
			}
			
		});
		contentPanel.add(tfKartenInc);
		contentPanel.add(tfKartenDec, "wrap");
		
		JPanel imagePanel = new JPanel();
		imagePanel.setBackground(new Color(209,222,235));
		JLabel lblImage = new JLabel();
		lblImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/thermorollen.png")));
		imagePanel.add(lblImage);
		
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new MigLayout());
		infoPanel.setBackground(new Color(209,222,235));
		JLabel lbl = new JLabel("Bemerkungen:");
		lbl.setFont(new Font("Times New Roman",Font.BOLD, 20));
		infoPanel.add(lbl,"wrap");
		infoPanel.add(tfInfo,"wrap");
		sendButton.setBackground(new Color(102,255,102));
		sendButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				sendEmail();
				setCanceled(false);
				dispose();
			}
		});
		cancelButton.setBackground(new Color(255,153,153));
		cancelButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setCanceled(true);
				dispose();
			}
			
		});
		
		
		JPanel qwertyPanel = new JPanel();
		qwertyPanel.setBackground(new Color(209,222,235));
		
		qwertyPanel.setLayout(new MigLayout());
		qwertyPanel.add(new QwertyKeyPad(),"wrap"); 
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(209,222,235));
		buttonPanel.add(sendButton);
		buttonPanel.add(cancelButton);
		qwertyPanel.add(buttonPanel);
		
		JPanel southPanel = new JPanel();
		southPanel.setBackground(new Color(209,222,235));
		southPanel.setLayout(new BorderLayout());
		southPanel.add(infoPanel, BorderLayout.NORTH);
		southPanel.add(qwertyPanel,BorderLayout.SOUTH);
		
		getContentPane().add(imagePanel,BorderLayout.WEST);
		getContentPane().add(contentPanel,BorderLayout.CENTER);
		getContentPane().add(southPanel,BorderLayout.SOUTH);
	}

	public void sendEmail()
	{
		final String username = "www.khana.de@gmail.com";
		final String password = "CH03ms23";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("www.khana.de@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse("www.khana.de@gmail.com"));
			String email = RestaurantDAO.getRestaurant().getEmail();
			message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(email));
			message.setSubject("Bon Bestellung von:  "+ RestaurantDAO.getRestaurant().getName());
			
			String msg = "Sehr geehrte Kundin, sehr geehrter Kunde, \n \n Ihre Bestellung ist bei uns eingegangen \n \n Folgende sind die Bestellangaben";
			if(Integer.parseInt(tfPack.getText()) > 0)
					msg = msg +    "   1 Pack:"+ tfPack.getText() + "\n";
			if(Integer.parseInt(tfPack10.getText()) > 0)
					msg = msg +"   10 Pack:" + tfPack10.getText() + "\n";
			if(Integer.parseInt(tfKarten.getText()) > 0)
					msg = msg +"   Karton:" + tfKarten.getText() + "\n";
			if(tfInfo.getText().length() > 0)
			{
					msg = msg +    "\nBemerkungen: \n"
							  +    tfInfo.getText() + "\n";
			}
			msg = msg + "\n\nRegards, \n"+ RestaurantDAO.getRestaurant().getName();
					
		   message.setText(msg);

			Transport.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
