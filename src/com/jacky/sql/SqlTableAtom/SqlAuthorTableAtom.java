package com.jacky.sql.SqlTableAtom;

import com.jacky.sql.sqlData.BaseSqlData;
import com.jacky.sql.sqlData.nullData;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlAuthorTableAtom extends BaseAtom{
    SqlAuthorTableAtom(String tableName, ResultSet set) throws SQLException {
        super(tableName, set);
    }

    SqlAuthorTableAtom(String tableName, BaseSqlData... data) {
        super(tableName, data);
    }

    @Override
    public void loadFromResultSet(ResultSet set) throws SQLException {
        setData(dataNames.get(0),set.getInt(dataNames.get(0)));
        setData(dataNames.get(1),set.getString(dataNames.get(1)));
    }

    @Override
    protected void initial() {
        tableName="author";
        dataNames.add("p_id");
        dataNames.add("author_name");
        setPrimaryKey(new nullData("p_id"));

    }

    @Override
    public boolean isSetPrimaryKey() {
        return primaryKeyValue.toString().matches("^\\d+$");
    }
}
