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
        CmdLine cmdLine = new CmdLine(readNextLine());

        switch (cmdLine.getCmd()) {
          case QUIT:
            receivingCmds = false;
            controller.disconnect();
            break;
          case CONNECT:
            controller.connect("127.0.0.1", 8080, new ConsoleOutput());
            break;
          case GUESS:
            controller.guess(cmdLine.getArg(0));
            break;
        }
      } catch (Exception e) {
        outMsg.println("Operation failed!");
        e.printStackTrace();
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
