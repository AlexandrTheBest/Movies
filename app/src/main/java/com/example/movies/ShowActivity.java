package com.example.movies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView poster;
    TextView name, description;
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

        switch (getIntent().getStringExtra("name")) {
            case "iron_man_1":
                poster.setImageResource(R.drawable.iron_man_1_poster);
                name.setText(R.string.iron_man_1);
                description.setText(R.string.iron_man_1_description);
                break;
            case "iron_man_2":
                poster.setImageResource(R.drawable.iron_man_2_poster);
                name.setText(R.string.iron_man_2);
                description.setText(R.string.iron_man_2_description);
                break;
            case "thor":
                poster.setImageResource(R.drawable.thor_poster);
                name.setText(R.string.thor);
                description.setText(R.string.thor_description);
                break;
            case "the_avengers":
                poster.setImageResource(R.drawable.the_avengers_poster);
                name.setText(R.string.the_avengers);
                description.setText(R.string.the_avengers_description);
                break;
            case "iron_man_3":
                poster.setImageResource(R.drawable.iron_man_3_poster);
                name.setText(R.string.iron_man_3);
                description.setText(R.string.iron_man_3_description);
                break;
            default:
                break;
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