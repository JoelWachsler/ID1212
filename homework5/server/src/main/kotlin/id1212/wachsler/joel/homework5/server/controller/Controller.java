package id1212.wachsler.joel.homework5.server.controller;

import id1212.wachsler.joel.homework5.common.GameState;
import id1212.wachsler.joel.homework5.server.game.Game;
import id1212.wachsler.joel.homework5.server.word.WordList;

/**
 * Controller for the server.
 */
public class Controller {
  private Game game;

  /**
   * @see Game#Constructor()
   */
  public GameState newHangmanGame() {
    game = new Game(this);

    return game.newGameInstance();
  }

  /**
   * @see Game#guess(String)
   */
  public GameState guess(String guess) {
    return game.guess(guess);
  }

  /**
   * @see WordList#getRandomWord()
   */
  public String randomWord() {
    return WordList.getRandomWord();
  }
}
