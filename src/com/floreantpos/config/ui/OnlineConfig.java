package com.floreantpos.config.ui;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.config.TerminalConfig;

public class OnlineConfig extends OnlineConfigurationView {
	private JTextField tfUserName = new JTextField(30);
	private JPasswordField tfPassword = new JPasswordField(30);
	private JLabel lblUserName = new JLabel("FTP Konto Name");
	private JLabel lblPassword = new JLabel("FTP Passwort");
	private JLabel lblServer = new JLabel("FTP Server");
	private JTextField tfServer = new JTextField(30);
	private JLabel lblPath = new JLabel("FTP Path Shop 1");
	private JLabel lblSecPath = new JLabel("FTP Path Shop 2");
	private JTextField tfPath = new JTextField(30);
	private JTextField tfSecPath = new JTextField(30);
	public OnlineConfig() {
		super();
		
		initComponents();
	}
	
	private void initComponents() {
		setLayout(new MigLayout()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		setBackground(new Color(209,222,235));
		
		JPanel FtpServicePanel = new JPanel(new MigLayout());
		FtpServicePanel.setBorder(BorderFactory.createTitledBorder("FTP DIENSTLEISTUNG"));
		
		FtpServicePanel.add(lblUserName);
		FtpServicePanel.add(tfUserName,"wrap");
		FtpServicePanel.add(lblPassword);
		FtpServicePanel.add(tfPassword,"wrap");
		FtpServicePanel.add(lblServer);
		FtpServicePanel.add(tfServer,"wrap");
		FtpServicePanel.add(lblPath);
		FtpServicePanel.add(tfPath,"wrap");
		FtpServicePanel.add(lblSecPath);
		FtpServicePanel.add(tfSecPath,"wrap");
		FtpServicePanel.setBackground(new Color(209,222,235));
		setBackground(new Color(209,222,235));
		add(FtpServicePanel);
	}
	
	@Override
	public boolean save() throws Exception {
		
		TerminalConfig.setFtpUserName(tfUserName.getText());
		TerminalConfig.setFtpPassword(new String(tfPassword.getPassword()));
		TerminalConfig.setFtpServer(new String(tfServer.getText()));
		TerminalConfig.setFtpMenuPath(new String(tfPath.getText()));
		TerminalConfig.setFtpMenuPathSec(new String(tfSecPath.getText()));
		
		return true;
	}

	@Override
	public void initialize() throws Exception {
		tfUserName.setText(TerminalConfig.getFtpUserName());
		tfPassword.setText(TerminalConfig.getFtpPassword());
		tfServer.setText(TerminalConfig.getFtpServer());
		tfPath.setText(TerminalConfig.getFtpMenuPath());
		tfSecPath.setText(TerminalConfig.getFtpMenuPathSec());
		setInitialized(true);
	}

	@Override
	public String getName() {
		return "FTP Konfig";
	}
}
