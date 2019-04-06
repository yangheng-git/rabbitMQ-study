package com.yanghx.rabbitmqspring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yanghx.rabbitmqspring.entity.Order;
import com.yanghx.rabbitmqspring.entity.Packaged;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    /**
     * 发送消息。测试转换器和适配器
     * <p>
     * 转换器判断contentType 将字节数组转化为字符串
     * 适配器将数据交给 MessageDelegate 的 consumeMessage 方法进行处理
     */
    @Test
    public void testMessage4Text() {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("text/plan");
        Message message = new Message("mq 消息1234".getBytes(), messageProperties);
        rabbitTemplate.send("topic001", "spring.abc", message);
        rabbitTemplate.send("topic002", "rabbit.abc", message);
    }



    @Test
    public void testSendJsonMessage() throws Exception {

        Order order = new Order();
        order.setId("001");
        order.setName("消息订单");
        order.setContent("描述信息");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(order);
        System.err.println("order 4 json: " + json);

        MessageProperties messageProperties = new MessageProperties();
        //这里注意一定要修改contentType为 application/json
        messageProperties.setContentType("application/json");
        Message message = new Message(json.getBytes(), messageProperties);

        rabbitTemplate.send("topic001", "spring.order", message);
    }



    @Test
    public void testSendJavaMessage() throws Exception {

        Order order = new Order();
        order.setId("001");
        order.setName("订单消息");
        order.setContent("订单描述信息");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(order);
        System.err.println("order 4 json: " + json);

        MessageProperties messageProperties = new MessageProperties();
        //这里注意一定要修改contentType为 application/json
        messageProperties.setContentType("application/json");
        messageProperties.getHeaders().put("__TypeId__", "com.bfxy.spring.entity.Order");
        Message message = new Message(json.getBytes(), messageProperties);

        rabbitTemplate.send("topic001", "spring.order", message);
    }


    @Test
    public void testSendMappingMessage() throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        Order order = new Order();
        order.setId("001");
        order.setName("订单消息");
        order.setContent("订单描述信息");

        String json1 = mapper.writeValueAsString(order);
        System.err.println("order 4 json: " + json1);

        MessageProperties messageProperties1 = new MessageProperties();
        //这里注意一定要修改contentType为 application/json
        messageProperties1.setContentType("application/json");
        messageProperties1.getHeaders().put("__TypeId__", "order");
        Message message1 = new Message(json1.getBytes(), messageProperties1);
        rabbitTemplate.send("topic001", "spring.order", message1);

        Packaged pack = new Packaged();
        pack.setId("002");
        pack.setName("包裹消息");
        pack.setDescription("包裹描述信息");

        String json2 = mapper.writeValueAsString(pack);
        System.err.println("pack 4 json: " + json2);

        MessageProperties messageProperties2 = new MessageProperties();
        //这里注意一定要修改contentType为 application/json
        messageProperties2.setContentType("application/json");
        messageProperties2.getHeaders().put("__TypeId__", "packaged");
        Message message2 = new Message(json2.getBytes(), messageProperties2);
        rabbitTemplate.send("topic001", "spring.pack", message2);
    }



    @Test
    public void testSendExtConverterMessage() throws Exception {
//			byte[] body = Files.readAllBytes(Paths.get("d:/002_books", "picture.png"));
//			MessageProperties messageProperties = new MessageProperties();
//			messageProperties.setContentType("image/png");
//			messageProperties.getHeaders().put("extName", "png");
//			Message message = new Message(body, messageProperties);
//			rabbitTemplate.send("", "image_queue", message);

        byte[] body = Files.readAllBytes(Paths.get("d:/002_books", "mysql.pdf"));
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/pdf");
        Message message = new Message(body, messageProperties);
        rabbitTemplate.send("", "pdf_queue", message);
    }



}
