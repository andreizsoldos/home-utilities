package com.home.utilities.service;

import java.sql.SQLException;

public interface TruncateDatabaseService {

    void truncateAllTablesFromDatabaseAndInsertTwoUsers() throws SQLException;
}
