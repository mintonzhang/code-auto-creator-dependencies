package cn.minsin.server.config;

import cn.minsin.core.tools.FileUtil;
import cn.minsin.core.tools.IOUtil;
import cn.minsin.server.constant.JarDictionaryConstant;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.DbType;
import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @author: minton.zhang
 * @since: 2020/5/30 17:32
 */
@Configuration
public class DriverConfig {

    @Bean
    public DriverMap driverMap() {
        DriverMap driverMap = new DriverMap();
        driverMap.loadFromJson();
        return driverMap;
    }


    @Getter
    @Setter
    public static class DriverMap {

        static {
            FileUtil.checkPath(JarDictionaryConstant.DEFAULT_DRIVER_SAVE_DIRECTORY);
        }

        private List<DriverMapNode> local;


        public List<DbType> getDbTypes() {
            return local.stream().map(DriverMapNode::getDbType).collect(Collectors.toList());
        }

        public URL getDriverUrl(DbType dbType) throws MalformedURLException {
            for (DriverMapNode e : local) {
                if (e.getDbType().equals(dbType)) {
                    return e.getUrl().toURI().toURL();
                }
            }
            throw new NullPointerException("Please upload Driver");
        }


        public void loadFromJson() {
            try {
                File file = new File(JarDictionaryConstant.DEFAULT_DRIVER_MAP_SAVE_FILE);
                if (!file.exists()) {
                    file.createNewFile();
                    local = new CopyOnWriteArrayList<>();
                    return;
                }

                @Cleanup
                FileReader stringReader = new FileReader(file);

                @Cleanup
                BufferedReader br = new BufferedReader(stringReader);
                String str;

                StringBuilder sb = new StringBuilder();

                while ((str = br.readLine()) != null) {
                    sb.append(str);
                }
                String string = sb.toString();
                this.local = JSON.parseArray(string, DriverMapNode.class);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void addAndWriteJson(DbType dbType, InputStream inputStream) {
            try {
                @Cleanup
                InputStream inputStreamCopy = inputStream;
                //创建新文件
                File file = new File(JarDictionaryConstant.DEFAULT_DRIVER_SAVE_DIRECTORY + "/" + dbType.name() + "_" + System.currentTimeMillis() + ".jar");
                @Cleanup
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                IOUtil.inputStream2OutputStreamWithIO(inputStreamCopy, fileOutputStream, true);

                this.local.add(new DriverMapNode(dbType, file));

                String string = JSON.toJSONString(this.local);

                @Cleanup
                FileOutputStream fileOutputStream1 = new FileOutputStream(JarDictionaryConstant.DEFAULT_DRIVER_MAP_SAVE_FILE);

                fileOutputStream1.write(string.getBytes());

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DriverMapNode {

        private DbType dbType;

        private File url;

        public void setDbType(String dbType) {
            this.dbType = DbType.getDbType(dbType);
        }

        public void setUrl(String url) {
            this.url = new File(url);
        }
    }
}
