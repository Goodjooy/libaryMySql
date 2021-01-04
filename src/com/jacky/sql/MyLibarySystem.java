package com.jacky.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Month;
import java.util.Scanner;

import com.jacky.sql.SqlTableAtom.SqlAuthorTableAtom;
import com.jacky.sql.SqlTableAtom.SqlBookTableAtom;
import com.jacky.sql.SqlTableAtom.SqlRentRecordAtom;
import com.jacky.sql.SqlTableAtom.SqlUserTableAtom;
import com.jacky.sql.err.failToInsertNewAuthor;

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

    private SqlAuthorTableAtom getAuthorFromSql(String authorName) throws SQLException {
        SqlAuthorTableAtom author = new SqlAuthorTableAtom();
        author.setData(SqlAuthorTableAtom.nameKey, authorName);

        String searchAuthorStatement = author.generateSelfSearchStatement();

        ResultSet set = statement.executeQuery(searchAuthorStatement);

        author = new SqlAuthorTableAtom(set);
        set.close();
        return author;
    }

    // 管理员专有
    public boolean checkAuthorExist(String authorName) throws SQLException {
        ResultSet set = null;
        try {
            SqlAuthorTableAtom authorTableAtom = new SqlAuthorTableAtom();
            authorTableAtom.setData(SqlAuthorTableAtom.nameKey, authorName);

            set = statement.executeQuery(authorTableAtom.generateSelfSearchStatement());

            return set.next();
        } finally {
            if (set != null) {
                set.close();
            }
        }
    }

    public SqlAuthorTableAtom addNewAuthor(String authorName) throws SQLException {
        ResultSet set = null;
        try {
            SqlAuthorTableAtom authorTableAtom = new SqlAuthorTableAtom();
            authorTableAtom.setData(SqlAuthorTableAtom.nameKey, authorName);

            statement.execute(authorTableAtom.generateInsertStatement(), Statement.RETURN_GENERATED_KEYS);
            set = statement.getGeneratedKeys();
            if (set.next()) {
                authorTableAtom.setData(authorTableAtom.getPrimaryKeyName(), set.getInt(1));
                return authorTableAtom;
            }
            throw new failToInsertNewAuthor(authorName);
        } finally {
            if (set != null)
                set.close();
        }
    }

    // 向仓库添加新书
    public void AppendNewBook(String bookName, String authorName, String infomationString) throws SQLException {
        SqlAuthorTableAtom authorTableAtom;

        // 检查是否有作者记录
        try {
            System.out.println("检查作者是否在数据库中\n");
            // 如果有
            if (checkAuthorExist(authorName)) {
                authorTableAtom = getAuthorFromSql(authorName);
                System.out.printf("在数据库中找到作者信息！\n\t %s, id为=%s\n", authorTableAtom.getData(SqlAuthorTableAtom.nameKey),
                        authorTableAtom.getData(SqlAuthorTableAtom.IDKey));
            } else {
                // 插入
                authorTableAtom = addNewAuthor(authorName);
                System.out.printf("在数据库中未找到作者信息！已经完成添加！\n\t %s, id为=%s\n", authorName,
                        authorTableAtom.getPrimaryKeyValue());
            }
            // 添加书本
            SqlBookTableAtom book = SqlBookTableAtom.generateNewBook(bookName, authorTableAtom);

            statement.execute(book.generateInsertStatement());
            System.out.printf("完成添加书本->%s  作者为：%s\n", bookName, authorName);

        } catch (failToInsertNewAuthor f) {
            System.out.println("在数据库中未找到作者信息！添加失败！\n");
        }
    }

    // 搜索书本
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

    // 显示全部书本
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

    /**
     * @param userID     借阅的用户
     * @param bookID     书本的id
     * @param borrowDays 借阅时常
     */
    public void borrowBook(int userID, int bookID, int borrowDays) throws SQLException {
        // 检查指定图书是否已经被借阅
        SqlBookTableAtom book = new SqlBookTableAtom();
        book.setData(book.getPrimaryKeyName(), bookID);
        book.setData(SqlBookTableAtom.RentStatusKey, false);
        ResultSet set = statement.executeQuery(book.generateSelfSearchStatement());
        if (set.next()) {
            book = new SqlBookTableAtom(set);
            set.close();
            SqlUserTableAtom user = new SqlUserTableAtom();
            user.setData(user.getPrimaryKeyName(), userID);
            // 检查用户是否存在
            set = statement.executeQuery(user.generateSelfSearchStatement());
            if (set.next()) {
                user = new SqlUserTableAtom(set);
                set.close();
                // 还未被借出
                // 进行借阅处理
                LocalDate outData = LocalDate.now();
                LocalDate returnData = outData.plusDays(borrowDays);
                book.setData(SqlBookTableAtom.RentStatusKey, true);
                statement.execute(book.generateUpdateStatement(SqlBookTableAtom.RentStatusKey));

                SqlRentRecordAtom rentRecord = new SqlRentRecordAtom();
                rentRecord.setData(SqlRentRecordAtom.BorrowDataKey, outData);
                rentRecord.setData(SqlRentRecordAtom.ReturnDataKey, returnData);

                rentRecord.setData(book.generateForeignInSqlData(rentRecord.getTableName()));
                rentRecord.setData(user.generateForeignInSqlData(rentRecord.getTableName()));

                rentRecord.setData(SqlRentRecordAtom.StatusKay, false);

                statement.execute(rentRecord.generateInsertStatement());

            } else {
                System.out.println("用户不存在");
            }
        } else {
            System.out.println("该图书已经被借出");
        }
    }

    public void returnBook(int bookID) throws SQLException {
        ResultSet set;
        SqlBookTableAtom book;
        SqlRentRecordAtom rentRecord = new SqlRentRecordAtom();
        rentRecord.setData(SqlRentRecordAtom.BookIDKey, bookID);
        rentRecord.setData(SqlRentRecordAtom.StatusKay, false);
        // 查找书本是否存在
        set = statement.executeQuery(rentRecord.generateSelfSearchStatement());
        if (set.next()) {
            rentRecord = new SqlRentRecordAtom(set);
            rentRecord.setData(SqlRentRecordAtom.StatusKay, true);

            book = new SqlBookTableAtom();
            book = (SqlBookTableAtom) rentRecord.generateForeignKeyAtom(book);
            book.setData(SqlBookTableAtom.RentStatusKey, false);
            set.close();
            // 归还图书，关闭借阅记录
            statement.execute(book.generateUpdateStatement(SqlBookTableAtom.RentStatusKey));
            statement.execute(rentRecord.generateUpdateStatement(SqlRentRecordAtom.StatusKay));

            // 如果超时归还，发出警告
            Scanner scanner = new Scanner(
                    rentRecord.getData(SqlRentRecordAtom.ReturnDataKey).replace("-", " ").replace("'", ""));
            int year = scanner.nextInt();
            int month = scanner.nextInt();
            int day = scanner.nextInt();
            scanner.close();
            LocalDate date = LocalDate.of(year, Month.of(month), day);
            if (date.isBefore(LocalDate.now())) {
                System.out.println("超时归还图书！");
            }

        } else {
            System.out.println("该图书不在借阅状态！");
        }
    }

    public int createNewUser(String userName) throws SQLException {
        SqlUserTableAtom user = new SqlUserTableAtom();
        user.setData(SqlUserTableAtom.NameKey, userName);

        statement.execute(user.generateInsertStatement(), Statement.RETURN_GENERATED_KEYS);
        ResultSet set;
        set = statement.getGeneratedKeys();
        if (set.next()) {
            return set.getInt(1);
        } else {
            System.out.println("账户新建失败！");
            return -1;
        }
    }

    public ResultSet excute(String sql) throws SQLException {

        return statement.executeQuery(sql);

    }

}
