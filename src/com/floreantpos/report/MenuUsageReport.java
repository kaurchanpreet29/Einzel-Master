package com.floreantpos.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.bo.ui.explorer.ListTableModel;
import com.floreantpos.util.NumberUtil;

public class MenuUsageReport {
	private Date fromDate;

	private Date toDate;

	private Date reportTime;

	private List<MenuUsageReportData> reportDatas = new ArrayList<MenuUsageReportData>();

	private MenuUsageReportTableModel tableModel;

	public MenuUsageReportTableModel getTableModel() {
		Collections.sort(reportDatas, new MenuUsageReport.MenuUsageReportDataComparator());
		if (tableModel == null) {
			tableModel = new MenuUsageReportTableModel(reportDatas);
		}
		return tableModel;
	}

	public List<MenuUsageReportData> getReportDatas()
	{
		return reportDatas; 
	}
	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public void addReportData(MenuUsageReportData data) {
		reportDatas.add(data);
	}
	
	public void setReportData(List<MenuUsageReportData> reportDatas) {
	  this.reportDatas = reportDatas;
	}
	
	public static class MenuUsageReportDataComparator implements Comparator {
		public MenuUsageReportDataComparator(){}
	    public int compare(Object o1, Object o2) {
	      if (!(o1 instanceof MenuUsageReportData) || !(o2 instanceof MenuUsageReportData))
	        throw new ClassCastException();

	      MenuUsageReportData e1 = (MenuUsageReportData) o1;
	      MenuUsageReportData e2 = (MenuUsageReportData) o2;

	      return (int) (e2.getCount() - e1.getCount() );
      }
   }
	public static class MenuUsageReportData {
		private int count;
		private String categoryName;
		private double grossSales;
		private double discount;
		private double netSales;
		private double avgSales;
		private double profit;
		private Integer tax;
		private double costPercentage;
		private double percentage;

		public double getAvgSales() {
			return avgSales;
		}

		public void setAvgSales(double avgSales) {
			this.avgSales = avgSales;
		}

		public String getCategoryName() {
			return categoryName;
		}

		public void setCategoryName(String categoryName) {
			this.categoryName = categoryName;
		}

		public double getCostPercentage() {
			return costPercentage;
		}

		public void setCostPercentage(double costPercentage) {
			this.costPercentage = costPercentage;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public double getDiscount() {
			return discount;
		}

		public void setDiscount(double discount) {
			this.discount = discount;
		}

		public double getGrossSales() {
			return grossSales;
		}

		public void setGrossSales(double grossSales) {
			this.grossSales = grossSales;
		}

		public double getNetSales() {
			return netSales;
		}

		public void setNetSales(double netSales) {
			this.netSales = netSales;
		}

		public double getPercentage() {
			return percentage;
		}

		public void setPercentage(double percentage) {
			this.percentage = percentage;
		}

		public double getProfit() {
			return profit;
		}

		public void setProfit(double profit) {
			this.profit = profit;
		}

		public Integer getTax() {
      return tax;
    }

    public void setTax(Integer tax) {
      this.tax = tax;
    }

    public void calculate() {
			netSales = grossSales - discount;
			profit = netSales;
		}
	}

	public static class MenuUsageReportTableModel extends ListTableModel {

		public MenuUsageReportTableModel(List<MenuUsageReportData> datas) {
			super(new String[] { "category", "count", "grossSale", "discount", "netSale", "avgSale", "profit", "cost", "percentage" }, datas);
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			MenuUsageReportData data = (MenuUsageReportData) rows.get(rowIndex);

			switch (columnIndex) {
			case 0:
			  final String taxSuffix = data.getTax() != null ? " ("+ data.getTax() + "%)" : StringUtils.EMPTY;				
			 
			  try {
				  return data.getCategoryName() + taxSuffix;
				}catch(Exception ex) {
					return "";
				}

			case 1:
				try {
					return String.valueOf(data.getCount());
				}catch(Exception ex) {
					return "";
				}
				

			case 2:
				try {
					return NumberUtil.formatNumber(data.getGrossSales()-data.getDiscount())+" €";
				}catch(Exception ex) {
					return "";
				}

			case 3:
				try {
					return NumberUtil.formatNumber(data.getDiscount());
				}catch(Exception ex) {
					return "";
				}

			case 4:
				try {
					return NumberUtil.formatNumber(data.getNetSales());
				}catch(Exception ex) {
					return "";
				}
			case 5:
				return " ";//Application.formatNumber(data.getAvgSales());
			case 6:
				try {
					return NumberUtil.formatNumber(data.getProfit());
				}catch(Exception ex) {
					return "";
				}
			case 7:
				return " ";//Application.formatNumber(data.getCostPercentage());
			case 8:
				return " ";//Application.formatNumber(data.getPercentage());
			}

			return null;
		}

	}
}
