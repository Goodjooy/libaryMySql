package com.jacky.sql.SqlTableAtom;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.jacky.sql.foreignKey.ForeignInKey;
import com.jacky.sql.sqlData.nullData;

public class SqlAuthorTableAtom extends BaseAtom{
    public final static String IDKey="p_id";
    public final static String nameKey="author_name";

    public SqlAuthorTableAtom(ResultSet set) throws SQLException {
        super(set);
    }

    public SqlAuthorTableAtom() {
        super();
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

        foreignInTables.add(new ForeignInKey(SqlBookTableAtom.AuthorIDKey, "book",IDKey ));

        setPrimaryKey(new nullData("p_id"));

    }

    @Override
    public boolean isSetPrimaryKey() {
        return primaryKeyValue.toString().matches("^\\d+$");
    }
}
