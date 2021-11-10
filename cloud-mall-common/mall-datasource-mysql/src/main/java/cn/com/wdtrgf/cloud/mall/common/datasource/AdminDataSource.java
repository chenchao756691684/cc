package cn.com.wdtrgf.cloud.mall.common.datasource;

/**
 *
 * @author cc
 * @date 2021/11/10 17:37
 **/

import cn.com.wdtrgf.cloud.common.core.base.mapper.BaseMapper;
import com.alibaba.druid.pool.DruidDataSource;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@EnableConfigurationProperties(DruidConfig.class)
@EnableTransactionManagement
@MapperScan(basePackages = {"cn.com.wdtrgf.cloud.mall.mysql.dao.**"}, sqlSessionTemplateRef = "adminSqlSessionTemplate", markerInterface = BaseMapper.class)
public class AdminDataSource {

    //  主库
    @Value("${spring.datasource.db.url}")
    private String dbUrl;
    @Value("${spring.datasource.db.username}")
    private String username;
    @Value("${spring.datasource.db.password}")
    private String password;
    @Value("${spring.datasource.db.driverClassName}")
    private String driverClassName;
    @Autowired
    private DruidConfig druidConfig;

    @Bean
    public DataSource adminData() throws SQLException {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setName("datasource-db-master");
        datasource.setUrl(dbUrl);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);
        BeanUtils.copyProperties(druidConfig, datasource);
        datasource.init();
        return datasource;
    }


    @Bean(name = "adminSqlSessionFactory")
    public SqlSessionFactory adminSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(routingDataSource());
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/mysql/**/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "adminTransactionManager")
    public DataSourceTransactionManager adminTransactionManager() throws SQLException {
        return new DataSourceTransactionManager(routingDataSource());
    }

    @Bean(name = "adminSqlSessionTemplate")
    public SqlSessionTemplate adminSqlSessionTemplate(@Qualifier("adminSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }


    /**
     * 设置数据源路由，通过该类中的determineCurrentLookupKey决定使用哪个数据源
     */
    @Bean
    public AbstractRoutingDataSource routingDataSource() throws SQLException {
        MyAbstractRoutingDataSource proxy = new MyAbstractRoutingDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>(2);
        targetDataSources.put(DbContextHolder.MASTER, adminData());
        proxy.setDefaultTargetDataSource(adminData());
        proxy.setTargetDataSources(targetDataSources);
        return proxy;
    }

}

    