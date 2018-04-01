package com.chierici.speechrecog;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.vikramezhil.droidspeech.DroidSpeech;
import com.vikramezhil.droidspeech.OnDSListener;

import java.util.List;

public class SpeechService extends Service implements OnDSListener {

    private DroidSpeech droidSpeech;
    private IBinder binder = new SpeechBinder(this);
    public final String TAG_LOG = "SpeechService";


    public SpeechService() {
    }

    @Override
    public void onCreate() {
        Log.d(TAG_LOG, "onCreate");
        super.onCreate();

        initDroidSpeech();
        startListening();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG_LOG, "onDestroy");
        stopListening();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String message = "SpeechService onStartCommand() method.";
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        Log.d(TAG_LOG, message);

        return super.onStartCommand(intent, flags, startId);
    }

    private void initDroidSpeech() {
        Log.d(TAG_LOG, "initDroidSpeech");

        droidSpeech = new DroidSpeech(this, null);
        droidSpeech.setContinuousSpeechRecognition(true);
        droidSpeech.setOnDroidSpeechListener(this);
        droidSpeech.setShowRecognitionProgressView(false);
        droidSpeech.setOneStepResultVerify(false);
    }

    private void startListening() {
        Log.d(TAG_LOG, "startListening");
        droidSpeech.startDroidSpeechRecognition();
    }

    private void stopListening() {
        Log.d(TAG_LOG, "stopListening");
        droidSpeech.closeDroidSpeechOperations();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDroidSpeechSupportedLanguages(String currentSpeechLanguage, List<String> supportedSpeechLanguages) {
        Log.i(TAG_LOG, "Current speech language = " + currentSpeechLanguage);
        Log.i(TAG_LOG, "Supported speech languages = " + supportedSpeechLanguages.toString());

        if (supportedSpeechLanguages.contains("pt-BR")) {
            // Setting the droid speech preferred language as tamil if found
            droidSpeech.setPreferredLanguage("pt-BR");
        }
    }

    @Override
    public void onDroidSpeechRmsChanged(float rmsChangedValue) {
        //Log.i(TAG_LOG, "Rms change value = " + rmsChangedValue);
    }

    @Override
    public void onDroidSpeechLiveResult(String liveSpeechResult) {
        Log.i(TAG_LOG, "Live speech result = " + liveSpeechResult);
    }

    @Override
    public void onDroidSpeechFinalResult(String finalSpeechResult) {
        Log.d(TAG_LOG, "onDroidSpeechFinalResult: " + finalSpeechResult);
        Toast.makeText(this, finalSpeechResult, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDroidSpeechClosedByUser() {
        Log.d(TAG_LOG, "onDroidSpeechClosedByUser");
        Toast.makeText(this, TAG_LOG + " onDroidSpeechClosedByUser", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDroidSpeechError(String errorMsg) {
        Log.d(TAG_LOG, "onDroidSpeechError");
        // Speech error
        Toast.makeText(this, TAG_LOG + " " + errorMsg, Toast.LENGTH_LONG).show();
    }
}
