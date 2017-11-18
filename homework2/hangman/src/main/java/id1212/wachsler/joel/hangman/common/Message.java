package id1212.wachsler.joel.hangman.common;

import java.io.Serializable;

/**
 * Message for communicating between the client and server
 */
public class Message implements Serializable {
  private final MsgType type;
  private final String body;

  public Message(MsgType type, String body) {
    this.type = type;
    this.body = body;
  }

  /**
   * Get the message type.
   *
   * @return The type.
   */
  public MsgType getType() {
    return type;
  }

  /**
   * Get the body.
   *
   * @return Get the message body.
   */
  public String getBody() {
    return body;
  }
}
