package id1212.wachsler.joel.hangman.server.game;

import id1212.wachsler.joel.hangman.server.controller.Controller;
import id1212.wachsler.joel.hangman.server.word.WordList;

import java.util.Arrays;
import java.util.HashSet;
import java.util.StringJoiner;

/**
 * Represents the Hangman game and its state.
 */
public class HangmanGame {
  private int score = 0;
  private int totalTries;
  private int tries;
  private HangmanGameInstance gameInstance;
  private Controller controller;

  public HangmanGame(Controller controller) {
    this.controller = controller;
  }

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
    if (gameInstance == null) return "There is no game instance, start a new instance in order to play!";
    if (tries > totalTries) return "There are no tries left! You have to disconnect and connect again to start a new one!";
    if (gameInstance.correctWord)
      return "You have already guessed the correct word for this game instance.\n" +
      "Start a new game instance in order to guess again!";
    if (gameInstance.wordAlreadyTried(guess)) return "You have already tried that char/word...";

    char[] msg = guess.toLowerCase().toCharArray();
    char[] guessResult;

    if (msg.length > 1) {
      guessResult = gameInstance.guess(msg);
    } else {
      guessResult = gameInstance.guess(msg[0]);
    }

    StringJoiner response = new StringJoiner(", ");

    response.add("Word: " + new String(guessResult));
    response.add("attempts: " + tries + "/" + totalTries);
    response.add("score: " + score);

    return response.toString();
  }

  private class HangmanGameInstance {
    private final char[] word = controller.randomWord().toCharArray();
    private char[] wordGuess;
    private boolean correctWord;
    private HashSet<String> guesses = new HashSet<>();

    private HangmanGameInstance() {
      System.out.println("A new game was started and the word is: " + new String(word));

      wordGuess = new char[word.length];

      for (int i = 0; i < wordGuess.length; i++) {
        wordGuess[i] = '_';
      }

      tries = 0;
      totalTries = word.length;
    }

    /**
     * Make a guess for the Hangman game instance.
     *
     * @param guess The guess.
     * @return The word where the chars are on the correct place.
     */
    char[] guess(char guess) {
      guesses.add(String.valueOf(guess));

      boolean correct = false;
      correctWord = true;

      for (int i = 0; i < word.length; i++) {
        if (word[i] == guess) {
          correct = true;
          wordGuess[i] = guess;
        }

        if (wordGuess[i] == '_') correctWord = false;
      }

      if (!correct) tries++;
      if (correctWord) score++;

      return wordGuess;
    }

    /**
     * Make a guess for the whole word.
     *
     * @param guess The guessed word.
     * @return The correct word if the guess was correct else the previous guessed word.
     */
    char[] guess(char[] guess) {
      guesses.add(String.valueOf(guess));

      if (!Arrays.equals(word, guess)) {
        tries++;
        return wordGuess;
      }

      score++;
      correctWord = true;
      return word;
    }

    boolean wordAlreadyTried(String guess) {
      return guesses.contains(guess);
    }
  }
}
