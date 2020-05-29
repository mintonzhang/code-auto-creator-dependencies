package cn.minsin.code_auto_creator.core;

import cn.minsin.core.tools.StringUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.FileType;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 文件夹如果带有中文会报错，所以重写了这个类
 *
 * @Author: minton.zhang
 * @Date: 2019/7/19 16:42
 */
public class MyTemplateEngine extends VelocityTemplateEngine {

    @Override
    public AbstractTemplateEngine mkdirs() {
        getConfigBuilder().getPathInfo().forEach((key, value) -> {
            if (StringUtil.isNotBlank(value)) {
                File dir = new File(value);
                if (!dir.exists()) {
                    boolean result = dir.mkdirs();
                    if (result) {
                        logger.debug("创建目录： [" + value + "]");
                    }
                }
            }
        });
        return this;
    }

    @Override
    public AbstractTemplateEngine batchOutput() {
        try {
            List<TableInfo> tableInfoList = this.getConfigBuilder().getTableInfoList();

            for (TableInfo tableInfo : tableInfoList) {
                Map<String, Object> objectMap = this.getObjectMap(tableInfo);
                Map<String, String> pathInfo = this.getConfigBuilder().getPathInfo();
                TemplateConfig template = this.getConfigBuilder().getTemplate();
                InjectionConfig injectionConfig = this.getConfigBuilder().getInjectionConfig();
                if (null != injectionConfig) {
                    injectionConfig.initMap();
                    objectMap.put("cfg", injectionConfig.getMap());
                    List<FileOutConfig> focList = injectionConfig.getFileOutConfigList();
                    if (CollectionUtils.isNotEmpty(focList)) {

                        for (FileOutConfig foc : focList) {
                            if (this.isCreate(FileType.OTHER, foc.outputFile(tableInfo))) {
                                this.writer(objectMap, foc.getTemplatePath(), foc.outputFile(tableInfo));
                            }
                        }
                    }
                }

                String entityName = tableInfo.getEntityName();
                String controllerFile;
                if (null != entityName && null != pathInfo.get("entity_path")) {
                    String entity_path = URLDecoder.decode(pathInfo.get("entity_path"), String.valueOf(StandardCharsets.UTF_8)) + File.separator + "%s" + this.suffixJavaOrKt();
                    controllerFile = String.format(entity_path, entityName);
                    if (this.isCreate(FileType.ENTITY, controllerFile)) {
                        this.writer(objectMap, this.templateFilePath(template.getEntity(this.getConfigBuilder().getGlobalConfig().isKotlin())), controllerFile);
                    }
                }

                if (null != tableInfo.getMapperName() && null != pathInfo.get("mapper_path")) {
                    controllerFile = String.format(URLDecoder.decode(pathInfo.get("mapper_path"), String.valueOf(StandardCharsets.UTF_8)) + File.separator + tableInfo.getMapperName() + this.suffixJavaOrKt(), entityName);
                    if (this.isCreate(FileType.MAPPER, controllerFile)) {
                        this.writer(objectMap, this.templateFilePath(template.getMapper()), controllerFile);
                    }
                }

                if (null != tableInfo.getXmlName() && null != pathInfo.get("xml_path")) {
                    controllerFile = String.format(URLDecoder.decode(pathInfo.get("xml_path"), String.valueOf(StandardCharsets.UTF_8)) + File.separator + tableInfo.getXmlName() + ".xml", entityName);
                    if (this.isCreate(FileType.XML, controllerFile)) {
                        this.writer(objectMap, this.templateFilePath(template.getXml()), controllerFile);
                    }
                }

                if (null != tableInfo.getServiceName() && null != pathInfo.get("service_path")) {
                    controllerFile = String.format(URLDecoder.decode(pathInfo.get("service_path"), String.valueOf(StandardCharsets.UTF_8)) + File.separator + tableInfo.getServiceName() + this.suffixJavaOrKt(), entityName);
                    if (this.isCreate(FileType.SERVICE, controllerFile)) {
                        this.writer(objectMap, this.templateFilePath(template.getService()), controllerFile);
                    }
                }

                if (null != tableInfo.getServiceImplName() && null != pathInfo.get("service_impl_path")) {
                    controllerFile = String.format(URLDecoder.decode(pathInfo.get("service_impl_path"), String.valueOf(StandardCharsets.UTF_8)) + File.separator + tableInfo.getServiceImplName() + this.suffixJavaOrKt(), entityName);
                    if (this.isCreate(FileType.SERVICE_IMPL, controllerFile)) {
                        this.writer(objectMap, this.templateFilePath(template.getServiceImpl()), controllerFile);
                    }
                }

                if (null != tableInfo.getControllerName() && null != pathInfo.get("controller_path")) {
                    controllerFile = String.format(URLDecoder.decode(pathInfo.get("controller_path"), String.valueOf(StandardCharsets.UTF_8)) + File.separator + tableInfo.getControllerName() + this.suffixJavaOrKt(), entityName);
                    if (this.isCreate(FileType.CONTROLLER, controllerFile)) {
                        this.writer(objectMap, this.templateFilePath(template.getController()), controllerFile);
                    }
                }
            }
        } catch (Exception var11) {
            logger.error("无法创建文件，请检查配置信息！", var11);
        }

        return this;
    }
}
