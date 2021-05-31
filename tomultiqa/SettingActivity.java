package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;


public class SettingActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //make StatusBar is same color as ActionBar in Activity.
        //thanks to: https://blog.csdn.net/kiven9609/article/details/73162307 !
        // https://www.color-hex.com/color/26c6da ! #26C6DA is our App Primary Color.
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.rgb(38,198,218));
        //底部导航栏
        //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
        //main method below.
        CloseAllLayout();//get total number of id in database according records in SharedPreference.
        getIdNumberInfo();

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

        //Loading AppMode from SharedPreference.
        InitializingAppMode();
    }

    //Quest Making system.
    int QuestTotalNumber = 0;
    int QuestLevel = 1;
    //main method, provide support for store id number.
    //Thanks to:https://www.jianshu.com/p/59b266c644f3
    public void StoreQuest(View view){
        ContentValues values = new ContentValues();
        EditText TypeQuestTitle = findViewById(R.id.TypeQuestTitle);
        EditText TypeQuestAnswer = findViewById(R.id.TypeQuestAnswer);
        SeekBar ChooseQuestLevelBar = findViewById(R.id.ChooseQuestLevelBar);
        QuestDataBaseBasic QuestDataBase = new QuestDataBaseBasic(this);
        SQLiteDatabase db = QuestDataBase.getWritableDatabase();
        if(!TypeQuestTitle.getText().toString().equals("") || !TypeQuestAnswer.getText().toString().equals("")){
                //prepare the data collection need to be put into database.
                values.put(QuestDataBaseBasic.COLUMN_NAME_QuestTitle,TypeQuestTitle.getText().toString());
                values.put(QuestDataBaseBasic.COLUMN_NAME_QuestAnswer,TypeQuestAnswer.getText().toString());
                values.put(QuestDataBaseBasic.COLUMN_NAME_QuestType,getString(R.string.QuestQuestionTypeTran));
                values.put(QuestDataBaseBasic.COLUMN_NAME_QuestLevel,QuestLevel);
                values.put(QuestDataBaseBasic.COLUMN_NAME_QuestHint,"Nothing");

                //initializing the database. And put the data collection in it.
                long newRowId = db.insert(QuestDataBaseBasic.TABLE_NAME, null, values);
            //reset the user interface.
            TypeQuestTitle.setText("");
            TypeQuestAnswer.setText("");
            ChooseQuestLevelBar.setProgress(0);

            //save current total number of id,and show it to user by this sub method.
            SavedShowIdNumber();
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
        ShowQuestNumber.setText(IdNumber + "");
    }

    @SuppressLint("SetTextI18n")
    private void getIdNumberInfo(){
        SharedPreferences IdNumberInfo = getSharedPreferences("IdNumberStoreProfile", MODE_PRIVATE);
        TextView ShowQuestNumber = findViewById(R.id.ShowQuestNumber);
        QuestTotalNumber = IdNumberInfo.getInt("IdNumber", 0);
        ShowQuestNumber.setText(QuestTotalNumber + "");
    }

    private void CalculateQuestLevel(){

    }//end of Quest Making system.


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

        //1.close all Function Layout before management, to do it easily.
        CloseAllLayout();

        //2.confirm that which button has been touched, and return it`s Text to use in next step as recognizing identifier.
        //thanks to: https://stackoverflow.com/questions/3412180/how-to-determine-which-button-pressed-on-android !
        Button AnyButtonView = findViewById(view.getId());
        String ButtonText = AnyButtonView.getText().toString();

        //3.open certain Layout. and record which layout have been opened.
        if(ButtonText.equals(getString(R.string.AppModeWordTran))){
            ModeChooseLayout.setVisibility(View.VISIBLE);
        }else if(ButtonText.equals(getString(R.string.AboutQuestTitleTran))){
            QuestSettingLayout.setVisibility(View.VISIBLE);
        }

        //4.show return button to user.And close choose Function layout.
        ReturnSettingButton.setText(getString(R.string.ReturnToWordTran)+ " " + ButtonText +" >");
        ReturnSettingButton.setVisibility(View.VISIBLE);
        ButtonLayout.setVisibility(View.GONE);
    }

    //sub method.
    private void CloseAllLayout(){
        Button ReturnSettingButton = findViewById(R.id.ReturnSettingButton);
        ConstraintLayout ModeChooseLayout = findViewById(R.id.ModeChooseLayout);
        ConstraintLayout QuestSettingLayout = findViewById(R.id.QuestSettingLayout);

        ReturnSettingButton.setVisibility(View.GONE);
        ModeChooseLayout.setVisibility(View.GONE);
        QuestSettingLayout.setVisibility(View.GONE);
    }//end of Layout Management system.


    //basic support system.
    public void GoToAssistActivity(View view){
        Intent i = new Intent(this,AssistActivity.class);
        startActivity(i);
    }

    public void GoToEditorActivity(View view){
        Intent i = new Intent(this,EditorActivity.class);
        startActivity(i);
    }

    public void GoToBackUpActivity(View view){
        Intent i = new Intent(this,BackUpActivity.class);
        startActivity(i);
    }
}