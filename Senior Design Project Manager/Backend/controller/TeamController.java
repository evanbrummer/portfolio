package com.iastate.project_matcher.controller;

import com.iastate.project_matcher.entity.TeamEntity;
import com.iastate.project_matcher.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value= "team")
public class TeamController {
    TeamService teamService;
    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<TeamEntity>> getAllTeams() {
        try {
            List<TeamEntity> response = teamService.getAllTeams();
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<String> deleteAllTeams() {
        try {
            teamService.deleteAllTeams();
            return new ResponseEntity<>("All teams deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete teams", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
