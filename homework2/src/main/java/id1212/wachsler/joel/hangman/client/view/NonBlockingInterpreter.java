package id1212.wachsler.joel.hangman.client.view;

import id1212.wachsler.joel.hangman.client.controller.Controller;
import id1212.wachsler.joel.hangman.client.net.Listener;

import java.util.Scanner;

public class NonBlockingInterpreter implements Runnable {
  private static final String PROMPT = "> ";
  private boolean receivingCmds = false;
  private Controller controller;
  private Console console = new Console();

  /**
   * Starts the interpreter
   */
  public void start() {
    if (receivingCmds) return;

    receivingCmds = true;
    controller = new Controller();
    controller.addServerConnectionListener(console);

    new Thread(this).start();
  }

  @Override
  public void run() {
    while (receivingCmds) {
      try {
        CmdLineParser cmdLine;
        try {
          cmdLine = new CmdLineParser(console.readNextLine());
        } catch (InvalidCommandException e) {
          // Catching the exception here in order to not close the connection to the server
          // if the user is entering an invalid command.
          console.error(e.getMessage(), e);
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
            controller.connect("127.0.0.1", 8080);
            break;
          case START:
            controller.startGame();
            break;
          case GUESS:
            try {
              controller.guess(cmdLine.getArg(0));
            } catch (IndexOutOfBoundsException e) {
              console.error("Invalid use of guess!\n" +
                "The correct way is: \"guess <char|string>\"", e);
            }
            break;
        }
      } catch (Exception e) {
        if (receivingCmds) console.error(e.getMessage(), e);
      }
    }
  }

  private class Console implements Listener {
    private final ThreadSafeStdOut outMsg = new ThreadSafeStdOut();
    private final Scanner console = new Scanner(System.in);

    @Override
    public void print(String msg) {
      outMsg.println(msg);
      outMsg.print(PROMPT);
    }

    @Override
    public void error(String error, Exception e) {
      outMsg.println("ERROR:");
      outMsg.println(error);
    }

    @Override
    public void disconnect() {
      print("You are now disconnected!");
    }

    String readNextLine() {
      outMsg.print(PROMPT);

      return console.nextLine();
    }
  }
}
