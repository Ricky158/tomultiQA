package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

public class FavoriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        ImportFavoriteData();
        ImportFavoriteSetting();
        InitializingAutoKeyboard();
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
        if (item.getItemId() == R.id.action_BackPackHelp) {//if setting icon in Menu be touched.
            SupportClass.CreateNoticeDialog(this,
                    getString(R.string.HelpWordTran),
                    ValueClass.FAVORITE_HELP,
                    getString(R.string.ConfirmWordTran));
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
            ReadCursorPosition = SupportClass.CreateRandomNumber(0,ReadCursorLength - 1);//Length is base on 1, but Position is base on 0. So need to minus 1.
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
            ReadCursorPosition = SupportClass.CreateRandomNumber(0,ReadCursorLength - 1);//Length is base on 1, but Position is base on 0. So need to minus 1.
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
    private void ReloadFavoriteText(){
        EditText FavoriteTitleView = findViewById(R.id.FavoriteTitleView);
        EditText FavoriteAnswerView = findViewById(R.id.FavoriteAnswerView);
        //1.Reload Text and show to user.
        FavoriteTitle = FavReadCursor.getString(FavReadCursor.getColumnIndex(FavoriteDataBaseBasic.DataBaseEntry.COLUMN_NAME_FavoriteTitle));
        FavoriteAnswer = FavReadCursor.getString(FavReadCursor.getColumnIndex(FavoriteDataBaseBasic.DataBaseEntry.COLUMN_NAME_FavoriteAnswer));
        FavoriteTitleView.setText(FavoriteTitle);
        if(IsShowFavoriteAnswer){
            FavoriteAnswerView.setText(FavoriteAnswer);
        }else{
            FavoriteAnswerView.setText("");
        }
        //2.Auto Keyboard function.
        AutoKeyboardControl();
        //3.Show Process in Favorite Database.
        PrintFavoriteProcess();
        //4.Reset and Start Timer.
        StartChronometer();
    }

    //lv.2 method, main method of Import Favorite data from db.
    private void ImportFavoriteData(){
        FavoriteDataBaseBasic FavoriteDbHelper = new FavoriteDataBaseBasic(this);
        FavoriteDbRead = FavoriteDbHelper.getReadableDatabase();
        FavoriteDbWrite = FavoriteDbHelper.getWritableDatabase();
        FavReadCursor =  FavoriteDbRead.query(
                FavoriteDataBaseBasic.DataBaseEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        IsCursorNotEmpty = FavReadCursor.moveToFirst();
        FavoriteNumber = SupportClass.getIntData(this,"FavoriteFile","FavoriteNumber",0);
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
        SupportClass.saveBooleanData(this,"FavoriteFile","ShowFavoriteAnswer",IsShowFavoriteAnswer);
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
        SupportClass.saveStringData(this,"FavoriteFile","ReloadFavoriteMode",ReloadFavoriteMode);
    }

    //lv.1 method, main method of Reset Cursor Position.
    public void ResetFavoriteQueue(View view){
        FavReadCursor.moveToFirst();
        SupportClass.CreateNoticeDialog(this,
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
            //2.1 preparation.
            EditText FavoriteAnswerView = findViewById(R.id.FavoriteAnswerView);
            TextView FavoriteCorrectShowView = findViewById(R.id.FavoriteCorrectShowView);
            //2.2 Judge and show result to user.
            if(FavoriteAnswerView.getText().toString().equals(FavoriteAnswer)){
                FavoriteCorrectShowView.setText(getString(R.string.CorrectWordTran) + "!");
            }else{
                FavoriteCorrectShowView.setText(getString(R.string.WrongWordTran) + "...");
            }
            //2.3 after show result for a while, stop showing.
            Handler handler = new Handler();
            handler.postDelayed(() -> FavoriteCorrectShowView.setText(""),500);
        }
    }

    //lv.1 method, main method.
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private void ImportFavoriteSetting(){
        //1.load variables.
        ReloadFavoriteMode = SupportClass.getStringData(this,"FavoriteFile","ReloadFavoriteMode","Random");
        IsShowFavoriteAnswer = SupportClass.getBooleanData(this,"FavoriteFile","ShowFavoriteAnswer",false);
        IsAutoJudge = SupportClass.getBooleanData(this,"FavoriteFile","FavoriteJudge",false);
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
        SupportClass.saveBooleanData(this,"FavoriteFile","ShowFavoriteAnswer",IsAutoJudge);
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
                    String WhereClause = FavoriteDataBaseBasic.DataBaseEntry.COLUMN_NAME_FavoriteTitle + " = ?";
                    String[] WhereArgs = {FavoriteTitle};
                    FavoriteDbWrite.delete(
                            FavoriteDataBaseBasic.DataBaseEntry.TABLE_NAME,
                            WhereClause,
                            WhereArgs
                    );
                    FavoriteNumber = FavoriteNumber - 1;
                    SupportClass.saveIntData(this,"FavoriteFile","FavoriteNumber",FavoriteNumber);
                    //2.Reload the database which after Deleted, and reload Favorite prevent from data error.
                    ImportFavoriteData();
                    boolean IsFavoriteEmpty = FavReadCursor.moveToFirst();
                    if(IsFavoriteEmpty){//2.1 if Cursor still not Empty.
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
                        SupportClass.CreateNoticeDialog(this,
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
        AlertDialog DialogView = dialog.create();
        DialogView.show();
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


    //Auto keyboard function.
    //to store keyboard state.
    boolean IsKeyboardOpened = false;
    //when FavoriteActivity is initialized, the keyboard is hidden(false).
    //if AutoKeyboard function is on, when user try to reload the Quest, the keyboard will appear.(turn the state to true)
    //using this variable to detect if keyboard is opened, and be the base of AutoKeyboard function.

    //store function state (ON or OFF).
    boolean IsAutoKeyboard = false;

    //main method.
    private void InitializingAutoKeyboard(){
        IsAutoKeyboard = SupportClass.getBooleanData(this,"TimerSettingProfile", "AutoKeyboard", false);
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
}