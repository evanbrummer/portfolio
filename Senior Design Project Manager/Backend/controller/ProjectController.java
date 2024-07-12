package com.iastate.project_matcher.controller;

import com.iastate.project_matcher.dto.ProjectDTO;
import com.iastate.project_matcher.entity.ProjectEntity;
import com.iastate.project_matcher.exception.ProjectAlreadyExistsException;
import com.iastate.project_matcher.exception.ProjectDoesNotExistException;
import com.iastate.project_matcher.exception.UserAlreadyExistsException;
import com.iastate.project_matcher.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value= "project")
public class ProjectController {

    ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/create")
    public ResponseEntity<ProjectEntity> createProject(@RequestBody ProjectDTO request) {
        try {
            ProjectEntity response = projectService.createProject(request);
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.CREATED);
        } catch (ProjectAlreadyExistsException e) {
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectEntity> getProject(@PathVariable int id) {
        try {
            ProjectEntity response = projectService.getProject(id);
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
        } catch (ProjectDoesNotExistException e) {
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("")
    public ResponseEntity<List<ProjectEntity>> getProjects() {
        try {
            List<ProjectEntity> response = projectService.getProjects();
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/active")
    public ResponseEntity<List<ProjectEntity>> getActiveProjects() {
        try {
            List<ProjectEntity> response = projectService.getActiveProjects();
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ProjectEntity> updateProjectEntity(@RequestBody ProjectEntity projectEntity) {
        try {
            ProjectEntity response = projectService.updateProjectEntity(projectEntity);
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }
}
