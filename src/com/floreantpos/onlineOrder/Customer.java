package com.floreantpos.onlineOrder;

public class Customer {
	private String customerNo;
	private DeliveryAddress deliveryAddress;
	
	public void setCustomerNo(String customerNo)
	{
		this.customerNo = customerNo;
	}
	
	public String getCustomerNo()
	{
		return customerNo;
	}
	
	public void setDeliveryAddress(DeliveryAddress deliveryAddress)
	{
		this.deliveryAddress = deliveryAddress;
	}
	
	public DeliveryAddress getDeliveryAddress()
	{
		return deliveryAddress;
	}
}
