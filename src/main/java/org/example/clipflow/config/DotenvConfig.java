package org.example.clipflow.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

/**
 * 加载.env文件，用于读取环境变量，隐藏敏感信息
 *
 * @author hugo
 */
@Configuration
public class DotenvConfig {

    private final Dotenv dotenv;

    public DotenvConfig() {
        this.dotenv = Dotenv.load();
    }

    @PostConstruct
    public void loadEnvVariables() {
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
    }
}