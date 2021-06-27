package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AssistActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assist);
        //make StatusBar is same color as ActionBar in Activity.
        //thanks to: https://blog.csdn.net/kiven9609/article/details/73162307 !
        // https://www.color-hex.com/color/26c6da ! #26C6DA is our App Primary Color.
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.rgb(38,198,218));
        //底部导航栏
        //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
        //main method below.
        InitializingTimerData();
        InitializingAssistSwitch();

        final SeekBar TimerTargetBar = findViewById(R.id.TimerTargetBar);
        final TextView TimerTargetDayView = findViewById(R.id.TimerTargetDayView);
        TimerTargetBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TimerTargetDayView.setText(TimerTargetBar.getProgress() + 1 + "");
                SupportClass.saveIntData(AssistActivity.this,"TimerSettingProfile","TimerTargetDay",TimerTargetBar.getProgress() + 1);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                TimerTargetDayView.setText(TimerTargetBar.getProgress() + 1 + "");
                SupportClass.saveIntData(AssistActivity.this,"TimerSettingProfile","TimerTargetDay",TimerTargetBar.getProgress() + 1);
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                TimerTargetDayView.setText(TimerTargetBar.getProgress() + 1 + "");
                SupportClass.saveIntData(AssistActivity.this,"TimerSettingProfile","TimerTargetDay",TimerTargetBar.getProgress() + 1);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        EditText TimerUnitView = findViewById(R.id.TimerUnitView);
        String TimerUnitString = TimerUnitView.getText().toString();
        if(TimerUnitString.equals("")){//Prevent from Empty.
            TimerUnitString = "Sol.";
        }
        SupportClass.saveStringData(this,"TimerSettingProfile","TimerUnit",TimerUnitString);
    }

    //Assist initializing function.
    //the data are stored in TimerSettingProfile for history reason. Please pay attention!!
    private void InitializingAssistSwitch(){
        Switch AutoKeyboardSwitch = findViewById(R.id.AutoKeyboardSwitch);
        Switch DamageDialogSwitch = findViewById(R.id.DamageDialogSwitch);
        Switch AutoClearAnswerSwitch = findViewById(R.id.AutoClearAnswerSwitch);

        AutoKeyboardSwitch.setChecked(SupportClass.getBooleanData(this,"TimerSettingProfile","AutoKeyboard",false));
        DamageDialogSwitch.setChecked(SupportClass.getBooleanData(this,"TimerSettingProfile","StopDamageDialog",true));
        AutoClearAnswerSwitch.setChecked(SupportClass.getBooleanData(this,"TimerSettingProfile","AutoClearAnswer",false));
    }//end of Assist initializing function.


    //Timer system.
    int TimerTargetDay = 1;
    @SuppressLint("SetTextI18n")
    private void InitializingTimerData(){
        Switch TimerEnableSwitch = findViewById(R.id.TimerEnableSwitch);
        Switch TimerFortuneSwitch = findViewById(R.id.TimerFortuneSwitch);
        SeekBar TimerTargetBar = findViewById(R.id.TimerTargetBar);
        TextView TimerTargetDayView = findViewById(R.id.TimerTargetDayView);
        EditText TimerUnitView = findViewById(R.id.TimerUnitView);

        TimerEnableSwitch.setChecked(SupportClass.getBooleanData(this,"TimerSettingProfile","TimerEnabled",false));
        TimerFortuneSwitch.setChecked(SupportClass.getBooleanData(this,"TimerSettingProfile","TimerFortune",false));
        TimerTargetDay = SupportClass.getIntData(this,"TimerSettingProfile","TimerTargetDay",30);
        TimerTargetDayView.setText(TimerTargetDay + "");
        TimerTargetBar.setProgress(TimerTargetDay);
        TimerUnitView.setText(SupportClass.getStringData(this,"TimerSettingProfile","TimerUnit","Sol."));
    }//end of Timer system.


    //Switch Management system.
    public void ChangeSwitchState(View view){
        Switch AnySwitchView = findViewById(view.getId());
        boolean SwitchIsChecked = AnySwitchView.isChecked();
        String SwitchText = AnySwitchView.getText().toString();
        SharedPreferences TimerInfo = getSharedPreferences("TimerSettingProfile", MODE_PRIVATE);
        SharedPreferences.Editor editor= TimerInfo.edit();

        if(SwitchText.equals(getString(R.string.EnableWordTran))){
            editor.putBoolean("TimerEnabled",SwitchIsChecked);
        }else if(SwitchText.equals(getString(R.string.ShowFortuneWordTran))){
            editor.putBoolean("TimerFortune",SwitchIsChecked);
        }else if(SwitchText.equals(getString(R.string.AutoCloseTimerWordTran))){
            editor.putBoolean("TimerAutoClose",SwitchIsChecked);
        }else if(SwitchText.equals(getString(R.string.AutoKeyboardSwitchTran))){
            editor.putBoolean("AutoKeyboard",SwitchIsChecked);
        }else if(SwitchText.equals(getString(R.string.DamageDialogSwitchTran))){
            editor.putBoolean("StopDamageDialog",SwitchIsChecked);
        }else if(SwitchText.equals(getString(R.string.AutoClearAnswerTran))){
            editor.putBoolean("AutoClearAnswer",SwitchIsChecked);
        }
        editor.apply();
    }//end of Switch Management system.


    //Show Assist Function Help.
    public void ShowAssistHelp(View view){
        SupportClass.CreateNoticeDialog(this,
                getString(R.string.HelpWordTran),
                ValueClass.ASSIST_FUNCTION_HELP,
                getString(R.string.ConfirmWordTran));
    }
}