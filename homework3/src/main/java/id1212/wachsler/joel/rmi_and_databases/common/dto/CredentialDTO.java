package id1212.wachsler.joel.rmi_and_databases.common.dto;

import java.io.Serializable;

/**
 * Class used to encapsulate the login credentials.
 */
public class CredentialDTO implements Serializable {
  private String username;
  private String password;

  public CredentialDTO(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }
}
