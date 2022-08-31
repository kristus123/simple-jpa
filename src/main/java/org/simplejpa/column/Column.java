package org.simplejpa.column;

import lombok.Getter;

import java.util.Objects;

@Getter
public abstract class Column<T> {

    private final ColumnName columnName;
    private final T ColumnValue;

    public Column(ColumnName columnName, T columnValue) {
        this.columnName = columnName;
        this.ColumnValue = Objects.requireNonNull(columnValue, "Value for" + columnName + " was null");
    }

    @Override
    public String toString() {
        return ColumnValue.toString();
    }

}
