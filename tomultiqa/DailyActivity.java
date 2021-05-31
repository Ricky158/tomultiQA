package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

@SuppressLint("SetTextI18n")
public class DailyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences RecordInfo = getSharedPreferences("RecordDataFile", MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);
        //make StatusBar is same color as ActionBar in Activity.
        //thanks to: https://blog.csdn.net/kiven9609/article/details/73162307 !
        // https://www.color-hex.com/color/26c6da ! #26C6DA is our App Primary Color.
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.rgb(38,198,218));
        //底部导航栏
        //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
        //main method below.
        InitializingDailyData();
        InitializingResourceData();
        InitializingEXPData();
        if(RecordInfo.getInt("MaxDailyDifficulty",0) >= 10){
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

    //Resource and EXP Port.
    int UserHaveEXP;
    int UserPoint;
    int UserMaterial;

    //read EXP data method.
    private void InitializingEXPData(){
        SharedPreferences EXPInfo = getSharedPreferences("EXPInformationStoreProfile", MODE_PRIVATE);
        UserHaveEXP = EXPInfo.getInt("UserHaveEXP", 0);
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
        if(IsAutoClearEnable){
            String RewardTypeShowToUser = "Error";
            switch (DailyType) {
                case "EXP":
                    UserHaveEXP = UserHaveEXP + 20 + DailyDifficulty * 30;
                    SupportClass.saveIntData(this,"EXPInformationStoreProfile", "UserHaveEXP", UserHaveEXP);
                    RewardTypeShowToUser = getString(R.string.EXPWordTran);
                    break;
                case "Point":
                    UserPoint = UserPoint + 1000 + DailyDifficulty * 1500;
                    SupportClass.saveIntData(this,"BattleDataProfile", "UserPoint", UserPoint);
                    RewardTypeShowToUser = getString(R.string.PointWordTran);
                    break;
                case "Material":
                    UserMaterial = UserMaterial + (5 + DailyDifficulty * 3);
                    SupportClass.saveIntData(this,"BattleDataProfile", "UserMaterial", UserMaterial);
                    RewardTypeShowToUser = getString(R.string.MaterialWordTran);
                    break;
            }
            SupportClass.CreateOnlyTextDialog(this,
                    "Report",
                    "AutoClear of Daily Challenge ["+RewardTypeShowToUser+"] is Completed.",
                    getString(R.string.ConfirmWordTran),
                    "Nothing",
                    true);
        }else{
            SupportClass.CreateOnlyTextDialog(this,
                    getString(R.string.HintWordTran),
                    "You need finish [Difficulty 10] to Open AutoClear function",
                    getString(R.string.ConfirmWordTran),
                    "Nothing",
                    true);
        }
    }

    @SuppressLint("SetTextI18n")
    private void InitializingDailyData(){
        TextView DailyCompleteStateView =findViewById(R.id.DailyCompleteStateView);
        TextView DailyRewardTypeShow = findViewById(R.id.DailyRewardTypeShow);
        //because of lack of Counting Time system, we just make Default value to 1, in future, it will be depended on this system.
        if(SupportClass.getIntData(this,"BattleDataProfile","DailyChance",1) > 0){
            DailyChance = 1;
            DailyCompleteStateView.setText("Waiting!");
        }else{
            DailyChance = 0;
            DailyCompleteStateView.setText("Complete!");
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
            DailyRewardValueShow.setText(1000 + DailyDifficulty * 1500 + "");
        }else{
            DailyRewardTypeShow.setText(getString(R.string.RewardWordTran) + getString(R.string.MaterialRewardWordTran));
            DailyRewardValueShow.setText(5 + DailyDifficulty * 3 + "");
        }
    }

    public void StartDaily(View view){
        if(DailyChance > 0){
            int UserLevel = SupportClass.getIntData(this,"EXPInformationStoreProfile","UserLevel", 1);
            int EXPReward = 20 + DailyDifficulty * 30;
            int PointReward = 1000 + DailyDifficulty * 1500;
            int MaterialReward = 5 + DailyDifficulty * 3;
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("DailyDifficulty",DailyDifficulty);
            i.putExtra("Name","Daily");
            i.putExtra("Level",UserLevel);
            i.putExtra("HP",(UserLevel * SupportClass.CreateRandomNumber(11 + DailyDifficulty *10,61 + DailyDifficulty *8)));
            i.putExtra("SD",0);
            i.putExtra("ModeNumber",1);
            i.putExtra("Turn",8);
            if(DailyType.equals("EXP")){
                i.putExtra("EXPReward",EXPReward);
            }else if(DailyType.equals("Points")){
                i.putExtra("PointReward",PointReward);
            }else{
                i.putExtra("MaterialReward",MaterialReward);
            }
            i.putExtra("BossAbility",new ArrayList<>());
            //if this state is 1, it means Boss have information transporting from SquareAbility.
            i.putExtra("BossState",2);
            startActivity(i);
            finish();
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
