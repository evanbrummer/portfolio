package com.iastate.project_matcher.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProposalDTO {
    private String client;
    private String submitter;
    private String subEmail;
    private String contact;
    private String contEmail;
    private String title;
    private String projectAbstract;
    private String deliverables;
    private String resources;
    private String cost;
    private String financeResources;
    private boolean electricalEng;
    private boolean computerEng;
    private boolean softwareEng;
    private boolean cyberEng;
    private String skills;
    private String interactionFreq;
    private String interactionStyle;
    private int abet1;
    private int abet2;
    private int abet3;
    private int abet4;
    private int abet5;
    // Add other fields as needed
}