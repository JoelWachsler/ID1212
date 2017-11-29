package id1212.wachsler.joel.rmi_and_databases.server.model;

import id1212.wachsler.joel.rmi_and_databases.common.Listener;
import id1212.wachsler.joel.rmi_and_databases.common.dto.CredentialDTO;
import id1212.wachsler.joel.rmi_and_databases.server.integration.UserDAO;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import javax.security.auth.login.LoginException;
import java.nio.channels.SocketChannel;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class User {
  private UserDAO userDao;
  private CredentialDTO credentials;
  private List<Listener> listeners = new ArrayList<>();
  private SocketChannel socketChannel;

  public User(CredentialDTO credentials) {
    this.credentials = credentials;
  }

  /**
   * Registers a user using the <code>CredentialDTO</code> from the constructor.
   *
   * @throws RemoteException When something with the communication goes wrong.
   */
  public void register() throws RemoteException {
    userDao = new UserDAO();

    try {
      userDao.setUsername(credentials.getUsername());
      userDao.setPassword(credentials.getPassword());

      Session session = UserDAO.getSession();
      session.beginTransaction();
      session.save(userDao);

      session.getTransaction().commit();

      alertListeners("You are now registered!");
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  /**
   * Authenticates users.
   *
   * @return The id of the authenticated in user.
   * @throws LoginException When invalid credentials were provided.
   * @throws RemoteException When something with the communication goes wrong.
   */
  public long login() throws LoginException, RemoteException {
    Session session = UserDAO.getSession();
    try {
      session.beginTransaction();
      Query query = session.createQuery("Select ua from User ua where ua.username=:username and ua.password=:password");

      query.setParameter("username", credentials.getUsername());
      query.setParameter("password", credentials.getPassword());

      userDao = (UserDAO) query.getSingleResult();
      alertListeners(String.format("You are now logged in and your id is: %d", userDao.getId()));

      return userDao.getId();
    } catch (NoResultException e) {
      throw new LoginException("Wrong username or password!");
    }
  }

  /**
   * Alerts all observers of an event.
   *
   * @param msg The message to pass to the listeners.
   * @throws RemoteException When something with the communication goes wrong.
   */
  void alertListeners(String msg) throws RemoteException {
    CompletableFuture.runAsync(() -> {
      listeners.forEach(listener -> {
        try {
          listener.print(msg);
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
   * @see id1212.wachsler.joel.rmi_and_databases.common.FileServer#upload(long, String, boolean, boolean, boolean)
   */
  public void upload(String filename, boolean publicAccess, boolean readable, boolean writable) throws IllegalAccessException {
    File file = new File();

    file.setUserId(userDao.getId());
    file.setFilename(filename);
    file.setPublicAccess(publicAccess);
    file.setReadable(readable);
    file.setWritable(writable);

    file.upload(socketChannel);
  }
}
