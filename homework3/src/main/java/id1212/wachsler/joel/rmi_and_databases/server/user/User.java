package id1212.wachsler.joel.rmi_and_databases.server.user;

import com.sun.istack.internal.NotNull;
import id1212.wachsler.joel.rmi_and_databases.common.CredentialDTO;
import id1212.wachsler.joel.rmi_and_databases.common.RegisterException;
import id1212.wachsler.joel.rmi_and_databases.server.integration.UserDAO;

import javax.security.auth.login.LoginException;

public class User {
  private UserDAO userDAO;

  public User(UserDAO userDAO) {
    this.userDAO = userDAO;
  }

  public long login(@NotNull CredentialDTO credentialDTO) throws LoginException {
    return userDAO.login(credentialDTO);
  }

  public long register(@NotNull CredentialDTO credentialDTO) throws RegisterException {
    return userDAO.register(credentialDTO);
  }
}
