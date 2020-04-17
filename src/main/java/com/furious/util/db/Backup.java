package com.furious.util.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import com.furious.util.Throwables;

public class Backup {

    private DatabaseMetaData databaseMetaData;
    private Statement stmt;
    private String quote = "";

    private Fsync fsync;

    private final List<String> tableNames = new LinkedList<>();
    private final List<Table> tables = new LinkedList<>();

    public Backup(DataSource dataSource, String path) {
        try {
            Connection connection = dataSource.getConnection();
            this.databaseMetaData = connection.getMetaData();
            this.stmt = connection.createStatement();
            this.quote = databaseMetaData.getIdentifierQuoteString();
            this.tableNames.addAll(tableNames());
            this.fsync = new Fsync(path);
        } catch (SQLException e) {
            Throwables.raise(e);
        }
    }

    public void execute() {
        try {
            buildTables();
            for (Table table : tables) {
                fsync.sync(table.buildInsertSqlStatement());
            }
        } catch (SQLException e) {
            Throwables.raise(e);
        }
    }

    private List<String> tableNames() throws SQLException {
        List<String> tables = new LinkedList<>();
        ResultSet rs = databaseMetaData.getTables(null, null, null, null);
        while (rs.next()) {
            tables.add(rs.getString("TABLE_NAME"));
        }
        return tables;
    }

    private void buildTables() throws SQLException {
        for (String tableName : tableNames) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + quote + tableName + quote);
            ResultSetMetaData metaData = rs.getMetaData();

            int columnCount = metaData.getColumnCount();

            Table table = new Table(tableName, quote);
            for (int i = 0; i < columnCount; i++) {
                table.getFields().add(metaData.getColumnName(i + 1));
            }
            Row row;
            Column<String> column;
            while (rs.next()) {
                row = new Row();
                for (int i = 0; i < columnCount; i++) {
                    column = new Column<>();
                    column.setField(metaData.getColumnName(i + 1));
                    column.setType(metaData.getColumnType(i + 1));
                    //todo set value by Types
                    column.setValue(rs.getString(i + 1));
                    row.getColumns().add(column);
                }
                table.getRows().add(row);
            }
            tables.add(table);
        }
    }
}
