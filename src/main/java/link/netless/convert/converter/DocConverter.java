package link.netless.convert.converter;

import com.aliyun.fc.runtime.FunctionComputeLogger;
import com.aspose.slides.PptUnsupportedFormatException;
import com.aspose.slides.Presentation;
import com.aspose.words.Document;
import com.aspose.words.ImageSaveOptions;
import com.aspose.words.SaveFormat;
import com.aspose.words.UnsupportedFileFormatException;
import com.fasterxml.jackson.core.JsonProcessingException;
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
public class DocConverter extends Converter<Integer> {
    public DocConverter(FcRequest request, FunctionComputeLogger logger, File sourceFile, String extension, String tempLocalDir) throws JsonProcessingException {
        super(request, logger, sourceFile, extension, tempLocalDir);
    }

    @Override
    FcResponse convert() throws Exception {
        com.aspose.words.License docLicense = new com.aspose.words.License();
        docLicense.setLicense(new FileInputStream("Aspose.license.lic"));
        Document doc;
        try {
            doc = new Document(sourceFile.getPath());
        } catch (UnsupportedFileFormatException e) {
            logger.error("Task failed: Unsupported format " + extension + " , team " + teamId + ", task " + taskId);
            return buildFailedResponse( teamId, taskId, "Unsupported format : " + extension, null);
        }

        ImageSaveOptions options = new ImageSaveOptions(getOutputFormat(request.getOutputFormat()));
        // 单位是 dpi， 120 与原来 1.2 相同
        int dpi = Math.round(request.getScale() * 100);
        options.setHorizontalResolution(dpi);
        options.setVerticalResolution(dpi);
        int count = doc.getPageCount();
        options.setPageCount(count);

        for(int index = 0; index < count; index++) {
            options.setPageIndex(index);
            int pageNumber = index + 1;

            String fileName = pageNumber + "." + getOutputFormatString(request.getOutputFormat());
            String temporaryFilePath = tempLocalDir + taskUuid + File.separator + fileName;
            doc.save(temporaryFilePath, options);

            File temporaryFile = new File(temporaryFilePath);

            BufferedImage image = ImageIO.read(temporaryFile);

            String uploadedFilePath = getUploadedFilePath(request.getTargetFolder(), request.getPreview(), taskUuid, fileName);
            storageSDK.uploadFile(uploadedFilePath, temporaryFile);

            StaticResource resource = new StaticResource();
            resource.setHeight(image.getHeight());
            resource.setWidth(image.getWidth());
            resource.setTargetPath(uploadedFilePath);

            updateTableColumn(tableStoreService, request.getPreview(), teamId, taskId, pageNumber, resource);
        }
        return buildSucceedResponse(teamId, taskId);
    }

    @Override
    Integer getOutputFormat(String customFormat) {
        if (customFormat == null || customFormat.isEmpty()) {
            return SaveFormat.PNG;
        }
        switch (customFormat) {
            case "png":
                return SaveFormat.PNG;
            case "jpg":
            case "jpeg":
                return SaveFormat.JPEG;
            default:
                throw new ConverterException("Unsupport output format : " + customFormat);
        }
    }

    private String getOutputFormatString(String customFormat) {
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
