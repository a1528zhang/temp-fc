package link.netless.convert.converter;

import com.aliyun.fc.runtime.FunctionComputeLogger;
import com.aspose.pdf.ImageType;
import com.aspose.pdf.devices.Resolution;
import com.aspose.pdf.exceptions.InvalidPdfFileFormatException;
import com.aspose.pdf.operators.EX;
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
public class PdfConverter extends Converter<ImageType> {
    public PdfConverter(FcRequest request, FunctionComputeLogger logger, File sourceFile, String extension, String tempLocalDir) throws JsonProcessingException {
        super(request, logger, sourceFile, extension, tempLocalDir);
    }

    @Override
    public FcResponse convert() throws Exception {
        com.aspose.pdf.License pdfLicense = new com.aspose.pdf.License();
        pdfLicense.setLicense(new FileInputStream("Aspose.license.lic"));
        com.aspose.pdf.facades.PdfConverter pdfConverter = new com.aspose.pdf.facades.PdfConverter();
        try {
            pdfConverter.bindPdf(sourceFile.getPath());
        } catch (InvalidPdfFileFormatException e) {
            pdfConverter.close();
            logger.error("Task failed: Unsupported format " + extension + " , team " + teamId + ", task " + taskId);
            return buildFailedResponse( teamId, taskId, "Unsupported format : " + extension, null);
        }

        pdfConverter.doConvert();
        // 单位是 dpi， 120 与原来 1.2 相同
        int dpi = Math.round(request.getScale() * 100);
        pdfConverter.setResolution(new Resolution(dpi));
        int pdfIndex = 1;
        // check if pages exist and then convert to image one by one
        try {
            while (pdfConverter.hasNextImage()) {
                String fileName = pdfIndex + "." + getOutputFormatString(request.getOutputFormat());
                String temporaryFilePath = tempLocalDir + taskUuid + File.separator + fileName;
                Common.createPathAndFile(temporaryFilePath);

                pdfConverter.getNextImage(temporaryFilePath, getOutputFormat(request.getOutputFormat()));

                File temporaryFile = new File(temporaryFilePath);

                BufferedImage image = ImageIO.read(temporaryFile);

                String uploadedFilePath = getUploadedFilePath(request.getTargetFolder(), request.getPreview(), taskUuid, fileName);
                storageSDK.uploadFile(uploadedFilePath, temporaryFile);
                logger.info("path " + uploadedFilePath);

                StaticResource resource = new StaticResource();
                resource.setHeight(image.getHeight());
                resource.setWidth(image.getWidth());
                resource.setTargetPath(uploadedFilePath);

                updateTableColumn(tableStoreService, request.getPreview(), teamId, taskId, pdfIndex, resource);
                pdfIndex++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            pdfConverter.close();
        }

        return buildSucceedResponse(teamId, taskId);
    }

    @Override
    ImageType getOutputFormat(String customFormat) {
        if (customFormat == null || customFormat.isEmpty()) {
            return ImageType.getPng();
        }
        switch (customFormat) {
            case "png":
                return ImageType.getPng();
            case "jpg":
            case "jpeg":
                return ImageType.getJpeg();
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
