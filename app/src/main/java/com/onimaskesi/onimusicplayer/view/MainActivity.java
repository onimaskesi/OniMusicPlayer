package com.onimaskesi.onimusicplayer.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.onimaskesi.onimusicplayer.R;
import com.onimaskesi.onimusicplayer.adapter.RecyclerViewAdapter;
import com.onimaskesi.onimusicplayer.util.ViewUtil;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnAudioListener {

    private int STORAGE_PERMISSION_CODE = 1;

    private ArrayList<File> audioFiles;
    private String[] audioFileNames;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);

    }

    public void checkPermission(String permission, int requestCode)
    {

        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[] {permission }, requestCode);

        }
        else {
            //Toast.makeText(MainActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
            displayAudioFilesName();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Toast.makeText(MainActivity.this, "Read External Storage Permission Granted", Toast.LENGTH_SHORT).show();
                displayAudioFilesName();

            }
            else {
                //Toast.makeText(MainActivity.this, "Read External Storage Denied", Toast.LENGTH_SHORT).show();
                ViewUtil.createSnackbar(
                        "If you want to use this app, you have to accept permissions",
                        findViewById(R.id.main_layout)
                );

            }
        }
    }

    public ArrayList<File> getAllAudioFiles(File file)
    {
        ArrayList<File> arrayList = new ArrayList<File>();
        
        File[] allFiles = file.listFiles();
        
        for(File theFile : allFiles)
        {
            if(theFile.isDirectory())
            {
                arrayList.addAll(getAllAudioFiles(theFile));
            }
            else
            {
                if(theFile.getName().endsWith(".mp3") || theFile.getName().endsWith(".aac") || theFile.getName().endsWith(".wav") || theFile.getName().endsWith(".wma") || theFile.getName().endsWith(".m4a"))
                {
                    arrayList.add(theFile);
                }
            }
        }

        return arrayList;
    }

    private void displayAudioFilesName()
    {
        audioFiles = getAllAudioFiles(Environment.getExternalStorageDirectory());

        audioFileNames = new String[audioFiles.size()];

        for(int i = 0; i < audioFiles.size(); i++)
        {
            audioFileNames[i] = audioFiles.get(i).getName();
        }

        progressBar.setVisibility(View.INVISIBLE);
        initializeRecyclerView();

    }

    private void initializeRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerViewAdapter = new RecyclerViewAdapter(audioFileNames, this);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void onAudioClick(int position) {
        Intent intent = new Intent(this, SongPlayerActivity.class);
        intent.putExtra("position",position);
        intent.putExtra("audios", audioFiles);
        startActivity(intent);
    }
}