package com.example.android.tomultiqa;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class FolderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);
        //thanks to: https://dev.mi.com/console/doc/detail?pId=2229 !
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
    }

    //add a button menu to ActionBar in MainActivity.
    //thanks to: https://stackoverflow.com/questions/17425742/how-to-add-button-in-actionbarandroid !
    // https://blog.csdn.net/oxiaoxio/article/details/48900763 !
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.folder_activity_button, menu);
        return true;
    }

    //copy note text to system clipboard.
    //thanks to: https://blog.csdn.net/asdf717/article/details/52678009 !
    // https://stackoverflow.com/questions/33207809/what-exactly-is-label-parameter-in-clipdata-in-android !
    // https://blog.csdn.net/zhuifengshenku/article/details/19080227 !
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_copy) {//if copy icon in Menu be touched.
            TextView MemoTextShowView = findViewById(R.id.MemoTextShowView);
            ClipboardManager mCM = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
            ClipData mClipData = ClipData.newPlainText("Note from tomultiQA", MemoTextShowView.getText().toString());
            //1.show dialog to user.
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(getString(R.string.NoticeWordTran));
            dialog.setMessage(getString(R.string.FolderCopy1Tran) + "\n" +
                    getString(R.string.FolderCopy2Tran));
            dialog.setCancelable(true);
            dialog.setPositiveButton(
                    getString(R.string.ConfirmWordTran),
                    (dialog12, id) -> {
                        mCM.setPrimaryClip(mClipData);//paste note to clipboard.
                        Toast.makeText(FolderActivity.this, getString(R.string.NotePasteToClipBoardTran), Toast.LENGTH_SHORT).show();
                        dialog12.cancel();
                    });
            dialog.setNegativeButton(
                    getString(R.string.CancelWordTran),
                    (dialog1, id) -> dialog1.cancel());
            AlertDialog DialogView = dialog.create();
            DialogView.show();
        }
        return super.onOptionsItemSelected(item);
    }//end of copy note text to system clipboard.


    //Note AutoSave Function.
    @Override
    protected void onPause() {
        super.onPause();
        EditText MemoTextShowView = findViewById(R.id.MemoTextShowView);
        SupportLib.saveStringData(FolderActivity.this,"FolderProfile","NoteText", MemoTextShowView.getText().toString());
    }

    @Override
    protected void onStart() {
        super.onStart();
        //InitializingFolderData.
        EditText MemoTextShowView = findViewById(R.id.MemoTextShowView);
        MemoTextShowView.setText(SupportLib.getStringData(this,"FolderProfile","NoteText","-There is no Note-"));
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
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.NoticeWordTran));
        dialog.setMessage(getString(R.string.ConfirmClearTextTran));
        dialog.setCancelable(true);
        dialog.setPositiveButton(
                getString(R.string.ConfirmWordTran),
                (dialog12, id) -> {
                    MemoTextShowView.setText("");
                    SupportLib.saveStringData(FolderActivity.this,"FolderProfile","NoteText", "");
                    Toast.makeText(this, getString(R.string.ClearTextButtonTran) + " " + getString(R.string.CompletedWordTran), Toast.LENGTH_SHORT).show();
                    dialog12.cancel();
                });
            dialog.setNegativeButton(
                    getString(R.string.CancelWordTran),
                    (dialog1, id) -> dialog1.cancel());
        AlertDialog DialogView = dialog.create();
        DialogView.show();
    }

    public void GoToBackup(View view){
        EditText MemoTextShowView = findViewById(R.id.MemoTextShowView);
        SupportLib.saveStringData(FolderActivity.this,"FolderProfile","NoteText", MemoTextShowView.getText().toString());
        Intent i = new Intent(this,BackUpActivity.class);
        startActivity(i);
        finish();
    }
}