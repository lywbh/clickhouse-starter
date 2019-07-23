package com.lyw.clickhousestarter.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;

@Configuration
public class ClickHouseConnectionHolder implements ApplicationContextAware {

    private static Connection connection;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        connection = (Connection) applicationContext.getBean("clickHouseConnection");
    }

    public static Connection getConnection() {
        return connection;
    }

}
