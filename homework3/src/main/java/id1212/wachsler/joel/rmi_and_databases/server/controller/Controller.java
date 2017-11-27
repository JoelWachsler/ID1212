package id1212.wachsler.joel.rmi_and_databases.server.controller;

import id1212.wachsler.joel.rmi_and_databases.common.Listener;
import id1212.wachsler.joel.rmi_and_databases.common.*;
import id1212.wachsler.joel.rmi_and_databases.common.dto.CredentialDTO;
import id1212.wachsler.joel.rmi_and_databases.common.dto.FileInfoDTO;
import id1212.wachsler.joel.rmi_and_databases.common.exceptions.RegisterException;
import id1212.wachsler.joel.rmi_and_databases.server.userHandler.ClientHandler;

import javax.security.auth.login.LoginException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller for the file server which is directly called by other nodes.
 */
public class Controller extends UnicastRemoteObject implements FileServer {
  private final ClientHandler clientHandler = new ClientHandler();

  public Controller() throws RemoteException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
    System.out.println("Trying to create a new controller!");

    System.out.println("Initializing database!");
  }

  /**
   * @see ClientHandler#login(Listener, CredentialDTO)
   */
  @Override
  public long login(Listener console, CredentialDTO credentialDTO) throws RemoteException, LoginException {
    return clientHandler.login(console, credentialDTO);
  }

  /**
   * @see ClientHandler#register(Listener, CredentialDTO)
   */
  @Override
  public long register(Listener console, CredentialDTO credentialDTO) throws RemoteException, RegisterException {
    return clientHandler.register(console, credentialDTO);
  }

  /**
   * @see ClientHandler#list(long)
   */
  @Override
  public List<FileInfoDTO> list(long userId) throws RemoteException, IllegalAccessException {
    return clientHandler.list(userId);
  }

  public void fileWriteAllowed(long userId, String filename) throws IllegalAccessException {
    clientHandler.allowedToUpload(userId, filename);
  }
}
