package com.jardin.api.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Region;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.amazonaws.auth.AWSCredentials;

@Configuration
public class AmazonConfig {

    @Bean
    public AmazonS3 s3() {

        AWSCredentials credentials = new BasicAWSCredentials(
                "AKIA2RYEXBESKOQ6VCXP",
                "bI3x5lSMOhXnrOms5NBe8IeG3mxWZxPW1T84bZlJ"
        );
        Region region = Region.SA_SaoPaulo;

        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(String.valueOf(region))
                .build();
    }
}
