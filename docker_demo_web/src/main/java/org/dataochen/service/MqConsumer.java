package org.dataochen.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author dataochen
 * @Description
 * @date: 2021/1/27 10:21
 */
@Component
@Slf4j
public class MqConsumer {
    public static Object mqRes;

    @RabbitListener(queues = "demo")
    public void mq(Message message) {
        log.info("消费MQ message={}", message.toString());
        mqRes = new String(message.getBody());
    }
}
