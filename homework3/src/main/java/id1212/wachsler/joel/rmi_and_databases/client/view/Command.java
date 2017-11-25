package id1212.wachsler.joel.rmi_and_databases.client.view;

/**
 * Defines all commands that can be performed by a user.
 */
public enum Command {
  /**
   * Login.
   */
  LOGIN,
  /**
   * Register.
   */
  REGISTER,
  /**
   * Lists all files in the directory
   */
  LIST,
  /**
   * Leave the application.
   */
  QUIT,
}
