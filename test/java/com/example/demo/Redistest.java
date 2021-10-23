package com.example.demo;

import com.example.demo.util.Redisservice;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class Redistest {
    @Resource
    Redisservice redisService;
    @Test
    public void setValueTest() {
        redisService.setValue("stock", 1L);
    }
    @Test
    public void getValueTest() {
        String stock = redisService.getValue("stock");
        System.out.println(stock);
    }
    @Test
    public void stockValidtorTest(){
        String key = "stock:658689";
        redisService.setValue(key, 10L);
        redisService.stockDeductValidator(key);
    }
}
