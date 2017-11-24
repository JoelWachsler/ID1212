package id1212.wachsler.joel.rmi_and_databases.server.user;

import com.sun.istack.internal.NotNull;
import id1212.wachsler.joel.rmi_and_databases.common.Credentials;

public class User {
  private Credentials credentials;

  public User(@NotNull Credentials credentials) {
    this.credentials = credentials;
  }
}
