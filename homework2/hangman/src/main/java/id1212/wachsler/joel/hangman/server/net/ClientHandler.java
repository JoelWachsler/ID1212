package id1212.wachsler.joel.hangman.server.net;

import id1212.wachsler.joel.hangman.common.*;
import id1212.wachsler.joel.hangman.common.message.Message;
import id1212.wachsler.joel.hangman.common.message.MessageCreator;
import id1212.wachsler.joel.hangman.common.message.MessageParser;
import id1212.wachsler.joel.hangman.common.message.MessageType;
import id1212.wachsler.joel.hangman.server.controller.Controller;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;

/**
 * Handles a client socket instance.
 */
public class ClientHandler implements Runnable {
  private final SocketChannel clientChannel;
  private final HangmanServer server;
  private Controller controller = new Controller();
  private final ByteBuffer messageBuffer = ByteBuffer.allocateDirect(Constants.MSG_MAX_LEN);
  private final MessageParser messageParser = new MessageParser();
  private final Queue<ByteBuffer> messageQueue = new ArrayDeque<>(); // Init capacity = 16
  private SelectionKey channelKey;

  ClientHandler(SocketChannel clientChannel, HangmanServer server) {
    this.clientChannel = clientChannel;
    this.server = server;
    controller.newHangmanGame();
  }

  @Override
  public void run() {
    while (messageParser.hasNext()) try {
      Message message = messageParser.nextMsg();

      switch (message.getType()) {
        case GUESS:
          System.out.println("A new guess is being processed: " + message.getBody());
          sendGuessResponse(controller.guess(message.getBody()));
          break;
        case START:
          System.out.println("The client wants to start a new game instance.");
          controller.startNewGameInstance();
          break;
        case DISCONNECT:
          disconnectClient();
          break;
        default:
          throw new StreamCorruptedException("Received a corrupt message: " + message.getType());
      }
    } catch (EOFException e) {
      System.err.println("The client unexpectedly disconnected...");
      disconnectClient();
    } catch (IOException e) {
      System.err.println(e.getMessage());
      disconnectClient();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
  }

  private void sendGuessResponse(String response) throws IOException, IllegalArgumentException {
    if (response == null) throw new IllegalArgumentException("Got a null response!");

    addMsg(MessageType.GUESS_RESPONSE, response);
  }

  private void addMsg(MessageType guessResponse, String response) {
    ByteBuffer msg = MessageCreator.createMessage(guessResponse, response);

    synchronized (messageQueue) {
      messageQueue.add(msg);
    }

    server.addPendingMsg(channelKey);
    server.wakeup();
  }

  /**
   * Sends all messages in the message queue to the client.
   */
  void sendMessages() throws IOException {
    ByteBuffer msg;
    synchronized (messageQueue) {
      while ((msg = messageQueue.peek()) != null) {
        sendMsg(msg);
        messageQueue.poll();
      }
    }
  }

  private void sendMsg(ByteBuffer msg) throws IOException {
    clientChannel.write(msg);

    if (msg.hasRemaining()) throw new IOException("The message could not be sent!");
  }

  void disconnectClient() {
    try {
      clientChannel.close();
    } catch (IOException e) {
      System.err.println("Something went wrong when trying to disconnect a client...");
    }
  }

  void receiveMsg() throws IOException {
    messageBuffer.clear();
    int readBytes = clientChannel.read(messageBuffer);

    if (readBytes == -1) throw new IOException("Client closed the connection...");

    String receivedMsg = extractMsgFromBuffer();
    messageParser.addMessage(receivedMsg);

    CompletableFuture.runAsync(this);
  }

  private String extractMsgFromBuffer() {
    messageBuffer.flip();
    byte[] bytes = new byte[messageBuffer.remaining()];
    messageBuffer.get(bytes);

    return new String(bytes);
  }

  void registerKey(SelectionKey channelKey) {
    this.channelKey = channelKey;
  }
}
