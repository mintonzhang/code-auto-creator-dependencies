package cn.minsin.code_auto_creator.constant;

/**
 * @author: minton.zhang
 * @since: 2020/5/29 14:24
 */
public interface DefaultConstant {

    /**
     * 默认全局保存目录
     */
    String DEFAULT_SAVE_DIRECTORY = System.getProperty("user.home").concat("/AppData/Local/code_auto_creator/");
}
