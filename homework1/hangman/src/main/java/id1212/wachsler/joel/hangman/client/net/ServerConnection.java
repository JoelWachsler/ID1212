package id1212.wachsler.joel.hangman.client.net;

import id1212.wachsler.joel.hangman.common.Message;
import id1212.wachsler.joel.hangman.common.MsgType;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Handles connections and message handling to and from the server
 */
public class ServerConnection {
  private static final int SOCKET_TIMEOUT = 1800000; // Set socket timeout to half a minute
  private Socket socket;
  private ObjectOutputStream toServer;
  private ObjectInputStream fromServer;
  private volatile boolean connected; // Read from main memory and not cache

  /**
   * Connects to the specified server
   *
   * @param host The IP of the server
   * @param port The port to connect to on the server
   * @param broadcastHandler Where to print output
   * @throws IOException
   */
  public void connect(String host, int port, OutputHandler broadcastHandler) throws IOException {
    socket = new Socket();
    socket.connect(new InetSocketAddress(host, port), SOCKET_TIMEOUT);
    socket.setSoTimeout(SOCKET_TIMEOUT);
    connected = true;
    toServer = new ObjectOutputStream(socket.getOutputStream());
    fromServer = new ObjectInputStream(socket.getInputStream());
    new Thread(new Listener(broadcastHandler)).start();
  }

  /**
   * Sends a message to the server without a body
   */
  private void sendMsg(MsgType type) throws IOException {
    send(new Message(type, ""));
  }

  /**
   * Encapsulates the message and sends it to the server.
   */
  private void sendMsg(MsgType type, String body) throws IOException {
    send(new Message(type, body));
  }

  private void send(Message message) throws IOException {
    toServer.writeObject(message);
    toServer.flush(); // Flush the pipe
    toServer.reset(); // Remove object cache
  }

  /**
   * Disconnects from the server
   *
   * @throws IOException
   */
  public void disconnect() throws IOException {
    sendMsg(MsgType.DISCONNECT);
    socket.close();
    socket = null;
    connected = false;
  }

  /**
   * Sends a guess to the server
   *
   * @param guessingWord The word or letter to guess
   */
  public void sendGuess(String guessingWord) throws IOException {
    sendMsg(MsgType.GUESS, guessingWord);
  }

  /**
   * Sends a start message to the server
   */
  public void startGame() throws IOException {
    sendMsg(MsgType.START);
  }

  /**
   * Returns true or false if the client is connected to the server
   *
   * @return true if connected to the server else false
   */
  public boolean isConnected() {
    return connected;
  }

  private class Listener implements Runnable {
    private final OutputHandler outputHandler;

    private Listener(OutputHandler broadcastHandler) {
      this.outputHandler = broadcastHandler;
    }

    @Override
    public void run() {
      try {
        while (true) {
          Message message = (Message) fromServer.readObject();

          outputHandler.handleMsg(message.getBody());
        }
      } catch (Throwable connectionFailure) {
        if (connected) outputHandler.handleMsg("Connection lost to the server...");
      }
    }
  }
}
