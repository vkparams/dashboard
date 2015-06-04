package com.evault.form;

import java.util.ArrayList;
import java.util.HashMap;

public class SprintIssueForm {

	ArrayList<String> keys_completedIssues, summaries_completedIssues,
			assigneeNames_completedIssues, statusNames_completedIssues,
			creationDate_completedIssues, issueType_completedIssues;

	ArrayList<String> keys_puntedIssues, summaries_puntedIssues,
			assigneeNames_puntedIssues, statusNames_puntedIssues,
			creationDate_puntedIssues, issueType_puntedIssues;
	
	ArrayList<String> keys_incompleteIssues, summaries_incompleteIssues,
	assigneeNames_incompleteIssues, statusNames_incompleteIssues,
	creationDate_incompleteIssues, issueType_incompleteIssues;

	String sprint_start_date, sprint_end_date, sprint_status;

	int bug_count, story_count, task_count;

	ArrayList<String> issues_added_sprint;

	ArrayList<String> components;

	public ArrayList<String> getSeverityList() {
		return severityList;
	}

	public void setSeverityList(ArrayList<String> severityList) {
		this.severityList = severityList;
	}

	ArrayList<String> severityList;

	public ArrayList<String> getDaysOpen() {
		return daysOpen;
	}

	public void setDaysOpen(ArrayList<String> daysOpen) {
		this.daysOpen = daysOpen;
	}

	ArrayList<String> daysOpen;

	public ArrayList<String> getCustomerName() {
		return customerName;
	}

	public void setCustomerName(ArrayList<String> customerName) {
		this.customerName = customerName;
	}

	ArrayList<String> customerName;

	int closed_count, open_count, inProgress_count, resolved_count,
			total_count, newFeature_count, otherIssue_count, verified_count;

	int bugs_open, bugs_inprogress,bugs_resolved,bugs_closed;
	int tasks_open;

	String component;
	HashMap<String, Integer> bugs_open_map, bugs_closed_map, bugs_inProgress_map, bugs_resolved_map;

	public HashMap<String, Integer> getBugs_open_map() {
		return bugs_open_map;
	}

	public void setBugs_open_map(HashMap<String, Integer> bugs_open_map) {
		this.bugs_open_map = bugs_open_map;
	}

	public HashMap<String, Integer> getBugs_closed_map() {
		return bugs_closed_map;
	}

	public void setBugs_closed_map(HashMap<String, Integer> bugs_closed_map) {
		this.bugs_closed_map = bugs_closed_map;
	}

	public HashMap<String, Integer> getBugs_inProgress_map() {
		return bugs_inProgress_map;
	}

	public void setBugs_inProgress_map(HashMap<String, Integer> bugs_inProgress_map) {
		this.bugs_inProgress_map = bugs_inProgress_map;
	}

	public HashMap<String, Integer> getBugs_resolved_map() {
		return bugs_resolved_map;
	}

