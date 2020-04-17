package com.furious.util.db;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.MariaDbDataSource;

class BackupTest {

    private static MariaDbDataSource dataSource;

    @BeforeAll
    static void getConnection() throws Exception {
        String url = "jdbc:mariadb://localhost:3306/mytest2";
        String username = "root";
        String password = "root";
        dataSource = new MariaDbDataSource(url);
        dataSource.setUserName(username);
        dataSource.setPassword(password);
    }

    @Test
    void execute() {
        new Backup(dataSource, "/Users/furious/Temp").execute();
    }
}