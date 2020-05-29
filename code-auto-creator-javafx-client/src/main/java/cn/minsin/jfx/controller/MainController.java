package cn.minsin.jfx.controller;

import cn.minsin.code_auto_creator.core.MyAutoGenerator;
import cn.minsin.code_auto_creator.core.MyDataSourceConfig;
import cn.minsin.core.tools.StringUtil;
import cn.minsin.jfx.constant.GlobalVariables;
import cn.minsin.jfx.model.ClientGeneratorParams;
import cn.minsin.openjfx.tools.FileExtensionUtils;
import cn.minsin.openjfx.tools.MButtonType;
import cn.minsin.openjfx.tools.MDialogs;
import cn.minsin.openjfx.tools.MFileDialog;
import cn.minsin.openjfx.tools.MRadioButton;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * @author: minton.zhang
 * @since: 2020/4/11 21:47
 */
public class MainController implements Initializable {

    public ComboBox<String> databaseType;

    public PasswordField password;

    public TextField username;

    public TextField url;


    public TextField globalDir;
    public TextField entityBaseClass;
    public TextField logicDelete;

    public TextField entitySuffix;

    public TextField entityDir;

    public TextField entityPackage;

    public TextArea tablePrefixes;
    public TextArea tables;

    public TextField mapperBaseClass;
    public TextField mapperXmlDir;
    public TextField mapperDir;
    public TextField mapperPackage;
    public TextField mapperSuffix;

    public TextField diverPath;

    private DataSourceConfig dataSourceConfig;

    public ToggleGroup isDate = MRadioButton.create();

    public ToggleGroup isSkipView = MRadioButton.create();

    public ToggleGroup isUnderlineToCamel = MRadioButton.create();

    public ToggleGroup isEnableLombok = MRadioButton.create();

    public ToggleGroup isOverride = MRadioButton.create();

    private ClientGeneratorParams clientGeneratorParams;

    public void testConnection(MouseEvent actionEvent) {
        this.checkDatasource(true);
    }

