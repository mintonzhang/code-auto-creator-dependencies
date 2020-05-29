package cn.minsin.jfx.model;

import cn.minsin.code_auto_creator.model.GeneratorParams;
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
public class ClientGeneratorParams extends GeneratorParams {

    public String globalDir;

    public String entityDir;

    public String mapperDir;

    public String xmlDir;

    //数据库配置
    public String urlText;

    public String usernameText;
    public String passwordText;
    public String databaseTypeValue;
    public String diverPathText;
}
