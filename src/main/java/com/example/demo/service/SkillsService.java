package com.example.demo.service;

import com.example.demo.model.Skills;
import com.example.demo.repository.SkillsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service

public class SkillsService {

    @Autowired
    SkillsRepo repo;
    public List<Skills> getSkills() {
        return  repo.findAll();
    }
//    @Bean
//    public List<Skills> getSkillsById(int id){
//        return repo.findAllById(Collections.singleton(id));
//    }

//    public void addSkills(Skills skills) {
//        repo.save(skills);
//    }

}
