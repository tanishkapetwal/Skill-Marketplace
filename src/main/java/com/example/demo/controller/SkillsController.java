package com.example.demo.controller;

import com.example.demo.model.Skills;
import com.example.demo.service.SkillsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// @CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/skills")
public class SkillsController {

    @Autowired
    private SkillsService skillsService;


    @GetMapping
    public List<Skills> getAllSkills() {
        return skillsService.getAllSkills();
    }

    @GetMapping("/{id}")
    public List<Skills> getSkillById(@PathVariable int id) {
        return skillsService.getSkillById(id);
    }
}
