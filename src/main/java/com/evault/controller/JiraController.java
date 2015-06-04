package com.evault.controller;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.evault.form.*;
import com.evault.utils.*;
import org.dom4j.rule.Mode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Jayanth Nallapothula on 3/23/15. This is the main Controller Class
 * for Jira
 */

@Controller
public class JiraController {

	static String trendData = null;
	static SprintIssueForm global_IssueForm;
	static TestLinkForm global_AutomationForm;
	static String global_BugTrend;

	/**
	 * This method is the controller handler for 'Issue Summary' button on the
	 * sprints tab
	 * 
	 * @param model
	 *            Model object to pass data to the View
	 * @param projectId
	 *            Project ID value as String
	 * @param sprintId
	 *            Sprint ID value as String
	 * @param projectName
	 *            String value of project name
	 * @return A ModelAndView object containing the View (issueDetails.jsp)
	 *         called and Model passed.
	 */
	@RequestMapping(value = "/issueReport/{project}/{sprint}/{projectName}/{all}", method = RequestMethod.GET)
	public ModelAndView showIssueReport(Model model,
			@PathVariable("project") String projectId,
			@PathVariable("sprint") String sprintId,
			@PathVariable("projectName") String projectName,
			@PathVariable("all") String all) {

		System.out.println("Selected Project Id: " + projectId + ", "
				+ projectName + ", " + sprintId);

		if (projectName.equals("- Select Project -")) {
			return showIssueReport(model, projectId, sprintId, "LTS2 Scality",
					null);
		}

		/*
		 * An object of the Jira Utils class which contains all data processing
		 * and fetching methods
		 */
		JiraUtilsNew jiraUtils = new JiraUtilsNew();

		/*
		 * The getDateRange() method will return a form of type JiraTrendsForm
		 * that contains the start and end date of the sprint selected by the
		 * user on the dashboard homepage
		 */
		JiraTrendsForm trendsForm = jiraUtils.getDateRange(
				Integer.parseInt(projectId), Integer.parseInt(sprintId));

		/*
		 * jqlGeneral() method processes all the issues for the selected sprint
		 * and returns a form containing the data for all the issues in a given
		 * sprint
		 */
		SprintIssueForm form = jiraUtils.jqlGeneral("sprint=" + sprintId
				+ " &maxResults=500");

		/* Contains the Bug count for the open, closed and In progress status' */
		String bug_graph = "[['Bugs','Count'],['Open'," + form.getBugs_open()
				+ "]" + ",['Fixed'," + form.getBugs_closed() + "]"
				+ ",['In Progress'," + form.getBugs_inprogress() + "]]";

		/* Contains the Task count for the open, closed and In progress status' */
		String tasks_graph = "[['Bugs','Count'],['Open',"
				+ form.getTasks_open() + "]" + ",['Fixed',"
				+ form.getTasks_closed() + "]" + ",['In Progress',"
				+ form.getTasks_inprogress() + "]]";

		/* Contains the Story count for the open, closed and In progress status' */
		String story_graph = "[['Bugs','Count'],['Open',"
				+ form.getStory_open() + "]" + ",['Fixed',"
				+ form.getStory_closed() + "]" + ",['In Progress',"
				+ form.getStory_inprogress() + "]]";

		/* Passing all the data to the View for displaying */
		model.addAttribute("bugGraph", bug_graph);
		model.addAttribute("taskGraph", tasks_graph);
		model.addAttribute("storyGraph", story_graph);
		model.addAttribute("projectName", projectName);
		model.addAttribute("fromDate", trendsForm.getStart_date());
		model.addAttribute("toDate", trendsForm.getEnd_date());

		global_IssueForm = form;

		return new ModelAndView("issueDetails", "SprintIssueForm", form);
	}

