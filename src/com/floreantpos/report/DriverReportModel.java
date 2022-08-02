package com.floreantpos.report;

import java.text.DecimalFormat;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.floreantpos.main.Application;
import com.floreantpos.util.NumberUtil;

public class DriverReportModel extends AbstractTableModel {

	
	
	private String[] columnNames = {"driverName1","driverAmount1","driverName2","driverAmount2","driverName3","driverAmount3","driverName4","driverAmount4","driverName5","driverAmount5","driverName6","driverAmount6","driverName7","driverAmount7","driverName8","driverAmount8","driverName9","driverAmount9","driverDelivery1","driverDelivery2","driverDelivery3","driverDelivery4","driverDelivery5","driverDelivery6","driverDelivery7","driverDelivery8","driverDelivery9","totalAmount","totalNr"};

	private List<DriverData> data;
	
	public DriverReportModel() {
		super();
		
	}

	public int getRowCount() {
		if(data == null) {
			return 0;
		}
		
		return data.size();
	}

	public int getColumnCount() {
		return columnNames.length;
	}
	
	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		DriverData driverData = data.get(rowIndex);
		
		switch(columnIndex) {
		
			case 0:
				return driverData.getDriverName1();
				
			case 1:
				return driverData.getDriverAmount1();
				
			case 2:
				return driverData.getDriverName2();
				
			case 3:
				return driverData.getDriverAmount2();
				
			case 4:
				return driverData.getDriverName3();
				
			case 5:
				return driverData.getDriverAmount3();
				
			case 6: 
				return driverData.getDriverName4();
			case 7:
				return driverData.getDriverAmount4();
			case 8:
				return driverData.getDriverName5();
			case 9:
				return driverData.getDriverAmount5();
				
			case 10:
				return driverData.getDriverName6();
			case 11:
				return driverData.getDriverAmount6();
			case 12:
				return driverData.getDriverName7();
			case 13:
				return driverData.getDriverAmount7(); 
		
			case 14:
				return driverData.getDriverName8();
		
			case 15:
				return driverData.getDriverAmount8();
			case 16:
				return driverData.getDriverName9();
			case 17:
				return driverData.getDriverAmount9();
			case 18:
				return driverData.getDriverDelivery1();
			case 19:
				return driverData.getDriverDelivery2();
			case 20:
				return driverData.getDriverDelivery3();
			case 21:
				return driverData.getDriverDelivery4();
			case 22:
				return driverData.getDriverDelivery5();
			case 23:
				return driverData.getDriverDelivery6();
			case 24:
				return driverData.getDriverDelivery7();
			case 25:
				return driverData.getDriverDelivery8();
			case 26:
				return driverData.getDriverDelivery9();
			case 27:
				return driverData.getTotalAmount();
			case 28:
				return driverData.getTotalNr();
			}
		
		
		return null;
	}

	public List<DriverData> getItems() {
		return data;
	}

	public void setItems(List<DriverData> data) {
		this.data = data;
	}
}
