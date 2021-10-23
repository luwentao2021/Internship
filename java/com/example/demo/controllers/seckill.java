package com.example.demo.controllers;


//import ch.qos.logback.classic.Logger;
//import com.example.demo.db.dao.SeckillActivityDao;
//import com.example.demo.db.dao.SeckillCommodityDao;
//import com.example.demo.db.po.SeckillActivity;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.servlet.ModelAndView;
//import com.example.demo.db.po.Order;
//import com.example.demo.db.dao.OrderDao;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.example.demo.db.dao.OrderDao;
import com.example.demo.db.dao.SeckillActivityDao;
import com.example.demo.db.dao.SeckillCommodityDao;
import com.example.demo.db.po.Order;
import com.example.demo.db.po.SeckillActivity;
import com.example.demo.db.po.SeckillCommodity;
import com.example.demo.service.SeckillActivityService;
import com.example.demo.util.Redisservice;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Slf4j
@Controller
public class seckill {

    @Autowired
    private SeckillActivityDao seckillActivityDao;

    @Autowired
    private SeckillCommodityDao seckillCommodityDao;

    @Autowired
    private OrderDao orderDao;

//    @Autowired
//    SeckillActivityService seckillActivityService;
//
//    @Autowired
//    private OrderDao orderDao;


    @RequestMapping("/addSeckillActivity")
    public String addSeckillActivity() {
        return "add_activity";
    }

    @RequestMapping("/SeckillActivitycheck")
    public String SeckillActivitycheck() {
        return "check_activity";
    }

    @RequestMapping("/SeckillActivitydelete")
    public String SeckillActivitydelete() {
        return "delete_activity";
    }

    @RequestMapping("/SeckillActivityActiondelete")
    public String SeckillActivityActiondelete(@RequestParam("id") long id) {
        seckillActivityDao.deleterow(id);
        return "done_delete";
    }

    @RequestMapping("/SeckillActivityupdate")
    public String SeckillActivityupdate() {
        return "update_activity";
    }

    @RequestMapping("/SeckillActivityActionupdate")
    public String SeckillActivityActionUpdate(
            @RequestParam("id") long id,
            @RequestParam("name") String name,
            @RequestParam("commodityId") long commodityId,
            @RequestParam("seckillPrice") BigDecimal seckillPrice,
            @RequestParam("oldPrice") BigDecimal oldPrice,
            @RequestParam("seckillNumber") long seckillNumber,
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime) throws ParseException {
            startTime = startTime.substring(0, 10) +  startTime.substring(11);
        endTime = endTime.substring(0, 10) +  endTime.substring(11);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddhh:mm");
        SeckillActivity seckillActivity = new SeckillActivity();
        seckillActivity.setId(id);
        seckillActivity.setName(name);
        seckillActivity.setCommodityId(commodityId);
        seckillActivity.setSeckillPrice(seckillPrice);
        seckillActivity.setOldPrice(oldPrice);
        seckillActivity.setTotalStock(seckillNumber);
        seckillActivity.setAvailableStock(new Integer("" + seckillNumber));
        seckillActivity.setLockStock(0L);
        seckillActivity.setActivityStatus(1);
        seckillActivity.setStartTime(format.parse(startTime));
        seckillActivity.setEndTime(format.parse(endTime));
        seckillActivityDao.updateSeckillActivity(seckillActivity);
        return "done_update";
    }


    @RequestMapping("/SeckillActivityActioncheck")
    public String SeckillActivityActioncheck(Map<String, Object> resultMap,  @RequestParam("commodityId") long commodityId) {
//        try (Entry entry = SphU.entry("seckills")) {
            SeckillActivity seckillActivities = seckillActivityDao.checkSeckillActivityBycustomerid(commodityId);
            resultMap.put("seckillActivities", seckillActivities);
            return "check_done";
    }


    @RequestMapping("/addSeckillActivityAction")
    public String addSeckillActivityAction(
            @RequestParam("name") String name,
            @RequestParam("commodityId") long commodityId,
            @RequestParam("seckillPrice") BigDecimal seckillPrice,
            @RequestParam("oldPrice") BigDecimal oldPrice,
            @RequestParam("seckillNumber") long seckillNumber,
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime,
            Map<String, Object> resultMap
    ) throws ParseException {
        startTime = startTime.substring(0, 10) + startTime.substring(11);
        endTime = endTime.substring(0, 10) + endTime.substring(11);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddhh:mm");
        SeckillActivity seckillActivity = new SeckillActivity();
        seckillActivity.setName(name);
        seckillActivity.setCommodityId(commodityId);
        seckillActivity.setSeckillPrice(seckillPrice);
        seckillActivity.setOldPrice(oldPrice);
        seckillActivity.setTotalStock(seckillNumber);
        seckillActivity.setAvailableStock(new Integer("" + seckillNumber));
        seckillActivity.setLockStock(0L);
        seckillActivity.setActivityStatus(1);
        seckillActivity.setStartTime(format.parse(startTime));
        seckillActivity.setEndTime(format.parse(endTime));
        seckillActivityDao.inertSeckillActivity(seckillActivity);
        resultMap.put("seckillActivity", seckillActivity);
        return "add_success";
    }
    @RequestMapping("/seckill/orderQuery/{userId}/{orderNo}")
    public ModelAndView orderQuery(@PathVariable String orderNo, @PathVariable String userId) {
        log.info("订单查询，订单号：" + orderNo);
        Order order = orderDao.queryOrder(orderNo);
        ModelAndView modelAndView = new ModelAndView();
        if (order != null) {
            modelAndView.setViewName("order");
            modelAndView.addObject("order", order);
            SeckillActivity seckillActivity =
                    seckillActivityDao.querySeckillActivityById(order.getSeckillActivityId());
            modelAndView.addObject("seckillActivity", seckillActivity);
        } else {
            modelAndView.setViewName("order_wait");
        }
        return modelAndView;
    }

}


