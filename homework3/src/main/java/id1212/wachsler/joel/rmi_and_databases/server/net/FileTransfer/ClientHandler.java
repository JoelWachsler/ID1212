package id1212.wachsler.joel.rmi_and_databases.server.net.FileTransfer;

import id1212.wachsler.joel.rmi_and_databases.common.Header;
import id1212.wachsler.joel.rmi_and_databases.common.dto.FileHandlingRequestDTO;
import id1212.wachsler.joel.rmi_and_databases.common.dto.FileHandlingResponseDTO;
import id1212.wachsler.joel.rmi_and_databases.common.net.FileTransferHandler;
import id1212.wachsler.joel.rmi_and_databases.server.controller.Controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.channels.SocketChannel;

/**
 * Handles a socket connection with a client.
 */
class ClientHandler {
  private ObjectOutputStream outputStream;
  private ObjectInputStream inputStream;
  private Controller controller;
  private SocketChannel client;

  ClientHandler(Controller controller, SocketChannel client) {
    this.controller = controller;
    this.client = client;

    try {
      inputStream = new ObjectInputStream(client.socket().getInputStream());
      outputStream = new ObjectOutputStream(client.socket().getOutputStream());

      listenForMessages();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    } finally {
      try {
        System.out.println("Closing client connection.");
        client.close();
      } catch (IOException e) {
        System.err.println("Failed to close the client connection!");
        e.printStackTrace();
      }
    }
  }

  private void listenForMessages() throws IOException, ClassNotFoundException {
    System.out.println("Waiting for the client to write the info");
    FileHandlingRequestDTO msg = (FileHandlingRequestDTO) inputStream.readObject();

    String filename = msg.getFileUploadName();
    long userId = msg.getUserId();

    try {
      controller.fileWriteAllowed(userId, filename);
    } catch (IllegalAccessException e) {
      sendMsg(new FileHandlingResponseDTO(Header.FILE_UNAUTHORIZED_READ));
      return;
    }

    sendMsg(new FileHandlingResponseDTO(Header.FILE_OK));

    System.out.println("Starting to receive the file...");
    FileTransferHandler.receiveFile(client, String.format("server_files/%s", filename));
  }

  private <T extends Serializable> void sendMsg(T msg) throws IOException {
    outputStream.writeObject(msg);
    outputStream.flush();
    outputStream.reset();
  }
}
