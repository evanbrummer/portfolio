package com.iastate.project_matcher.repository;

import com.iastate.project_matcher.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<TeamEntity, Integer> {
}
