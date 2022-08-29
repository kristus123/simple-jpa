package org.simplejpa.column;

import java.sql.JDBCType;

public enum ColumnValueType {
    STRING(JDBCType.VARCHAR),
    LOCAL_DATE(JDBCType.DATE),
    INTEGER(JDBCType.INTEGER),
    ARRAY(JDBCType.ARRAY),
    ;

    public final JDBCType jdbcType;

    ColumnValueType(JDBCType jdbcType) {
        this.jdbcType = jdbcType;
    }
}
