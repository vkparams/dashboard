package com.evault.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.evault.form.CommitDetails;
import com.evault.form.ProjectForm;
import com.evault.utils.ConfigurationManager;
import com.evault.utils.EvaultDBUtils;
import com.evault.utils.ProjectUtils;
import com.evault.utils.StashUtils;

@Controller
public class StashController {

    @RequestMapping(value = "/reports", method = RequestMethod.POST)
    public ModelAndView displayProjects(
            @RequestParam(value = "project", required = false) String projectName,
            @RequestParam(value = "repo", required = false) String repos,
            @RequestParam(value = "user", required = false) String users,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate,
            @RequestParam(value = "branch", required = false) String branch) {

        System.out.println(" Project  " + projectName + " repo  " + repos
                + " branch  " + branch);
        System.out.println(" fromDate  " + fromDate + " toDate  " + toDate);

        ProjectUtils projUtils = new ProjectUtils();

        ArrayList repoList;
        String[] repoArray;
        if (repos != null && !repos.isEmpty()) {
            repoArray = repos.split(",");
            repoList = new ArrayList(Arrays.asList(repoArray));
        } else {
            ArrayList repoJSONList = projUtils.getRepos(projectName);
            repoList = projUtils.getRepoDetails(repoJSONList);
        }

        ArrayList userList = new ArrayList();
        String[] userArray;
        if (users != null && !users.isEmpty()) {
            userArray = users.split(",");
            userList = new ArrayList(Arrays.asList(userArray));
        }

        System.out.println("User List: " + userList);

        SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyyy");
        Date date = new Date();
        if (fromDate == null || fromDate.isEmpty()) {
            fromDate = sdf.format(date);
        }
        if (toDate == null || toDate.isEmpty()) {
            toDate = sdf.format(date);
        }

        HashMap commitMap = projUtils.getCommits(projectName, repoList,
                userList, branch, fromDate, toDate);

        System.out.println("-------Project Name: " + projectName + " repoList: " + repoList + " userList: " + userList + " fromDate: " + fromDate + " toDate: " + toDate);

        System.out.println("commitMap -->" + commitMap);
        ProjectForm projects = new ProjectForm();
        projects.setCommitMap(commitMap);
        HashMap finaldata = new HashMap();
        HashMap tableData = new HashMap();
        ArrayList totalData = new ArrayList();

        ArrayList graphList = new ArrayList();
        if (StashUtils.isGraphDisabled().equals("true")) {

            EvaultDBUtils dbUtils = new EvaultDBUtils();
            dbUtils.cleanExistingRecords();

            dbUtils.saveCommits(commitMap);
            ArrayList datesbetweenDate = StashUtils.getDatesBetweenDate(
                    fromDate, toDate);
            dbUtils.saveCommit_report(datesbetweenDate,
                    dbUtils.retriveCommits());
            finaldata = dbUtils.prepareReportdata(datesbetweenDate);
            graphList = (ArrayList) finaldata.get("trendData");
            tableData = (HashMap) finaldata.get("tableData");

            totalData = (ArrayList) tableData.get("Total");

        }
        CommitDetails commitDetailsObj = new CommitDetails();
        commitDetailsObj.setCommitMap(commitMap);
        commitDetailsObj.setGraphData(graphList);
        commitDetailsObj.setTableData(tableData);
//		commitDetailsObj.setTotalData(totalData);

        return new ModelAndView("displayCommitDetails", "commitDetailsObj",
                commitDetailsObj);
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ModelAndView displayProjects() {

        ProjectUtils projUtils = new ProjectUtils();
        List projectsList = projUtils.getProjectDetailsNew();
        List userList = projUtils.displayStashUsers(projUtils.getStashUsers());

        ProjectForm projects = new ProjectForm();
        projects.setProjects(projectsList);
        projects.setUsers(userList);

        System.out.println(" Project  " + projectsList);

        return new ModelAndView("selectRepos", "projectForm", projects);
    }


    @RequestMapping(value = "/home/project/{name}", method = RequestMethod.GET)
    public ModelAndView displayRepos(@PathVariable String name) {

        System.out.println(" Selected Project  " + name);
        ProjectUtils projUtils = new ProjectUtils();
        List projectsList = projUtils.getProjectDetailsNew();

        ProjectForm projects = new ProjectForm();
        projects.setProjects(projectsList);
        projects.setProjectName(name);

        ArrayList repoJSONList = projUtils.getRepos(name);
        ArrayList repoList = projUtils.getRepoDetails(repoJSONList);
        List userList = projUtils.displayStashUsers(projUtils.getStashUsers());

        projects.setRepos(repoList);
        projects.setUsers(userList);

        System.out.println(" Project  " + projectsList);

        return new ModelAndView("selectRepos", "projectForm", projects);
    }

}
