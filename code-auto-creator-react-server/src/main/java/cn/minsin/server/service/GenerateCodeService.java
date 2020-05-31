package cn.minsin.server.service;

import cn.minsin.code_auto_creator.Zip;
import cn.minsin.code_auto_creator.core.MyAutoGenerator;
import cn.minsin.code_auto_creator.driver.DriverMap;
import cn.minsin.server.config.DriverConfig;
import cn.minsin.server.model.WebGeneratorParams;
import com.baomidou.mybatisplus.annotation.DbType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author: minton.zhang
 * @since: 2020/5/30 17:30
 */
@Service
@RequiredArgsConstructor
public class GenerateCodeService {

    private final DriverConfig.DriverMap driverMap;

    public void generate(WebGeneratorParams webGeneratorParams, OutputStream outputStream) throws MalformedURLException {

        DriverMap dbType = webGeneratorParams.getDbType();

        URL url = driverMap.getDriverUrl(dbType.getDbType());

        new MyAutoGenerator(webGeneratorParams).overrideDatasource(e -> {
            try {
                e.setDriverWithURL(url);
            } catch (MalformedURLException malformedURLException) {
                malformedURLException.printStackTrace();
            }
        }).execute(e -> {
            //压缩文件
            try {
                Zip.of(e).zip(outputStream, "generator-code");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    /**
     * @param dbType
     * @param file
     * @return
     */
    public boolean saveJar(DbType dbType, MultipartFile file) {
        try {
            driverMap.addAndWriteJson(dbType, file.getInputStream());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
