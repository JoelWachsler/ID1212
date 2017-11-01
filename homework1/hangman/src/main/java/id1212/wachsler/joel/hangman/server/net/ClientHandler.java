package id1212.wachsler.joel.hangman.server.net;

import id1212.wachsler.joel.hangman.common.Constants;
import id1212.wachsler.joel.hangman.common.MessageException;
import id1212.wachsler.joel.hangman.common.MsgType;
import id1212.wachsler.joel.hangman.server.controller.Controller;
import id1212.wachsler.joel.hangman.server.game.HangmanGame;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.StringJoiner;

import static id1212.wachsler.joel.hangman.common.Constants.MSG_DELIMITER;

public class ClientHandler implements Runnable {
  private final HangmanServer server;
  private final Socket clientSocket;
  private BufferedReader fromClient;
  private PrintWriter toClient;
  private boolean connected;
  private final HangmanGame game;

  ClientHandler(HangmanServer server, Socket clientSocket, Controller controller) {
    this.server = server;
    this.clientSocket = clientSocket;
    game = new HangmanGame(controller);
    connected = true;
  }

  @Override
  public void run() {
    // Create input and output streams to the client
    try {
      boolean autoFlush = true; // Just for readability
      fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      toClient = new PrintWriter(clientSocket.getOutputStream(), autoFlush);
    } catch (IOException e) {
      System.err.println("Failed to create read/write streams...");
      e.printStackTrace();
    }

    while (connected) {
      try {
        Message msg = new Message(fromClient.readLine());

        switch (msg.msgType) {
          case GUESS:
            System.out.println("Got a message!");
            System.out.println("The message is: " + msg.msgBody);
            sendGuessResponse(game.guess(msg.msgBody), game.getTries(), game.getScore());
            break;
          case START:
            System.out.println("The client wants to start a new game instance!");
            game.newGameInstance();
            break;
          case DISCONNECT:
            disconnectClient();
            break;
          default:
            throw new MessageException("Received a corrupt message: " + msg.receivedString);
        }
      } catch (IOException e) {
        disconnectClient();
        throw new MessageException(e);
      }
    }
  }

  private void sendGuessResponse(char[] guess, int tries, int score) {
    if (guess == null) return;

    sendMsg(
      MsgType.GUESS_RESPONSE.toString(),
      "Word: " + String.valueOf(guess) + " | " +
        "Remaining failed attempts: " + tries + " | " +
        "Score: " + score
    );
  }

  private void sendMsg(String... parts) {
    StringJoiner joiner = new StringJoiner(Constants.MSG_DELIMITER);

    Arrays.stream(parts).forEach(joiner::add);

    toClient.println(joiner.toString());
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

  private static class Message {
    private MsgType msgType;
    private String msgBody;
    private String receivedString;

    private Message(String receivedString) {
      this.receivedString = receivedString;
      parse(receivedString);
    }

    private void parse(String strToParse) {
      try {
        String[] msgTokens = strToParse.split(MSG_DELIMITER);
        msgType = MsgType.valueOf(msgTokens[Constants.MSG_TYPE_INDEX].toUpperCase());
        if (hasBody(msgTokens)) {
          msgBody = msgTokens[Constants.MSG_BODY_INDEX];
        }
      } catch (Throwable throwable) {
        throw new MessageException(throwable);
      }
    }

    private boolean hasBody(String[] msgTokens) {
      return msgTokens.length > 1;
    }
  }
}
