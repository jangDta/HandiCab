package com.hello.handicab1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText name,phone;
    DatabaseReference db;

    String inputName;
    String inputPhone;

    int checkNumber = 0;
    SharedPreferences auto;
    SharedPreferences.Editor autoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        name = (EditText)findViewById(R.id.login_name);
        phone = (EditText)findViewById(R.id.login_phone);
        auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        autoLogin = auto.edit();

    }

    public void loginClick(View view) {
        inputPhone = phone.getText().toString();
        inputName = name.getText().toString();

        if(name.getText().toString().equals("") || phone.getText().toString().equals("")){
            Toast.makeText(this, "이름과 전화번호를 모두 입력하세요", Toast.LENGTH_SHORT).show();
        }else{
            IDCheck(inputPhone, inputName);
        }
    }

    public void IDCheck(final String myphone, final String myname){
        db = FirebaseDatabase.getInstance().getReference("User");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserInformation myinfo = snapshot.getValue(UserInformation.class);
                    if(myinfo.getUserPhone().equals(myphone) && myinfo.getUserName().equals(myname)) {
                        Log.v("yong", "통과");
                        autoLogin.putString("inputId", myname);
                        autoLogin.putString("inputPwd", myphone);
                        autoLogin.putString("inputNeed",myinfo.getUserNeed());
                        autoLogin.putString("inputParentPhone",myinfo.getUserParentPhone());
                        autoLogin.putString("inputHandicap", myinfo.getUserHandicap());
                        autoLogin.commit();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        Log.v("yong","inputhandi:"+auto.getString("inputHandicap",null));
                        checkNumber = 1;

                        startActivity(intent);
                    }
                }
                if(checkNumber == 0)
                    Toast.makeText(getApplicationContext(), "해당하는 회원이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public void signupClick(View view) {
        Intent intent = new Intent(this,SignupActivity.class);
        startActivity(intent);
    }

}