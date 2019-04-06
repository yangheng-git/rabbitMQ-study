package com.yanghx.rabbitmqspringcloutstreamproducer;

import com.yanghx.rabbitmqspringcloutstreamproducer.steam.RabbitMqSender;
import org.apache.http.client.utils.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqSpringcloutstreamProducerApplicationTests {

    @Test
    public void contextLoads() {
    }


    @Autowired
    private RabbitMqSender rabbitmqSender;


    @Test
    public void sendMessageTest() {
        for (int i = 0; i < 1; i++) {

            HashMap<String, Object> properties = new HashMap<>(2);
            properties.put("SERIAL_NUMBER", "12345");
            properties.put("BANK_NUMBER", "abc");
            properties.put("PLAT_SEND_TIME", DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));

            rabbitmqSender.sendMessage("Hello, I am amqp sender num :" + i, properties);
        }
    }
}
