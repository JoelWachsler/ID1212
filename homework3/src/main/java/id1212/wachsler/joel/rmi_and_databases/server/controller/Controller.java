package id1212.wachsler.joel.rmi_and_databases.server.controller;

import id1212.wachsler.joel.rmi_and_databases.common.*;
import id1212.wachsler.joel.rmi_and_databases.common.dto.CredentialDTO;
import id1212.wachsler.joel.rmi_and_databases.common.dto.FileInfoDTO;
import id1212.wachsler.joel.rmi_and_databases.common.exceptions.RegisterException;
import id1212.wachsler.joel.rmi_and_databases.server.catalog.Catalog;
import id1212.wachsler.joel.rmi_and_databases.server.integration.UserDAO;
import id1212.wachsler.joel.rmi_and_databases.server.user.User;

import javax.security.auth.login.LoginException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller for the file server which is directly called by other nodes.
 */
public class Controller extends UnicastRemoteObject implements FileServer {
  private User user;

  public Controller(String datasource, CredentialDTO dbLogin) throws RemoteException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
    System.out.println("Trying to create a new controller!");

    System.out.println("Initializing database!");
    UserDAO.init(datasource, dbLogin);
    user = new User();
  }

  @Override
  public long login(CredentialDTO credentialDTO) throws RemoteException, LoginException {
    System.out.println("Trying to login!");

    return user.login(credentialDTO);
  }

  @Override
  public long register(CredentialDTO credentialDTO) throws RemoteException, RegisterException {
    System.out.println("Trying to register a user");
    
    return user.register(credentialDTO);
  }

  @Override
  public List<FileInfoDTO> list(long userId) throws RemoteException, IllegalAccessException {
    Catalog catalog = new Catalog(new User(userId));

    return catalog.list();
  }

  public void fileWriteAllowed(long userId, String filename) throws IllegalAccessException {
    Catalog catalog = new Catalog(new User(userId));
    catalog.allowedToUpload(filename);
  }
}
