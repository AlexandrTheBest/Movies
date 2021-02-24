package com.example.movies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class GradientActivity extends AppCompatActivity {

    CardView gradient;
    LinearLayout colorEditLayout;
    View colorStart, colorEnd, settings;
    ScrollView allColors;
    SeekBar angle, sbRed1, sbGreen1, sbBlue1, sbRed2, sbGreen2, sbBlue2;
    EditText etRed1, etGreen1, etBlue1, etRed2, etGreen2, etBlue2;
    RadioButton linear, radial, sweep;
    Button changeColorButton, changeColorTypeButton;
    boolean isChangingColors;
    int typeChanging; // 0 - color1, 1 - color2, 2 - settings

    @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
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

        sbRed1.setOnSeekBarChangeListener(onSeekBarChangeListener);
        sbGreen1.setOnSeekBarChangeListener(onSeekBarChangeListener);
        sbBlue1.setOnSeekBarChangeListener(onSeekBarChangeListener);
        sbRed2.setOnSeekBarChangeListener(onSeekBarChangeListener);
        sbGreen2.setOnSeekBarChangeListener(onSeekBarChangeListener);
        sbBlue2.setOnSeekBarChangeListener(onSeekBarChangeListener);

        angle.setOnSeekBarChangeListener(onSeekBarChangeListener);
        ((SeekBar) settings.findViewById(R.id.seekBarRadialRadius)).setOnSeekBarChangeListener(onSeekBarChangeListener);
        ((SeekBar) settings.findViewById(R.id.seekBarRadialX)).setOnSeekBarChangeListener(onSeekBarChangeListener);
        ((SeekBar) settings.findViewById(R.id.seekBarRadialY)).setOnSeekBarChangeListener(onSeekBarChangeListener);
        ((SeekBar) settings.findViewById(R.id.seekBarSweepX)).setOnSeekBarChangeListener(onSeekBarChangeListener);
        ((SeekBar) settings.findViewById(R.id.seekBarSweepY)).setOnSeekBarChangeListener(onSeekBarChangeListener);

//        TextWatcher textWatcher = new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String number = s.toString();
//
//                if (number.isEmpty()) {
//                    s.clear();
//                    return;
//                }
//
//                if (number.length() > 1 && number.charAt(0) == '0') {
//                    s.clear();
//                    s.append(number.charAt(1));
//                    return;
//                }
//
//                if (Integer.parseInt(number) > 255) {
//                    s.clear();
//                    s.append("255");
//                }
//            }
//        };
//
//        etRed1.addTextChangedListener(textWatcher);
//        etGreen1.addTextChangedListener(textWatcher);
//        etBlue1.addTextChangedListener(textWatcher);
//        etRed2.addTextChangedListener(textWatcher);
//        etGreen2.addTextChangedListener(textWatcher);
//        etBlue2.addTextChangedListener(textWatcher);

        ((RadioGroup) settings.findViewById(R.id.radioGroup)).setOnCheckedChangeListener((group, checkedId) -> {
            Handler handler = new Handler();

            settings.findViewById(R.id.someSettings).startAnimation(disappearance);
            switch (checkedId) {
                case R.id.linear:
                    gradient.startAnimation(disappearance);
                    handler.postDelayed(() -> {
                        gradient.startAnimation(appearance);
                        settings.findViewById(R.id.linearLayout).setVisibility(View.VISIBLE);
                        settings.findViewById(R.id.radialLayout).setVisibility(View.GONE);
                        settings.findViewById(R.id.sweepLayout).setVisibility(View.GONE);
                    }, 500);
                    break;
                case R.id.radial:
                    gradient.startAnimation(disappearance);
                    handler.postDelayed(() -> {
                        gradient.startAnimation(appearance);
                        settings.findViewById(R.id.linearLayout).setVisibility(View.GONE);
                        settings.findViewById(R.id.radialLayout).setVisibility(View.VISIBLE);
                        settings.findViewById(R.id.sweepLayout).setVisibility(View.GONE);
                    }, 500);
                    break;
                case R.id.sweep:
                    gradient.startAnimation(disappearance);
                    handler.postDelayed(() -> {
                        gradient.startAnimation(appearance);
                        settings.findViewById(R.id.linearLayout).setVisibility(View.GONE);
                        settings.findViewById(R.id.radialLayout).setVisibility(View.GONE);
                        settings.findViewById(R.id.sweepLayout).setVisibility(View.VISIBLE);
                    }, 500);
                    break;
            }
            handler.postDelayed(() -> {
                settings.findViewById(R.id.someSettings).startAnimation(appearance);
                setDrawable();
            }, 500);
        });

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
            switch (typeChanging) {
                case 0:
                    colorStart.startAnimation(disappearance);
                    colorEnd.startAnimation(appearance);
                    colorEnd.setVisibility(View.VISIBLE);
                    colorStart.setVisibility(View.GONE);
                    changeColorTypeButton.setText("Change Settings");
                    typeChanging = 1;
                    break;
                case 1:
                    colorEnd.startAnimation(disappearance);
                    settings.startAnimation(appearance);
                    colorEnd.setVisibility(View.GONE);
                    settings.setVisibility(View.VISIBLE);
                    changeColorTypeButton.setText("Change Start Color");
                    typeChanging = 2;
                    break;
                case 2:
                    settings.startAnimation(disappearance);
                    colorStart.startAnimation(appearance);
                    settings.setVisibility(View.GONE);
                    colorStart.setVisibility(View.VISIBLE);
                    changeColorTypeButton.setText("Change End Color");
                    typeChanging = 0;
                    break;
            }
        });

        updateValuesFromSeekBar();
        setDrawable();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(GradientActivity.this, MainActivity.class));
        finish();
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    private void initialComponent() {
        gradient = findViewById(R.id.gradient);
        colorEditLayout = findViewById(R.id.colorEditLayout);
        allColors = findViewById(R.id.allColors);

        colorStart = getLayoutInflater().inflate(R.layout.layout_edit_color, null);
        colorEnd = getLayoutInflater().inflate(R.layout.layout_edit_color, null);
        settings = getLayoutInflater().inflate(R.layout.layout_edit_gradient, null);

        ((TextView) colorStart.findViewById(R.id.title)).setText("Start color");
        ((TextView) colorEnd.findViewById(R.id.title)).setText("End color");

        colorStart.setVisibility(View.VISIBLE);
        colorEnd.setVisibility(View.GONE);
        settings.setVisibility(View.GONE);

        sbRed1 = colorStart.findViewById(R.id.seekBarRed);
        sbGreen1 = colorStart.findViewById(R.id.seekBarGreen);
        sbBlue1 = colorStart.findViewById(R.id.seekBarBlue);
        sbRed2 = colorEnd.findViewById(R.id.seekBarRed);
        sbGreen2 = colorEnd.findViewById(R.id.seekBarGreen);
        sbBlue2 = colorEnd.findViewById(R.id.seekBarBlue);

        etRed1 = colorStart.findViewById(R.id.editTextRed);
        etGreen1 = colorStart.findViewById(R.id.editTextGreen);
        etBlue1 = colorStart.findViewById(R.id.editTextBlue);
        etRed2 = colorEnd.findViewById(R.id.editTextRed);
        etGreen2 = colorEnd.findViewById(R.id.editTextGreen);
        etBlue2 = colorEnd.findViewById(R.id.editTextBlue);

        angle = settings.findViewById(R.id.seekBarAngle);
        linear = settings.findViewById(R.id.linear);
        radial = settings.findViewById(R.id.radial);
        sweep = settings.findViewById(R.id.sweep);

        ((LinearLayout) findViewById(R.id.colorEditLayout)).addView(colorStart);
        ((LinearLayout) findViewById(R.id.colorEditLayout)).addView(colorEnd);
        ((LinearLayout) findViewById(R.id.colorEditLayout)).addView(settings);

        changeColorButton = findViewById(R.id.changeColor);
        changeColorTypeButton = findViewById(R.id.changeColorType);

        sbRed1.setProgress(100);
        ((SeekBar) settings.findViewById(R.id.seekBarRadialRadius)).setProgress(25);
        ((SeekBar) settings.findViewById(R.id.seekBarRadialX)).setProgress(50);
        ((SeekBar) settings.findViewById(R.id.seekBarRadialY)).setProgress(50);
        ((SeekBar) settings.findViewById(R.id.seekBarSweepX)).setProgress(50);
        ((SeekBar) settings.findViewById(R.id.seekBarSweepY)).setProgress(50);
        typeChanging = 0;
    }

    private void updateValuesFromEditText() {
        if (etRed1.getText().toString().isEmpty()) {
            etRed1.setText("0");
        }
        if (etGreen1.getText().toString().isEmpty()) {
            etGreen1.setText("0");
        }
        if (etBlue1.getText().toString().isEmpty()) {
            etBlue1.setText("0");
        }
        if (etRed2.getText().toString().isEmpty()) {
            etRed2.setText("0");
        }
        if (etGreen2.getText().toString().isEmpty()) {
            etGreen2.setText("0");
        }
        if (etBlue2.getText().toString().isEmpty()) {
            etBlue2.setText("0");
        }
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

        ((EditText) settings.findViewById(R.id.editTextRadialRadius)).setText(String.valueOf(((SeekBar) settings.findViewById(R.id.seekBarRadialRadius)).getProgress() * 10));
        ((EditText) settings.findViewById(R.id.editTextRadialX)).setText(String.valueOf(((SeekBar) settings.findViewById(R.id.seekBarRadialX)).getProgress()));
        ((EditText) settings.findViewById(R.id.editTextRadialY)).setText(String.valueOf(((SeekBar) settings.findViewById(R.id.seekBarRadialY)).getProgress()));
        ((EditText) settings.findViewById(R.id.editTextSweepX)).setText(String.valueOf(((SeekBar) settings.findViewById(R.id.seekBarSweepX)).getProgress()));
        ((EditText) settings.findViewById(R.id.editTextSweepY)).setText(String.valueOf(((SeekBar) settings.findViewById(R.id.seekBarSweepY)).getProgress()));
    }

    @SuppressLint("NonConstantResourceId")
    private void setDrawable() {
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] { Color.argb(255, sbRed1.getProgress(), sbGreen1.getProgress(), sbBlue1.getProgress()),
                        Color.argb(255, sbRed2.getProgress(), sbGreen2.getProgress(), sbBlue2.getProgress()) });

        drawable.setShape(GradientDrawable.RECTANGLE);
        switch (((RadioGroup) settings.findViewById(R.id.radioGroup)).getCheckedRadioButtonId()) {
            case R.id.linear:
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
                drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
                break;
            case R.id.radial:
                drawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
                drawable.setGradientRadius(((SeekBar) settings.findViewById(R.id.seekBarRadialRadius)).getProgress() * 10);
                drawable.setGradientCenter((float)((SeekBar) settings.findViewById(R.id.seekBarRadialX)).getProgress() / 100,
                        (float)((SeekBar) settings.findViewById(R.id.seekBarRadialY)).getProgress() / 100);
                break;
            case R.id.sweep:
                drawable.setGradientType(GradientDrawable.SWEEP_GRADIENT);
                drawable.setGradientCenter((float)((SeekBar) settings.findViewById(R.id.seekBarSweepX)).getProgress() / 100,
                        (float)((SeekBar) settings.findViewById(R.id.seekBarSweepY)).getProgress() / 100);
                break;
        }

        gradient.setForeground(drawable);
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                v.clearFocus();
                InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
            updateValuesFromEditText();
        }
        return super.dispatchTouchEvent(event);
    }
}