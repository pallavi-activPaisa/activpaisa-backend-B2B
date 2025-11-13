package com.activpaisa.loan_app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "teams")
@Getter
@Setter
public class Team {

    @Id
    @Column(name = "team_id", length = 50)
    private String id;

    @ManyToOne
    @JoinColumn(name = "org_id")
    private Organization organization;

    @Column(name = "team_name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "manager_user_id")
    private User manager;

    @ManyToOne
    @JoinColumn(name = "parent_team_id")
    private Team parentTeam;

    @OneToMany(mappedBy = "parentTeam")
    private List<Team> subTeams;

    @OneToMany(mappedBy = "team")
    private List<EmployeeProfile> employees;

    // Getters & Setters
}