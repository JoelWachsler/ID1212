package id1212.wachsler.joel.rmi_and_databases.server.catalog;

import id1212.wachsler.joel.rmi_and_databases.common.dto.FileInfoDTO;
import id1212.wachsler.joel.rmi_and_databases.server.integration.FileDAO;
import id1212.wachsler.joel.rmi_and_databases.server.integration.UserDoesNotExistException;
import id1212.wachsler.joel.rmi_and_databases.server.userHandler.User;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Catalog {
  private FileDAO fileDAO;
  private User user;

  public Catalog(User user) {
    this.user = user;
    fileDAO = FileDAO.getInstance();
  }

  public List<FileInfoDTO> list() {
    try {
      return fileDAO.getFiles(user);
    } catch (SQLException e) {
      System.err.println("Failed to execute the list command!");
      e.printStackTrace();

      // Silent fail...
      return new ArrayList<>();
    }
  }

  /**
   * Checks if the user is allowed to upload the specified file.
   *
   * @param filename The filename to check if the user is allowed to upload.
   */
  public void allowedToUpload(String filename) throws IllegalAccessException {
    try {
      FileInfoDTO fileInfo = fileDAO.getFile(filename);

      if (fileInfo.getOwner().equals(user.getUsername())) return;
      if (!fileInfo.isPublic()) throw new IllegalAccessException("This file is not public!");
      if (!fileInfo.isWritable()) throw new IllegalAccessException("This file is not writable!");
    } catch (FileNotFoundException ignored) {
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (UserDoesNotExistException e) {
      throw new IllegalAccessException("The provided user does not exist!");
    }
  }
}
