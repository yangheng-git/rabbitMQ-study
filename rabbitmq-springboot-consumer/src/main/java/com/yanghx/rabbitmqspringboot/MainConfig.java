package com.yanghx.rabbitmqspringboot;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;

/**
 * 主配置类
 *
 * @author yangHX
 * createTime  2019/4/6 18:53
 */
@Configuration
@ComponentScan({"com.yanghx.rabbitmqspringboot.*"})
public class MainConfig {
}
