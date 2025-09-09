package com.example.demo.controller;

import com.example.demo.model.Skills;
import com.example.demo.service.SkillsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/skills")
public class SkillsController {

    private final SkillsService skillsService;

    public SkillsController(SkillsService skillsService) {
        this.skillsService = skillsService;
    }

    @GetMapping
    public List<Skills> getAllSkills() {
        return skillsService.getAllSkills();
    }

    @GetMapping("/{id}")
    public Optional<Skills> getSkillById(@PathVariable int id) {
        return skillsService.getSkillById(id);
    }

    @PostMapping
    public Skills createSkill(@RequestBody Skills skill) {
        return skillsService.createSkill(skill);
    }

    @PutMapping("/{id}")
    public Skills updateSkill(@PathVariable int id, @RequestBody Skills skill) {
        return skillsService.updateSkill(id, skill);
    }

    @DeleteMapping("/{id}")
    public void deleteSkill(@PathVariable int id) {
        skillsService.deleteSkill(id);
    }
}
