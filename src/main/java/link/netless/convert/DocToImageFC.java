package link.netless.convert;

import com.aliyun.fc.runtime.Context;
import com.aliyun.fc.runtime.FunctionComputeLogger;
import com.aliyun.fc.runtime.PojoRequestHandler;
import link.netless.convert.bo.FailedMessage;
import link.netless.convert.bo.FcRequest;
import link.netless.convert.bo.FcResponse;
import link.netless.convert.converter.*;
import java.io.*;

import static link.netless.convert.Common.deleteFile;
import static link.netless.convert.Common.getFileExtension;

/**
 * Created by az on 2020/9/14.
 */
public class DocToImageFC implements PojoRequestHandler<FcRequest, FcResponse> {
    private FunctionComputeLogger logger;
    public static final String tempLocalDir = "staticConvert/";
    @Override
    public FcResponse handleRequest(FcRequest request, Context context) {
        Long teamId = request.getTeamId();
        Long taskId = request.getId();
        logger = context.getLogger();
        logger.info("start static convertsion task: " + request.toString());
        File sourceFile = null;
        String extension;
        try {
            sourceFile = Common.downloadFile(teamId, taskId, request.getSourceUrl(), logger);
            extension = getFileExtension(new FileInputStream(sourceFile));
        } catch (Exception e) {
            deleteFile(sourceFile);
            e.printStackTrace();
            logger.error("Download file "+ request.getSourceUrl() +" failed, team " + teamId + ", task " + taskId);
            return buildFailedResponse( teamId, taskId, "Download file failed ", Common.getExceptionStackTrace(e));
        }
        try {
            switch (extension) {
                case "pdf":
                    Converter pdfConverter = new PdfConverter(request, logger, sourceFile, extension, tempLocalDir);
                    return pdfConverter.doConvert();
                case "ppt":
                case "pptx":
                    Converter pptConverter = new PptConverter(request, logger, sourceFile, extension, tempLocalDir);
                    return pptConverter.doConvert();
                case "doc":
                case "docx":
                    Converter docConverter = new DocConverter(request, logger, sourceFile, extension, tempLocalDir);
                    return docConverter.doConvert();
                default:
                    logger.error("not support file format : " + extension);
                    return buildFailedResponse( teamId, taskId, "Unsupported format : " + extension, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Convert task failed, team " + teamId + ", task " + taskId);
            return buildFailedResponse(teamId, taskId, "Convert task failed ", Common.getExceptionStackTrace(e));
        } finally {
            deleteFile(sourceFile);
        }

    }

    private FcResponse buildFailedResponse(Long teamId, Long taskId, String errorMessage, String stack) {
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

}

