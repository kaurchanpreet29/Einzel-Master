package com.floreantpos.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.UIManager;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.ui.dialog.OpenDialog;
import com.khana.backup.KhanaZipper;

public class Einzel {
	private static final String DEVELOPMENT_MODE = "developmentMode";

	@SuppressWarnings("static-access")
	public static void main(String[] args) throws Exception {
		OpenDialog dialog = new OpenDialog();
		dialog.setUndecorated(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		int x = (screenSize.width / 2) - (dialog.getPreferredSize().width / 2);
		int y = (screenSize.height / 2) - (dialog.getPreferredSize().height / 2);

		dialog.setLocation(x, y);
		dialog.pack();
		dialog.setVisible(true);

		if(!TerminalConfig.isBuildMode()&&TerminalConfig.isRemoteBackup()) {
			doDBBackup();
		}

		if(TerminalConfig.isNoGUI()) {
			startDbServer();
			return;
		}else if(TerminalConfig.isKhanaServer()) {
			startDbServer();
		}


		Options options = new Options();
		options.addOption(DEVELOPMENT_MODE, true, "State if this is developmentMode");
		CommandLineParser parser = new BasicParser();
		CommandLine commandLine = parser.parse(options, args);
		String optionValue = commandLine.getOptionValue(DEVELOPMENT_MODE);

		UIManager.put("TabbedPane.background", 
				new javax.swing.plaf.ColorUIResource(35,35,36));
		UIManager.put("TabbedPane.selectedForeground",
				new javax.swing.plaf.ColorUIResource(new Color(0, 0, 204)));
		UIManager.put("ScrollBar.thumb", 
				new javax.swing.plaf.ColorUIResource(Color.GRAY));		

		try {
			ResetModifierIdenity resetModifier =new ResetModifierIdenity();
			resetModifier.reset();	       
		}catch(Exception ex) {

		}



		Application application = Application.getInstance();

		if(optionValue != null) {
			application.setDevelopmentMode(Boolean.valueOf(optionValue));
		}
		application.start();

		dialog.dispose();
	}


	private synchronized static boolean doDBBackup() {
		try {
			KhanaZipper zipper1 = new KhanaZipper();
			return zipper1.zipMyFolder();
		}catch(Throwable ex) {
			return false;
		}
	}

	public static void startDbServer() {
		try {
			System.out.println("Executing startDbServer.vbs");
			Runtime.getRuntime().exec("cmd /c start startDbServer.vbs");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
