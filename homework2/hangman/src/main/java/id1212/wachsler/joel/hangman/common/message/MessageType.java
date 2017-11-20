package id1212.wachsler.joel.hangman.common.message;

/**
 * Defines different message types.
 */
public enum MessageType {
  /**
   * Starts a new game.
   */
  START,
  /**
   * The server response for a client game request.
   */
  GAME_RESPONSE,
  /**
   * A client guess of a hangman word.
   */
  GUESS,
  /**
   * The client is about to disconnect.
   */
  DISCONNECT
}
