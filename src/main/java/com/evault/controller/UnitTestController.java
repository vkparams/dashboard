package com.evault.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.evault.form.CommitDetails;
import com.evault.form.ProjectForm;
import com.evault.form.UnitTestForm;
import com.evault.utils.EvaultDBUtils;
import com.evault.utils.ProjectUtils;
import com.evault.utils.StashUtils;
import com.evault.utils.UnitTestUtils;

@Controller
public class UnitTestController {

	
	@RequestMapping(value = "/unitTest", method = RequestMethod.GET)
	public ModelAndView displayUnitTests() {
		return new ModelAndView("unitTest");
	}
	
	
	@RequestMapping(value = "/unitTestTrend", method = RequestMethod.GET)
	public ModelAndView selectComponents() {

		UnitTestUtils testUtils = new UnitTestUtils();
		ArrayList componentList = testUtils.getComponents();

		UnitTestForm unitTests = new UnitTestForm();
		unitTests.setComponents(componentList);

		System.out.println(" componentList  " + componentList);

		return new ModelAndView("selectUnitTests", "unitTestForm", unitTests);
	}

	
	
	@RequestMapping(value = "/unitTestreports", method = RequestMethod.POST)
	public ModelAndView displayUnitTestsTrend(
			@RequestParam(value = "component", required = false) String component,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "days", required = false) String days) {

		System.out.println(" component  " + component + " days  " + days);
		System.out.println(" fromDate  " + fromDate + " toDate  " + toDate);
		

		
		SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyyy");
		Date date = new Date();
		if (fromDate == null || fromDate.isEmpty()) {
			fromDate = sdf.format(date);
		}
		if (toDate == null || toDate.isEmpty()) {
			toDate = sdf.format(date);
		}
		
		
		UnitTestUtils unitTestUtils = new UnitTestUtils();


		ArrayList componentList;
		String[] componentArray;
		if (component != null && !component.equalsIgnoreCase("none")) {
			componentArray = component.split(",");
			componentList = new ArrayList(Arrays.asList(componentArray));
		} else {
			componentList =unitTestUtils.getComponents();
		}
		
		HashMap trendMap= unitTestUtils.getUnitTestTrend(componentList, fromDate.replace("/", "-"), toDate.replace("/", "-"));
		
		System.out.println("trendMap -->" + trendMap);
		UnitTestForm unitTests = new UnitTestForm();
		unitTests.setComponentMap(trendMap);
		
		

		return new ModelAndView("displayUnitTestsTrend", "unitTests",
				unitTests);
	}	
	
	
}
