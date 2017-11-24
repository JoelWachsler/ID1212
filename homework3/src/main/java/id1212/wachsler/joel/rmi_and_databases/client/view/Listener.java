package id1212.wachsler.joel.rmi_and_databases.client.view;

/**
 * Message handling.
 */
public interface Listener {
  /**
   * @param msg <code>String</code> to print.
   */
  void print(String msg);

  /**
   * @param msg <code>String</code> with a custom error message.
   * @param e   <code>Exception</code> which contains information of what went wrong.
   */
  void error(String msg, Exception e);

  /**
   * Information about a disconnection.
   */
  void disconnect();
}

