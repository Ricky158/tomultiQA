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
                        "【10.2.0】2021.4.28 必要更新\n" +
                        "【功能性更新】\n" +
                        "1.在兑换码使用完毕后，会弹出toast提示。\n" +
                        "2.现在，答题错误时，App会弹窗告知，并提供本题目的正确答案。（取消了旧版的toast提示）\n" +
                        "3.追加了显示题目的“题号”功能。\n" +
                        "4.追加了显示题目“答题用时”的功能。\n" +
                        "【内容更新】\n" +
                        "1.现在，购买商品积分不足时的弹窗“自动更改数量”的功能只会在可购买最大数量大于0时才会出现更改的选项，以减少用户的不必要操作。\n" +
                        "【内容调整】\n" +
                        "1.调整了主页面的UI细节。\n" +
                        "【优化相关】\n" +
                        "1.改进了天赋“连续升级”的实现逻辑，减少在积分不足时试图升级引起的卡顿。\n" +
                        "【bug修复】\n" +
                        "1.修复了“天赋连续升级”因积分不足提示弹窗过多导致卡死的bug。\n" +
                        "2.修复了“敌临境”可通过按钮选择，抵达未通关层数的bug。\n" +
                        "3.修复了无法查看“敌临境”层数通关奖励的bug。\n" +
                        "4.修复了可以重复获取“敌临境”通关奖励的bug。\n" +
                        "5.修复了无法获取“望晨崖（日常副本）”奖励的bug。\n" +
                        "6.修复了“附加功能”页面显示不全的bug。\n" +
                        "7.修复了“商贸居”底部显示效果不正确的bug。\n" +
                        "8.修复了“比武台”能力选择部分显示不正确的bug。\n" +
                        "9.删除了“比武台”界面多余的“Clear Ability”按钮。\n" +
                        "\n" +
                        "皆さん、お楽しみください！\n" +
                        "tomultiQA开发组全体成员：\n" +
                        "Armen，小织，爱未敬上",
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