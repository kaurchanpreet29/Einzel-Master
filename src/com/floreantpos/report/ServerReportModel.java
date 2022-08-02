package com.floreantpos.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;




import com.floreantpos.main.Application;
import com.floreantpos.util.NumberUtil;

public class ServerReportModel extends AbstractReportDataSource  {
	List<ServerData> list;
	public ServerReportModel() {
		super(new String[] { "kellnerName","Bestellen","Gesamt" });
	}
	
	@Override
	public int getColumnCount() {
		
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		if(list == null)return 0;
		
		return list.size();
	}

	public void setList(List<ServerData> list)
	{
		this.list = list;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ServerData serverData = list.get(rowIndex);
		
		switch(columnIndex) {
		case 0:
		{
		
			return String.valueOf(serverData.getName());
		}
		case 1:
			return serverData.getTotalOrders()+"";
		case 2:
			return NumberUtil.formatNumber (serverData.getTotalAmount())+ " " + Application.getCurrencySymbol();
		}
		return null;
	}

}
