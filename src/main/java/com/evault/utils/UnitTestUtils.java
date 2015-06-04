package com.evault.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.evault.utils.EvaultDBUtils;

public class UnitTestUtils {

	EvaultDBUtils dbUtils = new EvaultDBUtils();

	public ArrayList getComponents() {
		Connection con = dbUtils.getConnection();

		ArrayList componentList = new ArrayList();
		ResultSet rs = dbUtils.executeQuery(con,
				"SELECT component_name FROM unittest_components");

		try {
			while (rs.next()) {
//				UnitTestForm unitTestObj = new UnitTestForm();
//				unitTestObj.setComponentId(rs.getInt("id"));
//				unitTestObj.setComponentName(rs.getString("component_name"));
				componentList.add(rs.getString("component_name"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			dbUtils.closeConnection(con);
		}

		return componentList;

	}

	public HashMap getUnitTestTrend(ArrayList componentsList, String strFrom,
			String strTo) {
		Connection con = dbUtils.getConnection();

		ArrayList componentTrendList;

		HashMap unitTestMap = new HashMap();

		Date fromDate = null;
		Date toDate = null;

		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		SimpleDateFormat formatter5 = new SimpleDateFormat("yyyy-MM-dd");

		String component_name;
		String query;
		int count=0;
		try {
			strFrom = formatter5.format(sdf.parse(strFrom));
			strTo = formatter5.format(sdf.parse(strTo));

			for (int i = 0; i < componentsList.size(); i++) {
				// componentTrendList = new ArrayList();
				StringBuffer strBuffer = new StringBuffer();

				component_name = (String) componentsList.get(i);
				query = "SELECT build_date , avg(percentage) percentage FROM stash.unittest_details u where u.component_id=(select component_id from stash.unittest_components where component_name='"
						+ component_name
						+ "') and (build_date BETWEEN '"
						+ strFrom
						+ "' AND  '"
						+ strTo
						+ "')  group by build_date";
				
				System.out.println("query --> "+query);
				ResultSet rs = dbUtils.executeQuery(con, query);
				System.out.println("RS status "+rs.wasNull());
				try {
					if (rs != null) {
						count=0;
						while (rs.next()) {
							if (count == 0) {
								strBuffer.append("['Date', 'Percentage']");
							}
							strBuffer.append(",['" + rs.getString("build_date")
									+ "'," + rs.getInt("percentage") + "]");
							//
							// UnitTestForm unitTestObj = new UnitTestForm();
							// unitTestObj.setDates(rs.getString("build_date"));
							// unitTestObj.setPercentage(rs.getInt("percentage"));
							// componentTrendList.add(unitTestObj);
							count++;

						}
						if(count>0)
						unitTestMap.put(component_name, strBuffer);

					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbUtils.closeConnection(con);
		}

		return unitTestMap;

	}

}
