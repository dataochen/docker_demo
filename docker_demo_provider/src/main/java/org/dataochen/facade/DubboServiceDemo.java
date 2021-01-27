package org.dataochen.facade;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.dataochen.api.DemoInterface;
import org.dataochen.entity.Demo;
import org.dataochen.entity.DemoExample;
import org.dataochen.mapper.DemoMapper;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @author dataochen
 * @Description
 * @date: 2021/1/26 16:47
 */
@DubboService(version = "1.0")
@Slf4j
public class DubboServiceDemo implements DemoInterface {
    @Autowired
    private DemoMapper demoMapper;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private RabbitTemplate template;

    @PostConstruct
    public void init() {
        template.setConfirmCallback((correlationData, ack, cause) -> {
            if (null == correlationData) {
                return;
            }
            if (!ack) {
                log.error("send message failed: " + cause + correlationData.toString());
                return;
            }
        });
    }
    @Override
    public void demoMethod() {
        log.info("==========调用demoMethod方法===========");
        Demo demo = new Demo();
        demo.setName("nameTest");
        demo.setCreatedDate(new Date());
        int insert = demoMapper.insert(demo);
        log.info("mySql is {}", insert > 0 ? "ok" : "fail");
        try {
            redisTemplate.opsForValue().set("demo", System.currentTimeMillis());
            log.info("redis is ok.");
        } catch (Exception e) {
            log.info("redis is fail.");
            log.error("redis e={}", e);
        }
        try {
            CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
            template.convertAndSend(template.getExchange(), "demo", System.currentTimeMillis(), correlationData);
            log.info("rabbitMq is ok.");
        } catch (Exception e) {
            log.info("rabbitMq is fail.");
            log.error("rabbitMq e={}", e);
        }

    }

    @Override
    public Map<String, Object> resultMap() {
        HashMap<String, Object> result = new HashMap<>();
        DemoExample demoExample = new DemoExample();
        demoExample.setOrderByClause("id desc");
        DemoExample.Criteria criteria = demoExample.createCriteria();
        List<Demo> demos = demoMapper.selectByExample(demoExample);
        result.put("db", demos.get(0));
        Object demo = redisTemplate.opsForValue().get("demo");
        result.put("redis", demo);
        return result;
    }
}
