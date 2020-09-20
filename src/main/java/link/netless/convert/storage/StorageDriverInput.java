package link.netless.convert.storage;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by az on 2020/9/14.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StorageDriverInput {
    private Long id;
    private Long teamId;
    private String ak;
    private String sk;
    private String bucket;
    private String region;
    private String domain;
    private StorageDriverProvider provider;

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

    public StorageDriverProvider getProvider() {
        return provider;
    }

    public void setProvider(StorageDriverProvider provider) {
        this.provider = provider;
    }

    @Override
    public String toString() {
        return "link.netless.convert.storage.StorageDriver{" +
                "id=" + id +
                ", teamId=" + teamId +
                ", ak='" + ak + '\'' +

                ", bucket='" + bucket + '\'' +
                ", region='" + region + '\'' +
                ", domain='" + domain + '\'' +
                ", provider=" + provider +
                '}';
    }
}
