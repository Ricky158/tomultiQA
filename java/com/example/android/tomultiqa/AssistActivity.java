package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class AssistActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assist);
        //thanks to: https://dev.mi.com/console/doc/detail?pId=2229 !
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        InitializingTimerData();
        InitializingAssistSwitch();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EditText TimerUnitView = findViewById(R.id.TimerUnitView);
        EditText TimerTargetDayView = findViewById(R.id.TimerTargetDayView);
        String TimerUnitString = TimerUnitView.getText().toString();
        //save Timer Unit.
        if(TimerUnitString.equals("") || TimerUnitString.length() > 6){//Prevent from Empty.
            TimerUnitString = "Sol.";
        }
        SupportLib.saveStringData(this,"TimerSettingProfile","TimerUnit",TimerUnitString);
        //save Timer Target Day.
        int TimerTargetDay = SupportLib.getInputNumber(TimerTargetDayView);
        if(TimerTargetDay <= 0 || TimerTargetDay > 3660){
            TimerTargetDay = 30;
        }
        SupportLib.saveIntData(this,"TimerSettingProfile","TimerTargetDay",TimerTargetDay);
    }

    //Assist initializing function.
    //the data are stored in TimerSettingProfile for history reason. Please pay attention!!
    private void InitializingAssistSwitch(){
        Switch AutoKeyboardSwitch = findViewById(R.id.AutoKeyboardSwitch);
        Switch AutoClearAnswerSwitch = findViewById(R.id.AutoClearAnswerSwitch);
        Switch DamageDialogSwitch = findViewById(R.id.DamageDialogSwitch);
        Switch PopWrongWindowSwitch = findViewById(R.id.PopWrongWindowSwitch);
        RadioButton AutoCaseOff = findViewById(R.id.AutoCaseOff);
        RadioButton AutoCaseCaps = findViewById(R.id.AutoCaseCaps);
        RadioButton AutoCaseLows = findViewById(R.id.AutoCaseLows);

        boolean[] SwitchData = SupportLib.getMultiBoolean(
                this,
                "TimerSettingProfile",
                new String[]{"AutoKeyboard","AutoClearAnswer","StopDamageDialog","AutoWrongWindow"},
                new boolean[]{false,false,true,true}
        );
        AutoKeyboardSwitch.setChecked(SwitchData[0]);
        AutoClearAnswerSwitch.setChecked(SwitchData[1]);
        DamageDialogSwitch.setChecked(SwitchData[2]);
        PopWrongWindowSwitch.setChecked(SwitchData[3]);
        int AutoCaseState = SupportLib.getIntData(this,"TimerSettingProfile","AutoCase",-1);
        if(AutoCaseState == 1){
            AutoCaseCaps.setChecked(true);
        }else if(AutoCaseState == 0){
            AutoCaseLows.setChecked(true);
        }else{
            AutoCaseOff.setChecked(true);
        }
    }//end of Assist initializing function.


    //Timer system.
    @SuppressLint("SetTextI18n")
    private void InitializingTimerData(){
        Switch TimerEnableSwitch = findViewById(R.id.TimerEnableSwitch);
        Switch TimerFortuneSwitch = findViewById(R.id.TimerFortuneSwitch);
        EditText TimerTargetDayView = findViewById(R.id.TimerTargetDayView);
        EditText TimerUnitView = findViewById(R.id.TimerUnitView);
        boolean[] TimeBooleans = SupportLib.getMultiBoolean(
                this,
                "TimerSettingProfile",
                new String[]{"TimerEnabled","TimerFortune"},
                new boolean[]{false,false}
        );
        TimerEnableSwitch.setChecked(TimeBooleans[0]);
        TimerFortuneSwitch.setChecked(TimeBooleans[1]);
        TimerTargetDayView.setText(SupportLib.getIntData(this,"TimerSettingProfile","TimerTargetDay",30) + "");
        TimerUnitView.setText(SupportLib.getStringData(this,"TimerSettingProfile","TimerUnit","Sol."));
    }//end of Timer system.


    //Switch Management system.
    public void ChangeSwitchState(View view){
        Switch AnySwitch = findViewById(view.getId());
        boolean SwitchIsChecked = AnySwitch.isChecked();
        String SwitchText = AnySwitch.getText().toString();
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
        }else if(SwitchText.equals(getString(R.string.AutoWrongWindowTran))){
            editor.putBoolean("AutoWrongWindow",SwitchIsChecked);
        }
        editor.apply();
    }//end of Switch Management system.


    //RadioGroup Management system.
    public void ChangeRadioState(View view){
        RadioButton AnyRadioButton = findViewById(view.getId());
        String ButtonText = AnyRadioButton.getText().toString();
        SharedPreferences TimerInfo = getSharedPreferences("TimerSettingProfile", MODE_PRIVATE);
        SharedPreferences.Editor editor= TimerInfo.edit();

        if(ButtonText.equals(getString(R.string.OffWordTran))){
            editor.putInt("AutoCase",-1);
        }else if(ButtonText.equals(getString(R.string.AllLowsTran))){
            editor.putInt("AutoCase",0);
        }else if(ButtonText.equals(getString(R.string.AllCapsTran))){
            editor.putInt("AutoCase",1);
        }
        editor.apply();
    }


    //Show Assist Function Help.
    public void ShowAssistHelp(View view){
        SupportLib.CreateNoticeDialog(this,
                getString(R.string.HelpWordTran),
                ValueLib.ASSIST_FUNCTION_HELP,
                getString(R.string.ConfirmWordTran));
    }
}