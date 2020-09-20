package link.netless.convert.converter;

import com.aliyun.fc.runtime.FunctionComputeLogger;
import com.aspose.slides.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import link.netless.convert.Common;
import link.netless.convert.bo.FcRequest;
import link.netless.convert.bo.FcResponse;
import link.netless.convert.bo.StaticResource;
import link.netless.convert.exception.ConverterException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by az on 2020/9/18.
 */
public class PptConverter extends Converter<String> {
    public PptConverter(FcRequest request, FunctionComputeLogger logger, File sourceFile, String extension, String tempLocalDir) throws JsonProcessingException {
        super(request, logger, sourceFile, extension, tempLocalDir);
    }

    @Override
    FcResponse convert() throws Exception {
        License pptLicense = new License();
        pptLicense.setLicense(new FileInputStream("Aspose.license.lic"));
        Presentation pres;
        try {
            pres = new Presentation(sourceFile.getPath());
        } catch (PptUnsupportedFormatException e) {
            logger.error("Task failed: Unsupported format " + extension + " , team " + teamId + ", task " + taskId);
            return buildFailedResponse( teamId, taskId, "Unsupported format : " + extension, null);
        }
        ISlideCollection slides = pres.getSlides();

        for (ISlide slide: slides) {
            // todo 先换成 1.5，原 1.2 转出来是 1600/900， 现在 1.5 转出来是 1440/810，大小也会变大
            int pptIndex = slide.getSlideNumber();
            BufferedImage image = slide.getThumbnail(1.5f, 1.5f);
            String fileName = pptIndex + "." + getOutputFormat(request.getOutputFormat());

            String temporaryFilePath = tempLocalDir + taskUuid + File.separator + fileName;
            File temporaryFile = Common.createPathAndFile(temporaryFilePath);

            ImageIO.write(image, getOutputFormat(request.getOutputFormat()), temporaryFile);

            String uploadedFilePath = getUploadedFilePath(request.getTargetFolder(), request.getPreview(), taskUuid, fileName);
            storageSDK.uploadFile(uploadedFilePath, temporaryFile);

            StaticResource resource = new StaticResource();
            resource.setHeight(image.getHeight());
            resource.setWidth(image.getWidth());
            resource.setTargetPath(uploadedFilePath);

            updateTableColumn(tableStoreService, request.getPreview(), teamId, taskId, pptIndex, resource);
        }

        return buildSucceedResponse(teamId, taskId);
    }

    @Override
    String getOutputFormat(String customFormat) {
        if (customFormat == null || customFormat.isEmpty()) {
            return "png";
        }
        switch (customFormat) {
            case "png":
                return "png";
            case "jpg":
            case "jpeg":
                return "jpeg";
            default:
                throw new ConverterException("Unsupport output format : " + customFormat);
        }
    }
}
