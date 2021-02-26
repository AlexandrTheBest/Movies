package com.example.movies;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;

public class GradientActivity extends AppCompatActivity {

    CardView gradient;
    LinearLayout colorEditLayout;
    List<View> colors;
    View settings;
    RadioButton linear, radial, sweep;
    Button changeColorTypeButton, changeSettingsButton;
    boolean isEditorActive, isChangingSettings;
    int activeNow; // 0 - color1, 1 - color2, ...
    SeekBar.OnSeekBarChangeListener onSeekBarChangeListener;

    @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gradient);

        initialComponent();

        colors.get(0).setVisibility(View.VISIBLE);
        ((SeekBar) colors.get(0).findViewById(R.id.seekBarRed)).setProgress(100);

        Animation appearance = AnimationUtils.loadAnimation(this, R.anim.alpha_appearance);
        Animation disappearance = AnimationUtils.loadAnimation(this, R.anim.alpha_disappearance);

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

//        for (View x : colors) {
//            ((SeekBar) x.findViewById(R.id.seekBarRed)).setOnSeekBarChangeListener(onSeekBarChangeListener);
//            ((SeekBar) x.findViewById(R.id.seekBarGreen)).setOnSeekBarChangeListener(onSeekBarChangeListener);
//            ((SeekBar) x.findViewById(R.id.seekBarBlue)).setOnSeekBarChangeListener(onSeekBarChangeListener);
//
////            ((EditText) x.findViewById(R.id.editTextRed)).addTextChangedListener(textWatcher);
////            ((EditText) x.findViewById(R.id.editTextGreen)).addTextChangedListener(textWatcher);
////            ((EditText) x.findViewById(R.id.editTextBlue)).addTextChangedListener(textWatcher);
//        }

        ((SeekBar) settings.findViewById(R.id.seekBarAngle)).setOnSeekBarChangeListener(onSeekBarChangeListener);
        ((SeekBar) settings.findViewById(R.id.seekBarRadialRadius)).setOnSeekBarChangeListener(onSeekBarChangeListener);
        ((SeekBar) settings.findViewById(R.id.seekBarRadialX)).setOnSeekBarChangeListener(onSeekBarChangeListener);
        ((SeekBar) settings.findViewById(R.id.seekBarRadialY)).setOnSeekBarChangeListener(onSeekBarChangeListener);
        ((SeekBar) settings.findViewById(R.id.seekBarSweepX)).setOnSeekBarChangeListener(onSeekBarChangeListener);
        ((SeekBar) settings.findViewById(R.id.seekBarSweepY)).setOnSeekBarChangeListener(onSeekBarChangeListener);

        ((RadioGroup) settings.findViewById(R.id.radioGroup)).setOnCheckedChangeListener((group, checkedId) -> {
            Handler handler = new Handler();
            settings.findViewById(R.id.someSettings).startAnimation(disappearance);
            gradient.startAnimation(disappearance);

            switch (checkedId) {
                case R.id.linear:
                    handler.postDelayed(() -> {
                        settings.findViewById(R.id.linearLayout).setVisibility(View.VISIBLE);
                        settings.findViewById(R.id.radialLayout).setVisibility(View.GONE);
                        settings.findViewById(R.id.sweepLayout).setVisibility(View.GONE);
                    }, 500);
                    break;
                case R.id.radial:
                    handler.postDelayed(() -> {
                        settings.findViewById(R.id.linearLayout).setVisibility(View.GONE);
                        settings.findViewById(R.id.radialLayout).setVisibility(View.VISIBLE);
                        settings.findViewById(R.id.sweepLayout).setVisibility(View.GONE);
                    }, 500);
                    break;
                case R.id.sweep:
                    handler.postDelayed(() -> {
                        settings.findViewById(R.id.linearLayout).setVisibility(View.GONE);
                        settings.findViewById(R.id.radialLayout).setVisibility(View.GONE);
                        settings.findViewById(R.id.sweepLayout).setVisibility(View.VISIBLE);
                    }, 500);
                    break;
            }

            handler.postDelayed(() -> {
                settings.findViewById(R.id.someSettings).startAnimation(appearance);
                gradient.startAnimation(appearance);
                setDrawable();
            }, 500);
        });

        gradient.setOnClickListener(v -> {
            if (!isEditorActive) {
                findViewById(R.id.buttons).startAnimation(appearance);
                findViewById(R.id.scroll).setVisibility(View.VISIBLE);
                findViewById(R.id.buttons).setVisibility(View.VISIBLE);
                isEditorActive = true;
            } else {
                findViewById(R.id.buttons).startAnimation(disappearance);
                findViewById(R.id.scroll).setVisibility(View.GONE);
                findViewById(R.id.buttons).setVisibility(View.GONE);
                isEditorActive = false;
            }
        });

        ((Button) findViewById(R.id.addColor)).setOnClickListener(v -> {
            blockButtons();
            addColor();
            gradient.startAnimation(disappearance);
            new Handler().postDelayed(() -> {
                setDrawable();
                gradient.startAnimation(appearance);
            }, 500);
            changeColorTypeButton.setText("Color " + (activeNow + 2));
        });

        // TODO протестировать / улучшить
        ((Button) findViewById(R.id.deleteColor)).setOnClickListener(v -> {
            if (colors.size() > 2) {
                Handler handler = new Handler();
                gradient.startAnimation(disappearance);
                blockButtons();

                if (activeNow == colors.size() - 1 && !isChangingSettings) {
                    colors.get(activeNow).startAnimation(disappearance);
                    handler.postDelayed(() -> {
                        colors.get(activeNow - 1).startAnimation(appearance);
                        colors.get(activeNow - 1).setVisibility(View.VISIBLE);
                        colors.get(activeNow).setVisibility(View.GONE);
                        activeNow--;
                    }, 500);
                } else {
                    if (activeNow == colors.size() - 1) {
                        activeNow--;
                    }
                }
                if (activeNow == colors.size() - 2) {
                    changeColorTypeButton.setText("Color " + 1);
                }

                handler.postDelayed(() -> {
                    colors.remove(colors.size() - 1);
                    setDrawable();
                    gradient.startAnimation(appearance);
                }, 500);
            }
        });

        changeColorTypeButton.setOnClickListener(v -> {
            Handler handler = new Handler();
            blockButtons();

            if (isChangingSettings) {
                settings.startAnimation(disappearance);
                handler.postDelayed(() -> settings.setVisibility(View.GONE), 500);
                isChangingSettings = false;
            } else {
                colors.get(activeNow).startAnimation(disappearance);
                handler.postDelayed(() -> colors.get(activeNow).setVisibility(View.GONE), 500);
            }

            handler.postDelayed(() -> {
                colors.get((activeNow + 1) % colors.size()).startAnimation(appearance);
                colors.get((activeNow + 1) % colors.size()).setVisibility(View.VISIBLE);
                changeColorTypeButton.setText("Color " + ((activeNow + 2) % colors.size() + 1));
                activeNow = (activeNow + 1) % colors.size();
            }, 500);
        });

        changeSettingsButton.setOnClickListener(v -> {
            Handler handler = new Handler();
            blockButtons();

            if (isChangingSettings) {
                settings.startAnimation(disappearance);
                handler.postDelayed(() -> {
                    settings.setVisibility(View.GONE);
                    colors.get(activeNow).startAnimation(appearance);
                    colors.get(activeNow).setVisibility(View.VISIBLE);
                }, 500);
                isChangingSettings = false;
            } else {
                colors.get(activeNow).startAnimation(disappearance);
                handler.postDelayed(() -> {
                    colors.get(activeNow).setVisibility(View.GONE);
                    settings.startAnimation(appearance);
                    settings.setVisibility(View.VISIBLE);
                }, 500);
                isChangingSettings = true;
            }
        });

        ((Button) settings.findViewById(R.id.saveGradient)).setOnClickListener(v -> saveDrawable(this));

        updateValuesFromSeekBar();
        setDrawable();

        Toast.makeText(getApplicationContext(),
                "Click on the gradient to change it", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBackPressed() {

        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.dialog_save_gradient, null);
        ((TextView) view.findViewById(R.id.title)).setText("Would you like to save the gradient?");
        ((TextView) view.findViewById(R.id.positive)).setOnClickListener(v -> {
            if (saveDrawable(this)) {
                startActivity(new Intent(GradientActivity.this, MainActivity.class));
                finish();
            }
        });

        ((TextView) view.findViewById(R.id.negative)).setOnClickListener(v -> {
            startActivity(new Intent(GradientActivity.this, MainActivity.class));
            finish();
        });

        Dialog dialog = new Dialog(this);
        dialog.setContentView(view);
        dialog.show();
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    private void initialComponent() {
        gradient = findViewById(R.id.gradient);
        colorEditLayout = findViewById(R.id.colorEditLayout);
        colors = new ArrayList<>();

        onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
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

        addColor();
        addColor();

        settings = getLayoutInflater().inflate(R.layout.layout_edit_gradient, null);

        settings.setVisibility(View.GONE);

        linear = settings.findViewById(R.id.linear);
        radial = settings.findViewById(R.id.radial);
        sweep = settings.findViewById(R.id.sweep);

        colorEditLayout.addView(settings);

        changeColorTypeButton = findViewById(R.id.changeColorType);
        changeSettingsButton = findViewById(R.id.changeSettings);

        ((SeekBar) settings.findViewById(R.id.seekBarRadialRadius)).setProgress(25);
        ((SeekBar) settings.findViewById(R.id.seekBarRadialX)).setProgress(50);
        ((SeekBar) settings.findViewById(R.id.seekBarRadialY)).setProgress(50);
        ((SeekBar) settings.findViewById(R.id.seekBarSweepX)).setProgress(50);
        ((SeekBar) settings.findViewById(R.id.seekBarSweepY)).setProgress(50);
        isChangingSettings = false;
        isEditorActive = false;
        activeNow = 0;
    }

//    private void updateValuesFromEditText() {
//        for (View x : colors) {
//            if (((EditText) x.findViewById(R.id.editTextRed)).getText().toString().isEmpty()) {
//                ((EditText) x.findViewById(R.id.editTextRed)).setText("0");
//            }
//            if (((EditText) x.findViewById(R.id.editTextGreen)).getText().toString().isEmpty()) {
//                ((EditText) x.findViewById(R.id.editTextGreen)).setText("0");
//            }
//            if (((EditText) x.findViewById(R.id.editTextBlue)).getText().toString().isEmpty()) {
//                ((EditText) x.findViewById(R.id.editTextBlue)).setText("0");
//            }
//            ((SeekBar) x.findViewById(R.id.seekBarRed)).setProgress(Integer.parseInt(((EditText) x.findViewById(R.id.editTextRed)).getText().toString()));
//            ((SeekBar) x.findViewById(R.id.seekBarGreen)).setProgress(Integer.parseInt(((EditText) x.findViewById(R.id.editTextGreen)).getText().toString()));
//            ((SeekBar) x.findViewById(R.id.seekBarBlue)).setProgress(Integer.parseInt(((EditText) x.findViewById(R.id.editTextBlue)).getText().toString()));
//        }
//    }

    private void updateValuesFromSeekBar() {
        for (View x : colors) {
            ((EditText) x.findViewById(R.id.editTextRed)).setText(String.valueOf(((SeekBar) x.findViewById(R.id.seekBarRed)).getProgress()));
            ((EditText) x.findViewById(R.id.editTextGreen)).setText(String.valueOf(((SeekBar) x.findViewById(R.id.seekBarGreen)).getProgress()));
            ((EditText) x.findViewById(R.id.editTextBlue)).setText(String.valueOf(((SeekBar) x.findViewById(R.id.seekBarBlue)).getProgress()));
        }
        ((EditText) settings.findViewById(R.id.editTextRadialRadius)).setText(String.valueOf(((SeekBar) settings.findViewById(R.id.seekBarRadialRadius)).getProgress() * 10));
        ((EditText) settings.findViewById(R.id.editTextRadialX)).setText(String.valueOf(((SeekBar) settings.findViewById(R.id.seekBarRadialX)).getProgress()));
        ((EditText) settings.findViewById(R.id.editTextRadialY)).setText(String.valueOf(((SeekBar) settings.findViewById(R.id.seekBarRadialY)).getProgress()));
        ((EditText) settings.findViewById(R.id.editTextSweepX)).setText(String.valueOf(((SeekBar) settings.findViewById(R.id.seekBarSweepX)).getProgress()));
        ((EditText) settings.findViewById(R.id.editTextSweepY)).setText(String.valueOf(((SeekBar) settings.findViewById(R.id.seekBarSweepY)).getProgress()));
    }

    @SuppressLint("SetTextI18n")
    private void addColor() {
        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.layout_edit_color, null);
        ((TextView) view.findViewById(R.id.title)).setText("Color " + (colors.size() + 1));
        view.setVisibility(View.GONE);

        ((SeekBar) view.findViewById(R.id.seekBarRed)).setOnSeekBarChangeListener(onSeekBarChangeListener);
        ((SeekBar) view.findViewById(R.id.seekBarGreen)).setOnSeekBarChangeListener(onSeekBarChangeListener);
        ((SeekBar) view.findViewById(R.id.seekBarBlue)).setOnSeekBarChangeListener(onSeekBarChangeListener);

        colors.add(view);
        colorEditLayout.addView(view);
    }

    @SuppressLint("NonConstantResourceId")
    private GradientDrawable createDrawable() {
        int[] rgbColors = new int[colors.size()];
        for (int i = 0; i < colors.size(); i++) {
            rgbColors[i] = Color.argb(255,
                    ((SeekBar) colors.get(i).findViewById(R.id.seekBarRed)).getProgress(),
                    ((SeekBar) colors.get(i).findViewById(R.id.seekBarGreen)).getProgress(),
                    ((SeekBar) colors.get(i).findViewById(R.id.seekBarBlue)).getProgress()
            );
        }
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, rgbColors);

        drawable.setShape(GradientDrawable.RECTANGLE);
        switch (((RadioGroup) settings.findViewById(R.id.radioGroup)).getCheckedRadioButtonId()) {
            case R.id.linear:
                switch (((SeekBar) settings.findViewById(R.id.seekBarAngle)).getProgress()) {
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

        return drawable;
    }

    private void setDrawable() {
        gradient.setForeground(createDrawable());
    }

    private boolean saveDrawable(Activity activity) {
        int permissionStatus = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            Bitmap bitmap = drawableToBitmap(createDrawable());

            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            try {
                FileOutputStream outStream = new FileOutputStream(extStorageDirectory + "/gradient.png");
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                outStream.flush();
                outStream.close();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            return true;
        } else {
            ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            return false;
        }
    }

    public Bitmap drawableToBitmap(GradientDrawable drawable) {
        Bitmap bitmap;

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(720, 720, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

//    @Override
//    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            View v = getCurrentFocus();
//            if (v instanceof EditText) {
//                v.clearFocus();
//                InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//            }
//            updateValuesFromEditText();
//        }
//        return super.dispatchTouchEvent(event);
//    }

    private void blockButtons() {
        ((Button) findViewById(R.id.addColor)).setEnabled(false);
        ((Button) findViewById(R.id.deleteColor)).setEnabled(false);
        changeColorTypeButton.setEnabled(false);
        changeSettingsButton.setEnabled(false);
        new Handler().postDelayed(() -> {
            ((Button) findViewById(R.id.addColor)).setEnabled(true);
            ((Button) findViewById(R.id.deleteColor)).setEnabled(true);
            changeColorTypeButton.setEnabled(true);
            changeSettingsButton.setEnabled(true);
        }, 500);
    }
}