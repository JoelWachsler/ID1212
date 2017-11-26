package id1212.wachsler.joel.rmi_and_databases.client.controller;

import id1212.wachsler.joel.rmi_and_databases.client.net.ServerConnection;

import java.io.IOException;

public class Controller {
  private ServerConnection connection;
  private long userId;

  private void initSocket() throws IOException {
    connection = new ServerConnection();
  }

  private void checkAuth() throws IllegalAccessException {
    if (userId == 0) throw new IllegalAccessException("You must be logged in to do this!");
  }

  public void upload(String filename, String fileUploadName) throws Exception {
    checkAuth();

    connection.upload(filename, fileUploadName, userId);
  }

  public void endConnection() throws IOException, IllegalAccessException {
    checkAuth();

    connection.close();
    connection = null;
  }

  public void authenticated(long userId) throws IOException {
    this.userId = userId;
    initSocket();
  }

  public long getUserId() throws IllegalAccessException {
    checkAuth();

    return userId;
  }
}
