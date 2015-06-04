package com.evault.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.evault.form.CommitDetails;
import com.mysql.jdbc.PreparedStatement;

public class EvaultDBUtils {

	public static Connection getConnection() {

		Connection connection = null;

		String stash_host = ConfigurationManager.getProperty("stash.host");
		String stash_schema = ConfigurationManager.getProperty("stash.schema");
		String stash_user_name = ConfigurationManager.getProperty("stash.user.name");
		String stash_password = ConfigurationManager.getProperty("stash.user.password");



		try {
			Class.forName("com.mysql.jdbc.Driver");

			// EvaultLogger.info("Connection details: "
			// +hostname+" "+schema+" "+username+" "+password);

			connection = (Connection) DriverManager.getConnection(
					"jdbc:mysql://" + stash_host + "/" + stash_schema, stash_user_name,
					stash_password);

			if (connection == null) {
			}

		} catch (SQLException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();

		}
		return connection;

	}
	
	
	public static Connection getTestLinkConnection() {

		Connection connection = null;

		String host = ConfigurationManager.getProperty("testlinkDB.host");
		String schema = ConfigurationManager.getProperty("testlinkDB.schema");
		String user_name = ConfigurationManager.getProperty("testlinkDB.user.name");
		String password = ConfigurationManager.getProperty("testlinkDB.user.password");



		try {
			Class.forName("com.mysql.jdbc.Driver");

			// EvaultLogger.info("Connection details: "
			// +hostname+" "+schema+" "+username+" "+password);

			connection = (Connection) DriverManager.getConnection(
					"jdbc:mysql://" + host + "/" + schema, user_name,
					password);

			if (connection == null) {
			}

		} catch (SQLException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();

		}
		return connection;

	}

	public static Connection getDBConnection() {

		Connection connection = null;

		String host = ConfigurationManager.getProperty("cibuild.host");
		String schema = ConfigurationManager.getProperty("cibuild.schema");
		String user_name = ConfigurationManager.getProperty("cibuild.user.name");
		String password = ConfigurationManager.getProperty("cibuild.user.password");



		try {
			Class.forName("com.mysql.jdbc.Driver");

			// EvaultLogger.info("Connection details: "
			// +hostname+" "+schema+" "+username+" "+password);

			connection = (Connection) DriverManager.getConnection(
					"jdbc:mysql://" + host + "/" + schema, user_name,
					password);

			if (connection == null) {
			}

		} catch (SQLException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();

		}
		return connection;

	}
	
