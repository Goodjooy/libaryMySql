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

import com.jacky.sql.sqlData.*;

import static com.jacky.sql.SQLStatementGeneration.sqlInsertString;
import static com.jacky.sql.SQLStatementGeneration.sqlUpdateString;

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
    //管理员专有
    //向仓库添加新书
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
    //搜索书本
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
    //显示全部书本
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
     * @param userID 借阅的用户
     * @param bookID 书本的id
     * @param borrowDays 借阅时常
     */
    public  void  borrowBook(int userID,int bookID,int borrowDays) throws SQLException {
        //检查指定图书是否已经被借阅
        ResultSet set = statement.executeQuery(String.format("select *from book where b_id=%d and is_rent=false",
                bookID));
        if(set.next()){
            set.close();
            //检查用户是否存在
            set=statement.executeQuery(String.format("select *from users where uid=%d",userID));
            if(set.next()){
                set.close();
            //还未被借出
            //进行借阅处理
            LocalDate outData=LocalDate.now();
            LocalDate returnData=outData.plusDays(borrowDays);

            statement.execute(String.format("update book set is_rent=true where b_id=%d",bookID));
            statement.execute(
                    sqlInsertString("rent_record",new nullData("id"),new SqlNumber("book_id",bookID),
                    new SqlNumber("user_id",userID),new SqlData("start_data",outData),
                    new SqlData("return_data",returnData),new SqlBoolean("is_close",false)));

            }else {
                System.out.println("用户不存在");
            }
        }else {
            System.out.println("该图书已经被借出");
        }
    }
    public  void returnBook(int bookID)throws SQLException{
        ResultSet set;
        //查找书本是否存在
        set=statement.executeQuery(String.format("select *from rent_record where book_id=%d and is_close=false",bookID));
        if (set.next()){
            int recordID=set.getInt("id");
            String returnDate=set.getString("return_data");
            set.close();
            //归还图书，关闭借阅记录
            statement.execute(sqlUpdateString("book",new SqlNumber("b_id",bookID),
                    new SqlBoolean("is_rent",false)));
            statement.execute(sqlUpdateString("rent_record",new SqlNumber("id",recordID),
                    new SqlBoolean("is_close",true)));

            //如果超时归还，发出警告
            Scanner scanner=new Scanner(returnDate.replace("-"," "));
            int year =scanner.nextInt();
            int month=scanner.nextInt();
            int day=scanner.nextInt();
            scanner.close();
            LocalDate date=LocalDate.of(year,Month.of(month),day);
            if(date.isBefore(LocalDate.now())){
                System.out.println("超时归还图书！");
            }

        }else {
        System.out.println("该图书不在借阅状态！");
        }
    }
    public int newUser(String userName) throws SQLException {
        statement.execute(sqlInsertString("users",new nullData("uid"),
                new SqlString("name",userName)),Statement.RETURN_GENERATED_KEYS);
        ResultSet set;
        set=statement.getGeneratedKeys();
        if(set.next()){
            return set.getInt(1);
        }else {
            System.out.println("账户新建失败！");
            return -1;
        }
    }


}
