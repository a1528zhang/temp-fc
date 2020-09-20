package link.netless.convert.bo;

import com.fasterxml.jackson.annotation.JsonInclude;
import link.netless.convert.TaskStatus;
import link.netless.convert.TaskType;

/**
 * Created by az on 2020/9/14.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FcRequest {
    private Long id;
    private Long teamId;
    private TaskStatus taskStatus;
    private TaskType taskType;
    private Long lease;
    private Long statusUpdatedAt;
    // 传入是 json 字符串
    private String storageDriver;
    private Long storageDriverId;
    private String sourceUrl;
    private String targetBucket;
    private String targetFolder;
    private String starer;
    private Boolean preview = false;
    private Float scale = 1.2f;
    private String outputFormat = "png";
    private Boolean pack = false;
    private String temporaryToken;
    private String starter;

    @Override
    public String toString() {
        return "link.netless.convert.bo.FcRequest{" +
                "id=" + id +
                ", teamId=" + teamId +
                ", taskStatus=" + taskStatus +
                ", taskType=" + taskType +
                ", lease=" + lease +
                ", statusUpdatedAt=" + statusUpdatedAt +
                ", storageDriver=" + storageDriver +
                ", storageDriverId=" + storageDriverId +
                ", sourceUrl='" + sourceUrl + '\'' +
                ", targetBucket='" + targetBucket + '\'' +
                ", targetFolder='" + targetFolder + '\'' +
                ", starer='" + starer + '\'' +
                ", preview=" + preview +
                ", scale=" + scale +
                ", outputFormat='" + outputFormat + '\'' +
                ", pack=" + pack +
                ", starter="+ starter +
                '}';
    }

    public String getStarter() {
        return starter;
    }

    public void setStarter(String starter) {
        this.starter = starter;
    }

    public String getTemporaryToken() {
        return temporaryToken;
    }

    public void setTemporaryToken(String temporaryToken) {
        this.temporaryToken = temporaryToken;
    }

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

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public Long getLease() {
        return lease;
    }

    public void setLease(Long lease) {
        this.lease = lease;
    }

    public Long getStatusUpdatedAt() {
        return statusUpdatedAt;
    }

    public void setStatusUpdatedAt(Long statusUpdatedAt) {
        this.statusUpdatedAt = statusUpdatedAt;
    }

    public String getStorageDriver() {
        return storageDriver;
    }

    public void setStorageDriver(String storageDriver) {
        this.storageDriver = storageDriver;
    }

    public Long getStorageDriverId() {
        return storageDriverId;
    }

    public void setStorageDriverId(Long storageDriverId) {
        this.storageDriverId = storageDriverId;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getTargetBucket() {
        return targetBucket;
    }

    public void setTargetBucket(String targetBucket) {
        this.targetBucket = targetBucket;
    }

    public String getTargetFolder() {
        return targetFolder;
    }

    public void setTargetFolder(String targetFolder) {
        this.targetFolder = targetFolder;
    }

    public String getStarer() {
        return starer;
    }

    public void setStarer(String starer) {
        this.starer = starer;
    }

    public Boolean getPreview() {
        return preview;
    }

    public void setPreview(Boolean preview) {
        this.preview = preview;
    }

    public Float getScale() {
        return scale;
    }

    public void setScale(Float scale) {
        this.scale = scale;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    public Boolean getPack() {
        return pack;
    }

    public void setPack(Boolean pack) {
        this.pack = pack;
    }
}
