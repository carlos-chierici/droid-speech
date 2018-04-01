package com.chierici.speechrecog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vikramezhil.droidspeech.DroidSpeech;
import com.vikramezhil.droidspeech.OnDSListener;
import com.vikramezhil.droidspeech.OnDSPermissionsListener;

import java.util.List;
import java.util.Random;

/**
 * Droid Speech Example Activity
 *
 * @author Vikram Ezhil
 */

public class Activity_DroidSpeech extends Activity implements OnClickListener, OnDSListener, OnDSPermissionsListener {
    public final String TAG = "Activity_DroidSpeech";

    private DroidSpeech droidSpeech;
    private TextView finalSpeechResult;
    private ImageView start, stop;
    private Button startServiceButton,stopServiceButton;

    // MARK: Activity Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setting the layout;[.
        setContentView(R.layout.activity_droid_speech);

        // Initializing the droid speech and setting the listener
        //droidSpeech = new DroidSpeech(this, getFragmentManager());

        if (droidSpeech != null) {
            droidSpeech.setContinuousSpeechRecognition(true);
            droidSpeech.setOnDroidSpeechListener(this);
            droidSpeech.setShowRecognitionProgressView(true);
            //droidSpeech.setOneStepResultVerify(true);
            droidSpeech.setOneStepResultVerify(false);

            droidSpeech.setRecognitionProgressMsgColor(Color.WHITE);
            droidSpeech.setOneStepVerifyConfirmTextColor(Color.WHITE);
            droidSpeech.setOneStepVerifyRetryTextColor(Color.WHITE);
        }

        finalSpeechResult = findViewById(R.id.finalSpeechResult);

        start = findViewById(R.id.start);
        start.setOnClickListener(this);

        stop = findViewById(R.id.stop);
        stop.setOnClickListener(this);

        startServiceButton = findViewById(R.id.button_start_service);
        startServiceButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity_DroidSpeech.this.startSpeechService();
            }
        });

        stopServiceButton = findViewById(R.id.button_stop_service);
        stopServiceButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity_DroidSpeech.this.stopSpeechService();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (stop.getVisibility() == View.VISIBLE) {
            stop.performClick();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (stop.getVisibility() == View.VISIBLE) {
            stop.performClick();
        }
    }

    // MARK: OnClickListener Method

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start:

                // Starting droid speech
                if (droidSpeech != null)
                    droidSpeech.startDroidSpeechRecognition();

                // Setting the view visibilities when droid speech is running
                start.setVisibility(View.GONE);
                stop.setVisibility(View.VISIBLE);

                break;

            case R.id.stop:

                // Closing droid speech
                if (droidSpeech != null)
                    droidSpeech.closeDroidSpeechOperations();

                stop.setVisibility(View.GONE);
                start.setVisibility(View.VISIBLE);

                break;
        }
    }

    // MARK: DroidSpeechListener Methods

    @Override
    public void
    onDroidSpeechSupportedLanguages(String currentSpeechLanguage, List<String> supportedSpeechLanguages) {
        Log.i(TAG, "Current speech language = " + currentSpeechLanguage);
        Log.i(TAG, "Supported speech languages = " + supportedSpeechLanguages.toString());

        if (droidSpeech != null && supportedSpeechLanguages.contains("pt-BR")) {
            // Setting the droid speech preferred language as tamil if found
            droidSpeech.setPreferredLanguage("pt-BR");
            // Setting the confirm and retry text in tamil
            droidSpeech.setOneStepVerifyConfirmText("confirmar");
            droidSpeech.setOneStepVerifyRetryText("repetir");
        }
    }

    @Override
    public void onDroidSpeechRmsChanged(float rmsChangedValue) {
        //Log.i(TAG_LOG, "Rms change value = " + rmsChangedValue);
    }

    @Override
    public void onDroidSpeechLiveResult(String liveSpeechResult) {
        Log.i(TAG, "Live speech result = " + liveSpeechResult);
    }

    @Override
    public void onDroidSpeechFinalResult(String finalSpeechResult) {
        // Setting the final speech result
        this.finalSpeechResult.setText(finalSpeechResult);

        if (droidSpeech.getContinuousSpeechRecognition()) {
            int[] colorPallets1 = new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, Color.MAGENTA};
            int[] colorPallets2 = new int[]{Color.YELLOW, Color.RED, Color.CYAN, Color.BLUE, Color.GREEN};

            // Setting random color pallets to the recognition progress view
            droidSpeech.setRecognitionProgressViewColors(new Random().nextInt(2) == 0 ? colorPallets1 : colorPallets2);
        } else {
            stop.setVisibility(View.GONE);
            start.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDroidSpeechClosedByUser() {
        stop.setVisibility(View.GONE);
        start.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDroidSpeechError(String errorMsg) {
        // Speech error
        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();

        stop.post(new Runnable() {
            @Override
            public void run() {
                // Stop listening
                stop.performClick();
            }
        });
    }

    // MARK: DroidSpeechPermissionsListener Method

    @Override
    public void onDroidSpeechAudioPermissionStatus(boolean audioPermissionGiven, String errorMsgIfAny) {
        if (audioPermissionGiven) {
            start.post(new Runnable() {
                @Override
                public void run() {
                    // Start listening
                    start.performClick();
                }
            });
        } else {
            if (errorMsgIfAny != null) {
                // Permissions error
                Toast.makeText(this, errorMsgIfAny, Toast.LENGTH_LONG).show();
            }

            stop.post(new Runnable() {
                @Override
                public void run() {
                    // Stop listening
                    stop.performClick();
                }
            });
        }
    }


    private void startSpeechService() {
        Intent speechServiceIntent = new Intent(Activity_DroidSpeech.this, SpeechService.class);
        startService(speechServiceIntent);
    }

    private void stopSpeechService() {
        Intent speechServiceIntent = new Intent(Activity_DroidSpeech.this, SpeechService.class);
        stopService(speechServiceIntent);
    }

}
