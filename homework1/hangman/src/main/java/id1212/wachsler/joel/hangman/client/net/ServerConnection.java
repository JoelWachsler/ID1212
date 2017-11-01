package id1212.wachsler.joel.hangman.client.net;

import id1212.wachsler.joel.hangman.common.Constants;
import id1212.wachsler.joel.hangman.common.MessageException;
import id1212.wachsler.joel.hangman.common.MsgType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.StringJoiner;

/**
 * Handles connections and message handling to and from the server
 */
public class ServerConnection {
  private static final int SOCKET_TIMEOUT = 1800000; // Set socket timeout to half a minute
  private Socket socket;
  private PrintWriter toServer;
  private BufferedReader fromServer;
  private volatile boolean connected;

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
    boolean autoFlush = true;
    toServer = new PrintWriter(socket.getOutputStream(), autoFlush);
    fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    new Thread(new Listener(broadcastHandler)).start();
  }

  public void sendChatEntry(String msg) {
    sendMsg(MsgType.ENTRY.toString(), msg);
  }

  /**
   * Joins the arguments and sends them to the server
   *
   * @param parts Arbitrary number of args (parts is treated as an array in the body)
   */
  private void sendMsg(String... parts) {
    StringJoiner joiner = new StringJoiner(Constants.MSG_DELIMITER);

    Arrays.stream(parts).forEach(joiner::add);

    toServer.println(joiner.toString());
  }

  /**
   * Disconnects from the server
   *
   * @throws IOException
   */
  public void disconnect() throws IOException {
    sendMsg(MsgType.DISCONNECT.toString());
    socket.close();
    socket = null;
    connected = false;
  }

  /**
   * Sends a guess to the server
   *
   * @param guessingWord The word or letter to guess
   */
  public void sendGuess(String guessingWord) {
    System.out.println("Sending a guess!");
    sendMsg(MsgType.GUESS.toString(), guessingWord);
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
          outputHandler.handleMsg(extractMsgBody(fromServer.readLine()));
        }
      } catch (Throwable connectionFailure) {
        if (connected) {
          outputHandler.handleMsg("Lost connection...");
        }
      }
    }

    private String extractMsgBody(String entireMsg) {
      String[] msgParts = entireMsg.split(Constants.MSG_DELIMITER);
      if (MsgType.valueOf(msgParts[Constants.MSG_TYPE_INDEX].toUpperCase()) != MsgType.BROADCAST) {
        throw new MessageException("A corrupt message was received: " + entireMsg);
      }
      return msgParts[Constants.MSG_BODY_INDEX];
    }
  }
}
