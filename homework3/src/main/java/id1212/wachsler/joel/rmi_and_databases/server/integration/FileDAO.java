package id1212.wachsler.joel.rmi_and_databases.server.integration;

import id1212.wachsler.joel.rmi_and_databases.common.dto.FileDTO;
import id1212.wachsler.joel.rmi_and_databases.server.model.ClientManager;
import id1212.wachsler.joel.rmi_and_databases.server.model.File;
import id1212.wachsler.joel.rmi_and_databases.server.model.User;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class FileDAO {
  /**
   * @param filename The file to lookup.
   * @return <code>File</code> containing various file information.
   */
  public File getFileByName(String filename) {
    Session session = File.getSession();

    Query query = session.createQuery("Select file from File file where file.name=:filename");
    query.setParameter("filename", filename);

    return (File) query.getSingleResult();
  }

  /**
   * Inserts a new file record in the database.
   * @param client The user who owns the object.
   * @param fileDTO Information about the file to insert.
   */
  public void insert(ClientManager client, FileDTO fileDTO) {
    Session session = File.getSession();

    try {
      session.beginTransaction();

      File file = new File();
      file.setOwner(client.getUser());
      file.setName(fileDTO.getFilename());
      file.setPublicAccess(fileDTO.isPublicAccess());
      file.setReadable(fileDTO.isReadable());
      file.setWritable(fileDTO.isWritable());

      session.save(file);
      session.getTransaction().commit();
    } catch (Exception e) {
      session.getTransaction().rollback();
      throw e;
    } finally {
      session.close();
    }
  }

  /**
   * Checks if the <code>User</code> is the owner of a file with the provided filename.
   *
   * @param user The user to check if they're the owner.
   * @param filename The filename to check whether the user is the owner for.
   * @return <code>true</code> if the user is the owner of the file.
   *         <code>false</code> if the user is not the owner of the file.
   */
  public boolean isFileOwner(User user, String filename) {
    File file = getFileByName(filename);

    return file.getOwner().getId() == user.getId();
  }
}
