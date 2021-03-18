package com.onimaskesi.onimusicplayer.view;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.onimaskesi.onimusicplayer.R;

import java.io.File;
import java.util.ArrayList;

public class SongPlayerActivity extends AppCompatActivity {

    private ImageView musicIcon;
    private TextView musicNameTV;
    private ImageView playPauseIcon;

    private ArrayList<File> audioFiles;
    private int position;

    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_player);

        musicIcon = findViewById(R.id.musicIcon);
        musicNameTV = findViewById(R.id.audioNameTV);
        playPauseIcon = findViewById(R.id.playPauseBtn);

        Bundle bundle = getIntent().getExtras();
        audioFiles = (ArrayList) bundle.getParcelableArrayList("audios");
        position = bundle.getInt("position");

        updatePlayer();

    }

    public void backClick(View view) {
        if(position == 0){
            position = audioFiles.size() - 1 ;
        } else {
            position -= 1;
        }
        updatePlayer();
    }

    public void playPauseClick(View view) {


        if(player.isPlaying())
        {
            player.pause();
            playPauseIcon.setImageResource(R.drawable.play);

        } else {

            player.start();
            playPauseIcon.setImageResource(R.drawable.pause);

        }

    }

    public void nextClick(View view) {
        if(position == audioFiles.size() - 1){
            position = 0;
        } else {
            position += 1;
        }
        updatePlayer();
    }

    public void updatePlayer(){

        if (player != null && player.isPlaying()) {
            player.stop();
        }

        musicNameTV.setText(audioFiles.get(position).getName());

        Uri uri = Uri.parse(audioFiles.get(position).toString());

        player = MediaPlayer.create(this, uri);
        player.start();
        playPauseIcon.setImageResource(R.drawable.pause);
    }


}