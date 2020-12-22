package com.jacky.sql.err;

import com.jacky.sql.SqlTableAtom.BaseAtom;

public class TableAtomIDNotSetException extends RuntimeException{
    public TableAtomIDNotSetException(BaseAtom atom){
        super(String.format("【%s】表元素id未指定",atom.getTableName()));
    }
}
