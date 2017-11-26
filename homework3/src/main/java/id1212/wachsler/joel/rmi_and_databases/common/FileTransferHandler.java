package id1212.wachsler.joel.rmi_and_databases.common;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

public class FileTransferHandler {
  private final SocketChannel channel;

  public FileTransferHandler(SocketChannel channel) {
    this.channel = channel;
  }

  /**
   * Handles the receiving of a file over a TCP socket.
   *
   * @param filename The name of the file to be uploaded.
   * @throws IOException If something goes wrong with the file transfer.
   */
  public void receiveFile(String filename) throws IOException {
    Path path = Paths.get(filename);

    FileChannel fileChannel = FileChannel.open(path,
      EnumSet.of(StandardOpenOption.CREATE,
        StandardOpenOption.TRUNCATE_EXISTING,
        StandardOpenOption.WRITE));

    ByteBuffer buffer = ByteBuffer.allocate(1024);
    while (channel.read(buffer) > 0) {
      buffer.flip();
      fileChannel.write(buffer);
      buffer.clear();
    }

    fileChannel.close();
    System.out.println("File uploaded!");
  }

  /**
   * Handles sending of a file over a TCP socket.
   *
   * @param filename The file to send.
   * @throws IOException If something goes wrong with the file transfer.
   */
  public void sendFile(String filename) throws IOException {
    Path path = Paths.get(filename);
    FileChannel inChannel = FileChannel.open(path);

    ByteBuffer buffer = ByteBuffer.allocate(1024);
    while (inChannel.read(buffer) > 0) {
      buffer.flip();
      channel.write(buffer);
      buffer.clear();
    }

    channel.close();
    System.out.println("File upload complete!");
  }
}
