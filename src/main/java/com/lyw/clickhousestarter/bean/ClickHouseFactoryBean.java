package com.lyw.clickhousestarter.bean;

import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

public class ClickHouseFactoryBean<T> implements FactoryBean<T> {

    private Class<T> clazz;

    public ClickHouseFactoryBean(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T getObject() {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new ClickHouseMapperProxy());
    }

    @Override
    public Class<?> getObjectType() {
        return clazz;
    }

}
