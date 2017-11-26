package id1212.wachsler.joel.rmi_and_databases.server.user;

import com.sun.istack.internal.NotNull;
import id1212.wachsler.joel.rmi_and_databases.common.dto.CredentialDTO;
import id1212.wachsler.joel.rmi_and_databases.common.exceptions.RegisterException;
import id1212.wachsler.joel.rmi_and_databases.server.integration.UserDAO;

import javax.security.auth.login.LoginException;

public class User {
  private UserDAO userDAO;
  private long id;

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

  public long login(@NotNull CredentialDTO credentialDTO) throws LoginException {
    if (id != 0) throw new LoginException("You're already logged in!");

    id = userDAO.login(credentialDTO);

    return id;
  }

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
}
