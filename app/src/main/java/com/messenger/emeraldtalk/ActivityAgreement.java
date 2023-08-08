package com.messenger.emeraldtalk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActivityAgreement extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);

    }

    @Override public void onBackPressed() {
        //super.onBackPressed();
        finishAffinity();

    }


    public void btnDisagree(View v){
        finishAffinity();

    }

    public void btnAgree(View v){
        SharedPreferences pref = getSharedPreferences("EMERALDTALK", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("privacy_policy", "agree");
        editor.commit();

        finish();

    }
}
