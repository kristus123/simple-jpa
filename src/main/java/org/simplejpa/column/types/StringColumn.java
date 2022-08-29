package org.simplejpa.column.types;

import org.wfm.persistence.RsHelper;
import org.wfm.persistence.column.names.ColumnNameWithValueTypeString;

import java.sql.ResultSet;

public class StringColumn extends Column<String> {

    public StringColumn(ColumnNameWithValueTypeString columnName, String columnValue) {
        super(columnName, columnValue);
    }

    public StringColumn(ColumnNameWithValueTypeString columnName, ResultSet rs) {
        this(columnName, RsHelper.getString(columnName, rs));
    }
}
