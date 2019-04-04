/**
 * - **不处理路由键。只需要简单的将队列绑定到交换机上**
 * - **f发送到交换机的消息都会被转发到于该交换机绑定的所有队列上。**
 * - **Fanout 交换机转发消息是最快的**
 */
package com.yanghx.rabbitmq.api.exchanges.fanout;