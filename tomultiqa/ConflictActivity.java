package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.security.interfaces.RSAKey;
import java.util.ArrayList;
import java.util.Arrays;

public class ConflictActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conflict);
        InitializingConflictData();
    }

    //Conflict Function system.
    //decide the max floor number of whole system.
    int MaxConflictFloor = 20;
    //User cleared floor number.
    int UserConflictFloor = 0;
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
        UserConflictFloor = SupportClass.getIntData(this,"BattleDataProfile","UserConflictFloor",0);
        if(SupportClass.getIntData(this,"BattleDataProfile","UserConflictFloor",0) > 1){
            CurrentShowFloor = UserConflictFloor;
        }else{
            CurrentShowFloor = 1;
        }
        CurrentFloorShowView.setText(CurrentShowFloor + "");
        ShowConflictBossInformation();
    }

    public void ShowBossAbilityDetail(View view){
        SupportClass.CreateOnlyTextDialog(this,
                "Ability Detail",
                BossAbilityText,getString(R.string.DialogConfirmButtonTran),
                "Nothing",
                true);
    }

    public void ShowConflictHelpText(View view){
        SupportClass.CreateOnlyTextDialog(this,
                "Help",
                "·This is a Tower, called [Conflict].\n" +
                        "·There are so many dangerous monsters in it, and whole tower is full-filled with negative emotions.\n" +
                        "·These Creations are making strong power to affect this world in a mystery form. \n" +
                        "·So, our Hero need to overcome these hardness and take the world`s order back.\n" +
                        "·But be careful, Adventurer, there are so many things we don`t know yet.\n"+
                        "·No one know what you will see, and what you will take.",
                "Know",
                "Nothing",
                true);
    }

    public void ShowConflictReward(View view){
        SupportClass.CreateOnlyTextDialog(this,
                getString(R.string.RewardWordTran),
                "EXP:" + BossEXPReward + "\nPoint:" + BossPointReward + "\nMaterial:" + BossMaterialReward,
                getString(R.string.ConfirmWordTran),
                "Nothing",
                true);
    }

    //main method, provide function that let user jump certain floor.
    public void JumpFloor (View view){
    final EditText FloorJumpNumberView = new EditText(this);
    //set this EditText only accept Number input.
        FloorJumpNumberView.setInputType(InputType.TYPE_CLASS_NUMBER);
        new AlertDialog.Builder(this)
                .setTitle("Input Floor You Want to Jump")
                .setView(FloorJumpNumberView)
                .setPositiveButton(getString(R.string.ConfirmWordTran), new DialogInterface.OnClickListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            String InputText = FloorJumpNumberView.getText().toString();
            //1. get the number in String form, and transform it to int form.
            //thanks to: https://stackoverflow.com/questions/5585779/how-do-i-convert-a-string-to-an-int-in-java?rq=1 !
            int JumpFloor = Integer.parseInt(FloorJumpNumberView.getText().toString());//store the exact Number in Code in int form.
            try {
                //2. get the first number in String, and transform it to int form.
                if(JumpFloor <= UserConflictFloor && JumpFloor > 0 && JumpFloor <= MaxConflictFloor && !InputText.isEmpty()){
                    TextView CurrentFloorShowView = findViewById(R.id.CurrentFloorShowView);
                    CurrentShowFloor = JumpFloor;
                    CurrentFloorShowView.setText(CurrentShowFloor + "");
                    ShowConflictBossInformation();
                }else if(JumpFloor > UserConflictFloor && JumpFloor <= MaxConflictFloor){
                    SupportClass.CreateOnlyTextDialog(ConflictActivity.this,
                            getString(R.string.NoticeWordTran),
                            "You need to clear more Floor to unlock higher available.",
                            getString(R.string.ConfirmWordTran),
                            "Nothing",
                            true);
                }else if(JumpFloor > MaxConflictFloor){
                    SupportClass.CreateOnlyTextDialog(ConflictActivity.this,
                            getString(R.string.NoticeWordTran),
                            "No higher Floor available to Jump.We can only send you to [Highest Point].",
                            getString(R.string.ConfirmWordTran),
                            "Nothing",
                            true);
                    CurrentShowFloor = UserConflictFloor;
                }
            }
            catch (NumberFormatException e)
            {
                CurrentShowFloor = 1;
                //3.try to catch the error, if there are no number in Resource Code String.
                //this method will give this variable a default value, to prevent it from crash.
            }
            //end of after input confirm.
        }
                }).setNegativeButton(getString(R.string.CancelWordTran),null).show();
    }

    //lv.5 method, show user the current floor information according to floor number.
    @SuppressLint("SetTextI18n")
    public void ShowUpperFloor(View view){
        TextView CurrentFloorShowView = findViewById(R.id.CurrentFloorShowView);
        if(CurrentShowFloor < MaxConflictFloor && CurrentShowFloor < UserConflictFloor + 1){
            CurrentShowFloor = CurrentShowFloor + 1;
        }else if(CurrentShowFloor >= MaxConflictFloor){
            SupportClass.CreateOnlyTextDialog(this,
                    getString(R.string.NoticeWordTran),
                    "No higher floor available .",
                    getString(R.string.ConfirmWordTran),
                    "Nothing",
                    true);
        }else if(CurrentShowFloor >= UserConflictFloor + 1){
            SupportClass.CreateOnlyTextDialog(this,
                    getString(R.string.NoticeWordTran),
                    "You have no winning record on next floor.",
                    getString(R.string.ConfirmWordTran),
                    "Nothing",
                    true);
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
            SupportClass.CreateOnlyTextDialog(this,
                    getString(R.string.NoticeWordTran),
                    "You have reached the Lowest Point of Conflict.",
                    getString(R.string.ConfirmWordTran),
                    "Nothing",
                    true);
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
    }

    //lv.2 method, which belongs to BossDataSet method. this method define each floor`s battle data. and combine them as a collection. so if we want to add more floor to Conflict Function, we can do it by insert new numbers , how easily!
    @SuppressLint("SetTextI18n")
    private void SetConflictBoss(int floor, String Name, int Level, int RecommendLevel, int HP, int SD, int ModeNumber, int Turn, int EXPReward, int PointReward, int MaterialReward, String MethodMode){
        //1.Set boss` Ability before show information to user.No matter the MethodMode is what.
        SetBossAbilityValue(5, new String[]{getString(R.string.ProudAbilityTran)});
        SetBossAbilityValue(10, new String[]{getString(R.string.FragileAbilityTran)});
        SetBossAbilityValue(15, new String[]{getString(R.string.WeakSpotAbilityTran)});
        SetBossAbilityValue(20, new String[]{getString(R.string.TreasureAbilityTran),getString(R.string.CorrosionAbilityTran)});

        //keep these data to be used in dialog.
        BossEXPReward = EXPReward;
        BossPointReward = PointReward;
        BossMaterialReward = MaterialReward;

        //2.Set another information to be showed.
        if(CurrentShowFloor == floor && MethodMode.equals("Start")){
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
            startActivity(i);
            finish();

        }else if(CurrentShowFloor == floor && MethodMode.equals("Show")){
            //"Show" mode: put data in UI, and show them to user.
            TextView BossNameView = findViewById(R.id.BossNameView);
            TextView RecommendLevelView = findViewById(R.id.RecommendLevelView);
            TextView BossLevelView = findViewById(R.id.BossLevelView);
            TextView BossHPView = findViewById(R.id.BossHPView);
            TextView BattleTurnView = findViewById(R.id.BattleTurnView);
            TextView BossAbilityShowView =findViewById(R.id.BossAbilityShowView);

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


    //basic support system.
    public void GoToMainPage(View view){
        Intent i = new Intent(this, MainActivity.class);
        //if this state is 1, it means Boss have information transporting from ConflictActivity.
        startActivity(i);
    }//end of basic support system.
}