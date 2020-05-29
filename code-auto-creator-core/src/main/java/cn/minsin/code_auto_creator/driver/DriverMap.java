package cn.minsin.code_auto_creator.driver;

import com.baomidou.mybatisplus.annotation.DbType;

import java.util.HashMap;

/**
 * 驱动类与数据库进行关联
 *
 * @author: minton.zhang
 * @since: 2020/5/29 13:49
 */
public enum DriverMap {
    MYSQL(DbType.MYSQL, "com.mysql.jdbc.Driver", "com.mysql.cj.jdbc.Driver"),
    MARIADB(DbType.MARIADB, "com.mysql.jdbc.Driver", "org.mariadb.jdbc.Driver "),
    ORACLE(DbType.ORACLE, "oracle.jdbc.driver.OracleDriver", "oracle.jdbc.OracleDriver"),
    ORACLE_12C(DbType.ORACLE_12C, "oracle.jdbc.driver.OracleDriver", "oracle.jdbc.OracleDriver"),
    DB2(DbType.DB2, "com.ibm.db2.jcc.DB2Driver"),
    H2(DbType.H2, "org.h2.Driver"),
    HSQL(DbType.HSQL, "org.hsqldb.jdbcDriver"),
    SQLITE(DbType.SQLITE, "org.sqlite.JDBC"),
    POSTGRE_SQL(DbType.POSTGRE_SQL, "org.postgresql.Driver"),
    SQL_SERVER2005(DbType.SQL_SERVER2005, "com.microsoft.jdbc.sqlserver.SQLServerDriver", "com.microsoft.sqlserver.jdbc.SQLServerDriver"),
    SQL_SERVER(DbType.SQL_SERVER, "com.microsoft.sqlserver.jdbc.SQLserverDriver"),
    DM(DbType.DM, "dm.jdbc.driver.DmDriver"),
    XU_GU(DbType.DM, "com.xugu.jdbc.Driver"),
    KINGBASE_ES(DbType.DM, "com.kingbase.Driver"),
    ;
    private final DbType dbType;

    private final String[] driverName;

    DriverMap(DbType dbType, String... driverName) {
        this.dbType = dbType;
        this.driverName = driverName;
    }

    private static final HashMap<DbType, String[]> MAP = new HashMap<>(20);

    static {
        DriverMap[] values = DriverMap.values();
        for (DriverMap value : values) {
            MAP.put(value.dbType, value.driverName);
        }
    }

    public static String[] getDriverClasses(DbType dbType) {
        return MAP.get(dbType);
    }
}
