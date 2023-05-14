import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class TestMain {

    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:postgresql://192.168.0.20:54321/postgres?&useSSL=false", "postgres", "password")
                .globalConfig(builder -> {
                    builder.author("pdd") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .commentDate("yyyy-MM-dd")
                            .outputDir("/Users/pxx/temporary/"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.hngd") // 设置父包名
                            .moduleName("user") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "/Users/pxx/temporary/xml/")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder
                            .addInclude(
                                    "tb_role_device_group_rela"
                                    //"tb_enterpise",
                                    //"tb_user",
                                    //"tb_device_group",
                                    //"tb_role",
                                    //"tb_user_role_rela",
                                    //"tb_user_expand",
                                    //"tb_organization",
                                    //"tb_device",
                                    //"tb_button",
                                    //"tb_menu",
                                    //"tb_device_group",
                                    //"tb_interface",
                                    //"tb_role_menu_rela",
                                    //"tb_role_device_rela",
                                    //"tb_role_interface_rela",
                                    //"tb_role_button_rela"
                            ) // 设置需要生成的表名
                            .addTablePrefix("tb_")
                            .entityBuilder()
                            .idType(IdType.ASSIGN_UUID)
                            .enableFileOverride()
                            .enableLombok();
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }

}
