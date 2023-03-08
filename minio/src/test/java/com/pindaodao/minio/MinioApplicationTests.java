package com.pindaodao.minio;

import com.pindaodao.minio.util.MinioUtil;
import io.minio.*;
import io.minio.errors.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class MinioApplicationTests {

    public static final String WOSHINIDIE = "woshinidie";

    @Resource
    MinioUtil minioUtil;

    @Resource
    private MinioClient minioClient;

    @Test
    void contextLoads() throws NoSuchAlgorithmException, InvalidKeyException, IOException, ServerException, InsufficientDataException, ErrorResponseException, InvalidResponseException, XmlParserException, InternalException {
        minioUtil.mkdir("woshinidie", "path/to/to/");
    }

    @Test
    void contextL1oads() throws NoSuchAlgorithmException, InvalidKeyException, IOException, ServerException, InsufficientDataException, ErrorResponseException, InvalidResponseException, XmlParserException, InternalException {
        minioUtil.removeObject("woshinidie", "21321/代码.zip");
    }

    @Test
    void test1() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        InputStream is = Files.newInputStream(Paths.get("/Users/pxx/code/pl/bases/base/src/main/java/org/example/data/structure/左旋转.mp4"));
        minioUtil.putObject("woshinidie", is, is.available(), "/我是没有的文件/1.mp4");
    }

}
