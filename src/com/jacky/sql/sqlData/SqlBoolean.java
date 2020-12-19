package com.jacky.sql.sqlData;

public class SqlBoolean extends BaseSqlData {
    String data;

    public SqlBoolean(String name, Boolean b) {
        super(name);
        data = String.format("%s", b.booleanValue() ? "true" : "false");
    }

    @Override
    public String toString() {
        return data;
    }
}
