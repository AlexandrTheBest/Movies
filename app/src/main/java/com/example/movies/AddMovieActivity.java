package com.example.movies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movies.Entity.Movie;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class AddMovieActivity extends AppCompatActivity {

    EditText name, description;
    ImageView poster;
    String posterPath;
    Button add, load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);

        initialComponent();

        add.setOnClickListener(v -> {
            if (isMovieValid()) {
                MainActivity.allMovies.add(new Movie(name.getText().toString(), description.getText().toString(), posterPath));
                MainActivity.saveMovies(this);

                startActivity(new Intent(AddMovieActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Invalid values", Toast.LENGTH_SHORT).show();
            }
        });

        load.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, 1);
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddMovieActivity.this, MainActivity.class));
        finish();
    }

    private void initialComponent() {
        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        poster = findViewById(R.id.poster);
        add = findViewById(R.id.add);
        load = findViewById(R.id.load);
    }

    private boolean isMovieValid() {
        return !name.getText().toString().isEmpty() && !description.getText().toString().isEmpty();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                posterPath = imageReturnedIntent.getDataString();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(posterPath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                poster.setImageBitmap(bitmap);
            }
        }
    }
}