package com.campus.marketplace;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.campus.marketplace.mapper")
public class CampusMarketplaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampusMarketplaceApplication.class, args);
    }
}
