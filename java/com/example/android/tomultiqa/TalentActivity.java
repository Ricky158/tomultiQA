package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class TalentActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talent);
        //thanks to: https://dev.mi.com/console/doc/detail?pId=2229 !
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        InitializingResourceData();
        InitializingTalentData();

        final CheckBox TalentCostAllPointCheckBox = findViewById(R.id.TalentCostAllPointCheckBox);
        TalentCostAllPointCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(TalentCostAllPointCheckBox.isChecked()){
                SetUpgradeTime();
            }
        });
        //Initialize Text.
        TalentCostAllPointCheckBox.setText(getString(R.string.CostAllPointsWordTran) + ": " + UpgradeTime);
    }

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

    static final int MaxCRLevel = 1000;
    static final int MaxCDLevel = 1000;

    @SuppressLint("SetTextI18n")
    public void UpgradeTalent(View view){
        //1.judge if User have enough Point to Upgrade all selected Talent(s).
        CheckBox TalentCostAllPointCheckBox = findViewById(R.id.TalentCostAllPointCheckBox);
        /*
        ATK Talent.
        CR Talent.
        CD Talent.
         */
        CheckBox[] UpgradeGroup = new CheckBox[]{
                findViewById(R.id.AttackTalentCheckBox),
                findViewById(R.id.CriticalRateCheckBox),
                findViewById(R.id.CriticalDamageCheckBox)
        };
        int InitialValue = UpgradeTime;
        if(TalentCostAllPointCheckBox.isChecked()){
            //2.1 upgrade in cost all mode.
            while (resourceIO.UserPoint > 0 && UpgradeTime > 0){
                UpgradeTime = UpgradeTime - 1;
                UpgradeTalentSub(UpgradeGroup);
            }
        }else if(!TalentCostAllPointCheckBox.isChecked() && resourceIO.UserPoint > 0){
            //2.2 upgrade in only 1 level mode.
            UpgradeTalentSub(UpgradeGroup);
        }else {
            //2.3 if user don`t have enough Points,tell user.
            SupportLib.CreateNoticeDialog(this,
                    getString(R.string.NoticeWordTran),
                    getString(R.string.NotEnoughPointTran),
                    getString(R.string.ConfirmWordTran));
        }

        //2.4 because of Upgrade Time is cost in loop, so back it to initial value here.
        UpgradeTime = InitialValue;
        //3. after the upgrade calculation, save data and show detail to user.
        SaveTalentData();
        ShowTalentEffectAndLevel();
        //3.1 refresh UserPoint and UI.
        TextView PointCountingView = findViewById(R.id.PointCountInMarket);
        PointCountingView.setText(SupportLib.ReturnKiloIntString(resourceIO.UserPoint));
        //4. after the upgrade, higher level will have higher demand for next level.
        resourceIO.ApplyChanges(this);
        //4.1 update UpgradeCost UI.
        TextView AttackTalentCostView = findViewById(R.id.AttackTalentCostView);
        TextView CRTalentCostView = findViewById(R.id.CRTalentCostView);
        TextView CDTalentCostView = findViewById(R.id.CDTalentCostView);
        AttackTalentCostView.setText(ATKTalentCost + "");
        CRTalentCostView.setText(CRTalentCost + "");
        CDTalentCostView.setText(CDTalentCost + "");
    }

    /**
     * sub method of UpgradeTalent method.
     * @param CheckBoxes share CheckBox object with main method, to decrease the cost of object creating in loop.
     */
    private void UpgradeTalentSub(CheckBox[] CheckBoxes){
        //1.check user`s Point number is enough or not. and upgrade the Talent.
            if(CheckBoxes[0].isChecked() && resourceIO.UserPoint >= ATKTalentCost){
                //2.1.1 upgrade ATK talent.
                ATKTalentLevel = ATKTalentLevel + 1;
                resourceIO.CostPoint(ATKTalentCost);
            }

            //2.1.2 upgrade CR talent.
            if(CheckBoxes[1].isChecked() && CRTalentLevel < MaxCRLevel  && resourceIO.UserPoint >= CRTalentCost){
                CRTalentLevel = CRTalentLevel + 1;
                resourceIO.CostPoint(CRTalentCost);
            }else if(CheckBoxes[1].isChecked() && CRTalentLevel >= MaxCRLevel){
                //2.1.2.1 if user`s CR or CD talent Level is max, remove this part of cost. And cancel this part of upgrade. but the circulation will keep running.
                CheckBoxes[1].setChecked(false);
            }

            //2.1.3 upgrade CD talent.
            if(CheckBoxes[2].isChecked() && CDTalentLevel < MaxCDLevel && resourceIO.UserPoint >= CDTalentCost){
                CDTalentLevel = CDTalentLevel + 1;
                resourceIO.CostPoint(CDTalentCost);
            }else if(CDTalentLevel >= MaxCDLevel){
                //2.1.3.1 if user`s CR or CD talent Level is max, remove this part of cost. And cancel this part of upgrade. but the circulation will keep running.
                CheckBoxes[2].setChecked(false);
            }

        //2.1.4 reset the upgrade cost for next level.
        CalculateTalentCost();
    }

    @SuppressLint("SetTextI18n")
    private void SetUpgradeTime(){
        //0.preparation.
        LinearLayout SelectorLayout = new LinearLayout(this);
        TextView NumberCounter = new TextView(this);
        SeekBar NumberSelector = new SeekBar(this);
        SelectorLayout.setOrientation(LinearLayout.VERTICAL);
        SelectorLayout.addView(NumberCounter);
        SelectorLayout.addView(NumberSelector);
        final int[] InputNumber = {UpgradeTime};//Default Value.
        final int MaxNumber = 100;
        NumberSelector.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                InputNumber[0] = NumberSelector.getProgress();
                NumberCounter.setText(InputNumber[0] + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                InputNumber[0] = NumberSelector.getProgress();
                NumberCounter.setText(InputNumber[0] + "");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                InputNumber[0] = NumberSelector.getProgress();
                NumberCounter.setText(InputNumber[0] + "");
            }
        });
        //1.set SeekBar Max value.
        NumberCounter.setText(InputNumber[0] + "");
        NumberCounter.setPadding(40,4,0,4);
        NumberCounter.setTextSize(16);//16sp
        NumberSelector.setProgress(InputNumber[0]);
        NumberSelector.setPadding(8,0,8,0);
        NumberSelector.setMax(MaxNumber);
        //2.show dialog to user.
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.SelectNumberWordTran));
        //2.set basic values of dialog, including content text,button text,and title.
        dialog.setView(SelectorLayout);
        dialog.setCancelable(true);
        dialog.setPositiveButton(
                getString(R.string.ConfirmWordTran),
                (dialog12, id) -> {
                    //refresh UI.
                    if(InputNumber[0] > 0){
                        UpgradeTime = InputNumber[0];
                    }else{
                        UpgradeTime = 1;
                    }
                    CheckBox TalentCostAllPointCheckBox = findViewById(R.id.TalentCostAllPointCheckBox);
                    TalentCostAllPointCheckBox.setText(getString(R.string.CostAllPointsWordTran) + ": " + UpgradeTime);
                    dialog12.cancel();
                });
        dialog.setNegativeButton(
                getString(R.string.CancelWordTran),
                (dialog1, id) -> dialog1.cancel());
        //3. Use this object to create a actual View in android.
        AlertDialog DialogView = dialog.create();
        DialogView.show();
    }

    @SuppressLint("SetTextI18n")
    private void CalculateTalentCost(){
        ATKTalentCost = (int)(135 + Math.pow(ATKTalentLevel,1.15));
        CRTalentCost = (int)(165 + Math.pow(CRTalentLevel,1.37));
        CDTalentCost = (int)(200 + Math.pow(CDTalentLevel,1.29));
    }

    @SuppressLint("SetTextI18n")
    private void ShowTalentEffectAndLevel(){
        TextView AttackTalentEffectView = findViewById(R.id.AttackTalentEffectView);
        TextView CRTalentEffectView = findViewById(R.id.CRTalentEffectView);
        TextView CDTalentEffectView = findViewById(R.id.CDTalentEffectView);
        TextView AttackTalentLevelView = findViewById(R.id.AttackTalentLevelView);
        TextView CRTalentLevelView = findViewById(R.id.CRTalentLevelView);
        TextView CDTalentLevelView = findViewById(R.id.CDTalentLevelView);

        AttackTalentEffectView.setText("+" + ATKTalentLevel * ValueLib.ATK_TALENT_INDEX);
        CRTalentEffectView.setText("+" + SupportLib.ReturnTwoBitText(CRTalentLevel * ValueLib.CR_TALENT_INDEX) + "%");
        CDTalentEffectView.setText("+" + SupportLib.ReturnTwoBitText(CDTalentLevel * ValueLib.CD_TALENT_INDEX) + "%");
        AttackTalentLevelView.setText(ATKTalentLevel + "");
        CRTalentLevelView.setText(CRTalentLevel + "");
        CDTalentLevelView.setText(CDTalentLevel + "");
    }

    private void SaveTalentData(){
        SupportLib.saveIntData(this,"BattleDataProfile","ATKTalentLevel",ATKTalentLevel);
        SupportLib.saveIntData(this,"BattleDataProfile","CRTalentLevel",CRTalentLevel);
        SupportLib.saveIntData(this,"BattleDataProfile","CDTalentLevel",CDTalentLevel);
    }

    @SuppressLint("SetTextI18n")
    private void InitializingTalentData(){
        ATKTalentLevel = SupportLib.getIntData(this,"BattleDataProfile","ATKTalentLevel",0);
        CRTalentLevel = SupportLib.getIntData(this,"BattleDataProfile","CRTalentLevel",0);
        CDTalentLevel = SupportLib.getIntData(this,"BattleDataProfile","CDTalentLevel",0);
        //initialize UpgradeCost UI.
        CalculateTalentCost();
        TextView AttackTalentCostView = findViewById(R.id.AttackTalentCostView);
        TextView CRTalentCostView = findViewById(R.id.CRTalentCostView);
        TextView CDTalentCostView = findViewById(R.id.CDTalentCostView);
        AttackTalentCostView.setText(ATKTalentCost + "");
        CRTalentCostView.setText(CRTalentCost + "");
        CDTalentCostView.setText(CDTalentCost + "");
        ShowTalentEffectAndLevel();

        CheckBox CriticalRateCheckBox = findViewById(R.id.CriticalRateCheckBox);
        CheckBox CriticalDamageCheckBox = findViewById(R.id.CriticalDamageCheckBox);
        //if some talent`s level touch the max line, system will stop user choosing these talent(s) to upgrade.
        if(CRTalentLevel >= MaxCRLevel){
            CriticalRateCheckBox.setEnabled(false);
        }
        if(CDTalentLevel >= MaxCDLevel){
            CriticalDamageCheckBox.setEnabled(false);
        }
    }//end of talent system.


    //basic value of whole activity.
    ResourceIO resourceIO;

    //main method.
    @SuppressLint("SetTextI18n")
    private void InitializingResourceData(){
        TextView PointCountingView = findViewById(R.id.PointCountInMarket);
        TextView MaterialCountingView = findViewById(R.id.KeyCountInMarket);
        resourceIO = new ResourceIO(this);
        PointCountingView.setText(SupportLib.ReturnKiloIntString(resourceIO.UserPoint));
        MaterialCountingView.setText(SupportLib.ReturnKiloIntString(resourceIO.UserMaterial));
    }//end of basic value.
}