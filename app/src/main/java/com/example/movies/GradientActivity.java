package com.example.movies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class GradientActivity extends AppCompatActivity {

    RelativeLayout mainLayout;
    CardView gradient;
    LinearLayout colorEditLayout;
    View color1, color2;
    ScrollView allColors;
    SeekBar angle, sbRed1, sbGreen1, sbBlue1, sbRed2, sbGreen2, sbBlue2;
    EditText etRed1, etGreen1, etBlue1, etRed2, etGreen2, etBlue2;
    Button changeColorButton, changeColorTypeButton;
    boolean isChangingColors, isChangingColor1;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gradient);

        initialComponent();

        Animation appearance = AnimationUtils.loadAnimation(this, R.anim.alpha_appearance);
        Animation disappearance = AnimationUtils.loadAnimation(this, R.anim.alpha_disappearance);

        SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateValuesFromSeekBar();
                setDrawable();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        };

        angle.setOnSeekBarChangeListener(onSeekBarChangeListener);
        sbRed1.setOnSeekBarChangeListener(onSeekBarChangeListener);
        sbGreen1.setOnSeekBarChangeListener(onSeekBarChangeListener);
        sbBlue1.setOnSeekBarChangeListener(onSeekBarChangeListener);
        sbRed2.setOnSeekBarChangeListener(onSeekBarChangeListener);
        sbGreen2.setOnSeekBarChangeListener(onSeekBarChangeListener);
        sbBlue2.setOnSeekBarChangeListener(onSeekBarChangeListener);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String number = s.toString();

                if (number.isEmpty()) {
                    s.clear();
                    return;
                }

                if (number.length() > 1 && number.charAt(0) == '0') {
                    s.clear();
                    s.append(number.charAt(1));
                    return;
                }

                if (Integer.parseInt(number) > 255) {
                    s.clear();
                    s.append("255");
                }
            }
        };

        etRed1.addTextChangedListener(textWatcher);
        etGreen1.addTextChangedListener(textWatcher);
        etBlue1.addTextChangedListener(textWatcher);
        etRed2.addTextChangedListener(textWatcher);
        etGreen2.addTextChangedListener(textWatcher);
        etBlue2.addTextChangedListener(textWatcher);

        View.OnFocusChangeListener onFocusChangeListener = (v, hasFocus) -> {
            if (!hasFocus) {
                updateValuesFromEditText();
            }
        };

        etRed1.setOnFocusChangeListener(onFocusChangeListener);
        etGreen1.setOnFocusChangeListener(onFocusChangeListener);
        etBlue1.setOnFocusChangeListener(onFocusChangeListener);
        etRed2.setOnFocusChangeListener(onFocusChangeListener);
        etGreen2.setOnFocusChangeListener(onFocusChangeListener);
        etBlue2.setOnFocusChangeListener(onFocusChangeListener);

        changeColorButton.setOnClickListener(v -> {
            if (!isChangingColors) {
                allColors.setVisibility(View.VISIBLE);
                changeColorButton.setText("Close changing colors");
                isChangingColors = true;
            } else {
                allColors.setVisibility(View.GONE);
                changeColorButton.setText("Open changing colors");
                isChangingColors = false;
            }
        });

        changeColorTypeButton.setOnClickListener(v -> {
            if (!isChangingColor1) {
                color2.startAnimation(disappearance);
                color1.startAnimation(appearance);
                color2.setVisibility(View.GONE);
                color1.setVisibility(View.VISIBLE);
                changeColorTypeButton.setText("Change Color 2");
                isChangingColor1 = true;
            } else {
                color1.startAnimation(disappearance);
                color2.startAnimation(appearance);
                color1.setVisibility(View.GONE);
                color2.setVisibility(View.VISIBLE);
                changeColorTypeButton.setText("Change Color 1");
                isChangingColor1 = false;
            }
        });

        updateValuesFromEditText();
        setDrawable();

        mainLayout.startAnimation(appearance);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(GradientActivity.this, MainActivity.class));
        finish();
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    private void initialComponent() {
        mainLayout = findViewById(R.id.mainLayout);
        gradient = findViewById(R.id.gradient);
        colorEditLayout = findViewById(R.id.colorEditLayout);
        allColors = findViewById(R.id.allColors);
        angle = findViewById(R.id.seekBarAngle);

        color1 = getLayoutInflater().inflate(R.layout.layout_edit_color, null);
        color2 = getLayoutInflater().inflate(R.layout.layout_edit_color, null);

        color1.setVisibility(View.VISIBLE);
        color2.setVisibility(View.GONE);

        sbRed1 = color1.findViewById(R.id.seekBarRed);
        sbGreen1 = color1.findViewById(R.id.seekBarGreen);
        sbBlue1 = color1.findViewById(R.id.seekBarBlue);
        sbRed2 = color2.findViewById(R.id.seekBarRed);
        sbGreen2 = color2.findViewById(R.id.seekBarGreen);
        sbBlue2 = color2.findViewById(R.id.seekBarBlue);

        etRed1 = color1.findViewById(R.id.editTextRed);
        etGreen1 = color1.findViewById(R.id.editTextGreen);
        etBlue1 = color1.findViewById(R.id.editTextBlue);
        etRed2 = color2.findViewById(R.id.editTextRed);
        etGreen2 = color2.findViewById(R.id.editTextGreen);
        etBlue2 = color2.findViewById(R.id.editTextBlue);

        ((LinearLayout) findViewById(R.id.colorEditLayout)).addView(color1);
        ((LinearLayout) findViewById(R.id.colorEditLayout)).addView(color2);

        etRed1.setText("100");
        etGreen1.setText("0");
        etBlue1.setText("0");
        etRed2.setText("0");
        etGreen2.setText("0");
        etBlue2.setText("0");

        changeColorButton = findViewById(R.id.changeColor);
        changeColorTypeButton = findViewById(R.id.changeColorType);
        isChangingColor1 = true;
    }

    private void updateValuesFromEditText() {
        sbRed1.setProgress(Integer.parseInt(etRed1.getText().toString()));
        sbGreen1.setProgress(Integer.parseInt(etGreen1.getText().toString()));
        sbBlue1.setProgress(Integer.parseInt(etBlue1.getText().toString()));
        sbRed2.setProgress(Integer.parseInt(etRed2.getText().toString()));
        sbGreen2.setProgress(Integer.parseInt(etGreen2.getText().toString()));
        sbBlue2.setProgress(Integer.parseInt(etBlue2.getText().toString()));
    }

    private void updateValuesFromSeekBar() {
        etRed1.setText(String.valueOf(sbRed1.getProgress()));
        etGreen1.setText(String.valueOf(sbGreen1.getProgress()));
        etBlue1.setText(String.valueOf(sbBlue1.getProgress()));
        etRed2.setText(String.valueOf(sbRed2.getProgress()));
        etGreen2.setText(String.valueOf(sbGreen2.getProgress()));
        etBlue2.setText(String.valueOf(sbBlue2.getProgress()));
    }

    private void setDrawable() {
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] { Color.argb(255, sbRed1.getProgress(), sbGreen1.getProgress(), sbBlue1.getProgress()),
                        Color.argb(255, sbRed2.getProgress(), sbGreen2.getProgress(), sbBlue2.getProgress()) });
        switch (angle.getProgress()) {
            case 0:
            case 8:
                drawable.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
                break;
            case 1:
                drawable.setOrientation(GradientDrawable.Orientation.TL_BR);
                break;
            case 2:
                drawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
                break;
            case 3:
                drawable.setOrientation(GradientDrawable.Orientation.BL_TR);
                break;
            case 4:
                drawable.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);
                break;
            case 5:
                drawable.setOrientation(GradientDrawable.Orientation.BR_TL);
                break;
            case 6:
                drawable.setOrientation(GradientDrawable.Orientation.RIGHT_LEFT);
                break;
            case 7:
                drawable.setOrientation(GradientDrawable.Orientation.TR_BL);
                break;
        }
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gradient.setForeground(drawable);
    }
}