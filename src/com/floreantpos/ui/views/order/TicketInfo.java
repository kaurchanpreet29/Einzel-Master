package com.floreantpos.ui.views.order;

import javax.xml.bind.annotation.XmlAttribute;

public class TicketInfo {
private Double totalAmount;
private String paymentType;
private String formatedDate;
private int ticketId;
private String userName;
public Double getTotalAmount() {
	return totalAmount;
}
public void setTotalAmount(Double totalAmount) {
	this.totalAmount = totalAmount;
}
public String getPaymentType() {
	return paymentType;
}
public void setPaymentType(String paymentType) {
	this.paymentType = paymentType;
}
public String getFormatedDate() {
	return formatedDate;
}
public void setFormatedDate(String formatedDate) {
	this.formatedDate = formatedDate;
}
public int getTicketId() {
	return ticketId;
}

public void setTicketId(int ticketId) {
	this.ticketId = ticketId;
}
public String getUserName() {
	return userName;
}
public void setUserName(String userName) {
	this.userName = userName;
}
}
