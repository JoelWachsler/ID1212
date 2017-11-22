package id1212.wachsler.joel.hangman.server.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.InvalidParameterException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class HangmanServer {
  private static final int LINGER_TIME = 5000; // Time to keep on sending if the connection is closed
  private static final int port = 8080; // Server default listening port

  private Selector selector;
  private final Queue<SelectionKey> pendingWrite = new ArrayDeque<>();
  private final Map<ClientHandler, SelectionKey> reverseKeyLookup = new HashMap<>();

  /**
   * Initializes the HangmanServer
   */
  public static void start() {
    System.out.println("Starting the HangmanServer!");

    HangmanServer server = new HangmanServer();
    server.serve();
  }

  private void serve() {
    try {
      selector = Selector.open(); // Create a new selector

      ServerSocketChannel listeningSocketChannel = ServerSocketChannel.open(); // Open server socket channel
      listeningSocketChannel.configureBlocking(false);
      listeningSocketChannel.bind(new InetSocketAddress(port));
      // Register channel to selector and we're interested in accepting connections
      listeningSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

      while (true) {
        while (!pendingWrite.isEmpty()) pendingWrite.poll().interestOps(SelectionKey.OP_WRITE);

        selector.select(); // Blocking until at least one channel is selected

        // Go through each selected key and check if there's something to do
        for (SelectionKey key : selector.selectedKeys()) {
          selector.selectedKeys().remove(key); // Remove the selected current key

          if (!key.isValid()) continue;

          if      (key.isAcceptable()) startClientHandler(key);
          else if (key.isReadable())   receiveFromClient(key);
          else if (key.isWritable())   sendToClient(key);
        }
      }

    } catch (IOException e) {
      System.err.println("Server failed...");
      e.printStackTrace();
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
  }

  private void startClientHandler(SelectionKey key) throws IOException {
    System.out.println("A client wants to connect!");

    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
    SocketChannel clientChannel = serverSocketChannel.accept(); // Establish connection
    clientChannel.configureBlocking(false);

    ClientHandler handler = new ClientHandler(clientChannel, this);
    SelectionKey clientKey = clientChannel.register(selector, SelectionKey.OP_READ, handler);
    reverseKeyLookup.put(handler, clientKey);
    clientChannel.setOption(StandardSocketOptions.SO_LINGER, LINGER_TIME);
  }

  private void receiveFromClient(SelectionKey clientKey) throws IOException {
    ClientHandler clientHandler = (ClientHandler) clientKey.attachment();
    try {
      clientHandler.receiveMsg();
    } catch (IOException clientClosedConn) {
      System.out.println("A client closed their connection!");
      removeClient(clientKey);
    }
  }

  private void sendToClient(SelectionKey clientKey) throws IOException {
    ClientHandler clientHandler = (ClientHandler) clientKey.attachment();

    clientHandler.sendMessages();
    clientKey.interestOps(SelectionKey.OP_READ);
  }

  private void removeClient(SelectionKey clientKey) throws IOException {
    ClientHandler clientHandler = (ClientHandler) clientKey.attachment();
    clientHandler.disconnectClient();
    clientKey.cancel(); // Make the key invalid
    reverseKeyLookup.remove(clientHandler);
  }

  /**
   * Adds a key to be read from at a later time.
   *
   * @param client The client who we have a ready message to write to.
   * @throws InvalidParameterException When the <code>SelectionKey</code> is null, invalid or not an instance of <code>SocketChannel</code>.
   */
  void addPendingMsg(ClientHandler client) throws InvalidParameterException {
    SelectionKey channelKey = reverseKeyLookup.get(client);

    if (!channelKey.isValid() || !(channelKey.channel() instanceof SocketChannel))
      throw new InvalidParameterException("The channel key is invalid!");
    
    synchronized (pendingWrite) {
      pendingWrite.add(channelKey);
    }

    selector.wakeup();
  }
}
