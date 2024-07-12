package com.iastate.project_matcher.controller;

import org.apache.commons.csv.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.iastate.project_matcher.constants.Major;
import com.iastate.project_matcher.constants.UserType;
import com.iastate.project_matcher.dto.UserDTO;
import com.iastate.project_matcher.service.UserService;

import jakarta.persistence.EnumType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

@RestController
@RequestMapping("/api")
public class CsvController {

    private UserService userService;

    @Autowired
    public CsvController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/upload-csv")
    public ResponseEntity<?> uploadCsv(@RequestParam("file") MultipartFile file) {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
            for (CSVRecord record : records) {
                UserDTO userDto = new UserDTO();
                userDto.setFirstName(record.get("firstName"));
                userDto.setLastName(record.get("lastName"));
                userDto.setEmail(record.get("email"));
                userDto.setUsername(record.get("username"));
                userDto.setMajor(record.get("major"));
                userDto.setIsActive(Boolean.parseBoolean(record.get("isActive")));
            try {
                userDto.setUserType(UserType.parseFromRecord(record.get("userType")));
            } catch (IllegalArgumentException e) {
                // Log the error, skip this record, return an error response TODO
                continue;
            }
                
                userService.saveUserFromDto(userDto);
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process CSV");
        }

        return ResponseEntity.ok().body("CSV processed successfully");
    }
}
