package com.hello.handicab1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    DatabaseReference db;
    CheckBox[] checkBox;
    String special = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void requestSignup(View view) {
        EditText SignupName = (EditText)findViewById(R.id.signup_name);
        EditText SignupPhone  = (EditText)findViewById(R.id.signup_phone);
        EditText SignupParentPhone = (EditText)findViewById(R.id.signup_parent_phone);
        EditText SignupAnother  = (EditText)findViewById(R.id.signup_another);
        EditText SignupNeed = (EditText)findViewById(R.id.signup_need);


        checkBox=new CheckBox[9];
        checkBox[0]=(CheckBox)findViewById(R.id.check1);
        checkBox[1]=(CheckBox)findViewById(R.id.check2);
        checkBox[2]=(CheckBox)findViewById(R.id.check3);
        checkBox[3]=(CheckBox)findViewById(R.id.check4);
        checkBox[4]=(CheckBox)findViewById(R.id.check5);
        checkBox[5]=(CheckBox)findViewById(R.id.check6);
        checkBox[6]=(CheckBox)findViewById(R.id.check7);
        checkBox[7]=(CheckBox)findViewById(R.id.check8);
        checkBox[8]=(CheckBox)findViewById(R.id.check9);


        for(int i=0;i<9;i++){
            if(checkBox[i].isChecked()) special += checkBox[i].getText().toString() +" ";
        }

        Log.v("yong",special);

        db = FirebaseDatabase.getInstance().getReference("User");
        UserInformation user = new UserInformation(SignupName.getText().toString(), SignupPhone.getText().toString(), SignupParentPhone.getText().toString(),special, SignupNeed.getText().toString(),"");
        db.child(SignupPhone.getText().toString()).setValue(user);

        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);

    }
}
