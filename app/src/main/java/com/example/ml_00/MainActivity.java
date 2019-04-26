package com.example.ml_00;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonElement;

import java.util.Map;


import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class MainActivity extends AppCompatActivity implements AIListener {

    private TextView textView;
    private EditText editText;
    private AIService aiService;
    private AIDataService aiDataService;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        Button sendBtn = findViewById(R.id.send_btn);
        textView = findViewById(R.id.textView);
        editText = findViewById(R.id.editText);

        final AIConfiguration config = new AIConfiguration("7c2e14c6a22442a6b9c170140714ec10",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        // text input
        aiDataService = new AIDataService(this, config);

        // voice input
        aiService = AIService.getService(this, config);
        aiService.setListener(this);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // asyntask
                AIRequest aiRequest = new AIRequest();
                String mQuery = editText.getText().toString();
                aiRequest.setQuery(mQuery);
                editText.setText("");

                if (aiRequest == null) {
                    throw new IllegalArgumentException("aiRequest must be not null");
                }

                @SuppressLint("StaticFieldLeak") final AsyncTask<AIRequest, Integer, AIResponse> task =
                        new AsyncTask<AIRequest, Integer, AIResponse>() {
                            private AIError aiError;

                            @Override
                            protected AIResponse doInBackground(final AIRequest... params) {
                                final AIRequest request = params[0];
                                try {
                                    final AIResponse response = aiDataService.request(request);
                                    // Return response
                                    return response;
                                } catch (final AIServiceException e) {
                                    aiError = new AIError(e);
                                    return null;
                                }
                            }

                            @Override
                            protected void onPostExecute(final AIResponse response) {
                                if (response != null) {
                                    onResult(response);
                                } else {
                                    onError(aiError);
                                }
                            }
                        };
                task.execute(aiRequest);
            }
        });


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
