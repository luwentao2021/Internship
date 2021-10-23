package com.example.demo.service;

import com.example.demo.mq.RocketMQService;
import com.example.demo.util.SnowFlake;
import com.alibaba.fastjson.JSON;
import com.example.demo.db.dao.OrderDao;
import com.example.demo.db.dao.SeckillActivityDao;
import com.example.demo.db.dao.SeckillCommodityDao;
import com.example.demo.db.po.Order;
import com.example.demo.db.po.SeckillActivity;
import com.example.demo.db.po.SeckillCommodity;
import com.example.demo.util.Redisservice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SeckillActivityService {
    @Autowired
    private Redisservice redisservice;

    @Autowired
    private SeckillActivityDao seckillActivityDao;

    @Autowired
    private RocketMQService rocketMQService;

    public boolean seckillStockValidator(long activityId){
        String key = "stock:"+activityId;
        return redisservice.stockDeductValidator(key);
    }
    /**
     * datacenterId; 数据中⼼
     * machineId; 机器标识
     * 在分布式环境中可以从机器配置上读取
     * 单机开发环境中先写死
     */
    private SnowFlake snowFlake = new SnowFlake(1, 1);
    /**
     * 创建订单
     */
//    public void createOrder(long seckillActivityId) {
//        /*
//         * 1.创建订单
//         */
//        SeckillActivity seckillActivity = seckillActivityDao.querySeckillActivityById(seckillActivityId);
//        Order order = new Order();
////采⽤雪花算法⽣成订单ID
//        order.setOrderNo(String.valueOf(snowFlake.nextId()));
////订单状态 0:已创建，1：已⽀付，3:已发货，4：已收货，99：订单关闭
//        order.setOrderStatus(0);
//        order.setSeckillActivityId(seckillActivity.getId());
//        order.setUserId("test");
//        order.setOrderAmount(seckillActivity.getSeckillPrice().longValue());
//    }
    public Order createOrder(long seckillActivityId,long userId) throws
            Exception {
        /*
         * 1.创建订单
         */
        SeckillActivity seckillActivity =
                seckillActivityDao.querySeckillActivityById(seckillActivityId);
        Order order = new Order();
//采⽤雪花算法⽣成订单ID
        order.setOrderNo(String.valueOf(snowFlake.nextId()));
        order.setSeckillActivityId(seckillActivity.getId());
        order.setUserId(userId);
        order.setOrderAmount(seckillActivity.getSeckillPrice());
        /*
         *2.发送创建订单消息
         */
        rocketMQService.sendMessage("seckill_order", JSON.toJSONString(order));
/*
* 3.发送订单付款状态校验消息
* 开源RocketMQ⽀持延迟消息，但是不⽀持秒级精度。默认⽀持18个level的延迟消息，这
是通过broker端的messageDelayLevel配置项确定的，如下：
* messageDelayLevel=1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m
30m 1h 2h
*/
        rocketMQService.sendDelayMessage("pay_check",JSON.toJSONString(order),5);
        return order;
    }
}
