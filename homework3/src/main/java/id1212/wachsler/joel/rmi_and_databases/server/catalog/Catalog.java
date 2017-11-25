package id1212.wachsler.joel.rmi_and_databases.server.catalog;

import id1212.wachsler.joel.rmi_and_databases.common.FileInfoDTO;
import id1212.wachsler.joel.rmi_and_databases.server.integration.FileDAO;
import id1212.wachsler.joel.rmi_and_databases.server.user.User;

import java.util.List;

public class Catalog {
  private FileDAO fileDAO;
  private User user;

  public Catalog(User user) {
    this.user = user;
    fileDAO = FileDAO.getInstance();
  }

  public List<FileInfoDTO> list() {
    return fileDAO.getFiles(user.getId());
  }
}
