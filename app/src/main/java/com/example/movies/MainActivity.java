package com.example.movies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button iron_man1_show, iron_man2_show, thor_show, the_avengers_show, iron_man3_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialComponents();
    }

    private void initialComponents() {
        iron_man1_show = findViewById(R.id.iron_man_1_show);
        iron_man2_show = findViewById(R.id.iron_man_2_show);
        thor_show = findViewById(R.id.thor_show);
        the_avengers_show = findViewById(R.id.the_avengers_show);
        iron_man3_show = findViewById(R.id.iron_man_3_show);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iron_man_1_show:
                startActivity(new Intent(MainActivity.this, ShowActivity.class).putExtra("name","iron_man_1"));
                finish();
                break;
            case R.id.iron_man_2_show:
                startActivity(new Intent(MainActivity.this, ShowActivity.class).putExtra("name","iron_man_2"));
                finish();
                break;
            case R.id.thor_show:
                startActivity(new Intent(MainActivity.this, ShowActivity.class).putExtra("name","thor"));
                finish();
                break;
            case R.id.the_avengers_show:
                startActivity(new Intent(MainActivity.this, ShowActivity.class).putExtra("name","the_avengers"));
                finish();
                break;
            case R.id.iron_man_3_show:
                startActivity(new Intent(MainActivity.this, ShowActivity.class).putExtra("name","iron_man_3"));
                finish();
                break;
        }
    }
}