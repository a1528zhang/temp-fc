package link.netless.convert.storage;

import link.netless.convert.exception.ConverterException;

import java.io.File;

/**
 * Created by az on 2019/4/13.
 */
public class StorageSDK {
    private StorageClientInterface storageClient;

    private StorageDriver storageDriver;

    public StorageSDK(StorageDriver customStorageDriver) throws ConverterException {
        this.storageDriver = customStorageDriver;
        switch (customStorageDriver.getProvider()) {
            case NETLESS:
            case ALIYUN:
                this.storageClient = new AliyunStorageClient(customStorageDriver);
                break;
            case QINIU:
                this.storageClient = new QiniuStorageClient(customStorageDriver);
                break;
            case AWS:
                this.storageClient = new AWSStorageClient(customStorageDriver);
                break;
            default:
                throw new ConverterException("Unknown link.netless.convert.storage driver provider : " + customStorageDriver.getProvider());
        }
    }

    /**
     * 返回值没有域名前缀
     */
    public String uploadFile(String targetFileUrl, File file) {
        String filePath = storageClient.uploadFile(storageDriver.getBucket(), targetFileUrl, file);
        return filePath;
    }

    public String uploadFile(String targetFileUrl, byte[] file) {
        String filePath = storageClient.uploadFile(storageDriver.getBucket(), targetFileUrl, file);
        return filePath;
    }

    /**
     * 用户和 netless 资源同时存在才算存在
     * @param targetFileUrl
     * @return
     */
    public Boolean doesObjectExist(String targetFileUrl) {
        Boolean exist =  storageClient.doesObjectExist(storageDriver.getBucket(), targetFileUrl);
        return exist;
    }

    public StorageDriver getStorageDriver() {
        return storageDriver;
    }

    public Boolean useDefaultStorageDriver() {
        return StorageDriverProvider.NETLESS.equals(storageDriver.getProvider());
    }

    public void shutDown() {
        storageClient.shutDown();
    }

    /**
     * 获取存储前缀，即用户配置的 domain + targetFolder
     * @return
     */
    public String getStoragePrefix() {
        return storageClient.getStoragePrefix();
    }

}
