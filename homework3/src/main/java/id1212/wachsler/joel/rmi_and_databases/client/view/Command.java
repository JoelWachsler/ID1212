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
   * Downloads the specified file.
   */
  DOWNLOAD,
  /**
   * Uploads the specified file.
   */
  UPLOAD,
  /**
   * Leave the application.
   */
  QUIT,
  /**
   * Deletes a file.
   */
  DELETE,
  /**
   * Unregister the user.
   */
  UNREGISTER,
  /**
   * Notifies the user when a public file is read or written to.
   */
  NOTIFY,
  /**
   * Prints the last stacktrace
   */
  TRACE,
}
