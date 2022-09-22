package com.mjcdouai.go4lunch.ui;

import android.os.Bundle;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.mjcdouai.go4lunch.R;
import com.mjcdouai.go4lunch.databinding.ActivitySettingsBinding;
import com.mjcdouai.go4lunch.utils.SharedPrefsHelper;

public class SettingsActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private ActivitySettingsBinding mBinding;
    protected void onCreate(Bundle savedInstanceState) {
        mBinding = ActivitySettingsBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(mBinding.getRoot());
        SharedPrefsHelper sharedPrefsHelper = new SharedPrefsHelper(getApplicationContext());
        mBinding.rangePicker.setProgress(sharedPrefsHelper.getRadius());
        mBinding.notificationSwitch.setChecked(sharedPrefsHelper.getNotification());
        mBinding.rangePicker.setKeyProgressIncrement(100);
        mBinding.rangeNumber.setText(getString(R.string.meters,mBinding.rangePicker.getProgress()));

        mBinding.rangePicker.setOnSeekBarChangeListener(this);
        mBinding.saveButton.setOnClickListener(view -> {

            sharedPrefsHelper.setRadius(mBinding.rangePicker.getProgress());
            sharedPrefsHelper.setNotification(mBinding.notificationSwitch.isChecked());
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