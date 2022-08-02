/**
 * 
 */
package com.khana.update;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
/**
 * @author Jyoti Rai
 *
 */
public class OnlineDataService {
	private static OnlineDataService service;
	/**
	 * @return the service
	 */
	public static OnlineDataService getService() {
		if(service==null)
			service = new OnlineDataService();
		return service;
	}
	public OnlineDataService() {

	}
	public static TseInfo getTseInfo(String tseID) throws SQLException {
		TseInfo tseInfo = null;
		String myDriver = "org.gjt.mm.mysql.Driver";
		String myUrl = "jdbc:mysql://vwp9989.webpack.hosteurope.de/db10975332-khana";
		Connection conn = null;
		try {
			Class.forName(myDriver);
			conn = DriverManager.getConnection(myUrl,
					"dbu10975332",
					"ch03ms23");

			Statement m_Statement = conn.createStatement();
			String query = "SELECT * FROM "+"tse_info"+" WHERE tse_Id="+tseID;
			ResultSetMapper<TseInfo> orderSetMapper = new ResultSetMapper<TseInfo>();
			ResultSet m_ResultSet = m_Statement.executeQuery(query);
			m_Statement.closeOnCompletion();
			List<TseInfo> tseInfoList = orderSetMapper.mapRersultSetToObject(m_ResultSet, TseInfo.class);
			if(tseInfoList!=null&&tseInfoList.size()>0)
				tseInfo = tseInfoList.get(0);			
		}catch(Exception ex) {ex.printStackTrace();
		}finally {
			conn.close();
		}
		return tseInfo;
	}
}
