package org.simplejpa;

import lombok.SneakyThrows;
import org.simplejpa.column.Column;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;

class PreparedStatementHelper {

    @SneakyThrows
    static void set(int i, Column<?> c, PreparedStatement ps) {
        switch (c.getColumnName().getColumnValueType()) {
            case INTEGER -> ps.setInt(i, (Integer) c.getColumnValue());
            case STRING -> ps.setString(i, c.getColumnValue().toString());
            case LOCAL_DATE -> ps.setDate(i, Date.valueOf(((LocalDate) c.getColumnValue())));
            case ARRAY -> ps.setObject(i, c.getColumnValue());
        }
    }

}
