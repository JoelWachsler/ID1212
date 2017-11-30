package id1212.wachsler.joel.rmi_and_databases.server.integration;

import id1212.wachsler.joel.rmi_and_databases.common.dto.FileDTO;
import id1212.wachsler.joel.rmi_and_databases.common.net.FileTransferHandler;
import id1212.wachsler.joel.rmi_and_databases.server.model.File;
import id1212.wachsler.joel.rmi_and_databases.server.model.User;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CompletableFuture;

public class FileDAO {

  private final User user;
  private final String filename;
  private final long size;
  private final boolean publicAccess;
  private final boolean readable;
  private final boolean writable;

  FileDAO(User user, String filename, long size, boolean publicAccess, boolean readable, boolean writable) {
    this.user = user;
    this.filename = filename;
    this.size = size;
    this.publicAccess = publicAccess;
    this.readable = readable;
    this.writable = writable;
  }

  void upload(SocketChannel socketChannel) throws IllegalAccessException {
    try {
      File fileDAO = getFileByName(filename);

      if (fileDAO.getOwner().getId() != user.getId() && !fileDAO.isWritable())
        throw new IllegalAccessException("The file is not owned by you and is not writable!");

      if (fileDAO.getOwner().getId() == user.getId()) {
        updateFileRecord(fileDAO);
        uploadFile(socketChannel, size, filename);
      }
    } catch (NoResultException e) {
      // The record doesn't exist -> we're free to upload
      insertNewFileRecord();
      uploadFile(socketChannel, size, filename);
    }
  }

  private void updateFileRecord(File fileDAO) {
    Session session = File.getSession();
    try {
      session.beginTransaction();
      fileDAO.setPublicAccess(publicAccess);
      fileDAO.setReadable(readable);
      fileDAO.setWritable(writable);

      session.save(fileDAO);
      session.getTransaction().commit();
    } catch (Exception e) {
      session.getTransaction().rollback();
    } finally {
      session.close();
    }
  }

  private void insertNewFileRecord() {
  }

  private void uploadFile(SocketChannel socketChannel, long fileSize, String filename) {
    CompletableFuture.runAsync(() -> {
      try {
        FileTransferHandler.receiveFile(socketChannel, fileSize, String.format("server_files/%s", filename));
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  public File getFileByName(String filename) {
    Session session = File.getSession();

    Query query = session.createQuery("Select file from File file where file.name=:filename");
    query.setParameter("filename", filename);

    return (File) query.getSingleResult();
  }

  /**
   * Inserts a new file record in the database.
   *
   * @param user The user who owns the object.
   * @param fileDTO Information about the file to insert.
   */
  public void insert(User user, FileDTO fileDTO) {
    Session session = File.getSession();
    try {
      session.beginTransaction();
      File file = new File();
      file.setOwner(user);
      file.setName(filename);
      file.setPublicAccess(publicAccess);
      file.setReadable(readable);
      file.setWritable(writable);

      session.save(file);
      session.getTransaction().commit();
    } catch (Exception e) {
      session.getTransaction().rollback();
    } finally {
      session.close();
    }
  }
}
