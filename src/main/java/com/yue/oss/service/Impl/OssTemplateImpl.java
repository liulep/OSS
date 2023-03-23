package com.yue.oss.service.Impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.yue.oss.service.OssTemplate;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@RequiredArgsConstructor
public class OssTemplateImpl implements OssTemplate {

    private final AmazonS3 amazonS3;

    // 创建Bucket
    // AmazonS3：https://docs.aws.amazon.com/AmazonS3/latest/API/API_CreateBucket.html
    @Override
    @SneakyThrows
    public void createBucket(String bucketName) {
        if(!amazonS3.doesBucketExistV2(bucketName)){
            amazonS3.createBucket(bucketName);
        }
    }

    // 获取所有的buckets
    // AmazonS3：https://docs.aws.amazon.com/AmazonS3/latest/API/API_ListBuckets.html
    @Override
    @SneakyThrows
    public List<Bucket> getAllBuckets() {
        return amazonS3.listBuckets();
    }

    // 通过bucket名称删除Bucket
    //  AmazonS3：https://docs.aws.amazon.com/AmazonS3/latest/API/API_DeleteBucket.html
    @Override
    @SneakyThrows
    public void removeBucket(String bucketName) {
        amazonS3.deleteBucket(bucketName);
    }

    // 上传对象
    // AmazonS3：https://docs.aws.amazon.com/AmazonS3/latest/API/API_PutObject.html
    @Override
    @SneakyThrows
    public void putObject(String bucketName, String objectName, InputStream stream) throws IOException {
        putObject(bucketName,objectName,stream,stream.available(),"application/octet-stream");
    }

    // 通过bucket名称和object名称获取对象
    // AmazonS3：https://docs.aws.amazon.com/AmazonS3/latest/API/API_GetObject.html
    @Override
    @SneakyThrows
    public S3Object getObject(String bucketName, String objectName) {
        return amazonS3.getObject(bucketName,objectName);
    }

    // 获取对象的url
    // AmazonS3：https://docs.aws.amazon.com/AmazonS3/latest/API/API_GeneratePresignedUrl.html
    @Override
    @SneakyThrows
    public String getObjectUrl(String bucketName, String objectName, Integer expires) {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH,expires);
        URL url = amazonS3.generatePresignedUrl(bucketName, objectName, calendar.getTime());
        return url.toString();
    }

    // 根据bucket名称和object名称删除对象
    // AmazonS3：https://docs.aws.amazon.com/AmazonS3/latest/API/API_DeleteObject.html
    @Override
    @SneakyThrows
    public void removeObject(String bucketName, String objectName) throws Exception {
        amazonS3.deleteObject(bucketName,objectName);
    }

    // 根据bucket名称和prefix获取对象集合
    // AmazonS3：https://docs.aws.amazon.com/AmazonS3/latest/API/API_ListObjects.html
    @Override
    @SneakyThrows
    public List<S3ObjectSummary> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive) {
        ObjectListing objectListing = amazonS3.listObjects(bucketName, prefix);
        return objectListing.getObjectSummaries();
    }

    // 上传文件
    @SneakyThrows
    public PutObjectResult putObject(String bucketName,String objectName,InputStream stream,long size,String contextType){
        byte[] bytes = IOUtils.toByteArray(stream);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contextType);
        objectMetadata.setContentLength(size);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        // 上传
        return amazonS3.putObject(bucketName,objectName,byteArrayInputStream,objectMetadata);
    }
}
