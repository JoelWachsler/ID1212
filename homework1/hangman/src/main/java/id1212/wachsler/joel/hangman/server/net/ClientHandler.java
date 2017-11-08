package id1212.wachsler.joel.hangman.server.net;

import id1212.wachsler.joel.hangman.common.Message;
import id1212.wachsler.joel.hangman.common.MessageException;
import id1212.wachsler.joel.hangman.common.MsgType;
import id1212.wachsler.joel.hangman.server.controller.Controller;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
  private final HangmanServer server;
  private final Socket clientSocket;
  private ObjectInputStream fromClient;
  private ObjectOutputStream toClient;
  private boolean connected;
  private Controller controller = new Controller();

  ClientHandler(HangmanServer server, Socket clientSocket) {
    this.server = server;
    this.clientSocket = clientSocket;
    controller.newHangmanGame();
    connected = true;
  }

  @Override
  public void run() {
    // Create input and output streams to the client
    try {
      fromClient = new ObjectInputStream(clientSocket.getInputStream());
      toClient = new ObjectOutputStream(clientSocket.getOutputStream());
    } catch (IOException e) {
      System.err.println("Failed to create read/write streams...");
      e.printStackTrace();
    }

    while (connected) {
      try {
        Message msg = (Message) fromClient.readObject();

        switch (msg.getType()) {
          case GUESS:
            System.out.println("Got a message!");
            System.out.println("The message is: " + msg.getBody());
            sendGuessResponse(controller.guess(msg.getBody()), controller.getTries(), controller.getScore());
            break;
          case START:
            System.out.println("The client wants to start a new game instance!");
            controller.startNewGameInstance();
            break;
          case DISCONNECT:
            disconnectClient();
            break;
          default:
            throw new MessageException("Received a corrupt message: " + msg.getType());
        }
      } catch (IOException | ClassNotFoundException e) {
        disconnectClient();
        System.err.println(e.getMessage());
      }
    }
  }

  private void sendGuessResponse(char[] guess, int tries, int score) throws IOException {
    if (guess == null) return;

    sendMsg(
      MsgType.GUESS_RESPONSE,
      "Word: " + String.valueOf(guess) + ", " +
        "Remaining failed attempts: " + tries + ", " +
        "Score: " + score
    );
  }

  /**
   * Encapsulates the message and sends it to the server.
   */
  private void sendMsg(MsgType type, String body) throws IOException {
    Message message = new Message(type, body);
    toClient.writeObject(message);
    toClient.flush(); // Flush the pipe
    toClient.reset(); // Remove object cache
  }

  private void disconnectClient() {
    try {
      clientSocket.close();
    } catch (IOException e) {
      System.err.println("Couldn't close a client connection!");
      e.printStackTrace();
    }

    connected = false;
    server.removeHandler(this);
  }
}
