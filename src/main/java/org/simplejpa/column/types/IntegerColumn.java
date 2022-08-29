package org.simplejpa.column.types;

import org.wfm.persistence.RsHelper;
import org.wfm.persistence.column.names.ColumnNameWithValueTypeInteger;

import java.sql.ResultSet;

public class IntegerColumn extends Column<Integer> {

    public IntegerColumn(ColumnNameWithValueTypeInteger columnName, Integer columnValue) {
        super(columnName, columnValue);
    }

    public IntegerColumn(ColumnNameWithValueTypeInteger columnName, ResultSet rs) {
        super(columnName, RsHelper.getInt(columnName, rs));
    }
}
