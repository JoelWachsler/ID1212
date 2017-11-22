package id1212.wachsler.joel.hangman.server.hangman;

import id1212.wachsler.joel.hangman.server.controller.Controller;

import java.util.StringJoiner;

/**
 * Represents the Hangman game and its state.
 */
public class Game {
  private int score = 0;
  private GameInstance gameInstance;
  private Controller controller;

  public Game(Controller controller) {
    this.controller = controller;
  }

  /**
   * Starts a new Game instance.
   *
   * @throws IllegalArgumentException If there already is a valid game instance in use.
   */
  public void newGameInstance() throws IllegalAccessException {
    if (gameInstance != null && gameInstance.valid())
      throw new IllegalStateException("There is already a valid game running!");

    gameInstance = new GameInstance(controller.randomWord());
  }

  /**
   * Calls the corresponding game instance guess and tries the guess against the chosen word.
   *
   * @param msgToGuess The <code>String</code> to guess.
   * @return A response message to be passed to the client or null if something is invalid.
   */
  public String guess(String msgToGuess) {
    if (gameInstance == null)
      throw new IllegalStateException("There is no game instance, start a new instance in order to play!");

    gameInstance.guess(msgToGuess);
    if (gameInstance.correctGuess()) score++;
    else if (!gameInstance.valid()) score--;

    StringJoiner response = new StringJoiner(", ");

    response.add("Word: " + gameInstance.getWordGuess());
    response.add("attempts: " + gameInstance.getTries() + "/" + gameInstance.getTotalTries());
    response.add("score: " + score);

    return response.toString();
  }
}
