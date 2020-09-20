package link.netless.convert.storage;

/**
 * Created by az on 2019/4/12.
 */
public enum StorageDriverProvider {
    ALIYUN("aliyun"), QINIU("qiniu"), AWS("aws"), NETLESS("netless") ;

    private String value;
    private StorageDriverProvider(String name) {
        this.value = name;
    }

    public String getValue() {
        return value;
    }

    public void setName(String name) {
        this.value = name;
    }

    public static StorageDriverProvider getStorageDriverProvider(String value) {
        for (StorageDriverProvider p : StorageDriverProvider.values()) {
            if (p.getValue().equals(value)) {
                return p;
            }
        }
        return null;
    }
}
