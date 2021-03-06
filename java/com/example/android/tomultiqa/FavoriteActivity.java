package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.Locale;

public class FavoriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        //thanks to: https://dev.mi.com/console/doc/detail?pId=2229 !
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        ImportFavoriteData();
        ImportFavoriteSetting();
        InitializingAssistFunction();
    }

    //add a button menu to ActionBar in Activity.
    //thanks to: https://stackoverflow.com/questions/17425742/how-to-add-button-in-actionbarandroid !
    // https://blog.csdn.net/oxiaoxio/article/details/48900763 !
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.backpack_activity_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_TimerHistory) {//if setting icon in Menu be touched.
            SupportLib.CreateNoticeDialog(this,
                    getString(R.string.HelpWordTran),
                    ValueLib.FAVORITE_HELP,
                    getString(R.string.ConfirmWordTran));
        }else if (item.getItemId() == android.R.id.home) {
            Intent i = new Intent(this,MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            return true;//keep it here!
        }
        return super.onOptionsItemSelected(item);
    }//end of Menu Button.


    //Favorite system.
    //Variable part.
    SQLiteDatabase FavoriteDbRead;
    SQLiteDatabase FavoriteDbWrite;
    //Cursor data.
    //Fav is Favorite meaning.
    Cursor FavReadCursor;
    boolean IsCursorNotEmpty;
    int ReadCursorPosition;
    int ReadCursorLength;
    //Favorite data.
    int FavoriteNumber;
    String FavoriteTitle = "";
    String FavoriteAnswer = "";
    //String FavoriteHint; //NOT USED NOW.
    //Favorite Setting data.
    //[false] means Random load Favorite(Default), [True] means Queue load Favorite.
    String ReloadFavoriteMode;
    boolean IsShowFavoriteAnswer;
    boolean IsAutoJudge;
    //end of Variable part.


    //Favorite Loading part.
    //lv.3 method, main method of LastQuest Button.
    public void LastQuestButton(View view){
        //1.move the Cursor Position according to Current Mode.
        if(IsCursorNotEmpty && ReloadFavoriteMode.equals("Random")){
            ReadCursorPosition = SupportLib.CreateRandomNumber(0,ReadCursorLength - 1);//Length is base on 1, but Position is base on 0. So need to minus 1.
            FavReadCursor.moveToPosition(ReadCursorPosition);
        }else if(IsCursorNotEmpty && ReloadFavoriteMode.equals("Queue")){
            if(!FavReadCursor.isFirst()){//if Cursor have enough Quest to load, or back to First.
                FavReadCursor.moveToPrevious();
            }else{
                FavReadCursor.moveToLast();
            }
            ReadCursorPosition = FavReadCursor.getPosition();
        }
        //2.According to Judge Switch state, Judge User`s Answer.
        JudgeUserAnswer();
        //3.start load data from specific Position.
        ReloadFavoriteText();
    }

    //lv.3 method, main method of NextQuest Button.
    public void NextQuestButton(View view){
        //1.move the Cursor Position according to Current Mode.
        if(IsCursorNotEmpty && ReloadFavoriteMode.equals("Random")){
            ReadCursorPosition = SupportLib.CreateRandomNumber(0,ReadCursorLength - 1);//Length is base on 1, but Position is base on 0. So need to minus 1.
            FavReadCursor.moveToPosition(ReadCursorPosition);
        }else if(IsCursorNotEmpty && ReloadFavoriteMode.equals("Queue")){
            if(!FavReadCursor.isLast()){//if Cursor have enough Quest to load, or back to First.
                FavReadCursor.moveToNext();
            }else{
                FavReadCursor.moveToFirst();
            }
            ReadCursorPosition = FavReadCursor.getPosition();
        }
        //2.According to Judge Switch state, Judge User`s Answer.
        JudgeUserAnswer();
        //3.start load data from specific Position.
        ReloadFavoriteText();
    }

    //lv.2 method, sub method of LastQuestButton(), NextQuestButton() method.
    @SuppressLint("Range")
    private void ReloadFavoriteText(){
        EditText FavoriteTitleView = findViewById(R.id.FavoriteTitleView);
        EditText FavoriteAnswerView = findViewById(R.id.FavoriteAnswerView);
        //1.Reload Text and show to user.
        if(FavReadCursor.moveToFirst()){//Empty Check.
            FavoriteTitle = FavReadCursor.getString(FavReadCursor.getColumnIndex(FavoriteDbHelper.DataBaseEntry.COLUMN_NAME_FavoriteTitle));
            FavoriteAnswer = FavReadCursor.getString(FavReadCursor.getColumnIndex(FavoriteDbHelper.DataBaseEntry.COLUMN_NAME_FavoriteAnswer));
        }
        FavoriteTitleView.setText(FavoriteTitle);
        if(IsShowFavoriteAnswer){
            FavoriteAnswerView.setText(FavoriteAnswer);
        }else{
            FavoriteAnswerView.setText("");
        }
        //2.Auto Keyboard function.
        AutoKeyboardControl();
        AutoClearAnswerControl();
        //3.Show Process in Favorite Database.
        PrintFavoriteProcess();
        //4.Reset and Start Timer.
        StartChronometer();
    }

    //lv.2 method, main method of Import Favorite data from db.
    private void ImportFavoriteData(){
        FavoriteDbHelper FavoriteDbHelper = new FavoriteDbHelper(this);
        FavoriteDbRead = FavoriteDbHelper.getReadableDatabase();
        FavoriteDbWrite = FavoriteDbHelper.getWritableDatabase();
        FavReadCursor =  FavoriteDbRead.query(
                com.example.android.tomultiqa.FavoriteDbHelper.DataBaseEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        IsCursorNotEmpty = FavReadCursor.moveToFirst();
        FavoriteNumber = SupportLib.getIntData(this,"FavoriteFile","FavoriteNumber",0);
        ReadCursorLength = FavReadCursor.getCount();
        PrintFavoriteProcess();
    }


    //lv.1 method, main method of Show Answer.
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    public void ShowAnswerSwitch(View view){
        Switch FavoriteAnswerShowSwitch = findViewById(R.id.FavoriteAnswerShowSwitch);
        EditText FavoriteAnswerView = findViewById(R.id.FavoriteAnswerView);
        //1.save change state to storage.
        IsShowFavoriteAnswer = FavoriteAnswerShowSwitch.isChecked();
        SupportLib.saveBooleanData(this,"FavoriteFile","ShowFavoriteAnswer",IsShowFavoriteAnswer);
        //2.if Switch is ON, show user answer.
        if(IsShowFavoriteAnswer && !FavoriteTitle.equals("")){
            FavoriteAnswerView.setText(FavoriteAnswer);
        }else if(IsShowFavoriteAnswer){//if Title not Empty and ShowAnswer is ON.
            Toast.makeText(this,getString(R.string.QuestNotLoadedTran),Toast.LENGTH_SHORT).show();
        }else{//if Title not Empty and ShowAnswer switch changed from ON to OFF.
            FavoriteAnswerView.setText("");
        }
    }

    //lv.1 method, main method.
    public void ChangeLoadFavoriteMode(View view){
        RadioGroup ReloadModeGroup = findViewById(R.id.ReloadModeGroup);
        RadioButton RandomReloadChoose = findViewById(R.id.RandomReloadChoose);
        RadioButton QueueReloadChoose = findViewById(R.id.QueueReloadChoose);
        int SelectedID = ReloadModeGroup.getCheckedRadioButtonId();
        if(SelectedID == RandomReloadChoose.getId()){
            ReloadFavoriteMode = "Random";
        }else if(SelectedID == QueueReloadChoose.getId()){
            ReloadFavoriteMode = "Queue";
        }
        SupportLib.saveStringData(this,"FavoriteFile","ReloadFavoriteMode",ReloadFavoriteMode);
    }

    //lv.1 method, main method of Reset Cursor Position.
    public void ResetFavoriteQueue(View view){
        FavReadCursor.moveToFirst();
        SupportLib.CreateNoticeDialog(this,
                getString(R.string.ReportWordTran),
                getString(R.string.ResetFavoriteQueueTran),
                getString(R.string.ConfirmWordTran));
    }

    //lv.1 method, sub method of reset Favorite Process View.
    @SuppressLint("SetTextI18n")
    private void PrintFavoriteProcess(){
        TextView FavoriteProcessView = findViewById(R.id.FavoriteProcessView);
        FavoriteProcessView.setText((ReadCursorPosition + 1) + " / " + ReadCursorLength);//Length is base on 1, but Position is base on 0. So need to plus 1.
    }//end of Favorite Loading part.


    //Favorite Judging & Setting part.
    //lv.1 method, sub method of LastQuest() and NextQuest() method.
    @SuppressLint("SetTextI18n")
    private void JudgeUserAnswer(){
        //1.start Judging, stop counting time.
        StopChronometer();
        //2.if AutoJudge function is ON, Judge Answer for user.
        if(IsAutoJudge){
            //2.0 preparation.
            EditText FavoriteAnswerView = findViewById(R.id.FavoriteAnswerView);
            TextView FavoriteCorrectShowView = findViewById(R.id.FavoriteCorrectShowView);
            //2.1 get input answer.
            String QuestUserAnswer = FavoriteAnswerView.getText().toString();
            //2.2 if user enable Auto Case function, App will fix Letter Case automatically.
            //"-1" means function not enable.
            //"0" means auto fix to Lower Case (A to a).
            //"1" means auto fix to Upper Case (a to A).
            if(AutoCaseState == 0){
                QuestUserAnswer = QuestUserAnswer.toLowerCase(Locale.ROOT);
            }else if(AutoCaseState == 1){
                QuestUserAnswer = QuestUserAnswer.toUpperCase(Locale.ROOT);
            }
            //2.3 Judge and show result to user.
            if(QuestUserAnswer.equals(FavoriteAnswer)){
                FavoriteCorrectShowView.setText(getString(R.string.CorrectWordTran) + "!");
            }else{
                FavoriteCorrectShowView.setText(getString(R.string.WrongWordTran) + "...");
            }
            //2.4 after show result for a while, stop showing.
            Handler handler = new Handler();
            handler.postDelayed(() -> FavoriteCorrectShowView.setText(""),500);
        }
    }

    //lv.1 method, main method.
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private void ImportFavoriteSetting(){
        //1.load variables.
        ReloadFavoriteMode = SupportLib.getStringData(this,"FavoriteFile","ReloadFavoriteMode","Random");
        IsShowFavoriteAnswer = SupportLib.getBooleanData(this,"FavoriteFile","ShowFavoriteAnswer",false);
        IsAutoJudge = SupportLib.getBooleanData(this,"FavoriteFile","FavoriteJudge",false);
        //2.Set RadioButton state.
        if(ReloadFavoriteMode.equals("Random")){
            RadioButton RandomReloadChoose = findViewById(R.id.RandomReloadChoose);
            RandomReloadChoose.setChecked(true);
        }else if(ReloadFavoriteMode.equals("Queue")){
            RadioButton QueueReloadChoose = findViewById(R.id.QueueReloadChoose);
            QueueReloadChoose.setChecked(true);
        }
        //3.Set Switch State.
        Switch FavoriteAnswerShowSwitch = findViewById(R.id.FavoriteAnswerShowSwitch);
        Switch FavoriteJudgeSwitch = findViewById(R.id.FavoriteJudgeSwitch);
        FavoriteAnswerShowSwitch.setChecked(IsShowFavoriteAnswer);
        FavoriteJudgeSwitch.setChecked(IsAutoJudge);
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    public void AutoJudgeSwitch(View view){
        Switch FavoriteJudgeSwitch = findViewById(R.id.FavoriteJudgeSwitch);
        //1.save change state to storage.
        IsAutoJudge = FavoriteJudgeSwitch.isChecked();
        SupportLib.saveBooleanData(this,"FavoriteFile","ShowFavoriteAnswer",IsAutoJudge);
    }

    //lv.1 method, main method.
    public void ShowFavoriteSetting(View view){
        CardView FavoriteSettingCard = findViewById(R.id.FavoriteSettingCard);
        if(FavoriteSettingCard.getVisibility() == View.VISIBLE){
            FavoriteSettingCard.setVisibility(View.GONE);
        }else{
            FavoriteSettingCard.setVisibility(View.VISIBLE);
        }
    }//end of Favorite Judging & Setting part.
    

    //Favorite Delete part.
    public void DeleteFavorite(View view){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.HintWordTran));
        dialog.setMessage(getString(R.string.DeleteFavoriteHintTran) + "\n\n" +
                FavoriteTitle);
        dialog.setCancelable(true);
        dialog.setPositiveButton(
                getString(R.string.ConfirmWordTran),
                (dialog12, id) -> {
                    //1.delete Favorite.
                    String WhereClause = FavoriteDbHelper.DataBaseEntry.COLUMN_NAME_FavoriteTitle + " = ?";
                    String[] WhereArgs = {FavoriteTitle};
                    FavoriteDbWrite.delete(
                            FavoriteDbHelper.DataBaseEntry.TABLE_NAME,
                            WhereClause,
                            WhereArgs
                    );
                    FavoriteNumber = FavoriteNumber - 1;
                    SupportLib.saveIntData(this,"FavoriteFile","FavoriteNumber",FavoriteNumber);
                    //2.Reload the database which after Deleted, and reload Favorite prevent from data error.
                    ImportFavoriteData();
                    if(IsCursorNotEmpty){//2.1 if Cursor still not Empty.
                        ReloadFavoriteText();
                        Toast.makeText(this,getString(R.string.ProgressCompletedTran),Toast.LENGTH_SHORT).show();
                    }else{//2.2 if Cursor is Empty.
                        Button LastFavoriteButton = findViewById(R.id.LastFavoriteButton);
                        Button RemoveFavoriteButton = findViewById(R.id.RemoveFavoriteButton);
                        Button NextFavoriteButton = findViewById(R.id.NextFavoriteButton);
                        //if Cursor is Empty, show warning to user.
                        LastFavoriteButton.setEnabled(false);
                        RemoveFavoriteButton.setEnabled(false);
                        NextFavoriteButton.setEnabled(false);
                        FavoriteTitle = "";
                        FavoriteAnswer = "";
                        SupportLib.CreateNoticeDialog(this,
                                getString(R.string.WarningWordTran),
                                getString(R.string.EmptyFavoriteWarningTran),
                                getString(R.string.ConfirmWordTran));
                    }
                    //3.finish delete process, show to user.
                    dialog12.cancel();
                });
        dialog.setNegativeButton(
                getString(R.string.CancelWordTran),
                (dialog1, id) -> dialog1.cancel());
        if(!FavoriteTitle.equals("")){//if Title text is empty, not show dialog to user.
            AlertDialog DialogView = dialog.create();
            DialogView.show();
        }
    }
    //end of Favorite Delete part.
    //end of Favorite system.


    //Chronometer function, sub part of Favorite system.
    private void StopChronometer(){
        Chronometer FavoriteTimerView = findViewById(R.id.FavoriteTimerView);
        FavoriteTimerView.stop();
    }

    private void StartChronometer(){
        Chronometer FavoriteTimerView = findViewById(R.id.FavoriteTimerView);
        FavoriteTimerView.setBase(SystemClock.elapsedRealtime());
        FavoriteTimerView.start();
    }//end of Chronometer function.


    //Assist Functions.
    //Auto keyboard function.
    //to store keyboard state.
    boolean IsKeyboardOpened = false;
    //when FavoriteActivity is initialized, the keyboard is hidden(false).
    //if AutoKeyboard function is on, when user try to reload the Quest, the keyboard will appear.(turn the state to true)
    //using this variable to detect if keyboard is opened, and be the base of AutoKeyboard function.

    //store function state (ON or OFF).
    boolean IsAutoKeyboard = false;
    boolean IsAutoClearAnswer =false;
    //Auto Case Function.
    //"-1" means function not enable.
    //"0" means auto fix to Lower Case (A to a).
    //"1" means auto fix to Upper Case (a to A).
    int AutoCaseState = -1;

    //main method.
    private void InitializingAssistFunction(){
        IsAutoKeyboard = SupportLib.getBooleanData(this,"TimerSettingProfile", "AutoKeyboard", false);
        IsAutoClearAnswer = SupportLib.getBooleanData(this,"TimerSettingProfile","AutoClearAnswer",false);
        AutoCaseState = SupportLib.getIntData(this,"TimerSettingProfile","AutoCase",-1);
    }

    private void AutoKeyboardControl(){
        //help user focus on typing. Auto pop the virtual keyboard.Thanks to:
        // https://www.jianshu.com/p/6f09de9e903b !
        // https://blog.csdn.net/fenglolo/article/details/108893330 !
        EditText FavoriteAnswerView = findViewById(R.id.FavoriteAnswerView);
        if(IsAutoKeyboard){
            FavoriteAnswerView.requestFocus();
            InputMethodManager imm = (InputMethodManager) FavoriteActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (null != imm && !IsKeyboardOpened) {
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                IsKeyboardOpened = !IsKeyboardOpened;
            }
        }
    }//end of Auto keyboard function.


    //Auto Clear Answer Function.
    private void AutoClearAnswerControl(){
        EditText FavoriteAnswerView = findViewById(R.id.FavoriteAnswerView);
        if(IsAutoClearAnswer){
            FavoriteAnswerView.setText("");
        }
    }//end of Auto Clear Answer Function.
    //end of Assist Functions.
}