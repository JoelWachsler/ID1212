package id1212.wachsler.joel.rmi_and_databases.common;

import javax.security.auth.login.LoginException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FileServer extends Remote {
  String REGISTRY_NAME = "file_server";

  /**
   * A user can register at the catalog, unregister, login and logout.
   * To be allowed to upload/download files, a user must be registered and logged in.
   */
  long login(Credentials credentials) throws RemoteException, LoginException;
}
