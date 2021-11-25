package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;

public class EditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        //thanks to: https://dev.mi.com/console/doc/detail?pId=2229 !
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        //initializing Mode Spinner.
        LoadModeSpinner();
        //3.get Quest number from SharedPreference instead of database to improve loading time.
        QuestTotalNumber = SupportLib.getIntData(this,"IdNumberStoreProfile","IdNumber",0);
        //3.1 if Quest number is 0, close the UI accessibility to prevent from user caused NPE.
        ControlEditor(QuestTotalNumber <= 0);
    }


    //menu search system.
    //thanks to: https://stackoverflow.com/questions/17425742/how-to-add-button-in-actionbarandroid !
    // https://blog.csdn.net/oxiaoxio/article/details/48900763 !
    //search view:
    // https://blog.csdn.net/cx1229/article/details/70890633 !
    // https://developer.android.google.cn/reference/androidx/appcompat/widget/SearchView.OnQueryTextListener !
    // https://blog.csdn.net/allbule/article/details/52808277 !
    // https://blog.csdn.net/yechaoa/article/details/80658940 !
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //preparation.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editor_activity_button, menu);
        MenuItem menuSearch = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) menuSearch.getActionView();
        //set submit search icon inside of the Search Bar.
        searchView.setSubmitButtonEnabled(true);
        //set hint to tell user how to search.
        //because of source code limitation, you can't search with null or "" text.
        //so we need to tell user to input anything to start.
        searchView.setQueryHint(getString(R.string.AllModeHintTran));
        //set listener.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String input) {
                //1.decide method should search which part of database.
                String Selection = null;
                String[] SelectionArgs = null;
                switch (SearchMode){
                    case 0:
                        break;//if in "All" search mode, just skip this switch and not change any default value.
                    case 1:
                        Selection = QuestDbHelper.DataBaseEntry.COLUMN_NAME_QuestTitle + " = ?";
                        SelectionArgs = new String[]{input};
                        break;
                    case 2:
                        Selection = QuestDbHelper.DataBaseEntry.COLUMN_NAME_QuestAnswer + " = ?";
                        SelectionArgs = new String[]{input};
                        break;
                    case 3:
                        Selection = QuestDbHelper.DataBaseEntry.COLUMN_NAME_QuestHint + " = ?";
                        SelectionArgs = new String[]{input};
                        break;
                    case 4:
                        Selection = QuestDbHelper.DataBaseEntry.COLUMN_NAME_QuestLevel + " = ?";
                        SelectionArgs = new String[]{input};
                        break;
                }
                //close the SearchView and Keyboard without UI wrong movement. if directly use searchView.collapseActionView() method.
                //2.1 clear the focus state of input, to help user faster switch to Quest Edit.
                searchView.clearFocus();
                //after it executed, the Search icon will move to right place of Return icon.
                //see: https://stackoverflow.com/questions/13613791/how-to-completely-collapse-a-searchview-after-an-item-selected
                menuSearch.collapseActionView();
                //2.2 after define the SearchMode, do search.
                //if SearchMode = 0, it means method did a search and refreshed UI, then skip this part of code to prevent from repeat loading.
                QueryCursor(Selection,SelectionArgs);
                //3.initialize.
                //3.1 base dialog structure.
                AlertDialog.Builder dialog = new AlertDialog.Builder(EditorActivity.this);
                dialog.setTitle(getString(R.string.NoticeWordTran));
                dialog.setCancelable(true);
                dialog.setPositiveButton(
                        getString(R.string.ConfirmWordTran),
                        (dialog1, id) -> dialog1.cancel());
                if(ResultCursor != null && ResultCursor.moveToFirst()){//if get not null result.
                    //refresh UI.
                    LoadDataFromCursor();
                    //close another function, to prevent from user's wrong operation.
                    ControlEditor(true);
                    //if return not null, show result number to user.
                    dialog.setMessage(ResultCursor.getCount() + " " + getString(R.string.ResultFoundedTran) + "\n" +
                            getString(R.string.SwitchResultByButtonTran));
                }else{
                    //if no result available.
                    dialog.setMessage(getString(R.string.NoResultWordTran));
                    //close Editor UI, to prevent from empty operation.
                    ControlEditor(false);
                }
                //4.end of method.
                //4.1 prepare final dialog.
                AlertDialog DialogView = dialog.create();
                //4.2 after View has been created, use api to remove black cover behind dialog.
                //thanks to: https://blog.csdn.net/summer_ck/article/details/79614582 !
                Window window = DialogView.getWindow();
                window.setDimAmount(0.04f);
                //4.3 show to user.
                DialogView.show();
                //4.4 refresh the counter UI to notice user.
                RefreshResultNumber();
                return true;//true if the query has been handled by the listener, false to let the SearchView perform the default action.
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;//false if the SearchView should perform the default action of showing any suggestions if available, true if the action was handled by the listener.
            }
        });
        return true;
    }//end of menu search system.


    //Quest Editor system.
    //thanks to: https://blog.csdn.net/IT_XF/article/details/82591770 !
    // https://developer.android.google.cn/training/data-storage/sqlite !


    //hint in code to change line: https://blog.csdn.net/longxzq/article/details/83634733 !
    /**
     * search mode: decide method should search which part of database.<br/>
     * if search mode's value not 0, then the method will follow user's input in search view,<br/>
     * return the search result which the selected part of Quest included user's INPUT.<br/>
     * 0 is for ALL, search entire database.<br/>
     * 1 is for QUEST, search Quests which Title included INPUT.<br/>
     * 2 is for ANSWER, search Quests which Answer included INPUT.<br/>
     * 3 is for HINT, search Quests which Hint included INPUT.<br/>
     * 4 is for Level, search Quests which Level included INPUT. (if Input is not a number, then Cursor will return nothing.)
     */
    int SearchMode = 0;
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
    Cursor ResultCursor = null;

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
                            int NumberInText = SupportLib.getInputNumber(InputView);//store the exact Number in Code in int form.
                            try {
                                //2. get the first number in String, and transform it to int form.
                                if(NumberInText > 0 && NumberInText <= 10){
                                    TitleLevel = NumberInText;
                                }else{
                                    SupportLib.CreateNoticeDialog(EditorActivity.this,
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
        QuestDbHelper QuestDataBase = new QuestDbHelper(this);
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
            String TableName = QuestDbHelper.TABLE_NAME;
            db.update(TableName, TitleValue, "QuestTitle = ?", OldTitle);
            db.update(TableName, AnswerValue,"QuestAnswer = ?",OldAnswer);
            db.update(TableName, HintValue,"QuestHint = ?",OldTitleHint);
            db.update(TableName, LevelValue,"QuestTitle = ?",OldTitle);//update Quest which have Certain Title 's Level data.
            //3. finish change, report to user.
            SupportLib.CreateNoticeDialog(this,
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
            SupportLib.CreateNoticeDialog(this,
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
                    //2.preparation.
                    QuestDbHelper QuestDataBase = new QuestDbHelper(this);
                    SQLiteDatabase db = QuestDataBase.getWritableDatabase();
                    //3.delete Quest which have certain Title.
                    db.delete(QuestDbHelper.TABLE_NAME,"QuestTitle = ?", OldTitle);
                    //3.1 after deleted, total number of Quest should re-calculate.
                    QuestTotalNumber = QueryCursor(null,null);//it might delete multi Quest(with same Title), so we can not only minus 1.
                    SupportLib.saveIntData(this,"IdNumberStoreProfile","IdNumber", QuestTotalNumber);
                    //3.2 after deleted, change the behavior of Editor.
                    if(QuestTotalNumber <= 0){
                        ControlEditor(true);
                        finish();//make sure data correct.
                    }else{
                        LoadDataFromCursor();//refresh Editor (load another Quest).
                    }
                    if(TitleText.equals("") || AnswerText.equals("")){
                        SupportLib.CreateNoticeDialog(this,
                                getString(R.string.NoticeWordTran),
                                "You have removed all not full Quest in database.",
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
                    QuestDbHelper QuestDataBase = new QuestDbHelper(EditorActivity.this);
                    SQLiteDatabase db = QuestDataBase.getWritableDatabase();
                    QuestDataBase.DeleteEntire(db);
                    ControlEditor(true);
                    QuestTotalNumber = 0;
                    SupportLib.saveIntData(this,"IdNumberStoreProfile","IdNumber", QuestTotalNumber);
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
    //thanks to: https://stackoverflow.com/questions/10244222/android-database-cursorindexoutofboundsexception-index-0-requested-with-a-size !
    @SuppressLint({"SetTextI18n", "Range"})
    private void LoadDataFromCursor(){
        EditText EditTitleView = findViewById(R.id.EditTitleView);
        EditText EditAnswerView = findViewById(R.id.EditAnswerView);
        EditText EditHintView = findViewById(R.id.EditHintView);
        TextView TitleLevelView = findViewById(R.id.TitleLevelView);
        //if first row is not null, instead of using Cursor.moveToFirst() method, this method will not move Cursor actually.
        if(ResultCursor != null && ResultCursor.getCount() > 0){
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
        }
        RefreshResultNumber();
    }

    private int QueryCursor(String Selection, String[] SelectionArgs){
        QuestDbHelper QuestDataBase = new QuestDbHelper(this);
        SQLiteDatabase db = QuestDataBase.getReadableDatabase();
        //do search according SearchMode.
        if(SearchMode == 0 || SearchMode == 4){
            //All or Level SearchMode, these mode don't need blur searching.
            ResultCursor = db.query(QuestDbHelper.TABLE_NAME, null, Selection, SelectionArgs, null, null, null);
        }else{
            //not All search mode, use native sqlite to do blur search.
            //return all lines which content including SelectionArgs text, no matter the text position in content.
            //sqlite grammar: https://www.runoob.com/sqlite/sqlite-like-clause.html and https://blog.csdn.net/fantianheyey/article/details/9199235 !
            /*
            manual:
            1. Selection.replace(), because of Selection input is like "XX = ?",
            according the sqlite grammar, this parameter must be Column name itself, can't contain another char, so we need to delete " = ?" before query.
            2.SelectionArgs[0], we need to add "%" in the begin and end of Arg, to use sqlite blur search function.
            and because of this parameter only have ONE element, so just call "%" + XX[0] + "%".
             */
            ResultCursor = db.rawQuery(
                    "SELECT * FROM " + QuestDbHelper.TABLE_NAME + " WHERE " + Selection.replace(" = ?","") + " LIKE " + "'%" + SelectionArgs[0] + "%'",
                    null
                    );
        }
        //after search, return the Cursor length to method to tell user how many result founded.
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
        int LineNumber = ResultCursor.getCount();
        if(LineNumber > 0){
            CurrentQuestNumberView.setText( (ResultCursor.getPosition() + 1 ) + " / " + LineNumber);
        }else{
            CurrentQuestNumberView.setText(0 + " / " + 0);
        }
    }

    //Spinner and ArrayAdapter, thanks to:
    // https://www.jianshu.com/p/3b2da5604c40 !
    // https://developer.android.google.cn/guide/topics/ui/controls/spinner?hl=zh-cn !
    // https://blog.csdn.net/gengkui9897/article/details/89233115 !
    private void LoadModeSpinner(){
        Spinner SearchModeSpinner = findViewById(R.id.SearchModeSpinner);
        //1.Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{
                        getString(R.string.AllModeWordTran),
                        getString(R.string.SetQuestTitleTran),
                        getString(R.string.AnswerShowTitleTran),
                        getString(R.string.HintWordTran),
                        getString(R.string.QuestLevelWordTran)
                }
        );
        //1.1 Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //1.2 Apply the adapter to the spinner
        SearchModeSpinner.setAdapter(adapter);
        //1.3 set the default value for Spinner, to prevent from NPE.
        SearchModeSpinner.setSelection(0,true);
        //1.4 set Listener to Spinner when the item in it is clicked, to record which mode user selected.
        SearchModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SearchMode = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                SearchMode = 0;//default value.
            }
        });
    }

    //protection function.

    /**
     * You can use this method to easily control UI of Editor, to help user not operate empty or not existed data.<br/>
     * @param CloseFunction if true, tell user [Editor find some result, ready to work.]<br/>
     *                      if false, tell user [Editor can't find any result available, and close the UI.]
     */
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
    //end of basic support system.
}