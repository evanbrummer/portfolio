package com.iastate.project_matcher.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "preferences_table")
public class PreferencesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_choices_id", nullable = false)
    private int id;

    @Column(name = "project_one", nullable = true)
    private int p1;

    @Column(name = "project_two", nullable = true)
    private int p2;

    @Column(name = "project_three", nullable = true)
    private int p3;

    @Column(name = "project_four")
    private int p4;

    @Column(name = "project_five")
    private int p5;

    @Column(name = "student_one", nullable = true)
    private int s1;

    @Column(name = "student_two", nullable = true)
    private int s2;

    @Column(name = "student_three", nullable = true)
    private int s3;

    @Column(name = "project_one_bid")
    private int bidP1;

    @Column(name = "project_two_bid")
    private int bidP2;

    @Column(name = "project_three_bid")
    private int bidP3;

    @Column(name = "project_four_bid")
    private int bidP4;

    @Column(name = "project_five_bid")
    private int bidP5;

    @Column(name = "student_one_bid")
    private int bidS1;

    @Column(name = "student_two_bid")
    private int bidS2;

    @Column(name = "student_three_bid")
    private int bidS3;

}
