package com.evault.form;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: pkumarasamy
 * Date: 12/9/13
 * Time: 1:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class CommitDetails {

	
    String displayId;
    String name;
    String message;
    String date;
    String changetListPath;
    String count;
    HashMap commitMap;
    ArrayList graphData;
    HashMap tableData;
    ArrayList totalData;

    
    
    
    public ArrayList getTotalData() {
		return totalData;
	}

	public void setTotalData(ArrayList totalData) {
		this.totalData = totalData;
	}

	public HashMap getTableData() {
		return tableData;
	}

	public void setTableData(HashMap tableData) {
		this.tableData = tableData;
	}

	public ArrayList getGraphData() {
		return graphData;
	}

	public void setGraphData(ArrayList graphData) {
		this.graphData = graphData;
	}

	public HashMap getCommitMap() {
		return commitMap;
	}

	public void setCommitMap(HashMap commitMap) {
		this.commitMap = commitMap;
	}



	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getChangetListPath() {
		return changetListPath;
	}

	public void setChangetListPath(String id) {
		this.changetListPath = id;
	}

	public String getDisplayId() {
        return displayId;
    }

    public void setDisplayId(String id) {
        this.displayId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