	/**
	 * This method is used to process the Bug Trend request issued by the 'Bug
	 * Trends' button on the dashboard homepage
	 * 
	 * @param projectId
	 *            Project ID value as String
	 * @param sprintId
	 *            Sprint ID value as String
	 * @param model
	 *            Model object to pass data to the View
	 * @param projectName
	 *            String value of project name
	 * @param fromDate
	 *            Start date if the sprint mode is not selected else 'none'
	 * @param toDate
	 *            End date if the sprint mode is not selected else 'none'
	 * @return A ModelAndView object containing the View (displayTrends.jsp)
	 *         called and Model passed.
	 */
	@RequestMapping(value = "/jiraTrends/{projectId}/{sprintId}/{projectName}/{fromDate}/{toDate}/{all}", method = RequestMethod.GET)
	public ModelAndView showSprintTrends(@PathVariable String projectId,
			@PathVariable String sprintId, Model model,
			@PathVariable("projectName") String projectName,
			@PathVariable("fromDate") String fromDate,
			@PathVariable("toDate") String toDate,
			@PathVariable("all") String all) {

		System.out.println("Rapid is: " + projectId + ", Sprint is: "
				+ sprintId);

		/*
		 * An object of the Jira Utils class which contains all data processing
		 * and fetching methods
		 */
		JiraUtilsNew jiraUtils = new JiraUtilsNew();
		JiraTrendsForm jiraTrendsForm = null;
		ArrayList dateRange;
		int total_created = 0, total_updated = 0;
		int total_tasks_created = 0, total_tasks_updated = 0;
		int total_story_created = 0, total_story_updated = 0;

		/* variable to check if the sprint mode was selected */
		boolean sprint;

		/*
		 * The dates will be null if the sprint mode was selected in the
		 * dashboard page
		 */
		if ((!(fromDate.equals("null"))) && (!(toDate.equals("null")))) {

			/* Sprint mode not selected */

			fromDate = fromDate.replace("-", "/");
			toDate = toDate.replace("-", "/");

			/*
			 * getDatesBetweenDate() generates an ArrayList of all the dates
			 * between the fromDate and toDate
			 */
			dateRange = JiraUtilsNew.getDatesBetweenDate(fromDate, toDate,
					false, true);

			/* Sprint mode is set to false */
			sprint = false;
		}

		/* Sprint mode selected */
		else {

			/* Sprint mode is set to True */
			sprint = true;

			/*
			 * Since sprint mode was selected, the start date and end date need
			 * to be determined which is done with getDateRange()
			 */
			jiraTrendsForm = jiraUtils.getDateRange(
					Integer.parseInt(projectId), Integer.parseInt(sprintId));

			/*
			 * The date format is processed with formatted as the input date
			 * format
			 */
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yy");

			System.out.println("From Date: " + jiraTrendsForm.getStart_date());
			System.out.println("To Date: " + jiraTrendsForm.getEnd_date());

			String today = new SimpleDateFormat("dd/MMM/yy").format(new Date());
			Date today_date;
			try {
				today_date = formatter.parse(today);
				Date endDate = formatter.parse(jiraTrendsForm.getEnd_date());
				System.out.println("Today: " + today + "End Date: "
						+ jiraTrendsForm.getEnd_date());
				System.out.println("Today: " + today_date.toString()
						+ "End Date: " + endDate.toString());

				/*
				 * To check if the end date is after Today's date. In this case,
				 * today's date is selected as end date
				 */
				if (endDate.after(today_date)) {
					jiraTrendsForm.setEnd_date(today);
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}

			/*
			 * Now the dates between the sprint start and end date are generated
			 * and returned as an ArrayList using getDatesBetweenDate()
			 */
			dateRange = jiraUtils.getDatesBetweenDate(
					jiraTrendsForm.getStart_date(),
					jiraTrendsForm.getEnd_date(), false, true);
		}

		String start_date = null, end_date = null;
		// ArrayList<SprintTrendForm> sprintForm = new
		// ArrayList<SprintTrendForm>(dateRange.size());
		String graphData = "";
		Map<String, SprintTrendForm> map;
		Map<String, SprintTrendForm> tasksmap = null;
		Map<String, SprintTrendForm> storymap = null;

		SprintTrendForm form = new SprintTrendForm();
		form.setCreated_count(total_created);
		form.setUpdated_count(total_updated);

		Integer created_count = 0, updated_count = 0;
		Integer created_tasks_count = 0, updated_tasks_count = 0;
		Integer created_story_count = 0, updated_story_count = 0;

		String current_date = null;

		/*
		 * The bugTrends() method is used to generate the Hashmap for the key:
		 * date, value: SprintTrendForm containing the Opened and Fixed bugs for
		 * that given date
		 */
		if (sprint) {
			map = jiraUtils.bugTrends(sprintId, (String) dateRange.get(0),
					(String) dateRange.get(dateRange.size() - 1), dateRange,
					false, true, false, projectName, null, "bug");
			if (all.equalsIgnoreCase("all")) {
				tasksmap = jiraUtils.bugTrends(sprintId,
						(String) dateRange.get(0),
						(String) dateRange.get(dateRange.size() - 1),
						dateRange, false, true, false, projectName, null,
						"tasks");
				storymap = jiraUtils.bugTrends(sprintId,
						(String) dateRange.get(0),
						(String) dateRange.get(dateRange.size() - 1),
						dateRange, false, true, false, projectName, null,
						"story");

			}

		}

		else {
			map = jiraUtils.bugTrends(sprintId, (String) dateRange.get(0),
					(String) dateRange.get(dateRange.size() - 1), dateRange,
					false, false, false, projectName, null, "bug");
			if (all.equalsIgnoreCase("all")) {
				tasksmap = jiraUtils.bugTrends(sprintId,
						(String) dateRange.get(0),
						(String) dateRange.get(dateRange.size() - 1),
						dateRange, false, false, false, projectName, null,
						"tasks");
				storymap = jiraUtils.bugTrends(sprintId,
						(String) dateRange.get(0),
						(String) dateRange.get(dateRange.size() - 1),
						dateRange, false, false, false, projectName, null,
						"story");

			}

		}

		System.out.println("<----------------- Updated ------------------>");

		/* Iterator to iterate the HashMap generated in the above step */
		// Iterator it = map.entrySet().iterator();
		// while (it.hasNext()) {
		// Map.Entry pair = (Map.Entry) it.next();
		// SprintTrendForm form_new = (SprintTrendForm) pair.getValue();
		//
		// System.out.println(pair.getKey() + " = " +
		// form_new.getCreated_count() + "," + form_new.getUpdated_count());
		// }

		/*
		 * Generating the graph data for the bug trend graph dateRange is the
		 * ArrayList containing all the dates between the start and end dates
		 */
		Collections.sort(dateRange);

		for (int i = 0; i < dateRange.size(); i++) {
			System.out.println("***** " + dateRange.get(i));
			if (map.get(dateRange.get(i)) != null) {
				created_count = map.get(dateRange.get(i)).getCreated_count();
				updated_count = map.get(dateRange.get(i)).getUpdated_count();
				current_date = jiraUtils.formatDateRange(map.get(
						dateRange.get(i)).getDate());
				total_created += map.get(dateRange.get(i)).getCreated_count();
				total_updated += map.get(dateRange.get(i)).getUpdated_count();
			}
			if (all.equalsIgnoreCase("all")) {

				if (tasksmap.get(dateRange.get(i)) != null) {
					created_tasks_count = tasksmap.get(dateRange.get(i))
							.getCreated_count();
					updated_tasks_count = tasksmap.get(dateRange.get(i))
							.getUpdated_count();
					current_date = jiraUtils.formatDateRange(tasksmap.get(
							dateRange.get(i)).getDate());
					total_tasks_created += tasksmap.get(dateRange.get(i))
							.getCreated_count();
					total_tasks_updated += tasksmap.get(dateRange.get(i))
							.getUpdated_count();
				}
				if (storymap.get(dateRange.get(i)) != null) {
					created_story_count = storymap.get(dateRange.get(i))
							.getCreated_count();
					updated_story_count = storymap.get(dateRange.get(i))
							.getUpdated_count();
					current_date = jiraUtils.formatDateRange(storymap.get(
							dateRange.get(i)).getDate());
					total_story_created += storymap.get(dateRange.get(i))
							.getCreated_count();
					total_story_updated += storymap.get(dateRange.get(i))
							.getUpdated_count();
				}
				graphData += ",['" + current_date + "'," + created_count + ","
						+ updated_count + "," + created_tasks_count + ","
						+ updated_tasks_count + "," + created_story_count + ","
						+ updated_story_count + "]";
			} else {
				graphData += ",['" + current_date + "'," + created_count + ","
						+ updated_count + "]";
			}

		}
		if (all.equalsIgnoreCase("all")) {

			graphData = "[['Date','Bug Opened: " + total_created
					+ "',' Bug Fixed: " + total_updated + "','Tasks Opened: "
					+ total_tasks_created + "',' Tasks Fixed: "
					+ total_tasks_updated + "','Story Opened: "
					+ total_story_created + "',' Story Fixed: "
					+ total_story_updated + "']" + graphData + "]";
		} else {
			graphData = "[['Date','Bug Opened: " + total_created
					+ "',' Bug Fixed: " + total_updated + "']" + graphData
					+ "]";
		}

		/*
		 * Since the dates for sprint mode will be sprint specific and
		 * non-sprint mode will be passed in the URL, the dates are set
		 * separately
		 */
		if (!sprint) {
			model.addAttribute("fromDate", dateRange.get(0));
			model.addAttribute("toDate", dateRange.get(dateRange.size() - 1));
		} else {
			model.addAttribute("fromDate", jiraTrendsForm.getStart_date());
			model.addAttribute("toDate", jiraTrendsForm.getEnd_date());
		}

		/*
		 * Since the same jsp is used to display the bug trends for customers,
		 * the "customer" key will be used to distinguish this method from the
		 * customers method
		 */
		model.addAttribute("customer", "not customer");
		model.addAttribute("projectName", projectName);

		global_BugTrend = graphData;

		return new ModelAndView("displayTrends", "graphData", graphData);
	}

	/**
	 * This is the handler for comparing the sprint bug trends.
	 * 
	 * @param project
	 *            Project ID value as String
	 * @param sprintIds
	 *            List of Sprint ID's to be compared (comma separated), else
	 *            null
	 * @param model
	 *            Model object to pass data to the View
	 * @param projectName
	 *            String value of project name
	 * @return A ModelAndView object containing the View
	 *         (displaySprintComparison.jsp) called and Model passed.
	 */
	@RequestMapping(value = "/sprintTrends/{project}/{sprintIds}/{projectName}/{both}", method = RequestMethod.GET)
	protected ModelAndView compareSprints(
			@PathVariable("project") String project,
			@PathVariable("sprintIds") String sprintIds, Model model,
			@PathVariable("projectName") String projectName,
			@PathVariable("both") String both) {

		System.out.println("Sprints are: " + sprintIds);
		System.out.println("both = " + both);

		String[] sprints = sprintIds.split(",");
		System.out.println(sprints);

		/*
		 * An object of the Jira Utils class which contains all data processing
		 * and fetching methods
		 */
		JiraUtilsNew jiraUtils = new JiraUtilsNew();
		String graphData = "";

		/* A separate form for each sprint */
		ArrayList<JiraTrendsForm> form = new ArrayList<JiraTrendsForm>(
				sprints.length);
		int total_created = 0, total_updated = 0;
		ArrayList<SprintTrendForm> sprintData = new ArrayList<SprintTrendForm>(
				sprints.length);

		/*
		 * In this segment, the start and end dates for each sprint are
		 * generated using the getDateRange() method. Using the
		 * JqlSprintToSprint() method, the bugs count for opened bugs and fixed
		 * bugs is generated as a form and added to the sprintData ArrayList.
		 * The total_Created and total_updated counts are incremented for each
		 * iteration
		 */
		for (int i = 0; i < sprints.length; i++) {
			form.add(jiraUtils.getDateRange(Integer.parseInt(project),
					Integer.parseInt(sprints[i])));
			sprintData.add(i, jiraUtils.JqlSprintToSprint(sprints[i],
					form.get(i).getStart_date(), form.get(i).getEnd_date(),
					projectName));

			total_created += sprintData.get(i).getCreated_count();
			total_updated += sprintData.get(i).getUpdated_count();
		}
		System.out.println("Size: " + sprintData.size());

		/*
		 * This segment arranges the bug comparison graph data as Latest date to
		 * previous dates (Reverse chronological order)
		 */
		for (int i = sprintData.size() - 1; i >= 0; i--) {
			graphData += ",['" + sprintData.get(i).getDate() + "',"
					+ sprintData.get(i).getCreated_count() + ","
					+ sprintData.get(i).getUpdated_count() + "]";
		}

		/* Passing necessary data to the View */
		graphData = "[['Date','Opened','Fixed']" + graphData + "]";
		model.addAttribute("total_opened", total_created);
		model.addAttribute("total_closed", total_updated);
		model.addAttribute("toDate", form.get(0).getEnd_date());
		model.addAttribute("fromDate", form.get(sprints.length - 1)
				.getStart_date());

		/*
		 * If 'Both' button is clicked, the data for Automation Comparison is
		 * processed and the result is sent to the view to display
		 */
		if (both.equals("true")) {
			displayAutomationComparisonTrend(project, sprintIds, model, "true");
			model.addAttribute("trendData", trendData);
			model.addAttribute("both", "both");
		}

		return new ModelAndView("displaySprintComparison", "graphData",
				graphData);
	}

	/**
	 * Unused method
	 * 
	 * @return
	 */
	@RequestMapping(value = "/jiraDashboard", method = RequestMethod.GET)
	public ModelAndView getSprints() {

		JiraUtilsNew jiraUtils = new JiraUtilsNew();
		ArrayList<ProjectDetails> rapidView = jiraUtils.getRapidViewInfo();
		SprintForm form = new SprintForm();
		form.setRapidView(rapidView);

		return new ModelAndView("selectSprints", "sprintForm", form);
	}

	/**
	 * This is the handler for a selected project in the sprints tab. Each time
	 * the project is changed, this method is invoked.
	 * 
	 * @param name
	 *            The integer value of the project ID
	 * @return A ModelAndView object containing the View (selectSprints.jsp)
	 *         called and Model passed.
	 */
	@RequestMapping(value = "/jiraDashboard/project/{name}", method = RequestMethod.GET)
	public ModelAndView showSprintDetails(@PathVariable String name) {

		System.out.println(" Selected Project  " + name);

		/*
		 * An object of the Jira Utils class which contains all data processing
		 * and fetching methods
		 */
		JiraUtilsNew utils = new JiraUtilsNew();

		/*
		 * The getRapidViewInfo() method will get all the projects information
		 * for the Sprints tab in the dashboard
		 */
		ArrayList<ProjectDetails> rapidView = utils.getRapidViewInfo();
		SprintForm form = new SprintForm();
		form.setRapid(name);
		form.setRapidView(rapidView);

		/*
		 * Getting the customer names from the properties file for populating in
		 * the dashboard page
		 */
		String[] customer_names = ConfigurationManager.getProperty(
				"customer_names").split(",");
		ArrayList customerNames = new ArrayList(customer_names.length);
		for (int i = 0; i < customer_names.length; i++) {
			customerNames.add(customer_names[i]);
		}

		/*
		 * Getting the customer severity types from the properties file for
		 * populating in the dashboard page
		 */
		String[] customer_severity = ConfigurationManager.getProperty(
				"customer_severity").split(",");
		ArrayList severity = new ArrayList(customer_severity.length);
		for (int i = 0; i < customer_severity.length; i++) {
			severity.add(customer_severity[i]);
		}

		/*
		 * Getting the component names from the properties file for populating
		 * in the dashboard page
		 */
		String[] component_names = ConfigurationManager.getProperty(
				"component_names").split(",");
		ArrayList componentNames = new ArrayList(component_names.length);
		for (int i = 0; i < component_names.length; i++) {
			componentNames.add(component_names[i]);
		}

		form.setCustomerNames(customerNames);
		form.setSeverity(severity);
		form.setComponentNames(componentNames);

		/*
		 * The getRapidViewDetails() method will get all the sprints information
		 * for a selected project in the dashboardof the Sprints tab
		 */
		SprintInfoForm sprintForm = utils.getRapidViewDetails(Integer
				.parseInt(name));
		form.setSprintNames(sprintForm.getSprintNames());
		form.setSprintIds(sprintForm.getSprintIds());
		form.setLength(Integer.toString(sprintForm.getSprintNames().size()));

		utils.getJiraProjects(form);

		return new ModelAndView("selectSprints", "sprintForm", form);
	}

	/**
	 * Unused method
	 * 
	 * @param rapidViewId
	 * @param sprintId
	 * @return
	 */
	@RequestMapping(value = "/jiraDashboard/sprintReport/{rapidViewId}/{sprintId}", method = RequestMethod.GET)
	public ModelAndView getSprintIssues(@PathVariable String rapidViewId,
			@PathVariable String sprintId) {

		JiraUtilsNew jiraUtils = new JiraUtilsNew();

		SprintIssueForm form = jiraUtils.jqlGeneral("sprint=" + sprintId
				+ " &maxResults=500");

		return new ModelAndView("selectSprints", "SprintIssueForm", form);
	}

	/**
	 * This method is used to process the Automation Trend request issued by the
	 * 'Automation Trend' button on the dashboard homepage
	 * 
	 * @param projectId
	 *            Project ID value as String
	 * @param sprintId
	 *            Sprint ID value as String
	 * @param model
	 *            Model object to pass data to the View
	 * @param projectName
	 *            String value of project name
	 * @param from_Date
	 *            Start date if the sprint mode is not selected else 'none'
	 * @param to_Date
	 *            End date if the sprint mode is not selected else 'none'
	 * @return A ModelAndView object containing the View
	 *         (displayAutomationTestsTrend.jsp) called and Model passed.
	 */
	@RequestMapping(value = "/automationTrend/{projectId}/{sprintId}/{projectName}/{from_Date}/{to_Date}/{all}", method = RequestMethod.GET)
	public ModelAndView displayAutomationTrend(
			@PathVariable("projectId") String projectId,
			@PathVariable("sprintId") String sprintId, Model model,
			@PathVariable("projectName") String projectName,
			@PathVariable("from_Date") String from_Date,
			@PathVariable("to_Date") String to_Date,
			@PathVariable("all") String all) {

		/*
		 * This form is used to store all the information in regard to
		 * Automation Trends
		 */
		TestLinkForm automationForm = new TestLinkForm();
		String fromDate, toDate;

		/* This class contains all the Automation related methods */
		AutomationUtils testUtils = new AutomationUtils();

		/*
		 * An object of the Jira Utils class which contains all data processing
		 * and fetching methods
		 */
		JiraUtilsNew jiraUtils = new JiraUtilsNew();
		JiraTrendsForm form;
		AutomationDateForm dateForm;

		/*
		 * The dates will be null if the sprint mode was selected in the
		 * dashboard page
		 */
		if ((!(from_Date.equals("null"))) && (!(to_Date.equals("null")))) {

			/*
			 * Sprint mode not selected getFormattedDate() method will format
			 * the dates as required for fetching Automation data
			 */
			dateForm = testUtils.getFormattedDate(from_Date, to_Date, false);
		} else {

			/*
			 * Sprint mode selected getDateRange() method will fetch the start
			 * and end dates for the selected sprint getFormattedDate() method
			 * will format the dates as required for fetching Automation data
			 */
			form = jiraUtils.getDateRange(Integer.parseInt(projectId),
					Integer.parseInt(sprintId));
			dateForm = testUtils.getFormattedDate(form.getStart_date(),
					form.getEnd_date(), true);
		}

		fromDate = dateForm.getStart_date();
		toDate = dateForm.getEnd_date();

		System.out.println("fromDate = " + fromDate);
		System.out.println("toDate = " + toDate);

		TreeMap trendMap = testUtils.getAutomationTrend(fromDate, toDate);

		System.out.println("Date: " + fromDate + " " + toDate);
		System.out.println("trendMap -->" + trendMap);

		String trendData = "";
		/*
		 * trendMap contains all the automation specific data which has to be
		 * formatted for display on the View page
		 */
		if (trendMap.size() > 0) {

			trendData = "['Date','%BVT','%Regression','%Full Regression']";
			Set<String> keySet = trendMap.keySet();
			Iterator<String> keySetIterator = keySet.iterator();
			while (keySetIterator.hasNext()) {
				String key = keySetIterator.next();
				trendData += ",['"
						+ key
						+ "',"
						+ ((AutomationTrendForm) trendMap.get(key))
								.getBvtCount()
						+ ","
						+ ((AutomationTrendForm) trendMap.get(key))
								.getRegressionCount()
						+ ","
						+ ((AutomationTrendForm) trendMap.get(key))
								.getFullRegressionCount() + "]";

			}

			System.out.println("trendData -- >" + trendData);

			automationForm.setTrendData(trendData);

		}

		/* Passing necessary data to the View */
		model.addAttribute("fromDate", fromDate);
		model.addAttribute("toDate", toDate);
		model.addAttribute("data", trendData);
		model.addAttribute("projectName", projectName);

		global_AutomationForm = automationForm;

		return new ModelAndView("displayAutomationTestsTrend",
				"automationForm", automationForm);
	}

	/**
	 * This is the handler for comparing the Automation bug trends for the
	 * selected sprints in the 'multi-sprint' tab of the dashboard.
	 * 
	 * @param projectId
	 *            Project ID value as String
	 * @param sprintIds
	 *            List of Sprint ID's to be compared (comma separated), else
	 *            null
	 * @param model
	 *            Model object to pass data to the View
	 * @return A ModelAndView object containing the View
	 *         (displayAutomationTrendComparison.jsp) called and Model passed.
	 */
	@RequestMapping(value = "/automationTrendComparison/{projectId}/{sprintId}/{both}", method = RequestMethod.GET)
	public ModelAndView displayAutomationComparisonTrend(
			@PathVariable("projectId") String projectId,
			@PathVariable("sprintId") String sprintIds, Model model,
			@PathVariable("both") String both) {

		/*
		 * This object contains the start and end dates for the aforementioned
		 * sprint periods
		 */
		AutomationDateForm dateForm;

		/*
		 * This class contains all the Automation related methods to fetch data
		 * from the database and parse as needed
		 */
		AutomationUtils testUtils = new AutomationUtils();
		String fromDate, toDate;

		/*
		 * An object of the Jira Utils class which contains all data processing
		 * and fetching methods
		 */
		JiraUtilsNew jiraUtils = new JiraUtilsNew();
		JiraTrendsForm form;

		/* Structure to hold the automation data */
		TreeMap trendMap;

		String[] sprints = sprintIds.split(",");
		trendData = "[['Date','%BVT','%Regression','%Full Regression']";
		ArrayList<String> trends = new ArrayList<String>(sprints.length);
		ArrayList<String> start_dates = new ArrayList<String>(sprints.length);
		ArrayList<String> end_dates = new ArrayList<String>(sprints.length);
		ArrayList<Integer> bvt_count = new ArrayList<Integer>(sprints.length);
		ArrayList<Integer> regression_count = new ArrayList<Integer>(
				sprints.length);
		ArrayList<Integer> fullRegression_count = new ArrayList<Integer>(
				sprints.length);
		int bvt = 0, regression = 0, fullRegression = 0;
		int count = 0;

		/* Iterate for each sprint */
		for (int i = 0; i < sprints.length; i++) {

			/* Get the date range for the sprint */
			form = jiraUtils.getDateRange(Integer.parseInt(projectId),
					Integer.parseInt(sprints[i]));

			/* format the dates as required by the automation class method */
			dateForm = testUtils.getFormattedDate(form.getStart_date(),
					form.getEnd_date(), true);

			fromDate = dateForm.getStart_date();
			toDate = dateForm.getEnd_date();

			start_dates.add(i, fromDate);
			end_dates.add(i, toDate);

			/*
			 * The main Automation method that fetches all the data from the db
			 * and stores it in the TreeMap
			 */
			trendMap = testUtils.getAutomationTrend(fromDate, toDate);

			/* Parse the treeMap and arrange the data as required by the view */
			if (trendMap.size() > 0) {

				Set<String> keySet = trendMap.keySet();
				Iterator<String> keySetIterator = keySet.iterator();
				while (keySetIterator.hasNext()) {
					count++;
					String key = keySetIterator.next();

					/*
					 * Take the sum of all the respective test results for each
					 * day of each sprint and later take the average of the test
					 * results for each sprint
					 */
					bvt += ((AutomationTrendForm) trendMap.get(key))
							.getBvtCount();
					regression += ((AutomationTrendForm) trendMap.get(key))
							.getRegressionCount();
					fullRegression += ((AutomationTrendForm) trendMap.get(key))
							.getFullRegressionCount();
				}

				System.out.println("count = " + count);

				/*
				 * Compute average of each test for the given period of each
				 * sprint i.e. count is at most = 14 days
				 */
				bvt_count.add(i, bvt / count);
				regression_count.add(i, regression / count);
				fullRegression_count.add(i, fullRegression / count);

				trends.add(i, bvt / count + "," + regression / count + ","
						+ fullRegression / count);
				System.out.println("\n\nTrends= " + trends.get(i));

			}

			/*
			 * If the data for that sprint does not exist, initialize all the
			 * test result values to zero
			 */
			else if (trendMap.size() == 0) {
				bvt_count.add(i, 0);
				regression_count.add(i, 0);
				fullRegression_count.add(i, 0);

				trends.add(i, 0 + "," + 0 + "," + 0);
				System.out.println("\n\nTrends= " + trends.get(i));
			}
		}

		/* Arrange the data in reverse chronological order for display purpose */
		for (int i = sprints.length - 1; i >= 0; i--) {
			if (i == sprints.length - 1)
				trendData += ",['" + start_dates.get(i) + "'," + trends.get(i);
			else
				trendData += "],['" + start_dates.get(i) + "'," + trends.get(i);
			System.out.println("trendData = " + trendData);
		}

		trendData += "]]";

		/* Set the data to be passed to the view */
		System.out.println("trendData = " + trendData);
		model.addAttribute("fromDate", start_dates.get(sprints.length - 1));
		model.addAttribute("toDate", end_dates.get(0));

		/*
		 * If 'Both' button is clicked on the Dashboard, this method gets called
		 * from the SprintComparison method and the computed data gets stored in
		 * the trendData static variable which is accessed from the
		 * SprintComparison method
		 */
		if (both.equals("true")) {
			return null;
		}

		return new ModelAndView("displayAutomationTrendComparison",
				"graphData", trendData);
	}

	/**
	 * This method is the handler for
	 * 
	 * @param projectId
	 *            Project ID of the selected project as a String
	 * @param sprintId
	 *            Sprint ID of the selected project as a String
	 * @param project_Name
	 *            Project Name of the selected project
	 * @param model
	 *            Model object to pass data to the View
	 * @param from_Date
	 *            Start date if the sprint mode is not selected else 'none'
	 * @param to_Date
	 *            End date if the sprint mode is not selected else 'none'
	 * @return A ModelAndView object containing the View
	 *         (displayCommitDetails.jsp) called and Model passed.
	 */
	@RequestMapping(value = "/commits/{projectId}/{sprintId}/{projectName}/{fromDate}/{toDate}", method = RequestMethod.GET)
	public ModelAndView displayCommits(
			@PathVariable("projectId") String projectId,
			@PathVariable("sprintId") String sprintId,
			@PathVariable("projectName") String project_Name, Model model,
			@PathVariable("fromDate") String from_Date,
			@PathVariable("toDate") String to_Date) {

		/* This is the main class for Stash related methods */
		ProjectUtils projUtils = new ProjectUtils();

		/*
		 * An object of the Jira Utils class which contains all data processing
		 * and fetching methods
		 */
		JiraUtilsNew jiraUtils = new JiraUtilsNew();
		JiraTrendsForm form;

		String fromDate, toDate;

		/*
		 * Check if sprint mode was selected, If so, the dates will be null Else
		 * compute the dates for the selected sprint
		 */
		if ((!(from_Date.equals("null"))) && (!(to_Date.equals("null")))) {
			fromDate = from_Date.replace("-", "/");
			toDate = to_Date.replace("-", "/");
			form = new JiraTrendsForm();
			jiraUtils.formatDates_stash(fromDate, toDate, form);
		}

		/* Sprint mode selected */
		else {

			/* Get the start and end date for the selected sprint */
			form = jiraUtils.getDateRange(Integer.parseInt(projectId),
					Integer.parseInt(sprintId));

			/* Format the dates for stash related queries */
			jiraUtils.formatDates_stash(form.getStart_date(),
					form.getEnd_date(), form);
		}

		fromDate = form.getStart_date();
		toDate = form.getEnd_date();

		/*
		 * Hardcoded project name and branch name which will be passed as
		 * parameters in the stash URL
		 */
		String projectName = "SCAL";
		String branch = "master";

		ArrayList repoList;

		/* Get the names of all the repos related to the project */
		ArrayList repoJSONList = projUtils.getRepos(projectName);

		/* Get the details of all the repos */
		repoList = projUtils.getRepoDetails(repoJSONList);

		ArrayList userList = new ArrayList();

		SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyyy");
		Date date = new Date();
		if (fromDate == null || fromDate.isEmpty()) {
			fromDate = sdf.format(date);
		}
		if (toDate == null || toDate.isEmpty()) {
			toDate = sdf.format(date);
		}

		System.out.println("-------Project Name: " + projectName
				+ " repoList: " + repoList + " userList: " + userList
				+ " fromDate: " + fromDate + " toDate: " + toDate);

		/* Get the commit details of all the repos specified in repoList */
		HashMap commitMap = projUtils.getCommits(projectName, repoList,
				userList, branch, fromDate, toDate);
		System.out.println("commitMap -->" + commitMap);
		ProjectForm projects = new ProjectForm();
		projects.setCommitMap(commitMap);
		HashMap finaldata;
		HashMap tableData = new HashMap();
		ArrayList totalData = new ArrayList();

		ArrayList graphList = new ArrayList();
		if (StashUtils.isGraphDisabled().equals("true")) {

			EvaultDBUtils dbUtils = new EvaultDBUtils();
			dbUtils.cleanExistingRecords();

			dbUtils.saveCommits(commitMap);
			ArrayList datesbetweenDate = StashUtils.getDatesBetweenDate(
					fromDate, toDate);
			dbUtils.saveCommit_report(datesbetweenDate,
					dbUtils.retriveCommits());
			finaldata = dbUtils.prepareReportdata(datesbetweenDate);
			graphList = (ArrayList) finaldata.get("trendData");
			tableData = (HashMap) finaldata.get("tableData");

			totalData = (ArrayList) tableData.get("Total");

		}
		CommitDetails commitDetailsObj = new CommitDetails();
		commitDetailsObj.setCommitMap(commitMap);
		commitDetailsObj.setGraphData(graphList);
		commitDetailsObj.setTableData(tableData);

		model.addAttribute("projectName", project_Name);
		model.addAttribute("fromDate", fromDate);
		model.addAttribute("toDate", toDate);

		return new ModelAndView("displayCommitDetails", "commitDetailsObj",
				commitDetailsObj);
	}

	/**
	 * This method is the handler for the 'customer' tab on the dashboard.
	 * 
	 * @param projectId
	 *            The selected project ID passed as a String
	 * @param sprintId
	 *            The selected sprint ID passed as a String
	 * @param customerName
	 *            Names of all the selected customers. If all are selected,
	 *            customerName = - All -, Else all customer names will be comma
	 *            separated
	 * @param severity
	 *            Severity of the customer related Issues.
	 * @param fromDate
	 *            Start date in MM/dd/yyyy format
	 * @param toDate
	 *            End date in MM/dd/yyyy format
	 * @param summary
	 *            This value will be equal to "summary" if the 'Customer Issue
	 *            Summary' button is clicked
	 * @param projectName
	 *            Name of the project as in the 'Projects' dropdown.
	 * @param model
	 *            Model object to pass data to the View
	 * @return Depending on the summary value, if "summary", A ModelAndView
	 *         object containing the View (issueDetails.jsp) called Else,
	 *         (displayTrends.jsp) called.
	 */
	@RequestMapping(value = "/customer", method = RequestMethod.POST)
	public ModelAndView displayCustomerBugStatus(
			@RequestParam(value = "project", required = false) String projectId,
			@RequestParam(value = "sprint", required = true) String sprintId,
			@RequestParam(value = "customer", required = false) String customerName,
			@RequestParam(value = "severity", required = true) String severity,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = true) String toDate,
			@RequestParam(value = "summary", required = false) String summary,
			@RequestParam(value = "projectName", required = false) String projectName,
			Model model) {

		System.out.println("customerName = " + customerName + " Severity = "
				+ severity + " Project = " + projectId + " Sprint = "
				+ sprintId + " From Date= " + fromDate + " To Date= " + toDate
				+ "Summary = " + summary + " ProjectName = " + projectName);

		JiraUtilsNew jiraUtils = new JiraUtilsNew();

		String customers = "";
		String jql_query = null;
		boolean all_customers;

		/*
		 * If all customers option is selected, then the JQL query will contain
		 * Customer != Empty indicating all customers
		 */
		if (customerName.equals("- All -")) {
			customers = "Customer != EMPTY";
			all_customers = true;
		} else {
			/* If not, the customer names will be separated and queried */
			String[] customer_names = customerName.split(",");
			System.out.println("customer_names = " + customer_names[0]);
			for (int i = 0; i < customer_names.length; i++) {
				customers += "Customer = " + "'" + customer_names[i] + "'"
						+ " OR ";
			}
			all_customers = false;
		}

		/* formatting the dates to make it JIRA JQL compatible */
		JiraTrendsForm form2 = jiraUtils.getDateRange_jql(fromDate, toDate,
				"customer");

		int total_created = 0, total_updated = 0;

		/* Remove spaces and other characters like 'OR' at the end of the line */
		if (!all_customers)
			customers = customers.substring(0, customers.length() - 4);

		System.out.println("customers = " + customers);
		customers = " AND (" + customers + ")";

		/*
		 * If the user has selected the severity option other than '- All -',
		 * this will separate the severity with AND's
		 */
		if (!severity.equals("none")) {
			customers += " AND severity = '" + severity + "'";
		} else {
			customers += "";
		}

		System.out.println("summary = " + summary);

		/*
		 * jqlGeneral method is parses all the issues related information and
		 * stores the data in a SprintIssueForm
		 */
		SprintIssueForm form = jiraUtils.jqlGeneral(customers.substring(5,
				customers.length())
				+ " and project='"
				+ projectName
				+ "' and ((created >= '"
				+ form2.getStart_date()
				+ "' and created < '"
				+ form2.getEnd_date()
				+ "') or (resolutiondate >='"
				+ form2.getStart_date()
				+ "' and resolutiondate <'" + form2.getEnd_date() + "'))");

		/* If the "Customer Issue Summary" button was selected */
		if (summary.equals("true")) {

			System.out.println(customers.substring(5, customers.length())
					+ " and project='" + projectName + "' and ((created >= '"
					+ form2.getStart_date() + "' and created < '"
					+ form2.getEnd_date() + "') or (resolutiondate >='"
					+ form2.getStart_date() + "' and resolutiondate <'"
					+ form2.getEnd_date() + "'))");

			model.addAttribute("fromDate", fromDate);
			model.addAttribute("toDate", toDate);

			/* Format the graphs as required by Google charts */

			String bug_graph = "[['Bugs','Count'],['Open',"
					+ form.getBugs_open() + "]" + ",['Fixed',"
					+ form.getBugs_closed() + "]" + ",['Resolved',"
					+ form.getBugs_resolved() + "]" + ",['In Progress',"
					+ form.getBugs_inprogress() + "]]";

			System.out.println("bug_graph = " + bug_graph);

			String tasks_graph = "[['Bugs','Count'],['Open',"
					+ form.getTasks_open() + "]" + ",['Fixed',"
					+ form.getTasks_closed() + "]" + ",['Resolved',"
					+ form.getTasks_resolved() + "]" + ",['In Progress',"
					+ form.getTasks_inprogress() + "]]";

			System.out.println("tasks_graph = " + tasks_graph);

			String story_graph = "[['Bugs','Count'],['Open',"
					+ form.getStory_open() + "]" + ",['Fixed',"
					+ form.getStory_closed() + "]" + ",['Resolved',"
					+ form.getStory_resolved() + "]" + ",['In Progress',"
					+ form.getStory_inprogress() + "]]";

			System.out.println("story_graph = " + story_graph);

			model.addAttribute("bugGraph", bug_graph);
			model.addAttribute("taskGraph", tasks_graph);
			model.addAttribute("storyGraph", story_graph);
			model.addAttribute("customerName", customerName);
			model.addAttribute("customer", "customer");

			return new ModelAndView("issueDetails", "SprintIssueForm", form);
		} else {

			/*
			 * If the 'Bug trends' button was selected, all the dates between
			 * the selected start and end date are computed
			 */
			ArrayList dateRange = jiraUtils.getDatesBetweenDate(
					form2.getStart_date(), form2.getEnd_date(), true, true);
			String graphData = "";

			/*
			 * This Hashmap stores a relation between the date and the bug trend
			 * associated with that date
			 */
			Map<String, SprintTrendForm> map;
			SprintTrendForm trendForm = new SprintTrendForm();
			trendForm.setCreated_count(total_created);
			trendForm.setUpdated_count(total_updated);

			Integer created_count, updated_count;
			String current_date;

			System.out.println("jql_query = " + jql_query);

			/*
			 * The bug Trends method maps the bug trends for each date and
			 * returns a hashmap
			 */
			map = jiraUtils.bugTrends(sprintId, (String) dateRange.get(0),
					(String) dateRange.get(dateRange.size() - 1), dateRange,
					true, false, false, projectName, customers, "bug");
			System.out
					.println("<----------------- Updated ------------------>");

			Iterator it = map.entrySet().iterator();

			/* For debugging */
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				SprintTrendForm form_new = (SprintTrendForm) pair.getValue();

				System.out.println(pair.getKey() + " = "
						+ form_new.getCreated_count() + ","
						+ form_new.getUpdated_count());
			}

			/* Format the data as required by Google charts */
			for (int i = 0; i < dateRange.size() - 1; i++) {

				created_count = map.get(dateRange.get(i)).getCreated_count();
				updated_count = map.get(dateRange.get(i)).getUpdated_count();
				// open_count =
				// Integer.parseInt(remaining_open_.get(i).toString());
				current_date = map.get(dateRange.get(i)).getDate();

				graphData += ",['" + current_date + "'," + created_count + ","
						+ updated_count + "]";
				total_created += map.get(dateRange.get(i)).getCreated_count();
				total_updated += map.get(dateRange.get(i)).getUpdated_count();
			}

			graphData = "[['Date','Outstanding','Outgoing']" + graphData + "]";

			/* Pass all the required information to the View */
			model.addAttribute("fromDate", form2.getStart_date());
			model.addAttribute("toDate", form2.getEnd_date());
			model.addAttribute("customerName", customerName);
			model.addAttribute("customer", "customer");
			model.addAttribute("graphData", graphData);

			return new ModelAndView("displayTrends", "SprintIssueForm", form);
		}
	}

	/**
	 * This method is the handler for the component tab on the dashboard
	 * 
	 * @param fromDate
	 *            Start date in dd-MMM-yy format
	 * @param toDate
	 *            End date in dd-MMM-yy format
	 * @param summary
	 *            This value will be equal to "summary" if the 'Component Issue
	 *            Summary' button is clicked
	 * @param componentName
	 *            This list of all selected components (comma separated) or '-
	 *            All -' if all selected
	 * @param model
	 *            An object to pass the data to the view
	 * @return Depending on the summary value, if "summary", A ModelAndView
	 *         object containing the View (issueDetails.jsp) called Else,
	 *         (displayTrends.jsp) called.
	 */
	@RequestMapping(value = "/componentReport/{fromDate}/{toDate}/{summary}/{componentName}")
	public ModelAndView componentReport(
			@PathVariable("fromDate") String fromDate,
			@PathVariable("toDate") String toDate,
			@PathVariable("summary") String summary,
			@PathVariable("componentName") String componentName, Model model) {

		/*
		 * An object of the JiraUtilsNew class containing all JIRA related
		 * methods
		 */
		JiraUtilsNew jiraUtils = new JiraUtilsNew();

		String components = "";
		String jql_query = null;
		boolean all_components = true;

		/*
		 * If the '-All-' option is selected for the components, then the JQL
		 * query will contain Component != EMPTY
		 */
		if (componentName.equals("- All -")) {
			components = "Component != EMPTY";
			all_components = true;
		} else {
			/*
			 * Else each component passed will be stored in an Array and
			 * combined in the query with OR's
			 */
			String[] component_names = componentName.split(",");
			for (int i = 0; i < component_names.length; i++) {
				System.out.println("Component = " + component_names[i]);
				components += "Component = " + "'" + component_names[i] + "'"
						+ " OR ";
			}
			all_components = false;

			System.out.println("components = " + components);
		}

		/* Replacing the '-' in the date with a '/' */
		fromDate = fromDate.split("-")[0] + "/" + fromDate.split("-")[1] + "/"
				+ fromDate.split("-")[2];
		toDate = toDate.split("-")[0] + "/" + toDate.split("-")[1] + "/"
				+ toDate.split("-")[2];

		System.out.println("fromDate = " + fromDate);
		System.out.println("toDate = " + toDate);

		/*
		 * Once the dates are formatted, Formatting the date as required for
		 * JIRA JQL
		 */
		JiraTrendsForm form2 = jiraUtils.getDateRange_jql(fromDate, toDate,
				"component");

		int total_created = 0, total_updated = 0, remaining_open;

		/*
		 * Remove all the extra characters at the end of the string like spaces
		 * and 'OR'
		 */
		if (!all_components)
			components = components.substring(0, components.length() - 4);

		System.out.println("components = " + components);
		components = " AND (" + components + ")";

		/* If the user selects the Issue Summary option */
		if (summary.equals("true")) {

			System.out.println(components.substring(5, components.length())
					+ " and ((created >= '" + form2.getStart_date()
					+ "' and created < '" + form2.getEnd_date()
					+ "') or (resolutiondate >='" + form2.getStart_date()
					+ "' and resolutiondate <'" + form2.getEnd_date()
					+ "')) and issueType=Bug &maxResults=200");

			/*
			 * jqlGeneral method is parses all the issues related information
			 * and stores the data in a SprintIssueForm
			 */
			SprintIssueForm form = jiraUtils.jqlGeneral(components.substring(5,
					components.length())
					+ " and ((created >= '"
					+ form2.getStart_date()
					+ "' and created < '"
					+ form2.getEnd_date()
					+ "') or (resolutiondate >='"
					+ form2.getStart_date()
					+ "' and resolutiondate <'"
					+ form2.getEnd_date()
					+ "')) and issueType=Bug &maxResults=200");

			/*
			 * Hashmap stores the bug count of the each component against its
			 * name
			 */
			HashMap<String, Integer> map = new HashMap<String, Integer>(15);
			ArrayList<String> component_Names = form.getComponents();
			ArrayList<String> component_keys = new ArrayList<String>(map
					.keySet().size());

			/* Initializing the map with 0 against all the component names */
			for (int i = 0; i < component_Names.size(); i++) {
				map.put(component_Names.get(i), 0);
			}

			/* Initializing the map with 1 against all the component names */
			for (int i = 0; i < component_Names.size(); i++) {
				int component_count = map.get(component_Names.get(i));
				component_count += 1;
				map.put(component_Names.get(i), component_count);
			}

			String issue_graph = "";

			/* Formatting the data as required by the Google charts pie chart */
			for (String key : map.keySet()) {
				System.out.println(key + " : " + map.get(key));
				issue_graph += "['" + key + "'," + map.get(key) + "],";
				component_keys.add(key);
			}

			/* Eliminating the last character space */
			issue_graph = "[['Bugs','Count'],"
					+ issue_graph.substring(0, issue_graph.length() - 1) + "]";
			String individual_component_graph = jiraUtils.individualComponents(
					form, component_keys);

			model.addAttribute("fromDate", fromDate);
			model.addAttribute("toDate", toDate);

			model.addAttribute("componentName", componentName);
			model.addAttribute("component", "component");

			model.addAttribute("issueGraph", issue_graph);
			model.addAttribute("componentGraph", individual_component_graph);

			return new ModelAndView("issueDetails", "SprintIssueForm", form);
		} else {

			/*
			 * If the 'bug trend' option is selected, the dates between the from
			 * and to date are computed using the getDatesBetweenDate() method
			 */
			ArrayList dateRange = jiraUtils.getDatesBetweenDate(
					form2.getStart_date(), form2.getEnd_date(), true, true);
			String start_date = null, end_date = null;
			ArrayList<SprintTrendForm> sprintForm = new ArrayList<SprintTrendForm>(
					dateRange.size());
			String graphData = "";
			Map<String, SprintTrendForm> map;
			SprintTrendForm form = new SprintTrendForm();
			form.setCreated_count(total_created);
			form.setUpdated_count(total_updated);

			Integer created_count, updated_count;
			String current_date;

			System.out.println("jql_query = " + jql_query);

			/*
			 * The bug Trends method maps the bug trends for each date and
			 * returns a hashmap
			 */
			map = jiraUtils.bugTrends(null, (String) dateRange.get(0),
					(String) dateRange.get(dateRange.size() - 1), dateRange,
					false, false, true, null, components, "bug");
			System.out
					.println("<----------------- Updated ------------------>");

			Iterator it = map.entrySet().iterator();

			/*
			 * Debugging purpose - Simply printing the date against the count
			 * for each day
			 */
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				SprintTrendForm form_new = (SprintTrendForm) pair.getValue();

				System.out.println(pair.getKey() + " = "
						+ form_new.getCreated_count() + ","
						+ form_new.getUpdated_count());
			}

			ArrayList remaining_open_ = new ArrayList();
			Integer open_count;

			/* Generate the bug trends for each date */
			for (int i = 0, j = 1; i < dateRange.size() - 1
					&& j < dateRange.size(); i++, j++) {
				start_date = dateRange.get(i).toString();
				end_date = dateRange.get(j).toString();
				sprintForm.add(i, jiraUtils.jqlQuery(null, start_date,
						end_date, dateRange.get(i), false, true, components,
						null));
				remaining_open = sprintForm.get(i).getRemaining_open();
				remaining_open_.add(remaining_open);
			}

			/* Format the bug trend as required by the Google charts */
			for (int i = 0; i < dateRange.size() - 1; i++) {

				created_count = map.get(dateRange.get(i)).getCreated_count();
				updated_count = map.get(dateRange.get(i)).getUpdated_count();
				open_count = Integer
						.parseInt(remaining_open_.get(i).toString());
				current_date = map.get(dateRange.get(i)).getDate();

				graphData += ",['" + current_date + "'," + created_count + ","
						+ updated_count + "," + open_count + "]";
				total_created += map.get(dateRange.get(i)).getCreated_count();
				total_updated += map.get(dateRange.get(i)).getUpdated_count();
			}

			graphData = "[['Date','Opened','Fixed','Remaining Open']"
					+ graphData + "]";

			/* Pass the required information to the View */
			model.addAttribute("fromDate", form2.getStart_date());
			model.addAttribute("toDate", form2.getEnd_date());
			model.addAttribute("customerName", componentName);
			model.addAttribute("customer", "Component");

			return new ModelAndView("displayTrends", "graphData", graphData);

		}
	}

	/**
	 * This is the handler for
	 * 
	 * @param model
	 * @param projectId
	 * @param sprintId
	 * @param projectName
	 * @param all
	 * @return
	 */
	@RequestMapping(value = "/sprintReport/{project}/{sprint}/{projectName}/{all}", method = RequestMethod.GET)
	public ModelAndView showSprintReport(Model model,
			@PathVariable("project") String projectId,
			@PathVariable("sprint") String sprintId,
			@PathVariable("projectName") String projectName,
			@PathVariable("all") String all) {

		showIssueReport(model, projectId, sprintId, projectName, "all");
		showSprintTrends(projectId, sprintId, model, projectName, "null",
				"null", "all");
		displayAutomationTrend(projectId, sprintId, model, projectName, "null",
				"null", "all");

		model.addAttribute("automationForm", global_AutomationForm);
		model.addAttribute("sprint_all", "sprint_all");
		model.addAttribute("graphData", global_BugTrend);

		return new ModelAndView("issueDetails", "SprintIssueForm",
				global_IssueForm);
	}

}