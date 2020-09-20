package link.netless.convert.service;

/**
 * Created by az on 2019/2/16.
 */
public class TableStoreKeys {
    public static final String MEDIA_TRANSCODE_PROGRESS_TABLE_NAME = "mediaTranscodeProgress";

    public static final String DYNAMIC_CONVERSION_TASK_TABLE_NAME = "dynamicConversionTask";
    public static final String STATIC_CONVERSION_TASK_TABLE_NAME = "staticConversionTask";
    public static final String PREVIEW_TABLE_NAME = "conversionTaskPreview";
    public static final String MEDIA_JOB_ID_TASK_ID_MAP_TABLE_NAME = "mediaTransJobIdTaskIdMap";
    public static final String MEDIA_TRANSCODE_JOB_TABLE_NAME = "mediaTranscodeJob";
    public static final String RESOURCE_PACKAGING_STATE_TABLE_NAME = "resourcePackagingState";

    // 表的索引名
    public static final String CONVERSION_START_TIME_INDEX = "startTime_state";

    // 转换文件 uuid 与 taskid 映射关系表
    public static final String FILE_UUID_TASK_ID_MAP_TABLE_NAME = "fileUuidTaskIdMap";

    public static final String CONVERT_LOG_TABLE_NAME = "conversionTaskLog";
    public static final String CONVERSION_TASK_LOG_TABLE_INDEX = "startTime_state";

    public static final String PK_MEDIA_TRANSCODE_JOB_ID = "jobId";
    public static final String PK_TEAM_ID = "teamId";
    public static final String PK_TASK_ID = "taskId";
    public static final String PK_FILE_UUID = "uuid";
    public static final String PK_PAGE_INDEX = "pageIndex";

    public static final String ATTR_HEIGHT = "height";
    public static final String ATTR_WIDTH = "width";
    public static final String ATTR_PATH = "path";
    public static final String ATTR_TEAM_ID = "teamId";
    public static final String ATTR_TASK_ID = "taskId";
    public static final String ATTR_SOURCE_URL = "sourceUrl";
    public static final String ATTR_TARGET_BUCKET = "targetBucket";
    public static final String ATTR_TARGET_FOLDER = "targetFolder";
    public static final String ATTR_SOURCE_FILE_NAME = "sourceFileName";
    public static final String ATTR_TASK_TYPE = "taskType";
    public static final String ATTR_CURRENT_STATE = "currentState";
    public static final String ATTR_FILE_UUID = "uuid";
    public static final String ATTR_START_TIME = "startTime";
    public static final String ATTR_END_TIME = "endTime";
    public static final String ATTR_PREVIEW_STATUS = "previewStatus";

    public static final String ATTR_PREFIX_FILE = "file_";

    public static final String ATTR_MEDIA_TRANSCODE_INPUT = "mediaTranscodeInput";
    public static final String ATTR_MEDIA_TRANSCODE_OUTPUT = "mediaTranscodeOutput";

    public static final String ATTR_RESOURCE_PACKAGING_STATE = "resourcePackagingState";

    // 转换文件状态的占位符
    public static final String PPT_FILE_CONVERTING = "converting";
    public static final String PPT_SLIDE_CONVERTING = "converting";
    public static final String PPT_MEDIA_CONVERTING = "converting";
    public static final String PPT_MEDIA_DONE = "done";
    public static final String PPT_PREVIEW_DONE = "done";
    public static final String PPT_MEDIA_FAILED = "failed";

}
