package com.example.sony.training;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class LoginActivity2 extends AppCompatActivity {
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        mSharedPreferences = getApplicationContext().getSharedPreferences(Constants.SHARE_PREF_NAME, Context.MODE_PRIVATE);
    }

    public void onLoginButtonClicked(View view) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(Constants.SHARE_KEY_PREF_LOGIN, true);
        editor.commit();
        finish();
        Intent intent = new Intent(this, LoginSuccessActivity.class);
        startActivity(intent);
    }
}
