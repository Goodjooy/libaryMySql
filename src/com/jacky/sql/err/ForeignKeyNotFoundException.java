package com.jacky.sql.err;

public class ForeignKeyNotFoundException extends RuntimeException{
    /**
     *
     */
    private static final long serialVersionUID = -2597186703503935024L;

    public ForeignKeyNotFoundException(String fromTable, String inTable) {
        super(String.format("在表单【%s】未找到指向【%s】的外键",fromTable,inTable));
    }
}
