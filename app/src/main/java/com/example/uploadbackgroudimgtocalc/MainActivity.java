package com.example.uploadbackgroudimgtocalc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_PERMISSION_READ_STORAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = findViewById(R.id.textView);
        final Button setButton = findViewById(R.id.buttonSet);
        final ImageView imageView = findViewById(R.id.imageView);
        final LinearLayout calcLayout = findViewById(R.id.CalcLayout);
        final LinearLayout setLayout = findViewById(R.id.SettingsLayout);
        final Button okButton = findViewById(R.id.button_OK);

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calcLayout.setVisibility(View.GONE);
                setLayout.setVisibility(View.VISIBLE);
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permissionStatus = ContextCompat.checkSelfPermission(MainActivity.this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE);
                if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                    LoadImg(calcLayout, setLayout);
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_PERMISSION_READ_STORAGE);
                }


            }
        });


    }

    private void LoadImg(LinearLayout calcLayout, LinearLayout setLayout) {
        if (isExternalStorageReadable()) {
            EditText text = findViewById(R.id.editTextImgName);
            String name = text.getText().toString();
            if (name.equals(null)) {
                setLayout.setVisibility(View.GONE);
                calcLayout.setVisibility(View.VISIBLE);

            } else {
                File file = new File(Environment.getExternalStoragePublicDirectory
                        (Environment.DIRECTORY_DOWNLOADS), name);
                if (file.length() == 0) {
                    Toast.makeText(MainActivity.this, "File not found", Toast.LENGTH_SHORT).show();
                } else {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    ImageView iv = findViewById(R.id.imageView);
                    iv.setImageBitmap(bitmap);

                    File filelog = new File(getExternalFilesDir(null),
                            "log.txt");

                    try (FileWriter writer = new FileWriter(filelog, true);
                         FileReader reader = new FileReader(filelog)){
                        writer.write("app started");
                      
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    calcLayout.setVisibility(View.VISIBLE);
                    setLayout.setVisibility(View.GONE);
                }
            }
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}