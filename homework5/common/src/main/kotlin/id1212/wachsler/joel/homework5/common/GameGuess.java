package id1212.wachsler.joel.homework5.common;

import java.io.Serializable;

public class GameGuess implements Serializable {
  private String guess;

  public GameGuess(String guess) {
    this.guess = guess;
  }

  public String getGuess() {
    return guess;
  }
}
