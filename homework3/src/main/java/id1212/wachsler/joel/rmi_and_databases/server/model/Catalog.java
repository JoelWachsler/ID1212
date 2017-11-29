package id1212.wachsler.joel.rmi_and_databases.server.model;

import id1212.wachsler.joel.rmi_and_databases.server.integration.FileDAO;


public class Catalog {
  private FileDAO fileDAO;

  public Catalog() {
  }

  /**
   * Checks if the user is allowed to upload the specified file.
   *
   * @param filename The filename to check if the user is allowed to upload.
   */
  public void allowedToUpload(String filename) throws IllegalAccessException {
  }
}
