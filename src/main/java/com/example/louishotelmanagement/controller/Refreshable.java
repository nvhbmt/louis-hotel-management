package com.example.louishotelmanagement.controller;

import java.sql.SQLException;

public interface Refreshable {
    void refreshData() throws SQLException;
}
