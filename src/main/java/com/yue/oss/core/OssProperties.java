package com.yue.oss.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "oss")
public class OssProperties {

    // 对象存储服务的URL
    private String endpoint;

    //区域
    private String region;

    private Boolean pathStyleAccess=true;

    // Access Key
    private String accessKey;

    // Secret Key
    private String secretKey;

    // 最大线程数，默认：100
    private Integer maxConnections=100;
}
