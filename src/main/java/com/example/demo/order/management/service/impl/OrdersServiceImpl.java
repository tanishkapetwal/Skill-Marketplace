package com.example.demo.order.management.service.impl;

import com.example.demo.customer.managment.model.Customer;
import com.example.demo.order.management.dto.AllOrderResponse;
import com.example.demo.order.management.dto.CustomerAllOrderDTO;
import com.example.demo.order.management.mapper.OrderMapper;
import com.example.demo.order.management.service.OrdersService;
import com.example.demo.customer.managment.repository.CustomerRepo;
import com.example.demo.skill.management.repository.SkillsListingRepo;
import com.example.demo.system.configuration.email.management.service.impl.SendEmail;
import com.example.demo.system.configuration.exception.ResourceNotFoundException;
import com.example.demo.order.management.model.Orders;
import com.example.demo.order.management.repository.OrdersRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static com.example.demo.order.management.constant.Status.PENDING;

@Service
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {

    private final OrdersRepo ordersRepo;
    private final SkillsListingRepo skillslistingRepo;
    private final SendEmail sendEmail;
    private final CustomerRepo customerRepo;
    private final OrderMapper orderMapper;

    public List<Orders> getAllOrders() {
        return ordersRepo.findAll();
    }

    public List<Orders> getOrderById(int id) {
        return Collections.singletonList(ordersRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id" + id)));
    }

    public void createOrder(int listingId, int userId ,Orders orders){
        orders.setCustomer(customerRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("")));
        orders.setSkillslisting(skillslistingRepo.findById(listingId).orElseThrow(() -> new ResourceNotFoundException("SkillsListing not found with id" + listingId)));
        orders.setOrderDate(LocalDate.now());
        orders.setStatus(PENDING);
        sendEmail.sendEmail(listingId);
        ordersRepo.save(orders);
    }
//    public List<CustomerAllOrderDTO> getallOrders(int id) {
//        Customer customer = customerRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer not found with id" + id));
//
//        return  orderMapper.allOrdersToDTO(customer.getOrder());
//    }
    public Page<AllOrderResponse> getPaginatedProducts( int id, int pageIndex) {
        Pageable page= PageRequest.of(pageIndex,5 , Sort.by("id").descending());
        return orderMapper.allOrdersToDTO(ordersRepo.findByCustomerId(page,id));
//        return ordersrepo.findByCustomerId(page, id).map(order->modelmapper.map(order, AllOrderResponse.class));
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
