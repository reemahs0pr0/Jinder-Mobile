package com.example.jinder_mobile;

import java.io.Serializable;

public class Job implements Serializable {

    private String jobTitle;
    private String companyName;
    private String jobDescription;
    private String skills;
    private String jobAppUrl;

    public Job() {
        super();
    }

    public Job(String jobTitle, String companyName, String jobDescription, String skills, String jobAppUrl) {
        super();
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.jobDescription = jobDescription;
        this.skills = skills;
        this.jobAppUrl = jobAppUrl;
    }

    public String getJobTitle() {
        return jobTitle;
    }
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getJobAppUrl() {
        return jobAppUrl;
    }

    public void setJobAppUrl(String jobAppUrl) {
        this.jobAppUrl = jobAppUrl;
    }

    @Override
    public String toString() {
        return "Job [jobTitle=" + jobTitle + ", companyName=" + companyName + ", jobDescription=" + jobDescription
                + ", skills=" + skills + ", jobAppUrl=" + jobAppUrl + "]";
    }
}
