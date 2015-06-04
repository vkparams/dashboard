package com.evault.form;

public class SprintTrendForm {

	int created_count;
	int updated_count;
	int remaining_open;
	String date;

	public int getRemaining_open() {
		return remaining_open;
	}

	public void setRemaining_open(int remaining_open) {
		this.remaining_open = remaining_open;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getCreated_count() {
		return created_count;
	}

	public void setCreated_count(int created_count) {
		this.created_count = created_count;
	}

	public int getUpdated_count() {
		return updated_count;
	}

	public void setUpdated_count(int updated_count) {
		this.updated_count = updated_count;
	}
}
