package com.example.demo.admin.management.mapper;

import com.example.demo.admin.management.service.AdminService;
import com.example.demo.system.configuration.dto.CustomerResponseDto;
import com.example.demo.system.configuration.dto.RegisterCustomerDto;
import com.example.demo.system.configuration.dto.RegisterSellerDto;
import com.example.demo.seller.management.service.SellerService;
import com.example.demo.system.configuration.constant.Role;
import com.example.demo.customer.managment.service.CustomerService;
import com.example.demo.system.configuration.model.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AdminMapper {

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    public User registerCustomerDtoToCustomer(RegisterCustomerDto customer) {

        User user = modelMapper.map(customer, User.class);
        user.setPassword(passwordEncoder.encode(customer.getPassword()));
        user.setRole(Role.CUSTOMER);
        return user;
    }
    public User registerSellerDtoToSeller(RegisterSellerDto seller) {

        User user = modelMapper.map(seller, User.class);
        user.setPassword(passwordEncoder.encode(seller.getPassword()));
        user.setRole(Role.SELLER);
        return user;
    }

    public User registerAdminDtoToAdmin(RegisterCustomerDto admin) {
        User user = modelMapper.map(admin, User.class);
        user.setRole(Role.ADMIN);
        user.setPassword(passwordEncoder.encode(admin.getPassword()));
        return user;
    }

    public List<CustomerResponseDto> allAdminsDto(List<User> byRole) {
        return byRole.stream().map
                (user -> modelMapper.map(user, CustomerResponseDto.class)).toList();
    }
}
