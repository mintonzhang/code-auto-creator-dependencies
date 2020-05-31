package cn.minsin.code_auto_creator;

import com.google.common.collect.Lists;
import lombok.Cleanup;
import lombok.Getter;
import lombok.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipOutputStream;

/**
 * @author: minton.zhang
 * @since: 2020/5/29 11:27
 */
public class Zip {

    public static Zip of(@NonNull File... files) {
        Zip zip = new Zip();
        for (File file : files) {
            zip.addFiles(file);
        }
        return zip;
    }

    public static Zip of(@NonNull List<File> files) {
        Zip zip = new Zip();
        for (File file : files) {
            zip.addFiles(file);
        }
        return zip;
    }


    @Getter
    private List<File> files;

    /**
     * 添加文件
     *
     * @param file
     * @return
     */
    public Zip addFiles(File file) {
        if (this.files == null) {
            this.files = Lists.newArrayList(file);
        } else {
            this.files.add(file);
        }
        return this;
    }

    /**
     * 压缩文件
     *
     * @param outputStream 输出文件流
     * @throws IOException
     */
    public void zip(OutputStream outputStream, String zipName) throws IOException {
        @Cleanup OutputStream copyOutputStream = outputStream;
        @Cleanup ZipOutputStream zipOutputStream = new ZipOutputStream(copyOutputStream);
        for (File file : files) {
            ZipUtil.zipDictionary(file, zipName, zipOutputStream);
        }
    }

    /**
     * 压缩文件
     *
     * @param saveFile 输出文件夹
     * @throws IOException
     */
    public void zip(File saveFile, String zipName) throws IOException {
        @Cleanup
        FileOutputStream fileOutputStream = new FileOutputStream(saveFile);
        @Cleanup
        ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
        for (File file : files) {
            ZipUtil.zipDictionary(file, zipName, zipOutputStream);
        }
    }

    /**
     * 解压文件
     *
     * @param saveDictionary
     * @param ifErrorContinue
     * @throws IOException
     */
    public void unZip(File saveDictionary, boolean ifErrorContinue) throws IOException {
        for (File file : files) {
            ZipUtil.unZip(file, saveDictionary, ifErrorContinue);
        }
    }
}
