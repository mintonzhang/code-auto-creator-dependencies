package cn.minsin.jfx.constant;

import cn.minsin.jfx.model.ClientGeneratorParams;
import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

import static cn.minsin.code_auto_creator.constant.DefaultConstant.DEFAULT_SAVE_DIRECTORY;

/**
 * @author: minton.zhang
 * @since: 2020/4/6 16:51
 */
public class GlobalVariables {

    public static double width = 500;

    public static double height = 500;

    public static String LOCAL_FILE = "/log/LAST_CONFIG";

    public static String LAST_TIME_FILE = DEFAULT_SAVE_DIRECTORY.concat(LOCAL_FILE);

    static {
        File file = new File(DEFAULT_SAVE_DIRECTORY);
        if (!file.exists()) {
            file.mkdirs();
        }
    }


    public static void save(String data) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(LAST_TIME_FILE, false);
            fileOutputStream.write(data.getBytes(StandardCharsets.UTF_8));
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ClientGeneratorParams read() {
        try {
            File file = new File(LAST_TIME_FILE);
            if (file.exists()) {
                //读取文件
                FileInputStream fileInputStream = new FileInputStream(file);
                StringBuilder stringBuilder = new StringBuilder();
                byte[] buf = new byte[1024];
                while (fileInputStream.read(buf) != -1) {
                    stringBuilder.append(new String(buf, StandardCharsets.UTF_8));
                }
                return JSON.parseObject(stringBuilder.toString(), ClientGeneratorParams.class);
            }
        } catch (Exception ignored) {

        }
        return null;
    }
}
