package com.jacky.sql.sqlData;

public class SqlString extends BaseSqlData {
    private String s;

    public SqlString(String name,String s) {
        super(name);
        this.s = String.format("'%s'", s);

    }

    @Override
    public String toString() {
        return s;
    }
}
