package com.example.demo.skill.management.repository;

import com.example.demo.skill.management.model.Skills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillsRepo extends JpaRepository<Skills, Integer> {
}
