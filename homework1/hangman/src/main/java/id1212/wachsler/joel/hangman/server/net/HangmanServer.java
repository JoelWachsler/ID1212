package id1212.wachsler.joel.hangman.server.net;

import id1212.wachsler.joel.hangman.server.controller.Controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class HangmanServer {
  private static final int LINGER_TIME = 5000; // Time to keep on sending if the connection is closed
  private static final int SOCKET_TIMEOUT = 1800000; // Set socket timeout to half a minute
  private final Controller controller = new Controller();
  private final int PORT = 8080; // Server listening port
  private final List<ClientHandler> clients = new ArrayList<>();

  public static void main(String[] args) {
    System.out.println("HangmanServer started!");

    HangmanServer server = new HangmanServer();
    server.serve();
  }

  private void startClientHandler(Socket client) throws SocketException {
    System.out.println("Got a connection!!!");

    client.setSoLinger(true, LINGER_TIME); // Set linger time
    client.setSoTimeout(SOCKET_TIMEOUT); // Set socket timeout

    ClientHandler handler = new ClientHandler(this, client);
    synchronized (clients) {
      clients.add(handler);
    }
    Thread handlerThread = new Thread(handler);
    handlerThread.setPriority(Thread.MAX_PRIORITY);
    handlerThread.start();

    System.out.printf("There are now %d clients connected!\n", clients.size());
  }

  void removeHandler(ClientHandler client) {
    clients.remove(client);
  }

  private void serve() {
    try (ServerSocket socket = new ServerSocket(PORT)) {
      // Continuously listen for incoming client connections
      while (true) {
        System.out.println("Listening...");
        Socket client = socket.accept(); // Blocking
        startClientHandler(client);
      }
    } catch (IOException e) {
      System.err.println("Server failed...");
      e.printStackTrace();
    }
  }
}
