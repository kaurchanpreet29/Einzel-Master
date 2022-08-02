package com.floreantpos.config.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.POSConstants;
import com.floreantpos.config.TerminalConfig;

import net.miginfocom.swing.MigLayout;

public class ftpConfigurationView extends ConfigurationView {
	
	private JTextField tfUserName = new JTextField(30);
	private JPasswordField tfPassword = new JPasswordField(30);
	private JLabel lblUserName = new JLabel("FTP Kontoname");
	private JLabel lblPassword = new JLabel("FTP Passwort");
	private JCheckBox cbFtp = new JCheckBox("FTP Service");
	private JCheckBox cbOnline = new JCheckBox("Onlineservice");
	private JLabel lblServer = new JLabel("FTP Server");
	private JTextField tfServer = new JTextField(30);
	private JLabel lblPath = new JLabel("FTP Pfad");
	private JLabel lblPathSec = new JLabel("FTP Sekundaerpfad");
	private JTextField tfPath = new JTextField(30);
	private JTextField tfPathSec = new JTextField(30);

	private JLabel lblKAPath = new JLabel("FTP Keep Alive Path");
	private JTextField tfKAPath = new JTextField(30);
	
	public ftpConfigurationView() {
		super();
		
		initComponents();
	}
	
	private void initComponents() {
		setLayout(new MigLayout()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		setBackground(new Color(209,222,235));
		
		JPanel FtpServicePanel = new JPanel(new MigLayout());
		FtpServicePanel.setBorder(BorderFactory.createTitledBorder("FTP Service"));
		
		JLabel label_warning = new JLabel("Hier Bitte keine Aenderungen vornehmen!!!");
		if(StringUtils.isNotEmpty(POSConstants.Aenderungen_vornehmen))
			label_warning.setText(POSConstants.Aenderungen_vornehmen);
		
		label_warning.setFont(new Font("Times New Roman", Font.BOLD, 16));
		label_warning.setForeground(Color.RED);
		add(label_warning,"wrap");   
		FtpServicePanel.add(lblUserName);
		FtpServicePanel.add(tfUserName,"wrap");
		FtpServicePanel.add(lblPassword);
		FtpServicePanel.add(tfPassword,"wrap");
		FtpServicePanel.add(lblServer);
		FtpServicePanel.add(tfServer,"wrap");
		FtpServicePanel.add(lblPath);
		FtpServicePanel.add(tfPath,"wrap");
		FtpServicePanel.add(lblPathSec);
		FtpServicePanel.add(tfPathSec,"wrap");
		FtpServicePanel.add(lblKAPath);
		FtpServicePanel.add(tfKAPath,"wrap");
		FtpServicePanel.setBackground(new Color(209,222,235));
		FtpServicePanel.add(cbFtp);
		cbFtp.setBackground(new Color(209,222,235));
		cbOnline.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(cbOnline.isSelected()) {
					TerminalConfig.setOnline(true);
				} 
				else {
					TerminalConfig.setOnline(false);
				}
			}
		});
		cbOnline.setBackground(new Color(209,222,235));
		FtpServicePanel.add(cbOnline,"wrap");
		add(FtpServicePanel,"grow");
	}
	
	@Override
	public boolean save() throws Exception {
		
		TerminalConfig.setFtpUserName(tfUserName.getText());
		TerminalConfig.setFtpPassword(new String(tfPassword.getPassword()));
		TerminalConfig.setFtpServer(new String(tfServer.getText()));
		TerminalConfig.setFtpPath(new String(tfPath.getText()));
		TerminalConfig.setFtpPathSec(new String(tfPathSec.getText()));
		TerminalConfig.setFtpKAPath(new String(tfKAPath.getText()));
		TerminalConfig.setFtp(cbFtp.isSelected());
		return true;
	}

	@Override
	public void initialize() throws Exception {
		tfUserName.setText(TerminalConfig.getFtpUserName());
		tfPassword.setText(TerminalConfig.getFtpPassword());
		tfServer.setText(TerminalConfig.getFtpServer());
		tfPath.setText(TerminalConfig.getFtpPath());
		tfPathSec.setText(TerminalConfig.getFtpPathSec());
		tfKAPath.setText(TerminalConfig.getFtpKAPath());
		cbFtp.setSelected(TerminalConfig.isFtpEnabled());
		cbOnline.setSelected(TerminalConfig.isOnlineEnabled());
		setInitialized(true);
	}
	
	@Override
	public String getName() {
		return "Online bestellen";
	}

}

