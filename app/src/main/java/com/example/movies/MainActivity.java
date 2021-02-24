package com.example.movies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movies.Entity.Movie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    LinearLayout moviesLayout;
    Button add, delete, gradient;
    List<View> allViews;
    static List<Movie> allMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialComponents();
        loadMovies();

        updateMovies();

        add.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddMovieActivity.class));
            finish();
        });

        delete.setOnClickListener(v -> {
            allMovies.clear();
            saveMovies(this);
            startActivity(new Intent(MainActivity.this, MainActivity.class));
            finish();
        });

        gradient.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, GradientActivity.class));
            finish();
        });
    }

    @Override
    public void onBackPressed() {
    }

    private void initialComponents() {
        moviesLayout = findViewById(R.id.moviesLayout);
        add = findViewById(R.id.add);
        delete = findViewById(R.id.delete);
        gradient = findViewById(R.id.gradient);
        allViews = new ArrayList<>();
        allMovies = new ArrayList<>();
    }

    private void loadMovies() {
        try {
            InputStream inputStream = openFileInput("movies.json");

            if (inputStream != null) {
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(isr);
                String line;
                StringBuilder builder = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    builder.append(line).append('\n');
                }

                inputStream.close();
                allMovies = new Gson().fromJson(builder.toString(), new TypeToken<List<Movie>>() {}.getType());
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void saveMovies(Context context) {
        try {
            OutputStream outputStream = context.openFileOutput("movies.json", MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(outputStream);
            osw.write(new Gson().toJson(MainActivity.allMovies));
            osw.close();
        } catch (Throwable t) {
            Toast.makeText(context.getApplicationContext(),
                    "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void updateMovies() {
        for (Movie x : allMovies) {
            @SuppressLint("InflateParams")
            final View view = getLayoutInflater().inflate(R.layout.layout_movie_title,null);
            ImageView poster = view.findViewById(R.id.poster);
            TextView name = view.findViewById(R.id.name);
            Button show = view.findViewById(R.id.show);

            name.setText(x.getName());

            if (x.getPosterPath() != null) {
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(x.getPosterPath()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                poster.setImageBitmap(bitmap);
            }

            show.setOnClickListener(v1 -> {
                startActivity(new Intent(MainActivity.this, ShowActivity.class)
                        .putExtra("name", x.getName())
                        .putExtra("description", x.getDescription())
                        .putExtra("posterPath", x.getPosterPath())
                );
                finish();
            });

            allViews.add(view);
            moviesLayout.addView(view);
        }
    }
}