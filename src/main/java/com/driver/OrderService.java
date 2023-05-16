package com.driver;

import io.swagger.models.auth.In;
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
        return orderRepository.getNoOfOrderByPartnerId(partnerId);

    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return orderRepository.getOrdersByPartnerId(partnerId);
    }

    public List<String> getAllOrders() {
        List<String> keys = orderRepository.getAllOrderKey();
//        List<String> allOrder = new ArrayList<>();
//        for (String key : keys) {
//            allOrder.add(getOrderById(key).getId());
//        }
        return keys;
    }

    public Integer countOfUnassignedOrder() {

        List<String> allOrder = getAllOrders();
        int assignedOrder = orderRepository.getNoOfAssignedOrder();
        Integer count=allOrder.size()-assignedOrder;
        return count;
    }

    public Integer countLeftOrder(String time, String partnerId) {
        String[] temp=time.split(":");
        int t=(Integer.parseInt(temp[0])*60)+Integer.parseInt(temp[1]);
        int count = 0;
        List<String> orderList = getOrdersByPartnerId(partnerId);
        for (String orderId : orderList) {
            Order order = getOrderById(orderId);
            if (t > order.getDeliveryTime())
                count++;
        }
        return count;
    }

    public String getLastDeliveryTime(String partnerId) {
        List<String> orderList = getOrdersByPartnerId(partnerId);
        int maxTime = 0;
        for (String orderId : orderList) {
            Order order = getOrderById(orderId);
            if (order.getDeliveryTime() > maxTime)
                maxTime = order.getDeliveryTime();
        }
        int hh=maxTime/60;
        int mm=maxTime%60;
        return hh+":"+mm;
    }

    public void deletePartnerByID(String partnerId) {
        orderRepository.deletePartnerByID(partnerId);
    }

    public void deleteOrderById(String orderId) {
        List<String> pairOrder=orderRepository.getAllPartnerId();
        orderRepository.removeOrderFromOrderPartnerById(orderId);
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
