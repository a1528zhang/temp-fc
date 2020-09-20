package link.netless.convert.storage;

import java.io.File;

/**
 * Created by az on 2019/4/13.
 */
public interface StorageClientInterface {
    String uploadFile(String targetBucket, String targetFileUrl, File file);

    String uploadFile(String targetBucket, String targetFileUrl, byte[] file);

    Boolean doesObjectExist(String targetBucket, String targetFileUrl);

    /**
     * 获取存储地址前缀，即 domain + targetfolder
     * @return
     */
    String getStoragePrefix();

    void shutDown();
}
