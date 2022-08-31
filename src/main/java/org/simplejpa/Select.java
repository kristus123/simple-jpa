package org.simplejpa;

import org.flywaydb.core.internal.database.base.Table;
import org.simplejpa.column.Column;
import org.simplejpa.column.ColumnName;

import java.util.List;
import java.util.stream.Collectors;

class Select {

    static String byExactMatch(Table table, List<Column> findBy, List<ColumnName> columnsToGet) {

        var query = new StringBuilder(String.format("SELECT %s FROM %s WHERE ",
                columnsToGet.stream().map(ColumnName::toString).collect(Collectors.joining(",")),
                table.name()));

        for (var c : findBy) {
            query.append(c.getColumnName()).append("=?").append(" AND ");
        }

        query.setLength(query.length() - 5);

        return query.toString();
    }

    static String byExactMatch(String table, List<Column> columns) {

        var query = new StringBuilder(String.format("SELECT * FROM %s WHERE ", table));
        for (var c : columns) {
            query.append(c.getColumnName()).append("=?").append(" AND ");
        }

        query.setLength(query.length() - 5);

        return query.toString();
    }

    static String atLeastOneMatch(String table, List<Column> columns) {

        var query = new StringBuilder(String.format("SELECT * FROM %s WHERE ", table));
        for (var c : columns) {
            query.append(c.getColumnName()).append("=?").append(" OR ");
        }

        query.setLength(query.length() - 5);

        return query.toString();
    }
}
