package com.iastate.project_matcher.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {

    private String projectName;

    private String projectDescription;

    private Boolean isAvailable;

    private Boolean ee;

    private Boolean se;

    private Boolean cpre;

    private Boolean cyber;
}
