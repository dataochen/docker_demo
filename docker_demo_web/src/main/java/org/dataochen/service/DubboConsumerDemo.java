package org.dataochen.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.dataochen.api.DemoInterface;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author dataochen
 * @Description
 * @date: 2021/1/26 18:39
 */
@Component
@Slf4j
public class DubboConsumerDemo {
    @DubboReference(check = false,version = "1.0")
    private DemoInterface demoInterface;
    public Map<String, Object> demoTest() throws InterruptedException {
        log.info("demoTest 开始");
        demoInterface.demoMethod();
        Map<String, Object> stringObjectMap = demoInterface.resultMap();
        Thread.sleep(3000L);
        stringObjectMap.put("mq", MqConsumer.mqRes);
        log.info("demoTest 结束");
        return stringObjectMap;
    }
}
