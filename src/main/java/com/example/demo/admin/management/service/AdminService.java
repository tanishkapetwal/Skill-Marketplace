package com.example.demo.admin.management.service;

import com.example.demo.admin.management.mapper.AdminMapper;
import com.example.demo.system.configuration.dto.CustomerResponseDto;
import com.example.demo.skill.management.model.Skills;
import com.example.demo.system.configuration.model.User;
import com.example.demo.system.configuration.constant.Role;
import com.example.demo.customer.managment.repository.CustomerRepo;
import com.example.demo.seller.management.repository.SellerRepo;
import com.example.demo.skill.management.repository.SkillsRepo;
import com.example.demo.system.configuration.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminService {
    @Autowired
    private ModelMapper modelMapper;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final SkillsRepo skillsRepo;
    private final CustomerRepo customerRepo;
    private final SellerRepo sellerRepo;
    private final AdminMapper adminMapper;

    public void deleteCustomer(int id) {
        int userId = customerRepo.findById(id).orElseThrow().getUser().getId();
        userRepo.deleteById(userId);
    }
    public void deleteSeller(int id) {
        int userId = sellerRepo.findById(id).orElseThrow().getUser().getId();
        userRepo.deleteById(userId);
    }
    public User signupAdmin(User user) {
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

    public List<CustomerResponseDto> getAdmins() {
        return adminMapper.allAdminsDto(userRepo.findByRole(Role.ADMIN));
    }
}
