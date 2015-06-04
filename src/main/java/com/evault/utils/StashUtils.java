package com.evault.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;

import com.evault.form.CommitDetails;

public class StashUtils {
    String stash_url = ConfigurationManager.getProperty("stash.url");

    public static void main(String args[]) {
        StashUtils util = new StashUtils();
        System.out.println(util.getColorCode());
//        String projectsResult = util.getProjects();
//        ArrayList projList = util.getProjectDetails(projectsResult);
//        ArrayList repoList = util.getRepos(projList);
//
//        util.getCommits(repoList);
        //util.displayCommitDetails(commitsResult);
    }



    public String getauthStringEnc() {
        String name = ConfigurationManager.getProperty("username");
        String password = ConfigurationManager.getProperty("password");

        String authString = name + ":" + password;
        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        String authStringEnc = new String(authEncBytes);
        return authStringEnc;
    }

    public static String getDate_Time(String seconds) {
        seconds = seconds.substring(0, seconds.length() - 3);
        Long tempSeconds = Long.parseLong(seconds);
        Date date = new Date(tempSeconds * 1000L); // *1000 is to convert
        // minutes to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss"); // the
        // format
        // of
        // your
        // date
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    public static String getDate(String seconds) {
        seconds = seconds.substring(0, seconds.length() - 3);
        Long tempSeconds = Long.parseLong(seconds);
        Date date = new Date(tempSeconds * 1000L); // *1000 is to convert
        // minutes to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy"); // the
        // format
        // of
        // your
        // date
        String formattedDate = sdf.format(date);
        return formattedDate;
    }


    
    public static ArrayList getDatesBetweenDate(String from_date, String to_date){

    	ArrayList dates = new ArrayList();

    	ArrayList datesList = new ArrayList();


		DateFormat formatter;

		formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date startDate=null;
		Date endDate=null;
		try {
			startDate = (Date) formatter.parse(from_date);

			endDate = (Date) formatter.parse(to_date);
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
			datesList.add(ds);

			System.out.println(" Date is ..." + ds);
		}
		
		return datesList;
	
    }
    
    
	public static HashMap createGraphData(ArrayList dates,
			HashMap user_commit_details) {
		Iterator iterator = user_commit_details.entrySet().iterator();
		HashMap userMap=new HashMap();
		String date="";

		try {
			for(int i=0;i<dates.size();i++) {
				date=(String)dates.get(i);
				System.out.println("The Date is: " + date);
				while (iterator.hasNext()) {
					ArrayList userCountByDateList=new ArrayList();
					Map.Entry mapEntry = (Map.Entry) iterator.next();
					System.out.println("The key is: " + mapEntry.getKey());
					
					ArrayList commitListObj=(ArrayList)user_commit_details.get(mapEntry.getKey());
					for(int j=0;j<commitListObj.size();j++){
						CommitDetails comitObj=(CommitDetails)commitListObj.get(j);
						if(comitObj.getDate().equals(date)){
							userCountByDateList.add(comitObj.getCount());
						}
						
					}
					userMap.put(mapEntry.getKey(), userCountByDateList);
					System.out.println("value is :" + userMap.get(mapEntry.getKey()));

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return userMap;
	}
    
    
    public static String trimName(String name) {
        String tmpName=name;
        if(name!=null && name.length()>100){
            tmpName=name.substring(0,74)+"...";
        }
        return tmpName;
    }
    
    public static String isGraphDisabled() {
        return ConfigurationManager.getProperty("isGraphEnabled");
    }
    
    
    public static String getColorCode() {
    	
    	char[] alphNum = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

    	Random rnd = new Random();

    	StringBuilder sb = new StringBuilder((100000 + rnd.nextInt(900000)) + "");
    	    //sb.append(alphNum[rnd.nextInt(alphNum.length)]);

    	String id = sb.toString();
    	
        return id;
    }    

}
