package com.jacky.sql.sqlData;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Scanner;

public class SqlData extends BaseSqlData{
    private final LocalDate data;

    public SqlData(String name, LocalDate data){
     super(name);
        this.data = data;
    }
    public SqlData(String name, Date data){
        super(name);
        this.data=data.toLocalDate();
    }
    public SqlData(String name,String data){
        super(name);
        Scanner scanner=new Scanner(data.replace("-"," "));
        int year,month,day;
        year=scanner.nextInt();
        month=scanner.nextInt();
        day= scanner.nextInt();
        this.data=LocalDate.of(year,month,day);

    }

    @Override
    public String toString() {

        return String.format("'%4d-%02d-%02d'",data.getYear(),data.getMonth().getValue(),data.getDayOfMonth());
    }
}
