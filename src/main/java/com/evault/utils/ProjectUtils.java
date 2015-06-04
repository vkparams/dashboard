package com.evault.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import com.evault.form.CommitDetails;
import com.evault.form.ProjectDetails;
import com.evault.form.UserDetails;

/**
 * Created with IntelliJ IDEA. User: pkumarasamy Date: 1/17/14 Time: 11:04 AM To
 * change this template use File | Settings | File Templates.
 */
public class ProjectUtils {

	StashUtils util = new StashUtils();
	String stash_url = ConfigurationManager.getProperty("stash.url");

	public String getProjects() {
		String result = "";

		try {

			URL url = new URL(stash_url);
			URLConnection urlConnection = url.openConnection();
			urlConnection.setRequestProperty("Authorization",
					"Basic " + util.getauthStringEnc());
			InputStream is = urlConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);

			int numCharsRead;
			char[] charArray = new char[1024];
			StringBuffer sb = new StringBuffer();
			while ((numCharsRead = isr.read(charArray)) > 0) {
				sb.append(charArray, 0, numCharsRead);
			}
			result = sb.toString();

			System.out.println("***  Projects ***" + result);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList getProjectDetails(String jsonTxt) {
		ArrayList projectListKey = new ArrayList();

		JSONObject mainJson = (JSONObject) JSONSerializer.toJSON(jsonTxt);

		JSONArray valuesJsonarray = mainJson.getJSONArray("values");

		for (int i = 0; i < valuesJsonarray.size(); i++) {
			JSONObject element = valuesJsonarray.getJSONObject(i);
			projectListKey.add(element.get("key"));
		}

		return projectListKey;
	}

	public ArrayList getProjectDetailsNew() {
		ArrayList projectList = new ArrayList();

		JSONObject mainJson = (JSONObject) JSONSerializer.toJSON(getProjects());

		JSONArray valuesJsonarray = mainJson.getJSONArray("values");

		for (int i = 0; i < valuesJsonarray.size(); i++) {
			ProjectDetails projDetailsObj = new ProjectDetails();
			JSONObject element = valuesJsonarray.getJSONObject(i);
			projDetailsObj.setName(element.get("name").toString());
			projDetailsObj.setKey(element.get("key").toString());
			projectList.add(projDetailsObj);
		}

		return projectList;
	}

	public ArrayList getRepos(String projKey) {
		String result = "";
		ArrayList repoList = new ArrayList();

		try {
			URL url = new URL(stash_url + projKey + "/repos?limit=100");
			URLConnection urlConnection = url.openConnection();
			urlConnection.setRequestProperty("Authorization",
					"Basic " + util.getauthStringEnc());
			InputStream is = urlConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);

			int numCharsRead;
			char[] charArray = new char[1024];
			StringBuffer sb = new StringBuffer();
			while ((numCharsRead = isr.read(charArray)) > 0) {
				sb.append(charArray, 0, numCharsRead);
			}
			result = sb.toString();
			repoList.add(result);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("***  Repos ***" + repoList);

		return repoList;
	}

	public ArrayList getRepoDetails(ArrayList repoJSONList) {
		ArrayList repoNameList = new ArrayList();
		try {
			for (int i = 0; i < repoJSONList.size(); i++) {
				try {
					JSONObject mainJson = (JSONObject) JSONSerializer
							.toJSON(repoJSONList.get(i));
					JSONArray valuesJsonarray = mainJson.getJSONArray("values");
					for (int j = 0; j < valuesJsonarray.size(); j++) {
						JSONObject element = valuesJsonarray.getJSONObject(j);
						repoNameList.add(element.getString("name").toString());

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return repoNameList;
	}

	public HashMap getCommits(String projectName, ArrayList repoList,
			ArrayList user, String branch, String fromDate, String toDate) {
		String result = "";
		ArrayList commitList;
		HashMap commitMap = new HashMap();
		//if (commitsLimit == null || commitsLimit.isEmpty()) {
		String	commitsLimit = "150";
		//}
		try {
			String path = "";
			String repoName = "";
			String gitChangetListPath = "";

			for (int i = 0; i < repoList.size(); i++) {
				commitList = new ArrayList();
				try {
					repoName = (String) repoList.get(i);
					path = stash_url + projectName + "/repos/" + repoName
							+ "/commits?until="+branch+"&limit=" + commitsLimit + "&start=0";
					
//					path = stash_url + projectName + "/repos/" + repoName
//							+ "/commits?limit=" + commitsLimit + "&start=0";
					System.out.println("path *** = " + path);
					URL url = new URL(path);
					URLConnection urlConnection = url.openConnection();
					urlConnection.setRequestProperty("Authorization", "Basic "
							+ util.getauthStringEnc());
					InputStream is = urlConnection.getInputStream();
					InputStreamReader isr = new InputStreamReader(is);

					int numCharsRead;
					char[] charArray = new char[2024];
					StringBuffer sb = new StringBuffer();
					while ((numCharsRead = isr.read(charArray)) > 0) {
						sb.append(charArray, 0, numCharsRead);
					}
					result = sb.toString();
					System.out
							.println("----------------------------------------------------------------------------------------------------------------");
					System.out.println("*** project Name = " + projectName
							+ " Repo Name = " + repoName + "***");
					gitChangetListPath = stash_url.substring(0,
							stash_url.indexOf("rest"))
							+ "projects/"
							+ projectName
							+ "/repos/"
							+ repoName
							+ "/commits/";
					commitList.add(displayCommitDetails(gitChangetListPath,
							result, user, fromDate.replace("/", "-"),
							toDate.replace("/", "-")));
					commitMap.put(repoName, commitList);
					System.out.println("*** END ***");

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return commitMap;
	}

	public ArrayList displayCommitDetails(String gitChangetListPath,
			String jsonTxt, ArrayList user, String strFrom, String strTo) {

		ArrayList commitList = new ArrayList();
		if (!jsonTxt.isEmpty()) {
			JSONObject mainJson = (JSONObject) JSONSerializer.toJSON(jsonTxt);

			JSONArray valuesJsonarray = mainJson.getJSONArray("values");

			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
			SimpleDateFormat commit_sdf = new SimpleDateFormat("dd-MMM-yyyy");

			Date fromDate = null;
			Date toDate = null;
			try {
				fromDate = sdf.parse(strFrom);
				toDate = sdf.parse(strTo);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String strCommit = null;
			Date commit_date = null;

			for (int i = 0; i < valuesJsonarray.size(); i++) {
				JSONObject element = valuesJsonarray.getJSONObject(i);
				JSONObject authorJson = element.getJSONObject("author");

				String seconds = element.get("authorTimestamp").toString();

				seconds = seconds.substring(0, seconds.length() - 3);

				Date date = new Date(Integer.parseInt(seconds) * 1000);

				strCommit = StashUtils.getDate(element.get("authorTimestamp")
						.toString());
				try {
					commit_date = commit_sdf.parse(strCommit);

				} catch (Exception e) {
					e.printStackTrace();
				}

				if (fromDate.compareTo(commit_date) <= 0
						&& toDate.compareTo(commit_date) >= 0) {
					String commit_msg = (String) element.get("message");
					//if (!commit_msg.startsWith("Merge")) {

						if (user.size() > 0) {

							if (user.contains((String) authorJson.get("name"))) {
								CommitDetails commitObject = new CommitDetails();
								commitObject
										.setChangetListPath(gitChangetListPath
												+ (String) element.get("id"));
								commitObject.setDisplayId((String) element
										.get("displayId"));
								commitObject.setName((String) authorJson
										.get("name"));
								commitObject.setMessage((String) element
										.get("message"));

								commitObject.setDate(StashUtils
										.getDate_Time(element.get(
												"authorTimestamp").toString()));

								commitList.add(commitObject);

							}

						} else {

							CommitDetails commitObject = new CommitDetails();
							commitObject.setChangetListPath(gitChangetListPath
									+ (String) element.get("id"));
							commitObject.setDisplayId((String) element
									.get("displayId"));
							commitObject.setName((String) authorJson
									.get("name"));
							commitObject.setMessage((String) element
									.get("message"));

							commitObject
									.setDate(StashUtils.getDate_Time(element
											.get("authorTimestamp").toString()));

							commitList.add(commitObject);

						}
					//}

				}

			}
		}
		return commitList;
	}

	public String getStashUsers() {
		ArrayList userList = new ArrayList();

		String result = "";

		try {

			URL url = new URL(
					"http://stash.fb.lab/rest/api/1.0/admin/groups/more-members?context=stash-users&limit=100");
			URLConnection urlConnection = url.openConnection();
			urlConnection.setRequestProperty("Authorization",
					"Basic " + util.getauthStringEnc());
			InputStream is = urlConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);

			int numCharsRead;
			char[] charArray = new char[1024];
			StringBuffer sb = new StringBuffer();
			while ((numCharsRead = isr.read(charArray)) > 0) {
				sb.append(charArray, 0, numCharsRead);
			}
			result = sb.toString();

			System.out.println("***  Users ***" + result);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;

	}

	public ArrayList displayStashUsers(String jsonTxt) {

		ArrayList userList = new ArrayList();
		String name;
		String displayName;
		if (!jsonTxt.isEmpty()) {
			JSONObject mainJson = (JSONObject) JSONSerializer.toJSON(jsonTxt);

			JSONArray valuesJsonarray = mainJson.getJSONArray("values");

			for (int i = 0; i < valuesJsonarray.size(); i++) {
				UserDetails userUtilsObj = new UserDetails();
				JSONObject element = valuesJsonarray.getJSONObject(i);

				userUtilsObj.setName(element.get("name").toString());
				userUtilsObj.setDisplayName(element.get("displayName")
						.toString());
				userList.add(userUtilsObj);

			}
		}
		return userList;
	}

	public static void main(String args[]) {
		ProjectUtils utils = new ProjectUtils();
		utils.getStashUsers();
	}

}
