package link.netless.convert.converter;

import com.aliyun.fc.runtime.FunctionComputeLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import link.netless.convert.Common;
import link.netless.convert.TaskType;
import link.netless.convert.ZipTool;
import link.netless.convert.bo.FailedMessage;
import link.netless.convert.bo.FcRequest;
import link.netless.convert.bo.FcResponse;
import link.netless.convert.bo.StaticResource;
import link.netless.convert.service.TableStoreKeys;
import link.netless.convert.service.TableStoreService;
import link.netless.convert.storage.StorageSDK;

import java.io.File;
import java.io.IOException;

/**
 * Created by az on 2020/9/18.
 */
public abstract class Converter<T> {
    TableStoreService tableStoreService;
    StorageSDK storageSDK;
    String taskUuid;
    Long teamId;
    Long taskId;
    String extension;
    File sourceFile;
    FunctionComputeLogger logger;
    FcRequest request;
    String tempLocalDir;

    Converter(FcRequest request, FunctionComputeLogger logger, File sourceFile, String extension, String tempLocalDir) throws JsonProcessingException {
        tableStoreService = new TableStoreService();
        this.request = request;
        this.extension = extension;
        this.sourceFile = sourceFile;
        this.logger = logger;
        this.tempLocalDir = tempLocalDir;
        teamId = request.getTeamId();
        taskId = request.getId();
        taskUuid = tableStoreService.getDynamicConversionUuidByTaskId(teamId, taskId);
        storageSDK = Common.getStorageSDK(request);

        logger.info("build static convertsion task: " + request.toString());
    }

    public FcResponse doConvert() {
        try {
            FcResponse response =  convert();
            if (request.getPack() == true) {
                zipAndUploadImages(storageSDK, taskUuid);
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Task failed: team " + teamId + ", task " + taskId);
            return buildFailedResponse( teamId, taskId, e.getMessage(), Common.getExceptionStackTrace(e));
        } finally {
            cleanTempFiles(storageSDK, tableStoreService, taskUuid);
        }
    }

    abstract FcResponse convert() throws Exception;

    abstract T getOutputFormat(String customFormat);

    FcResponse buildSucceedResponse(Long teamId, Long taskId) {
        FcResponse response = new FcResponse();
        response.setTeamId(teamId);
        response.setTaskId(taskId);
        response.setStatus(true);
        response.setTaskType(TaskType.STATIC_PPT);
        return response;
    }

    FcResponse buildFailedResponse(Long teamId, Long taskId, String errorMessage, String stack) {
        FcResponse response = new FcResponse();
        response.setTeamId(teamId);
        response.setTaskId(taskId);
        response.setStatus(false);
        response.setTaskType(TaskType.STATIC_PPT);

        FailedMessage failedMessage = new FailedMessage();
        failedMessage.setMessage(errorMessage);
        failedMessage.setStack(stack);

        response.setFailedMessage(Common.toJson(failedMessage));
        return response;
    }

    String getUploadedFilePath(String targetFolder, Boolean preview, String taskUuid, String fileName) {
        String filePath = "";
        if (targetFolder.endsWith("/")) {
            filePath += targetFolder + taskUuid;
        } else {
            filePath += targetFolder + "/" + taskUuid;
        }
        if (preview == true) {
            filePath += "/preview/" + fileName;
        } else {
            filePath += "/" + fileName;
        }
        return filePath;
    }

    void updateTableColumn(TableStoreService tableStoreService, Boolean preview, Long teamId, Long taskId, int pptIndex, StaticResource resource) {
        if (preview == true) {
            tableStoreService.updatePreviewTableColumn(teamId, taskId, pptIndex, resource);
        } else {
            tableStoreService.updateStaticTaskColumn(teamId, taskId, TableStoreKeys.ATTR_PREFIX_FILE + pptIndex, Common.toJson(resource));
        }
    }

    String zipAndUploadImages(StorageSDK storageSDK, String taskUUID) throws IOException {
        ZipTool zipTool = new ZipTool(tempLocalDir + taskUUID, logger);
        String zipFilePath = tempLocalDir + taskUUID + ".zip";
        zipTool.zip(zipFilePath);
        storageSDK.uploadFile(zipFilePath, new File(zipFilePath));
        return zipFilePath;
    }

    void cleanTempFiles(StorageSDK storageSDK, TableStoreService tableStoreService, String taskUUID) {
        if (storageSDK != null) {
            storageSDK.shutDown();
        }
        tableStoreService.shutdown();
        Common.deleteDir(new File(tempLocalDir + taskUUID));
        File zipFile = new File(tempLocalDir + taskUUID + ".zip");
        if (zipFile.exists()) {
            zipFile.delete();
        }
    }
}
