package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
        //thanks to: https://dev.mi.com/console/doc/detail?pId=2229 !
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        CloseAllLayout();
        SetLanguageHint();
        AbilityList abilityList = new AbilityList(this);
        AbilityDataLimit = abilityList.ReturnAbilityNumber();
    }

    //add a button menu to ActionBar in MainActivity.
    //thanks to: https://stackoverflow.com/questions/17425742/how-to-add-button-in-actionbarandroid !
    // https://blog.csdn.net/oxiaoxio/article/details/48900763 !
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.backpack_activity_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_TimerHistory) {//if setting icon in Menu be touched.
            SupportClass.CreateNoticeDialog(this,
                    getString(R.string.HelpWordTran),
                    "Boss能力用语规范：\n" +
                            "【数值上升（+）/数值下降（-）】：数值本身的上升/下降（加算）。\n" +
                            "如：数值上升30%，200% + 30% = 230%\n" +
                            "【比例上升（+）/比例下降（-）】：数值比例的上升/下降（乘算）。\n" +
                            "如：比例上升30%，200% *（1 + 30%）= 260%\n" +
                            "如果只标注了【上升/下降】或是【+ / -】，则一般指【比例上升/比例下降】。",
                    getString(R.string.ConfirmWordTran));
        }
        return super.onOptionsItemSelected(item);
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
        TextView BossFailCount = findViewById(R.id.BossFailCount);
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
        BossFailCount.setText(getString(R.string.BossFailTran) + " " + SupportClass.ReturnKiloIntString(SupportClass.getIntData(this,"RecordDataFile","BattleFail",0)));
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
    int AbilityDataLimit;

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
        AbilityList abilityList = new AbilityList(this);
        AbilityDataLimit = abilityList.ReturnAbilityNumber();
        EditText AbilityNameShowView = findViewById(R.id.AbilityNameShowView);
        EditText AbilityEffectShowView = findViewById(R.id.AbilityEffectShowView);
        AbilityNameShowView.setText(abilityList.NameList.get(CurrentAbilityNumber - 1) );//trans to based on 0.
        AbilityEffectShowView.setText(abilityList.EffectList.get(CurrentAbilityNumber - 1) );//trans to based on 0.
    }//end of handbook system.


    //playerData system.
    int LevelATK;
    int AddATK;
    int TotalATK;

    @SuppressLint("SetTextI18n")
    private void LoadPlayerData(){
        //0. load the data from SharedPreference.
        LevelATK = 10 + SupportClass.getIntData(this,"EXPInformationStoreProfile","UserLevel",1) * 10;
        AddATK = SupportClass.getIntData(this,"BattleDataProfile","ATKTalentLevel",0) * 3 +
                SupportClass.getIntData(this,"ExcessDataFile","LevelExcessATK",0);
        double AlchemyATKIndex = Math.max(0,SupportClass.getIntData(this,"AlchemyDataFile","ATKup",0) / 100.0);
        TotalATK = (int) ( (LevelATK + AddATK) * (1 + AlchemyATKIndex) );
        double TotalCR =
                5.00 + SupportClass.getIntData(this,"BattleDataProfile","CRTalentLevel",0) * 0.05 +
                        SupportClass.getIntData(this,"AlchemyDataFile","CRup",0) +
                        SupportClass.getIntData(this,"ExcessDataFile","LevelExcessCR",0);
        double TotalCD =
                150.0 + SupportClass.getIntData(this,"BattleDataProfile","CDTalentLevel",0) * 0.2 +
                        SupportClass.getIntData(this,"AlchemyDataFile","CDup",0) +
                        SupportClass.getIntData(this,"ExcessDataFile","LevelExcessCD",0);
        int AlchemyTurn = SupportClass.getIntData(this,"AlchemyDataFile","AlchemyTurn",0);
        //1. preparation.
        TextView PlayerDataView = findViewById(R.id.PlayerDataView);
        //1.1 Text Branch.
        String Line8;
        if(AlchemyTurn <= 0){
            Line8 = "--";
        }else{
            Line8 = String.valueOf(AlchemyTurn);
        }
        //1.2 show text to user.
        String[] Data = new String[]{
                getString(R.string.ATKWordTran),
                String.valueOf(TotalATK),
                getString(R.string.CritialRateWordTran),
                TotalCR + "%",
                getString(R.string.CritialDamageWordTran),
                TotalCD + "%",
                getString(R.string.AlchemyTurnWordTran),
                Line8
        };
        StringBuilder TotalText = new StringBuilder();
        for(String EachLine: Data){
            TotalText.append(EachLine).append("\n");//just like TotalText  + EachLine + "\n";
        }
        PlayerDataView.setText(TotalText.toString());
        //note: we can not using "+" in String mix with int data, because "+" in this situation, instead of "Add", it will work like "Combine".
        //for example ("Abc:" + 10 + 20 * 10) , it will not show "abc:210" , it will provide you "abc:10200"(combine with all number together!)
    }

    public void ShowPlayerDetail(View view){
        SupportClass.CreateNoticeDialog(this,
                getString(R.string.PlayerDataDetailTran),
                getString(R.string.ATKWordTran) + "\n" +
                getString(R.string.LevelWordTran) + "\n" +
                        LevelATK + "\n" +
                        getString(R.string.TalentWordTran) + "\n" +
                        (SupportClass.getIntData(this,"BattleDataProfile","ATKTalentLevel",0) * 3) + "\n" +
                        getString(R.string.LevelExcessTran) + "\n" +
                        SupportClass.getIntData(this,"ExcessDataFile","LevelExcessATK",0) + "\n" +
                        getString(R.string.AlchemyButtonTran) + "\n" +
                        (TotalATK - LevelATK - AddATK) + "\n\n" +
                        getString(R.string.CritialRateWordTran) + "\n" +
                        getString(R.string.LevelWordTran) + "\n" +
                        "5.00\n" +
                        getString(R.string.TalentWordTran) + "\n" +
                        (SupportClass.getIntData(this,"BattleDataProfile","CRTalentLevel",0) * 0.05) + "\n" +
                        getString(R.string.LevelExcessTran) + "\n" +
                        SupportClass.getIntData(this,"ExcessDataFile","LevelExcessCR",0) + "\n" +
                        getString(R.string.AlchemyButtonTran) + "\n" +
                        SupportClass.getIntData(this,"AlchemyDataFile","CRup",0) + "\n\n" +
                        getString(R.string.CritialDamageWordTran) + "\n" +
                        getString(R.string.LevelWordTran) + "\n" +
                        "150.0\n" +
                        getString(R.string.TalentWordTran) + "\n" +
                        (SupportClass.getIntData(this,"BattleDataProfile","CDTalentLevel",0) * 0.2) + "\n" +
                        getString(R.string.LevelExcessTran) + "\n" +
                        SupportClass.getIntData(this,"ExcessDataFile","LevelExcessCD",0) + "\n" +
                        getString(R.string.AlchemyButtonTran) + "\n" +
                        SupportClass.getIntData(this,"AlchemyDataFile","CRup",0),
                getString(R.string.ConfirmWordTran)
        );
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