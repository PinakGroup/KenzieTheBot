package com.kps.kenziethebot.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource {

    /**
     * This class establishes the connection to the central database.
     */

    private static DataSource instance = new DataSource();
    private final String DB_NAME = getClass().getResource("./central_database.db").toString();
    private final String DATABASE_PATH = "jdbc:sqlite:" + DB_NAME;
    private Connection centralConnection;

    private DataSource() {

    }

    public boolean open() {

        try {

            centralConnection = DriverManager.getConnection(DATABASE_PATH);
            return true;

        } catch (SQLException sqle) {

            System.out.println("[X] Exception while initializing database.");
            System.out.println(sqle.getMessage());
            return false;
        }
    }

    public void close() {

        try {

            if (centralConnection != null) {

                System.out.println("[#] Database connection was closed.");
                centralConnection.close();
            } else {

                System.out.println("[?] Please close the connection manually.");
            }

        } catch (SQLException sqle) {

            System.out.println("[X] Unable to close database.");
            System.out.println(sqle.getMessage());
        }

    }

    public static DataSource getInstance() {

        return instance;
    }

    public Connection getCentralConnection() {

        return centralConnection;
    }
}
