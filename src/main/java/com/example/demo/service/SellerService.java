package com.example.demo.service;

import com.example.demo.model.Seller;
import com.example.demo.repository.SellerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SellerService {

    @Autowired
    private SellerRepo repo;
    public List<Seller> getSeller() {
        return repo.findAll();
    }

    public List<Seller> getSellerById(Integer id) {
        return repo.findAllById(Collections.singleton(id));
    }

    public void addSeller(Seller seller) {
        repo.save(seller);
    }

    public void deleteSeller(int id) {
        repo.deleteById(id);
    }
}
