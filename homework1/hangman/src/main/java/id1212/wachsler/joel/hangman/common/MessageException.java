package id1212.wachsler.joel.hangman.common;

/**
 * Exception throwing when the message couldn't be received
 */
public class MessageException extends RuntimeException {
  public MessageException(String msg) {
    super(msg);
  }
}
