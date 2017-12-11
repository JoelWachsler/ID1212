package id1212.wachsler.joel.homework5.server.game;

import id1212.wachsler.joel.homework5.server.common.GameState;
import id1212.wachsler.joel.homework5.server.controller.Controller;

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
  public void newGameInstance() throws IllegalStateException {
    if (gameInstance != null && gameInstance.valid())
      throw new IllegalStateException("There is already a valid game running!");

    gameInstance = new GameInstance(controller.randomWord());
  }

  /**
   * Calls the corresponding game instance guess and tries the guess against the chosen word.
   *
   * @param msgToGuess The <code>String</code> to guess.
   * @return The current state of the game as a <code>GameState</code> object.
   */
  public GameState guess(String msgToGuess) {
    if (gameInstance == null)
      throw new IllegalStateException("There is no game instance, start a new instance in order to play!");

    gameInstance.guess(msgToGuess);
    if (gameInstance.correctGuess()) score++;
    else if (!gameInstance.valid()) score--;

    return gameStateFactory();
  }

  private GameState gameStateFactory() {
    return new GameState(score, gameInstance.getTries(), gameInstance.getTotalTries(), gameInstance.getWordGuess());
  }
}
