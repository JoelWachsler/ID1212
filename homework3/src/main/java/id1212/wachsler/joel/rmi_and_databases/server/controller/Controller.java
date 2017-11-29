package id1212.wachsler.joel.rmi_and_databases.server.controller;

import id1212.wachsler.joel.rmi_and_databases.common.Listener;
import id1212.wachsler.joel.rmi_and_databases.common.*;
import id1212.wachsler.joel.rmi_and_databases.common.dto.CredentialDTO;
import id1212.wachsler.joel.rmi_and_databases.server.model.User;

import javax.security.auth.login.LoginException;
import java.nio.channels.SocketChannel;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Controller for the file server which is directly called by other nodes.
 */
public class Controller extends UnicastRemoteObject implements FileServer {
  private final Map<Long, User> users = new ConcurrentHashMap<>();

  public Controller() throws RemoteException {
  }

  /**
   * @see FileServer#login(Listener, CredentialDTO)
   */
  @Override
  public long login(Listener console, CredentialDTO credentialDTO) throws RemoteException, LoginException {
    User user = new User(credentialDTO);
    user.addListener(console);

    long id = user.login();
    users.put(id, user);

    return id;
  }

  /**
   * @see FileServer#register(Listener, CredentialDTO)
   */
  @Override
  public void register(Listener console, CredentialDTO credentials) throws RemoteException {
    User user = new User(credentials);
    user.addListener(console);
    user.register();
  }

  /**
   * @see FileServer#list(long)
   */
  @Override
  public void list(long userId) throws RemoteException, IllegalAccessException {
  }

  /**
   * @see FileServer#upload(long, String, boolean, boolean, boolean)
   */
  @Override
  public void upload(long userId, String filename, boolean publicAccess, boolean readable, boolean writable) throws RemoteException, IllegalAccessException {
    users.get(userId).upload(filename, publicAccess, readable, writable);
  }

  /**
   * @see User#attachSocketHandler(SocketChannel)
   */
  public void attachSocketToUser(long userId, SocketChannel socketChannel) throws RemoteException {
    users.get(userId).attachSocketHandler(socketChannel);
  }
}
