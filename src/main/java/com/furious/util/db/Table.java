package com.furious.util.db;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Table {
    private final String name;
    private final String quote;
    private String insertSqlStatement;
    private Set<String> fields = new TreeSet<>();
    private List<Row> rows = new LinkedList<>();

    /**
     * insert into `table` (`f1`,'f2'...) VALUES (v1,v2...)
     */
    public String buildInsertSqlStatement() {
        if (!Objects.isNull(insertSqlStatement)) {
            return insertSqlStatement;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").append(quote).append(name).append(quote).append("(");
        for (String field : fields) {
            sb.append(quote).append(field).append(quote).append(",");
        }
        sb.deleteCharAt(sb.length() - 1).append(") VALUES ");
        for (Row row : rows) {
            sb.append("\n(");
            for (Column<?> column : row.getColumns()) {
                //todo set value by Types
                sb.append("'").append(column.getValue()).append("',");
            }
            sb.deleteCharAt(sb.length() - 1).append("),");
        }
        sb.deleteCharAt(sb.length() - 1).append(";\n");
        this.insertSqlStatement = sb.toString();
        return insertSqlStatement;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (String field : fields) {
            sb.append(field).append(" | ");
        }

        sb.append("\n");
        int maxLine = Math.min(10, rows.size());
        Row row;
        for (int i = 0; i < maxLine; i++) {
            row = rows.get(i);
            for (Column<?> column : row.getColumns()) {
                sb.append(column.getValue()).append(" | ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
