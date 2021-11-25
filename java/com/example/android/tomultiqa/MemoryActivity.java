package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
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

    //Override ActionBar's return button, it can't controlled by OnKeyDown().
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_TimerHistory) {//if setting icon in Menu be touched.
            SupportLib.CreateNoticeDialog(this,
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

    //override return button on global system.
    //listen return button, thanks to: https://www.cnblogs.com/HDK2016/p/8695052.html !
    //method support: https://blog.csdn.net/ccpat/article/details/45176665 !
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){//if user touch return button, and the key is pressed.
            //0.preparation: listing all Function Layout in this activity.
            ScrollView MemoryButtonList = findViewById(R.id.MemoryButtonList);
            if(MemoryButtonList.getVisibility() == View.GONE){
                //1.Close all Layout.
                CloseAllLayout();
                //2. showing ButtonList and close ReturnButton showing to provide user Functions which can be chosen.
                MemoryButtonList.setVisibility(View.VISIBLE);
                return false;//stop this time of return action of Activity.
            }else{
                Intent i = new Intent(this,MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                return true;//continue return action.
            }
        }
        return super.onKeyDown(keyCode, event);//continue execute system's return process.
    }//end of override.


    //Layout Management system.
    String CurrentShowingLayout = "Null";

    //closing all function layout.
    private void CloseAllLayout(){
        ConstraintLayout RecordLayout = findViewById(R.id.RecordLayout);
        ConstraintLayout PlayerDataLayout = findViewById(R.id.PlayerDataLayout);

        //1.close all Function Layout before management, to do it easily.
        RecordLayout.setVisibility(View.GONE);
        PlayerDataLayout.setVisibility(View.GONE);
    }

    @SuppressLint("SetTextI18n")
    public void OpenFunctionLayout(View view){
        //0.preparation: listing all Function Layout in this activity.
        ScrollView MemoryButtonList = findViewById(R.id.MemoryButtonList);
        ConstraintLayout RecordLayout = findViewById(R.id.RecordLayout);
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
        }else if(ButtonText.equals(getString(R.string.PlayerDataWordTran))){
            LoadPlayerData();
            PlayerDataLayout.setVisibility(View.VISIBLE);
        }
        CurrentShowingLayout = ButtonText;

        //4.show return button to user.And close choose Function layout.
        MemoryButtonList.setVisibility(View.GONE);
    }

    public void CloseFunctionLayout(View view){
        //0.preparation: listing all Function Layout in this activity.
        ScrollView MemoryButtonList = findViewById(R.id.MemoryButtonList);
        CloseAllLayout();

        //1.showing ButtonLayout and close ReturnButton showing to provide user Functions which can be chosen.
        MemoryButtonList.setVisibility(View.VISIBLE);
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
        UserLevelCount.setText("Lv."+ SupportLib.getIntData(this,"EXPInformationStoreProfile","UserLevel",1));
        ComboCount.setText(getString(R.string.MaxComboTran)+ " " + SupportLib.getIntData(this,"RecordDataFile","ComboGotten",0));
        DailyDifficultyCount.setText(getString(R.string.DailyClearTran) + " " + SupportLib.getIntData(this,"RecordDataFile","MaxDailyDifficulty",0));
        EXPGottenCount.setText(SupportLib.ReturnKiloIntString(SupportLib.getIntData(this,"RecordDataFile","EXPGotten",0)) + " " + getString(R.string.EXPWordTran));
        PointGottenCount.setText(SupportLib.ReturnKiloLongString(SupportLib.getLongData(this,"RecordDataFile","PointGotten",0)) + " " + getString(R.string.PointWordTran));
        MaterialGottenCount.setText(SupportLib.ReturnKiloIntString(SupportLib.getIntData(this,"RecordDataFile","MaterialGotten",0)) + " " + getString(R.string.MaterialWordTran));
        DefeatBossCount.setText(getString(R.string.BossDeadTimeTran) +" " +  SupportLib.ReturnKiloIntString(SupportLib.getIntData(this,"BattleDataProfile","BossDeadTime",0)));
        BossFailCount.setText(getString(R.string.BossFailTran) + " " + SupportLib.ReturnKiloIntString(SupportLib.getIntData(this,"RecordDataFile","BattleFail",0)));
        PassedConflictCount.setText(getString(R.string.ConflictClearTran) + " " + SupportLib.getIntData(this,"BattleDataProfile","UserConflictFloor",0));
        TourneyPtView.setText(getString(R.string.TourneyPtWordTran) + " " + SupportLib.ReturnKiloLongString(SupportLib.getLongData(this,"TourneyDataFile","MaxPtRecord",0)));
        //1.1 calculating the Answering data.
        int RightAnswering = SupportLib.getIntData(this,"RecordDataFile","RightAnswering",0);
        int WrongAnswering = SupportLib.getIntData(this,"RecordDataFile","WrongAnswering",0);
        int TotalAnswering = RightAnswering + WrongAnswering;
        int RightRate = SupportLib.CalculatePercent(RightAnswering,TotalAnswering);
        int WrongRate = SupportLib.CalculatePercent(WrongAnswering,TotalAnswering);
        RightAnsweringCount.setText(RightRate + "% / " + RightAnswering + " " + getString(R.string.CorrectWordTran));
        WrongAnsweringCount.setText(WrongRate + "% / " + WrongAnswering + " " + getString(R.string.WrongWordTran));
        UserAnsweringCount.setText(TotalAnswering + " " + getString(R.string.TotalAnsweringTran));
    }

    public void ReloadButtonMethod(View view){
        LoadRecordData();
    }//end of Record system.


    //playerData system.
    int LevelATK;
    int AddATK;
    int TotalATK;

    @SuppressLint("SetTextI18n")
    private void LoadPlayerData(){
        //0. load the data from SharedPreference.
        LevelATK = 10 + SupportLib.getIntData(this,"EXPInformationStoreProfile","UserLevel",1) * 10;
        AddATK = SupportLib.getIntData(this,"BattleDataProfile","ATKTalentLevel",0) * ValueLib.ATK_TALENT_INDEX +
                SupportLib.getIntData(this,"ExcessDataFile","LevelExcessATK",0);
        double AlchemyATKIndex = Math.max(0, SupportLib.getIntData(this,"AlchemyDataFile","ATKup",0) / 100.0);
        TotalATK = (int) ( (LevelATK + AddATK) * (1 + AlchemyATKIndex) );
        double TotalCR =
                5.00 + SupportLib.getIntData(this,"BattleDataProfile","CRTalentLevel",0) * ValueLib.CR_TALENT_INDEX +
                        SupportLib.getIntData(this,"AlchemyDataFile","CRup",0) +
                        SupportLib.getIntData(this,"ExcessDataFile","LevelExcessCR",0);
        double TotalCD =
                150.0 + SupportLib.getIntData(this,"BattleDataProfile","CDTalentLevel",0) * ValueLib.CD_TALENT_INDEX +
                        SupportLib.getIntData(this,"AlchemyDataFile","CDup",0) +
                        SupportLib.getIntData(this,"ExcessDataFile","LevelExcessCD",0);
        int AlchemyTurn = SupportLib.getIntData(this,"AlchemyDataFile","AlchemyTurn",0);
        //1. preparation.
        TextView PlayerDataView = findViewById(R.id.PlayerDataView);
        //1.1 Text Branch.
        String AlchemyEffect;
        if(AlchemyTurn <= 0){
            AlchemyEffect = "-";
        }else{
            AlchemyEffect = String.valueOf(AlchemyTurn);
        }
        //1.2 show text to user.
        String[] Data = new String[]{
                getString(R.string.ATKWordTran),
                TotalATK + "\n",
                getString(R.string.CritialRateWordTran),
                TotalCR + "%\n",
                getString(R.string.CritialDamageWordTran),
                TotalCD + "%\n",
                getString(R.string.AlchemyTurnWordTran),
                AlchemyEffect
        };
        StringBuilder AllText = new StringBuilder();
        for(String EachLine: Data){
            AllText.append(EachLine).append("\n");//just like TotalText  + EachLine + "\n";
        }
        PlayerDataView.setText(AllText.toString());
        //note: we can not using "+" in String mix with int data, because "+" in this situation, instead of "Add", it will work like "Combine".
        //for example ("Abc:" + 10 + 20 * 10) , it will not show "abc:210" , it will provide you "abc:10200"(combine with all number together!)
    }

    public void ShowPlayerDetail(View view){
        SupportLib.CreateNoticeDialog(this,
                getString(R.string.PlayerDataDetailTran),
                getString(R.string.ATKWordTran) + "\n" +
                getString(R.string.LevelWordTran) + "\n" +
                        LevelATK + "\n" +
                        getString(R.string.TalentWordTran) + "\n" +
                        (SupportLib.getIntData(this,"BattleDataProfile","ATKTalentLevel",0) * ValueLib.ATK_TALENT_INDEX) + "\n" +
                        getString(R.string.LevelExcessTran) + "\n" +
                        SupportLib.getIntData(this,"ExcessDataFile","LevelExcessATK",0) + "\n" +
                        getString(R.string.AlchemyButtonTran) + "\n" +
                        (TotalATK - LevelATK - AddATK) + "\n\n" +
                        getString(R.string.CritialRateWordTran) + "\n" +
                        getString(R.string.LevelWordTran) + "\n" +
                        "5.00\n" +
                        getString(R.string.TalentWordTran) + "\n" +
                        (SupportLib.getIntData(this,"BattleDataProfile","CRTalentLevel",0) * ValueLib.CR_TALENT_INDEX) + "\n" +
                        getString(R.string.LevelExcessTran) + "\n" +
                        SupportLib.getIntData(this,"ExcessDataFile","LevelExcessCR",0) + "\n" +
                        getString(R.string.AlchemyButtonTran) + "\n" +
                        SupportLib.getIntData(this,"AlchemyDataFile","CRup",0) + "\n\n" +
                        getString(R.string.CritialDamageWordTran) + "\n" +
                        getString(R.string.LevelWordTran) + "\n" +
                        "150.0\n" +
                        getString(R.string.TalentWordTran) + "\n" +
                        (SupportLib.getIntData(this,"BattleDataProfile","CDTalentLevel",0) * ValueLib.CD_TALENT_INDEX) + "\n" +
                        getString(R.string.LevelExcessTran) + "\n" +
                        SupportLib.getIntData(this,"ExcessDataFile","LevelExcessCD",0) + "\n" +
                        getString(R.string.AlchemyButtonTran) + "\n" +
                        SupportLib.getIntData(this,"AlchemyDataFile","CRup",0),
                getString(R.string.ConfirmWordTran)
        );
    }

    //hint function.
    private void SetLanguageHint(){
        String Country = Locale.getDefault().getCountry();
        if(!Country.equals("CN")){
            SupportLib.CreateNoticeDialog(this,
                    getString(R.string.HintWordTran),
                    "Some of Text in this Page has not translated yet,\n" +
                            "you need to have the language skill to read Chinese.",
                    getString(R.string.ConfirmWordTran)
            );
        }
    }//end of hint function.

    //basic support system.
    public void GoToStory(View view){
        Intent i = new Intent(this, StoryActivity.class);
        startActivity(i);
    }

    public void GoToAchievement(View view){
        Intent i = new Intent(this,AchievementActivity.class);
        startActivity(i);
    }

    public void GoToHandbook(View view){
        Intent i = new Intent(this,HandbookActivity.class);
        startActivity(i);
    }
}