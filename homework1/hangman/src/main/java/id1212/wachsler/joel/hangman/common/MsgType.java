package id1212.wachsler.joel.hangman.common;

/**
 * Defines different message types
 */
public enum MsgType {
  /**
   * Make a guess on the word
   */
  GUESS,
  /**
   * A new entry in a conversation
   */
  ENTRY,
  /**
   * The server wants to broadcast a message to all clients
   */
  BROADCAST,
  /**
   * The client is about to disconnect
   */
  DISCONNECT
}
