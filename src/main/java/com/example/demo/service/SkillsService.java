package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Skills;
import com.example.demo.repository.SkillsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
