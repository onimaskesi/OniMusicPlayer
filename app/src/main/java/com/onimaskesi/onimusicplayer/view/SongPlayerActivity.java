package com.onimaskesi.onimusicplayer.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.hms.mlplugin.asr.MLAsrCaptureActivity;
import com.huawei.hms.mlplugin.asr.MLAsrCaptureConstants;
import com.onimaskesi.onimusicplayer.R;

import java.io.File;
import java.util.ArrayList;

public class SongPlayerActivity extends AppCompatActivity {

    private int VOICE_PERMISSION_CODE = 2;
    private static final int REQUEST_CODE_ASR = 100;

    private String voiceText;
    private String NEXT_COMMAND;
    private String BACK_COMMAND;
    private String PLAY_COMMAND;
    private String PAUSE_COMMAND;


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

        NEXT_COMMAND = getString(R.string.next_command).toUpperCase();
        BACK_COMMAND = getString(R.string.back_command).toUpperCase();
        PLAY_COMMAND = getString(R.string.play_command).toUpperCase();
        PAUSE_COMMAND = getString(R.string.pause_command).toUpperCase();

    }

    public void backClick(View view) {
        back();
    }

    public void back(){
        if(position == 0){
            position = audioFiles.size() - 1 ;
        } else {
            position -= 1;
        }
        updatePlayer();
    }

    public void playPauseClick(View view) {

        playPause();

    }

    public void playPause(){
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
        next();
    }

    public void next(){
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

    public void useVoiceCommand(){
        voiceText = voiceText.toUpperCase();

        if(!voiceText.isEmpty()){
            if(voiceText.contains(NEXT_COMMAND)){
                next();
            }
            else if(voiceText.contains(BACK_COMMAND)){
                back();
            }
            else if(voiceText.contains(PLAY_COMMAND) || voiceText.contains(PAUSE_COMMAND) ){
                playPause();
            }
            else {
                Toast.makeText(this, voiceText, Toast.LENGTH_LONG).show();
            }
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        // This is the center button for headphones
        if(event.getKeyCode() ==KeyEvent.KEYCODE_HEADSETHOOK)
        {
            openASR();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void voiceCommandClick(View view) {

        checkPermission(Manifest.permission.RECORD_AUDIO, VOICE_PERMISSION_CODE);

    }

    public void openASR(){
        // Use Intent for recognition settings.
        Intent intent = new Intent(this, MLAsrCaptureActivity.class)
                // Set the language that can be recognized to English. If this parameter is not set, English is recognized by default. Example: "zh-CN": Chinese; "en-US": English; "fr-FR": French; "es-ES": Spanish; "de-DE": German; "it-IT": Italian; "ar": Arabic.
                .putExtra(MLAsrCaptureConstants.LANGUAGE, "en-US")
                // Set whether to display the recognition result on the speech pickup UI. MLAsrCaptureConstants.FEATURE_ALLINONE: no; MLAsrCaptureConstants.FEATURE_WORDFLUX: yes.
                .putExtra(MLAsrCaptureConstants.FEATURE, MLAsrCaptureConstants.FEATURE_WORDFLUX);

        startActivityForResult(intent, REQUEST_CODE_ASR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String text = "";
        // REQUEST_CODE_ASR: request code between the current activity and speech pickup UI activity defined in step 3.
        if (requestCode == REQUEST_CODE_ASR) {
            switch (resultCode) {
                // MLAsrCaptureConstants.ASR_SUCCESS: Recognition is successful.
                case MLAsrCaptureConstants.ASR_SUCCESS:
                    if (data != null) {
                        Bundle bundle = data.getExtras();
                        // Obtain the text information recognized from speech.
                        if (bundle != null && bundle.containsKey(MLAsrCaptureConstants.ASR_RESULT)) {
                            text = bundle.getString(MLAsrCaptureConstants.ASR_RESULT);
                            // Process the recognized text information.
                        }
                        if (text == null || "" == text) {
                            text = "Result is null.";
                        }
                        voiceText = text;
                        useVoiceCommand();
                    }
                    break;
                // MLAsrCaptureConstants.ASR_FAILURE: Recognition fails.
                case MLAsrCaptureConstants.ASR_FAILURE:
                    // Processing logic for recognition failure.
                    if(data != null) {
                        Bundle bundle = data.getExtras();
                        // Check whether a result code is contained.
                        if(bundle.containsKey(MLAsrCaptureConstants.ASR_ERROR_CODE)) {
                            int errorCode = bundle.getInt(MLAsrCaptureConstants.ASR_ERROR_CODE);
                            text = "Error Code: " + errorCode;
                            // Perform troubleshooting based on the result code.
                        }
                        // Check whether error information is contained.
                        if(bundle.containsKey(MLAsrCaptureConstants.ASR_ERROR_MESSAGE)){
                            String errorMsg = bundle.getString(MLAsrCaptureConstants.ASR_ERROR_MESSAGE);
                            text = "Error: " + errorMsg;
                            // Perform troubleshooting based on the error information.
                        }
                        // Check whether a sub-result code is contained.
                        if(bundle.containsKey(MLAsrCaptureConstants.ASR_SUB_ERROR_CODE)) {
                            int subErrorCode = bundle.getInt(MLAsrCaptureConstants.ASR_SUB_ERROR_CODE);
                            text += "  Sub Error Code: " + subErrorCode;
                            // Process the sub-result code.
                        }
                    }
                    voiceText = text + "Failure";
                    useVoiceCommand();
                    break;
                default:
                    voiceText = "Failure.";
                    useVoiceCommand();
                    break;
            }
        }
    }

    public void checkPermission(String permission, int requestCode)
    {

        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(SongPlayerActivity.this, permission) == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(SongPlayerActivity.this, new String[] { permission }, requestCode);

        }
        else {
            openASR();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == VOICE_PERMISSION_CODE) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                openASR();

            }
            else {
                //Toast.makeText(MainActivity.this, "Read External Storage Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void informationClick(View view) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.pop_up_info, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

}