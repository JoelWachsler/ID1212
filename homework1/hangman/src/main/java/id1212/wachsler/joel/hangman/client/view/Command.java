package id1212.wachsler.joel.hangman.client.view;

/**
 * Defines all commands that can be performed by a user of the chat application.
 */
public enum Command {
  /**
   * Guess a letter or the whole word
   */
  GUESS,
  /**
   * Establish a connection to the server. The first parameter is IP address (or host name), the second is port number.
   */
  CONNECT,
  /**
   * Leave the application.
   */
  QUIT,
}
