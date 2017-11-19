package id1212.wachsler.joel.hangman.common;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Thread-safe handling of multiple or partial messages.
 */
public class MessageParser {
  private StringBuilder receivedChars = new StringBuilder();
  private final Queue<String> messages = new ArrayDeque<>();

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
  public synchronized String nextMsg() {
    return messages.poll();
  }

  /**
   * @return <code>true</code> if there is another message.
   *         <code>false</code> if there isn't.
   */
  public synchronized boolean hasNextMsg() {
    return !messages.isEmpty();
  }

  private boolean extractMessage() {
    String currentReceivedChars = this.receivedChars.toString();
    String[] splitAtHeaderLen = currentReceivedChars.split(Constants.MSG_LEN_DELIMITER);

    if (splitAtHeaderLen.length < 2) return false; // Only parts of the message has been received

    int msgLen = Integer.parseInt(splitAtHeaderLen[0]);
    if (!isMessageComplete(msgLen, splitAtHeaderLen[1])) return false; // All bytes for this message weren't received

    String msg = splitAtHeaderLen[1].substring(0, msgLen);
    messages.add(msg); // Add to complete messages

    // TODO: Check if this works!
    receivedChars.delete(0, currentReceivedChars.length()); // Remove the extracted message

    return true;
  }

  private boolean isMessageComplete(int msgLen, String msg) {
    return msg.length() >= msgLen;
  }
}
