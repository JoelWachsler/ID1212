package id1212.wachsler.joel.hangman.common;

/**
 * Defines different message types
 */
public enum MsgType {
  /**
   * Starts a new game
   */
  START,
  /**
   * The result of a guess which sends Word, Remaining failed attempts and Score
   */
  GUESS_RESPONSE,
  /**
   * Make a guess on the word
   */
  GUESS,
  /**
   * The server wants to broadcast a message to all clients
   */
  BROADCAST,
  /**
   * The client is about to disconnect
   */
  DISCONNECT
}
