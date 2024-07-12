package com.iastate.project_matcher.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iastate.project_matcher.constants.Major;
import com.iastate.project_matcher.constants.UserType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_table")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private int id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "user_type", nullable = true)
    private HashSet<UserType> userType;

    public void addUserType(UserType type) {
        if (userType == null) { userType = new HashSet<>(); }

        userType.add(type);
    }

    public boolean hasUserType(UserType type) {
        if (userType == null) { return false; }

        return userType.contains(type);
    }

    @Column(name = "is_available")
    private Boolean isAvailable;

    @Column(name="sectionNumber")
    private int sectionNumber;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "has_submitted")
    private Boolean hasSubmitted;

    @Enumerated(EnumType.STRING)
    @Column(name = "major", nullable = true)
    private Major major;

    @Column(name = "active_project")
    private int projectTeam;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "user_preferences")
    private PreferencesEntity preferencesEntity;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "user_team",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "team_id"))
    private List<TeamEntity> teams;

    public boolean usesSingleSignOn() {
        return email.endsWith("iastate.edu");
    }
}
