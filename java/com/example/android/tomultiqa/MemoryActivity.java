package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Locale;

public class MemoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);
        //make StatusBar is same color as ActionBar in Activity.
        //thanks to: https://blog.csdn.net/kiven9609/article/details/73162307 !
        // https://www.color-hex.com/color/26c6da ! #26C6DA is our App Primary Color.
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.rgb(38,198,218));
        //底部导航栏
        //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
        //main method below.
        CloseAllLayout();
        SetLanguageHint();
    }


    //hint function.
    private void SetLanguageHint(){
        String Country = Locale.getDefault().getCountry();
        if(!Country.equals("CN")){
            SupportClass.CreateNoticeDialog(this,
                    getString(R.string.HintWordTran),
                    "Some of Text in this Page has not translated yet,\n" +
                            "you need to have the language skill to read Chinese.",
                    getString(R.string.ConfirmWordTran)
                    );
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
        ReturnLayoutButton.setText(getString(R.string.ReturnToWordTran)+ " " + CurrentShowingLayout +" >");
        ReturnLayoutButton.setVisibility(View.VISIBLE);
        ButtonLayout.setVisibility(View.GONE);
    }

    public void CloseFunctionLayout(View view){
        //0.preparation: listing all Function Layout in this activity.
        Button ReturnLayoutButton = findViewById(R.id.ReturnLayoutButton);
        LinearLayout ButtonLayout = findViewById(R.id.ButtonLayout);
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
        //1.loading data.
        UserLevelCount.setText("Lv."+ SupportClass.getIntData(this,"EXPInformationStoreProfile","UserLevel",1));
        ComboCount.setText(getString(R.string.MaxComboTran)+ " " + SupportClass.getIntData(this,"RecordDataFile","ComboGotten",0));
        DailyDifficultyCount.setText(getString(R.string.DailyClearTran) + " " + SupportClass.getIntData(this,"RecordDataFile","MaxDailyDifficulty",0));
        EXPGottenCount.setText(SupportClass.ReturnKiloIntString(SupportClass.getIntData(this,"RecordDataFile","EXPGotten",0)) + " " + getString(R.string.EXPWordTran));
        PointGottenCount.setText(SupportClass.ReturnKiloLongString(SupportClass.getLongData(this,"RecordDataFile","PointGotten",0)) + " " + getString(R.string.PointWordTran));
        MaterialGottenCount.setText(SupportClass.ReturnKiloIntString(SupportClass.getIntData(this,"RecordDataFile","MaterialGotten",0)) + " " + getString(R.string.MaterialWordTran));
        DefeatBossCount.setText(getString(R.string.BossDeadTimeTran) +" " +  SupportClass.ReturnKiloIntString(SupportClass.getIntData(this,"BattleDataProfile","BossDeadTime",0)));
        PassedConflictCount.setText(getString(R.string.ConflictClearTran) + " " + SupportClass.getIntData(this,"BattleDataProfile","UserConflictFloor",0));
        TourneyPtView.setText(getString(R.string.TourneyPtWordTran) + " " + SupportClass.ReturnKiloLongString(SupportClass.getLongData(this,"TourneyDataFile","MaxPtRecord",0)));
        //1.1 calculating the Answering data.
        int RightAnswering = SupportClass.getIntData(this,"RecordDataFile","RightAnswering",0);
        int WrongAnswering = SupportClass.getIntData(this,"RecordDataFile","WrongAnswering",0);
        int TotalAnswering = RightAnswering + WrongAnswering;
        int RightRate = SupportClass.CalculatePercent(RightAnswering,TotalAnswering);
        int WrongRate = SupportClass.CalculatePercent(WrongAnswering,TotalAnswering);
        RightAnsweringCount.setText(RightRate + "% / " + RightAnswering + " " + getString(R.string.CorrectWordTran));
        WrongAnsweringCount.setText(WrongRate + "% / " + WrongAnswering + " " + getString(R.string.WrongWordTran));
        UserAnsweringCount.setText(TotalAnswering + " " + getString(R.string.TotalAnsweringTran));
}

    public void ReloadButtonMethod(View view){
        LoadRecordData();
    }//end of Record system.


    //handbook system.
    static int CurrentAbilityNumber = 1;
    static int AbilityDataLimit = 24;

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
        SupportClass.CreateNoticeDialog(this,
                getString(R.string.HelpWordTran),
                getString(R.string.AbilityHelpTextTran),
                getString(R.string.ConfirmWordTran));
    }

    public void ShowFormulaDialog(View view){
        SupportClass.CreateNoticeDialog(this,
                getString(R.string.GameFormulaWordTran),
                ValueClass.GAME_FORMULA_HELP,
                getString(R.string.ConfirmWordTran));
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