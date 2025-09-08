package com.example.demo.controller;

import com.example.demo.model.Seller;
import com.example.demo.model.Skills;
import com.example.demo.service.SkillsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/skills")
public class SkillsController {

    @Autowired
    private SkillsService service;

//    @GetMapping(value={"/", "/{id}"})
//    public List<Skills>  getSkills(@PathVariable(required = false) Integer id){
//        if(id!=null) return service.getSkillsById(id);
//        else return service.getSkills();
//    }
//
//    @PostMapping("/add")
//    public void addSkills(@RequestBody Skills skills){
//        service.addSkills(skills);
//    }

}
