package id1212.wachsler.joel.rmi_and_databases.client.view;

import id1212.wachsler.joel.rmi_and_databases.common.CredentialDTO;
import id1212.wachsler.joel.rmi_and_databases.common.FileServer;
import id1212.wachsler.joel.rmi_and_databases.common.RegisterException;

import javax.security.auth.login.LoginException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Interpreter implements Runnable {
  private FileServer server;
  private boolean running = false;
  private Console console = new Console();
  private long userId;
  CmdLineParser parser;


  public void start(FileServer server) {
    this.server = server;
    if (running) return;

    running = true;
    new Thread(this).start();
  }

  @Override
  public void run() {
    while (running) {
      try {
        parser = new CmdLineParser(console.readNextLine());
      } catch (InvalidCommandException e) {
        console.error(e.getMessage(), e);
        continue;
      }

      try {
        switch (parser.getCmd()) {
          case LOGIN: login(); break;
          case REGISTER: register(); break;
          case QUIT:
            console.disconnect();
            break;
        }
      } catch (RemoteException e) {
        console.error(e.getMessage(), e);
      }
    }
  }

  private void register() throws RemoteException {
    try {
      CredentialDTO credentialDTO = createCredentials(parser);
      userId = server.register(credentialDTO);
      console.print("You're now registered and your id is: " + userId);
    } catch (InvalidCommandException e) {
      console.error(
        "Invalid use of the register command!\n" +
          "The correct way is:\n" +
          "register <username> <password>", e);
    } catch (RegisterException e) {
      console.error(e.getMessage(), e);
    }
  }

  private void login() throws RemoteException {
    try {
      CredentialDTO credentialDTO = createCredentials(parser);
      userId = server.login(credentialDTO);
      console.print("You're now logged in! The id is: " + userId);
    } catch (InvalidCommandException e) {
      console.error(
        "Invalid use of the login command!\n" +
          "The correct way is:\n" +
          "login <username> <password>", e);
    } catch (LoginException e) {
      console.error(e.getMessage(), e);
    }
  }

  private CredentialDTO createCredentials(CmdLineParser parser) throws InvalidCommandException {
    String username = parser.getArg(0);
    String password = parser.getArg(1);
    return new CredentialDTO(username, password);
  }

  private class Console implements Listener {
    private static final String PROMPT = "> ";
    private final ThreadSafeStdOut outMsg = new ThreadSafeStdOut();
    private final Scanner console = new Scanner(System.in);

    @Override
    public void print(String msg) {
      outMsg.println(msg);
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
