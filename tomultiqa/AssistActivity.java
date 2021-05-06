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
                        "【0.10.5版】2021.5.6\n" +
                        "【优化相关】\n" +
                        "1.删除了战斗结算阶段的多余代码。\n" +
                        "【bug修复】\n" +
                        "1.修复了题库编辑器在未对难度作修改时，保存将会使难度数值重置为1的bug。\n" +
                        "2.修复了题库编辑器可以将题目和答案保存为空值的bug。\n" +
                        "3.修复了题库编辑器在保存更改后不会重置显示文本的bug。\n" +
                        "4.修复了题库编辑器在未导入题目时仍可使用的bug。\n" +
                        "5.修复了题库编辑器“清空题库数据”的功能导致题库管理系统无法运行的bug。\n" +
                        "6.修复了“敌临境”可以传送至未曾通关的层数的bug。\n" +
                        "7.修复了无法记录“敌临境”通关层数，进而影响层数解锁的bug。\n" +
                        "8.修复了“比武台”统计项显示格式不正确的bug。\n" +
                        "9.修复了已经有boss存在时，仍有可能生成普通boss，导致战斗过程异常的bug。\n" +
                        "10.修复了天赋升级时，当暴击率天赋已达满级时，不会自动取消升级勾选的bug。\n" +
                        "11.修复了题号显示不正确的bug。\n",
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