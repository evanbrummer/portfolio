package com.iastate.project_matcher.controller;

import com.iastate.project_matcher.dto.ProjectDTO;
import com.iastate.project_matcher.dto.ProposalDTO;
import com.iastate.project_matcher.entity.ProjectEntity;
import com.iastate.project_matcher.entity.ProposalEntity;
import com.iastate.project_matcher.entity.UserEntity;
import com.iastate.project_matcher.exception.ProjectAlreadyExistsException;
import com.iastate.project_matcher.exception.ProjectDoesNotExistException;
import com.iastate.project_matcher.exception.UserAlreadyExistsException;
import com.iastate.project_matcher.service.ProjectService;
import com.iastate.project_matcher.service.ProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value= "proposal")
public class ProposalController
{
    ProposalService proposalService;

    @Autowired
    public ProposalController(ProposalService proposalService) {
        this.proposalService = proposalService;
    }

    @PostMapping("/create")
    public ResponseEntity<ProposalEntity> createProposal(@RequestBody ProposalDTO request) {
        try {
            ProposalEntity response = proposalService.createProposal(request);
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/allProposals")
    public ResponseEntity<List<ProposalEntity>> getAllProjectProposals() {
        try {
            List<ProposalEntity> response = proposalService.getAllProposals();
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }
}
