package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class SettingActivity extends AppCompatActivity {

    @SuppressLint({"UseSwitchCompatOrMaterialCode", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //thanks to: https://dev.mi.com/console/doc/detail?pId=2229 !
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        CloseAllLayout();//get total number of id in database according records in SharedPreference.

        //provide visualize support for seekBar moving report.
        //Thanks to:https://stackoverflow.com/questions/41774963/android-seekbar-show-progress-value-along-the-seekbar
        final SeekBar ChooseQuestLevelBar = findViewById(R.id.ChooseQuestLevelBar);
        final TextView ShowQuestLevelView = findViewById(R.id.ShowQuestLevelView);
        ChooseQuestLevelBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                QuestLevel = ChooseQuestLevelBar.getProgress() + 1;
                ShowQuestLevelView.setText(QuestLevel + "");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                QuestLevel = ChooseQuestLevelBar.getProgress() + 1;
                ShowQuestLevelView.setText(QuestLevel + "");
            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                QuestLevel = ChooseQuestLevelBar.getProgress() + 1;
                ShowQuestLevelView.setText(QuestLevel + "");
            }
        });

        InitializingAutoQuestLevel();//DO NOT MOVE POSITION.
        TypeQuestTitle = findViewById(R.id.TypeQuestTitle);
        TypeQuestTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { CalculateQuestLevel(); }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { CalculateQuestLevel(); }

            @Override
            public void afterTextChanged(Editable s) {
                CalculateQuestLevel();
            }
        });
        TypeQuestAnswer = findViewById(R.id.TypeQuestAnswer);
        TypeQuestAnswer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { CalculateQuestLevel(); }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { CalculateQuestLevel(); }

            @Override
            public void afterTextChanged(Editable s) {
                CalculateQuestLevel();
            }
        });

        //Loading Experiment Switch from SharedPreference.
        ImportExperiment();
    }



    //override return button on global system.
    //listen return button, thanks to: https://www.cnblogs.com/HDK2016/p/8695052.html !
    //method support: https://blog.csdn.net/ccpat/article/details/45176665 !
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){//if user touch return button, and the key is pressed.
            //0.preparation: listing all Function Layout in this activity.
            ScrollView SettingButtonList = findViewById(R.id.SettingButtonList);
            if(SettingButtonList.getVisibility() == View.GONE){
                //1.Close all Layout.
                CloseAllLayout();
                //2. showing ButtonList and close ReturnButton showing to provide user Functions which can be chosen.
                SettingButtonList.setVisibility(View.VISIBLE);
                return false;//stop this time of return action of Activity.
            }else{
                Intent i = new Intent(this,MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                return true;//continue return action.
            }
        }
        return super.onKeyDown(keyCode, event);//continue execute system's return process.
    }//end of override.

    @Override
    protected void onResume() {
        super.onResume();
        getIdNumberInfo();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //save data when Activity is onPause().
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch AutoQuestLevelSwitch = findViewById(R.id.AutoQuestLevelSwitch);
        SupportLib.saveBooleanData(this,"TimerSettingProfile","AutoQuestLevel",AutoQuestLevelSwitch.isChecked());
    }


    //Quest Making system.
    EditText TypeQuestTitle;
    EditText TypeQuestAnswer;
    int QuestTotalNumber = 0;
    int QuestLevel = 1;
    //main method, provide support for store id number.
    //Thanks to:https://www.jianshu.com/p/59b266c644f3
    public void StoreQuest(View view){
        ContentValues values = new ContentValues();
        EditText TypeQuestHint = findViewById(R.id.TypeQuestHint);
        QuestDbHelper QuestDataBase = new QuestDbHelper(this);
        SQLiteDatabase db = QuestDataBase.getWritableDatabase();
        //1.content empty check.
        String TitleText = TypeQuestTitle.getText().toString();
        String AnswerText = TypeQuestAnswer.getText().toString();
        String HintText = TypeQuestHint.getText().toString();
        if(HintText.equals("")){
            HintText = "Nothing";//default value, DO NOT CHANGE!
        }
        //2. store data to db.
        if(!TitleText.isEmpty() && !AnswerText.isEmpty() && QuestTotalNumber < ValueLib.QUEST_MAX_NUMBER){
            //prepare the data collection need to be put into database.
            values.put(QuestDbHelper.DataBaseEntry.COLUMN_NAME_QuestTitle,TitleText);
            values.put(QuestDbHelper.DataBaseEntry.COLUMN_NAME_QuestAnswer,AnswerText);
            values.put(QuestDbHelper.DataBaseEntry.COLUMN_NAME_QuestType,getString(R.string.QuestQuestionTypeTran));
            values.put(QuestDbHelper.DataBaseEntry.COLUMN_NAME_QuestLevel,QuestLevel);
            values.put(QuestDbHelper.DataBaseEntry.COLUMN_NAME_QuestHint,HintText);
            //initializing the database. And put the data collection in it.
            db.insert(QuestDbHelper.TABLE_NAME, null, values);
            //reset the user interface.
            ClearQuestInput(null);

            //save current total number of id,and show it to user by this sub method.
            SavedShowIdNumber();
        }else if(QuestTotalNumber > ValueLib.QUEST_MAX_NUMBER){
            SupportLib.CreateNoticeDialog(this,
                    getString(R.string.ErrorWordTran),
                    "The max Limit of Quest number is 5,000. You can't add more Quest than this number.",
                    getString(R.string.ConfirmWordTran));
        }else{
            SupportLib.CreateNoticeDialog(this,
                    getString(R.string.ErrorWordTran),
                    "You can't create a Quest with empty Title or Answer.",
                    getString(R.string.ConfirmWordTran));
        }
    }

    //method of StoreQuest.
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
        QuestTotalNumber = IdNumber;
        ShowQuestNumber.setText(IdNumber + "");
    }

    @SuppressLint("SetTextI18n")
    private void getIdNumberInfo(){
        SharedPreferences IdNumberInfo = getSharedPreferences("IdNumberStoreProfile", MODE_PRIVATE);
        TextView ShowQuestNumber = findViewById(R.id.ShowQuestNumber);
        QuestTotalNumber = IdNumberInfo.getInt("IdNumber", 0);
        ShowQuestNumber.setText(QuestTotalNumber + "");
    }

    public void ClearQuestInput(View view){
        EditText TypeQuestHint = findViewById(R.id.TypeQuestHint);
        SeekBar ChooseQuestLevelBar = findViewById(R.id.ChooseQuestLevelBar);
        TypeQuestTitle.setText("");
        TypeQuestAnswer.setText("");
        TypeQuestHint.setText("");
        ChooseQuestLevelBar.setProgress(0);
    }

    //Auto Quest Level Function.
    //lv.1 method, main method.
    @SuppressLint("SetTextI18n")
    private void CalculateQuestLevel(){
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch AutoQuestLevelSwitch = findViewById(R.id.AutoQuestLevelSwitch);
        if(AutoQuestLevelSwitch.isChecked()){
            TextView ShowQuestLevelView = findViewById(R.id.ShowQuestLevelView);
            SeekBar ChooseQuestLevelBar = findViewById(R.id.ChooseQuestLevelBar);
            //1.get and calculate Quest Level from Title and Answer String Length.
            int TotalLength = TypeQuestTitle.length() + TypeQuestAnswer.length();
            double LengthValue = Math.pow(TotalLength,0.57);//x^0.57.
            //2.change the actual value of QuestLevel.
            if(LengthValue <= 10 && LengthValue >= 1){
                QuestLevel = (int) LengthValue;
            }else if(LengthValue > 10){
                QuestLevel = 10;
            }else{
                QuestLevel = 1;
            }
            //3.Show result to user.
            ShowQuestLevelView.setText(QuestLevel + "");
            ChooseQuestLevelBar.setProgress(QuestLevel - 1);
        }
        //record in this time, if the Quest is null or empty.

    }

    //lv.1 method, sub method of CalculateQuestLevel() method.
    private void InitializingAutoQuestLevel(){
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch AutoQuestLevelSwitch = findViewById(R.id.AutoQuestLevelSwitch);
        AutoQuestLevelSwitch.setChecked(SupportLib.getBooleanData(this,"TimerSettingProfile","AutoQuestLevel",false));
    }//end of Auto Quest Level Function.
    //end of Quest Making system.


    //AppMode Menu system.
    //thanks to: https://developer.android.google.cn/guide/topics/ui/dialogs !
    public void ShowModeMenu(View view){
        //0.preparation.
        final String[] AppMode = {SupportLib.getStringData(this, "ChooseAppModeProfile", "AppMode", ValueLib.NORMAL_MODE)};
        //1.Mode List preparation.
        CharSequence[] ModeItemList = {ValueLib.NORMAL_MODE, ValueLib.FOCUS_MODE, ValueLib.GAME_MODE};
        int SelectedItemId;
        if(AppMode[0].equals(ValueLib.NORMAL_MODE)){
            SelectedItemId = 0;
        }else if(AppMode[0].equals(ValueLib.FOCUS_MODE)){
            SelectedItemId = 1;
        }else{
            SelectedItemId = 2;//Game Mode.
        }
        //2.set single choice dialog to user.
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.AppModeWordTran));
        dialog.setSingleChoiceItems(ModeItemList, SelectedItemId, (dialog12, which) -> {
            switch (which)
            {
                case -1:
                    SupportLib.CreateNoticeDialog(this,
                            getString(R.string.ErrorWordTran),
                            "NO Mode Selected, please Select a Mode or Cancel the dialog to keep the original option.",
                            getString(R.string.ConfirmWordTran));
                    break;
                case 0:
                    AppMode[0] = ValueLib.NORMAL_MODE;
                    break;
                case 1:
                    AppMode[0] = ValueLib.FOCUS_MODE;
                    break;
                case 2:
                    AppMode[0] = ValueLib.GAME_MODE;
                    break;
            }
            //3.save AppMode changed.
            SupportLib.saveStringData(this,"ChooseAppModeProfile","AppMode", AppMode[0]);
        });
        dialog.setCancelable(true);
        dialog.setNegativeButton(getString(R.string.HelpWordTran), (dialogInterface, i) -> SupportLib.CreateNoticeDialog(
                this,
                getString(R.string.HelpWordTran),
                getString(R.string.AppModeHelpTran),
                getString(R.string.ConfirmWordTran))
        );
        dialog.setPositiveButton(
                getString(R.string.CloseWordTran),
                (dialog1, id) -> dialog1.cancel());
        AlertDialog DialogView = dialog.create();
        DialogView.show();
    }//end of AppMode Menu system.


    //Experiment Function.
    // TODO: 2021/6/12 if Item system be Release, remove these code.

    private void ImportExperiment(){
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch EXPItemSwitch = findViewById(R.id.EXPItemSwitch);
        EXPItemSwitch.setChecked(SupportLib.getBooleanData(this,"EXPFuncFile","ItemSystemEnable",false));
    }
    public void ChangeItemEXPState(View view){
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch EXPItemSwitch = findViewById(R.id.EXPItemSwitch);
        SupportLib.saveBooleanData(this,"EXPFuncFile","ItemSystemEnable",EXPItemSwitch.isChecked());
    }

    public void ShowExperimentHelp(View view){
        SupportLib.CreateNoticeDialog(
                this,
                getString(R.string.HelpWordTran),
                ValueLib.EXPERIMENT_HELP,
                getString(R.string.ConfirmWordTran));
    }//end of Experiment Function.


    //layout management system.
    //main method.
    @SuppressLint("SetTextI18n")
    public void OpenFunctionLayout(View view){
        ScrollView SettingButtonList = findViewById(R.id.SettingButtonList);
        ConstraintLayout QuestSettingLayout = findViewById(R.id.QuestSettingLayout);
        ConstraintLayout ExperimentLayout = findViewById(R.id.ExperimentLayout);

        //1.close all Function Layout before management, to do it easily.
        CloseAllLayout();

        //2.confirm that which button has been touched, and return it`s Text to use in next step as recognizing identifier.
        //thanks to: https://stackoverflow.com/questions/3412180/how-to-determine-which-button-pressed-on-android !
        Button AnyButtonView = findViewById(view.getId());
        String ButtonText = AnyButtonView.getText().toString();

        //3.open certain Layout. and record which layout have been opened.
        if(ButtonText.equals(getString(R.string.AboutQuestTitleTran))){
            QuestSettingLayout.setVisibility(View.VISIBLE);
        }else if(ButtonText.equals(getString(R.string.EXPFunctionTran))){
            ExperimentLayout.setVisibility(View.VISIBLE);
        }

        //4.show return button to user.And close choose Function layout.
        SettingButtonList.setVisibility(View.GONE);
    }

    //sub method.
    private void CloseAllLayout(){
        ConstraintLayout QuestSettingLayout = findViewById(R.id.QuestSettingLayout);
        ConstraintLayout ExperimentLayout = findViewById(R.id.ExperimentLayout);

        QuestSettingLayout.setVisibility(View.GONE);
        ExperimentLayout.setVisibility(View.GONE);
    }//end of Layout Management system.


    //basic support system.
    public void GoToAssistActivity(View view){
        Intent i = new Intent(this,AssistActivity.class);
        startActivity(i);
    }

    public void GoToEditorActivity(View view){
        if(QuestTotalNumber > 0){
            Intent i = new Intent(this,EditorActivity.class);
            startActivity(i);
        }else{
            SupportLib.CreateNoticeDialog(
                    this,
                    getString(R.string.ErrorWordTran),
                    getString(R.string.NoQuestToEditHintTran),
                    getString(R.string.ConfirmWordTran)
            );
        }
    }

    public void GoToBackUpActivity(View view){
        Intent i = new Intent(this,BackUpActivity.class);
        startActivity(i);
    }

    public void GoToAboutAppActivity(View view){
        Intent i = new Intent(this,AboutAppActivity.class);
        startActivity(i);
    }
}