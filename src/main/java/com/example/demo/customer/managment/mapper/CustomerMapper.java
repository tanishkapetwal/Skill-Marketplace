package com.example.demo.customer.managment.mapper;

import com.example.demo.customer.managment.model.Customer;
import com.example.demo.system.configuration.dto.CustomerResponseDto;
import com.example.demo.system.configuration.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerMapper {
    private final ModelMapper modelMapper;
    private final UserRepo userRepo;
    public CustomerResponseDto customerToCustomerResponseDto(int userId){
        return modelMapper.map(userRepo.findById(userId), CustomerResponseDto.class);
    }
    public List<CustomerResponseDto> allCustomersListToCustomerResponseDto(List<Customer> customers){
        return customers.stream().map(customer -> modelMapper.map(customer, CustomerResponseDto.class)).toList();
    }
}
