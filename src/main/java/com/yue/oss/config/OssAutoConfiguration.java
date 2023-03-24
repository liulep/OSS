package com.yue.oss.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.yue.oss.core.OssProperties;
import com.yue.oss.service.Impl.OssTemplateImpl;
import com.yue.oss.service.OssTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(OssProperties.class)
public class OssAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AmazonS3 ossClient(OssProperties ossProperties){
        // 客户端配置
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setMaxConnections(ossProperties.getMaxConnections());
        // url以及region配置
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(ossProperties.getEndpoint(), ossProperties.getRegion());
        // 凭证配置
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(ossProperties.getAccessKey(), ossProperties.getSecretKey());
        AWSStaticCredentialsProvider awsStaticCredentialsProvider = new AWSStaticCredentialsProvider(basicAWSCredentials);
        // build amazonS3Client客户端
        return AmazonS3Client.builder()
                .withEndpointConfiguration(endpointConfiguration)
                .withClientConfiguration(clientConfiguration)
                .withCredentials(awsStaticCredentialsProvider)
                .disableChunkedEncoding()
                .withPathStyleAccessEnabled(ossProperties.getPathStyleAccess())
                .build();
    }

    @Bean
    @ConditionalOnBean(AmazonS3.class)
    public OssTemplate ossTemplate(@Qualifier("ossClient") AmazonS3 amazonS3){
        return new OssTemplateImpl(amazonS3);
    }
}
