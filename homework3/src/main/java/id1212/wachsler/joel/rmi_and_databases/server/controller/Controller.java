package id1212.wachsler.joel.rmi_and_databases.server.controller;

import id1212.wachsler.joel.rmi_and_databases.common.CredentialDTO;
import id1212.wachsler.joel.rmi_and_databases.common.FileInfoDTO;
import id1212.wachsler.joel.rmi_and_databases.common.FileServer;
import id1212.wachsler.joel.rmi_and_databases.common.RegisterException;
import id1212.wachsler.joel.rmi_and_databases.server.catalog.Catalog;
import id1212.wachsler.joel.rmi_and_databases.server.integration.UserDAO;
import id1212.wachsler.joel.rmi_and_databases.server.user.User;

import javax.security.auth.login.LoginException;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller for the file server which is directly called by other nodes.
 */
public class Controller extends UnicastRemoteObject implements FileServer {
  public Controller(String datasource, CredentialDTO dbLogin) throws RemoteException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
    System.out.println("Trying to create a new controller!");

    System.out.println("Initializing database!");
    UserDAO.init(datasource, dbLogin);
  }

  @Override
  public long login(CredentialDTO credentialDTO) throws RemoteException, LoginException {
    System.out.println("Trying to login!");

    User user = new User();
    return user.login(credentialDTO);
  }

  @Override
  public long register(CredentialDTO credentialDTO) throws RemoteException, RegisterException {
    System.out.println("Trying to register a user");
    
    User user = new User();
    return user.register(credentialDTO);
  }

  @Override
  public List<FileInfoDTO> list(long userId) throws RemoteException, IllegalAccessException {
    Catalog catalog = new Catalog(new User(userId));

    return catalog.list();
  }

  @Override
  public void upload(String filename) {
    try {
      FileInputStream inf = new FileInputStream(filename);
      try (FileChannel channel = inf.getChannel()) {
        MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        WritableByteChannel out = Channels.newChannel(System.out);
        while (buffer.hasRemaining()) {
          out.write(buffer);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
