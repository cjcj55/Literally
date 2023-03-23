package com.cjcj55.literallynot;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class TestFragment extends Fragment implements RecognitionListener{
    private TextView speechTest;
    private Button testBtn;
    public static final int REQUEST_RECORD_AUDIO_PERMISSION_CODE = 1;
    private SpeechRecognizer speechRecognizer;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.test_page, container, false);
        speechTest = view.findViewById(R.id.speechTest) ;
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            System.out.print("Broken");
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.RECORD_AUDIO},
                    REQUEST_RECORD_AUDIO_PERMISSION_CODE);
        } else {
            System.out.print("Hello World");
            initializeSpeechRecognizer();
        }
        return view;
    }

    private void initializeSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(getContext())) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());
            speechRecognizer.setRecognitionListener(this);
            Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
            speechRecognizer.startListening(speechRecognizerIntent);
        }
    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float v) {

    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int i) {

    }

    @Override
    public void onResults(Bundle bundle) {
        System.out.println("Hello World");
        ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (matches != null) {
            for (String match : matches) {
                if (match.toLowerCase().contains("literally")) {
                    speechTest.setText(match);
                    break;
                }
            }
        }
        speechRecognizer.startListening(createSpeechRecognizerIntent());
    }
    @Override
    public void onResume() {
        super.onResume();
        if (speechRecognizer != null) {
            speechRecognizer.startListening(createSpeechRecognizerIntent());
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (speechRecognizer != null) {
            speechRecognizer.stopListening();
            speechRecognizer.cancel();
            speechRecognizer.destroy();
            speechRecognizer = null;
        }
    }
    private Intent createSpeechRecognizerIntent() {
        Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        return speechRecognizerIntent;
    }

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }

}