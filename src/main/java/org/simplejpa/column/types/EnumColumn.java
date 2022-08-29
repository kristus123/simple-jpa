package org.simplejpa.column.types;

import org.wfm.persistence.column.names.ColumnNameWithValueTypeString;

import java.sql.ResultSet;

public class EnumColumn<T> extends StringColumn {

    public EnumColumn(ColumnNameWithValueTypeString columnName, T columnValue) {
        super(columnName, columnValue.toString());
    }

    public EnumColumn(ColumnNameWithValueTypeString columnName, ResultSet rs) {
        super(columnName, rs);
    }
}
