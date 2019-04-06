package com.yanghx.rabbitmqspringboot;

import com.yanghx.rabbitmqspringboot.entity.Order;
import com.yanghx.rabbitmqspringboot.producer.RabbitSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqSpringbootProducerApplicationTests {

    @Test
    public void contextLoads() {
    }


    @Autowired
    private RabbitSender rabbitSender;


    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * 消息发送成功。但是返回noAck
     */
    @Test
    public void testSender1() throws Exception {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("number", "123456");
        properties.put("send_time", simpleDateFormat.format(new Date()));
        rabbitSender.send("Hello RabbitMQ For Springboot !", properties);

        Thread.sleep(2000);
        System.out.println("睡眠结束");
    }


    @Test
    public void testSender2() throws Exception {
        Order order = new Order("001", "第一个订单");
        rabbitSender.sendOrder(order);

        Thread.sleep(2000);
        System.out.println("睡眠结束");
    }


}
