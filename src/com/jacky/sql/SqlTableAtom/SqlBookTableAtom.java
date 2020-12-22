package com.jacky.sql.SqlTableAtom;

import com.jacky.sql.sqlData.BaseSqlData;
import com.jacky.sql.sqlData.nullData;

import java.sql.ResultSet;
import java.util.Iterator;

public class SqlBookTableAtom extends BaseAtom{

    SqlBookTableAtom(String tableName, ResultSet set) {
        super(tableName, set);

    }

    SqlBookTableAtom(String tableName, BaseSqlData... data) {
        super(tableName, data);
    }

    @Override
    public void loadFromResultSet(ResultSet set) {

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
}
