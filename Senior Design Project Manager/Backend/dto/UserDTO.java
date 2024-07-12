package com.iastate.project_matcher.dto;

import com.iastate.project_matcher.constants.Major;
import com.iastate.project_matcher.constants.UserType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private String major;

    private HashSet<UserType> userType;

    private Boolean isActive;
}
