package com.example.demo.order.management.rest.controller;

import com.example.demo.order.management.dto.AllOrderResponse;
import com.example.demo.order.management.dto.CreateOrderDTO;

import com.example.demo.order.management.mapper.OrderMapper;
import com.example.demo.order.management.model.Orders;
import com.example.demo.order.management.service.impl.OrdersServiceImpl;
import com.example.demo.system.configuration.security.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class OrdersController {

    private final OrdersServiceImpl ordersService;
    private final OrderMapper orderMapper;
    private final JWTService jwtService;

    @GetMapping("orders/")
    public List<Orders> getAllOrders() {
        return ordersService.getAllOrders();
    }

    @GetMapping("orders/{id}")
    public List<Orders> getOrderById(@PathVariable int id) {
        return ordersService.getOrderById(id);
    }

    @PostMapping("/customer/order/{listingId}")
    public ResponseEntity<Void> createOrder(@RequestHeader("Authorization")String authToken, @PathVariable int listingId, @RequestBody CreateOrderDTO createorderdto, HttpServletRequest request) {
        String token = authToken.split(" ")[1];
        int userId = jwtService.extractUserId(token);
        ordersService.createOrder(listingId, userId, orderMapper.toCreateOrderFromDTO( createorderdto));
        return ResponseEntity.ok().build();
    }

    @GetMapping("customer/orders")
    public Page<AllOrderResponse> getAllProducts(@RequestHeader("Authorization") String token, @RequestParam int page) {
        return ordersService.getPaginatedProducts( jwtService.extractUserId(token.split(" ")[1]),page);
    }


    @PutMapping("orders/{id}")
    public Orders updateOrder(@PathVariable int id, @RequestBody Orders order) {
        return ordersService.updateOrder(id, order);
    }

    @DeleteMapping("orders/{id}")
    public void deleteOrder(@PathVariable int id) {
        ordersService.deleteOrder(id);
    }
}
