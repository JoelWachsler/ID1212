package id1212.wachsler.joel.rmi_and_databases.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Message handling.
 */
public interface Listener extends Remote {
  /**
   * @param msg <code>String</code> to print.
   */
  void print(String msg) throws RemoteException;

  /**
   * @param msg <code>String</code> with a custom error message.
   * @param e   <code>Exception</code> which contains information of what went wrong.
   */
  void error(String msg, Exception e) throws RemoteException;
}