	public void setBugs_resolved_map(HashMap<String, Integer> bugs_resolved_map) {
		this.bugs_resolved_map = bugs_resolved_map;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public ArrayList<String> getComponents() {
		return components;
	}

	public void setComponents(ArrayList<String> components) {
		this.components = components;
	}

	public int getTasks_inprogress() {
		return tasks_inprogress;
	}

	public void setTasks_inprogress(int tasks_inprogress) {
		this.tasks_inprogress = tasks_inprogress;
	}

	public int getBugs_open() {
		return bugs_open;
	}

	public void setBugs_open(int bugs_open) {
		this.bugs_open = bugs_open;
	}

	public int getBugs_inprogress() {
		return bugs_inprogress;
	}

	public void setBugs_inprogress(int bugs_inprogress) {
		this.bugs_inprogress = bugs_inprogress;
	}

	public int getBugs_resolved() {
		return bugs_resolved;
	}

	public void setBugs_resolved(int bugs_resolved) {
		this.bugs_resolved = bugs_resolved;
	}

	public int getBugs_closed() {
		return bugs_closed;
	}

	public void setBugs_closed(int bugs_closed) {
		this.bugs_closed = bugs_closed;
	}

	public int getTasks_open() {
		return tasks_open;
	}

	public void setTasks_open(int tasks_open) {
		this.tasks_open = tasks_open;
	}

	public int getTasks_resolved() {
		return tasks_resolved;
	}

	public void setTasks_resolved(int tasks_resolved) {
		this.tasks_resolved = tasks_resolved;
	}

	public int getTasks_closed() {
		return tasks_closed;
	}

	public void setTasks_closed(int tasks_closed) {
		this.tasks_closed = tasks_closed;
	}

	public int getStory_open() {
		return story_open;
	}

	public void setStory_open(int story_open) {
		this.story_open = story_open;
	}

	public int getStory_inprogress() {
		return story_inprogress;
	}

	public void setStory_inprogress(int story_inprogress) {
		this.story_inprogress = story_inprogress;
	}

	public int getStory_resolved() {
		return story_resolved;
	}

	public void setStory_resolved(int story_resolved) {
		this.story_resolved = story_resolved;
	}

	public int getStory_closed() {
		return story_closed;
	}

	public void setStory_closed(int story_closed) {
		this.story_closed = story_closed;
	}

	int tasks_inprogress;
	int tasks_resolved;
	int tasks_closed;
	int story_open, story_inprogress,story_resolved,story_closed;
	
	
	public ArrayList<String> getKeys_incompleteIssues() {
		return keys_incompleteIssues;
	}

	public void setKeys_incompleteIssues(ArrayList<String> keys_incompleteIssues) {
		this.keys_incompleteIssues = keys_incompleteIssues;
	}

	public ArrayList<String> getSummaries_incompleteIssues() {
		return summaries_incompleteIssues;
	}

	public void setSummaries_incompleteIssues(
			ArrayList<String> summaries_incompleteIssues) {
		this.summaries_incompleteIssues = summaries_incompleteIssues;
	}

	public ArrayList<String> getAssigneeNames_incompleteIssues() {
		return assigneeNames_incompleteIssues;
	}

	public void setAssigneeNames_incompleteIssues(
			ArrayList<String> assigneeNames_incompleteIssues) {
		this.assigneeNames_incompleteIssues = assigneeNames_incompleteIssues;
	}

	public ArrayList<String> getStatusNames_incompleteIssues() {
		return statusNames_incompleteIssues;
	}

	public void setStatusNames_incompleteIssues(
			ArrayList<String> statusNames_incompleteIssues) {
		this.statusNames_incompleteIssues = statusNames_incompleteIssues;
	}

	public ArrayList<String> getCreationDate_incompleteIssues() {
		return creationDate_incompleteIssues;
	}

	public void setCreationDate_incompleteIssues(
			ArrayList<String> creationDate_incompleteIssues) {
		this.creationDate_incompleteIssues = creationDate_incompleteIssues;
	}

	public ArrayList<String> getIssueType_incompleteIssues() {
		return issueType_incompleteIssues;
	}

	public void setIssueType_incompleteIssues(
			ArrayList<String> issueType_incompleteIssues) {
		this.issueType_incompleteIssues = issueType_incompleteIssues;
	}

	public int getNewFeature_count() {
		return newFeature_count;
	}

	public void setNewFeature_count(int newFeature_count) {
		this.newFeature_count = newFeature_count;
	}

	public int getOtherIssue_count() {
		return otherIssue_count;
	}

	public void setOtherIssue_count(int otherIssue_count) {
		this.otherIssue_count = otherIssue_count;
	}

	public int getVerified_count() {
		return verified_count;
	}

	public void setVerified_count(int verified_count) {
		this.verified_count = verified_count;
	}

	public int getTotal_count() {
		return total_count;
	}

	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}

	public int getClosed_count() {
		return closed_count;
	}

	public void setClosed_count(int closed_count) {
		this.closed_count = closed_count;
	}

	public int getOpen_count() {
		return open_count;
	}

	public void setOpen_count(int open_count) {
		this.open_count = open_count;
	}

	public int getInProgress_count() {
		return inProgress_count;
	}

	public void setInProgress_count(int inProgress_count) {
		this.inProgress_count = inProgress_count;
	}

	public int getResolved_count() {
		return resolved_count;
	}

	public void setResolved_count(int resolved_count) {
		this.resolved_count = resolved_count;
	}

	public String getSprint_start_date() {
		return sprint_start_date;
	}

	public void setSprint_start_date(String sprint_start_date) {
		this.sprint_start_date = sprint_start_date;
	}

	public String getSprint_end_date() {
		return sprint_end_date;
	}

