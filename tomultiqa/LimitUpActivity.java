package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LimitUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limit_up);
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
        //Import data from another Activity.
        InitializingResourceData();
        InitializingEXPInformation();
        InitializingQuestData();
        //Start calculation.
        InitializingLevelExcess();
    }


    //Level Excess system.
    //tips: [LE] means [Level Excess].
    int LevelLimit = 50;
    int LevelExcessRank = 0;
    int LevelRankLimit = 10;//the max Rank user can upgrade.
    int LevelExcessATK = 0;
    int LevelExcessCR = 0;
    int LevelExcessCD = 0;
    //Requirement.
    int LevelExcessPoint = 0;
    int LevelExcessEXP = 0;
    boolean LEMissionFinish = false;
    int LERightRate = 0;
    int LETotalQuest = 0;
    int LECombo = 0;

    //lv.3 method, main method.
    @SuppressLint("SetTextI18n")
    public void StartLevelExcess(View view){
        //0.decide if user can upgrade available or not.
        if(UserPoint >= LevelExcessPoint && UserHaveEXP >= LevelExcessEXP && LevelExcessRank < LevelRankLimit){
            TextView LevelExcessNumberView = findViewById(R.id.LevelExcessNumberView);
            TextView PointCountInExcess = findViewById(R.id.PointCountInExcess);
            //1.upgrade the Level Rank.
            LevelExcessRank = LevelExcessRank + 1;
            //2.Cost Point and EXP.
            UserPoint = UserPoint - LevelExcessPoint;
            PointCountInExcess.setText(UserPoint + "");
            SupportClass.saveIntData(this,"BattleDataProfile","UserPoint",UserPoint);
            SupportClass.saveIntData(this,"EXPInformationStoreProfile","UserHaveEXP",UserHaveEXP - LevelExcessEXP);
            //3.show new Rank to user.
            LevelExcessNumberView.setText(LevelExcessRank + "");
            //4. reload the upgrade requirement of New Rank.
            LoadLevelExcessNeed();
            LoadLevelExcessAdd();
            CheckLevelExcessMission();
        }else if(LevelExcessRank >= LevelRankLimit){
            //0.1 User have finished all Ranks available.
            SupportClass.CreateOnlyTextDialog(this,
                    getString(R.string.ReportWordTran),
                    "You have finished Max Rank.",
                    getString(R.string.ConfirmWordTran),
                    "Nothing",
                    true);
        }else{
            //0.2 User has not enough Point or EXP to upgrade.
            SupportClass.CreateOnlyTextDialog(this,
                    getString(R.string.ReportWordTran),
                    "Upgrade Requirement not finished.",
                    getString(R.string.ConfirmWordTran),
                    "Nothing",
                    true);
        }
        //5.store Rank data.
        SaveLevelExcess();
    }

    //lv.3 method, main method.
    @SuppressLint("SetTextI18n")
    private void InitializingLevelExcess(){
        TextView LevelExcessNumberView = findViewById(R.id.LevelExcessNumberView);
        LevelLimit = SupportClass.getIntData(this,"ExcessDataFile","LevelLimit",50);
        LevelExcessRank = SupportClass.getIntData(this,"ExcessDataFile","LevelExcessRank",0);
        LevelExcessATK= SupportClass.getIntData(this,"ExcessDataFile","LevelExcessATK",0);
        LevelExcessCR = SupportClass.getIntData(this,"ExcessDataFile","LevelExcessCR",0);
        LevelExcessCD = SupportClass.getIntData(this,"ExcessDataFile","LevelExcessCD",0);
        LevelExcessNumberView.setText(LevelExcessRank + "");
        LoadLevelExcessNeed();
        LoadLevelExcessAdd();
        CheckLevelExcessMission();
    }

    //lv.2 method, sub method of InitializingLevelExcess() method.
    @SuppressLint("SetTextI18n")
    private void LoadLevelExcessNeed(){
        //standard: cost about 1.5-2 times of max level EXP.
        TextView LevelExcessPointView = findViewById(R.id.LevelExcessPointView);
        TextView LevelExcessEXPView = findViewById(R.id.LevelExcessEXPView);
        PrintLevelExcessNeed(0,100000,750);//Excess lv.75
        PrintLevelExcessNeed(1,250000,2000);//Excess lv.100
        PrintLevelExcessNeed(2,500000,3500);//Excess lv.125
        PrintLevelExcessNeed(3,800000,5000);//Excess lv.150
        PrintLevelExcessNeed(4,1200000,7000);//Excess lv.175
        PrintLevelExcessNeed(5,1600000,9000);//Excess lv.200
        PrintLevelExcessNeed(6,2000000,11000);//Excess lv.225
        PrintLevelExcessNeed(7,2500000,13000);//Excess lv.250
        PrintLevelExcessNeed(8,3250000,15000);//Excess lv.275
        PrintLevelExcessNeed(9,5000000,20000);//Excess lv.300
        LevelExcessPointView.setText(LevelExcessPoint + " " + getString(R.string.PointWordTran));
        LevelExcessEXPView.setText(LevelExcessEXP + " " + getString(R.string.EXPWordTran));
    }

    //lv.2 method, sub method of InitializingLevelExcess() method.
    @SuppressLint("SetTextI18n")
    private void LoadLevelExcessAdd(){
        TextView LevelExcessATKView = findViewById(R.id.LevelExcessATKView);
        TextView LevelExcessCRView = findViewById(R.id.LevelExcessCRView);
        TextView LevelExcessCDView = findViewById(R.id.LevelExcessCDView);
        TextView LevelMaxView = findViewById(R.id.LevelMaxView);
        PrintLevelExcessAdd(1,75,80,2,5);
        PrintLevelExcessAdd(2,100,250,4,10);
        PrintLevelExcessAdd(3,125,400,6,15);
        PrintLevelExcessAdd(4,150,600,8,20);
        PrintLevelExcessAdd(5,175,1000,10,25);
        PrintLevelExcessAdd(6,200,1500,12,30);
        PrintLevelExcessAdd(7,225,2300,14,35);
        PrintLevelExcessAdd(8,250,3000,17,40);
        PrintLevelExcessAdd(9,275,4000,20,50);
        PrintLevelExcessAdd(10,300,5000,25,50);
        LevelExcessATKView.setText(getString(R.string.ATKWordTran) + " +" + LevelExcessATK);
        LevelExcessCRView.setText(getString(R.string.CritialRateWordTran) + " +" + LevelExcessCR + "%");
        LevelExcessCDView.setText(getString(R.string.CritialDamageWordTran) + " +" + LevelExcessCD + "%");
        LevelMaxView.setText(getString(R.string.LevelLimitTran) + " Lv." + LevelLimit);
    }

    //lv.2 method, main method.
    private void CheckLevelExcessMission(){
        Button LevelExcessMissionButton = findViewById(R.id.LevelExcessMissionButton);
        PrintLevelExcessMission(0,50,25,2);
        PrintLevelExcessMission(1,60,100,3);
        PrintLevelExcessMission(2,65,220,4);
        PrintLevelExcessMission(3,70,400,5);
        PrintLevelExcessMission(4,75,600,6);
        PrintLevelExcessMission(5,80,850,7);
        PrintLevelExcessMission(6,83,1200,8);
        PrintLevelExcessMission(7,86,1800,9);
        PrintLevelExcessMission(8,89,3000,10);
        PrintLevelExcessMission(9,90,4500,10);
        if(UserRightRate >= LERightRate && UserTotalQuest >= LETotalQuest && UserCombo >= LECombo){
            LEMissionFinish = true;
            LevelExcessMissionButton.setTextColor(Color.GREEN);
        }
    }

    //lv.1 method, main method of Level Excess Mission.
    public void ShowLEMission(View view){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.MissionWordTran));
        dialog.setMessage(getString(R.string.ProgressWordTran) + "\n" +
                getString(R.string.RightRateWordTran) + "\n" +
                UserRightRate + " / " + LERightRate + "\n" +
                getString(R.string.TotalAnsweringTran) + "\n" +
                UserTotalQuest + " / " + LETotalQuest + "\n" +
                getString(R.string.MaxComboTran) + "\n" +
                UserCombo + " / " + LECombo);
        dialog.setCancelable(true);
        dialog.setPositiveButton(
                getString(R.string.ConfirmWordTran),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog DialogView = dialog.create();
        DialogView.show();
    }

    //lv.1 method, sub method of StartLevelExcess() method.
    private void SaveLevelExcess(){
        SupportClass.saveIntData(this,"ExcessDataFile","LevelLimit",LevelLimit);
        SupportClass.saveIntData(this,"ExcessDataFile","LevelExcessRank",LevelExcessRank);
        SupportClass.saveIntData(this,"ExcessDataFile","LevelExcessATK",LevelExcessATK);
        SupportClass.saveIntData(this,"ExcessDataFile","LevelExcessCR",LevelExcessCR);
        SupportClass.saveIntData(this,"ExcessDataFile","LevelExcessCD",LevelExcessCD);
    }

    //lv.1 method, sub method of LoadLevelExcessNeed() method.
    private void PrintLevelExcessNeed(int Rank,  int Point, int EXP){
        if(LevelExcessRank == Rank){
            LevelExcessPoint = Point;
            LevelExcessEXP = EXP;
        }
    }

    //lv.1 method, sub method of LoadLevelExcessAdd() method.
    private void PrintLevelExcessAdd(int Rank, int Level, int ATK, int CR, int CD){
        if(LevelExcessRank == Rank){
            LevelLimit = Level;
            LevelExcessATK = ATK;
            LevelExcessCR = CR;
            LevelExcessCD = CD;
        }
    }

    //lv.1 method, sub method of LoadLevelExcessMission() method.
    private void PrintLevelExcessMission(int Rank, int RightRate, int TotalQuest, int Combo){
        if(LevelExcessRank == Rank){
            LERightRate = RightRate;
            LETotalQuest = TotalQuest;
            LECombo = Combo;
        }
    }//end of Level Excess system.


    //resource Import.
    int UserPoint;
    int UserMaterial;

    @SuppressLint("SetTextI18n")
    private void InitializingResourceData(){
        TextView PointCountInExcess = findViewById(R.id.PointCountInExcess);
        TextView MaterialCountInExcess = findViewById(R.id.MaterialCountInExcess);
        UserPoint = SupportClass.getIntData(this,"BattleDataProfile","UserPoint",0);
        UserMaterial = SupportClass.getIntData(this,"BattleDataProfile","UserMaterial",0);
        //make number into financial form.
        PointCountInExcess.setText(SupportClass.ReturnKiloIntString(UserPoint));
        MaterialCountInExcess.setText(SupportClass.ReturnKiloIntString(UserMaterial));
    }//end of resource Import.


    //EXP Import.
    int UserHaveEXP;

    //read EXP data method.
    private void InitializingEXPInformation(){
        UserHaveEXP = SupportClass.getIntData(this,"EXPInformationStoreProfile","UserHaveEXP",0);
    }//end of EXP Import.


    //Quest Import.
    int UserCombo = 0;
    int UserTotalQuest = 0;
    int UserRightRate = 0;

    private void InitializingQuestData(){
        UserCombo = SupportClass.getIntData(this,"RecordDataFile","ComboGotten",0);
        int RightAnswering = SupportClass.getIntData(this,"RecordDataFile","RightAnswering",0);
        int WrongAnswering = SupportClass.getIntData(this,"RecordDataFile","WrongAnswering",0);
        UserTotalQuest = RightAnswering + WrongAnswering;
        UserRightRate = SupportClass.CalculatePercent(RightAnswering,UserTotalQuest);
    }//end of Quest Import.


    //layout management system.
    public void CloseFunctionLayout(View view){
        //0.preparation: listing all Function Layout in this activity.
        Button ReturnToExcessButton = findViewById(R.id.ReturnToExcessButton);
        LinearLayout ExcessButtonLayout = findViewById(R.id.ExcessButtonLayout);

        //1.Close all Layout.
        CloseAllLayout();

        //2. showing ButtonLayout and close ReturnButton showing to provide user Functions which can be chosen.
        ExcessButtonLayout.setVisibility(View.VISIBLE);
        ReturnToExcessButton.setVisibility(View.GONE);
    }

    //main method.
    @SuppressLint("SetTextI18n")
    public void OpenFunctionLayout(View view){
        Button ReturnToExcessButton = findViewById(R.id.ReturnToExcessButton);
        LinearLayout ExcessButtonLayout = findViewById(R.id.ExcessButtonLayout);
        ConstraintLayout LevelExcessLayout = findViewById(R.id.LevelExcessLayout);

        //1.close all Function Layout before management, to do it easily.
        CloseAllLayout();

        //2.confirm that which button has been touched, and return it`s Text to use in next step as recognizing identifier.
        //thanks to: https://stackoverflow.com/questions/3412180/how-to-determine-which-button-pressed-on-android !
        Button AnyButtonView = findViewById(view.getId());
        String ButtonText = AnyButtonView.getText().toString();

        //3.open certain Layout. and record which layout have been opened.
        if(ButtonText.equals(getString(R.string.LevelExcessTran))){
            LevelExcessLayout.setVisibility(View.VISIBLE);
        }

        //4.show return button to user.And close choose Function layout.
        ReturnToExcessButton.setText(getString(R.string.ReturnToWordTran)+ " " + ButtonText +" >");
        ReturnToExcessButton.setVisibility(View.VISIBLE);
        ExcessButtonLayout.setVisibility(View.GONE);
    }

    //sub method.
    private void CloseAllLayout(){
        Button ReturnToExcessButton = findViewById(R.id.ReturnToExcessButton);
        ConstraintLayout LevelExcessLayout = findViewById(R.id.LevelExcessLayout);

        ReturnToExcessButton.setVisibility(View.GONE);
        LevelExcessLayout.setVisibility(View.GONE);
    }//end of Layout Management system.
}