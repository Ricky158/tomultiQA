package com.example.android.tomultiqa;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
        //make StatusBar is same color as ActionBar in Activity.
        //thanks to: https://blog.csdn.net/kiven9609/article/details/73162307 !
        // https://www.color-hex.com/color/26c6da ! #26C6DA is our App Primary Color.
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.rgb(38,198,218));
        //底部导航栏
        //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
        //main method below.
        InitializingFolderData();
        Switch AutoSaveSwitch =findViewById(R.id.AutoSaveSwitch);
        AutoSaveNote(AutoSaveSwitch);
    }

    private void InitializingFolderData(){
        EditText MemoTextShowView = findViewById(R.id.MemoTextShowView);
        MemoTextShowView.setText(SupportClass.getStringData(this,"FolderProfile","NoteText","-There is no Note-"));
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
                    SupportClass.saveStringData(FolderActivity.this,"FolderProfile","NoteText", MemoTextShowView.getText().toString());
                }
            }
        }
        //run the MyTimerTask Class per 5000ms(0.5s).
            Timer timer = new Timer();
            timer.schedule(new MyTimerTask(), 0, 500);
    }

    public void SaveNote(View view){
        EditText MemoTextShowView = findViewById(R.id.MemoTextShowView);
        SupportClass.saveStringData(this,"FolderProfile","NoteText", MemoTextShowView.getText().toString());
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
        SupportClass.saveStringData(this,"FolderProfile","NoteText", "");
        Toast.makeText(getApplicationContext(), "Successfully Clear", Toast.LENGTH_SHORT).show();
    }
}