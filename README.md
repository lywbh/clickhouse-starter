# clickhouse-starter -- access clickhouse db like mybatis in springboot

##### use Dynamic Proxy and ImportBeanDefinitionRegistrar to help generating code of sql execution.

## Installation
```xml
<dependency>
    <groupId>com.lyw</groupId>
    <artifactId>clickhouse-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

## Configuration
```properties
clickhouse.url=jdbc:clickhouse://host:port/db_name
clickhouse.user=admin
clickhouse.password=admin
```

## Use
```java
@SpringBootApplication
@MapperScan(basePackages = "com.lyw.clickhousedemo.*.mapper")
public class ClickHouseDemoApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ClickHouseDemoApplication.class, args);
    }
    
}
```
```java
@Service
public class ClickHouseDemoService {
    
    @Resource
    private ClickHouseDemoMapper demoMapper;
    
}
```
```java
public interface ClickHouseDemoMapper {
    
    @Sql("select * from user_info where name = '${name}'")
    List<Map<String, Object>> test(@Param("name") String name);
    
}
```

##### Quote marks are needed when transmitting a string value.
##### @Param annotation can be omitted.

###### 搞Spring就是逊啦
