package com.skysoftsolution.todolistreminder.microphoneVoice;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.skysoftsolution.todolistreminder.databinding.ActivitySpeechBinding;
import com.skysoftsolution.todolistreminder.interfaces.SpeechRecognitionCallback;
import com.skysoftsolution.todolistreminder.services.SpeechRecognitionService;

import java.util.ArrayList;

public class SpeechActivity extends AppCompatActivity {

    private static final int REQUEST_RECORD_PERMISSION = 100;
    private boolean listening = false;
    private ActivitySpeechBinding binding;
    private SpeechRecognitionService speechService;
    private boolean isBound = false;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SpeechRecognitionService.SpeechBinder binder = (SpeechRecognitionService.SpeechBinder) service;
            speechService = binder.getService();
            isBound = true;
            speechService.setCallback(speechCallback);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    SpeechRecognitionCallback speechCallback =
            new SpeechRecognitionCallback() {
                @Override
                public void onReadyForSpeech(Bundle params) {
                    // Handle onReadyForSpeech callback
                }

                @Override
                public void onBeginningOfSpeech() {
                    // Handle onBeginningOfSpeech callback
                }

                @Override
                public void onRmsChanged(float rmsdB) {
                    binding.progressBar1.setProgress((int) rmsdB);
                }

                @Override
                public void onBufferReceived(byte[] buffer) {
                    // Handle onBufferReceived callback
                }

                @Override
                public void onEndOfSpeech() {
                    // Handle onEndOfSpeech callback
                }

                @Override
                public void onError(int error) {
                    String errorMessage = getErrorText(error);
                    binding.textView1.setText(errorMessage);
                }

                @Override
                public void onResults(Bundle results) {
                    ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    String text = "";
                    for (String result : matches)
                        text += result + "\n";
                    binding.textView1.setText(text);
                }

                @Override
                public void onPartialResults(Bundle partialResults) {
                    // Handle onPartialResults callback
                }

                @Override
                public void onEvent(int eventType, Bundle params) {
                    // Handle onEvent callback
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpeechBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toggleButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    listening = true;
                    binding.progressBar1.setVisibility(View.VISIBLE);
                    binding.progressBar1.setIndeterminate(true);
                    ActivityCompat.requestPermissions(SpeechActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_PERMISSION);
                } else {
                    listening = false;
                    binding.progressBar1.setIndeterminate(false);
                    binding.progressBar1.setVisibility(View.INVISIBLE);
                    if (isBound) {
                        speechService.stopListening();
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(SpeechActivity.this, "Start talking...", Toast.LENGTH_SHORT).show();
                if (isBound) {
                    speechService.startListening();
                }
            } else {
                Toast.makeText(SpeechActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, SpeechRecognitionService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
    }

    private String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                if (isBound) {
                    speechService.stopListening();
                }
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "Error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "";
                break;
        }
        return message;
    }
}