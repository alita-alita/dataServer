package cn.idicc.taotie.service.message.data;

import java.util.Map;

/**
 * @Author: MengDa
 * @Date: 2023/8/15
 * @Description:
 * @version: 1.0
 */
public class DynamicValueEntity {
    private String columnName;

    private String tableName;

    private String targetColumnName;

    private Map<String,String> conditions;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTargetColumnName() {
        return targetColumnName;
    }

    public void setTargetColumnName(String targetColumnName) {
        this.targetColumnName = targetColumnName;
    }

    public Map<String, String> getConditions() {
        return conditions;
    }

    public void setConditions(Map<String, String> conditions) {
        this.conditions = conditions;
    }

    @Override
    public String toString() {
        return "DynamicValueEntity{" +
                "columnName='" + columnName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", targetColumnName='" + targetColumnName + '\'' +
                ", conditions=" + conditions +
                '}';
    }
}
