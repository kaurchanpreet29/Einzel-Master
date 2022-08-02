package com.floreantpos.customer;

import java.util.List;

import com.floreantpos.bo.ui.explorer.ListTableModel;
import com.floreantpos.model.Customer;

public class CustomerListTableModel extends ListTableModel<Customer> {
	private final static String[] columns = {"Nr.","Firma", "Name", "Anschift","Tel."};

	public CustomerListTableModel() {

		super(columns);
	}
	
	public CustomerListTableModel(List<Customer> customers) {
		super(columns, customers);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		Customer customer = getRowData(rowIndex);

		switch (columnIndex) {
		case 0:
			return customer.getLoyaltyNo();
		case 1:
			return customer.getFirmName();
		case 2:			
			return customer.getName();
		case 3:	
			if(customer.getDoorNo()!=null&&customer.getDoorNo().length()>1)
				return customer.getAddress()+" "+customer.getDoorNo()+", "+customer.getZipCode()+" "+customer.getCity();
			else
				return customer.getAddress()+" "+customer.getDoorNo()+" "+customer.getZipCode()+" "+customer.getCity();
		case 4:			
			return customer.getTelephoneNo();

		}
		return null;
	} 
}
