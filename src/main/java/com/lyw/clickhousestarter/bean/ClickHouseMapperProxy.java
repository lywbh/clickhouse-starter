package com.lyw.clickhousestarter.bean;

import com.lyw.clickhousestarter.annotation.Param;
import com.lyw.clickhousestarter.annotation.Sql;
import com.lyw.clickhousestarter.executor.SqlExecutor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ClickHouseMapperProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws InvocationTargetException, IllegalAccessException, SQLException {
        String sql = getSql(method);
        if (sql == null) {
            return method.invoke(this, args);
        }
        Map<String, Object> params = getParams(method, args);
        return SqlExecutor.getInstance()
                .setSql(sql)
                .setParams(params)
                .execute();
    }

    private String getSql(Method method) {
        Sql sqlAnnotation = method.getAnnotation(Sql.class);
        return sqlAnnotation == null ? null : sqlAnnotation.value();
    }

    private Map<String, Object> getParams(Method method, Object[] args) {
        Map<String, Object> paramMap = new HashMap<>();
        if (args != null) {
            Parameter[] parameters = method.getParameters();
            for (int i = 0; i < args.length; ++i) {
                Param paramAnnotation = parameters[i].getAnnotation(Param.class);
                String paramName = paramAnnotation == null ? parameters[i].getName() : paramAnnotation.value();
                paramMap.put(paramName, args[i]);
            }
        }
        return paramMap;
    }

}
