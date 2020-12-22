package com.jacky.sql.sqlData;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SqlData extends BaseSqlData{
    private final LocalDate data;

    public SqlData(String name, LocalDate data){
     super(name);

        this.data = data;
    }

    @Override
    public String toString() {

        return String.format("'%4d-%02d-%02d'",data.getYear(),data.getMonth().getValue(),data.getDayOfMonth());
    }
}
