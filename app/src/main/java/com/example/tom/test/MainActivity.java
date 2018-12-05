package com.example.tom.test;

import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.ColorSpace.Rgb;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.text.MessageFormat;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {

    GarbageGame garbageGame = new GarbageGame(0, 100);
    final Handler handler = new Handler();

    Button leftButton;
    Button rightButton;
    TextView pointsLabel;
    Runnable feedbackRunnable;

    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = findViewById(android.R.id.content);

        leftButton = findViewById(R.id.left_button);
        rightButton = findViewById(R.id.right_button);
        pointsLabel = findViewById(R.id.points);

        garbageGame.newRound();
        repaint();

        feedbackRunnable = new Runnable() {
            @Override
            public void run() {
                repaint();
                toggleUiEnabled(true);
            }
        };
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("garbagegame", garbageGame);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        garbageGame = (GarbageGame) savedInstanceState.getSerializable("garbagegame");
        repaint();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(feedbackRunnable);
    }

    private void repaint() {
        leftButton.setText(String.valueOf(garbageGame.getFirstNumber()));
        rightButton.setText(String.valueOf(garbageGame.getSecondNumber()));
        pointsLabel.setText(MessageFormat
            .format("{0} {1}", getString(R.string.points), garbageGame.getScore()));

        float[] hsv = new float[3];
        @ColorInt int fullGreen = Color.GREEN;
        Color.colorToHSV(fullGreen, hsv);
        hsv[1] = Math.min((garbageGame.getStreak() * 1f) / 255f, 1.0f);
        @ColorInt int greenShade = Color.HSVToColor(hsv);
        view.setBackgroundColor(greenShade);
    }

    private void toggleUiEnabled(boolean isEnabled) {
        leftButton.setClickable(isEnabled);
        rightButton.setClickable(isEnabled);
    }

    private void feedbackFromGuess(boolean isCorrect) {
        if (!isCorrect) {
            view.setBackgroundColor(Color.RED);
        }
        toggleUiEnabled(false);
        handler.postDelayed(feedbackRunnable, 150);
    }

    private boolean guessNumber(Button button) {
        return garbageGame.guessNumber(Integer.parseInt((button).getText().toString()));
    }

    public void buttonPressed(View button) {
        boolean isCorrect = guessNumber((Button) button);
        repaint();
        feedbackFromGuess(isCorrect);
    }
}
