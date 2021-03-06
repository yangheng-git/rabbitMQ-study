package com.yanghx.rabbitmqspring;

import com.rabbitmq.client.Channel;
import com.yanghx.rabbitmqspring.adapter.MessageDelegate;
import com.yanghx.rabbitmqspring.convert.ImageMessageConverter;
import com.yanghx.rabbitmqspring.convert.PDFMessageConverter;
import com.yanghx.rabbitmqspring.convert.TextMessageConverter;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * rabbitMQ配置类
 *
 * @author yangHX
 * createTime  2019/4/3 23:42
 */

@Configuration
@ComponentScan({"com.yanghx.rabbitmqspring.*"})
public class RabbitMQConfig {


    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost("132.232.33.80");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        return connectionFactory;
    }


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }


    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    /**
     * 针对消费者配置
     * 1. 设置交换机类型
     * 2. 将队列绑定到交换机
     * FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
     * HeadersExchange ：通过添加属性key-value匹配
     * DirectExchange:按照routingkey分发到指定队列
     * TopicExchange:多关键字匹配
     */
    @Bean
    public TopicExchange exchange001() {
        return new TopicExchange("topic001", true, false);
    }

    @Bean
    public Queue queue001() {
        return new Queue("queue001", true); //队列持久
    }

    @Bean
    public Binding binding001() {
        return BindingBuilder.bind(queue001()).to(exchange001()).with("spring.*");
    }

    @Bean
    public TopicExchange exchange002() {
        return new TopicExchange("topic002", true, false);
    }

    @Bean
    public Queue queue002() {
        return new Queue("queue002", true); //队列持久
    }

    @Bean
    public Binding binding002() {
        return BindingBuilder.bind(queue002()).to(exchange002()).with("rabbit.*");
    }

    @Bean
    public Queue queue003() {
        return new Queue("queue003", true); //队列持久
    }

    @Bean
    public Binding binding003() {
        return BindingBuilder.bind(queue003()).to(exchange001()).with("mq.*");
    }

    @Bean
    public Queue queue_image() {
        return new Queue("image_queue", true); //队列持久
    }

    @Bean
    public Queue queue_pdf() {
        return new Queue("pdf_queue", true); //队列持久
    }


    /**
     * 简单消息监听容器
     * 配置完成后。可以在管控台看到消息者信息。 以及消费者标签信息
     *
     * @param connectionFactory 链接工厂
     * @return SimpleMessageListenerContainer
     */
    @Bean
    public SimpleMessageListenerContainer messageContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(connectionFactory);
        //设置要监听的队列
        simpleMessageListenerContainer.setQueues(queue001(), queue002(), queue003(), queue_image(), queue_pdf());
        //初始化消费者数量
        simpleMessageListenerContainer.setConcurrentConsumers(1);
        //最大消费者数量
        simpleMessageListenerContainer.setMaxConcurrentConsumers(5);
        //设置是否重回队列[一般为false]
        simpleMessageListenerContainer.setDefaultRequeueRejected(false);
        //设置自动ack
        simpleMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        //设置channel 是否外露
        simpleMessageListenerContainer.setExposeListenerChannel(true);
        //设置消费端标签的策略
        simpleMessageListenerContainer.setConsumerTagStrategy(new ConsumerTagStrategy() {
            @Override
            public String createConsumerTag(String queueName) {
                return queueName + "_" + UUID.randomUUID().toString();
            }
        });

        ///设置消息监听 ChannelAwareMessageListener
        /*
        simpleMessageListenerContainer.setMessageListener(new ChannelAwareMessageListener() {
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                String msg = new String(message.getBody());
                System.out.println("----------消费者： " + msg);
            }
        });
        */

        ///消息监听适配器
        /*
         * 适配器方式。 默认是有自己的方法名字。 handleMessage
         *  可以自己指定一个方法的名称。 consumerMessage
         *  也可以添加一个转换器： 从字节数组转换为String
         */
        /*
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(new MessageDelegate());
        messageListenerAdapter.setDefaultListenerMethod("consumeMessage");
        messageListenerAdapter.setMessageConverter(new TextMessageConverter());
        simpleMessageListenerContainer.setMessageListener(messageListenerAdapter);
        */


        ///为每个队列单独指定处理方法
        /*
        2适配器方式。我们的队列名称 和 方法名称 也可以进行一一的匹配
        不知道为啥会报错
         */
/*
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(new MessageDelegate());
        //设置转换器委托对象
        messageListenerAdapter.setMessageConverter(new TextMessageConverter());
        HashMap<String, String> queueOrTagToMethodName = new HashMap<>(2);
        queueOrTagToMethodName.put("queue001", "method1");
        queueOrTagToMethodName.put("queue002", "method2");
        messageListenerAdapter.setQueueOrTagToMethodName(queueOrTagToMethodName);
        simpleMessageListenerContainer.setMessageListener(messageListenerAdapter);
   */

        /*      -------------------------------其他转换器的使用      */

        /// 1.1 支持json格式的转换器
        /*
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("consumeMessage");
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        adapter.setMessageConverter(jackson2JsonMessageConverter);
        simpleMessageListenerContainer.setMessageListener(adapter);
        */


        /// 1.2 DefaultJackson2JavaTypeMapper & Jackson2JsonMessageConverter 支持java对象转换
        /*
         MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
         adapter.setDefaultListenerMethod("consumeMessage");

         Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();

         DefaultJackson2JavaTypeMapper javaTypeMapper = new DefaultJackson2JavaTypeMapper();
         jackson2JsonMessageConverter.setJavaTypeMapper(javaTypeMapper);

         adapter.setMessageConverter(jackson2JsonMessageConverter);
         container.setMessageListener(adapter);
         */


        ///1.3 DefaultJackson2JavaTypeMapper & Jackson2JsonMessageConverter 支持java对象多映射转换
        /*
         MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
         adapter.setDefaultListenerMethod("consumeMessage");
         Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
         DefaultJackson2JavaTypeMapper javaTypeMapper = new DefaultJackson2JavaTypeMapper();

         Map<String, Class<?>> idClassMapping = new HashMap<String, Class<?>>();
         idClassMapping.put("order", com.bfxy.spring.entity.Order.class);
         idClassMapping.put("packaged", com.bfxy.spring.entity.Packaged.class);

         javaTypeMapper.setIdClassMapping(idClassMapping);

         jackson2JsonMessageConverter.setJavaTypeMapper(javaTypeMapper);
         adapter.setMessageConverter(jackson2JsonMessageConverter);
         container.setMessageListener(adapter);
         */

        //1.4 ext convert
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("consumeMessage");

        //全局的转换器:
        ContentTypeDelegatingMessageConverter convert = new ContentTypeDelegatingMessageConverter();

        TextMessageConverter textConvert = new TextMessageConverter();
        convert.addDelegate("text", textConvert);
        convert.addDelegate("html/text", textConvert);
        convert.addDelegate("xml/text", textConvert);
        convert.addDelegate("text/plain", textConvert);

        Jackson2JsonMessageConverter jsonConvert = new Jackson2JsonMessageConverter();
        convert.addDelegate("json", jsonConvert);
        convert.addDelegate("application/json", jsonConvert);

        ImageMessageConverter imageConverter = new ImageMessageConverter();
        convert.addDelegate("image/png", imageConverter);
        convert.addDelegate("image", imageConverter);

        PDFMessageConverter pdfConverter = new PDFMessageConverter();
        convert.addDelegate("application/pdf", pdfConverter);


        adapter.setMessageConverter(convert);
        simpleMessageListenerContainer.setMessageListener(adapter);


        return simpleMessageListenerContainer;


    }
}
