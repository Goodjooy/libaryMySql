package com.jacky.sql.sqlData;

public abstract class BaseSqlData {
    protected String name;

    public BaseSqlData(String name) {
        this.name = name;
    }

    @Override
    public abstract String toString();

    public String dataName() {
        return name;
    }
}
