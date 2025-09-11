package com.example.demo.service;

import com.example.demo.dto.AllOrderResponse;
import com.example.demo.dto.CreateOrderDTO;

import com.example.demo.dto.CustomerResponseDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.dto.*;
import com.example.demo.model.*;

import com.example.demo.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.example.demo.model.type.Status.PENDING;

@Service
public class CustomerService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CustomerRepo customerrepo;
    @Autowired
    private SkillsRepo skillsrepo;
    @Autowired
    private SkillsListingRepo skillslistingrepo;
    @Autowired
    private OrdersRepo ordersrepo;
    @Autowired
    private ModelMapper modelmapper;
    public List<CustomerResponseDto> getCustomers() {
        return (customerrepo.findAll().stream().map
                (customer -> modelmapper.map(customer, CustomerResponseDto.class)).toList());
    }

    public CustomerResponseDto getCustomerbyId(Integer id) {

        return modelmapper.map(userRepo.findById(id), CustomerResponseDto.class);
    }

    public Customer addCustomers(RegisterCustomerDto customer) {
//        Customer cust = Customer.builder().name(customer.getName()).email(customer.getEmail()).
//                phone(customer.getPhone()).password(customer.getPassword()).createdAt(LocalDateTime.now()).build();
//        cust.setName(customer.getName());
//        cust.setEmail(customer.getEmail());
//        cust.setPhone(customer.getPhone());
//        cust.setPassword(customer.getPassword());
//        cust.setCreatedAt();

        Customer cust = modelmapper.map(customer, Customer.class);
        return customerrepo.save(cust);
    }

    public void deleteCustomer(Integer id) {
        customerrepo.deleteById(id);
    }

    public List<SkillsListingDTO> getallskills() {
        return skillslistingrepo.findAll().stream().map(listing->modelmapper.map(listing, SkillsListingDTO.class)).toList();
    }


    public SkillsListing getallskillsbyId(Integer id) {
        return skillslistingrepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Skill not found with id" + id));

    }

    public void createOrder(int custid, int listingId, CreateOrderDTO createOrderDTO){
        Orders orders = new Orders();

        orders =   modelmapper.map(createOrderDTO, Orders.class);
        orders.setCustomer(customerrepo.findById(custid).orElseThrow(() -> new ResourceNotFoundException("Customer not found with id" + custid)));
        orders.setSkillslisting(skillslistingrepo.findById(listingId).orElseThrow(() -> new ResourceNotFoundException("SkillsListing not found with id" + listingId)));
        orders.setOrderDate(LocalDate.now());
        orders.setStatus(PENDING);
        ordersrepo.save(orders);
    }

    public List<AllOrderResponse> getallOrders(int id) {
        User user=userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer not found with id" + id));
        Customer customer = customerrepo.findById(user.getId()).orElseThrow(() -> new ResourceNotFoundException("Customer not found with id" + id));

        List<AllOrderResponse> orderResponseList = customer.getOrder().stream()
                                                    .map(order -> modelmapper.map(order,AllOrderResponse.class)).toList();

        return orderResponseList;
    }
}
