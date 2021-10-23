package com.example.demo;
import com.example.demo.mq.RocketMQService;
import com.example.demo.service.SeckillActivityService;
import org.junit.jupiter.api.Test;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
public class MQTest {
    @Autowired
    private RocketMQService rocketMQService;
    @Test
    public void sendMQTest() throws Exception {
        rocketMQService.sendMessage("test-jiuzhang","hello world"+ new Date().toString());
    }
}