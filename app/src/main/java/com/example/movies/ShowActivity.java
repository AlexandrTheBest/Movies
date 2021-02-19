package com.example.movies;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ShowActivity extends AppCompatActivity implements View.OnClickListener {

    TextView name, description;
    ImageView poster;
    Button back;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ShowActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        initialComponents();
    }

    private void initialComponents() {
        poster = findViewById(R.id.poster);
        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        back = findViewById(R.id.back);

        name.setText(getIntent().getStringExtra("name"));
        description.setText(getIntent().getStringExtra("description"));

        if (getIntent().getStringExtra("posterPath") != null) {
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(getIntent().getStringExtra("posterPath")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            poster.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back) {
            startActivity(new Intent(ShowActivity.this, MainActivity.class));
            finish();
        }
    }
}