package com.route.routedatesource.conf;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DynamicDataSource extends AbstractRoutingDataSource {
    public static final Map<Object,Object> dataSourceMap = new ConcurrentHashMap<>();

    public DynamicDataSource() {
        this.setTargetDataSources(dataSourceMap);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        String i="";
        String j="";
        String k="";
        //本地线程变量取出数据源id
        String dataconfigId = DatabaseContextHolder.getDbType();
        //判断数据源是否已经包含在集合里
        if(!dataSourceMap.containsKey(dataconfigId)){
            //DatabaseConfig config = databaseConfigService.selectByBrandId(DataSourceContextHolder.getDataSourceName());
            DruidDataSource druidDataSource = new DruidDataSource();
            druidDataSource.setUrl("");
            druidDataSource.setUsername("");
            druidDataSource.setPassword("");
            druidDataSource.setDriverClassName("");
            druidDataSource.setInitialSize(1);
            druidDataSource.setRemoveAbandoned(true);
            druidDataSource.setRemoveAbandonedTimeout(300);
            druidDataSource.setMinIdle(1);
            druidDataSource.setMaxActive(100);
            druidDataSource.setMaxWait(60000);
            druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
            druidDataSource.setMinEvictableIdleTimeMillis(300000);
            dataSourceMap.put(dataconfigId, druidDataSource);
            super.setTargetDataSources(dataSourceMap);
            super.afterPropertiesSet();
        }

        return dataconfigId;//只需要返回map的key,框架内部根据key寻找对应的数据源，可以决定使用那个db
    }

    //初始化后会走这里，将所有的数据源存储起来
    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
    }
}
