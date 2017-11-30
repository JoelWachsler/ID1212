package id1212.wachsler.joel.rmi_and_databases.server.model;

import id1212.wachsler.joel.rmi_and_databases.common.Listener;
import id1212.wachsler.joel.rmi_and_databases.common.dto.CredentialDTO;
import id1212.wachsler.joel.rmi_and_databases.common.exceptions.RegisterException;
import id1212.wachsler.joel.rmi_and_databases.server.integration.UserDAO;

import javax.security.auth.login.LoginException;
import java.nio.channels.SocketChannel;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Handles a connected client.
 */
public class ClientManager {
  private final UserDAO userDAO;
  private CredentialDTO credentialDTO;
  private Listener console;
  private SocketChannel socketChannel;
  private List<Listener> listeners = new ArrayList<>();

  public ClientManager(CredentialDTO credentialDTO) {
    this.credentialDTO = credentialDTO;
    userDAO = new UserDAO();
  }

  public long login() throws LoginException, RemoteException {
    return userDAO.login(credentialDTO);
  }

  public long register() throws RemoteException, RegisterException {
    return userDAO.register(credentialDTO);
  }

  /**
   * Attaches the provided <code>SocketChannel</code> to this user.
   *
   * @param socketChannel The client to attach to the user.
   * @throws RemoteException When something goes wrong with the connection.
   */
  public void attachSocketHandler(SocketChannel socketChannel) throws RemoteException {
    this.socketChannel = socketChannel;
    alertListeners("A socket has been attached to you client!");
  }

  /**
   * Alerts all observers of an event.
   *
   * @param message The message to pass to the listeners.
   * @throws RemoteException When something with the communication goes wrong.
   */
  void alertListeners(String message) throws RemoteException {
    CompletableFuture.runAsync(() -> {
      listeners.forEach(listener -> {
        try {
          listener.print(message);
        } catch (RemoteException e) {
          e.printStackTrace();
        }
      });
    });
  }

  /**
   * Adds an observer to be alerted on events.
   *
   * @param console Where to print.
   */
  public void addListener(Listener console) {
    listeners.add(console);
  }
}
