package id1212.wachsler.joel.hangman.client.controller;

import id1212.wachsler.joel.hangman.client.net.Listener;
import id1212.wachsler.joel.hangman.client.net.ServerConnection;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * Controller for the client.
 */
public class Controller {
  private final ServerConnection serverConnection = new ServerConnection();

  /**
   * @see ServerConnection#addListener(Listener)
   */
  public void addServerConnectionListener(Listener listener) {
    serverConnection.addListener(listener);
  }

  /**
   * @see ServerConnection#connect(String, int)
   */
  public void connect(String host, int port) {
    CompletableFuture.runAsync(() -> serverConnection.connect(host, port));
  }

  /**
   * @see ServerConnection#disconnect()
   */
  public void disconnect() throws IOException {
    serverConnection.disconnect();
  }

  /**
   * @see ServerConnection#isConnected()
   */
  public boolean isConnected() {
    return serverConnection.isConnected();
  }

  /**
   * @see ServerConnection#sendGuess(String)
   */
  public void guess(String guessingWord) {
    // Send async guess
    CompletableFuture.runAsync(() -> serverConnection.sendGuess(guessingWord));
  }

  /**
   * @see ServerConnection#startGame()
   */
  public void startGame() {
    CompletableFuture.runAsync(() -> serverConnection.startGame());
  }
}
