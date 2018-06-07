package com.hello.handicab1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText name,phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name = (EditText)findViewById(R.id.login_name);
        phone = (EditText)findViewById(R.id.login_phone);
    }

    public void loginClick(View view) {
        if(name.getText().toString().equals("") || phone.getText().toString().equals("")){
            Toast.makeText(this, "이름과 전화번호를 모두 입력하세요", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
    }

    public void signupClick(View view) {
        Intent intent = new Intent(this,SignupActivity.class);
        startActivity(intent);
    }
}
