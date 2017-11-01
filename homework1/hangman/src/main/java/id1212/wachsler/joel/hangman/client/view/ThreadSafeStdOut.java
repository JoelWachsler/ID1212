package id1212.wachsler.joel.hangman.client.view;

class ThreadSafeStdOut {
  /**
   * Thread safe printing
   * @param output
   */
  synchronized void print(String output) {
    System.out.print(output);
  }

  /**
   * Thread safe printing with line break
   * @param output
   */
  synchronized void println(String output) {
    System.out.println(output);
  }
}
