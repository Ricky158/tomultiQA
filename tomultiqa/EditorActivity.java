package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class EditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        ControlEditor(false);
        getIdNumberInfo();

        //Close all RadioButton.Because we don`t finish the Multi Search Mode yet.
        RadioButton AllSearchMode = findViewById(R.id.AllSearchMode);
        RadioButton SearchByQuestMode = findViewById(R.id.SearchByQuestMode);
        RadioButton SearchByOrderMode = findViewById(R.id.SearchByOrderMode);
        AllSearchMode.setClickable(false);
        SearchByQuestMode.setClickable(false);
        SearchByOrderMode.setClickable(false);
    }

    //sub method of StoreQuest.
    //Thanks to:https://www.jianshu.com/p/59b266c644f3!
    @SuppressLint("SetTextI18n")
    private void getIdNumberInfo(){
        SharedPreferences IdNumberInfo = getSharedPreferences("IdNumberStoreProfile", MODE_PRIVATE);
        QuestTotalNumber = IdNumberInfo.getInt("IdNumber", 0);
    }//end of Quest Making system.


    //Quest Editor system.
    //thanks to: https://blog.csdn.net/IT_XF/article/details/82591770 !
    // https://developer.android.google.cn/training/data-storage/sqlite !
    String SearchMode = "All";
    int QuestTotalNumber = 0;

    //Quest Information.
    //old Information about Quest which need to update.
    String[] OldTitle = new String[]{""};
    String[] OldAnswer = new String[]{""};
    String[] OldTitleLevel = new String[]{"1"};
    //String OldTitleHintText = "";//not used yet.
    //String OldTitleType = "";//not used yet.
    //new Information about Quest which need to update.
    String TitleText = "";
    String AnswerText = "";
    int TitleLevel = 1;
    //String TitleHintText = "";//not used yet.
    //String TitleType = "";//not used yet.

    //cursor data.
    Cursor ResultCursor;
    int CursorPosition = 1;

    //protection function.
    //if user have not search to load database to this Activity, then this variable will block user from editing to prevent Error.
    private void ControlEditor(boolean CloseFunction){
        ImageView LastQuestButton = findViewById(R.id.LastQuestButton);
        ImageView NextQuestButton = findViewById(R.id.NextQuestButton);
        ImageView EditLevelButton = findViewById(R.id.EditLevelButton);
        EditText EditTitleView = findViewById(R.id.EditTitleView);
        EditText EditAnswerView = findViewById(R.id.EditAnswerView);
        LinearLayout DataBaseEditLayout = findViewById(R.id.DataBaseEditLayout);
        TextView SaveStateView = findViewById(R.id.SaveStateView);
        TextView TitleLevelView = findViewById(R.id.TitleLevelView);

        LastQuestButton.setEnabled(CloseFunction);
        NextQuestButton.setEnabled(CloseFunction);
        EditLevelButton.setEnabled(CloseFunction);
        EditTitleView.setEnabled(CloseFunction);
        EditAnswerView.setEnabled(CloseFunction);

        if(CloseFunction){
            DataBaseEditLayout.setVisibility(View.VISIBLE);
            SaveStateView.setText(getString(R.string.EditNotSaveTran));
            SaveStateView.setTextColor(Color.RED);
        }else{
            DataBaseEditLayout.setVisibility(View.GONE);
            SaveStateView.setText(getString(R.string.NoQuestWaitToEditTran));
            SaveStateView.setTextColor(Color.GREEN);
            EditTitleView.setText(getString(R.string.QuestEditStartHintTran));
            EditAnswerView.setText("--");
            TitleLevelView.setText("--");
        }
    }//end of protection function.


    //Editor system method.
    public void ChangeSearchMode(View view){
        RadioButton AllSearchMode = findViewById(R.id.AllSearchMode);
        RadioButton SearchByQuestMode = findViewById(R.id.SearchByQuestMode);
        RadioButton SearchByOrderMode = findViewById(R.id.SearchByOrderMode);

        if(AllSearchMode.isChecked()){
            SearchMode = "All";
        }else if(SearchByQuestMode.isChecked()){
            SearchMode = "Quest";
        }else if(SearchByOrderMode.isChecked()){
            SearchMode = "ID";
        }
    }

    //Editor system method.
    @SuppressLint("SetTextI18n")
    public void SearchDataBase(View view){
        //0.preparation.
        //EditText QuestSearchView = findViewById(R.id.QuestSearchView);
        //String SearchInputText = QuestSearchView.getText().toString();
        QuestDataBaseBasic QuestDataBase = new QuestDataBaseBasic(this);
        SQLiteDatabase db = QuestDataBase.getReadableDatabase();

        //1.research database by id.
        //define rules used in searching data in database. Returning entire table in database. And the results will be sorted in the resulting Cursor.
        //return whole table in "Cursor" form.
        if(SearchMode.equals("All")){
            ResultCursor = db.query(
                    QuestDataBaseBasic.TABLE_NAME,   // The table to query
                    null,             // The array of columns to return (pass null to get all)
                    null,              // The columns for the WHERE clause
                    null,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    null               // The sort order
            );
            if(ResultCursor != null){
                ResultCursor.moveToFirst();
                LoadDataFromCursor();
                ControlEditor(true);
                if(!ResultCursor.isLast()){
                    SupportClass.CreateOnlyTextDialog(this,
                            getString(R.string.NoticeWordTran),
                            ResultCursor.getCount() + getString(R.string.ResultFoundedTran) + "\n" +
                                    getString(R.string.SwitchResultByButtonTran) + "\n",
                            getString(R.string.ConfirmWordTran),
                            "Nothing",
                            true);
                }
            }else {
                SupportClass.CreateOnlyTextDialog(this,
                        getString(R.string.ErrorWordTran),
                        getString(R.string.NoResultWordTran),
                        getString(R.string.ConfirmWordTran),
                        "Nothing",
                        true);
            }
            db.close();
        }
    }

    //Editor system method.
    public void ShowNextResult(View view){
        boolean IsAvailable = ResultCursor.moveToNext();
        if(IsAvailable && CursorPosition < ResultCursor.getCount()){
            CursorPosition = CursorPosition + 1;
            LoadDataFromCursor();
        }else{
            Toast.makeText(getApplicationContext(), getString(R.string.NoMoreResultTran), Toast.LENGTH_SHORT).show();
        }
    }

    //Editor system method.
    public void ShowLastResult(View view){
        boolean IsAvailable = ResultCursor.moveToPrevious();
        if(IsAvailable && CursorPosition > 1){
            CursorPosition = CursorPosition - 1;
            LoadDataFromCursor();
        }else{
            Toast.makeText(getApplicationContext(), getString(R.string.NoLessResultTran), Toast.LENGTH_SHORT).show();
        }
    }

    //Editor system method.
    @SuppressLint("SetTextI18n")
    public void ChangeTitleLevel(View view){
        final EditText InputView = new EditText(this);
        final TextView TitleLevelView = findViewById(R.id.TitleLevelView);
        //set this EditText only accept Number input.
        InputView.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.InputQuestLevelTran))
                .setView(InputView)
                .setPositiveButton(getString(R.string.ConfirmWordTran),
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String InputText = InputView.getText().toString();
                        //1. get the number in String form, and transform it to int form.
                        //thanks to: https://stackoverflow.com/questions/5585779/how-do-i-convert-a-string-to-an-int-in-java?rq=1 !
                        int NumberInText = Integer.parseInt(InputText);//store the exact Number in Code in int form.
                        try {
                            //2. get the first number in String, and transform it to int form.
                            if(NumberInText > 0 && NumberInText <= 10 && !InputText.isEmpty()){
                                TitleLevel = NumberInText;
                            }else{
                                SupportClass.CreateOnlyTextDialog(EditorActivity.this,
                                        getString(R.string.ErrorWordTran),
                                        getString(R.string.InputCastFailTran),
                                        getString(R.string.ConfirmWordTran),
                                        "Nothing",
                                        true);
                            }
                        }
                        catch (NumberFormatException e)
                        {
                            TitleLevel = 1;
                            //3.try to catch the error, if there are no number in Resource Code String.
                            //this method will give this variable a default value, to prevent it from crash.
                        }
                        TitleLevelView.setText(TitleLevel + "");
                        Toast.makeText(getApplicationContext(),getString(R.string.SavingCompletedTran),Toast.LENGTH_LONG).show();
                        //end of after input confirm.
                        }
                });
        //3. Use this object to create a actual View in android.
        AlertDialog DialogView = dialog.create();
        DialogView.show();
    }

    //Editor system method.
    public void UpdateData(View view){
        //0.preparation.
        ContentValues TitleValue = new ContentValues();
        ContentValues AnswerValue = new ContentValues();
        ContentValues LevelValue = new ContentValues();
        QuestDataBaseBasic QuestDataBase = new QuestDataBaseBasic(this);
        SQLiteDatabase db = QuestDataBase.getWritableDatabase();
        EditText EditTitleView = findViewById(R.id.EditTitleView);
        EditText EditAnswerView = findViewById(R.id.EditAnswerView);

        //1.decide the data needed.
        TitleText = EditTitleView.getText().toString();
        AnswerText = EditAnswerView.getText().toString();
        //TitleLevel`s value is depend on global int variable.

        //2.casting data and location where needed in update into ContentValue form "pack". and ready to transport to database.
        if(!TitleText.equals("") && !AnswerText.equals("")){
            TitleValue.put("QuestTitle",TitleText);
            AnswerValue.put("QuestAnswer",AnswerText);
            LevelValue.put("QuestLevel",TitleLevel);

            //2.after which data should deliver to database, use database class ""upgrade" method to update data.
            db.update("Quest", TitleValue, "QuestTitle = ?", OldTitle);
            db.update("Quest", AnswerValue,"QuestAnswer = ?",OldAnswer);
            db.update("Quest", LevelValue,"QuestLevel = ?",OldTitleLevel);

            //3. finish change, report to user.
            SupportClass.CreateOnlyTextDialog(this,
                    getString(R.string.NoticeWordTran),
                    getString(R.string.QuestCompletedTran) + "\n" +
                            getString(R.string.QuestShowTitleTran) + "\n" +
                            OldTitle[0] + " → " + TitleText + "\n" +
                            getString(R.string.SetQuestAnswerTitleTran) + "\n" +
                            OldAnswer[0] + " → " + AnswerText + "\n" +
                            getString(R.string.QuestLevelWordTran) + "\n" +
                            OldTitleLevel[0] + " → " + TitleLevel + "\n" +
                            getString(R.string.ReQuestEditTran),
                    getString(R.string.ConfirmWordTran),
                    "Nothing",
                    true
            );
        }else{
            SupportClass.CreateOnlyTextDialog(this,
                    getString(R.string.NoticeWordTran),
                    getString(R.string.EditEmptyFailTran),
                    getString(R.string.ConfirmWordTran),
                    "Nothing",
                    true);
        }

        //4.close the Editor to prevent Edit which not excepted.
        ControlEditor(false);
    }

    //Editor system method.
    public void WipeDataBase(View view){
        SharedPreferences IdNumberInfo = getSharedPreferences("IdNumberStoreProfile", MODE_PRIVATE);
        SharedPreferences.Editor editor= IdNumberInfo.edit();

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.WarningWordTran));
        dialog.setMessage(getString(R.string.WipeAllQuestTran));
        dialog.setCancelable(true);

        dialog.setPositiveButton(
                getString(R.string.ConfirmWordTran),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        QuestDataBaseBasic QuestDataBase = new QuestDataBaseBasic(EditorActivity.this);
                        SQLiteDatabase db = QuestDataBase.getWritableDatabase();
                        QuestDataBase.DeleteEntire(db);
                        ControlEditor(true);
                        dialog.cancel();
                    }
                });
            dialog.setNegativeButton(
                    getString(R.string.CancelWordTran),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
        AlertDialog DialogView = dialog.create();
        DialogView.show();

        QuestTotalNumber = 0;
        editor.putInt("IdNumber", QuestTotalNumber);
        editor.apply();
    }//end of Editor system.


    //basic support system.
    @SuppressLint("SetTextI18n")
    private void LoadDataFromCursor(){
        EditText EditTitleView = findViewById(R.id.EditTitleView);
        EditText EditAnswerView = findViewById(R.id.EditAnswerView);
        TextView TitleLevelView = findViewById(R.id.TitleLevelView);
        OldTitle[0] =ResultCursor.getString(ResultCursor.getColumnIndex("QuestTitle")) ;
        OldAnswer[0] =ResultCursor.getString(ResultCursor.getColumnIndex("QuestAnswer"));
        TitleLevel = ResultCursor.getInt(ResultCursor.getColumnIndex("QuestLevel"));
        OldTitleLevel[0] = String.valueOf(TitleLevel);
        //report the result to user.
        EditTitleView.setText(OldTitle[0] + "");
        EditAnswerView.setText(OldAnswer[0] + "");
        TitleLevelView.setText(OldTitleLevel[0] + "");
        RefreshResultNumber();
    }

    @SuppressLint("SetTextI18n")
    private void RefreshResultNumber(){
        TextView CurrentQuestNumberView = findViewById(R.id.CurrentQuestNumberView);
        CurrentQuestNumberView.setText(CursorPosition + " / " + ResultCursor.getCount());
    }
}