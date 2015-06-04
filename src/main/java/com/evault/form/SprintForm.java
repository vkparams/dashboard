package com.evault.form;

import java.util.ArrayList;

public class SprintForm {

	private ArrayList rapidView;
	private ArrayList sprintNames;
	private ArrayList sprintIds;
	private ArrayList customerNames;
	private ArrayList componentNames;
	private ArrayList severity;
	private String rapid;

	public ArrayList<String> getProjectNames() {
		return projectNames;
	}

	public void setProjectNames(ArrayList<String> projectNames) {
		this.projectNames = projectNames;
	}

	public ArrayList<String> getProjectKeys() {
		return projectKeys;
	}

	public void setProjectKeys(ArrayList<String> projectKeys) {
		this.projectKeys = projectKeys;
	}

	private ArrayList<String> projectNames, projectKeys;

	private String length;

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getRapid() {
		return rapid;
	}

	public void setRapid(String rapid) {
		this.rapid = rapid;
	}

	public ArrayList getSprintNames() {
		return sprintNames;
	}

	public void setSprintNames(ArrayList sprintNames) {
		this.sprintNames = sprintNames;
	}

	public ArrayList getRapidView() {
		return rapidView;
	}

	public void setRapidView(ArrayList rapidView) {
		this.rapidView = rapidView;
	}

	public ArrayList getSprintIds() {
		return sprintIds;
	}

	public void setSprintIds(ArrayList sprintIds) {
		this.sprintIds = sprintIds;
	}

	public ArrayList getCustomerNames() {
		return customerNames;
	}

	public void setCustomerNames(ArrayList customerNames) {
		this.customerNames = customerNames;
	}

	public ArrayList getSeverity() {
		return severity;
	}

	public void setSeverity(ArrayList severity) {
		this.severity = severity;
	}


	public ArrayList getComponentNames() {
		return componentNames;
	}

	public void setComponentNames(ArrayList componentNames) {
		this.componentNames = componentNames;
	}

}
