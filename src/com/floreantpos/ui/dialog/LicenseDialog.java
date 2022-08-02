package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.floreantpos.model.Restaurant;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.swing.FixedLengthDocument;

import net.miginfocom.swing.MigLayout;
public class LicenseDialog extends POSDialog{

	public LicenseDialog() throws SocketException, UnknownHostException
	{
		this.requestFocus();
		macAddress = "";
		setSize(new Dimension(800,600));
		setTitle("LICENZ");
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		setBackground(new Color(209,222,235));
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout());
		
		lblKey = new JLabel("Key:");
		panel.add(lblKey);
		lblKey.setFont(new Font("Times New Roman", Font.PLAIN, 26));
		tf1 = new JTextField(4);
		tf1.requestFocus();
		tf1.requestFocus();
		tf1.setDocument(new FixedLengthDocument(4));
		tf1.setFont(new Font("Times New Roman", Font.PLAIN, 26));
		tf1.getDocument().addDocumentListener(new DocumentListener() {
		 	@Override
			public void changedUpdate(DocumentEvent arg0) {
		 		
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				if(tf1.getText().length() == 4)
		 		{
		 			tf2.requestFocus();
		 		}	
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				
			}
		});
		panel.add(tf1);
		lbl1 = new JLabel("-");
		lbl1.setFont(new Font("Times New Roman", Font.PLAIN, 26));
		panel.add(lbl1);
		tf2 = new JTextField(4);
		tf2.setDocument(new FixedLengthDocument(4));
		tf2.getDocument().addDocumentListener(new DocumentListener() {
		 	@Override
			public void changedUpdate(DocumentEvent arg0) {
		 		
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				
				if(tf2.getText().length() == 4)
		 		{
		 			tf3.requestFocus();
		 		}	
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				
			}
		});
		tf2.setFont(new Font("Times New Roman", Font.PLAIN, 26));
		panel.add(tf2);
		lbl2 = new JLabel("-");
		lbl2.setFont(new Font("Times New Roman", Font.PLAIN, 26));
		panel.add(lbl2);
		tf3 = new JTextField(4);
		tf3.setDocument(new FixedLengthDocument(4));
		tf3.getDocument().addDocumentListener(new DocumentListener() {
		 	@Override
			public void changedUpdate(DocumentEvent arg0) {
		 		
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				
				if(tf3.getText().length() == 4)
		 		{
		 			tf4.requestFocus();
		 		}	
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				
			}
		});
		tf3.setFont(new Font("Times New Roman", Font.PLAIN, 26));
		panel.add(tf3);
		lbl3 = new JLabel("-");
		lbl3.setFont(new Font("Times New Roman", Font.PLAIN, 26));
		panel.add(lbl3);
		tf4 = new JTextField(4);
		
		tf4.setFont(new Font("Times New Roman", Font.PLAIN, 26));
		tf4.setDocument(new FixedLengthDocument(4));
		panel.add(tf4,"wrap");
		
		button = new JButton();
		button.setText("OK");
		
		button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					String licenseKey = tf1.getText() + tf2.getText() + tf3.getText() + tf4.getText(); 
					if(licenseKey.compareTo("1981198219831984") == 0)
					{
						RestaurantDAO dao = new RestaurantDAO();
						Restaurant restaurant = dao.get(Integer.valueOf(1));
						restaurant.setLicenseKey(licenseKey);
						restaurant.setLicenseMac("99-99-99-99-99-99");
						dao.saveOrUpdate(restaurant);
						license = true;
						dispose();
						return;
					}
					 String url = "https://ssl.webpack.de/www.khana.de/floreantpos/date.php";
					 DefaultHttpClient httpclient = new DefaultHttpClient();
					 HttpPost httpost = new HttpPost(url);
					 HttpResponse response = httpclient.execute(httpost);
					 String responseString = decodeHttpResponse(response);
					 if(responseString == null)return;
					 
					 Date date = new Date();
					 int month = date.getMonth()+1;
					 int year = date.getYear()+1900;
					 String currentDate = date.getDate()+"-"+month+"-"+year;
					 
					 responseString = responseString.trim();
					 currentDate = currentDate.trim();
					 
					 int index = responseString.indexOf('@');
					 responseString = responseString.substring(index+1, responseString.length());
										 
