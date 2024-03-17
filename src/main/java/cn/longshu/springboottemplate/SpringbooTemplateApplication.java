package cn.longshu.springboottemplate;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"cn.longshu.springboottemplate.dao"})
public class SpringbooTemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbooTemplateApplication.class, args);
    }

}
