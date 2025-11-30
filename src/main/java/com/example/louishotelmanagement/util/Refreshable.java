package com.example.louishotelmanagement.util;

import java.sql.SQLException;

public interface Refreshable {
    void refreshData() throws SQLException, Exception;
}

