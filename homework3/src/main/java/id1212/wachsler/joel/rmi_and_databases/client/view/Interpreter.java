package id1212.wachsler.joel.rmi_and_databases.client.view;

import id1212.wachsler.joel.rmi_and_databases.client.controller.Controller;
import id1212.wachsler.joel.rmi_and_databases.common.*;
import id1212.wachsler.joel.rmi_and_databases.common.dto.CredentialDTO;
import id1212.wachsler.joel.rmi_and_databases.common.dto.FileInfoDTO;
import id1212.wachsler.joel.rmi_and_databases.common.exceptions.RegisterException;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class Interpreter implements Runnable {
  private FileServer server;
  private boolean running = false;
  private Console console;
  private CmdLineParser parser;
  private Controller controller = new Controller();

  /**
   * Starts a new interpreter on a separate thread.
   *
   * @param server The server registry to communicate with.
   */
  public void start(FileServer server) throws RemoteException {
    this.server = server;
    if (running) return;

    running = true;
    console = new Console();
    new Thread(this).start();
  }

  /**
   * Main interpreter loop on a separate thread.
   * Waits for user input and then evaluates the command accordingly.
   */
  @Override
  public void run() {
    while (running) {
      try {
        parser = new CmdLineParser(console.readNextLine());
      } catch (InvalidCommandException e) {
        console.error(e.getMessage(), e);
        continue;
      } catch (RemoteException e) {
        console.error(e.getMessage(), e);
      }

      try {
        switch (parser.getCmd()) {
          case LOGIN:     login();    break;
          case REGISTER:  register(); break;
          case LIST:      list();     break;
          case UPLOAD:    upload();   break;
          case QUIT:
            controller.endConnection();
            console.disconnect();
            running = false;
            break;
          default:
            InvalidCommandException e = new InvalidCommandException("Invalid command!");
            console.error(e.getMessage(), e);
        }
      } catch (RemoteException e) {
        console.error(e.getMessage(), e);
      } catch (IOException e) {
        console.error("Failed to disconnect!", e);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
  }

  private void upload() {
    try {
      String file = parser.getArg(0);
      String filename = parser.getArg(1);
      controller.upload(file, filename);
      console.print("File uploaded!");
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InvalidCommandException e) {
      console.error(
        "Invalid use of the upload command!\n" +
          "the correct way is:\n" +
          "upload <local filename> <upload filename>", e);
    } catch (Exception e) {
      console.error(e.getMessage(), e);
    }
  }

  private void list() {
    try {
      for (FileInfoDTO file : server.list(controller.getUserId()))
        console.print(file.toString());
    } catch (RemoteException | IllegalAccessException e) {
      console.error(e.getMessage(), e);
    }
  }

  private void register() throws RemoteException {
    try {
      CredentialDTO credentialDTO = createCredentials(parser);
      long userId = server.register(console, credentialDTO);
      controller.authenticated(userId);
      console.print("You're now registered and your id is: " + userId);
    } catch (InvalidCommandException e) {
      console.error(
        "Invalid use of the register command!\n" +
          "the correct way is:\n" +
          "register <username> <password>", e);
    } catch (RegisterException | IOException e) {
      console.error(e.getMessage(), e);
    }
  }

  private void login() throws RemoteException {
    try {
      long userId = server.login(console, createCredentials(parser));
      controller.authenticated(userId);
      console.print("You're now logged in! Your id is: " + userId);
    } catch (InvalidCommandException e) {
      console.error(
        "Invalid use of the login command!\n" +
          "The correct way is:\n" +
          "login <username> <password>", e);
    } catch (Exception e) {
      e.printStackTrace();
      console.error(e.getMessage(), e);
    }
  }

  private CredentialDTO createCredentials(CmdLineParser parser) throws InvalidCommandException {
    String username = parser.getArg(0);
    String password = parser.getArg(1);
    return new CredentialDTO(username, password);
  }

  public class Console extends UnicastRemoteObject implements Listener {
    private static final String PROMPT = "> ";
    private final ThreadSafeStdOut outMsg = new ThreadSafeStdOut();
    private final Scanner console = new Scanner(System.in);

    Console() throws RemoteException {
    }

    @Override
    public void print(String msg) throws RemoteException {
      outMsg.println(msg);
    }

    @Override
    public void error(String error, Exception e) {
      outMsg.println("ERROR:");
      outMsg.println(error);
    }

    @Override
    public void disconnect() throws RemoteException {
      print("You are now disconnected!");
    }

    String readNextLine() throws RemoteException {
      outMsg.print(PROMPT);

      return console.nextLine();
    }
  }
}
