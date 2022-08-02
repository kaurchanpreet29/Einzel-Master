package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.views.TicketDetailView.PrintType;

public class BonDialog extends POSDialog{

	private PosButton bon1;
	private PosButton bon2;
	private PosButton bon3;
	private PosButton bon4;
	private PosButton btnCancel;
	public PrintType printType;
	private int bon;
	private boolean cancel = true;
	public BonDialog(int bon)
	{
		this.bon = bon;
		initComponents();
	}
	
	public boolean isCancelled()
	{
		return cancel;
	}
	public void setPrintType(PrintType type)
	{
		printType = type;
	}
	
	public PrintType getPrintType()
	{
		return printType;
	}
	public void initComponents()
	{
		getContentPane().setBackground(new Color(209,222,235));
		
		setTitle("Mehr ausdrucke");
		setLayout(new BorderLayout());
		JPanel panel = new JPanel();
		panel.setBackground(new Color(209,222,235));
		panel.setLayout(new MigLayout());
		bon1 = new PosButton("BON1");
		if(bon == 1)
			bon1.setBackground(new Color(102,255,102));
		else
			bon1.setBackground(new Color(255,255,204));
		bon1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				setPrintType(PrintType.REGULAR);
				cancel = false;
				dispose();
			}
		});
		panel.add(bon1);
		bon2 = new PosButton("BON2");
		if(bon == 2)
			bon2.setBackground(new Color(102,255,102));
		else
			bon2.setBackground(new Color(255,255,204));
		bon2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				setPrintType(PrintType.REGULAR2);
				cancel = false;
				dispose();
			}
		});
		panel.add(bon2);
		bon3 = new PosButton("BON3");
		if(bon == 3)
			bon3.setBackground(new Color(102,255,102));
		else
			bon3.setBackground(new Color(255,255,204));
		bon3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				setPrintType(PrintType.REGULAR3);
				cancel = false;
				dispose();
			}
		});
		panel.add(bon3);
		bon4 = new PosButton("BON4");
		if(bon == 4)
			bon4.setBackground(new Color(102,255,102));
		else
			bon4.setBackground(new Color(255,255,204));
		bon4.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				setPrintType(PrintType.REGULAR4);
				cancel = false;
				dispose();
			}
		});
		panel.add(bon4,"wrap");
		btnCancel = new PosButton("ABBRECHEN");
		btnCancel.setBackground(new Color(255,153,153));
		btnCancel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				cancel = true;
				dispose();
			}
		});
		getContentPane().add(panel,BorderLayout.NORTH);
		getContentPane().add(btnCancel,BorderLayout.SOUTH);
	}
	
}
