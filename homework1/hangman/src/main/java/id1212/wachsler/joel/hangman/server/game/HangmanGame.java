package id1212.wachsler.joel.hangman.server.game;

import id1212.wachsler.joel.hangman.server.filehandling.WordList;

import java.util.Arrays;
import java.util.StringJoiner;

public class HangmanGame {
  private int score = 0;
  private final int totalTries = 7;
  private int tries = totalTries;
  private HangmanGameInstance gameInstance;

  /**
   * @see HangmanGameInstance#HangmanGameInstance()
   */
  public void newGameInstance() {
    gameInstance = new HangmanGameInstance();
  }

  /**
   * Calls the corresponding game instance guess and tries the guess against the chosen word.
   *
   * @param guess The string to guess.
   * @return A response message to be passed to the client or null if something is invalid.
   */
  public String guess(String guess) {
    if (tries < 1) return "There are no tries left!";
    if (gameInstance == null) return "There is no game instance, start a new instance in order to play!";
    if (gameInstance.correctWord)
      return "You have already guessed the correct word for this game instance.\n" +
      "Start a new game instance in order to guess again!";

    char[] msg = guess.toCharArray();
    char[] guessResult;

    if (msg.length > 1) {
      guessResult = gameInstance.guess(msg);
    } else {
      guessResult = gameInstance.guess(msg[0]);
    }

    StringJoiner response = new StringJoiner(", ");

    response.add("Word: " + new String(guessResult));
    response.add("attempts: " + (totalTries - tries) + "/" + totalTries);
    response.add("score: " + score);

    return response.toString();
  }

  private class HangmanGameInstance {
    private final char[] word = WordList.getRandomWord().toCharArray();
    private char[] wordGuess;
    private boolean correctWord;

    private HangmanGameInstance() {
      wordGuess = new char[word.length];
      for (int i = 0; i < wordGuess.length; i++) {
        wordGuess[i] = '_';
      }
    }

    /**
     * Make a guess for the Hangman game instance
     *
     * @param guess The guess
     * @return The word where the chars are on the correct place
     */
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

    /**
     * Make a guess for the whole word
     *
     * @param guess The guessed word.
     * @return The correct word if correct or the previous guessed word if incorrect.
     */
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
