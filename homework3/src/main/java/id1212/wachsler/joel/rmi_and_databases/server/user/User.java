package id1212.wachsler.joel.rmi_and_databases.server.user;

import com.sun.istack.internal.NotNull;
import id1212.wachsler.joel.rmi_and_databases.common.Credentials;
import id1212.wachsler.joel.rmi_and_databases.server.integration.UserDAO;

import javax.security.auth.login.LoginException;

public class User {
  private UserDAO userDAO;

  public User(UserDAO userDAO) {
    this.userDAO = userDAO;
  }

  public long login(@NotNull Credentials credentials) throws LoginException {
    return userDAO.login(credentials);
  }
}
