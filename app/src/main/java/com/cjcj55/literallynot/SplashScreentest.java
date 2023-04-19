package com.cjcj55.literallynot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.cjcj55.literallynot.databinding.SplashscreentestuiBinding;

public class SplashScreentest extends AppCompatActivity {

    private SplashscreentestuiBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreentestui);



            ModelManager.getInstance().initModel(this, new ModelManager.Callback() {
                @Override
                public void onSuccess() {
                    System.out.println("models finished loading!");
                    continueToMainActivity();
                }

                @Override
                public void onFailure(Exception exception) {
                    System.out.println("model files loading failed!");
                    continueToMainActivity();
                }
            });
    }

    private void continueToMainActivity() {
        Intent iHome = new Intent(SplashScreentest.this, MainActivity.class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(iHome);
                finish();
            }
        }, 0);
    }
}