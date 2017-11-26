package id1212.wachsler.joel.rmi_and_databases.server.controller;

import id1212.wachsler.joel.rmi_and_databases.common.*;
import id1212.wachsler.joel.rmi_and_databases.common.dto.CredentialDTO;
import id1212.wachsler.joel.rmi_and_databases.common.dto.FileInfoDTO;
import id1212.wachsler.joel.rmi_and_databases.common.exceptions.RegisterException;
import id1212.wachsler.joel.rmi_and_databases.server.catalog.Catalog;
import id1212.wachsler.joel.rmi_and_databases.server.integration.UserDAO;
import id1212.wachsler.joel.rmi_and_databases.server.user.User;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
  public void initUpload(String filename) throws RemoteException {
    CompletableFuture.runAsync(() -> {
      try {
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(Constants.SERVER_SOCKET_PORT));
        System.out.println("Listening...");
        SocketChannel client = serverSocket.accept();

        System.out.println("Connection established: " + client.getRemoteAddress());

        FileTransferHandler receiver = new FileTransferHandler(client);
        receiver.receiveFile(String.format("server_files/%s", filename));

        client.close();
        serverSocket.close();
        System.out.println("Connection closed!");
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }
}
