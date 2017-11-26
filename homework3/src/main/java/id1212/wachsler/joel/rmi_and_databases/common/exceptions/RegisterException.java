package id1212.wachsler.joel.rmi_and_databases.common.exceptions;

/**
 * Exception thrown the server failed to register a user with the provided credentials.
 */
public class RegisterException extends Exception {
  public RegisterException(String message) {
    super(message);
  }
}
