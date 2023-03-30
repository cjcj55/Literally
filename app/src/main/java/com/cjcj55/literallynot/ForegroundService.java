package com.cjcj55.literallynot;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Locale;

public class ForegroundService extends Service implements Runnable{

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

            // Start the foreground service with the notification
            startForeground(1001, notification.build());
        }
        // Do your work here, such as recording audio
        SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        final Intent spIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        spIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        spIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        spIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        //Initializes the MediaRecorder for audio recording
        PackageManager m = getPackageManager();
        String s = getPackageName();
        PackageInfo p = null;
        try {
            p = m.getPackageInfo(s, 0);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        s = p.applicationInfo.dataDir;
        System.out.println(s);
        System.out.println(m);
        System.out.println(p);
        try {
            AudioRecorder recorder = new AudioRecorder();
            recorder.startRecording();
          //  recorder.stopRecording();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //speechRecognizer.startListening(spIntent);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isRecording) {
            stopRecording();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private void startStreaming() {
        final Handler handler = new Handler(Looper.getMainLooper());
        Thread streamThread = new Thread(() -> {
            try {
                Log.d(TAG, "Creating the buffer of size " + BUFFER_SIZE);
                short[] buffer = new short[BUFFER_SIZE];

                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_AUDIO);

                Log.d(TAG, "Creating the AudioRecord");
                recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, RECORDING_RATE, CHANNEL, FORMAT, BUFFER_SIZE * 10);

                Log.d(TAG, "AudioRecord recording...");
                recorder.startRecording();

                while (!Thread.interrupted()) {
                    int readSize = recorder.read(buffer, 0, buffer.length);
                    double maxAmplitude = 0;
                    for (int i = 0; i < readSize; i++) {
                        if (Math.abs(buffer[i]) > maxAmplitude) {
                            maxAmplitude = Math.abs(buffer[i]);
                        }
                    }
                    double db = 0;
                    if (maxAmplitude != 0) {
                        db = 20.0 * Math.log10(maxAmplitude / 32767.0) + 90;
                    }
                    Log.d(TAG, "Max amplitude: " + maxAmplitude + " ; DB: " + db);

                    String bufferString = new String(buffer.toString());
                    if (bufferString.toLowerCase().contains("literally")) {
                        if (!isRecording) {
                            isRecording = true;
                            startRecording();
                            handler.postDelayed(() -> {
                                stopRecording();
                                isRecording = false;
                            }, FILE_LENGTH_IN_SECONDS * 1000);
                        }
                    }
                }

                Log.d(TAG, "AudioRecord finished recording");
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e);
            }
        });

        streamThread.start();
    }

    private void startRecording() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "WRITE_EXTERNAL_STORAGE permission not granted");
            return;
        }

        File recordingFile = new File(Environment.getExternalStorageDirectory(), FILE_NAME);
        if (recordingFile.exists()) {
            recordingFile.delete();
        }

        try {
            FileOutputStream outputStream = new FileOutputStream(recordingFile);
            recorder.startRecording();
            executorService.submit(new RecordingTask(outputStream));
        } catch (IOException e) {
            Log.e(TAG, "Error starting recording: " + e.getMessage());
        }
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    private void startLoop() {
        final Handler handler = new Handler(Looper.getMainLooper());
        Thread loopThread = new Thread(() -> {
            try {
                while (!Thread.interrupted()) {
                    if (!isRecording) {
                        isRecording = true;
                        startRecording();
                        handler.postDelayed(() -> {
                            stopRecording();
                            isRecording = false;
                        }, FILE_LENGTH_IN_SECONDS * 1000);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e);
            }
        });

        loopThread.start();
    }

    private void saveRecording(File recordingFile) {
        // save the recording file here
        // example: send it to a server or upload to a cloud storage service
        Log.d(TAG, "Recording saved to file: " + recordingFile.getAbsolutePath());
    }

    private class RecordingTask implements Runnable {
        private final FileOutputStream outputStream;

            }
        });
        // Stop the foreground service when your work is done
        //stopForeground(true);
       // stopSelf();

        @Override
        public void run() {
            byte[] buffer = new byte[BUFFER_SIZE];
            while (isRecording) {
                int readSize = recorder.read(buffer, 0, buffer.length);
                try {
                    outputStream.write(buffer, 0, readSize);
                } catch (IOException e) {
                    Log.e(TAG, "Error writing to file: " + e.getMessage());
                }
            }
            try {
                outputStream.flush();
                outputStream.close();
                saveRecording(new File(Environment.getExternalStorageDirectory(), FILE_NAME));
            } catch (IOException e) {
                Log.e(TAG, "Error closing file: " + e.getMessage());
            }
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.d(TAG, "onTaskRemoved() called");
        stopRecording();
        stopSelf();
    }

    @Override
    public void run() {

    }
    //In order to get context for file location was required to
    public class AudioRecorder{

        private MediaRecorder recorder;
        private ByteArrayOutputStream output;
        private Handler handler;
        private Runnable callback;
        //Constructor  for the Audio Recorder.  Thank you sleep deprivation
        public AudioRecorder() throws IOException{
            //Initialize the AudioRecorder variables
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_WB);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
            recorder.setAudioSamplingRate(44100);
            recorder.setAudioChannels(AudioFormat.ENCODING_DEFAULT);  //Doesn't crash program when set to DEFAUKT
            recorder.setAudioEncodingBitRate(AudioFormat.ENCODING_MP3);
            //Create the output stream for the thing
            output = new ByteArrayOutputStream();
            handler = new Handler();

        }

        public void startRecording() throws IOException{
            //outputFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/AudioRecording_" + System.currentTimeMillis() + ".mp3");
            //If we can get setOutputFile Working this will be completed
            String audioFileName = "myaudiofile.mp3";
            File outputFile = new File(getCacheDir(), audioFileName);
            recorder.setOutputFile(outputFile.getAbsolutePath());
            recorder.prepare();
            recorder.start();
            callback = new Runnable() {
                @Override
                public void run() {
                    byte[] recordBytes = output.toByteArray();
                    String recordedText = new String(recordBytes, StandardCharsets.UTF_8);
                    System.out.println(recordedText);
                    handler.postDelayed(this, 5000);
                }
            };
            handler.postDelayed(callback, 5000);

        }
        public void stopRecording(){
            recorder.stop();
            recorder.reset();
            recorder.release();
            handler.removeCallbacks(callback);
        }
    }
}
