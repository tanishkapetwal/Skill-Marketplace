package com.example.demo.order.management.service;

import com.example.demo.order.management.model.Orders;
import java.util.List;

public interface OrdersService {

     List<Orders> getOrderById(int id);
     void createOrder( int listingId, int userId, Orders orders);
     Orders updateOrder(int id, Orders orderDetails);
     void deleteOrder(int id);
     List<Orders> getAllOrders();
}
