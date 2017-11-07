package id1212.wachsler.joel.hangman.server.controller;

import id1212.wachsler.joel.hangman.server.game.HangmanGame;

public class Controller {
  private static String wordPath;
  private HangmanGame game;

  public void setWordPath(String wordPath) {
    Controller.wordPath = wordPath;
  }

  public void newHangmanGame() {
    game = new HangmanGame();
  }

  public char[] guess(String msgBody) {
    return game.guess(msgBody);
  }

  public int getTries() {
    return game.getTries();
  }

  public int getScore() {
    return game.getScore();
  }
}
