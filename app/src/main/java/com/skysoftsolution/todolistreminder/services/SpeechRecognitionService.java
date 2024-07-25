package com.skysoftsolution.todolistreminder.services;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import com.skysoftsolution.todolistreminder.interfaces.SpeechRecognitionCallback;

public class SpeechRecognitionService extends Service implements RecognitionListener {
    private static final String LOG_TAG = "SpeechRecognitionService";
    private final IBinder binder = new SpeechBinder();
    private SpeechRecognizer speechRecognizer;
    private Intent recognizerIntent;
    private SpeechRecognitionCallback callback;

    public class SpeechBinder extends Binder {
        public SpeechRecognitionService getService() {
            return SpeechRecognitionService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(this);

        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "hi-IN,en-US");
    }

    public void startListening() {
        speechRecognizer.startListening(recognizerIntent);
    }

    public void stopListening() {
        speechRecognizer.stopListening();
    }

    public void setCallback(SpeechRecognitionCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        if (callback != null) callback.onReadyForSpeech(params);
    }

    @Override
    public void onBeginningOfSpeech() {
        if (callback != null) callback.onBeginningOfSpeech();
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        if (callback != null) callback.onRmsChanged(rmsdB);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        if (callback != null) callback.onBufferReceived(buffer);
    }

    @Override
    public void onEndOfSpeech() {
        if (callback != null) callback.onEndOfSpeech();
    }

    @Override
    public void onError(int error) {
        if (callback != null) callback.onError(error);
    }

    @Override
    public void onResults(Bundle results) {
        if (callback != null) callback.onResults(results);
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        if (callback != null) callback.onPartialResults(partialResults);
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        if (callback != null) callback.onEvent(eventType, params);
    }

}