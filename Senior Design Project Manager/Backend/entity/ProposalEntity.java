package com.iastate.project_matcher.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "proposal_table")
public class ProposalEntity
{
    @Id
    @GeneratedValue
    @Column(name = "proposal_id")
    private int id;

    @Column(name = "client_name")
    private String client;

    @Column(name = "submitter")
    private String submitter;

    @Column(name = "sub_email")
    private String subEmail;

    @Column(name = "contact")
    private String contact;

    @Column(name = "cont_email")
    private String contEmail;

    @Column(name = "title")
    private String title;

    @Column(name = "abstract")
    private String projectAbstract;

    @Column(name = "deliverables")
    private String deliverables;

    @Column(name = "resources")
    private String resources;

    @Column(name = "cost")
    private String cost;

    @Column(name = "finance_resources")
    private String financeResources;

    @Column(name = "electrical_eng")
    private Boolean electricalEng;

    @Column(name = "computer_eng")
    private Boolean computerEng;

    @Column(name = "software_eng")
    private Boolean softwareEng;

    @Column(name = "cyber_eng")
    private Boolean cyberEng;

    @Column(name = "skills")
    private String skills;

    @Column(name = "interaction_freq")
    private String interactionFreq;

    @Column(name = "interaction_style")
    private String interactionStyle;

    @Column(name = "abet_1")
    private int abet1;

    @Column(name = "abet_2")
    private int abet2;

    @Column(name = "abet_3")
    private int abet3;

    @Column(name = "abet_4")
    private int abet4;

    @Column(name = "abet_5")
    private int abet5;

    @Column(name = "approved")
    private Boolean approved;

    // Setter methods
    public void setId(int id) {
        this.id = id;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    public void setSubEmail(String subEmail) {
        this.subEmail = subEmail;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setContEmail(String contEmail) {
        this.contEmail = contEmail;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setProjectAbstract(String projectAbstract) {
        this.projectAbstract = projectAbstract;
    }

    public void setDeliverables(String deliverables) {
        this.deliverables = deliverables;
    }

    public void setResources(String resources) {
        this.resources = resources;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public void setFinanceResources(String financeResources) {
        this.financeResources = financeResources;
    }

    public void setElectricalEng(Boolean electricalEng) {
        this.electricalEng = electricalEng;
    }

    public void setComputerEng(Boolean computerEng) {
        this.computerEng = computerEng;
    }

    public void setSoftwareEng(Boolean softwareEng) {
        this.softwareEng = softwareEng;
    }

    public void setCyberEng(Boolean cyberEng) {
        this.cyberEng = cyberEng;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public void setInteractionFreq(String interactionFreq) {
        this.interactionFreq = interactionFreq;
    }

    public void setInteractionStyle(String interactionStyle) {
        this.interactionStyle = interactionStyle;
    }

    public void setAbet1(int abet1) {
        this.abet1 = abet1;
    }

    public void setAbet2(int abet2) {
        this.abet2 = abet2;
    }

    public void setAbet3(int abet3) {
        this.abet3 = abet3;
    }

    public void setAbet4(int abet4) {
        this.abet4 = abet4;
    }

    public void setAbet5(int abet5) {
        this.abet5 = abet5;
    }
}
