package com.home.utilities.services;

import java.sql.SQLException;

public interface TruncateDatabaseService {

    void truncateAllTablesFromDatabaseAndInsertTwoUsers() throws SQLException;
}
