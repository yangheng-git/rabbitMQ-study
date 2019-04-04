package com.yanghx.rabbitmqspring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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


    @Resource
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendMessage() {
        //1.创建消息
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.getHeaders().put("desc", "信息描述。。。。");
        messageProperties.getHeaders().put("type", "自定义消息类型。。。");
        Message message = new Message("Hello RabbitMQ".getBytes(), messageProperties);

        rabbitTemplate.convertAndSend("topic001", "spring.amqp", message, message1 -> {
            System.out.println("-----添加额外的设置。都会发送到消息端-----");
            //同名的参数会覆盖
            message1.getMessageProperties().getHeaders().put("desc", "额外修改的信息描述");
            message1.getMessageProperties().getHeaders().put("attr", "额外新加的属性");
            return message1;
        });
    }


    /**
     * rabbitTemplate 发送消息的方式有很多。用法灵活
     */
    @Test
    public void testSendMessage2() {
        //1 创建消息
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("test/plain");
        Message message = new Message("mq 消息1234".getBytes(), messageProperties);

        rabbitTemplate.send("topic001", "spring.abc", message);

        rabbitTemplate.convertAndSend("topic001", "spring.amqp", "hello object message send!");

        rabbitTemplate.convertAndSend("topic002", "rabbit.abc", "hello object message send!");


    }


}
