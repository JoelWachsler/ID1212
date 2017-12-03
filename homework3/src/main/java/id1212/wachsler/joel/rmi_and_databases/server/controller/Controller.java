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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Controller for the file server which is directly called by other nodes.
 */
public class Controller extends UnicastRemoteObject implements FileServer {
  private final Map<Long, ClientManager> clients = new ConcurrentHashMap<>();

  public Controller() throws RemoteException {
  }

  private ClientManager auth(long userId) throws IllegalAccessException {
    if (!clients.containsKey(userId))
      throw new IllegalAccessException("You must be logged in to do that!");

    return clients.get(userId);
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
    ClientManager client = auth(userId);

    for (File file : client.getFileDAO().getFiles(client.getUser())) {
      StringJoiner msg = new StringJoiner(", ");
      msg.add("Name: " + file.getName());
      msg.add("Size: " + file.getSize() + " Bytes");
      msg.add("Owner: " + file.getOwner().getUsername());
      msg.add("Public: " + file.isPublicAccess());
      msg.add("Read: " + file.isReadable());
      msg.add("Writable: " + file.isWritable());
      client.alertListeners(msg.toString());
    }
  }

  /**
   * Uploads the provided file.
   *
   * @param userId Id of the user who wants to upload the file.
   * @param fileDTO Container for file information.
   * @throws RemoteException If something goes wrong with the connection.
   * @throws IllegalAccessException If the user is not allowed to upload the file.
   */
  @Override
  public void upload(long userId, FileDTO fileDTO) throws RemoteException, IllegalAccessException {
    ClientManager client = auth(userId);
    FileDAO fileDAO = client.getFileDAO();

    try {
      File file = fileDAO.getFileByName(fileDTO.getFilename());

      if (file.getOwner().getId() == client.getUser().getId()) {
        fileDAO.update(fileDTO);
        uploadFile(client, fileDTO);
      } else if (!file.isPublicAccess()) {
        throw new IllegalAccessException("You're not the owner and the file is not public!");
      } else if (!file.isWritable()) {
        throw new IllegalAccessException("You're not the owner of the file and the file is not writable!");
      } else {
        fileDAO.updateFileSize(fileDTO);
        uploadFile(client, fileDTO);

        String alertMsg = String.format("The user \"%s\" has updated your public writable file: \"%s\"",
          client.getUser().getUsername(),
          fileDTO.getFilename());

        alertOwnerFileChange(file, alertMsg);
      }
    } catch (NoResultException e) {
      // File doesn't exist and we're allowed to do whatever
      fileDAO.insert(client, fileDTO);
      uploadFile(client, fileDTO);
    }
  }

  private void alertOwnerFileChange(File file, String msg) throws RemoteException {
    long fileOwnerId = file.getOwner().getId();

    // Check if the user is logged in before alerting them!
    if (!clients.containsKey(fileOwnerId)) return;

    clients
      .get(file.getOwner().getId())
      .alertFileUpdate(file.getId(), msg);
  }

  @Override
  public FileDTO getFileInfo(long userId, String filename) throws RemoteException, IllegalAccessException {
    ClientManager user = auth(userId);
    FileDAO fileDAO = new FileDAO();

    File file = fileDAO.getFileByName(filename);

    if (file.getOwner().getId() == user.getUser().getId()) {
      return new FileDTO(file);
    } else if (!file.isPublicAccess()) {
      throw new IllegalAccessException("You're not the owner and the file is not public!");
    } else if (!file.isReadable()) {
      throw new IllegalAccessException("You're not the owner of the file and the file is not readable!");
    } else {
      return new FileDTO(file);
    }
  }

  @Override
  public void download(long userId, String filename) throws IOException, IllegalAccessException {
    ClientManager client = auth(userId);

    File file = client.getFileDAO().getFileByName(filename);
    Path serverFilePath = Paths.get("server_files/" + filename);

    if (file.getOwner().getId() == client.getUser().getId()) {
      FileTransferHandler.sendFile(client.getSocketChannel(), serverFilePath);
    } else if (!file.isPublicAccess()) {
      throw new IllegalAccessException("You're not the owner and the file is not public!");
    } else if (!file.isReadable()) {
      throw new IllegalAccessException("You're not the owner of the file and the file is not readable!");
    } else {
      FileTransferHandler.sendFile(client.getSocketChannel(), serverFilePath);

      String alertMsg = String.format("The user \"%s\" has downloaded your public readable file: \"%s\"",
        client.getUser().getUsername(),
        filename);

      alertOwnerFileChange(file, alertMsg);
    }
  }

  @Override
  public void logout(long userId) throws IOException, IllegalAccessException {
    ClientManager client = auth(userId);
    client.logout();

    clients.remove(userId);
  }

  /**
   * Removes a user.
   *
   * @param userId The user to unregister.
   */
  @Override
  public void unregister(long userId) throws IOException, IllegalAccessException {
    ClientManager client = auth(userId);
    client.remove();
    logout(userId);
  }

  /**
   * Request to be notified when the file gets updated.
   *
   * @param userId               The file owner who wants to be notified.
   * @param fileToNotifyOnUpdate The file to be notified on.
   * @throws RemoteException When something goes wrong with the connection.
   */
  @Override
  public void notifyFileUpdate(long userId, String fileToNotifyOnUpdate) throws RemoteException, IllegalAccessException {
    ClientManager client = auth(userId);
    File file = client.getFileDAO().getFileByName(fileToNotifyOnUpdate);

    if (file.getOwner().getId() != client.getUser().getId())
      throw new IllegalAccessException("You are not the owner of that file!");

    client.addFileToBeUpdatedOn(file.getId());
  }

  /**
   * Deletes a file on the server.
   *
   * @param userId   The user who wants to delete the file.
   * @param filename The file to delete.
   */
  @Override
  public void delete(long userId, String filename) throws RemoteException, IllegalAccessException {
    ClientManager client = auth(userId);

    File file = client.getFileDAO().getFileByName(filename);

    if (file.getOwner().getId() != client.getUser().getId())
      throw new IllegalAccessException("Only the owner can delete that file!");

    client.getFileDAO().deleteFile(file);
    client.alertListeners(String.format("The file \"%s\" has been deleted.", filename));
  }

  private void uploadFile(ClientManager client, FileDTO file) {
    CompletableFuture.runAsync(() -> {
      try {
        FileTransferHandler
          .receiveFile(client.getSocketChannel(), Paths.get("server_files/" + file.getFilename()), file.getSize());

        client.alertListeners(String.format("The file \"%s\" has been uploaded!", file.getFilename()));
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
