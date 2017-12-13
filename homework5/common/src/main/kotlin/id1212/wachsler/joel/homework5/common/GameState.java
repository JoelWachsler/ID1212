package id1212.wachsler.joel.homework5.common;

import java.io.Serializable;

public class GameState implements Serializable {
  private final int score;
  private final int tries;
  private final int totalTries;
  private final String state;

  public GameState(int score, int tries, int totalTries, String state) {
    this.score = score;
    this.tries = tries;
    this.totalTries = totalTries;
    this.state = state;
  }

  public int getScore() {
    return score;
  }

  public int getTries() {
    return tries;
  }

  public int getTotalTries() {
    return totalTries;
  }

  public String getState() {
    return state;
  }
}
