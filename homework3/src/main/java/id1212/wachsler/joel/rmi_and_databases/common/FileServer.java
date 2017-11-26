package id1212.wachsler.joel.rmi_and_databases.common;

import javax.security.auth.login.LoginException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Registry for RMI.
 */
public interface FileServer extends Remote {
  String REGISTRY_NAME = "file_server";

  /**
   * Users should be able to login.
   *
   * @param credentialDTO The credentials used to login.
   * @return The id of the logged in user -> used for authentication.
   * @throws RemoteException When something goes wrong with the communication.
   * @throws LoginException When something goes wrong with the authentication.
   */
  long login(CredentialDTO credentialDTO) throws RemoteException, LoginException;

  /**
   * Users should be able to register.
   *
   * @param credentialDTO Credentials used to register.
   * @return The id of the newly created account.
   * @throws RemoteException When something goes wrong with the communication.
   * @throws RegisterException When something goes wrong with the registration.
   */
  long register(CredentialDTO credentialDTO) throws RemoteException, RegisterException;

  /**
   * Users should be able to view their files.
   *
   * @param userId The id of the user who wants to see their files or public files.
   * @return A list of files on the server.
   * @throws RemoteException When something goes wrong with the communication.
   * @throws IllegalAccessException When the userId is invalid.
   */
  List<FileInfoDTO> list(long userId) throws RemoteException, IllegalAccessException;

  void upload(String filename) throws RemoteException;
}
