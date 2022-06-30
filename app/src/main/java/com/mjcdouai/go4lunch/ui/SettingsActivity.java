package com.mjcdouai.go4lunch.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import com.mjcdouai.go4lunch.R;
import com.mjcdouai.go4lunch.databinding.ActivitySettingsBinding;
import com.mjcdouai.go4lunch.utils.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SettingsActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private ActivitySettingsBinding mBinding;
    protected void onCreate(Bundle savedInstanceState) {
        mBinding = ActivitySettingsBinding.inflate(getLayoutInflater());
        String[] languages = getResources().getStringArray(R.array.language_array);
        List<String> languagesList = new ArrayList<>();
        Collections.addAll(languagesList,languages);
        super.onCreate(savedInstanceState);
        setContentView(mBinding.getRoot());
        SharedPrefsHelper sharedPrefsHelper = new SharedPrefsHelper(getApplicationContext());
        mBinding.rangePicker.setProgress(sharedPrefsHelper.getRange());
        mBinding.notificationSwitch.setChecked(sharedPrefsHelper.getNotification());
        mBinding.languageSpinner.setSelection(languagesList.indexOf(sharedPrefsHelper.getLanguage()));
        mBinding.rangePicker.setKeyProgressIncrement(100);
        mBinding.rangeNumber.setText(getString(R.string.meters,mBinding.rangePicker.getProgress()));

        mBinding.rangePicker.setOnSeekBarChangeListener(this);
        mBinding.saveButton.setOnClickListener(view -> {

            sharedPrefsHelper.setRange(mBinding.rangePicker.getProgress());
            sharedPrefsHelper.setLanguage(mBinding.languageSpinner.getSelectedItem().toString());
            sharedPrefsHelper.setNotification(mBinding.notificationSwitch.getShowText());
            finish();
        });
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        mBinding.rangeNumber.setText(getString(R.string.meters,i));

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}