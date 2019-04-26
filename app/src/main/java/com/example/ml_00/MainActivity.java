package com.example.ml_00;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonElement;

import java.util.Map;

import ai.api.AIDataService;
import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class MainActivity extends AppCompatActivity implements AIListener {

    private Button button;
    private TextView textView;
    private AIService aiService;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);

        final AIConfiguration config = new AIConfiguration("7c2e14c6a22442a6b9c170140714ec10",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);


        aiService = AIService.getService(this, config);
        aiService.setListener(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aiService.startListening();
                Log.d(TAG, "onClick: " + "listening started");
            }
        });
    }


    @Override
    public void onResult(final AIResponse response) {
        //Result result = response.getResult();

        final Result result = response.getResult();
        final String speech = result.getFulfillment().getSpeech();
        Log.i(TAG, "Speech: " + speech);
        textView.setText(speech);

    }

    @Override
    public void onError(AIError error) {
        Log.d(TAG, "onError: " + error.toString());
    }

    @Override
    public void onAudioLevel(final float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }
}
