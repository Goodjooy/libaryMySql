package com.jacky.sql.SqlTableAtom;

import com.jacky.sql.err.TableAtomIDNotSetException;
import com.jacky.sql.sqlData.*;

import java.beans.IndexedPropertyDescriptor;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.HashMap;

//用于处理表格每行元素
public abstract class BaseAtom {
    protected  BaseSqlData primaryKeyValue;
    protected String tableName;
    protected HashMap<String,String> data=new HashMap<>();


    BaseAtom(String tableName, ResultSet set){
        this.tableName = tableName;
        //this.primaryKeyName = primaryKeyName;
        loadFromResultSet(set);
    }
     BaseAtom(String tableName, BaseSqlData... data){
        this.tableName = tableName;
         //this.primaryKeyName = primaryKeyName;
         for (BaseSqlData d :
                data) {
            this.data.put(d.dataName(), d.toString());
        }
    }
    public abstract void loadFromResultSet(ResultSet set);
    public  String generateInsertStatement(){
        StringBuilder builder=new StringBuilder(String.format("INSERT INTO %s VALUE(",tableName));
        boolean needDiv=false;

        for (String v:data.values()){
            if(needDiv){
                builder.append(",");
            }
            builder.append(v);
            needDiv=true;
        }
        builder.append(")");
        return builder.toString();

    }
    public String generateDeleteStatement()throws TableAtomIDNotSetException{
        if(primaryKeyValue ==null){
            throw  new TableAtomIDNotSetException(this);
        }else {
            return String.format("DELETE FORM %s WHERE %s=%s", tableName,
                    primaryKeyValue.dataName(), primaryKeyValue.toString());
        }
    }
    public  String generateUpdateStatement(){
        if(primaryKeyValue ==null){
            throw  new TableAtomIDNotSetException(this);
        }else {
            StringBuilder builder=new StringBuilder("UPDATE %s SET ");
            boolean needDiv=false;
            for (String k :
                    data.values()) {
                if(needDiv){
                    builder.append(",");
                }
                builder.append(String.format("%s=%s", k, data.get(k)));
                needDiv=true;
            }
            builder.append(String.format(" WHERE %s=%s",primaryKeyValue.dataName(),primaryKeyValue.toString()));
            return builder.toString();
        }
    }

    public  void setData(BaseSqlData data){
        this.data.put(data.dataName(),data.toString());
    }
    public  void setData(String  name,boolean value){
        setData(new SqlBoolean(name,value));
    }
    public void setData(String name,int value) {
        setData(new SqlNumber(name,value));
    }
    public void  setData(String name,String value){
        setData(new SqlString(name,value));
    }
    public void  setData(String name, LocalDate date){
        setData(new SqlData(name,date));
    }
    public  void  setNullData(String name){
        setData(new nullData(name));
    }
    public String getData(String name){
        return this.data.get(name);
    }

    public void setPrimaryKey(BaseSqlData primaryKey) {
            this.primaryKeyValue = primaryKey;
    }

    public String getTableName() {
        return tableName;
    }
}
