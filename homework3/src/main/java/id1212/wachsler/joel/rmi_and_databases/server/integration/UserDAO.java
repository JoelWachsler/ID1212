package id1212.wachsler.joel.rmi_and_databases.server.integration;

import id1212.wachsler.joel.rmi_and_databases.common.CredentialDTO;
import id1212.wachsler.joel.rmi_and_databases.common.RegisterException;

import javax.security.auth.login.LoginException;
import java.sql.*;

import static java.sql.Types.NULL;

/**
 * Data access object used to communicate with the database.
 * All calls to the database are encapsulated in this class.
 */
public class UserDAO extends DB {
  private static final String USERS_TABLE = "users";
  private PreparedStatement loginStmt;
  private PreparedStatement registerStmt;
  private PreparedStatement userExistsStmt;
  private PreparedStatement getUserFromIdStmt;
  private static UserDAO instance;

  private UserDAO() {
    try {
      prepareStatements(super.getConnection());
    } catch (SQLException e) {
      System.err.println("Failed to prepare UserDAO statements..");
      e.printStackTrace();
    }
  }

  public static UserDAO getInstance() {
    if (instance == null) instance = new UserDAO();

    return instance;
  }

  private void prepareStatements(Connection connection) throws SQLException {
    registerStmt = connection.prepareStatement(
      "INSERT INTO " + USERS_TABLE +
        " VALUES (?, ?, ?)");

    getUserFromIdStmt = connection.prepareStatement(
      "SELECT * FROM " + USERS_TABLE +
        " WHERE " +
        "`id`=?");

    userExistsStmt = connection.prepareStatement(
      "SELECT * FROM " + USERS_TABLE +
        " WHERE" +
        "`username`=?");

    loginStmt = connection.prepareStatement(
        "SELECT * FROM " + USERS_TABLE +
          " WHERE" +
          "`username`=? AND" +
          "`password`=?");
  }

  /**
   * Tries to login the user.
   *
   * @param credentialDTO The credentialDTO to use when logging in.
   * @return The ID of the user with the provided credentialDTO.
   */
  public long login(CredentialDTO credentialDTO) throws LoginException {
    try {
      loginStmt.setString(1, credentialDTO.getUsername());
      loginStmt.setString(2, credentialDTO.getPassword());
      ResultSet result = loginStmt.executeQuery();

      if (!result.next()) throw new LoginException("Invalid username or password!");

      return result.getInt("id");
    } catch (SQLException e) {
      System.err.println("Failed to execute login query!");
      e.printStackTrace();

      throw new LoginException("Something went terribly wrong when trying to login...");
    }
  }

  /**
   * Registers a user using the provided credentials.
   *
   * @param credentialDTO The credentials
   * @return The user id of the registered user.
   * @throws RegisterException When failing to register a user.
   */
  public long register(CredentialDTO credentialDTO) throws RegisterException {
    try {
      if (userExists(credentialDTO.getUsername()))
        throw new RegisterException(String.format("A user with the username: \"%s\" already exists...",
          credentialDTO.getUsername()));

      registerStmt.setInt(1, NULL);
      registerStmt.setString(2, credentialDTO.getUsername());
      registerStmt.setString(3, credentialDTO.getPassword());

      int result = registerStmt.executeUpdate();

      if (result == 0) throw new RegisterException("Unknown register error!");

      return login(credentialDTO);
    } catch (SQLException e) {
      System.err.println("Failed to execute register query!");
      e.printStackTrace();

      throw new RegisterException("Something went terribly wrong when trying to register...");
    } catch (LoginException e) {
      System.err.println("Failed to login after register!");

      throw new RegisterException("Failed to login after register...");
    }
  }

  private boolean userExists(String username) throws SQLException {
    userExistsStmt.setString(1, username);

    ResultSet result = userExistsStmt.executeQuery();

    return result.next();
  }

  /**
   * Checks if a user with the provided id exists.
   *
   * @param userId The user id to check if a user has it.
   * @return <code>true</code> if the user exists. <code>false</code> if the user does not exist.
   */
  public boolean userExists(long userId) {
    if (userId == 0) return false;

    try {
      getUserFromIdStmt.setLong(1, userId);

      ResultSet result = getUserFromIdStmt.executeQuery();

      return result.next();
    } catch (SQLException e) {
      System.err.println("Failed to execute the userExists query!");
      e.printStackTrace();

      return false;
    }
  }

  public String getUsername(int id) throws Exception {
    try {
      getUserFromIdStmt.setLong(1, id);

      ResultSet result = getUserFromIdStmt.executeQuery();

      if (!result.next()) throw new Exception("User does not exist");

      return result.getString("username");
    } catch (SQLException e) {
      System.err.println("Failed to get username");

      e.printStackTrace();

      return "";
    }
  }
}
