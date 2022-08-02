package com.floreantpos.report;

import java.util.Date;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JRViewer;

public abstract class Report {
	public static final int REPORT_TYPE_1 = 0;
	public static final int REPORT_TYPE_2 = 1;

	private Date startDate;
	private Date endDate;
	public String input;
	public String dineincashfood;
	public String dineincashdrink;
	public String dineincardfood;
	public String dineincarddrink;
	public String homecashfood;
	public String homecashdrink;
	public String homecardfood;
	public String homecarddrink;
	public String invoices;
	public String items;
	public String salesid;
	public String time;
	public int type;
	public JasperPrint jasperprint;
	private boolean vorschau;
	public int reportType = REPORT_TYPE_1;
	protected net.sf.jasperreports.swing.JRViewer viewer;
	private boolean jaherAbs = false;

	public boolean isJaherAbs() {
		return jaherAbs;
	}
	public void setJaherAbs(boolean jaherAbs) {
		this.jaherAbs = jaherAbs;
	}

	public abstract void refresh() throws Exception;
	public abstract void refresh(boolean print) throws Exception;

	public abstract boolean isDateRangeSupported();

	public abstract boolean isTypeSupported();
	
	public net.sf.jasperreports.swing.JRViewer getViewer() {
		return viewer;
	}

	public Date getEndDate() {
		if(endDate == null) {
			return new Date();
		}
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getReportType() {
		return reportType;
	}

	public void setReportType(int reportType) {
		
		this.reportType = reportType;
	}

	public int getType()
	{
		return type;
	}
	public void setType(int type)
	{
		this.type = type;
	}
	
	public String getInput()
	{
		return input;
	}
	public void setJasperPrint(JasperPrint print)
	{
		this.jasperprint =print;
	}
	public JasperPrint getJasperPrint()
	{
		return jasperprint;
	}
	public void setInput(String input,String A,String B,String C,String D, String E,String F, String G,String H,String I,String J,String K,
			String L)
	{
		this.input = input.replace(',', '.');
		this.dineincashfood = A.replace(',', '.');
		this.dineincashdrink = B.replace(',', '.');
		this.dineincardfood = C.replace(',', '.');
		this.dineincarddrink = D.replace(',', '.');
		this.homecashfood = E.replace(',', '.');
		this.homecashdrink = F.replace(',', '.');
		this.homecardfood = G.replace(',', '.');
		this.homecarddrink = H.replace(',', '.');
		this.invoices = I;
		this.items = J;
		this.salesid = K;
		this.time = L;
	}
	public Date getStartDate() {
		if(startDate == null) {
			return new Date();
		}
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

}
