package com.evault.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.evault.form.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.evault.form.JiraTrendsForm;
import com.evault.form.ProjectDetails;
import com.evault.form.SprintInfoForm;
import com.evault.form.SprintIssueForm;
import com.evault.form.SprintTrendForm;

/**
 * Created by Jayanth Nallapothula on 3/23/15.
 * Main Utils Class for Jira specific Methods
 */
public class JiraUtilsNew {

    ArrayList<Integer> id_names;
    ArrayList view_names;

     String result;
    static String authString;

    static String sessionId = "";

    /**
     * Gets the authorization from Jira and store the session id in the sessionId class variable
     * @param input_url : Any url that receives a response code other than 2xx.
     * @throws IOException
     */

    public static void getAuthorization(String input_url)
            throws IOException {

        HttpURLConnection httpURLConnection = null;

        try {

            URL url = new URL(input_url);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);

            authString = ConfigurationManager.getProperty("username")
                    + ":" + ConfigurationManager.getProperty("password");

            byte[] authEncBytes = org.apache.commons.codec.binary.Base64
                    .encodeBase64(authString.getBytes());
            String authStringEnc = new String(authEncBytes);

            /* Request Auth for Jira using Basic Authentication*/
            httpURLConnection.setRequestProperty("Authorization", "Basic "
                    + authStringEnc);
            Map<String, List<String>> map = httpURLConnection.getHeaderFields();

            /* Jira session ID from server header*/
            String jsessionId = map.get("Set-Cookie").toString().split(",")[1].split(";")[0];
            sessionId = jsessionId;
            System.out.println("jsessionId = " + jsessionId);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method will get all the projects information from Jira to select for the sprint and multi-sprint options
     * @return an ArrayList containing the name and rapid view Id's of all projects
     */
    public ArrayList getRapidViewInfo() {

        try {

            /* method which gets server response for the given request endpoint */
            result = getServerResponse(ConfigurationManager
                    .getProperty("jira_url_rapidview_list"));

            JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(result);
            JSONArray jsonArray = jsonObject.getJSONArray("views");
            int array_length = jsonArray.size();

            /* ArrayLists to store the rapid view ID's and names */
            id_names = new ArrayList<Integer>(array_length);
            view_names = new ArrayList<ProjectDetails>(array_length);

            JSONObject id_object;
            int id_value;
            String view_name;

            /* Parse JSON response to extract the id and names */
            for (int i = 0; i < array_length; i++) {
                ProjectDetails jira_rapid_view_obj = new ProjectDetails();
                id_object = jsonArray.getJSONObject(i);
                id_value = id_object.getInt("id");
                jira_rapid_view_obj.setKey(Integer.toString(id_value));
                id_names.add(i, id_value);
                view_name = id_object.getString("name");
                jira_rapid_view_obj.setName(view_name);
                view_names.add(jira_rapid_view_obj);
            }

            for (int i = 0; i < array_length; i++) {
                System.out.println("id = " + id_names.get(i) + ", name = "
                        + view_names.get(i));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view_names;
    }

    /**
     *
     * Method to return the list of all sprints for the given rapid view ID.
     * The ID, name and state of each sprint is stored in a SprintInfoForm object.
     *
     * @param rapidViewId
     *        Integer value of the rapid view
     *
     * @return the object of type SprintInfoForm
     */

    public SprintInfoForm getRapidViewDetails(Integer rapidViewId) {

        ArrayList<Integer> sprint_ids;
        ArrayList<String> sprint_names = null, sprint_state, sprint_dates;

        SprintInfoForm form = new SprintInfoForm();

        System.out.println("/nSprint List for RapidViewId " + rapidViewId
                + "---------------->");
        try {

            String url = ConfigurationManager
                    .getProperty("jira_url_sprint_list_1")
                    + rapidViewId
                    + ConfigurationManager
                    .getProperty("jira_url_sprint_list_2")
                    + rapidViewId.toString();
            result = getServerResponse(url);

            JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(result);
            JSONArray sprints_array = jsonObject.getJSONArray("sprints");

            int array_length = sprints_array.size();
            sprint_ids = new ArrayList<Integer>(array_length);
            sprint_names = new ArrayList<String>(array_length);
            sprint_state = new ArrayList<String>(array_length);


            /* JSON parsing to extract the ID, name and state of each sprint */
            for (int i = 0; i < array_length; i++) {
                JSONObject sprint_object = sprints_array.getJSONObject(i);

                sprint_ids.add(i, (Integer) sprint_object.get("id"));
                sprint_names.add(i, (String) sprint_object.get("name"));
                sprint_state.add(i, (String) sprint_object.get("state"));
            }


            /* ArrayLists to store the names and ID's in chronological order */
            ArrayList<String> sprint_names_chronological = new ArrayList<String>(
                    array_length);
            ArrayList<Integer> sprint_ids_chronological = new ArrayList<Integer>(
                    array_length);

            /* Reverse the sprint order to view in chronological order */
            for (int i = array_length - 1, j = 0; i >= 0; i--, j++) {
                sprint_names_chronological.add(sprint_names.get(i));
                sprint_ids_chronological.add(sprint_ids.get(i));
            }

            form.setSprintStates(sprint_state);
            form.setSprintNames(sprint_names_chronological);
            form.setSprintIds(sprint_ids_chronological);

            System.out.println("SPRINT_COUNT: " + array_length);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return form;

    }

    /**
     * Method to fetch all Jira projects for customer and compoenent sections
     * @param form
     *        Type SprintForm - Contains all sprint related information (rapidView, Customer, Component, sprintNames etc.)
     */
    public void getJiraProjects(SprintForm form){
        String url = ConfigurationManager.getProperty("jira_project_uri");
        result = getServerResponse(url);

        System.out.println("result = " + result);
        JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(result);

        /* ArrayList to store the project names and their keys */
        ArrayList<String> projectNames, projectKeys;

        projectKeys = new ArrayList<String>(jsonArray.size());
        projectNames = new ArrayList<String>(jsonArray.size());

        /* JSON parsing to extract the name and key for each project*/
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject project = (JSONObject)jsonArray.get(i);
            String name = project.getString("name");
            String key = project.getString("key");

            projectKeys.add(key);
            projectNames.add(name);
        }

        /* Assign the project names and ID's arrayList to the SprintForm object */
        form.setProjectKeys(projectKeys);
        form.setProjectNames(projectNames);
    }

    /**
     * Method to get the Start and End dates for the given sprint.
     * The value is directly extracted from the JSON response with the startDate and endDate fields.
     * The values are then formatted to comply with the JIRA jql date format type using SimpleDateFormat class.
     * @param rapidViewId
     *        Integer value of the project number
     *
     * @param sprintId
     *        Integer value of the sprint number
     * @return an object of type JiraTrendForm which contains the start and end date Strings
     */
    public JiraTrendsForm getDateRange(int rapidViewId, int sprintId) {

        String jira_url_given_sprint = ConfigurationManager
                .getProperty("jira_url_given_sprint_1")
                + rapidViewId
                + ConfigurationManager.getProperty("jira_url_given_sprint_2")
                + sprintId;

        /* Get the repsonse from server for the given request endpoint */
        result = getServerResponse(jira_url_given_sprint);
        JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(result);

        /* Get the start and End dates for the given sprint, also state of sprint */
        JSONObject sprint_details = jsonObject.getJSONObject("sprint");
        System.out.println(sprint_details);
        String sprint_start_date = sprint_details.getString("startDate");
        String sprint_end_date = sprint_details.getString("endDate");
        String sprint_status = sprint_details.getString("state");
        System.out.println("" + sprint_start_date + "  " + sprint_end_date
                + "  " + sprint_status);

        JiraTrendsForm form = new JiraTrendsForm();

        /* The start and end dates are described with time in the JSON response but we need only the date
         * so we store the entire date in an array and then split to get only the 0th index */
        String[] start_date_array = sprint_start_date.split(" ");
        String[] end_date_array = sprint_end_date.split(" ");

        System.out.println(start_date_array[0]);

        /* Input date format */
        SimpleDateFormat format = new SimpleDateFormat("dd/MMM/yy",
                Locale.ENGLISH);
        try {
            Date instance_start = format.parse(start_date_array[0]);
            Date instance_end = format.parse(end_date_array[0]);

            /* Output date format */
            SimpleDateFormat formatOut = new SimpleDateFormat("yyyy/MM/dd",
                    Locale.ENGLISH);

            /* format() method converts Date object to String */
            System.out.println("Start Date Formatted: "
                    + formatOut.format(instance_start));
            System.out.println("End Date Formatted:"
                    + formatOut.format(instance_end));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        /* Store the start and end dates as Strings */
        form.setStart_date(start_date_array[0]);
        form.setEnd_date(end_date_array[0]);

        return form;
    }

    /**
     * This method is used to format the dates to make it JIRA jql compatible
     * @param startDate
     *        Un-formatted Start date String
     *
     * @param endDate
     *        Un-formatted End Date String
     *
     * @param component
     *        String parameter to check if the request was from the component field of the dashboard.
     *
     * @return a JiraTrendsForm object containing the formatted Start and End dates
     */
    public JiraTrendsForm getDateRange_jql(String startDate, String endDate, String component) {

        JiraTrendsForm form = new JiraTrendsForm();
        SimpleDateFormat format;

        /* Checks if the request was issued from customer tab.
         * Sets the input format of the Date depending on this boolean value*/
        if (component.equals("customer")) {
            format = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        } else {
            format = new SimpleDateFormat("dd/MMM/yy", Locale.ENGLISH);
        }

        /* Date output format to support Jira JQL requests */
        SimpleDateFormat formatOut = new SimpleDateFormat("yyyy/MM/dd",
                Locale.ENGLISH);

        Date instance_start = null, instance_end = null;
        String end_date = null;

        try {
            instance_start = format.parse(startDate);
            instance_end = format.parse(endDate);


            /* format() converts Date object to String */
            System.out.println("Start Date Formatted: "
                    + formatOut.format(instance_start));
            System.out.println("End Date Formatted:"
                    + formatOut.format(instance_end));

            end_date = formatOut.format(instance_end);

            /* This segment adds a date to the end_date String. This is done to give a range for the given date >= start_date and < end_date */
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(end_date));
            c.add(Calendar.DATE, 1);  // number of days to add
            end_date = sdf.format(c.getTime());  // dt is now the new date

            System.out.println("end_date = " + end_date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        /* set the formatted dates to the form object, will be used later in the controller */
        form.setStart_date(formatOut.format(instance_start));
        form.setEnd_date(end_date);

        return form;
    }

    /**
     *
     * This method is used to generate the Bug trends View which shows the Opened and Closed counts for the date
     * passed in the parameters. Also check the remaining open Bugs for the given date.
     *
     * @param sprintId
     *        Integer value of the sprintId
     *
     * @param start_date
     *        Formatted(JIRA jql compatible) Start date String
     *
     * @param end_date
     *        Formatted(JIRA jql compatible) End date String
     *
     * @param current_date
     *        Today's date String
     *
     * @param customer
     *        boolean value to check if the method was requested from the customer section of dashboard
     *
     * @param component
     *        boolean value to check if the method wa requested from the component section of dashboard
     *
     * @param jqlQuery
     *        The JQL query String to be passed in the URL request to JIRA
     *
     * @param projectName
     *        project name String to be included in the JQL request
     *
     * @return a SprintTrendForm object
     */
    public SprintTrendForm jqlQuery(String sprintId, String start_date, String end_date, Object current_date, boolean customer, boolean component, String jqlQuery, String projectName) {

        String jira_url_jql = "";
        SprintTrendForm form = new SprintTrendForm();
        JSONObject jsonObject;
        String total_created = null, total_updated = null, remaining_open = "0";

        /* For any query other customer and component */
        try {
            if (!customer && !component) {

                /* Request for sprint related issues between start_date and end_date.
                *  Used to track bugs opened on the specific date */
                jira_url_jql = ConfigurationManager.getProperty("jira_jql_url")
                        + URLEncoder.encode("sprint=" + sprintId
                        + " and issuetype=Bug and (created >= " + "'"
                        + start_date + "'" + " " + "and created < " + "'"
                        + end_date + "') &maxResults=500", "UTF-8");


                result = getServerResponse(jira_url_jql);

                jsonObject = (JSONObject) JSONSerializer.toJSON(result);

                /* Total issues that were created */
                total_created = jsonObject.getString("total");
                System.out.println("total_created = " + total_created);

                /* Request for sprint related issues between start_date and end_date
                *  Used to track bugs resolved on the specific date */
                jira_url_jql = ConfigurationManager.getProperty("jira_jql_url")
                        + URLEncoder.encode("sprint=" + sprintId
                        + " and issuetype=Bug and (resolutiondate >= " + "'"
                        + start_date + "'" + " " + "and resolutiondate < " + "'"
                        + end_date + "')"
                        + " and (status=Resolved or status=Closed) &maxResults=500", "UTF-8");

                System.out.println("sprint=" + sprintId
                        + " and issuetype=Bug and (resolutiondate >= " + "'"
                        + start_date + "'" + " " + "and resolutiondate < " + "'"
                        + end_date + "')"
                        + " and (status=Resolved or status=Closed) &maxResults=500");

                result = getServerResponse(jira_url_jql);
                jsonObject = (JSONObject) JSONSerializer.toJSON(result);

                /* Total number of issues that were resolved */
                total_updated = jsonObject.getString("total");
                System.out.println("total_updated = " + total_updated);
            }
            /* Query to check for remaining open bugs for a given date */
            else {

                if (customer) {
                    jira_url_jql = ConfigurationManager.getProperty("jira_jql_url")
                            + URLEncoder.encode("project ='SCAL' and issuetype=Bug and (created <= " + "'"
                            + start_date + "')"
                            + " and status=Open" + jqlQuery + " &maxResults=500", "UTF-8");

                    System.out.println("Remaining Open QUERY = " + "project ='SCAL' and issuetype=Bug and (created <= " + "'"
                            + start_date + "')"
                            + " and status=Open" + jqlQuery + " &maxResults=500");
                } else if (component) {
                    jira_url_jql = ConfigurationManager.getProperty("jira_jql_url")
                            + URLEncoder.encode("issuetype=Bug and (created <= " + "'"
                            + start_date + "')"
                            + " and status=Open" + jqlQuery + " &maxResults=500", "UTF-8");

                    System.out.println("Remaining Open QUERY = " + "issuetype=Bug and (created <= " + "'"
                            + start_date + "')"
                            + " and status=Open" + jqlQuery + " &maxResults=500");
                }

                result = getServerResponse(jira_url_jql);
                jsonObject = (JSONObject) JSONSerializer.toJSON(result);

                /* Total number of remaining open bugs for the date */
                remaining_open = jsonObject.getString("total");
                System.out.println("remaining_open = " + remaining_open);

            }

            /* Error checking for opened and Fixed bugs */
            if (total_created != null) {
                form.setCreated_count(Integer.parseInt(total_created));
            }
            if (total_updated != null) {
                form.setUpdated_count(Integer.parseInt(total_updated));
            }

            /* Assign the counts to the form object */
            form.setRemaining_open(Integer.parseInt(remaining_open));
            form.setDate(current_date.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return form;
    }

    /**
     *
     * Method to determine the Bug status (Opened and Closed) for the specified date range.
     * It is called when the user wants to compare the Bug trends for various sprints (multi-sprint).
     *
     * @param sprintId
     *        Integer value of the sprint ID
     *
     * @param start_date
     *        Start date string
     *
     * @param end_date
     *        End date string
     *
     * @param projectName
     *        String containing the project Name
     *
     * @return a SprintTrendForm object
     */
    public SprintTrendForm JqlSprintToSprint(String sprintId, String start_date, String end_date, String projectName) {

        SprintTrendForm form = new SprintTrendForm();

        /* Input date format */
        SimpleDateFormat format = new SimpleDateFormat("dd/MMM/yy",
                Locale.ENGLISH);

        SimpleDateFormat formatOut;
        Date instance_start, instance_end;

        try {
            instance_start = format.parse(start_date);
            Calendar c = Calendar.getInstance();
            c.setTime(format.parse(end_date));
            c.add(Calendar.DATE, 1);
            end_date = format.format(c.getTime());

            instance_end = format.parse(end_date);

            /* Output date format */
            formatOut = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);


            System.out.println("Start: " + formatOut.format(instance_start) + ", End: " + formatOut.format(instance_end));

            /* Query to get the number of created bugs for the specified sprint */
            String jira_url_jql = ConfigurationManager.getProperty("jira_jql_url")
                    + URLEncoder.encode("sprint=" + sprintId
                    + " and issuetype=Bug and created >= " + "'"
                    + formatOut.format(instance_start) + "'" + " " + "and created < " + "'"
                    + formatOut.format(instance_end) + "'", "UTF-8");
            result = getServerResponse(jira_url_jql);

            JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(result);

            /* Total created bugs */
            String total_created = jsonObject.getString("total");

            /* Query to get the number of Fixed bugs for the specified sprint */
            jira_url_jql = ConfigurationManager.getProperty("jira_jql_url")
                    + URLEncoder.encode("sprint=" + sprintId + " and issuetype=Bug and (status = Resolved or status = Closed) &maxResults=500", "UTF-8");

            System.out.println("jira_url_jql = " + "sprint=" + sprintId + " and issuetype=Bug and (status = Resolved or status = Closed) &maxResults=500");

            result = getServerResponse(jira_url_jql);

            jsonObject = (JSONObject) JSONSerializer.toJSON(result);

            /* Total Fixed Bugs */
            String total_updated = jsonObject.getString("total");

            form.setCreated_count(Integer.parseInt(total_created));
            form.setUpdated_count(Integer.parseInt(total_updated));
            form.setDate(start_date);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return form;
    }


    /**
     *
     * Method to generate a map for the Date ---> BugTrend.
     * This method is common for requests from customers, components and regular Sprint tabs.
     * When the sprint mode is selected in the dashboard view, the sprint ID is passed as a JQL query else the start and end dates
     * are passed in the query
     *
     * @param sprintId
     *        Integer value of the sprint Id
     *
     * @param start_date
     *        String value of the Start date
     *
     * @param end_date
     *        String value of end date
     *
     * @param dateRange
     *        ArrayList containing all the dates between the start and end dates
     *
     * @param customer
     *        boolean value to check if the request was from the customer tab
     *
     * @param sprint
     *        boolean value to check if the sprint mode is enabled
     *
     * @param component
     *        boolean value to check if the request was from the component tab
     *
     * @param projectName
     *        String value of project name
     *
     * @param jqlQuery
     *        JQL query to be passed in the URL to request data from JIRA
     *
     * @return a HashMap: key - Date String, value - an object of the SprintTrendForm
     */
    public Map<String, SprintTrendForm> bugTrends(String sprintId, String start_date, String end_date, ArrayList dateRange, boolean customer, boolean sprint, boolean component, String projectName, String jqlQuery, String issuetype) {

        Map<String, SprintTrendForm> map = null;
        String jira_url_jql_created = null;
        String jira_url_jql_updated = null;
        String strIssueType="";
		if (issuetype.equalsIgnoreCase("bug")) {
			strIssueType = "issuetype=Bug";
		} else if (issuetype.equalsIgnoreCase("tasks")) {
			strIssueType = "(issuetype=Task or issuetype=Sub-task)";
		} else if (issuetype.equalsIgnoreCase("story")) {
			strIssueType = "(issuetype=Story)";
		}

        /* For requests other than customer and component */
        if (!customer && !component) {

            try {

                /* Query for created bugs  */
                if (sprint) {

                    /* If we need bugs for the created sprint, the sprint ID is specified in the request */
                    jira_url_jql_created = ConfigurationManager.getProperty("jira_jql_url") +
                            URLEncoder.encode("sprint=" + sprintId + " and "+strIssueType+" ORDER BY created ASC &maxResults=500", "UTF-8");
                    System.out.println("Created Query = " + "sprint=" + sprintId + " and "+strIssueType+" ORDER BY created ASC &maxResults=500");
                } else {

                    /* If not sprint, the project name is passed along with the date range */
                    jira_url_jql_created = ConfigurationManager.getProperty("jira_jql_url") +
                            URLEncoder.encode("project='" + projectName + "' and "+strIssueType+" and created >= '" + start_date + "' and created < '" + end_date + "' ORDER BY created ASC &maxResults=500", "UTF-8");
                    System.out.println("Created Query = " + "project='" + projectName + "' and "+strIssueType+" and created >= '" + start_date + "' and created < '" + end_date + "'ORDER BY created ASC &maxResults=500");
                }

                result = getServerResponse(jira_url_jql_created);
                JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(result);

                /* Get the issues array from the JSON response */
                JSONArray issues_array = jsonObject.getJSONArray("issues");

                /* Hashmap to keep a track of the date against the created and Fixed bugs
                *  The created and Fixed bugs are stored in an object of type SprintTrendForm */
                map = new HashMap<String, SprintTrendForm>(issues_array.size());

                /* Iterate the issues array to get the created date */
                for (int i = 0; i < issues_array.size(); i++) {

                    JSONObject issue = issues_array.getJSONObject(i);
                    JSONObject fields = issue.getJSONObject("fields");

                    String created_date_unformatted = fields.getString("created").split("T")[0];
                    if (compareDates(created_date_unformatted, start_date) >= 0) {

                        /* If the Hashmap does not contain the created date, a new object of SprintTrendForm is
                         * created and the created issues count is set to 1 and Fixed issues count is set to 0.
                         * For subsequent encounters of this date, the created count is updated in the 'Else' condition */
                        if (!map.containsKey(created_date_unformatted)) {
                            SprintTrendForm form = new SprintTrendForm();
                            form.setCreated_count(1);
                            form.setUpdated_count(0);
                            form.setDate(created_date_unformatted);
                            map.put(created_date_unformatted, form);
                            if (!dateRange.contains(created_date_unformatted))
                                dateRange.add(created_date_unformatted);

                        } else {

                            SprintTrendForm form = map.get(created_date_unformatted);
                            Integer created_count = form.getCreated_count();
                            form.setCreated_count(created_count + 1);

                        }
                    }
                }

                /* Query for Fixed bugs  */
                if (sprint) {

                    /* If we need bugs Fixed for the sprint, the sprint ID is specified in the request */
                    jira_url_jql_updated = ConfigurationManager.getProperty("jira_jql_url") +
                            URLEncoder.encode("sprint=" + sprintId + " and "+strIssueType+" AND (status = Closed OR status = Resolved) ORDER BY updated ASC, created ASC &maxResults=500", "UTF-8");
                    System.out.println("Updated Query = " + "sprint=" + sprintId + " and "+strIssueType+" AND (status = Closed OR status = Resolved) ORDER BY updated ASC, created ASC &maxResults=500");
                } else {

                    /* If not sprint, the project name is passed along with the date range */
                    jira_url_jql_updated = ConfigurationManager.getProperty("jira_jql_url") +
                            URLEncoder.encode("project='" + projectName + "' and "+strIssueType+" and resolutiondate >= '" + start_date + "' and resolutiondate < '" + end_date + "' ORDER BY updated ASC, created ASC &maxResults=500", "UTF-8");
                    System.out.println("Updated Query" + "project='" + projectName + "' and "+strIssueType+" and resolutiondate >= '" + start_date + "' and resolutiondate < '" + end_date + "' ORDER BY updated ASC, created ASC &maxResults=500");
                }

                result = getServerResponse(jira_url_jql_updated);
                jsonObject = (JSONObject) JSONSerializer.toJSON(result);

                /* Get the issues array from the JSON server response */
                issues_array = jsonObject.getJSONArray("issues");

                for (int i = 0; i < issues_array.size(); i++) {

                    JSONObject issue = issues_array.getJSONObject(i);
                    JSONObject fields = issue.getJSONObject("fields");

                    String resolved_date = fields.getString("resolutiondate").split("T")[0];

                    /* If the Hashmap does not contain the Fixed date, a new object of SprintTrendForm is
                     * created and the Fixed bug count is set to 1 and Created issues count is set to 0.
                     * For subsequent encounters of this date, the Fixed bug count is updated in the 'Else' condition */
                    if (!map.containsKey(resolved_date)) {
                        SprintTrendForm form = new SprintTrendForm();
                        form.setUpdated_count(1);
                        form.setCreated_count(0);
                        form.setDate(resolved_date);
                        map.put(resolved_date, form);
                        if (!dateRange.contains(resolved_date))
                            dateRange.add(resolved_date);

                    } else {

                        SprintTrendForm form = map.get(resolved_date);
                        form.setUpdated_count(form.getUpdated_count() + 1);

                    }

                }

                /* For all the dates that we not mentioned in the created or Fixed fields, a new object of SprintTrendForm is
                 * created with the Fixed bug count and created bug count set to 0. */
                for (int i = 0; i < dateRange.size(); i++) {
                    if (!map.containsKey(dateRange.get(i))) {
                        SprintTrendForm form = new SprintTrendForm();
                        form.setUpdated_count(0);
                        form.setCreated_count(0);
                        form.setDate((String) dateRange.get(i));
                        map.put((String) dateRange.get(i), form);

                    }
                }


            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
        }
        /* For requests other than customer and component */
        else {

            try {
                if (customer) {
                    jira_url_jql_created = ConfigurationManager.getProperty("jira_jql_url") +
                            URLEncoder.encode("project='" + projectName + "' and issuetype=Bug and (created >= " + "'"
                                    + start_date + "'" + " " + "and created < " + "'"
                                    + end_date + "')" + jqlQuery + " &maxResults=2000", "UTF-8");

                    System.out.println("Query = " + "project='" + projectName + "' and issuetype=Bug and (created >= " + "'"
                            + start_date + "'" + " " + "and created < " + "'"
                            + end_date + "')" + jqlQuery + " &maxResults=2000");
                } else if (component) {
                    jira_url_jql_created = ConfigurationManager.getProperty("jira_jql_url") +
                            URLEncoder.encode("issuetype=Bug and (created >= " + "'"
                                    + start_date + "'" + " " + "and created < " + "'"
                                    + end_date + "')" + jqlQuery + " &maxResults=2000", "UTF-8");

                    System.out.println("Query = issuetype=Bug and (created >= " + "'"
                            + start_date + "'" + " " + "and created < " + "'"
                            + end_date + "')" + jqlQuery + " &maxResults=2000");
                }

                result = getServerResponse(jira_url_jql_created);
                JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(result);
                JSONArray issues_array = jsonObject.getJSONArray("issues");
                map = new HashMap<String, SprintTrendForm>(issues_array.size());

                for (int i = 0; i < issues_array.size(); i++) {

                    JSONObject issue = issues_array.getJSONObject(i);
                    JSONObject fields = issue.getJSONObject("fields");

                    String created_date_unformatted = fields.getString("created").split("T")[0];
                    if (compareDates(created_date_unformatted, start_date) >= 0) {

                        if (!map.containsKey(created_date_unformatted)) {
                            SprintTrendForm form = new SprintTrendForm();
                            form.setCreated_count(1);
                            form.setUpdated_count(0);
                            form.setDate(created_date_unformatted);
                            map.put(created_date_unformatted, form);
                            if (!dateRange.contains(created_date_unformatted))
                                dateRange.add(created_date_unformatted);

                        } else {

                            SprintTrendForm form = map.get(created_date_unformatted);
                            Integer created_count = form.getCreated_count();
                            form.setCreated_count(created_count + 1);

                        }
                    }
                }

                if (customer) {
                    jira_url_jql_updated = ConfigurationManager.getProperty("jira_jql_url") +
                            URLEncoder.encode("project='"+ projectName +"' and issuetype=Bug and (resolutiondate >= " + "'"
                                    + start_date + "'" + " " + "and resolutiondate < " + "'"
                                    + end_date + "')" + jqlQuery + " &maxResults=2000", "UTF-8");

                    System.out.println("project='"+ projectName +"' and issuetype=Bug and (resolutiondate >= " + "'"
                            + start_date + "'" + " " + "and resolutiondate < " + "'"
                            + end_date + "')" + jqlQuery + " &maxResults=2000");

                } else if (component) {
                    jira_url_jql_updated = ConfigurationManager.getProperty("jira_jql_url") +
                            URLEncoder.encode("issuetype=Bug and (resolutiondate >= " + "'"
                                    + start_date + "'" + " " + "and resolutiondate < " + "'"
                                    + end_date + "')" + jqlQuery + " &maxResults=2000", "UTF-8");
                    System.out.println("issuetype=Bug and (resolutiondate >= " + "'"
                            + start_date + "'" + " " + "and resolutiondate < " + "'"
                            + end_date + "')" + jqlQuery + " &maxResults=2000");
                }

                result = getServerResponse(jira_url_jql_updated);
                jsonObject = (JSONObject) JSONSerializer.toJSON(result);
                issues_array = jsonObject.getJSONArray("issues");

                for (int i = 0; i < issues_array.size(); i++) {

                    JSONObject issue = issues_array.getJSONObject(i);
                    JSONObject fields = issue.getJSONObject("fields");

                    String resolved_date = fields.getString("resolutiondate").split("T")[0];

                    if (!map.containsKey(resolved_date)) {
                        SprintTrendForm form = new SprintTrendForm();
                        form.setUpdated_count(1);
                        form.setCreated_count(0);
                        form.setDate(resolved_date);
                        map.put(resolved_date, form);
                        if (!dateRange.contains(resolved_date))
                            dateRange.add(resolved_date);

                    } else {

                        SprintTrendForm form = map.get(resolved_date);
                        form.setUpdated_count(form.getUpdated_count() + 1);

                    }

                }

                for (int i = 0; i < dateRange.size(); i++) {
                    if (!map.containsKey(dateRange.get(i))) {
                        SprintTrendForm form = new SprintTrendForm();
                        form.setUpdated_count(0);
                        form.setCreated_count(0);
                        form.setDate((String) dateRange.get(i));
                        map.put((String) dateRange.get(i), form);

                    }
                }

                int remaining = 0, updated = 0;

                /* Iterate the hashmap to get the outstanding bug count = opened + fixed for a given date
                 * The outstanding bugs are added to the outstanding count of the next day.
                 * Basically, the 'updated' value is the cumulative count of bugs for the specified period of dates*/
                for (int i = 0; i < dateRange.size(); i++) {
                    String key =  (String)dateRange.get(i);
                    SprintTrendForm form = map.get(key);
                    int created_count = form.getCreated_count();
                    int updated_count = form.getUpdated_count();

                    remaining += created_count - updated_count;

                    /* This is to avoid the outstanding count from dropping to below 0 */
                    if (remaining < 0){
                        remaining = 0;
                    }
                    updated += updated_count;

                    form.setCreated_count(remaining);
                    form.setUpdated_count(updated);


                }

            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

        }

        return map;
    }


    
    /**
     * Method to compare dates that are passed as parameters
     * @param date1
     *        String date 1 to compare
     *
     * @param date2
     *        String date 2 to compare
     *
     * @return 0 if both dates are same, 1 if date 1 is greater than date 2, -1 if date 2 is greater than date 1
     */
    public int compareDates(String date1, String date2) {

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date_start = null, date_end = null;

        try {
            date_start = format.parse(date1);
            date_end = format.parse(date2);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date_start.compareTo(date_end) > 0) {
            return 1;
        } else if (date_start.compareTo(date_end) < 0) {
            return -1;
        } else {
            return 0;
        }
    }

    public static String trimNameJira(String name) {
        String tmpName = name;
        if (name != null && name.length() > 70) {
            tmpName = name.substring(0, 50) + "...";
        }
        return tmpName;
    }

    public static String trimNameJiraAssignee(String name) {
        String tmpName = name;
        if (name != null && name.length() > 17) {
            tmpName = name.substring(0, 14) + "...";
        }
        return tmpName;
    }

    /**
     * Method to generate dates between the passed start date and end date.
     *
     * @param from_date
     *        String value of the start date
     *
     * @param to_date
     *        String value of the end date
     *
     * @param customer
     *        boolean value to check if the request was from the customer tab of dashboard
     *
     * @param sprintBugs
     *        boolean value to check if the request was from the sprint tab
     *
     * @return an ArrayList of the all the dates in String format betweent the from and to date.
     */
    public static ArrayList getDatesBetweenDate(String from_date, String to_date, boolean customer, boolean sprintBugs) {

        ArrayList dates = new ArrayList();

        ArrayList datesList = new ArrayList();
        SimpleDateFormat format;

        /* Input format for the customer date field is different from other date fields */
        if (customer) {
            format = new SimpleDateFormat("yyyy/MM/dd",
                    Locale.ENGLISH);
        } else {
            format = new SimpleDateFormat("dd/MMM/yy",
                    Locale.ENGLISH);
        }

        DateFormat formatter;
        SimpleDateFormat formatOut = null;
        Date instance_start = null, instance_end = null;

        try {

            instance_start = format.parse(from_date);

            instance_end = format.parse(to_date);

            /* Different output date format for sprint related bugs and otherwise */
            if (!sprintBugs) {
                formatOut = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
            } else {
                formatOut = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            }

            formatOut.format(instance_start);
            formatOut.format(instance_end);

        } catch (ParseException e) {

            e.printStackTrace();

        }

        /* Different output date format for sprint related bugs and otherwise */
        if (!sprintBugs) {
            formatter = new SimpleDateFormat("yyyy/MM/dd");
        } else {
            formatter = new SimpleDateFormat("yyyy-MM-dd");
        }

        Date startDate = null;
        Date endDate = null;
        try {
            startDate = (Date) formatter
                    .parse(formatOut.format(instance_start));

            endDate = (Date) formatter.parse(formatOut.format(instance_end));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        /* Algorithm to compute the date betweent the given dates*/
        long interval = 24 * 1000 * 60 * 60; // 1 hour in millis
        long endTime = endDate.getTime(); // create your endtime here, possibly

        // using Calendar or Date
        long curTime = startDate.getTime();
        while (curTime <= endTime) {
            dates.add(new Date(curTime));
            curTime += interval;
        }
        for (int i = 0; i < dates.size(); i++) {
            Date lDate = (Date) dates.get(i);
            String ds = formatter.format(lDate);
            datesList.add(ds);

            System.out.println(" Date is ..." + ds);
        }
        Date date = new Date();
        String last_date = (String) datesList.get(datesList.size() - 1);

        try {
            /* Adds an extra date to the last_date*/
            Date extra_date = formatter.parse(last_date);
            Calendar c = Calendar.getInstance();
            c.setTime(extra_date);
            c.add(Calendar.DATE, 1);
            String extra_date_string = formatter.format(c.getTime());

            datesList.add(extra_date_string);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return datesList;

    }

    /**
     *
     * Method to format the date to Stash compatible for the commits field in the dashboard
     *
     * @param fromDate
     *        String value of the start date
     *
     * @param toDate
     *        String value of the end date
     *
     * @param form
     *        a JiraTrendsForm object which will be updated with the formatted value of start and end dates
     */

    public void formatDates_stash(String fromDate, String toDate, JiraTrendsForm form) {

        /* Input date format*/
        SimpleDateFormat format = new SimpleDateFormat("dd/MMM/yy",
                Locale.ENGLISH);

        SimpleDateFormat formatOut;
        Date instance_start, instance_end;

        try {
            instance_start = format.parse(fromDate);

            instance_end = format.parse(toDate);

            /* Output date format */
            formatOut = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

            String start_date = formatOut.format(instance_start);
            String end_date = formatOut.format(instance_end);

            System.out.println("Start: " + start_date + ", End: " + end_date);

            form.setStart_date(start_date);
            form.setEnd_date(end_date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * This is the method which is used to store all the issue related information which is displayed for sprints as well
     * as the customers and components in the 'Issue Summary' tab of the dashboard.
     *
     * @param jqlQuery
     *        The JQL query to pass in the URL to request all issues from JIRA.
     * @return a SprintIssueForm object which contains all the information used to populate the the Issue Summary pages
     *
     */

    public SprintIssueForm jqlGeneral(String jqlQuery) {

        String days_open = null, severity;

        try {
            System.out.println("JQL: " + ConfigurationManager.getProperty("jira_jql_url") + jqlQuery);
            result = getServerResponse(ConfigurationManager.getProperty("jira_jql_url") + URLEncoder.encode(jqlQuery, "UTF-8"));
            System.out.println("Result = " + result);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("result = " + result);

        JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(result);

        /* extract the "issues" array  from the JSON server response*/
        JSONArray issues = jsonObject.getJSONArray("issues");
        System.out.println("Issues Array: " + issues.toString());

        SprintIssueForm form = new SprintIssueForm();
        form.setTotal_count(issues.size());

        /* ArrayList to store all the issue related information and then copy this information into the SprintIssueForm object */
        ArrayList<String> keys_completedIssues, summaries_completedIssues, assigneeNames_completedIssues, statusNames_completedIssues, creationDate_completedIssues, issueType_completedIssues, components, customerNames, daysOpen, severityList;
        String issue_key, issue_type, component_name;
        HashMap<String, Integer> bugs_open_map, bugs_closed_map, bugs_inProgress_map, bugs_resolved_map;


        int resolved_count = 0, open_count = 0, inProgress_count = 0, closed_count = 0, verified_count = 0, newFeature_count = 0, otherIssue_count = 0;
        int bugs_open = 0, bugs_inprogress = 0, bugs_resolved = 0, bugs_closed = 0;
        int tasks_open = 0, tasks_inprogress = 0, tasks_resolved = 0, tasks_closed = 0;
        int story_open = 0, story_inprogress = 0, story_resolved = 0, story_closed = 0;
        int bug_count = 0, task_count = 0, story_count = 0;

        /* Initializing all ArrayLists with the issues array size */
        keys_completedIssues = new ArrayList<String>(issues.size());
        summaries_completedIssues = new ArrayList<String>(issues.size());
        assigneeNames_completedIssues = new ArrayList<String>(issues.size());
        statusNames_completedIssues = new ArrayList<String>(issues.size());
        creationDate_completedIssues = new ArrayList<String>(issues.size());
        issueType_completedIssues = new ArrayList<String>(issues.size());
        components = new ArrayList<String>(issues.size());
        customerNames = new ArrayList<String>(issues.size());
        daysOpen = new ArrayList<String>(issues.size());
        severityList = new ArrayList<String>(issues.size());

        /* Hashmap to keep a track of Each issue type and its corresponding status */
        bugs_open_map = new HashMap<String, Integer>(issues.size());
        bugs_closed_map = new HashMap<String, Integer>(issues.size());
        bugs_inProgress_map = new HashMap<String, Integer>(issues.size());
        bugs_resolved_map = new HashMap<String, Integer>(issues.size());

        System.out.println("issues.size() = " + issues.size());

        /* Iterate all the indexes of the issues array */
        for (int i = 0; i < issues.size(); i++) {

            JSONObject issue = issues.getJSONObject(i);

            System.out.println("issue = " + issue);
            issue_key = issue.getString("key");
            keys_completedIssues.add(i, issue_key);

            /* Extract the fields object from each issues array index */
            JSONObject fields = issue.getJSONObject("fields");

            System.out.println("fields = " + fields);


            try {
                /* Get Assignee Name */
                JSONObject assigneeObject = fields.getJSONObject("assignee");
                System.out.println("assigneeObject = " + assigneeObject);
                assigneeNames_completedIssues.add(i, assigneeObject.getString("displayName"));
                System.out.println("assignee Name = " + assigneeObject.getString("displayName"));
            } catch (Exception e) {
                assigneeNames_completedIssues.add(i, "Un-Assigned");
            }

            /* Get Component name */
            try {
                JSONArray componentsArray = fields.getJSONArray("components");
                JSONObject component = componentsArray.getJSONObject(0);
                component_name = component.getString("name");
                System.out.println("component_name = " + component_name);
            } catch (Exception e) {
                component_name = "Un-Assigned";
            }

            /* Add component to form object */
            components.add(component_name);

            /* Get Customer name if assigned */
            String customerName;
            try {
                JSONObject customerObject = fields.getJSONObject("customfield_10100");
                customerName = customerObject.getString("value");
            } catch (Exception e) {
                customerName = "Un-Assigned";
            }

            System.out.println("customerName = " + customerName);
            customerNames.add(customerName);


            /* Get issues Status */
            JSONObject statusObject = fields.getJSONObject("status");
            String status = statusObject.getString("name");

            /* Get Severity */
            try {
                JSONObject severity_object = fields.getJSONObject("customfield_10202");
                severity = severity_object.getString("value");
            } catch (Exception e) {
                severity = "Un-Assigned";
            }

            severityList.add(severity);

            /* For each issue status, get the number of days the issue was open or is still open */
            if (status.equals("Closed") || status.equals("Resolved")) {
                closed_count++;
                status = "Fixed";
                days_open = getDaysOpen(fields.getString("created").split("T")[0], false, fields.getString("resolutiondate").split("T")[0]);
            }
            else if (status.equals("Open") || status.equals("Reopened")) {
                open_count++;
                days_open = getDaysOpen(fields.getString("created").split("T")[0], true, null);
            }
            else if (status.equals("In Progress")) {
                inProgress_count++;
                days_open = getDaysOpen(fields.getString("created").split("T")[0], true, null);
            }
            else if (status.equals("Verified")) {
                verified_count++;
                days_open = getDaysOpen(fields.getString("created").split("T")[0], false, fields.getString("resolutiondate").split("T")[0]);
            }

            System.out.println("status = " + status);

            daysOpen.add(days_open);

            statusNames_completedIssues.add(i, status);
            summaries_completedIssues.add(i, fields.getString("summary"));

            /* Get the issue type */
            JSONObject issueType = fields.getJSONObject("issuetype");
            issue_type = issueType.getString("name");
            issueType_completedIssues.add(i, issue_type);

            /* For each type of issue, insert the component information into the respective hashmap.
             * Each hashmap is updated with based on the component name and status */
            if (issue_type.equals("Bug")) {
                bug_count++;
                if (status.equals("Open") || status.equals("Reopened")) {
                    try {
                        int open_count_component = bugs_open_map.get(component_name);
                        open_count_component += 1;
                        bugs_open_map.put(component_name, open_count_component);
                    } catch (NullPointerException e) {
                        bugs_open_map.put(component_name, 1);
                    }
                    bugs_open++;
                } else if (status.equals("Fixed")) {
                    try {
                        int closed_count_component = bugs_closed_map.get(component_name);
                        closed_count_component += 1;
                        bugs_closed_map.put(component_name, closed_count_component);
                    } catch (NullPointerException e) {
                        bugs_closed_map.put(component_name, 1);
                    }
                    bugs_closed++;
                } else if (status.equals("In Progress")) {
                    if (bugs_inProgress_map.get(component_name) == null) {
                        bugs_inProgress_map.put(component_name, 1);
                    } else {
                        int inProgress_count_component = bugs_inProgress_map.get(component_name);
                        inProgress_count_component += 1;
                        bugs_inProgress_map.put(component_name, inProgress_count_component);
                    }
                    bugs_inprogress++;
                }

            } else if (issue_type.equals("Task") || issue_type.equals("Sub-task")) {
                task_count++;
                if (status.equals("Open") || status.equals("Reopened"))
                    tasks_open++;
                else if (status.equals("Fixed"))
                    tasks_closed++;
                else if (status.equals("In Progress"))
                    tasks_inprogress++;
            } else if (issue_type.equals("Story")) {
                story_count++;
                if (status.equals("Open") || status.equals("Reopened"))
                    story_open++;
                else if (status.equals("Fixed"))
                    story_closed++;
                else if (status.equals("In Progress"))
                    story_inprogress++;
            } else if (issue_type.equals("New Feature"))
                newFeature_count++;
            else
                otherIssue_count++;

            System.out.println("Completed Issues--------> Name: " + keys_completedIssues.get(i)
                    + ", Status " + statusNames_completedIssues.get(i));
        }

        /* Assign all the information to the SprintIssueForm object */
        form.setAssigneeNames_incompleteIssues(assigneeNames_completedIssues);
        form.setCreationDate_incompleteIssues(creationDate_completedIssues);
        form.setIssueType_incompleteIssues(issueType_completedIssues);
        form.setKeys_incompleteIssues(keys_completedIssues);
        form.setStatusNames_incompleteIssues(statusNames_completedIssues);
        form.setSummaries_incompleteIssues(summaries_completedIssues);

        form.setBugs_open(bugs_open);
        form.setBugs_closed(bugs_closed);
        form.setBugs_inprogress(bugs_inprogress);
        form.setBugs_resolved(bugs_resolved);

        form.setTasks_open(tasks_open);
        form.setTasks_closed(tasks_closed);
        form.setTasks_inprogress(tasks_inprogress);
        form.setTasks_resolved(tasks_resolved);

        form.setStory_open(story_open);
        form.setStory_closed(story_closed);
        form.setStory_inprogress(story_inprogress);
        form.setStory_resolved(story_resolved);

        form.setBug_count(bug_count);
        form.setStory_count(story_count);
        form.setTask_count(task_count);

        form.setOpen_count(open_count);
        form.setInProgress_count(inProgress_count);
        form.setVerified_count(verified_count);
        form.setClosed_count(closed_count);
        form.setResolved_count(resolved_count);

        form.setComponents(components);
        form.setBugs_open_map(bugs_open_map);
        form.setBugs_closed_map(bugs_closed_map);
        form.setBugs_resolved_map(bugs_resolved_map);
        form.setBugs_inProgress_map(bugs_inProgress_map);

        form.setCustomerName(customerNames);
        form.setDaysOpen(daysOpen);

        form.setSeverityList(severityList);
        return form;

    }

    /**
     * Method to generate the graph data String for the 'Component Issue Summary' pie charts
     * @param form
     *        SprintIssueForm object to get the values of open, closed and in progress bugs.
     *
     * @param component_keys
     *        An ArrayList of Strings containing the names of components that appeared for a duration
     * @return Column chart data String to show the bifurcation of status' of all components
     */
    public String individualComponents(SprintIssueForm form, ArrayList<String> component_keys) {

        String individual_component_graph = "[['Status','Open','Closed','In Progress',{ role: 'annotation' }],";

        for (String key : component_keys) {

            int open_count;
            try {
                open_count = form.getBugs_open_map().get(key);
            } catch (NullPointerException e) {
                open_count = 0;
            }
            int closed_count;
            try {
                closed_count = form.getBugs_closed_map().get(key);
            } catch (NullPointerException e) {
                closed_count = 0;
            }
            int inProgress_count;
            try {
                inProgress_count = form.getBugs_inProgress_map().get(key);
            } catch (NullPointerException e) {
                inProgress_count = 0;
            }

            System.out.println(key + " = " + open_count + ", " + closed_count + ", " + inProgress_count);

            individual_component_graph += "['" + key + "'," + open_count + "," + closed_count + "," + inProgress_count + ",''],";
        }

        individual_component_graph = (individual_component_graph.substring(0, individual_component_graph.length() - 1)) + "]";
        System.out.println("individual_component_graph = " + individual_component_graph);

        return individual_component_graph;
    }

    /**
     * Method to get the number of days the issue/bug has been open for since the day it was created to the resolution day
     * @param date1
     *        Created date String
     *
     * @param date2
     *        Resolution date String
     *
     * @return the integer value for Open days
     */
    public int getOpenDays(Date date1, Date date2){

        Calendar cal1 = new GregorianCalendar();
        Calendar cal2 = new GregorianCalendar();

        cal1.setTime(date1);
        cal2.setTime(date2);

        return (int)( (cal1.getTime().getTime() - cal2.getTime().getTime()) / (1000 * 60 * 60 * 24));
    }

    /**
     * Method to get the number of days the issue/bug has been open (not fixed)
     * @param createdDate
     *        String value of the created date
     *
     * @param current_open
     *        boolean value to indicate if the issue is currently open
     *
     * @param resolutionDate
     *        String value of the date the issue was resolved if the above parameter is false
     *
     * @return String value of the number of days the issue was open
     */
    public String getDaysOpen(String createdDate, boolean current_open, String resolutionDate){

        /* Check if the issue is still 'Open' */
        if (current_open) {
            Date created_date = convertStringToDate(createdDate);
            Date today_date = new Date();

            int openedSince = getOpenDays(today_date, created_date);
            System.out.println("openedSince = " + openedSince);

            return "" + openedSince;
        }
        else {
            Date created_date = convertStringToDate(createdDate);
            Date resolvedOn = convertStringToDate(resolutionDate);

            int openDays = getOpenDays(resolvedOn, created_date);

            return  "" + openDays;
        }
    }

    /**
     * Method to convert the String input to Date object
     * @param dateIn
     *        Input String value of date
     *
     * @return the Date object for the given input String
     */
    public Date convertStringToDate(String dateIn){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date dateOut = null;
        try {
            dateOut = format.parse(dateIn);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateOut;
    }

    /**
     * Method that sends the Jira server a request for the input URL string
     * @param url
     *        String value of the Jira REST end point
     *
     * @return server response string
     */
    public String getServerResponse(String url) {
        InputStream is;
        InputStreamReader isr;
        try {
            result = sendGet(url);
            if (result != null)
                return result;
            else {
                return sendGet(url);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * Method that adds Cookie header (session ID) to the GET request and sends the request to Server
     * If the server sends 2xx response code, the Response Body is passed to the requesting method
     * Else, the getAuthorization() method is called that generates the new session ID which is stored for subsequent requests.
     *
     * @param url
     *        The End point of the server
     *
     * @return the server response String
     * @throws Exception
     *         In case any other response code other than 2xx is sent from the server, the exception calls the getAuthorization() method
     */
    private String sendGet(String url) throws Exception {


        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);

        // add request header

        request.addHeader("Cookie", sessionId);


        HttpResponse response = client.execute(request);

        System.out.println("\nSending 'GET' request to URL : " + url);
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("Response Code : " + statusCode);
        if (statusCode == 200 || statusCode == 201) {
            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);
            return responseBody;
        } else {
            getAuthorization(url);
            sendGet(url);
        }

        return null;

    }
    
    public String formatDateRange(String strDate){
    	SimpleDateFormat existingFormat = new SimpleDateFormat("yyyy-MM-dd");

		SimpleDateFormat newFormat = new SimpleDateFormat("MM/dd/yy");

    	String tempDateStr = null;

    		try {
    			tempDateStr=newFormat.format(existingFormat.parse(strDate));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

    	}
    	return tempDateStr;
    }
   
}
