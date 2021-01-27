package org.dataochen.controller;

import lombok.extern.slf4j.Slf4j;
import org.dataochen.service.DubboConsumerDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author dataochen
 * @Description
 * @date: 2021/1/26 18:43
 */
@RestController
@RequestMapping("/")
@Slf4j
public class IndexController {
    @Autowired
    private DubboConsumerDemo dubboConsumerDemo;

    @GetMapping("/demo")
    public Map<String, Object> demo() throws InterruptedException {
        log.info("demo 开始");
        Map<String, Object> stringObjectMap = dubboConsumerDemo.demoTest();
        log.info("demo 结束");
        return stringObjectMap;
    }
}
