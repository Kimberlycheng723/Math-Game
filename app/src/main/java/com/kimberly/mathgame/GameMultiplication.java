package com.kimberly.mathgame;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;
import java.util.Random;

public class GameMultiplication extends AppCompatActivity {
    TextView score;
    TextView life;
    TextView time;
    TextView question;
    EditText answer;
    Button check;
    Button next;

    Random random = new Random();
    int number1;
    int number2;
    int userAnswer;
    int realAnswer;
    boolean isAnswerChecked = false;

    int userScore = 0;
    int userLife = 3;

    CountDownTimer timer;
    private static final long START_TIMER_IN_MILIS = 10000; //can change the time given to player in milliseconds
    Boolean timer_running;
    long time_left_in_milis = START_TIMER_IN_MILIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);

        score = findViewById(R.id.textViewScore);
        life = findViewById(R.id.textViewLife);
        time = findViewById(R.id.textViewTime);
        question = findViewById(R.id.textViewQuestion);
        answer = findViewById(R.id.editTextAnswer);
        check = findViewById(R.id.buttonCheck);
        next = findViewById(R.id.buttonNext);


        gameContinue();
        next.setEnabled(false);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!answer.getText().toString().isEmpty()) {
                    userAnswer = Integer.valueOf(answer.getText().toString());
                    pauseTimer();

                    if (userAnswer == realAnswer) {

                        userScore = userScore + 10;
                        score.setText("" + userScore);
                        question.setText(getString(R.string.correct));
                        question.setBackgroundColor(Color.GREEN);
                        check.setEnabled(false);

                    } else {

                        userLife = userLife - 1;
                        life.setText("" + userLife);
                        question.setText(getString(R.string.wrong));
                        question.setBackgroundColor(Color.RED);
                        check.setEnabled(false);
                        answer.setEnabled(false);

                    }

                    isAnswerChecked = true;
                    next.setEnabled(true);
                } else{
                    Toast.makeText(getApplicationContext(),getString(R.string.enter_answer),Toast.LENGTH_LONG).show();
                }
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                if(isAnswerChecked)

                {
                    answer.setText("");
                    answer.setEnabled(true);
                    check.setEnabled(true);
                    resetTimer();

                    if (userLife > 0) {
                        question.setBackgroundColor(Color.WHITE);
                        gameContinue();

                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.lose_game), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(GameMultiplication.this, Result.class);
                        intent.putExtra("score", userScore);
                        startActivity(intent);
                        finish();
                    }

                    isAnswerChecked = false;
                    next.setEnabled(false);
                } else {
                    Toast.makeText(getApplicationContext(),getString(R.string.check_first), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void gameContinue(){
        number1 = random.nextInt(11);
        number2 = random.nextInt(11);
        realAnswer = number1 * number2;
        question.setText(number1 + " x " + number2);
        startTimer();
    }

    public void startTimer(){
        timer = new CountDownTimer(time_left_in_milis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time_left_in_milis = millisUntilFinished;
                updateText();
            }

            @Override
            public void onFinish() {
                timer_running =false;
                pauseTimer();
                resetTimer();
                updateText();
                userLife = userLife-1;
                life.setText(""+userLife);
                question.setBackgroundColor(Color.YELLOW);
                question.setText(getString(R.string.time_out));
                answer.setEnabled(false);
                check.setEnabled(false);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        question.setBackgroundColor(Color.WHITE);
                        gameContinue();
                        answer.setEnabled(true);
                        check.setEnabled(true);
                    }
                }, 5000);

            }
        }.start();

        timer_running = true;
    }

    public void updateText(){
        int second = (int)(time_left_in_milis / 1000) % 60;
        String time_left = String.format(Locale.getDefault(),"%02d",second);
        time.setText(time_left);
    }
    public void pauseTimer(){
        timer.cancel();
        timer_running=false;
    }

    public void resetTimer(){
        time_left_in_milis = START_TIMER_IN_MILIS;
        updateText();
    }
}

