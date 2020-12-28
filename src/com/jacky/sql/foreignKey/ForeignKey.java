package com.jacky.sql.foreignKey;

public class ForeignKey {
    private final String fkName;
    private final String targetTable;
    private final String tkName;

    public ForeignKey(String fkName, String targetTable, String tkName)
    {

        this.fkName = fkName;
        this.targetTable = targetTable;
        this.tkName = tkName;
    }

    public String getFkName() {
        return fkName;
    }

    public String getTargetTable() {
        return targetTable;
    }

    public String getTkName() {
        return tkName;
    }
}
