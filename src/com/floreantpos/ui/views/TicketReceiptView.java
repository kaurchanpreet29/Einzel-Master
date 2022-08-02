package com.floreantpos.ui.views;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

import net.sf.jasperreports.engine.JasperPrint;

public class TicketReceiptView extends JPanel {
	net.sf.jasperreports.swing.JRViewer jrViewer;

	public TicketReceiptView(JasperPrint jasperPrint) {
		setLayout(new BorderLayout());
		setBackground(new Color(209,222,235));
		jrViewer = new net.sf.jasperreports.swing.JRViewer(jasperPrint);
		jrViewer.setToolbarVisible(false);
		jrViewer.setStatusbarVisible(false);
		jrViewer.setBackground(new Color(209,222,235));
		jrViewer.getReportPanel().setBackground(new Color(209,222,235));
		add(jrViewer);
	}
	
	public JPanel getReportPanel() {
		return jrViewer.getReportPanel();
	}
}
