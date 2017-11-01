package id1212.wachsler.joel.hangman.server.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Controller {
  private List<String> words;
  private Random random = new Random();

  public Controller(String wordPath) {
    try (BufferedReader reader = Files.newBufferedReader(Paths.get(wordPath))) {
      words = reader.lines().collect(Collectors.toList());
    } catch (IOException e) {
      System.err.printf("Failed to read the file \"%s\"", wordPath);
      e.printStackTrace();
    }
  }

  public String getRandomWord() {
    return words.get(random.nextInt(words.size()));
  }
}
