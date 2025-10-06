package com.example.demo.skill.management.service;

import com.example.demo.system.configuration.exception.ResourceNotFoundException;
import com.example.demo.skill.management.model.Skills;
import com.example.demo.skill.management.repository.SkillsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SkillsService {
    @Autowired
    private SkillsRepo skillsRepo;

    public List<Skills> getAllSkills() {
        return skillsRepo.findAll();
    }

    public List<Skills> getSkillById(int id) {
        return Collections.singletonList(skillsRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Skill not found with id" + id)));
    }

    public Skills createSkill(Skills skill) {
        return skillsRepo.save(skill);
    }



    public Skills addSkills(Skills skill){
        return skillsRepo.save(skill);
    }
    public void deleteSkill(int id) {
         skillsRepo.deleteById(id);
    }
}
