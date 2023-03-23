package com.yue.oss.service;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface OssTemplate {

    // 创建bucket
    public void createBucket(String bucketName);

    // 获取所有的bucket
    public List<Bucket> getAllBuckets();

    // 通过bucket名称删除bucket
    public void removeBucket(String bucketName);

    // 上传文件
    public void putObject(String bucketName, String objectName, InputStream stream) throws IOException;

    // 获取文件
    public S3Object getObject(String bucketName,String objectName);

    // 获取对象的url
    public String getObjectUrl(String bucketName,String objectName,Integer expires);

    // 通过bucketName和ObjectName删除对象
    public void removeObject(String bucketName,String objectName) throws Exception;

    // 根据文件前置查询文件
    /**
     * @param bucketName bucket名称
     * @param prefix 前缀
     * @param recursive 是否递归查询
     * @return
     */
    public List<S3ObjectSummary> getAllObjectsByPrefix(String bucketName,String prefix,boolean recursive);

}
