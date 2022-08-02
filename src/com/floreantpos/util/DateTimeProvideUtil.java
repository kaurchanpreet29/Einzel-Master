package com.floreantpos.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JLabel;

import com.floreantpos.add.service.AnimationLoader;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.dialog.POSDialog;

import net.miginfocom.swing.MigLayout;

public class DateTimeProvideUtil extends POSDialog{
	/**
	 * Date Time Chooser dialog
	 */
	private static final long serialVersionUID = -5502191019217165385L;
	private TransparentPanel loderPane;
	private DateTimePicker DatePickerVon = new DateTimePicker();	
	private DateTimePicker DatePickerBis = new DateTimePicker();
	private Date startDate;
	private Date endDate;

	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getDateTime() {
		return startDate;
	}
	public void setDateTime(Date dateTime) {
		this.startDate = dateTime;
	}
	
	public DateTimeProvideUtil() {
		setBackground(new Color(255,255,255));
		setLayout(new BorderLayout());
		DatePickerVon.setDate(new Date());
		DatePickerBis.setDate(new Date());
		loderPane = new TransparentPanel();
		loderPane.setOpaque(false);
		loderPane.setLayout(new MigLayout("alignx 50%"));
		loderPane.setOpaque(false);		
		PosButton btnOk = new PosButton("  OK ");
		btnOk.setBackground(Color.GREEN);
		btnOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setCanceled(false);
				startDate = DatePickerVon.getDate();
				endDate = DatePickerBis.getDate();
				dispose();				
			}
		});
		PosButton btnCancel = new PosButton("ZUM ANFANGS");
		btnCancel.setBackground(Color.BLUE);
		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				startDate = new Date();
				setCanceled(true);
				dispose();
			}
		});
		loderPane.add(new Label("Von:   "), "growx");
		loderPane.add(DatePickerVon, "growx");
		loderPane.add(new Label("Bis:   "), "growx");
		loderPane.add(DatePickerBis, "wrap");
		loderPane.add(btnOk, "growx");
		loderPane.add(btnCancel, "growx, wrap");	    
		getContentPane().add(new JLabel("Bitte Waehlen Sie Datum ein"), BorderLayout.NORTH);
		getContentPane().add(loderPane, BorderLayout.CENTER);	
	}
}