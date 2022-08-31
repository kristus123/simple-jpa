package org.simplejpa;

import org.simplejpa.column.Column;

import java.sql.PreparedStatement;
import java.util.*;

class ColumnsToInsert {

    final Map<Integer, Column> all;

    ColumnsToInsert(List<Column> columns) {
        int i = 0;
        Map<Integer, Column> map = new HashMap<>();

        for (var c : columns) {
            i += 1;
            map.put(i, c);
        }

        this.all = map;
    }

    void setAll(PreparedStatement ps) {
        for (var i : all.keySet()) {
            PreparedStatementHelper.set(i, all.get(i), ps);
        }
    }

    String valuesInQuestionMarks() {
        var s = new StringBuilder();
        s.append("?,".repeat(all.size()));
        s.setLength(Math.max(s.length() - 1, 0));
        return s.toString();
    }

    String columnNamesSeparatedByComma() {
        var s = new StringBuilder();

        all.values().forEach(c -> s.append(c.getColumnName()).append(","));
        s.setLength(Math.max(s.length() - 1, 0));

        return s.toString();
    }

}
