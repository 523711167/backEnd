import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class TestMain {

    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/test?&useSSL=false", "root", "123456")
                .globalConfig(builder -> {
                    builder.author("拼叨叨") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .commentDate("yyyy-MM-dd")
                            .outputDir("/Users/pxx/temporary/"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("org.pindaodao") // 设置父包名
                            .moduleName("admin") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "/Users/pxx/temporary/xml/")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder
                            .addInclude("user", "role", "userrole") // 设置需要生成的表名
                    .entityBuilder()
                            .enableFileOverride()
                            .enableLombok();
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }

}
