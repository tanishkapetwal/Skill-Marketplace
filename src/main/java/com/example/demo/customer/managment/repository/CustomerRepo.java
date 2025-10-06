package com.example.demo.customer.managment.repository;

import com.example.demo.customer.managment.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Integer> {
   Optional<Customer> findByUserId(int user_id);
//    Optional<List<Customer>> findAllByEmail(String Email);
}
