package org.dataochen;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author dataochen
 * @Description
 * @date: 2021/1/26 16:17
 */
@MapperScan(basePackages = {"org.dataochen.mapper"})
@SpringBootApplication(scanBasePackages = {"org.dataochen"})
@EnableDubbo
public class Application  extends SpringBootServletInitializer{
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
