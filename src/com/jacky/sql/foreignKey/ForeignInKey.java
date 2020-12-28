package com.jacky.sql.foreignKey;

import java.util.Objects;

public class ForeignInKey {
    private final String fkName;
    private final String inTable;
    private final String ikName;

    /**
     * @param fkName 指向自身的表的外部表对应的键名称
     * @param inTable  指向自身的表名
     * @param ikName  自身键名
     */
    public  ForeignInKey(String fkName, String inTable, String ikName){

        this.fkName = fkName;
        this.inTable = inTable;
        this.ikName = ikName;
    }

    public String getIkName() {
        return ikName;
    }

    public String getInTable() {
        return inTable;
    }

    public String getFkName() {
        return fkName;
    }


    public boolean isEquals(String table){
        return table.equals(inTable);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
