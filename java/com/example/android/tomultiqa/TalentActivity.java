package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TalentActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talent);
        //make StatusBar is same color as ActionBar in Activity.
        //thanks to: https://blog.csdn.net/kiven9609/article/details/73162307 !
        // https://www.color-hex.com/color/26c6da ! #26C6DA is our App Primary Color.
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.rgb(38,198,218));
        //底部导航栏
        //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
        //main method below.
        InitializingResourceData();
        InitializingTalentData();

        final CheckBox TalentCostAllPointCheckBox = findViewById(R.id.TalentCostAllPointCheckBox);
        TalentCostAllPointCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(TalentCostAllPointCheckBox.isChecked()){
                    SupportClass.CreateOnlyTextDialog(
                            TalentActivity.this,
                            getString(R.string.NoticeWordTran),
                            "This function is in experiment.\n" +
                                    "Make sure you have enough Point to upgrade.\n" +
                                    "And don`t set too large number.\n"+
                                    "It might make Lag or Crash.",
                            getString(R.string.ConfirmWordTran),
                            "Nothing",
                            true
                            );
                }
            }
        });

    }

    //basic value of whole activity.
    int UserPoint;
    int UserMaterial;
//    int UserHaveEXP;

//    private void GetEXP(int AddNumber){
//        UserHaveEXP = UserHaveEXP + AddNumber;
//        SupportClass.saveIntData(this,"EXPInformationStoreProfile","UserHaveEXP",UserHaveEXP);
//    }
//
//    private void getEXPInformation(){
//        SharedPreferences EXPInfo = getSharedPreferences("EXPInformationStoreProfile", MODE_PRIVATE);
//        UserHaveEXP = EXPInfo.getInt("UserHaveEXP", 0);
//    }//end of EXP system.

    //sub method.
    @SuppressLint("SetTextI18n")
    private void CostPoints(int CostNumber){
        UserPoint = UserPoint - CostNumber;
        SupportClass.saveIntData(this,"BattleDataProfile","UserPoint",UserPoint);
        TextView PointCountingView = findViewById(R.id.PointCountInMarket);
        PointCountingView.setText(SupportClass.ReturnKiloIntString(UserPoint));
    }

    @SuppressLint("SetTextI18n")
    private void InitializingResourceData(){
        TextView PointCountingView = findViewById(R.id.PointCountInMarket);
        TextView MaterialCountingView = findViewById(R.id.KeyCountInMarket);
        UserPoint = SupportClass.getIntData(this,"BattleDataProfile","UserPoint",0);
        UserMaterial = SupportClass.getIntData(this,"BattleDataProfile","UserMaterial",0);
        PointCountingView.setText(SupportClass.ReturnKiloIntString(UserPoint));
        MaterialCountingView.setText(SupportClass.ReturnKiloIntString(UserMaterial));
    }//end of basic value.


    //talent system.
    //basic values of talent system.
    int ATKTalentLevel;
    //CR means Critical Rate, and CD means Critical Damage.
    int CRTalentLevel;
    int CDTalentLevel;
    int ATKTalentCost;
    int CRTalentCost;
    int CDTalentCost;
    //recording total cost of chosen talent(s), which need to be upgraded.
    int UpgradeTime = 1;

    @SuppressLint("SetTextI18n")
    private void CalculateAndShowTalentCost(){
        TextView AttackTalentCostView = findViewById(R.id.AttackTalentCostView);
        TextView CRTalentCostView = findViewById(R.id.CRTalentCostView);
        TextView CDTalentCostView = findViewById(R.id.CDTalentCostView);

        ATKTalentCost = (int)(135 + Math.pow(ATKTalentLevel,1.12));
        CRTalentCost = (int)(165 + Math.pow(CRTalentLevel,1.37));
        CDTalentCost =(int)(200+ Math.pow(CDTalentLevel,1.33));

        AttackTalentCostView.setText(ATKTalentCost + "");
        CRTalentCostView.setText(CRTalentCost + "");
        CDTalentCostView.setText(CDTalentCost + "");
    }

    @SuppressLint("SetTextI18n")
    private void ShowTalentEffectAndLevel(){
        TextView AttackTalentEffectView =findViewById(R.id.AttackTalentEffectView);
        TextView CRTalentEffectView = findViewById(R.id.CRTalentEffectView);
        TextView CDTalentEffectView = findViewById(R.id.CDTalentEffectView);
        TextView AttackTalentLevelView = findViewById(R.id.AttackTalentLevelView);
        TextView CRTalentLevelView = findViewById(R.id.CRTalentLevelView);
        TextView CDTalentLevelView = findViewById(R.id.CDTalentLevelView);

        AttackTalentEffectView.setText("+" + ATKTalentLevel * 3);
        CRTalentEffectView.setText("+" + SupportClass.ReturnTwoBitText(CRTalentLevel * 0.05) + "%");
        CDTalentEffectView.setText("+" + SupportClass.ReturnTwoBitText(CDTalentLevel * 0.2) + "%");
        AttackTalentLevelView.setText(ATKTalentLevel + "");
        CRTalentLevelView.setText(CRTalentLevel + "");
        CDTalentLevelView.setText(CDTalentLevel + "");
    }

    private void SaveTalentData(){
        SupportClass.saveIntData(this,"BattleDataProfile","ATKTalentLevel",ATKTalentLevel);
        SupportClass.saveIntData(this,"BattleDataProfile","CRTalentLevel",CRTalentLevel);
        SupportClass.saveIntData(this,"BattleDataProfile","CDTalentLevel",CDTalentLevel);
    }

    private void InitializingTalentData(){
        ATKTalentLevel = SupportClass.getIntData(this,"BattleDataProfile","ATKTalentLevel",0);
        CRTalentLevel = SupportClass.getIntData(this,"BattleDataProfile","CRTalentLevel",0);
        CDTalentLevel = SupportClass.getIntData(this,"BattleDataProfile","CDTalentLevel",0);
        CalculateAndShowTalentCost();
        ShowTalentEffectAndLevel();

        CheckBox CriticalRateCheckBox = findViewById(R.id.CriticalRateCheckBox);
        CheckBox CriticalDamageCheckBox = findViewById(R.id.CriticalDamageCheckBox);
        //if some talent`s level touch the max line, system will stop user choosing these talent(s) to upgrade.
        if(CRTalentLevel >= 1000){
            CriticalRateCheckBox.setClickable(false);
        }
        if(CDTalentLevel >= 750){
            CriticalDamageCheckBox.setClickable(false);
        }
    }

    @SuppressLint("SetTextI18n")
    public void UpgradeTalent(View view){
        //1.reload UpgradeTime.
        EditText UpgradeNumberView = findViewById(R.id.UpgradeNumberView);
        if(UpgradeNumberView.getText().length() != 0){
            UpgradeTime = Integer.parseInt(UpgradeNumberView.getText().toString());
            if(UpgradeTime > 1000 || UpgradeTime < 1){
                UpgradeTime = 1;
                UpgradeNumberView.setText(UpgradeTime + "");
            }
        }else{
            UpgradeTime = 1;
            UpgradeNumberView.setText(UpgradeTime + "");
        }
        //2.judge if User have enough Point to Upgrade all selected Talent(s).
        CheckBox TalentCostAllPointCheckBox = findViewById(R.id.TalentCostAllPointCheckBox);
        if(TalentCostAllPointCheckBox.isChecked()){
            //2.1 upgrade in cost all mode.
            while (UserPoint > 0 && UpgradeTime > 0){
                UpgradeTime = UpgradeTime - 1;
                UpgradeTalentSub();
            }
        }else if(!TalentCostAllPointCheckBox.isChecked() && UserPoint > 0){
            //2.2 upgrade in only 1 level mode.
            UpgradeTalentSub();
        }else {
            //2.3 if user don`t have enough Points,tell user.
            SupportClass.CreateOnlyTextDialog(this,
                    getString(R.string.NoticeWordTran),
                    "Not enough Point to upgrade.",
                    getString(R.string.ConfirmWordTran),
                    "Nothing",
                    true);
        }

        //3. after the upgrade calculation, save data and show detail to user.
        SaveTalentData();
        ShowTalentEffectAndLevel();

        //4. after the upgrade, higher level will have higher demand for next level.(reset the upgrade cost.)
        CalculateAndShowTalentCost();
        SupportClass.CreateOnlyTextDialog(this,
                getString(R.string.NoticeWordTran),
                "Upgrade Completed.",
                getString(R.string.ConfirmWordTran),
                "Nothing",
                true);
    }

    //sub method of UpgradeTalent method.
    private void UpgradeTalentSub(){
        CheckBox AttackTalentCheckBox = findViewById(R.id.AttackTalentCheckBox);
        CheckBox CriticalRateCheckBox = findViewById(R.id.CriticalRateCheckBox);
        CheckBox CriticalDamageCheckBox = findViewById(R.id.CriticalDamageCheckBox);
        //1.check user`s Point number is enough or not. and upgrade the Talent.
            if(AttackTalentCheckBox.isChecked() && UserPoint >= ATKTalentCost){
                //2.1.1 upgrade ATK talent.
                ATKTalentLevel = ATKTalentLevel + 1;
                CostPoints(ATKTalentCost);
                CalculateAndShowTalentCost();
            }else if(UserPoint < ATKTalentCost){
                //if point is not enough to upgrade, it will quit entire UpgradeTalentSub() method.
                return;
            }

            //2.1.2 upgrade CR talent.
            if(CriticalRateCheckBox.isChecked() && CRTalentLevel < 1000  && UserPoint >= CRTalentCost){
                CRTalentLevel = CRTalentLevel + 1;
                CostPoints(CRTalentCost);
                CalculateAndShowTalentCost();
            }else if(CriticalRateCheckBox.isChecked() && CRTalentLevel >= 1000){
                //2.1.2.1 if user`s CR or CD talent Level is max, remove this part of cost. And cancel this part of upgrade. but the circulation will keep running.
                CriticalRateCheckBox.setChecked(false);
            }else if(UserPoint < CRTalentCost){
                //if point is not enough to upgrade, it will quit entire UpgradeTalentSub() method.
                return;
            }

            //2.1.3 upgrade CD talent.
            if(CriticalDamageCheckBox.isChecked() && CDTalentLevel < 750 && UserPoint >= CDTalentCost){
                CDTalentLevel = CDTalentLevel + 1;
                CostPoints(CDTalentCost);
                CalculateAndShowTalentCost();
            }else if(CDTalentLevel >= 750){
                //2.1.3.1 if user`s CR or CD talent Level is max, remove this part of cost. And cancel this part of upgrade. but the circulation will keep running.
                CriticalDamageCheckBox.setChecked(false);
            }else if(UserPoint < CDTalentCost){
                //if point is not enough to upgrade, it will quit entire UpgradeTalentSub() method.
                return;//don`t remove it.
            }
    }//end of talent system.
}