package id1212.wachsler.joel.rmi_and_databases.client.view;

import id1212.wachsler.joel.rmi_and_databases.common.Credentials;
import id1212.wachsler.joel.rmi_and_databases.common.FileServer;

import java.rmi.RemoteException;
import java.util.Scanner;

public class Interpreter implements Runnable {
  private FileServer server;
  private boolean running = false;
  private Console console = new Console();

  public void start(FileServer server) {
    this.server = server;
    if (running) return;

    running = true;
    new Thread(this).start();
  }

  @Override
  public void run() {
    CmdLineParser parser;
    while (running) {
      try {
        parser = new CmdLineParser(console.readNextLine());
      } catch (InvalidCommandException e) {
        console.error(e.getMessage(), e);
        continue;
      }

      try {
        switch (parser.getCmd()) {
          case LOGIN:
            Credentials credentials = new Credentials();
            server.login(credentials);
            break;
          case QUIT:
            break;
        }
      } catch (RemoteException e) {
        console.error(e.getMessage(), e);
      }
    }
  }

  private class Console implements Listener {
    private static final String PROMPT = "> ";
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
