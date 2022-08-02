package com.floreantpos.ui.dialog;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.main.Application;
import com.floreantpos.report.PrintSalesReport;
import com.floreantpos.ui.util.UiUtil;

public class ShowSalesReportDialog extends JDialog{

	JLabel startdate = new JLabel("Datum:");
	private org.jdesktop.swingx.JXDatePicker dpStartDate;
	
	JLabel lblId = new JLabel("(oder) Id");
	JTextField tfId = new JTextField(10);
	JButton btnPrint = new JButton("Druck"); 
	public ShowSalesReportDialog()
	{
		super(BackOfficeWindow.getInstance(),true);
		init();
	}
	
	public void init()
	{
		setLayout(new MigLayout());
		setTitle("Tages Abschluss Archiv");
		getContentPane().setBackground(new Color(209,222,235));
		dpStartDate = UiUtil.getCurrentMonthStart();
        dpStartDate.getMonthView().setFont(new Font("Times New Roman", Font.PLAIN, 28));
        dpStartDate.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        dpStartDate.setDate(new Date());
        btnPrint.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        btnPrint.setBackground(new Color(102,255,102));
        btnPrint.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					
					if(tfId.getText() != null && tfId.getText().length() > 0)
						PrintSalesReport.PrintDataFromDb(Integer.parseInt(tfId.getText()), dpStartDate.getDate());
					else
						PrintSalesReport.PrintDataFromDb(0, dpStartDate.getDate());
				}
				catch(Exception e){e.printStackTrace();}
			}
        	
        });
        add(startdate);
        add(dpStartDate);
        add(lblId);
        add(tfId,"wrap");
        add(btnPrint);
    }
}
