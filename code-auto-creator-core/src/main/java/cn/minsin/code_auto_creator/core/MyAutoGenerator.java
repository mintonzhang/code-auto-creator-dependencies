package cn.minsin.code_auto_creator.core;

import cn.minsin.code_auto_creator.constant.DefaultConstant;
import cn.minsin.code_auto_creator.model.GeneratorParams;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.io.File;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author: minton.zhang
 * @since: 2020/5/29 10:30
 */
public class MyAutoGenerator extends AutoGenerator {

    public MyAutoGenerator(DataSourceConfig dataSourceConfig, GeneratorParams generatorParams) {
        super.setDataSource(dataSourceConfig);
        this.generatorParams = generatorParams;
    }

    private GeneratorParams generatorParams;

    /**
     * default save Path
     */
    private String defaultSaveDictionary = DefaultConstant.DEFAULT_SAVE_DIRECTORY+System.currentTimeMillis();


    /**
     * 1 实体类 2 mapper 3 xml
     *
     * @param type
     * @return
     */
    protected String getSaveFile(int type) {
        switch (type) {
            case 1:
                return defaultSaveDictionary.concat("/entity");
            case 2:
                return defaultSaveDictionary.concat("/mapper");
            case 3:
                return defaultSaveDictionary.concat("/mapper/xml");
        }
        return null;
    }

    public MyAutoGenerator setGlobalDir(String path) {
        this.defaultSaveDictionary = path;
        return this;
    }


    public void run(Consumer<File> consumer) {
        TemplateConfig templateConfig = new TemplateConfig();
        super.setTemplate(templateConfig);
        super.setTemplateEngine(new MyTemplateEngine());
        GlobalConfig gc = new GlobalConfig();
        gc.setFileOverride(generatorParams.isOverrideFile);
        //输出路径
        gc.setAuthor("created by `code-auto-creator`");
        gc.setDateType(generatorParams.isDate ? DateType.ONLY_DATE : DateType.TIME_PACK);
        gc.setOpen(false);
        gc.setSwagger2(false);
        gc.setBaseColumnList(true);
        gc.setEnableCache(false);
        gc.setEntityName("%s".concat(generatorParams.entitySuffix));
        gc.setMapperName("%s".concat(generatorParams.mapperSuffix));
        gc.setBaseColumnList(false);
        gc.setBaseResultMap(false);
        super.setGlobalConfig(gc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setLogicDeleteFieldName(generatorParams.logicDeleteFiled);
        strategy.setEntityColumnConstant(true);
        strategy.setEntityLombokModel(generatorParams.isEnableLombok);
        //跳过视图
        strategy.setSkipView(generatorParams.isSkipView);
        if (generatorParams.tables != null && generatorParams.tables.length > 0) {
            strategy.setInclude(generatorParams.tables);
        }
        strategy.setTablePrefix(generatorParams.tablePrefix);
        strategy.setEntityBooleanColumnRemoveIsPrefix(true);
        strategy.setSuperEntityClass(generatorParams.entityBaseClass);
        strategy.setSuperMapperClass(generatorParams.mapperBaseClass);
        super.setStrategy(strategy);
        InjectionConfig injectionConfig = this.getInjectionConfig();
        if (injectionConfig != null) {
            super.setCfg(injectionConfig);
        }
        super.execute();
        consumer.accept(new File(defaultSaveDictionary));
    }


    protected InjectionConfig getInjectionConfig() {
        // 自定义配置 cfg.XXXX
        return new InjectionConfig() {
            @Override
            public void initMap() {
                //更改输出文件目录
                ConfigBuilder config = super.getConfig();
                Map<String, String> packageInfo = config.getPackageInfo();
                packageInfo.put(ConstVal.MAPPER, MyAutoGenerator.this.generatorParams.mapperPackage);
                packageInfo.put(ConstVal.ENTITY, MyAutoGenerator.this.generatorParams.entityPackage);
                Map<String, String> pathInfo = config.getPathInfo();
                //controller和service、serviceImpl不要
                pathInfo.put(ConstVal.SERVICE_IMPL_PATH, null);
                pathInfo.put(ConstVal.SERVICE_PATH, null);
                pathInfo.put(ConstVal.CONTROLLER_PATH, null);
                //mapper输出到mybaits模块
                pathInfo.put(ConstVal.MAPPER_PATH, MyAutoGenerator.this.getSaveFile(2));
                pathInfo.put(ConstVal.ENTITY_PATH, MyAutoGenerator.this.getSaveFile(1));
                //xml输出到mybaits的resource下
                pathInfo.put(ConstVal.XML_PATH, MyAutoGenerator.this.getSaveFile(3));
            }
        };
    }
}
