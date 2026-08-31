package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class Ordertask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 定时处理超时订单
     */
    @Scheduled(cron = "0 * * * * ?") //每分钟触发一次
    public void processTimeoutOrder(){
        log.info("处理超时订单 : {}", LocalDateTime.now());

        List<Orders> bystatusAndOrderTimeLT = orderMapper
                .getBystatusAndOrderTimeLT(Orders.PENDING_PAYMENT, LocalDateTime.now().minusMinutes(15));
        if(bystatusAndOrderTimeLT != null && bystatusAndOrderTimeLT.size() > 0){
            for (Orders orders : bystatusAndOrderTimeLT) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单超时未支付，系统自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }

    /**
     * 处理一直处于派送中的订单
     */
    @Scheduled(cron="0 0 1 * * ?")//每天凌晨一点触发一次
    public void processDeliveryOrder(){
        log.info("处理一直处于派送中的订单 : {}", LocalDateTime.now());
        List<Orders> bystatusAndOrderTimeLT = orderMapper
                .getBystatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, LocalDateTime.now().minusMinutes(60));
        if(bystatusAndOrderTimeLT != null && bystatusAndOrderTimeLT.size() > 0){
            for (Orders orders : bystatusAndOrderTimeLT) {
                orders.setStatus(Orders.COMPLETED);
                orders.setDeliveryTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }
}
