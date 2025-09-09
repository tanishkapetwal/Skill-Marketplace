package com.example.demo.service;

import com.example.demo.dto.AllOrderResponse;
import com.example.demo.dto.CreateOrderDTO;
import com.example.demo.dto.CustomerDTO;
import com.example.demo.model.Customer;
import com.example.demo.model.Orders;
import com.example.demo.model.Skills;
import com.example.demo.model.SkillsListing;

import com.example.demo.repository.CustomerRepo;
import com.example.demo.repository.OrdersRepo;
import com.example.demo.repository.SkillsListingRepo;
import com.example.demo.repository.SkillsRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static com.example.demo.model.type.Status.PENDING;

@Service
public class CustomerService {

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
    public List<Customer> getCustomers() {
        return customerrepo.findAll();

    }

    public Customer getCustomerbyId(Integer id) {
        return customerrepo.findById(id).orElse(new Customer());
    }

    public Customer addCustomers(CustomerDTO customer) {
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

    public List<Skills> getallskills() {
        return skillsrepo.findAll();
    }

    public Skills getallskillsbyId(Integer id) {
        return skillsrepo.findById(id).orElseThrow(RuntimeException::new);
    }

    public void createOrder(int custid, int listingId, CreateOrderDTO createOrderDTO){
        Orders orders = new Orders();

        orders =   modelmapper.map(createOrderDTO, Orders.class);
        orders.setCustomer(customerrepo.findById(custid).orElseThrow(RuntimeException::new));
        orders.setSkillsListing((SkillsListing) Collections.singletonList(skillslistingrepo.findById(listingId).orElseThrow(RuntimeException::new)));
        orders.setOrderDate(LocalDate.now());
        orders.setStatus(PENDING);
        ordersrepo.save(orders);
    }


    public List<AllOrderResponse> getallOrders(int id) {
        Customer customer=customerrepo.findById(id).orElse(null);

        List<AllOrderResponse> orderResponseList = customer.getOrder().stream()
                                                    .map(order -> modelmapper.map(order,AllOrderResponse.class)).toList();

        return orderResponseList;
    }
}
