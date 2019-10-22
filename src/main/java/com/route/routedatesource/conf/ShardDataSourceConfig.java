package com.route.routedatesource.conf;

import com.alibaba.druid.pool.DruidDataSource;
import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(ShardDataSourceProperties.class)
public class ShardDataSourceConfig {

	@Autowired
	private ShardDataSourceProperties shardDataSourceProperties;

	private DruidDataSource parentDs() throws SQLException {
		DruidDataSource ds = new DruidDataSource();
		ds.setDriverClassName(shardDataSourceProperties.getDriverClassName());
		ds.setUsername(shardDataSourceProperties.getUsername());
		ds.setUrl(shardDataSourceProperties.getUrl());
		ds.setPassword(shardDataSourceProperties.getPassword());
		ds.setFilters(shardDataSourceProperties.getFilters());
		ds.setMaxActive(shardDataSourceProperties.getMaxActive());
		ds.setInitialSize(shardDataSourceProperties.getInitialSize());
		ds.setMaxWait(shardDataSourceProperties.getMaxWait());
		ds.setMinIdle(shardDataSourceProperties.getMinIdle());
		ds.setTimeBetweenEvictionRunsMillis(shardDataSourceProperties.getTimeBetweenEvictionRunsMillis());
		ds.setMinEvictableIdleTimeMillis(shardDataSourceProperties.getMinEvictableIdleTimeMillis());
		ds.setValidationQuery(shardDataSourceProperties.getValidationQuery());
		ds.setTestWhileIdle(shardDataSourceProperties.isTestWhileIdle());
		ds.setTestOnBorrow(shardDataSourceProperties.isTestOnBorrow());
		ds.setTestOnReturn(shardDataSourceProperties.isTestOnReturn());
		ds.setPoolPreparedStatements(shardDataSourceProperties.isPoolPreparedStatements());
		ds.setMaxPoolPreparedStatementPerConnectionSize(
				shardDataSourceProperties.getMaxPoolPreparedStatementPerConnectionSize());
		ds.setRemoveAbandoned(shardDataSourceProperties.isRemoveAbandoned());
		ds.setRemoveAbandonedTimeout(shardDataSourceProperties.getRemoveAbandonedTimeout());
		ds.setLogAbandoned(shardDataSourceProperties.isLogAbandoned());
		ds.setConnectionInitSqls(shardDataSourceProperties.getConnectionInitSqls());
		return ds;
	}

	private DataSource ds0() throws SQLException {
		DruidDataSource ds = parentDs();
		ds.setUsername(shardDataSourceProperties.getUsername0());
		ds.setUrl(shardDataSourceProperties.getUrl0());
		ds.setPassword(shardDataSourceProperties.getPassword0());
		return ds;
	}

	private DataSource ds1() throws SQLException {
		DruidDataSource ds = parentDs();
		ds.setUsername(shardDataSourceProperties.getUsername1());
		ds.setUrl(shardDataSourceProperties.getUrl1());
		ds.setPassword(shardDataSourceProperties.getPassword1());
		return ds;
	}

	@Bean
	public AbstractRoutingDataSource routingDataSource() throws SQLException {
		//在这里初始化的时候我们将所有的数据源已经放在了集合缓存里
		//我们还可在初始化的时候不要传入等到要使用的时候再传入切换

		Map<Object, Object> targetDataSources = new HashMap<>();
		targetDataSources.put("my", ds0());
		targetDataSources.put("he", ds1());

		AbstractRoutingDataSource routingDataSource = new DynamicDataSource();
		routingDataSource.setTargetDataSources(targetDataSources);
		routingDataSource.setDefaultTargetDataSource(ds1());
		return routingDataSource;
	}

	@Bean
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(routingDataSource());
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mybatis/*.xml"));

		return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws SQLException {
        return new DataSourceTransactionManager(routingDataSource());
    }


}
