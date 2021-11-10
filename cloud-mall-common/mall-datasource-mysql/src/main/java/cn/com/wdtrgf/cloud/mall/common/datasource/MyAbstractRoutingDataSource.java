package cn.com.wdtrgf.cloud.mall.common.datasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author bovine
 * @date 2019-06-16 15:17
 */
@Slf4j
public class MyAbstractRoutingDataSource extends AbstractRoutingDataSource {

/*    @Value("${spring.datasource.slave.db.num}")
    private int num;*/

    private static int getRandom(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1)) + min;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        String typeKey = DbContextHolder.getDbType();
        if (typeKey.equals(DbContextHolder.MASTER)) {
            log.debug("admin.routingDataSource:db_type=master|write");
            return typeKey;
        }
        //使用随机数决定使用哪个读库
/*        int sum = getRandom(1, num);
        log.debug("admin.routingDataSource:db_type=slave|read{}", sum);*/
        return DbContextHolder.MASTER;
    }
}