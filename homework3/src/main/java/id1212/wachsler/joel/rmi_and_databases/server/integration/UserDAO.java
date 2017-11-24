package id1212.wachsler.joel.rmi_and_databases.server.integration;

import id1212.wachsler.joel.rmi_and_databases.common.Credentials;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Data access object used to communicate with the database.
 * All calls to the database is encapsulated in this class.
 */
public class UserDAO {
  private static final String USER_TABLE = "users";
  private PreparedStatement loginStmt;

  public UserDAO(String datasource, Credentials credentials)
    throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {

    Class.forName("com.mysql.jdbc.Driver").newInstance(); // Get driver
    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + datasource + "?" +
      "user=" + credentials.getUsername() + "&password=" + credentials.getPassword());

    prepareStatements(connection);
  }

  private void prepareStatements(Connection connection) throws SQLException {
    final String loginQuery = String.format("SELECT * FROM %s WHERE `username`=? AND `password`=?", USER_TABLE);

    loginStmt = connection.prepareStatement(loginQuery);
  }

  public long login(String username, String password) {
    return -1;
  }
}
