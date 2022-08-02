package com.floreantpos.onlineOrder;

public class Article {
	private String articleNo;
	private String articleName;
	private String articleSize;
	private int beverage;
	private int count;
	private Double price;
	private Double unitPrice;
	private String partition;
	private Double tax;
	private Double deposit;
	private String comment;
	private SubArticleList subArticleList;
	
	public void setArticleNo(String articleNo)
	{
		this.articleNo = articleNo;
	}
	
	public String getArticleNo()
	{
		return articleNo;
	}
	
	public void setBeverage(int beverage)
	{
		this.beverage = beverage;
	}
	
	public int getBeverage()
	{
		return beverage;
	}
	
	public void setArticleName(String articleName)
	{
		this.articleName = articleName;
	}
	
	public String getArticleName()
	{
		return articleName;
	}
	
	public void setArticleSize(String articleSize)
	{
		this.articleSize = articleSize;
	}
	
	public String getArticleSize()
	{
		return articleSize;
	}
	
	public void setCount(int count)
	{
		this.count = count;
	}
	
	public int getCount()
	{
		return count;
	}
	
	public void setPrice(Double price)
	{
		this.price = price;
	}
	
	public Double getPrice()
	{
		return price;
	}
	
	public void setUnitPrice(Double unitPrice)
	{
		this.unitPrice = unitPrice;
	}
	
	public Double getUnitPrice()
	{
		return unitPrice;
	}
	
	public void setPartition(String partition)
	{
		this.partition = partition;
	}
	
	public String getParition()
	{
		return partition;
	}
	
	public void setTax(Double tax)
	{
		this.tax = tax;
	}
	
	public Double getTax()
	{
		return tax;
	}
	public void setDeposit(Double deposit)
	{
		this.deposit = deposit;
	}
	
	public Double getDeposit()
	{
		return deposit;
	}
	public void setComment(String comment)
	{
		this.comment = comment;
	}
	
	public String getComment()
	{
		return comment;
	}
}
