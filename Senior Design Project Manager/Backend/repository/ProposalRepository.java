package com.iastate.project_matcher.repository;

import com.iastate.project_matcher.entity.ProposalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProposalRepository extends JpaRepository<ProposalEntity, Integer> {
}
