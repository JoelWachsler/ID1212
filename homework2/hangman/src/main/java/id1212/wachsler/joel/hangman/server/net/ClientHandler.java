package id1212.wachsler.joel.hangman.server.net;

import id1212.wachsler.joel.hangman.common.Constants;
import id1212.wachsler.joel.hangman.common.MessageCreator;
import id1212.wachsler.joel.hangman.common.MessageParser;
import id1212.wachsler.joel.hangman.common.MessageType;
import id1212.wachsler.joel.hangman.server.controller.Controller;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

/**
 * Handles a client socket instance.
 */
public class ClientHandler implements Runnable {
  private final SocketChannel clientChannel;
  private Controller controller = new Controller();
  private final ByteBuffer messageBuffer = ByteBuffer.allocateDirect(Constants.MSG_MAX_LEN);
  private final MessageParser messageParser = new MessageParser();
  private final Queue<ByteBuffer> messageQueue = new ArrayDeque<>(); // Init capacity = 16
  private final Selector serverSelector;

  ClientHandler(SocketChannel clientChannel, Selector serverSelector) {
    this.clientChannel = clientChannel;
    this.serverSelector = serverSelector;
    controller.newHangmanGame();
  }

  @Override
  public void run() {
    while (messageParser.hasNext()) try {
      Message message = new Message(messageParser.nextMsg());

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
    }
  }

  private void sendGuessResponse(String response) throws IOException {
    if (response == null) return;

    addMsg(MessageType.GUESS_RESPONSE, response);
  }

  private void addMsg(MessageType guessResponse, String response) {
    ByteBuffer msg = MessageCreator.createMessage(guessResponse, response);

    synchronized (messageQueue) {
      messageQueue.add(msg);
    }

    serverSelector.wakeup();
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
      System.err.println("Couldn't disconnect a client!");
    }
  }

  void receiveMsg() throws IOException {
    messageBuffer.clear();
    int readBytes = clientChannel.read(messageBuffer);

    if (readBytes == -1) throw new IOException("Client closed the connection...");

    String receivedMsg = extractMsgFromBuffer();

    messageParser.addMessage(receivedMsg);
    CompletableFuture.runAsync(this);
//    ForkJoinPool.commonPool().execute(this);
  }

  private String extractMsgFromBuffer() {
    messageBuffer.flip();
    byte[] bytes = new byte[messageBuffer.remaining()];
    messageBuffer.get(bytes);

    return new String(bytes);
  }

  /**
   * Parses and encapsulates a message from the provided string.
   * A message must contain a <code>MessageType</code> and a message body.
   */
  private static class Message {
    private MessageType msgType;
    private String msgBody;

    Message(String unparsedMsg) {
      parse(unparsedMsg);
    }

    private void parse(String unparsedMsg) {
      try {
        String[] splitMsg = unparsedMsg.split(Constants.MSG_TYPE_DELIMITER);

        msgType = MessageType.valueOf(splitMsg[Constants.MSG_TYPE_INDEX].toUpperCase());
        msgBody = splitMsg[Constants.MSG_BODY_INDEX];
      } catch (Exception e) {
        System.err.println("Unable to parse the following message: " + unparsedMsg);
      }
    }

    MessageType getType() {
      return msgType;
    }

    String getBody() {
      return msgBody;
    }
  }
}
