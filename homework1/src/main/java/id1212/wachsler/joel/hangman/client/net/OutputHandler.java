package id1212.wachsler.joel.hangman.client.net;

/**
 * Handles broadcasts from the server
 */
public interface OutputHandler {
  /**
   * Called when a broadcast message is received from the server.
   *
   * @param msg Message from the server
   */
  void handleMsg(String msg);
}
