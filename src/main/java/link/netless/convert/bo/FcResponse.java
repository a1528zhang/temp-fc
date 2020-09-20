package link.netless.convert.bo;

import com.fasterxml.jackson.annotation.JsonInclude;
import link.netless.convert.TaskType;

/**
 * Created by az on 2020/9/14.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FcResponse {
    private Long teamId;
    private Long taskId;
    private TaskType taskType;
    private Boolean status;
    private String failedMessage;

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getFailedMessage() {
        return failedMessage;
    }

    public void setFailedMessage(String failedMessage) {
        this.failedMessage = failedMessage;
    }
}
