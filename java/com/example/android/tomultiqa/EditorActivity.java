package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class EditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        //thanks to: https://dev.mi.com/console/doc/detail?pId=2229 !
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        getIdNumberInfo();
        ControlEditor(QuestTotalNumber <= 0);
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
        QuestTotalNumber = SupportClass.getIntData(this,"IdNumberStoreProfile","IdNumber",0);
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
    String[] OldTitleHint = new String[]{""};
    //String OldTitleType = "";//not used yet.
    //new Information about Quest which need to update.
    String TitleText = "";
    String AnswerText = "";
    int TitleLevel = 1;
    String TitleHint = "";
    //String TitleType = "";//not used yet.

    //cursor data.
    Cursor ResultCursor;

    //protection function.
    //if user have not search to load database to this Activity, then this variable will block user from editing to prevent Error.
    private void ControlEditor(boolean CloseFunction){
        ImageView LastQuestButton = findViewById(R.id.LastQuestButton);
        ImageView NextQuestButton = findViewById(R.id.NextQuestButton);
        ImageView EditLevelButton = findViewById(R.id.EditLevelButton);
        EditText EditTitleView = findViewById(R.id.EditTitleView);
        EditText EditAnswerView = findViewById(R.id.EditAnswerView);
        EditText EditHintView = findViewById(R.id.EditHintView);
        ConstraintLayout DataBaseEditLayout = findViewById(R.id.DataBaseEditLayout);
        TextView SaveStateView = findViewById(R.id.SaveStateView);
        TextView TitleLevelView = findViewById(R.id.TitleLevelView);

        LastQuestButton.setEnabled(CloseFunction);
        NextQuestButton.setEnabled(CloseFunction);
        EditLevelButton.setEnabled(CloseFunction);
        EditTitleView.setEnabled(CloseFunction);
        EditAnswerView.setEnabled(CloseFunction);
        EditHintView.setEnabled(CloseFunction);

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
            EditHintView.setText("--");
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
        //1.research database by id.
        //define rules used in searching data in database. Returning entire table in database. And the results will be sorted in the resulting Cursor.
        //return whole table in "Cursor" form.
        if(SearchMode.equals("All")){
            QueryCursor();
            if(ResultCursor != null){
                ResultCursor.moveToFirst();
                LoadDataFromCursor();
                ControlEditor(true);
                if(!ResultCursor.isLast()){
                    SupportClass.CreateNoticeDialog(this,
                            getString(R.string.NoticeWordTran),
                            ResultCursor.getCount() + " " + getString(R.string.ResultFoundedTran) + "\n" +
                                    getString(R.string.SwitchResultByButtonTran) + "\n",
                            getString(R.string.ConfirmWordTran));
                }
            }else {
                SupportClass.CreateNoticeDialog(this,
                        getString(R.string.ErrorWordTran),
                        getString(R.string.NoResultWordTran),
                        getString(R.string.ConfirmWordTran));
            }
        }
    }

    //Editor system method.
    public void ShowNextResult(View view){
        boolean NotLast = ResultCursor.moveToNext();//without consider the position, cursor will be moved.
        if(!NotLast){
            ResultCursor.moveToFirst();
        }
        LoadDataFromCursor();
    }

    //Editor system method.
    public void ShowLastResult(View view){
        boolean NotFirst = ResultCursor.moveToPrevious();//without consider the position, cursor will be moved.
        if(!NotFirst){
            ResultCursor.moveToLast();
        }
        LoadDataFromCursor();
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
                        (dialogInterface, i) -> {
                            //1. get the number in String form, and transform it to int form.
                            //thanks to: https://stackoverflow.com/questions/5585779/how-do-i-convert-a-string-to-an-int-in-java?rq=1 !
                            int NumberInText = SupportClass.getInputNumber(InputView);//store the exact Number in Code in int form.
                            try {
                                //2. get the first number in String, and transform it to int form.
                                if(NumberInText > 0 && NumberInText <= 10){
                                    TitleLevel = NumberInText;
                                }else{
                                    SupportClass.CreateNoticeDialog(EditorActivity.this,
                                            getString(R.string.ErrorWordTran),
                                            getString(R.string.InputCastFailTran),
                                            getString(R.string.ConfirmWordTran)
                                    );
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
        ContentValues HintValue = new ContentValues();
        ContentValues LevelValue = new ContentValues();
        QuestDataBaseBasic QuestDataBase = new QuestDataBaseBasic(this);
        SQLiteDatabase db = QuestDataBase.getWritableDatabase();
        EditText EditTitleView = findViewById(R.id.EditTitleView);
        EditText EditAnswerView = findViewById(R.id.EditAnswerView);
        EditText EditHintView = findViewById(R.id.EditHintView);

        //1.decide the data needed.
        TitleText = EditTitleView.getText().toString();
        AnswerText = EditAnswerView.getText().toString();
        TitleHint = EditHintView.getText().toString();
        //TitleLevel`s value is depend on global int variable.

        //2.casting data and location where needed in update into ContentValue form "pack". and ready to transport to database.
        if(!TitleText.equals("") && !AnswerText.equals("")){
            TitleValue.put("QuestTitle",TitleText);
            AnswerValue.put("QuestAnswer",AnswerText);
            HintValue.put("QuestHint",TitleHint);
            LevelValue.put("QuestLevel",TitleLevel);

            //2.after which data should deliver to database, use database class ""upgrade" method to update data.
            db.update("Quest", TitleValue, "QuestTitle = ?", OldTitle);
            db.update("Quest", AnswerValue,"QuestAnswer = ?",OldAnswer);
            db.update("Quest",HintValue,"QuestHint = ?",OldTitleHint);
            db.update("Quest", LevelValue,"QuestTitle = ?",OldTitle);//update Quest which have Certain Title 's Level data.
            //3. finish change, report to user.
            SupportClass.CreateNoticeDialog(this,
                    getString(R.string.NoticeWordTran),
                    getString(R.string.QuestCompletedTran) + "\n" +
                            getString(R.string.QuestShowTitleTran) + "\n" +
                            OldTitle[0] + " → " + TitleText + "\n" +
                            getString(R.string.SetQuestAnswerTitleTran) + "\n" +
                            OldAnswer[0] + " → " + AnswerText + "\n" +
                            getString(R.string.HintWordTran) + "\n" +
                            OldTitleHint[0] + " → " + TitleHint + "\n" +
                            getString(R.string.QuestLevelWordTran) + "\n" +
                            OldTitleLevel[0] + " → " + TitleLevel + "\n" +
                            getString(R.string.ReQuestEditTran),
                    getString(R.string.ConfirmWordTran)
            );
        }else{
            SupportClass.CreateNoticeDialog(this,
                    getString(R.string.NoticeWordTran),
                    getString(R.string.EditEmptyFailTran),
                    getString(R.string.ConfirmWordTran));
        }

        //4.close the Editor to prevent Edit which not excepted.
        ControlEditor(false);
    }

    public void DeleteSingleQuest(View view){
        //1.decide the data needed.
        EditText EditTitleView = findViewById(R.id.EditTitleView);
        EditText EditAnswerView = findViewById(R.id.EditAnswerView);
        TitleText = EditTitleView.getText().toString();
        AnswerText = EditAnswerView.getText().toString();
        //1.1 show confirm dialog to user.
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.WarningWordTran));
        dialog.setMessage(getString(R.string.DeleteThisQuestTran) + "?\n" +
                getString(R.string.SetQuestTitleTran) + "\n" +
                OldTitle[0] + "\n" +
                getString(R.string.SetQuestAnswerTitleTran) + "\n" +
                OldAnswer[0] + "\n" +
                getString(R.string.HintWordTran) + "\n" +
                OldTitleHint[0]
        );
        dialog.setCancelable(true);
        dialog.setPositiveButton(
                getString(R.string.ConfirmWordTran),
                (dialog12, id) -> {
                    if(!TitleText.equals("") && !AnswerText.equals("")){
                        //2.preparation.
                        QuestDataBaseBasic QuestDataBase = new QuestDataBaseBasic(this);
                        SQLiteDatabase db = QuestDataBase.getWritableDatabase();
                        //3.delete Quest which have certain Title.
                        db.delete(QuestDataBaseBasic.TABLE_NAME,"QuestTitle = ?", OldTitle);
                        //3.1 after deleted, total number of Quest should re-calculate.
                        QuestTotalNumber = QueryCursor();//it might delete multi Quest(with same Title), so we can not only minus 1.
                        SupportClass.saveIntData(this,"IdNumberStoreProfile","IdNumber", QuestTotalNumber);
                        //3.2 after deleted, change the behavior of Editor.
                        if(QuestTotalNumber <= 0){
                            ControlEditor(true);
                            finish();//make sure data correct.
                        }else{
                            LoadDataFromCursor();//refresh Editor (load another Quest).
                        }
                    }else{
                        SupportClass.CreateNoticeDialog(this,
                                getString(R.string.NoticeWordTran),
                                getString(R.string.EditEmptyFailTran),
                                getString(R.string.ConfirmWordTran));
                    }
                    dialog12.cancel();
                });
        dialog.setNegativeButton(
                getString(R.string.CancelWordTran),
                (dialog1, id) -> dialog1.cancel());
        AlertDialog DialogView = dialog.create();
        DialogView.show();
    }

    //Editor system method.
    public void WipeDataBase(View view){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.WarningWordTran));
        dialog.setMessage(getString(R.string.WipeAllQuestTran));
        dialog.setCancelable(true);
        dialog.setPositiveButton(
                getString(R.string.ConfirmWordTran),
                (dialog12, id) -> {
                    QuestDataBaseBasic QuestDataBase = new QuestDataBaseBasic(EditorActivity.this);
                    SQLiteDatabase db = QuestDataBase.getWritableDatabase();
                    QuestDataBase.DeleteEntire(db);
                    ControlEditor(true);
                    QuestTotalNumber = 0;
                    SupportClass.saveIntData(this,"IdNumberStoreProfile","IdNumber", QuestTotalNumber);
                    dialog12.cancel();
                    finish();
                });
            dialog.setNegativeButton(
                    getString(R.string.CancelWordTran),
                    (dialog1, id) -> dialog1.cancel());
        AlertDialog DialogView = dialog.create();
        DialogView.show();
    }//end of Editor system.


    //basic support system.
    @SuppressLint("SetTextI18n")
    private void LoadDataFromCursor(){
        EditText EditTitleView = findViewById(R.id.EditTitleView);
        EditText EditAnswerView = findViewById(R.id.EditAnswerView);
        EditText EditHintView = findViewById(R.id.EditHintView);
        TextView TitleLevelView = findViewById(R.id.TitleLevelView);
        OldTitle[0] = ResultCursor.getString(ResultCursor.getColumnIndex("QuestTitle")) ;
        OldAnswer[0] = ResultCursor.getString(ResultCursor.getColumnIndex("QuestAnswer"));
        OldTitleHint[0] = ResultCursor.getString(ResultCursor.getColumnIndex("QuestHint"));
        TitleLevel = ResultCursor.getInt(ResultCursor.getColumnIndex("QuestLevel"));
        OldTitleLevel[0] = String.valueOf(TitleLevel);
        //report the result to user.
        EditTitleView.setText(OldTitle[0] + "");
        EditAnswerView.setText(OldAnswer[0] + "");
        EditHintView.setText(OldTitleHint[0] + "");
        TitleLevelView.setText(OldTitleLevel[0] + "");
        RefreshResultNumber();
    }

    private int QueryCursor(){
        QuestDataBaseBasic QuestDataBase = new QuestDataBaseBasic(this);
        SQLiteDatabase db = QuestDataBase.getReadableDatabase();
        ResultCursor = db.query(
                QuestDataBaseBasic.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        if(ResultCursor.moveToFirst()){
            return ResultCursor.getCount();
        }else{
            return 0;
        }
    }

    //ResultCursor.getPosition() + 1: turn number which based on 0 to based on 1.
    @SuppressLint("SetTextI18n")
    private void RefreshResultNumber(){
        TextView CurrentQuestNumberView = findViewById(R.id.CurrentQuestNumberView);
        CurrentQuestNumberView.setText( (ResultCursor.getPosition() + 1 ) + " / " + ResultCursor.getCount());
    }
}