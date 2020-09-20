import com.aliyuncs.fc.client.FunctionComputeClient;
import com.aliyuncs.fc.request.InvokeFunctionRequest;
import com.aliyuncs.fc.response.InvokeFunctionResponse;
import com.aspose.pdf.ImageType;
import com.aspose.pdf.devices.Resolution;
import com.aspose.pdf.facades.PdfConverter;
import com.aspose.words.Document;
import com.aspose.words.ImageSaveOptions;
import com.aspose.words.SaveFormat;
import com.google.gson.Gson;
import link.netless.convert.*;
import link.netless.convert.bo.FcRequest;
import link.netless.convert.bo.FcResponse;
import link.netless.convert.storage.StorageDriverInput;
import link.netless.convert.storage.StorageDriverProvider;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by az on 2020/9/15.
 */
public class FunTest {
    private FunctionComputeClient client;

    public static void main(String[] args) throws Exception {
//        testLocal();
        testRemote();
//        testLocalPdf();
//        testLocalDoc();
    }

    private static void testLocal() {
        DocToImageFC d = new DocToImageFC();
        FcResponse fcResponse = d.handleRequest(null, null);
    }

    private static void testLocalDoc() throws Exception {
        com.aspose.words.License docLicense = new com.aspose.words.License();
        docLicense.setLicense(new FileInputStream("Aspose.license.lic"));
        Document doc = new Document(new File("rs.docx").getPath());
        ImageSaveOptions options = new ImageSaveOptions(SaveFormat.JPEG);
        // 单位是 dpi， 120 与原来 1.2 相同
        options.setHorizontalResolution(120);
        options.setVerticalResolution(120);
        options.setPageCount(doc.getPageCount());

        int count = doc.getPageCount();
        System.out.println("count " + count);
        for(int index = 0; index < count; index++) {
            System.out.println("save " + index);
            options.setPageIndex(index);
            int pageNumber = index + 1;
            String fileName = pageNumber + ".jpg";
            String temporaryFilePath = "test/" + File.separator + fileName;
            doc.save(temporaryFilePath, options);
        }

    }


    private static void testLocalPdf() throws Exception {
//        DocToImageFC d = new DocToImageFC();
//        FcResponse fcResponse = d.handleRequest(null, null);
        com.aspose.pdf.License license = new com.aspose.pdf.License();
        license.setLicense(new FileInputStream("Aspose.license.lic"));
        PdfConverter pdfConverter = new PdfConverter();
        pdfConverter.bindPdf("zt.pdf");
        pdfConverter.doConvert();
        pdfConverter.setResolution(new Resolution(10));
//                    pdfConverter.getNextImage(“201109P001.jpg”, ImageType.Jpeg);
//                    pdfConverter.Close()
//                    pdfConverter = Nothing
//                    PdfConverter
//                    pdfDocument.convert()
        int i = 1;
        // check if pages exist and then convert to image one by one
        while (pdfConverter.hasNextImage()) {
            String fileName = i + ".png";
            String temporaryFilePath = "test/" + File.separator + fileName;
            Common.createPathAndFile(temporaryFilePath);

            pdfConverter.getNextImage(temporaryFilePath, ImageType.getPng());

            File temporaryFile = new File(temporaryFilePath);

            BufferedImage image = ImageIO.read(temporaryFile);
            System.out.println(" scale "+ image.getWidth() + "/" + image.getHeight());

            i++;
        }
        pdfConverter.close();

    }

    private static void testRemote() {

        System.setProperty("TABLESTORE_ENDPOINT", "https://doc-convert-dev.cn-hangzhou.ots.aliyuncs.com");
        System.setProperty("TABLESTORE_AK", "");
        System.setProperty("TABLESTORE_SK", "");
        System.setProperty("TABLESTORE_CONVERT_INSTANCE", "doc-convert-dev");

        FunctionComputeClient client = new FunctionComputeClient("cn-hangzhou",
                "1039352964232466",
                "",
                "");

        InvokeFunctionRequest docToPdfReq = new InvokeFunctionRequest("fun-doc-to-image", "conversion");

        FcRequest request = new FcRequest();
        request.setId(758636L);
        request.setTeamId(177L);
        request.setTaskStatus(TaskStatus.RUNNING);
        request.setTaskType(TaskType.STATIC_PPT);
        request.setLease(1800000L);
        request.setStatusUpdatedAt(1600563849430L);

        request.setStorageDriver("");
        request.setStorageDriverId(43L);
        request.setSourceUrl("http://white-cn-doc-convert.oss-cn-hangzhou.aliyuncs.com/tempPdf/8eb45900f5ae11eabef5dded0cfc5f0e/315f31353939.pdf");
        request.setTargetBucket("white-cn-doc-convert-dev");
        request.setTargetFolder("staticConvert");
        request.setStarer("");
        request.setPack(false);

        DocToImageFC fc = new DocToImageFC();
        fc.handleRequest(request, new TestContext());

    }

}