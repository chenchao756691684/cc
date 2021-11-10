package cn.com.wdtrgf.cloud.mall.common.datasource;

import lombok.extern.slf4j.Slf4j;

/**
 * db type
 *
 * @author bovine
 * @date 2019-06-16 15:18
 */
@Slf4j
public class DbContextHolder {
    public static final String MASTER = "master";
    public static final String SLAVE = "slave";

    private static ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static String getDbType() {
        return contextHolder.get() == null ? MASTER : contextHolder.get();
    }

    public static void setDbType(String dbType) {
        contextHolder.set(MASTER);
    }

    public static void clearDbType() {
        contextHolder.remove();
    }
}