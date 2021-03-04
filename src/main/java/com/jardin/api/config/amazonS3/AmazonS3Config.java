package com.jardin.api.config.amazonS3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Region;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonS3Config {

  private static final String accessKey = "AKIA2RYEXBESKOQ6VCXP";
  private static final String secretKey = "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";
  private static final String region = "s3-sa-east-1";

  public static String getRegion() { return region; }

  @Bean
  public AmazonS3 s3() {
    AWSCredentials credentials = new BasicAWSCredentials(
      accessKey,
      secretKey
    );
    Region region = Region.SA_SaoPaulo;

    return AmazonS3ClientBuilder
      .standard()
      .withCredentials(new AWSStaticCredentialsProvider(credentials))
      .withRegion(String.valueOf(region))
      .build();
  }
}
