package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepository {
    private Map<String,Order> orderMap=new HashMap<>();
    private Map<String,DeliveryPartner> partnerMap=new HashMap<>();
    private Map<String,String> orderPartnerMap=new HashMap<>();
    private Map<String,List<String>> partnerOrderMap=new HashMap<>();

    public void addOrder(Order order) {
        orderMap.put(order.getId(),order);
    }

    public void addPartner(String partnerId) {
        partnerMap.put(partnerId,new DeliveryPartner(partnerId));
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        if(orderMap.containsKey(orderId) && partnerMap.containsKey(partnerId)) {
            orderPartnerMap.put(orderId, partnerId);
            List<String> list = new ArrayList<>();
            if(partnerOrderMap.containsKey(partnerId)) {
                list = partnerOrderMap.get(partnerId);
                list.add(orderId);
                partnerOrderMap.put(partnerId, list);
            } else {

                list.add(orderId);
                partnerOrderMap.put(partnerId, list);
            }
            DeliveryPartner deliveryPartner=new DeliveryPartner(partnerId);
            deliveryPartner.setNumberOfOrders(list.size());
        }
    }

    public Order getOrderByID(String orderId) {
        return orderMap.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return partnerMap.get(partnerId);
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return partnerOrderMap.get(partnerId);
    }

    public List<String> getAllOrderKey() {
        List<String> key=new ArrayList<>();
        for(String keys:orderMap.keySet())
            key.add(keys);
        return key;
    }

    public List<String> getAllPartnerId() {
        List<String> key=new ArrayList<>();
        for(String keys:partnerOrderMap.keySet())
            key.add(keys);
        return key;
    }

    public void deletePartnerByID(String partnerId) {
        partnerMap.remove(partnerId);
        List<String> order=partnerOrderMap.get(partnerId);
        partnerOrderMap.remove(partnerId);
        for(String key:order)
            orderPartnerMap.remove(key);
    }


    public void deleteOrderByOrderID(String orderId,String partnerId) {
        List<String> order=partnerOrderMap.get(partnerId);
        List<String> newOrder=new ArrayList<>();
        for(String or:order){
            if(!or.equals(orderId))
                newOrder.add(or);
        }
        partnerOrderMap.put(partnerId,newOrder);
    }

    public void deleteOrderByOrderID(String orderId) {
        orderMap.remove(orderId);
    }

    public int getNoOfAssignedOrder() {
        return orderPartnerMap.size();
    }

    public void removeOrderFromOrderPartnerById(String orderId) {
        orderPartnerMap.remove(orderId);
    }
}
