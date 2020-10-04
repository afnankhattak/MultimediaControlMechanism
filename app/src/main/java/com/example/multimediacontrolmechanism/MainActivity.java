package com.example.multimediacontrolmechanism;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, RecognitionListener {


    private boolean initialized;
    private String queuedText;
    private String TAG = "TTS";
    private TextToSpeech tts;


    private TextView textType;


    private ImageButton btnAudio,btnVideo;
    protected Intent intent;
    protected SpeechRecognizer recognizer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textType=(TextView) findViewById(R.id.textType);
        btnAudio=(ImageButton) findViewById(R.id.btnAudio);
        btnVideo=(ImageButton) findViewById(R.id.btnVideo);

        tts = new TextToSpeech(this /* context */, this /* listener */);
        tts.setOnUtteranceProgressListener(mProgressListener);

        btnAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAudioFolder();
            }
        });


        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openVideoFolder();
            }
        });







    }


    @Override
    protected void onStart() {
        super.onStart();
        speak("Select Type of Multimedia. Audio or Video");
    }

    public void speak(String text) {

        if (!initialized) {
            queuedText = text;
            return;
        }
        queuedText = null;

        setTtsListener(); // no longer creates a new UtteranceProgressListener each time
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        tts.speak(text, TextToSpeech.QUEUE_ADD, map);
    }


    private void setTtsListener() {

    }





    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            initialized = true;
            tts.setLanguage(Locale.ENGLISH);

            if (queuedText != null) {
                speak(queuedText);
            }
        }
    }



    private abstract class runnable implements Runnable {
    }




    private UtteranceProgressListener mProgressListener = new UtteranceProgressListener() {
        @Override
        public void onStart(String utteranceId) {
        } // Do nothing

        @Override
        public void onError(String utteranceId) {
        } // Do nothing.

        @Override
        public void onDone(String utteranceId) {

            new Thread()
            {
                public void run()
                {
                    MainActivity.this.runOnUiThread(new runnable()
                    {
                        public void run()
                        {

                            Toast.makeText(getBaseContext(), "TTS Completed", Toast.LENGTH_SHORT).show();

                            speak();


                        }
                    });
                }
            }.start();

        }
    };




private void speak(){
    intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
    intent.putExtra(RecognizerIntent.EXTRA_WEB_SEARCH_ONLY, "false");
    intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, "3000");

    recognizer = SpeechRecognizer.createSpeechRecognizer(this);
    recognizer.setRecognitionListener(this);
    recognizer.startListening(intent);
}



    @Override
    public void onReadyForSpeech(Bundle params) {


        Toast.makeText(getBaseContext(), "Listening...", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int error) {
        String message;
        switch (error) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client error";
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
                message = "Speech Recognizer is busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "Server error";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Speech Recognizer cannot understand you";
                break;
        }


        Toast.makeText(getBaseContext(), ""+message, Toast.LENGTH_SHORT).show();
        //recognizer.stopListening();
        //recognizer.startListening(intent);
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> words = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        String text = "";

        for (String word : words) {
            text += word + "";
        }



       // Toast.makeText(getBaseContext(), ""+text, Toast.LENGTH_SHORT).show();
        textType.setText(text);
        if(textType.getText().toString().equalsIgnoreCase("audio")){
           openAudioFolder();
        }



        if(textType.getText().toString().equalsIgnoreCase("video")){
           openVideoFolder();
        }
        //recognizer.stopListening();
        //recognizer.startListening(intent);
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }










    private void openAudioFolder(){
        Intent intent=new Intent(MainActivity.this,AudioFolder.class);
        startActivity(intent);
    }

    private void openVideoFolder(){
        Intent intent=new Intent(MainActivity.this,VideoFolder.class);
        startActivity(intent);
    }


}