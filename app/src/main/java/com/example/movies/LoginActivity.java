package com.example.movies;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout buttons_layout, sign_in_layout, sign_up_layout;
    TextView title;
    EditText sign_in_login, sign_in_password, sign_up_personName, sign_up_email, sign_up_login, sign_up_password, sign_up_repeat_password;
    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialComponents();
    }

    @Override
    public void onBackPressed() {
        title.setText(R.string.hello);
        sign_in_layout.setVisibility(View.GONE);
        sign_up_layout.setVisibility(View.GONE);
        buttons_layout.setVisibility(View.VISIBLE);
    }

    @SuppressLint("SetTextI18n")
    private void initialComponents() {
        title = findViewById(R.id.title);

        buttons_layout = findViewById(R.id.buttons_layout);
        sign_in_layout = findViewById(R.id.sign_in_layout);
        sign_up_layout = findViewById(R.id.sign_up_layout);

        sign_in_login = findViewById(R.id.sign_in_login);
        sign_in_password = findViewById(R.id.sign_in_password);

        sign_up_personName = findViewById(R.id.sign_up_personName);
        sign_up_email = findViewById(R.id.sign_up_email);
        sign_up_login = findViewById(R.id.sign_up_login);
        sign_up_password = findViewById(R.id.sign_up_password);
        sign_up_repeat_password = findViewById(R.id.sign_up_repeat_password);

        ((TextView) findViewById(R.id.version)).setText("App version: " + BuildConfig.VERSION_NAME);
        mSettings = getSharedPreferences("settings", Context.MODE_PRIVATE);
    }

    private boolean isEmailValid(CharSequence target) {
        if (target == null) return false;
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private boolean isSignUpValid() {
        return !sign_up_personName.getText().toString().isEmpty() &&
                !sign_up_login.getText().toString().isEmpty() &&
                !sign_up_password.getText().toString().isEmpty() &&
                !sign_up_repeat_password.getText().toString().isEmpty() &&
                sign_up_password.getText().toString().equals(sign_up_repeat_password.getText().toString()) &&
                isEmailValid(sign_up_email.getText().toString());
    }

    private boolean isSignInValid() {
        return !sign_in_login.getText().toString().isEmpty() && !sign_in_password.getText().toString().isEmpty();
    }

    private boolean isSignInSuccessful() {
        return mSettings.contains("Login") && mSettings.contains("Password") &&
                sign_in_login.getText().toString().equals(mSettings.getString("Login", "")) &&
                sign_in_password.getText().toString().equals(mSettings.getString("Password", ""));
    }

    private void saveRegisterData() {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.clear();
        editor.putString("PersonName", sign_up_personName.getText().toString());
        editor.putString("Email",sign_up_email.getText().toString());
        editor.putString("Login",sign_up_login.getText().toString());
        editor.putString("Password",sign_up_password.getText().toString());
        editor.apply();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_first:
                title.setText(R.string.sign_in);
                buttons_layout.setVisibility(View.GONE);
                sign_in_layout.setVisibility(View.VISIBLE);
                break;
            case R.id.sign_up_first:
                title.setText(R.string.sign_up);
                buttons_layout.setVisibility(View.GONE);
                sign_up_layout.setVisibility(View.VISIBLE);
                break;
            case R.id.sign_in_second:
                if (!isSignInValid()) {
                    Toast.makeText(getApplicationContext(),
                            "Invalid values", Toast.LENGTH_SHORT).show();
                } else {
                    if (!isSignInSuccessful()) {
                        Toast.makeText(getApplicationContext(),
                                "Invalid login or password", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                }
                break;
            case R.id.sign_up_second:
                if(!isSignUpValid()) {
                    Toast.makeText(getApplicationContext(),
                            "Invalid values", Toast.LENGTH_SHORT).show();
                } else {
                    title.setText(R.string.hello);
                    saveRegisterData();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    sign_up_layout.setVisibility(View.GONE);
                    buttons_layout.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
}