package com.evault.utils;

import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
import br.eti.kinoshita.testlinkjavaapi.constants.ActionOnDuplicate;
import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionType;
import br.eti.kinoshita.testlinkjavaapi.constants.TestImportance;
import br.eti.kinoshita.testlinkjavaapi.model.TestCase;
import br.eti.kinoshita.testlinkjavaapi.model.TestCaseStep;

public class TestLinkUtils {
	//
	public String reportManualTests(InputStream inputStream, String fileName) {
		String status = "";
		String testCaseName = "";
		String testSummary="none";
		String testExpectedResults = "";
		String testImportance = "";
		String testActualResults = "";
		String testUserName = "";
		String testPlan = null;
		TestImportance importance = TestImportance.HIGH;
		Connection conn=null;
		int suiteId=0;
		int testCaseId=0;
		int addTestCaseToTestPlanStatus=0;
		try {

			String testLinkTestresult = null;


			Workbook workbook = null;
			if (fileName.toLowerCase().endsWith("xlsx")) {
				workbook = new XSSFWorkbook(inputStream);
			} else if (fileName.toLowerCase().endsWith("xls")) {
				workbook = new HSSFWorkbook(inputStream);
			}


			int noOfSheets = workbook.getNumberOfSheets();

				Sheet sheet = workbook.getSheetAt(0);
				String suiteName = sheet.getSheetName();
				conn=EvaultDBUtils.getTestLinkConnection();
				Statement stmt=conn.createStatement();
				
				ResultSet rs=stmt.executeQuery("SELECT id FROM testlink.nodes_hierarchy where name='"+suiteName+"'");
				while(rs.next()){
				suiteId=rs.getInt("id");
				}
				rs.close();stmt.close();conn.close();
				 
				
				// Iterate through each rows one by one
				Iterator<Row> rows = sheet.iterator();
			while (rows.hasNext()) {

				Row row = rows.next();
				if (row.getRowNum() == 0) {
					continue;
				}
				// For each row, iterate through all the columns

				testCaseName = (row.getCell(0) != null ? row.getCell(0)
						.toString().trim() : null);

				testSummary = ((row.getCell(1) != null && row.getCell(1).toString().length()>0)  ? row.getCell(1)
						.toString().trim() : "none");

				testExpectedResults = ((row.getCell(2) != null && row.getCell(3).toString().length()>0)? row.getCell(2)
						.toString().trim() : "none");

				testActualResults = ((row.getCell(3) != null && row.getCell(3).toString().length()>0) ? row.getCell(3)
						.toString().trim() : "none");

				testImportance = ((row.getCell(4) != null && row.getCell(4).toString().length()>0) ? row.getCell(4)
						.toString().trim() : "none");

				testPlan = (row.getCell(5) != null ? row.getCell(5).toString()
						.trim() : null);
				testUserName = (row.getCell(6) != null ? row.getCell(6)
						.toString().trim() : null);

				if (testCaseName != null) {

					if (testImportance != null) {
						if (testImportance.equalsIgnoreCase("HIGH"))
							importance = TestImportance.HIGH;
						if (testImportance.equalsIgnoreCase("MEDIUM"))
							importance = TestImportance.MEDIUM;
						if (testImportance.equalsIgnoreCase("LOW"))
							importance = TestImportance.LOW;
					}

					ArrayList steps = new ArrayList();
					TestCaseStep step = new TestCaseStep();
					step.setNumber(1);
					step.setExpectedResults(testExpectedResults);
					step.setExecutionType(ExecutionType.AUTOMATED);
					step.setActions(testActualResults);
					steps.add(step);
					TestLinkAPI tcapi = getUtilAPIInstance();
					// tcapi.createTestCase(testCaseName, testSuiteId,
					// testProjectId, authorLogin, summary, steps,
					// preconditions, importance, execution, order,
					// internalId, checkDuplicatedName,
					// actionOnDuplicatedName)
					TestCase tcCreated = tcapi.createTestCase(testCaseName,
							suiteId, 630, testUserName, testSummary, steps, null,
							importance, ExecutionType.AUTOMATED,
							new Integer(10), null, true,
							ActionOnDuplicate.BLOCK);
					conn=EvaultDBUtils.getTestLinkConnection();
					stmt=conn.createStatement();					
					 rs=stmt.executeQuery("SELECT id FROM testlink.nodes_hierarchy where name='"+testCaseName+"'");
					while(rs.next()){
						testCaseId=rs.getInt("id");
					}
					rs.close();stmt.close();conn.close();
					
					if (testCaseId > 1) {
						String testPlanArray[] = testPlan.split(",");
						for (int j = 0; j < testPlanArray.length; j++) {
							try {
								addTestCaseToTestPlanStatus = tcapi
										.addTestCaseToTestPlan(
												630,
												getTestPlanIDByName(testPlanArray[j]),
												testCaseId, new Integer(1),
												new Integer(1), new Integer(1),
												new Integer(1));
							} catch (Exception e) {
								System.out.println("Test Case -- "
										+ testCaseName + " is already part of Test Plan "
										+ testPlanArray[j]);

							}
						}
						System.out.println("addTestCaseToTestPlanStatus-- "
								+ addTestCaseToTestPlanStatus);
					}

				} else {
					return status = "Please verify Suite name "
							+ suiteName
							+ " / Excel sheet name should match with Existing Suite name";
				}
			}


			

			status = "success";
			// file.close();
		} catch (Exception e) {
			status = testCaseName + " -- " + e.getMessage();
			e.printStackTrace();
		}
		return status;
	}

