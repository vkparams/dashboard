package com.evault.form;

import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pkumarasamy
 * Date: 1/17/14
 * Time: 11:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProjectForm {

    private List projects;
    private List repos;
    private String projectName;
    private HashMap commitMap;
    private List users;


    public List getUsers() {
		return users;
	}

	public void setUsers(List users) {
		this.users = users;
	}

	public List getProjects() {
        return projects;
    }

    public void setProjects(List projects) {
        this.projects = projects;
    }

    public List getRepos() {
        return repos;
    }

    public void setRepos(List repos) {
        this.repos = repos;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public HashMap getCommitMap() {
        return commitMap;
    }

    public void setCommitMap(HashMap commitMap) {
        this.commitMap = commitMap;
    }
}