	public ResultSet executeQuery(Connection connection, String query) {
		ResultSet rs = null;
		Statement stmt = null;
		try {

			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			// while (rs.next()) {
			// String method = rs.getString("method");
			// String status_code = rs.getString("status_code");
			// String container = rs.getString("container");
			// String obj = rs.getString("obj");
			// System.out.println(method + "\t" + status_code + "\t"
			// + container + "\t" + obj);
			// }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} /*
		 * finally { try { rs.close(); stmt.close(); } catch (SQLException e) {
		 * // TODO Auto-generated catch block e.printStackTrace(); } }
		 */
		return rs;

	}

	public static void closeConnection(Connection connection) {
		try {
			connection.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public void saveCommits(HashMap commitMap) {
		Connection connection = null;
		PreparedStatement statement = null;
		String SQL_INSERT = "insert into commit_details(name,date,repo) values (?,?,?)";
		try {
			connection = getDBConnection();
			statement = (PreparedStatement) connection
					.prepareStatement(SQL_INSERT);
			 Iterator it = commitMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pairs = (Map.Entry) it.next();
				ArrayList commitsList = (ArrayList) pairs.getValue();
				for (int i = 0; i < commitsList.size(); i++) {
					ArrayList commits = (ArrayList) commitsList.get(i);

					for (int j = 0; j < commits.size(); j++) {

						CommitDetails commitDetailsObj = (CommitDetails) commits
								.get(j);
						statement.setString(1, commitDetailsObj.getName());
						
						SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy"); // your template here
						java.util.Date dateStr = formatter.parse(commitDetailsObj.getDate().substring(0, commitDetailsObj.getDate().indexOf(" ")));
						java.sql.Date dateDB = new java.sql.Date(dateStr.getTime());
						
						
						statement.setDate(2, dateDB);
						statement.setString(3, (String) pairs.getKey());
						statement.addBatch();
						if ((j + 1) % 10 == 0) {
							statement.executeBatch(); // Execute every 10
														// items.
						}
					}
				}

			}
			statement.executeBatch();
		} catch (SQLException logOrIgnore) {
			logOrIgnore.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (statement != null)
				try {
					statement.close();
				} catch (SQLException logOrIgnore) {
				}
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException logOrIgnore) {
				}
		}
	}
	
	
	public HashMap retriveCommits() {
		Connection connection = null;
		String SQL_SELECT_NAME = "select name from commit_details GROUP BY name";
		String SQL_SELECT_COMMITS = "select count(name) counts,date_format(date, '%m/%d/%Y') date from commit_details";
		HashMap commitMap=new HashMap();
		try {
			connection = getDBConnection();
			Statement stmt = connection.createStatement();
			Statement stmt1 = connection.createStatement();

			ResultSet rs = stmt
					.executeQuery(SQL_SELECT_NAME);
			while (rs.next()) {
				ArrayList commitList=new ArrayList();

				String name = rs.getString("name");
				//System.out.println("User Name----> "+name +" QUERY=="+SQL_SELECT_COMMITS+" where name='"+name+"' GROUP BY date,name ORDER BY date");

				ResultSet rs1 = stmt1
						.executeQuery(SQL_SELECT_COMMITS+" where name='"+name+"' GROUP BY date,name ORDER BY date");
				while (rs1.next()) {
					
					CommitDetails comitObj=new CommitDetails();
					commitList.add(comitObj);
					comitObj.setCount( rs1.getString("counts"));
					comitObj.setDate(rs1.getString("date"));
					String counts = rs1.getString("counts");
					String date = rs1.getString("date");
//					System.out.println("counts----> "+counts);
//					System.out.println("date----> "+date);	
				}
				commitMap.put(name, commitList);

			}

		} catch (SQLException logOrIgnore) {
			logOrIgnore.printStackTrace();
		} finally {		
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException logOrIgnore) {
				}
		}
		return commitMap;
	}
	
	
	
	
	public void saveCommit_report(ArrayList datesbetweenDate, HashMap commitMap) {
		Connection connection = null;
		PreparedStatement statement = null;
		String date = "";
		String SQL_INSERT = "insert into commit_report(name,date,count) values (?,?,?)";
		try {
			connection =getDBConnection();
			statement = (PreparedStatement) connection
					.prepareStatement(SQL_INSERT);
			Iterator it = commitMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pairs = (Map.Entry) it.next();
				ArrayList commitsList = (ArrayList) pairs.getValue();
				for (int i = 0; i < commitsList.size(); i++) {
					CommitDetails commitDetailsObj = (CommitDetails) commitsList
							.get(i);

					statement.setString(1, (String) pairs.getKey());

					SimpleDateFormat formatter = new SimpleDateFormat(
							"MM/dd/yyyy"); // your template here
					java.util.Date dateStr = formatter.parse(commitDetailsObj
							.getDate());
					java.sql.Date dateDB = new java.sql.Date(dateStr.getTime());
					statement.setDate(2, dateDB);
					statement.setString(3, commitDetailsObj.getCount());
					statement.addBatch();
					if ((i + 1) % 10 == 0) {
						statement.executeBatch(); // Execute every 10
													// items.
					}

				}

			}
			statement.executeBatch();
			Statement stmtSelect = connection.createStatement();
			Iterator itInsert = commitMap.entrySet().iterator();

			while (itInsert.hasNext()) {
				Map.Entry pairs = (Map.Entry) itInsert.next();

				for (int j = 0; j < datesbetweenDate.size(); j++) {
					Boolean isEmpty = true;
					String strDate = (String) datesbetweenDate.get(j);
					String SQL_SELECT_NAME = "select * from commit_report  where  name='"
							+ pairs.getKey()
							+ "' and date=DATE_FORMAT(STR_TO_DATE('"+strDate+"', '%m/%d/%Y'), '%Y-%m-%d')";
					ResultSet rsSelect = stmtSelect
							.executeQuery(SQL_SELECT_NAME);
					while (rsSelect.next()) {
						isEmpty = false;
					}
					if(isEmpty){
						
						statement.setString(1, (String) pairs.getKey());

						SimpleDateFormat formatter = new SimpleDateFormat(
								"MM/dd/yyyy"); // your template here
						java.util.Date dateStr = formatter.parse(strDate);
						java.sql.Date dateDB = new java.sql.Date(dateStr.getTime());
						statement.setDate(2, dateDB);
						statement.setString(3, "0");
						statement.executeUpdate();
						
						
						//System.out.println(pairs.getKey()+" --No record for date "+strDate);
					}
				}
			}
		} catch (SQLException logOrIgnore) {
			logOrIgnore.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (statement != null)
				try {
					statement.close();
				} catch (SQLException logOrIgnore) {
				}
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException logOrIgnore) {
				}
		}
	}

	
	
	
	public HashMap prepareReportdata(ArrayList datesbetweenDate) {
		HashMap finaldata=new HashMap();
		Connection connection = null;
		String SQL_SELECT_NAME = "select name from commit_report GROUP BY name";
		String SQL_SELECT_COMMITS = "select count from commit_report";
		LinkedHashMap commitMap = new LinkedHashMap();
		ArrayList totalList = new ArrayList();

		int temp_count=0;
		try {
			datesbetweenDate.add("Total");
			commitMap.put("Name/Date", datesbetweenDate);
			connection = getDBConnection();
			Statement stmt = connection.createStatement();
			Statement stmt1 = connection.createStatement();

			ResultSet rs = stmt.executeQuery(SQL_SELECT_NAME);
			while (rs.next()) {
				temp_count=0;
				ArrayList commitList = new ArrayList();

				String name = rs.getString("name");
				// System.out.println("User Name----> "+name
				// +" QUERY=="+SQL_SELECT_COMMITS+" where name='"+name+"' GROUP BY date,name ORDER BY date");

				ResultSet rs1 = stmt1.executeQuery(SQL_SELECT_COMMITS
						+ " where name='" + name
						+ "' GROUP BY date,name ORDER BY date");
				while (rs1.next()) {

					int count = rs1.getInt("count");
					temp_count+=count;
					commitList.add(count+"");

					// System.out.println("count----> "+count);
				}
				commitList.add(temp_count+"");
				commitMap.put(name, commitList);

			}

			for (int j = 0; j < datesbetweenDate.size(); j++) {

				Boolean isEmpty = true;
				String strDate = (String) datesbetweenDate.get(j);
				ResultSet rs2 = stmt1
						.executeQuery("select sum(count) count from commit_report where date=DATE_FORMAT(STR_TO_DATE('"
								+ strDate
								+ "', '%m/%d/%Y'), '%Y-%m-%d') GROUP BY date ORDER BY date");

				while (rs2.next()) {
					int count = rs2.getInt("count");
					totalList.add(count + "");
					isEmpty=false;

				}
				if (isEmpty) {
					totalList.add("0");
				}
			}
			

			commitMap.put("Total", totalList);			
			
			
			

		} catch (SQLException logOrIgnore) {
			logOrIgnore.printStackTrace();
		} finally {
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException logOrIgnore) {
				}
		}
		finaldata.put("tableData", commitMap);
		
		
		
		
		

