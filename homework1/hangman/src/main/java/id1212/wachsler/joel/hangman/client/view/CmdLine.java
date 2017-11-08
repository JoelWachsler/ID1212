package id1212.wachsler.joel.hangman.client.view;

import java.util.Arrays;

/**
 * Parse commands from strings
 */
class CmdLine {
  private static final String PARAM_DELIMITER = " ";
  private String[] params;
  private Command cmd;

  CmdLine(String enteredLine) throws InvalidCommandException {
    parseCommands(enteredLine);
    parseArgs(enteredLine);
  }

  private void parseArgs(String enteredLine) {
    if (enteredLine == null) {
      params = null;
      return;
    }

    params = enteredLine.split(PARAM_DELIMITER);
    // Remove the command
    params = Arrays.copyOfRange(params, 1, params.length);
  }

  private void parseCommands(String enteredLine) throws InvalidCommandException {
    if (enteredLine == null) return;

    String[] splitText = enteredLine.split(PARAM_DELIMITER);

    switch (splitText[0].toUpperCase()) {
      case "GUESS":   cmd = Command.GUESS;    break;
      case "START":   cmd = Command.START;    break;
      case "CONNECT": cmd = Command.CONNECT;  break;
      case "QUIT":    cmd = Command.QUIT;     break;
      default:
        throw new InvalidCommandException("\"" + splitText[0].toLowerCase() + "\" is not a valid command!");
    }
  }

  /**
   * @return The current command
   */
  Command getCmd() {
    return cmd;
  }

  /**
   * Retrieves the argument at the specified index
   *
   * @param i The index of the argument
   * @return The argument or null if it doesn't exist
   */
  String getArg(int i) {
    if (params == null) return null;
    if (params.length < i) return null;

    return params[i];
  }
}
