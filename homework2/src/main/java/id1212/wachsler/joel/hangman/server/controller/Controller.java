package id1212.wachsler.joel.hangman.server.controller;

import id1212.wachsler.joel.hangman.server.hangman.Game;
import id1212.wachsler.joel.hangman.server.word.WordList;

/**
 * Controller for the server.
 */
public class Controller {
  private Game game;

  /**
   * @see Game#Constructor()
   */
  public void newHangmanGame() {
    game = new Game(this);
  }

  /**
   * @see Game#newGameInstance()
   */
  public void startNewGameInstance() throws IllegalAccessException {
    game.newGameInstance();
  }

  /**
   * @see Game#guess(String)
   */
  public String guess(String guess) {
    return game.guess(guess);
  }

  /**
   * @see WordList#getRandomWord()
   */
  public String randomWord() {
    return WordList.getRandomWord();
  }
}
