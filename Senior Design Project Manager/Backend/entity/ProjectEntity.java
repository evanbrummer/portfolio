package com.iastate.project_matcher.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "project_table")
public class ProjectEntity {
    @Id
    @GeneratedValue
    @Column(name = "project_id")
    private int id;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "project_description", columnDefinition = "TEXT")
    private String projectDescription;

    @Column(name = "is_available")
    private Boolean isAvailable;
    
    @Column(name = "is_approved")
    private Boolean isApproved;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "amount_needed")
    private int numPeople;

    @Column(name = "wants_ee")
    private Boolean ee;

    @Column(name = "wants_se")
    private Boolean se;

    @Column(name = "wants_cpre")
    private Boolean cpre;

    @Column(name = "wants_cyber")
    private Boolean cyber;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "projectEntity")
    private List<TeamEntity> teams;

}
