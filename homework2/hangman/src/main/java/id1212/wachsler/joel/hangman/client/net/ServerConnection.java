package id1212.wachsler.joel.hangman.client.net;

import id1212.wachsler.joel.hangman.common.*;
import id1212.wachsler.joel.hangman.common.message.Message;
import id1212.wachsler.joel.hangman.common.message.MessageCreator;
import id1212.wachsler.joel.hangman.common.message.MessageParser;
import id1212.wachsler.joel.hangman.common.message.MessageType;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.*;

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
  private final List<Listener> listeners = new ArrayList<>();

  /**
   * Connects to the specified server.
   *
   * @param host The IP of the server
   * @param port The port to connect to on the server
   * @throws IOException
   */
  public void connect(String host, int port) {
    try {
      serverAddress = new InetSocketAddress(host, port);
      new Thread(this).start();
    } catch (Exception e) {
      alertError("Something went wrong when connecting...", e);
    }
  }

  public void addListener(Listener listener) {
    listeners.add(listener);
  }

  /**
   * Sends a message to the server without a body
   */
  private void sendMsg(MessageType type) {
    send(type, "");
  }

  /**
   * Encapsulates the message and sends it to the server.
   */
  private void sendMsg(MessageType type, String body) {
    send(type, body);
  }

  private void send(MessageType type, String body) {
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
  public void sendGuess(String guessingWord) {
    sendMsg(MessageType.GUESS, guessingWord);
  }

  /**
   * Sends a start message to the server
   */
  public void startGame() {
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

  /**
   * The non-blocking communication handler between the server and client, handled by a single thread.
   */
  @Override
  public void run() {
    try {
      selector = Selector.open(); // Create a new selector

      socketChannel = SocketChannel.open();
      socketChannel.configureBlocking(false);
      socketChannel.connect(serverAddress);
      connected = true;

      socketChannel.register(selector, SelectionKey.OP_CONNECT);

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

          if      (key.isConnectable()) completeConnection(key);
          else if (key.isReadable())    receiveFromServer(key);
          else if (key.isWritable())    sendToServer(key);
        }
      }
    } catch (Exception e) {
      alertError("Something went wrong with the connection...", e);
    }
  }

  private void sendToServer(SelectionKey key) throws IOException {
    ByteBuffer msg;
    synchronized (messageQueue) {
      while ((msg = messageQueue.peek()) != null) {
        socketChannel.write(msg);
        if (msg.hasRemaining()) return; // Failed to send the message
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

    while (messageParser.hasNext()) {
      Message message = messageParser.nextMsg();

      alertListeners(message.getBody());
    }
  }

  private String extractMsgFromBuffer() {
    messageBuffer.flip();
    byte[] bytes = new byte[messageBuffer.remaining()];
    messageBuffer.get(bytes);

    return new String(bytes);
  }

  private void completeConnection(SelectionKey key) throws IOException {
    socketChannel.finishConnect();
    key.interestOps(SelectionKey.OP_WRITE);

    alertListeners("Connected to: " + serverAddress);
  }

  private void alertListeners(String msg) {
    listeners.forEach(listener -> listener.print(msg));
  }

  private void alertError(String error, Exception e) {
    listeners.forEach(listener -> listener.error(error, e));
  }
}
