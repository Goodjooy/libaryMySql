package com.jacky.sql.SqlTableAtom;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.jacky.sql.foreignKey.ForeignKey;
import com.jacky.sql.sqlData.nullData;

public class SqlRentRecordAtom extends BaseAtom {
    public static final String IDKey = "id";
    public static final String BookIDKey = "book_id";
    public static final String UserIDKey = "user_id";
    public static final String BorrowDataKey = "borrow_data";
    public static final String ReturnDataKey = "return_data";
    public static final String StatusKay = "is_close";

    public SqlRentRecordAtom(ResultSet set) throws SQLException {
        super(set);
    }

    public SqlRentRecordAtom() {
        super();
    }

    @Override
    public void loadFromResultSet(ResultSet set) throws SQLException {
        setData(dataNames.get(0), set.getInt(dataNames.get(0)));
        setData(dataNames.get(1), set.getInt(dataNames.get(1)));
        setData(dataNames.get(2), set.getInt(dataNames.get(2)));
        setData(dataNames.get(3), set.getDate(dataNames.get(3)));
        setData(dataNames.get(4), set.getDate(dataNames.get(4)));
        setData(dataNames.get(5), set.getBoolean(dataNames.get(5)));
    }

    @Override
    protected void initial() {
        tableName = "rent_record";
        dataNames.add("id");
        dataNames.add("booK_id");
        dataNames.add("user_id");
        dataNames.add("borrow_data");
        dataNames.add("return_data");
        dataNames.add("is_close");

        foreignKeys.add(new ForeignKey("book_id", "book", "b_id"));
        foreignKeys.add(new ForeignKey("user_id", "users", "uid"));

        setPrimaryKey(new nullData("id"));
    }

    @Override
    public boolean isSetPrimaryKey() {
        return !primaryKeyValue.toString().matches("^\\d+$");
    }
}
