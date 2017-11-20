package id1212.wachsler.joel.hangman.common.message;

import id1212.wachsler.joel.hangman.common.Constants;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Thread-safe handling of multiple or partial messages.
 */
public class MessageParser {
  private StringBuilder receivedChars = new StringBuilder();
  private final Queue<Message> messages = new ArrayDeque<>();

  /**
   * Append the new message to previously received messages.
   *
   * @param receivedMsg The new message to append.
   */
  public synchronized void addMessage(String receivedMsg) {
    receivedChars.append(receivedMsg);
    while (extractMessage());
  }

  /**
   * @return <code>String</code> if there is another message.
   *         <code>null</code> if there is no other message.
   */
  public synchronized Message nextMsg() {
    return messages.poll();
  }

  /**
   * @return <code>true</code> if there is another message.
   *         <code>false</code> if there isn't.
   */
  public synchronized boolean hasNext() {
    return !messages.isEmpty();
  }

  private boolean extractMessage() {
    String currentReceivedChars = receivedChars.toString();
    String[] splitAtHeaderLen = currentReceivedChars.split(Constants.MSG_LEN_DELIMITER);

    if (splitAtHeaderLen.length < 2) return false; // Only parts of the message has been received

    int msgLen = Integer.parseInt(splitAtHeaderLen[0]);
    if (!isMessageComplete(msgLen, splitAtHeaderLen[1])) return false; // All bytes for this message weren't received

    String msg = splitAtHeaderLen[1].substring(0, msgLen);
    messages.add(new Message(msg)); // Add to complete messages

    receivedChars.delete(0, currentReceivedChars.length()); // Remove the extracted message

    return true;
  }

  private boolean isMessageComplete(int msgLen, String msg) {
    return msg.length() >= msgLen;
  }
}
