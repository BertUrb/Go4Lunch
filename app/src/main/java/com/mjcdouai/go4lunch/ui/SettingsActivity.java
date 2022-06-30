package com.mjcdouai.go4lunch.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.SeekBar;

import com.mjcdouai.go4lunch.R;
import com.mjcdouai.go4lunch.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private ActivitySettingsBinding mBinding;
    protected void onCreate(Bundle savedInstanceState) {
        mBinding = ActivitySettingsBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(mBinding.getRoot());
        mBinding.rangePicker.setKeyProgressIncrement(100);
        mBinding.rangeNumber.setText(getString(R.string.meters,mBinding.rangePicker.getProgress()));

        mBinding.rangePicker.setOnSeekBarChangeListener(this);
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