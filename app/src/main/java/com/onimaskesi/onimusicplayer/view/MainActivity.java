package com.onimaskesi.onimusicplayer.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;

import com.onimaskesi.onimusicplayer.R;
import com.onimaskesi.onimusicplayer.adapter.RecyclerViewAdapter;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private int STORAGE_PERMISSION_CODE = 1;

    private String[] audioFileNames;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);

        recyclerView = findViewById(R.id.recyclerView);

        displayAudioFilesName();
    }

    public void checkPermission(String permission, int requestCode)
    {

        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[] { permission }, requestCode);

        }
        else {
            //Toast.makeText(MainActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
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

            }
            else {
                //Toast.makeText(MainActivity.this, "Read External Storage Denied", Toast.LENGTH_SHORT).show();
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
        final ArrayList<File> audioFiles = getAllAudioFiles(Environment.getExternalStorageDirectory());

        audioFileNames = new String[audioFiles.size()];

        for(int i = 0; i < audioFiles.size(); i++)
        {
            audioFileNames[i] = audioFiles.get(i).getName();
        }

        initializeRecyclerView();

    }

    private void initializeRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerViewAdapter = new RecyclerViewAdapter(audioFileNames);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

}