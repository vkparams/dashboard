package com.evault.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.evault.form.AutomationTrendForm;
import com.evault.form.TestLinkForm;
import com.evault.utils.AutomationUtils;
import com.evault.utils.StashUtils;

@Controller
public class TestLinkController {

	@RequestMapping(value = "/automationTrend", method = RequestMethod.GET)
	public ModelAndView selectComponents() {

		AutomationUtils testUtils = new AutomationUtils();
		ArrayList componentList = testUtils.getPlans();

		TestLinkForm automationForm = new TestLinkForm();
		automationForm.setPlanList(componentList);

		System.out.println(" Test Plans  " + componentList);

		return new ModelAndView("selectTestPlans", "automationForm",
				automationForm);
	}

	 @RequestMapping(value = "/automationTestTrend", method =
	 RequestMethod.POST)
	 public ModelAndView displayUnitTestsTrend(
	 @RequestParam(value = "plan", required = false) String plan,
	 @RequestParam(value = "fromDate", required = false) String fromDate,
	 @RequestParam(value = "toDate", required = false) String toDate,Model
	 model) {
	
	 System.out.println(" plan  " + plan);
	 System.out.println(" fromDate  " + fromDate + " toDate  " + toDate);
	 TestLinkForm automationForm = new TestLinkForm();
	
	 StashUtils stashUtilsObj = new StashUtils();
	 ArrayList dateList = stashUtilsObj
	 .getDatesBetweenDate(fromDate, toDate);
	
	 AutomationUtils testUtils = new AutomationUtils();
	 TreeMap trendMap = testUtils.getTestTrend(plan, dateList);
	
	 System.out.println("trendMap -->" + trendMap);
	 String trendData = "";
	 if (trendMap.size() > 0) {
	
	 trendData = "['Date','Pass','Fail']";
	 Set<String> keySet = trendMap.keySet();
	 Iterator<String> keySetIterator = keySet.iterator();
	 while (keySetIterator.hasNext()) {
	 String key = keySetIterator.next();
	 trendData += ",['" + key + "'," + trendMap.get(key) + "]";
	 }
	
	 System.out.println("trendData -- >" + trendData);
	
	 automationForm.setTrendData(trendData);
	
	 }
	 model.addAttribute("plan", plan);
	 model.addAttribute("fromDate", fromDate);
	 model.addAttribute("toDate", toDate);
	
	
	 return new ModelAndView("displayAutomationTestsTrend",
	 "automationForm", automationForm);
	 }

	@RequestMapping(value = "/automationTestReports", method = RequestMethod.POST)
	public ModelAndView displayAutomationTrend(
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			Model model) {

		System.out.println(" fromDate  " + fromDate + " toDate  " + toDate);
		TestLinkForm automationForm = new TestLinkForm();

		StashUtils stashUtilsObj = new StashUtils();


		AutomationUtils testUtils = new AutomationUtils();
		TreeMap trendMap = testUtils.getAutomationTrend(fromDate,toDate);

		System.out.println("trendMap -->" + trendMap);
		String trendData = "";
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
								.getFullRegressionCount()
//						+ ","
//
//						+ ((AutomationTrendForm) trendMap.get(key))
//								.getBvtCount()
//						+ ","
//						+ ((AutomationTrendForm) trendMap.get(key))
//								.getRegressionCount()
//						+ ","
//						+ ((AutomationTrendForm) trendMap.get(key))
//								.getFullRegressionCount()
						+ "]";
				// trendData += ",['2004/05',2,3,4]";

			}

			System.out.println("trendData -- >" + trendData);

			automationForm.setTrendData(trendData);

		}
		model.addAttribute("fromDate", fromDate);
		model.addAttribute("toDate", toDate);

		return new ModelAndView("displayAutomationTestsTrend",
				"automationForm", automationForm);
	}
}
