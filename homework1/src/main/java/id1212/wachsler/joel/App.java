package id1212.wachsler.joel;

/**
 * Hangman ( https://en.wikipedia.org/wiki/Hangman_(game) )
 *
 * Client and server must communicate by sending messages over a TCP connection, using blocking TCP sockets.
 * The client must not store any data. All data entered by the user must be sent to the server for processing,
 * and all data displayed to the user must be received from the server.
 *
 * The client must have a responsive user interface, which means it must be multithreaded. The user must be able to
 * give commands, for example to quit the program, even if the client is waiting for a message from the server.
 *
 * The server must be able to handle multiple clients playing concurrently, which means it must be multithreaded.
 *
 * The user interface must be informative. The current state of the program must be clear to the user,
 * and the user must understand what to do next.
 */
public class App {
  public static void main(String[] args) {

  }
}