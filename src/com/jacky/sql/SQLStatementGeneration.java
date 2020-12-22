package com.jacky.sql;

import com.jacky.sql.sqlData.BaseSqlData;

public class SQLStatementGeneration {
    public static String sqlInsertString(String tableName, BaseSqlData... values) {
        StringBuilder builder = new StringBuilder("INSERT INTO");
        builder.append(String.format(" %s value(", tableName));

        for (int i = 0; i < values.length; i++) {
            builder.append(String.format("%s", values[i].toString()));
            if (i != values.length - 1) {
                builder.append(",");
            }
        }
        builder.append(");");
        return builder.toString();
    }

    public static String sqlUpdateString(String tableName, String cmpValue, BaseSqlData... changedDatas) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("UPDATE %s SET ", tableName));
        for (int i = 0; i < changedDatas.length; i++) {
            builder.append(String.format("%s=%s", changedDatas[i].dataName(), changedDatas[i].toString()));
            if (i != changedDatas.length - 1) {
                builder.append(",");
            }
        }
        if (!cmpValue.equals(""))
            builder.append(String.format(" WHERE %s", cmpValue));
        builder.append(";");
        return builder.toString();
    }

    // 不能进行复杂的条件语句
    public static String sqlUpdateString(String tableName, BaseSqlData cmpValue, BaseSqlData... changedDatas) {
        if (cmpValue != null)
            return sqlUpdateString(tableName, String.format("%s=%s", cmpValue.dataName(), cmpValue.toString()),
                    changedDatas);
        else
            return sqlUpdateString(tableName, "", changedDatas);
    }

    public static String sqlDeleteString(String tableName, String limits) {
        if (limits.equals("")) {
            // 删除全部
            return String.format("DELETE FORM %s;", tableName);
        } else {
            return String.format("DELETE FORM %s WHERE %s", tableName, limits);
        }
    }
}
