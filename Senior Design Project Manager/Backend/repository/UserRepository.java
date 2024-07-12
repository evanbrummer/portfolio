package com.iastate.project_matcher.repository;

import com.iastate.project_matcher.entity.UserEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    boolean existsCurrentUserByUsername(String username);
}
