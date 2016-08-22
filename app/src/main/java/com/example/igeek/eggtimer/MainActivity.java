package com.example.igeek.eggtimer;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.annotation.IntegerRes;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    SeekBar timerSeekBar;
    TextView timerTextView;
    Button go;
    Boolean counterIsActive = false;
    CountDownTimer countDownTimer;

    public void resetTimer(){
        timerTextView.setText("0:30");
        timerSeekBar.setProgress(30);
        countDownTimer.cancel();
        go.setText("GO");
        timerSeekBar.setEnabled(true);
        counterIsActive =false;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void updateTimer(int secondsLeft){
        //First we need to convert from seconds to minutes and seconds
        //What the below code is doing  is taking in the number of seconds in total (progress)
        //and dividing it by 60. We also neeed to cast it by an integer so that we will return the number of whole minutes
        int minutes = (int)secondsLeft /60;

        //What this line of code is doing is giving us the number of seconds minus the
        //number of seconds we've already calculated.
        int seconds = secondsLeft - minutes *60;

        //The below conditional code is just so that our UI wont look odd whenever
        //our number ends with :00! It's not really required though...
        String secondString = Integer.toString(seconds);

        if(seconds <= 9){
            secondString = "0" + secondString;
        }
        //Here we are setting our requested time whenever the seekBar is moved by the user
        timerTextView.setText(Integer.toString(minutes) + ":" + secondString);
    }


    public void controlTimer(View view){

        //When the button is first clicked we want to start the timer

        if(!counterIsActive) {
            counterIsActive = true;
            timerSeekBar.setEnabled(false);
            go.setText("Stop");
           countDownTimer= new CountDownTimer(timerSeekBar.getProgress() * 1000 + 100, 1000) {


                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onTick(long l) {

                    updateTimer((int) l / 1000);
                }

                @Override
                public void onFinish() {
                    resetTimer();
                    MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.explosion);
                    mediaPlayer.start();
                }
            }.start();
        }else{
            resetTimer();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         timerSeekBar = (SeekBar)findViewById(R.id.timerSeekBar);
         go = (Button)findViewById(R.id.go);
         timerTextView = (TextView)findViewById(R.id.timerTextView);

        //Set the max time to 10min or in other words 600seconds
        //Obviously this can be whatever you wan it to be.
        timerSeekBar.setMax(600);

        //Remember that our seekBar is initilaly at 00:30
        timerSeekBar.setProgress(30);

        timerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

                updateTimer(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                //Not interested in this method
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                //Not interested in this method

            }
        });
    }
}
