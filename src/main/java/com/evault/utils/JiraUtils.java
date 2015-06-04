package com.evault.utils;

import java.net.URI;
import java.util.Iterator;

import javax.naming.directory.SearchResult;

public class JiraUtils {
	
	
//	 public static void main(String[] args) throws Exception {
//		 
//	        JerseyJiraRestClientFactory f = new JerseyJiraRestClientFactory();
//	        JiraRestClient jc = f.createWithBasicHttpAuthentication(new URI("http://localhost:8080"), "admin", "123");
//	 
//	        SearchResult r = jc.getSearchClient().searchJql("type = Epic ORDER BY RANK ASC", null);
//	         
//	        Iterator<BasicIssue> it = r.getIssues().iterator();
//	        while (it.hasNext()) {
//	             
//	            Issue issue = jc.getIssueClient().getIssue(((BasicIssue)it.next()).getKey(), null);
//	             
//	            System.out.println("Epic: " + issue.getKey() + " " + issue.getSummary());
//	             
//	            Iterator<IssueLink> itLink = issue.getIssueLinks().iterator();
//	            while (itLink.hasNext()) {
//	                 
//	                IssueLink issueLink = (IssueLink)itLink.next();
//	                Issue issueL = jc.getIssueClient().getIssue((issueLink).getTargetIssueKey(), null);
//	                 
//	                System.out.println(issueLink.getIssueLinkType().getDescription() + ": " + issueL.getKey() + " " + issueL.getSummary() + " " + issueL.getFieldByName("Story Points").getValue());
//	                 
//	            }
//	             
//	        }
//	         
//	    }

}
