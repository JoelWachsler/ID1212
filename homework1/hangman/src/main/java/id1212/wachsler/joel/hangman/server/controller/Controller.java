package id1212.wachsler.joel.hangman.server.controller;

import id1212.wachsler.joel.hangman.server.game.HangmanGame;

public class Controller {
  private HangmanGame game;

  public void newHangmanGame() {
    game = new HangmanGame();
  }

  public void startNewGameInstance() {
    game.newGameInstance();
  }

  public String guess(String msgBody) {
    return game.guess(msgBody);
  }
}
