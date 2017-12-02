package id1212.wachsler.joel.rmi_and_databases.client.view;

/**
 * Exception for invalid usage of commands.
 */
class InvalidCommandUsageException extends Exception {
  InvalidCommandUsageException(String message) {
    super(message);
  }
}
