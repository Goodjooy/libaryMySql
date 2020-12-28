package com.jacky.sql.err;

public class ForeignInKeyNotFoundException extends RuntimeException{

    public ForeignInKeyNotFoundException(String fromTable, String inTable){
        super(String.format("在表单【%s】未找到来自【%s】的外键",fromTable,inTable));
    }
}