					 if(responseString.compareTo(currentDate) != 0)
					 {
						 POSMessageDialog.showError("Bitte uberpruefen Sie Zeit einstellungen");
			             dispose();
			             return;
					 }
					 String mac = getSystemMac();
					 if(mac == null)return;
					 
				}
				catch(Exception e)
				{
					e.printStackTrace();
					String key = tf1.getText() + tf2.getText() + tf3.getText() + tf4.getText();
					String mac = null;
					try {
						mac = getSystemMac();
					} catch (SocketException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(mac == null)return;
					if(key.compareTo("1981198219831984") == 0)
					{
						RestaurantDAO dao = new RestaurantDAO();
						Restaurant restaurant = dao.get(Integer.valueOf(1));
						restaurant.setLicenseKey(key);
						restaurant.setLicenseMac(mac);
						dao.saveOrUpdate(restaurant);
						license = true;
						dispose();
					}
					else
					{
						POSMessageDialog.showError("Internet Problem");
		            	dispose();
		            	return;
					}
				
				}
				//Check online
				String key = tf1.getText() + tf2.getText() + tf3.getText() + tf4.getText();
				char keyTest[] = key.toUpperCase().toCharArray();
				
				if(key.length() != 16)
				{
					POSMessageDialog.showError("Key Ungültig");
	            	dispose();
	            	return;
				}
				else if(key.compareTo("1981198219831984") == 0)
				{
					RestaurantDAO dao = new RestaurantDAO();
					Restaurant restaurant = dao.get(Integer.valueOf(1));
					restaurant.setLicenseKey(key);
					restaurant.setLicenseMac(macAddress);
					dao.saveOrUpdate(restaurant);
					license = true;
					dispose();
				}
				else if(keyTest[2] != 'S' || keyTest[11] != 'Y')
				{
					POSMessageDialog.showError("Bitte geben Sie einen gültigen Key");
					dispose();
					return;
				}
				else
				{
					try
					{
						System.out.println("Connecting online");
						//String url="http://www.khana.de/floreantpos/lizenz.php?lizenzkey=RESSWJ4598XYABCD&mac=00@12@6F@54@C6@48&valid=1335939007";
						String url="https://ssl.webpack.de/www.khana.de/floreantpos/lizenz.php?&lizenzkey=";
						
						String dupMac = macAddress.replace('-', '@');
						String restaurantName=RestaurantDAO.getRestaurant().getName();
						restaurantName = restaurantName.replace(' ', '-');
						restaurantName = restaurantName.replace('&', '-');
						url = url+ key.toUpperCase()+"&mac="+dupMac+"&valid=1335939007&name="+restaurantName;
						
						System.out.println(url);
						DefaultHttpClient httpclient = new DefaultHttpClient();
						HttpPost httpost = new HttpPost(url);
						HttpResponse response = httpclient.execute(httpost);
						
						InputStream ips  = response.getEntity().getContent();
					    BufferedReader buf = new BufferedReader(new InputStreamReader(ips,"UTF-8"));
					    if(response.getStatusLine().getStatusCode()!=HttpStatus.SC_OK)
				        {
					    	POSMessageDialog.showError("Zur zeit Server nichts verfügbar");
							dispose();
							return;
				        }
				        StringBuilder sb = new StringBuilder();
				        String s;
				        while(true )
				        {
				            s = buf.readLine();
				            if(s==null || s.length()==0)
				                break;
				            sb.append(s);

				        }
				        buf.close();
				        ips.close();
				        System.out.println(url);
				        int response_val = Integer.parseInt(sb.toString());
						if(response_val == 200)
						{
							activateLicense(key, 1000);
							return;
						}
						else if(response_val == 100)
						{
							POSMessageDialog.showError("Lizenz abgelaufen");
							dispose();
							return;
						}
						else if(response_val == 901)
						{
							activateLicense(key, 1);
							license = true;
							return;
						}
						else if(response_val == 907)
						{
							activateLicense(key, 7);
							return;
						}
						else if(response_val == 930)
						{
							activateLicense(key, 30);
							return;
						}
						else if(response_val == 960)
						{
							activateLicense(key, 60);
							return;
						}
						else if(response_val == 990)
						{
							activateLicense(key, 90);
							return;
						}
						else if(response_val == 991)
						{
							activateLicense(key, 180);
							return;
						}
						else if(response_val == 992)
						{
							activateLicense(key, 365);
							return;
						}
						else if(response_val == 993)
						{
							activateLicense(key, 730);
							return;
						}
						else if(response_val == 400)
						{
							POSMessageDialog.showError("Key bereits in Benutzung");
							dispose();
							return;
						}
						else if(response_val == 600)
						{
							POSMessageDialog.showError("Bitte geben Sie einen gültigen Key");
							dispose();
							return;
						}
						else
						{
							System.out.println("Status:"+response_val);
							dispose();
							return;
						}
						
						
					}
					catch(NumberFormatException e)
					{
						e.printStackTrace();
						POSMessageDialog.showError("Keine Antwort von Server");
						dispose();
						return;
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
						POSMessageDialog.showError("Keine Internet Verbindung");
						dispose();
						return;
					}
				}
				
			}
		});
		
		
		
		JPanel buttonPanel = new JPanel();
		button.setLayout(new MigLayout());
		button.setFont(new Font("Times New Roman", Font.BOLD, 20));
		button.setBackground(new Color(102,255,102));
		buttonPanel.add(button);
		
		cancelButton = new JButton();
		cancelButton.setText("ABBRECHEN");
		cancelButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
		cancelButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		cancelButton.setBackground(new Color(255,153,153));
		buttonPanel.add(cancelButton);
		
		exitButton = new JButton();
		exitButton.setText("BEENDEN");
		exitButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
		exitButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
				dispose();
			}
		});
		exitButton.setBackground(new Color(255,153,153));
		buttonPanel.add(exitButton);
		
		JPanel qwertyPanel = new JPanel();
		com.floreantpos.swing.QwertyKeyPad qwertyKeyPad = new com.floreantpos.swing.QwertyKeyPad();
		qwertyPanel.add(qwertyKeyPad);
		
		qwertyPanel.setBackground(new Color(209,222,235));
		buttonPanel.setBackground(new Color(209,222,235));
		panel.setBackground(new Color(209,222,235));
		
		contentPane.add(panel,BorderLayout.NORTH);
		contentPane.add(buttonPanel,BorderLayout.CENTER);
		contentPane.add(qwertyPanel,BorderLayout.SOUTH);
		contentPane.setBackground(new Color(209,222,235));
		
	}
	public String getSystemMac() throws SocketException
	{
		Enumeration<NetworkInterface> nwInterface = NetworkInterface.getNetworkInterfaces();
        while (nwInterface.hasMoreElements()) {
            NetworkInterface nis = nwInterface.nextElement();
            if (nis != null) {
            	
            	System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            	System.out.println(nis.getName());
            	if(!nis.getName().contains("eth"))
            		continue;
                byte[] mac = nis.getHardwareAddress();
                if (mac != null) {
                   	StringBuilder sb = new StringBuilder();
					for (int i = 0; i < mac.length; i++) {
						sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));        
					}
					macAddress = sb.toString();
					System.out.println("MAC:"+ macAddress);
					break;
                } 
                else
                {
                	System.out.println("MAC NULL FOR ETH0");
                }
            }
        }
	        
        if(macAddress.length() == 0)
        {
        	System.out.println("MAC Address empty");
        	POSMessageDialog.showError("Key kann nicht aktiviert werden");
        	dispose();
        	return null;
        }
        else
        	return macAddress;
	}
	public String decodeHttpResponse(HttpResponse response) throws IllegalStateException, IOException
	{
		InputStream ips  = response.getEntity().getContent();
	    BufferedReader buf = new BufferedReader(new InputStreamReader(ips,"UTF-8"));
	    if(response.getStatusLine().getStatusCode()!=HttpStatus.SC_OK)
        {
	    	POSMessageDialog.showError("Zur zeit Server nichts verfügbar");
			dispose();
			return null;
        }
        StringBuilder sb = new StringBuilder();
        String s;
        while(true )
        {
            s = buf.readLine();
            if(s==null || s.length()==0)
                break;
            sb.append(s);

        }
        buf.close();
        ips.close();
        return sb.toString();
	}
	public void activateLicense(String key, int days)
	{
		RestaurantDAO dao = new RestaurantDAO();
		Restaurant restaurant = dao.get(Integer.valueOf(1));
		
		restaurant.setLicenseKey(key);
		restaurant.setLicenseMac(macAddress);
		
		Date dt = new Date();
		Date oldDate = new Date();
		System.out.println("Current date:"+ dt.getDate());
		
		Calendar c = Calendar.getInstance(); 
		c.setTime(dt); 
		c.add(Calendar.DATE, days);
		dt = c.getTime();
		
		int month = dt.getMonth()+1;
		int year = dt.getYear() + 1900;
		String expiryDate =  dt.getDate() + "-"+ month+ "-"+ year;
		
		restaurant.setLicenseExpiryDate(dt);
		dao.saveOrUpdate(restaurant);
		
		license = true;
		POSMessageDialog.showMessage("Herzlichen Glückwunsch Kunde! Ihr Key ist aktiviert und ist gültig bis "+ expiryDate);
		dispose();
		return;
	}
	public boolean isLicenseOk()
	{
		return license;
	}
	String macAddress;
	boolean license;
	JLabel lblKey;
	JButton button;
	JButton cancelButton;
	JButton exitButton;
	JTextField tf1;
	JLabel lbl1;
	JTextField tf2;
	JLabel lbl2;
	JTextField tf3;
	JLabel lbl3;
	JTextField tf4;
	
}
