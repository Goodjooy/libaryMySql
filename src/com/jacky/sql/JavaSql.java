package com.jacky.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.xml.transform.stax.StAXResult;

import com.jacky.sql.MyLibarySystem;
import com.jacky.sql.MyLibarySystem.SearchType;


public class JavaSql {

    public static void main2(String[] args) throws Exception {

        System.out.println("Hello, World!");

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();// TODO: handle exception
        }

        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/mySql?characterEncoding=UTF-8&serverTimezone=UTC", "root", "wyq020222");
        System.out.println("链接成功");
        Statement statement = connection.createStatement();
        
        statement.execute("CREATE TABLE IF NOT EXISTS libary(" + " book_id int UNSIGNED AUTO_INCREMENT,"
        + " book_name VARCHAR(50) NOT NULL," + "book_author VARCHAR(50) NOT NULL ," + "is_rent BOOLEAN,"
        + "PRIMARY KEY ( book_id )" + ")ENGINE=InnoDB DEFAULT CHARSET=utf8;");
        String sql = String.format("insert into libary values(null, '%s','%s',false)", "算法导论", "abab");
        
        PreparedStatement pStatement =connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        
        statement.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);

        ResultSet t = statement.getGeneratedKeys();
        if (t.next()) {
            System.out.println(t.getInt(1));
        }
        /*
         * String sql =
         * String.format("insert into libary values(null, '%s','%s',false)", "算法导论",
         * "abab"); statement.execute(sql); sql =
         * String.format("insert into libary values(null, '%s','%s',false)", "代码大全",
         * "abab2"); statement.execute(sql); sql =
         * String.format("insert into libary values(null, '%s','%s',false)", "机器学习实战",
         * "abab3"); statement.execute(sql);
         * 
         * System.out.println("插入成功");
         */
        sql = String.format("select * from libary order by book_author");
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            int id = resultSet.getInt("book_id");
            String name = resultSet.getString("book_name");
            String author = resultSet.getString("book_author");
            boolean isRent = resultSet.getBoolean("is_rent");
            System.out.printf("id=%d,\tname=%s,\t author=%s\tisRent =%s\n", id, name, author, isRent ? "yea" : "no");
        }
        resultSet.close();
        connection.close();
    }

    public static void main(String[] args)throws Exception {
        MyLibarySystem system=new MyLibarySystem();

        system.showAllBook();

        //system.borrowBook(1,5,4);
        //system.returnBook(5);
    //    system.bookSearch("林", true, false, SearchType.AUTHOR_NAME);

  //      system.AppendNewBook("《大话哦ds 数据结构》", "不是吧阿sir", "");
//        system.AppendNewBook("《sds s》", "dssdfsdsd", "");
        System.out.println();
        system.showAllBook();
        system.close();
    }
}
