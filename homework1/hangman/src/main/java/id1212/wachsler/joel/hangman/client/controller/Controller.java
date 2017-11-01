package id1212.wachsler.joel.hangman.client.controller;

import id1212.wachsler.joel.hangman.client.net.OutputHandler;
import id1212.wachsler.joel.hangman.client.net.ServerConnection;
import id1212.wachsler.joel.hangman.common.MsgType;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.CompletableFuture;

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

  public void sendMsg(String msg) {
    // Send message async
    CompletableFuture.runAsync(() -> serverConnection.sendChatEntry(msg));
  }

  public void disconnect() throws IOException {
    serverConnection.disconnect();
  }

  public void guess(String guessingWord) {
    // Send async guess
    CompletableFuture.runAsync(() -> serverConnection.sendGuess(guessingWord));
  }
}
