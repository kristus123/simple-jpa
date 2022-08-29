package org.simplejpa;

import lombok.SneakyThrows;
import org.wfm.persistence.column.names.ColumnName;

import java.sql.ResultSet;
import java.time.LocalDate;

public class RsHelper {

    @SneakyThrows
    public static LocalDate getLocalDate(ColumnName c, ResultSet rs) {
        return rs.getDate(c.toString()).toLocalDate();
    }

    @SneakyThrows
    public static String getString(ColumnName c, ResultSet rs) {
        return rs.getString(c.toString());
    }

    @SneakyThrows
    public static int getInt(ColumnName c, ResultSet rs) {
        return rs.getInt(c.toString());
    }

    @SneakyThrows
    public static Object get(ColumnName c, ResultSet rs) {
        return switch (c.getColumnValueType()) {
            case INTEGER -> rs.getInt(c.toString());
            case STRING -> rs.getString(c.toString());
            case LOCAL_DATE -> rs.getDate(c.toString());
            case ARRAY -> rs.getArray(c.toString()).getArray();
        };
    }

}
