package com.iastate.project_matcher.repository;

import com.iastate.project_matcher.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Integer> {
    boolean existsCurrentProjectByProjectName(String projectName);
}
