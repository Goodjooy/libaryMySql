package com.jacky.sql.SqlTableAtom;

import com.jacky.sql.sqlData.BaseSqlData;
import com.jacky.sql.sqlData.nullData;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlUserTableAtom extends BaseAtom{
    SqlUserTableAtom(String tableName, ResultSet set) throws SQLException {
        super(tableName, set);
    }

    SqlUserTableAtom(String tableName, BaseSqlData... data) {
        super(tableName, data);
    }

    @Override
    public void loadFromResultSet(ResultSet set) throws SQLException {
        setData(dataNames.get(0),set.getInt(dataNames.get(0)));
        setData(dataNames.get(1),set.getString(dataNames.get(1)));
    }

    @Override
    protected void initial() {
        tableName="users";
        dataNames.add("uid");
        dataNames.add("name");

        setPrimaryKey(new nullData("uid"));
    }

    @Override
    public boolean isSetPrimaryKey() {
        return primaryKeyValue.toString().matches("^\\d+$");
    }
}
