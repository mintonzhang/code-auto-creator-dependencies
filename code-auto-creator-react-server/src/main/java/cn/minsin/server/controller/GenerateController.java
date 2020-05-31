package cn.minsin.server.controller;

import cn.minsin.code_auto_creator.driver.DriverMap;
import cn.minsin.core.vo.VO;
import cn.minsin.core.web.result.Result;
import cn.minsin.server.config.DriverConfig;
import cn.minsin.server.model.WebGeneratorParams;
import cn.minsin.server.service.GenerateCodeService;
import com.baomidou.mybatisplus.annotation.DbType;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: minton.zhang
 * @since: 2020/5/30 16:48
 */
@RequestMapping("/generator")
@RestController
@RequiredArgsConstructor
public class GenerateController {

    private final GenerateCodeService generateCodeService;

    private final DriverConfig.DriverMap driverMap;

    @ApiOperation("Get supported JDBC types")
    @GetMapping("/support/jdbc")
    public Result<List<VO>> supportDataTypes() {
        DriverMap[] values = DriverMap.values();
        List<DbType> dbTypes = driverMap.getDbTypes();
        List<VO> collect = Stream.of(values).map(e -> {
            String desc = e.getDbType().getDesc();
            return VO.init("name", e.name()).put("desc", desc).put("hasDriver", dbTypes.contains(e.getDbType()));
        }).collect(Collectors.toList());
        return Result.ok(collect);
    }

    @ApiOperation("Generate and compress to zip")
    @PostMapping("/generate")
    public void generate(@RequestBody WebGeneratorParams webGeneratorParams, HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        try {
            httpServletResponse.setContentType("application/x-msdownload; charset=utf-8");
            String name = "generator-code-" + System.currentTimeMillis() + ".zip";
            httpServletResponse.setHeader("content-disposition", "attachment;filename=" + name);
            generateCodeService.generate(webGeneratorParams, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
            httpServletResponse.setContentType("application/json");
            outputStream.write(Result.error(e.getMessage()).toString().getBytes());
            outputStream.flush();
        }
    }

    @ApiOperation("Saved drivers")
    @PostMapping("/addDriver")
    public Result<String> generate(String dbTypeName, @RequestParam("driverJar") MultipartFile file) {

        String originalFilename = file.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));

        boolean jar = substring.equalsIgnoreCase(".jar");

        if (!(jar && file.getSize() > 0)) {
            return Result.fail("The file must be the driver jar. please reselect.");
        }

        DbType dbType = DbType.getDbType(dbTypeName);
        if (dbType.equals(DbType.OTHER)) {
            return Result.fail(String.format("Not support '%s'", dbTypeName));
        }

        try {
            driverMap.addAndWriteJson(dbType, file.getInputStream());
            return Result.ok();
        } catch (Exception e) {
            return Result.fail();
        }
    }

}
