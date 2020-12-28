package com.jacky.sql.SqlTableAtom;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.jacky.sql.foreignKey.ForeignInKey;
import com.jacky.sql.sqlData.nullData;

public class SqlUserTableAtom extends BaseAtom {
    public static  final String IDKey = "uid";
    public static final String NameKey = "name";

    public SqlUserTableAtom(ResultSet set) throws SQLException {
        super(set);
    }

    public SqlUserTableAtom() {
        super();
    }

    @Override
    public void loadFromResultSet(ResultSet set) throws SQLException {
        setData(dataNames.get(0), set.getInt(dataNames.get(0)));
        setData(dataNames.get(1), set.getString(dataNames.get(1)));
    }

    @Override
    protected void initial() {
        tableName = "users";
        dataNames.add("uid");
        dataNames.add("name");

        foreignInTables.add(new ForeignInKey("user_id","rent_record","uid"));

        setPrimaryKey(new nullData("uid"));
    }

    @Override
    public boolean isSetPrimaryKey() {
        return primaryKeyValue.toString().matches("^\\d+$");
    }
}
