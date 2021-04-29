package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;


public class SettingActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //get total number of id in database according records in SharedPreference.
        CloseAllLayout();
        getIdNumberInfo();
        InitializingQuestEditor();

        //provide visualize support for seekBar moving report.
        //Thanks to:https://stackoverflow.com/questions/41774963/android-seekbar-show-progress-value-along-the-seekbar
        final SeekBar ChooseQuestLevelBar = findViewById(R.id.ChooseQuestLevelBar);
        final TextView ShowQuestLevelView = findViewById(R.id.ShowQuestLevelView);
        ChooseQuestLevelBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                ShowQuestLevelView.setText(ChooseQuestLevelBar.getProgress() + 1 + "");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                ShowQuestLevelView.setText(ChooseQuestLevelBar.getProgress() + 1 + "");
            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                ShowQuestLevelView.setText(progress+ 1 + "");
            }
        });

        //Loading AppMode from SharedPreference.
        InitializingAppMode();
    }


    //Quest Making system.
    //provide support for store id number.
    //Thanks to:https://www.jianshu.com/p/59b266c644f3
    public void StoreQuest(View view){
        ContentValues values = new ContentValues();
        EditText TypeQuestTitle = findViewById(R.id.TypeQuestTitle);
        EditText TypeQuestAnswer = findViewById(R.id.TypeQuestAnswer);
        SeekBar ChooseQuestLevelBar = findViewById(R.id.ChooseQuestLevelBar);
        QuestDataBaseBasic QuestDataBase = new QuestDataBaseBasic(this);
        SQLiteDatabase db = QuestDataBase.getWritableDatabase();
        if(!TypeQuestTitle.getText().toString().equals("") || !TypeQuestAnswer.getText().toString().equals("")){
            if (!EditorMode) {
                //prepare the data collection need to be put into database.
                values.put(QuestDataBaseBasic.COLUMN_NAME_QuestTitle,TypeQuestTitle.getText().toString());
                values.put(QuestDataBaseBasic.COLUMN_NAME_QuestAnswer,TypeQuestAnswer.getText().toString());
                values.put(QuestDataBaseBasic.COLUMN_NAME_QuestType,getString(R.string.QuestQuestionTypeTran));
                values.put(QuestDataBaseBasic.COLUMN_NAME_QuestLevel,ChooseQuestLevelBar.getProgress()+ 1);
                values.put(QuestDataBaseBasic.COLUMN_NAME_QuestHint,"Nothing");

                //initializing the database. And put the data collection in it.
                long newRowId = db.insert(QuestDataBaseBasic.TABLE_NAME, null, values);
            }else{
                // put title Text to String[] as searching material.
                String[] ColumnForSearch = new String[]{QuestDataBaseBasic.DataBaseEntry._ID};
                String[] TitleForSearch = new String[]{TypeQuestTitle.getText().toString()};
                String[] ShowingQuestID = new String[1];
                // search Quest ID by using Title Text in database`s ID row.
                Cursor ResultCursor = db.query(
                        QuestDataBaseBasic.TABLE_NAME,// The table to query
                        ColumnForSearch,// The array of columns to return (pass null to get all)
                        QuestDataBaseBasic.COLUMN_NAME_QuestTitle,// The columns for the WHERE clause
                        TitleForSearch,          // The values for the WHERE clause
                        null,                   // don't group the rows
                        null,                   // don't filter by row groups
                        null               // The sort order
                );
                //return the ID information and logging it.
                if(ResultCursor.getCount() > 0){
                    ResultCursor.moveToPosition(0);
                    ShowingQuestID[0] = ResultCursor.getString(ResultCursor.getColumnIndex(QuestDataBaseBasic.DataBaseEntry._ID));
                    ResultCursor.close();
                    //confirm to change the data in database row according the ID.
                    values.put(QuestDataBaseBasic.COLUMN_NAME_QuestTitle,TypeQuestTitle.getText().toString());
                    values.put(QuestDataBaseBasic.COLUMN_NAME_QuestAnswer,TypeQuestAnswer.getText().toString());
                    values.put(QuestDataBaseBasic.COLUMN_NAME_QuestType,getString(R.string.QuestQuestionTypeTran));
                    values.put(QuestDataBaseBasic.COLUMN_NAME_QuestLevel,ChooseQuestLevelBar.getProgress()+ 1);
                    values.put(QuestDataBaseBasic.COLUMN_NAME_QuestHint,"Nothing");
                    long newRodId = db.update(QuestDataBaseBasic.TABLE_NAME,values,QuestDataBaseBasic.DataBaseEntry._ID,ShowingQuestID);
                }else{
                    //if return is null, stop the process.
                    SupportClass.CreateOnlyTextDialog(this,
                            "Error",
                            "The Quest which you want to change is Not Exist.",
                            getString(R.string.ConfirmWordTran),
                            "Nothing",
                            true
                    );
                }
            }
            //reset the user interface.
            TypeQuestTitle.setText("");
            TypeQuestAnswer.setText("");
            ChooseQuestLevelBar.setProgress(0);

            //save current total number of id,and show it to user by this sub method.
            SavedShowIdNumber();
        }
    }

    //sub method of StoreQuest.
    //Thanks to:https://www.jianshu.com/p/59b266c644f3!
    @SuppressLint("SetTextI18n")
    private void SavedShowIdNumber(){
        TextView ShowQuestNumber = findViewById(R.id.ShowQuestNumber);
        SharedPreferences IdNumberInfo = getSharedPreferences("IdNumberStoreProfile", MODE_PRIVATE);
        int IdNumber = IdNumberInfo.getInt("IdNumber", 0);
        //获取Editor
        SharedPreferences.Editor editor= IdNumberInfo.edit();
        //得到Editor后，写入需要保存的数据
        IdNumber = IdNumber + 1 ;
        editor.putInt("IdNumber", IdNumber);
        editor.apply();//提交修改
        ShowQuestNumber.setText(IdNumber + "");
    }

    @SuppressLint("SetTextI18n")
    private void getIdNumberInfo(){
        SharedPreferences IdNumberInfo = getSharedPreferences("IdNumberStoreProfile", MODE_PRIVATE);
        TextView ShowQuestNumber = findViewById(R.id.ShowQuestNumber);
        QuestTotalNumber = IdNumberInfo.getInt("IdNumber", 0);
        ShowQuestNumber.setText(QuestTotalNumber + "");
    }//end of Quest Making system.


    //Quest Editor system.
    //the selected Quest ID in database.
    int CurrentPosition = 0;
    boolean EditorMode = false;
    String SearchMode = "All";
    int QuestTotalNumber = 0;
    int SearchResultLength = 0;

    private void InitializingQuestEditor(){
        Switch EditorModeSwitch = findViewById(R.id.EditorModeSwitch);
        //if Quest is less than or equals to 1, the Editor will be disabled to prevent from Bug.
        if(QuestTotalNumber <= 1){
            EditorModeSwitch.setClickable(false);
        }
    }

    public void ChangeEditorMode(View view){
        ConstraintLayout QuestEditorLayout = findViewById(R.id.QuestEditorLayout);
        Switch EditorModeSwitch = findViewById(R.id.EditorModeSwitch);
        EditorMode = EditorModeSwitch.isChecked();
        if(EditorMode){
            QuestEditorLayout.setVisibility(View.VISIBLE);
        }else{
            QuestEditorLayout.setVisibility(View.GONE);
        }
    }

    @SuppressLint("SetTextI18n")
    public void ChangeCurrentPosition(View view){
        Button AnyButtonView = findViewById(view.getId());
        String ButtonText = AnyButtonView.getText().toString();
        TextView CurrentQuestNumberView = findViewById(R.id.CurrentQuestNumberView);
        //1.decide the Position for Cursor.
        if(ButtonText.equals("-") && CurrentPosition > 0){
            CurrentPosition = CurrentPosition - 1;
        }else if(ButtonText.equals("+") && CurrentPosition <= QuestTotalNumber){
            CurrentPosition = CurrentPosition + 1;
        }else{
            CurrentPosition = 0;
        }
        //2.return searching result by asking Cursor according to Position.
        ShowReturnedQuest();
        //3.receive the length of cursor, show it to user.
        CurrentQuestNumberView.setText(CurrentPosition + "/" + SearchResultLength);
    }

    //sub method of ShowSearchedQuest and ShowChosenQuest method.
    @SuppressLint("SetTextI18n")
    public void ChangeSearchMode(View view){
        RadioButton AllSearchMode = findViewById(R.id.AllSearchMode);
        RadioButton SearchByQuestMode = findViewById(R.id.SearchByQuestMode);
        RadioButton SearchByOrderMode = findViewById(R.id.SearchByOrderMode);
        //1.change MODE variable according to RadioButton State.
        if(AllSearchMode.isChecked()){
            SearchMode = "All";
        }else if(SearchByQuestMode.isChecked()){
            SearchMode = "Quest";
        }else if(SearchByOrderMode.isChecked()){
            SearchMode = "Order";
        }
        //2.after change Mode, refresh the result of Searching.
        ShowReturnedQuest();
    }

    //return the Quest according Search Input.Sub method.
    @SuppressLint("SetTextI18n")
    private void ShowReturnedQuest(){
        EditText TypeQuestTitle = findViewById(R.id.TypeQuestTitle);
        EditText QuestSearchView = findViewById(R.id.QuestSearchView);
        TextView TypeQuestAnswer = findViewById(R.id.TypeQuestAnswer);
        TextView ShowQuestLevelView = findViewById(R.id.ShowQuestLevelView);
        TextView CurrentQuestNumberView = findViewById(R.id.CurrentQuestNumberView);
        QuestDataBaseBasic QuestDataBase = new QuestDataBaseBasic(this);
        SQLiteDatabase db = QuestDataBase.getReadableDatabase();

        //String[] is Start with 0, but it`s length is Start from 1.
        String[] SearchInputText = new String[1];
        String BeSearchedColumn = "";
        //1.if Search Bar have some text, record it as searching material. and try to pass to Cursor.
        if(!QuestSearchView.getText().toString().equals("") && !SearchMode.equals("All")){
            //only in !SearchMode.equals("All") situation can be passed,because pass this data in "All" Mode will interrupt the result.
            SearchInputText[0] = QuestSearchView.getText().toString();
            //null in "All" Mode code is on below.
        }
        //2.according the state of Search Mode Switch, decide the column that need to be searched.
        switch (SearchMode) {
            case "Quest"://return the Quest(s) which Title contains with Text in Search Bar.
                BeSearchedColumn = "QuestTitle";
                break;
            case "Order"://return the Quest(s) which _ID is equal to input in Search Bar.
                //set id column be searched.
                BeSearchedColumn = QuestDataBaseBasic.DataBaseEntry._ID;
                break;
            case "All"://return the Quest according entire database.
                //BeSearchedColumn variable will keep null.
                SearchInputText = null;
                break;
        }
        //define rules used in searching data in database. Returning entire table in database. And the results will be sorted in the resulting Cursor.
        //return whole table in "Cursor" form.
        Cursor ResultCursor = db.query(
                QuestDataBaseBasic.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                BeSearchedColumn,              // The columns for the WHERE clause
                SearchInputText,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        //logging the Cursor`s length.
        //3.counting the total number of Searching result. And report result to User.
        int CursorLength = ResultCursor.getCount();

        SupportClass.CreateOnlyTextDialog(this,
                "Report",
                "According the Input and Chosen Search Mode,\n"+
                        "Editor get:"+ CursorLength + "Result(s).",
                getString(R.string.ConfirmWordTran),
                "Nothing",
                true
        );
        //4.return the searching result to user.
        //choose a line in table "ResultCursor" , return all data we need in this line of table.
        //show these content to user.
        if(CursorLength > 0){
            //prevent from NullPointerException.
            CurrentPosition = 0;
            ResultCursor.moveToPosition(CurrentPosition);
            CurrentQuestNumberView.setText(CurrentPosition + 1 + "/" + CursorLength);
            TypeQuestTitle.setText(ResultCursor.getString(ResultCursor.getColumnIndex("QuestTitle")));
            TypeQuestAnswer.setText(ResultCursor.getString(ResultCursor.getColumnIndex("QuestAnswer")));
            ShowQuestLevelView.setText(ResultCursor.getInt(ResultCursor.getColumnIndex("QuestLevel")));
            //4.1 return the Cursor Length to show to user.
            SearchResultLength = ResultCursor.getCount();
            ResultCursor.close();
        }else{
            //if return is null, stop the process.
            SupportClass.CreateOnlyTextDialog(this,
                    "Error",
                    "The Search Result is Not Exist.",
                    getString(R.string.ConfirmWordTran),
                    "Nothing",
                    true
            );
        }
    }//end of Quest Editor system.


    //AppMode Management system.
    public void ChooseAppMode(View view){
        //basic preparation.
        RadioButton NormalMode = findViewById(R.id.ChooseNormalMode);//view number id 1000178
        RadioButton GameMode = findViewById(R.id.ChooseGameMode);//view number id 1000226
        RadioButton FocusMode = findViewById(R.id.ChooseFocusMode);
        RadioGroup AppModeChoice = findViewById(R.id.ChooseAppModeView);
        SharedPreferences ChooseAppMode = getSharedPreferences("ChooseAppModeProfile", MODE_PRIVATE);
        SharedPreferences.Editor editor= ChooseAppMode.edit();

        int ModeCheckedId;
        ModeCheckedId = AppModeChoice.getCheckedRadioButtonId();
        if (ModeCheckedId == NormalMode.getId()){
            editor.putString("AppMode", "Normal");
        }else if(ModeCheckedId == GameMode.getId()) {
            editor.putString("AppMode", "Game");
        }else if(ModeCheckedId == FocusMode.getId()){
            editor.putString("AppMode","Focus");
        }
        editor.apply();
    }

    private void InitializingAppMode(){
        SharedPreferences ChooseAppMode = getSharedPreferences("ChooseAppModeProfile", MODE_PRIVATE);
        String AppModeText = ChooseAppMode.getString("AppMode", "Normal");
        RadioButton NormalMode = findViewById(R.id.ChooseNormalMode);//view number id 1000178
        RadioButton GameMode = findViewById(R.id.ChooseGameMode);//view number id 1000226
        RadioButton FocusMode = findViewById(R.id.ChooseFocusMode);
        RadioGroup AppModeChoice = findViewById(R.id.ChooseAppModeView);
        assert AppModeText != null;
        switch (AppModeText) {
            case "Game":
                AppModeChoice.check(GameMode.getId());
                break;
            case "Focus":
                AppModeChoice.check(FocusMode.getId());
                break;
            default:
                //do it just for more reliable.
                AppModeChoice.check(NormalMode.getId());
                break;
        }
    }

    public void ShowModeInfoDialog(View view){
        SupportClass.CreateOnlyTextDialog(this,
                getString(R.string.HelpWordTran),
                "[Normal] Mode:\n"+
                "Provide standard experience of entire App.\n"+
                "[Focus] Mode:\n"+
                "Ripe off all Game-ish elements on Main Page, EXP and Points system will not showed. Give user full Studying experience\n"+
                "[Game] Mode:\n"+
                "Provide Game-ish Function to User on Main Page, You can get EXP and Points as reward of Answering Quest. And EXP and Points system will be showed.",
                getString(R.string.ConfirmWordTran),
                "Nothing",
                true
        );
    }//end of AppMode Management system.


    //layout management system.
    public void CloseFunctionLayout(View view){
        //0.preparation: listing all Function Layout in this activity.
        Button ReturnSettingButton = findViewById(R.id.ReturnSettingButton);
        LinearLayout ButtonLayout = findViewById(R.id.ButtonLayout);

        //1.Close all Layout.
        CloseAllLayout();

        //2. showing ButtonLayout and close ReturnButton showing to provide user Functions which can be chosen.
        ButtonLayout.setVisibility(View.VISIBLE);
        ReturnSettingButton.setVisibility(View.GONE);
    }

    //main method.
    @SuppressLint("SetTextI18n")
    public void OpenFunctionLayout(View view){
        Button ReturnSettingButton = findViewById(R.id.ReturnSettingButton);
        LinearLayout ButtonLayout = findViewById(R.id.ButtonLayout);
        ConstraintLayout ModeChooseLayout = findViewById(R.id.ModeChooseLayout);
        ConstraintLayout QuestSettingLayout = findViewById(R.id.QuestSettingLayout);
        ConstraintLayout QuestEditorLayout = findViewById(R.id.QuestEditorLayout);

        //1.close all Function Layout before management, to do it easily.
        CloseAllLayout();

        //2.confirm that which button has been touched, and return it`s Text to use in next step as recognizing identifier.
        //thanks to: https://stackoverflow.com/questions/3412180/how-to-determine-which-button-pressed-on-android !
        Button AnyButtonView = findViewById(view.getId());
        String ButtonText = AnyButtonView.getText().toString();

        //3.open certain Layout. and record which layout have been opened.
        if(ButtonText.equals(getString(R.string.AboutQuestTitleTran))){
            QuestSettingLayout.setVisibility(View.VISIBLE);
            QuestEditorLayout.setVisibility(View.GONE);
        }else if(ButtonText.equals(getString(R.string.AppModeWordTran))){
           ModeChooseLayout.setVisibility(View.VISIBLE);
        }else if(ButtonText.equals(getString(R.string.AdditionalFunctionTran))){

        }

        //4.show return button to user.And close choose Function layout.
        ReturnSettingButton.setText(getString(R.string.ReturnToWordTran)+ ButtonText +" >");
        ReturnSettingButton.setVisibility(View.VISIBLE);
        ButtonLayout.setVisibility(View.GONE);
    }

    //sub method.
    private void CloseAllLayout(){
        Button ReturnSettingButton = findViewById(R.id.ReturnSettingButton);
        ConstraintLayout ModeChooseLayout = findViewById(R.id.ModeChooseLayout);
        ConstraintLayout QuestSettingLayout = findViewById(R.id.QuestSettingLayout);
        ConstraintLayout QuestEditorLayout = findViewById(R.id.QuestEditorLayout);

        ReturnSettingButton.setVisibility(View.GONE);
        ModeChooseLayout.setVisibility(View.GONE);
        QuestSettingLayout.setVisibility(View.GONE);
        QuestEditorLayout.setVisibility(View.GONE);
    }//end of Layout Management system.


    //basic support system.
    public void GoToAssistActivity(View view){
        Intent i = new Intent(this,AssistActivity.class);
        startActivity(i);
    }
}