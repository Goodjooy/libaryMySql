package com.jacky.sql.SqlTableAtom;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.jacky.sql.foreignKey.ForeignInKey;
import com.jacky.sql.foreignKey.ForeignKey;
import com.jacky.sql.sqlData.nullData;

public class SqlBookTableAtom extends BaseAtom{
        public  static final String IDKey="b_id";
        public  static final String NameKey="book_name";
        public static final String RentStatusKey="is_rent";
        public static final String AuthorIDKey="author_id";

    public SqlBookTableAtom(ResultSet set) throws SQLException {
        super(set);
    }

    public SqlBookTableAtom() {
        super();
    }

    public static SqlBookTableAtom generateNewBook(String bookName,BaseAtom authorAtom) {
        SqlBookTableAtom book = new SqlBookTableAtom();
        book.setData(SqlBookTableAtom.NameKey, bookName);
        book.setData(authorAtom.generateForeignInSqlData(book.getTableName()));
        book.setData(SqlBookTableAtom.RentStatusKey,false);

        return book;
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
        dataNames.add("b_id"     );
        dataNames.add("book_name");
        dataNames.add("is_rent"  );
        dataNames.add("author_id");

        foreignKeys.add(new ForeignKey("author_id","author","author_name"));
        foreignInTables.add(new ForeignInKey("book_id","rent_record","b_id"));

        setPrimaryKey(new nullData("b_id"));
    }

    @Override
    public boolean isSetPrimaryKey() {
        //纯数字
        return !primaryKeyValue.toString().matches("\\d+");
    }
}
