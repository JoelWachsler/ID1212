package id1212.wachsler.joel.rmi_and_databases.server.integration;

import id1212.wachsler.joel.rmi_and_databases.common.CredentialDTO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Handles the connection to the database.
 */
abstract class DB {
  private static Connection connection;

  public static void init(String datasource, CredentialDTO dbLogin) throws SQLException {
    final String connectionString = String.format("jdbc:mysql://localhost:3306/%s?user=%s&password=%s",
      datasource, dbLogin.getUsername(), dbLogin.getPassword());

    connection = DriverManager.getConnection(connectionString);
  }

  Connection getConnection() {
    if (connection == null) throw new IllegalStateException("Connection is not initialized!");

    return connection;
  }
}
