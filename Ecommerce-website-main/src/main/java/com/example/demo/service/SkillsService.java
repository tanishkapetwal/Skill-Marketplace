package com.example.demo.service;

import com.example.demo.model.Skills;
import com.example.demo.repository.SkillsRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SkillsService {

    private final SkillsRepo skillsRepo;

    public SkillsService(SkillsRepo skillsRepo) {
        this.skillsRepo = skillsRepo;
    }

    public List<Skills> getAllSkills() {
        return skillsRepo.findAll();
    }

    public Optional<Skills> getSkillById(int id) {
        return skillsRepo.findById(id);
    }

    public Skills createSkill(Skills skill) {
        return skillsRepo.save(skill);
    }

    public Skills updateSkill(int id, Skills skillDetails) {
        return skillsRepo.findById(id).map(skill -> {
            skill.setName(skillDetails.getName());
            skill.setDescription(skillDetails.getDescription());
            return skillsRepo.save(skill);
        }).orElseThrow(() -> new RuntimeException("Skill not found"));
    }

    public void deleteSkill(int id) {
        skillsRepo.deleteById(id);
    }
}
