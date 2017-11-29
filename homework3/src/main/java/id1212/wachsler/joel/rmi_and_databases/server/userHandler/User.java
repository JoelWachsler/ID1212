package id1212.wachsler.joel.rmi_and_databases.server.userHandler;

import id1212.wachsler.joel.rmi_and_databases.common.Listener;
import id1212.wachsler.joel.rmi_and_databases.common.dto.CredentialDTO;
import id1212.wachsler.joel.rmi_and_databases.common.exceptions.RegisterException;
import id1212.wachsler.joel.rmi_and_databases.server.integration.UserDAO;
import id1212.wachsler.joel.rmi_and_databases.server.integration.UserDoesNotExistException;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;

public class User {
  private final CredentialDTO credentials;
  private final Listener console;
  private UserDAO userDAO;
  private long id;
  private String username;

  private void init() {
    userDAO = UserDAO.getInstance();
  }

  User(Listener console, CredentialDTO credentials) {
    init();

    this.credentials = credentials;
    this.console = console;
  }

  /**
   * Authenticates a user.
   *
   * @return The user id of the user.
   * @throws LoginException If the user is already logged in or something went wrong when logging in.
   */
  long login() throws LoginException {
    if (id != 0) throw new LoginException("You're already logged in!");

    id = userDAO.login(credentials);

    return id;
  }

  /**
   * Registers a user.
   *
   * @return The user id of the registered user.
   * @throws RegisterException If the user is already logged in or something went wrong with the registration.
   */
  long register() throws RegisterException {
    if (id != 0) throw new RegisterException("You cannot register if you're logged in!");

    id = userDAO.register(credentials);

    return id;
  }

  /**
   * @return Id of the current user.
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
