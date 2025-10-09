package com.example.demo.system.configuration.repository;

import com.example.demo.system.configuration.model.User;
import com.example.demo.system.configuration.constant.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

     Optional<User> findByEmail(String email);


     List<User> findByRole(Role role);

     Optional<User> findBySellerId(int id);
}
