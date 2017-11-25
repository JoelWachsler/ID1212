package id1212.wachsler.joel.rmi_and_databases.server.integration;

import id1212.wachsler.joel.rmi_and_databases.common.Credentials;

import javax.security.auth.login.LoginException;
import java.sql.*;

/**
 * Data access object used to communicate with the database.
 * All calls to the database is encapsulated in this class.
 */
public class UserDAO {
  private static final String USER_TABLE = "users";
  private PreparedStatement loginStmt;

  public UserDAO(String datasource, Credentials credentials)
    throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {

    final String connectionString = String.format("jdbc:mysql://localhost:3306/%s?user=%s&password=%s",
      datasource, credentials.getUsername(), credentials.getPassword());

    Connection connection = DriverManager.getConnection(connectionString);

    prepareStatements(connection);
  }

  private void prepareStatements(Connection connection) throws SQLException {
    final String loginQuery = String.format("SELECT * FROM %s WHERE `username`=? AND `password`=?", USER_TABLE);

    loginStmt = connection.prepareStatement(loginQuery);
  }

  /**
   * Tries to login the user.
   *
   * @param credentials The credentials to use when logging in.
   * @return The ID of the user with the provided credentials.
   */
  public long login(Credentials credentials) throws LoginException {
    try {
      loginStmt.setString(1, credentials.getUsername());
      loginStmt.setString(2, credentials.getPassword());
      ResultSet result = loginStmt.executeQuery();

      if (!result.next()) throw new LoginException("Invalid username or password!");

      return result.getInt("id");
    } catch (SQLException e) {
      System.err.println("Failed to execute query!");
      e.printStackTrace();
    }

    throw new LoginException("Something went terribly wrong when logging in...");
  }
}
