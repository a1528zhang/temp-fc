package link.netless.convert;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by az on 2018/11/21.
 */
public class FileTypes {
    public static Map<String, String> InputFileTypes = new HashMap<String, String>();
    public static Map<String, String> OutputFileTypes = new HashMap<String, String>();

    static {
        InputFileTypes.put("application/vnd.ms-excel", "xls");
        InputFileTypes.put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xlsx");
        InputFileTypes.put("application/msword", "doc");
        InputFileTypes.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx");
        InputFileTypes.put("vnd.ms-powerpoint", "ppt");
        InputFileTypes.put("application/vnd.openxmlformats-officedocument.presentationml.presentation", "pptx");
        InputFileTypes.put("application/pdf", "pdf");
        InputFileTypes.put("application/vnd.ms-powerpoint", "pot");

        OutputFileTypes.put("image/png", "png");
    }
}
