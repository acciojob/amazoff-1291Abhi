package com.driver;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    public void addOrder(Order order) {
        orderRepository.addOrder(order);
    }

    public void addPartner(String partnerId) {
        orderRepository.addPartner(partnerId);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        orderRepository.addOrderPartnerPair(orderId, partnerId);
    }

    public Order getOrderById(String orderId) {
        return orderRepository.getOrderByID(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return orderRepository.getPartnerById(partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        DeliveryPartner partner = getPartnerById(partnerId);
        return partner.getNumberOfOrders();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return orderRepository.getOrdersByPartnerId(partnerId);
    }

    public List<String> getAllOrders() {
        List<String> keys = orderRepository.getAllOrderKey();
        List<String> allOrder = new ArrayList<>();
        for (String key : keys) {
            allOrder.add(getOrderById(key).getId());
        }
        return allOrder;
    }

    public Integer countOfUnassignedOrder() {
        int count = 0;
        List<String> allOrder = getAllOrders();
        List<String> allPartnerId = orderRepository.getAllPartnerId();
        List<String> assignedOrder = new ArrayList<>();
        for (String key : allPartnerId) {
            for (String order : getOrdersByPartnerId(key))
                assignedOrder.add(order);
        }
        return allOrder.size() - assignedOrder.size();
    }

    public Integer countLeftOrder(String time, String partnerId) {
        int count = 0;
        List<String> orderList = getOrdersByPartnerId(partnerId);
        for (String orderId : orderList) {
            Order order = getOrderById(orderId);
            if (Integer.parseInt(time) > order.getDeliveryTime())
                count++;
        }
        return count;
    }

    public String getLastDeliveryTime(String partnerId) {
        List<String> orderList = getOrdersByPartnerId(partnerId);
        int maxTime = Integer.MIN_VALUE;
        for (String orderId : orderList) {
            Order order = getOrderById(orderId);
            if (order.getDeliveryTime() > maxTime)
                maxTime = order.getDeliveryTime();
        }
        return maxTime + "";
    }

    public void deletePartnerByID(String partnerId) {
        orderRepository.deletePartnerByID(partnerId);
    }

    public void deleteOrderById(String orderId) {
        List<String> pairOrder=orderRepository.getAllPartnerId();
        orderRepository.deleteOrderByOrderID(orderId);
        for(String partnerId:pairOrder){
            for(String order:orderRepository.getOrdersByPartnerId(partnerId))
            {
                if(order.equals(orderId)){
                    orderRepository.deleteOrderByOrderID(orderId,partnerId);
                    return;
                }
            }
        }
    }
}
