package com.jacky.sql.err;

public class ForeignKeyNotFoundException extends RuntimeException{
    public ForeignKeyNotFoundException(String fromTable, String inTable){
        super(String.format("在表单【%s】未找到指向【%s】的外键",fromTable,inTable));
    }
}
