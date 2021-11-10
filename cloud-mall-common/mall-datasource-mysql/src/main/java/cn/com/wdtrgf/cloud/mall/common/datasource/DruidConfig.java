package cn.com.wdtrgf.cloud.mall.common.datasource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author TANG
 * @version 1.0
 * @createtime 2018/8/9 6:22 PM
 */
@Data
@ConfigurationProperties(prefix = "spring.datasource.druid")
public class DruidConfig {

    private Boolean keepAlive;

    private int initialSize;

    private int minIdle;

    private int maxActive;

    private int maxWait;

    private int timeBetweenEvictionRunsMillis;

    private int minEvictableIdleTimeMillis;

    private String validationQuery;

    private boolean testWhileIdle;

    private boolean testOnBorrow;

    private boolean testOnReturn;

    private boolean poolPreparedStatements;

    private int maxPoolPreparedStatementPerConnectionSize;

    private String filters;

    private String connectionProperties;

}
