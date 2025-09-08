package com.example.demo.controller;

import com.example.demo.dto.AllOrderResponse;
import com.example.demo.dto.CreateOrderDTO;
import com.example.demo.dto.CustomerDTO;
import com.example.demo.model.Customer;
import com.example.demo.model.Orders;
import com.example.demo.model.Skills;
import com.example.demo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService service;

    @GetMapping("")
    public List<Customer> getCustomers(Model model){
       return service.getCustomers();
    }

    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable Integer id){
        return service.getCustomerbyId(id);
    }

    @GetMapping("/skills")
    public ResponseEntity<List<Skills>> getallskills(){
        return new ResponseEntity<>(service.getallskills(), HttpStatus.OK);
    }

    @GetMapping("/skills/{id}")
    public ResponseEntity<Skills> getallskillsbyId(@PathVariable Integer id){
        return new ResponseEntity<>(service.getallskillsbyId(id), HttpStatus.OK);
    }


    @PostMapping("/{id}/order/{listingId}")
    public ResponseEntity<Orders> createOrder(@PathVariable int id, @PathVariable int listingId, @RequestBody CreateOrderDTO createorderdto){
        service.createOrder(id,listingId,createorderdto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/all-orders")
    public ResponseEntity<List<AllOrderResponse>> getallOrders(@PathVariable int id){
        return new ResponseEntity<>(service.getallOrders(id), HttpStatus.OK);
    }
    @PostMapping("/add")
    public ResponseEntity<Customer> addCustomers(@RequestBody CustomerDTO customer){
        return new ResponseEntity<>(service.addCustomers(customer), HttpStatus.OK) ;
    }

    @DeleteMapping(value = {"/delete/{id}"})
    public ResponseEntity<Void>  deleteCustomer(@PathVariable Integer id){
            service.deleteCustomer(id);
            return ResponseEntity.ok().build();

    }
}
