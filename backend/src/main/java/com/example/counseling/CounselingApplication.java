package com.example.counseling;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.counseling.mapper")
public class CounselingApplication {
    public static void main(String[] args) {
        SpringApplication.run(CounselingApplication.class, args);
    }
}
