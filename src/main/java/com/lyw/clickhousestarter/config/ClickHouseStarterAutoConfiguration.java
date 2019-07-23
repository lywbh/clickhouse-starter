package com.lyw.clickhousestarter.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.clickhouse.ClickHouseDataSource;

import java.sql.Connection;
import java.sql.SQLException;

@Configuration
@ConditionalOnProperty(prefix = "clickhouse", name = "url")
@EnableConfigurationProperties(ClickHouseProperties.class)
public class ClickHouseStarterAutoConfiguration {

    private ClickHouseProperties properties;

    public ClickHouseStarterAutoConfiguration(ClickHouseProperties properties) {
        this.properties = properties;
    }

    @Bean("clickHouseConnection")
    public Connection connection() throws SQLException {
        ClickHouseDataSource clickHouseDataSource = new ClickHouseDataSource(properties.getUrl(), properties);
        return clickHouseDataSource.getConnection();
    }

}
