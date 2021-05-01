package com.example.android.tomultiqa;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class FolderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);
        InitializingFolderData();
        Switch AutoSaveSwitch =findViewById(R.id.AutoSaveSwitch);
        AutoSaveNote(AutoSaveSwitch);
    }

    private void InitializingFolderData(){
        SharedPreferences A = getSharedPreferences("FolderProfile", MODE_PRIVATE);
        EditText MemoTextShowView = findViewById(R.id.MemoTextShowView);
        MemoTextShowView.setText(A.getString("NoteText","-There is no Note-"));
    }

    //Note system.
    //provide auto save function for Note Text.
    //thanks to: https://www.cnblogs.com/aademeng/articles/11117082.html !
    public void AutoSaveNote(View view){
        class MyTimerTask extends TimerTask{
            public void run() {
                //Mission that needs Timer.
                Switch AutoSaveSwitch =findViewById(R.id.AutoSaveSwitch);
                if(AutoSaveSwitch.isChecked()){
                    EditText MemoTextShowView = findViewById(R.id.MemoTextShowView);
                    SharedPreferences A = getSharedPreferences("FolderProfile", MODE_PRIVATE);
                    SharedPreferences.Editor editor= A.edit();
                    editor.putString("NoteText", MemoTextShowView.getText().toString());
                    editor.apply();
                }
            }
        }
        //run the MyTimerTask Class per 5000ms(0.5s).
            Timer timer = new Timer();
            timer.schedule(new MyTimerTask(), 0, 500);
    }

    public void SaveNote(View view){
        EditText MemoTextShowView = findViewById(R.id.MemoTextShowView);
        SharedPreferences A = getSharedPreferences("FolderProfile", MODE_PRIVATE);
        SharedPreferences.Editor editor= A.edit();
        editor.putString("NoteText", MemoTextShowView.getText().toString());
        editor.apply();
        Toast.makeText(getApplicationContext(), "Successfully Save", Toast.LENGTH_SHORT).show();
    }

    public void ShowAutoSaveHelp(View view){
        SupportClass.CreateOnlyTextDialog(this,
                getString(R.string.HelpWordTran),
                "·Warning:\n·The [AutoSave] function is in experiment.\n·So please press [Save] Button Manually.\n(Saving is automatic in [Clear] button.)",
                getString(R.string.ConfirmWordTran),
                "Nothing",
                true);
    }

    public void ClearNoteText(View view){
        EditText MemoTextShowView = findViewById(R.id.MemoTextShowView);
        MemoTextShowView.setText("");
        SharedPreferences A = getSharedPreferences("FolderProfile", MODE_PRIVATE);
        SharedPreferences.Editor editor= A.edit();
        editor.putString("NoteText", "");
        editor.apply();
        Toast.makeText(getApplicationContext(), "Successfully Clear", Toast.LENGTH_SHORT).show();
    }
}