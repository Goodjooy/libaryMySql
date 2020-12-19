package com.jacky.sql.sqlData;

public class SqlNumber extends BaseSqlData {
    String data;

    public SqlNumber(String name, int numI) {
        super(name);
        data = Integer.toString(numI);
    }

    public SqlNumber(String name, Double i) {
        super(name);
        data = String.format("%.4lf", i);
    }

    @Override
    public String toString() {
        return data;
    }
}
