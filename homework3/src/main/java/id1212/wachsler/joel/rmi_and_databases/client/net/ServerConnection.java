package id1212.wachsler.joel.rmi_and_databases.client.net;

import id1212.wachsler.joel.rmi_and_databases.common.Constants;
import id1212.wachsler.joel.rmi_and_databases.common.Header;
import id1212.wachsler.joel.rmi_and_databases.common.dto.FileHandlingRequestDTO;
import id1212.wachsler.joel.rmi_and_databases.common.dto.FileHandlingResponseDTO;
import id1212.wachsler.joel.rmi_and_databases.common.net.FileTransferHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class ServerConnection {
  private SocketChannel channel;
  private ObjectOutputStream outputStream;
  private ObjectInputStream inputStream;

  public ServerConnection() throws IOException {
    channel = createSocketChannel();
    outputStream = new ObjectOutputStream(channel.socket().getOutputStream());
    inputStream = new ObjectInputStream(channel.socket().getInputStream());
  }

  private SocketChannel createSocketChannel() throws IOException {
    SocketChannel socketChannel = SocketChannel.open();
    socketChannel.connect(new InetSocketAddress(Constants.SERVER_ADDRESS, Constants.SERVER_SOCKET_PORT));

    return socketChannel;
  }

  /**
   * Sends a file upload request to the server.
   *
   * @param filename The local filename of the file.
   * @param fileUploadName The filename on the server.
   * @param userId The user who wants to upload the file.
   */
  public void upload(String filename, String fileUploadName, long userId) throws Exception {
    sendMsg(new FileHandlingRequestDTO(Header.FILE_UPLOAD, fileUploadName, userId));

    FileHandlingResponseDTO response = (FileHandlingResponseDTO) inputStream.readObject();

    switch (response.getHeader()) {
      case FILE_OK: break;
      case FILENAME_DUPLICATE:      throw new Exception("Filename already exists on server...");
      case FILE_UNAUTHORIZED_READ:  throw new Exception("You are not allowed to read this file...");
      case FILE_UNAUTHORIZED_WRITE: throw new Exception("You are not allowed to write to this file...");
      default: throw new Exception("The server sent an invalid response...");
    }

    FileTransferHandler.sendFile(channel, String.format("client_files/%s", filename));
  }

  private <T extends Serializable> void sendMsg(T msg) throws IOException {
    outputStream.writeObject(msg);
    outputStream.flush();
    outputStream.reset();
  }

  public void close() throws IOException {
    channel.close();
  }
}
