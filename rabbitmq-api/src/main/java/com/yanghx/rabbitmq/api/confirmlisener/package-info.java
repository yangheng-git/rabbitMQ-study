/**
 * 消息投递模式
 * <p>
 * 消息的确认，是指生产者投递消息后，如果Broker收到消息，则会给我们生产者一个应答。
 * 生产者记性接收应答，用来确认这条消息是否正常的发送到Brokeer。 这种方式也是消息的可靠性投递的核心保障！
 */
package com.yanghx.rabbitmq.api.confirmlisener;