package com.jacky.sql.sqlData;

public class nullData extends BaseSqlData {

    public nullData(String name) {
        super(name,true);

    }

    @Override
    public String toString() {
        return "null";
    }
}
