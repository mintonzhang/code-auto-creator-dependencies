package cn.minsin.code_auto_creator.core;

import cn.minsin.code_auto_creator.driver.DriverMap;
import cn.minsin.code_auto_creator.driver.DynamicDriver;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;

/**
 * @author: minton.zhang
 * @since: 2020/5/29 9:13
 */
public class MyDataSourceConfig extends DataSourceConfig {


    public static MyDataSourceConfig of(String url, String username, String password, DbType datasourceType) {
        MyDataSourceConfig dataSourceConfig = new MyDataSourceConfig();
        dataSourceConfig.setPassword(password);
        dataSourceConfig.setUsername(username);
        dataSourceConfig.setDbType(datasourceType);
        dataSourceConfig.setUrl(url);
        return dataSourceConfig;
    }

    public static MyDataSourceConfig of(String url, String username, String password, DbType datasourceType, String driverName) {
        return of(url, username, password, datasourceType).setDriverName(driverName);
    }

    @Override
    public MyDataSourceConfig setDriverName(String driverName) {
        super.setDriverName(driverName);
        return this;
    }

    /**
     * The
     */
    private URL driver;

    public MyDataSourceConfig setDriverWithPath(String path) throws MalformedURLException {
        this.driver = new URL(String.format("file:%s", path));
        return this;
    }

    public MyDataSourceConfig setDriverWithInputStream(InputStream path) throws IOException {
        this.setDriverWithFile(this.inputStreamToFile(path));
        return this;

    }

    public MyDataSourceConfig setDriverWithFile(File path) throws MalformedURLException {
        this.driver = path.toURI().toURL();
        return this;

    }

    public MyDataSourceConfig setDriverWithURL(URL url) throws MalformedURLException {
        this.driver = url;
        return this;
    }


    @Override
    public Connection getConn() {
        if (this.driver == null) {
            return super.getConn();
        }
        try {
            String[] driverClasses = DriverMap.getDriverClasses(this.getDbType());
            for (String driverClass : driverClasses) {
                try {
                    URLClassLoader classLoader = new URLClassLoader(new URL[]{this.driver});
                    Driver driver = (Driver) classLoader.loadClass(driverClass).newInstance();
                    // 注册驱动
                    DriverManager.registerDriver(new DynamicDriver(driver));
                    return DriverManager.getConnection(super.getUrl(), super.getUsername(), super.getPassword());
                } catch (Exception e) {
                    //
                }
            }
            throw new UnsupportedOperationException("未找到合适的驱动程序");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private File inputStreamToFile(InputStream inputStream) throws IOException {
        File tempFile = File.createTempFile("driver" + System.currentTimeMillis(), ".jar");
        FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
        int index;
        byte[] bytes = new byte[1024];

        while ((index = inputStream.read(bytes)) != -1) {
            fileOutputStream.write(bytes, 0, index);
            fileOutputStream.flush();
        }
        inputStream.close();
        fileOutputStream.close();
        return tempFile;
    }
}
