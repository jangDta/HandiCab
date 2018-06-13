package com.hello.handicab1;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

/**
 * Created by hansol on 2018. 6. 7..
 */

public class CallDialog extends Dialog{
    RadioGroup radioGroup;
    Button okButton;
    String selected = "간편콜";
    public CallDialog(@NonNull final Context context) {
        super(context);
        setContentView(R.layout.dialog_call);
        radioGroup = (RadioGroup)findViewById(R.id.dialog_radiogroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.dialog_radio_easy:
                        selected = "간편콜";
                        break;
                    case R.id.dialog_radio_detail:
                        selected = "즐겨찾기";
                        break;
                        default:
                            break;
                }
            }
        });
        okButton = (Button)findViewById(R.id.dialog_okbutton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected.equals("간편콜")){ // 간편콜 선택
                    Intent intent = new Intent(context,EasyCallActivity.class);
                    context.startActivity(intent);
                }else{ // 세부콜 선택
                    Intent intent = new Intent(context,DetailCallActivity.class);
                    context.startActivity(intent);
                }
                dismiss();
            }
        });
    }


}
