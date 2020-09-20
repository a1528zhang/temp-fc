package link.netless.convert.storage;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import link.netless.convert.exception.ConverterException;

import java.io.File;

/**
 * Created by az on 2019/4/13.
 * 七牛 sdk 无法使用临时凭证来查看文件信息，只能使用 aksk
 * 因此七牛只能支持用户配置好 aksk 到 netless
 */
public class QiniuStorageClient implements StorageClientInterface {
    private UploadManager uploadManager;
    private Auth auth;
    private Configuration cfg;
    private String path;
    private String storagePrefix;

    public QiniuStorageClient(StorageDriver storageDriver) {
        if (storageDriver.getFolder() == null || storageDriver.getFolder().equals("")) {
            path = "";
        } else {
            path = storageDriver.getFolder() + File.separator;
        }
        storagePrefix = storageDriver.getDomain() + path;
        Zone zone;
        switch (storageDriver.getRegion()) {
            case "华东":
                zone = Zone.zone0();
                break;
            case "华北":
                zone = Zone.zone1();
                break;
            case "华南":
                zone = Zone.zone2();
                break;
            case "北美":
                zone = Zone.zoneNa0();
                break;
            case "东南亚":
                zone = Zone.zoneAs0();
                break;
            default:
                throw new ConverterException("Unknown link.netless.convert.storage driver region : " + storageDriver.getRegion());
        }
        cfg = new Configuration(zone);
        this.uploadManager = new UploadManager(cfg);
        this.auth = Auth.create(storageDriver.getAk(), storageDriver.getSk());
    }

    @Override
    public String uploadFile(String targetBucket, String targetFileUrl, File file) {
        String filePath = path + targetFileUrl;
        // 使用覆盖上传
        String upToken = auth.uploadToken(targetBucket, filePath);

        try {
            uploadManager.put(file, filePath, upToken);
            return filePath;
        } catch (QiniuException e) {
            throw new ConverterException(e);
        }
    }

    @Override
    public String uploadFile(String targetBucket, String targetFileUrl, byte[] file) {
        String filePath = path + targetFileUrl;
        // 使用覆盖上传
        String upToken = auth.uploadToken(targetBucket, filePath);

        try {
            uploadManager.put(file, filePath, upToken);
            return filePath;
        } catch (QiniuException e) {
            throw new ConverterException(e);
        }
    }

    @Override
    public Boolean doesObjectExist(String targetBucket, String targetFileUrl) {
        try {
            BucketManager bucketManager = new BucketManager(auth, cfg);

            FileInfo fileInfo = bucketManager.stat(targetBucket, path + targetFileUrl);
            return fileInfo != null;
        }  catch (QiniuException e) {
            throw new ConverterException(e);
        }
    }

    @Override
    public String getStoragePrefix() {
        return storagePrefix;
    }

    @Override
    public void shutDown() {}
}


