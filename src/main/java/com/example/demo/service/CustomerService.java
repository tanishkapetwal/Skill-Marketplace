package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AllOrderResponse;
import com.example.demo.dto.CreateOrderDTO;
import com.example.demo.dto.CustomerResponseDto;
import com.example.demo.dto.RegisterCustomerDto;
import com.example.demo.dto.SkillsListingDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Customer;
import com.example.demo.model.Orders;
import com.example.demo.model.SkillsListing;
import com.example.demo.model.User;
import static com.example.demo.model.type.Status.PENDING;
import com.example.demo.repository.CustomerRepo;
import com.example.demo.repository.OrdersRepo;
import com.example.demo.repository.SkillsListingRepo;
import com.example.demo.repository.SkillsRepo;
import com.example.demo.repository.UserRepo;

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
        orders.setCustomer(customerrepo.findByUserId(userRepo.findById(custid).orElseThrow(() -> new ResourceNotFoundException("User not found with id" + custid)).getId()).orElseThrow(() -> new ResourceNotFoundException("Customer not found with id" + custid)));
        orders.setSkillslisting(skillslistingrepo.findById(listingId).orElseThrow(() -> new ResourceNotFoundException("SkillsListing not found with id" + listingId)));
        orders.setOrderDate(LocalDate.now());
        orders.setStatus(PENDING);
        ordersrepo.save(orders);
    }

    public List<AllOrderResponse> getallOrders(int id) {
        User user=userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id" + id));
        Customer customer = customerrepo.findByUserId(user.getId()).orElseThrow(() -> new ResourceNotFoundException("Customer not found with id" + id));

        List<AllOrderResponse> orderResponseList = customer.getOrder().stream()
                                                    .map(order -> modelmapper.map(order,AllOrderResponse.class)).toList();

        return orderResponseList;
    }
    @Autowired
    private OrdersRepo ordersRepo;
    @Autowired
    private RatingService ratingService;

    public Orders findOrderById(int orderId) {
        return ordersRepo.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Orders saveOrder(Orders order) {
        return ordersRepo.save(order);
    }

    public void updateRatings(int orderId) {
        ratingService.updateRatingsAfterOrder(orderId);
    }

}
