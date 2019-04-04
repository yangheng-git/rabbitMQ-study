package com.yanghx.rabbitmqspring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqSpringApplicationTests {

    @Test
    public void contextLoads() {
    }


    @Resource
    private RabbitAdmin rabbitAdmin;

    @Test
    public void testAdmin() {
        rabbitAdmin.declareExchange(new DirectExchange("test.direct", false, false));

        rabbitAdmin.declareExchange(new TopicExchange("test.topic", false, false));

        rabbitAdmin.declareExchange(new FanoutExchange("test.fanout", false, false));

        rabbitAdmin.declareQueue(new Queue("test.direct.queue", false));

        rabbitAdmin.declareQueue(new Queue("test.topic.queue", false));

        rabbitAdmin.declareQueue(new Queue("test.fanout.queue", false));


        //绑定的一种形式
        rabbitAdmin.declareBinding(new Binding("test.direct.queue"
                , Binding.DestinationType.QUEUE, "test.direct", "direct", new HashMap<>()));

        //绑定的另一种形式
        rabbitAdmin.declareBinding(BindingBuilder
                //直接创建队列
                .bind(new Queue("test.topic.queue", false))
                //直接创建交换机，建立关联关系
                .to(new TopicExchange("test.topic", false, false))
                //指定路由关系
                .with("user.#"));

        rabbitAdmin.declareBinding(BindingBuilder
                .bind(new Queue("test.fanout.queue", false))
                .to(new FanoutExchange("test.fanout", false, false)));


        //清空队列数据
        rabbitAdmin.purgeQueue("test.topic.queue", false);
        rabbitAdmin.purgeQueue("test_ack_queue", false);


    }


}
