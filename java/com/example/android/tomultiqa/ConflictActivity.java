package com.example.android.tomultiqa;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class ConflictActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conflict);
        //thanks to: https://dev.mi.com/console/doc/detail?pId=2229 !
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        InitializingConflictData();
    }

    //add a button menu to ActionBar in this Activity.
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
            AbilityList AllAbility = new AbilityList(this);
            SupportClass.CreateNoticeDialog(this,
                    getString(R.string.BossAbilityWordTran) + getString(R.string.HandBookWordTran),
                    AllAbility.PrintFullAbilityString(),
                    getString(R.string.ConfirmWordTran));
        }
        return super.onOptionsItemSelected(item);
    }


    //Conflict Function system.
    //Conflict max floor number.
    static final int CONFLICT_MAX_FLOOR = 30;// TODO: make sure define here when you add new floors to Conflict.
    //User cleared floor number.
    int UserUnlockedFloor = 0;
    //the floor show on UI.
    int CurrentShowFloor = 1;
    //store boss ability`s array list.
    String BossAbilityText = "Nothing.";
    //store the BossAbility data in ArrayList form.
    ArrayList<String> BossAbility = new ArrayList<>();
    ArrayList<String> BossAbilitySecond = new ArrayList<>();
    ArrayList<String> BossAbilityThird = new ArrayList<>();

    int BossPointReward = 0;
    int BossEXPReward = 0;
    int BossMaterialReward = 0;


    //thanks to:
    //https://www.runoob.com/java/java-arraylist.html !
    //https://stackoverflow.com/questions/43018802/arraylist-explicit-type-argument-string-can-be-replaced-with !

    @SuppressLint("SetTextI18n")
    private void InitializingConflictData(){
        TextView CurrentFloorShowView = findViewById(R.id.CurrentFloorShowView);
        UserUnlockedFloor = SupportClass.getIntData(this,"BattleDataProfile","UserConflictFloor",0);
        CurrentShowFloor = Math.max(UserUnlockedFloor, 1);
//       equals to: if(UserConflictFloor > 1){
//                    CurrentShowFloor = UserConflictFloor;
//                  }else{
//                    CurrentShowFloor = 1;
//                  }
        CurrentFloorShowView.setText(CurrentShowFloor + "");
        ShowConflictBossInformation();
    }

    public void ShowBossAbilityDetail(View view){
        SupportClass.CreateNoticeDialog(this,
                getString(R.string.AbilityDetailTran),
                BossAbilityText,
                getString(R.string.DialogConfirmButtonTran));
    }

    public void ShowConflictHelpText(View view){
        SupportClass.CreateNoticeDialog(this,
                getString(R.string.HelpWordTran),
                getString(R.string.ConflictHelpTextTran),
                getString(R.string.ConfirmWordTran)
                );
    }

    public void ShowConflictReward(View view){
        SupportClass.CreateNoticeDialog(this,
                getString(R.string.RewardWordTran),
                "EXP" + "\n" +
                        BossEXPReward + "\n" +
                        getString(R.string.PointWordTran) + "\n" +
                        BossPointReward + "\n" +
                        getString(R.string.MaterialWordTran) + "\n" +
                        BossMaterialReward,
                getString(R.string.ConfirmWordTran)
                );
    }

    //main method, provide function that let user jump certain floor.
    @SuppressLint("SetTextI18n")
    public void JumpFloor (View view){
    final EditText FloorJumpNumberView = new EditText(this);
    //set this EditText only accept Number input.
        FloorJumpNumberView.setInputType(InputType.TYPE_CLASS_NUMBER);
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.JumpFloorTitleTran))
                .setMessage(getString(R.string.JumpFloorMessageTran) + UserUnlockedFloor)
                .setView(FloorJumpNumberView)
                .setPositiveButton(getString(R.string.ConfirmWordTran), (dialogInterface, i) -> {
                    String InputText = FloorJumpNumberView.getText().toString();
                    //1. get the number in String form, and transform it to int form.
                    //thanks to: https://stackoverflow.com/questions/5585779/how-do-i-convert-a-string-to-an-int-in-java?rq=1 !
                    int JumpFloor = Integer.parseInt(FloorJumpNumberView.getText().toString());//store the exact Number in Code in int form.
                    try {
                        //2. get the first number in String, and transform it to int form.
                        if(JumpFloor <= UserUnlockedFloor && JumpFloor > 0 && JumpFloor <= CONFLICT_MAX_FLOOR && !InputText.isEmpty()){
                            TextView CurrentFloorShowView = findViewById(R.id.CurrentFloorShowView);
                            CurrentShowFloor = JumpFloor;
                            CurrentFloorShowView.setText(CurrentShowFloor + "");
                            ShowConflictBossInformation();
                        }else if(JumpFloor > UserUnlockedFloor && JumpFloor <= CONFLICT_MAX_FLOOR){
                            SupportClass.CreateNoticeDialog(ConflictActivity.this,
                                    getString(R.string.NoticeWordTran),
                                    getString(R.string.GoToUnlockJumpTextTran),
                                    getString(R.string.ConfirmWordTran)
                            );
                        }else if(JumpFloor > CONFLICT_MAX_FLOOR){
                            SupportClass.CreateNoticeDialog(ConflictActivity.this,
                                    getString(R.string.NoticeWordTran),
                                    getString(R.string.NoHigherFloorHintTran),
                                    getString(R.string.ConfirmWordTran)
                                    );
                            CurrentShowFloor = UserUnlockedFloor;
                        }
                    }
                    catch (NumberFormatException e)
                    {
                        CurrentShowFloor = 1;
                        //3.try to catch the error, if there are no number in Resource Code String.
                        //this method will give this variable a default value, to prevent it from crash.
                    }
                    //end of after input confirm.
                }).setNegativeButton(getString(R.string.CancelWordTran),null).show();
    }

    //lv.5 method, show user the current floor information according to floor number.
    @SuppressLint("SetTextI18n")
    public void ShowUpperFloor(View view){
        TextView CurrentFloorShowView = findViewById(R.id.CurrentFloorShowView);
        if(CurrentShowFloor < CONFLICT_MAX_FLOOR && CurrentShowFloor < UserUnlockedFloor + 1){
            CurrentShowFloor = CurrentShowFloor + 1;
        }else if(CurrentShowFloor >= CONFLICT_MAX_FLOOR){
            SupportClass.CreateNoticeDialog(this,
                    getString(R.string.NoticeWordTran),
                    getString(R.string.NoHigherFloorOpenHintTran),
                    getString(R.string.ConfirmWordTran)
            );
        }else if(CurrentShowFloor >= UserUnlockedFloor + 1){
            SupportClass.CreateNoticeDialog(this,
                    getString(R.string.NoticeWordTran),
                    getString(R.string.NoWinConflictRecordHintTran),
                    getString(R.string.ConfirmWordTran)
            );
        }
        CurrentFloorShowView.setText(CurrentShowFloor + "");
        ShowConflictBossInformation();
    }

    //lv.5 method, show user the current floor information according to floor number.
    @SuppressLint("SetTextI18n")
    public void ShowLowerFloor(View view){
        TextView CurrentFloorShowView = findViewById(R.id.CurrentFloorShowView);
        if(CurrentShowFloor > 1){
            CurrentShowFloor = CurrentShowFloor - 1;
            CurrentFloorShowView.setText(CurrentShowFloor + "");
            ShowConflictBossInformation();
        }else{
            SupportClass.CreateNoticeDialog(this,
                    getString(R.string.NoticeWordTran),
                    getString(R.string.ReachedLowestFloorTran),
                    getString(R.string.ConfirmWordTran)
            );
        }
    }

    //lv.4 method, main method of Start button in Conflict UI.
    public void StartConflict(View view){
        //using method in "Start" Mode to load boss data.
        BossDataSet("Start");
        SupportClass.saveIntData(this,"BattleDataProfile","CurrentShowFloor",CurrentShowFloor);
    }

    //lv.4 method, method which belongs to ShowHigherFloor/ShowLowerFloor/InitializingConflictData method.
    public void ShowConflictBossInformation(){
        //before every floor`s boss shown to user, clear the record of boss from another floor.
        BossAbility = new ArrayList<>();
        BossAbilitySecond = new ArrayList<>();
        BossAbilityThird = new ArrayList<>();
        //using method in "Show" Mode to load boss data.
        BossDataSet("Show");
        //it only defines Boss`s basic data, their ability should using SetBossAbilityValue method to define in the SetConflictBoss method.
    }

    //lv.3 method, this is collection which storing Boss data(except Ability Data, they will be set in SetConflictBoss method`s beginning), it will be used in showing information or Start the battle.
    //It collects every floors` information. and put them here to easily modify.
    private void BossDataSet(String ImportMethodMode){
        SetConflictBoss(1,"挫折" ,10,10,1350,0,1,10,5,350,0,ImportMethodMode);
        SetConflictBoss(2,"动心",12,12,1600,0,1,10,10,420,0,ImportMethodMode);
        SetConflictBoss(3,"惊吓",14,14,1900,0,1,10,16,500,0,ImportMethodMode);
        SetConflictBoss(4,"敌视",18,18,2370,0,1,10,22,720,0,ImportMethodMode);
        SetConflictBoss(5,"忍让",21,21,2760,0,1,10,28,1000,0,ImportMethodMode);
        SetConflictBoss(6,"反感",25,25,3340,0,1,10,35,1310,1,ImportMethodMode);
        SetConflictBoss(7,"不快",28,28,3760,0,1,10,42,1650,1,ImportMethodMode);
        SetConflictBoss(8,"傲气",31,31,4120,0,1,10,50,2000,1,ImportMethodMode);
        SetConflictBoss(9,"愤怒",35,35,4700,0,1,10,58,2730,1,ImportMethodMode);
        SetConflictBoss(10,"恨意",40,40,5350,0,1,9,65,3500,1,ImportMethodMode);
        SetConflictBoss(11,"亏欠",42,42,5130,0,1,9,72,4000,1,ImportMethodMode);
        SetConflictBoss(12,"后悔",44,44,5540,0,1,9,80,5400,1,ImportMethodMode);
        SetConflictBoss(13,"可惜",46,46,5800,0,1,9,85,6100,1,ImportMethodMode);
        SetConflictBoss(14,"为难",48,48,6050,0,1,9,90,7000,1,ImportMethodMode);
        SetConflictBoss(15,"心痛",50,50,6890,0,1,9,100,7850,1,ImportMethodMode);
        SetConflictBoss(16,"震惊",52,52,7330,0,1,9,110,8600,1,ImportMethodMode);
        SetConflictBoss(17,"失落",54,54,8000,0,1,9,120,9600,1,ImportMethodMode);
        SetConflictBoss(18,"孤单",56,56,8360,0,1,9,130,10130,1,ImportMethodMode);
        SetConflictBoss(19,"苦恼",58,58,8970,0,1,9,140,11000,1,ImportMethodMode);
        SetConflictBoss(20,"失落",60,60,9500,0,1,8,150,12500,2,ImportMethodMode);
        SetConflictBoss(21,"淡漠",62,62,9900,0,1,8,165,13750,2,ImportMethodMode);
        SetConflictBoss(22,"冷漠",64,64,10800,0,1,8,180,15200,2,ImportMethodMode);
        SetConflictBoss(23,"生气",66,66,11570,0,1,8,195,16350,2,ImportMethodMode);
        SetConflictBoss(24,"气恼",68,68,12200,0,1,8,210,18300,2,ImportMethodMode);
        SetConflictBoss(25,"惶惶",70,70,13900,0,1,8,225,20000,2,ImportMethodMode);
        SetConflictBoss(26,"抱恨",72,72,14200,0,1,8,240,21340,2,ImportMethodMode);
        SetConflictBoss(27,"惶惶",74,74,15500,0,1,8,255,22500,2,ImportMethodMode);
        SetConflictBoss(28,"嫌弃",76,76,16000,0,1,8,270,24000,2,ImportMethodMode);
        SetConflictBoss(29,"糟心",78,78,16600,0,1,8,285,25300,2,ImportMethodMode);
        SetConflictBoss(30,"阴郁",80,80,14800,0,1,7,300,27000,3,ImportMethodMode);
    }

    //lv.2 method, which belongs to BossDataSet method. this method define each floor`s battle data. and combine them as a collection.
    //so if we want to add more floor to Conflict Function, we can do it by insert new numbers , how easily!
    //return stack, thanks to: https://blog.csdn.net/cp25807720/article/details/30820693 !
    // https://developer.android.google.cn/guide/components/activities/tasks-and-back-stack?hl=zh-cn
    @SuppressLint("SetTextI18n")
    private void SetConflictBoss(int floor, String Name, int Level, int RecommendLevel, int HP, int SD, int ModeNumber, int Turn, int EXPReward, int PointReward, int MaterialReward, String MethodMode){
        //1.Set boss` Ability before show information to user.No matter the MethodMode is what.
        SetBossAbilityValue(5, new String[]{getString(R.string.ProudAbilityTran)});
        SetBossAbilityValue(10, new String[]{getString(R.string.FragileAbilityTran)});
        SetBossAbilityValue(15, new String[]{getString(R.string.WeakSpotAbilityTran)});
        SetBossAbilityValue(20, new String[]{getString(R.string.TreasureAbilityTran),getString(R.string.CorrosionAbilityTran)});
        SetBossAbilityValue(25, new String[]{getString(R.string.FearAbilityTran)});
        SetBossAbilityValue(30,new String[]{getString(R.string.DiversionAbilityTran),getString(R.string.TrialAbilityTran),getString(R.string.FragileAbilityTran)});

        if(CurrentShowFloor == floor){
            //keep these data to be used in dialog.
            BossEXPReward = EXPReward;
            BossPointReward = PointReward;
            BossMaterialReward = MaterialReward;

            //2.Set another information to be showed.
            if(MethodMode.equals("Start")){
                //"Start"mode: put data in intent and start MainActivity.
                Intent i = new Intent(this, MainActivity.class);
                i.putExtra("Name",Name);
                i.putExtra("Level",Level);
                i.putExtra("HP",HP);
                i.putExtra("SD",SD);
                i.putExtra("ModeNumber",ModeNumber);
                i.putExtra("Turn",Turn);
                i.putExtra("EXPReward",BossEXPReward);
                i.putExtra("PointReward",BossPointReward);
                i.putExtra("MaterialReward",BossMaterialReward);
                i.putExtra("BossAbility",BossAbility);
                //if this boss have multi mode, storing Abilities which used in another Mode.
                if(ModeNumber == 2){
                    i.putExtra("BossAbilitySecond",BossAbilitySecond);
                }
                if(ModeNumber == 3){
                    i.putExtra("BossAbilityThird",BossAbilityThird);
                }
                //if this state is 1, it means Boss have information transporting from SquareAbility.
                i.putExtra("BossState",1);
                i.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();

            }else if(MethodMode.equals("Show")){
                //"Show" mode: put data in UI, and show them to user.
                TextView BossNameView = findViewById(R.id.BossNameView);
                TextView RecommendLevelView = findViewById(R.id.RecommendLevelView);
                TextView BossLevelView = findViewById(R.id.BossLevelView);
                TextView BossHPView = findViewById(R.id.TourneyHPView);
                TextView BattleTurnView = findViewById(R.id.BattleTurnView);
                TextView BossAbilityShowView =findViewById(R.id.BossAbilityShowView);

                //keep these data to be used in dialog.
                BossEXPReward = EXPReward;
                BossPointReward = PointReward;
                BossMaterialReward = MaterialReward;
                BossNameView.setText(Name);
                RecommendLevelView.setText(RecommendLevel + "");
                BossLevelView.setText(Level + "");
                BossHPView.setText(HP + "");
                BattleTurnView.setText(Turn + "");
                if(BossAbility.size() >= 5){
                    BossAbilityText = BossAbility.toString();
                    BossAbilityShowView.setText("Please checking Detail.");
                }else{
                    BossAbilityShowView.setText(BossAbility.toString() + "");
                }
            }
        }
    }

    //lv.1 method, sub method of SetConflictBoss, using in import boss Ability Data.
    //thanks to: https://stackoverflow.com/questions/43457079/adding-multiple-items-at-once-to-arraylist-in-java !
    //Transforming Ability data from Array to ArrayList form.
    private void SetBossAbilityValue(int floor,String[] AbilityArray){
        if(CurrentShowFloor == floor){
            BossAbility = new ArrayList<>(Arrays.asList(AbilityArray));
        }
    }

    //lv.1 method, sub method of SetConflictBoss, using in import multi-mode boss Ability data.
    //SetBossAbilityValue method is using create single mode boss` Abilities, SetBossSecondAbility method is using create multi mode boss` Abilities, please use them properly!
    //Transforming Ability data from Array to ArrayList form.
    private void SetBossMultiAbility(int floor, String[] AbilityOne, String[] AbilityTwo, String[] AbilityThird){
        if(CurrentShowFloor == floor){
            BossAbility = new ArrayList<>(Arrays.asList(AbilityOne));
            BossAbilitySecond = new ArrayList<>(Arrays.asList(AbilityTwo));
            BossAbilityThird =  new ArrayList<>(Arrays.asList(AbilityThird));
        }
    }//end of conflict system.
}