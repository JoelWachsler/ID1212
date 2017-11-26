package id1212.wachsler.joel.rmi_and_databases.server.user;

import com.sun.istack.internal.NotNull;
import id1212.wachsler.joel.rmi_and_databases.common.dto.CredentialDTO;
import id1212.wachsler.joel.rmi_and_databases.common.exceptions.RegisterException;
import id1212.wachsler.joel.rmi_and_databases.server.integration.UserDAO;
import id1212.wachsler.joel.rmi_and_databases.server.integration.UserDoesNotExistException;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;

public class User {
  private UserDAO userDAO;
  private long id;
  private String username;

  private void init() {
    userDAO = UserDAO.getInstance();
  }

  public User() {
    init();
  }

  public User(long userId) throws IllegalAccessException {
    init();

    if (!userDAO.userExists(userId)) throw new IllegalAccessException("You are not logged in!");

    id = userId;
  }

  /**
   * Logs in a user.
   *
   * @param credentialDTO The credentials used to authenticate the user.
   * @return The user id of the user.
   * @throws LoginException If the user is already logged in or something went wrong when logging in.
   */
  public long login(@NotNull CredentialDTO credentialDTO) throws LoginException {
    if (id != 0) throw new LoginException("You're already logged in!");

    id = userDAO.login(credentialDTO);

    return id;
  }

  /**
   * Registers a user.
   *
   * @param credentialDTO The credentials used for the registration.
   * @return The user id of the registered user.
   * @throws RegisterException If the user is already logged in or something went wrong with the registration.
   */
  public long register(@NotNull CredentialDTO credentialDTO) throws RegisterException {
    if (id != 0) throw new RegisterException("You cannot register if you're logged in!");

    id = userDAO.register(credentialDTO);

    return id;
  }

  /**
   * @return The ID of the current user
   */
  public long getId() {
    return id;
  }

  /**
   * @return The username of the of the user.
   * @throws UserDoesNotExistException If the user does not exist.
   */
  public String getUsername() throws UserDoesNotExistException {
    try {
      if (username == null) username = userDAO.getUsername(id);
    } catch (SQLException e) {
      System.err.println("Failed to execute the getUsername query!");
      e.printStackTrace();
    }

    return username;
  }
}
