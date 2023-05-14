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
    private Map<String,List<String>> pairMap=new HashMap<>();
    public void addOrder(Order order) {
        orderMap.put(order.getId(),order);
    }

    public void addPartner(String partnerId) {
        partnerMap.put(partnerId,new DeliveryPartner(partnerId));
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        if(pairMap.containsKey(partnerId)){
            List<String> list=pairMap.get(partnerId);
            list.add(orderId);
            pairMap.put(partnerId,list);
        }else{
            List<String> list=new ArrayList<>();
            list.add(orderId);
            pairMap.put(partnerId,list);
        }
    }

    public Order getOrderByID(String orderId) {
        return orderMap.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return partnerMap.get(partnerId);
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return pairMap.get(partnerId);
    }

    public List<String> getAllOrderKey() {
        List<String> key=new ArrayList<>();
        for(String keys:orderMap.keySet())
            key.add(keys);
        return key;
    }

    public List<String> getAllPartnerId() {
        List<String> key=new ArrayList<>();
        for(String keys:pairMap.keySet())
            key.add(keys);
        return key;
    }

    public void deletePartnerByID(String partnerId) {
        partnerMap.remove(partnerId);
        pairMap.remove(partnerId);
    }


    public void deleteOrderByOrderID(String orderId,String partnerId) {
        List<String> order=pairMap.get(partnerId);
        List<String> newOrder=new ArrayList<>();
        for(String or:order){
            if(!or.equals(orderId))
                newOrder.add(or);
        }
        pairMap.put(partnerId,newOrder);
    }

    public void deleteOrderByOrderID(String orderId) {
        orderMap.remove(orderId);
    }
}
