package com.lyw.clickhousestarter.executor;

import com.lyw.clickhousestarter.config.ClickHouseConnectionHolder;
import org.springframework.util.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SqlExecutor {

    private Connection connection;
    private String originSql;
    private Map<String, Object> paramMap;

    private SqlExecutor(Connection connection) {
        this.connection = connection;
    }

    public static SqlExecutor getInstance() {
        return new SqlExecutor(ClickHouseConnectionHolder.getConnection());
    }

    public SqlExecutor setSql(String originSql) {
        this.originSql = originSql;
        return this;
    }

    public SqlExecutor setParams(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
        return this;
    }

    public Object execute() throws SQLException {
        String finalSql = parseSql();
        if (StringUtils.startsWithIgnoreCase(finalSql, "SELECT")) {
            return handleSelect(finalSql);
        } else {
            return handleOthers(finalSql);
        }
    }

    private String parseSql() {
        if (originSql == null || paramMap == null || paramMap.isEmpty()) {
            return originSql;
        }
        String parsed = originSql;
        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            parsed = parsed.replace("${" + entry.getKey() + "}", String.valueOf(entry.getValue()));
        }
        return parsed;
    }

    private List<Map<String, Object>> handleSelect(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery(sql);
        statement.closeOnCompletion();
        ResultSetMetaData metaData = results.getMetaData();
        List<Map<String, Object>> r = new ArrayList<>();
        while (results.next()) {
            Map<String, Object> row = new LinkedHashMap<>();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                row.put(metaData.getColumnName(i), results.getObject(metaData.getColumnName(i)));
            }
            r.add(row);
        }
        results.close();
        return r;
    }

    private Object handleOthers(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(sql);
        statement.closeOnCompletion();
        return null;
    }

}
