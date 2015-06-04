package com.evault.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.evault.form.AutomationDateForm;
import com.evault.form.AutomationTrendForm;
import com.evault.form.JiraTrendsForm;

public class AutomationUtils {

    public ArrayList getPlans() {
        String plans[] = ConfigurationManager.getProperty("testlink.plans")
                .split(",");
        ArrayList planList = new ArrayList(Arrays.asList(plans));
        return planList;
    }

    public TreeMap getTestTrend(String planName, ArrayList dateList) {
        TreeMap testMap = new TreeMap();
        EvaultDBUtils dbUtils = new EvaultDBUtils();
        String testLink_projectName = ConfigurationManager
                .getProperty("testlink.project");
        Connection con = null;
        String build_ids_Query;
        String test_count_query_passed;
        String test_count_query_failed;

        SimpleDateFormat sourceDateFM = new SimpleDateFormat("MM/dd/yyyy");
        Date sourceDate;
        SimpleDateFormat destDateFM = new SimpleDateFormat("dd-M-yyyy");
        String strDate;


        try {
            con = dbUtils.getTestLinkConnection();
            String build_id = "";
            String build_name = "";
            String test_count_passed = "";
            String test_count_failed = "";

            String planId = "";
            String str_select_plan_query = "SELECT id FROM testlink.nodes_hierarchy where name= '"
                    + planName+ "'";
            System.out.println("str_select_plan_query ---> " + str_select_plan_query);
            ResultSet plan_rs = dbUtils
                    .executeQuery(con, str_select_plan_query);
            while (plan_rs.next()) {
                planId = plan_rs.getString("id");
            }
            for (int i = 0; i < dateList.size(); i++) {
                sourceDate = sourceDateFM.parse(dateList.get(i).toString());
                strDate = destDateFM.format(sourceDate);

                build_ids_Query = "SELECT id,name FROM testlink.builds where testplan_id=" + planId + " and name like '" + strDate + "%'";
                System.out.println("build_ids_Query ---> " + build_ids_Query);

                ResultSet build_rs = dbUtils.executeQuery(con, build_ids_Query);
                while (build_rs.next()) {
                    build_id = build_rs.getString("id");
                    build_name = build_rs.getString("name");
                    test_count_query_passed = "SELECT count(*) count FROM testlink.executions where build_id= " + build_id + " and status='p'";
                    System.out.println("test_count_query_passed ---> " + test_count_query_passed);

                    ResultSet test_count_rs_passed = dbUtils.executeQuery(con,
                            test_count_query_passed);
                    if (test_count_rs_passed.next()) {
                        test_count_passed = test_count_rs_passed.getString("count");
                    }
                    test_count_query_failed = "SELECT count(*) count FROM testlink.executions where build_id= " + build_id + " and status='f'";
                    System.out.println("test_count_query_failed ---> " + test_count_query_failed);

                    ResultSet test_count_rs_failed = dbUtils.executeQuery(con,
                            test_count_query_failed);
                    if (test_count_rs_failed.next()) {
                        test_count_failed = test_count_rs_failed.getString("count");
                    }
                    testMap.put(build_name, test_count_passed + "," + test_count_failed);


                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                dbUtils.closeConnection(con);
        }
        return testMap;

    }

    public TreeMap getAutomationTrend(String fromDate, String toDate) {
        TreeMap trendMap = new TreeMap();
        EvaultDBUtils dbUtils = new EvaultDBUtils();
        String testLink_projectName = ConfigurationManager
                .getProperty("testlink.project");
        Connection con = null;
        SimpleDateFormat sourceDateFM = new SimpleDateFormat("MM/dd/yyyy");
        Date sourceDate;
        SimpleDateFormat destDateFM = new SimpleDateFormat("yyyy-M-dd");
        String strDate;


        try {
            destDateFM.format(sourceDateFM.parse(fromDate));

            con = dbUtils.getDBConnection();
            String str_select_plan_query = "SELECT created_date, bvt_count, regression_count,full_regression_count FROM cibuild.automation_trend_summary where `created_date` >= '" + destDateFM.format(sourceDateFM.parse(fromDate)) + "' AND `created_date` <= '" + destDateFM.format(sourceDateFM.parse(toDate)) + "'  ORDER BY created_date ";
            System.out.println("str_select_plan_query ---> " + str_select_plan_query);
            ResultSet trend_rs = dbUtils
                    .executeQuery(con, str_select_plan_query);
            while (trend_rs.next()) {
                AutomationTrendForm trendObj = new AutomationTrendForm();
                trendObj.setBvtCount(Integer.parseInt(trend_rs.getString("bvt_count")));
                trendObj.setRegressionCount(Integer.parseInt(trend_rs.getString("regression_count")));
                trendObj.setFullRegressionCount(Integer.parseInt(trend_rs.getString("full_regression_count")));
                trendMap.put(trend_rs.getString("created_date"), trendObj);
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                dbUtils.closeConnection(con);
        }
        return trendMap;

    }

    public AutomationDateForm getFormattedDate(String from_Date, String to_Date, boolean sprint) {

        AutomationDateForm dateForm = new AutomationDateForm();
        SimpleDateFormat format;
        String today;
        Date today_date;

        if (!sprint) {
            format = new SimpleDateFormat("dd-MMM-yy",
                    Locale.ENGLISH);
        } else {
            format = new SimpleDateFormat("dd/MMM/yy",
                    Locale.ENGLISH);
        }
        SimpleDateFormat formatOut = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);


        DateFormat formatter;
        Date instance_start = null, instance_end = null;

        try {
            instance_start = format.parse(from_Date);
            instance_end = format.parse(to_Date);

            if (!sprint) {
                dateForm.setStart_date(formatOut.format(instance_start));
                dateForm.setEnd_date(formatOut.format(instance_end));
            } else {
                today = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                today_date = new SimpleDateFormat("MM/dd/yyyy").parse(today);

                System.out.println("today_date = " + today_date);
                dateForm.setStart_date(formatOut.format(instance_start));
                dateForm.setEnd_date(formatOut.format(instance_end));

                if (instance_end.after(today_date))
                    dateForm.setEnd_date(formatOut.format(today_date));

                formatOut.format(instance_start);
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }


        return dateForm;
    }
}
