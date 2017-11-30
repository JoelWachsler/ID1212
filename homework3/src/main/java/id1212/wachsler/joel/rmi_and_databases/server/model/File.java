package id1212.wachsler.joel.rmi_and_databases.server.model;

import id1212.wachsler.joel.rmi_and_databases.common.net.FileTransferHandler;
import id1212.wachsler.joel.rmi_and_databases.server.integration.FileDAO;
import id1212.wachsler.joel.rmi_and_databases.server.integration.UserDAO;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CompletableFuture;

class File {

  private String filename;
  private boolean publicAccess;
  private boolean readable;
  private boolean writable;
  private UserDAO user;

  void setUser(UserDAO user) {
    this.user = user;
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

      if (fileDAO.getOwner().getId() != user.getId() && !fileDAO.isWritable())
        throw new IllegalAccessException("The file is not owned by you and is not writable!");

      if (fileDAO.getOwner().getId() == user.getId()) {
        updateFileRecord(fileDAO);
        uploadFile(socketChannel, filename);
      }
    } catch (NoResultException e) {
      // The record doesn't exist -> we're free to upload
      insertNewFileRecord();
      uploadFile(socketChannel, filename);
    }
  }

  private void updateFileRecord(FileDAO fileDAO) {
    Session session = FileDAO.getSession();
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
    Session session = FileDAO.getSession();
    try {
      session.beginTransaction();
      FileDAO fileDAO = new FileDAO();
      fileDAO.setOwner(user);
      fileDAO.setName(filename);
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

  private void uploadFile(SocketChannel socketChannel, String filename) {
    CompletableFuture.runAsync(() -> {
      try {
        FileTransferHandler.receiveFile(socketChannel, String.format("server_files/%s", filename));
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
