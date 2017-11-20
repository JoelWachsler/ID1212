package id1212.wachsler.joel.hangman.common;

public class Constants {
  /**
   * Message length header delimiter. Used to tell the stream how many bytes the message is.
   */
  public static final String MSG_LEN_DELIMITER = "###";
  /**
   * The maximum length of a message, used in order to avoid buffer resizing on the fly.
   */
  public static final int MSG_MAX_LEN = 8192;
  /**
   * Delimiter between the message body and type.
   */
  public static final String MSG_TYPE_DELIMITER = "##";
  /**
   * Index of the message body in the message.
   */
  public static final int MSG_BODY_INDEX = 1;
  /**
   * Index of the message type in the message.
   */
  public static final int MSG_TYPE_INDEX = 0;
}
