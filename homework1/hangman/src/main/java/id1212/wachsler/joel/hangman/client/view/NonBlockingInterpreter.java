package id1212.wachsler.joel.hangman.client.view;

import id1212.wachsler.joel.hangman.client.controller.Controller;
import id1212.wachsler.joel.hangman.client.net.OutputHandler;

import java.util.Scanner;

public class NonBlockingInterpreter implements Runnable {
  private static final String PROMPT = "> ";
  private final Scanner console = new Scanner(System.in);
  private boolean receivingCmds = false;
  private Controller controller;
  private final ThreadSafeStdOut outMsg = new ThreadSafeStdOut();

  /**
   * Starts the interpreter
   */
  public void start() {
    if (receivingCmds) return;

    receivingCmds = true;
    controller = new Controller();

    new Thread(this).start();
  }

  @Override
  public void run() {
    while (receivingCmds) {
      try {
        CmdLineParser cmdLine;
        try {
          cmdLine = new CmdLineParser(readNextLine());
        } catch (InvalidCommandException e) {
          // Catching the exception here in order to not close the connection to the server
          // if the user is entering an invalid command.
          outMsg.println(e.getMessage());
          continue;
        }

        if (!controller.isConnected() && cmdLine.getCmd() != Command.CONNECT) {
          throw new IllegalStateException("You are not connected to the server.\nRun the \"connect\" command to connect.");
        }

        switch (cmdLine.getCmd()) {
          case QUIT:
            receivingCmds = false;
            controller.disconnect();
            break;
          case CONNECT:
            controller.connect("127.0.0.1", 8080, new ConsoleOutput());
            break;
          case START:
            controller.startGame();
            break;
          case GUESS:
            try {
              controller.guess(cmdLine.getArg(0));
            } catch (IndexOutOfBoundsException e) {
              outMsg.println("Invalid use of guess!\n" +
                "The correct way is: \"guess <char|string>\"");
            }
            break;
        }
      } catch (Exception e) {
        if (receivingCmds) outMsg.println(e.getMessage());
      }
    }
  }

  private String readNextLine() {
    outMsg.print(PROMPT);
    return console.nextLine();
  }

  private class ConsoleOutput implements OutputHandler {
    @Override
    public void handleMsg(String msg) {
      outMsg.println(msg);
      outMsg.print(PROMPT);
    }
  }
}