		ArrayList trendList = new ArrayList();
		for(int i=0;i<datesbetweenDate.size();i++){
			trendList.add("[");
			if(i==0){
			trendList.add("'" +"Date" + "',");
			trendList.add("'"+"Count"+"'");
			trendList.add("],");
			trendList.add("[");
			
			}

			trendList.add("'" + datesbetweenDate.get(i) + "',");
			trendList.add(totalList.get(i) );
			if(i<datesbetweenDate.size()-1)
			trendList.add("],");
			else
				trendList.add("]");


		}

		finaldata.put("trendData", trendList);		
		
		
		
		
		
//		Iterator itInsert = commitMap.entrySet().iterator();
//		int counter=0;
//		ArrayList headerList = new ArrayList();
//		headerList.add("[");
//		while (itInsert.hasNext()) {
//			Map.Entry pairs = (Map.Entry) itInsert.next();
//			headerList.add("'" + pairs.getKey() + "'");
//				if (counter < commitMap.size() - 1) {
//					headerList.add(",");
//				}
//
//			counter++;
//
//		}
//		headerList.add("],");
//		
//		Iterator itData = commitMap.entrySet().iterator();
//		ArrayList dataList = new ArrayList();
//
//		while (itData.hasNext()) {
//			Map.Entry pairs = (Map.Entry) itData.next();
//			dataList.add(pairs.getValue());
//		}
//		
//		
//		int totalRecordSet=dataList.size();
//		int individualRecordSet=((ArrayList)dataList.get(1)).size();
//		
//		System.out.println("totalRecordSet== "+totalRecordSet+" individualRecordSet== "+individualRecordSet);
//
//		int temp=0;
//		//11 times
//		for (int i = 0; i < individualRecordSet; i++) {
//			// 5 times
//			headerList.add("[");
//
//			for (int j = 0; j < totalRecordSet; j++) {
//				try{
//				// 5 times
//				if (j == 0) {
//					
//					headerList.add("'" + ((ArrayList) dataList.get(j)).get(i)
//							+ "'");
//				} else {
//					headerList.add(((ArrayList) dataList.get(j)).get(i));
//				}
//				if (j < totalRecordSet-1) {
//					headerList.add(",");
//				}
//				}
//				catch(Exception e){
//					
//				}
//			}
//			headerList.add("]");
//			if (i < individualRecordSet-1) {
//
//				headerList.add(",");
//			}
//
//		}
//
//
//		finaldata.put("trendData", headerList);

