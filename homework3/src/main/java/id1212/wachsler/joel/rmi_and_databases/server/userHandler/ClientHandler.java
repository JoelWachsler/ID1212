package id1212.wachsler.joel.rmi_and_databases.server.userHandler;

import id1212.wachsler.joel.rmi_and_databases.common.Listener;
import id1212.wachsler.joel.rmi_and_databases.common.dto.CredentialDTO;
import id1212.wachsler.joel.rmi_and_databases.common.dto.FileInfoDTO;
import id1212.wachsler.joel.rmi_and_databases.common.exceptions.RegisterException;
import id1212.wachsler.joel.rmi_and_databases.server.catalog.Catalog;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Keeps track of all active users. Is responsible for sending messages to users who has public files
 * which have been updated by other users.
 */
public class ClientHandler {
  private ConcurrentMap<Long, User> clients = new ConcurrentHashMap<>();

  /**
   * @see User#login()
   */
  public long login(Listener console, CredentialDTO credentials) throws LoginException {
    User user = new User(console, credentials);
    clients.put(user.login(), user);

    return user.getId();
  }

  public List<FileInfoDTO> list(long userId) throws IllegalAccessException {
    User user = authCheck(userId);

    return new Catalog(user).list();
  }

  private User authCheck(long userId) throws IllegalAccessException {
    if (!clients.containsKey(userId))
      throw new IllegalAccessException("The provided user is not logged in or does not exist!");

    return clients.get(userId);
  }

  /**
   * @see User#register()
   */
  public long register(Listener console, CredentialDTO credentials) throws RegisterException {
    User user = new User(console, credentials);
    clients.put(user.register(), user);

    return user.getId();
  }

  public void allowedToUpload(long userId, String filename) throws IllegalAccessException {
    User user = authCheck(userId);

    new Catalog(user).allowedToUpload(filename);
  }
}
