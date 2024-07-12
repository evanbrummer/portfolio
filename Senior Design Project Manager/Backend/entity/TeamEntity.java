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
@Table(name = "team_table")
public class TeamEntity {
    @Id
    @GeneratedValue
    @Column(name = "team_id")
    private int id;

    @Column(name = "cohort_name")
    private String cohortName;

    @Column(name = "team_number")
    private int team_number;

    @Column(name = "min_members")
    private int minMembers;

    @Column(name = "max_members")
    private int maxMembers;

    @Column(name = "project_id")
    private int projectEntity;
    
    @Column(name= "project_name")
    private String projectName;

    @ManyToMany(mappedBy = "teams")
    private List<UserEntity> teamMembers;

}
