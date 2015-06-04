package com.evault.form;

public class JiraTrendsForm {

	int open_issues, resolved_issues, closed_issues;
	String start_date, end_date;
	String start_date_jql, end_date_jql;

	public String getStart_date_jql() {
		return start_date_jql;
	}

	public void setStart_date_jql(String start_date_jql) {
		this.start_date_jql = start_date_jql;
	}

	public String getEnd_date_jql() {
		return end_date_jql;
	}

	public void setEnd_date_jql(String end_date_jql) {
		this.end_date_jql = end_date_jql;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public int getOpen_issues() {
		return open_issues;
	}

	public void setOpen_issues(int open_issues) {
		this.open_issues = open_issues;
	}

	public int getResolved_issues() {
		return resolved_issues;
	}

	public void setResolved_issues(int resolved_issues) {
		this.resolved_issues = resolved_issues;
	}

	public int getClosed_issues() {
		return closed_issues;
	}

	public void setClosed_issues(int closed_issues) {
		this.closed_issues = closed_issues;
	}
}
