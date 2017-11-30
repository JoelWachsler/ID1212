package id1212.wachsler.joel.rmi_and_databases.server.integration;

import id1212.wachsler.joel.rmi_and_databases.common.Listener;
import id1212.wachsler.joel.rmi_and_databases.common.dto.CredentialDTO;
import id1212.wachsler.joel.rmi_and_databases.common.exceptions.RegisterException;
import id1212.wachsler.joel.rmi_and_databases.server.model.User;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import javax.security.auth.login.LoginException;
import java.nio.channels.SocketChannel;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UserDAO {
  private User userDao;

  public UserDAO() {
  }

  private boolean userWithUsernameExists() {
    try {
      Session session = User.getSession();
      Query query = session.createQuery("Select ua from User ua where ua.username=:username");
      query.setParameter("username", credentials.getUsername());

      query.getSingleResult();

      return true;
    } catch (NoResultException e) {
      return false;
    }
  }

  /**
   * Registers a user using the <code>CredentialDTO</code> from the constructor.
   *
   * @throws RemoteException When something with the communication goes wrong.
   * @param credentialDTO
   */
  public long register(CredentialDTO credentials) throws RemoteException, RegisterException {
    userDao = new User();

    if (userWithUsernameExists())
      throw new RegisterException("A user with that username already exists!");

    try {
      userDao.setUsername(credentials.getUsername());
      userDao.setPassword(credentials.getPassword());

      Session session = User.getSession();
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
  public long login(CredentialDTO credentials) throws LoginException, RemoteException {
    Session session = User.getSession();
    try {
      session.beginTransaction();
      Query query = session.createQuery("Select ua from User ua where ua.username=:username and ua.password=:password");

      query.setParameter("username", credentials.getUsername());
      query.setParameter("password", credentials.getPassword());

      userDao = (User) query.getSingleResult();
      alertListeners(String.format("You are now logged in and your id is: %d", userDao.getId()));

      return userDao.getId();
    } catch (NoResultException e) {
      throw new LoginException("Wrong username or password!");
    }
  }
}
