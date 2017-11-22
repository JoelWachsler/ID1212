package id1212.wachsler.joel.hangman.common.message;

import id1212.wachsler.joel.hangman.common.Constants;

/**
 * Parses and encapsulates a message from the provided string.
 * A message must contain a <code>MessageType</code> and a message body.
 */
public class Message {
  private String[] splitMsg;

  Message(String unparsedMsg) {
    parse(unparsedMsg);
  }

  private void parse(String unparsedMsg) {
    try {
      splitMsg = unparsedMsg.split(Constants.MSG_TYPE_DELIMITER);
    } catch (Exception e) {
      System.err.println("Unable to parse the following message: " + unparsedMsg);
    }
  }

  public MessageType getType() {
    return MessageType.valueOf(splitMsg[Constants.MSG_TYPE_INDEX].toUpperCase());
  }

  public String getBody() {
    return splitMsg[Constants.MSG_BODY_INDEX];
  }
}
