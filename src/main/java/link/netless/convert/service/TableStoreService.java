package link.netless.convert.service;

import com.alicloud.openservices.tablestore.SyncClient;
import com.alicloud.openservices.tablestore.model.*;
import link.netless.convert.bo.StaticResource;

/**
 * Created by az on 2019/2/17.
 */
public class TableStoreService {
    private final static String TABLESTORE_AK = "TABLESTORE_AK";
    private final static String TABLESTORE_SK = "TABLESTORE_SK";
    private final static String TABLESTORE_ENDPOINT = "TABLESTORE_ENDPOINT";
    private final static String TABLESTORE_CONVERT_INSTANCE = "TABLESTORE_CONVERT_INSTANCE";

    private SyncClient tableStoreSyncClient;

    public TableStoreService() {
        System.out.println(System.getProperty(TABLESTORE_ENDPOINT));
        System.out.println(System.getProperty(TABLESTORE_AK));
        System.out.println(System.getProperty(TABLESTORE_SK));
        System.out.println(System.getProperty(TABLESTORE_CONVERT_INSTANCE));
        tableStoreSyncClient = new SyncClient(System.getProperty(TABLESTORE_ENDPOINT),
                System.getProperty(TABLESTORE_AK),
                System.getProperty(TABLESTORE_SK),
                System.getProperty(TABLESTORE_CONVERT_INSTANCE));
    }

    public void shutdown() {
        if (tableStoreSyncClient != null) {
            tableStoreSyncClient.shutdown();
        }
    }

    public String getDynamicConversionUuidByTaskId(Long teamId, Long taskId) {
        SingleRowQueryCriteria criteria = new SingleRowQueryCriteria(
                TableStoreKeys.DYNAMIC_CONVERSION_TASK_TABLE_NAME,
                generateConversionTaskPK(teamId, taskId)
        );
        // 设置读取最新版本
        criteria.setMaxVersions(1);
        criteria.addColumnsToGet(TableStoreKeys.ATTR_FILE_UUID);
        GetRowResponse getRowResponse = tableStoreSyncClient.getRow(new GetRowRequest(criteria));
        String uuid = getRowResponse.getRow().getColumn(TableStoreKeys.ATTR_FILE_UUID).get(0).getValue().asString();
        return uuid;
    }

    public void updatePreviewTableColumn(Long teamId, Long taskId, Integer pageIndex, StaticResource resource) {
        PrimaryKey pk = PrimaryKeyBuilder.createPrimaryKeyBuilder()
                .addPrimaryKeyColumn(TableStoreKeys.PK_TEAM_ID, PrimaryKeyValue.fromLong(teamId))
                .addPrimaryKeyColumn(TableStoreKeys.PK_TASK_ID, PrimaryKeyValue.fromLong(taskId))
                .addPrimaryKeyColumn(TableStoreKeys.PK_PAGE_INDEX, PrimaryKeyValue.fromLong(pageIndex)).build();

        RowUpdateChange rowUpdateChange = new RowUpdateChange(TableStoreKeys.PREVIEW_TABLE_NAME, pk);
        rowUpdateChange.put(new Column(TableStoreKeys.ATTR_HEIGHT, ColumnValue.fromLong(resource.getHeight())));
        rowUpdateChange.put(new Column(TableStoreKeys.ATTR_WIDTH, ColumnValue.fromLong(resource.getWidth())));
        rowUpdateChange.put(new Column(TableStoreKeys.ATTR_PATH, ColumnValue.fromString(resource.getTargetPath())));
        tableStoreSyncClient.updateRow(new UpdateRowRequest(rowUpdateChange));
    }

    public void updateStaticTaskColumn(Long teamId, Long taskId, String columnKey, String value) {
        RowUpdateChange rowUpdateChange = new RowUpdateChange(TableStoreKeys.STATIC_CONVERSION_TASK_TABLE_NAME, generateConversionTaskPK(teamId, taskId));
        rowUpdateChange.put(new Column(columnKey, ColumnValue.fromString(value)));
        tableStoreSyncClient.updateRow(new UpdateRowRequest(rowUpdateChange));
    }


    private PrimaryKey generateMediaTranscodePK(String jobId) {
        return PrimaryKeyBuilder.createPrimaryKeyBuilder()
                .addPrimaryKeyColumn(TableStoreKeys.PK_MEDIA_TRANSCODE_JOB_ID, PrimaryKeyValue.fromString(jobId)).build();
    }

    private PrimaryKey generateConversionTaskPK(Long teamId, Long taskId) {
        return PrimaryKeyBuilder.createPrimaryKeyBuilder()
                .addPrimaryKeyColumn(TableStoreKeys.PK_TEAM_ID, PrimaryKeyValue.fromLong(teamId))
                .addPrimaryKeyColumn(TableStoreKeys.PK_TASK_ID, PrimaryKeyValue.fromLong(taskId)).build();
    }

    private PrimaryKey generateFileUuidPK(String uuid) {
        return PrimaryKeyBuilder.createPrimaryKeyBuilder()
                .addPrimaryKeyColumn(TableStoreKeys.PK_FILE_UUID, PrimaryKeyValue.fromString(uuid)).build();
    }
}
