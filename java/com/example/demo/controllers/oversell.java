package com.example.demo.controllers;

import com.example.demo.service.SeckillActivityService;
import com.example.demo.service.SeckillOverSellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;
@Controller
public class oversell {

    @Autowired
    private SeckillOverSellService seckillOverSellService;

    @Autowired
    private SeckillActivityService seckillActivityService;

    @ResponseBody
    @RequestMapping("/seckill/{seckillActivityId}")
    public String seckil(@PathVariable long seckillActivityId){
        return seckillOverSellService.processSeckill(seckillActivityId);
    }
    @ResponseBody
    @RequestMapping("/seckilll/{seckillActivityId}")
    public String seckillCommodity(@PathVariable long seckillActivityId) {
        boolean stockValidateResult =
                seckillActivityService.seckillStockValidator(seckillActivityId);
        return stockValidateResult ? "恭喜你秒杀成功" : "商品已经售完，下次再来";
    }
}
