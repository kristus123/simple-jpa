package org.simplejpa;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import org.simplejpa.column.Column;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DataSource {

    private static final HikariDataSource hikariDataSource; static {

        var c = new HikariConfig();
        c.setJdbcUrl(String.format("jdbc:postgresql://%s:5432/wfm", Objects.requireNonNull(System.getenv("DATABASE_URL"))));
        c.setUsername("postgres");
        c.setPassword("postgres");

        hikariDataSource = new HikariDataSource(c);
    }

    @SneakyThrows
    public void insert(String table, List<Column> c) {
        var columns = new ColumnsToInsert(c);

        try (var connection = hikariDataSource.getConnection()) {

            var ps = connection.prepareStatement(String.format("INSERT INTO %s (%s) VALUES(%s)",
                    table, columns.columnNamesSeparatedByComma(), columns.valuesInQuestionMarks()));

            for (var i : columns.all.keySet()) {
                PreparedStatementHelper.set(i, columns.all.get(i), ps);
            }

            ps.execute();
        }
    }

    @SneakyThrows
    public void update(String table, Column update, Column id) {

        try (var connection = hikariDataSource.getConnection()) {

            String query = String.format("UPDATE %s SET %s=? WHERE %s=?",
                    table, update.getColumnName(), id.getColumnName());

            var ps = connection.prepareStatement(query);
            PreparedStatementHelper.set(1, update, ps);
            PreparedStatementHelper.set(2, id, ps);

            ps.executeUpdate();
        }
    }

    @SneakyThrows
    public <T> T findByExactMatch(String table, List<Column> columns, Function<ResultSet, T> mapper) {
        try (var connection = hikariDataSource.getConnection()) {

            var ps = connection.prepareStatement(Select.byExactMatch(table, columns));
            new ColumnsToInsert(columns).setAll(ps);

            var query = ps.executeQuery();
            if (query.next()) {
                return mapper.apply(query);
            } else {
                throw new RuntimeException("Could not find by columns " + columns);
            }
        }
    }

    @SneakyThrows
    public <T> T insert(String table, List<Column> c, Function<ResultSet, T> function) {
        var columns = new ColumnsToInsert(c);

        try (var connection = hikariDataSource.getConnection()) {

            var ps = connection.prepareStatement(String.format("INSERT INTO %s (%s) VALUES(%s) RETURNING *",
                    table, columns.columnNamesSeparatedByComma(), columns.valuesInQuestionMarks()));

            for (var i : columns.all.keySet()) {
                PreparedStatementHelper.set(i, columns.all.get(i), ps);
            }

            var rs = ps.executeQuery();
            if (rs.next()) {
                return function.apply(rs);
            } else {
                throw new RuntimeException("XXX");
            }

        }
    }

    @SneakyThrows
    public <T> List<T> findAllByExactMatch(String table, List<Column> columns, Function<ResultSet, T> mapper) {
        try (var connection = hikariDataSource.getConnection()) {

            var ps = connection.prepareStatement(Select.byExactMatch(table, columns));
            new ColumnsToInsert(columns).setAll(ps);

            var q = ps.executeQuery();
            List<T> list = new ArrayList<>();

            while (q.next()) {
                list.add(mapper.apply(q));
            }
            return list;
        }
    }

    @SneakyThrows
    public <T> List<T> findAllIfOneMatch(String table, List<Column> columns, Function<ResultSet, T> mapper) {
        try (var connection = hikariDataSource.getConnection()) {

            var ps = connection.prepareStatement(Select.atLeastOneMatch(table, columns));
            new ColumnsToInsert(columns).setAll(ps);

            var q = ps.executeQuery();
            List<T> list = new ArrayList<>();

            while (q.next()) {
                list.add(mapper.apply(q));
            }
            return list;
        }
    }

    @SneakyThrows
    public void delete(String table, Column idColumn) {
        try (var connection = hikariDataSource.getConnection()) {

            var ps = connection.prepareStatement(String.format(
                    "DELETE FROM %s WHERE %s=?", table, idColumn.getColumnName()));
            PreparedStatementHelper.set(1, idColumn, ps);

            ps.execute();
        }
    }

    @SneakyThrows
    public Map<ColumnName, Object> findByExactMatchAndExtract(Table table,
                                                              List<Column> queryColumns,
                                                              List<ColumnName> columnsToExtract) {
        try (var connection = hikariDataSource.getConnection()) {

            var ps = connection.prepareStatement(Select.byExactMatch(table, queryColumns, columnsToExtract));
            new ColumnsToInsert(queryColumns).setAll(ps);

            var rs = ps.executeQuery();

            if (rs.next()) {
                return columnsToExtract.stream().collect(Collectors.toMap(
                        c -> c,
                        c -> RsHelper.get(c, rs)));
            } else {
                throw new RuntimeException("Could not find by columns " + queryColumns);
            }
        }
    }

    @SneakyThrows
    public void removeElementFromArray(String table, Column<?> id, Column<?> arrayColumnValue) {
        try (var connection = hikariDataSource.getConnection()) {

            var x = String.format("UPDATE %s set %s = array_remove(%s, %s) WHERE %s=?",
                    table,
                    arrayColumnValue.getColumnName(),
                    arrayColumnValue.getColumnName(),
                    arrayColumnValue.getColumnValue().toString(),
                    id.getColumnName());
            var ps = connection.prepareStatement(x);
            PreparedStatementHelper.set(1, id, ps);

            ps.execute();
        }
    }

    @SneakyThrows
    public void addElementToArray(String table, Column<?> id, Column<?> add) {
        try (var connection = hikariDataSource.getConnection()) {

            var x = String.format("UPDATE %s set %s = array_append(%s, %s) WHERE %s=?",
                    table,
                    add.getColumnName(),
                    add.getColumnName(),
                    add.getColumnValue().toString(),
                    id.getColumnName());
            var ps = connection.prepareStatement(x);
            PreparedStatementHelper.set(1, id, ps);

            ps.execute();
        }
    }
}
