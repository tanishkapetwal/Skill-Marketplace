package com.example.demo.customer.managment.service;

import java.util.List;

import com.example.demo.customer.managment.mapper.CustomerMapper;
import com.example.demo.customer.managment.model.Customer;
import com.example.demo.order.management.model.Orders;
import com.example.demo.rating.management.service.RatingService;
import com.example.demo.skill.management.dto.SkillsListingDTO;
import com.example.demo.skill.management.mapper.SkillMapper;
import com.example.demo.skill.management.model.SkillsListing;
import com.example.demo.system.configuration.dto.CustomerResponseDto;
import com.example.demo.system.configuration.email.management.service.impl.EmailServiceImpl;
import com.example.demo.system.configuration.exception.ResourceNotFoundException;
import com.example.demo.system.configuration.model.User;
import com.example.demo.system.configuration.service.impl.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import com.example.demo.customer.managment.repository.CustomerRepo;
import com.example.demo.order.management.repository.OrdersRepo;
import com.example.demo.skill.management.repository.SkillsListingRepo;
import com.example.demo.skill.management.repository.SkillsRepo;
import com.example.demo.system.configuration.repository.UserRepo;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final UserRepo userRepo;

    private final CustomerRepo customerrepo;
    private final SkillsRepo skillsrepo;
    private final SkillsListingRepo skillslistingrepo;
    private final OrdersRepo ordersrepo;
    private final ModelMapper modelmapper;
    private final OrdersRepo ordersRepo;
    private final RatingService ratingService;
    private final EmailServiceImpl emailService;
    private final AuthenticationService authenticationService;
    private final CustomerMapper customerMapper;
    private final SkillMapper skillMapper;
    public List<CustomerResponseDto> getCustomers() {
        return customerMapper.allCustomersListToCustomerResponseDto(customerrepo.findAll());
    }

    public CustomerResponseDto getCustomerbyId(Integer id) {
        int userId = customerrepo.findById(id).orElseThrow().getUser().getId();

        return customerMapper.customerToCustomerResponseDto(userId);
    }

    public User addCustomers(User user) {
        Customer customer1 = customerrepo.save(Customer.builder().user(user).build());
        user.setCustomer(customer1);
        return userRepo.save(user);
    }

    public void deleteCustomer(Integer id) {
        customerrepo.deleteById(id);
    }


    public Page<SkillsListingDTO> getallskills(int pageIndex) {
        Pageable page= PageRequest.of(pageIndex,6 , Sort.by("id").descending());
        return skillMapper.allSkillsListingToSkillsListingDto(page, skillslistingrepo.findAll(page));
//        return skillslistingrepo.findAll(page).map(skills->modelmapper.map(skills, SkillsListingDTO.class));
    }
    public SkillsListingDTO getallskillsbyId(Integer id) {
        return skillMapper.skillListingDtoById(skillslistingrepo.findById(id).
                orElseThrow(()->new ResourceNotFoundException("Skill List not found by this Id")));
//        return;

    }

    public Orders findOrderById(int orderId) {
        return ordersRepo.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public String saveOrder(int orderId, int customerId, int ratingValue) {
        Orders order = this.findOrderById(orderId);

        if (order.getCustomer().getId() != customerId) {
            return "You can only rate your own orders";
        }

        order.setOrderRating(ratingValue);

        // Update product & seller averages
        this.updateRatings(orderId);
        ordersRepo.save(order);
        SkillsListing skillsListing=skillslistingrepo.findById(order.getSkillslisting().getId()).orElseThrow(RuntimeException::new);
        skillsListing.setRatingCount(skillsListing.getRatingCount()+1);
        skillslistingrepo.save(skillsListing);
        return "Sucessfully submiited rating";
    }


    public void updateRatings(int orderId) {
        ratingService.updateRatingsAfterOrder(orderId);
    }

}
