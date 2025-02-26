package org.example.clipflow;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("org.example.clipflow.mapper")
@SpringBootApplication
public class ClipFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClipFlowApplication.class, args);
    }

}
