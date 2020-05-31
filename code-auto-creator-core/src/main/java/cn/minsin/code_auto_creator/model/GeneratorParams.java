package cn.minsin.code_auto_creator.model;

import cn.minsin.code_auto_creator.driver.DriverMap;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author: minton.zhang
 * @since: 2020/4/12 20:19
 */
@Getter
@Setter
@Accessors(chain = true)
public class GeneratorParams {

    @ApiModelProperty("The basic entity class name like 'cn.minsin.model.BaseModel'")
    public String entityBaseClass;

    @ApiModelProperty("The entity Suffix like 'PO'")
    public String entitySuffix = "PO";

    @ApiModelProperty("The entity package like 'cn.minsin.model")
    public String entityPackage = "cn.minsin.model";

    @ApiModelProperty("The logic delete  like 'del_flag")
    public String logicDeleteFiled;

    @ApiModelProperty("The basic mapper class name like 'cn.minsin.model.BaseMapper'")
    public String mapperBaseClass;

    @ApiModelProperty("The mapper suffix like 'Mapper'")
    public String mapperSuffix = "Mapper";

    @ApiModelProperty("The mapper package like 'cn.minsin.mapper'")
    public String mapperPackage = "cn.minsin.mapper";

    @ApiModelProperty("Use or not lombok default 'true'")
    public boolean isEnableLombok = true;

    @ApiModelProperty("If true, java.util.Data will be used, otherwise java.time.*")
    public boolean isDate = true;

    @ApiModelProperty("Whether to enable hump conversion")
    public boolean isUnderlineToCamel = true;

    @ApiModelProperty("Whether to skip the view")
    public boolean isSkipView = false;

    @ApiModelProperty("Table to be generated If not filled, all tables will be generated")
    public String[] tables;

    @ApiModelProperty("Table prefix If filled, it will not be generated into the class or interface name when it is generated")
    public String[] tablePrefix;

    //数据库配置
    @ApiModelProperty(value = "The datasource connect url", required = true)
    public String connectUrl;

    @ApiModelProperty(value = "username of the database", required = true)
    public String username;

    @ApiModelProperty(value = "password of the database", required = true)
    public String password;

    @ApiModelProperty(value = "password of the database", required = true)
    public DriverMap dbType;

    @ApiModelProperty(value = "If fill in, you will use this driver")
    public String driverName;
}
