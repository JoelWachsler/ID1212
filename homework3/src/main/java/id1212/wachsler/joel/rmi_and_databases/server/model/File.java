package id1212.wachsler.joel.rmi_and_databases.server.model;

import id1212.wachsler.joel.rmi_and_databases.common.net.FileTransferHandler;
import id1212.wachsler.joel.rmi_and_databases.server.integration.FileDAO;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CompletableFuture;

class File {

  private long userId;
  private String filename;
  private boolean publicAccess;
  private boolean readable;
  private boolean writable;

  void setUserId(long userId) {
    this.userId = userId;
  }

  void setFilename(String filename) {
    this.filename = filename;
  }

  void setPublicAccess(boolean publicAccess) {
    this.publicAccess = publicAccess;
  }

  void setReadable(boolean readable) {
    this.readable = readable;
  }

  void setWritable(boolean writable) {
    this.writable = writable;
  }

  void upload(SocketChannel socketChannel) throws IllegalAccessException {
    try {
      FileDAO fileDAO = getFileByName(filename);

      if (fileDAO.getOwner().getId() != userId && !fileDAO.isWritable())
        throw new IllegalAccessException("The file is not owned by you and is not writable!");
    } catch (Exception e) {
      e.printStackTrace();
    }

//    uploadFile(socketChannel, filename);
  }

  private void uploadFile(SocketChannel socketChannel, String filename) {
    CompletableFuture.runAsync(() -> {
      try {
        FileTransferHandler.receiveFile(socketChannel, filename);
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  private FileDAO getFileByName(String filename) {
    Session session = FileDAO.getSession();

    Query query = session.createQuery("Select file from File file where file.name=:filename");
    query.setParameter("filename", filename);

    return (FileDAO) query.getSingleResult();
  }
}
