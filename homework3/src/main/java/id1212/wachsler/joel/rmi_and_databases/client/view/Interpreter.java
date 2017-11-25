package id1212.wachsler.joel.rmi_and_databases.client.view;

import id1212.wachsler.joel.rmi_and_databases.common.Credentials;
import id1212.wachsler.joel.rmi_and_databases.common.FileServer;

import javax.security.auth.login.LoginException;
import java.rmi.RemoteException;
import java.sql.SQLException;
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
    long userId;

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
            try {
              String username = parser.getArg(0);
              String password = parser.getArg(1);
              Credentials credentials = new Credentials(username, password);
              userId = server.login(credentials);
              console.print("We're now logged in! The id is: " + userId);
            } catch (InvalidCommandException e) {
              console.error(
                "Invalid use of the login command!\n" +
                "The correct way is:\n" +
                "login <username> <password>", e);
            } catch (LoginException e) {
              console.error(e.getMessage(), e);
            }
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
