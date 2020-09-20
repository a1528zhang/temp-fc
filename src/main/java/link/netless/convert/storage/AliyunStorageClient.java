package link.netless.convert.storage;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

import java.io.ByteArrayInputStream;
import java.io.File;

/**
 * Created by az on 2019/4/13.
 */
public class AliyunStorageClient implements StorageClientInterface {
    private OSS ossClient;
    private String path;
    private String storagePrefix;
    // 与 oss 在同一个 region 就可以使用内网进行访问
    private static final String regionHangZhou = "oss-cn-hangzhou";

    public AliyunStorageClient(StorageDriver storageDriver) {
        String endpoint;

        if (storageDriver.getFolder() == null || storageDriver.getFolder().equals("")) {
            path = "";
        } else {
            // todo 判断最后一个是不是分隔符，如果不是就加上
            path = storageDriver.getFolder() + File.separator;
        }
        storagePrefix = storageDriver.getDomain() + path;

        if (regionHangZhou.equals(storageDriver.getRegion())) {
            endpoint = storageDriver.getRegion() + ".aliyuncs.com";
        } else {
            endpoint = storageDriver.getRegion() + ".aliyuncs.com";
        }
        if (storageDriver.getTemporaryToken() != null && !storageDriver.getTemporaryToken().isEmpty()) {
            this.ossClient = new OSSClientBuilder().build(
                    endpoint,
                    storageDriver.getAk(),
                    storageDriver.getSk(),
                    storageDriver.getTemporaryToken());
        } else {
            this.ossClient = new OSSClientBuilder().build(
                    endpoint,
                    storageDriver.getAk(),
                    storageDriver.getSk());
        }
    }

    @Override
    public String uploadFile(String targetBucket, String targetFileUrl, File file) {
        String filePath = path + targetFileUrl;
        ossClient.putObject(targetBucket, filePath, file);
        return filePath;
    }

    @Override
    public String uploadFile(String targetBucket, String targetFileUrl, byte[] file) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(file);
        String filePath = path + targetFileUrl;
        ossClient.putObject(targetBucket, filePath, inputStream);
        return filePath;
    }

    @Override
    public Boolean doesObjectExist(String targetBucket, String targetFileUrl) {
        return ossClient.doesObjectExist(targetBucket, path + targetFileUrl);
    }

    @Override
    public String getStoragePrefix() {
        return storagePrefix;
    }

    @Override
    public void shutDown() {
        ossClient.shutdown();
    }
}
