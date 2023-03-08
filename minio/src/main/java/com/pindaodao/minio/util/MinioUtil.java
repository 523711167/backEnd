package com.pindaodao.minio.util;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.DeleteObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;


@Component
@Slf4j
public class MinioUtil {

    private final MinioClient minioClient;

    @Autowired
    public MinioUtil(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    /**
     * 判断当前bucketExists是否存在
     * @param bucketName bucket名字
     * @return 是否存在bucket
     * @throws IOException              读取云存储服务异常
     * @throws NoSuchAlgorithmException 缺少MD5 or SHA-256类库
     * @throws InvalidKeyException      缺少缺少HMAC SHA-256库
     */
    public boolean bucketExists(String bucketName) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        boolean exists = false;
        try {
            exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (MinioException e) {
            log.error("Minio异常--", e);
        }
        return exists;
    }

    /**
     * 上传文件
     * @param bucketName bucket名字
     * @param is 文件流
     * @param objectSize 文件流大小
     * @param filePath 文件绝对路径
     * @throws IOException
     * @throws ServerException
     * @throws InsufficientDataException
     * @throws ErrorResponseException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws InvalidResponseException
     * @throws XmlParserException
     * @throws InternalException
     */
    public void putObject(String bucketName, InputStream is, long objectSize ,String filePath) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName).object(filePath).stream(is, objectSize, -1)
                        .build());
    }

    /**
     * 创建文件夹
     * @param bucketName bucket名字
     * @param dirPath 文件夹路径
     * @throws ServerException
     * @throws InsufficientDataException
     * @throws ErrorResponseException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws InvalidResponseException
     * @throws XmlParserException
     * @throws InternalException
     */
    public void mkdir(String bucketName, String dirPath) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        try(ByteArrayInputStream bis = new ByteArrayInputStream(new byte[]{});) {
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucketName).object(dirPath).stream(
                                    bis, 0, -1)
                            .build());
        }
    }

    public void removeBucket(String bucketName) throws IOException, NoSuchAlgorithmException, InvalidKeyException, ServerException, InsufficientDataException, ErrorResponseException, InvalidResponseException, XmlParserException, InternalException {
        boolean exists = bucketExists(bucketName);
        if (exists) {
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
        }
    }

    /**
     * 删除单个文件
     * @param bucketName bucket名字
     * @param filePath 文件绝对路径
     * @throws ServerException
     * @throws InsufficientDataException
     * @throws ErrorResponseException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws InvalidResponseException
     * @throws XmlParserException
     * @throws InternalException
     */
    public void removeObject(String bucketName, String filePath) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(filePath)
                        .build());
    }

    /**
     * 批量删除文件
     * @param bucketName bucket名字
     * @param filePath 文件绝对路径
     * @throws ServerException
     * @throws InsufficientDataException
     * @throws ErrorResponseException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws InvalidResponseException
     * @throws XmlParserException
     * @throws InternalException
     */
    public void batchRemoveObject(String bucketName, List<String> filePath) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        List<DeleteObject> objects = new LinkedList<>();
        objects.add(new DeleteObject("my-objectname1"));
        objects.add(new DeleteObject("my-objectname2"));
        objects.add(new DeleteObject("my-objectname3"));


    }

    public void getBucketPolicy(String bucketName, String filePath) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        try {
            String policy =
                    minioClient.getBucketPolicy(
                            GetBucketPolicyArgs.builder().bucket(bucketName).build());
            System.out.println("Current policy: " + policy);
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
        }
    }

}
