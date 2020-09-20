package link.netless.convert.storage;

/**
 * Created by az on 2019/4/12.
 */
public class StorageDriver {
    private Long id;
    private Long teamId;
    private StorageDriverProvider provider;
    private String ak;
    private String sk;
    private String bucket;
    private String region;
    private String domain;
    private String folder;  // 用户配置的 bucket 下的路径
    private String temporaryToken;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public StorageDriverProvider getProvider() {
        return provider;
    }

    public void setProvider(StorageDriverProvider provider) {
        this.provider = provider;
    }

    public String getAk() {
        return ak;
    }

    public void setAk(String ak) {
        this.ak = ak;
    }

    public String getSk() {
        return sk;
    }

    public void setSk(String sk) {
        this.sk = sk;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getTemporaryToken() {
        return temporaryToken;
    }

    public void setTemporaryToken(String temporaryToken) {
        this.temporaryToken = temporaryToken;
    }
}
