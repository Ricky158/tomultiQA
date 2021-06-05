package com.example.android.tomultiqa;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
    }

    // TODO: 2021/6/3 notice user 20 lines note limitation. 
    //Note AutoSave Function.
    @Override
    protected void onPause() {
        super.onPause();
        EditText MemoTextShowView = findViewById(R.id.MemoTextShowView);
        SupportClass.saveStringData(FolderActivity.this,"FolderProfile","NoteText", MemoTextShowView.getText().toString());
    }

    private void InitializingFolderData(){
        EditText MemoTextShowView = findViewById(R.id.MemoTextShowView);
        MemoTextShowView.setText(SupportClass.getStringData(this,"FolderProfile","NoteText","-There is no Note-"));
    }//end of Note AutoSave Function.


    //Note system.
    //provide auto save function for Note Text.
    //thanks to: https://www.cnblogs.com/aademeng/articles/11117082.html !
//    public void AutoSaveNote(View view){
//        class MyTimerTask extends TimerTask{
//            public void run() {
//                //Mission that needs Timer.
//                //......
//            }
//        }
//        //run the MyTimerTask Class per 5000ms(0.5s).
//            Timer timer = new Timer();
//            timer.schedule(new MyTimerTask(), 0, 500);
//    }

    public void ClearNoteText(View view){
        EditText MemoTextShowView = findViewById(R.id.MemoTextShowView);
        MemoTextShowView.setText("");
        SupportClass.saveStringData(this,"FolderProfile","NoteText", "");
        Toast.makeText(getApplicationContext(), "Successfully Clear", Toast.LENGTH_SHORT).show();
    }
}