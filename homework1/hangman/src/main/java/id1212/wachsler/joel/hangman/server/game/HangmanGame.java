package id1212.wachsler.joel.hangman.server.game;

import java.util.Arrays;

public class HangmanGame {
  private int score = 0;
  private int tries = 7;
  private HangmanGameInstance gameInstance;

  public int getTries() {
    return tries;
  }

  public int getScore() {
    return score;
  }

  /**
   * @see HangmanGameInstance#HangmanGameInstance()
   */
  public void newGameInstance() {
    gameInstance = new HangmanGameInstance();
  }

  /**
   * @see HangmanGameInstance#guess(String)
   */
  public char[] guess(String msgBody) {
    if (tries < 1) return null;
    if (gameInstance == null) return null;
    if (gameInstance.correctWord) return null;

    char[] msg = msgBody.toCharArray();

    if (msg.length > 1)
      return gameInstance.guess(msg);

    return gameInstance.guess(msg[0]);
  }

  private class HangmanGameInstance {
    private final char[] word = WordList.getInstance().getRandomWord().toCharArray();
    private char[] wordGuess;
    private boolean correctWord;

    private HangmanGameInstance() {
      wordGuess = new char[word.length];
      for (int i = 0; i < wordGuess.length; i++) {
        wordGuess[i] = '_';
      }
    }

    char[] guess(char guess) {
      boolean correct = false;
      correctWord = true;

      for (int i = 0; i < word.length; i++) {
        if (word[i] == guess) {
          correct = true;
          wordGuess[i] = guess;
        }

        if (wordGuess[i] == '_') correctWord = false;
      }

      if (!correct) tries--;
      if (correctWord) score++;

      return wordGuess;
    }

    char[] guess(char[] guess) {
      if (!Arrays.equals(word, guess)) {
        tries--;
        return wordGuess;
      }

      score++;
      correctWord = true;
      return word;
    }
  }
}
