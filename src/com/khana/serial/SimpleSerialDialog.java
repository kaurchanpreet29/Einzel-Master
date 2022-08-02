package com.khana.serial;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.swing.DoubleTextField;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.khana.weight.management.ScaleWeight;

import gnu.io.NoSuchPortException;
import net.miginfocom.swing.MigLayout;

public class SimpleSerialDialog extends POSDialog{
	
private static FixedLengthTextField tfGiftCertNumber;
private DoubleTextField tfFaceValue;
public ScaleWeight serial;
public SimpleSerialDialog(JDialog parent) {	
	super(parent, true);
	
	setTitle("Kana Weight Machine");
	
	TitlePanel titlePanel = new TitlePanel();
	titlePanel.setTitle("Take Weight of object");
	getContentPane().add(titlePanel, BorderLayout.NORTH);
	
	JPanel panel = new JPanel();
	getContentPane().add(panel, BorderLayout.CENTER);
	panel.setLayout(new MigLayout("", "[][grow]", "[][]"));
	
	JLabel lblGiftCertificateNumber = new JLabel("Weight in KG");
	panel.add(lblGiftCertificateNumber, "cell 0 0,alignx trailing");
	
	tfGiftCertNumber = new FixedLengthTextField(64);
	panel.add(tfGiftCertNumber, "cell 1 0,growx");	
	
	JPanel panel_1 = new JPanel();
	getContentPane().add(panel_1, BorderLayout.SOUTH);
	
	PosButton psbtnOk = new PosButton();
	psbtnOk.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {			
		serial = new ScaleWeight().getInstance();
		}
	});
	psbtnOk.setText("Start");
	panel_1.add(psbtnOk);
	
	PosButton psbtnCancel = new PosButton();
	psbtnCancel.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {			
			serial.close();
			dispose();
		}
	});
	psbtnCancel.setText("End");
	
	
	PosButton psbtnWeight = new PosButton();
	psbtnWeight.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			serial.reqImmediateWeights();
//			serial.reqStableBasicWeights();
//			serial.reqStableWeights();
		}
	});
	
	psbtnWeight.setText("Weight");
	psbtnOk.setText("Start");
	panel_1.add(psbtnOk);
	panel_1.add(psbtnWeight);
	panel_1.add(psbtnCancel);	
}

public String getGiftCertNumber() {
	return tfGiftCertNumber.getText();
}

public static void setWeight(String value) {
	tfGiftCertNumber.setText(value);
}

public double getGiftCertFaceValue() {
	return tfFaceValue.getDouble();
}

public static void main(String[] args)
{
	SimpleSerialDialog dialog = new SimpleSerialDialog(new JDialog());
	dialog.pack();
	dialog.open();	
  
};
}
