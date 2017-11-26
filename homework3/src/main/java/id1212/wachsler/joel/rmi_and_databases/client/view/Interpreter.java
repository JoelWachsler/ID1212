package id1212.wachsler.joel.rmi_and_databases.client.view;

import id1212.wachsler.joel.rmi_and_databases.common.*;
import id1212.wachsler.joel.rmi_and_databases.common.dto.CredentialDTO;
import id1212.wachsler.joel.rmi_and_databases.common.dto.FileInfoDTO;
import id1212.wachsler.joel.rmi_and_databases.common.exceptions.RegisterException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Interpreter implements Runnable {
  private FileServer server;
  private boolean running = false;
  private Console console = new Console();
  private long userId;
  private CmdLineParser parser;

  /**
   * Starts a new interpreter on a separate thread.
   *
   * @param server The server registry to communicate with.
   */
  public void start(FileServer server) {
    this.server = server;
    if (running) return;

    running = true;
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
      }

      try {
        switch (parser.getCmd()) {
          case LOGIN:     login();    break;
          case REGISTER:  register(); break;
          case LIST:      list();     break;
          case UPLOAD:    upload();   break;
          case QUIT:
            console.disconnect();
            running = false;
            break;
          default:
            InvalidCommandException e = new InvalidCommandException("Invalid command!");
            console.error(e.getMessage(), e);
        }
      } catch (RemoteException e) {
        console.error(e.getMessage(), e);
      }
    }
  }

  private void upload() {
    try {
      String file = parser.getArg(0);

      server.initUpload(file);

      FileTransferHandler handler = new FileTransferHandler(createSocketChannel());
      handler.sendFile(String.format("client_files/%s", file));
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InvalidCommandException e) {
      console.error(
        "Invalid use of the upload command!\n" +
          "the correct way is:\n" +
          "upload <filename>", e);
    }
  }

  private SocketChannel createSocketChannel() throws IOException {
    SocketChannel socketChannel = SocketChannel.open();
    console.print("Trying to connect...");
    socketChannel.connect(new InetSocketAddress(Constants.SERVER_ADDRESS, Constants.SERVER_SOCKET_PORT));
    console.print("Connected!");

    return socketChannel;
  }

  private void list() {
    try {
      for (FileInfoDTO file : server.list(userId))
        console.print(file.toString());
    } catch (RemoteException | IllegalAccessException e) {
      console.error(e.getMessage(), e);
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
          "the correct way is:\n" +
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
