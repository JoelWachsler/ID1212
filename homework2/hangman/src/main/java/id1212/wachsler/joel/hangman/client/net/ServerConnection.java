package id1212.wachsler.joel.hangman.client.net;

import id1212.wachsler.joel.hangman.common.Constants;
import id1212.wachsler.joel.hangman.common.MessageCreator;
import id1212.wachsler.joel.hangman.common.MessageParser;
import id1212.wachsler.joel.hangman.common.MessageType;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Handles connections and message handling to and from the server
 */
public class ServerConnection implements Runnable {
  private volatile boolean connected; // Read from main memory and not cache
  private InetSocketAddress serverAddress;
  private Selector selector;
  private SocketChannel socketChannel;
  private final Queue<ByteBuffer> messageQueue = new ArrayDeque<>();
  private final ByteBuffer messageBuffer = ByteBuffer.allocateDirect(Constants.MSG_MAX_LEN);
  private final MessageParser messageParser = new MessageParser();
  private volatile boolean timeToSend = false;

  /**
   * Connects to the specified server.
   *
   * @param host The IP of the server
   * @param port The port to connect to on the server
   * @param broadcastHandler Where to print output
   * @throws IOException
   */
  public void connect(String host, int port, OutputHandler broadcastHandler) throws IOException {
    serverAddress = new InetSocketAddress(host, port);
    new Thread(this).start();
  }

  /**
   * Sends a message to the server without a body
   */
  private void sendMsg(MessageType type) throws IOException {
    send(type, "");
  }

  /**
   * Encapsulates the message and sends it to the server.
   */
  private void sendMsg(MessageType type, String body) throws IOException {
    send(type, body);
  }

  private void send(MessageType type, String body) throws IOException {
    ByteBuffer msg = MessageCreator.createMessage(type, body);
    synchronized (messageQueue) {
      messageQueue.add(msg);
    }
    timeToSend = true;
    selector.wakeup();
  }

  /**
   * Disconnects from the server
   *
   * @throws IOException
   */
  public void disconnect() throws IOException {
    connected = false;
    sendMsg(MessageType.DISCONNECT);
    socketChannel.close();
    socketChannel.keyFor(selector).cancel();
  }

  /**
   * Sends a guess to the server
   *
   * @param guessingWord The word or letter to guess
   */
  public void sendGuess(String guessingWord) throws IOException {
    sendMsg(MessageType.GUESS, guessingWord);
  }

  /**
   * Sends a start message to the server
   */
  public void startGame() throws IOException {
    sendMsg(MessageType.START);
  }

  /**
   * Returns true or false if the client is connected to the server
   *
   * @return true if connected to the server else false
   */
  public boolean isConnected() {
    return connected;
  }

  @Override
  public void run() {
    try {
      socketChannel = SocketChannel.open();
      socketChannel.configureBlocking(false);
      socketChannel.connect(serverAddress);
      connected = true;

      selector = Selector.open();
      while (connected) {
        if (timeToSend) {
          socketChannel.keyFor(selector).interestOps(SelectionKey.OP_WRITE);
          timeToSend = false;
        }

        selector.select(); // Blocking until at least one channel is selected

        // Go through each selected key and check if there's something to do
        for (SelectionKey key : selector.selectedKeys()) {
          selector.selectedKeys().remove(key); // Remove the selected current key

          if (!key.isValid()) continue;

          if      (key.isAcceptable())  completeConnection(key);
          else if (key.isReadable())    receiveFromServer(key);
          else if (key.isWritable())    sendToServer(key);
        }
      }
    } catch (Exception e) {
      System.err.println("Something went wrong with the connection...");
      e.printStackTrace();
    }
  }

  private void sendToServer(SelectionKey key) throws IOException {
    ByteBuffer msg;
    synchronized (messageQueue) {
      while ((msg = messageQueue.peek()) != null) {
        socketChannel.write(msg);
        if (msg.hasRemaining()) return; // Failed to send the messsage
        messageQueue.remove();
      }
    }
    key.interestOps(SelectionKey.OP_READ);
  }

  private void receiveFromServer(SelectionKey key) throws IOException {
    messageBuffer.clear();
    int readBytes = socketChannel.read(messageBuffer);

    if (readBytes == -1) throw new IOException("Failed to read from server...");

    String receivedMsg = extractMsgFromBuffer();
    messageParser.addMessage(receivedMsg);

    while (messageParser.hasNextMsg()) {
      String msg = messageParser.nextMsg();

      System.out.println(msg);
    }
  }

  private String extractMsgFromBuffer() {
    messageBuffer.flip();
    byte[] bytes = new byte[messageBuffer.remaining()];

    return String.valueOf(messageBuffer.get(bytes));
  }

  private void completeConnection(SelectionKey key) throws IOException {
    socketChannel.finishConnect();
    key.interestOps(SelectionKey.OP_WRITE);
  }
}
