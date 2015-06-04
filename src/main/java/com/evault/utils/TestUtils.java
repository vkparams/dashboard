package com.evault.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestUtils {

	public static void main(String args[]) {
		List<Date> dates = new ArrayList<Date>();

		String str_date = "02/27/2014";
		String end_date = "03/05/2014";

		DateFormat formatter;

		formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date startDate=null;
		Date endDate=null;
		try {
			startDate = (Date) formatter.parse(str_date);

			endDate = (Date) formatter.parse(end_date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			System.out.println(" Date is ..." + ds);
		}
	}

}
