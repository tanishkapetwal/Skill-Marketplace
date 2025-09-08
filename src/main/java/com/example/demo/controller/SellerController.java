package com.example.demo.controller;

import com.example.demo.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.example.demo.model.Seller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("seller")
public class SellerController {

    @Autowired
    private SellerService service;

    @GetMapping(value={"/", "{id}"})
    public List<Seller> getSeller(@PathVariable (required = false) Integer id){
        if(id!= null)return service.getSellerById(id);
        else return service.getSeller();
    }

    @PostMapping("/add")
    public void addSeller(@RequestBody Seller seller){
        service.addSeller(seller);
    }

    @DeleteMapping("/delete")
    public void deleteSeller(@PathVariable int id){
        service.deleteSeller(id);
    }
}
