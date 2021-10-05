package com.batch.SpringBatchExmaple.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;


@Configuration
@Order(1)
public class DataSourceConfig {
    @Value("${DBUrl}")
    private String dataSourceUrl;
    
    @Value("${cub.aco.db.onsConfiguration:N}")
    private String onsConfiguration;

//    @Bean(name = "FXSLOCAL")
//    @Primary
//    @Profile("local")
//    public DataSource getLocalDataSource() throws SQLException {

//        PoolDataSource poolDataSource = PoolDataSourceFactory.getPoolDataSource();
//
//        poolDataSource.setUser("OTRLXFXS01");
//        poolDataSource.setPassword("OTRLXFXS01");
//        poolDataSource.setURL(dataSourceUrl);
//        poolDataSource.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");
//        poolDataSource.setFastConnectionFailoverEnabled(true);
//        poolDataSource.setValidateConnectionOnBorrow(true); // boolean, 放true
//        poolDataSource.setSQLForValidateConnection("select 1 from dual");
//
//        return poolDataSource;
//    }
}
