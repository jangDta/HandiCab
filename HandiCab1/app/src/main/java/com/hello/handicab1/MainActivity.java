package com.hello.handicab1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity implements RecognitionListener {

    TextView needTextView;
    CallDialog callDialog;

    SpeechRecognizer speechRecognizer;
    String message;
    String ParentPhone;
    ArrayList<String> myHandicapList;
    Handler handler;
    TextToSpeech textToSpeech;

    SharedPreferences auto;
    SharedPreferences.Editor autoLogin;

    final int RESULT_SPEECH = 1000;
    final int callRequest = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        needTextView = (TextView)findViewById(R.id.main_need);

        auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        autoLogin = auto.edit();


            needTextView.setText(auto.getString("inputNeed",null));
            ParentPhone = auto.getString("inputParentPhone",null);


            StringTokenizer tokenizer = new StringTokenizer(auto.getString("inputHandicap",null));
            myHandicapList = new ArrayList<>();
            while(tokenizer.hasMoreTokens()){
                myHandicapList.add(tokenizer.nextToken());
            }
            Log.v("yong","size: "+myHandicapList.size());


        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(this);

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != 0) {
                    // 언어를 선택한다.
                    textToSpeech.setLanguage(Locale.KOREAN);
                }
            }
        });

        needTextView = (TextView)findViewById(R.id.main_need);
        needTextView.setMovementMethod(new ScrollingMovementMethod());
    }

    public void callParent(View view) {
        Uri number = Uri.parse("tel:"+ParentPhone);
        Intent callIntent = new Intent(Intent.ACTION_CALL, number);
        if(checkAppPermission(new String[]{android.Manifest.permission.CALL_PHONE})){
            startActivity(callIntent);
        }
        else{
            askPermission(new String[]{android.Manifest.permission.CALL_PHONE}, callRequest);
        }

    }
    void askPermission(String[] requestPermission, int REQ_PERMISSION) {
        ActivityCompat.requestPermissions(
                this,
                requestPermission,
                REQ_PERMISSION
        );
    }
    boolean checkAppPermission(String[] requestPermission){
        boolean[] requestResult = new boolean[requestPermission.length];
        for(int i=0; i< requestResult.length; i++){
            requestResult[i] = (ContextCompat.checkSelfPermission(this,
                    requestPermission[i]) == PackageManager.PERMISSION_GRANTED );
            if(!requestResult[i]){
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case callRequest :
                if (checkAppPermission(permissions)) {
                    Uri number = Uri.parse("tel:"+ParentPhone);
                    Intent callIntent = new Intent(Intent.ACTION_CALL,number);
                    startActivity(callIntent);
                } else {
                    Toast.makeText(this, "권한이 거절됨",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    public void callTaxi(View view) {

        if(myHandicapList.contains("시각장애")){
            textToSpeech.speak("목적지를 말씀하세요",TextToSpeech.QUEUE_FLUSH, null,null);
            handler = new Handler();
            handler.postDelayed(new Runnable(){
                @Override
                public void run()
                {
                    Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "ko-KR"); //언어지정입니다.
                    recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
                    recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
                    recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);   //검색을 말한 결과를 보여주는 갯수
                    startActivityForResult(recognizerIntent, RESULT_SPEECH);
                }
            }, 1500);

        }else{
            callDialog = new CallDialog(this);
            callDialog.show();
        }


    }

    public void logoutClick(View view) {
        autoLogin.clear();
        autoLogin.commit();
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

                    if(text.contains("집")){
                        textToSpeech.speak("집으로 갑니다으아아으으아아아",TextToSpeech.QUEUE_FLUSH,null,null);
                        //자기위치 받아와야한다. 택시기사위치랑 내위치랑 비교 if 위도경도 차이가 0.07안에 있는 택시만 다 거리계산해서 제일 절대값작은걸 뽑는다.
                    }
                }

                break;
            }

        }
    }
}
