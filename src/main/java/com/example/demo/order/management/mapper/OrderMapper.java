package com.example.demo.order.management.mapper;

import com.example.demo.order.management.dto.AllOrderResponse;
import com.example.demo.order.management.dto.CreateOrderDTO;
import com.example.demo.order.management.dto.CustomerAllOrderDTO;
import com.example.demo.order.management.model.Orders;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderMapper {

private final ModelMapper modelMapper;

    public Orders toCreateOrderFromDTO(CreateOrderDTO createorderdto) {
        if(createorderdto == null){
            return null;
        }
        return modelMapper.map(createorderdto, Orders.class);
    }

    public Page<AllOrderResponse> allOrdersToDTO(Page<Orders> orders) {
      return orders
                .map(order -> modelMapper.map(order, AllOrderResponse.class));

    }
}