    public void about(ActionEvent actionEvent) throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://github.com/mintonzhang/code-auto-creator"));
    }

    public void commit(ActionEvent actionEvent) {
        if (dataSourceConfig == null) {
            this.checkDatasource(false);
            return;
        }
        String entityDirValue = entityDir.getText();
        String mapperDirValue = mapperDir.getText();
        String mapperXmlDirValue = mapperXmlDir.getText();
        String globalDirValue = globalDir.getText();
        if (StringUtil.isBlank(globalDirValue)) {
            if (StringUtil.isBlank(entityDirValue)) {
                MDialogs.showWarning("请选择实体类保存目录");
                entityDir.requestFocus();
                return;
            }
            if (StringUtil.isBlank(mapperDirValue)) {
                MDialogs.showWarning("请选择Mapper保存目录");
                mapperDir.requestFocus();
                return;
            }
            if (StringUtil.isBlank(mapperXmlDirValue)) {
                MDialogs.showWarning("请选择MapperXml保存目录");
                mapperXmlDir.requestFocus();
                return;
            }

        }


        //package
        String entityPackageText = entityPackage.getText();
        String mapperPackageText = mapperPackage.getText();


        Boolean isDateValue = MRadioButton.getUserDataValue(isDate, Boolean.class);
        Boolean isEnableLombokValue = MRadioButton.getUserDataValue(isEnableLombok, Boolean.class);
        Boolean isSkipViewValue = MRadioButton.getUserDataValue(isSkipView, Boolean.class);
        Boolean isUnderlineToCamelValue = MRadioButton.getUserDataValue(isUnderlineToCamel, Boolean.class);
        Boolean isOverrideValue = MRadioButton.getUserDataValue(isOverride, Boolean.class);


        //父类
        String entityBaseClassText = entityBaseClass.getText();
        String mapperBaseClassText = mapperBaseClass.getText();


        //后缀
        String entitySuffixText = entitySuffix.getText();
        String mapperSuffixText = mapperSuffix.getText();

        //逻辑删除

        String logicDeleteText = logicDelete.getText();

        //表、视图
        String tablesText = tables.getText();

        String[] tableArray = StringUtil.isBlank(tablesText) ? null : tablesText.split("\n");

        //表前缀

        String tablePrefixesText = tablePrefixes.getText();

        String[] tablePrefixesArray = StringUtil.isBlank(tablePrefixesText) ? null : tablePrefixesText.split("\n");

        try {
            clientGeneratorParams.setGlobalDir(globalDirValue)
                    .setEntityDir(entityDirValue)
                    .setMapperDir(mapperDirValue)
                    .setXmlDir(mapperXmlDirValue);
            clientGeneratorParams.setDate(isDateValue)
                    .setSkipView(isSkipViewValue)
                    .setUnderlineToCamel(isUnderlineToCamelValue)
                    .setEnableLombok(isEnableLombokValue)
                    .setOverrideFile(isOverrideValue)
                    .setEntityBaseClass(entityBaseClassText)
                    .setMapperBaseClass(mapperBaseClassText)
                    .setEntityPackage(entityPackageText)
                    .setMapperPackage(mapperPackageText)
                    .setEntitySuffix(entitySuffixText)
                    .setMapperSuffix(mapperSuffixText)
                    .setLogicDeleteFiled(logicDeleteText)
                    .setTables(tableArray)
                    .setTablePrefix(tablePrefixesArray);
            new MyAutoGenerator(dataSourceConfig, clientGeneratorParams).setGlobalDir(globalDirValue).run((e) -> {
            });

            //SaveFile
            GlobalVariables.save(JSON.toJSONString(clientGeneratorParams));
            MButtonType mButtonType = MDialogs.showConfirm("生成成功,是否打开对应目录?");
            if (mButtonType.getKey().equals(MButtonType.OK.getKey())) {
                if (StringUtil.isBlank(globalDirValue)) {
                    this.openDir(entityDirValue);
                    this.openDir(mapperDirValue);
                    this.openDir(mapperXmlDirValue);
                } else {
                    this.openDir(globalDirValue);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            MDialogs.showError(e.getMessage());
        }
    }

    public void chooseFile(MouseEvent actionEvent) {
        TextField source = (TextField) actionEvent.getSource();
        String id = source.getId();
        if (id.equals("diverPath")) {
            MFileDialog.chooseFile(source, FileExtensionUtils.jar());
        } else {
            MFileDialog.chooseDirectory(source);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resetSelectButton();
        //加载文件
        ClientGeneratorParams read = GlobalVariables.read();
        MButtonType mButtonType = MDialogs.showConfirm("检测到您上次使用的配置,是否应用?");
        if (read != null && mButtonType.getKey().equals(MButtonType.OK.getKey())) {
            this.clientGeneratorParams = read;
            MRadioButton.setRadioButtonSelect(isDate, read.isDate);
            MRadioButton.setRadioButtonSelect(isUnderlineToCamel, read.isUnderlineToCamel);
            MRadioButton.setRadioButtonSelect(isSkipView, read.isSkipView);
            MRadioButton.setRadioButtonSelect(isOverride, read.isOverrideFile);
            MRadioButton.setRadioButtonSelect(isEnableLombok, read.isEnableLombok);
            globalDir.setText(read.globalDir);
            entityDir.setText(read.entityDir);
            mapperDir.setText(read.mapperDir);
            mapperXmlDir.setText(read.xmlDir);
            entityBaseClass.setText(read.entityBaseClass);
            mapperBaseClass.setText(read.mapperBaseClass);
            entityPackage.setText(read.entityPackage);
            mapperPackage.setText(read.mapperPackage);

            entitySuffix.setText(read.entitySuffix);
            mapperSuffix.setText(read.mapperSuffix);
            logicDelete.setText(read.logicDeleteFiled);
            if (read.tables != null) {
                tables.setText(String.join("\n", read.tables));
            }
            if (read.tablePrefix != null) {
                tablePrefixes.setText(String.join("\n", read.tablePrefix));
            }
            databaseType.setValue(read.databaseTypeValue);
            this.url.setText(read.urlText);
            username.setText(read.usernameText);
            password.setText(read.passwordText);
            diverPath.setText(read.diverPathText);
        } else {
            this.reset(null);
        }
    }

    private void checkDatasource(boolean successTips) {
        String urlText = url.getText();
        if (StringUtil.isBlank(urlText)) {
            MDialogs.showWarning("请输入URL连接语句");
            url.requestFocus();
            return;
        }

        String usernameText = username.getText();
        if (StringUtil.isBlank(usernameText)) {
            MDialogs.showWarning("请输入用户名");
            username.requestFocus();
            return;
        }

        String passwordText = password.getText();
        if (StringUtil.isBlank(passwordText)) {
            MDialogs.showWarning("请输入密码");
            password.requestFocus();
            return;
        }
        String databaseTypeValue = databaseType.getValue();
        String diverPathText = diverPath.getText();

        try {
            DbType dbType = DbType.getDbType(databaseTypeValue.split("\\|")[0]);
            DataSourceConfig dataSourceConfig = MyDataSourceConfig.of(urlText, usernameText, passwordText, dbType).setDriverWithPath(diverPathText);
            dataSourceConfig.getConn();
            if (successTips) {
                MDialogs.showInformation("连接成功");
            }
            this.dataSourceConfig = dataSourceConfig;
            this.clientGeneratorParams.urlText = urlText;
            this.clientGeneratorParams.usernameText = usernameText;
            this.clientGeneratorParams.passwordText = passwordText;
            this.clientGeneratorParams.databaseTypeValue = databaseTypeValue;
            this.clientGeneratorParams.diverPathText = diverPathText;
        } catch (Exception e) {
            MDialogs.showException(e);
        }
    }

    private void openDir(String path) throws IOException {
        Desktop.getDesktop().open(new File(path));
    }

    private void resetSelectButton() {
        List<String> collect = Arrays.stream(DbType.values()).map(e -> e.getDb().concat("|").concat(e.getDesc())).collect(Collectors.toList());

        ObservableList<String> strings = FXCollections.observableArrayList(collect);
        //添加选择数据
        databaseType.setItems(strings);
        databaseType.setValue(strings.get(0));
    }


    public void reset(MouseEvent mouseEvent) {
        clientGeneratorParams = new ClientGeneratorParams();
        MRadioButton.setRadioButtonSelect(isDate, true);
        MRadioButton.setRadioButtonSelect(isUnderlineToCamel, true);
        MRadioButton.setRadioButtonSelect(isSkipView, true);
        MRadioButton.setRadioButtonSelect(isOverride, false);
        MRadioButton.setRadioButtonSelect(isEnableLombok, true);
        globalDir.clear();
        entityDir.clear();
        mapperDir.clear();
        mapperXmlDir.clear();
        entityBaseClass.clear();
        mapperBaseClass.clear();
        entityPackage.setText("cn.minsin.po");
        mapperPackage.setText("cn.minsin.mapper");

        entitySuffix.setText("PO");
        mapperSuffix.setText("Mapper");
        logicDelete.clear();
        tables.clear();
        tablePrefixes.clear();
        this.resetSelectButton();
        username.setText("root");
        password.clear();
        diverPath.clear();
    }
}
