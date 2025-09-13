package com.example.demo.service;


import com.example.demo.dto.RegisterCustomerDto;
import com.example.demo.model.Skills;
import com.example.demo.model.User;
import com.example.demo.model.type.Role;
import com.example.demo.repository.SkillsRepo;
import com.example.demo.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final SkillsRepo skillsRepo;
    public void deleteUser(int id) {
        userRepo.deleteById(id);
    }
    public User signupAdmin(RegisterCustomerDto input) {

        User user = new User();
        user.setName(input.getName());
        user.setPhone(input.getPhone());
        user.setEmail(input.getEmail());
        user.setRole(Role.ADMIN);
        user.setPassword(passwordEncoder.encode(input.getPassword()));

        User savedUser = userRepo.save(user);
        return savedUser;
    }

    public Skills updateSkill(int id, Skills skillDetails) {
        return skillsRepo.findById(id).map(skill -> {
            skill.setName(skillDetails.getName());
            skill.setDescription(skillDetails.getDescription());
            return skillsRepo.save(skill);
        }).orElseThrow(() -> new RuntimeException("Skill not found"));
    }
}
