package id1212.wachsler.joel.rmi_and_databases.server.integration;

import id1212.wachsler.joel.rmi_and_databases.common.dto.FileInfoDTO;
import id1212.wachsler.joel.rmi_and_databases.server.user.User;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FileDAO extends DB {
  private static final String FILE_TABLE = "files";
  private static FileDAO instance;
  private PreparedStatement getFilesStmt;
  private PreparedStatement getFileStmt;

  private FileDAO() {
    try {
      prepareStatements(super.getConnection());
    } catch (SQLException e) {
      System.err.println("Failed to prepare FileDAO statements..");
      e.printStackTrace();
    }
  }

  private void prepareStatements(Connection connection) throws SQLException {
    getFilesStmt = connection.prepareStatement(
      "SELECT * FROM " + FILE_TABLE +
        " WHERE " +
        "`owner`=?" +
        " OR " +
        "`public`=TRUE");

    getFileStmt = connection.prepareStatement(
      "SELECT * FROM " + FILE_TABLE +
        " WHERE " +
        "`name`=?");
  }

  /**
   * @return The singleton instance of the <code>FileDAO</code>.
   */
  public static FileDAO getInstance() {
    if (instance == null) instance = new FileDAO();

    return instance;
  }

  /**
   * Retrieves all files the provided user has access to.
   *
   * @param user The user to check which files it has access to.
   * @return A list of <code>FileInfoDTO</code>.
   * @throws SQLException When the query fails...
   */
  public List<FileInfoDTO> getFiles(User user) throws SQLException {
    try {
      getFilesStmt.setLong(1, user.getId());

      ResultSet result = getFilesStmt.executeQuery();

      List<FileInfoDTO> files = new ArrayList<>();

      while (result.next()) {
        try {
          files.add(createFileInfo(result));
        } catch (Exception e) {
          System.err.println("Failed to add user...");
          e.printStackTrace();
        } catch (UserDoesNotExistException e) {
          e.printStackTrace();
        }
      }

      return files;
    } catch (SQLException e) {
      System.err.println("Failed to execute the getFiles query!");
      e.printStackTrace();

      throw e;
    }
  }

  private FileInfoDTO createFileInfo(ResultSet result) throws SQLException, UserDoesNotExistException {
    String name = result.getString("name");
    long size = result.getLong("size");
    String owner = UserDAO.getInstance().getUsername(result.getInt("owner"));
    boolean isPublic = result.getBoolean("public");
    boolean read = result.getBoolean("read");
    boolean write = result.getBoolean("write");

    return new FileInfoDTO(name, size, owner, isPublic, read, write);
  }

  /**
   * Retrieves information about a specific file.
   *
   * @param filename The file to retrieve information about.
   * @return <code>FileInfoDTO</code> with the file info.
   * @throws FileNotFoundException If the file was not found.
   * @throws SQLException When the query fails.
   */
  public FileInfoDTO getFile(String filename) throws FileNotFoundException, SQLException, UserDoesNotExistException {
    try {
      getFileStmt.setString(1, filename);
      ResultSet result = getFileStmt.executeQuery();

      if (!result.next()) throw new FileNotFoundException("The file does not exist!");

      return createFileInfo(result);
    } catch (SQLException e) {
      System.err.println("Failed to execute getFile query!");
      e.printStackTrace();

      throw e;
    } catch (UserDoesNotExistException e) {
      e.printStackTrace();

      throw e;
    }
  }
}
