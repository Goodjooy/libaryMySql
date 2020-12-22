package com.jacky.sql.SqlTableAtom;

import com.jacky.sql.sqlData.BaseSqlData;
import com.jacky.sql.sqlData.nullData;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlBookTableAtom extends BaseAtom{

    SqlBookTableAtom(String tableName, ResultSet set) throws SQLException{
        super(tableName, set);

    }

    SqlBookTableAtom(String tableName, BaseSqlData... data) {
        super(tableName, data);
    }

    @Override
    public void loadFromResultSet(ResultSet set) throws SQLException {
        setData(dataNames.get(0),set.getInt(dataNames.get(0)));
        setData(dataNames.get(1),set.getString(dataNames.get(1)));
        setData(dataNames.get(2),set.getBoolean(dataNames.get(2)));
        setData(dataNames.get(3),set.getInt(dataNames.get(3)));
    }

    @Override
    protected void initial() {
        tableName="book";
        dataNames.add("b_id");
        dataNames.add("book_name");
        dataNames.add("is_rent");
        dataNames.add("author_id");

        setPrimaryKey(new nullData("b_id"));
    }

    @Override
    public boolean isSetPrimaryKey() {
        //纯数字
        return !primaryKeyValue.toString().matches("\\d+");
    }
}
