package com.floreantpos.report;

public class ServerData {
	String name;
	Double totalAmount;
	int totalOrders;
	public void setTotalAmount(Double totalAmount)
	{
		this.totalAmount = totalAmount;
	}
	public Double getTotalAmount()
	{
		return totalAmount;
	}
	public void setTotalOrders(int totalOrders)
	{
		this.totalOrders = totalOrders;
	}
	public int getTotalOrders()
	{
		return totalOrders;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
}
