package com.floreantpos.ui.views.order;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ticketInfo")
public class DaillySalesXml {
private TicketInfo ticketInfo;

public TicketInfo getTicketInfo() {
	return ticketInfo;
}
@XmlElement
public void setTicketInfo(TicketInfo ticketInfo) {
	this.ticketInfo = ticketInfo;
}
}
