package id1212.wachsler.joel.rmi_and_databases.server.net;

import id1212.wachsler.joel.rmi_and_databases.common.dto.SocketIdentifierDTO;
import id1212.wachsler.joel.rmi_and_databases.server.controller.Controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.channels.SocketChannel;

/**
 * Attaches a <code>SocketChannel</code> to a user who sends their id.
 */
class ClientHandler {
  ClientHandler(Controller controller, SocketChannel socketChannel) throws IOException {
    ObjectInputStream inputStream = new ObjectInputStream(socketChannel.socket().getInputStream());

    attachToUser(controller, inputStream, socketChannel);
  }

  private void attachToUser(Controller controller, ObjectInputStream inputStream, SocketChannel socketChannel) {
    System.out.println("Waiting for the user to tell who they are...");

    try {
      SocketIdentifierDTO identifier = (SocketIdentifierDTO) inputStream.readObject();
      controller.attachSocketToUser(identifier.getUserId(), socketChannel);

      System.out.println(String.format("The userId: %d was associated with a socket!", identifier.getUserId()));
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
}