	public TestLinkAPI getUtilAPIInstance() {
		String URL = "http://10.28.208.78/testlink/lib/api/xmlrpc/v1/xmlrpc.php";
		String DEVKEY = "a3bce8915fea73a7fd570460df66dd18";

		TestLinkAPI api = null;

		URL testlinkURL = null;

		try {
			testlinkURL = new URL(URL);

			api = new TestLinkAPI(testlinkURL, DEVKEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return api;
	}

	public String createTestCases() {
		TestLinkAPI tcapi = getUtilAPIInstance();

		ArrayList steps = new ArrayList();
		TestCaseStep step = new TestCaseStep();
		step.setNumber(1);
		step.setExpectedResults("Delete this");
		step.setExecutionType(ExecutionType.AUTOMATED);
		step.setActions("don't do");
		steps.add(step);
		// tcapi.createTestCase(testCaseName, testSuiteId, testProjectId,
		// authorLogin, summary, steps, preconditions, importance, execution,
		// order, internalId, checkDuplicatedName, actionOnDuplicatedName)
		tcapi.createTestCase("paramTest", 10, 1, "pkumarasamy",
				"paramTestSummary", steps, null, TestImportance.HIGH,
				ExecutionType.AUTOMATED, new Integer(10), null, true,
				ActionOnDuplicate.BLOCK);

		return null;
	}
	
	
	public void addTestCaseToTestPlan(){
		TestLinkAPI tcapi = getUtilAPIInstance();

		System.out.println("Test plan add status"+ tcapi.addTestCaseToTestPlan(630,
				getTestPlanIDByName("Regression"),
				3456, new Integer(1),
				new Integer(1), new Integer(1),
				new Integer(1)));
	
	}

	int getTestPlanIDByName(String testPlanName) {
		testPlanName = testPlanName.trim();
		int testPlanId = 0;
		if (testPlanName.equalsIgnoreCase("BVT")) {
			testPlanId = 631;
		} else if (testPlanName.equalsIgnoreCase("FullRegression")) {
			testPlanId = 632;
		} else if (testPlanName.equalsIgnoreCase("Regression")) {
			testPlanId = 633;
		} else if (testPlanName.equalsIgnoreCase("e2e_blf")) {
			testPlanId = 2583;
		} else if (testPlanName.equalsIgnoreCase("e2e_byr")) {
			testPlanId = 2584;
		} else if (testPlanName.equalsIgnoreCase("sample")) {
			testPlanId = 2000;
		}
		return testPlanId;
	}
	
	
	public static void main(String args[]) {
		TestLinkUtils tcUtils = new TestLinkUtils();
		tcUtils.addTestCaseToTestPlan();


	}
}
