package com.jacky.sql.SqlTableAtom;

import com.jacky.sql.err.ForeignInKeyNotFoundException;
import com.jacky.sql.err.ForeignKeyNotFoundException;
import com.jacky.sql.err.TableAtomIDNotSetException;
import com.jacky.sql.foreignKey.ForeignInKey;
import com.jacky.sql.foreignKey.ForeignKey;
import com.jacky.sql.sqlData.*;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

//用于处理表格每行元素
public abstract class BaseAtom {
    // 主键
    protected BaseSqlData primaryKeyValue = null;
    // 表名称
    protected String tableName;
    // 表内容
    protected HashMap<String, BaseSqlData> data = new HashMap<>();
    // 表数据名称
    protected final ArrayList<String> dataNames = new ArrayList<>();

    // 表外键和指向表的外键
    protected final ArrayList<ForeignKey> foreignKeys = new ArrayList<>();
    protected final ArrayList<ForeignInKey> foreignInTables = new ArrayList<>();

    BaseAtom(String tableName) {
        this.tableName = tableName;
    }

    BaseAtom() {
        tableName = "";
        initial();
    }

    BaseAtom(ResultSet set) throws SQLException {
        tableName = "";
        // this.primaryKeyName = primaryKeyName;
        initial();
        loadFromResultSet(set);
    }

    BaseAtom(BaseSqlData... data) {
        initial();
        tableName = "";
        // this.primaryKeyName = primaryKeyName;
        for (BaseSqlData d : data) {
            this.data.put(d.dataName(), d);
        }
    }

    /**
     * <strong>初始化时由构造函数调用</strong> 根据给定的该行的数据初始化数据
     *
     * @param set 数据库请求返回的表格容器
     * @throws SQLException 对set操作可能产生SQL错误
     */
    public abstract void loadFromResultSet(ResultSet set) throws SQLException;

    /**
     * <strong>初始化时由构造函数调用</strong> 初始化表格数据 包括所在表名称、包含元素、外键、指向自身的外键、主键
     */
    protected abstract void initial();

    /**
     * 检查主键是否已经被设置
     *
     * @return 是否设置了主键
     */
    public abstract boolean isSetPrimaryKey();

    public final String generateInsertStatement() {
        StringBuilder builder = new StringBuilder(String.format("INSERT INTO %s VALUE(", tableName));
        boolean needDiv = false;

        for (String v : dataNames) {
            BaseSqlData data = this.data.get(v);

            if (needDiv) {
                builder.append(",");
            }

            if (data == null)
                builder.append(new nullData(v).toString());
            else
                builder.append(data.toString());
            needDiv = true;
        }
        builder.append(")");
        return builder.toString();

    }

    public final String generateDeleteStatement() throws TableAtomIDNotSetException {
        if (isSetPrimaryKey()) {
            throw new TableAtomIDNotSetException(this);
        } else {
            return String.format("DELETE FROM %s WHERE %s=%s", tableName, primaryKeyValue.dataName(),
                    primaryKeyValue.toString());
        }
    }

    public final String generateUpdateStatement(String... updateKeys) {
        if (isSetPrimaryKey()) {
            throw new TableAtomIDNotSetException(this);
        } else {
            StringBuilder builder = new StringBuilder(String.format("UPDATE %s SET ",tableName));
            boolean needDiv = false;
            for (String k : updateKeys) {
                // 如果值为空或者为主键，表示不修改该值，跳过
                if (k.equals(getPrimaryKeyName()) || !data.containsKey(k)) {
                    continue;
                }
                if (needDiv) {
                    builder.append(",");
                }
                builder.append(String.format("%s=%s", k, data.get(k).toString()));
                needDiv = true;
            }
            builder.append(String.format(" WHERE %s=%s", primaryKeyValue.dataName(), primaryKeyValue.toString()));
            return builder.toString();
        }
    }

    public final String generateSelfSearchStatement() {
        StringBuilder builder = new StringBuilder(String.format("SELECT *FROM %s WHERE ", tableName));
        boolean isFirst = true;
        for (BaseSqlData d : data.values()) {
            if (d.isEmpty())
                continue;
            if (!isFirst) {
                builder.append(",");
            }
            builder.append(String.format("%s=%s", d.dataName(), d.toString()));
            isFirst = false;
        }
        return builder.toString();
    }

    public BaseAtom generateForeignKeyAtom(BaseAtom atom) {
        for (ForeignKey k : foreignKeys) {
            if (k.getTargetTable().equals(atom.getTableName())) {
                atom.setData(generateForeignSqlData(k));
                return atom;
            }
        }
        throw new ForeignKeyNotFoundException(tableName, atom.tableName);
    }

    public final String generateForeignKeySearchStatement(BaseAtom atom) {
        for (ForeignKey k : foreignKeys) {
            if (k.getTargetTable().equals(atom.getTableName())) {
                atom.setData(generateForeignSqlData(k));
                return atom.generateSelfSearchStatement();
            }
        }
        throw new ForeignKeyNotFoundException(tableName, atom.tableName);
    }

    public BaseSqlData generateForeignSqlData(ForeignKey k) {
        BaseSqlData t;
        t = getSqlData(k.getFkName());
        t.ChangeName(k.getTkName());
        return t;
    }

    public BaseSqlData generateForeignSqlData(String table) {
        BaseSqlData t;
        for (ForeignKey foreignKey : foreignKeys) {
            if (foreignKey.getTargetTable().equals(table)) {
                return generateForeignSqlData(foreignKey);
            }
        }
        throw new ForeignKeyNotFoundException(tableName, table);
    }

    public BaseSqlData generateForeignInSqlData(String inTable) {
        BaseSqlData t;
        for (ForeignInKey foreignInTable : foreignInTables) {
            if (foreignInTable.isEquals(inTable)) {
                t = getSqlData(foreignInTable.getIkName());
                t.ChangeName(foreignInTable.getFkName());
                return t;
            }
        }
        throw new ForeignInKeyNotFoundException(this.tableName, inTable);
    }

    public void setData(BaseSqlData data) {
        if (data.dataName().equals(getPrimaryKeyName())) {
            setPrimaryKey(data);
        }
        if (dataNames.contains(data.dataName()))
            this.data.put(data.dataName(), data);
    }

    public void setData(String name, boolean value) {
        setData(new SqlBoolean(name, value));
    }

    public void setData(String name, int value) {
        setData(new SqlNumber(name, value));
    }

    public void setData(String name, String value) {
        setData(new SqlString(name, value));
    }

    public void setData(String name, LocalDate date) {
        setData(new SqlData(name, date));
    }

    public void setData(String name, Date date) {
        setData(new SqlData(name, date));
    }

    public void setNullData(String name) {
        setData(new nullData(name));
    }

    public String getData(String name) {
        return this.data.get(name).toString();
    }

    public BaseSqlData getSqlData(String name) {
        return this.data.get(name);
    }

    public void setPrimaryKey(BaseSqlData primaryKey) {
        if (this.primaryKeyValue != null) {
            if (!this.primaryKeyValue.dataName().equals(primaryKey.dataName()))
                return;
        }
        this.primaryKeyValue = primaryKey;
        if (!data.containsKey(primaryKey.dataName())) {
            data.put(primaryKey.dataName(), primaryKey);
        }
    }

    public String getPrimaryKeyName() {
        return primaryKeyValue.dataName();
    }

    public String getPrimaryKeyValue() {
        return primaryKeyValue.toString();
    }

    public String getTableName() {
        return tableName;
    }

}
