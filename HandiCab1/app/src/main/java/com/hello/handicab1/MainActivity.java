package com.hello.handicab1;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecognitionListener {

    TextView needTextView;
    CallDialog callDialog;

    SpeechRecognizer speechRecognizer;
    String message;
    final int RESULT_SPEECH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(this);

        needTextView = (TextView)findViewById(R.id.main_need);
        needTextView.setMovementMethod(new ScrollingMovementMethod());
    }

    public void callParent(View view) {

    }

    public void callTaxi(View view) {
        //음성인식 intent
//        Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "ko-KR"); //언어지정입니다.
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);   //검색을 말한 결과를 보여주는 갯수
//        startActivityForResult(recognizerIntent, RESULT_SPEECH);

        // 시각 장애인의 경우 음성지원, 아닌 경우 다이얼로그
        callDialog = new CallDialog(this);
        callDialog.show();

    }

    public void logoutClick(View view) {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onReadyForSpeech(Bundle params) {

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
        switch (error) {

            case SpeechRecognizer.ERROR_AUDIO:
                message = "오디오 에러";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                break;

            case SpeechRecognizer.ERROR_CLIENT:
                message = "클라이언트 에러";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                break;

            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "퍼미션없음";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                break;

            case SpeechRecognizer.ERROR_NETWORK:
                message = "네트워크 에러";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                break;

            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "네트웍 타임아웃";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                break;

            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "찾을수 없음";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                break;

            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "바쁘대";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                break;

            case SpeechRecognizer.ERROR_SERVER:
                message = "서버이상";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                break;

            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "말하는 시간초과";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                break;

            default:
                message = "알수없음";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                break;

        }
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        for(int i = 0; i < matches.size() ; i++){
            Log.v("GoogleActivity", "onResults text : " + matches.get(i));
        }
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    for (int i = 0; i < text.size(); i++) {
                        Log.v("yong", "speech result : " + text.get(i));
                    }
                }

                break;
            }

        }
    }
}
