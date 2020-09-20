package link.netless.convert;

import com.aliyun.fc.runtime.FunctionComputeLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by az on 2020/5/21.
 */
public class ZipTool {
    private List<String> fileList = new ArrayList<>();
    private String sourceFolder;
    private FunctionComputeLogger logger;

    public ZipTool(String sourceFolder, FunctionComputeLogger logger) {
        this.sourceFolder = sourceFolder;
        this.logger = logger;
    }

    public void zip(String zipFile) throws IOException {
        generateFileList(new File(sourceFolder));

        byte[] buffer = new byte[1024];
        String source = new File(sourceFolder).getName();
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(fos);

            logger.info("Output to Zip : " + zipFile);
            FileInputStream in = null;

            for (String file: fileList) {
                ZipEntry ze = new ZipEntry(source + File.separator + file);
                zos.putNextEntry(ze);
                try {
                    in = new FileInputStream(sourceFolder + File.separator + file);
                    int len;
                    while ((len = in .read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                } finally {
                    in.close();
                }
            }

            zos.closeEntry();
            logger.info("Folder successfully compressed");

        } catch (IOException ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            try {
                if (zos != null) {
                    zos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }
    }

    public void generateFileList(File node) {
        if (node.isFile()) {
            fileList.add(generateZipEntry(node.toString()));
        }

        if (node.isDirectory()) {
            String[] subNote = node.list();
            for (String filename: subNote) {
                generateFileList(new File(node, filename));
            }
        }
    }

    private String generateZipEntry(String file) {
        return file.substring(sourceFolder.length() + 1, file.length());
    }
}
