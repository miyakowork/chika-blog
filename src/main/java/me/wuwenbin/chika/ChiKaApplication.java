package me.wuwenbin.chika;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ChiKaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChiKaApplication.class, args);
    }

}
