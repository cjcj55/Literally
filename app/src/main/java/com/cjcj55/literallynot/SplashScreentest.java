package com.cjcj55.literallynot;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjcj55.literallynot.databinding.SplashscreentestuiBinding;

public class SplashScreentest extends AppCompatActivity {


    private SplashscreentestuiBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreentestui);

        new Handler().postDelayed(new Runnable() {
            Intent iHome = new Intent(SplashScreentest.this, MainActivity.class);
            @Override
            public void run() {
                startActivity(iHome);
                finish();
            }
        }, 4000);

            }

    }
