package id1212.wachsler.joel.hangman.server.controller;

import id1212.wachsler.joel.hangman.server.game.HangmanGame;
import id1212.wachsler.joel.hangman.server.word.WordList;

/**
 * Controller for the server.
 */
public class Controller {
  private HangmanGame game;

  /**
   * @see HangmanGame#Constructor()
   */
  public void newHangmanGame() {
    game = new HangmanGame(this);
  }

  /**
   * @see HangmanGame#newGameInstance()
   */
  public void startNewGameInstance() {
    game.newGameInstance();
  }

  /**
   * @see HangmanGame#guess(String)
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
