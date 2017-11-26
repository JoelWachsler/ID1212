package id1212.wachsler.joel.rmi_and_databases.server.integration;

import id1212.wachsler.joel.rmi_and_databases.common.dto.FileInfoDTO;

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
  }

  public static FileDAO getInstance() {
    if (instance == null) instance = new FileDAO();

    return instance;
  }

  public List<FileInfoDTO> getFiles(long userId) throws UnknownError {
    try {
      getFilesStmt.setLong(1, userId);

      ResultSet result = getFilesStmt.executeQuery();

      List<FileInfoDTO> files = new ArrayList<>();

      while (result.next()) {
        try {
          String name = result.getString("name");
          long size = result.getLong("size");
          String owner = UserDAO.getInstance().getUsername(result.getInt("owner"));
          boolean isPublic = result.getBoolean("public");
          boolean read = result.getBoolean("read");
          boolean write = result.getBoolean("write");

          files.add(new FileInfoDTO(name, size, owner, isPublic, read, write));
        } catch (Exception e) {
          System.err.println("Failed to add user...");
          e.printStackTrace();
        }
      }

      return files;
    } catch (SQLException e) {
      System.err.println("Failed to execute the getFiles query!");
      e.printStackTrace();

      throw new UnknownError("Failed to get files...");
    }
  }
}
