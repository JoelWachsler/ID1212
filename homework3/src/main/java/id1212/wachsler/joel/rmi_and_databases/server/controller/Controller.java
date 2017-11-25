package id1212.wachsler.joel.rmi_and_databases.server.controller;

import id1212.wachsler.joel.rmi_and_databases.common.CredentialDTO;
import id1212.wachsler.joel.rmi_and_databases.common.FileServer;
import id1212.wachsler.joel.rmi_and_databases.common.RegisterException;
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

  public Controller(String datasource, CredentialDTO dbLogin) throws RemoteException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
    System.out.println("Trying to create a new controller!");
    userDao = new UserDAO(datasource, dbLogin);
  }

  @Override
  public long login(CredentialDTO credentialDTO) throws RemoteException, LoginException {
    System.out.println("Trying to login!");

    User user = new User(userDao);
    return user.login(credentialDTO);
  }

  @Override
  public long register(CredentialDTO credentialDTO) throws RemoteException, RegisterException {
    System.out.println("Trying to register a user");
    
    User user = new User(userDao);
    return user.register(credentialDTO);
  }
}
