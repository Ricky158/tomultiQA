package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

        final EditText TimerUnitView = findViewById(R.id.TimerUnitView);
        TimerUnitView.setOnClickListener(new EditText.OnClickListener(){
            final String UnitText = TimerUnitView.getText().toString();
            @Override
            public void onClick(View v) {
                if(!UnitText.equals("")){
                    SupportClass.saveStringData(AssistActivity.this,"ChooseAppModeProfile","TimerUnit",UnitText);
                }else{
                    SupportClass.saveStringData(AssistActivity.this,"ChooseAppModeProfile","TimerUnit","Sol.");
                }
            }
        });
    }

    //About App function.
    public void ShowAboutAppText(View view){
        //You need to use Edit Text software to edit text, and paste here to keep format.
        SupportClass.CreateOnlyTextDialog(this,
                getString(R.string.AboutAppWordTran),
                "This part is only provided with Chinese.\n"+
                        "Make sure you have skill to read Chinese.\n"+
                        "\n" +//do not remove text above this line. these are language hint.
                        "【0.12.0版】2021.5.26\n" +
                        "【功能性更新】\n" +
                        "1.“等级突破”系统追加！\n" +
                        "1.1 自0.12.0版本起，用户的等级在达到一定程度后，EXP会累积而无法升级，需要通过等级突破的考验才能解锁升级权限。\n" +
                        "详情请您前往“广场”-“试炼场”-“等级突破”页面查看。\n" +
                        "2.App工作模式机制变更：\n" +
                        " 2.1在App开启“专注模式”后，“广场”功能的入口将会被改为“修身馆”的入口。（为保持内容排版，按钮文本不会变更）\n" +
                        " 2.2 现在，“普通模式”下也不再能够使用下列功能：“敌临境”“望晨崖”“炼金室”“比武台”。\n" +
                        "【内容更新】\n" +
                        "1.“剧情”阅读器更新。\n" +
                        " 1.1 新增“剧情类型”-- 轶事（Tale.）\n" +
                        "相较于主线和手记，轶事的文笔非常随意，长度也飘忽不定，希望能够大家提供一些不一样的娱乐。\n" +
                        " 1.2 以下“剧情类型”的名称变更：\n" +
                        "“主线剧情（MainStory）> 主线（Main.）”\n" +
                        "“开发者漫谈（DeveloperStory）>手记（Dev.）”\n" +
                        " 1.3 追加新的1篇“轶事”--《贵族和炼金术》\n" +
                        " 1.4 阅读器的字体大小下降。\n" +
                        " 1.5 阅读器UI微调。\n" +
                        "2.战斗系统更新：\n" +
                        " 2.1 追加“等级压制”机制，Boss每与用户相差1级，用户的输出总伤害变动3%（伤害可被扣至负数）。\n" +
                        "Boss等级较高，则受到伤害下降，反之则提高。\n" +
                        " 2.2 用户伤害值的计算改为全程浮点计算，只在结束计算时转化为整数扣除HP和显示。\n" +
                        "3.“纪录”“敌临境”页面追加部分翻译。\n" +
                        "4.追加新的“兑换码”，\n" +
                        " 4.1 “maxrank”：将等级突破及其附加属性升至满级。\n" +
                        " 4.2 “unlockvhquest”：将“敌临境”的“已通关层数”设为999（解锁所有存在的层数）。\n" +
                        " 4.3 附上早期版本已经追加的兑换码：\n" +
                        " 4.3.1“greedisgood”：使用户获得输入数量的积分。\n" +
                        " 4.3.2“whosyourdaddy”：使用户的攻击力追加输入数值（注：重复更改将会覆盖效果！）\n" +
                        " 4.3.3 “/xp”：给予用户输入数量的EXP。\n" +
                        "5.“图鉴”-“游戏公式”内容更新。\n" +
                        "6.“备份”系统新增可备份内容：\n" +
                        "等级上限，等级突破阶段，等级突破攻击加成，等级突破暴击加成，等级突破暴伤加成。这些内容被追加至“笔记文本”所在行数的下方。\n" +
                        "【内容调整】\n" +
                        "1.用户的初始等级上限下调至50级，更高的等级上限需要后续解锁。\n" +
                        "（注：最高等级上限仍为300级不变。）\n" +
                        "2.“广场”页面未开发完成的“任务（委托所）”入口关闭，避免误操作。\n" +
                        "3.在战斗时，主界面上的“EXP计数器”将会消失以节省屏幕空间。\n" +
                        "4.进入“广场”时，未开启游戏模式的提示文本更改。\n" +
                        "5.主页面-伤害显示功能显示的时长略微缩短。（550ms》500ms）\n" +
                        "6.出于平衡性目的，炼金要素“平衡”提供的持续回合由 3 / 每要素增加到 5 / 每要素。\n" +
                        "【开发相关】\n" +
                        "1.整理并抽象化了部分主页面的代码。\n" +
                        "2.精简了主页面导入App工作模式的代码。\n" +
                        "3.将App模式作为静态值定义在子类“ViewClass”当中。\n" +
                        "4.精简了“敌临境”用户通关层数导入时不必要的代码。\n" +
                        "【bug修复】\n" +
                        "1.修复了“阅读”进度条的初始位置异常的bug。\n" +
                        "2.修复了“成就”-不同种类的“普通成就”显示不一的bug。\n" +
                        "3.修复了进入战斗时，数据初始化时可能产生数据错误的bug。\n" +
                        "4.修复了“考验”“护盾”“腐蚀”等战斗开始时触发的能力会在每回合结束后触发的bug。\n" +
                        "5.修复了“敌临境”用户通关层数数值过高时，boss数据加载异常的bug。\n" +
                        "【已知问题】\n" +
                        "1.获得的素材量是正常的10倍（原因未知，不过不影响游戏，暂时无法修复）。\n" +
                        "2.若“笔记”文本行数多于1，则会引起“备份”系统无法工作的bug。\n" +
                        "【0.13.0版】2021.5.27~ 开发中\n" +
                        "【重要变更】\n" +
                        "1.App依赖库由android.support支持库迁移到androidX支持库。\n" +
                        "（这是导致本次更新带来App的体积明显上涨的主因。）\n" +
                        "【功能性更新】\n" +
                        "1.增加了“推荐难度”的功能，可以根据题目和答案的长度来给出App认为合理的难度值。（开发中）\n" +
                        "2.新增“背包”系统，用户可于此处管理自己在游戏进程当中获得的物品。（开发中）\n" +
                        "【内容更新】\n" +
                        "1.追加新的页面：“冒险”\n" +
                        "原“广场”页签下的战斗相关入口移动到本页面下。方便统一管理和使用。\n" +
                        "变更范围如下：\n" +
                        "“试炼场”“望晨崖”“敌临境”“比武台”\n" +
                        "【内容调整】\n" +
                        "1.主页面UI变更：\n" +
                        " 1.1 原“设置”按钮功能移动到App的标题栏的“齿轮”按钮上。\n" +
                        " 1.2 原“设置”按钮变为“冒险”页面按钮。\n" +
                        "2.微调了EXP计数器的UI。\n" +
                        "【开发相关】\n" +
                        "1.通过移除不必要的资源使App体积下降（当然，更新内容导致效果被抵消了）。\n" +
                        "【bug修复】\n" +
                        "1.修复了购买商品后，无法再次进行购买的bug。\n" +
                        "2.修复了特定情况下，达到等级上限后仍然能购买EXP的bug。 \n" +
                        "3.修复了在事先输入过购买数量的情况下，选择购买“成就”不会重置数量显示的bug。（实际效果无异常）\n" +
                        "4.修复了炼金等级效果在页面初始化时不显示的bug。（实际效果无异常）",
                getString(R.string.ConfirmWordTran),
                "Nothing",
                true);
    }//end of About App function.


    //Open App Official website on Github.
    //url: https://github.com/Ricky158/tomultiQA/releases !
    public void OpenAppWebsite(View view){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.NoticeWordTran));
        dialog.setMessage("You are going to App Official Download Page on Github\n"+
                "Do you confirm to Open a Website?\n" +
                "(Sometimes, the website may not accessible.)");
        dialog.setCancelable(true);
        dialog.setPositiveButton(
                getString(R.string.ConfirmWordTran),
                (dialog12, id) -> {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Ricky158/tomultiQA/releases"));
                    startActivity(browserIntent);
                    dialog12.cancel();
                });
        dialog.setNegativeButton(
                    getString(R.string.CancelWordTran),
                (dialog1, id) -> dialog1.cancel());
        //3. Use this object to create a actual View in android.
        AlertDialog DialogView = dialog.create();
        DialogView.show();
    }//end of App Official website function.


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
}