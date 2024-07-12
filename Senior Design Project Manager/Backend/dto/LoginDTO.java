package com.iastate.project_matcher.dto;

import com.iastate.project_matcher.constants.Major;
import com.iastate.project_matcher.constants.UserType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    private String email;
    private String password;

}
