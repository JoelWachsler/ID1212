package id1212.wachsler.joel.rmi_and_databases.client.view;

/**
 * Exception for invalid commands.
 */
class InvalidCommandException extends Exception {
  InvalidCommandException(String message) {
    super(message);
  }
}
