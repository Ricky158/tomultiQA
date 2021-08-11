package com.example.android.tomultiqa;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

@SuppressLint("SetTextI18n")
public class DailyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);
        //thanks to: https://dev.mi.com/console/doc/detail?pId=2229 !
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        InitializingDailyData();
        InitializingResourceData();
        InitializingEXPData();
        if(SupportClass.getIntData(this,"RecordDataFile","MaxDailyDifficulty",0) >= 10){
            IsAutoClearEnable = true;
        }

        //provide visualize support for seekBar moving report.
        //Thanks to:https://stackoverflow.com/questions/41774963/android-seekbar-show-progress-value-along-the-seekbar
        final SeekBar ChooseDailyDifficultyBar = findViewById(R.id.ChooseDailyDifficultyBar);
        final TextView DailyDifficultyShowView = findViewById(R.id.DailyDifficultyShowView);
        ChooseDailyDifficultyBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                DailyDifficulty = ChooseDailyDifficultyBar.getProgress();
                DailyDifficultyShowView.setText(DailyDifficulty + "");
                ShowDailyReward();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                DailyDifficulty = ChooseDailyDifficultyBar.getProgress();
                DailyDifficultyShowView.setText(DailyDifficulty + "");
                ShowDailyReward();
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                DailyDifficulty = ChooseDailyDifficultyBar.getProgress();
                DailyDifficultyShowView.setText(DailyDifficulty + "");
                ShowDailyReward();
            }
        });
    }

    //User information Part.
    int UserLevel;
    int UserHaveEXP;
    int UserPoint;
    int UserMaterial;

    //read EXP data method.
    private void InitializingEXPData(){
        UserHaveEXP = SupportClass.getIntData(this,"EXPInformationStoreProfile","UserHaveEXP", 0);
        UserLevel = SupportClass.getIntData(this,"EXPInformationStoreProfile","UserLevel", 1);
    }

    //read Resource data method.
    private void InitializingResourceData(){
        UserPoint = SupportClass.getIntData(this,"BattleDataProfile","UserPoint",0);
        UserMaterial = SupportClass.getIntData(this,"BattleDataProfile","UserMaterial",0);
    }//end of Resource and EXP Port.


    //Daily system.
    //represents user`s Daily choice.
    String DailyType = "EXP";
    //store user`s chosen Difficulty.
    int DailyDifficulty = 0;
    //store if user have chance to start Daily.
    int DailyChance = 0;
    boolean IsAutoClearEnable = false;

    public void StartAutoClear(View view){
        if(IsAutoClearEnable && DailyChance > 0){
            String RewardTypeShowToUser = "Error";
            switch (DailyType) {
                case "EXP":
                    UserHaveEXP = UserHaveEXP + 20 + DailyDifficulty * 30;
                    SupportClass.saveIntData(this,"EXPInformationStoreProfile", "UserHaveEXP", UserHaveEXP);
                    RewardTypeShowToUser = getString(R.string.EXPWordTran);
                    break;
                case "Point":
                    UserPoint = UserPoint + 5000 + 90 * UserLevel * DailyDifficulty;
                    SupportClass.saveIntData(this,"BattleDataProfile", "UserPoint", UserPoint);
                    RewardTypeShowToUser = getString(R.string.PointWordTran);
                    break;
                case "Material":
                    UserMaterial = UserMaterial + (5 + DailyDifficulty * 3);
                    SupportClass.saveIntData(this,"BattleDataProfile", "UserMaterial", UserMaterial);
                    RewardTypeShowToUser = getString(R.string.MaterialWordTran);
                    break;
            }
            SupportClass.CreateNoticeDialog(this,
                    getString(R.string.ReportWordTran),
                    getString(R.string.AutoDailyReport1Tran)+RewardTypeShowToUser+getString(R.string.AutoDailyReport2Tran),
                    getString(R.string.ConfirmWordTran)
            );
        }else if(!IsAutoClearEnable){
            SupportClass.CreateNoticeDialog(this,
                    getString(R.string.HintWordTran),
                    getString(R.string.UnlockAutoDailyHintTran),
                    getString(R.string.ConfirmWordTran)
            );
        }else{
            SupportClass.CreateNoticeDialog(this,
                    getString(R.string.NoticeWordTran),
                    getString(R.string.HaveFinishedDailyTran),
                    getString(R.string.ConfirmWordTran));
        }
    }

    @SuppressLint("SetTextI18n")
    private void InitializingDailyData(){
        TextView DailyCompleteStateView =findViewById(R.id.DailyCompleteStateView);
        TextView DailyRewardTypeShow = findViewById(R.id.DailyRewardTypeShow);
        //because of lack of Counting Time system, we just make Default value to 1, in future, it will be depended on this system.
        if(SupportClass.getIntData(this,"BattleDataProfile","DailyChance",1) > 0){
            DailyChance = 1;
            DailyCompleteStateView.setText(getString(R.string.NotFinishDailyHintTran));
        }else{
            DailyChance = 0;
            DailyCompleteStateView.setText(getString(R.string.CompletedWordTran) + "!");
        }
        DailyRewardTypeShow.setText(getString(R.string.RewardWordTran) + "(EXP)");
    }

    public void SetDailyType(View view){
        RadioButton CheckEXPDailyButton = findViewById(R.id.CheckEXPDailyButton);
        RadioButton CheckPointDailyButton = findViewById(R.id.CheckPointDailyButton);
        RadioButton CheckMaterialDailyButton = findViewById(R.id.CheckMaterialDailyButton);
        RadioGroup DailyTypeChooseGroup = findViewById(R.id.DailyTypeChooseGroup);
        int TypeCheckedId;
        TypeCheckedId = DailyTypeChooseGroup.getCheckedRadioButtonId();
        if (TypeCheckedId == CheckEXPDailyButton.getId()){
            DailyType = "EXP";
        }else if(TypeCheckedId == CheckPointDailyButton.getId()) {
            DailyType = "Points";
        }else if(TypeCheckedId == CheckMaterialDailyButton.getId()){
            DailyType = "Material";
        }
        ShowDailyReward();
    }

    //show user the reward of Daily in current Difficulty.
    @SuppressLint("SetTextI18n")
    private void ShowDailyReward(){
        TextView DailyRewardTypeShow = findViewById(R.id.DailyRewardTypeShow);
        TextView DailyRewardValueShow = findViewById(R.id.DailyRewardValueShow);
        if(DailyType.equals("EXP")){
            DailyRewardTypeShow.setText(getString(R.string.RewardWordTran) + getString(R.string.EXPRewardWordTran));
            DailyRewardValueShow.setText(20 + DailyDifficulty * 30 + "");
        }else if(DailyType.equals("Points")){
            DailyRewardTypeShow.setText(getString(R.string.RewardWordTran) + getString(R.string.PointsRewardWordTran));
            DailyRewardValueShow.setText(5000 + 90 * UserLevel * DailyDifficulty + "");
        }else{
            DailyRewardTypeShow.setText(getString(R.string.RewardWordTran) + getString(R.string.MaterialRewardWordTran));
            DailyRewardValueShow.setText(5 + DailyDifficulty * 3 + "");
        }
    }

    //return stack, thanks to: https://blog.csdn.net/cp25807720/article/details/30820693 !
    // https://developer.android.google.cn/guide/components/activities/tasks-and-back-stack?hl=zh-cn
    public void StartDaily(View view){
        if(DailyChance > 0){
            //1.decide daily reward number.
            int EXPReward = 0;
            int PointReward = 0;
            int MaterialReward = 0;
            if(DailyType.equals("EXP")){
                EXPReward = 20 + DailyDifficulty * 30;
            }else if(DailyType.equals("Points")){
                PointReward = 5000 + 90 * UserLevel * DailyDifficulty;
            }else{
                MaterialReward = 5 + DailyDifficulty * 3;
            }
            //2.prepare boss information.
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("DailyDifficulty",DailyDifficulty);
            i.putExtra("Name","Daily");
            i.putExtra("Level",UserLevel);
            i.putExtra("HP",(UserLevel * SupportClass.CreateRandomNumber(11 + DailyDifficulty * 11,61 + DailyDifficulty * 9) ));
            i.putExtra("SD",0);
            i.putExtra("ModeNumber",1);
            i.putExtra("Turn",8);
            i.putExtra("EXPReward",EXPReward);
            i.putExtra("PointReward",PointReward);
            i.putExtra("MaterialReward",MaterialReward);
            i.putExtra("BossAbility",new ArrayList<>());
            //if this state is 1, it means Boss have information transporting from SquareAbility.
            i.putExtra("BossState",2);
            //3.cost DailyChance.
            DailyChance = DailyChance - 1;
            SupportClass.saveIntData(this,"BattleDataProfile","DailyChance",DailyChance);
            //4.start challenging.
            i.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }else{
            SupportClass.CreateNoticeDialog(this,
                    getString(R.string.NoticeWordTran),
                    getString(R.string.HaveFinishedDailyTran),
                    getString(R.string.ConfirmWordTran));
        }
    }//end of Daily system.


    //basic support system for whole activity, these methods will improve coding.
    //Square button`s function--jump to SquareActivity.
    public void GoToSquare(View view){
        Intent i = new Intent(DailyActivity.this, SquareActivity.class);
//        if(AppMode.equals("Game")){
//            i.putExtra("AppModeImport",100);
//        }
        startActivity(i);
    }
}
