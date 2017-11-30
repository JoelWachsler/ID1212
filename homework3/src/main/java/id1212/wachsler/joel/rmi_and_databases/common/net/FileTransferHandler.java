package id1212.wachsler.joel.rmi_and_databases.common.net;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

/**
 * File handling over a TCP socket using NIO.
 */
public class FileTransferHandler {
  private static final int initBufferSize = 1024;

  /**
   * Handles receiving of a file over a TCP socket.
   *
   * @param channel The channel to receive the file from.
   * @param filename The name of the file to be uploaded.
   * @throws IOException If something goes wrong with the file transfer.
   */
  public static void receiveFile(SocketChannel channel, long fileSize, String filename) throws IOException {
    Path path = Paths.get(filename);

    try (FileChannel fileChannel = FileChannel.open(path,
        EnumSet.of(StandardOpenOption.CREATE,
          StandardOpenOption.TRUNCATE_EXISTING,
          StandardOpenOption.WRITE))) {

      ByteBuffer buffer = ByteBuffer.allocate(initBufferSize);
      while (channel.read(buffer) > 0) {
        buffer.flip();
        fileChannel.write(buffer);
        System.out.println("Buffer size: " + buffer.position());
        buffer.clear();
      }

      System.out.println("File uploaded!");
      // channel.shutdownInput();
    }
  }

  /**
   * Handles sending of a file over a TCP socket.
   *
   * @param channel The channel to receive the file from.
   * @param filename The file to send.
   * @throws IOException If something goes wrong with the file transfer.
   */
  public static void sendFile(SocketChannel channel, String filename) throws IOException {
    Path path = Paths.get(filename);

    try (FileChannel fileChannel = FileChannel.open(path)) {
      ByteBuffer buffer = ByteBuffer.allocate(initBufferSize);
      while (fileChannel.read(buffer) > 0) {
        buffer.flip();
        channel.write(buffer);
        buffer.clear();
      }

      // channel.shutdownOutput();
    }
  }
}
