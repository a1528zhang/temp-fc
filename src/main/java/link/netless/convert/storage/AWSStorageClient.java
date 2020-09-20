package link.netless.convert.storage;


import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;

/**
 * Created by az on 2020/9/4.
 */
public class AWSStorageClient implements StorageClientInterface {
    private String path;
    private S3Client s3Client;
    private String storagePrefix;
    public AWSStorageClient(StorageDriver storageDriver) {
        if (storageDriver.getFolder() == null || storageDriver.getFolder().equals("")) {
            path = "";
        } else {
            path = storageDriver.getFolder() + File.separator;
        }
        storagePrefix = storageDriver.getDomain() + path;
        AwsCredentials credentials;
        if (storageDriver.getTemporaryToken() != null && !storageDriver.getTemporaryToken().isEmpty()) {
            // 临时 token 不为空就是临时凭证
            credentials = AwsSessionCredentials.create(storageDriver.getAk(), storageDriver.getSk(), storageDriver.getTemporaryToken());
        } else{
            credentials = AwsBasicCredentials.create(storageDriver.getAk(), storageDriver.getSk());
        }
        s3Client = S3Client.builder().region(Region.CN_NORTH_1).credentialsProvider(StaticCredentialsProvider.create(credentials)).build();
    }

    @Override
    public String uploadFile(String targetBucket, String targetFileUrl, File file) {
        String filePath = path + targetFileUrl;
        s3Client.putObject(PutObjectRequest.builder()
                .bucket(targetBucket)
                .key(filePath)
                .build(), RequestBody.fromFile(file));
        return filePath;
    }

    @Override
    public String uploadFile(String targetBucket, String targetFileUrl, byte[] file) {
        String filePath = path + targetFileUrl;
        s3Client.putObject(PutObjectRequest.builder().bucket(targetBucket).key(filePath).build(),
                RequestBody.fromBytes(file));
        return filePath;
    }

    @Override
    public Boolean doesObjectExist(String targetBucket, String targetFileUrl) {
        try {
            // 没有直接判断文件存在的方法，参考 https://stackoverflow.com/questions/26726862/how-to-determine-if-object-exists-aws-s3-node-js-sdk
            s3Client.headObject(HeadObjectRequest.builder().bucket(targetBucket).key(path + targetFileUrl).build());
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        }
    }

    @Override
    public String getStoragePrefix() {
        return storagePrefix;
    }

    @Override
    public void shutDown() {
        s3Client.close();
    }
}
