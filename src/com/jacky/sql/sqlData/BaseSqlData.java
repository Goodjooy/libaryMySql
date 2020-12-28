package com.jacky.sql.sqlData;

import java.util.Objects;

public abstract class BaseSqlData {
    protected String name;
    private final boolean isEmpty;

    public BaseSqlData(String name) {
        this.name = name;isEmpty=false;
    }

    public BaseSqlData(String name,boolean isEmpty){
        this.name=name;
        this.isEmpty = isEmpty;
    }

    @Override
    public abstract String toString();

    public boolean isEmpty(){
        return this.isEmpty;
    }

    public String dataName() {
        return name;
    }
    public void ChangeName(String newName){
        this.name=newName;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseSqlData that = (BaseSqlData) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
