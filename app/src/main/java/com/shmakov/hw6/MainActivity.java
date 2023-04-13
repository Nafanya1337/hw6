package com.shmakov.hw6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    TextView group, songname, lyrics, question, rate;
    int song, fav_songs;
    ImageView img;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        song = 0;
        fav_songs = 0;
        constraintLayout = findViewById(R.id.main);
        group = findViewById(R.id.group);
        songname = findViewById(R.id.songname);
        lyrics = findViewById(R.id.lyrics);
        question = findViewById(R.id.question);
        rate = findViewById(R.id.rate);
        img = findViewById(R.id.img);
        makeString();
    }

    public void changeSong(View view) {
        song += 1;
        song %= 2;
        switch (song) {
            case 0:
                group.setText(R.string.group);
                songname.setText(R.string.songname);
                lyrics.setText(R.string.lyrics);
                img.setImageResource(R.drawable.queen_img);
                break;
            case 1:
                group.setText(R.string.group2);
                songname.setText(R.string.songname2);
                lyrics.setText(R.string.lyrics2);
                img.setImageResource(R.drawable.ac_dc_img);
                break;
        }
        fav_songs = 0;
        TextView count = findViewById(R.id.count);
        count.setText("0");
        question.setText("Сколько ваших любимых песен группы " + group.getText());
    }
    public void makeString() {
        LinearLayout layout = findViewById(R.id.string_layout);
        String model = Build.BRAND + " " + Build.MODEL;
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TextView txt = new TextView(this);
        txt.setText(getResources().getString(R.string.welcome, model, hour, minute, Build.USER));
        txt.setTextSize(20);
        layout.addView(txt);
    }

    public void makeFavourite(View view) {
        TextView count = findViewById(R.id.count);
        switch (view.getId()) {
            case R.id.minus:
                fav_songs = fav_songs > 0 ? --fav_songs : 0;
                break;
            case R.id.plus:
                fav_songs++;
                break;
        }
        String str = getResources().getQuantityString(R.plurals.songs, fav_songs, fav_songs);
        count.setText(str);
    }

    public void openGroupPhoto(View view){
        ImageView imageView = new ImageView(this);
        FrameLayout layout = new FrameLayout(this);
        String filename = "queen_img.png";
        if (song == 1)
            filename = "ac_dc_img.png";
        try(InputStream inputStream =
                    getApplicationContext().getAssets().open(filename))
        {
            Button closeButton = new Button(this);

            closeButton.setText("Закрыть");
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(300, 100, Gravity.TOP);
            params.setMargins(40,1100,0,0);
            closeButton.setLayoutParams(params);

            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Remove the FrameLayout from ConstraintLayout when the button is clicked
                    constraintLayout.removeView(layout);
                    constraintLayout.setClickable(true);
                }
            });
            params = new FrameLayout.LayoutParams(1200, 1200, Gravity.TOP|Gravity.CENTER);
            params.setMargins(0,25,0,0);
            layout.addView(closeButton);
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            imageView.setImageDrawable(drawable);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(params);
            layout.addView(imageView);
            layout.setLayoutParams(new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    2200,
                    Gravity.CENTER));
            constraintLayout.addView(layout);
            constraintLayout.setClickable(false);
        }
        catch (IOException e){
            Log.e("MyApp", "Error loading image", e);
            e.printStackTrace();
        }
    }
}