		return finaldata;
	}	
	
	
	
	
	public  void cleanExistingRecords(){
		Connection connection = null;
		Statement statement = null;
		try {
			connection = getDBConnection();
			statement = connection.createStatement();

			  statement.executeUpdate("TRUNCATE commit_details");
			  statement.executeUpdate("TRUNCATE commit_report");

		} catch (SQLException logOrIgnore) {
			logOrIgnore.printStackTrace();
		}

	}
	

	
//	public static void main(String args[]) {
//		Connection connection = EvaultDBUtils.getConnection();
//		try {
//			Statement stmt = connection.createStatement();
//
//			// DateFormat dateFormat = new
//			// SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//			// Date date = new Date();
//			// String formatDate = dateFormat.format(date);
//
//			String insertSQL = "insert into commit_details(name,date,repo) values ('param',STR_TO_DATE('02-02-2012', '%d-%m-%Y'),'automation')";
//
//			int status = stmt.executeUpdate(insertSQL);
//
//			ResultSet rs = stmt
//					.executeQuery("select * from stash.commit_details");
//			// while (rs.next()) {
//			// String method = rs.getString("method");
//			// String status_code = rs.getString("status_code");
//			// String container = rs.getString("container");
//			// String obj = rs.getString("obj");
//			// System.out.println(method + "\t" + status_code + "\t"
//			// + container + "\t" + obj);
//			// }
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}

}
