package com.example.tom.test;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

public class GarbageGame implements Serializable {

  private int minNumber;
  private int maxNumber;

  private int firstNumber;
  private int secondNumber;

  private int score;
  private int streak;

  public int getMinNumber() {
    return minNumber;
  }

  public  int getMaxNumber() {
    return maxNumber;
  }

  public int getFirstNumber() {
    return firstNumber;
  }
  public int getSecondNumber() {
    return secondNumber;
  }

  public int getScore() {
    return score;
  }
  public int getStreak() {
    return streak;
  }

  public GarbageGame(int minNumber, int maxNumber) {
    this.minNumber = minNumber;
    this.maxNumber = maxNumber;
    newRound();
  }

  public void newRound() {
    firstNumber = generateRandomNumber(minNumber, maxNumber);
    do {
      secondNumber = generateRandomNumber(minNumber, maxNumber);
    } while (firstNumber == secondNumber);
  }

  public boolean guessNumber(int guessedNumber) {

    boolean isGuessCorrect = guessedNumber == Math.max(firstNumber, secondNumber);
    if (isGuessCorrect) {
      score++;
      streak++;
    } else {
      score -= 10;
      streak = 0;
    }
    newRound();
    return isGuessCorrect;
  }

  private int generateRandomNumber(int min, int max) {
    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
    return max + (int)(Math.random() * ((max - min) + 1));
  }
}
