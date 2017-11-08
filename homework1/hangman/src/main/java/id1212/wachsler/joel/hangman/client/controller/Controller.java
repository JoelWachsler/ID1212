package id1212.wachsler.joel.hangman.client.controller;

import id1212.wachsler.joel.hangman.client.net.OutputHandler;
import id1212.wachsler.joel.hangman.client.net.ServerConnection;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.CompletableFuture;

/**
 * Controller for the client.
 */
public class Controller {
  private final ServerConnection serverConnection = new ServerConnection();

  public void connect(String host, int port, OutputHandler outputHandler) {
    CompletableFuture.runAsync(() -> {
      try {
        serverConnection.connect(host, port, outputHandler);
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }).thenRun(() -> outputHandler.handleMsg("Connected to " + host + ":" + port));
  }

  /**
   * @see ServerConnection#disconnect()
   */
  public void disconnect() throws IOException {
    serverConnection.disconnect();
  }

  /**
   * @see ServerConnection#sendGuess(String)
   */
  public void guess(String guessingWord) {
    // Send async guess
    CompletableFuture.runAsync(() -> {
      try {
        serverConnection.sendGuess(guessingWord);
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  /**
   * @see ServerConnection#startGame()
   */
  public void startGame() {
    CompletableFuture.runAsync(() -> {
      try {
        serverConnection.startGame();
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }
}
