package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

public class AssistActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assist);
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

        final EditText TimerUnitView = findViewById(R.id.TimerUnitView);
        TimerUnitView.setOnClickListener(new EditText.OnClickListener(){
            final String UnitText = TimerUnitView.getText().toString();
            @Override
            public void onClick(View v) {
                SharedPreferences A = getSharedPreferences("ChooseAppModeProfile", MODE_PRIVATE);
                SharedPreferences.Editor editor= A.edit();
                if(!UnitText.equals("")){
                    editor.putString("TimerUnit",UnitText);
                }else{
                    editor.putString("TimerUnit","Sol.");
                }
                editor.apply();
            }
        });
    }

    //About App function.
    public void ShowAboutAppText(View view){
        SupportClass.CreateOnlyTextDialog(this,
                getString(R.string.AboutAppWordTran),
                "This part is only provided with Chinese.\n"+
                        "Make sure you have skill to read Chinese.\n"+
                        "\n" +//do not remove text above this line. these are language hint.
                        "【0.10.4】 2021.5.1"+
                        "【功能性更新】\n" +
                        "1.“题库编辑”系统v1完成。\n" +
                        "1.1 支持遍历题库，导出和修改特定题目，清空数据库的操作。\n" +
                        "1.2 系统文本已完全翻译。"+
                        "【已知问题】\n" +
                        "1.App的部分文本存在汉英混合的问题。\n" +
                        "2.“设置”-“附加功能”-“简化弹窗”关闭伤害弹窗开关无效。",
                getString(R.string.ConfirmWordTran),
                "Nothing",
                true);
    }//end of About App function.


    //Assist initializing function.
    //the data are stored in TimerSettingProfile for history reason. Please pay attention!!
    private void InitializingAssistSwitch(){
        Switch AutoKeyboardSwitch = findViewById(R.id.AutoKeyboardSwitch);
        Switch DamageDialogSwitch = findViewById(R.id.DamageDialogSwitch);

        AutoKeyboardSwitch.setChecked(SupportClass.getBooleanData(this,"TimerSettingProfile","AutoKeyboard",false));
        DamageDialogSwitch.setChecked(SupportClass.getBooleanData(this,"TimerSettingProfile","StopDamageDialog",false));
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
        SharedPreferences TimerInfo = getSharedPreferences("TimerSettingProfile", MODE_PRIVATE);
        TimerTargetDay = TimerInfo.getInt("TimerTargetDay",30);
        TimerTargetDayView.setText(TimerTargetDay + "");
        TimerTargetBar.setProgress(TimerTargetDay);
        TimerUnitView.setText(TimerInfo.getString("TimerUnit","Sol."));
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
        }
        editor.apply();
    }//end of Switch Management system.

}