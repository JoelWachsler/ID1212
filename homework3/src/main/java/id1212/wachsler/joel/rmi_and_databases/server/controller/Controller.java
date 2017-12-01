package id1212.wachsler.joel.rmi_and_databases.server.controller;

import id1212.wachsler.joel.rmi_and_databases.common.Listener;
import id1212.wachsler.joel.rmi_and_databases.common.*;
import id1212.wachsler.joel.rmi_and_databases.common.dto.CredentialDTO;
import id1212.wachsler.joel.rmi_and_databases.common.dto.FileDTO;
import id1212.wachsler.joel.rmi_and_databases.common.exceptions.RegisterException;
import id1212.wachsler.joel.rmi_and_databases.common.net.FileTransferHandler;
import id1212.wachsler.joel.rmi_and_databases.server.integration.FileDAO;
import id1212.wachsler.joel.rmi_and_databases.server.model.ClientManager;
import id1212.wachsler.joel.rmi_and_databases.server.model.File;

import javax.persistence.NoResultException;
import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Controller for the file server which is directly called by other nodes.
 */
public class Controller extends UnicastRemoteObject implements FileServer {
  private final Map<Long, ClientManager> clients = new ConcurrentHashMap<>();

  public Controller() throws RemoteException {
  }

  /**
   * @see FileServer#login(Listener, CredentialDTO)
   */
  @Override
  public long login(Listener console, CredentialDTO credentialDTO) throws RemoteException, LoginException {
    ClientManager client = new ClientManager();
    client.addListener(console);

    long id = client.login(credentialDTO);
    clients.put(id, client);

    return id;
  }

  /**
   * @see FileServer#register(Listener, CredentialDTO)
   */
  @Override
  public void register(Listener console, CredentialDTO credentials) throws RemoteException, RegisterException, LoginException {
    ClientManager client = new ClientManager();
    client.register(credentials);
  }

  /**
   * @see FileServer#list(long)
   */
  @Override
  public void list(long userId) throws RemoteException, IllegalAccessException {
  }

  /**
   * @see FileServer#upload(long, FileDTO)
   */
  @Override
  public void upload(long userId, FileDTO fileDTO) throws RemoteException, IllegalAccessException {
    ClientManager user = clients.get(userId);
    FileDAO fileDAO = new FileDAO();
    File file;

    try {
      file = fileDAO.getFileByName(fileDTO.getFilename());
      fileDAO.isFileOwner(user.getUser(), fileDTO.getFilename());
    } catch (NoResultException e) {
      // File doesn't exist and we're allowed to do whatever
      fileDAO.insert(user, fileDTO);
      uploadFile(user.getSocketChannel(), fileDTO);
      return;
    }
  }

  @Override
  public void logout(long userId) throws RemoteException {
    clients.remove(userId);
  }

  private void uploadFile(SocketChannel socketChannel, FileDTO file) {
    CompletableFuture.runAsync(() -> {
      try {
        FileTransferHandler
          .receiveFile(socketChannel, Paths.get("server_files/" + file.getFilename()), file.getSize());
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  /**
   * @see ClientManager#attachSocketHandler(SocketChannel)
   */
  public void attachSocketToUser(long userId, SocketChannel socketChannel) throws RemoteException {
    clients.get(userId).attachSocketHandler(socketChannel);
  }
}
