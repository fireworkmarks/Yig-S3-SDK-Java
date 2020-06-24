package com.Demo;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.SDKGlobalConfiguration;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.*;
import org.junit.Test;

import java.io.File;

public class ObjectGlacierSample {
    private final static String ACCESS_KEY = "hehehehe";
    private final static String SECRET_KEY = "hehehehe";
    private final static String END_POINT = "oss-cn-north-2.unicloudsrv.com";
    static AmazonS3Client s3 = getAmazonS3Client(ACCESS_KEY, SECRET_KEY, END_POINT);

    static String bucketName = "javatest";
    static String objectName = "java-sdk-key";
    static String filePath = "s3Demo/src/test.rar";

    public static AmazonS3Client getAmazonS3Client(String accessKey, String secretKey, String endPoint) {
        System.setProperty(SDKGlobalConfiguration.DISABLE_CERT_CHECKING_SYSTEM_PROPERTY, "true");
        BasicAWSCredentials cred = new BasicAWSCredentials(accessKey, secretKey);
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setProtocol(Protocol.HTTP);
        AmazonS3Client s3 = new AmazonS3Client(cred,clientConfiguration);
        S3ClientOptions options = S3ClientOptions.builder().setPathStyleAccess(true).setPayloadSigningEnabled(true).disableChunkedEncoding().build();
        s3.setS3ClientOptions(options);
        s3.setEndpoint(endPoint);
        return s3;
    }

    /**
     * 上传文件归档型文件
     */
    @Test
    public void testPutObject() {
        try {
            s3.putObject(new PutObjectRequest(bucketName, objectName, filePath).withStorageClass(StorageClass.Standard)
            .withCannedAcl(CannedAccessControlList.Private));
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
        System.out.println("Test put object with storageClass Glacier done!");
    }

    /**
     *  解冻归档型对象
     *  解冻天数：1天
     */
    @Test
    public void testRestoreObject() {
        try{
            s3.restoreObject(new RestoreObjectRequest(bucketName, objectName).withExpirationInDays(1));
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
        System.out.println("Test restore object done!");
    }
}
