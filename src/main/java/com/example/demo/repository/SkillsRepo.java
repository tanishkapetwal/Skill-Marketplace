package com.example.demo.repository;

import com.example.demo.model.Skills;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillsRepo extends JpaRepository<Skills, Integer> {
}
