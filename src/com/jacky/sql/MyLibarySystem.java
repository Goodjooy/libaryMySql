package com.jacky.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.jacky.sql.sqlData.BaseSqlData;
import com.jacky.sql.sqlData.SqlBoolean;
import com.jacky.sql.sqlData.SqlNumber;
import com.jacky.sql.sqlData.SqlString;
import com.jacky.sql.sqlData.nullData;

public class MyLibarySystem {
    final String connectUrl = "jdbc:mysql://127.0.0.1:3306/libSys?characterEncoding=UTF-8&serverTimezone=UTC";
    final String user = "root";
    final String password = "wyq020222";
    // 数据库链接
    Connection connection;
    // 数据库语句执行器
    Statement statement = null;
    PreparedStatement pStatement = null;

    public enum SearchType {
        BOOK_NAME, AUTHOR_NAME;
    }

    public MyLibarySystem() {
        // 加载相关组件
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException exception) {
            System.out.println("无法加载相关配置！");
            exception.printStackTrace();
            return;
        }
        // 链接到数据库
        try {
            connection = DriverManager.getConnection(connectUrl, user, password);
            statement = connection.createStatement();

        } catch (Exception e) {
            System.out.println("链接失败");
            e.printStackTrace();
        }

    }

    public void close() throws SQLException {
        statement.close();
        connection.close();
    }

    public void AppendNewBook(String bookName, String authorName, String infomationString) throws SQLException {
        int author_id = -1;
        // 检查是否有作者记录
        System.out.println("检查作者是否在数据库中\n");
        ResultSet set = statement.executeQuery(String.format("select *from author where author_name='%s'", authorName));
        // 如果有
        if (set.next()) {
            author_id = set.getInt("p_id");
            System.out.printf("在数据库中找到作者信息！\n\t %s, id为=%d\n", set.getString("author_name"), author_id);
            set.close();
        } else {
            set.close();
            // 插入
            statement.execute(sqlInsertString("author", new nullData("author"), new SqlString("author", authorName)),
                    Statement.RETURN_GENERATED_KEYS);
            set = statement.getGeneratedKeys();
            if (set.next()) {
                author_id = set.getInt(1);
                System.out.printf("在数据库中未找到作者信息！已经完成添加！\n\t %s, id为=%d\n", authorName, author_id);
                set.close();
            } else {
                System.out.println("在数据库中未找到作者信息！添加失败！\n");
                return;
            }
        }

        // 添加书本
        statement.execute(sqlInsertString("book", new nullData("book"), new SqlString("book", bookName),
                new SqlBoolean("book", false), new SqlNumber("book", author_id)));
        System.out.printf("完成添加书本->%s  作者为：%s\n", bookName, authorName);

    }

    public void bookSearch(String keyWord, boolean cloumMatch, boolean ignoreRent, SearchType searchType)
            throws SQLException {
        String where;
        String matchCode = String.format("like '%%%s%%'", cloumMatch ? keyWord.replace("", "%") : keyWord);
        matchCode = matchCode.replace("%%", "%");
        if (ignoreRent) {
            matchCode = String.format("%s and is_rent=false", matchCode);
        }

        if (searchType == SearchType.BOOK_NAME) {
            where = String.format("book_name %s", matchCode);
        } else {
            where = String.format("author_name %s", matchCode);
        }

        String sql = String.format(
                "select book.b_id,book.book_name,book.is_rent,author.author_name from book inner join author on book.author_id=author.p_id where %s",
                where);

        ResultSet set = statement.executeQuery(sql);
        System.out.println("编号\t书名\t作者名\t是否被借出");
        while (set.next()) {
            System.out.printf("%03d):\t%-20s%-20s%s\n", set.getInt("b_id"), set.getString("book_name"),
                    set.getString("author_name"), set.getBoolean("is_rent") ? "已经借出" : "未被借出");
        }
        set.close();
    }

    public void showAllBook() throws SQLException {
        String sql = "select book.b_id,book.book_name,book.is_rent,author.author_name from book inner join author on book.author_id=author.p_id";

        ResultSet set = statement.executeQuery(sql);
        System.out.println("编号\t书名\t作者名\t是否被借出");
        while (set.next()) {
            System.out.printf("%03d):\t%-20s%-20s%s\n", set.getInt("b_id"), set.getString("book_name"),
                    set.getString("author_name"), set.getBoolean("is_rent") ? "已经借出" : "未被借出");
        }
        set.close();
    }

    public static String sqlInsertString(String tableName, BaseSqlData... values) {
        StringBuilder builder = new StringBuilder("INSERT INTO");
        builder.append(String.format(" %s value(", tableName));

        for (int i = 0; i < values.length; i++) {
            builder.append(String.format("%s", values[i].toString()));
            if (i != values.length - 1) {
                builder.append(",");
            }
        }
        builder.append(");");
        return builder.toString();
    }

    public static String sqlUpdateString(String tableName, String cmpValue, BaseSqlData... changedDatas) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("UPDATE %s SET ", tableName));
        for (int i = 0; i < changedDatas.length; i++) {
            builder.append(String.format("%s=%s", changedDatas[i].dataName(), changedDatas[i].toString()));
            if (i != changedDatas.length - 1) {
                builder.append(",");
            }
        }
        if (cmpValue.equals("") || cmpValue != null)
            builder.append(String.format("WHERE %s", cmpValue));
        builder.append(";");
        return builder.toString();
    }

    // 不能进行复杂的条件语句
    public static String sqlUpdateString(String tableName, BaseSqlData cmpValue, BaseSqlData... changedDatas) {
        if (cmpValue != null)
            return sqlUpdateString(tableName, String.format("%s=%s", cmpValue.dataName(), cmpValue.toString()),
                    changedDatas);
        else
            return sqlUpdateString(tableName, "", changedDatas);
    }

    public static String sqlDeleteString(String tableName, String limits) {
        if (limits.equals("")) {
            // 删除全部
            return String.format("DELETE FORM %s;", tableName);
        } else {
            return String.format("DELETE FORM %s WHERE %s", tableName, limits);
        }
    }

}
