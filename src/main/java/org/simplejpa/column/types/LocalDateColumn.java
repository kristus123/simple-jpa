package org.simplejpa.column.types;

import org.wfm.persistence.RsHelper;
import org.wfm.persistence.column.names.ColumnNameWithValueTypeLocalDate;

import java.sql.ResultSet;
import java.time.LocalDate;

public class LocalDateColumn extends Column<LocalDate> {

    public LocalDateColumn(ColumnNameWithValueTypeLocalDate columnName, LocalDate columnValue) {
        super(columnName, columnValue);
    }

    public LocalDateColumn(ColumnNameWithValueTypeLocalDate columnName, ResultSet rs) {
        super(columnName, RsHelper.getLocalDate(columnName, rs));
    }
}
