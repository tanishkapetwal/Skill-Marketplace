package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Orders;
import com.example.demo.repository.OrdersRepo;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class OrdersService {

    private final OrdersRepo ordersRepo;

    public OrdersService(OrdersRepo ordersRepo) {
        this.ordersRepo = ordersRepo;
    }

    public List<Orders> getAllOrders() {
        return ordersRepo.findAll();
    }

    public List<Orders> getOrderById(int id) {
        return Collections.singletonList(ordersRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found with id" + id)));
    }

    public Orders createOrder(Orders order) {
        return ordersRepo.save(order);
    }

    public Orders updateOrder(int id, Orders orderDetails) {
        return ordersRepo.findById(id).map(order -> {
            order.setAppointmentStart(orderDetails.getAppointmentStart());
            order.setAppointmentEnd(orderDetails.getAppointmentEnd());
            order.setOrderRating(orderDetails.getOrderRating());
            order.setStatus(orderDetails.getStatus());
            order.setOrderDate(orderDetails.getOrderDate());
            order.setCustomer(orderDetails.getCustomer());
            order.setSkillslisting(orderDetails.getSkillslisting());
            return ordersRepo.save(order);
        }).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public void deleteOrder(int id) {
        ordersRepo.deleteById(id);
    }
}
