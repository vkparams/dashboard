package com.evault.form;

import java.util.ArrayList;

public class SprintInfoForm {
	
	private ArrayList<String> keys, typeNames, summary, assigneeName, priorityName, statusName;
	private ArrayList<Boolean> done;
	private ArrayList<Integer> sprintIds;
	private ArrayList<String> sprintStates, sprintNames;
	
	public ArrayList<String> getSprintNames() {
		return sprintNames;
	}
	public void setSprintNames(ArrayList<String> sprintNames) {
		this.sprintNames = sprintNames;
	}
	public ArrayList<String> getSprintStates() {
		return sprintStates;
	}
	public void setSprintStates(ArrayList<String> sprintStates) {
		this.sprintStates = sprintStates;
	}
	public ArrayList<Integer> getSprintIds() {
		return sprintIds;
	}
	public void setSprintIds(ArrayList<Integer> sprintIds) {
		this.sprintIds = sprintIds;
	}
	public ArrayList<String> getKeys() {
		return keys;
	}
	public void setKeys(ArrayList<String> keys) {
		this.keys = keys;
	}
	public ArrayList<String> getTypeNames() {
		return typeNames;
	}
	public void setTypeNames(ArrayList<String> typeNames) {
		this.typeNames = typeNames;
	}
	public ArrayList<String> getSummary() {
		return summary;
	}
	public void setSummary(ArrayList<String> summary) {
		this.summary = summary;
	}
	public ArrayList<String> getAssigneeName() {
		return assigneeName;
	}
	public void setAssigneeName(ArrayList<String> assigneeName) {
		this.assigneeName = assigneeName;
	}
	public ArrayList<String> getPriorityName() {
		return priorityName;
	}
	public void setPriorityName(ArrayList<String> priorityName) {
		this.priorityName = priorityName;
	}
	public ArrayList<String> getStatusName() {
		return statusName;
	}
	public void setStatusName(ArrayList<String> statusName) {
		this.statusName = statusName;
	}
	public ArrayList<Boolean> getDone() {
		return done;
	}
	public void setDone(ArrayList<Boolean> done) {
		this.done = done;
	}
	

}
