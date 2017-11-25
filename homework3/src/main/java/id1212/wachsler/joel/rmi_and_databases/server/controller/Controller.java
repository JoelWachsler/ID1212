package id1212.wachsler.joel.rmi_and_databases.server.controller;

import id1212.wachsler.joel.rmi_and_databases.common.Credentials;
import id1212.wachsler.joel.rmi_and_databases.common.FileServer;
import id1212.wachsler.joel.rmi_and_databases.server.integration.UserDAO;
import id1212.wachsler.joel.rmi_and_databases.server.user.User;

import javax.security.auth.login.LoginException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

/**
 * Controller for the file server which is directly called by other nodes.
 */
public class Controller extends UnicastRemoteObject implements FileServer {
  private final UserDAO userDao;

  public Controller(String datasource, Credentials dbLogin) throws RemoteException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
    System.out.println("Trying to create a new controller!");
    userDao = new UserDAO(datasource, dbLogin);
  }

  @Override
  public long login(Credentials credentials) throws RemoteException, LoginException {
    System.out.println("Trying to login!");
    System.out.println("Username: " + credentials.getUsername());
    System.out.println("Password: " + credentials.getPassword());

    User user = new User(userDao);
    return user.login(credentials);
  }
}
