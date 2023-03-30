package com.cjcj55.literallynot;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Locale;
public class ForegroundService extends Service implements RecognitionListener{
private static final String TAG = "MyService";
private SpeechRecognizer speechRecognizer;
private boolean isRecording = false;

@Override
public void onCreate() {
        super.onCreate();
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(this);


        }

@Override
public int onStartCommand(Intent intent, int flags, int startId) {
        speechRecognizer.startListening(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH));
        return START_STICKY;
        }

@Override
public void onDestroy() {
        super.onDestroy();
        if (speechRecognizer != null) {
        speechRecognizer.destroy();
        }
        }

@Override
public void onReadyForSpeech(Bundle params) {}

@Override
public void onBeginningOfSpeech() {
        Log.i(TAG, "Speech started");
        }

@Override
public void onRmsChanged(float rmsdB) {}

@Override
public void onBufferReceived(byte[] buffer) {}

@Override
public void onEndOfSpeech() {
        Log.i(TAG, "Speech ended");
        }

@Override
public void onError(int error) {
        Log.i(TAG, "Speech error: " + error);
        speechRecognizer.startListening(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH));
        }

@Override
public void onResults(Bundle results) {
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        for (String match : matches) {
        if (match.toLowerCase().contains("literally")) {
        Log.i(TAG, "Detected 'literally'");
        if (!isRecording) {
        isRecording = true;
        startRecording();
        }
        }
        }
        speechRecognizer.startListening(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH));
        }

@Override
public void onPartialResults(Bundle partialResults) {}

@Override
public void onEvent(int eventType, Bundle params) {}

private void startRecording() {
        MediaRecorder mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(getExternalCacheDir().getAbsolutePath() + "/recording.3gp");
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
        mediaRecorder.prepare();
        } catch (IOException e) {
        Log.e(TAG, "MediaRecorder prepare failed: " + e.getMessage());
        isRecording = false;
        return;
        }
        mediaRecorder.start();
        new Handler().postDelayed(new Runnable() {
@Override
public void run() {
        mediaRecorder.stop();
        mediaRecorder.release();
        isRecording = false;
        }
        }, 5000);
        }

@Nullable
@Override
public IBinder onBind(Intent intent) {
        return null;
        }
        }
