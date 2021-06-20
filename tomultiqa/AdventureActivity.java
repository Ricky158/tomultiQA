package com.example.android.tomultiqa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class AdventureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adventure);
    }

    //Entry part.
    public void OpenConflictActivity(View view){
        Intent i = new Intent(this, ConflictActivity.class);
        startActivity(i);
    }

    public void OpenDailyActivity(View view){
        Intent i = new Intent(this,DailyActivity.class);
        startActivity(i);
    }

    public void OpenTourneyActivity(View view){
        Intent i = new Intent(this,TourneyActivity.class);
        startActivity(i);
    }

    public void OpenLimitUpActivity(View view){
        Intent i = new Intent(this,LimitUpActivity.class);
        startActivity(i);
    }
}