package com.example.android.tomultiqa;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TourneyActivity extends AppCompatActivity {
    //name: https://cn.bing.com/dict/search?q=tourney&FORM=BDVSP6&mkt=zh-cn !

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourney);
        //thanks to: https://dev.mi.com/console/doc/detail?pId=2229 !
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        //initializing MaxEvaluation number.
        TextView MaxEvaluationView = findViewById(R.id.MaxEvaluationView);
        MaxEvaluationView.setText(SupportClass.ReturnKiloLongString(SupportClass.getLongData(this,"TourneyDataFile","MaxPtRecord",0) ) );
        //set default boss level and show to user.
        TextView BossLevelView = findViewById(R.id.TourneyLevelView);
        TextView BossHPView = findViewById(R.id.TourneyHPView);
        TextView BossTurnView = findViewById(R.id.TourneyTurnView);
        UserLevel = SupportClass.getIntData(this,"EXPInformationStoreProfile","UserLevel",1);
        BossLevel = SupportClass.getIntData(this,"TourneyDataFile","BossLevel",UserLevel);
        BossHP = SupportClass.getIntData(this,"TourneyDataFile","BossHP",1000);
        BossTurn = SupportClass.getIntData(this,"TourneyDataFile","BossTurn",10);
        BossLevelView.setText("Lv." + BossLevel);
        BossHPView.setText(BossHP + "");
        BossTurnView.setText(BossTurn + "");
        CalculateTotalPt();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SupportClass.saveIntData(this,"TourneyDataFile","BossLevel",BossLevel);
        SupportClass.saveIntData(this,"TourneyDataFile","BossHP",BossHP);
        SupportClass.saveIntData(this,"TourneyDataFile","BossTurn",BossTurn);
    }

    //Tourney system.
    int UserLevel;
    long PtValue = 0;
    int BossLevel = 1;
    int BossHP = 1000;
    int BossTurn = 10;
    double PtIndex = 0;
    double LevelIndex = 0;
    //record the Abilities which boss need to have.
    ArrayList<String> BossAbility = new ArrayList<>();
    ArrayList<String> EffectList = new ArrayList<>();

    //lv.3 method, main method of start battle.
    //return stack, thanks to: https://blog.csdn.net/cp25807720/article/details/30820693 !
    // https://developer.android.google.cn/guide/components/activities/tasks-and-back-stack?hl=zh-cn
    // text process: https://blog.csdn.net/qq_34262794/article/details/80065502 !
    @SuppressLint("SetTextI18n")
    public void StartTourney(View view){
        //prepare Effect text which need to show to user.Do not change replace() method parameters.
        String EffectShow = EffectList.toString().replace(",","").replace("["," ").replace("]","");
        //create dialog.
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.NoticeWordTran));
        dialog.setMessage(getString(R.string.TourneyStartHintTran) + "\n\n" + EffectShow);
        dialog.setCancelable(true);
        dialog.setPositiveButton(
               getString(R.string.ConfirmWordTran),
                (dialog1, id) -> {
                    //Set information needed.
                    if(BossHP > 0 && BossTurn > 0 && PtValue > 0){
                        //"Start"mode: put data in intent and start MainActivity.
                        Intent i = new Intent(this, MainActivity.class);
                        i.putExtra("Name",getString(R.string.PastNameTran));
                        i.putExtra("Level",BossLevel);
                        i.putExtra("HP",BossHP);
                        if(BossAbility.contains(getString(R.string.ShieldAbilityTran))){
                            i.putExtra("SD",(int)(BossHP * 0.35));
                        }else{
                            i.putExtra("SD",0);
                        }
                        i.putExtra("ModeNumber",1);
                        i.putExtra("Turn",BossTurn);
                        i.putExtra("EXPReward",0);
                        i.putExtra("PointReward",0);
                        i.putExtra("MaterialReward",0);
                        i.putExtra("BossAbility", BossAbility);
                        i.putExtra("BossPtValue",PtValue);
                        //if this state is 1, it means Boss have information transporting from SquareAbility.
                        i.putExtra("BossState",3);
                        i.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    }else{
                        SupportClass.CreateNoticeDialog(this,
                                getString(R.string.NoticeWordTran),
                                getString(R.string.StartTourneyHintTran),
                                getString(R.string.ConfirmWordTran)
                        );
                    }
                    dialog1.cancel();
                });
        dialog.setNegativeButton(
                getString(R.string.CancelWordTran),
                (dialog12, id) -> dialog12.cancel());
        //3. Use this object to create a actual View in android.
        AlertDialog DialogView = dialog.create();
        //4.after View has been created, use api to remove black cover behind dialog.
        //thanks to: https://blog.csdn.net/summer_ck/article/details/79614582 !
        Window window = DialogView.getWindow();
        window.setDimAmount(0.04f);
        //5.show to user.
        DialogView.show();
    }

    //lv.2 method, main method of design boss attributes.
    @SuppressLint("SetTextI18n")
    public void DesignBossInf(View view){
        //User UI.
        TextView BossLevelView = findViewById(R.id.TourneyLevelView);
        TextView BossHPView = findViewById(R.id.TourneyHPView);
        TextView BossTurnView = findViewById(R.id.TourneyTurnView);
        //dialog UI.
        LinearLayout BossInfLayout = new LinearLayout(this);
        EditText InputLevelView = new EditText(this);
        EditText InputHPView = new EditText(this);
        EditText InputTurnView = new EditText(this);
        BossInfLayout.setOrientation(LinearLayout.VERTICAL);
        BossInfLayout.addView(InputLevelView);
        BossInfLayout.addView(InputHPView);
        BossInfLayout.addView(InputTurnView);
        //set EditText only accept Number input, show user input hint and set default value.
        InputLevelView.setInputType(InputType.TYPE_CLASS_NUMBER);
        InputLevelView.setHint(getString(R.string.BossLevelWordTran));
        InputHPView.setInputType(InputType.TYPE_CLASS_NUMBER);
        InputHPView.setHint(getString(R.string.BossHPWordTran));
        InputTurnView.setInputType(InputType.TYPE_CLASS_NUMBER);
        InputTurnView.setHint(getString(R.string.BattleTurnWordTran));
        //show the input dialog.
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.EditWordTran))
                .setView(BossInfLayout)
                .setPositiveButton(getString(R.string.ConfirmWordTran), (dialogInterface, i) -> {
                    //1.read input.
                    int Level = SupportClass.getInputNumber(InputLevelView);
                    int HP = SupportClass.getInputNumber(InputHPView);
                    int Turn = SupportClass.getInputNumber(InputTurnView);
                    //2.data fixing branch.fix data which not correct, but right data will pass these code.
                    if(Level < 1 || Level < UserLevel - 33 || Level > UserLevel + 33){//if Input Level not in UserLevel+ 33, the damage will be 0 constantly, if it in UserLevel - 33, the Pt will below 0.
                        Level = SupportClass.getIntData(this,"TourneyDataFile","BossLevel",UserLevel);//reset value to default(User) level.
                    }else if(InputLevelView.getText().length() == 0){
                        Level = BossLevel;//reset value to previous value.
                    }
                    if(HP < 1){
                        HP = SupportClass.getIntData(this,"TourneyDataFile","BossHP",1000);
                    }else if(InputHPView.getText().length() == 0){
                        HP = BossHP;//reset value to previous value.
                    }
                    if(Turn < 1){
                        Turn = SupportClass.getIntData(this,"TourneyDataFile","BossTurn",10);
                    }else if(Turn > 1000){
                        Turn = 1000;
                    }else if(InputTurnView.getText().length() == 0){
                        Turn = BossTurn;//reset value to previous value.
                    }
                    //3.pass data to global variables.completed process.
                    SupportClass.CreateNoticeDialog(this,
                            getString(R.string.ReportWordTran),
                            getString(R.string.SavingCompletedTran) + "\n" +
                                    getString(R.string.BossLevelWordTran) + "\n" +
                                    BossLevel + " → " + Level + "\n" +
                                    getString(R.string.BossHPWordTran) + "\n" +
                                    BossHP + " → " + HP + "\n" +
                                    getString(R.string.BattleTurnWordTran) + "\n" +
                                    BossTurn + " → " + Turn,
                            getString(R.string.ConfirmWordTran));
                    BossLevel = Level;
                    BossHP = HP;
                    BossTurn = Turn;
                    CalculateTotalPt();
                    //4.show user the edit result.
                    BossHPView.setText(BossHP + "");
                    BossLevelView.setText("Lv." + BossLevel);
                    BossTurnView.setText(BossTurn + "");
                    //end of after input confirm.
                }).setNegativeButton(getString(R.string.CancelWordTran),null).show();
    }

    //lv.2 method, main method, change Ability data when any CheckBox is clicked.
    public void ChangeChoice(View view){
        //0.preparation.
        CheckBox AnyCheckBox = findViewById(view.getId());
        ArrayList<String> AllEffectList = new AbilityList(this).EffectList;
        String CheckBoxName = AnyCheckBox.getText().toString();

        boolean IsChecked = AnyCheckBox.isChecked();
        //1.CheckBox Data Collection.
        //PtValue is percent(%) number.
        ChangeAbilityList(CheckBoxName,IsChecked,getString(R.string.TrialAbilityTran),AllEffectList.get(0),30);
        ChangeAbilityList(CheckBoxName,IsChecked,getString(R.string.RushAbilityTran),AllEffectList.get(1),17);
        ChangeAbilityList(CheckBoxName,IsChecked,getString(R.string.FragileAbilityTran),AllEffectList.get(2),-70);
        ChangeAbilityList(CheckBoxName,IsChecked,getString(R.string.WeakSpotAbilityTran),AllEffectList.get(3),-25);
        ChangeAbilityList(CheckBoxName,IsChecked,getString(R.string.SlowAbilityTran),AllEffectList.get(4),-17);
        ChangeAbilityList(CheckBoxName,IsChecked,getString(R.string.CorrosionAbilityTran),AllEffectList.get(5),25);
        ChangeAbilityList(CheckBoxName,IsChecked,getString(R.string.CurseAbilityTran),AllEffectList.get(6),60);
        ChangeAbilityList(CheckBoxName,IsChecked,getString(R.string.DiversionAbilityTran),AllEffectList.get(7),30);
        ChangeAbilityList(CheckBoxName,IsChecked,getString(R.string.RecoverAbilityTran),AllEffectList.get(9),85);
        ChangeAbilityList(CheckBoxName,IsChecked,getString(R.string.LastHurtAbilityTran),AllEffectList.get(10),-105);
        ChangeAbilityList(CheckBoxName,IsChecked,getString(R.string.ShieldAbilityTran),AllEffectList.get(11),35);
        ChangeAbilityList(CheckBoxName,IsChecked,getString(R.string.FastStepAbilityTran),AllEffectList.get(14),100);
        ChangeAbilityList(CheckBoxName,IsChecked,getString(R.string.ProudAbilityTran),AllEffectList.get(15),36);
        ChangeAbilityList(CheckBoxName,IsChecked,getString(R.string.FearAbilityTran),AllEffectList.get(16),8);
        ChangeAbilityList(CheckBoxName,IsChecked,getString(R.string.ReTimeAbilityTran),AllEffectList.get(17),-33);
        ChangeAbilityList(CheckBoxName,IsChecked,getString(R.string.ActiveAbilityTran),AllEffectList.get(18),9);

        //2.after the Boss Ability Chosen, calculate the PtValue.
        CalculateTotalPt();
    }

    //lv.1 method, sub method of ChangeChoice() method.
    private void ChangeAbilityList(String CheckBoxName, boolean IsChecked, String AbilityName, String AbilityEffect, double PtValue){
        if(CheckBoxName.equals(AbilityName) && IsChecked){
            BossAbility.add(AbilityName);
            EffectList.add(AbilityName + "\n" + AbilityEffect + "\n\n");
            PtIndex = PtIndex + PtValue / 10;
        }else if(CheckBoxName.equals(AbilityName)){
            //!IsChecked is not necessary.
            BossAbility.remove(AbilityName);
            EffectList.remove(AbilityName + "\n" + AbilityEffect + "\n\n");
            PtIndex = PtIndex - PtValue / 10;
        }
    }

    //lv.1 method, calculate TotalPtValue, sub method.
    @SuppressLint("SetTextI18n")
    private void CalculateTotalPt(){
        TextView BossAbilityView = findViewById(R.id.BossAbilityView);
        TextView EvaluationValueView = findViewById(R.id.EvaluationValueView);
        //old formula: BasicPtValue = 100 +（bossHP比例数值）-【（boss回合数- 10）*（bossHP比例 / boss回合数）】+【（bossHP比例 / boss回合数）*（能力评价加成）】
        if(PtIndex == 0){
            PtIndex = 1;
        }
        LevelIndex = (BossLevel - UserLevel) * 0.03 + 1;
        PtValue = (long) ( BossHP / BossTurn * PtIndex * LevelIndex);
        BossAbilityView.setText(BossAbility.toString());
        EvaluationValueView.setText(SupportClass.ReturnKiloLongString(PtValue) + "");
    }//end of Tourney system.
}

