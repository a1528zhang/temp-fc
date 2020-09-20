package link.netless.convert;

import com.aliyun.fc.runtime.FunctionComputeLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import link.netless.convert.bo.FcRequest;
import link.netless.convert.storage.StorageDriver;
import link.netless.convert.storage.StorageDriverInput;
import link.netless.convert.storage.StorageSDK;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by az on 2019/4/23.
 */
public class Common {
    private static Integer EmuToPtScale = 12700;
    private static Integer EmuToPxScale = 9525;

    public static Gson gson =  new Gson();

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static void deleteFile(File file) {
        if (file != null) {
            file.delete();
        }
    }

    public static File createPathAndFile(String filePath) throws IOException {
        File outputFile = new File(filePath);
        if(!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
        }
        if (!outputFile.isFile()) {
            outputFile.createNewFile();
        }
        return outputFile;
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    public static Float EmuToPt(Float emu) {
        return emu / EmuToPtScale;
    }

    public static String byteArrayToHex(byte[] bytes) {
        StringBuffer buf = new StringBuffer("");
        for (int i = 0; i < bytes.length; i++) {
            buf.append(Integer.toHexString((bytes[i] & 0x000000FF) | 0xFFFFFF00).substring(6));
        }
        return buf.toString();
    }

    public static String getFileExtension(InputStream fileInputStream) throws IOException, TikaException, SAXException {
        ContentHandler contenthandler = new BodyContentHandler(10*1024*1024);
        Metadata metadata = new Metadata();
        Parser parser = new AutoDetectParser();
        parser.parse(fileInputStream, contenthandler, metadata, new ParseContext());
        return FileTypes.InputFileTypes.get(metadata.get(Metadata.CONTENT_TYPE));
    }

    public static StorageSDK getStorageSDK(FcRequest request) throws JsonProcessingException {
        StorageDriver storageDriver = new StorageDriver();

        ObjectMapper objectMapper = new ObjectMapper();
        StorageDriverInput storageDriverInput = objectMapper.readValue(request.getStorageDriver(), StorageDriverInput.class);

        storageDriver.setId(request.getId());
        storageDriver.setTeamId(request.getTeamId());
        storageDriver.setProvider(storageDriverInput.getProvider());
        storageDriver.setAk(storageDriverInput.getAk());
        storageDriver.setSk(storageDriverInput.getSk());
        storageDriver.setBucket(request.getTargetBucket());
        storageDriver.setRegion(storageDriverInput.getRegion());
        storageDriver.setDomain(storageDriverInput.getDomain());
        storageDriver.setFolder(request.getTargetFolder());
        storageDriver.setTemporaryToken(request.getTemporaryToken());

        return new StorageSDK(storageDriver);
    }

    public static File inputStreamToFile(InputStream ins, String fileName) throws IOException {
        File tempFile = new File(fileName);
        OutputStream os = new FileOutputStream(tempFile);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        ins.close();
        return tempFile;
    }

    public static File downloadFile(Long teamId, Long taskId, String sourceUrl, FunctionComputeLogger logger) throws Exception {
        InputStream fileInputStream = null;

        File sourceFile = null;
        try {
            URLConnection connection = new URL(sourceUrl).openConnection();

            fileInputStream = connection.getInputStream();

            // 使用 taskId 和 teamId 作为唯一的文件名，避免冲突
            sourceFile = inputStreamToFile(fileInputStream, teamId+ "_"+ taskId);

            fileInputStream.close();

            return sourceFile;
        } catch (Exception e) {
            e.printStackTrace();
            Common.deleteFile(sourceFile);
            throw new Exception("Download source file failed", e);
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException ioException) {
                logger.error("FileInputStream close error " + ioException);
            }
        }
    }

    public static String getExceptionStackTrace(Throwable throwable) {
        if (throwable == null) {
            return "";
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        try
        {
            throwable.printStackTrace(pw);
            return sw.toString();
        } finally {
            pw.close();
        }
    }

}
