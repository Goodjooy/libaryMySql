package unit.test;

import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.jacky.sql.MyLibarySystem;
import com.jacky.sql.SqlTableAtom.SqlAuthorTableAtom;
import com.jacky.sql.SqlTableAtom.SqlBookTableAtom;
import com.jacky.sql.SqlTableAtom.SqlRentRecordAtom;
import com.jacky.sql.sqlData.BaseSqlData;

import org.junit.Before;
import org.junit.Test;

public class App {
    MyLibarySystem system;
    SqlAuthorTableAtom author;
    SqlBookTableAtom book=new SqlBookTableAtom();
    
    String authorName="unit";

    @Before
    public void PreTestWork() {
        system = new MyLibarySystem();
        author=new SqlAuthorTableAtom();
        author.setData(SqlAuthorTableAtom.nameKey,authorName);

       
        book.setData(SqlBookTableAtom.NameKey,"aaaaa");
        book.setData(SqlBookTableAtom.AuthorIDKey,1);
        book.setData(SqlBookTableAtom.RentStatusKey,false);
        
    }
    
    public void clear() throws SQLException {
        ResultSet set=system.excute(String.format("select *from author where author_name=%s",authorName));
        set.next();
        int id=set.getInt(1);

        system.excute(String.format("delete from book where author_id=%d", id));
        system.excute(String.format("delete from author where author_name=%s",authorName));

        system.close();
    }

    @Test
    public void addOneBookWithExistAuthorTest() throws SQLException {
        int bookCount=0;
        ResultSet set = system.excute("Select Count(b_id) as count From book");
        set.next();
            bookCount=set.getInt("count");
        set.close();

        system.AppendNewBook("goodsGenerater", authorName,"");

        set = system.excute("Select Count(b_id) as count From book");
        set.next();
        

        assertEquals("书本行",bookCount+1, set.getInt("count"));
        set.close();
    }

    @Test
    public void checkBookSqlInsertGeneraterTest(){
        

        String sql=(book.generateInsertStatement().toLowerCase()); 

        assertEquals(sql, "insert into book value(null,'aaaaa',false,1)");
        
    }

    @Test
    public void checkBookSqlUpdateGeneraterTest() throws SQLException {
        book.setData(SqlBookTableAtom.RentStatusKey ,true);
        book.setData(SqlBookTableAtom.IDKey, 9);
        String sql=book.generateUpdateStatement(SqlBookTableAtom.RentStatusKey);

        assertEquals(sql,"UPDATE book SET is_rent=true WHERE b_id=9");
    }

    @Test
    public void checkBookSqlDeleteGenerateTest(){
        book.setData(SqlBookTableAtom.IDKey,5);

        String sql=book.generateDeleteStatement();

        assertEquals(sql.toLowerCase(), 
        "delete from book where b_id=5");
    }
    @Test
    public void checkBookSqlForeigneInKayGeneraterTest(){
        BaseSqlData data= book.generateForeignInSqlData("rent_record");
        
        assertEquals(data.dataName(), SqlRentRecordAtom.BookIDKey);
    }
}
