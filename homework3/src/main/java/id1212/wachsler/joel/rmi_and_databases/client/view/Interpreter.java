package id1212.wachsler.joel.rmi_and_databases.client.view;

import id1212.wachsler.joel.rmi_and_databases.common.*;
import id1212.wachsler.joel.rmi_and_databases.common.dto.CredentialDTO;
import id1212.wachsler.joel.rmi_and_databases.common.dto.FileDTO;
import id1212.wachsler.joel.rmi_and_databases.common.dto.SocketIdentifierDTO;
import id1212.wachsler.joel.rmi_and_databases.common.exceptions.RegisterException;
import id1212.wachsler.joel.rmi_and_databases.common.net.FileTransferHandler;

import javax.security.auth.login.LoginException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringJoiner;

public class Interpreter implements Runnable {
  private FileServer server;
  private boolean running = false;
  private Console console;
  private CmdLineParser parser;
  private long userId;
  private SocketChannel socket;

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
        try {
          parser = new CmdLineParser(console.readNextLine());
        } catch (InvalidCommandException e) {
          StringJoiner joiner = new StringJoiner("\n");
          joiner.add(e.getMessage());
          joiner.add("");
          joiner.add("The available commands are:");
          joiner.add("");

          for (Command cmd : Command.values())
            joiner.add(cmdUsage(cmd));

          joiner.add("");

          throw new InvalidCommandException(joiner.toString());
        }

        switch (parser.getCmd()) {
          case LOGIN:      login();      break;
          case REGISTER:   register();   break;
          case LIST:       list();       break;
          case UPLOAD:     upload();     break;
          case DOWNLOAD:   download();   break;
          case UNREGISTER: unregister(); break;
          case NOTIFY:     notifyFile(); break;
          case TRACE:      console.printTrace(); break;
          case QUIT:
            server.logout(userId);
            UnicastRemoteObject.unexportObject(console, true);
            running = false;
            console.print("You are now disconnected!");
            break;
        }
      } catch (InvalidCommandUsageException e) {
        StringJoiner errorMsg = new StringJoiner("\n");
        errorMsg.add(String.format("Invalid use of the %s command!", e.getMessage()));
        errorMsg.add("The correct way is:");
        errorMsg.add(cmdUsage(Command.valueOf(e.getMessage())));
        console.error(errorMsg.toString(), e);
      } catch (Exception e) {
        console.error(e.getMessage(), e);
      }
    }
  }

  private String cmdUsage(Command cmd) {
    switch (cmd) {
      case LOGIN:     return "login <username:string> <password:string>";
      case REGISTER:  return "register <username:string> <password:string>";
      case LIST:      return "list";
      case DOWNLOAD:  return "download <server filename:string>";
      case UPLOAD:    return "upload <local filename:string> <upload filename:string> " +
                             "<public:boolean> <read:boolean> <write:boolean>";
      case QUIT:      return "quit";
      case UNREGISTER:return "unregister";
      case NOTIFY:    return "notify <remote filename:string>";
      case TRACE:     return "trace";
      default:        return "That command doesn't exist!";
    }
  }

  private void notifyFile() throws RemoteException, IllegalAccessException, InvalidCommandUsageException {
    try {
      String fileToNotifyOnUpdate = parser.getArg(0);
      server.notifyFileUpdate(userId, fileToNotifyOnUpdate);
    } catch (InvalidCommandUsageException e) {
      throw new InvalidCommandUsageException(Command.NOTIFY.toString());
    }
  }

  private void unregister() throws IOException, IllegalAccessException {
    server.unregister(userId);
    userId = 0;
  }

  private void download() throws IOException, IllegalAccessException, InvalidCommandUsageException {
    try {
      String filename = parser.getArg(0);

      FileDTO serverFileInfo = server.getFileInfo(userId, filename);
      server.download(userId, filename);

      Path savePath = Paths.get("client_files/" + filename);
      FileTransferHandler.receiveFile(socket, savePath, serverFileInfo.getSize());
      console.print("File downloaded!");
    } catch (InvalidCommandUsageException e) {
      throw new InvalidCommandUsageException(Command.DOWNLOAD.toString());
    }
  }

  private void upload() throws IOException, InvalidCommandException, IllegalAccessException, InvalidCommandUsageException {
    try {
      String localFilename = parser.getArg(0);

      Path filePath = Paths.get(String.format("client_files/%s", localFilename));

      if (!Files.exists(filePath))
        throw new FileNotFoundException(String.format("The file \"%s\" does not exist!", localFilename));

      long fileSize = Files.size(filePath);

      String serverFilename = parser.getArg(1);
      boolean publicAccess = Boolean.valueOf(parser.getArg(2));
      boolean readable = Boolean.valueOf(parser.getArg(3));
      boolean writable = Boolean.valueOf(parser.getArg(4));

      FileDTO serverFile = new FileDTO(userId, serverFilename, fileSize, publicAccess, readable, writable);

      server.upload(userId, serverFile);

      FileTransferHandler.sendFile(socket, filePath);
    } catch (InvalidCommandUsageException e) {
      throw new InvalidCommandUsageException(Command.UPLOAD.toString());
    }
  }

  private void list() throws RemoteException, IllegalAccessException {
    server.list(userId);
  }

  private void register() throws IOException, RegisterException, LoginException, InvalidCommandUsageException {
    try {
      CredentialDTO credentialDTO = createCredentials(parser);
      server.register(console, credentialDTO);
      login();
    } catch (InvalidCommandUsageException e) {
      throw new InvalidCommandUsageException(Command.REGISTER.toString());
    }
  }

  private void login() throws IOException, LoginException, InvalidCommandUsageException {
    try {
      userId = server.login(console, createCredentials(parser));

      createServerSocket(userId);
    } catch (InvalidCommandUsageException e) {
      throw new InvalidCommandUsageException(Command.LOGIN.toString());
    }
  }

  private void createServerSocket(long userId) throws IOException {
    // Create the actual socket
    socket = SocketChannel.open();
    socket.connect(new InetSocketAddress(Constants.SERVER_ADDRESS, Constants.SERVER_SOCKET_PORT));

    // Lets identify this socket with the current user to the server.
    ObjectOutputStream output = new ObjectOutputStream(socket.socket().getOutputStream());

    output.writeObject(new SocketIdentifierDTO(userId));
    output.flush();
  }

  private CredentialDTO createCredentials(CmdLineParser parser) throws InvalidCommandUsageException {
    String username = parser.getArg(0);
    String password = parser.getArg(1);

    return new CredentialDTO(username, password);
  }

  public class Console extends UnicastRemoteObject implements Listener {
    private static final String PROMPT = "> ";
    private final ThreadSafeStdOut outMsg = new ThreadSafeStdOut();
    private final Scanner console = new Scanner(System.in);
    private final Stack<Exception> exceptionList = new Stack<>();

    Console() throws RemoteException {
    }

    @Override
    public void print(String msg) throws RemoteException {
      outMsg.println("\n" + msg);
      outMsg.print(PROMPT);
    }

    @Override
    public void error(String error, Exception e) {
      exceptionList.push(e);

      outMsg.println("\nERROR:\n");
      outMsg.println(error);
    }

    String readNextLine() throws RemoteException {
      outMsg.print(PROMPT);

      return console.nextLine();
    }

    void printTrace() throws RemoteException {
      exceptionList.pop().printStackTrace();
    }
  }
}