	public void setSprint_end_date(String sprint_end_date) {
		this.sprint_end_date = sprint_end_date;
	}

	public String getSprint_status() {
		return sprint_status;
	}

	public void setSprint_status(String sprint_status) {
		this.sprint_status = sprint_status;
	}

	public int getBug_count() {
		return bug_count;
	}

	public void setBug_count(int bug_count) {
		this.bug_count = bug_count;
	}

	public int getStory_count() {
		return story_count;
	}

	public void setStory_count(int story_count) {
		this.story_count = story_count;
	}

	public int getTask_count() {
		return task_count;
	}

	public void setTask_count(int task_count) {
		this.task_count = task_count;
	}

	public ArrayList<String> getKeys_completedIssues() {
		return keys_completedIssues;
	}

	public void setKeys_completedIssues(ArrayList<String> keys_completedIssues) {
		this.keys_completedIssues = keys_completedIssues;
	}

	public ArrayList<String> getSummaries_completedIssues() {
		return summaries_completedIssues;
	}

	public void setSummaries_completedIssues(
			ArrayList<String> summaries_completedIssues) {
		this.summaries_completedIssues = summaries_completedIssues;
	}

	public ArrayList<String> getAssigneeNames_completedIssues() {
		return assigneeNames_completedIssues;
	}

	public void setAssigneeNames_completedIssues(
			ArrayList<String> assigneeNames_completedIssues) {
		this.assigneeNames_completedIssues = assigneeNames_completedIssues;
	}

	public ArrayList<String> getStatusNames_completedIssues() {
		return statusNames_completedIssues;
	}

	public void setStatusNames_completedIssues(
			ArrayList<String> statusNames_completedIssues) {
		this.statusNames_completedIssues = statusNames_completedIssues;
	}

	public ArrayList<String> getCreationDate_completedIssues() {
		return creationDate_completedIssues;
	}

	public void setCreationDate_completedIssues(
			ArrayList<String> creationDate_completedIssues) {
		this.creationDate_completedIssues = creationDate_completedIssues;
	}

	public ArrayList<String> getIssueType_completedIssues() {
		return issueType_completedIssues;
	}

	public void setIssueType_completedIssues(
			ArrayList<String> issueType_completedIssues) {
		this.issueType_completedIssues = issueType_completedIssues;
	}

	public ArrayList<String> getKeys_puntedIssues() {
		return keys_puntedIssues;
	}

	public void setKeys_puntedIssues(ArrayList<String> keys_puntedIssues) {
		this.keys_puntedIssues = keys_puntedIssues;
	}

	public ArrayList<String> getSummaries_puntedIssues() {
		return summaries_puntedIssues;
	}

	public void setSummaries_puntedIssues(
			ArrayList<String> summaries_puntedIssues) {
		this.summaries_puntedIssues = summaries_puntedIssues;
	}

	public ArrayList<String> getAssigneeNames_puntedIssues() {
		return assigneeNames_puntedIssues;
	}

	public void setAssigneeNames_puntedIssues(
			ArrayList<String> assigneeNames_puntedIssues) {
		this.assigneeNames_puntedIssues = assigneeNames_puntedIssues;
	}

	public ArrayList<String> getStatusNames_puntedIssues() {
		return statusNames_puntedIssues;
	}

	public void setStatusNames_puntedIssues(
			ArrayList<String> statusNames_puntedIssues) {
		this.statusNames_puntedIssues = statusNames_puntedIssues;
	}

	public ArrayList<String> getCreationDate_puntedIssues() {
		return creationDate_puntedIssues;
	}

	public void setCreationDate_puntedIssues(
			ArrayList<String> creationDate_puntedIssues) {
		this.creationDate_puntedIssues = creationDate_puntedIssues;
	}

	public ArrayList<String> getIssueType_puntedIssues() {
		return issueType_puntedIssues;
	}

	public void setIssueType_puntedIssues(
			ArrayList<String> issueType_puntedIssues) {
		this.issueType_puntedIssues = issueType_puntedIssues;
	}

	public ArrayList<String> getIssues_added_sprint() {
		return issues_added_sprint;
	}

	public void setIssues_added_sprint(ArrayList<String> issues_added_sprint) {
		this.issues_added_sprint = issues_added_sprint;
	}

}
