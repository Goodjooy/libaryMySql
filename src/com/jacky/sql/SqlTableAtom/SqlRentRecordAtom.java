package com.jacky.sql.SqlTableAtom;

import com.jacky.sql.sqlData.BaseSqlData;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlRentRecordAtom extends BaseAtom{
    SqlRentRecordAtom(String tableName, ResultSet set) throws SQLException {
        super(tableName, set);
    }

    SqlRentRecordAtom(String tableName, BaseSqlData... data) {
        super(tableName, data);
    }

    @Override
    public void loadFromResultSet(ResultSet set) throws SQLException {

    }

    @Override
    protected void initial() {

    }

    @Override
    public boolean isSetPrimaryKey() {
        return false;
    }
}
