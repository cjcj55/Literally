package com.cjcj55.literallynot;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

public class ForegroundService extends Service implements Runnable {

    private static final String TAG = "ForegroundService";
    private static final int NOTIFICATION_ID = 1;

    private static AudioRecord audioRecord = null;
    private static final int SAMPLE_RATE = 44100;
    private static final long  LONG_SAMPLE_RATE = SAMPLE_RATE;
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;

    private static final int BUFFER_SIZE_SECONDS = 5;
    private static final int BUFFER_SIZE_BYTES = BUFFER_SIZE_SECONDS * SAMPLE_RATE * 2 * 1;  // 1 second of 44100, 2-byte (16 bit) audio is 88200 bytes io

    private static final int MIN_BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE,
            CHANNEL_CONFIG, AUDIO_FORMAT);

    private final AtomicBoolean isRecording = new AtomicBoolean(false);
    private Thread audioReaderThread = null;



    //////////////////////////////////
    public class AudioReaderRunnable implements Runnable {
        // This thread monitors the audio recording buffer.
        // It reads the data after 10 seconds of audio are filled.
        // It saves the data to a raw file.
        // It saves data to to a wav format also with the Wav header so it can be tested in audio players.
        // It continually saves files recording1, recording2, recording3, recording4, recording5.  You can append files together to assemble bigger files.
        @Override
        public void run() {
            int fileNum = 1;
            Log.d(TAG, "RecordingThread started");

            final ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE_BYTES);
            Log.d(TAG, "RecordingThread Buffer size is " + buffer.remaining() + " bytes ");


            float secondsOfRecording = buffer.remaining() / SAMPLE_RATE / 1 / 2;  // 1 channel,  2 bytes (16 bits) per channel

            Log.d(TAG, "RecordingThread Buffer size is " + secondsOfRecording + " seconds  ");

            while (isRecording.get()) {
                //Log.d(TAG, "RecordingThread isRecording");  // Just a sanity check.  This will flood the log.
                int result = audioRecord.read(buffer, BUFFER_SIZE_BYTES);  // This read() will block the thread until the buffer is full.  (10 seconds or whatever)
                File fileRaw = new File(getApplicationContext().getExternalFilesDir(null), "recording" + fileNum + ".pcm");
                File fileWav = new File(getApplicationContext().getExternalFilesDir(null), "recording" + fileNum + ".wav");
                // saves files to /Android/data/com.cjcj55.literallnot/files

                try {
                    FileOutputStream rawOutputStream = new FileOutputStream(fileRaw);
                    FileOutputStream wavOutputStream = new FileOutputStream(fileWav);

                    if (result < 0) {
                        throw new RuntimeException("Handle the error of audio read buffer fail " + getBufferReadFailureReason(result));
                    }
                    // process the audio data
                    rawOutputStream.write(buffer.array(), 0, BUFFER_SIZE_BYTES);

                    wavOutputStream.write(wavHeader( buffer ));
                    wavOutputStream.write( buffer.array(), 0, BUFFER_SIZE_BYTES );

                    buffer.clear();
                    rawOutputStream.close();
                    wavOutputStream.close();


                    Log.d(TAG, " Wrote file " + fileRaw.getName());
                    Log.d(TAG,  " Wrote file " + fileWav.getName());
                    fileNum++;
                    if (fileNum > 5) {
                        fileNum = 1;
                    }

                } catch (IOException ex) {
                    throw new RuntimeException("Could not write file " + fileRaw.getAbsolutePath());
                }

            }
        }

        private byte[]  wavHeader(ByteBuffer buffer)
        {

            byte[] header = new byte[44];
            int totalDataLen = buffer.array().length + 36;

            header[0] = 'R';  // RIFF/WAVE header
            header[1] = 'I';
            header[2] = 'F';
            header[3] = 'F';
            header[4] = (byte) (totalDataLen & 0xff);
            header[5] = (byte) ((totalDataLen >> 8) & 0xff);
            header[6] = (byte) ((totalDataLen >> 16) & 0xff);
            header[7] = (byte) ((totalDataLen >> 24) & 0xff);
            header[8] = 'W';
            header[9] = 'A';
            header[10] = 'V';
            header[11] = 'E';
            header[12] = 'f';  // 'fmt ' chunk
            header[13] = 'm';
            header[14] = 't';
            header[15] = ' ';
            header[16] = 16;  // 4 bytes: size of 'fmt ' chunk
            header[17] = 0;
            header[18] = 0;
            header[19] = 0;
            header[20] = 1;  // format = 1
            header[21] = 0;
            header[22] = (byte) 1;
            header[23] = 0;
            header[24] = (byte) (LONG_SAMPLE_RATE & 0xff);
            header[25] = (byte) ((LONG_SAMPLE_RATE >> 8) & 0xff);
            header[26] = (byte) ((LONG_SAMPLE_RATE >> 16) & 0xff);
            header[27] = (byte) ((LONG_SAMPLE_RATE >> 24) & 0xff);
            header[28] = (byte) (LONG_SAMPLE_RATE * 2 & 0xff);
            header[29] = (byte) ((LONG_SAMPLE_RATE * 2 >> 8) & 0xff);
            header[30] = (byte) ((LONG_SAMPLE_RATE * 2 >> 16) & 0xff);
            header[31] = (byte) ((LONG_SAMPLE_RATE * 2 >> 24) & 0xff);
            header[32] = (byte) (2 * 16 / 8);  // block align
            header[33] = 0;
            header[34] = 16;  // bits per sample
            header[35] = 0;
            header[36] = 'd';
            header[37] = 'a';
            header[38] = 't';
            header[39] = 'a';
            header[40] = (byte) (buffer.array().length & 0xff);
            header[41] = (byte) ((buffer.array().length >> 8) & 0xff);
            header[42] = (byte) ((buffer.array().length >> 16) & 0xff);
            header[43] = (byte) ((buffer.array().length >> 24) & 0xff);

           return  header;
        }

        private String getBufferReadFailureReason(int errorCode) {
            switch (errorCode) {
                case AudioRecord.ERROR_INVALID_OPERATION:
                    return "ERROR_INVALID_OPERATION";
                case AudioRecord.ERROR_BAD_VALUE:
                    return "ERROR_BAD_VALUE";
                case AudioRecord.ERROR_DEAD_OBJECT:
                    return "ERROR_DEAD_OBJECT";
                case AudioRecord.ERROR:
                    return "ERROR";
                default:
                    return "Unknown (" + errorCode + ")";
            }
        }
    }
    //////////////////////////////////
    void startRecorder()
    {
        audioRecord.startRecording();
        Log.d(TAG, "AudioRecord startRecording");
        isRecording.set(true);
    }

    void stopRecorder()
    {
        audioRecord.stop();
        Log.d(TAG, "AudioRecord stopRecording");
        isRecording.set(false);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Create a notification for the foreground service
        final String CHANNEL_ID = "Foreground Service";


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel("1369420", "Literally Not", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }


        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_LOW);

            getSystemService(NotificationManager.class).createNotificationChannel(channel);
            Notification.Builder notification = new Notification.Builder(this, CHANNEL_ID)
                    .setContentText("Hello")
                    .setContentTitle("Blah");


            // Start the foreground service with the notification
            startForeground(1001, notification.build());
        }



        if (audioRecord == null) {
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, BUFFER_SIZE_BYTES);
            Log.d(TAG, "onStartCommand: AudioRecord created");
            startRecorder();
            audioReaderThread  = new Thread( new AudioReaderRunnable(), "recording thread");
            audioReaderThread.start();
        }

        // https://developer.android.com/reference/android/speech/SpeechRecognizer
        //    " As such this API is not intended to be used for continuous recognition, which would consume a significant amount of battery and bandwidth"

        // Also:  per documentation, this must be invoked from the main thread

        SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        final Intent spIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        spIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        spIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        spIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());




        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
                Log.d(TAG, "onReadyForSpeech: ");
            }

            @Override
            public void onBeginningOfSpeech() {
                Log.d(TAG, "onBeginningOfSpeech: ");
            }

            @Override
            public void onRmsChanged(float v) {
                Log.d(TAG, "onRmsChanged: ");
            }

            @Override
            public void onBufferReceived(byte[] bytes) {
                Log.d(TAG, "onBufferReceived: ");
            }

            @Override
            public void onEndOfSpeech() {
                Log.d(TAG, "onEndOfSpeech: ");
            }

            @Override
            public void onError(int i) {
                Log.d(TAG, "onError: " + i );
            }

            @Override
            public void onResults(Bundle results) {
                Log.d(TAG, "onResults: ");
                //Inside of here make sure to to add the circular buffer functionality
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && matches.size() > 0) {
                    String spokenText = matches.get(0);
                    //System.out.println(spokenText);
                    Log.d(TAG, "onResults: " + spokenText);
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {
                Log.d(TAG, "onPartialResults: ");

            }

            @Override
            public void onEvent(int i, Bundle bundle) {
                Log.d(TAG, "onEvent: ");
            }
        });


        // You cannot feed speechRecognizer a file or buffer of audio.
        // You also cannot simultaneously run the recognizer while recording.
        //speechRecognizer.startListening(intent);

        return START_NOT_STICKY;
    } // end onstartCommand

    @Override
    public IBinder onBind(Intent intent) {
        // This service does not support binding, so return null
        return null;
    }

    @Override
    public void run() {

    }
    //In order to get context for file location was required to

}
