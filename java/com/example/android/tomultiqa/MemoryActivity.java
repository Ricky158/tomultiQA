package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

public class MemoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);
        CloseAllLayout();
        SetLanguageHint();
    }


    //hint function.
    private void SetLanguageHint(){
        String Country = Locale.getDefault().getCountry();
        if(!Country.equals("CN")){
            //1.using android api to create a dialog object.
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            //2.set basic values of dialog, including content text,button text,and title.
            dialog.setTitle("Hint");
            dialog.setMessage("Some of Text in this Page has not translated yet,\n" +
                    "you need to have the language skill to read Chinese.");
            dialog.setCancelable(true);
            dialog.setPositiveButton(
                    //set left button`s text.
                    getString(R.string.ConfirmWordTran),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            //3. Use this object to create a actual View in android.
            AlertDialog DialogView = dialog.create();
            DialogView.show();
        }
    }//end of hint function.


    //Layout Management system.
    String CurrentShowingLayout = "Null";

    //closing all function layout.
    private void CloseAllLayout(){
        Button ReturnLayoutButton = findViewById(R.id.ReturnLayoutButton);
        ConstraintLayout RecordLayout = findViewById(R.id.RecordLayout);
        ConstraintLayout HandBookLayout = findViewById(R.id.HandBookLayout);
        ConstraintLayout PlayerDataLayout = findViewById(R.id.PlayerDataLayout);

        //1.close all Function Layout before management, to do it easily.
        ReturnLayoutButton.setVisibility(View.GONE);
        RecordLayout.setVisibility(View.GONE);
        HandBookLayout.setVisibility(View.GONE);
        PlayerDataLayout.setVisibility(View.GONE);
    }

    @SuppressLint("SetTextI18n")
    public void OpenFunctionLayout(View view){
        //0.preparation: listing all Function Layout in this activity.
        Button ReturnLayoutButton = findViewById(R.id.ReturnLayoutButton);
        LinearLayout ButtonLayout = findViewById(R.id.ButtonLayout);
        ConstraintLayout RecordLayout = findViewById(R.id.RecordLayout);
        ConstraintLayout HandBookLayout = findViewById(R.id.HandBookLayout);
        ConstraintLayout PlayerDataLayout = findViewById(R.id.PlayerDataLayout);

        //1.close all Function Layout before management, to do it easily.
        CloseAllLayout();

        //2.confirm that which button has been touched, and return it`s Text to use in next step as recognizing identifier.
        //thanks to: https://stackoverflow.com/questions/3412180/how-to-determine-which-button-pressed-on-android !
        Button AnyButtonView = findViewById(view.getId());
        String ButtonText = AnyButtonView.getText().toString();

        //3.open certain Layout. and record which layout have been opened.
        if(ButtonText.equals(getString(R.string.RecordWordTran))){
            LoadRecordData();
            RecordLayout.setVisibility(View.VISIBLE);
        }else if(ButtonText.equals(getString(R.string.HandBookWordTran))){
            LoadAbilityText();
            HandBookLayout.setVisibility(View.VISIBLE);
        }else if(ButtonText.equals(getString(R.string.PlayerDataWordTran))){
            LoadPlayerData();
            PlayerDataLayout.setVisibility(View.VISIBLE);
        }
        CurrentShowingLayout = ButtonText;

        //4.show return button to user.And close choose Function layout.
        ReturnLayoutButton.setText(getString(R.string.ReturnToWordTran)+ CurrentShowingLayout +" >");
        ReturnLayoutButton.setVisibility(View.VISIBLE);
        ButtonLayout.setVisibility(View.GONE);
    }

    public void CloseFunctionLayout(View view){
        //0.preparation: listing all Function Layout in this activity.
        Button ReturnLayoutButton = findViewById(R.id.ReturnLayoutButton);
        LinearLayout ButtonLayout = findViewById(R.id.ButtonLayout);
//        ConstraintLayout RecordLayout = findViewById(R.id.RecordLayout);
//        ConstraintLayout HandBookLayout = findViewById(R.id.HandBookLayout);
//        ConstraintLayout PlayerDataLayout = findViewById(R.id.PlayerDataLayout);

        //1.Close certain Layout.
//        switch (CurrentShowingLayout) {
//            case "Record":
//                RecordLayout.setVisibility(View.GONE);
//                break;
//            case "Achievement":
//                //......
//                break;
//            case "HandBook":
//                HandBookLayout.setVisibility(View.GONE);
//                break;
//            case "PlayerData":
//                PlayerDataLayout.setVisibility(View.GONE);
//                break;
//        }
        CloseAllLayout();

        //2. showing ButtonLayout and close ReturnButton showing to provide user Functions which can be chosen.
        ButtonLayout.setVisibility(View.VISIBLE);
        ReturnLayoutButton.setVisibility(View.GONE);
    }//end of Layout Management system.


    //Record system.
    //sub method, using in loading data from SharedPreference.
    @SuppressLint("SetTextI18n")
    private void LoadRecordData(){
        //0.preparation.
        TextView UserLevelCount = findViewById(R.id.UserLevelCount);
        TextView ComboCount = findViewById(R.id.ComboCount);
        TextView DailyDifficultyCount = findViewById(R.id.DailyDifficultyCount);
        TextView EXPGottenCount = findViewById(R.id.EXPGottenCount);
        TextView PointGottenCount = findViewById(R.id.PointGottenCount);
        TextView MaterialGottenCount = findViewById(R.id.MaterialGottenCount);
        TextView DefeatBossCount = findViewById(R.id.DefeatBossCount);
        TextView PassedConflictCount =findViewById(R.id.PassedConflictCount);
        TextView UserAnsweringCount = findViewById(R.id.UserAnsweringCount);
        TextView RightAnsweringCount = findViewById(R.id.RightAnsweringCount);
        TextView WrongAnsweringCount = findViewById(R.id.WrongAnsweringCount);
        TextView TourneyPtView = findViewById(R.id.TourneyPtView);
        SharedPreferences RecordInfo = getSharedPreferences("RecordDataFile", MODE_PRIVATE);
        //1.loading data.
        UserLevelCount.setText("Lv."+ SupportClass.getIntData(this,"EXPInformationStoreProfile","UserLevel",1));
        ComboCount.setText("Quest Combo "+ SupportClass.getIntData(this,"RecordDataFile","ComboGotten",0));
        DailyDifficultyCount.setText("Daily Difficulty " + RecordInfo.getInt("MaxDailyDifficulty",0));
        EXPGottenCount.setText(SupportClass.getIntData(this,"RecordDataFile","EXPGotten",0) + " " + getString(R.string.EXPWordTran));
        PointGottenCount.setText(SupportClass.getLongData(this,"RecordDataFile","PointGotten",0) + " " + getString(R.string.PointWordTran));
        MaterialGottenCount.setText(SupportClass.getIntData(this,"RecordDataFile","MaterialGotten",0) + " " + getString(R.string.MaterialWordTran));
        DefeatBossCount.setText("Boss defeated "+ SupportClass.getIntData(this,"BattleDataProfile","BossDeadTime",0));
        PassedConflictCount.setText("Conflict Floor " + SupportClass.getIntData(this,"BattleDataProfile","UserConflictFloor",0));
        TourneyPtView.setText(getString(R.string.TourneyPtWordTran) + "" + SupportClass.getLongData(this,"TourneyDataFile","MaxPtRecord",0));
        //1.1 calculating the Answering data.
        int RightAnswering = SupportClass.getIntData(this,"RecordDataFile","RightAnswering",0);
        int WrongAnswering = SupportClass.getIntData(this,"RecordDataFile","WrongAnswering",0);
        int TotalAnswering = RightAnswering + WrongAnswering;
        int RightRate = SupportClass.CalculatePercent(RightAnswering,TotalAnswering);
        int WrongRate = SupportClass.CalculatePercent(WrongAnswering,TotalAnswering);
        RightAnsweringCount.setText(RightRate + "% / " + RightAnswering + " Correct");
        WrongAnsweringCount.setText(WrongRate + "% / " + WrongAnswering + " Wrong");
        UserAnsweringCount.setText(TotalAnswering + " Answering");
}

    public void ReloadButtonMethod(View view){
        LoadRecordData();
    }//end of Record system.


    //handbook system.
    int CurrentAbilityNumber = 1;
    int AbilityDataLimit = 24;

    public void NextAbility(View view){
        if(CurrentAbilityNumber < AbilityDataLimit){
            CurrentAbilityNumber = CurrentAbilityNumber + 1;
        }else{
            CurrentAbilityNumber = 1;
        }
        LoadAbilityText();
    }

    public void BeforeAbility(View view){
        if(CurrentAbilityNumber > 1){
            CurrentAbilityNumber = CurrentAbilityNumber - 1;
        }else{
            CurrentAbilityNumber = AbilityDataLimit;
        }
        LoadAbilityText();
    }

    public void ShowAbilityHelpDialog(View view){
        SupportClass.CreateOnlyTextDialog(this,
                getString(R.string.HelpWordTran),
                getString(R.string.AbilityHelpTextTran),
                getString(R.string.ConfirmWordTran),
                "Nothing",
                true);
    }

    public void ShowFormulaDialog(View view){
        SupportClass.CreateOnlyTextDialog(this,
                getString(R.string.GameFormulaWordTran),
                "2021.4.22 更新\n" +
                        "APP内的公式：\n" +
                        "1 用户相关\n" +
                        "1.1 升级所需的EXP\n" +
                        "当前等级 ^ 1.6 + 15 - 当前等级 ^ 1.1\n" +
                        "1.2 答题奖励积分\n" +
                        "题目难度  *  2 + 连击数 ^ 1.65\n" +
                        "\n" +
                        "2 天赋相关\n" +
                        "2.1 升级费用\n" +
                        "（注：X为天赋当前等级）\n" +
                        "2.1.1 攻击天赋\n" +
                        "135 + X ^ 1.12 积分 / 级\n" +
                        "2.1.2 暴击率天赋\n" +
                        "165 + X ^ 1.37 积分 / 级\n" +
                        "2.1.3 暴击伤害天赋\n" +
                        "200 + X ^ 1.33 积分 / 级\n" +
                        "\n" +
                        "2.2 天赋效果\n" +
                        "2.2.1 攻击天赋\n" +
                        "+ 3 攻击力 / 级\n" +
                        "2.2.2 暴击率天赋\n" +
                        "+ 0.05% 暴击率 / 级\n" +
                        "2.2.3 暴击伤害天赋\n" +
                        "+ 0.2% 暴击伤害 / 级\n" +
                        "\n" +
                        "3 日常副本（望晨崖）相关\n" +
                        "（注1：A = 用户等级，B = 副本难度）\n" +
                        "（注2：【随机数：a~b】意为，大小在a和b之间（含a，b）的随机整数。例如：【随机数：10~20】可以是10到20之间的任何整数（包含10和20）。）\n" +
                        "3.1 Boss的HP值\n" +
                        "A * 【随机数：11 + （B * 10）~ 61 + （B * 8）】\n" +
                        "3.2 副本奖励\n" +
                        "3.2.1 EXP\n" +
                        "获得：（20+B*30 ）EXP\n" +
                        "3.2.2 积分\n" +
                        "获得：（1000+B*1500）积分\n" +
                        "3.2.3 素材\n" +
                        "获得：（2 + B * 3）素材\n" +
                        "\n" +
                        "4 战斗相关\n" +
                        "（注：A = boss的累计击败数）\n" +
                        "4.1 普通Boss的HP\n" +
                        "23 * (A + 1) * {14 + 【0.1 *  (A+ 1) 】}\n" +
                        "\n" +
                        "5 炼金相关\n" +
                        "（注：A = 要素数量， B = 要素上限--通常是6个（最大可达8个），C = 二阶要素使用量）\n" +
                        "5.1 炼金成功率\n" +
                        "【100 - A * （B - 3）】% / 次 \n" +
                        "5.2 素材消耗量\n" +
                        "{【（A / B）* 3 * B】 + C  * 7 } 素材 / 次\n" +
                        "5.3 炼金效果持续回合\n" +
                        "（注：此处的要素数量不含“平衡”系列的要素！“平衡”系列的要素会按照描述相应增加效果的持续时间。）\n" +
                        "【6 - A】回合 / 次\n" +
                        "5.4 炼金术等级\n" +
                        "5.4.1 等级需求和等级加成\n" +
                        "注1：升级后原有EXP将会被扣除！\n" +
                        "注2：各级效果不叠加，取最高值计算。\n" +
                        "等级 / 升级经验 / 效果\n" +
                        "lv.1  300EXP 成功率+2%\n" +
                        "lv.2  500EXP 成功率+4%\n" +
                        "lv.3  900EXP 成功率+7%\n" +
                        "lv.4  1400EXP 成功率+7%，要素上限+1\n" +
                        "lv.5  1900EXP 成功率+10%，要素上限+1\n" +
                        "lv.6  2500EXP 成功率+12%，要素上限+1\n" +
                        "lv.7  3200EXP 成功率+14%，要素上限+1\n" +
                        "lv.8  3900EXP 成功率+16%，要素上限+1\n" +
                        "lv.9  4700EXP 成功率+18%，要素上限+1\n" +
                        "lv.10 5500EXP 成功率+20%，要素上限+1\n" +
                        "lv.11 6400EXP 成功率+22%，要素上限+1\n" +
                        "lv.12 7300EXP 成功率+22%，要素上限+2\n" +
                        "lv.13 8200EXP 成功率+24%，要素上限+2\n" +
                        "lv.14 9100EXP 成功率+27%，要素上限+2\n" +
                        "lv.15 10000EXP 成功率+30%，要素上限+2\n" +
                        "5.4.2 经验获取\n" +
                        "每次炼金成功都将会获得：\n" +
                        "“要素数量 * 炼金消耗 * 【随机数：0~10】点”的炼金EXP。\n" +
                        "5.5 炼金效果及补充说明\n" +
                        "5.5.1 不同种类要素的效果是同时生效的。\n" +
                        "5.5.2 一次炼金当中，不同种类要素，多个同种要素的效果均会叠加，并持续至炼金效果的持续回合耗尽。\n" +
                        "5.5.3 一次炼金当中，包含的所有要素将同时开始和结束计时。",
                getString(R.string.ConfirmWordTran),
                "Nothing",
                true);
    }

    @SuppressLint("SetTextI18n")
    private void LoadAbilityText(){
        TextView AbilityShowingNumberView = findViewById(R.id.AbilityShowingNumberView);
        AbilityShowingNumberView.setText(CurrentAbilityNumber + " / " + AbilityDataLimit);
        PrintAbilityText(1,"考验","boss生命值上限是无此能力boss的1.3倍。出现概率18%");
        PrintAbilityText(2,"速攻","boss战斗回合数-1。出现概率10%");
        PrintAbilityText(3,"脆弱","玩家对此boss的暴击率升至100%，出现概率9%");
        PrintAbilityText(4,"弱点","玩家对此boss的暴击伤害数值+50%，出现概率7%");
        PrintAbilityText(5,"迟缓","玩家对此boss的战斗时长+1，出现概率11%");
        PrintAbilityText(6,"腐蚀","玩家对此boss的攻击力下降25%，数值向上取整，出现概率14%");
        PrintAbilityText(7,"诅咒","玩家对此boss的暴击率下降至0%，出现概率3%");
        PrintAbilityText(8,"卸力","玩家对此boss的暴击伤害下降至130%，出现概率6%");
        PrintAbilityText(9,"宝藏","玩家战胜该boss后获得的积分翻倍。出现概率9%");
        PrintAbilityText(10,"恢复","boss每回合随机恢复1点-（25%最大HP）点的HP，出现概率10%，恢复量向上取整");
        PrintAbilityText(11,"残伤","boss每回合随机损失1点-（17%最大HP）点的HP，出现概率6%，损失值向下取整");
        PrintAbilityText(12,"护盾","boss在生成时有5%概率获得自身HP20%-60%的护盾。护盾将会优先于HP扣除以抵挡伤害");
        PrintAbilityText(13,"绝望","boss不会受到任何伤害。（此能力不会概率生成，只有特殊boss拥有）");
        PrintAbilityText(14,"决斗","战斗回合为1.同时boss的HP上限较低。（注：APP不会自动生成此能力，需手动添加。即仅在特殊boss生效。）");
        PrintAbilityText(15,"快进","boss的战斗回合数消耗翻倍（-2/回合），出现概率5%");
        PrintAbilityText(16,"傲立","boss免疫低于自身HP上限11%的伤害。出现概率8%");
        PrintAbilityText(17,"惧意","boss单回合受到的伤害不高于自身HP上限的25%。出现概率9%");
        PrintAbilityText(18,"时滞","回合结算时，有33%概率不消耗回合数。（在“快进”能力存在时无效！）");
        PrintAbilityText(19,"考验II","boss生命值上限改为乘1.7倍。出现几率21%。（不与考验I叠加）");
        PrintAbilityText(20,"速攻II","boss战斗回合数再-1，出现几率18%");
        PrintAbilityText(21,"弱点II","用户的暴击伤害数值再+50%。出现几率10%");
        PrintAbilityText(22,"迟缓II","boss的战斗回合再+1，出现几率13%");
        PrintAbilityText(23,"腐蚀II","用户的攻击力再下降25%。出现几率14%");
        PrintAbilityText(24,"卸力II","用户的暴击伤害再降低20%。出现几率19%");
        //.......
    }

    //sub method of LoadAbilityText method.
    private void PrintAbilityText(int AbilityID, String AbilityName, String AbilityEffect){
        EditText AbilityNameShowView = findViewById(R.id.AbilityNameShowView);
        EditText AbilityEffectShowView = findViewById(R.id.AbilityEffectShowView);
        if(AbilityID == CurrentAbilityNumber){
            AbilityNameShowView.setText(AbilityName);
            AbilityEffectShowView.setText(AbilityEffect);
        }
    }//end of handbook system.


    //playerData system.
//    int ATKTalentLevel;
//    int CRTalentLevel;
//    int CDTalentLevel;
//    int UserLevel;

    @SuppressLint("SetTextI18n")
    private void LoadPlayerData(){
        //0. preparation.
        TextView LevelATKView = findViewById(R.id.LevelATKView);
        TextView LevelCRView = findViewById(R.id.LevelCRView);
        TextView LevelCDView = findViewById(R.id.LevelCDView);
        TextView AddATKView = findViewById(R.id.AddATKView);
        TextView AddCRView = findViewById(R.id.AddCRView);
        TextView AddCDView = findViewById(R.id.AddCDView);

        //1. load the data from SharedPreference.
        //note: we can not using "+" in String mix with int data, because "+" in this situation, instead of "Add", it will work like "Combine".
        //for example ("Abc:" + 10 + 20 *10) , it will not show "abc:210" , it will provide you "abc:10200"(combine with all number together!)
        int UserATK = 10 + SupportClass.getIntData(this,"EXPInformationStoreProfile","UserLevel",1) * 10;

        LevelATKView.setText(getString(R.string.ATKWordTran) + "  " + UserATK);
        LevelCRView.setText(getString(R.string.CritialRateWordTran) + "  " + 5.00 + "%");
        LevelCDView.setText(getString(R.string.CritialDamageWordTran) + "  " + 150.0 + "%");
        AddATKView.setText("(+" + SupportClass.getIntData(this,"BattleDataProfile","ATKTalentLevel",0) * 3 + ")");
        AddCRView.setText("(+" + SupportClass.getIntData(this,"BattleDataProfile","CRTalentLevel",0) * 0.05 + "%)");
        AddCDView.setText("(+" + SupportClass.getIntData(this,"BattleDataProfile","CDTalentLevel",0) * 0.2 + "%)");
    }


    //basic support system.
    public void GoToStory(View view){
        Intent i = new Intent(MemoryActivity.this, StoryActivity.class);
        startActivity(i);
    }

    public void GoToAchievement(View view){
        Intent i = new Intent(MemoryActivity.this,AchievementActivity.class);
        startActivity(i);
    }
}