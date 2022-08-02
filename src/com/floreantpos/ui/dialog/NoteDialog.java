package com.floreantpos.ui.dialog;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.floreantpos.swing.PosButton;

import net.miginfocom.swing.MigLayout;

public class NoteDialog extends PaperBuyDialog{

	JTextArea tfArea;
	String text;
	public NoteDialog(String text)
	{
		initComponents();
	}
	
	public void initComponents()
	{
		getContentPane().setBackground(new Color(209,222,235));
		getContentPane().setLayout(new MigLayout());
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(209,222,235));
		panel.setLayout(new MigLayout());
		tfArea = new JTextArea();
		tfArea.setText(text);
		panel.add(new JLabel("Bemerkung"));
		panel.add(tfArea);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(209,222,235));
		buttonPanel.setLayout(new MigLayout());
		PosButton okButton = new PosButton("OK");
		okButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				setCanceled(false);
				dispose();
			}
			
		});
		buttonPanel.add(okButton);
		PosButton cancelButton = new PosButton("ABBRECHEN");
		cancelButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
			
		});
		buttonPanel.add(cancelButton);
		
		getContentPane().add(panel,"wrap");
		getContentPane().add(buttonPanel);
	}
}
